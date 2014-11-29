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

import org.hibernate.exception.ConstraintViolationException;
import org.primefaces.model.DualListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.hibernate.validator.ValidatorHelper;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.commons.citspresentation.util.JSFRequestBean;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.Cidade;
import br.com.ilog.cadastro.business.entity.CondicaoPagamento;
import br.com.ilog.cadastro.business.entity.Contato;
import br.com.ilog.cadastro.business.entity.Continente;
import br.com.ilog.cadastro.business.entity.Endereco;
import br.com.ilog.cadastro.business.entity.Estado;
import br.com.ilog.cadastro.business.entity.IdiomaContato;
import br.com.ilog.cadastro.business.entity.Pais;
import br.com.ilog.cadastro.business.entity.PessoaJuridica;
import br.com.ilog.cadastro.business.entity.TipoPessoa;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroCidade;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroEstado;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.cadastro.presentation.util.ConverterUtil;
import br.com.ilog.cadastro.presentation.util.ExceptionFiltro;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;
import br.com.ilog.geral.presentation.mbean.AbstractManterPaginacao;

/**
 * 
 * @author Wisley Souza
 * @since 20/06/2011
 * @comentario Bean de manuteir pessoas juridicas
 * 
 */

@Controller("mBeanManterPessoaJuridica")
@AccessScoped
public class MBeanManterPessoaJuridica extends AbstractManterPaginacao {

	private static final long serialVersionUID = -3279707891776972581L;
	private static Logger logger = LoggerFactory
			.getLogger(MBeanManterPessoaJuridica.class);

	private Integer paginaAtual;

	private PessoaJuridica pessoa;
	private Boolean novaPessoa;

	private List<SelectItem> comboContinentes;
	private List<SelectItem> comboIdiomas;
	private List<SelectItem> comboCondicaoPagamento;

	private Cidade cidade;
	private List<Cidade> comboCidades;
	// private EntityConverter<Cidade> converterCidade;
	private ConverterUtil<Cidade> converterCidade;

	private Estado estado;
	private List<Estado> comboEstados;
	// private EntityConverter<Estado> converterEstado;
	private ConverterUtil<Estado> converterEstado;

	private Pais pais;
	private boolean paisSelecionado;
	private boolean faturado;

	private List<Pais> comboPaises;
	// private EntityConverter<Pais> converterPais;
	private ConverterUtil<Pais> converterPais;

	private Endereco endereco;
	private Contato contatoPrincipal;
	private Contato contatoOutro;
	private Boolean editarItem;
	private List<Contato> listContatos;

	// private EntityConverter<TipoPessoa> converterTipoPessoa;
	private ConverterUtil<TipoPessoa> converterTipoPessoa;
	private List<TipoPessoa> comboTiposPessoas;
	private List<TipoPessoa> tiposPessoa;

	private DualListModel<TipoPessoa> sourcePick;

	private List<TipoPessoa> source;
	private List<TipoPessoa> target;

	private PessoaJuridica selectPerson;

	@Resource(name = "controllerCadastro")
	CadastroFacade facade;

	@PostConstruct
	void inicializar() {
		contatoPrincipal = new Contato();
		inicializarObjetos();

		novaPessoa = false;
		listContatos = Collections.emptyList();

		setPaginaAtual(1);
		popularCombos();
		popularCidadeEstado();
		//
	}

