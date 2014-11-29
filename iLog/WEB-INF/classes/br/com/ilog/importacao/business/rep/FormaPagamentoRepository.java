package br.com.ilog.importacao.business.rep;

import java.util.List;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.cadastro.business.entity.FormaPagamento;

public interface FormaPagamentoRepository extends
		CrudRepository<FormaPagamento> {

	/**
	 * @param codeSAP
	 * @return
	 * @throws BusinessException
	 */
	public FormaPagamento getBycodeSAP(String codeSAP) throws BusinessException;

	/**
	 * @return
	 * @throws BusinessException
	 */
	public List<FormaPagamento> listarFormasPagamentoAtivos() throws BusinessException;


}
