package br.com.ilog.geral.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import br.com.ilog.geral.presentation.SimNao;


public class ConverterSimNao implements Converter{
	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		try{
			return (SimNao.SIM.getLabel().equalsIgnoreCase(arg2)?true:false);
		}catch (Exception e) {
			throw new ConverterException(e);
		}
		
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		try{
			return SimNao.getLabel(new Boolean(arg2.toString()).booleanValue());
		}catch (Exception e) {
			throw new ConverterException(e);
		}

	}
	
}
