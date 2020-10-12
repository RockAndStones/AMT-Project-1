package ch.heigvd.amt.stoneoverflow.infrastructure.persistance.exception;

public class PersistenceException extends RuntimeException{
    public PersistenceException(String message) {
        super(message);
    }
}
