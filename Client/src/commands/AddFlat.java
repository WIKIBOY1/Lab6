package commands;

import collection.*;
import exceptions.IncorrectInputDataException;

import java.io.Serializable;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;
import static commands.Checker.check;

public class AddFlat implements Serializable {
    String last_string;
    States statesOfFlat = States.READ_NAME;
    States statesOfHouse = States.READ_ADD_HOUSE;

    public Flat addFlat(Scanner in){
        String nameFlat=null;
        double X = 0;
        float Y = 0;
        float area1 = 0;
        Furnish furnish= null;
        View view = null;
        Transport transport = null;
        long numberOfRooms = 0;
        House house1 = null;
        boolean m = false;
        String s = "";
        do {
            try {
                if(statesOfFlat == States.READ_NAME){
                    System.out.println("Введите название квартиры: ");
                    nameFlat = addName(check(in));
                    statesOfFlat = States.READ_X;
                }
                if(statesOfFlat == States.READ_X){
                    System.out.println("Введите координату x: ");
                    X = addX(check(in));
                    statesOfFlat = States.READ_Y;
                }
                if(statesOfFlat == States.READ_Y) {
                    System.out.println("Введите координату y(больше -850): ");
                    Y = addY(check(in));
                    statesOfFlat = States.READ_AREA;
                }
                if(statesOfFlat == States.READ_AREA){
                    System.out.println("Введите площадь(больше 0): ");
                    area1 = addArea(check(in));
                    statesOfFlat = States.READ_NUMBER_OF_ROOMS;
                }
                if(statesOfFlat == States.READ_NUMBER_OF_ROOMS){
                    System.out.println("Введите количество комнат(больше 0): ");
                    numberOfRooms = addLong(check(in));
                    statesOfFlat = States.READ_VIEW;
                }
                if(statesOfFlat == States.READ_VIEW){
                    System.out.println("Выберете вид 1-3 (" + Arrays.toString(View.values()) + "): ");
                    view = addView(check(in));
                    statesOfFlat = States.READ_TRANSPORT;
                }
                if(statesOfFlat == States.READ_TRANSPORT){
                    System.out.println("Выберете транспорт 1-5(" + Arrays.toString(Transport.values()) + ") или 0, если ничего не знаете о транспорте: ");
                    transport = addTransport(check(in));
                    statesOfFlat = States.READ_FURNISH;
                }
                if(statesOfFlat == States.READ_FURNISH){
                    System.out.println("Выберете мебель 1-5(" + Arrays.toString(Furnish.values()) + "): ");
                    furnish = addFurnish(check(in));
                    house1 = addHouse(in);
                    statesOfFlat = States.READ_NAME;
                    m = true;
                }
            }catch (IncorrectInputDataException e) {
                System.err.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.err.println("Вы ввели неверные данные");
            }
        }while (!m);
         return new Flat(nameFlat, new Coordinates(X, Y), area1, numberOfRooms, furnish, view, transport, house1);
    }
    /**
     * Добовление дома
     */
    public House addHouse(Scanner in){
        long year1 = 0;
        long numberOfFlatsOnFloor1 = 0;
        String nameHouse1=null;
        boolean m = false;
        boolean houseAnswer = false;
        do {
            try {
                if(statesOfHouse == States.READ_ADD_HOUSE){
                    System.out.println("Хотите ввести дом[Да - 1, Нет - 2]?");
                    houseAnswer = TryAddSomething.addSomething(check(in));
                    if(!houseAnswer){
                        m = true;
                    }else {
                        statesOfHouse = States.READ_HOUSE_NAME;
                    }
                }
                if(statesOfHouse == States.READ_HOUSE_NAME){
                    System.out.println("Введите имя дома: ");
                    nameHouse1 = addName(check(in));
                    statesOfHouse = States.READ_YEAR;
                }
                if(statesOfHouse == States.READ_YEAR){
                    System.out.println("Введите год постройки дома(больше 0): ");
                    year1 = addLong(check(in));
                    statesOfHouse = States.READ_NUMBER_OF_FLATS_ON_FLOOR;
                }
                if(statesOfHouse == States.READ_NUMBER_OF_FLATS_ON_FLOOR) {
                    System.out.println("Введите количество квартир на этаже(больше 0): ");
                    numberOfFlatsOnFloor1 = addLong(check(in));
                    statesOfHouse = States.READ_ADD_HOUSE;
                    m = true;
                    return new House(nameHouse1, year1, numberOfFlatsOnFloor1);
                }
            } catch (IncorrectInputDataException e) {
                System.err.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.err.println("Вы ввели неверные данные");
            }
        }while (!m);
        return null;
    }
    /**
     * Добавляет элемент в коллекцию
     */
//    public void add(Flat flat){
//        flats.getFlats().add(flat);
//    }

