package telran.exception;

public class ExcessiveWeightException extends RuntimeException {

    public ExcessiveWeightException() {
        super("Excessive weight for the drone");
    }
}