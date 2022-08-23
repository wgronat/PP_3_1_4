package ru.kata.spring.boot_security.demo.service;

import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


    /*
          я не понимаю, что не так...  у Трегулова, на туториал сайтах  и тд. используется просто метод save
          как я понял, если id существует, этот метод не создает новую сущность, а вносит изменения текущую

          попробовал сделать как на этом сайте https://www.baeldung.com/spring-data-crud-repository-save

          на вид все работает одинаково...

          When we use findById() to retrieve an entity within a transactional method, the returned entity is managed by the persistence provider.
          So, any change to that entity will be automatically persisted in the database, regardless of whether we are invoking the save() method.
    * */

    @Override
    @Transactional
    public void updateUser(User user) {

        User updatedUser = userRepository.findById(user.getId()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        updatedUser.setFirstName(user.getFirstName());
        updatedUser.setLastName(user.getLastName());
        updatedUser.setAge(user.getAge());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setPassword(passwordEncoder.encode(user.getPassword()));
        updatedUser.setRoles(user.getRoles());

    }

//    @Override
//    @Transactional
//    public void updateUser(User user) {
//        if (!user.getPassword().equals(userRepository.findUserByEmail(user.getEmail()).getPassword())) {
//            user.setPassword(passwordEncoder.encode(user.getPassword()));
//        }
//        userRepository.save(user);
//    }

    @Override
    public User findUserByID(long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void delete(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findUserByEmail(String userEmail) {
        return userRepository.findUserByEmail(userEmail);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email);
        if (email == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}