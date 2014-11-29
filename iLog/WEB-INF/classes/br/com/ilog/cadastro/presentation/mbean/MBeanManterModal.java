package br.com.ilog.cadastro.presentation.mbean;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.hibernate.validator.ValidatorHelper;
import br.cits.commons.citspresentation.mbeans.AbstractManter;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.commons.citspresentation.util.JSFRequestBean;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.Modal;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.cadastro.presentation.util.ExceptionFiltro;
import br.com.ilog.geral.presentation.CommonsList;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;

@AccessScoped
@Component( "mBeanManterModal" )
public class MBeanManterModal extends AbstractManter {

	/** */
	private static final long serialVersionUID = 4208296511485551331L;
	private static Logger logger = LoggerFactory.getLogger(MBeanPesquisarModal.class);

	@Resource( name = "controllerCadastro" )
	CadastroFacade facade;
	
	@Resource( name = "commonsList" )
	CommonsList commonsList;
	
	private Modal modal;
	
	private boolean edicao;
	
	private List<SelectItem> comboAtivo;
	
	private List<String> imagens;
	
	
	@Override
	@PostConstruct
	public void inicializarObjetos() {
		
		modal = new Modal();
		comboAtivo = commonsList.listaBooleanAtivoInativo();
		edicao = false;
		
		imagens =  new ArrayList<String>();
		imagens.add("/pub/img/aereo.png");
		imagens.add("/pub/img/maritimo.png");
		imagens.add("/pub/img/terrestre.png");
		imagens.add("/pub/img/maritimo_aereo.png");
	}
	
	/**
	 * Metodo prepara para insercao de novos objetos
	 * @return
	 */
	public String novo() {
		
		inicializarObjetos();
		return "modal.jsf";
	}
	
	@Override
	public String salvar() {

		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			ResourceBundle resourceBundle = TemplateMessageHelper
					.getResourceBundle(MensagensSistema.MODAL, fc
							.getViewRoot().getLocale());
			List<String> errorMessages = ValidatorHelper.valida(modal,
					resourceBundle);
			if (errorMessages.isEmpty()) {
				try {
					if (edicao) {
						modal = facade.alterarModal(modal);
					} else {
						modal = facade.cadastrarModal(modal);
					}
				} catch (Exception e) {
					
				


					logger.error("error: {}", e);

					if (!edicao) {
						modal.setId(null);
					}

					ConstraintViolationException exc = (ConstraintViolationException) e
							.getCause();
					e.printStackTrace();

					// Throwable lastCause = ExceptionFiltro.getLastException(e);
					if (e.getMessage().contains("ConstraintViolationException")) {
						if (exc.getSQLException().getNextException().getMessage()
								.contains("uk_modal")) {
							Messages.adicionaMensagemDeErro(TemplateMessageHelper
									.getMessage(MensagensSistema.MODAL,
											"msgUnique", fc.getViewRoot()
													.getLocale()));
						} else if (exc.getSQLException().getNextException()
								.getMessage().contains("uk_modal")) {
							Messages.adicionaMensagemDeErro(TemplateMessageHelper
									.getMessage(MensagensSistema.MODAL, "msgUnique",
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

			return "modais.jsf";
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	
	}
	
	@Override
	public String editar() {
		Long idRegistro = Long.valueOf(JSFRequestBean.getParameter("id"));
		try {
			
			modal = facade.getModalById( idRegistro );
				
		} catch (BusinessException e) {
			e.printStackTrace();

			logger.error("erro: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.MODAL, e));
			return null;
		}
		
		edicao = true;

		return "modal.jsf";
	}

	@Override
	public String excluir() {
		try {
			
			facade.excluirModal(modal);
		
			FacesContext fc = FacesContext.getCurrentInstance();
			Messages.adicionaMensagemDeInfo(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG_EXCLUIR_SUCESSO", fc
							.getViewRoot().getLocale()));
			
		} catch (Exception e) {
			e.printStackTrace();

			logger.error("erro: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper
					.getMessage(ExceptionFiltro.recursiveException(e)));
			return null;
			
		}
		return "modais.jsf";
	}

	public String cancelar() {
		
		return "modais.jsf";
	}
	
	@Override
	protected void refazerPesquisa() {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the modal
	 */
	public Modal getModal() {
		return modal;
	}

	/**
	 * @param modal the modal to set
	 */
	public void setModal(Modal modal) {
		this.modal = modal;
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
	 * @return the imagens
	 */
	public List<String> getImagens() {
		return imagens;
	}

	/**
	 * @param imagens the imagens to set
	 */
	public void setImagens(List<String> imagens) {
		this.imagens = imagens;
	}

}
