package br.com.luismatos.controlefinanceiro.controller;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.luismatos.controlefinanceiro.service.ArquivoService;

@Controller
@RequestMapping("/upload")
public class CarregaArquivoController {

	@Autowired
	private ArquivoService arquivoService;

	@PostMapping
	public String singleFileUpload(@RequestParam("arquivo") MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {

		if (file.isEmpty()) {
			redirectAttributes.addFlashAttribute("messageError", "Arquivo vazio ou selecione um arquivo para fazer upload!");
			return "redirect:/";
		}
	
		arquivoService.infosArquivo(file);

		arquivoService.lerArquivo(file);
		
		arquivoService.processarTransacoes(file);
		
		try {
			arquivoService.salvarTransacoesNoBanco();
		} catch (SQLIntegrityConstraintViolationException e) {
			redirectAttributes.addFlashAttribute("messageError", "As transações já foram processadas e registradas no sitema para o dia informado!");
			return "redirect:/";
		}

		redirectAttributes.addFlashAttribute("message", "Você carregou com sucesso '" + file.getOriginalFilename() + "'");

		return "redirect:/";
	}

}
