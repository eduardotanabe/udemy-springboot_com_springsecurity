package com.mballem.curso.security.udemyconsultamedico.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mballem.curso.security.udemyconsultamedico.domain.Perfil;
import com.mballem.curso.security.udemyconsultamedico.domain.Usuario;
import com.mballem.curso.security.udemyconsultamedico.repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {   // O UserDetailsService que faz os testes das credenciais

	@Autowired
	private UsuarioRepository repository;
	
	@Transactional(readOnly = true)
	public Usuario buscarPorEmail(String email) {
		return repository.findByEmail(email);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {   // aqui q faz os testes de login
		Usuario usuario = buscarPorEmail(username);
		return new User(
					usuario.getEmail(),
					usuario.getSenha(),
					AuthorityUtils.createAuthorityList(this.getAuthorities(usuario.getPerfis()))   // o método .createAuthorityList aceita apenas uma array de String (String[])
					);   
	}
	
	private String[] getAuthorities(List<Perfil> perfis) {    // a intenção deste método é transformar uma List em um array de String (String[])
		String[] authorities = new String[perfis.size()];
		for (int i = 0; i < perfis.size(); i++) {
			authorities[i] = perfis.get(i).getDesc();
		}
		return authorities;
 	}
	
}
