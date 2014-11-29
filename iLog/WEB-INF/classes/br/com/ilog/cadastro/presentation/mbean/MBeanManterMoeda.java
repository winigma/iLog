package br.com.ilog.cadastro.presentation.mbean;

import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.hibernate.validator.ValidatorHelper;
import br.cits.commons.citspresentation.mbeans.AbstractManter;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.commons.citspresentation.util.JSFRequestBean;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.Moeda;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.cadastro.presentation.util.ExceptionFiltro;
import br.com.ilog.geral.presentation.CommonsList;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;

/**
 * @author Heber Santiago
 *
 */
@AccessScoped
@Component( "mBeanManterMoeda" )
public class MBeanManterMoeda extends AbstractManter {
	
	/** */
	private static final long serialVersionUID = 6644764375045973267L;
	private static Logger logger = LoggerFactory.getLogger(MBeanManterMoeda.class);

	@Resource( name = "controllerCadastro" )
	CadastroFacade facade;
	
	@Resource( name = "commonsList" )
	CommonsList commonsList;
	
	/**
	 * entidade a ser editada ou salva.
	 */
	private Moeda moeda;
	
	/**
	 * combo status moeda.
	 */
	private List<SelectItem> comboAtivo;
	
	/**
	 * Combo de moeda padrao.
	 */
	private List<SelectItem> comboMoedaPadrao;
	
	private boolean edicao;
	
	@Override
	@PostConstruct
	public void inicializarObjetos() {
		edicao = false;
		
		moeda = new Moeda();
		moeda.setMoedaPadrao( false );
		comboAtivo = commonsList.listaBooleanAtivoInativo();
		comboMoedaPadrao = commonsList.listaSimNao();
		
	}
	
	/**
	 * Prepara para insercao.
	 * @return
	 */
	public String novo() {
		
		inicializarObjetos();
		
		return "moeda.jsf";
	}
	
	@Override
	public String editar() {
		Long idRegistro = Long.valueOf(JSFRequestBean.getParameter("id"));
		try {
			
			moeda = (Moeda) facade.getMoedaById(idRegistro);
			
		} catch (BusinessException e) {
			logger.error("erro: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(MensagensSistema.MOEDA, e));
			return null;
		}
		
		edicao = true;
		return "moeda.jsf";
	}

	@Override
	public String salvar() {
		FacesContext fc = FacesContext.getCurrentInstance();
		ResourceBundle resourceBundle = TemplateMessageHelper.getResourceBundle(MensagensSistema.MOEDA, fc.getViewRoot().getLocale());
		List<String> errorMessages = ValidatorHelper.valida(moeda,	resourceBundle);
		
		if ( moeda.getMoedaPadrao() ) {
			errorMessages.add(TemplateMessageHelper.getMessage(MensagensSistema.MOEDA, "MSGMoedaPadrao"));
		}
		
		if (errorMessages.isEmpty()) {
			try {
				if (edicao) {
					facade.alterarMoeda(moeda);
				} else {
					facade.cadastrarMoeda(moeda);
				}

			} catch (Exception e) {
				e.printStackTrace();
				logger.error("error: {}", e);
				Throwable lastCause = ExceptionFiltro.getLastException(e);
				if (lastCause.getMessage().contains("unique")) {
					if (lastCause.getMessage().contains("CHECK constraint")) {
						if (lastCause.getMessage().toLowerCase().contains("chk_moeda_duplicada")) {
							Messages.adicionaMensagemDeErro(TemplateMessageHelper
									.getMessage(MensagensSistema.MOEDA,
											"msgUniqueSigla",fc.getViewRoot().getLocale()));
						}
						return null;
					} else if (lastCause.getMessage().contains("'UK_MOEDA'")) {
						Messages.adicionaMensagemDeErro(TemplateMessageHelper
								.getMessage(MensagensSistema.MOEDA, "msgUnique",
										fc.getViewRoot().getLocale()));
					}
					return null;

				} else {
					Messages.adicionaMensagemDeErro(TemplateMessageHelper
							.getMessage(MensagensSistema.SISTEMA,
									ExceptionFiltro.recursiveException(e), fc
											.getViewRoot().getLocale()));
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
		
		return "moedas.jsf";
	}
	
	@Override
	public String excluir() {
		
		if ( moeda.getMoedaPadrao() ) {
			Messages.adicionaMensagemDeInfo(TemplateMessageHelper.getMessage(MensagensSistema.MOEDA, "MSGMoedaPadrao"));
			return null;
		}
		
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			facade.excluirMoeda(moeda);
			Messages.adicionaMensagemDeInfo(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG_EXCLUIR_SUCESSO", fc
							.getViewRoot().getLocale()));

			this.refazerPesquisa();
		} catch (Exception e) {
			logger.error("error: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper
					.getMessage(ExceptionFiltro.recursiveException(e)));

			return null;
		}
		return "moedas.jsf";
	}

	public String cancelar() {
		return "moedas.jsf";
	}

	@Override
	protected void refazerPesquisa() {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the moeda
	 */
	public Moeda getMoeda() {
		return moeda;
	}

	/**
	 * @param moeda the moeda to set
	 */
	public void setMoeda(Moeda moeda) {
		this.moeda = moeda;
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

	/**
	 * @return the comboMoedaPadrao
	 */
	public List<SelectItem> getComboMoedaPadrao() {
		return comboMoedaPadrao;
	}

	/**
	 * @param comboMoedaPadrao the comboMoedaPadrao to set
	 */
	public void setComboMoedaPadrao(List<SelectItem> comboMoedaPadrao) {
		this.comboMoedaPadrao = comboMoedaPadrao;
	}

}
