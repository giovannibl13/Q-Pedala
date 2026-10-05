package ti.mvc.modelo.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import ti.mvc.connection.ConnectionFactory;
import ti.mvc.modelo.*;

public class BicicletaDAO {
    public void adiciona(Bicicleta bicicleta) {
        String sql = "INSERT INTO bicicleta (marca, modelo, tipo, tamanho_quadro, numero_serie, ano_fabricacao, proprietario_id) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, bicicleta.getMarca());
            ps.setObject(2, bicicleta.getModelo());
            ps.setString(3, bicicleta.getTipo().name());
            ps.setObject(4, bicicleta.getTamanhoQuadro());
            ps.setObject(5, bicicleta.getNumeroSerie());
            ps.setObject(6, bicicleta.getAnoFabricacao());
            ps.setObject(7, bicicleta.getProprietario() == null ? null : bicicleta.getProprietario().getId(), Types.BIGINT);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                bicicleta.setId(rs.getLong("id"));
            }
        } catch (SQLException e) { throw new RuntimeException("Não foi possível cadastrar bicicleta.", e); }
    }

    public void atualiza(Bicicleta bicicleta) {
        String sql = "UPDATE bicicleta SET marca=?, modelo=?, tipo=?, tamanho_quadro=?, numero_serie=?, ano_fabricacao=?, proprietario_id=? WHERE id=?";
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, bicicleta.getMarca());
            ps.setObject(2, bicicleta.getModelo());
            ps.setString(3, bicicleta.getTipo().name());
            ps.setObject(4, bicicleta.getTamanhoQuadro());
            ps.setObject(5, bicicleta.getNumeroSerie());
            ps.setObject(6, bicicleta.getAnoFabricacao());
            ps.setObject(7, bicicleta.getProprietario() == null ? null : bicicleta.getProprietario().getId(), Types.BIGINT);
            ps.setLong(8, bicicleta.getId());
            if (ps.executeUpdate() != 1) throw new IllegalArgumentException("Registro não encontrado.");
        } catch (SQLException e) { throw new RuntimeException("Não foi possível atualizar bicicleta.", e); }
    }

    // As chaves estrangeiras impedem remover registros usados no histórico.
    public void remove(Long id) {
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM bicicleta WHERE id=?")) {
            ps.setLong(1, id);
            if (ps.executeUpdate() != 1) throw new IllegalArgumentException("Registro não encontrado.");
        } catch (SQLException e) { throw new RuntimeException("Não foi possível remover: confira os vínculos do registro.", e); }
    }

    public Bicicleta buscaPorId(Long id) {
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM bicicleta WHERE id=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? leRegistro(rs) : null; }
        } catch (SQLException e) { throw new RuntimeException("Não foi possível consultar bicicleta.", e); }
    }

    public List<Bicicleta> lista() {
        List<Bicicleta> registros = new ArrayList<>();
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM bicicleta ORDER BY id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) registros.add(leRegistro(rs));
            return registros;
        } catch (SQLException e) { throw new RuntimeException("Não foi possível listar bicicleta.", e); }
    }

    private Bicicleta leRegistro(ResultSet rs) throws SQLException {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(rs.getLong("id"));
        bicicleta.setMarca(rs.getString("marca"));
        bicicleta.setModelo(rs.getString("modelo"));
        bicicleta.setTipo(TipoBicicleta.valueOf(rs.getString("tipo")));
        bicicleta.setTamanhoQuadro(rs.getString("tamanho_quadro"));
        bicicleta.setNumeroSerie(rs.getString("numero_serie"));
        bicicleta.setAnoFabricacao(rs.getInt("ano_fabricacao"));
        Long dono = rs.getObject("proprietario_id", Long.class);
        if (dono != null) bicicleta.setProprietario(new ClienteDAO().buscaPorId(dono));
        return bicicleta;
    }
    
    public List<Bicicleta> listaPorCliente(Long clienteId) {
		
    	List<Bicicleta> bicicletas = new ArrayList<Bicicleta>();
    	
    	String sql = "SELECT * FROM bicicleta WHERE proprietario_id = ? ORDER BY id";

    	try (Connection con = new ConnectionFactory().getConnection()){ 
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setLong(1, clienteId);
			
            ResultSet rs = ps.executeQuery();

            while (rs.next()) bicicletas.add(leRegistro(rs));
            return bicicletas;
		} catch (SQLException e) { throw new RuntimeException("Não foi possível listar bicicleta.", e); }
	}
}

