package br.com.luismatos.controlefinanceiro.model;

public class UsuarioDTO {

	private Integer id;
	
	private String nome;

	private String email;

	private String senha;

	private boolean senhaNova;

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

	public boolean isSenhaNova() {
		return senhaNova;
	}

	public void setSenhaNova(boolean senhaNova) {
		this.senhaNova = senhaNova;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "UsuarioDTO [id=" + id + ", nome=" + nome + ", email=" + email + ", senha=" + senha + ", senhaNova="
				+ senhaNova + "]";
	}

}
