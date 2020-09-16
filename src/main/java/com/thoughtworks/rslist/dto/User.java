package com.thoughtworks.rslist.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class User {

    @NotEmpty
    @Size(max = 8)
    private String userName;

    @NotNull
    @Max(100)
    @Min(18)
    private Integer age;

    @NotEmpty
    private String gender;

    @Email
    private String email;

    @NotEmpty
    @Pattern(regexp = "^1(\\d){10}$")
    private String phone;

    public User(String userName, Integer age, String gender, String email, String phone) {
        this.userName = userName;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
    }
}
