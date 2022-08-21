package ru.kata.spring.boot_security.demo.service;

import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User saveUser(User user);

    User findUserByID(long id);

    void delete(long id);

    User findUserByEmail(String userEmail);
}
