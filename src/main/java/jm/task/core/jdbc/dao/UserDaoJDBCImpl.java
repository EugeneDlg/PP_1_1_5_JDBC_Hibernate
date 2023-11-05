package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private Statement stmt;
    private PreparedStatement prepareStmt;
    private ResultSet resultSet;
    private int updateResult;
    private final String tableExistsQuery = "SHOW TABLE STATUS FROM katadb where NAME='users'";
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        String createQuery = "CREATE TABLE users " +
                "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                "name VARCHAR(200), " +
                "lastname VARCHAR(200), " +
                "age TINYINT)";
        try (Connection connection = Util.getConnection()){
            stmt = connection.createStatement();
            resultSet = stmt.executeQuery(tableExistsQuery);
            if (resultSet.next()){
                return;
            }
            updateResult = stmt.executeUpdate(createQuery);
            System.out.println("Table users created");
        } catch (SQLException e){
            System.out.println(e);
        }
    }

    public void dropUsersTable() {
        String query = "DROP TABLE users";
        try (Connection connection = Util.getConnection()){
            stmt = connection.createStatement();
            resultSet = stmt.executeQuery(tableExistsQuery);
            if (!resultSet.next()){
                return;
            }
            updateResult = stmt.executeUpdate(query);
            System.out.println("Table dropped");
        } catch (SQLException e){
            System.out.println(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String query = "INSERT INTO users (name, lastname, age) VALUES (?, ?, ?)";
        try (Connection connection = Util.getConnection()){
            prepareStmt = connection.prepareStatement(query);
            prepareStmt.setString(1, name);
            prepareStmt.setString(2, lastName);
            prepareStmt.setInt(3, age);
            updateResult = prepareStmt.executeUpdate();
            System.out.printf("User: %s %s saved%n", name, lastName);
        } catch (SQLException e){
            System.out.println(e);
        }
    }

    public void removeUserById(long id) {
        String query = "DELETE FROM users WHERE id = ?";
        try (Connection connection = Util.getConnection()){
            prepareStmt = connection.prepareStatement(query);
            prepareStmt.setLong(1, id);
            updateResult = prepareStmt.executeUpdate();
            if (updateResult > 0) {
                System.out.printf("User with ID %d removed%n", id);
            }
        } catch (SQLException e){
            System.out.println(e);
        }
    }

    public List<User> getAllUsers() {
        String query = "SELECT * FROM users";
        ArrayList<User> userArrayList = new ArrayList<>();
        try (Connection connection = Util.getConnection()) {
            stmt = connection.createStatement();
            resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                User user = new User();
                user.setName(resultSet.getString(2));
                user.setLastName(resultSet.getString(3));
                user.setAge(resultSet.getByte(4));
                userArrayList.add(user);
            }
        }catch (SQLException e){
            System.out.println(e);
        }
        return userArrayList;
    }

    public void cleanUsersTable() {
        String query = "TRUNCATE TABLE users";
        try (Connection connection = Util.getConnection()){
            stmt = connection.createStatement();
            resultSet = stmt.executeQuery(tableExistsQuery);
            if (!resultSet.next()){
                return;
            }
            updateResult = stmt.executeUpdate(query);
            System.out.println("Table cleaned");
        } catch (SQLException e){
            System.out.println(e);
        }

    }
}
