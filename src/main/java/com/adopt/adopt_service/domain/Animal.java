package com.adopt.adopt_service.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "animal")
@Builder
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "animal_id")
    private Long animalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String name;

    private String age;

    private Float kg;

    @Enumerated(EnumType.STRING)
    private Size size;

    @Enumerated(EnumType.STRING)
    private Type type;
    
    private int view;

    private int likes;

    private String createTime;

    @OneToOne(mappedBy="animal", cascade = CascadeType.ALL)
    @JsonManagedReference
    private AnimalState animalState;

    @OneToOne(mappedBy="animal", cascade=CascadeType.ALL)
    private AnimalDetail animalDetail;

    @Builder.Default
    @OneToMany(mappedBy="animal", cascade=CascadeType.ALL)
    private List<AnimalImage> images = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL)
    private List<AnimalOrder> orders = new ArrayList<>();

    @Builder.Default
    @ManyToMany
    @JoinTable(
        name="animal_personality",
        joinColumns = @JoinColumn(name="animal_id"),
        inverseJoinColumns = @JoinColumn(name="personality_id")
    )
    private Set<Personality> personalities = new HashSet<>();
    
}
