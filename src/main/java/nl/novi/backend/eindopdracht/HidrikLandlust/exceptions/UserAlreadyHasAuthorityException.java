package nl.novi.backend.eindopdracht.HidrikLandlust.exceptions;

import java.io.Serial;

public class UserAlreadyHasAuthorityException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public UserAlreadyHasAuthorityException(String username, String authority) {
        super("User " + username + " already has authority " + authority + ".");
    }

}