    /**
     * Добавляет координату X
     */
    public double addX(String inputString){
        if (!inputString.isEmpty()) {
            return Double.parseDouble(inputString);
        }else{
            throw new IncorrectInputDataException("Введите что-нибудь");
        }
    }

    /**
     * Добавляет координату Y
     */
    public float addY(String inputString){
        if (!inputString.isEmpty()) {
            float y = Float.parseFloat(inputString);
            if (y > -850) {
                return y;
            } else {
                throw new IncorrectInputDataException("Введёные данные не коректны");
            }
        }
        else{
            throw new IncorrectInputDataException("Введите что-нибудь");
        }
    }

    /**
     * Добавляет имя
     */
    public String addName(String inputString){
        if (!inputString.isEmpty()) {
            return inputString;//.split(" ")[0];
        }
        else {
            throw new IncorrectInputDataException("Введите что-нибудь");
        }
    }

    /**
     * Добавляет площадь
     */
    public float addArea(String inputString){
        if (!inputString.isEmpty()) {
            float AREA = Float.parseFloat(inputString);
            if (AREA > 0) {
                return AREA;
            }else{
                throw new IncorrectInputDataException("Введёные данные не коректны");
            }
        }
        else{
            throw new IncorrectInputDataException("Введите что-нибудь");
        }
    }

    /**
     * Добавляет переменную типа Long(numberOfRooms, year, numberOfFlatsOnFloor)
     */
    public long addLong(String inputString){
        if (!inputString.isEmpty()) {
            long longNumber = Long.parseLong(inputString);
            if (longNumber > 0) {
                return longNumber;
            }else{
                throw new IncorrectInputDataException("Вы ввели неверные данные, значение должно быть больше 0");
            }
        }
        else{
            throw new IncorrectInputDataException("Введите что-нибудь");
        }
    }

    /**
     * Добавляет вид
     */
    public View addView(String inputString){
        if (!inputString.isEmpty()) {
            int intNumber = Integer.parseInt(inputString);
            if (intNumber >= 1 && intNumber <= 3) {
                return View.values()[intNumber - 1];
            }else{
                throw new IncorrectInputDataException("Вы ввели неверные данные, аргумент должен принимать значения от 1 до 3");
            }
        }
        else {
            throw new IncorrectInputDataException("Введите что-нибудь");
        }
    }

    /**
     * Добавляет количество транспарта
     */
    public Transport addTransport(String inputString){
        if (!inputString.isEmpty()) {
            int intNumber = Integer.parseInt(inputString);
            if (intNumber >= 0 && intNumber <= 5) {
                if(intNumber > 0) {
                    return Transport.values()[intNumber - 1];
                }
                else{
                    return null;
                }
            }
            else{
                throw new IncorrectInputDataException("Вы ввели неверные данные, аргумент должен принимать значения от 0 до 5");
            }
        }
        else {
            throw new IncorrectInputDataException("Введите что-нибудь");
        }
    }

    /**
     * Добавляет состояние мебели
     */
    public Furnish addFurnish(String inputString){
        if (!inputString.isEmpty()) {
            int intNumber = Integer.parseInt(inputString);
            if (intNumber >= 1 && intNumber <= 5) {
                return Furnish.values()[intNumber - 1];
            }else{
                throw new IncorrectInputDataException("Вы ввели неверные данные, аргумент должен принимать значения от 1 до 5");
            }
        }
        else {
            throw new IncorrectInputDataException("Введите что-нибудь");
        }
    }
}
