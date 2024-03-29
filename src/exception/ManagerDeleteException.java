
package exception;

import java.util.Arrays;

public class ManagerDeleteException extends RuntimeException {

    private final String textError;

    public ManagerDeleteException(String textError) {
        this.textError = textError;
    }

    public void printTextError() {
        System.out.println(Arrays.toString(getStackTrace()) + " -> " + textError);
    }
}
