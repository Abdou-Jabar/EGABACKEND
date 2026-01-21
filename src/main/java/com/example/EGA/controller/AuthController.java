package com.example.EGA.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.EGA.dto.JwtResponse;
import com.example.EGA.entity.User;
import com.example.EGA.model.AdminType;
import com.example.EGA.repository.UserRepository;
import com.example.EGA.security.JwtUtil;
import com.example.EGA.service.EmailService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtil jwtUtils;
    @Autowired
    EmailService emailService;
    /*@PostMapping("/signin")
    public String authenticateUser(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return jwtUtils.generateToken(userDetails.getUsername());
    }*/


    @PostMapping("/signin")
    public JwtResponse authenticateUser(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );
        String jwt = jwtUtils.generateToken(user.getUsername());
        User userDetails = userRepository.findByUsername(user.getUsername());
        return new JwtResponse(
                jwt,
                userDetails.getUsername(),
                userDetails.getNom(), // Assure-toi que ces getters existent dans ton entité User
                userDetails.getPrenom(),
                userDetails.getRole()
        );
    }

    @PostMapping("/signup")
    public String registerUser(@RequestBody User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return "Erreur: Ce nom d'utilisateur est déjà pris !";
        }
        String passwordClair = generateRandomPassword();

        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(encoder.encode(passwordClair));
        newUser.setNom(user.getNom());
        newUser.setPrenom(user.getPrenom());
        newUser.setEmail(user.getEmail());
        newUser.setNumero(user.getNumero());

        if (user.getRole() != null) {
            newUser.setRole(user.getRole());
        } else {
            newUser.setRole(AdminType.ADMIN);
        }

        userRepository.save(newUser);

        // 2. Envoi de l'email avec le mot de passe généré
        emailService.envoyerEmailBienvenueAdmin(
                newUser.getEmail(),
                newUser.getPrenom(),
                newUser.getUsername(),
                passwordClair, // Le mot de passe clair généré
                newUser.getRole().toString()
        );

        return "Compte " + newUser.getRole() + " créé avec succès.";
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateAdmin(@PathVariable Long id, @RequestBody User adminDetails) {
        return userRepository.findById(id).map(user -> {
            user.setNom(adminDetails.getNom());
            user.setPrenom(adminDetails.getPrenom());
            user.setEmail(adminDetails.getEmail());
            user.setNumero(adminDetails.getNumero());
            user.setUsername(adminDetails.getUsername());
            user.setRole(adminDetails.getRole());
            userRepository.save(user);
            return ResponseEntity.ok("Administrateur mis à jour avec succès !");
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update-password/{id}")
    public ResponseEntity<String> updatePassword(@PathVariable Long id) {
        return userRepository.findById(id).map(user -> {
            String generatedPassword = generateRandomPassword();

            user.setPassword(encoder.encode(generatedPassword));
            userRepository.save(user);

            emailService.envoyerEmailResetPassword(
                    user.getEmail(),
                    user.getPrenom(),
                    generatedPassword
            );

            return ResponseEntity.ok("Mot de passe réinitialisé avec succès !");
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteAdmin(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Erreur : Administrateur introuvable avec l'ID : " + id);
        }
        try {
            userRepository.deleteById(id);
            return ResponseEntity.ok("L'administrateur a été supprimé avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la suppression : " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllAdmins() {
        List<User> admins = userRepository.findAll();

        if (admins.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(admins);
    }

    private String generateRandomPassword() {
    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$";
    java.security.SecureRandom random = new java.security.SecureRandom();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 10; i++) {
        sb.append(chars.charAt(random.nextInt(chars.length())));
    }
    return sb.toString();
}
}