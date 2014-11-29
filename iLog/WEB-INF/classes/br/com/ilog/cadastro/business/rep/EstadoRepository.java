package br.com.ilog.cadastro.business.rep;

import java.util.List;

import org.springframework.beans.BeansException;

import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.cadastro.business.entity.Estado;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroEstado;

public interface EstadoRepository extends CrudRepository<Estado> {
	
	public List<Estado> listarEstados( BasicFiltroEstado estado ) throws BeansException;

}
