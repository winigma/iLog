package br.com.ilog.importacao.business.rep;

import java.util.List;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.importacao.business.entity.AnexoInvoice;

public interface AnexoInvoiceRepository extends CrudRepository<AnexoInvoice> {

	public void adicionarAnexosInvoice(List<AnexoInvoice> anexos) throws BusinessException;

	public void excluirAnexoInvoice(List<AnexoInvoice> removeListAnexos) throws BusinessException;

}
