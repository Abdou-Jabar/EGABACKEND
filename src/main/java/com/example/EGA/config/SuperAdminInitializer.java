package com.example.EGA.config;

import com.example.EGA.entity.User;
import com.example.EGA.model.AdminType;
import com.example.EGA.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class SuperAdminInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(SuperAdminInitializer.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.superadmin.username}")
    private String username;

    @Value("${app.superadmin.password}")
    private String password;

    @Value("${app.superadmin.email}")
    private String email;

    @Value("${app.superadmin.nom}")
    private String nom;

    @Value("${app.superadmin.prenom}")
    private String prenom;

    @Value("${app.superadmin.numero}")
    private String numero;

    @Override
    public void run(String... args) throws Exception {
        boolean hasSuperAdmin = userRepository.findAll().stream()
                .anyMatch(user -> AdminType.SUPER_ADMIN.equals(user.getRole()));

        if (!hasSuperAdmin && !userRepository.existsByUsername(username) && !userRepository.existsByEmail(email)) {
            logger.info("No Super Admin found. Creating default Super Admin user...");

            User superAdmin = new User();
            superAdmin.setUsername(username);
            superAdmin.setNom(nom);
            superAdmin.setPrenom(prenom);
            superAdmin.setEmail(email);
            superAdmin.setNumero(numero);
            superAdmin.setRole(AdminType.SUPER_ADMIN);

            String finalPassword = password;
            if (finalPassword == null || finalPassword.trim().isEmpty()) {
                finalPassword = generateRandomPassword();
                logger.warn("==================================================");
                logger.warn("SUPER ADMIN PASSWORD NOT CONFIGURED!");
                logger.warn("Generated temporary password: {}", finalPassword);
                logger.warn("==================================================");
            }

            superAdmin.setPassword(passwordEncoder.encode(finalPassword));
            userRepository.save(superAdmin);
            logger.info("Super Admin user created successfully with username: {}", username);
        } else {
            logger.info("Super Admin user already exists. Skipping initialization.");
        }
    }

    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$!%&*";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
