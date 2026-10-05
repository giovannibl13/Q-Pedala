package ti.mvc.modelo;

public enum TipoBicicleta {
    MTB("MTB"), 
    ROAD("Road"), 
    GRAVEL("Gravel"), 
    BMX("BMX"), 
    ELETRICA("Elétrica");

    private final String descricao;

    TipoBicicleta(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
