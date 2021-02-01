package com.mballem.curso.security.udemyconsultamedico.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.mballem.curso.security.udemyconsultamedico.service.UsuarioService;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UsuarioService service;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {   // esse método irá modificar os pontos padrão onde pede o login pelo Spring Security. 

		http.authorizeRequests()
		.antMatchers("/").permitAll()      // por aqui seleciono as páginas (pela URL) ou os métodos http (GET, POST...) q quer liberar
			.antMatchers("/webjars/**", "/css/**", "/image/**", "/js/**").permitAll()  // como os arquivos html utiliza essas webjars (como o bootstrap), é necessário liberar pois o Spring Security impede a visaulização com essas URL
			.anyRequest().authenticated()
			.and()   // and é um método utilzado para concatenar instruções de diferente tipos
				.formLogin()  // aqui tá indicando que irá iniciar com instruções de login
				.loginPage("/login")
				.defaultSuccessUrl("/", true)
				.failureUrl("/login-error")
				.permitAll()   // para todo usuário poder acessar a página de login
			.and()
				.logout()
				.logoutSuccessUrl("/");
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {    // aqui q é criptografada a senha
		auth.userDetailsService(service).passwordEncoder(new BCryptPasswordEncoder());	
	}
	
	
	
	
}
