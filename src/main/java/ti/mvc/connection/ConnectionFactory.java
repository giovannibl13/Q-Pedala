package ti.mvc.connection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
    public Connection getConnection() {
        Properties configuracao = new Properties();

        try (InputStream arquivo = ConnectionFactory.class
                .getResourceAsStream("/banco.properties")) {

            if (arquivo == null) {
                throw new IllegalStateException(
                        "Arquivo banco.properties não encontrado.");
            }

            configuracao.load(arquivo);
            Class.forName("org.postgresql.Driver");

            return DriverManager.getConnection(
                    configuracao.getProperty("url"),
                    configuracao.getProperty("usuario"),
                    configuracao.getProperty("senha")
            );
        } catch (SQLException | ClassNotFoundException | IOException e) {
            throw new RuntimeException("Não foi possível conectar ao banco de dados.", e);
        }
    }
}
