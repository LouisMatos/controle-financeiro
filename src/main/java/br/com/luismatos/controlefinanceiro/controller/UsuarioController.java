package br.com.luismatos.controlefinanceiro.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.luismatos.controlefinanceiro.model.UsuarioDTO;
import br.com.luismatos.controlefinanceiro.service.UsuarioService;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping("/listar")
	public String listar(Model model, Principal principal) {
		model.addAttribute("usuarios", usuarioService.buscarUsuariosCadastrados());
		return "usuarios/listarUsuarios";
	}

	@GetMapping("/cadastrar")
	public String cadastrar(Model model, Principal principal) {
		return "usuarios/novoUsuario";
	}

	@PostMapping("/novoUsuario")
	public String novo(@Valid UsuarioDTO usuarioDTO, RedirectAttributes redirectAttributes) {

		if (usuarioService.verificaEmailExiste(usuarioDTO)) {
			redirectAttributes.addFlashAttribute("messageError", "Email já cadastrado!");
			return "redirect:/usuario/cadastrar";
		} else {
			usuarioService.criarNovoUsuario(usuarioDTO);
		}

		return "redirect:/usuario/listar";
	}
}
