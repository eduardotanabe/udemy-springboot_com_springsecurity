package com.mballem.curso.security.udemyconsultamedico.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mballem.curso.security.udemyconsultamedico.datatables.Datatables;
import com.mballem.curso.security.udemyconsultamedico.datatables.DatatablesColunas;
import com.mballem.curso.security.udemyconsultamedico.domain.Perfil;
import com.mballem.curso.security.udemyconsultamedico.domain.Usuario;
import com.mballem.curso.security.udemyconsultamedico.repository.UsuarioRepository;

import ch.qos.logback.core.joran.conditional.ThenOrElseActionBase;

@Service
public class UsuarioService implements UserDetailsService {   // O UserDetailsService que faz os testes das credenciais

	@Autowired
	private UsuarioRepository repository;
	
	@Autowired
	private Datatables datatables;
	
	@Transactional(readOnly = true)
	public Usuario buscarPorEmail(String email) {
		return repository.findByEmail(email);
	} 

	@Override
	@Transactional(readOnly = true)
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

	@Transactional(readOnly = true)
	public Map<String, Object> buscarTodos(HttpServletRequest request) {
		datatables.setRequest(request);
		datatables.setColunas(DatatablesColunas.USUARIOS);
		Page<Usuario> page = datatables.getSearch().isEmpty()
						? repository.findAll(datatables.getPageable())
						: repository.findByEmailOrPerfil(datatables.getSearch(), datatables.getPageable());
		return datatables.getResponse(page);
	}

	@Transactional(readOnly = false)
	public void salvarUsuario(Usuario usuario) {
		String cript = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(cript);
		
		repository.save(usuario);
	}

	@Transactional(readOnly = true)
	public Usuario buscarPorId(Long id) {

		return repository.findById(id).get();
	}

	@Transactional(readOnly = true)
	public Usuario buscarPorIdEPerfis(Long usuarioId, Long[] perfisId) {
		
		return repository.findByIdAndPerfis(usuarioId, perfisId)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário inexistente!"));  // Para que o seja lançada essa exception quando é inserido usuarioId inexistente, foi mudado o tipo de retorno no UsuarioRepository para Optional. 
																							// esta classe possui o método .orElseThrow() que irá lançar o tipo de exception que queremos se aparecer uma exception na consulta. Isso foi importante para poder ser tratado na classe ExceptionController que foi criada
	}

	public static boolean isSenhaCorreta(String senhaDigitada, String senhaArmazenada) {
		
		return new BCryptPasswordEncoder().matches(senhaDigitada, senhaArmazenada);
	}

	@Transactional(readOnly = false)
	public void alterarSenha(Usuario usuario, String senha) {
		usuario.setSenha(new BCryptPasswordEncoder().encode(senha));
		repository.save(usuario);
	}
	
}
