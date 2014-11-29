package br.com.ilog.geral.presentation;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citspresentation.messages.MessageHelper;
import br.com.ilog.cadastro.presentation.util.NumberDIException;
import br.com.ilog.geral.business.LocaleController;


public class TemplateMessageHelper extends MessageHelper {
	
	public static String getMessage(String messageId) {
		return getMessage(MensagensSistema.SISTEMA, messageId, LocaleController.getLocale());
	}
	public static String getMessage(String messageId, Locale locale) {
		return getMessage(MensagensSistema.SISTEMA, messageId, locale);
	}
	
	public static String getMessage(MensagensSistema mensagensSistema, String messageId) {
		String retorno = getMessage(mensagensSistema, messageId, LocaleController.getLocale());
		if (StringUtils.isBlank(retorno) && mensagensSistema != MensagensSistema.SISTEMA) {
			retorno = getMessage(messageId);
		} 
		return retorno;
	}
	
	public static String getMessage( MensagensSistema sistema, String messageID, List<String> params, Locale locale ) {
		String retorno = getMessage(sistema, messageID, locale );
		if ( params != null && !params.isEmpty() ) {
			retorno = TemplateMessageHelper.formatarParamMsg(retorno, params);
		}
		return retorno;
	}
	
	public static String getMessage(MensagensSistema mensagensSistema, String messageId, Locale locale) {
		String retorno = MessageHelper.getMessage(mensagensSistema, messageId, locale);
		if (StringUtils.isBlank(retorno) && mensagensSistema != MensagensSistema.SISTEMA) {
			retorno = getMessage(messageId);
		} 
		return retorno;
	}
	
	@SuppressWarnings("unchecked")
	public static String getMessage(MensagensSistema mensagensSistema, BusinessException e) {
		String msgFormatada = getMessage(mensagensSistema, e.getCodigo().getIdBundle());
		if (e.getMaisInfo() != null) {
			if (e.getMaisInfo() instanceof List) {
				msgFormatada = TemplateMessageHelper.formatarParamMsg(msgFormatada,(List<String>) e.getMaisInfo());				
			}
		} 
		return msgFormatada;		 
	}
	@SuppressWarnings("unchecked")
	public static String getMessage(MensagensSistema mensagensSistema, BusinessException e, Locale locale) {
		String msgFormatada = getMessage(mensagensSistema, e.getCodigo().getIdBundle(), locale);
		
		if ( e instanceof NumberDIException ) {
			msgFormatada = formatarParamDIs(msgFormatada, (NumberDIException) e );
		}
		
		if (e.getMaisInfo() != null) {
			if (e.getMaisInfo() instanceof List) {
				msgFormatada = TemplateMessageHelper.formatarParamMsg(msgFormatada,(List<String>) e.getMaisInfo());				
			}
		} 
		return msgFormatada;		 
	}
	
	/**
	 * Formata a mensagem conforme as dis com exceção.
	 * @param msgFormatada
	 * @param e
	 * @return
	 */
	public static String formatarParamDIs(String msgFormatada, NumberDIException e) {
		if ( !e.getErroDIs().isEmpty() 
				&& msgFormatada.contains("{0}") ) {
			String dis = "";
			int i = 0;
			while ( i + 1 < e.getErroDIs().size() ) {
				dis += e.getErroDIs().get(i) + ", ";
				i++;
			}
			dis += e.getErroDIs().get(i);
			
			msgFormatada = msgFormatada.replace("{0}", dis);
		}
		
		return msgFormatada;
		
	}
	
	
	public static String formatarParamMsg(String msgFormatada, List<String> params) {
		if (params.size() > 0) 
		{
			String param;
			for (int i=0; i < params.size(); i++) {
				param = "{"+i+"}";
				msgFormatada = msgFormatada.replace(param,params.get(i));
			}						
		}
		return msgFormatada;
	}
	
	
}
