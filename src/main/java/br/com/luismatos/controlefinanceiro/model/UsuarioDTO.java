package br.com.luismatos.controlefinanceiro.model;

public class UsuarioDTO {

	private String nome;

	private String email;

	private String senha;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	@Override
	public String toString() {
		return "UsuarioDTO [nome=" + nome + ", email=" + email + "]";
	}

}
