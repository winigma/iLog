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
import br.com.ilog.cadastro.business.entity.TipoPacote;
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
@Component( "mBeanManterEmbalagem" )
public class MBeanManterEmbalagem extends AbstractManter {

	/** */
	private static final long serialVersionUID = 2099421243754896668L;
	Logger logger = LoggerFactory.getLogger(MBeanManterEmbalagem.class);
	
	@Resource( name = "commonsList" )
	CommonsList commonsList;
	
	@Resource( name = "controllerCadastro" )
	CadastroFacade facade;
	
	private TipoPacote entity;
	
	private boolean edicao;
	
	private List<SelectItem> comboAtivo;
	
	@Override
	@PostConstruct
	public void inicializarObjetos() {
		entity = new TipoPacote();
		edicao = false;
		comboAtivo = commonsList.listaBooleanAtivoInativo();
	}
	
	public String novo() {
		
		inicializarObjetos();
		return "embalagem.jsf";
	}
	
	@Override
	public String editar() {
		Long idRegistro = Long.valueOf(JSFRequestBean.getParameter("id"));

		try {
			
			entity = facade.getTipoPacoteById(idRegistro);
			
		} catch (BusinessException e) {
			e.printStackTrace();
			logger.error("erro: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.MATERIA_PRIMA, e));
			return null;
		}
		edicao = true;
		return "embalagem.jsf";
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
						entity = facade.alterarTipoPacote(entity);
					} else {
						entity = facade.cadastrarTipoPacote(entity);
					}
					
				} catch (Exception e) {
	                if(!edicao)
	                	entity.setId(null);
					logger.error("error: {}", e);

					ConstraintViolationException ex = (ConstraintViolationException) e
							.getCause();
					ex.printStackTrace();
				//	e.printStackTrace();
					
					//Throwable lastCause = ExceptionFiltro.getLastException(e);
					if (e.getMessage().contains("ConstraintViolationException") ) {
						if (ex.getSQLException().getNextException().getMessage().contains("'UK_TIPO_PACOTE_SIGLA'")) {
							Messages.adicionaMensagemDeErro(TemplateMessageHelper
									.getMessage(MensagensSistema.SISTEMA,
											"msgUniqueSigla", fc.getViewRoot()
													.getLocale()));
						} else
						if (ex.getSQLException().getNextException().getMessage().contains("duplicar valor da chave viola a restrição de unicidade")) {
							Messages.adicionaMensagemDeErro(TemplateMessageHelper
									.getMessage(MensagensSistema.TIPO_PACOTE, "msgUniqueDesc",
											fc.getViewRoot().getLocale()));
						}else
						if (ex.getSQLException().getNextException().getMessage().contains("uk_tipo_pacote")) {
							Messages.adicionaMensagemDeErro(TemplateMessageHelper
									.getMessage(MensagensSistema.TIPO_PACOTE, "msgUniqueDesc",
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
			return "embalagens.jsf";
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error:{}", e);
			
			return null;
		}
	}
	
	public String cancelar() {
		return "embalagens.jsf";
	}
	
	@Override
	public String excluir() {
		try {
			
			facade.excluirTipoPacote( entity );
			
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
		return "embalagens.jsf";
	}

	@Override
	protected void refazerPesquisa() {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the entity
	 */
	public TipoPacote getEntity() {
		return entity;
	}

	/**
	 * @param entity the entity to set
	 */
	public void setEntity(TipoPacote entity) {
		this.entity = entity;
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
