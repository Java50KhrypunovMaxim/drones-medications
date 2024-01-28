package telran.drones.api;

public interface ValidationConstants {
	 String MISSING_NUMBER_OF_DRON_MESSAGE = "Missing number of drons";
	 String MISSING_MEDICATION_CODE_MESSAGE = "Missing medication code";
	 String WRONG_MEDICATION_CODE_MESSAGE ="Wrong medication code. Number must be (\\d{2}-\\d{2}-\\d{1}";
	 String MISSING_ModelType_OF_DRON_MESSAGE = "Missing model type of drone";
	 String DRONE_NUMBER_REGEXP = "(\\d{3}-\\d{3})";
	 String WRONG_DRONE_NUMBER_MESSAGE = "Wrong drone number. Number must be (\\d{3}-\\d{3})";
	 String MEDICATION_CODE_REGEXP = "(\\d{2}-\\d{2}-\\d{1})";
}
