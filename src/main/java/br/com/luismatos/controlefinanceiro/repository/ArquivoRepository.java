package br.com.luismatos.controlefinanceiro.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.luismatos.controlefinanceiro.model.AgenciaDTO;
import br.com.luismatos.controlefinanceiro.model.ContaDTO;
import br.com.luismatos.controlefinanceiro.model.DetalhesTransacaoDTO;
import br.com.luismatos.controlefinanceiro.model.ImportacaoRealizadaDTO;
import br.com.luismatos.controlefinanceiro.model.Transacao;

public interface ArquivoRepository extends JpaRepository<Transacao, Long> {

//	@Cacheable("getAllTransactions")
	@Query(value = "SELECT count(*) FROM transacoes WHERE data_transacao LIKE %:dataTransacao%", nativeQuery = true)
	Integer findByDataTransacao(@Param("dataTransacao") LocalDate dataTransacao);
	
//	@Cacheable("getAllTransactionsDateDetails")
	@Query(value = "SELECT * FROM transacoes WHERE data_transacao LIKE %:dataTransacao%", nativeQuery = true)
	List<Transacao> findByDataTransacaoDetalhado(@Param("dataTransacao") String dataTransacao);

//	@Cacheable("getTransactionDateImport")
	@Query(value = "SELECT distinct  t.data_transacao as dataTransacoes, t.data_importacao_transacoes as dataImportacao, u.nome as nomeUsuario FROM transacoes t JOIN usuarios as u ON t.usuario_id = u.id", nativeQuery = true)
	List<ImportacaoRealizadaDTO> findDataTRansacaoDataImportacao();

//	@Cacheable("getAllTransactionsDetails")
	@Query(value = "SELECT distinct  t.data_transacao as dataTransacoes, t.data_importacao_transacoes as dataImportacao, u.nome as nomeUsuario FROM transacoes t JOIN usuarios as u ON t.usuario_id = u.id WHERE t.data_transacao LIKE %:dataTransacao%", nativeQuery = true)
	DetalhesTransacaoDTO findTransacaoDetalhes(@Param("dataTransacao") String dataTransacao);
	
	@Query(value = "SELECT DISTINCT banco_origem as banco, t.conta_origem as conta, t.agencia_origem as agencia from transacoes t where t.data_transacao like %:dataTransacao%", nativeQuery = true)
	List<ContaDTO> findAllAgenciaAndContaOrigem(@Param("dataTransacao") String dataTransacao);
	
	@Query(value = "SELECT DISTINCT banco_destino as banco, t.conta_destino as conta, t.agencia_destino as agencia from transacoes t where t.data_transacao like %:dataTransacao%", nativeQuery = true)
	List<ContaDTO> findAllAgenciaAndContaDestino(@Param("dataTransacao") String dataTransacao);
	
	@Query(value = "SELECT DISTINCT banco_destino as banco,  t.agencia_destino as agencia from transacoes t where t.data_transacao like %:dataTransacao%", nativeQuery = true)
	List<AgenciaDTO> findAllAgenciaDestino(@Param("dataTransacao") String dataTransacao);
	
	@Query(value = "SELECT DISTINCT banco_origem as banco,  t.agencia_origem as agencia from transacoes t where t.data_transacao like %:dataTransacao%", nativeQuery = true)
	List<AgenciaDTO> findAllAgenciaOrigem(@Param("dataTransacao") String dataTransacao);
	
	@Query(value = "Select sum(valor_transacao) as total from transacoes where data_transacao like %:dataTransacao% and conta_origem = :conta and agencia_origem = :agencia and banco_origem = :banco HAVING total > 1000000.00", nativeQuery = true)
	BigDecimal getValorTransacoesSuspeitasOrigem(@Param("dataTransacao") String dataTransacao, @Param("conta") String conta,  @Param("agencia") String agencia, @Param("banco") String banco);
	
	@Query(value = "Select sum(valor_transacao) as total from transacoes where data_transacao like %:dataTransacao% and conta_destino = :conta and agencia_destino = :agencia and banco_destino = :banco HAVING total > 1000000.00", nativeQuery = true)
	BigDecimal getValorTransacoesSuspeitasSaida(@Param("dataTransacao") String dataTransacao, @Param("conta") String conta,  @Param("agencia") String agencia, @Param("banco") String banco);

	@Query(value = "Select sum(valor_transacao) as total from transacoes where banco_origem = :banco and agencia_origem = :agencia and data_transacao like %:dataTransacao% HAVING total > 1000000000.00", nativeQuery = true)
	BigDecimal getValorAgenciaSuspeitasOrigem(@Param("dataTransacao") String dataTransacao, @Param("agencia") String agencia, @Param("banco") String banco);

	@Query(value = "Select sum(valor_transacao) as total from transacoes where banco_destino = :banco and agencia_destino = :agencia and data_transacao like %:dataTransacao% HAVING total > 1000000000.00", nativeQuery = true)
	BigDecimal getValorAgenciaSuspeitasDestino(@Param("dataTransacao") String dataTransacao, @Param("agencia") String agencia, @Param("banco") String banco);

	
	
	
	
}
