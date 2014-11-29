package br.com.ilog.importacao.business.mbean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citspresentation.mbeans.AbstractPaginacao;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;
import br.com.ilog.importacao.business.entity.Carga;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroCarga;
import br.com.ilog.importacao.business.facade.ImportacaoFacade;

@AccessScoped
@Controller( "mBeanPesquisarExportSap" )
public class MBeanPesquisarExportSap extends AbstractPaginacao {

	/** */
	private static final long serialVersionUID = 8519819605611790528L;

	@Resource( name = "controllerImportacao" )
	ImportacaoFacade importacaoFacade;

	private static Logger logger = LoggerFactory.getLogger( MBeanPesquisarExportSap.class );
	
	private BasicFiltroCarga filtro;
	
	private List<Carga> result;
	
	/**
	 * Metodo chamado ao inicializar a classe
	 */
	@PostConstruct
	public void inicializar() {
		inicializarObjetos();
		
		doPesquisar( null );
	}

	/**
	 * inicializar os objetos.
	 */
	public void inicializarObjetos() {
		filtro = new BasicFiltroCarga();
		result = new ArrayList<Carga>();
		
	}
	
	@Override
	public void doPesquisar( ActionEvent evn ) {
		try {
			
			result = importacaoFacade.listarProcessosDI(filtro);
		
			FacesContext fc = FacesContext.getCurrentInstance();
			
			if (result.isEmpty()) {
				String message = TemplateMessageHelper.getMessage(MensagensSistema.SISTEMA,"MSG008",fc.getViewRoot().getLocale());
				Messages.adicionaMensagemDeInfo(message);
			}
			
			
		} catch ( BusinessException e ) {
			logger.error("error: {} " + e);
			e.printStackTrace();
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(MensagensSistema.SISTEMA, e));
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
	}

	/**
	 * @return the filtro
	 */
	public BasicFiltroCarga getFiltro() {
		return filtro;
	}

	/**
	 * @param filtro the filtro to set
	 */
	public void setFiltro(BasicFiltroCarga filtro) {
		this.filtro = filtro;
	}

	/**
	 * @return the result
	 */
	public List<Carga> getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(List<Carga> result) {
		this.result = result;
	}

}
