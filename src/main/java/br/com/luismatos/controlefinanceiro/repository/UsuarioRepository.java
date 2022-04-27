package br.com.luismatos.controlefinanceiro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.luismatos.controlefinanceiro.model.Usuario;
import br.com.luismatos.controlefinanceiro.model.UsuariosCadastradosDTO;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {


	@Query(value = "SELECT * FROM usuarios u WHERE u.email = :email and u.status = 1", nativeQuery = true)
	Optional<Usuario> findByEmail(@Param("email") String email);

	@Cacheable("findAllUser")
	@Query(value = "SELECT u.id, u.nome, u.email FROM usuarios u WHERE status = true", nativeQuery = true)
	List<UsuariosCadastradosDTO> findUsuariosCadastrados();

	Optional<Usuario> findByEmailAndSenha(String email, String senha);
}
