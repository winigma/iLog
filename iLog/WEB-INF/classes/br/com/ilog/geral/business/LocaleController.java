package br.com.ilog.geral.business;

import java.io.Serializable;
import java.util.Locale;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Wisley Souza
 * @coment Classe de controle de Locale pra internacionalização
 * 
 */
@Scope("session")
@Component("localeController")
public class LocaleController implements Serializable{

	
	private static final long serialVersionUID = 1L;
	private Locale currentLocale = FacesContext.getCurrentInstance().getViewRoot().getLocale();  
	private static Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();  
	  
	 public void englishLocale() {  
	  UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();  
	  currentLocale = Locale.US;
	  locale = currentLocale;
	  viewRoot.setLocale(currentLocale);  
	 }  
	  
	 public void portugueseLocale() {  
	  UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();  
	  currentLocale = new Locale("pt", "BR");
	  locale = currentLocale;
	  viewRoot.setLocale(currentLocale);  
	 }  
	  
	 public Locale getCurrentLocale() {  
	  return currentLocale;  
	 }
	 public static Locale getLocale() {
		 	return locale;
		}

	/**
	 * @param locale the locale to set
	 */
	public static void setLocale(Locale locale) {
		LocaleController.locale = locale;
	}


}
