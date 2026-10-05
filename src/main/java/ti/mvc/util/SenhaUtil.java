package ti.mvc.util;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class SenhaUtil {

    public static String gerarHash(String senha) {

        if (senha == null || senha.isBlank()) {
            throw new IllegalArgumentException("Digite uma senha.");
        }

        // 1. Cria um valor aleatório para acompanhar a senha
        byte[] salt = new byte[16];
        SecureRandom sorteador = new SecureRandom();
        sorteador.nextBytes(salt);

        // 2. Prepara os dados para calcular o hash
        int repeticoes = 600000;
        char[] letrasDaSenha = senha.toCharArray();

        PBEKeySpec dados = new PBEKeySpec(letrasDaSenha, salt, repeticoes, 256);

        try {
            // 3. Calcula o hash
            SecretKeyFactory algoritmo = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            byte[] hash = algoritmo.generateSecret(dados).getEncoded();

            // 4. Transforma os resultados em texto.
            String saltTexto = Base64.getEncoder().encodeToString(salt);
            String hashTexto = Base64.getEncoder().encodeToString(hash);

            // 5. Devolve o texto que será salvo no banco.
            return repeticoes + ":" + saltTexto + ":" + hashTexto;

        } catch (GeneralSecurityException erro) {
            throw new RuntimeException("Não foi possível gerar o hash.", erro);
        } finally {
            dados.clearPassword();
            java.util.Arrays.fill(letrasDaSenha, '\0');
        }
        
        
    }
    public static boolean verificar(String senha, String hashSalvo) {

        if (senha == null || hashSalvo == null) {
            return false;
        }

        // Separa as três informações que salvamos no banco.
        String[] partes = hashSalvo.split(":");

        if (partes.length != 3) {
            return false;
        }

        PBEKeySpec dados = null;
        char[] letrasDaSenha = senha.toCharArray();

        try {
            int repeticoes = Integer.parseInt(partes[0]);
            byte[] salt = Base64.getDecoder().decode(partes[1]);
            byte[] hashOriginal = Base64.getDecoder().decode(partes[2]);

            // Confere o formato usado pelo nosso gerarHash.
            if (repeticoes != 600000
                    || salt.length != 16 || hashOriginal.length != 32) {
                return false;
            }

            dados = new PBEKeySpec(
                letrasDaSenha, salt, repeticoes, 256
            );

            SecretKeyFactory algoritmo =
                SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            byte[] hashCalculado =
                algoritmo.generateSecret(dados).getEncoded();

            return MessageDigest.isEqual(hashOriginal, hashCalculado);

        } catch (IllegalArgumentException erro) {
            return false;
        } catch (GeneralSecurityException erro) {
            throw new RuntimeException("Não foi possível verificar a senha.", erro);
        } finally {
            if (dados != null) {
                dados.clearPassword();
            }
            java.util.Arrays.fill(letrasDaSenha, '\0');
        }
    }
}