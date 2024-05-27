package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientService {
    Connection connection = Database.getInstance().getConnection();

    private final static String GET_ALL = """
            SELECT * FROM public.client
            ORDER BY id ASC
            """;
    private final static String GET_BY_ID = """
            SELECT name FROM public.client
            WHERE id = ?
            """;
    private final static String ADD_NEW_CLIENT = """
            INSERT INTO client(name)
            	VALUES (?);
            """;
    private final static String CHANGE_CLIENT_NAME = """
            UPDATE client
            	SET name = ?
            	WHERE id = ?;
            """;
    private final static String DELETE_BY_ID = """
            DELETE FROM client
            	WHERE id = ?;
            """;

    public ClientService() throws SQLException {
    }

    public long create(String name){
        if (name.length() >1000){
            throw new IllegalArgumentException();
        }
        long result = 0L;
        try {
            PreparedStatement statement = connection.prepareStatement(ADD_NEW_CLIENT, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, name);
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            while (generatedKeys.next()) {
                result = generatedKeys.getLong("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
    public String getById(long id){
        String nameById = "";
        try {
            PreparedStatement statement = connection.prepareStatement(GET_BY_ID);
            statement.setLong(1, id);
            statement.execute();
            ResultSet result = statement.getResultSet();
            while (result.next()) {
                nameById = result.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nameById ;
    }
    public void setName(long id, String name){
        if (name.length() >1000){
            throw new IllegalArgumentException();
        }
        try {
            PreparedStatement statement = connection.prepareStatement(CHANGE_CLIENT_NAME);
            statement.setString(1, name);
            statement.setLong(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteById(long id){
        try {
            PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID);
            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<Client> listAll(){
        List<Client> allClient = new ArrayList<Client>();
        try {
            PreparedStatement statement = connection.prepareStatement(GET_ALL);
            statement.execute();
            ResultSet result = statement.getResultSet();
            while (result.next()){
                allClient.add(new Client(
                result.getLong("id"),
                result.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allClient;
    }
}
