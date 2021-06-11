package mainPart;

import collection.*;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;

public class DataBaseWorker {

    private final String url = "jdbc:postgresql://pg:5432/studs";
    private final String name = "s312506";
    private final String password = "rec281";
    private TreeSet<Flat> flats = new TreeSet<>(new IdComparator());
    private Connection connection = null;

    public void createNewAccountInDB(String userName, String userPassword) throws NoSuchAlgorithmException, SQLException {
        String cryptedPassword = cryptData(userPassword);
        connection.prepareStatement(String.format("INSERT INTO users VALUES ('%s','%s');", userName, cryptedPassword)).executeUpdate();
    }
    public void checkAccountInDB(String userName, String password) throws SQLException, NoSuchAlgorithmException {
        if (userName.equals("alreadyLoggedInUser")) throw new SQLException("Недопустимое имя пользователя");
        ResultSet users = connection.prepareStatement(String.format("SELECT * FROM users WHERE login = '%s';", userName)).executeQuery();
        String cryptPassword = cryptData(password);
        int k = 0;
        while (users.next()) {
            k++;
            if (!users.getString("password").equals(cryptPassword)) throw new SQLException("Пароль не подходит");
        }
        if (k == 0) throw new SQLException("Пользователя с данным именем не найденно");
    }
    public String cryptData(Serializable data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] cryptPassword = md.digest(ServerMaker.serialize(data + "UniqueSalt"));
        //System.out.println(cryptPassword);
        BigInteger no = new BigInteger(1, cryptPassword);
        return no.toString(16);
    }
    public void connectToDB() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        System.out.println("Драйвер подключен");
        connection = DriverManager.getConnection(url, name, password);
        System.out.println("Соединение установлено");
        Statement statement = connection.createStatement();
