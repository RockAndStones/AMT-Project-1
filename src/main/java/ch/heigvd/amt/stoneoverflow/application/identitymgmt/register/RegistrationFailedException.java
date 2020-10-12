package ch.heigvd.amt.stoneoverflow.application.identitymgmt.register;

import ch.heigvd.amt.stoneoverflow.application.BusinessException;

public class RegistrationFailedException extends BusinessException {
    public RegistrationFailedException(String message) {
        super(message);
    }
}
