package telran.drones.dto;

import static telran.drones.api.ValidationConstants.*;

import jakarta.validation.constraints.NotEmpty;

public record DroneDto(
		@NotEmpty (message=MISSING_NUMBER_OF_DRON_MESSAGE)String number,
		@NotEmpty (message=MISSING_ModelType_OF_DRON_MESSAGE)ModelType modelType) 
{

}
