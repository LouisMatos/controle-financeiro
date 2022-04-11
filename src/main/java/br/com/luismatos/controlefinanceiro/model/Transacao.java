package br.com.luismatos.controlefinanceiro.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transacao {

	private String bancoOrigem;

	private String agenciaOrigem;

	private String contaOrigem;

	private String bancoDestino;

	private String agenciaDestino;

	private String contaDestino;

	private BigDecimal valorTransacao;

	private LocalDateTime dataTransacao;

	public String getBancoOrigem() {
		return bancoOrigem;
	}

	public void setBancoOrigem(String bancoOrigem) {
		this.bancoOrigem = bancoOrigem;
	}

	public String getAgenciaOrigem() {
		return agenciaOrigem;
	}

	public void setAgenciaOrigem(String agenciaOrigem) {
		this.agenciaOrigem = agenciaOrigem;
	}

	public String getContaOrigem() {
		return contaOrigem;
	}

	public void setContaOrigem(String contaOrigem) {
		this.contaOrigem = contaOrigem;
	}

	public String getBancoDestino() {
		return bancoDestino;
	}

	public void setBancoDestino(String bancoDestino) {
		this.bancoDestino = bancoDestino;
	}

	public String getAgenciaDestino() {
		return agenciaDestino;
	}

	public void setAgenciaDestino(String agenciaDestino) {
		this.agenciaDestino = agenciaDestino;
	}

	public String getContaDestino() {
		return contaDestino;
	}

	public void setContaDestino(String contaDestino) {
		this.contaDestino = contaDestino;
	}

	public BigDecimal getValorTransacao() {
		return valorTransacao;
	}

	public void setValorTransacao(BigDecimal valorTransacao) {
		this.valorTransacao = valorTransacao;
	}

	public LocalDateTime getDataTransacao() {
		return dataTransacao;
	}

	public void setDataTransacao(LocalDateTime dataTransacao) {
		this.dataTransacao = dataTransacao;
	}

	@Override
	public String toString() {
		return "Transacao [bancoOrigem=" + bancoOrigem + ", agenciaOrigem=" + agenciaOrigem + ", contaOrigem="
				+ contaOrigem + ", bancoDestino=" + bancoDestino + ", agenciaDestino=" + agenciaDestino
				+ ", contaDestino=" + contaDestino + ", valorTransacao=" + valorTransacao + ", dataTransacao="
				+ dataTransacao + "]";
	}

}
