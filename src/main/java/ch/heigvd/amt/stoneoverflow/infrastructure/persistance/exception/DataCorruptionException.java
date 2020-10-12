package ch.heigvd.amt.StoneOverflow.infrastructure.persistance.exception;

public class DataCorruptionException extends PersistenceException{
    public DataCorruptionException(String message) {
        super(message);
    }
}
