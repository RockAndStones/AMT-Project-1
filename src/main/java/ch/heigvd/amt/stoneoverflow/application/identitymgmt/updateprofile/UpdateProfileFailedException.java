package ch.heigvd.amt.stoneoverflow.application.identitymgmt.updateprofile;

import ch.heigvd.amt.stoneoverflow.application.BusinessException;

public class UpdateProfileFailedException extends BusinessException {
    public UpdateProfileFailedException(String message) {
        super(message);
    }
}
