package br.com.ilog.geral.converter;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

public class ConverterMonetario implements Converter{

	@Override
	public BigDecimal getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		return this.getTextAsBigDecimal(arg2);
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		return this.getBigDecimalAsText(arg2);
	}

	private BigDecimal getTextAsBigDecimal(String text){
		if(text!=null && !text.trim().isEmpty()){
			try{
				
				text = text.replace(".", "");
				text = text.trim();
				text = text.replace(',', '.');
				return new BigDecimal(text);
			}catch (Exception e) {
				throw new ConverterException(e);
			}
		}
//		return new BigDecimal(0);
		return null;
	}

	private static final int UMA_CASA_DECIMAL = -2;
	private static final int NENHUMA_CASA_DECIMAL_COM_PONTO = -1;
	private static final int NENHUMA_CASA_DECIMAL_SEM_PONTO = 0;


	private String getBigDecimalAsText(Object obj){
		try{
			if(obj!=null){
				//verificando existencia de duas casas decimais apos a virgula
				String str = obj.toString();
				BigDecimal val = new BigDecimal( str ).setScale( 2, RoundingMode.HALF_EVEN);
				str = val.toString();
				
				int ponto = str.indexOf(".")==-1?0:str.indexOf(".")-str.length();
				switch (ponto) {
				case UMA_CASA_DECIMAL:
					str +="0";
					break;
				case NENHUMA_CASA_DECIMAL_COM_PONTO:
					str+="00";
					break;
				case NENHUMA_CASA_DECIMAL_SEM_PONTO:
					str+=".00";
					break;
				default:
					break;
				}
				
				StringBuilder sbAux = new StringBuilder();
				sbAux.append(str.toString().replace(".", ""));
				char[] values = sbAux.reverse().toString().toCharArray();
				StringBuilder sbFinal = new StringBuilder();
				int pontoOuVirgula = 0;
				for(char c : values){
					if(pontoOuVirgula==2){
						sbFinal.append(',');
					}else
						if((pontoOuVirgula-2)%3==0){
							sbFinal.append('.');
						}
					
					sbFinal.append(c);
					pontoOuVirgula++;
				}
				return sbFinal.reverse().toString();
			}
			
		}catch (Exception e) {
			throw new ConverterException(e);
		}
//		return "0,00";
		return "";
	}

	/*private boolean isOnlyFormatNumbers(String param){
		if(param!=null && !param.trim().isEmpty()){
			char[] values = param.toCharArray();
			for(char ch:values){
				if(!Character.isDigit(ch) && ch!='.' && ch!=','){
					return false;
				}
			}
			
		}
		return true;
	}*/
	
	/*public ConverterMonetario() {
		
	}
	
	public static void main(String[] a){
		new ConverterMonetario();
	}*/

}
