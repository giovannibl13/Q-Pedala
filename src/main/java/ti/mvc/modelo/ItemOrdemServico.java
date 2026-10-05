package ti.mvc.modelo;

public class ItemOrdemServico {
    private Long id;
    private OrdemServico ordem;
    private Servico servico;
    private boolean realizado;
    private Double valorExecutado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrdemServico getOrdem() {
        return ordem;
    }

    public void setOrdem(OrdemServico ordem) {
        this.ordem = ordem;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public boolean isRealizado() {
        return realizado;
    }

    public void setRealizado(boolean realizado) {
        this.realizado = realizado;
    }

    public Double getValorExecutado() {
        return valorExecutado;
    }

    public void setValorExecutado(Double valorExecutado) {
        this.valorExecutado = valorExecutado;
    }

    @Override
    public String toString() {
        return String.format("%s", servico);
    }
}
