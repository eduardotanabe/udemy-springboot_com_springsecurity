package com.mballem.curso.security.udemyconsultamedico.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mballem.curso.security.udemyconsultamedico.domain.Especialidade;

public interface EspecialidadeRepository extends JpaRepository<Especialidade, Long>{

	@Query("SELECT e FROM Especialidade e WHERE e.titulo like :search%")
	Page<Especialidade> findAllByTitulo(@Param("search") String search, Pageable pageable);

}
