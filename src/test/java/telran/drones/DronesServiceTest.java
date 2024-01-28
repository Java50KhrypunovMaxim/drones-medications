package telran.drones;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.jdbc.Sql;

import jakarta.transaction.Transactional;
import telran.drones.dto.DroneDto;
import telran.drones.dto.ModelType;
import telran.drones.model.Drone;
import telran.drones.model.DroneModel;
import telran.drones.repository.DroneRepository;
import telran.drones.service.DronesService;
import telran.exception.IllegalDroneStateException;

@SpringBootTest

class DronesServiceTest {
	@Autowired
	DroneRepository dronesRepo;

	private static final String SERVICE_TEST = "Service Test: ";

	/******************************************************/
	//Drones numbers
	private static final String DRONE_NUMBER_1 = "111-111";
	private static final  String DRONE_NUMBER_2 = "555-555";
	/***********************************************************/
	//Medication code
	private static final String MEDICATION_CODE1 = "12-34-0";
	private static final String MEDICATION_CODE2 = "55-22-1";
	/***********************************************************/
	//Drone model
	private static final DroneModel DRONE_MODEL1 = new DroneModel(ModelType.Middleweight, 500);
	private static final DroneModel DRONE_MODEL2 = new DroneModel(ModelType.Heavyweight, 1500);
	/***********************************************************/
	
	/*******************************************************************************/
	//Drones
	DroneDto droneDto1 = new DroneDto(DRONE_NUMBER_1,DRONE_MODEL1.getModelName());
	DroneDto droneDto2 = new DroneDto(DRONE_NUMBER_2,DRONE_MODEL2.getModelName());
	
	/******'*********************************************************************************/
	//service bean
	@Autowired
	DronesService droneService;
	/***************************************************************************************/
	
	@Test
	@DisplayName(SERVICE_TEST + TestName.REGISTR_DRONE)
	void testRegisterDronePerson() {
		assertEquals(droneDto1, droneService.registerDrone(droneDto1));
		assertThrowsExactly(IllegalDroneStateException.class,
				()->droneService.registerDrone(droneDto1));
		Drone drone = dronesRepo.findByNumber(droneDto1.number()).orElse(null);
		assertEquals(droneDto1, drone.build());
	}
	
	


}
