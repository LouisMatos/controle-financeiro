package br.com.luismatos.controlefinanceiro.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/upload")
public class CarregaArquivoController {

	// Save the uploaded file to this folder
	private static String UPLOADED_FOLDER = "C:\\Users\\siul_\\Documents\\temp";

	@GetMapping("/")
	public String index() {
		return "upload";
	}

	@PostMapping
	public String singleFileUpload(@RequestParam("arquivo") MultipartFile file, RedirectAttributes redirectAttributes) {

		double bytess = file.getSize();
		double kilobytes = (bytess / 1024);
		double megabytes = (kilobytes / 1024);     
		BigDecimal bigDecimal = new BigDecimal((kilobytes / 1024));
//		 System.out.println(megabytes);

		if (file.isEmpty()) {
			redirectAttributes.addFlashAttribute("messageError", "Selecione um arquivo para fazer upload!");
			return "redirect:/";
		}

		try {

			byte[] bytes = file.getBytes();
			Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
			Files.write(path, bytes);

			System.out.println("Nome do Arquivo: " + file.getOriginalFilename());
			System.out.println("Tamanho do Arquivo: " + bigDecimal.setScale(5, BigDecimal.ROUND_UP) +" MB");

			redirectAttributes.addFlashAttribute("message",
					"Você carregou com sucesso '" + file.getOriginalFilename() + "'");

		} catch (IOException e) {
			e.printStackTrace();
		}

		return "redirect:/";
	}

	@GetMapping("/uploadStatus")
	public String uploadStatus() {
		return "uploadStatus";
	}

}
