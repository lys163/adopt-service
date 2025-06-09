package com.adopt.adopt_service.domain;


import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "users")
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "email",unique = true)
    private String email;

    @Column(name = "pw")
    private String pw;

    @Column(name = "name")
    private String name;

    @Column(name = "pn")
    private String pn;

    @Column(name = "age")
    private int age;

    @Column(name="addr")
    private String addr;

    @Column(name="role")
    private String role;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade= CascadeType.ALL)
    private List<Animal> animals = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade= CascadeType.ALL)
    private List<AnimalOrder> orders = new ArrayList<>();

    public User(String email, String pw, String name, String pn, int age, String addr, String role) {
        this.email = email;
        this.pw = pw;
        this.name = name;
        this.pn = pn;
        this.age = age;
        this.addr = addr;
        this.role = role;
    }
}
