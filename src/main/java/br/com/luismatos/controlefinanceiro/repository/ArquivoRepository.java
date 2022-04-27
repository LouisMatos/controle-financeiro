package br.com.luismatos.controlefinanceiro.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.luismatos.controlefinanceiro.model.DetalhesTransacaoDTO;
import br.com.luismatos.controlefinanceiro.model.ImportacaoRealizadaDTO;
import br.com.luismatos.controlefinanceiro.model.Transacao;

public interface ArquivoRepository extends JpaRepository<Transacao, Long> {

	@Cacheable("getAllTransactions")
	@Query(value = "SELECT count(*) FROM transacoes WHERE data_transacao LIKE %:dataTransacao%", nativeQuery = true)
	Integer findByDataTransacao(@Param("dataTransacao") LocalDate dataTransacao);
	
	@Cacheable("getAllTransactionsDateDetails")
	@Query(value = "SELECT * FROM transacoes WHERE data_transacao LIKE %:dataTransacao%", nativeQuery = true)
	List<Transacao> findByDataTransacaoDetalhado(@Param("dataTransacao") String dataTransacao);

	@Cacheable("getTransactionDateImport")
	@Query(value = "SELECT distinct  t.data_transacao as dataTransacoes, t.data_importacao_transacoes as dataImportacao, u.nome as nomeUsuario FROM transacoes t JOIN usuarios as u ON t.usuario_id = u.id", nativeQuery = true)
	List<ImportacaoRealizadaDTO> findDataTRansacaoDataImportacao();

	@Cacheable("getAllTransactionsDetails")
	@Query(value = "SELECT distinct  t.data_transacao as dataTransacoes, t.data_importacao_transacoes as dataImportacao, u.nome as nomeUsuario FROM transacoes t JOIN usuarios as u ON t.usuario_id = u.id WHERE t.data_transacao LIKE %:dataTransacao%", nativeQuery = true)
	DetalhesTransacaoDTO findTransacaoDetalhes(@Param("dataTransacao") String dataTransacao);
}
