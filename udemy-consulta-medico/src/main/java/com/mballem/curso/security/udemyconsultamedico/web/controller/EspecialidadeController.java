package com.mballem.curso.security.udemyconsultamedico.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mballem.curso.security.udemyconsultamedico.domain.Especialidade;
import com.mballem.curso.security.udemyconsultamedico.service.EspecialidadeService;

@Controller
@RequestMapping("especialidades")
public class EspecialidadeController {

	@Autowired
	private EspecialidadeService service;
	
	@GetMapping({"", "/"})
	public String abrir(Especialidade especialidade) {
		
		return "especialidade/especialidade";    
	}
	
	@PostMapping("/salvar")
	public String salvar(Especialidade especialidade, RedirectAttributes attribute) {
		service.salvar(especialidade);
		attribute.addFlashAttribute("sucesso", "Operação realizada com sucesso!");
		return "redirect:/especialidades";		
	}
	
	@GetMapping("/datatables/server")     // no arquivo especialidades.js tem esse caminho
	public ResponseEntity<?> getEspecialidades(HttpServletRequest request) {
		
		return ResponseEntity.ok(service.buscarEspecialidades(request));    
	}
	
	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") Long id, ModelMap model) {
		model.addAttribute("especialidade", service.buscarPorId(id));
		return "especialidade/especialidade";    
	}
	
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, RedirectAttributes attributes) {
		service.remover(id);
		attributes.addFlashAttribute("sucesso", "Operação realizada com sucesso.");
		return "redirect:/especialidades";    
	}
	
	@GetMapping("/titulo")     // no arquivo especialidades.js tem esse caminho
	public ResponseEntity<?> getEspecialidadesPorTermo(@RequestParam("termo") String termo) {
		List<String> especialidades = service.buscarEspecialidadeByTermo(termo);  // é termo pq através d partes do q o usuário está digitando irá apresentar os tipos de especialidades correpondentes
		return ResponseEntity.ok(especialidades);    
	}
	
	@GetMapping("/datatables/server/medico/{id}")     // no arquivo cadastro.html do médico, na parte do inline js, tem esse caminho
	public ResponseEntity<?> getEspecialidadesPorMedico(@PathVariable("id") Long id, HttpServletRequest request) {

		return ResponseEntity.ok(service.buscarEspecialidadesPorMedico(id, request));    
	}
	
}
