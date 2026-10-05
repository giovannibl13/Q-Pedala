package ti.mvc.modelo;

public enum StatusOrdem {
    AGENDADA("Agendada"),
    EXECUTADA("Executada"),
    PAGA("Paga"), 
    CANCELADA("Cancelada");

    private final String descricao;

    StatusOrdem(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
