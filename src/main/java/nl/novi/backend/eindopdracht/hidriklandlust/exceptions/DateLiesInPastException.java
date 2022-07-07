package nl.novi.backend.eindopdracht.hidriklandlust.exceptions;

import java.io.Serial;

public class DateLiesInPastException extends RuntimeException {
        @Serial
        private static final long serialVersionUID = 1L;

        public DateLiesInPastException(String message) {
            super(message);
        }

}




