package com.example.EGA.service;

import com.example.EGA.entity.Compte;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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

    @Async
    public void envoyerReleveParEmail(String toEmail, String clientName, String numeroCompte, byte[] pdfContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("EGABank <egabank@gmail.com>");
        helper.setTo(toEmail);
        helper.setSubject("Votre Relevé de Compte EGA BANK - " + numeroCompte);

        String contenu = "Bonjour " + clientName + ",\n\n" +
                "Veuillez trouver ci-joint l'historique de vos opérations pour le compte : " + numeroCompte + ".\n\n" +
                "Merci de votre confiance.\n\n" +
                "L'équipe EGA BANK.";

        helper.setText(contenu);

        helper.addAttachment("releve_" + numeroCompte + ".pdf", new ByteArrayResource(pdfContent));

        mailSender.send(message);
    }

    @Async
    public void envoyerEmailBienvenueAdmin(String to, String prenom, String username, String password, String role) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("EGABank <egabank@gmail.com>");
        message.setTo(to);
        message.setSubject("Accès Administratif EGABank - Vos identifiants");

        String roleLabel = role.replace("_", " "); // Pour un affichage plus propre (ex: SUPER ADMIN)

        message.setText(
                "Bonjour " + prenom + ",\n\n" +
                        "Un compte administrateur vous a été créé sur la plateforme EGABank.\n\n" +
                        "Voici vos accès confidentiels :\n" +
                        "----------------------------------\n" +
                        "Rôle : " + roleLabel + "\n" +
                        "Nom d'utilisateur : " + username + "\n" +
                        "Mot de passe provisoire : " + password + "\n" +
                        "----------------------------------\n\n" +
                        "Pour des raisons de sécurité, nous vous conseillons de changer votre mot de passe dès votre première connexion.\n\n" +
                        "Cordialement,\n" +
                        "La Direction Technique EGA"
        );
        mailSender.send(message);
    }

    @Async
    public void envoyerEmailResetPassword(String to, String prenom, String newPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("EGABank <egabank@gmail.com>");
        message.setTo(to);
        message.setSubject("Réinitialisation de votre mot de passe - EGABank");

        message.setText(
                "Bonjour " + prenom + ",\n\n" +
                        "Votre mot de passe a été réinitialisé par un administrateur système.\n\n" +
                        "Votre nouveau mot de passe est : " + newPassword + "\n\n" +
                        "Si vous n'êtes pas à l'origine de cette demande, veuillez contacter immédiatement le support.\n\n" +
                        "Cordialement,\n" +
                        "EGABank"
        );
        mailSender.send(message);
    }
}

