package br.com.ilog.cadastro.presentation.mbean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citspresentation.mbeans.AbstractPaginacao;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.Estado;
import br.com.ilog.cadastro.business.entity.Pais;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroEstado;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;

@Component( "mBeanPesquisarEstado" )
@AccessScoped
public class MBeanPesquisarEstado extends AbstractPaginacao {


	/** */
	private static final long serialVersionUID = -6720915173656133829L;

	private static Logger logger = LoggerFactory
			.getLogger(MBeanPesquisarPais.class);

	@Resource(name = "controllerCadastro")
	CadastroFacade facade;

	private List<Pais> comboPaises;
	
	private List<Estado> result;

	private BasicFiltroEstado filtro;

	private boolean refazerPesquisa = false;

	@PostConstruct
	void inicializar() {
		
		filtro = new BasicFiltroEstado();
		filtro.setPais( new Pais() );
		
		comboPaises = Collections.emptyList();
		result = Collections.emptyList();
		
		popularComboPais();
		
		doPesquisar( null );
		
	}

	@Override
	public void doPesquisar(ActionEvent arg0) {
		FacesContext fc = FacesContext.getCurrentInstance();
		try {
			result = facade.listarEstados( filtro );
			
			if ( result.isEmpty() ) {
				String msg = TemplateMessageHelper.getMessage(MensagensSistema.SISTEMA, "MSG008", fc.getViewRoot().getLocale());
				Messages.adicionaMensagemDeInfo(msg);
			}
			setPaginaAtual(1);
			
		} catch (BusinessException e) {
			e.printStackTrace();
			logger.error("erro: {} " + e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper
					.getMessage(MensagensSistema.ESTADO, e));
		}
	}

	public void popularComboPais() {
		comboPaises = new ArrayList<Pais>();
		
		try {
			
			comboPaises = facade.listarPaises(null);
			
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public int getTotalRegistros() {
		if ( !result.isEmpty() ) {
			return result.size();
		}
		return 0;
	}

	@Override
	public void limpar() {
		
		filtro = new BasicFiltroEstado();
		filtro.setPais( new Pais() );
		
		setPaginaAtual( 1 );
		result.clear();
		
	}

	@Override
	public void refazerPesquisa() {

	}
	
	public String cancelar() {
		doPesquisar( null );
		return "estados.jsf";
	}
	
	public void setComboPaises(List<Pais> comboPaises) {
		this.comboPaises = comboPaises;
	}
	
	public List<Pais> getComboPaises() {
		return comboPaises;
	}
	
	public void setFiltro(BasicFiltroEstado filtro) {
		this.filtro = filtro;
	}
	
	public BasicFiltroEstado getFiltro() {
		return filtro;
	}
	
	public void setResult(List<Estado> result) {
		this.result = result;
	}
	
	public List<Estado> getResult() {
		return result;
	}

	public void setRefazerPesquisa(boolean refazerPesquisa) {
		this.refazerPesquisa = refazerPesquisa;
	}
	
	public boolean isRefazerPesquisa() {
		return refazerPesquisa;
	}
}
