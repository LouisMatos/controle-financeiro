package br.com.luismatos.controlefinanceiro.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.luismatos.controlefinanceiro.model.Transacao;

public interface ArquivoRepository extends JpaRepository<Transacao, Long> {

	@Query(value = "SELECT count(*) FROM .transacoes WHERE data_transacao LIKE %:dataTransacao%", nativeQuery = true)
	Integer findByDataTransacao(@Param("dataTransacao") LocalDate dataTransacao);

}
