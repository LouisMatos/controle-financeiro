package br.com.luismatos.controlefinanceiro.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.luismatos.controlefinanceiro.enums.EnumModulo;
import br.com.luismatos.controlefinanceiro.enums.EnumPerfil;
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
//		usuario.setPapeis(EnumModulo.USUARIO);
		usuario.setCadastro(LocalDateTime.now());
		usuario.setStatus(true);
		usuario.setSenha(Utils.encrypt(usuarioDTO.getSenha()));

		sendEmail.enviarEmail(usuarioDTO);
		usuarioRepository.save(usuario);

	}

	public Optional<Usuario> buscarUsuario(String email) {
		return usuarioRepository.findByEmail(email);
	}

	public Optional<Usuario> verificaEmailESenha(String login, String senha) {
		return usuarioRepository.findByEmailAndSenha(login, senha);
	}

}
