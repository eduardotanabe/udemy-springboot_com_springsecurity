package com.mballem.curso.security.udemyconsultamedico.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.mballem.curso.security.udemyconsultamedico.domain.PerfilTipo;
import com.mballem.curso.security.udemyconsultamedico.service.UsuarioService;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private static final String ADMIN = PerfilTipo.ADMIN.getDesc();
	private static final String MEDICO = PerfilTipo.MEDICO.getDesc();
	private static final String PACIENTE = PerfilTipo.PACIENTE.getDesc();

	@Autowired
	private UsuarioService service;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {   // esse método irá modificar os pontos padrão onde pede o login pelo Spring Security. 

		http.authorizeRequests()
			// acessos públicos liberados
			.antMatchers("/").permitAll()      // por aqui seleciono as páginas (pela URL) ou os métodos http (GET, POST...) q quer liberar
			.antMatchers("/webjars/**", "/css/**", "/image/**", "/js/**").permitAll()  // como os arquivos html utiliza essas webjars (como o bootstrap), é necessário liberar pois o Spring Security impede a visaulização com essas URL
			
			// acessos privados admin
			.antMatchers("/u/**").hasAuthority(ADMIN)
			
			// acessos privados médicos
			.antMatchers("/medicos/dados", "/medicos/salvar", "/medicos/editar").hasAnyAuthority(MEDICO, ADMIN)  // foi necessário colocar o MEDICO. Mesmo q na linha abaixo o MEDICO tenha acesso "/medicos/**", se já tiver essa restrição da presente linha, então tem q colocar MEDICO, senão esse perfil terá acesso a todos da pasta "/medicos/**" exceto os itens desta linha
			.antMatchers("/medicos/**").hasAuthority(MEDICO)
			
			// acessos privados pacientes
			.antMatchers("/pacientes/**").hasAuthority(PACIENTE)
			 
			// acessos privados especialidade
			.antMatchers("/especialidades/**").hasAuthority(ADMIN)
			
			.anyRequest().authenticated()
			.and()   // and é um método utilzado para concatenar instruções de diferente tipos
				.formLogin()  // aqui tá indicando que irá iniciar com instruções de login
				.loginPage("/login")
				.defaultSuccessUrl("/", true)
				.failureUrl("/login-error")
				.permitAll()   // para todo usuário poder acessar a página de login
			.and()
				.logout()
				.logoutSuccessUrl("/")
			.and()
				// métodos quando o acesso é negado nas páginas em que cada perfil não pode acessar, por exemplo, ADMIN não pode acessar página da aba do médico
				.exceptionHandling()
				.accessDeniedPage("/acesso-negado");
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {    // aqui q é criptografada a senha
		auth.userDetailsService(service).passwordEncoder(new BCryptPasswordEncoder());	
	}
	
	
	
	
}
