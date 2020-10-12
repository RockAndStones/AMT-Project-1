package ch.heigvd.amt.StoneOverflow.infrastructure.persistance.exception;

public class PersistenceException extends RuntimeException{
    public PersistenceException(String message) {
        super(message);
    }
}
