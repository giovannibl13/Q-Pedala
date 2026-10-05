package ti.mvc.logica;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ti.mvc.modelo.Cliente;
import ti.mvc.modelo.dao.ClienteDAO;

public class CadastroCliente implements Logica {

	@Override
	public void executa(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String cpf = texto(req, "cpf").replace(".", "").replace("-", "");
			String nome = texto(req, "nome");
			String dataNascimentoTexto = texto(req, "dataNascimento");
			String email = texto(req, "email");
			String telefone = texto(req, "telefone");

			if (!cpfValido(cpf)) {
				throw new IllegalArgumentException("Informe um CPF válido.");
			}
			if (nome.isEmpty()) {
				throw new IllegalArgumentException("Informe o nome do cliente.");
			}
			if (!emailValido(email)) {
				throw new IllegalArgumentException("Informe um e-mail válido.");
			}
			if (telefone.isEmpty()) {
				throw new IllegalArgumentException("Informe o telefone do cliente.");
			}

			LocalDate dataNascimento = LocalDate.parse(dataNascimentoTexto);
			if (dataNascimento.isAfter(LocalDate.now())) {
				throw new IllegalArgumentException("A data de nascimento não pode estar no futuro.");
			}

			Cliente cliente = new Cliente(cpf, nome, dataNascimento, email, telefone);
			new ClienteDAO().adiciona(cliente);

			resp.sendRedirect(req.getContextPath() + "/mvc?logica=Inicio");
		} catch (DateTimeParseException e) {
			req.setAttribute("erro", "Informe uma data de nascimento válida.");
			new NovoCliente().executa(req, resp);
		} catch (IllegalArgumentException e) {
			req.setAttribute("erro", e.getMessage());
			new NovoCliente().executa(req, resp);
		}
	}

	private String texto(HttpServletRequest req, String nome) {
		String valor = req.getParameter(nome);
		return valor == null ? "" : valor.trim();
	}

	private boolean emailValido(String email) {
		return email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
	}

	private boolean cpfValido(String cpf) {
		if (!cpf.matches("\\d{11}")) {
			return false;
		}

		boolean todosIguais = true;
		for (int i = 1; i < cpf.length(); i++) {
			if (cpf.charAt(i) != cpf.charAt(0)) {
				todosIguais = false;
				break;
			}
		}
		if (todosIguais) {
			return false;
		}

		int soma = 0;
		for (int i = 0; i < 9; i++) {
			soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
		}
		int primeiroDigito = 11 - (soma % 11);
		if (primeiroDigito >= 10) {
			primeiroDigito = 0;
		}
		if (primeiroDigito != Character.getNumericValue(cpf.charAt(9))) {
			return false;
		}

		soma = 0;
		for (int i = 0; i < 10; i++) {
			soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
		}
		int segundoDigito = 11 - (soma % 11);
		if (segundoDigito >= 10) {
			segundoDigito = 0;
		}

		return segundoDigito == Character.getNumericValue(cpf.charAt(10));
	}

}
