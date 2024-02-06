package telran.drones.service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.drones.api.PropertiesNames;
import telran.drones.configuration.DronesConfiguration;
import telran.drones.dto.*;

import telran.drones.model.*;
import telran.drones.projections.DroneNumber;
import telran.drones.projections.MedicationCode;
import telran.drones.repository.DronesModelRepo;
import telran.drones.repository.DronesRepo;
import telran.drones.repository.EventLogRepo;
import telran.drones.repository.MedicationRepo;
import telran.exception.DroneAlreadyExistException;
import telran.exception.DroneNotFoundException;
import telran.exception.IllegalDroneStateException;
import telran.exception.IllegalMedicationWeightException;
import telran.exception.LowBatteryCapacityException;
import telran.exception.MedicationNotFoundException;
import telran.exception.ModelNotFoundException;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly=true)
public class DronesServiceImpl implements DronesService {
	final DronesRepo droneRepo;
	final MedicationRepo medicationRepo;
	final EventLogRepo logRepo;
	final DronesModelRepo droneModelRepo;
	final Map<State, State> statesMachine;
	@Value("${" + PropertiesNames.CAPACITY_THRESHOLD + ":25}")
	int capacityThreshold;
	@Autowired
	DronesConfiguration dronesConfiguration;

	@Override
	@Transactional(readOnly = false)
	public DroneDto registerDrone(DroneDto droneDto) {
		log.debug("service got drone DTO: {}", droneDto);
		if (droneRepo.existsById(droneDto.number())) {
			throw new DroneAlreadyExistException();
		}
		Drone drone = Drone.of(droneDto);
		
		DroneModel droneModel = droneModelRepo.findById(droneDto.modelType())
				.orElseThrow(() -> new ModelNotFoundException());
		drone.setModel(droneModel);
		log.debug("drone object is {}", drone);
		droneRepo.save(drone);
		return droneDto;
	}

	@Override
	@Transactional(readOnly = false)
	public DroneMedication loadDrone(DroneMedication droneMedication) {
		String droneNumber = droneMedication.droneNumber();
		String medicationCode = droneMedication.medicationCode();
		log.debug("received: droneNumber={}, medicationCode={}",droneNumber ,
				droneMedication.medicationCode());
		log.debug("capacity threshold is {}", capacityThreshold);
		Drone drone = droneRepo.findById(droneNumber).orElseThrow(() -> new DroneNotFoundException());
		log.debug("found drone: {}", drone);
		Medication medication = medicationRepo.findById(medicationCode)
				.orElseThrow(() -> new MedicationNotFoundException());
		log.debug("found medication: {}", medication);
		if (drone.getState() != State.IDLE) {
			throw new IllegalDroneStateException();
		}

		if (drone.getBatteryCapacity() < capacityThreshold) {
			throw new LowBatteryCapacityException();
		}
		if (drone.getModel().getWeight() < medication.getWeight()) {
			throw new IllegalMedicationWeightException();
		}
		drone.setState(State.LOADING);
		EventLog eventLog = new EventLog(LocalDateTime.now(), drone.getNumber(), drone.getState(),
				drone.getBatteryCapacity(), medicationCode);
		logRepo.save(eventLog);
		
		log.debug("saved log: {}", eventLog);

		return droneMedication;
	}

	@Override
	public List<String> checkMedicationItems(String droneNumber) {
		if(!droneRepo.existsById(droneNumber)) {
			throw new DroneNotFoundException();
		}
		List<MedicationCode> codes =
				logRepo.findByDroneNumberAndState(droneNumber, State.LOADING);
		List<String> res =  codes.stream().map(MedicationCode::getMedicationCode).toList();
		log.debug("Loaded medication items on drone {} are {} ", droneNumber, res);
		return res;
	}

	@Override
	public List<String> checkAvailableDrones() {
		List<DroneNumber> numbers = 
droneRepo.findByStateAndBatteryCapacityGreaterThanEqual(State.IDLE, capacityThreshold);
		List<String> res = numbers.stream().map(DroneNumber::getNumber).toList();
		log.debug("Available drones are {}", res);
		return res;
	}

	@Override
	public int checkBatteryCapacity(String droneNumber) {
		Integer batteryCapacity = droneRepo.findBatteryCapacity(droneNumber);
		if (batteryCapacity == null) {
			throw new DroneNotFoundException();
		}

		log.debug("battery capacity of drone {} is {}", droneNumber, batteryCapacity);
		return batteryCapacity;
	}

	@Override
	public List<DroneItemsAmount> checkDroneLoadedItemAmounts() {
		List<DroneItemsAmount> res = logRepo.getItemAmounts();
		res.forEach(dia -> log.trace("drone {}, items amount {}", dia.getNumber(), dia.getAmount()));
		return res;
	}

	@Transactional(readOnly = false)
	@Scheduled(fixedDelay = 3000)
	public void dronesControl() {
		List<Drone> dronesList = droneRepo.findAll();
		for (Drone drone : dronesList) {
			if (drone.getState() == State.IDLE) {
				dronesControlIdleState(drone);
			} else {
				dronesControlChangeState(drone);
			}
		}		
	}

	private void dronesControlIdleState(Drone drone) {
		int batteryCapacity = drone.getBatteryCapacity();
		if (batteryCapacity < 100) {
			drone.setBatteryCapacity(batteryCapacity + 2);
			droneRepo.save(drone);
			logRepo.save(new EventLog(LocalDateTime.now(), drone.getNumber(), State.IDLE, drone.getBatteryCapacity()));
			log.debug("for drone {} battery capacity increased to {}", drone, batteryCapacity + 2);
		}

	}

	private void dronesControlChangeState(Drone drone) {
		int batteryCapacity = drone.getBatteryCapacity();
		Map<State, State> statesMap = dronesConfiguration.getStatesMachine();
		drone.setState(statesMap.get(drone.getState()));
		drone.setBatteryCapacity(batteryCapacity - 2);
		droneRepo.save(drone);
		logRepo.save(new EventLog(LocalDateTime.now(), drone.getNumber(), drone.getState(), drone.getBatteryCapacity()));
		log.debug("for drone {} battery capacity decreased to {} and state changed to {}", drone, batteryCapacity - 2,
				drone.getState());
	}
}