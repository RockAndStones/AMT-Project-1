package ch.heigvd.amt.stoneoverflow.infrastructure.persistance.exception;

public class IntegrityConstraintViolationException extends PersistenceException {
    public IntegrityConstraintViolationException(String message) {
        super(message);
    }
}
