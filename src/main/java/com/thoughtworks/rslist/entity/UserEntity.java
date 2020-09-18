package com.thoughtworks.rslist.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "name")
    private String userName;

    private int age;

    private String gender;

    private String email;

    private String phone;

    private int votes;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.REMOVE)
    private List<RsEventEntity> rsEventEntityList;

    @OneToMany(mappedBy = "userId", cascade = CascadeType.REMOVE)
    private List<VoteEntity> voteEntityList;

    public UserEntity(Integer id, String userName, int age, String gender, String email, String phone) {
        this.id = id;
        this.userName = userName;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
    }
}
