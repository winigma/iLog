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
import br.com.ilog.cadastro.business.entity.Moeda;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroMoeda;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.geral.presentation.CommonsList;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;

/**
 * @author Heber Santiago
 *
 */
@AccessScoped
@Component( "mBeanPesquisarMoeda" )
public class MBeanPesquisarMoeda extends AbstractPaginacao {

	/** */
	private static final long serialVersionUID = 3128839225102869063L;
	private static Logger logger = LoggerFactory.getLogger(MBeanPesquisarMoeda.class);
	
	@Resource( name = "controllerCadastro" )
	CadastroFacade facade;
	
	@Resource( name = "commonsList" )
	CommonsList commonsList;
	
	private BasicFiltroMoeda filtro;
	
	private List<Moeda> result;
	
	private List<SelectItem> comboAtivo;
	
	@PostConstruct
	public void inicializar() {
		inicializarObjetos();
		doPesquisar( null );
	}
	
	public void inicializarObjetos() {
		result = Collections.emptyList();
		filtro = new BasicFiltroMoeda();
		
		comboAtivo = commonsList.listaBooleanAtivoInativo();
	}
	
	@Override
	public void doPesquisar(ActionEvent arg0) {
		FacesContext fc = FacesContext.getCurrentInstance();
		try {
			
			if ( filtro != null && filtro.getDescricao() != null ) {
				filtro.setDescricao(filtro.getDescricao().trim());
			}
			result = facade.listarMoedas(filtro);
			
			if (result.isEmpty()) {
				String msg = TemplateMessageHelper.getMessage(MensagensSistema.SISTEMA,"MSG008",fc.getViewRoot().getLocale());
				Messages.adicionaMensagemDeInfo(msg);
				
			}
			setPaginaAtual(1);
		} catch (BusinessException e) {
			e.printStackTrace();
			logger.error("erro: {} " + e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(MensagensSistema.MOEDA, e));
			
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
	public BasicFiltroMoeda getFiltro() {
		return filtro;
	}

	/**
	 * @param filtro the filtro to set
	 */
	public void setFiltro(BasicFiltroMoeda filtro) {
		this.filtro = filtro;
	}

	/**
	 * @return the result
	 */
	public List<Moeda> getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(List<Moeda> result) {
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
