package br.com.ilog.cadastro.presentation.mbean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.hibernate.validator.ValidatorHelper;
import br.cits.commons.citspresentation.converter.EntityConverter;
import br.cits.commons.citspresentation.mbeans.AbstractPaginacao;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.Pais;
import br.com.ilog.cadastro.business.entity.PessoaJuridica;
import br.com.ilog.cadastro.business.entity.TipoPessoa;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroPessoaJuridica;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.cadastro.presentation.util.ExceptionFiltro;
import br.com.ilog.geral.presentation.CommonsList;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;

/**
 * 
 * 
 * 
 * @author Wisley Souza
 * @since 20/06/2011
 * 
 * 
 * 
 */

@Controller("mBeanPesquisarPessoaJuridica")
@AccessScoped
public class MBeanPesquisarPessoaJuridica extends AbstractPaginacao {

	private static final long serialVersionUID = 2440846753614185739L;

	private static Logger logger = LoggerFactory
			.getLogger(MBeanPesquisarPessoaJuridica.class);

	@Resource(name = "controllerCadastro")
	CadastroFacade personJuridicaFacade;

	// lista de objetos pessoas com seu objeto de filtro
	private List<PessoaJuridica> personsJuridicas;

	private BasicFiltroPessoaJuridica filtro;
	private List<Pais> paises;

	private List<SelectItem> comboStatus;

	private List<SelectItem> comboPaises;
	private EntityConverter<Pais> converterPais;

	// selectItem de Tipo de pessoa, objeto que fará com que se escolha o tipo
	private List<TipoPessoa> comboTiposPessoas;
	private EntityConverter<TipoPessoa> converterTipoPessoa;

	// booleanos que desabilitam as combos
	private Boolean disableEstado;
	private Boolean disableCidade;

	@Resource(name = "commonsList")
	CommonsList commonsList;

