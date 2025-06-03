package com.adopt.adopt_service.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@Table(name="animal_state")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnimalState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long animalStateId;

    @OneToOne
    @JoinColumn(name = "animal_id")
    @JsonIgnore
    private Animal animal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdoptState adoptState;
}
