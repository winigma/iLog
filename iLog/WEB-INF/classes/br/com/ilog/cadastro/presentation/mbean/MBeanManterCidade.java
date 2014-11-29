package br.com.ilog.cadastro.presentation.mbean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;

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
import br.com.ilog.cadastro.business.entity.Cidade;
import br.com.ilog.cadastro.business.entity.Estado;
import br.com.ilog.cadastro.business.entity.Pais;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroCidade;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroEstado;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.cadastro.presentation.util.ConverterUtil;
import br.com.ilog.cadastro.presentation.util.ExceptionFiltro;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;

/**
 * @author Celso Oscar Junior
 */

@Controller("mBeanManterCidade")
@AccessScoped
public class MBeanManterCidade extends AbstractManter {

	private static final long serialVersionUID = 6686725568471200046L;
	private static Logger logger = LoggerFactory
			.getLogger(MBeanManterCidade.class);

	private Cidade cidade;
	private BasicFiltroCidade filtro;

	private Cidade selectCidade;

	private List<Pais> comboPais;
	private List<Estado> comboEstado;

	private ConverterUtil<Pais> converterPais;
	private ConverterUtil<Estado> converterEstado;

	private boolean novaCidade;

	@Resource(name = "controllerCadastro")
	CadastroFacade facade;

