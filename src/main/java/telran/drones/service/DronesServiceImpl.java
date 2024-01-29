package telran.drones.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.drones.dto.DroneDto;
import telran.drones.dto.DroneMedication;
import telran.drones.dto.State;
import telran.drones.model.Drone;
import telran.drones.model.EventLog;
import telran.drones.model.Medication;
import telran.drones.repository.DroneRepository;
import telran.drones.repository.EventLogRepository;
import telran.drones.repository.MedicationRepository;
import telran.exception.DroneNotFoundException;
import telran.exception.ExcessiveWeightException;
import telran.exception.IllegalDroneStateException;
import telran.exception.InsufficientBatteryCapacityException;
import telran.exception.MedicationNotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j

public class DronesServiceImpl implements DronesService {
	
	final DroneRepository droneRepo;
	final MedicationRepository medicationRepo;
	final EventLogRepository eventLogRepo;
	
	@Override
	public DroneDto registerDrone(DroneDto droneDto) {
		if(droneRepo.existsByNumber(droneDto.number())) {
			throw new IllegalDroneStateException();
		}
		Drone drone = Drone.of(droneDto);
		droneRepo.save(drone);
		log.debug("drone {} has been saved", droneDto);
		return droneDto;
	}

	@Override
	public DroneMedication loanDrone(DroneMedication droneMedication) {
	    Drone drone = droneRepo.findByNumber(droneMedication.droneNumber())
	            .orElseThrow(DroneNotFoundException::new);

	    Medication medication = medicationRepo.findByCode(droneMedication.medicationCode())
	            .orElseThrow(MedicationNotFoundException::new);
	    
	    checkDroneAvailability(drone, medication);
	    
	    createEventLog(drone, State.LOADING, drone.getBatteryCapacity());

	    log.debug("DroneMedication {} has been loaded", droneMedication);
	    return droneMedication;
	}

	private void checkDroneAvailability(Drone drone, Medication medication) {

			if (drone.getState() != State.IDLE) {
		        throw new IllegalDroneStateException();
		    }

		    if (drone.getBatteryCapacity() < 25) {
		        throw new InsufficientBatteryCapacityException();
		    }
		    if (drone.getModel().getWeight() < medication.getWeight()) {
				throw new ExcessiveWeightException();
			}
		}
	    

    private void createEventLog(Drone drone, State newState, int batteryCapacity) {
        EventLog eventLog = new EventLog(LocalDateTime.now(), drone.getNumber(), newState, batteryCapacity);
        eventLogRepo.save(eventLog);
    }
}


