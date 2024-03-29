
package exception;

import java.util.Arrays;

public class ManagerEpicSubtaskListException extends RuntimeException {

    private final String textError;

    public ManagerEpicSubtaskListException(String textError) {
        this.textError = textError;
    }

    public void printTextError() {
        System.out.println(Arrays.toString(getStackTrace()) + " -> " + textError);
    }
}
