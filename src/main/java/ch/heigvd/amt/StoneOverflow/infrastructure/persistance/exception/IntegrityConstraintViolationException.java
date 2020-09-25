package ch.heigvd.amt.StoneOverflow.infrastructure.persistance.exception;

public class IntegrityConstraintViolationException extends PersistenceException {
    public IntegrityConstraintViolationException(String message) {
        super(message);
    }
}
