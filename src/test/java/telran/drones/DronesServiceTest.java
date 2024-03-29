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
import telran.drones.dto.DroneMedication;
import telran.drones.dto.ModelType;
import telran.drones.dto.State;
import telran.drones.model.Drone;
import telran.drones.model.DroneModel;
import telran.drones.model.Medication;
import telran.drones.repository.DroneRepository;
import telran.drones.repository.MedicationRepository;
import telran.drones.service.DronesService;
import telran.exception.IllegalDroneStateException;

@SpringBootTest
@Sql(scripts = {"classpath:test_data.sql"})

class DronesServiceTest {
	@Autowired
	DroneRepository dronesRepo;
	
	@Autowired
	MedicationRepository medicationRepo;


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
	void testRegisterDrone() {
		assertEquals(droneDto1, droneService.registerDrone(droneDto1));
		assertThrowsExactly(IllegalDroneStateException.class,
				()->droneService.registerDrone(droneDto1));
		Drone drone = dronesRepo.findByNumber(droneDto1.number()).orElse(null);
		assertEquals(droneDto1, drone.build());
	}
	
	@Test
	@DisplayName(SERVICE_TEST + TestName.LOAN_DRONE)
	void testLoanDrone() {
        dronesRepo.save(Drone.of(droneDto1));
        dronesRepo.save(Drone.of(droneDto2));

        Medication medication1 = new Medication(MEDICATION_CODE1, 2000);
        Medication medication2 = new Medication(MEDICATION_CODE2, 1500);

        medicationRepo.save(medication1);
        medicationRepo.save(medication2);

        Drone drone = dronesRepo.findByNumber(DRONE_NUMBER_1).orElse(null);
        drone.setState(State.IDLE);
        drone.setBatteryCapacity(50);
        dronesRepo.save(drone);
       
        DroneMedication droneMedication = new DroneMedication(DRONE_NUMBER_1, MEDICATION_CODE1);
        assertEquals(droneMedication, droneService.loanDrone(droneMedication));
        
        Drone drone2 = dronesRepo.findByNumber(DRONE_NUMBER_2).orElse(null);
        drone2.setState(State.LOADED);
        drone2.setBatteryCapacity(100);
        dronesRepo.save(drone2);
        DroneMedication droneMedication2 = new DroneMedication(DRONE_NUMBER_2, MEDICATION_CODE2);
        assertThrowsExactly(IllegalDroneStateException.class,
				()->droneService.loanDrone(droneMedication2));
      
    }
}
