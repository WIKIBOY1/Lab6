package mainPart;

import collection.*;

import javax.xml.bind.*;
import java.io.*;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeSet;

/**
 * Класс по работе с файлом
 */
public class FileWorker {

    private final TreeSet<Flat> collection;
    private File zeroCollection;
    private Flats flats;
    private boolean logging;

    public boolean isLogging() {
        return logging;
    }

    public void setLogging(boolean logging) {
        this.logging = logging;
    }

    public TreeSet<Flat> getCollection() {
        return collection;
    }

    public FileWorker(TreeSet<Flat> collection) {
        this.collection = collection;
    }

    public File getZeroCollection() {
        return zeroCollection;
    }

    public void setZeroCollection(File zeroCollection) {
        this.zeroCollection = zeroCollection;
    }


    /**
     * Сохранение коллекции
     * @param path - путь для сохранения
     */
    public String save(String path) {
        try {
            Flats flats1 = new Flats();
            flats1.setFlats(collection);
            JAXBContext jaxbContext = JAXBContext.newInstance(Flats.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            FileOutputStream fileOutputStream = new FileOutputStream(new File(System.getenv(path)));
            jaxbMarshaller.marshal(flats1, fileOutputStream);
        } catch (JAXBException | FileNotFoundException e) {
            System.err.println("Вы даже не открыли файл(");
        } catch (NullPointerException e) {
            return "Неверная переменная окружения";
        }
        return "Запись в файл прошла успешно.";
    }
    /**
     * Десериализует коллекцию из файла xml
     */
    public String fillUp() throws JAXBException{
        try {
            if (!getZeroCollection().canRead() || !getZeroCollection().canWrite()) throw new SecurityException();
        } catch (SecurityException ex) {
            if (!this.isLogging()) {
                System.out.println("Не хватает прав доступа для работы с файлом.");
            }
            System.exit(1);
        }
        try{
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(getZeroCollection()));
            JAXBContext context = JAXBContext.newInstance(Flats.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            flats = (Flats) unmarshaller.unmarshal(bufferedInputStream);
            collection.addAll(flats.getFlats());
            Iterator iterator = flats.getFlats().iterator();
            System.out.println("Программа готова к работе");
        } catch (UnmarshalException e){
            return"Файл должен быть формата xml";
        }catch ( NullPointerException | FileNotFoundException e){
            return "В файле не было коллекции";
        }
        return "FillUp успешно заполнил коллекцию";
    }

    /**
     * Находит путь по введённому значению переменной окружения
     */
    private String findPath(){
        String path = "";
        boolean m = false;
        Scanner input = new Scanner(System.in);
        String inputString = "";
        Scanner support;
        do {
            // if (!this.presenter.isLogging()) {
            System.out.println("Введите имя файла");
            // }
            try{
                if (!input.hasNextLine()) {
                    input.close();
                    try {
                        safeExit(path);
                    }catch (NullPointerException e){
                        System.err.println("Нельзя сохранять пустую коллекцию");
                    }
                }
                inputString = input.nextLine();
            }catch (IllegalStateException e){
                System.err.println("Не сегодня");
                System.exit(0);
            }
            if (!inputString.isEmpty()) {
                support = new Scanner(inputString);
                if (support.hasNext()) {
                    path = support.next();
                    m = true;
                }
            }
        } while (!m);
        return path;
    }

    /**
     * Безопасный выход. Сохраняет коллекцию в xml файл и выходит из программы.
     */
    private void safeExit(String path) {
        if (path == ""){
            System.exit(0);
        }
        save(path);
        System.exit(0);
    }

    /**
     * Анализирует на коректность введённого значения переменной окружения
     */
    public String analyzePath(String path){
        // String path = findPath();
        if (path == null){
            System.out.println("Неверный путь.");
            System.exit(1);
        }
        try {
            File file = new File(System.getenv(path));
            setZeroCollection(file);
            if (!getZeroCollection().exists()) {
                System.out.println("Файла по указанному пути не существует.");
                System.out.println(System.getenv(path));
                System.exit(1);
            }
            this.fillUp();
            this.setLogging(true);
        }catch (NullPointerException e){
            return "Неверная переменная окружения";
        }catch (JAXBException e) {
            e.printStackTrace();
        }
        return "Чтение файла прошло успешно";
    }
}
