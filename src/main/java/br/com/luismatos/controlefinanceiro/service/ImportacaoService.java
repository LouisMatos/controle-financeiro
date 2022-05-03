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

import br.com.luismatos.controlefinanceiro.model.AgenciaDTO;
import br.com.luismatos.controlefinanceiro.model.AgenciaSuspeita;
import br.com.luismatos.controlefinanceiro.model.ContaDTO;
import br.com.luismatos.controlefinanceiro.model.ContaSuspeita;
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

	public List<Transacao> analisarTransacoesSuspeitasData(String dataAnalisar) {
		
		
		List<Transacao> transacoes = arquivoRepository.findByDataTransacaoDetalhado(tratarDataPesquisa(dataAnalisar));
		
		List<Transacao> transacoesSuspeitas = new ArrayList<Transacao>();
		
		
		for (Transacao transacao : transacoes) {
			if(transacao.getValorTransacao().compareTo(new BigDecimal("100000.00")) >= 0){
				transacoesSuspeitas.add(transacao);
			}
		}
		
		return transacoesSuspeitas;
		
	}
	
	
	public ArrayList<ContaSuspeita> analisarContasSuspeitas(String dataAnalisar) {
		
		ArrayList<ContaSuspeita> contaSuspeitas = new ArrayList<ContaSuspeita>();
		
		//Aqui estou verificando as contas suspeitas de origem,quer dizer que estão mandando valores suspeitos, então vai virar valores de saida no front(SAIDA)
		List<ContaDTO> contasOrigem = arquivoRepository.findAllAgenciaAndContaOrigem(tratarDataPesquisa(dataAnalisar));
		
		for (ContaDTO conta : contasOrigem) {
			ContaSuspeita contaSuspeita = new ContaSuspeita();
			contaSuspeita.setBanco(conta.getBanco());
			contaSuspeita.setAgencia(conta.getAgencia());
			contaSuspeita.setConta(conta.getConta());
			contaSuspeita.setValorSuspeito(String.valueOf(arquivoRepository.getValorTransacoesSuspeitasOrigem(tratarDataPesquisa(dataAnalisar), conta.getConta(), conta.getAgencia(), conta.getBanco())));
			contaSuspeita.setTipoMovimentacao("SAIDA");
			if(!contaSuspeita.getValorSuspeito().equals("null")) {
				contaSuspeitas.add(contaSuspeita);
//				System.out.println(contaSuspeita.toString());
			}
		}
		
		//Aqui estou verificando as contas suspeitas de destino,quer dizer que estão recebendo valores suspeitos, então vai virar valores de Entrada no front(Entrada)
		List<ContaDTO> contasDestino = arquivoRepository.findAllAgenciaAndContaDestino(tratarDataPesquisa(dataAnalisar));
		
		for (ContaDTO conta : contasDestino) {
			
			ContaSuspeita contaSuspeita = new ContaSuspeita();
			contaSuspeita.setBanco(conta.getBanco());
			contaSuspeita.setAgencia(conta.getAgencia());
			contaSuspeita.setConta(conta.getConta());
			contaSuspeita.setValorSuspeito(String.valueOf(arquivoRepository.getValorTransacoesSuspeitasSaida(tratarDataPesquisa(dataAnalisar), conta.getConta(), conta.getAgencia(), conta.getBanco())));
			contaSuspeita.setTipoMovimentacao("ENTRADA");
			if(!contaSuspeita.getValorSuspeito().equals("null")) {
				contaSuspeitas.add(contaSuspeita);
//				System.out.println(contaSuspeita.toString());
			}
			
			
		}
		
		return contaSuspeitas;
	}
	
	public ArrayList<AgenciaSuspeita> analisarAgenciasSuspeitas(String dataAnalisar) {
		
		ArrayList<AgenciaSuspeita> agenciasSuspeitas = new ArrayList<AgenciaSuspeita>();
		
		
		List<AgenciaDTO> agenciaOrigem = arquivoRepository.findAllAgenciaOrigem(tratarDataPesquisa(dataAnalisar));
		
		
		for (AgenciaDTO agencia : agenciaOrigem) {
			AgenciaSuspeita agenciaSuspeita = new AgenciaSuspeita();
			agenciaSuspeita.setAgencia(agencia.getAgencia());
			agenciaSuspeita.setBanco(agencia.getBanco());
			agenciaSuspeita.setTipoMovimentacao("SAIDA");
			agenciaSuspeita.setValorSuspeito(String.valueOf(arquivoRepository.getValorAgenciaSuspeitasOrigem(tratarDataPesquisa(dataAnalisar), agencia.getAgencia(), agencia.getBanco())));
			
			if(!agenciaSuspeita.getValorSuspeito().equals("null")) {
				agenciasSuspeitas.add(agenciaSuspeita);
				
			}
		}
		
		List<AgenciaDTO> agenciaDestino = arquivoRepository.findAllAgenciaDestino(tratarDataPesquisa(dataAnalisar));
		
		for (AgenciaDTO agencia : agenciaDestino) {
			AgenciaSuspeita agenciaSuspeita = new AgenciaSuspeita();
			agenciaSuspeita.setAgencia(agencia.getAgencia());
			agenciaSuspeita.setBanco(agencia.getBanco());
			agenciaSuspeita.setTipoMovimentacao("ENTRADA");
			agenciaSuspeita.setValorSuspeito(String.valueOf(arquivoRepository.getValorAgenciaSuspeitasDestino(tratarDataPesquisa(dataAnalisar), agencia.getAgencia(), agencia.getBanco())));
			
			if(!agenciaSuspeita.getValorSuspeito().equals("null")) {
				agenciasSuspeitas.add(agenciaSuspeita);
				
			}
		}
		
		return agenciasSuspeitas;
	}

	private String tratarDataPesquisa(String dataAnalisar) {
		
		String dataSplit[] = dataAnalisar.split("/");
		
		return dataSplit[1] + "-" + dataSplit[0];
	}

	

	

}
