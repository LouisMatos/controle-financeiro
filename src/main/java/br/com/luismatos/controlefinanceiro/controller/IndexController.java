package br.com.luismatos.controlefinanceiro.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.luismatos.controlefinanceiro.model.ImportacaoRealizadaDTO;
import br.com.luismatos.controlefinanceiro.service.ArquivoService;

@Controller
@RequestMapping("/")
public class IndexController {

	@Autowired
	private ArquivoService arquivoService;

	@GetMapping
	public String home(Model model, Principal principal, RedirectAttributes redirectAttributes) {

		List<ImportacaoRealizadaDTO> importacoesRealizadas = arquivoService.buscarImportacoesRealizadas();

		model.addAttribute("importacoesRealizadas", importacoesRealizadas);

		return "index";
	}
}
