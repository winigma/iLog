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
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.Nivel;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.cadastro.presentation.util.ExceptionFiltro;
import br.com.ilog.geral.presentation.CommonsList;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;

/**
 * @author Wisley P. souza
 * @data 19/07/2012
 */

@Component("mBeanManterNivel")
@AccessScoped
public class MBeanManterNivel extends AbstractManter implements Serializable {

	private static final long serialVersionUID = 1L;

	private static Logger logger = LoggerFactory
			.getLogger(MBeanManterNivel.class);

	private Nivel nivel;
	private Nivel selectNivel;
	private Boolean novoNivel;

	private List<SelectItem> comboAtivo;

	@Resource(name = "controllerCadastro")
	CadastroFacade nivelFacade;

	@Resource(name = "commonsList")
	CommonsList commonsList;

	@PostConstruct
	void inicializar() {
		edicao = false;
		inicializarObjetos();

		comboAtivo = commonsList.listaBooleanAtivoInativo();

		novoNivel = false;

	}

	public String novoNivel() {
		nivel = new Nivel();
		selectNivel = new Nivel();
		novoNivel = true;
		comboAtivo = commonsList.listaBooleanAtivoInativo();

		edicao = false;

		return "nivel.jsf";
	}

	public String cancelar() {
		this.refazerPesquisa();
		return "niveis.jsf";

	}

	@Override
	public String salvar() {
		FacesContext fc = FacesContext.getCurrentInstance();
		ResourceBundle resourceBundle = TemplateMessageHelper
				.getResourceBundle(MensagensSistema.NIVEL, fc.getViewRoot()
						.getLocale());
		List<String> errorMessages = ValidatorHelper.valida(nivel,
				resourceBundle);

		if (errorMessages.isEmpty()) {
			try {

				if (edicao) {
					nivelFacade.alterarNivel(nivel);
				} else {
					nivel.setAtivo(true);
					nivelFacade.cadastrarNivel(nivel);
				}

			} catch (Exception e) {
				if (!edicao) {
					nivel.setId(null);
				}
				logger.error("error: {}", e);
				e.printStackTrace();

				ConstraintViolationException ex = (ConstraintViolationException) e
						.getCause();
				ex.printStackTrace();
				// Throwable lastCause = ExceptionFiltro.getLastException(e);
				if (e.getMessage().contains("ConstraintViolationException")) {

					if (ex.getSQLException().getNextException().getMessage().contains("uk_nivel")) {
						Messages.adicionaMensagemDeErro(TemplateMessageHelper
								.getMessage(MensagensSistema.NIVEL,
										"lblDescUnique", fc.getViewRoot()
												.getLocale()));
					}
					if (ex.getSQLException().getNextException().getMessage()
							.contains("uk_nivel_codigo")) {
						Messages.adicionaMensagemDeErro(TemplateMessageHelper
								.getMessage(MensagensSistema.NIVEL,
										"lblCodeUnique", fc.getViewRoot()
												.getLocale()));
					}

					return null;

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

		return "niveis.jsf";
	}

	@Override
	public String editar() {
		try {
			nivel = nivelFacade.getNivelById(selectNivel.getId());
			edicao = true;
		} catch (BusinessException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return "nivel.jsf";
	}

	@Override
	public String excluir() {
		try {

			nivelFacade.excluirNivel(nivel);

			FacesContext fc = FacesContext.getCurrentInstance();
			Messages.adicionaMensagemDeInfo(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG_EXCLUIR_SUCESSO", fc
							.getViewRoot().getLocale()));
			return "niveis.jsf";

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
		nivel = new Nivel();
		comboAtivo = commonsList.listaBooleanAtivoInativo();

	}

	@Override
	protected void refazerPesquisa() {
		// TODO Auto-generated method stub

	}

	public Nivel getNivel() {
		return nivel;
	}

	public void setNivel(Nivel nivel) {
		this.nivel = nivel;
	}

	public Nivel getSelectNivel() {
		return selectNivel;
	}

	public void setSelectNivel(Nivel selectNivel) {
		this.selectNivel = selectNivel;
	}

	public Boolean getNovoNivel() {
		return novoNivel;
	}

	public void setNovoNivel(Boolean novoNivel) {
		this.novoNivel = novoNivel;
	}

	public List<SelectItem> getComboAtivo() {
		return comboAtivo;
	}

	public void setComboAtivo(List<SelectItem> comboAtivo) {
		this.comboAtivo = comboAtivo;
	}

}
