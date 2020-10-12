package ch.heigvd.amt.stoneoverflow.application;

public class BusinessException extends Throwable {
    public BusinessException(String message) {
        super(message);
    }
}
