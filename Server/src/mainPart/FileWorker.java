package mainPart;

import exceptions.IncorrectInputDataException;
import collection.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.TreeSet;

/**
 * Class to work with files
 */
public class FileWorker {
    /**collection of tickets*/
    private final TreeSet<Ticket> c;
    /**file path*/
    String fileNameDefined = "Collection.csv";
    /**ticket id*/
    private  int id;
    /**ticket name*/
    private String name;
    /**ticket coordinates*/
    private Coordinates coordinates;
    /**ticket price*/
    private Double price;
    /**ticket type*/
    private TicketType type;
    /**venue name*/
    private String venueName;
    /**venue capacity*/
    private Integer venueCapacity;
    /**venue type*/
    private VenueType venueType;

    /**
     * Constructor with argument
     * @param c - collection of tickets
     */
    public FileWorker(TreeSet<Ticket> c) {
        this.c = c;
    }

    /**
     * File reader
     * @param filePath - file to read
     */
    public String read(String filePath) {
        try{
            if (!filePath.equals("")) fileNameDefined = filePath;
            File file = new File(fileNameDefined);
            Scanner inputStream = new Scanner(file);
            String delimiterDeterminant = inputStream.nextLine();
            String delimiter = ";";
            if (delimiterDeterminant.split(",").length > delimiterDeterminant.split(";").length) delimiter = ",";
            String[] queue = delimiterDeterminant.split(delimiter);
            while(inputStream.hasNextLine()){
                String[] data = inputStream.nextLine().split(delimiter);
                for (int k = 0; k < data.length; k++) {
                    if (!data[k].equals("")) {
                        if (queue[k].equals("id")) {
                            int m = Integer.parseInt(data[k]);
                            if (m > 0 && m < 214700000) {
                                for (Ticket t : c) if (t.getId() == m) throw new IncorrectInputDataException();
                                id = m;
                            } else throw new IncorrectInputDataException();
                            for (Ticket t : c) {
                                if (t.getId() == m) throw new IncorrectInputDataException();
                            }
                            if (m > Ticket.getGeneralId()) Ticket.setGeneralId(m);
                        }
                        if (queue[k].equals("name")) if (!data[k].equals("")) name = data[k];
                        else throw new IncorrectInputDataException();
                        if (queue[k].equals("coordinates")) {
                            String[] s = data[k].split(" ");
                            if (s.length == 2 && Double.parseDouble(s[0]) > -48 && Double.parseDouble(s[1]) > -48) {
                                Double[] m = {Double.parseDouble(s[0]), Double.parseDouble(s[1])};
                                coordinates = new Coordinates(m[0], m[1]);
                            } else throw new IncorrectInputDataException();
                        }
                        if (queue[k].equals("price"))
                            if (Double.parseDouble(data[k]) > 0) price = Double.parseDouble(data[k]);
                            else throw new IncorrectInputDataException();
                        if (queue[k].equals("venueName")) if (!data[k].equals("")) venueName = data[k];
                        else throw new IncorrectInputDataException();
                        if (queue[k].equals("venueCapacity")) {
                            int m = Integer.parseInt(data[k]);
                            if (m > 0) venueCapacity = Integer.parseInt(data[k]);
                            else throw new IncorrectInputDataException();
                        }
                        try {
                            if (queue[k].equals("type") && !data[k].equals(""))
                                type = TicketType.valueOf(data[k].toUpperCase());
                            if (queue[k].equals("venueType") && !data[k].equals(""))
                                venueType = VenueType.valueOf(data[k].toUpperCase());
                        } catch (IllegalArgumentException e) {
                            System.out.println("Один из типов в файле был введён неправльно, поэтому = null");
                        }
                    }
                }
                if (coordinates == null || price == null) { throw new IncorrectInputDataException();
                } else {
                    c.add(new Ticket(id, name, coordinates, price, type, venueName, venueCapacity, venueType));
                    name = null;
                    coordinates = null;
                    price = null;
                    type = null;
                    venueName = null;
                    venueCapacity = null;
                    venueType = null;
                }
            }
          //  CommandDecoder.sort(c);
        } catch (FileNotFoundException e){
            return "Не удалось найти указанный файл";
        }
        catch (SecurityException ex) {
            return "Не хватает прав доступа для работы с файлом.";
        }
        return "Чтение из файла прошло успешно";
    }

    /**
     * File writer
     * @param fileNameDefined - file to write
     */
    public String write(String fileNameDefined) {
        try {
            File file = new File(fileNameDefined);
            String output;
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.println("id,name,coordinates,price,type,venueName,venueCapacity,venueType");
                for (Ticket t : c) {
                    output = t.getId() + "," + t.getName() + "," + t.getCoordinates().getX() + " " + t.getCoordinates().getY() + "," + t.getPrice() + "," + t.getType();
                    if (t.getVenue() != null) {
                        output = output + "," + t.getVenue().getName() + "," + t.getVenue().getCapacity() + "," + t.getVenue().getType();
                    }
                    printWriter.println(output.replace("null", ""));
                }
            printWriter.close();
        } catch (FileNotFoundException e) {
            return "Не удалось найти указанный файл.";
        }
        catch (SecurityException ex) {
            return "Не хватает прав доступа для работы с файлом.";
        }
        return "Запись в файл прошла успешно.";
    }
}
