package br.com.ilog.cadastro.business.rep;

import java.util.List;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.cadastro.business.entity.Pais;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroPais;



/**
 * @author Wisley P. Souza
 */

public interface PaisRepository extends CrudRepository<Pais> {
	
	//public Pais getPaisById(Long id) throws BusinessException;
	public List<Pais> listarPaises( BasicFiltroPais filtroPais ) throws BusinessException;
	public List<Pais>listarPaises() throws BusinessException;
	public Pais getByPadrao();
} 
