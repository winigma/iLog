package br.com.ilog.cadastro.presentation.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import br.cits.commons.citsbusiness.util.Identificavel;

public class ConverterUtil<E extends Identificavel<?>> implements Converter {

	private Map<Object, Object> mapa;

	/**
	 * 
	 * @param entidades
	 */
	public ConverterUtil(List<E> entidades) {
		mapa = new HashMap<Object, Object>();

		for (E entidade : entidades)
			mapa.put(entidade.getPK().toString(), entidade);
	}

	/**
	 * 
	 * @param entidades
	 */
	public ConverterUtil(E... entidades) {
		mapa = new HashMap<Object, Object>();

		for (E entidade : entidades)
			mapa.put(entidade.getPK().toString(), entidade);
	}

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String value) {
		if (value == null || value.equals("") || value.trim().length() == 0)
			return null;

		return mapa.get(value);
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		if (value == null || value.equals("")
				|| value.toString().trim().length() == 0)
			return "";
		String retorno = null;
		Identificavel<?> identificavel = (Identificavel<?>) value;
		if(identificavel.getPK() !=null) {
			retorno = identificavel.getPK().toString();

		}
		
		return retorno;
	}

}
