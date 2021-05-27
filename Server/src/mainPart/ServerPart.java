package mainPart;

import commands.*;
import collection.*;
import exceptions.ConnectionException;
import exceptions.IdNotFoundException;
import exceptions.IncorrectInputDataException;

import java.util.TreeSet;

public class ServerPart {

    private final ServerMaker serverMaker;
    private final TreeSet<Flat> collection;
    private static boolean isClientConnected = true;
    private final FileWorker fw;
    private States states = States.READ_NAME;


    public ServerPart (TreeSet<Flat> collection, ServerMaker serverMaker) {
        this.collection = collection;
        this.serverMaker = serverMaker;
        Object inputObject;
        Object inputObject1 = null;
        String response = "";
        fw = new FileWorker(collection);
        do {
            try {
//                inputObject = serverMaker.waitForRead(31);
//                System.out.println(inputObject);
//               // String string = (String)inputObject;
//                //System.out.println(string);
//                Integer integer = (Integer) inputObject;
//                System.out.println(integer);
//                serverMaker.waitForWrite(integer.toString());
//                inputObject1 = serverMaker.waitForRead(integer);
//                String path = (String) inputObject1;
                inputObject = serverMaker.waitForRead();
                String path = (String) inputObject;
               // serverMaker.waitForWrite(fw.read(path));
                response = fw.analyzePath(path);
                serverMaker.waitForWrite(response);
                collection = fw.getCollection();
            } catch (IncorrectInputDataException e) {
                collection.clear();
                serverMaker.waitForWrite("Проверьте файл на корректность введённых данных.");
            /*} catch (ClassCastException e) {
                if (inputObject instanceof Command) {
                    fw.analyzePath("с");
                    readCommand((Command) inputObject);
                } //else System.exit(1);*/
            } catch (ConnectionException e) {
                System.out.println(e.getMessage());
                System.out.println(fw.save("USER"));
                System.exit(1);
            }
        }while (!response.contains("успешно"));
    }

    private void readCommand(Command command) {
        try {
            System.out.println("2");
            String s = check(command);
            System.out.println(s);
            serverMaker.waitForWrite(s);
        } catch (ConnectionException e) {
            System.out.println(e.getMessage());
            isClientConnected = false;
        }
    }

    public void readCommands() {
        while (isClientConnected) {
            try {
              //  System.out.println("3");
                Command command = (Command) serverMaker.waitForRead();
               // System.out.println("5");
                readCommand(command);
               // System.out.println("4");
            } catch(ConnectionException e) {
               // System.out.println(e.getMessage());
                isClientConnected = false;
            }
        }
    }

    private String check(Command command) {
        try {
            String s = "";
          //  System.out.println("out");
            if (command != null) {
             //   System.out.println("in");
                ((CommandWithoutAdditionalArgument) command).updateCollection(collection);
               // System.out.println("flat");
              //  System.out.println(command.getClass());
                if (command.getClass() == UpdateCommand.class) {
                    s = command.execute();
                   // System.out.println(1);
                }
                if (!s.equals("")) {
                    s += "Flat created";
                }
                if (command.getClass() != UpdateCommand.class) s += command.execute();
            }
            return s;
        } catch (IdNotFoundException e) {return e.getMessage();}
    }

}
