package br.com.ilog.cadastro.presentation.mbean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.hibernate.validator.ValidatorHelper;
import br.cits.commons.citspresentation.converter.EntityConverter;
import br.cits.commons.citspresentation.mbeans.AbstractPaginacao;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.commons.citspresentation.util.JSFRequestBean;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.Cidade;
import br.com.ilog.cadastro.business.entity.Estado;
import br.com.ilog.cadastro.business.entity.Pais;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroCidade;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroEstado;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.cadastro.presentation.util.ExceptionFiltro;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;

/**
 * @author Celso Oscar Junior
 */

@Controller("mBeanPesquisarCidade")
@AccessScoped
public class MBeanPesquisarCidade extends AbstractPaginacao {

	private static final long serialVersionUID = -3695448525808338129L;
	private static Logger logger = LoggerFactory
			.getLogger(MBeanPesquisarCidade.class);

	@Resource(name = "controllerCadastro")
	CadastroFacade facade;

	private BasicFiltroCidade filtro;

	private List<Cidade> cidades;
	private List<Pais> comboPais;
	private List<Estado> comboEstado;

	private EntityConverter<Pais> converterPaises;
	private EntityConverter<Estado> converterEstados;

	@SuppressWarnings("unused")
	@PostConstruct
	private void inicializador() {
		filtro = new BasicFiltroCidade();
		cidades = Collections.emptyList();
		comboPais = new ArrayList<Pais>();
		comboEstado = new ArrayList<Estado>();

		try {
			popularComboPais();
			popularComboEstado();
			doPesquisar( null );
		} catch (Exception e) {
			FacesContext fc = FacesContext.getCurrentInstance();
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG_001", fc.getViewRoot()
							.getLocale()));
		}
	}

	public void popularComboPais() {
		comboPais = new ArrayList<Pais>();

		try {

			comboPais = facade.listarPaises(null);

		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	public void popularComboEstado() {
		comboEstado = new ArrayList<Estado>();
		try {
			List<Estado> estados = facade.listarEstados(new BasicFiltroEstado(filtro.getPais()));
			comboEstado = estados;
			
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Metodo chamado pelo comboPais.
	 */
	public void popularCombosEstadoCidade() {
		popularComboEstado();
		//popularComboCidade();
	}
	@Override
	public void doPesquisar(ActionEvent arg0) {
		FacesContext fc = FacesContext.getCurrentInstance();
		ResourceBundle resourceBundle = TemplateMessageHelper
				.getResourceBundle(MensagensSistema.USUARIOS,
						JSFRequestBean.getLocale());
		List<String> errorMessages = ValidatorHelper.valida(filtro,
				resourceBundle, resourceBundle);

		if (errorMessages.isEmpty()) {
			try {
				cidades = facade.listarCidades(filtro);

				if (cidades.isEmpty()) {
					String message = TemplateMessageHelper.getMessage(
							MensagensSistema.SISTEMA, "MSG008", fc
									.getViewRoot().getLocale());
					Messages.adicionaMensagemDeInfo(message);
				}
				setPaginaAtual(1);
			} catch (BusinessException e1) {
				logger.error("error: {} " + e1);
				e1.printStackTrace();
				Messages.adicionaMensagemDeErro(TemplateMessageHelper
						.getMessage(MensagensSistema.SISTEMA, e1));
			}
		} else {
			Messages.adicionaMensagensDeErro(errorMessages);
		}
	}

	// Long que captura o id
	private Long idObjeto;

	/**
	 * @coment captura o id do item selecionados
	 * @param index
	 */

	public void capturarId(int index) {
		Cidade objeto = cidades.get(index);
		idObjeto = objeto.getId();
	}

	/**
	 * Excluir cidade.
	 * 
	 * @param id
	 */
	public void excluir() {

		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			facade.excluirCidade(new Cidade(idObjeto));
			Messages.adicionaMensagemDeInfo(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG_EXCLUIR_SUCESSO", fc
							.getViewRoot().getLocale()));

			this.refazerPesquisa();
		} catch (Exception e) {
			logger.error("error: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper
					.getMessage(ExceptionFiltro.recursiveException(e)));
			return;
		}

	}

	@Override
	public int getTotalRegistros() {
		if (cidades != null)
			return cidades.size();
		else
			return 0;
	}

	@Override
	public void limpar() {
		setPaginaAtual(1);
		filtro = new BasicFiltroCidade();
		cidades.clear();
	}

	@Override
	public void refazerPesquisa() {
		if (filtro == null)
			filtro = new BasicFiltroCidade();
		if (cidades.isEmpty())
			return;

		doPesquisar(null);
	}

	public BasicFiltroCidade getFiltro() {
		return filtro;
	}

	public void setFiltro(BasicFiltroCidade filtro) {
		this.filtro = filtro;
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

	public EntityConverter<Pais> getConverterPaises() {
		return converterPaises;
	}

	public void setConverterPaises(EntityConverter<Pais> converterPaises) {
		this.converterPaises = converterPaises;
	}

	public EntityConverter<Estado> getConverterEstados() {
		return converterEstados;
	}

	public void setConverterEstados(EntityConverter<Estado> converterEstados) {
		this.converterEstados = converterEstados;
	}

	public List<Cidade> getCidades() {
		return cidades;
	}

	public Long getIdObjeto() {
		return idObjeto;
	}

	public void setIdObjeto(Long idObjeto) {
		this.idObjeto = idObjeto;
	}

}
