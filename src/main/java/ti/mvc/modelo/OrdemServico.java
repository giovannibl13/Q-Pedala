package ti.mvc.modelo;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrdemServico {
    private Long id;
    private Cliente cliente;
    private Bicicleta bicicleta;
    private LocalDate dataAgendamento;
    private LocalDate dataExecucao;
    private StatusOrdem status;
    private Double subtotal = 0.0;
    private Double desconto = 0.0;
    private Double valorFinal = 0.0;
    private List<ItemOrdemServico> itens;

    public OrdemServico() {
    	itens = new ArrayList<>();
    	status = StatusOrdem.AGENDADA;
    }

    
    
	public OrdemServico(Cliente cliente, Bicicleta bicicleta, LocalDate dataAgendamento) {
		this();
		this.cliente = cliente;
		this.bicicleta = bicicleta;
		this.dataAgendamento = dataAgendamento;
	}



	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Bicicleta getBicicleta() {
        return bicicleta;
    }

    public void setBicicleta(Bicicleta bicicleta) {
        this.bicicleta = bicicleta;
    }

    public LocalDate getDataAgendamento() {
        return dataAgendamento;
    }

    public void setDataAgendamento(LocalDate dataAgendamento) {
        this.dataAgendamento = dataAgendamento;
    }

    public Date getDataAgendamentoParaExibicao() {
        return dataAgendamento == null ? null : Date.valueOf(dataAgendamento);
    }

    public LocalDate getDataExecucao() {
        return dataExecucao;
    }

    public void setDataExecucao(LocalDate dataExecucao) {
        this.dataExecucao = dataExecucao;
    }

    public Date getDataExecucaoParaExibicao() {
        return dataExecucao == null ? null : Date.valueOf(dataExecucao);
    }

    public StatusOrdem getStatus() {
        return status;
    }

    public void setStatus(StatusOrdem status) {
        this.status = status;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getDesconto() {
        return desconto;
    }

    public void setDesconto(Double desconto) {
        this.desconto = desconto;
    }

    public Double getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(Double valorFinal) {
        this.valorFinal = valorFinal;
    }

    public List<ItemOrdemServico> getItens() {
        return itens;
    }

    public void setItens(List<ItemOrdemServico> itens) {
        this.itens = itens;
    }

    @Override
    public String toString() {
        return String.format("Ordem nº %s", id);
    }
}
