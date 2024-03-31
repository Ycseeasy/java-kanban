package exception;

public class ManagerSaveException extends RuntimeException {

    public ManagerSaveException(String textError) {
        super(textError);
    }
}
