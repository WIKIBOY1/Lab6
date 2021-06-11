package mainPart;

import commands.*;
import exceptions.ConnectionException;
import exceptions.IdNotFoundException;
import java.io.Serializable;
import java.sql.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

public class ServerPart {

    private final ServerMaker serverMaker;
    private final DataBaseWorker dataBaseWorker;
    private static boolean isClientConnected = true;
    private HistoryCommand history = new HistoryCommand();
    private ReadWriteLock lock = new ReadWriteLock() {
        @Override
        public Lock readLock() {
            return null;
        }

        @Override
        public Lock writeLock() {
            return null;
        }
    };

    public ServerPart (ServerMaker serverMaker) {
        dataBaseWorker = new DataBaseWorker();
        this.serverMaker = serverMaker;
        history.addHistory();
    }

    private void readCommand(Command command, String userName) {
        lock.readLock();
        try {
            String s = check(command, userName);
            lock.writeLock();
            serverMaker.addRequest(userName, s);
        } catch (ConnectionException e) {
            System.out.println(e.getMessage());
            isClientConnected = false;
        }
    }

    public void readCommands() {
        ExecutorService service = Executors.newCachedThreadPool();
        while (isClientConnected) {
            try {
                try {
                    for (Serializable serializable : serverMaker.getResponses().keySet()) {
                        Command command = (Command) serializable;
                        service.execute(() -> {
                            if (command != null) {
                                String user = serverMaker.getResponses().get(serializable);
                                serverMaker.removeResponse(serializable);
                                if (user != null) {
                                    readCommand(command, user);
                                }
                            }
                        });
                    }
                } catch (ClassCastException e) {
                    System.out.println(serverMaker.getResponses());
                }
            } catch(ConnectionException e) {
                System.out.println(e.getMessage());
                isClientConnected = false;
            }
        }
        service.shutdown();
    }

    private String check(Command command, String userName) {
        try {
            String s = "";
            try {
                if (command != null) {
                    ((CommandWithoutAdditionalArgument) command).updateCollection(dataBaseWorker.getFlats());
                    history.addPlus(command);
                    if (command.getClass() == UpdateCommand.class) {
                        if(((UpdateCommand) command).ID >= 0){
                            ((UpdateCommand) command).getFlat().setId(((UpdateCommand) command).ID);
                            dataBaseWorker.addFlatToDB(((UpdateCommand) command).getFlat(), userName);
                        }
                    }
                    if(command.getClass() == RemoveByIdCommand.class){
                        dataBaseWorker.removeFlatFromDBById(((RemoveByIdCommand) command).getID(), userName);
                    }
                    if(command.getClass() == RemoveAllByHouseCommand.class){
                        dataBaseWorker.removeAllFlatsFromDBByHouse(((RemoveAllByHouseCommand) command).getHouse(), userName);
                    }
                    if(command.getClass() == RemoveGreaterCommand.class || command.getClass() == RemoveLowerCommand.class){
                        command.execute();
                        dataBaseWorker.removeAllFlatsFromDBById(((CommandWithFlatWithoutArgument) command).getHashOfFlats(), userName);
                    }
                    if(command.getClass() == AddCommand.class){
                        dataBaseWorker.addFlatToDB(((AddCommand) command).getFlat(), userName);
                    }
                    if(command.getClass() == ClearCommand.class){
                        dataBaseWorker.clearDB(userName);
                    }
                    if(command.getClass() == HistoryCommand.class){
                        s += history.execute();
                    }
                    if (!s.equals("")) {
                        s += "Flat created";
                    }
                    if (command.getClass() != HistoryCommand.class & command.getClass() != RemoveLowerCommand.class & command.getClass() != RemoveGreaterCommand.class) s += "\n" + command.execute() + "\n";
                }
                dataBaseWorker.updateDB();
                return s;
            } catch (IdNotFoundException e) {
                return e.getMessage();
            }
        } catch (SQLException throwable) {
            return "у вас нет прав доступа";
        }
    }

    public DataBaseWorker getDataBaseWorker() {return dataBaseWorker;}
    public boolean isIsClientConnected() {return isClientConnected;}
}
