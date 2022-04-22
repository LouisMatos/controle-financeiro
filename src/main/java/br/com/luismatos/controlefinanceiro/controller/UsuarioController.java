package br.com.luismatos.controlefinanceiro.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.luismatos.controlefinanceiro.model.Usuario;
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

	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {

		Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (usuario.getId() == id) {
			redirectAttributes.addFlashAttribute("messageError", "Voçê não pode se excluir do sistema!");
			return "redirect:/usuario/listar";
		}else if (usuarioService.verificaUsuarioAdministradorSistema(id)) {
			redirectAttributes.addFlashAttribute("messageError", "Voçê não pode se excluir o usuário administrador do sistema!");
			return "redirect:/usuario/listar";
		}else {
			usuarioService.excluirUsuarioDoSistema(id);
		}

		return "redirect:/usuario/listar";
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
