package ti.mvc.modelo;


public class Bicicleta {
    private Long id;
    private String marca;
    private String modelo;
    private TipoBicicleta tipo;
    private String tamanhoQuadro;
    private String numeroSerie;
    private Integer anoFabricacao;
    private Cliente proprietario;

    public Bicicleta() {
	}

    
	public Bicicleta(String marca, String modelo, TipoBicicleta tipo, String tamanhoQuadro, String numeroSerie,
			Integer anoFabricacao, Cliente proprietario) {
		super();
		this.marca = marca;
		this.modelo = modelo;
		this.tipo = tipo;
		this.tamanhoQuadro = tamanhoQuadro;
		this.numeroSerie = numeroSerie;
		this.anoFabricacao = anoFabricacao;
		this.proprietario = proprietario;
	}


	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public TipoBicicleta getTipo() {
        return tipo;
    }

    public void setTipo(TipoBicicleta tipo) {
        this.tipo = tipo;
    }

    public String getTamanhoQuadro() {
        return tamanhoQuadro;
    }

    public void setTamanhoQuadro(String tamanhoQuadro) {
        this.tamanhoQuadro = tamanhoQuadro;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public Integer getAnoFabricacao() {
        return anoFabricacao;
    }

    public void setAnoFabricacao(Integer anoFabricacao) {
        this.anoFabricacao = anoFabricacao;
    }

    public Cliente getProprietario() {
        return proprietario;
    }

    public void setProprietario(Cliente proprietario) {
        this.proprietario = proprietario;
    }

    @Override
    public String toString() {
        return String.format("%s %s", marca, modelo);
    }
}
