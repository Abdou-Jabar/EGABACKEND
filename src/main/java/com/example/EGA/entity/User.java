package com.example.EGA.entity;

import com.example.EGA.model.AdminType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "prenom", nullable = false)
    private String prenom;

    @Column(name = "email", nullable = false)
    @Email
    private String email;

    @Column(name = "username", unique = true, nullable = false)
    @NotBlank
    private String username;

    @Column(name = "numero", nullable = false)
    @Pattern(regexp = "^(?:\\\\+228\\\\s?)?\\\\d{8}$", message = "Numéro de téléphone invalide")
    private String numero;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private AdminType role;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    @PrePersist
    public void prePersit(){
        this.dateCreation = LocalDateTime.now();
    }

}
