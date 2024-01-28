package telran.drones.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import static telran.drones.api.ValidationConstants.*;

public record DroneMedication(
		@NotEmpty(message = MISSING_NUMBER_OF_DRON_MESSAGE) 
		@Pattern(regexp = DRONE_NUMBER_REGEXP, message = WRONG_DRONE_NUMBER_MESSAGE) String droneNumber,
		@NotEmpty(message = MISSING_MEDICATION_CODE_MESSAGE) 
		@Pattern(regexp = MEDICATION_CODE_REGEXP, message = WRONG_MEDICATION_CODE_MESSAGE)String medicationCode) {

}
