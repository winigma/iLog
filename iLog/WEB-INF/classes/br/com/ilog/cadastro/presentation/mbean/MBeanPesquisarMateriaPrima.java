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
import br.com.ilog.cadastro.business.entity.MateriaPrima;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroMateriaPrima;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.geral.presentation.CommonsList;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;

@AccessScoped
@Component( "mBeanPesquisarMateriaPrima" )
public class MBeanPesquisarMateriaPrima extends AbstractPaginacao {

	/** */
	private static final long serialVersionUID = -8284637203463002927L;
	private static Logger logger = LoggerFactory.getLogger(MBeanPesquisarMateriaPrima.class);
	
	@Resource( name = "controllerCadastro" )
	CadastroFacade facade;
	
	@Resource( name = "commonsList" )
	CommonsList commonsList;
	
	private BasicFiltroMateriaPrima filtro;
	
	private List<MateriaPrima> result;
	
	private List<SelectItem> comboAtivo;
	
	/** */
	@PostConstruct
	void inicializar() {
		inicializarObjetos();
		doPesquisar( null );
	}
	
	/**
	 * Inicializa os objetos de tela
	 */
	void inicializarObjetos() {
		filtro = new BasicFiltroMateriaPrima();
		result = Collections.emptyList();
		comboAtivo = commonsList.listaBooleanAtivoInativo();
	}
	
	@Override
	public void doPesquisar(ActionEvent arg0) {
		
		FacesContext fc = FacesContext.getCurrentInstance();
		try {
			
			result = facade.listarMateriaPrima(filtro);
			
			if (result.isEmpty()) {
				String message = TemplateMessageHelper.getMessage( MensagensSistema.SISTEMA, "MSG008", fc
						.getViewRoot().getLocale());
				Messages.adicionaMensagemDeInfo(message);
			}
			setPaginaAtual(1);
		} catch (BusinessException e) {
			logger.error("error: {} " + e);
			e.printStackTrace();
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, e));
		} catch (Exception e) {
			e.printStackTrace();
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
	public BasicFiltroMateriaPrima getFiltro() {
		return filtro;
	}

	/**
	 * @param filtro the filtro to set
	 */
	public void setFiltro(BasicFiltroMateriaPrima filtro) {
		this.filtro = filtro;
	}

	/**
	 * @return the result
	 */
	public List<MateriaPrima> getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(List<MateriaPrima> result) {
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
