package funcionario;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class GeradorCredenciais {
    private static final String MAIUSCULAS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String MINUSCULAS = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMEROS = "0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private GeradorCredenciais() {}

    public static String gerarSenha() {
        List<Character> caracteres = new ArrayList<>();
        caracteres.add(escolher(MAIUSCULAS));
        caracteres.add(escolher(MINUSCULAS));
        caracteres.add(escolher(NUMEROS));
        String todos = MAIUSCULAS + MINUSCULAS + NUMEROS;
        while (caracteres.size() < 10) caracteres.add(escolher(todos));
        Collections.shuffle(caracteres, RANDOM);
        StringBuilder senha = new StringBuilder();
        for (char caractere : caracteres) senha.append(caractere);
        return senha.toString();
    }

    private static char escolher(String opcoes) {
        return opcoes.charAt(RANDOM.nextInt(opcoes.length()));
    }
}
