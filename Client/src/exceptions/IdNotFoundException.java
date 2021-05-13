package exceptions;

/**
 * Id not found exception
 */
public class IdNotFoundException extends RuntimeException {
    /**
     * @return error message
     */
    @Override
    public String getMessage() {
        return "Не удалось найти элемент с таким id";
    }
}
