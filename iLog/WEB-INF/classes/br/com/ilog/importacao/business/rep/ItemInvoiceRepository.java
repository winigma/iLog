/**
 * 
 */
package br.com.ilog.importacao.business.rep;

import java.util.List;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.importacao.business.entity.Carga;
import br.com.ilog.importacao.business.entity.ItemInvoice;

/**
 * @author Manoel.Neto
 * @ Date 19/09/2012
 *
 */
public interface ItemInvoiceRepository extends CrudRepository<ItemInvoice> {

	public List<ItemInvoice> listarItemInvoicesByCarga(Carga carga) throws BusinessException;

}
