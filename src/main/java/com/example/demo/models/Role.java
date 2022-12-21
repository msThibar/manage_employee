package com.example.demo.models;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @OneToMany(fetch= FetchType.LAZY, mappedBy= "role") //mapped: anh xa den thuoc tinh trong class: User
    private Set<User> userList= new HashSet<>();

    public Role(String name) {
        this.name = name;
    }

    public Role(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Role(int id) {
        this.id = id;
    }
}
