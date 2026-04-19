package com.example.EGA.dto;

import com.example.EGA.model.AdminType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private String token;
    private String username;
    private String nom;
    private String prenom;
    private AdminType role;
    private String email;
    private String numero;
}
