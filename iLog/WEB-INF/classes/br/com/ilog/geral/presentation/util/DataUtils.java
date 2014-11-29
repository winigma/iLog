package br.com.ilog.geral.presentation.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.cits.commons.citsbusiness.util.DateUtils;

public class DataUtils extends DateUtils {

	public static Date converteData( String data, String padrao ) {
		
		try {
			if ( data.trim().equals( "" ) ) {
				return null;
			}
			SimpleDateFormat sdf = new SimpleDateFormat(padrao);
			return sdf.parse(data);
			
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * Metodo soma quantidade de dias de uma determinada data.
	 * 
	 * @param data data base da soma
	 * @param dias quantidade a ser somada
	 * @return
	 */
	public static Date somarDias( Date data, int dias ) {
		
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		
		c.add(Calendar.DAY_OF_YEAR, dias);
		
		return c.getTime();
	}
	/**
	 * Metodo soma quantidade de dias uteis apos uma determinada data, metodo verifica apenas se possui Sabado e Domingo.
	 * 
	 * @param data data base da soma
	 * @param dias quantidade a ser somada
	 * @return
	 */
	public static Date somarDiasUteis( Date data, int dias ) {
		
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		
		int diaAux = 1;
		while ( diaAux <= dias ) {
			
			c.add(Calendar.DAY_OF_YEAR, 1);
			
			//verificar se o dia e sabado ou domingo e pula
			if ( c.get( Calendar.DAY_OF_WEEK ) != Calendar.SATURDAY
					&& c.get( Calendar.DAY_OF_WEEK ) != Calendar.SUNDAY ) {
				diaAux++;
			}
			
		}
		
		
		return c.getTime();
	}

	/**
	 * Retorna a diferenca em dias das respectivas datas.
	 * 
	 * @param inicio
	 * @param fim
	 * @return 0 se inicio igual ao fim, > 0 para inicio menor que fim, e < 0 se
	 *         inicio maior que fim
	 */
	public static int diferencaEmDias( Date inicio, Date fim ) {
		
		int qtdeDias = 0;
		
		if ( inicio != null && fim != null ) {
			
			Calendar c = Calendar.getInstance();
			if ( inicio.before( fim ) ) {
				c.setTime( inicio );
				
				while( compararApenasData(c.getTime(), fim) < 0 ) {
					
					qtdeDias++;
					c.add(Calendar.DAY_OF_YEAR, 1);
				}
			} else {
				c.setTime( fim );
				while( compararApenasData(c.getTime(), inicio) < 0 ) {
					
					qtdeDias++;
					c.add(Calendar.DAY_OF_YEAR, 1);
				}
				qtdeDias = qtdeDias * ( -1 );
			}
		}
		
		return qtdeDias;
	}

}
