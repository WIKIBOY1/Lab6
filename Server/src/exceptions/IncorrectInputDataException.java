package exceptions;

public class IncorrectInputDataException extends RuntimeException {
    public IncorrectInputDataException(String s) {
        super(s);
    }

    @Override
    public String getMessage() {
        return "Неправильные входные данные";
    }
}
