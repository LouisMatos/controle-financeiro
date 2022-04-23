package br.com.luismatos.controlefinanceiro.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.luismatos.controlefinanceiro.model.ImportacaoRealizadaDTO;
import br.com.luismatos.controlefinanceiro.model.Transacao;
import br.com.luismatos.controlefinanceiro.model.Usuario;
import br.com.luismatos.controlefinanceiro.repository.ArquivoRepository;

@Service
public class ImportacaoService {

	@Autowired
	private ArquivoRepository arquivoRepository;

	private ArrayList<Transacao> transacaos;

	private LocalDateTime dataPrimeiraLinha;

	private LocalDateTime dataImportacao;
	
	private Usuario usuario;

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

	@SuppressWarnings("resource")
	public void processarTransacoes(MultipartFile file, Object sessao) {
		try {
			BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
			CSVParser csvParser;
			csvParser = new CSVParser(fileReader, CSVFormat.EXCEL);
			Iterable<CSVRecord> csvRecords = csvParser.getRecords();
			
			dataImportacao = LocalDateTime.now();
			transacaos = new ArrayList<>();
			usuario = (Usuario) sessao;

			for (CSVRecord csvRecord : csvRecords) {

				Transacao transacao = converterTransacaoParaObjeto(csvRecord, usuario);

				if (csvRecord.getRecordNumber() == 1) {
					dataPrimeiraLinha = LocalDateTime.parse(csvRecord.get(7));
				}

				if (transacao != null) {
					if (transacao.getDataTransacao().equals(dataPrimeiraLinha.toLocalDate())) {
						transacaos.add(transacao);
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private Transacao converterTransacaoParaObjeto(CSVRecord csvRecord, Usuario usuario) {
		Transacao transacao = new Transacao();

		transacao.setBancoOrigem(csvRecord.get(0));
		transacao.setAgenciaOrigem(csvRecord.get(1));
		transacao.setContaOrigem(csvRecord.get(2));
		transacao.setBancoDestino(csvRecord.get(3));
		transacao.setAgenciaDestino(csvRecord.get(4));
		transacao.setContaDestino(csvRecord.get(5));
		transacao.setValorTransacao(
				(csvRecord.get(6).trim().isEmpty()) ? null : new BigDecimal(csvRecord.get(6)).setScale(2));
		transacao.setDataTransacao(LocalDateTime.parse(csvRecord.get(7)).toLocalDate());
		transacao.setDataImportacaoTransacoes(this.dataImportacao);
		transacao.setUsuario(usuario);

		if (validator(transacao)) {
			return transacao;
		}

		return null;

	}

	public static boolean validator(Object myObject) {
		Field[] fields = myObject.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				Object objectValue = field.get(myObject);
				if ((objectValue == null || objectValue.toString().length() == 0) && !field.getName().equals("id")) {
//					System.out.println("null or empty field = '" + field.getName() + "' in : "
//							+ myObject.getClass().getCanonicalName());
					return false; // or you can throw a exception with a message, is better that return a boolean
				}
			} catch (IllegalArgumentException | IllegalAccessException ex) {
				System.out.println(ex);
			}
		}
		return true;
	}

	public void salvarTransacoesNoBanco() throws SQLIntegrityConstraintViolationException {

		if (arquivoRepository.findByDataTransacao(dataPrimeiraLinha.toLocalDate()) == 0) {
			arquivoRepository.saveAll(transacaos);
		} else {
			throw new SQLIntegrityConstraintViolationException();
		}

	}

	public List<ImportacaoRealizadaDTO> buscarImportacoesRealizadas() {

		return arquivoRepository.findDataTRansacaoDataImportacao();

	}

	public List<Transacao> buscarTransacoesDataDetalhado(String dataTransacao) {
		return arquivoRepository.findByDataTransacaoDetalhado(dataTransacao);
	}

	public Object buscarDetalhesTransacao(String dataTransacao) {
		return arquivoRepository.findTransacaoDetalhes(dataTransacao);
	}

}
