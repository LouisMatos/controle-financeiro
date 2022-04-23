package br.com.luismatos.controlefinanceiro.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class IndexController {

	@GetMapping
	public String home(Model model, Principal principal) {
		return "index";
	}
	
	@GetMapping("imagem/index")
	public String imagemHome(Model model, Principal principal) {
		return "base/imagens/a-importancia-do-controle-financeiros-de-projetos.jpg";
	}
}
