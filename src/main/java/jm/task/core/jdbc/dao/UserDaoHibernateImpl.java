package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;


public class UserDaoHibernateImpl implements UserDao {
    private final String tableExistsQuery = "SHOW TABLE STATUS FROM katadb WHERE name='users'";

    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        Transaction tx = null;
        String query = "CREATE TABLE users " +
                "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                "name VARCHAR(200), " +
                "lastname VARCHAR(200), " +
                "age TINYINT)";
        try (Session session = Util.getSession()) {
            if (!session.createNativeQuery(tableExistsQuery).list().isEmpty()) {
                return;
            }
            tx = session.beginTransaction();
            session.createNativeQuery(query).executeUpdate();
            tx.commit();
            System.out.println("Table users created");
        } catch (PersistenceException e) {
            System.out.println(e);
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    @Override
    public void dropUsersTable() {
        Transaction tx = null;
        String query = "DROP TABLE users";
        try (Session session = Util.getSession()) {
            if (session.createNativeQuery(tableExistsQuery).list().isEmpty()) {
                return;
            }
            tx = session.beginTransaction();
            session.createNativeQuery(query).executeUpdate();
            tx.commit();
            System.out.println("Table users dropped");
        } catch (PersistenceException e) {
            System.out.println(e);
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction tx = null;
        try (Session session = Util.getSession()) {
            tx = session.beginTransaction();
            session.save(new User(name, lastName, age));
            tx.commit();
            System.out.printf("User: %s %s saved%n", name, lastName);
        } catch (PersistenceException e) {
            System.out.println(e);
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction tx = null;
        try (Session session = Util.getSession()) {
            tx = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user == null) {
                return;
            }
            session.remove(user);
            tx.commit();
            System.out.printf("User with ID %d removed%n", id);
        } catch (PersistenceException e) {
            System.out.println(e);
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userArrayList = new ArrayList<>();
        try (Session session = Util.getSession()) {
            Query<User> query = session.createQuery("FROM User", User.class);
            userArrayList = query.list();
        } catch (PersistenceException e) {
            System.out.println(e);
        }
        return userArrayList;
    }

    @Override
    public void cleanUsersTable() {
        Transaction tx = null;
        String query = "DELETE User";
        try (Session session = Util.getSession()) {
            if (session.createNativeQuery(tableExistsQuery).list().isEmpty()) {
                return;
            }
            tx = session.beginTransaction();
            session.createQuery(query).executeUpdate();
            tx.commit();
            System.out.println("Table users cleaned");
        } catch (PersistenceException e) {
            System.out.println(e);
            if (tx != null) {
                tx.rollback();
            }
        }
    }
}
