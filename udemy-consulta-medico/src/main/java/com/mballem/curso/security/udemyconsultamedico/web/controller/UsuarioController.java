package com.mballem.curso.security.udemyconsultamedico.web.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mballem.curso.security.udemyconsultamedico.domain.Medico;
import com.mballem.curso.security.udemyconsultamedico.domain.Perfil;
import com.mballem.curso.security.udemyconsultamedico.domain.PerfilTipo;
import com.mballem.curso.security.udemyconsultamedico.domain.Usuario;
import com.mballem.curso.security.udemyconsultamedico.service.MedicoService;
import com.mballem.curso.security.udemyconsultamedico.service.UsuarioService;

@Controller
@RequestMapping("u")
public class UsuarioController {
	
	@Autowired
	private UsuarioService service;
	
	@Autowired
	private MedicoService medicoService;
	
	// abrir cadastro de usuário (medico/admin/paciente)
	@GetMapping("/novo/cadastro/usuario")
	public String cadastroPorAdminParaAdminMedicoPaciente(Usuario usuario) {
		return "usuario/cadastro";
	}
	
	// abrir lista de usuários
	@GetMapping("/lista")
	public String listarUsuario() {
		return "usuario/lista";
	}

	// listar usuários na datatables
	@GetMapping("/datatables/server/usuarios")
	public ResponseEntity<?> listarUsuarioDatatables(HttpServletRequest request) {
		
		return ResponseEntity.ok(service.buscarTodos(request));
	}
	
	// salvar cadastro de usuários por administrador
	@PostMapping("/cadastro/salvar")
	public String salvarUsuarios(Usuario usuario, RedirectAttributes attributes) {
		List<Perfil> perfis = usuario.getPerfis();
		if (perfis.size() > 2 ||
				perfis.containsAll(Arrays.asList(new Perfil(1L), new Perfil(3L))) ||  // 1L refere-se ao perfil Administrador e 3L à Paciente
				perfis.containsAll(Arrays.asList(new Perfil(2L), new Perfil(3L))) ) {  
			attributes.addFlashAttribute("falha", "Paciente não pode ser Administrador e/ou Médico.");
			attributes.addFlashAttribute("usuario", usuario); // essa parte volta para a página usuário, junto com o redirecionamento
		} else {
			try {
				service.salvarUsuario(usuario);
				attributes.addFlashAttribute("sucesso", "Operação realizada com sucesso!");
				
			} catch (DataIntegrityViolationException ex) {
				attributes.addFlashAttribute("falha", "Cadastro não realizado, email já existente.");
			}
		}
		
		return "redirect:/u/novo/cadastro/usuario";
	}
	
	// fornecer página para edição de credenciais de usuários
	@GetMapping("/editar/credenciais/usuario/{id}")
	public ModelAndView preEditarCredenciais(@PathVariable("id") Long id) {
		
		return new ModelAndView("usuario/cadastro", "usuario", service.buscarPorId(id));   // o primeiro é a página que irá ser aberta, o segundo é o nome da variável que será utilizado para enviar os dados do cadastro e o terceiro seria a busca dos dados do usuário
	}
	
	// pré-edição dos dados cadastrais dos usuários 
	@GetMapping("/editar/dados/usuario/{id}/perfis/{perfis}")
	public ModelAndView preEditarCadastroDadosPessoais(@PathVariable("id") Long usuarioId, @PathVariable("perfis") Long[] perfisId) {
		
		Usuario usuario = service.buscarPorIdEPerfis(usuarioId, perfisId);
		
		if (usuario.getPerfis().contains(new Perfil(PerfilTipo.ADMIN.getCod())) && 
				!usuario.getPerfis().contains(new Perfil(PerfilTipo.MEDICO.getCod()))) {
			return new ModelAndView("usuario/cadastro", "usuario", usuario);
		   
		} else if(usuario.getPerfis().contains(new Perfil(PerfilTipo.MEDICO.getCod()))) {

			Medico medico = medicoService.buscarPorUsuarioId(usuarioId);
			
			return medico.hasNotId()
					? new ModelAndView("medico/cadastro", "medico", new Medico(new Usuario(usuarioId)))
					: new ModelAndView("medico/cadastro", "medico", medico);
			
		} else if(usuario.getPerfis().contains(new Perfil(PerfilTipo.PACIENTE.getCod()))) {
			ModelAndView model = new ModelAndView("error");
			model.addObject("status", 403);
			model.addObject("error", "Área Restrita");
			model.addObject("message", "Os dados de pacientes são restritos a ele.");
			return model;
		}
		return new ModelAndView("redirect:/u/lista");
	}
			
	@GetMapping("/editar/senha")
	public String abrirEditarSenha() {
		
		return "usuario/editar-senha";
	}
	
	@PostMapping("/confirmar/senha")
	public String editarSenha(@RequestParam("senha1") String s1, @RequestParam("senha2") String s2, 
								@RequestParam("senha3") String s3, @AuthenticationPrincipal User user,
								RedirectAttributes attr) {
		if (!s1.equals(s2)) {
			attr.addFlashAttribute("falha", "Senhas não conferem, tente novemante.");
			return "redirect:/u/editar/senha";
		}
		
		Usuario u = service.buscarPorEmail(user.getUsername());
		if (!UsuarioService.isSenhaCorreta(s3, u.getSenha())) {
			attr.addFlashAttribute("falha", "Senha atual não confere, tente novemante.");
			return "redirect:/u/editar/senha";
		}
		
		service.alterarSenha(u, s1);
		attr.addFlashAttribute("sucesso", "Senha alterada com sucesso!");
		return "redirect:/u/editar/senha";
	}
}