	public String salvarNovo() {
		try {
			if (this.salvar() != null) {
				return this.novaPessoa();

			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * popular combo tipo de pessoas
	 */
	public void popularComboTypePersons() {
		comboTiposPessoas = new ArrayList<TipoPessoa>();
		source = new ArrayList<TipoPessoa>();
		target = new ArrayList<TipoPessoa>();
		try {
			List<TipoPessoa> typePersonAux = facade.listAllTypePersons();
			if (typePersonAux != null) {
				source = typePersonAux;
				sourcePick = new DualListModel<TipoPessoa>(source, target);

				converterTipoPessoa = new ConverterUtil<TipoPessoa>(
						typePersonAux);
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	public void popularCombos() {
		FacesContext fc = FacesContext.getCurrentInstance();
		// preenche continentes

		// preenche condicoes de pagamento
		comboCondicaoPagamento = new ArrayList<SelectItem>();
		comboCondicaoPagamento.add(new SelectItem(CondicaoPagamento.P_A,
				TemplateMessageHelper.getMessage(
						MensagensSistema.CONDICAO_PAGAMENTO,
						CondicaoPagamento.P_A.name(), fc.getViewRoot()
								.getLocale())));
		comboCondicaoPagamento.add(new SelectItem(CondicaoPagamento.FAT,
				TemplateMessageHelper.getMessage(
						MensagensSistema.CONDICAO_PAGAMENTO,
						CondicaoPagamento.FAT.name(), fc.getViewRoot()
								.getLocale())));

		// preenche idiomas
		comboIdiomas = new ArrayList<SelectItem>();
		for (IdiomaContato idioma : IdiomaContato.values()) {
			comboIdiomas.add(new SelectItem(idioma, TemplateMessageHelper
					.getMessage(MensagensSistema.PESSOA_JURIDICA,
							idioma.name(), fc.getViewRoot().getLocale())));
		}

		// metodo que popula a como de paises na pagina de pesquisa...:)
		comboPaises = new ArrayList<Pais>();
		try {
			List<Pais> paisesAux = facade.listarPaises();
			if (paisesAux != null) {
				comboPaises = paisesAux;
				converterPais = new ConverterUtil<Pais>(paisesAux);
			}

		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param event
	 */
	public void popularCidadeEstado() {
		popularEstado();
		popularCidade();
	}

	/**
	 * @param event
	 */
	public void popularEstado() {
		comboEstados = new ArrayList<Estado>();
		paisSelecionado = false;
		try {

			paisSelecionado = true;
			List<Estado> states = facade.listarEstados(new BasicFiltroEstado(
					pais));
			comboEstados = states;
			converterEstado = new ConverterUtil<Estado>(states);

		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param event
	 */
	public void popularCidade() {
		comboCidades = new ArrayList<Cidade>();
		paisSelecionado = false;
		try {

			paisSelecionado = true;
			BasicFiltroCidade f = new BasicFiltroCidade();
			f.setEstado(estado);
			f.setPais(pais);
			List<Cidade> cities = facade.listarCidades(f);
			// cidade = new Cidade();
			comboCidades = cities;

			converterCidade = new ConverterUtil<Cidade>(cities);

		} catch (BusinessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void verificaCondicaoPagamento(ActionEvent event) {
		if (pessoa.getCondicaoPagamento() != null
				&& pessoa.getCondicaoPagamento().equals(CondicaoPagamento.FAT)) {
			faturado = true;
		} else {
			faturado = false;
		}
	}

	public String novaPessoa() {

		inicializarObjetos();

		// sourcePick = new DualListModel<TipoPessoa>();
		edicao = false;
		novaPessoa = true;
		pessoa = new PessoaJuridica();
		listContatos = new ArrayList<Contato>();
		popularComboTypePersons();

		return "pessoajuridica.jsf";
	}

	private Cidade cityAux;

	@Override
	public String editar() {

		try {

			source = new ArrayList<TipoPessoa>();
			target = new ArrayList<TipoPessoa>();

			pessoa = (PessoaJuridica) facade
					.getPessoaById(selectPerson.getId());

			source = facade.listAllTypePersons();
			converterTipoPessoa = new ConverterUtil<TipoPessoa>(source);
			if (pessoa.getTiposPessoa() != null)
				target = pessoa.getTiposPessoa();

			source.removeAll(target);
			sourcePick = new DualListModel<TipoPessoa>(source, target);

			if (pessoa.getEnderecos() != null
					&& !pessoa.getEnderecos().isEmpty()) {
				endereco = pessoa.getEnderecos().get(0);
				endereco = (Endereco) facade.getEnderecoByid(endereco.getId());

				pais = facade.getPaisById(endereco.getPais().getId());
				popularCidadeEstado();

				cidade = facade.getCidadeById(endereco.getCidade().getId());

				if (cidade.getEstado() != null) {
					estado = cidade.getEstado();// facade.getEstadoById(cidade.getEstado().getId());
					popularCidade();
				}
				cityAux = new Cidade();
				cityAux = cidade;
			

			}

			listContatos = new ArrayList<Contato>();
			if (pessoa.getContatos() != null && !pessoa.getContatos().isEmpty()) {

				listContatos.addAll(pessoa.getContatos());
				contatoPrincipal = pessoa.getContatos().get(0);
				try {
					listContatos.remove(0);
				} catch (Exception e) {
					contatoOutro = new Contato();
					listContatos = new ArrayList<Contato>();
				}
			}

			// verificaCondicaoPagamento(null);
			// comboEstados = 0;
		} catch (BusinessException e) {
			logger.error("erro: {}", e);
			e.printStackTrace();
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.PESSOA_JURIDICA, e));
			return null;
		}
		edicao = true;
		return "pessoajuridica.jsf";
	}

	@Override
	public String salvar() {
		FacesContext fc = FacesContext.getCurrentInstance();
		ResourceBundle resource = TemplateMessageHelper.getResourceBundle(
				MensagensSistema.PESSOA_JURIDICA, fc.getViewRoot().getLocale());
		pessoa.setTiposPessoa(sourcePick.getTarget());
		List<String> erros = ValidatorHelper.valida(pessoa, resource);

		erros.addAll(verificarEndereco());
		if (erros.isEmpty()) {

			try {

				verificarContato();

				if (edicao)
					facade.alterarPessoaJuridica(pessoa);
				else
					facade.cadastrarPessoaJuridica(pessoa);

			} catch (Exception e) {
				if (!edicao) {
					pessoa.setId(null);
					pessoa.setEnderecos(new ArrayList<Endereco>());
					endereco.setId(null);
					contatoPrincipal.setId(null);
					pessoa.setContatos(new ArrayList<Contato>());
				}

				ConstraintViolationException exc = (ConstraintViolationException) e
						.getCause();

				// Throwable lastCause = ExceptionFiltro.getLastException(e);
				if (e.getMessage().contains("ConstraintViolationException")) {
					if (exc.getSQLException().getNextException().getMessage()
							.contains("uk_pj_razao_social")) {
						Messages.adicionaMensagemDeErro(TemplateMessageHelper
								.getMessage(MensagensSistema.PESSOA_JURIDICA,
										"msgUniquePJ", fc.getViewRoot()
												.getLocale()));
					}

					return null;
				} else {
					e.printStackTrace();
					Messages.adicionaMensagemDeErro(TemplateMessageHelper
							.getMessage(ExceptionFiltro.recursiveException(e)));
					return null;
				}
			}

		} else {
			Messages.adicionaMensagensDeErro(erros);
			return null;
		}

		String message = TemplateMessageHelper.getMessage(
				MensagensSistema.SISTEMA, "MSG001", fc.getViewRoot()
						.getLocale());
		Messages.adicionaMensagemDeInfo(message);
		return "pessoasJuridicas.jsf";
	}

	private List<String> verificarEndereco() {
		List<String> erros = new ArrayList<String>();
		FacesContext fc = FacesContext.getCurrentInstance();
		endereco.setPessoa(pessoa);
		endereco.setPais(pais);
		if (cidade != null)
			endereco.setCidade(cidade);
		else
			endereco.setCidade(cityAux);

		if (endereco.getRua() == null || endereco.getRua().equals("")) {
			erros.add(TemplateMessageHelper.getMessage(
					MensagensSistema.PESSOA_JURIDICA, "ruaObrigatorio", fc
							.getViewRoot().getLocale()));
		}
		if (endereco.getNumero() == null || endereco.getNumero().equals("")) {
			erros.add(TemplateMessageHelper.getMessage(
					MensagensSistema.PESSOA_JURIDICA, "numeroObrigatorio", fc
							.getViewRoot().getLocale()));
		}
		/*
		 * TODO remocao de obrigatoriedade de endereco if (endereco.getBairro()
		 * == null || endereco.getBairro().equals("")) {
		 * erros.add(TemplateMessageHelper.getMessage(
		 * MensagensSistema.PESSOA_JURIDICA, "bairroObrigatorio", fc
		 * .getViewRoot().getLocale())); }
		 */
		if (endereco.getCep() == null || endereco.getCep().equals("")) {
			erros.add(TemplateMessageHelper.getMessage(
					MensagensSistema.PESSOA_JURIDICA, "cepObrigatorio", fc
							.getViewRoot().getLocale()));
		}
		if (endereco.getContinente() == null) {
			erros.add(TemplateMessageHelper.getMessage(
					MensagensSistema.PESSOA_JURIDICA, "continenteObrigatorio",
					fc.getViewRoot().getLocale()));
		}
		if (endereco.getPais() == null) {
			erros.add(TemplateMessageHelper.getMessage(
					MensagensSistema.PESSOA_JURIDICA, "paisObrigatorio", fc
							.getViewRoot().getLocale()));
		}
		if (endereco.getCidade() == null) {
			erros.add(TemplateMessageHelper.getMessage(
					MensagensSistema.PESSOA_JURIDICA, "cidadeObrigatorio", fc
							.getViewRoot().getLocale()));
		}

		if (erros.isEmpty()) {
			if (pessoa.getEnderecos() != null)
				pessoa.getEnderecos().clear();
			else
				pessoa.setEnderecos(new ArrayList<Endereco>());
			pessoa.getEnderecos().add(endereco);
		}

		return erros;
	}

	private void verificarContato() {
		if (pessoa.getContatos() != null) {
			pessoa.getContatos().clear();
		} else
			pessoa.setContatos(new ArrayList<Contato>());

		contatoPrincipal.setPessoa(pessoa);

		if ((contatoPrincipal.getNomeResponsavel() != null && !contatoPrincipal
				.getNomeResponsavel().equals(""))
				|| (contatoPrincipal.getTelefone() != null && !contatoPrincipal
						.getTelefone().equals(""))
				|| (contatoPrincipal.getTelefoneOpcional() != null && !contatoPrincipal
						.getTelefoneOpcional().equals(""))
				|| (contatoPrincipal.getFax() != null && !contatoPrincipal
						.getFax().equals(""))
				|| (contatoPrincipal.getEmail() != null && !contatoPrincipal
						.getEmail().equals(""))
				|| (contatoPrincipal.getSite() != null && !contatoPrincipal
						.getSite().equals(""))
				|| (contatoPrincipal.getNomeResponsavel() != null && !contatoPrincipal
						.getNomeResponsavel().equals(""))
				|| (contatoPrincipal.getIdioma() != null)) {

			pessoa.getContatos().addAll(listContatos);
			pessoa.getContatos().add(0, contatoPrincipal);
		}
	}

	/**
	 * @param evnt
	 */
	public void addContato(ActionEvent evnt) {
		FacesContext fc = FacesContext.getCurrentInstance();
		contatoOutro.setPessoa(pessoa);

		ResourceBundle resource = TemplateMessageHelper.getResourceBundle(
				MensagensSistema.PESSOA_JURIDICA, fc.getViewRoot().getLocale());
		List<String> erros = ValidatorHelper.valida(contatoOutro, resource);

		if (erros.isEmpty()) {

			listContatos.add(contatoOutro);

			/**
			 * UIModalPanel modal = (UIModalPanel)
			 * RichFunction.findComponent("modalContato"); if (modal != null) {
			 * modal.setShowWhenRendered(false);
			 * AjaxContext.getCurrentInstance()
			 * .addComponentToAjaxRender(modal); }
			 * 
			 * } else { Messages.adicionaMensagensDeErro(erros); UIModalPanel
			 * modal = (UIModalPanel) RichFunction
			 * .findComponent("modalContato"); if (modal != null) {
			 * modal.setShowWhenRendered(true); AjaxContext.getCurrentInstance()
			 * .addComponentToAjaxRender(modal); } }
			 **/
		}
	}

	/**
	 * editar um registro da lista de contatos.
	 * 
	 * @param index
	 */
	public void editarItem(int index) {
		editarItem = true;
		contatoOutro = listContatos.get(index);
	}

	/**
	 * @param evn
	 */
	public void limpar(ActionEvent evn) {
		editarItem = false;
		contatoOutro = new Contato();
	}

	/**
	 * @param index
	 *            indice do registro na lista.
	 */
	public void excluirItem(int index) {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			listContatos.remove(index);
			Messages.adicionaMensagemDeInfo(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG_EXCLUIR_SUCESSO", fc
							.getViewRoot().getLocale()));

		} catch (Exception e) {
			FacesContext fc = FacesContext.getCurrentInstance();
			e.printStackTrace();
			Messages.adicionaMensagemDeInfo(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG_GEN_002", fc.getViewRoot()
							.getLocale()));
		}
	}

	@Override
	public String excluir() {
		try {

			facade.excluirPessoaJuridica(pessoa);

			FacesContext fc = FacesContext.getCurrentInstance();
			Messages.adicionaMensagemDeInfo(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG_EXCLUIR_SUCESSO", fc
							.getViewRoot().getLocale()));
			return "pessoasJuridicas.jsf";

		} catch (Exception e) {
			logger.error("error: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper
					.getMessage(ExceptionFiltro.recursiveException(e)));
			return null;
		}
	}

	@Override
	public void inicializarObjetos() {
		pessoa = new PessoaJuridica();
		pessoa.setAtivo(true);
		pessoa.setEnderecos(new ArrayList<Endereco>());
		pessoa.setTiposPessoa(new ArrayList<TipoPessoa>());

		endereco = new Endereco();
		contatoPrincipal = new Contato();
		contatoOutro = new Contato();

		pais = new Pais();
		estado = new Estado();
		cidade = new Cidade();
		contatoPrincipal = new Contato();

	}

	public int getTotalRegistros() {
		if (listContatos != null) {
			return listContatos.size();
		}
		return 0;
	}

	@Override
	protected void refazerPesquisa() {
		MBeanPesquisarPessoaJuridica mBean = (MBeanPesquisarPessoaJuridica) JSFRequestBean
				.getManagedBean("mBeanPesquisarPessoaJuridica");
		mBean.refazerPesquisa();

	}

	// metodo cancelar
	public String cancelar() {
		return "pessoasJuridicas.jsf";
	}

	public PessoaJuridica getPessoa() {
		return pessoa;
	}

	public void setPessoa(PessoaJuridica pessoa) {
		this.pessoa = pessoa;
	}

	public Boolean getNovaPessoa() {
		return novaPessoa;
	}

	public void setNovaPessoa(Boolean novaPessoa) {
		this.novaPessoa = novaPessoa;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		if (pais != null && pais.getId() != null) {
			paisSelecionado = true;
		} else {
			paisSelecionado = false;
		}
		this.pais = pais;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public Contato getContatoPrincipal() {
		return contatoPrincipal;
	}

	public void setContatoPrincipal(Contato contato) {
		this.contatoPrincipal = contato;
	}

	public List<Contato> getListContatos() {
		return listContatos;
	}

	public void setListContatos(List<Contato> listContatos) {
		this.listContatos = listContatos;
	}

	public Integer getPaginaAtual() {
		return paginaAtual;
	}

	public void setPaginaAtual(Integer paginaAtual) {
		this.paginaAtual = paginaAtual;
	}

	public List<TipoPessoa> getComboTiposPessoas() {
		popularComboTypePersons();
		return comboTiposPessoas;
	}

	public void setComboTiposPessoas(List<TipoPessoa> comboTiposPessoas) {
		this.comboTiposPessoas = comboTiposPessoas;
	}

	public List<Cidade> getComboCidades() {
		return comboCidades;
	}

	public void setComboCidades(List<Cidade> comboCidades) {
		this.comboCidades = comboCidades;
	}

	public List<Estado> getComboEstados() {
		return comboEstados;
	}

	public void setComboEstados(List<Estado> comboEstados) {
		this.comboEstados = comboEstados;
	}

	public List<Pais> getComboPaises() {
		return comboPaises;
	}

	public void setComboPaises(List<Pais> comboPaises) {
		this.comboPaises = comboPaises;
	}

	public boolean isPaisSelecionado() {
		return paisSelecionado;
	}

	public void setPaisSelecionado(boolean paisSelecionado) {
		this.paisSelecionado = paisSelecionado;
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

	public List<SelectItem> getComboCondicaoPagamento() {

		FacesContext fc = FacesContext.getCurrentInstance();
		comboCondicaoPagamento = new ArrayList<SelectItem>();

		comboCondicaoPagamento.add(new SelectItem(CondicaoPagamento.P_A,
				TemplateMessageHelper.getMessage(
						MensagensSistema.CONDICAO_PAGAMENTO,
						CondicaoPagamento.P_A.name(), fc.getViewRoot()
								.getLocale())));
		comboCondicaoPagamento.add(new SelectItem(CondicaoPagamento.FAT,
				TemplateMessageHelper.getMessage(
						MensagensSistema.CONDICAO_PAGAMENTO,
						CondicaoPagamento.FAT.name(), fc.getViewRoot()
								.getLocale())));

		return comboCondicaoPagamento;
	}

	public void setComboCondicaoPagamento(
			List<SelectItem> comboCondicaoPagamento) {
		this.comboCondicaoPagamento = comboCondicaoPagamento;
	}

	public boolean isFaturado() {
		return faturado;
	}

	public void setFaturado(boolean faturado) {
		this.faturado = faturado;
	}

	public List<SelectItem> getComboIdiomas() {
		comboIdiomas = new ArrayList<SelectItem>();
		FacesContext fc = FacesContext.getCurrentInstance();

		for (IdiomaContato idioma : IdiomaContato.values()) {
			comboIdiomas.add(new SelectItem(idioma, TemplateMessageHelper
					.getMessage(MensagensSistema.PESSOA_JURIDICA,
							idioma.name(), fc.getViewRoot().getLocale())));
		}
		return comboIdiomas;
	}

	public void setComboIdiomas(List<SelectItem> comboIdiomas) {
		this.comboIdiomas = comboIdiomas;
	}

	public List<TipoPessoa> getTiposPessoa() {
		return tiposPessoa;
	}

	public void setTiposPessoa(List<TipoPessoa> tiposPessoa) {
		this.tiposPessoa = tiposPessoa;
	}

	public Contato getContatoOutro() {
		return contatoOutro;
	}

	public void setContatoOutro(Contato contatoOutro) {
		this.contatoOutro = contatoOutro;
	}

	public Boolean getEditarItem() {
		return editarItem;
	}

	public void setEditarItem(Boolean editarItem) {
		this.editarItem = editarItem;
	}

	public ConverterUtil<Cidade> getConverterCidade() {
		return converterCidade;
	}

	public void setConverterCidade(ConverterUtil<Cidade> converterCidade) {
		this.converterCidade = converterCidade;
	}

	public ConverterUtil<Estado> getConverterEstado() {
		return converterEstado;
	}

	public void setConverterEstado(ConverterUtil<Estado> converterEstado) {
		this.converterEstado = converterEstado;
	}

	public ConverterUtil<Pais> getConverterPais() {
		return converterPais;
	}

	public void setConverterPais(ConverterUtil<Pais> converterPais) {
		this.converterPais = converterPais;
	}

	public ConverterUtil<TipoPessoa> getConverterTipoPessoa() {
		return converterTipoPessoa;
	}

	public void setConverterTipoPessoa(
			ConverterUtil<TipoPessoa> converterTipoPessoa) {
		this.converterTipoPessoa = converterTipoPessoa;
	}

	public PessoaJuridica getSelectPerson() {
		return selectPerson;
	}

	public void setSelectPerson(PessoaJuridica selectPerson) {
		this.selectPerson = selectPerson;
	}

	public DualListModel<TipoPessoa> getSourcePick() {
		return sourcePick;
	}

	public void setSourcePick(DualListModel<TipoPessoa> sourcePick) {
		this.sourcePick = sourcePick;
	}

	public List<TipoPessoa> getSource() {
		return source;
	}

	public void setSource(List<TipoPessoa> source) {
		this.source = source;
	}

	public List<TipoPessoa> getTarget() {
		return target;
	}

	public void setTarget(List<TipoPessoa> target) {
		this.target = target;
	}

	public Cidade getCityAux() {
		return cityAux;
	}

	public void setCityAux(Cidade cityAux) {
		this.cityAux = cityAux;
	}
}
