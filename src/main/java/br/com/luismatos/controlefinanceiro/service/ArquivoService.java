package br.com.luismatos.controlefinanceiro.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ArquivoService {

	@SuppressWarnings("resource")
	public void lerArquivo(MultipartFile file) {

		try {
			BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
			CSVParser csvParser;
			csvParser = new CSVParser(fileReader, CSVFormat.EXCEL);
			Iterable<CSVRecord> csvRecords = csvParser.getRecords();

			for (CSVRecord csvRecord : csvRecords) {
				System.out.println(csvRecord.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void infosArquivo(MultipartFile file) {
		double bytess = file.getSize();
		double kilobytes = (bytess / 1024);
		BigDecimal bigDecimal = new BigDecimal((kilobytes / 1024));

		System.out.println("Nome do Arquivo: " + file.getOriginalFilename());
		System.out.println("Tamanho do Arquivo: " + bigDecimal.setScale(5, BigDecimal.ROUND_UP) + " MB");
	}

}
