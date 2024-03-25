
package exception;

import java.util.Arrays;

public class ManagerAddException extends RuntimeException {

    private final String textError;

    public ManagerAddException(String textError) {
        this.textError = textError;
    }

    public void printTextError() {
        System.out.println(Arrays.toString(getStackTrace()) + " -> " + textError);
    }
}
