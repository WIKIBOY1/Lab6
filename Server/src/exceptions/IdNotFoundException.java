package exceptions;

public class IdNotFoundException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Не удалось найти элемент с таким id";
    }
}
