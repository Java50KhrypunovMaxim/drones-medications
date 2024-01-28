package telran.drones.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.drones.dto.DroneDto;
import telran.drones.dto.DroneMedication;
import telran.drones.service.DronesService;

@RestController
@RequestMapping("drones")
@RequiredArgsConstructor
@Slf4j
public class DronesController {

	final DronesService droneService;
	@PostMapping
	
	DroneDto registerDrone(@RequestBody @Valid DroneDto droneDto) {
		log.debug("register drone: received drone data: {}", droneDto);
		return droneService.registerDrone(droneDto);
	}
	
	@PostMapping("load")
	DroneMedication loanDrone(@RequestBody @Valid DroneMedication droneMedication) {
		log.debug("loan drone: received data: {}", droneMedication);
		return droneService.loanDrone(droneMedication);
	}
}
