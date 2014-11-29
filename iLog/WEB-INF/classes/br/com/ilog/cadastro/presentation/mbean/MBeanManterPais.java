package br.com.ilog.cadastro.presentation.mbean;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.hibernate.exception.ConstraintViolationException;
import org.primefaces.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.hibernate.validator.ValidatorHelper;
import br.cits.commons.citspresentation.mbeans.AbstractManter;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.commons.citspresentation.util.JSFRequestBean;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.Continente;
import br.com.ilog.cadastro.business.entity.Pais;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.cadastro.presentation.util.ExceptionFiltro;
import br.com.ilog.geral.presentation.Constantes;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;

/**
 * @author Wisley P. souza
 */

@Component("mBeanManterPais")
@AccessScoped
public class MBeanManterPais extends AbstractManter {

	private static final long serialVersionUID = 8777933102486046503L;
	private static Logger logger = LoggerFactory
			.getLogger(MBeanManterPais.class);
	private Pais paises;

	private Pais selectPais;
	private Pais paisPadrao;

	private Boolean novoPais;

	private List<SelectItem> comboContinentes;

	// private ConverterUtil<Pais> converterPais;

	// @ManagedProperty(value = "#{controllerCadastro}")
	@Resource(name = "controllerCadastro")
	CadastroFacade paisFacade;
	JSONObject json;
	String tess;

	@PostConstruct
	void inicializar() {

		if (!edicao) {
			inicializarObjetos();
		}
		novoPais = false;

	}

	public String salvarNovo() {
		try {
			if (this.salvar() != null) {
				return this.novoPais();

			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public String novoPais() {

		paises = new Pais();
		selectPais = new Pais();
		novoPais = true;
		edicao = false;
		return "pais.jsf";
	}

	public String cancelar() {
		this.refazerPesquisa();
		return "paises.jsf";

	}

	@Override
	public String editar() {

		try {
			paises = paisFacade.getPaisById(selectPais.getId());
			edicao = true;
			return "pais.jsf";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String excluir() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			paisFacade.excluirPais(paises);
			Messages.adicionaMensagemDeInfo(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG_EXCLUIR_SUCESSO", fc
							.getViewRoot().getLocale()));

			this.refazerPesquisa();
		} catch (Exception e) {
			logger.error("error: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper
					.getMessage(ExceptionFiltro.recursiveException(e)));

			this.refazerPesquisa();
			return null;
		}
		return "paises.jsf";
	}

	@Override
	public void inicializarObjetos() {
		paises = new Pais();

		paisPadrao = new Pais();

	}

	@Override
	protected void refazerPesquisa() {
		// atualiza a tela de pesquisa para atualizar possiveis modificacoes nos
		// campos utilizados na tabela de pesquisa

		MBeanPesquisarPais manageBean = (MBeanPesquisarPais) JSFRequestBean
				.getManagedBean(Constantes.MBEAM_PESQUISAR_PAISES);
		manageBean.refazerPesquisa();
	}

	/**
	 * public void mostrarModalPadrao() { UIModalPanel modal = (UIModalPanel)
	 * RichFunction .findComponent("popupConfirmacao"); if (modal != null) {
	 * modal.setShowWhenRendered(true);
	 * AjaxContext.getCurrentInstance().addComponentToAjaxRender(modal); }
	 * 
	 * }
	 **/
	// Verificar quem é o pais padrao
	private Pais verificarPaisPadrao() {
		try {
			return paisFacade.getPaisByPadrao();
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return null;
	}

	// Altera pais padrao se modificado
	public String alterarPaisPadrao() {
		if (paises.isPadrao() == true) {
			try {
				paisPadrao = verificarPaisPadrao();
				if (paisPadrao != null) {
					paisPadrao.setPadrao(false);
					paisFacade.alterarPais(paisPadrao);
				} else {
					paises.setPadrao(true);
				}
			} catch (BusinessException ex) {
				ex.printStackTrace();
			}
		}
		return salvarPais();
	}

	@Override
	public String salvar() {
		if (paises.isPadrao() == true) {

			if (!paises.equals(verificarPaisPadrao())) {
				return "";
			} else {
				return salvarPais();
			}
		} else {
			return salvarPais();
		}
	}

	public String salvarPais() {
		FacesContext fc = FacesContext.getCurrentInstance();
		ResourceBundle resourceBundle = TemplateMessageHelper
				.getResourceBundle(MensagensSistema.PAIS, fc.getViewRoot()
						.getLocale());
		List<String> errorMessages = ValidatorHelper.valida(paises,
				resourceBundle);
		if (errorMessages.isEmpty()) {
			try {
				if (edicao) {
					paisFacade.alterarPais(paises);
				} else {
					paisFacade.cadastrarPais(paises);
				}
			}

			catch (Exception ex) {

				logger.error("error: {}", ex);

				if (!edicao) {
					paises.setId(null);
				}

				ConstraintViolationException exc = (ConstraintViolationException) ex
						.getCause();
				ex.printStackTrace();

				// Throwable lastCause = ExceptionFiltro.getLastException(e);
				if (ex.getMessage().contains("ConstraintViolationException")) {
					if (exc.getSQLException().getNextException().getMessage()
							.contains("uk_pais_sigla")) {
						Messages.adicionaMensagemDeErro(TemplateMessageHelper
								.getMessage(MensagensSistema.PAIS,
										"msgUniqueSigla", fc.getViewRoot()
												.getLocale()));
					} else if (exc.getSQLException().getNextException()
							.getMessage().contains("uk_pais")) {
						Messages.adicionaMensagemDeErro(TemplateMessageHelper
								.getMessage(MensagensSistema.PAIS, "msgUnique",
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

		return "paises.jsf";
	}

	public List<SelectItem> getComboContinentes() {
		comboContinentes = new ArrayList<SelectItem>();
		for (Continente c : Continente.values()) {
			comboContinentes.add(new SelectItem(c, TemplateMessageHelper
					.getMessage(MensagensSistema.CONTINENTE, c.name())));
		}
		return comboContinentes;
	}

	public void setComboContinentes(List<SelectItem> comboContinentes) {
		this.comboContinentes = comboContinentes;
	}

	public Pais getPaises() {
		return paises;
	}

	public void setPaises(Pais paises) {
		this.paises = paises;
	}

	public Boolean getNovoPais() {
		return novoPais;
	}

	public void setNovoPais(Boolean novoPais) {
		this.novoPais = novoPais;
	}

	public JSONObject getJson() {
		return json;
	}

	public void setJson(JSONObject json) {
		this.json = json;
	}

	public String getTess() {
		return tess;
	}

	public void setTess(String tess) {
		this.tess = tess;
	}

	public Pais getSelectPais() {
		return selectPais;
	}

	public void setSelectPais(Pais selectPais) {
		this.selectPais = selectPais;
	}

	/**
	 * @param paisFacade
	 *            the paisFacade to set
	 */
	public void setPaisFacade(CadastroFacade paisFacade) {
		this.paisFacade = paisFacade;
	}

	/**
	 * @return the paisFacade
	 */
	public CadastroFacade getPaisFacade() {
		return paisFacade;
	}
}
