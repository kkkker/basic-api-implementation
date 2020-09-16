package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    public static List<User> userList = new ArrayList<>();

    @PostMapping("/rs/add/user")
    public void registerUser(@RequestBody User user) {
        userList.add(user);
    }
}
