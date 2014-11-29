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
import br.com.ilog.cadastro.business.entity.Canal;
import br.com.ilog.cadastro.business.entity.ParametroCanal;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;


/**
 * Classe de manter Canais
 * @author Heber Santiago
 *
 */
@AccessScoped
@Component( "mBeanManterCanal" )
public class MBeanManterCanal extends AbstractManter {

	/** */
	private static final long serialVersionUID = 1719310137609905030L;

	@Resource( name = "controllerCadastro" )
	CadastroFacade facade;
	
	Logger logger = LoggerFactory.getLogger( MBeanManterCanal.class );
	
	/**
	 * Canais
	 */
	private ParametroCanal vermelho;
	private ParametroCanal verde;
	private ParametroCanal amarelo;
	private ParametroCanal cinza;
	
	@PostConstruct
	@Override
	public void inicializarObjetos() {

		try {
			List<ParametroCanal> canais = facade.listarParametroCanais();
			for (ParametroCanal canal : canais) {
				if ( canal.getCanal().equals( Canal.AM ) ) {
					amarelo = canal;
				}
				if ( canal.getCanal().equals( Canal.VE ) ) {
					verde = canal;
				}
				if ( canal.getCanal().equals( Canal.VM ) ) {
					vermelho = canal;
				}
				if ( canal.getCanal().equals( Canal.CI ) ) {
					cinza = canal;
				}
				
			}
			
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		
	}
	
	public String salvar() {
		
		try {
			
			amarelo = facade.alterarParametroCanal( amarelo );
			verde = facade.alterarParametroCanal( verde );
			vermelho = facade.alterarParametroCanal( vermelho );
			cinza = facade.alterarParametroCanal( cinza );
			
			String message = TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG001", FacesContext.getCurrentInstance().getViewRoot()
							.getLocale());
			Messages.adicionaMensagemDeInfo(message);
			
		} catch (BusinessException e) {
			logger.error("erro: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, e));
		}
		
		return "canal.jsf";
	}
	
	@Override
	public String editar() {
		return null;
	}
	@Override
	public String excluir() {
		return null;
	}

	@Override
	protected void refazerPesquisa() {
	}

	/**
	 * @return the vermelho
	 */
	public ParametroCanal getVermelho() {
		return vermelho;
	}

	/**
	 * @param vermelho the vermelho to set
	 */
	public void setVermelho(ParametroCanal vermelho) {
		this.vermelho = vermelho;
	}

	/**
	 * @return the verde
	 */
	public ParametroCanal getVerde() {
		return verde;
	}

	/**
	 * @param verde the verde to set
	 */
	public void setVerde(ParametroCanal verde) {
		this.verde = verde;
	}

	/**
	 * @return the amarelo
	 */
	public ParametroCanal getAmarelo() {
		return amarelo;
	}

	/**
	 * @param amarelo the amarelo to set
	 */
	public void setAmarelo(ParametroCanal amarelo) {
		this.amarelo = amarelo;
	}

	/**
	 * @return the cinza
	 */
	public ParametroCanal getCinza() {
		return cinza;
	}

	/**
	 * @param cinza the cinza to set
	 */
	public void setCinza(ParametroCanal cinza) {
		this.cinza = cinza;
	}

}
