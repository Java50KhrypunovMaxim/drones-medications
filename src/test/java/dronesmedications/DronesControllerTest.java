package dronesmedications;

import static org.junit.jupiter.api.Assertions.*;
import org.springframework.http.MediaType;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import telran.drones.dto.DroneDto;
import telran.drones.dto.DroneMedication;
import telran.drones.dto.ModelType;
import telran.drones.model.DroneModel;
import telran.drones.service.DronesService;


@WebMvcTest
@ComponentScan(basePackages = "telran.drones.controller")

class DronesControllerTest {

	private static final String DRONE_NUMBER = "123-002";
	private static final String DRONE_NUMBER2 = "789-987";
	private static final String MEDICATION_CODE = "12-34-0";
	private static final DroneModel DRONE_MODEL = new DroneModel(ModelType.Middleweight, 500);
	private static final DroneMedication droneMedication = new DroneMedication(DRONE_NUMBER2,MEDICATION_CODE);
	@MockBean 
	DronesService dronesService;
	@Autowired 
	MockMvc mockMvc;
	DroneDto droneDto = new DroneDto(DRONE_NUMBER,DRONE_MODEL.getModelName());

	
	@Autowired 
	ObjectMapper mapper;
	
	@Test
	void testRegisterDrone() throws Exception {
		when(dronesService.registerDrone(droneDto)).thenReturn(droneDto);
		String jsonDroneDto = mapper.writeValueAsString(droneDto); 
		String actualJSON = mockMvc.perform(post("/drones").contentType(MediaType.APPLICATION_JSON)
				.content(jsonDroneDto)).andExpect(status().isOk()).andReturn().getResponse()
		.getContentAsString();
		assertEquals(jsonDroneDto, actualJSON );	
	}
	
	@Test
	void testLoanDrone() throws Exception {
		when(dronesService.loanDrone(droneMedication)).thenReturn(droneMedication);
		String jsonDroneMedication = mapper.writeValueAsString(droneMedication); 
		String actualJSON = mockMvc.perform(post("/drones/load").contentType(MediaType.APPLICATION_JSON)
				.content(jsonDroneMedication)).andExpect(status().isOk()).andReturn().getResponse()
		.getContentAsString();
		assertEquals(jsonDroneMedication, actualJSON );	
	}
	
	

}
