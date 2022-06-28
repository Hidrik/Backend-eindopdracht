package nl.novi.backend.eindopdracht.HidrikLandlust.exceptions;

import java.io.Serial;

public class InternalFailureException extends RuntimeException {
        @Serial
        private static final long serialVersionUID = 1L;

        public InternalFailureException(String message) {
            super(message);
        }

}
