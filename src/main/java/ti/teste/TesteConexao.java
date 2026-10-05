package ti.teste;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ti.mvc.connection.ConnectionFactory;

public class TesteConexao {

    public static void main(String[] args) {
        // Apenas consulta o banco; não altera os dados.
        String sql = "SELECT current_database(), current_user";
        try (Connection conexao = new ConnectionFactory().getConnection();
             PreparedStatement consulta = conexao.prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {
            if (resultado.next()) {
                System.out.println("Conexão realizada com sucesso!");
                System.out.println("Banco: " + resultado.getString(1));
                System.out.println("Usuário: " + resultado.getString(2));
            }
        } catch (SQLException e) {
            System.err.println("Não foi possível conectar. Confira o PostgreSQL e os dados de acesso.");
            System.err.println("Código SQL: " + e.getSQLState());
            System.exit(1);
        } catch (RuntimeException e) {
            System.err.println("Falha ao abrir a conexão. Confira o driver e os dados na ConnectionFactory.");
            System.exit(1);
        }
    }
}
