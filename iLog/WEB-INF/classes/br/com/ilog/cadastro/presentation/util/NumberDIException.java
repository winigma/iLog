package br.com.ilog.cadastro.presentation.util;

import java.util.List;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.exception.CodigoErro;

public class NumberDIException extends BusinessException {

	private List<String> erroDIs; 
	
	/** */
	private static final long serialVersionUID = 4103191168498034817L;
	
	public NumberDIException(CodigoErro codigo, List<String> erroDIs) {
		super(codigo);
		this.erroDIs = erroDIs;
	}

	/**
	 * @return the erroDIs
	 */
	public List<String> getErroDIs() {
		return erroDIs;
	}

	/**
	 * @param erroDIs the erroDIs to set
	 */
	public void setErroDIs(List<String> erroDIs) {
		this.erroDIs = erroDIs;
	}
	
}
