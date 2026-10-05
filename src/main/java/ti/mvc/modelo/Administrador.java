package ti.mvc.modelo;


public class Administrador {
    private Long id;
    private String usuario;
    private String senha;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenhaHash(String senha) {
        this.senha = senha;
    }

    @Override
    public String toString() {
        return String.format("%s", usuario);
    }
}
