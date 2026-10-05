package ti.mvc.modelo.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import ti.mvc.connection.ConnectionFactory;
import ti.mvc.modelo.*;

public class ClienteDAO {
    public void adiciona(Cliente obj) {
        String sql = "INSERT INTO cliente (cpf, nome, data_nascimento, email, telefone) VALUES (?, ?, ?, ?, ?) RETURNING id";
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, obj.getCpf());
            ps.setObject(2, obj.getNome());
            ps.setObject(3, obj.getDataNascimento());
            ps.setObject(4, obj.getEmail());
            ps.setObject(5, obj.getTelefone());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                obj.setId(rs.getLong("id"));
            }
        } catch (SQLException e) { throw new RuntimeException("Não foi possível cadastrar cliente.", e); }
    }

    public void atualiza(Cliente obj) {
        String sql = "UPDATE cliente SET cpf=?, nome=?, data_nascimento=?, email=?, telefone=? WHERE id=?";
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, obj.getCpf());
            ps.setObject(2, obj.getNome());
            ps.setObject(3, obj.getDataNascimento());
            ps.setObject(4, obj.getEmail());
            ps.setObject(5, obj.getTelefone());
            ps.setLong(6, obj.getId());
            if (ps.executeUpdate() != 1) throw new IllegalArgumentException("Registro não encontrado.");
        } catch (SQLException e) { throw new RuntimeException("Não foi possível atualizar cliente.", e); }
    }

    // As chaves estrangeiras impedem remover registros usados no histórico.
    public void remove(Long id) {
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM cliente WHERE id=?")) {
            ps.setLong(1, id);
            if (ps.executeUpdate() != 1) throw new IllegalArgumentException("Registro não encontrado.");
        } catch (SQLException e) { throw new RuntimeException("Não foi possível remover: confira os vínculos do registro.", e); }
    }

    public Cliente buscaPorId(Long id) {
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM cliente WHERE id=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? leRegistro(rs) : null; }
        } catch (SQLException e) { throw new RuntimeException("Não foi possível consultar cliente.", e); }
    }

    public List<Cliente> lista() {
        List<Cliente> registros = new ArrayList<>();
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM cliente ORDER BY id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) registros.add(leRegistro(rs));
            return registros;
        } catch (SQLException e) { throw new RuntimeException("Não foi possível listar cliente.", e); }
    }

    private Cliente leRegistro(ResultSet rs) throws SQLException {
        Cliente obj = new Cliente();
        obj.setId(rs.getLong("id"));
        obj.setCpf(rs.getString("cpf"));
        obj.setNome(rs.getString("nome"));
        obj.setDataNascimento(rs.getObject("data_nascimento", java.time.LocalDate.class));
        obj.setEmail(rs.getString("email"));
        obj.setTelefone(rs.getString("telefone"));
        return obj;
    }
}

