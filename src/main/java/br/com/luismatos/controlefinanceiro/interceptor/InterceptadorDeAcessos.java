package br.com.luismatos.controlefinanceiro.interceptor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import br.com.luismatos.controlefinanceiro.model.Usuario;



@SuppressWarnings("deprecation")
public class InterceptadorDeAcessos extends HandlerInterceptorAdapter {

	public static List<Acesso> acessos = new ArrayList<Acesso>();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Acesso acesso = new Acesso();
	
		acesso.path = request.getRequestURI();
		acesso.data = LocalDateTime.now();
		
		try {
			Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			acesso.nome = usuario.getNome();
			acesso.id = usuario.getId();
			acesso.email = usuario.getEmail();
		}catch (Exception e) {
			
		}
		

		request.setAttribute("acesso", acesso);

		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		Acesso acesso = (Acesso) request.getAttribute("acesso");
		acesso.duracao = Duration.between(acesso.data, LocalDateTime.now());
		acessos.add(acesso);
		System.out.println(acesso);
	}

	public static class Acesso {
		private Integer id;
		private String path;
		private LocalDateTime data;
		private Duration duracao;
		private String nome;
		private String email;

		
		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getNome() {
			return nome;
		}

		public void setNome(String nome) {
			this.nome = nome;
		}

		@Override
		public String toString() {
			return "Acesso [id=" + id + ", path=" + path + ", data=" + data + ", duracao=" + duracao + ", nome=" + nome
					+ ", email=" + email + "]";
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public LocalDateTime getData() {
			return data;
		}

		public void setData(LocalDateTime data) {
			this.data = data;
		}

		public Duration getDuracao() {
			return duracao;
		}

		public void setDuracao(Duration duracao) {
			this.duracao = duracao;
		}

	}

}
