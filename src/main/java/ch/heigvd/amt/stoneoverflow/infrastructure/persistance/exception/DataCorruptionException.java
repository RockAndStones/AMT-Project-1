package ch.heigvd.amt.stoneoverflow.infrastructure.persistance.exception;

public class DataCorruptionException extends PersistenceException{
    public DataCorruptionException(String message) {
        super(message);
    }
}
