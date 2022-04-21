package br.com.luismatos.controlefinanceiro.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.luismatos.controlefinanceiro.model.UsuarioDTO;

@Component
public class EnviarEmail {

	@Value("${send.smtp.email}")
	private String emailDe;

	@Value("${send.smtp.senha}")
	private String senha;

	@Value("${send.smtp.host}")
	private String host;

	@Value("${send.smtp.port}")
	private String port;

	@Value("${send.smtp.ssl}")
	private String ssl;

	@Value("${send.smtp.auth}")
	private String auth;

	public void enviarEmail(UsuarioDTO usuarioDTO) {
//		// O ID do e-mail do destinatário precisa ser mencionado.
//		String para = "luis.felipe.pereira.matos@gmail.com";

		// O ID do e-mail do remetente precisa ser mencionado
//		String from = "luis.felipe.pereira.matos@gmail.com";

		// Supondo que você está enviando e-mail por gmails smtp
//		String host = "smtp.gmail.com";

		// Obter propriedades do sistema
		Properties properties = System.getProperties();

		// Configurar servidor de e-mail
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", port);
		properties.put("mail.smtp.ssl.enable", ssl);
		properties.put("mail.smtp.auth", auth);

		// Obtenha o objeto Session.// e passe o nome de usuário e a senha
		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(emailDe, senha);
			}
		});
//
//        // Usado para depurar problemas de SMTP
//        session.setDebug (true);

		try {
			// Cria um objeto MimeMessage padrão.
			MimeMessage message = new MimeMessage(session);

			// Definir De: campo de cabeçalho do cabeçalho.
			message.setFrom(new InternetAddress(emailDe));

			// Definir como: campo de cabeçalho do cabeçalho.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(usuarioDTO.getEmail()));

			// Definir assunto: campo de cabeçalho
			message.setSubject("Acesso do " + usuarioDTO.getNome() + " ao sistema de controle Finaceiro");

			// Agora defina a mensagem real
			message.setText("Nome: " + usuarioDTO.getNome() + "\n" + "Email: " + usuarioDTO.getEmail() + "\n"
					+ "Senha: " + usuarioDTO.getSenha());

			System.out.println("enviando ...");
			// Enviar mensagem
			Transport.send(message);
			System.out.println("Mensagem enviada com sucesso ....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}

	}

}
