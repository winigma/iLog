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
import br.com.ilog.cadastro.business.entity.Projeto;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroProjeto;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.geral.presentation.CommonsList;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;

/**
 * @author Heber Santiago
 *
 */
@Component( "mBeanPesquisarProjeto" )
@AccessScoped
public class MBeanPesquisarProjeto extends AbstractPaginacao {

	/** */
	private static final long serialVersionUID = 5408009982622252932L;
	private static Logger logger = LoggerFactory.getLogger(MBeanPesquisarProjeto.class);
	
	@Resource( name = "controllerCadastro" )
	CadastroFacade facade;
	
	private BasicFiltroProjeto filtro;
	
	private List<Projeto> result;
	
	private List<SelectItem> comboAtivo;
	
	@Resource( name = "commonsList")
	CommonsList commonsList;
	
	@PostConstruct
	void inicializar() {

		inicializarObjetos();
		
		doPesquisar( null );
	}
	
	void inicializarObjetos() {
		filtro = new BasicFiltroProjeto();
		filtro.setAtivo( null );
		
		result = Collections.emptyList();
		
		comboAtivo = commonsList.listaBooleanAtivoInativo();
		
	}
	
	@Override
	public void doPesquisar(ActionEvent arg0) {
		try {
			
			result = facade.listarProjetos(filtro);
			
			FacesContext fc = FacesContext.getCurrentInstance();
			if ( result.isEmpty() ) {
				String msg = TemplateMessageHelper.getMessage(MensagensSistema.SISTEMA, "MSG008", fc.getViewRoot().getLocale());
				Messages.adicionaMensagemDeInfo(msg);
				return;
			}
			setPaginaAtual( 1 );
			
		} catch (BusinessException e1) {
			e1.printStackTrace();
			
			logger.error("erro: {} " + e1);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper
					.getMessage(MensagensSistema.PROJETO, e1));
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
		

	}

	/**
	 * @return the filtro
	 */
	public BasicFiltroProjeto getFiltro() {
		return filtro;
	}

	/**
	 * @param filtro the filtro to set
	 */
	public void setFiltro(BasicFiltroProjeto filtro) {
		this.filtro = filtro;
	}

	/**
	 * @return the result
	 */
	public List<Projeto> getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(List<Projeto> result) {
		this.result = result;
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

}
