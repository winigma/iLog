package br.com.ilog.geral.presentation.util;

import java.io.Serializable;
import java.math.BigDecimal;

public class BigDecimalUtils implements Serializable {

	/** */
	private static final long serialVersionUID = 2464722842980834424L;

	/**
	 * Transforma uma string em BigDecimal.
	 * @param valor <code> Ex: 0000045000</code>
	 * @param scala <code> Ex: 4</code>
	 * @return Ex: new {@link BigDecimal}( 4.5000 )
	 */
	public static BigDecimal parserToBigDecimal( String valor, int scala ) {
		
		try {
			
			valor = valor.subSequence(0, valor.length() - scala ) + "." + valor.substring( valor.length() - scala );
			BigDecimal bd = new BigDecimal(valor);
			bd.setScale(scala);
			
			return bd;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
}
