package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();
        userService.createUsersTable();
        User user = new User("Eugene", "Dolgov", (byte) 42);
        userService.saveUser(user.getName(), user.getLastName(), user.getAge());

        List<User> a = userService.getAllUsers();
        a.forEach(System.out::println);
        userService.removeUserById(1);
        userService.cleanUsersTable();

    }
}
