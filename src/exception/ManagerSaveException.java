
package exception;

public class ManagerSaveException extends RuntimeException {

    private final String textError;

    public ManagerSaveException(String textError) {
        this.textError = textError;
    }

    public void printTextError() {
        System.out.println(textError);
    }
}
