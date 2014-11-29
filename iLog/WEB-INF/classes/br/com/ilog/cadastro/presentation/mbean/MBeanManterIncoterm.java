package br.com.ilog.cadastro.presentation.mbean;

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
import br.com.ilog.cadastro.business.entity.Incoterm;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.cadastro.presentation.util.ExceptionFiltro;
import br.com.ilog.geral.presentation.CommonsList;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;

@AccessScoped
@Component( "mBeanManterIncoterm" )
public class MBeanManterIncoterm extends AbstractManter {

	/** */
	private static final long serialVersionUID = 3341043760974023979L;
	Logger logger = LoggerFactory.getLogger( MBeanManterIncoterm.class );
	
	@Resource( name = "commonsList" )
	CommonsList commonsList;
	
	@Resource( name = "controllerCadastro" )
	CadastroFacade facade;
	
	private Incoterm entity;
	
	private List<SelectItem> comboAtivo;
	
	private boolean edicao;
	
	@Override
	@PostConstruct
	public void inicializarObjetos() {
		entity = new Incoterm();
		edicao = false;
		comboAtivo = commonsList.listaBooleanAtivoInativo();
		
	}
	
	public String novo() {
		
		inicializarObjetos();
		
		return "incoterm.jsf";
	}
	
	@Override
	public String editar() {
		Long idRegistro = Long.valueOf(JSFRequestBean.getParameter("id"));

		try {
			
			entity = facade.getIncotermById(idRegistro);
			
		} catch (BusinessException e) {
			e.printStackTrace();
			logger.error("erro: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.INCOTERM, e));
			return null;
		}
		edicao = true;
		return "incoterm.jsf";
	}

	@Override
	public String salvar() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			ResourceBundle resourceBundle = TemplateMessageHelper.getResourceBundle(MensagensSistema.INCOTERM, fc
							.getViewRoot().getLocale());
			List<String> errorMessages = ValidatorHelper.valida(entity,
					resourceBundle);
			
			if (errorMessages.isEmpty()) {
				try {
			
					if (edicao) {
						facade.alterarIncoterm(entity);
					} else {
						facade.cadastrarIncoterm(entity);
					}
					
				} catch (Exception e) {
	
					logger.error("error: {}", e);
					e.printStackTrace();
					if (!edicao) {
						entity.setId(null);
					}

					ConstraintViolationException exc = (ConstraintViolationException) e
							.getCause();
					
					//Throwable lastCause = ExceptionFiltro.getLastException(e);
					if (e.getMessage().contains("ConstraintViolationException")) {
						if (exc.getSQLException().getNextException().getMessage()
								.contains("uk_incoterm_sigla")) {
							Messages.adicionaMensagemDeErro(TemplateMessageHelper
									.getMessage(MensagensSistema.INCOTERM,
											"msgUniqueIncoterms", fc.getViewRoot()
													.getLocale()));
						} else if (exc.getSQLException().getNextException().getMessage()
								.contains("uk_incoterm_desc")) {
							Messages.adicionaMensagemDeErro(TemplateMessageHelper
									.getMessage(MensagensSistema.INCOTERM, "msgUniqueDesc",
											fc.getViewRoot().getLocale()));
						}
						return null;

					} else {
						Messages.adicionaMensagemDeErro(TemplateMessageHelper
								.getMessage(MensagensSistema.SISTEMA,
										ExceptionFiltro.recursiveException(e), fc
												.getViewRoot().getLocale()));
					}
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
			return "incoterms.jsf";
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error:{}", e);
			
			return null;
		}
	}
	
	@Override
	public String excluir() {
		try {
			
			facade.excluirIncotermById(entity);
			
			FacesContext fc = FacesContext.getCurrentInstance();
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
		return "incoterms.jsf";
	}

	public String cancelar() {
		return "incoterms.jsf";
	}

	@Override
	protected void refazerPesquisa() {
		// TODO Auto-generated method stub
	}

	/**
	 * @return the entity
	 */
	public Incoterm getEntity() {
		return entity;
	}

	/**
	 * @param entity the entity to set
	 */
	public void setEntity(Incoterm entity) {
		this.entity = entity;
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

}
