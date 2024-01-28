package telran.exception;

import telran.drones.api.ServiceExceptionMessages;

public class MedicationNotFoundException extends NotFoundException {

	public  MedicationNotFoundException() {
		super(ServiceExceptionMessages.MEDICATION_NOT_FOUND);
	}
}

