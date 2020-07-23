package com.project.demo.services;

import com.project.demo.entity.EntityStatus;
import com.project.demo.entity.user.User;
import com.project.demo.entity.user.UserStatus;
import com.project.demo.exceptions.ValidationException;
import com.project.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private CommonService commonService;


    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init(){
        bCryptPasswordEncoder= new BCryptPasswordEncoder();
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

    public void updatePassword(User user){
        String hashPassword=bCryptPasswordEncoder.encode(user.getPassword());
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

        String hashPassword=bCryptPasswordEncoder.encode(user.getPassword());
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
}
