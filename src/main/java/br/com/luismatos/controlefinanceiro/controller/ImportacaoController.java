package br.com.luismatos.controlefinanceiro.controller;

import java.io.IOException;
import java.security.Principal;
import java.sql.SQLIntegrityConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.luismatos.controlefinanceiro.model.Usuario;
import br.com.luismatos.controlefinanceiro.service.ImportacaoService;

@Controller
@RequestMapping("/upload")
public class ImportacaoController {

	@Autowired
	private ImportacaoService importacaoService;
	
	
	@GetMapping("/transacoes")
	public String transacoes(Model model, Principal principal) {
		model.addAttribute("importacoesRealizadas", importacaoService.buscarImportacoesRealizadas());
		return "transacoes/transacoes";
	}
	
	@GetMapping("/transacao/detalhar/{data_transacao}")
	public String detalhar(@PathVariable("data_transacao") String dataTransacao, Model model, Principal principal) {
		
		model.addAttribute("detalhesImportacao", importacaoService.buscarDetalhesTransacao(dataTransacao));
		
		model.addAttribute("transacoesDetalhadas", importacaoService.buscarTransacoesDataDetalhado(dataTransacao));
		return "transacoes/detalharTransacao";
	}

	@PostMapping
	public String singleFileUpload(@RequestParam("arquivo") MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {

		if (file.isEmpty()) {
			redirectAttributes.addFlashAttribute("messageError", "Arquivo vazio ou selecione um arquivo para fazer upload!");
			return "redirect:/upload/transacoes";
		}
	
		importacaoService.infosArquivo(file);

		importacaoService.lerArquivo(file);
		
		importacaoService.processarTransacoes(file, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		
		try {
			importacaoService.salvarTransacoesNoBanco();
		} catch (SQLIntegrityConstraintViolationException e) {
			redirectAttributes.addFlashAttribute("messageError", "As transações já foram processadas e registradas no sitema para o dia informado!");
			return "redirect:/upload/transacoes";
		}

		redirectAttributes.addFlashAttribute("message", "Você carregou com sucesso '" + file.getOriginalFilename() + "'");

		return "redirect:/upload/transacoes";
	}

}
