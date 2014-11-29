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
import br.com.ilog.cadastro.business.entity.MateriaPrima;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.cadastro.presentation.util.ExceptionFiltro;
import br.com.ilog.geral.presentation.CommonsList;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;

@AccessScoped
@Component( "mBeanManterMateriaPrima" )
public class MBeanManterMateriaPrima extends AbstractManter {

	/**  */
	private static final long serialVersionUID = -228308480256137780L;
	private Logger logger = LoggerFactory.getLogger( MBeanManterMateriaPrima.class );
	
	@Resource( name = "commonsList" )
	CommonsList commonsList;
	
	@Resource( name = "controllerCadastro" )
	CadastroFacade facade;
	
	private MateriaPrima entity;
	
	private List<SelectItem> comboAtivo;
	
	private boolean edicao;
	
	@Override
	@PostConstruct
	public void inicializarObjetos() {
		entity = new MateriaPrima();
		edicao = false;
		comboAtivo = commonsList.listaBooleanAtivoInativo();
	}
	
	public String novo() {
		inicializarObjetos();
		return "materiaPrima.jsf";
	}
	
	@Override
	public String editar() {
		Long idRegistro = Long.valueOf(JSFRequestBean.getParameter("id"));

		try {
			
			entity = facade.getMateriaPrimaById(idRegistro);
			
		} catch (BusinessException e) {
			e.printStackTrace();
			logger.error("erro: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.MATERIA_PRIMA, e));
			return null;
		}
		edicao = true;
		return "materiaPrima.jsf";
	}

	@Override
	public String salvar() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			ResourceBundle resourceBundle = TemplateMessageHelper
					.getResourceBundle(MensagensSistema.MATERIA_PRIMA, fc
							.getViewRoot().getLocale());
			List<String> errorMessages = ValidatorHelper.valida(entity,
					resourceBundle);
			
			if (errorMessages.isEmpty()) {
				try {
			
					if (edicao) {
						facade.alterarMateriaPrima(entity);
					} else {
						facade.cadastrarMateriaPrima(entity);
					}
	
				} catch (BusinessException e) {
					logger.error("error:{}", e);
					e.printStackTrace();
					
					Messages.adicionaMensagemDeErro(TemplateMessageHelper
							.getMessage(MensagensSistema.MATERIA_PRIMA, e));
					return null;
				} catch (Exception e) {
					if (!edicao) {
						entity.setId(null);
					}
					logger.error("error:{}", e);
					e.printStackTrace();
					Messages.adicionaMensagemDeErro(TemplateMessageHelper
								.getMessage(ExceptionFiltro
										.recursiveException(e)));
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
			return "materiasPrima.jsf";
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error:{}", e);
			
			return null;
		}
	}
	
	@Override
	public String excluir() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			facade.excluirMateriaPrima(entity);
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
		return "materiasPrima.jsf";
	}


	public String cancelar() {
		return "materiasPrima.jsf";
	}
	
	@Override
	protected void refazerPesquisa() {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the entity
	 */
	public MateriaPrima getEntity() {
		return entity;
	}

	/**
	 * @param entity the entity to set
	 */
	public void setEntity(MateriaPrima entity) {
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
