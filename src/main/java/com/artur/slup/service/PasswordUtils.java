package com.artur.slup.service;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/** Utilitarios de senha usando recursos que ja fazem parte do Java. */
public final class PasswordUtils {

    private static final int ITERACOES = 120000;
    private static final int TAMANHO_CHAVE = 256;
    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordUtils() {
    }

    public static String gerarHash(String senha) {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        byte[] hash = calcularHash(senha, salt, ITERACOES);
        return "pbkdf2$" + ITERACOES + "$"
                + Base64.getEncoder().encodeToString(salt) + "$"
                + Base64.getEncoder().encodeToString(hash);
    }

    public static boolean verificar(String senhaDigitada, String senhaArmazenada) {
        if (senhaDigitada == null || senhaArmazenada == null) {
            return false;
        }

        // Compatibilidade temporaria com as contas antigas da carga inicial.
        if (!senhaArmazenada.startsWith("pbkdf2$")) {
            return senhaDigitada.equals(senhaArmazenada);
        }

        String[] partes = senhaArmazenada.split("\\$");
        if (partes.length != 4) {
            return false;
        }

        try {
            int iteracoes = Integer.parseInt(partes[1]);
            byte[] salt = Base64.getDecoder().decode(partes[2]);
            byte[] hashSalvo = Base64.getDecoder().decode(partes[3]);
            byte[] hashDigitado = calcularHash(senhaDigitada, salt, iteracoes);
            return MessageDigest.isEqual(hashSalvo, hashDigitado);
        } catch (IllegalArgumentException erro) {
            return false;
        }
    }

    public static boolean jaEstaEmHash(String senha) {
        return senha != null && senha.startsWith("pbkdf2$");
    }

    private static byte[] calcularHash(String senha, byte[] salt, int iteracoes) {
        PBEKeySpec especificacao = new PBEKeySpec(
                senha.toCharArray(), salt, iteracoes, TAMANHO_CHAVE);
        try {
            SecretKeyFactory fabrica = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return fabrica.generateSecret(especificacao).getEncoded();
        } catch (Exception erro) {
            throw new IllegalStateException("Nao foi possivel proteger a senha.", erro);
        } finally {
            especificacao.clearPassword();
        }
    }
}
