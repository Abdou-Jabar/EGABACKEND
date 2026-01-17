package com.example.EGA.entity;

import java.time.LocalDateTime;

import com.example.EGA.model.TypeTransaction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "transaction")
@Setter
@Getter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "Le montant est obligatoire")
    @Column(name = "montant")
    private Double montant;

    @NotNull
    @Column(name = "date_transaction",  nullable = false, updatable = false)
    private LocalDateTime dateTransaction;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, updatable = false)
    private TypeTransaction type;

    @ManyToOne
    @JoinColumn(name = "compte_source_id", updatable = false, nullable = false)
    private Compte compteSource;

    @ManyToOne
    @JoinColumn(name = "compte_destination_id", updatable = false, nullable = false)
    private Compte compteDestination;
}