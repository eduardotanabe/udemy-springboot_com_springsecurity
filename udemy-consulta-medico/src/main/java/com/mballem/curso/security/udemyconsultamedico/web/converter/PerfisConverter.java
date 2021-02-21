package com.mballem.curso.security.udemyconsultamedico.web.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.mballem.curso.security.udemyconsultamedico.domain.Perfil;

@Component
public class PerfisConverter implements Converter<String[], List<Perfil>>{

	
	// como na página cadastro.html utiliza um número no formato String para definir qual perfil o usuário selecionou,
	// será necessário converter esse perfil no objeto Perfil, como realizado abaixo
	@Override
	public List<Perfil> convert(String[] source) {
		List<Perfil> perfis = new ArrayList<Perfil>();
		for (String id : source) {
			if (!id.equals("0")) {    // foi colocado esse teste, pq no cadastro.html (aproximadamente linha 61) foi adicionado para que venha um 0 nos perfis selecionados, pois, se não colocasse isso, o Thymeleaf envia apenas uma String do item selecionado, quebrando esse conversor que precisa de um array  
				perfis.add(new Perfil(Long.parseLong(id)));
			}
			
		}
		return perfis;
	}

}
