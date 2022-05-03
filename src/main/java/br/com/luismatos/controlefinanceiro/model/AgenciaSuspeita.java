package br.com.luismatos.controlefinanceiro.model;

public class AgenciaSuspeita {

	private String banco;
	
	private String agencia;

	private String valorSuspeito;

	private String tipoMovimentacao;

	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}


	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public String getValorSuspeito() {
		return valorSuspeito;
	}

	public void setValorSuspeito(String valorSuspeito) {
		this.valorSuspeito = valorSuspeito;
	}

	public String getTipoMovimentacao() {
		return tipoMovimentacao;
	}

	public void setTipoMovimentacao(String tipoMovimentacao) {
		this.tipoMovimentacao = tipoMovimentacao;
	}

	@Override
	public String toString() {
		return "AgenciaSuspeita [banco=" + banco + ", agencia=" + agencia + ", valorSuspeito=" + valorSuspeito
				+ ", tipoMovimentacao=" + tipoMovimentacao + "]";
	}

}
