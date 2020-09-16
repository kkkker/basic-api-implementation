package com.thoughtworks.rslist.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class User {

    @NotEmpty
    @Size(max = 8)
    private String userName;
    private int age;

    @NotEmpty
    private String gender;
    private String email;
    private long phone;

    public User(String userName, int age, String gender, String email, long phone) {
        this.userName = userName;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
    }
}