//        statement.executeUpdate("CREATE TABLE users\n" +
//                "(\n" +
//                "    login text COLLATE pg_catalog.\"default\" NOT NULL,\n" +
//                "    password text COLLATE pg_catalog.\"default\" NOT NULL,\n" +
//                "    CONSTRAINT users_pkey PRIMARY KEY (login)\n" +
//                ")\n" +
//                "\n" +
//                "TABLESPACE pg_default;\n" +
//                "\n" +
//                "ALTER TABLE users\n" +
//                "    OWNER to s312506;");
//        statement.executeUpdate("CREATE TABLE Collection\n" +
//                "(\n" +
//                "    id serial NOT NULL,\n" +
//                "    login text NOT NULL,\n" +
//                "    name text NOT NULL,\n" +
//                "    coordinateX float  NOT NULL,\n" +
//                "    coordinateY float  NOT NULL,\n" +
//                "    numberOfRooms int  NOT NULL,\n" +
//                "    area float  NOT NULL,\n" +
//                "    transport text,\n" +
//                "    view text NOT NULL,\n" +
//                "    furnish text NOT NULL,\n" +
//                "    houseName text,\n" +
//                "    year int,\n" +
//                "    numberOfFlatsOnFloor int,\n" +
//                //"    CONSTRAINT tickets_pkey PRIMARY KEY (id),\n" +
//               // "    CONSTRAINT \"flat-userLink\" FOREIGN KEY (\"login\")\n" +
////                "        REFERENCES users (login) MATCH SIMPLE\n" +
////                "        ON UPDATE NO ACTION\n" +
////                "        ON DELETE NO ACTION\n" +
////                "        NOT VALID,\n" +
//                "    CONSTRAINT flats_coordinateX_check CHECK (coordinateY > -850::integer::float ),\n" +
//                "    CONSTRAINT flats_price_check CHECK (area > 0::float )\n" +
//                ");");
    }

    public void getCollectionFromDB() throws SQLException {
        ResultSet resultFlatSet = connection.prepareStatement("SELECT * FROM Collection;").executeQuery();
        while (resultFlatSet.next()) {
            int flatId = resultFlatSet.getInt("id");
            String name = resultFlatSet.getString("name");
            Double CoordinateX = resultFlatSet.getDouble("coordinateX");
            Float CoordinateY = resultFlatSet.getFloat("coordinateY");
            Float area = resultFlatSet.getFloat("area");
            Long numberOfRooms = resultFlatSet.getLong("numberOfRooms");
            Furnish furnish = Furnish.valueOf(resultFlatSet.getString("furnish"));
            View view = View.valueOf(resultFlatSet.getString("view"));
            Transport transport = null;
            House house = null;
            String login = resultFlatSet.getString("login");
            try {
                transport = Transport.valueOf(resultFlatSet.getString("transport"));
            } catch (NullPointerException e) {}
            String houseName = null;
            Long year = null;
            Long numberOfFlatsOnFloor = null;
            try {
                houseName = resultFlatSet.getString("houseName");
                year = resultFlatSet.getLong("year");
                numberOfFlatsOnFloor = resultFlatSet.getLong("numberOfFlatsOnFloor");
                house = new House(houseName, year, numberOfFlatsOnFloor);
            }catch (NullPointerException e) {}
            if(houseName.equals("NULL")){
                house = null;
            }
            Flat flat = new Flat(flatId, name, new Coordinates(CoordinateX, CoordinateY), area, numberOfRooms, furnish, view, transport, house, login);
            flats.add(flat);
        }
    }
    public void addFlatToDB(Flat flat, String userName) throws SQLException {
        flat.setLogin(userName);
        if (flat.getId() == -1) {
            if (flat.getHouse() != null) {
                connection.prepareStatement(String.format("INSERT INTO Collection (login, name, coordinateX, coordinateY, area, numberOfRooms, furnish, view, transport, houseName, year, numberOfFlatsOnFloor) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');",
                        flat.getLogin(), flat.getName(), flat.getCoordinates().getX(),
                        flat.getCoordinates().getY(),
                        flat.getArea().toString().replace(',','.'), flat.getNumberOfRooms().toString(),
                        flat.getFurnish().toString(), flat.getView().toString(), flat.getTransport().toString().replace("'null'", "NULL"),
                        flat.getHouse().getName(), flat.getHouse().getYear(), flat.getHouse().getNumberOfFlatsOnFloor())).executeUpdate();
            }
            else{
                connection.prepareStatement(String.format("INSERT INTO Collection (login, name, coordinateX, coordinateY, area, numberOfRooms, furnish, view, transport, houseName, year, numberOfFlatsOnFloor) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');",
                        flat.getLogin(), flat.getName(), flat.getCoordinates().getX(),
                        flat.getCoordinates().getY(),
                        flat.getArea().toString().replace(',','.'), flat.getNumberOfRooms().toString(),
                        flat.getFurnish().toString(), flat.getView().toString(), flat.getTransport().toString().replace("'null'", "NULL"),
                        "NULL", 0, 0)).executeUpdate();
            }
            setFlats(new TreeSet<>(new IdComparator()));
            getCollectionFromDB();
        } else updateFlatInDB(flat);
    }

    public void updateFlatInDB(Flat flat) throws SQLException {
        for(Flat flat1 : flats){
            if(flat.getId() == flat1.getId()){
                if(flat.getLogin().equals(flat1.getLogin())){
                    if (flat.getHouse() != null) {
                        connection.prepareStatement(String.format("UPDATE Collection SET login = '%s', name = '%s', coordinateX = '%s', coordinateY = '%s', area = '%s', numberOfRooms = '%s', furnish = '%s', view = '%s', transport = '%s', houseName = '%s', year = '%s', numberOfFlatsOnFloor = '%s' WHERE id = '%s';",
                                flat.getLogin(), flat.getName(), flat.getCoordinates().getX(),
                                flat.getCoordinates().getY(),
                                flat.getArea().toString().replace(',', '.'), flat.getNumberOfRooms().toString(),
                                flat.getFurnish().toString(), flat.getView().toString(), flat.getTransport().toString().replace("'null'", "NULL"),
                                flat.getHouse().getName(), flat.getHouse().getYear(), flat.getHouse().getNumberOfFlatsOnFloor(), flat.getId())).executeUpdate();
                    } else {
                        connection.prepareStatement(String.format("UPDATE Collection SET login = '%s', name = '%s', coordinateX = '%s', coordinateY = '%s', area = '%s', numberOfRooms = '%s', furnish = '%s', view = '%s', transport = '%s', houseName = '%s', year = '%s', numberOfFlatsOnFloor = '%s' WHERE id = '%s';",
                                flat.getLogin(), flat.getName(), flat.getCoordinates().getX(),
                                flat.getCoordinates().getY(),
                                flat.getArea().toString().replace(',', '.'), flat.getNumberOfRooms().toString(),
                                flat.getFurnish().toString(), flat.getView().toString(), flat.getTransport().toString().replace("'null'", "NULL"),
                                "NULL", 0, 0, flat.getId())).executeUpdate();
                    }
                }else throw new SQLException();
            }
        }
    }

    public void removeFlatFromDB(Flat flat) throws SQLException {
        for(Flat flat1 : flats) {
            if (flat.getId() == flat1.getId()) {
                if (flat.getLogin().equals(flat1.getLogin())) {
                    connection.prepareStatement(String.format("DELETE FROM Collection WHERE id = '%s';", flat.getId())).executeUpdate();
                }
            }
        }
    }

    public void removeFlatFromDBById(int id, String userName) throws SQLException {
        for(Flat flat1 : flats) {
            if (flat1.getId() == id) {
                if (flat1.getLogin().equals(userName)) {
                    connection.prepareStatement(String.format("DELETE FROM Collection WHERE id = '%s';", id)).executeUpdate();
                }
            }
        }
    }

    public void removeAllFlatsFromDBByHouse(House house, String userName) throws SQLException {
        for(Flat flat : flats) {
            if (flat.getHouse() == house) {
                if (flat.getLogin().equals(userName)) {
                    if(house != null) {
                        connection.prepareStatement(String.format("DELETE FROM Collection WHERE houseName = '%s', year = '%s', numberOfFlatsOnFloor = '%s';", house.getName(), house.getYear(), house.getNumberOfFlatsOnFloor())).executeUpdate();
                    }else {
                        connection.prepareStatement(String.format("DELETE FROM Collection WHERE houseName = '%s', year = '%s', numberOfFlatsOnFloor = '%s';", "NULL", 0, 0)).executeUpdate();
                    }
                }
            }
        }
    }

    public void removeAllFlatsFromDBById(HashSet<Integer> hashSet, String userName) throws SQLException {
        for(Integer integer: hashSet) {
            for(Flat flat1 : flats) {
                if (flat1.getId() == integer) {
                    if (flat1.getLogin().equals(userName)) {
                         connection.prepareStatement(String.format("DELETE FROM Collection WHERE id = '%s';", integer)).executeUpdate();
                    }
                }
            }
        }
    }

    public void clearDB(String userName) throws SQLException {
        connection.prepareStatement(String.format("DELETE FROM Collection WHERE login = '%s';", userName)).executeUpdate();
    }

    public void updateDB() throws SQLException {
        TreeSet<Flat> newFlats = getFlats();
        setFlats(new TreeSet<>(new IdComparator()));
        getCollectionFromDB();
        Set<Integer> flatId = new HashSet<>();
        for (Flat flat : newFlats) {
            flatId.add(flat.getId());
        }
        for (Flat flat : getFlats()) {
            if (!flatId.contains(flat.getId())) removeFlatFromDB(flat);
        }
        setFlats(newFlats);
    }

    public TreeSet<Flat> getFlats() {return flats;}
    public void setFlats(TreeSet<Flat> flats) {this.flats = flats;}
}