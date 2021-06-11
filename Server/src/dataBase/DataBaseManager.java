package dataBase;

import collection.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

class DatabaseManager {
    protected static Connection connection;
    protected static Statement statement;
    public static TreeSet<Flat> flats = new TreeSet<>(new IdComparator());
    public static Map<String,User> users = new HashMap<>();

    public static void connect() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(Config.url,  Config.name, Config.password);
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void disconnect() {
        try {
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void dropPeople() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DROP TABLE people");
        statement.executeUpdate();
    }


    public static void loadPeople() {
        try {
            flats.clear();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM people");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                //Integer height = resultSet.getInt("height");
                float x = resultSet.getFloat("x");
                float y = resultSet.getFloat("y");
                Float area = resultSet.getFloat("area");
                Long numberOfRooms = resultSet.getLong("numberOfRooms");
                Furnish furnish = Furnish.valueOf(resultSet.getString("furnish"));
                View view = View.valueOf(resultSet.getString("view"));
                Transport transport = Transport.valueOf(resultSet.getString("transport"));
                Coordinates coordinates = new Coordinates(x, y);
                String nameHouse = resultSet.getString("nameHouse");
                Long year = resultSet.getLong("year");
                Long numberOfFlatsOnFloor = resultSet.getLong("numberOfFlatsOnFloor");
                House house = new House(nameHouse, year, numberOfFlatsOnFloor);
                String login = resultSet.getString("login");
                String password = resultSet.getString("password");
                User user = new User(login,password,false);
                users.put(login,user);
                Flat flat = new Flat(id, name, coordinates, area, numberOfRooms, furnish, view, transport, house, login);
                flats.add(flat);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void savePeople() throws SQLException{
        deletePeople();
        Iterator<Flat> iterator = flats.iterator();
        while(iterator.hasNext()){
            Flat flat = iterator.next();
            PreparedStatement statement = connection.
                    prepareStatement("INSERT INTO flats (name,x,y,creationDate,area,numberOfRooms,furnish,view,transport,nameHouse,year,numberOfFlatsOnFloor,login,password) VALUES (\'"
                            + flat.getName() + "," + flat.getCoordinates().getX() +","+ flat.getCoordinates().getY() +
                            "\'," + flat.getArea() + ",\'" + flat.getCreationDate() + "\',\'" + flat.getNumberOfRooms()
                            +"\',\'"+flat.getFurnish() + "\',\'" + flat.getView() + "\',"+ flat.getTransport()
                            + "," + flat.getHouse().getName() + "," + flat.getHouse().getYear() + flat.getHouse().getNumberOfFlatsOnFloor()
                            +",\'"+ flat.getLogin() + "\'");
            statement.executeUpdate();
        }
    }

    public static void deletePeople() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM flats");
        statement.executeUpdate();
    }

    public static TreeSet<Flat> getFlats() {
        return flats;
    }

//    public static void CreateTable() throws SQLException {
//        statement.executeUpdate("CREATE TABLE flats (id serial, name text, coordinateX float, coordinateY float, height integer, eyeColor text, hairColor text, nationality text, x2 bigint, y2 float, z2 float, login text, password text);");
//    }

}
