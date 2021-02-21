package com.mballem.curso.security.udemyconsultamedico.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mballem.curso.security.udemyconsultamedico.datatables.Datatables;
import com.mballem.curso.security.udemyconsultamedico.datatables.DatatablesColunas;
import com.mballem.curso.security.udemyconsultamedico.domain.Especialidade;
import com.mballem.curso.security.udemyconsultamedico.repository.EspecialidadeRepository;

@Service
public class EspecialidadeService {

	@Autowired
	private EspecialidadeRepository repository;
	
	@Autowired
	private Datatables datatables;
	
	@Transactional(readOnly = false)
	public void salvar(Especialidade especialidade) {
		
		repository.save(especialidade);
	}

	@Transactional(readOnly = true)
	public Map<String, Object> buscarEspecialidades(HttpServletRequest request) {
		datatables.setRequest(request);
		datatables.setColunas(DatatablesColunas.ESPECIALIDADES);
		
		Page<?> page = datatables.getSearch().isEmpty()   // se estiver vazia quer dizer q o usuário fez consulta sem inserir informações
				? repository.findAll(datatables.getPageable())
				: repository.findAllByTitulo(datatables.getSearch(), datatables.getPageable());     // essa daqui é quando o usuário digitou algo no campo de busca. o método getSearch() pega o conteúdo digitado no campo da busca
		
		return datatables.getResponse(page);
	}

	@Transactional(readOnly = true)
	public Especialidade buscarPorId(Long id) {
		
		return repository.findById(id).get();    // como o método findById(id) retorna um objeto Optional utiliza-se o método get()
	}

	@Transactional(readOnly = false)
	public void remover(Long id) {

		repository.deleteById(id);
	}
}
