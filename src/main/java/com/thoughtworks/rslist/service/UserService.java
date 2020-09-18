package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.dto.User;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userEntity -> new User(userEntity.getUserName(),
                        userEntity.getAge(),
                        userEntity.getGender(),
                        userEntity.getEmail(),
                        userEntity.getPhone()))
                .collect(Collectors.toList());
    }

    public Optional<User> getUserById(int id) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);
        if (!optionalUserEntity.isPresent()) {
            return Optional.empty();
        }
        UserEntity userEntity = optionalUserEntity.get();
         User user = new User(userEntity.getUserName(),
                userEntity.getAge(),
                userEntity.getGender(),
                userEntity.getEmail(),
                userEntity.getPhone());
        return Optional.of(user);
    }

    public boolean registerUser(User user) {
        if (userRepository.existsByUserName(user.getUserName())) {
            return false;
        }
        final int DEFAULT_VOTES = 10;
        UserEntity userEntity = UserEntity.builder()
                .userName(user.getUserName())
                .age(user.getAge())
                .gender(user.getGender())
                .votes(DEFAULT_VOTES)
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
        userRepository.save(userEntity);
        return true;
    }

    public boolean deleteUserById(int id) {
        if (!userRepository.existsById(id)) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }
}
