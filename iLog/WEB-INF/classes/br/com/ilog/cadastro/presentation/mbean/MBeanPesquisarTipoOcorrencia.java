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
import br.com.ilog.cadastro.business.entity.TipoOcorrencia;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroTipoOcorrencia;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.geral.presentation.CommonsList;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;

@AccessScoped
@Component( "mBeanPesquisarTipoOcorrencia" )
public class MBeanPesquisarTipoOcorrencia extends AbstractPaginacao {

	/** */
	private static final long serialVersionUID = 251013959905885187L;
	Logger logger = LoggerFactory.getLogger( MBeanPesquisarTipoOcorrencia.class );
	
	@Resource
	CommonsList commonsList;
	
	@Resource( name = "controllerCadastro" )
	CadastroFacade facade;
	
	private List<SelectItem> comboAtivo;
	
	private List<TipoOcorrencia> result;
	
	private BasicFiltroTipoOcorrencia filtro;
	
	@PostConstruct
	public void inicializar() {
		inicializarObjetos();
		
		doPesquisar( null );
	}
	
	public void inicializarObjetos() {
		result = Collections.emptyList();
		filtro = new BasicFiltroTipoOcorrencia();
		comboAtivo = commonsList.listaBooleanAtivoInativo();
	}
	
	@Override
	public void doPesquisar(ActionEvent arg0) {
		
		FacesContext fc = FacesContext.getCurrentInstance();
		try {
			
			result = facade.listarTipoOcorrencias(filtro);
			
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
	public List<TipoOcorrencia> getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(List<TipoOcorrencia> result) {
		this.result = result;
	}

	/**
	 * @return the filtro
	 */
	public BasicFiltroTipoOcorrencia getFiltro() {
		return filtro;
	}

	/**
	 * @param filtro the filtro to set
	 */
	public void setFiltro(BasicFiltroTipoOcorrencia filtro) {
		this.filtro = filtro;
	}

}
