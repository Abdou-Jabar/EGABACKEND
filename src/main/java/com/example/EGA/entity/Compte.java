package com.example.EGA.entity;

import com.example.EGA.model.Type;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "compte")
public class Compte {
    @Id
    @Column(length = 34)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Column(name = "est_supprime", nullable = false)
    private boolean estSupprime;

    @Column(name = "solde", nullable = false)
    private Double solde = 0.0;

    @PrePersist
    protected void prePersist() {
        this.estSupprime = false;
        this.dateCreation = LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    @JsonIgnoreProperties({"comptes", "autreChampASupprimer"})
    private Client client;

}