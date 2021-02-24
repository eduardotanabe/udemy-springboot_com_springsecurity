package com.mballem.curso.security.udemyconsultamedico.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mballem.curso.security.udemyconsultamedico.domain.Medico;

public interface MedicoRepository extends JpaRepository<Medico, Long> {

	@Query("SELECT m FROM Medico m WHERE m.usuario.id = :id")
	Optional<Medico> findByUsuarioId(Long id);
	
	@Query("SELECT m FROM Medico m WHERE m.usuario.email LIKE :email")
	Optional<Medico> findByUsuarioEmail(String email);
	
	
}
