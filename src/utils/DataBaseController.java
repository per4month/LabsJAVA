package utils;

import Habitat.Habitat;
import Rabbit.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;

public class DataBaseController {
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "wasilchenko1";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";

    public static void saveConfigToDataBase() throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        Statement statement = connection.createStatement();
        String sqlClear = "truncate Config;";
        statement.executeUpdate(sqlClear);
        String sql = "insert into Config (N1, P1, N2, K, D1, D2) VALUES (?,?,?,?,?,?);";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1,Habitat.getN1());
        preparedStatement.setInt(2,Habitat.getP1());
        preparedStatement.setInt(3,Habitat.getN2());
        preparedStatement.setInt(4,Habitat.getK());
        preparedStatement.setInt(5,Habitat.getD1());
        preparedStatement.setInt(6,Habitat.getD2());
        preparedStatement.executeUpdate();
    }
    public static ArrayList<Integer> loadConfigFromDataBase() throws SQLException {
        ArrayList<Integer> params = new ArrayList<>();
        Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        Statement statement = connection.createStatement();
        String sql = "select * from Config;";
        ResultSet resultSet = statement.executeQuery(sql);
        while(resultSet.next()){
            params.add(resultSet.getInt("N1"));
            params.add(resultSet.getInt("P1"));
            params.add(resultSet.getInt("N2"));
            params.add(resultSet.getInt("K"));
            params.add(resultSet.getInt("D1"));
            params.add(resultSet.getInt("D2"));
        }
        return params;
    }
    public static void saveRabbitsToRancho() throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        Statement statement = connection.createStatement();
        String sqlClear = "truncate Rancho;";
        statement.executeUpdate(sqlClear);
        Vector<Rabbit> rabbitsVector = RabbitsStorage.getInstance().getRabbitVector();
        PreparedStatement preparedStatement;
        String sql;
        for (int i =0; i < rabbitsVector.size(); i++) {
            sql = "insert into Rancho (name, birthTime, deathTime, uuid, x, y, routeX, " +
                    "routeY, pathToImage) values (?,?,?,?,?,?,?,?,?);";
            preparedStatement = connection.prepareStatement(sql);
            if (rabbitsVector.get(i) instanceof RabbitAlbinos ) {
                preparedStatement.setString(1, "Albinos");
            } else {
                preparedStatement.setString(1, "Ordinary");
            }
            preparedStatement.setInt(2,rabbitsVector.get(i).getBirthTime());
            preparedStatement.setInt(3,rabbitsVector.get(i).getDeathTime());
            preparedStatement.setString(4,rabbitsVector.get(i).getUUID());
            preparedStatement.setInt(5,rabbitsVector.get(i).getX());
            preparedStatement.setInt(6,rabbitsVector.get(i).getY());
            preparedStatement.setInt(7,rabbitsVector.get(i).getRoteX());
            preparedStatement.setInt(8,rabbitsVector.get(i).getRoteY());
            preparedStatement.setString(9,rabbitsVector.get(i).getPathToImg());
            preparedStatement.executeUpdate();
        }
    }
    public static void loadRabbitsFromRancho()throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        Vector<Rabbit> rabbitsVector = new Vector<Rabbit>();
        Statement statement = connection.createStatement();
        String sql = "select * from Rancho;";
        ResultSet resultSet = statement.executeQuery(sql);
        while(resultSet.next()){
            RabbitAlbinos rabbitAlbinos;
            int _x = resultSet.getInt("x");
            int _y = resultSet.getInt("y");
            String _pathToImage = resultSet.getString("pathToImage");
            int _birthTime = resultSet.getInt("birthTime");
            int _deathTime = resultSet.getInt("deathTime");
            int _routeX = resultSet.getInt("routeX");
            int _routeY = resultSet.getInt("routeY");
            String _uuid = resultSet.getString("uuid");
            if ("Albinos".equals(resultSet.getString("name"))) {
                rabbitsVector.add(new RabbitAlbinos(_x,_y,_pathToImage,_birthTime,_deathTime, _uuid, _routeX, _routeY));
            } else {
                rabbitsVector.add(new RabbitSimple(_x,_y,_pathToImage,_birthTime,_deathTime, _uuid, _routeX, _routeY));
            }
            RabbitsStorage.getInstance().reset(); //обнуляем счетчики кроликов
            RabbitsStorage.getInstance().setAllRabbits(rabbitsVector);
            Vector<Rabbit> rabbitsList = RabbitsStorage.getInstance().getRabbitVector();

            if (!rabbitsList.isEmpty()) {
                for (int i = 0; i < rabbitsList.size(); i++) {
                    Rabbit rabbit = rabbitsList.get(i);
                    int deathTime = rabbit.getDeathTime() - rabbit.getBirthTime();
                    rabbit.setBirthTime(Habitat.getTime());
                    rabbit.setDeathTime(rabbit.getBirthTime() + deathTime);
                }
            }
        }
    }
}