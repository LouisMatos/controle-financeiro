package br.com.luismatos.controlefinanceiro.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.luismatos.controlefinanceiro.model.ImportacaoRealizadaDTO;
import br.com.luismatos.controlefinanceiro.model.Transacao;

public interface ArquivoRepository extends JpaRepository<Transacao, Long> {

	@Query(value = "SELECT count(*) FROM transacoes WHERE data_transacao LIKE %:dataTransacao%", nativeQuery = true)
	Integer findByDataTransacao(@Param("dataTransacao") LocalDate dataTransacao);
	
	@Query(value = "SELECT distinct  DATE_FORMAT(cast(data_transacao as date), '%d/%m/%Y') as dataTransacoes, DATE_FORMAT(t.data_importacao_transacoes, '%d/%m/%Y %H:%i:%s') as dataImportacao FROM transacoes t ORDER BY dataTransacoes DESC", nativeQuery = true)
	List<ImportacaoRealizadaDTO> findDataTRansacaoDataImportacao();
	
	
	

}
