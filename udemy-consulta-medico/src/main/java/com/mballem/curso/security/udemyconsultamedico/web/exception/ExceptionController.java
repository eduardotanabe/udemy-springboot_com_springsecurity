package com.mballem.curso.security.udemyconsultamedico.web.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionController {

	@ExceptionHandler(UsernameNotFoundException.class)  // essa anotação irá escutar as exceções com o argumento inserido, daí quando aparecer essa exceção, ele irá executar o método abaixo
	public ModelAndView usuarioNaoEncontradoException(UsernameNotFoundException ex) {
		ModelAndView model = new ModelAndView("error");  // o argumento do "error" refere-se ao error.html
		model.addObject("status", 404);
		model.addObject("error", "Operação não pode ser realizada.");
		model.addObject("message", ex.getMessage());
		return model;
	}
}
