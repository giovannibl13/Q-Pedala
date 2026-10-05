package ti.mvc.modelo.dao;

import java.sql.*;
import ti.mvc.connection.ConnectionFactory;
import ti.mvc.modelo.Administrador;

public class AdministradorDAO {
    // O argumento deve conter um hash codificado, nunca a senha em texto puro.
    public void adiciona(Administrador administrador, String senhaHash) {
        if (senhaHash == null || senhaHash.isBlank())
            throw new IllegalArgumentException("Informe o hash da senha.");
        String sql = "INSERT INTO administrador (usuario, senha_hash) VALUES (?, ?) RETURNING id";
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, administrador.getUsuario());
            ps.setString(2, senhaHash);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                administrador.setId(rs.getLong("id"));
                administrador.setSenhaHash(senhaHash);
            }
        } catch (SQLException e) { throw new RuntimeException("Não foi possível cadastrar administrador.", e); }
    }

    public Administrador buscaPorUsuario(String usuario) {
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM administrador WHERE usuario=?")) {
            ps.setString(1, usuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                Administrador administrador = new Administrador();
                administrador.setId(rs.getLong("id"));
                administrador.setUsuario(rs.getString("usuario"));
                administrador.setSenhaHash(rs.getString("senha_hash"));
                return administrador;
            }
        } catch (SQLException e) { throw new RuntimeException("Não foi possível consultar administrador.", e); }
    }

    public void alteraSenha(Long id, String senhaHash) {
        if (senhaHash == null || senhaHash.isBlank())
            throw new IllegalArgumentException("Informe o hash da senha.");
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE administrador SET senha_hash=? WHERE id=?")) {
            ps.setString(1, senhaHash);
            ps.setLong(2, id);
            if (ps.executeUpdate() != 1) throw new IllegalArgumentException("Administrador não encontrado.");
        } catch (SQLException e) { throw new RuntimeException("Não foi possível alterar a senha.", e); }
    }
}
