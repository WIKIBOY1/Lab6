package commands;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class Checker {
    /**
     * Проверка на конец потока вывода(Ctrl + D)
     */
    public static String check(Scanner in){
        try{
            return in.nextLine().trim();
        } catch (NoSuchElementException ex) {
            in.close();
            System.err.println("Хорошая попытка. Попоробуйте что-нибудь посложнее");
            return "";
        }
    }
}
