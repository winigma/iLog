/**
 * 
 */
package br.com.ilog.importacao.business.rep;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.cadastro.business.entity.CheckList;
import br.com.ilog.importacao.business.entity.Invoice;
import br.com.ilog.importacao.business.entity.InvoiceChecklist;

/**
 * @author cits
 *
 */
public interface InvoiceChecklistRepository extends CrudRepository<InvoiceChecklist> {

	public InvoiceChecklist getInvoiceChecklist(Invoice invoice, CheckList checklist) throws BusinessException;
	public InvoiceChecklist getInvoiceChecklist(Invoice invoice) throws BusinessException;

}
