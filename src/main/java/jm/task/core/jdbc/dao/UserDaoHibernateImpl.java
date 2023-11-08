package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;


public class UserDaoHibernateImpl implements UserDao {
    private final String tableExistsQuery = "SHOW TABLE STATUS FROM katadb where NAME='users'";
    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        String query = "CREATE TABLE users " +
                "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                "name VARCHAR(200), " +
                "lastname VARCHAR(200), " +
                "age TINYINT)";
        try (Session session = Util.getSession()) {
            Transaction tx = session.beginTransaction();
            if (!session.createNativeQuery(tableExistsQuery).list().isEmpty()){
                return;
            }
            session.createNativeQuery(query).executeUpdate();
            tx.commit();
            System.out.println("Table users created");
        } catch (PersistenceException e){
            System.out.println(e);
        }
    }

    @Override
    public void dropUsersTable() {
        String query = "DROP TABLE users";
        try (Session session = Util.getSession()) {
            Transaction tx = session.beginTransaction();
            if (session.createNativeQuery(tableExistsQuery).list().isEmpty()){
                return;
            }
            session.createNativeQuery(query).executeUpdate();
            tx.commit();
            System.out.println("Table users dropped");
        } catch (PersistenceException e){
            System.out.println(e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = Util.getSession()) {
            Transaction tx = session.beginTransaction();
            session.save(new User(name, lastName, age));
            tx.commit();
            System.out.printf("User: %s %s saved%n", name, lastName);
        } catch (PersistenceException e){
            System.out.println(e);
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = Util.getSession()) {
            Transaction tx = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user == null){
                return;
            }
            session.remove(user);
            tx.commit();
            System.out.printf("User with ID %d removed%n", id);
        } catch (PersistenceException e){
            System.out.println(e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userArrayList = new ArrayList<>();
        try (Session session = Util.getSession()) {
            Transaction tx = session.beginTransaction();
            userArrayList = session.createQuery("From User").list();
            tx.commit();
        } catch (PersistenceException e){
            System.out.println(e);
        }
        return userArrayList;
    }

    @Override
    public void cleanUsersTable() {
        String query = "TRUNCATE TABLE users";
        try (Session session = Util.getSession()) {
            Transaction tx = session.beginTransaction();
            if (session.createNativeQuery(tableExistsQuery).list().isEmpty()){
                return;
            }
            session.createNativeQuery(query).executeUpdate();
            tx.commit();
            System.out.println("Table users cleaned");
        } catch (PersistenceException e){
            System.out.println(e);
        }
    }
}
