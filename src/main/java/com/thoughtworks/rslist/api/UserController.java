package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.User;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exception.ExceptionMessage;
import com.thoughtworks.rslist.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class UserController {

    public static List<User> userList = new ArrayList<>();

    @Autowired
    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> userList = userRepository.findAll().stream()
                .map(userEntity -> new User(userEntity.getUserName(),
                        userEntity.getAge(),
                        userEntity.getGender(),
                        userEntity.getEmail(),
                        userEntity.getPhone()))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(userList);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);
        if (!optionalUserEntity.isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        UserEntity userEntity = optionalUserEntity.get();
        User user = new User(userEntity.getUserName(),
                userEntity.getAge(),
                userEntity.getGender(),
                userEntity.getEmail(),
                userEntity.getPhone());
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/user/register")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody User user) {
        UserEntity userEntity = UserEntity.builder()
                .userName(user.getUserName())
                .age(user.getAge())
                .gender(user.getGender())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
        userRepository.save(userEntity);
        userList.add(user);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ExceptionMessage> handleException(Exception ex) {
        ExceptionMessage exceptionMessage = new ExceptionMessage();
        Logger logger = LoggerFactory.getLogger(UserController.class);
        if (ex instanceof MethodArgumentNotValidException) {
            exceptionMessage.setError("invalid user");
            logger.error(exceptionMessage.getError());
            return ResponseEntity.status(400).body(exceptionMessage);
        }
        return ResponseEntity.status(400).body(null);
    }
}
