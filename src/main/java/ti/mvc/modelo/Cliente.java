package ti.mvc.modelo;

import java.sql.Date;
import java.time.LocalDate;

public class Cliente {
    private Long id;
    private String cpf;
    private String nome;
    private LocalDate dataNascimento;
    private String email;
    private String telefone;

    public Cliente() {
    }
    
    public Cliente(String cpf, String nome, LocalDate dataNascimento, String email, String telefone) {
		super();
		this.cpf = cpf;
		this.nome = nome;
		this.dataNascimento = dataNascimento;
		this.email = email;
		this.telefone = telefone;
	}

	 public Long getId() {
	        return id;
    }
	 
	public void setId(Long id) {
        this.id = id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Date getDataNascimentoParaExibicao() {
        return dataNascimento == null ? null : Date.valueOf(dataNascimento);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Override
    public String toString() {
        return String.format("%s", nome);
    }
}
