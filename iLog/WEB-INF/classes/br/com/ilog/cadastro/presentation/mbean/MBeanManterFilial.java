package br.com.ilog.cadastro.presentation.mbean;

import java.io.Serializable;
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
import br.com.ilog.cadastro.business.entity.Filial;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.cadastro.presentation.util.ExceptionFiltro;
import br.com.ilog.geral.presentation.CommonsList;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;


/**
 * @author Wisley P. souza
 */

@Component("mBeanManterFilial")
@AccessScoped
public class MBeanManterFilial  extends AbstractManter implements Serializable{

	
	private static final long serialVersionUID = -2208795529854355181L;
	private static Logger logger = LoggerFactory
			.getLogger(MBeanManterFilial.class);
	
	private Filial filial;
	
	private Filial selectFilial;
	
	private Boolean novaFilial;
	
	private List<SelectItem> comboAtivo;
	
	@Resource(name = "controllerCadastro")
	CadastroFacade filialFacade;

	@Resource( name = "commonsList")
	CommonsList commonsList;
	
	
	@PostConstruct
	void inicializar() {
        edicao =  false;
		inicializarObjetos();
	
		comboAtivo = commonsList.listaBooleanAtivoInativo();

		novaFilial= false;

	}

	public String novaFilial() {
		filial =  new Filial();
		selectFilial =  new Filial();
		novaFilial =  true;
		comboAtivo = commonsList.listaBooleanAtivoInativo();

		edicao =  false;
		
		return "filial.jsf";
	}

	public String cancelar() {
		this.refazerPesquisa();
		return "filiais.jsf";

	}
	
	
	@Override
	public String salvar() {
		
		FacesContext fc = FacesContext.getCurrentInstance();
		ResourceBundle resourceBundle = TemplateMessageHelper
				.getResourceBundle(MensagensSistema.FILIAL, fc.getViewRoot()
						.getLocale());
		List<String> errorMessages = ValidatorHelper.valida(filial,
				resourceBundle);
		if (errorMessages.isEmpty()) {
			try {
				
				if (edicao) {
					filialFacade.alterarFilial(filial);
				} else {
					filialFacade.cadastrarFilial(filial);
				}

			} catch (Exception ex) {
				logger.error("error: {}", ex);

				if (!edicao) {
					filial.setId(null);
				}

				ConstraintViolationException exc = (ConstraintViolationException) ex
						.getCause();
				ex.printStackTrace();

				// Throwable lastCause = ExceptionFiltro.getLastException(e);
				if (ex.getMessage().contains("ConstraintViolationException")) {
					if (exc.getSQLException().getNextException().getMessage()
							.contains("uk_filial_codigo")) {
						Messages.adicionaMensagemDeErro(TemplateMessageHelper
								.getMessage(MensagensSistema.FILIAL,
										"msgUnique", fc.getViewRoot()
												.getLocale()));
					} else if (exc.getSQLException().getNextException()
							.getMessage().contains("uk_filial")) {
						Messages.adicionaMensagemDeErro(TemplateMessageHelper
								.getMessage(MensagensSistema.FILIAL, "msgUnique",
										fc.getViewRoot().getLocale()));
					}
					return null;

				} else {
					Messages.adicionaMensagemDeErro(TemplateMessageHelper
							.getMessage(MensagensSistema.SISTEMA,
									ExceptionFiltro.recursiveException(ex), fc
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

		return "filiais.jsf";
	}
	
	@Override
	public String editar() {
		try {
			filial = filialFacade.getFilialById(selectFilial.getId());
			edicao = true;
		} catch (BusinessException e) {
			e.printStackTrace();
			return null;
		}

		return "filial.jsf";
	}

	@Override
	public String excluir() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			filialFacade.excluirFilial(filial);
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

		return "filiais.jsf";
	}

	@Override
	public void inicializarObjetos() {
		filial = new Filial();
		
		comboAtivo = commonsList.listaBooleanAtivoInativo();
		
	}

	@Override
	protected void refazerPesquisa() {
		MBeanPesquisarFilial mBean = (MBeanPesquisarFilial)JSFRequestBean
				.getManagedBean("mBeanPesquisarFilial");
		mBean.refazerPesquisa();
		
	}

	public Filial getFilial() {
		return filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	public Filial getSelectFilial() {
		return selectFilial;
	}

	public void setSelectFilial(Filial selectFilial) {
		this.selectFilial = selectFilial;
	}

	public Boolean getNovaFilial() {
		return novaFilial;
	}

	public void setNovaFilial(Boolean novaFilial) {
		this.novaFilial = novaFilial;
	}

	public List<SelectItem> getComboAtivo() {
		return comboAtivo;
	}

	public void setComboAtivo(List<SelectItem> comboAtivo) {
		this.comboAtivo = comboAtivo;
	}

}
