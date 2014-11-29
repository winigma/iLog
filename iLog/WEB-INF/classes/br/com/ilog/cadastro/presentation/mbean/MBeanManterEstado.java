package br.com.ilog.cadastro.presentation.mbean;

import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;

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
import br.com.ilog.cadastro.business.entity.Estado;
import br.com.ilog.cadastro.business.entity.Pais;
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
@Component("mBeanManterEstado")
public class MBeanManterEstado extends AbstractManter {

	/**  */
	private static final long serialVersionUID = -5557784599255046521L;

	private static Logger logger = LoggerFactory
			.getLogger(MBeanManterEstado.class);

	@Resource(name = "controllerCadastro")
	CadastroFacade estadoFacade;

	private BasicFiltroEstado filtro;
	private Estado estado;

	private Boolean novoEstado;

	private List<Pais> comboPaises;

	@PostConstruct
	void inicializar() {
		inicializarObjetos();

		try {
			popularComboPais();
		} catch (Exception e) {

			e.printStackTrace();

			FacesContext fc = FacesContext.getCurrentInstance();

			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG_001", fc.getViewRoot()
							.getLocale()));
		}

	}

	public void popularComboPais() {
		comboPaises = Collections.emptyList();

		try {

			comboPaises = estadoFacade.listarPaises(null);

		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	public String novoEstado() {
		estado = new Estado();
		novoEstado = true;
		edicao = false;

		return "estado.jsf";
	}

	public String salvar() {

		FacesContext fc = FacesContext.getCurrentInstance();
		ResourceBundle resourceBundle = TemplateMessageHelper
				.getResourceBundle(MensagensSistema.ESTADO, fc.getViewRoot()
						.getLocale());
		List<String> errorMessages = ValidatorHelper.valida(estado,
				resourceBundle);

		if (errorMessages.isEmpty()) {
			try {
				if (edicao) {
					estadoFacade.alterarEstado(estado);
				} else {
					estadoFacade.cadastrarEstado(estado);
				}
			} catch (Exception e) {

				logger.error("error: {}", e);
				if (!edicao) {
					estado.setId(null);
				}
				ConstraintViolationException exc = (ConstraintViolationException) e
						.getCause();
				e.printStackTrace();
				// Throwable lastCause = ExceptionFiltro.getLastException(e);
				if (e.getMessage().contains("ConstraintViolationException")) {
					if (exc.getSQLException().getNextException().getMessage()
							.contains("estado_unique")) {
						Messages.adicionaMensagemDeErro(TemplateMessageHelper
								.getMessage(MensagensSistema.ESTADO,
										"msgCheckEstado", fc.getViewRoot()
												.getLocale()));
						return null;

					} else if (exc.getSQLException().getNextException()
							.getMessage().contains("uk_estado_sigla")) {
						Messages.adicionaMensagemDeErro(TemplateMessageHelper
								.getMessage(MensagensSistema.ESTADO,
										"msgUniqueSigla", fc.getViewRoot()
												.getLocale()));
						return null;
					} else if (exc.getSQLException().getNextException()
							.getMessage().contains("chk_estado_duplicado")) {
						Messages.adicionaMensagemDeErro(TemplateMessageHelper
								.getMessage(MensagensSistema.ESTADO,
										"msgCheckEstado", fc.getViewRoot()
												.getLocale()));
						return null;

					} else if (exc.getSQLException().getNextException()
							.getMessage()
							.contains("chk_estado_sigla_duplicado")) {
						Messages.adicionaMensagemDeErro(TemplateMessageHelper
								.getMessage(MensagensSistema.ESTADO,
										"msgUniqueSigla", fc.getViewRoot()
												.getLocale()));
						return null;

					} else {
						e.printStackTrace();
						Messages.adicionaMensagemDeErro(TemplateMessageHelper
								.getMessage(ExceptionFiltro
										.recursiveException(e)));
					}

					return null;
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
		// refazerPesquisa();
		return "estados.jsf";
	}

	@Override
	public String editar() {

		Long idRegistro = Long.valueOf(JSFRequestBean.getParameter("id"));
		try {

			estado = (Estado) estadoFacade.getEstadoById(idRegistro);

		} catch (BusinessException e) {
			logger.error("erro: {}", e);
			e.printStackTrace();
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.CIDADE, e));
		}
		edicao = true;
		return "estado.jsf";

	}

	@Override
	public String excluir() {

		try {

			estadoFacade.excluirEstado(this.getEstado());

			FacesContext fc = FacesContext.getCurrentInstance();
			Messages.adicionaMensagemDeInfo(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG_EXCLUIR_SUCESSO", fc
							.getViewRoot().getLocale()));

			return "estados.jsf";
		} catch (Exception e) {

			e.printStackTrace();
			logger.error("error: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper
					.getMessage(ExceptionFiltro.recursiveException(e)));

			return null;
		}
	}

	public String cancelar() {
		return "estados.jsf";
	}

	@Override
	public void inicializarObjetos() {
		novoEstado = false;
		comboPaises = Collections.emptyList();

		estado = new Estado();
		estado.setPais(new Pais());
	}

	@Override
	protected void refazerPesquisa() {
		// TODO Auto-generated method stub

	}

	/**
	 * @param estadoFacade
	 *            the estadoFacade to set
	 */
	public void setEstadoFacade(CadastroFacade estadoFacade) {
		this.estadoFacade = estadoFacade;
	}

	/**
	 * @return the filtro
	 */
	public BasicFiltroEstado getFiltro() {
		return filtro;
	}

	/**
	 * @param filtro
	 *            the filtro to set
	 */
	public void setFiltro(BasicFiltroEstado filtro) {
		this.filtro = filtro;
	}

	/**
	 * @return the estado
	 */
	public Estado getEstado() {
		return estado;
	}

	/**
	 * @param estado
	 *            the estado to set
	 */
	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	/**
	 * @return the novoEstado
	 */
	public Boolean getNovoEstado() {
		return novoEstado;
	}

	/**
	 * @param novoEstado
	 *            the novoEstado to set
	 */
	public void setNovoEstado(Boolean novoEstado) {
		this.novoEstado = novoEstado;
	}

	/**
	 * @return the comboPaises
	 */
	public List<Pais> getComboPaises() {
		return comboPaises;
	}

	/**
	 * @param comboPaises
	 *            the comboPaises to set
	 */
	public void setComboPaises(List<Pais> comboPaises) {
		this.comboPaises = comboPaises;
	}

}
