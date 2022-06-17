package nl.novi.backend.eindopdracht.HidrikLandlust.exceptions;

import java.io.Serial;

public class EmailAlreadyInUseException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public EmailAlreadyInUseException(String email) {
        super("Email " + email + " already in use.");
    }

}