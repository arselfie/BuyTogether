package com.project.demo.services;

import com.project.demo.entity.EntityStatus;
import com.project.demo.entity.token.TokenType;
import com.project.demo.entity.user.User;
import com.project.demo.entity.user.UserStatus;
import com.project.demo.exceptions.ValidationException;
import com.project.demo.repository.UserRepository;
import com.project.demo.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private CommonService commonService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    AuthenticationManager authenticationManager;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostConstruct
    public void init() {
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    public List<User> find() {
        return userRepository.findAll();
    }

    public User find(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User findByLogin(String login) {

        return userRepository.findByLogin(login);
    }

    public void updatePassword(User user) {
        String hashPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        userRepository.save(user);
    }

    public User createUser(User user) {
        if (user == null) {
            throw new ValidationException("User is null");
        }
        user.setEntityStatus(EntityStatus.ACTIVE);
        user.setUserStatus(UserStatus.NEW);

        commonService.validate(user);

        String hashPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);

        return userRepository.save(user);
    }

    public User updateUser(Long id, User user) {
        User existedUser = find(id);

        if (existedUser == null) {
            throw new ValidationException("User not found in database");
        }

        commonService.validate(user);
        existedUser.setName(user.getName());
        existedUser.setEmail(user.getEmail());
        userRepository.save(existedUser);

        return existedUser;
    }

    public User changeStatus(Long id, EntityStatus entityStatus) {

        User existedUser = find(id);

        if (existedUser == null) {
            throw new ValidationException("User not found in database");
        }

        existedUser.setEntityStatus(entityStatus);

        userRepository.save(existedUser);

        return existedUser;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public String generateActivationToken(User user) {
        return jwtTokenProvider.createToken(user.getLogin(), TokenType.USER_ACTIVATION);
    }

    public void activateUser(String token) {
        Pair<String, TokenType> tokenPair = jwtTokenProvider.getLogin(token);
        User user = userRepository.findByLogin(tokenPair.getFirst());
        if (tokenPair.getSecond() != TokenType.USER_ACTIVATION) {
            throw new ValidationException("Wrong token type");
        }
        if (user == null) {
            throw new ValidationException("User not found in DB by login");
        }
        if (user.getEntityStatus() == EntityStatus.DELETED) {
            throw new ValidationException("User was deleted from DB");
        }

        if (user.getUserStatus() != UserStatus.NEW) {
            throw new ValidationException("User already activated");
        }

        user.setUserStatus(UserStatus.ACTIVE);

        userRepository.save(user);
    }

    public String login(String username, String password) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        authenticationManager.authenticate(authenticationToken);

        User user = findByLogin(username);
        if (user == null) {
            throw new ValidationException("User not found");
        }
        if (user.getEntityStatus() == EntityStatus.DELETED) {
            throw new ValidationException("User was deleted from DB");
        }

        if (user.getUserStatus() != UserStatus.ACTIVE) {
            throw new ValidationException("User not active");
        }

        return jwtTokenProvider.createToken(username, TokenType.USER_AUTHENTICATION);
    }


}
