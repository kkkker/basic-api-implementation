package com.thoughtworks.rslist.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class User {

    @NotEmpty
    @Size(max = 8)
    private String userName;

    @NotNull
    @Max(100)
    private Integer age;

    @NotEmpty
    private String gender;
    private String email;
    private long phone;

    public User(String userName, Integer age, String gender, String email, long phone) {
        this.userName = userName;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
    }
}
