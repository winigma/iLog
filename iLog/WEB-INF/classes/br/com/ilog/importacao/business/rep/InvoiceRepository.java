package br.com.ilog.importacao.business.rep;

import java.util.List;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.importacao.business.entity.AnexoInvoice;
import br.com.ilog.importacao.business.entity.Carga;
import br.com.ilog.importacao.business.entity.Invoice;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroCarga;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroInvoice;

public interface InvoiceRepository extends CrudRepository<Invoice>{
	public List<Invoice> listarInvoices(BasicFiltroInvoice filtro)throws BusinessException;
	public List<Invoice> listarInvoicesOfValidation(BasicFiltroInvoice filtro)throws BusinessException;
	public List<Invoice> listarInvoicesOfApproved(BasicFiltroInvoice filtro)throws BusinessException;
	public List<Invoice> listarInvoicesPorCarga(Carga carga)throws BusinessException;
	public List<Invoice> listarInvoicesPorCargaPlanejada(Carga carga)throws BusinessException;
	
	void removerAnexoInvoice(AnexoInvoice anexo) throws BusinessException;
	List<Invoice> listarInvoicesPorCarga(Carga carga, boolean carregarItens)
			throws BusinessException;
	public Integer getLastCodeByYear(int ano) throws BusinessException;
	public List<AnexoInvoice> getAnexosInvoice(Invoice invoice) throws BusinessException;
	public List<Invoice> listarInvoices(BasicFiltroCarga filtro) throws BusinessException;
	public Invoice getByNumero(String nrInvoice) throws BusinessException;
	/**
	 * @param filtro
	 * @return
	 * @throws BusinessException
	 */
	public List<Invoice> listarInvoicesComCarga(BasicFiltroInvoice filtro)
			throws BusinessException;

}
