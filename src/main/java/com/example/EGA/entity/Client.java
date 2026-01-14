package com.example.EGA.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.example.EGA.model.Sexe;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long id;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "prenom", nullable = false)
    private String prenom;

    @Column(name = "date_naissance", nullable = false)
    private LocalDate dateNaissance;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexe", nullable = false)
    private Sexe sexe;

    @Column(name = "telephone", nullable = false)
    @Pattern(regexp = "^(?:\\+228)?\\s?\\d{8}$", message = "Numéro de téléphone invalide")
    private String telephone;

    @Column(name = "email", nullable = false, unique = true)
    @Email
    private String email;

    @Column(name = "nationalite", nullable = false)
    private String nationalite;

    @Column(name = "est_supprime", nullable = false)
    private Boolean estSupprime;

    @Column(name = "date_inscription", nullable = false, updatable = false)
    private LocalDateTime dateInscription;

    @PrePersist
    public void prePersist() {
        this.estSupprime = false;
        this.dateInscription = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("client")
    private List<Compte> comptes;
}
