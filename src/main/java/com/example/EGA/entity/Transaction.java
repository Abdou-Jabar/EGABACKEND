package com.example.EGA.entity;

import com.example.EGA.model.TypeTransaction;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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
