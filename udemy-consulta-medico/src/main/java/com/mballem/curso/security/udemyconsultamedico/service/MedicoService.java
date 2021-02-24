package com.mballem.curso.security.udemyconsultamedico.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mballem.curso.security.udemyconsultamedico.domain.Medico;
import com.mballem.curso.security.udemyconsultamedico.repository.MedicoRepository;

@Service
public class MedicoService {

	@Autowired
	private MedicoRepository repository;
	
	@Transactional(readOnly = true)
	public Medico buscarPorUsuarioId(Long id) {
		
		return repository.findByUsuarioId(id).orElse(new Medico());   // o método .orElse() será executado quando se não encontrar um médico ele retornará uma instância de um médico para criar um médico
	}

	@Transactional(readOnly = false)
	public void salvar(Medico medico) {
		
		repository.save(medico);		
	}

	@Transactional(readOnly = false)
	public void editar(Medico medico) {   // como o médico m2 está no estado persistente, não preciso realizar repository.save(), mas se fizer não terá problema
		Medico m2 = repository.findById(medico.getId()).get();
		m2.setCrm(medico.getCrm());
		m2.setDtInscricao(medico.getDtInscricao());
		m2.setNome(medico.getNome());
		
		if (!medico.getEspecialidades().isEmpty()) {
			m2.getEspecialidades().addAll(medico.getEspecialidades());
		}
	}

	@Transactional(readOnly = true)
	public Medico buscarPorEmail(String email) {
		
		return repository.findByUsuarioEmail(email).orElse(new Medico());
	}

	@Transactional(readOnly = false)
	public void excluirEspecialidadePorMedico(Long idMed, Long idEsp) {
		Medico medico = repository.findById(idMed).get();   // com isso o objeto estará em estado persistente
		medico.getEspecialidades().removeIf(e -> e.getId().equals(idEsp));
		
	}
}
