package com.adopt.adopt_service.domain;



import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "personality")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Personality {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long personalityId;

    @Column(nullable = false,unique = true)
    private String name;

    @Builder.Default
    @ManyToMany(mappedBy = "personalities")
    private Set<Animal> animals = new HashSet<>();
}
