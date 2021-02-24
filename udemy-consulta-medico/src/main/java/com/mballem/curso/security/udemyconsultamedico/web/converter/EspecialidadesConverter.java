package com.mballem.curso.security.udemyconsultamedico.web.converter;


import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.mballem.curso.security.udemyconsultamedico.domain.Especialidade;
import com.mballem.curso.security.udemyconsultamedico.service.EspecialidadeService;


// Foi criada essa classe pq no autocomplete precisa de objetos do tipo Especialidade e não uma String
@Component
public class EspecialidadesConverter implements Converter<String[], Set<Especialidade>>{

	@Autowired
	private EspecialidadeService service;
	
	@Override
	public Set<Especialidade> convert(String[] titulos) {  // é uma array de String pq no input do autocomplete das especialidades do médico é desse tipo

		Set<Especialidade> especialidades = new HashSet<>();
		if (titulos != null && titulos.length > 0) {
			especialidades.addAll(service.buscarPorTitulos(titulos));
		}
		return especialidades;
	}

}
