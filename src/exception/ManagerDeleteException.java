package exception;

public class ManagerDeleteException extends RuntimeException {

    public ManagerDeleteException(String textError) {
        super(textError);
    }
}
