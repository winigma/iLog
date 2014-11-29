package br.com.ilog.cadastro.presentation.mbean;

import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.hibernate.validator.ValidatorHelper;
import br.cits.commons.citspresentation.mbeans.AbstractManter;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.commons.citspresentation.util.JSFRequestBean;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.Cidade;
import br.com.ilog.cadastro.business.entity.Estado;
import br.com.ilog.cadastro.business.entity.Pais;
import br.com.ilog.cadastro.business.entity.Terminal;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroCidade;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroEstado;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.cadastro.presentation.util.ExceptionFiltro;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;

/**
 * @author Heber Santiago
 * 
 */
@AccessScoped
@Component("mBeanManterTerminal")
public class MBeanManterTerminal extends AbstractManter {

	/** */
	private static final long serialVersionUID = -1078394211895608519L;
	private static Logger logger = LoggerFactory
			.getLogger(MBeanManterTerminal.class);

	@Resource(name = "controllerCadastro")
	CadastroFacade facade;

	private Terminal terminal;
	
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

	private boolean edicao;
	
	@PostConstruct
	public void inicializar() {

		inicializarObjetos();
		inicializarCombos();

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
	 * Metodo chamado pelo comboPais.
	 */
	public void popularCombosEstadoCidade() {
		popularComboEstado();
		popularComboCidade();
	}
	
	/**
	 * Preenche o combo de cidade
	 * 
	 */
	public void popularComboCidade() {
		comboCidade = Collections.emptyList();
		try {

			comboCidade = facade.listarCidades(new BasicFiltroCidade(terminal
					.getCidade().getPais(), terminal.getCidade().getEstado()));

		} catch (BusinessException e) {
			logger.error("error: {} " + e);
			e.printStackTrace();
		}
	}

	/**
	 * Preenche o combo de estado
	 * 
	 */
	public void popularComboEstado() {
		comboEstado = Collections.emptyList();

		try {

			comboEstado = facade.listarEstados(new BasicFiltroEstado(terminal
					.getCidade().getPais()));

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

	/**
	 * Metodo prepara para um novo registro.
	 * @return
	 */
	public String novo() {

		inicializarObjetos();
		return "terminal.jsf";
	}

	@Override
	public String editar() {
		
		Long idRegistro = Long.valueOf(JSFRequestBean.getParameter("id"));
		try {
			
			terminal = facade.getTerminalById( idRegistro );
				
		} catch (BusinessException e) {
			e.printStackTrace();

			logger.error("erro: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.TERMINAL, e));
			return null;
		}
		
		edicao = true;

		return "terminal.jsf";
	}

	@Override
	public String excluir() {
		
		try {
			
			facade.excluirTerminal(terminal);
		
			FacesContext fc = FacesContext.getCurrentInstance();
			Messages.adicionaMensagemDeInfo(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG_EXCLUIR_SUCESSO", fc
							.getViewRoot().getLocale()));
			
		} catch (Exception e) {
			e.printStackTrace();

			logger.error("error: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper
					.getMessage(ExceptionFiltro.recursiveException(e)));
			return null;
			
		}
		return "terminais.jsf";
	}

	@Override
	public String salvar() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			ResourceBundle resourceBundle = TemplateMessageHelper
					.getResourceBundle(MensagensSistema.TERMINAL, fc
							.getViewRoot().getLocale());
			List<String> errorMessages = ValidatorHelper.valida(terminal,
					resourceBundle);
			if (errorMessages.isEmpty()) {
				try {
					if (edicao) {
						facade.alterarTerminal(terminal);
					} else {
						terminal = facade.cadastrarTerminal(terminal);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Throwable lastCause = ExceptionFiltro.getLastException(e);
					if (lastCause.getMessage().contains("unique")) {
						if (lastCause.getMessage().contains(
								"'UK_TERMINAL_NOME'")) {
							Messages.adicionaMensagemDeErro(TemplateMessageHelper
											.getMessage(MensagensSistema.TERMINAL,
													"msgUniqueNome", fc.getViewRoot()
															.getLocale()));
						}
						if (lastCause.getMessage().contains(
								"'UK_TERMINAL_SIGLA'")) {
							Messages.adicionaMensagemDeErro(TemplateMessageHelper
											.getMessage(MensagensSistema.TERMINAL,
													"msgUniqueSigla", fc.getViewRoot()
															.getLocale()));
						}
						return null;
					} else {
						e.printStackTrace();
						Messages.adicionaMensagemDeErro(TemplateMessageHelper
								.getMessage(ExceptionFiltro
										.recursiveException(e)));
					}
					return null;
				}
			} else {
				Messages.adicionaMensagensDeErro(errorMessages);
				return null;
			}

			String message = TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG001", fc.getViewRoot()
							.getLocale());
			Messages.adicionaMensagemDeInfo(message);

			refazerPesquisa();

			return "terminais.jsf";
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Cancela a editar.
	 */
	public String cancelar() {
		return "terminais.jsf";
	}
	
	@Override
	public void inicializarObjetos() {
		
		edicao = false;
		
		terminal = new Terminal();
		terminal.setCidade(new Cidade());
		terminal.getCidade().setEstado(new Estado());
		terminal.getCidade().setPais(new Pais());

	}

	@Override
	protected void refazerPesquisa() {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the terminal
	 */
	public Terminal getTerminal() {
		return terminal;
	}

	/**
	 * @param terminal
	 *            the terminal to set
	 */
	public void setTerminal(Terminal terminal) {
		this.terminal = terminal;
	}

	/**
	 * @return the comboCidade
	 */
	public List<Cidade> getComboCidade() {
		return comboCidade;
	}

	/**
	 * @param comboCidade
	 *            the comboCidade to set
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
	 * @param comboEstado
	 *            the comboEstado to set
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
	 * @param comboPais
	 *            the comboPais to set
	 */
	public void setComboPais(List<Pais> comboPais) {
		this.comboPais = comboPais;
	}

	/**
	 * @return the edicao
	 */
	public boolean isEdicao() {
		return edicao;
	}

	/**
	 * @param edicao the edicao to set
	 */
	public void setEdicao(boolean edicao) {
		this.edicao = edicao;
	}

}
