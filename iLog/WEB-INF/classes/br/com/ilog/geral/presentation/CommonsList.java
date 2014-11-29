package br.com.ilog.geral.presentation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.springframework.stereotype.Component;

@Component("commonsList")
public class CommonsList implements Serializable {

	
	private static final long serialVersionUID = 1L;

	/**
	 * Metodo auxiliar para gerar um combo de boolean
	 * 
	 * @return
	 * @author allan.braga
	 * @since 21/01/2010
	 */
	public List<SelectItem> listaSimNao(){
		try{
		FacesContext fc = FacesContext.getCurrentInstance();
		
		List<SelectItem> itens = new ArrayList<SelectItem>();
		
		itens.add(new SelectItem(new Boolean(true), TemplateMessageHelper
				.getMessage("lblSim", fc.getViewRoot().getLocale())));
		itens.add(new SelectItem(new Boolean(false), TemplateMessageHelper
				.getMessage("lblNao", fc.getViewRoot().getLocale())));
		
		return itens;}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

	/**
	 * Metodo auxiliar para retornar uma lista de Select Item, com valores
	 * booleans.
	 * 
	 * @author Heber Santiago 
	 * @date 01/07/2011
	 * @return
	 */
	public List<SelectItem> listaBooleanAtivoInativo() {
		List<SelectItem> comboStatus = new ArrayList<SelectItem>();
		FacesContext fc = FacesContext.getCurrentInstance();
		
		comboStatus.add(new SelectItem(true, TemplateMessageHelper.getMessage(
				MensagensSistema.STATUS, Boolean.TRUE.toString(), fc.getViewRoot().getLocale() ) ) );
		comboStatus.add(new SelectItem(false, TemplateMessageHelper.getMessage(
				MensagensSistema.STATUS, Boolean.FALSE.toString(), fc.getViewRoot().getLocale() ) ) );

		return comboStatus;
	}

}
