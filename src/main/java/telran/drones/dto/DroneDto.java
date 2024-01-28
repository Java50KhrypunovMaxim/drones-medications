package telran.drones.dto;

import static telran.drones.api.ValidationConstants.*;

import java.util.Objects;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DroneDto(
		@NotEmpty (message=MISSING_NUMBER_OF_DRON_MESSAGE)
		@Pattern(regexp = DRONE_NUMBER_REGEXP, message = WRONG_DRONE_NUMBER_MESSAGE) String number,
		@NotNull (message=MISSING_ModelType_OF_DRON_MESSAGE)ModelType modelType) 
{
	@Override
	public int hashCode() {
		return Objects.hashCode(number);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DroneDto other = (DroneDto) obj;
		return Objects.equals(number, other.number);
	}

}
