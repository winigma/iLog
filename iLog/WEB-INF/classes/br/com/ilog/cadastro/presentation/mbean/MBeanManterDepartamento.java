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
import org.springframework.stereotype.Controller;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.hibernate.validator.ValidatorHelper;
import br.cits.commons.citspresentation.mbeans.AbstractManter;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.commons.citspresentation.util.JSFRequestBean;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.Departamento;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.cadastro.presentation.util.ExceptionFiltro;
import br.com.ilog.geral.presentation.CommonsList;
import br.com.ilog.geral.presentation.Constantes;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;

/**
 * @author Wisley P. souza
 */

@Controller("mBeanManterDepartamento")
@AccessScoped
public class MBeanManterDepartamento extends AbstractManter {

	private static final long serialVersionUID = 7706823618745473308L;
	private static Logger logger = LoggerFactory
			.getLogger(MBeanManterDepartamento.class);

	@Resource(name = "controllerCadastro")
	CadastroFacade departamentoFacade;
	
	@Resource( name = "commonsList")
	CommonsList commonsList;

	private Departamento departamento;
	private Boolean novoDepartamento;
	private Departamento selectDep;
	private List<SelectItem> comboStatus;
	private List<SelectItem> comboAtivo;

	@PostConstruct
	void inicializar() {
		inicializarObjetos();
		novoDepartamento = false;
		comboAtivo = commonsList.listaBooleanAtivoInativo();
		//comboStatus = CommonsList.
	}
	public String salvarNovo() {
		try {
			if (this.salvar() != null) {
				return this.novoDepartamento();

			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	public String novoDepartamento() {
		departamento = new Departamento();
		novoDepartamento = true;
		edicao = false;

		return "departamento.jsf";
	}

	public String cancelar() {

		return Constantes.CANCELAR;

	}

	@Override
	public String salvar() {
		FacesContext fc = FacesContext.getCurrentInstance();
		
		ResourceBundle resourceBundle = TemplateMessageHelper
				.getResourceBundle(MensagensSistema.DEPARTAMENTO, fc
						.getViewRoot().getLocale());

		List<String> errorMessages = ValidatorHelper.valida(departamento,
				resourceBundle);
		if (errorMessages.isEmpty()) {
			try {
				if (edicao) {
					departamentoFacade.alterarDepartamento(departamento);
				} else {
					departamento.setAtivo(true);
					departamentoFacade.cadastrarDepartamento(departamento);
				}
			} catch (BusinessException e) {
				logger.error("error: {}", e);

				Throwable lastCause = ExceptionFiltro.getLastException(e);
				if (lastCause.getMessage().contains("unique")) {
					if (lastCause.getMessage().contains("'UK_DEPARTAMENTO'")) {
						Messages.adicionaMensagemDeErro(TemplateMessageHelper
								.getMessage(MensagensSistema.DEPARTAMENTO,
										"msgUniqueDes", fc.getViewRoot()
												.getLocale()));
					}
					return null;

				} else {
					Messages.adicionaMensagemDeErro(TemplateMessageHelper
							.getMessage(MensagensSistema.SISTEMA,
									ExceptionFiltro.recursiveException(e), fc
											.getViewRoot().getLocale()));
				}

				return null;

			}catch (Exception e) {
				logger.error("error: {}", e);

				Throwable lastCause = ExceptionFiltro.getLastException(e);
				ConstraintViolationException ex = (ConstraintViolationException) e
						.getCause();
				ex.printStackTrace();
				if (e.getMessage().contains("ConstraintViolationException") ) {
					
					
					if (ex.getSQLException().getNextException().getMessage().contains("uk_departamento")) {
						Messages.adicionaMensagemDeErro(TemplateMessageHelper
								.getMessage(MensagensSistema.DEPARTAMENTO, "msgUniqueDes",
										fc.getViewRoot().getLocale()));
					}
					
					return null;

				}else
				if (lastCause.getMessage().contains("unique")) {
					if (lastCause.getMessage().contains("'UK_DEPARTAMENTO'")) {
						Messages.adicionaMensagemDeErro(TemplateMessageHelper
								.getMessage(MensagensSistema.DEPARTAMENTO,
										"msgUniqueDes", fc.getViewRoot()
												.getLocale()));
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
		return "departamentos.jsf";
	}

	@Override
	public String editar() {
		//Long idRegistro = Long.valueOf(JSFRequestBean.getParameter("id"));

		try {
			departamento = selectDep;
			edicao = true;
			return "departamento.jsf";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public String excluir() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			departamentoFacade.excluirDepartamento(departamento);
			Messages.adicionaMensagemDeInfo(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG_EXCLUIR_SUCESSO", fc
							.getViewRoot().getLocale()));

			this.refazerPesquisa();
			return "departamentos.jsf";
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper
					.getMessage(ExceptionFiltro.recursiveException(e)));

			return null;
		}

	}

	@Override
	public void inicializarObjetos() {
		departamento = new Departamento();

	}

	@Override
	protected void refazerPesquisa() {
		MBeanPesquisarDepartamento mBean = (MBeanPesquisarDepartamento) JSFRequestBean
				.getManagedBean("mBeanPesquisarDepartamento");
		mBean.refazerPesquisa();

	}

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	public Boolean getNovoDepartamento() {
		return novoDepartamento;
	}

	public void setNovoDepartamento(Boolean novoDepartamento) {
		this.novoDepartamento = novoDepartamento;
	}
	public Departamento getSelectDep() {
		return selectDep;
	}
	public void setSelectDep(Departamento selectDep) {
		this.selectDep = selectDep;
	}
	public List<SelectItem> getComboStatus() {
		return comboStatus;
	}
	public List<SelectItem> getComboAtivo() {
		return comboAtivo;
	}
	public void setComboAtivo(List<SelectItem> comboAtivo) {
		this.comboAtivo = comboAtivo;
	}
	public void setComboStatus(List<SelectItem> comboStatus) {
		this.comboStatus = comboStatus;
	}

}
