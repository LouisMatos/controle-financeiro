package br.com.luismatos.controlefinanceiro.model;

public class ImportacaoRealizada {

	private String dataTransacoes;

	private String dataImportacao;

	public String getDataTransacoes() {
		return dataTransacoes;
	}

	public void setDataTransacoes(String dataTransacoes) {
		this.dataTransacoes = dataTransacoes;
	}

	public String getDataImportacao() {
		return dataImportacao;
	}

	public void setDataImportacao(String dataImportacao) {
		this.dataImportacao = dataImportacao;
	}

	public ImportacaoRealizada(String dataTransacoes, String dataImportacao) {
		this.dataTransacoes = dataTransacoes;
		this.dataImportacao = dataImportacao;
	}

}