	// método inicializador dos objetos
	@PostConstruct
	public void inicializador() {

		filtro = new BasicFiltroPessoaJuridica();
		paises = Collections.emptyList();
		personsJuridicas = Collections.emptyList();

		comboPaises = new ArrayList<SelectItem>();
		comboTiposPessoas = new ArrayList<TipoPessoa>();
		doPesquisar(null);

		disableEstado = true;
		disableCidade = true;

		try {
			popularComboPais();
			// popularComboTypePersons();
		} catch (Exception e) {
			FacesContext fc = FacesContext.getCurrentInstance();
			e.printStackTrace();
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG_001", fc.getViewRoot()
							.getLocale()));
		}
	}

	public void popularComboPais() {
		// metodo que popula a como de paises na pagina de pesquisa...:)

		comboPaises = new ArrayList<SelectItem>();
		try {
			List<Pais> paisesAux = personJuridicaFacade.listarPaises();
			if (paisesAux != null) {
				for (Pais pais : paisesAux) {
					comboPaises.add(new SelectItem(pais, pais.getNome()));
				}
				converterPais = new EntityConverter<Pais>(paisesAux);
			}

			// disableCidade = false;

		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	// public void popularComboEstados(ActionEvent arg) {
	// // metodo que popula a combo de estados na pagina de pesquisa...:)
	// comboEstados = new ArrayList<SelectItem>();
	// comboCidades = new ArrayList<SelectItem>();
	// try {
	// List<Estado> estadoAux =
	// personJuridicaFacade.listarEstados(filtroEstado);
	// if (estadoAux != null) {
	// for (Estado estado : estadoAux) {
	// comboEstados.add(new SelectItem(estado, estado.getNome()));
	// }
	// converterEstado = new EntityConverter<Estado>(estadoAux);
	// }
	// disableEstado = false;
	// disableCidade = true;
	// } catch (BusinessException e) {
	// e.printStackTrace();
	// }
	// }
	// public void popularComboCidades(ActionEvent arg) {
	// // metodo que popula a combo de cidades na pagina de pesquisa...:)
	// comboCidades = new ArrayList<SelectItem>();
	// try {
	// List<Cidade> cidadeAux =
	// personJuridicaFacade.listarCidades(filtroCidade);;
	// if (cidadeAux != null) {
	// for (Cidade cidade : cidadeAux) {
	// comboCidades.add(new SelectItem(cidade, cidade.getNome()));
	// }
	// converterCidade = new EntityConverter<Cidade>(cidadeAux);
	// disableCidade = false;
	// }
	// } catch (BusinessException e) {
	// e.printStackTrace();
	// }
	//
	// }

	/**
	 * popular combo tipo de pessoas
	 */
	public void popularComboTypePersons() {
		comboTiposPessoas = new ArrayList<TipoPessoa>();
		try {
			List<TipoPessoa> typePersonAux = personJuridicaFacade
					.listAllTypePersons();
			if (typePersonAux != null) {
				comboTiposPessoas = typePersonAux;
				converterTipoPessoa = new EntityConverter<TipoPessoa>(
						typePersonAux);
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doPesquisar(ActionEvent arg0) {
		FacesContext fc = FacesContext.getCurrentInstance();
		ResourceBundle resourceBundle = TemplateMessageHelper
				.getResourceBundle(MensagensSistema.PESSOA_JURIDICA, fc
						.getViewRoot().getLocale());
		List<String> errorMessages = ValidatorHelper.valida(filtro,
				resourceBundle, resourceBundle);
		if (errorMessages.isEmpty()) {
			try {
				personsJuridicas = personJuridicaFacade
						.listarAllPersons(filtro);
				if (personsJuridicas.isEmpty()) {
					String message = TemplateMessageHelper.getMessage(
							MensagensSistema.SISTEMA, "MSG008", fc
									.getViewRoot().getLocale());
					Messages.adicionaMensagemDeInfo(message);
				}
			} catch (BusinessException e) {
				e.printStackTrace();
				logger.error("erro: {} " + e);
				Messages.adicionaMensagemDeErro(TemplateMessageHelper
						.getMessage(MensagensSistema.PESSOA_JURIDICA, e));
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
		PessoaJuridica objeto = personsJuridicas.get(index);
		idObjeto = objeto.getId();
	}

	/**
	 * Excluir PJ.
	 * 
	 * @param id
	 */
	public void excluir() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			personJuridicaFacade.excluirPessoaJuridica(new PessoaJuridica(
					idObjeto));
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
		if (personsJuridicas != null)
			return personsJuridicas.size();
		else
			return 0;
	}

	@Override
	public void limpar() {
		filtro = new BasicFiltroPessoaJuridica();
		setPaginaAtual(1);
		personsJuridicas.clear();
	}

	@Override
	public void refazerPesquisa() {
		if (filtro == null) {
			filtro = new BasicFiltroPessoaJuridica();
		}
		if (personsJuridicas == null) {
			return;
		}
		doPesquisar(null);
	}

	public List<PessoaJuridica> getPersonsJuridicas() {
		return personsJuridicas;
	}

	public void setPersonsJuridicas(List<PessoaJuridica> personsJuridicas) {
		this.personsJuridicas = personsJuridicas;
	}

	public BasicFiltroPessoaJuridica getFiltro() {
		return filtro;
	}

	public void setFiltro(BasicFiltroPessoaJuridica filtro) {
		this.filtro = filtro;
	}

	public List<Pais> getPaises() {
		return paises;
	}

	public void setPaises(List<Pais> paises) {
		this.paises = paises;
	}

	public List<SelectItem> getComboPaises() {
		return comboPaises;
	}

	public void setComboPaises(List<SelectItem> comboPaises) {
		this.comboPaises = comboPaises;
	}

	public EntityConverter<Pais> getConverterPais() {
		return converterPais;
	}

	public void setConverterPais(EntityConverter<Pais> converterPais) {
		this.converterPais = converterPais;
	}

	public Boolean getDisableEstado() {
		return disableEstado;
	}

	public void setDisableEstado(Boolean disableEstado) {
		this.disableEstado = disableEstado;
	}

	public Boolean getDisableCidade() {
		return disableCidade;
	}

	public void setDisableCidade(Boolean disableCidade) {
		this.disableCidade = disableCidade;
	}

	public List<TipoPessoa> getComboTiposPessoas() {
		popularComboTypePersons();
		return comboTiposPessoas;
	}

	public void setComboTiposPessoas(List<TipoPessoa> comboTiposPessoas) {
		this.comboTiposPessoas = comboTiposPessoas;
	}

	public EntityConverter<TipoPessoa> getConverterTipoPessoa() {
		return converterTipoPessoa;
	}

	public void setConverterTipoPessoa(
			EntityConverter<TipoPessoa> converterTipoPessoa) {
		this.converterTipoPessoa = converterTipoPessoa;
	}

	public List<SelectItem> getComboStatus() {
		comboStatus = commonsList.listaBooleanAtivoInativo();
		return comboStatus;
	}

	public void setComboStatus(List<SelectItem> comboStatus) {
		this.comboStatus = comboStatus;
	}

	public Long getIdObjeto() {
		return idObjeto;
	}

	public void setIdObjeto(Long idObjeto) {
		this.idObjeto = idObjeto;
	}
}
