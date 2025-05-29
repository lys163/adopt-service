package com.adopt.adopt_service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "animal_detail")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnimalDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long detailId;

    @OneToOne
    @JoinColumn(name = "animal_id", unique = true)
    private Animal animal;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
}
