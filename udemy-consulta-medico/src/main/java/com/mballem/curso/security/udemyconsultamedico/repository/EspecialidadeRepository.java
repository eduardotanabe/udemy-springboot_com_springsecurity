package com.mballem.curso.security.udemyconsultamedico.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mballem.curso.security.udemyconsultamedico.domain.Especialidade;

public interface EspecialidadeRepository extends JpaRepository<Especialidade, Long>{

	@Query("SELECT e FROM Especialidade e WHERE e.titulo like :search%")
	Page<Especialidade> findAllByTitulo(@Param("search") String search, Pageable pageable);

	@Query("SELECT e.titulo FROM Especialidade e WHERE e.titulo like :termo%")
	List<String> findEspecialidadesByTermo(String termo);

	@Query("SELECT e FROM Especialidade e WHERE e.titulo IN :titulos")  // como será um array de titulos q está testando, por isso coloca o termo "IN"
	Set<Especialidade> findByTitulos(String[] titulos);

	@Query("SELECT e FROM Especialidade e "
			+ "JOIN e.medicos m "
			+ "WHERE m.id = :id") 
	Page<Especialidade> findByIdMedico(Long id, Pageable pageable);

}
