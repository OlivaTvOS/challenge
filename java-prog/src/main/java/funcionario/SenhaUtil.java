package funcionario;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public final class SenhaUtil {
    private SenhaUtil() {}

    public static String gerarHash(String senha) {
        // SHA-256 evita salvar a senha pura nesta demonstração. Em produção seria
        // necessário usar um algoritmo próprio para senhas, como bcrypt ou Argon2.
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(senha.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 não disponível", e);
        }
    }
}
