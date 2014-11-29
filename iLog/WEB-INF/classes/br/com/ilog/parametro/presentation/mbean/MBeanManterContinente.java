package br.com.ilog.parametro.presentation.mbean;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citspresentation.mbeans.AbstractManter;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.Continente;
import br.com.ilog.cadastro.business.entity.ParametroContinente;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;

/**
 * Classe de manter parametros de continente.
 * @author Heber Santiago
 *
 */
@AccessScoped
@Component( "mBeanManterContinente" )
public class MBeanManterContinente extends AbstractManter {

	/** */
	private static final long serialVersionUID = -6209507541749058803L;

	@Resource( name = "controllerCadastro" )
	CadastroFacade facade;
	
	Logger logger = LoggerFactory.getLogger( MBeanManterContinente.class );
	
	private ParametroContinente africa;
	private ParametroContinente america;
	private ParametroContinente asia;
	private ParametroContinente europa;
	private ParametroContinente oceania;
	
	@Override
	public String editar() {
		return null;
	}

	@Override
	public String excluir() {
		return null;
	}

	@PostConstruct
	@Override
	public void inicializarObjetos() {
		List<ParametroContinente> cont;
		try {
			cont = facade.listarParametroContinente();
			for (ParametroContinente continente : cont) {
				if ( continente.getContinente().equals( Continente.AF ) ) {
					africa = continente;
				}
				if ( continente.getContinente().equals( Continente.AM ) ) {
					america = continente;
				}
				if ( continente.getContinente().equals( Continente.AS ) ) {
					asia = continente;
				}
				if ( continente.getContinente().equals( Continente.EU ) ) {
					europa = continente;
				}
				if ( continente.getContinente().equals( Continente.OC ) ) {
					oceania = continente;
				}
			}
			
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}
	
	public String salvar() {
		
		try {
			
			africa = facade.alterarParametroContinente( africa );
			america = facade.alterarParametroContinente( america );
			asia = facade.alterarParametroContinente( asia );
			europa = facade.alterarParametroContinente( europa );
			oceania = facade.alterarParametroContinente( oceania );
			
			String message = TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG001", FacesContext.getCurrentInstance().getViewRoot()
							.getLocale());
			Messages.adicionaMensagemDeInfo(message);
			
		} catch (BusinessException e) {
			logger.error("erro: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, e));
		}
		
		return "continente.jsf";
	}

	@Override
	protected void refazerPesquisa() {
	}

	/**
	 * @return the africa
	 */
	public ParametroContinente getAfrica() {
		return africa;
	}

	/**
	 * @param africa the africa to set
	 */
	public void setAfrica(ParametroContinente africa) {
		this.africa = africa;
	}

	/**
	 * @return the america
	 */
	public ParametroContinente getAmerica() {
		return america;
	}

	/**
	 * @param america the america to set
	 */
	public void setAmerica(ParametroContinente america) {
		this.america = america;
	}

	/**
	 * @return the asia
	 */
	public ParametroContinente getAsia() {
		return asia;
	}

	/**
	 * @param asia the asia to set
	 */
	public void setAsia(ParametroContinente asia) {
		this.asia = asia;
	}

	/**
	 * @return the europa
	 */
	public ParametroContinente getEuropa() {
		return europa;
	}

	/**
	 * @param europa the europa to set
	 */
	public void setEuropa(ParametroContinente europa) {
		this.europa = europa;
	}

	/**
	 * @return the oceania
	 */
	public ParametroContinente getOceania() {
		return oceania;
	}

	/**
	 * @param oceania the oceania to set
	 */
	public void setOceania(ParametroContinente oceania) {
		this.oceania = oceania;
	}

}