	@PostConstruct
	void inicializar() {
		cidade = new Cidade();
		cidade.setEstado(new Estado());

		filtro = new BasicFiltroCidade();
		filtro.setPais(new Pais());

		comboPais = new ArrayList<Pais>();
		comboEstado = new ArrayList<Estado>();

		try {
			popularComboPais();
			popularComboEstado();
		} catch (Exception e) {
			FacesContext fc = FacesContext.getCurrentInstance();
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG_001", fc.getViewRoot()
							.getLocale()));
		}
	}

	public String salvarNovo() {
		try {
			if (this.salvar() != null) {
				return this.novaCidade();

			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Metodo chamado pelo comboPais.
	 */
	public void popularCombosEstadoCidade() {
		popularComboEstado();
		// popularComboCidade();
	}

	public void popularComboPais() {
		comboPais = Collections.emptyList();

		try {

			comboPais = facade.listarPaises(null);

		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	public void popularComboEstado() {
		comboEstado = new ArrayList<Estado>();
		try {
			List<Estado> estados = facade.listarEstados(new BasicFiltroEstado(
					filtro.getPais()));

			comboEstado = estados;

		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	public String novaCidade() {
		filtro = new BasicFiltroCidade();
		filtro.setPais(new Pais());
		filtro.setEstado(new Estado());

		cidade = new Cidade();
		cidade.setEstado(new Estado());

		novaCidade = true;
		edicao = false;

		return "cidade.jsf";
	}

	@Override
	public String salvar() {

		cidade.setPais(filtro.getPais());
		cidade.setEstado(filtro.getEstado());
		//cidade.setNome(cidade.getNome().trim());
		// cidade.setNome(cidade.getNome().trim());
		// cidade.setSigla(filtro.getSiglaCidade());

		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			ResourceBundle resourceBundle = TemplateMessageHelper
					.getResourceBundle(MensagensSistema.CIDADE,
							JSFRequestBean.getLocale());
			List<String> errorMessages = ValidatorHelper.valida(cidade,
					resourceBundle);
			if (errorMessages.isEmpty()) {
				try {
					if (edicao) {
						facade.alterarCidade(cidade);
					} else {
						cidade = facade.cadastrarCidade(cidade);
					}
				} catch (Exception e) {

					logger.error("error: {}", e);
					if( !edicao ){
						cidade.setId(null);
					}
					/**
					 * VERIFICACAO DE CONSTRAINTS
					 */
					ConstraintViolationException exc = (ConstraintViolationException) e.getCause();
					//Throwable lastCause = ExceptionFiltro.getLastException(e);
					if (e.getMessage().contains("ConstraintViolationException")) {
						if (exc.getSQLException().getNextException().getMessage().contains(
								"uk_cidade")) {
							Messages.adicionaMensagemDeErro(TemplateMessageHelper
									.getMessage(MensagensSistema.CIDADE,
											"msgCheckCidade", fc.getViewRoot()
													.getLocale()));
						}
						return null;

					} else if (exc.getSQLException().getNextException().getMessage().contains("unique")) {
						if (exc.getSQLException().getNextException().getMessage().contains("'UK_CIDADE_SIGLA'")) {
							Messages.adicionaMensagemDeErro(TemplateMessageHelper
									.getMessage(MensagensSistema.CIDADE,
											"msgUniqueSigla", fc.getViewRoot()
													.getLocale()));
						}

						return null;
					} else {

						Messages.adicionaMensagemDeErro(TemplateMessageHelper
								.getMessage(ExceptionFiltro
										.recursiveException(e)));
						e.printStackTrace();
						//this.refazerPesquisa();
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
			cidade = new Cidade();
			refazerPesquisa();
			return "cidades.jsf";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String editar() {
		// Long idRegistro = Long.valueOf(JSFRequestBean.getParameter("id"));

		try {

			cidade = facade.getCidadeById( selectCidade.getId() );

			Estado estado = new Estado();
			if (cidade.getEstado() != null) {
				estado = facade.getEstadoById(cidade.getEstado().getId());
			}
			Pais pais =  new Pais();
			if (cidade.getPais() != null) {
				 pais = cidade.getPais();
			}
			filtro = new BasicFiltroCidade();
			filtro.setPais(pais);
			filtro.setEstado(estado);
			filtro.setNomeCidade(cidade.getNome());
			filtro.setSiglaCidade(cidade.getSigla());

			

		} catch (BusinessException e) {
			logger.error("erro: {}", e);
			e.printStackTrace();
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.CIDADE, e));
		}

		edicao = true;
		return "cidade.jsf";
	}

	@Override
	public String excluir() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			facade.excluirCidade(cidade);
			Messages.adicionaMensagemDeInfo(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG_EXCLUIR_SUCESSO", fc
							.getViewRoot().getLocale()));

			this.refazerPesquisa();
		} catch (Exception e) {
			/*
			 * logger.error("error: {}", e);
			 * Messages.adicionaMensagemDeErro(TemplateMessageHelper
			 * .getMessage("MSG_EXCLUIR")); this.refazerPesquisa();
			 */
			logger.error("error: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper
					.getMessage(ExceptionFiltro.recursiveException(e)));

			this.refazerPesquisa();
			return null;
		}
		return "cidades.jsf";
	}

	@Override
	public void inicializarObjetos() {
		cidade = new Cidade();
	}

	@Override
	protected void refazerPesquisa() {
		MBeanPesquisarCidade mBean = (MBeanPesquisarCidade) JSFRequestBean
				.getManagedBean("mBeanPesquisarCidade");
		mBean.refazerPesquisa();
	}

	// metodo cancelar
	public String cancelar() {
		return "cidades.jsf";
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public Boolean getNovaCidade() {
		return novaCidade;
	}

	public void setNovaCidade(Boolean novaCidade) {
		this.novaCidade = novaCidade;
	}

	public List<Pais> getComboPais() {
		return comboPais;
	}

	public void setComboPais(List<Pais> comboPais) {
		this.comboPais = comboPais;
	}

	public List<Estado> getComboEstado() {
		return comboEstado;
	}

	public void setComboEstado(List<Estado> comboEstado) {
		this.comboEstado = comboEstado;
	}

	public void setNovaCidade(boolean novaCidade) {
		this.novaCidade = novaCidade;
	}

	public BasicFiltroCidade getFiltro() {
		return filtro;
	}

	public void setFiltro(BasicFiltroCidade filtro) {
		this.filtro = filtro;
	}

	public ConverterUtil<Pais> getConverterPais() {
		return converterPais;
	}

	public void setConverterPais(ConverterUtil<Pais> converterPais) {
		this.converterPais = converterPais;
	}

	public ConverterUtil<Estado> getConverterEstado() {
		return converterEstado;
	}

	public void setConverterEstado(ConverterUtil<Estado> converterEstado) {
		this.converterEstado = converterEstado;
	}

	public Cidade getSelectCidade() {
		return selectCidade;
	}

	public void setSelectCidade(Cidade selectCidade) {
		this.selectCidade = selectCidade;
	}

}
