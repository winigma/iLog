package br.com.ilog.cadastro.presentation.mbean;

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
import br.com.ilog.cadastro.business.entity.Cidade;
import br.com.ilog.cadastro.business.entity.Estado;
import br.com.ilog.cadastro.business.entity.Pais;
import br.com.ilog.cadastro.business.entity.Terminal;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroCidade;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroEstado;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroTerminal;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;

/**
 * @author Heber Santiago
 * 
 */
@AccessScoped
@Component("mBeanPesquisarTerminal")
public class MBeanPesquisarTerminal extends AbstractPaginacao {

	/** */
	private static final long serialVersionUID = 5965669734324297931L;
	private static Logger logger = LoggerFactory.getLogger(MBeanPesquisarTerminal.class);

	@Resource(name = "controllerCadastro")
	CadastroFacade facade;

	/**
	 * Itens com o resultado da pesquisa.
	 */
	private List<Terminal> result;

	/**
	 * Filtro de pesquisa de terminal.
	 */
	private BasicFiltroTerminal filtro;

	/**
	 * Combo cidade
	 */
	private List<Cidade> comboCidade;

	/**
	 * combo estado
	 */
	private List<Estado> comboEstado;

	/**
	 * combo pais
	 */
	private List<Pais> comboPais;

	/**
	 * Inicializar objetos da tela.
	 */
	@PostConstruct
	public void inicializar() {
		
		inicializarObjetos();
		inicializarCombos();
		
		doPesquisar( null );
	}
	
	/**
	 * instancia os objetos
	 */
	void inicializarObjetos() {
		filtro = new BasicFiltroTerminal();
		filtro.setPais( new Pais() );
		filtro.setEstado( new Estado() );
		filtro.setCidade( new Cidade() );
		
		comboCidade = Collections.emptyList();
		comboEstado = Collections.emptyList();
		comboPais = Collections.emptyList();
		
		result = Collections.emptyList();
		
	}
	
	/**
	 * Inicializa os combos de cidade, estado e pais
	 */
	void inicializarCombos() {
		popularComboPais();
		popularComboEstado();
		popularComboCidade();
	}
	
	/**
	 * 
	 */
	public void popularCombosEstadoCidade() {
		popularComboEstado();
		popularComboCidade();
	}
	
	/**
	 * Preenche o combo de cidade
	 * @param listener
	 */
	public void popularComboCidade() {
		comboCidade = Collections.emptyList();
		try {
			
			comboCidade = facade.listarCidades( new BasicFiltroCidade( filtro.getPais(), filtro.getEstado() ) );
		
		} catch (BusinessException e) {
			logger.error("error: {} " + e);
			e.printStackTrace();
			
		}
	}

	/**
	 * Preenche o combo de estado
	 * @param listener
	 */
	public void popularComboEstado() {
		comboEstado = Collections.emptyList();
		
		try {
			
			comboEstado = facade.listarEstados( new BasicFiltroEstado( filtro.getPais() ) );
		
		} catch (BusinessException e1) {
			logger.error("error: {} " + e1);
			e1.printStackTrace();
		}
	}

	/**
	 * Popula o combo de pais. 
	 */
	public void popularComboPais() {
		comboPais = Collections.emptyList();
		try {
			
			comboPais = facade.listarPaises();
			
		} catch (BusinessException e1) {
			logger.error("error: {} " + e1);
			e1.printStackTrace();
		}
	}
	
	@Override
	public void doPesquisar(ActionEvent arg0) {

		FacesContext fc = FacesContext.getCurrentInstance();
		try {
			
			result = facade.listarTerminais( filtro );
			
			if ( result.isEmpty() ) {
				String msg = TemplateMessageHelper.getMessage(MensagensSistema.SISTEMA, "MSG008", 
						fc.getViewRoot().getLocale());
				Messages.adicionaMensagemDeInfo(msg);
			}
			
			setPaginaAtual(1);
			
		} catch (BusinessException e) {
			e.printStackTrace();
			logger.error("erro: {} " + e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper
					.getMessage(MensagensSistema.PAIS, e));
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
		inicializarCombos();

	}

	@Override
	public void refazerPesquisa() {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the result
	 */
	public List<Terminal> getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(List<Terminal> result) {
		this.result = result;
	}

	/**
	 * @return the filtro
	 */
	public BasicFiltroTerminal getFiltro() {
		return filtro;
	}

	/**
	 * @param filtro the filtro to set
	 */
	public void setFiltro(BasicFiltroTerminal filtro) {
		this.filtro = filtro;
	}

	/**
	 * @return the comboCidade
	 */
	public List<Cidade> getComboCidade() {
		return comboCidade;
	}

	/**
	 * @param comboCidade the comboCidade to set
	 */
	public void setComboCidade(List<Cidade> comboCidade) {
		this.comboCidade = comboCidade;
	}

	/**
	 * @return the comboEstado
	 */
	public List<Estado> getComboEstado() {
		return comboEstado;
	}

	/**
	 * @param comboEstado the comboEstado to set
	 */
	public void setComboEstado(List<Estado> comboEstado) {
		this.comboEstado = comboEstado;
	}

	/**
	 * @return the comboPais
	 */
	public List<Pais> getComboPais() {
		return comboPais;
	}

	/**
	 * @param comboPais the comboPais to set
	 */
	public void setComboPais(List<Pais> comboPais) {
		this.comboPais = comboPais;
	}

}
