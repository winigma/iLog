package br.com.ilog.geral.converter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.cits.commons.citsbusiness.util.Identificavel;

/**
 * @author Heber Santiago
 *
 * @param <E extends Identificavel<Long>>
 */
public class SelectManyConverter<E extends Identificavel<Long>> {

	private Object[] values;

	private List<E> source;
	
	private List<E> target;
	
	/**
	 * 
	 * @param s lista de valores a serem selecionados
	 * @param t lista de valores já selecionados
	 */
	public SelectManyConverter( List<E> s, List<E> t ) {
		
		if ( s != null )
			values = new Object[s.size()];
		
		this.source = s;
		this.target = t;
		
		carregarValores();
		
	}
	
	/**
	 * Carrega os valores para o Array Values
	 */
	private void carregarValores() {
		if ( target != null && !target.isEmpty() && source != null ) {
			int i = 0;
			for (E e : target) {
				if ( source.contains(e) ) {
					values[i] = e;
					i++;
				}
			}
		}
		
	}
	/**
	 * @return the values
	 */
	public Object[] getValues() {
		return values;
	}
	/**
	 * @param values the values to set
	 */
	public void setValues(Object[] values) {
		this.values = values;
	}
	/**
	 * @return the source
	 */
	public List<E> getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(List<E> source) {
		this.source = source;
	}
	/**
	 * @return the target
	 */
	public List<E> getTarget() {
		
		if ( values != null ) {
			target = new ArrayList<E>();
			for (Object entity : values) {
				target.add( findById( source, getIdByReflection( entity ) ) );
			}
		}
		return target;
	}
	
	private E findById(Collection<E> collection, Long idToFind) {
		for (E obj : collection) {
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
	
	/**	
	 * @param target the target to set
	 */
	public void setTarget(List<E> target) {
		this.target = target;
	}
}
