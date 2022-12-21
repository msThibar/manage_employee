package com.example.demo.models;

import com.example.demo.Enum.Gender;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String username;
    private String password;
    private String email;
    private String birth;
    private String phone;
    private String code;

    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name="role_id", nullable = true) //dat ten khac cho khoa ngoai
    private Role role;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.REMOVE)
    private Set<Timesheet> set= new HashSet<>();

    @Enumerated(EnumType.STRING)
    private Gender gender;

    public User(String name, String username, String password, String email, String birth, Role role, String phone, Gender gender) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.birth = birth;
        this.role = role;
        this.phone= phone;
        this.gender= gender;
    }
    public User(int id){
        this.id= id;
    }
}
