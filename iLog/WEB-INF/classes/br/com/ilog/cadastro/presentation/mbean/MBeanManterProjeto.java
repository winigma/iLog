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
import br.com.ilog.cadastro.business.entity.Projeto;
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
@Component( "mBeanManterProjeto" )
public class MBeanManterProjeto extends AbstractManter {

	/** */
	private static final long serialVersionUID = 2157792280715679667L;
	private static Logger logger = LoggerFactory.getLogger(MBeanPesquisarProjeto.class);
	
	@Resource( name = "controllerCadastro" )
	CadastroFacade facade;
	
	private Projeto projeto;
	
	private List<SelectItem> comboAtivo;
	
	private boolean edicao;
	
	@Resource( name = "commonsList")
	CommonsList commonsList;
	
	@Override
	@PostConstruct
	public void inicializarObjetos() {
		edicao = false;
		projeto = new Projeto();
		
		comboAtivo = commonsList.listaBooleanAtivoInativo();
		
	}
	
	public String novo() {
		inicializarObjetos();
		
		return "projeto.jsf";
	}
	
	
	@Override
	public String editar() {
		Long idRegistro = Long.valueOf(JSFRequestBean.getParameter("id"));
		try {
			
			projeto = facade.getProjetoById( idRegistro );
				
		} catch (BusinessException e) {
			e.printStackTrace();

			logger.error("erro: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.PROJETO, e));
			return null;
		}
		
		edicao = true;

		return "projeto.jsf";
	}

	@Override
	public String excluir() {
		
		try {
			
			facade.excluirProjeto(projeto);
		
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
		return "projetos.jsf";
	}
	
	@Override
	public String salvar() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			ResourceBundle resourceBundle = TemplateMessageHelper
					.getResourceBundle(MensagensSistema.PROJETO, fc
							.getViewRoot().getLocale());
			List<String> errorMessages = ValidatorHelper.valida(projeto,
					resourceBundle);
			if (errorMessages.isEmpty()) {
				try {
					if (edicao) {
						projeto = facade.alterarProjeto(projeto);
					} else {
						projeto = facade.cadastrarProjeto(projeto);
					}
				} catch (Exception e) {
					e.printStackTrace();
					if (!edicao) {
						projeto.setId(null);
					}

					ConstraintViolationException exc = (ConstraintViolationException) e.getCause();
					e.printStackTrace();
					
					//Throwable lastCause = ExceptionFiltro.getLastException(e);
					if (e.getMessage().contains("ConstraintViolationException")) {
						if (exc.getSQLException().getNextException().getMessage()
								.contains("uk_projeto")) {
							Messages.adicionaMensagemDeErro(TemplateMessageHelper
											.getMessage(MensagensSistema.PROJETO,
													"msgUnique", fc.getViewRoot()
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

			return "projetos.jsf";
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String cancelar() {
		return "projetos.jsf";
	}
	
	@Override
	protected void refazerPesquisa() {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the projeto
	 */
	public Projeto getProjeto() {
		return projeto;
	}

	/**
	 * @param projeto the projeto to set
	 */
	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
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
	 * @return the commonsList
	 */
	public CommonsList getCommonsList() {
		return commonsList;
	}

	/**
	 * @param commonsList the commonsList to set
	 */
	public void setCommonsList(CommonsList commonsList) {
		this.commonsList = commonsList;
	}

}
