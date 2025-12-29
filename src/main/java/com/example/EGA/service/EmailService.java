package com.example.EGA.service;

import com.example.EGA.entity.Compte;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void envoyerCreationCompte(String to, String nom, String iban) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("EGABank <egabank@gmail.com>");
        message.setTo(to);
        message.setSubject("Création de votre compte bancaire");
        message.setText(
                "Bonjour " + nom + ",\n\n" +
                        "Votre compte a été créé avec succès.\n" +
                        "IBAN : " + iban + "\n\n" +
                        "Merci de votre confiance."
        );
        mailSender.send(message);
    }

    @Async
    public void envoyerDepot(Compte compte, Double montant) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("EGABank <egabank@gmail.com>");
        message.setTo(compte.getClient().getEmail());
        message.setSubject("Dépôt effectué sur votre compte");
        message.setText(
                "Bonjour " + compte.getClient().getPrenom() + ",\n\n" +
                        "Un dépôt de " + montant + " FCFA a été effectué sur votre compte.\n" +
                        "Numéro de compte : " + compte.getId() + "\n" +
                        "Nouveau solde : " + compte.getSolde() + " FCFA\n\n" +
                        "Merci de votre confiance.\n" +
                        "EGA Banque"
        );

        mailSender.send(message);
    }

    @Async
    public void envoyerRetrait(Compte compte, Double montant) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("EGABank <egabank@gmail.com>");
        message.setTo(compte.getClient().getEmail());
        message.setSubject("Retrait effectué sur votre compte");
        message.setText(
                "Bonjour " + compte.getClient().getPrenom() + ",\n\n" +
                        "Un retrait de " + montant + " FCFA a été effectué sur votre compte.\n" +
                        "Numéro de compte : " + compte.getId() + "\n" +
                        "Nouveau solde : " + compte.getSolde() + " FCFA\n\n" +
                        "Si vous n'êtes pas à l'origine de cette opération, contactez-nous immédiatement.\n\n" +
                        "EGA Banque"
        );
        mailSender.send(message);
    }

    @Async
    public void envoyerVirement(Compte source, Compte dest, Double montant) {

        // Mail pour l'émetteur
        SimpleMailMessage msgSource = new SimpleMailMessage();
        msgSource.setFrom("EGABank <egabank@gmail.com>");
        msgSource.setTo(source.getClient().getEmail());
        msgSource.setSubject("Virement effectué");

        msgSource.setText(
                "Bonjour " + source.getClient().getPrenom() + ",\n\n" +
                        "Vous avez effectué un virement de " + montant + " FCFA.\n" +
                        "De votre compte : " + source.getId() + "\n" +
                        "Vers le compte : " + dest.getId() + "\n" +
                        "Nouveau solde : " + source.getSolde() + " FCFA\n\n" +
                        "Cordialement,\nEGABank"
        );

        mailSender.send(msgSource);

        // Mail pour le bénéficiaire
        SimpleMailMessage msgDest = new SimpleMailMessage();
        msgDest.setFrom("EGABank <egabank@gmail.com>");
        msgDest.setTo(dest.getClient().getEmail());
        msgDest.setSubject("Virement reçu");

        msgDest.setText(
                "Bonjour " + dest.getClient().getPrenom() + ",\n\n" +
                        "Vous avez reçu un virement de " + montant + " FCFA.\n" +
                        "Compte : " + dest.getId() + "\n" +
                        "Nouveau solde : " + dest.getSolde() + " FCFA\n\n" +
                        "Cordialement,\nEGABank"
        );

        mailSender.send(msgDest);
    }
}

