package br.com.luismatos.controlefinanceiro.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import br.com.luismatos.controlefinanceiro.model.Usuario;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UsuarioControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UsuarioController usuarioController;


	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(usuarioController).build();
	}
	@Test
	public void testeGetExcluirUsuarioAdm() throws Exception {
		

		Usuario usuario = mock(Usuario.class);
	        Authentication authentication = mock(Authentication.class);
	        SecurityContext securityContext = mock(SecurityContext.class);
	        when(securityContext.getAuthentication()).thenReturn(authentication);
	        SecurityContextHolder.setContext(securityContext);
	        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(usuario);
		
//		
//		Usuario usuario = new Usuario();
//		usuario.setEmail("admin@email.com.br");
//		usuario.setNome("Admin");
//		usuario.setId(1);
//		usuario.setStatus(true);
//		usuario.setSenha("$2a$10$/ENYaePxc9k29E01qs972uWoo15SDmJFZUMn3z1IRhEVrRiRbCQ8G");
//		
//		 UsernamePasswordAuthenticationToken principal = usuario;
//
//	        SecurityContextHolder.getContext().setAuthentication(principal); 
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/usuario/excluir/"))
		.andExpect(MockMvcResultMatchers.flash().attribute("messageError", "Voçê não pode se excluir o usuário administrador do sistema!"));
	}
	
	
	@Test
	public void testePostUsuarioJaExistente() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/usuario/novoUsuario")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("nome", "Novo Usuario Teste")
				        .param("email", "teste2@email.com.br"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.post("/usuario/novoUsuario")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("nome", "Novo Usuario Teste")
		        .param("email", "teste2@email.com.br"))
		.andExpect(MockMvcResultMatchers.flash().attribute("messageError", "Email já cadastrado!"));
	}

	@Test
	public void testePostNovoUsuario() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/usuario/novoUsuario")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("nome", "Novo Usuario Teste")
				        .param("email", "teste@email.com.br"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@Test
	public void testeGetUsuarios() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/usuario/listar"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

}
