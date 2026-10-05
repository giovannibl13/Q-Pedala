package ti.mvc.modelo;

public class Servico {
    private Long id;
    private Double valor;
    private String nome;
    
    public Servico() {
	}

	public Servico(Double valor, String nome) {
		this.valor = valor;
		this.nome = nome;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return String.format("%s", nome);
    }
}
