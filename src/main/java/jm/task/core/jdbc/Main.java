package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();
        userService.createUsersTable();
        User user0 = new User("Alex", "Alexov", (byte) 10);
        User user1 = new User("Max", "Maxin", (byte) 20);
        User user2 = new User("Maria", "Marieva", (byte) 30);
        User user3 = new User("Anna", "Annenko", (byte) 40);
        List<User> userList = List.of(user0, user1, user2, user3);
        for (User user : userList){
            userService.saveUser(user.getName(), user.getLastName(), user.getAge());
        }
        List<User> a = userService.getAllUsers();
        a.forEach(System.out::println);
        userService.removeUserById(1);
        userService.cleanUsersTable();
        userService.dropUsersTable();

    }
}
