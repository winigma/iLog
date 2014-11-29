package br.com.ilog.importacao.business.rep;

import java.util.List;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.importacao.business.entity.CustoDI;

/**
 * Interface de CustoDI
 * @author Heber Santiago
 *
 */
public interface CustoDIRepository extends CrudRepository<CustoDI> {
	
	public List<CustoDI> listarCustos( Long idCarga, String tipoFatura ) throws BusinessException;
	
}
