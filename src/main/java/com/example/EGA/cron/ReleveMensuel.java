package com.example.EGA.cron;

import com.example.EGA.entity.Client;
import com.example.EGA.entity.Compte;
import com.example.EGA.entity.Transaction;
import org.springframework.scheduling.annotation.Scheduled;
import com.example.EGA.repository.ClientRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReleveMensuel {


    /*@Scheduled(cron = "0 0 0 L * ?")
    public void envoyerReleve() {

        // 1. Définir la période (mois dernier)
        LocalDateTime fin = LocalDateTime.now();
        LocalDateTime debut = fin.withDayOfMonth(1).withHour(0).withMinute(0);



        List<Client> clients = clientRepository.findByEstSupprimeFalse();

        for (Client client : clients) {
            // 3. Créer une liste pour stocker tous les PDFs du client
            Map<String, byte[]> piecesJointes = new HashMap<>();

            // 4. Parcourir tous les comptes de ce client
            for (Compte compte : client.getComptes()) {
                if (!compte.isEstSupprime()) {
                    // Récupérer les transactions
                    List<Transaction> transactions = transactionService.obtenirReleveParPeriode(compte.getId(), debut, fin);

                    // Générer le PDF en bytes
                    byte[] pdf = pdfGeneratorService.generatePdfBytes(transactions, compte.getId(), debut, fin);

                    // Ajouter à la liste des pièces jointes (Nom du fichier -> Contenu)
                    piecesJointes.put("releve_" + compte.getId() + ".pdf", pdf);
                }
            }

            // 5. Envoyer l'unique email s'il y a au moins un compte
            if (!piecesJointes.isEmpty() && client.getEmail() != null) {
                emailService.envoyerEmailMultiplePieces(
                        client.getEmail(),
                        client.getPrenom(),
                        piecesJointes
                );
            }
        }
    }*/
}
