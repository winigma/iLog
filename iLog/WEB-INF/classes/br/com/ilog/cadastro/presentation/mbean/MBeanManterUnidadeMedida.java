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
import br.com.ilog.cadastro.business.entity.UnidadeMedida;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.cadastro.presentation.util.ExceptionFiltro;
import br.com.ilog.geral.presentation.CommonsList;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;

@Component("mBeanManterUnidadeMedida")
@AccessScoped
public class MBeanManterUnidadeMedida extends AbstractManter implements
		Serializable {

	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory
			.getLogger(MBeanManterUnidadeMedida.class);

	private UnidadeMedida selectUnidade;
	private UnidadeMedida unidade;
	private Boolean novaUnidade;

	private List<SelectItem> comboAtivo;

	@Resource(name = "controllerCadastro")
	CadastroFacade unidadeFacade;

	@Resource(name = "commonsList")
	CommonsList commonsList;

	@PostConstruct
	void inicializar() {
		edicao = false;
		inicializarObjetos();

		comboAtivo = commonsList.listaBooleanAtivoInativo();

		novaUnidade = false;

	}

	public String novaUnidade() {
		unidade = new UnidadeMedida();
		selectUnidade = new UnidadeMedida();
		novaUnidade = true;
		comboAtivo = commonsList.listaBooleanAtivoInativo();

		edicao = false;

		return "unidademedida.jsf";
	}

	public String cancelar() {
		this.refazerPesquisa();
		return "unidadesmedida.jsf";

	}

	@Override
	public String salvar() {
		FacesContext fc = FacesContext.getCurrentInstance();
		ResourceBundle resourceBundle = TemplateMessageHelper
				.getResourceBundle(MensagensSistema.UNIDADE_MEDIDA, fc
						.getViewRoot().getLocale());
		List<String> errorMessages = ValidatorHelper.valida(unidade,
				resourceBundle);
		if (errorMessages.isEmpty()) {

			try {
				if (edicao) {
					unidadeFacade.alterarUnidMedida(unidade);
				} else {
					unidadeFacade.cadsatrarUnidMedida(unidade);
				}
			} catch (BusinessException e) {
				logger.error("error: {}", e);
				e.printStackTrace();
				return null;
			} catch (Exception e) {
                    if(!edicao)
				         unidade.setId(null);
				logger.error("error: {}", e);
				//e.printStackTrace();
				ConstraintViolationException ex = (ConstraintViolationException) e
						.getCause();
				ex.printStackTrace();
				Throwable lastCause = ExceptionFiltro.getLastException(e);
				if (e.getMessage().contains("ConstraintViolationException") ) {
					
				//	System.out.println(ex.getSQL());
					if (ex.getSQLException().getNextException().getMessage().contains("uk_unidade_medida")) {
						Messages.adicionaMensagemDeErro(TemplateMessageHelper
								.getMessage(MensagensSistema.UNIDADE_MEDIDA, "msgUnique",
										fc.getViewRoot().getLocale()));
					}
					if (ex.getSQLException().getNextException().getMessage().contains("uk_descricao_unica")) {
						Messages.adicionaMensagemDeErro(TemplateMessageHelper
								.getMessage(MensagensSistema.UNIDADE_MEDIDA, "msgUnique",
										fc.getViewRoot().getLocale()));
					}
					
					return null;

				}else
				if (lastCause.getMessage().contains("uk_unidade_medida")) {
					if (lastCause.getMessage().contains(
							"chk_motivo_duplicado")) {
						Messages.adicionaMensagemDeErro(TemplateMessageHelper
								.getMessage(MensagensSistema.MOTIVO,
										"msgCheckMotivo", fc.getViewRoot()
												.getLocale()));
					} else {
						Messages.adicionaMensagemDeErro(TemplateMessageHelper
								.getMessage(MensagensSistema.SISTEMA,
										ExceptionFiltro.recursiveException(e), fc
												.getViewRoot().getLocale()));
					}
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

		return "unidadesmedida.jsf";
	}

	@Override
	public String editar() {
		try {
			unidade =  unidadeFacade.getUnidMedidaById(selectUnidade.getId());
			edicao = true;

		} catch (BusinessException e) {
			e.printStackTrace();
			return null;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return "unidademedida.jsf";
	}

	@Override
	public String excluir() {
		try {
			
			unidadeFacade.excluirUnidMedida(unidade);

			FacesContext fc = FacesContext.getCurrentInstance();
			Messages.adicionaMensagemDeInfo(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG_EXCLUIR_SUCESSO", fc
							.getViewRoot().getLocale()));
			
			return "unidadesmedida.jsf";
			
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
		unidade = new UnidadeMedida();
		comboAtivo = commonsList.listaBooleanAtivoInativo();

	}

	@Override
	protected void refazerPesquisa() {
		// TODO Auto-generated method stub

	}

	public UnidadeMedida getSelectUnidade() {
		return selectUnidade;
	}

	public void setSelectUnidade(UnidadeMedida selectUnidade) {
		this.selectUnidade = selectUnidade;
	}

	public UnidadeMedida getUnidade() {
		return unidade;
	}

	public void setUnidade(UnidadeMedida unidade) {
		this.unidade = unidade;
	}

	public Boolean getNovaUnidade() {
		return novaUnidade;
	}

	public void setNovaUnidade(Boolean novaUnidade) {
		this.novaUnidade = novaUnidade;
	}

	public List<SelectItem> getComboAtivo() {
		return comboAtivo;
	}

	public void setComboAtivo(List<SelectItem> comboAtivo) {
		this.comboAtivo = comboAtivo;
	}

}
