package telran.drones.dto;

import static telran.drones.api.ValidationConstants.*;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DroneDto(
		@NotEmpty (message=MISSING_NUMBER_OF_DRON_MESSAGE)
		@Pattern(regexp = DRONE_NUMBER_REGEXP, message = WRONG_DRONE_NUMBER_MESSAGE) String number,
		@NotNull (message=MISSING_ModelType_OF_DRON_MESSAGE)ModelType modelType) 
{

}
