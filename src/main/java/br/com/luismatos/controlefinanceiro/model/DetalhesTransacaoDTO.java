package br.com.luismatos.controlefinanceiro.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface DetalhesTransacaoDTO {
	
	public LocalDate getDataTransacoes();
	
	public LocalDateTime getDataImportacao();
	
	public String  getNomeUsuario();
	
}