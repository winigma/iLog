package br.com.ilog.exportacao.business.entity;

import br.com.ilog.importacao.business.entity.StatusCarga;

public enum StatusInvoice {
	/**
	 * @Legend 
	 *        
	 *         
	 *         C =  Cadastrada
	 *        
	 *         F = Fechado
	 */
			
	
	
	 C, F;
	
	
	public static StatusInvoice getStatus( StatusCarga status ) {
		for (StatusInvoice item : StatusInvoice.values()) {
			if ( item.name().equals( status.name() ) ) {
				return item;
			}
		}
		return null;
	}
	
	public static StatusInvoice[] valores() {
		StatusInvoice[] values = { C, F };
		return values;
	}
	 
}
