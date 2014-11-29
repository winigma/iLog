package br.com.ilog.importacao.business.entityFilter;

import java.io.Serializable;

import br.com.ilog.cadastro.business.entity.Filial;
import br.com.ilog.cadastro.business.entity.FormaPagamento;
import br.com.ilog.cadastro.business.entity.PessoaJuridica;

/**
 * 
 * @author Manoel Neto
 * @coment Classe de Filtro da Pesquisar Importar PO
 * 
 */
public class BasicFiltroPO implements Serializable {

	private static final long serialVersionUID = -4248934232657757865L;
	
	private String numPO;
	private String partNumber;
	private PessoaJuridica fornecedor;
	private FormaPagamento formaPagamento;
	private Filial filial;
	/**
	 * @return the numPO
	 */
	public String getNumPO() {
		return numPO;
	}
	/**
	 * @param numPO the numPO to set
	 */
	public void setNumPO(String numPO) {
		this.numPO = numPO;
	}
	/**
	 * @return the partNumber
	 */
	public String getPartNumber() {
		return partNumber;
	}
	/**
	 * @param partNumber the partNumber to set
	 */
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}
	/**
	 * @return the fornecedor
	 */
	public PessoaJuridica getFornecedor() {
		return fornecedor;
	}
	/**
	 * @param fornecedor the fornecedor to set
	 */
	public void setFornecedor(PessoaJuridica fornecedor) {
		this.fornecedor = fornecedor;
	}
	/**
	 * @return the formaPagamento
	 */
	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}
	/**
	 * @param formaPagamento the formaPagamento to set
	 */
	public void setFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}
	/**
	 * @return the filial
	 */
	public Filial getFilial() {
		return filial;
	}
	/**
	 * @param filial the filial to set
	 */
	public void setFilial(Filial filial) {
		this.filial = filial;
	} 
	

}
