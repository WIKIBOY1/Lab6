package exceptions;
/**
 * Incorrect input data exception
 */
public class IncorrectInputDataException extends RuntimeException {
    public IncorrectInputDataException(String s) {
        super(s);
    }

    /**
     * @return error message
     */
    @Override
    public String getMessage() {
        return "Неправильные входные данные";
    }
}
