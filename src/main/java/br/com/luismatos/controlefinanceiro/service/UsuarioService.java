package br.com.luismatos.controlefinanceiro.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.luismatos.controlefinanceiro.enums.EnumModulo;
import br.com.luismatos.controlefinanceiro.model.Papel;
import br.com.luismatos.controlefinanceiro.model.Usuario;
import br.com.luismatos.controlefinanceiro.model.UsuarioDTO;
import br.com.luismatos.controlefinanceiro.model.UsuariosCadastradosDTO;
import br.com.luismatos.controlefinanceiro.repository.UsuarioRepository;
import br.com.luismatos.controlefinanceiro.util.EnviarEmail;
import br.com.luismatos.controlefinanceiro.util.Utils;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private EnviarEmail sendEmail;

	public List<UsuariosCadastradosDTO> buscarUsuariosCadastrados() {
		return usuarioRepository.findUsuariosCadastrados();
	}

	public boolean verificaEmailExiste(UsuarioDTO usuarioDTO) {
		if (usuarioRepository.findByEmail(usuarioDTO.getEmail().toUpperCase()).isPresent()) {
			return true;
		}
		return false;
	}

	public void criarNovoUsuario(@Valid UsuarioDTO usuarioDTO) {

		usuarioDTO.setSenha(Utils.getRandomNumberString());

		Usuario usuario = new Usuario();
		usuario.setEmail(usuarioDTO.getEmail().toUpperCase());
		usuario.setNome(usuarioDTO.getNome());
		List<Papel> papeis = new ArrayList<Papel>();
		Papel papel = new Papel();
		papel.setNome(EnumModulo.USUARIO);
		papeis.add(papel);
		usuario.setPapeis(papeis);
		usuario.setCadastro(LocalDateTime.now());
		usuario.setStatus(true);
		usuario.setSenha(Utils.encrypt(usuarioDTO.getSenha()));

		sendEmail.enviarEmail(usuarioDTO);
		usuarioRepository.save(usuario).getId();

	}

	public Optional<Usuario> buscarUsuario(String email) {
		return usuarioRepository.findByEmail(email);
	}

	public Optional<Usuario> buscarUsuarioPorId(Integer id) {
		return usuarioRepository.findById(id);
	}

	public Optional<Usuario> verificaEmailESenha(String login, String senha) {
		return usuarioRepository.findByEmailAndSenha(login, senha);
	}

	public boolean excluirUsuarioDoSistema(Integer id) {
		Optional<Usuario> usuario = usuarioRepository.findById(id);

		if (usuario.isPresent()) {
			Usuario usuarioBD = usuario.get();
			usuarioBD.setStatus(false);
			usuarioRepository.save(usuarioBD);
			return true;
		} else {
			return false;
		}

	}

	public boolean verificaUsuarioAdministradorSistema(Integer id) {
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		if (usuario.get().getNome().equalsIgnoreCase("Admin")
				&& usuario.get().getEmail().equalsIgnoreCase("admin@email.com.br")) {
			return true;
		}
		return false;
	}

	public void editarUsuario(@Valid UsuarioDTO usuarioDTO) {
		Usuario usuario = usuarioRepository.findById(usuarioDTO.getId()).get();

		usuario.setEmail(usuarioDTO.getEmail().toUpperCase());
		usuario.setNome(usuarioDTO.getNome());
		

		if (usuarioDTO.isSenhaNova()) {
			usuario.setSenha(Utils.encrypt(Utils.getRandomNumberString()));
		}

		usuarioRepository.save(usuario);
		sendEmail.enviarEmail(usuarioDTO);
	}

}
