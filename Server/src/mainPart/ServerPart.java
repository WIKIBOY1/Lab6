package mainPart;

import commands.*;
import collection.*;
import exceptions.ConnectionException;
import exceptions.IdNotFoundException;
import exceptions.IncorrectInputDataException;

import java.util.LinkedList;
import java.util.TreeSet;

public class ServerPart {

    private final ServerMaker serverMaker;
    private final TreeSet<Ticket> collection;
    private static boolean isClientConnected = true;
    private final FileWorker fw;


    public ServerPart (TreeSet<Ticket> collection, ServerMaker serverMaker) {
        this.collection = collection;
        this.serverMaker = serverMaker;
        Object inputObject = null;
        fw = new FileWorker(collection);
        try {
            inputObject = serverMaker.waitForRead();
            String path = (String) inputObject;
            serverMaker.waitForWrite(fw.read(path));
        } catch (IncorrectInputDataException e) {
            collection.clear();
            serverMaker.waitForWrite("Проверьте файл на корректность введённых данных.");
        }
        catch (ClassCastException e) {
            if (inputObject instanceof Command) {
                fw.read("D:\\Java\\Labs\\Col.csv");
                readCommand((Command) inputObject);
            } else System.exit(1);
        }
        catch (ConnectionException e) {
            System.out.println(e.getMessage());
            System.out.println(fw.write("Col.csv"));
            System.exit(1);
        }
    }

    private void readCommand(Command command) {
        try {
            String s = check(command);
            serverMaker.waitForWrite(s);
        } catch (ConnectionException e) {
            System.out.println(e.getMessage());
            isClientConnected = false;
        }
    }

    public void readCommands() {
        while (isClientConnected) {
            try {
                Command command = (Command) serverMaker.waitForRead();
                readCommand(command);
            } catch(ConnectionException e) {
                System.out.println(e.getMessage());
                isClientConnected = false;
            }
        }
    }

    private String check(Command command) {
        try {
            String s = "";
            if (command != null) {
                ((CommandWithoutAdditionalArgument) command).updateCollection(collection);
                Ticket newTicket = null;

                if (command.getClass() == AddCommand.class) {
                    newTicket = ((AddCommand) command).ticket;
                }
//                if (command.getClass() == AddIfMaxCommand.class && ((AddIfMaxCommand) command).canNewTicketBeAdded()) {
//                    newTicket = ((AddIfMaxCommand) command).ticket;
//                }
                if (command.getClass() == UpdateCommand.class) {
                    s = command.execute();
                    newTicket = ((UpdateCommand) command).ticket;
                }
                if (newTicket != null) {
                    updateTicketFields(newTicket);
                    s += "Ticket created";
                }
                if (command.getClass() != UpdateCommand.class) s += command.execute();
            }
            return s;
        } catch (IdNotFoundException e) {return e.getMessage();}
    }

    private void updateTicketFields(Ticket ticket) {
        String s;
        String response = "";
        ticket.setCoordinates(null);
        ticket.setType(null);
        do {
            serverMaker.waitForWrite("Введите название билета: ");
            s = (String) serverMaker.waitForRead();
            //System.out.println(s);
            ticket.setName(s);
        } while(s.equals(""));

        do {
            serverMaker.waitForWrite(response + "Введите координаты: (в формате x y)");
            response = "";
            String p = (String) serverMaker.waitForRead();
            String[] j = p.split(" ");
            String[] jj = p.split("\t");
            if (jj.length > j.length) j = jj;
            try {
                if (j.length != 2) response += "Введите корректное число аргументов \n";
                else if (Double.parseDouble(j[0]) > -48 && Double.parseDouble(j[1]) > -48) ticket.setCoordinates(new Coordinates(Double.parseDouble(j[0]), Double.parseDouble(j[1])));
                else response += "Введите корректные значения x и y (они должны быть больше -48) \n";
            } catch (NumberFormatException e) {response += "Введите корректные значения x и y (они должны быть больше -48) \n";}

        } while(ticket.getCoordinates() == null);

        if (ticket.getId() != 0 || ticket.getPrice() == null) {
            ticket.setPrice(-1.0);
            do {
                serverMaker.waitForWrite(response + "Введите стоимость билета: (она должна быть больше 0)");
                response = "";
                String[] j = ((String) serverMaker.waitForRead()).split(" ");
                try {
                    if (j.length == 1) ticket.setPrice(Double.parseDouble(j[0]));
                    else {
                        response += "Введите корректное число \n";
                    }
                } catch (NumberFormatException e) {
                    response += "Введите корректную стоимость \n";
                }
            } while (ticket.getPrice() <= 0);
        }
        if (ticket.getId() == 0) ticket.setId(++Ticket.generalId);
        do {
            serverMaker.waitForWrite(response + "Введите тип билета: (оставьте поле пустым, если хотите) \nСписок возможным типов: VIP, USUAL, BUDGETARY, CHEAP");
            response = "";
            s = (String) serverMaker.waitForRead();
            if (!s.equals("")) {
                try {
                    ticket.setType(TicketType.valueOf(s.toUpperCase()));
                } catch (IllegalArgumentException e) { response += "Введите корректное название типа \n";}
            }
        } while (ticket.getType() == null && !s.equals(""));

        serverMaker.waitForWrite("Куда билет? (если не хотите вводить, оставьте поле пустым, для продолжения напишите название места)");
        s = (String) serverMaker.waitForRead();

        if (!s.equals("")) {
            ticket.setVenue(new Venue(s));
            s = "";
            do {
                serverMaker.waitForWrite(response + "Введите вместимость аудитории: (оставтье пустым, если она неизвестна)");
                response = "";
                try {
                    s = (String) serverMaker.waitForRead();
                    if (s != null && !s.equals("") && Integer.parseInt(s) > 0) ticket.getVenue().setCapacity(Integer.parseInt(s));
                } catch (NumberFormatException | NullPointerException e) {response += "Введите корректное значение \n";}
            } while (ticket.getVenue().getCapacity() == null && s != null && !s.equals(""));

            do {
                serverMaker.waitForWrite(response + "Введите тип аудитории: (оставьте поле пустым, если хотите) \nСписок возможным типов:   BAR, LOFT, THEATRE, MALL, STADIUM");
                response = "";
                s = (String) serverMaker.waitForRead();
                if (!s.equals("")) {
                    try {
                        ticket.getVenue().setType(VenueType.valueOf(s.toUpperCase()));
                    } catch (IllegalArgumentException e) {response += "Введите корректное название типа \n";}
                }
            } while (ticket.getVenue().getType() == null && !s.equals(""));
            s = "1";
            do {
                serverMaker.waitForWrite("Введите имя актёра: ");
                s = (String) serverMaker.waitForRead();
                if (!s.equals("")) ticket.addActor(s);
            } while (!s.equals(""));

        }
    }


}
