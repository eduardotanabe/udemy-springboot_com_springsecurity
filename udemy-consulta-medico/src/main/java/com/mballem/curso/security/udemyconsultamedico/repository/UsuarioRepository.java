package com.mballem.curso.security.udemyconsultamedico.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mballem.curso.security.udemyconsultamedico.domain.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	@Query("SELECT u FROM Usuario u WHERE u.email like :email")
	Usuario findByEmail(@Param("email") String email);

	@Query("SELECT u FROM Usuario u "
			+ "JOIN u.perfis p "
			+ "WHERE u.email like :search% OR p.desc like :search%")
	Page<Usuario> findByEmailOrPerfil(String search, Pageable pageable);

	@Query("SELECT u FROM Usuario u "
			+ "JOIN u.perfis p "
			+ "WHERE u.id = :usuarioId AND p.id IN :perfisId")   // a palavra IN desta linha é usado pq pode ter uma lista de IDs
	Optional<Usuario> findByIdAndPerfis(Long usuarioId, Long[] perfisId);

}
