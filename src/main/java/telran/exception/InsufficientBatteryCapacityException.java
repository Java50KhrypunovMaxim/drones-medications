package telran.exception;

public class InsufficientBatteryCapacityException  extends RuntimeException {

    public InsufficientBatteryCapacityException() {
        super("Insufficient battery capacity for the drone");
    }
}