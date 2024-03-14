package taskManagers;

import java.io.IOException;

public class ManagerSaveException extends RuntimeException {

    private String textError;
    public ManagerSaveException(String textError) {
        this.textError = textError;
    }
}
