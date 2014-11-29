package br.com.ilog.geral.converter;

import java.lang.reflect.Field;
import java.util.Collection;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.primefaces.model.DualListModel;

import br.cits.commons.citsbusiness.util.Identificavel;

@FacesConverter( "pickListConverter" )
public class PickListConverter implements Converter{

	@SuppressWarnings( "unchecked" )
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent component, String value) {
		if (value == null || value.equals(""))
			return null;
		
		try {
			DualListModel<Identificavel<Long>> item = (DualListModel<Identificavel<Long>>) component.getAttributes().get( "value" );
			
			return findById(item.getSource(), value);
		} catch (Exception ex) {
			throw new ConverterException(
					"Não foi possível aplicar conversão de item com valor ["
							+ value + "] no componente [" + component.getId()
							+ "]", ex);
		}

	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		if (value == null)
			return "";
		return getIdByReflection(value).toString();
	}
	
	@SuppressWarnings("rawtypes")
	private Object findById(Collection collection, String idToFind) {
		
		for (Object obj : collection) {
			Long id = getIdByReflection(obj);
			if (id.toString().equals( idToFind.toString() ) )
				return obj;
		}


		return null;
	}

	private Long getIdByReflection(Object bean) {
		try {
			Field idField = bean.getClass().getDeclaredField("id");
			idField.setAccessible(true);
			return (Long) idField.get(bean);
		} catch (Exception ex) {
			throw new RuntimeException(
					"Não foi possível obter a propriedade 'id' do item", ex);
		}
	}

}
