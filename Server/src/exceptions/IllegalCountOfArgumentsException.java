package exceptions;

public class IllegalCountOfArgumentsException extends RuntimeException{

    @Override
    public String getMessage() {
        return "Неправильное число аргкментов";
    }
}
