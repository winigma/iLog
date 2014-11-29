package br.com.ilog.cadastro.presentation.mbean;

import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citspresentation.mbeans.AbstractPaginacao;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.Motivo;
import br.com.ilog.cadastro.business.entity.TipoOcorrencia;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroMotivo;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroTipoOcorrencia;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.geral.presentation.CommonsList;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;

@AccessScoped
@Component( "mBeanPesquisarMotivo" )
public class MBeanPesquisarMotivo extends AbstractPaginacao {

	/** */
	private static final long serialVersionUID = -3221216380079256350L;
	Logger logger = LoggerFactory.getLogger(MBeanPesquisarMotivo.class);
	
	@Resource( name = "controllerCadastro" )
	CadastroFacade facade;
	
	@Resource
	CommonsList commonsList;
	
	private BasicFiltroMotivo filtro;
	
	private List<SelectItem> comboAtivo;
	
	private List<TipoOcorrencia> comboTipoOcorrencia;
	
	private List<Motivo> result;
	
	@PostConstruct
	void inicializar() {
		
		inicializarObjetos();
		doPesquisar( null );
	}
	
	/**
	 * Inicializa os objetos.
	 */
	void inicializarObjetos() {
		filtro = new BasicFiltroMotivo();
		result = Collections.emptyList();
		
		comboAtivo = commonsList.listaBooleanAtivoInativo();
		comboTipoOcorrencia = Collections.emptyList();
		try {
			comboTipoOcorrencia = facade.listarTipoOcorrencias( new BasicFiltroTipoOcorrencia( true ) );
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void doPesquisar(ActionEvent arg0) {
		
		FacesContext fc = FacesContext.getCurrentInstance();
		try {
			
			result = facade.listarMotivos(filtro);
			
			if (result.isEmpty()) {
				String message = TemplateMessageHelper.getMessage( MensagensSistema.SISTEMA, "MSG008", fc
						.getViewRoot().getLocale());
				Messages.adicionaMensagemDeInfo(message);
			}
			setPaginaAtual( 1 );
		} catch (BusinessException e) {
			logger.error( "error: {} " + e );
			e.printStackTrace();
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, e));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error( "error: {} " + e );
		}
	}

	@Override
	public int getTotalRegistros() {
		if ( result != null ) {
			return result.size();
		}
		return 0;
	}

	@Override
	public void limpar() {
		
		inicializarObjetos();

	}

	@Override
	public void refazerPesquisa() {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the filtro
	 */
	public BasicFiltroMotivo getFiltro() {
		return filtro;
	}

	/**
	 * @param filtro the filtro to set
	 */
	public void setFiltro(BasicFiltroMotivo filtro) {
		this.filtro = filtro;
	}

	/**
	 * @return the comboAtivo
	 */
	public List<SelectItem> getComboAtivo() {
		return comboAtivo;
	}

	/**
	 * @param comboAtivo the comboAtivo to set
	 */
	public void setComboAtivo(List<SelectItem> comboAtivo) {
		this.comboAtivo = comboAtivo;
	}

	/**
	 * @return the result
	 */
	public List<Motivo> getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(List<Motivo> result) {
		this.result = result;
	}

	/**
	 * @return the comboTipoOcorrencia
	 */
	public List<TipoOcorrencia> getComboTipoOcorrencia() {
		return comboTipoOcorrencia;
	}

	/**
	 * @param comboTipoOcorrencia the comboTipoOcorrencia to set
	 */
	public void setComboTipoOcorrencia(List<TipoOcorrencia> comboTipoOcorrencia) {
		this.comboTipoOcorrencia = comboTipoOcorrencia;
	}

}
