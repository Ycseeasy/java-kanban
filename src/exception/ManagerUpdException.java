package exception;

public class ManagerUpdException extends RuntimeException {
    private final String textError;

    public ManagerUpdException(String textError) {
        this.textError = textError;
    }

    public void printTextError() {
        System.out.println(textError);
    }
}
