package com.mballem.curso.security.udemyconsultamedico.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mballem.curso.security.udemyconsultamedico.domain.Medico;
import com.mballem.curso.security.udemyconsultamedico.domain.Usuario;
import com.mballem.curso.security.udemyconsultamedico.service.MedicoService;
import com.mballem.curso.security.udemyconsultamedico.service.UsuarioService;

@Controller
@RequestMapping("medicos")
public class MedicoController {
	
	@Autowired
	private MedicoService service;

	@Autowired
	private UsuarioService usuarioService;

	// abrir página de dados pessoais de médicos pelo MEDICO
	@GetMapping({"/dados"})
	public String abrirPorMedico(Medico medico, ModelMap model, @AuthenticationPrincipal User user) {
		if (medico.hasNotId()) {
			medico = service.buscarPorEmail(user.getUsername());
			model.addAttribute("medico", medico);
		}
		return "medico/cadastro";
	}
	
	// salvar médico
	@PostMapping({"/salvar"})
	public String salvar(Medico medico, RedirectAttributes attr,
			@AuthenticationPrincipal User user) {  // forma para que o Spring security fornece informações sobre o acesso
		if (medico.hasNotId() && medico.getUsuario().hasNotId()) {
			Usuario usuario = usuarioService.buscarPorEmail(user.getUsername());
			medico.setUsuario(usuario);
		}
		service.salvar(medico);
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso!");
		attr.addFlashAttribute("medico", medico);
		return "redirect:/medicos/dados";
	}
		
	// editar médico
	@PostMapping({"/editar"})
	public String editar(Medico medico, RedirectAttributes attr) {
		service.editar(medico);
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso!");
		attr.addFlashAttribute("medico", medico);
		return "redirect:/medicos/dados";
	}

	// excluir especialidade
	@GetMapping({"/id/{idMed}/excluir/especializacao/{idEsp}"})
	public String excluirEspecialidadePorMedico(@PathVariable("idMed") Long idMed, @PathVariable("idEsp") Long idEsp, RedirectAttributes attr) {
		service.excluirEspecialidadePorMedico(idMed, idEsp);
		attr.addFlashAttribute("sucesso", "Especialidade removida com sucesso!");
		return "redirect:/medicos/dados";
	}
}
