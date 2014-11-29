package br.com.ilog.cadastro.business.rep;

import java.util.List;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.cadastro.business.entity.PessoaJuridica;
import br.com.ilog.cadastro.business.entity.TipoPessoaEnum;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroPessoaJuridica;

public interface PessoaJuridicaRepository extends
		CrudRepository<PessoaJuridica> {

	public List<PessoaJuridica> listarPessoasByType(
			BasicFiltroPessoaJuridica filtro) throws BusinessException;

	public List<PessoaJuridica> listaPessoasByFilter(
			BasicFiltroPessoaJuridica filtro) throws BusinessException;

	public List<PessoaJuridica> listarAllFornecedores(String fornecedor)
			throws BusinessException;

	public List<PessoaJuridica> listarPessoasByTipo(TipoPessoaEnum tipo)
			throws BusinessException;

	public List<PessoaJuridica> listarAllFornecedoresByStatus(Boolean status)
			throws BusinessException;

	public List<PessoaJuridica> listarAllImportadoresByStatus(Boolean status)
			throws BusinessException;

	/**
	 * @param filtro
	 * @return
	 * @throws BusinessException
	 */
	public PessoaJuridica getPessoaBySap(BasicFiltroPessoaJuridica filtro)
			throws BusinessException;
	
	public PessoaJuridica getPessoaJuridicaBySapVendor(String s) throws BusinessException;
}
