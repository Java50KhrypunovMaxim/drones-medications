package telran.drones.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.drones.dto.DroneDto;
import telran.drones.dto.DroneMedication;
import telran.drones.model.Drone;
import telran.drones.repository.DroneRepository;
import telran.exception.IllegalDroneStateException;

@Service
@RequiredArgsConstructor
@Slf4j

public class DronesServiceImpl implements DronesService {
	final DroneRepository droneRepo;
	
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
		// TODO Auto-generated method stub
		return null;
	}

}
