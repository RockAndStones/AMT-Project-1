package ch.heigvd.amt.StoneOverflow.application.identitymgmt.register;

import ch.heigvd.amt.StoneOverflow.application.BusinessException;

public class RegistrationFailedException extends BusinessException {
    public RegistrationFailedException(String message) {
        super(message);
    }
}
