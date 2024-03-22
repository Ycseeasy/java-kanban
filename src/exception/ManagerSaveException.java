package exception;

public class ManagerSaveException extends RuntimeException {

    private final String textError;

    public ManagerSaveException(String textError) {
        this.textError = textError;
    }
}
