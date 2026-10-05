package ti.mvc.modelo.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import ti.mvc.connection.ConnectionFactory;
import ti.mvc.modelo.*;

public class ServicoDAO {
    public void adiciona(Servico obj) {
        String sql = "INSERT INTO servico (nome, valor) VALUES (?, ?) RETURNING id";
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, obj.getNome());
            ps.setDouble(2, obj.getValor());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                obj.setId(rs.getLong("id"));
            }
        } catch (SQLException e) { throw new RuntimeException("Não foi possível cadastrar servico.", e); }
    }

    public void atualiza(Servico obj) {
        String sql = "UPDATE servico SET nome=?, valor=? WHERE id=?";
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, obj.getNome());
            ps.setDouble(2, obj.getValor());
            ps.setLong(3, obj.getId());
            if (ps.executeUpdate() != 1) throw new IllegalArgumentException("Registro não encontrado.");
        } catch (SQLException e) { throw new RuntimeException("Não foi possível atualizar servico.", e); }
    }

    // As chaves estrangeiras impedem remover registros usados no histórico.
    public void remove(Long id) {
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM servico WHERE id=?")) {
            ps.setLong(1, id);
            if (ps.executeUpdate() != 1) throw new IllegalArgumentException("Registro não encontrado.");
        } catch (SQLException e) { throw new RuntimeException("Não foi possível remover: confira os vínculos do registro.", e); }
    }

    public Servico buscaPorId(Long id) {
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM servico WHERE id=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? leRegistro(rs) : null; }
        } catch (SQLException e) { throw new RuntimeException("Não foi possível consultar servico.", e); }
    }

    public List<Servico> lista() {
        List<Servico> registros = new ArrayList<>();
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM servico ORDER BY id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) registros.add(leRegistro(rs));
            return registros;
        } catch (SQLException e) { throw new RuntimeException("Não foi possível listar servico.", e); }
    }

    private Servico leRegistro(ResultSet rs) throws SQLException {
        Servico obj = new Servico();
        obj.setId(rs.getLong("id"));
        obj.setNome(rs.getString("nome"));
        obj.setValor(rs.getDouble("valor"));
        return obj;
    }
}
