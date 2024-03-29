package exception;

import java.util.Arrays;

public class ManagerIntersectionTimeException extends RuntimeException {
    private final String textError;

    public ManagerIntersectionTimeException(String textError) {
        this.textError = textError;
    }

    public void printTextError() {
        System.out.println(Arrays.toString(getStackTrace()) + " -> " + textError);
    }
}
