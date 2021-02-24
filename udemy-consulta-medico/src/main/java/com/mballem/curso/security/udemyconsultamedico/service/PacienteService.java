package com.mballem.curso.security.udemyconsultamedico.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mballem.curso.security.udemyconsultamedico.domain.Paciente;
import com.mballem.curso.security.udemyconsultamedico.repository.PacienteRepository;

@Service
public class PacienteService {
	
	@Autowired
	private PacienteRepository repository;
	
	@Transactional(readOnly = true)
	public Paciente buscarPorUsuarioEmail(String email) {
		
		return repository.findByUsuarioEmail(email).orElse(new Paciente());   // coloca esse .orElse() para não ter um nullpointerexception no PacienteController
	}

	@Transactional(readOnly = false)
	public void salvar(Paciente paciente) {
		
		repository.save(paciente);
	}

	@Transactional(readOnly = false)
	public void editar(Paciente paciente) {
		Paciente p2 = repository.findById(paciente.getId()).get();
		p2.setNome(paciente.getNome());
		p2.setDtNascimento(paciente.getDtNascimento());
	}
	
	
	
}
