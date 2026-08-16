package com.lavajato.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class SenhaUtil {
    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String criptografar(String senha) {
        return encoder.encode(senha);
    }

    public static boolean validarSenha(String senha, String senhaHash) {
        return encoder.matches(senha, senhaHash);
    }
}
