package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.User;
import com.thoughtworks.rslist.exception.ExceptionMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    public static List<User> userList = new ArrayList<>();

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok().body(userList);
    }

    @PostMapping("/user/register")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody User user) {
        userList.add(user);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ExceptionMessage> handleException(Exception ex) {
        ExceptionMessage exceptionMessage = new ExceptionMessage();
        if (ex instanceof MethodArgumentNotValidException) {
            exceptionMessage.setError("invalid user");
            return ResponseEntity.status(400).body(exceptionMessage);
        }
        return ResponseEntity.status(400).body(null);
    }
}
