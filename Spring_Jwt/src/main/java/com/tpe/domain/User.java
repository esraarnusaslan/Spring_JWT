package com.tpe.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "t_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 25,nullable = false)
    private String firstName;

    @Column(length = 25,nullable = false)
    private String lastName;

    @Column(length = 25,nullable = false,unique = true)
    private String userName;

    @Column(length = 255,nullable = false)//password DB ye kaydedilmeden önce şifreleneceği için
    private String password;//karmaşık ve uzun olacak

    //role
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"),
                                   inverseJoinColumns = @JoinColumn(name = "role_id"))//opsiyonel
    private Set<Role> roles=new HashSet<>();


    public User(String firstName, String lastName, String userName, String password, Set<Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.roles = roles;
    }
}
