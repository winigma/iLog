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
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.hibernate.validator.ValidatorHelper;
import br.cits.commons.citspresentation.converter.EntityConverter;
import br.cits.commons.citspresentation.mbeans.AbstractManter;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.commons.citspresentation.util.JSFRequestBean;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.Cidade;
import br.com.ilog.cadastro.business.entity.Modal;
import br.com.ilog.cadastro.business.entity.Pais;
import br.com.ilog.cadastro.business.entity.PessoaJuridica;
import br.com.ilog.cadastro.business.entity.Rota;
import br.com.ilog.cadastro.business.entity.Terminal;
import br.com.ilog.cadastro.business.entity.TipoPessoa;
import br.com.ilog.cadastro.business.entity.TipoPessoaEnum;
import br.com.ilog.cadastro.business.entity.Trecho;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroPessoaJuridica;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroTerminal;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroTrecho;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.cadastro.presentation.util.ExceptionFiltro;
import br.com.ilog.geral.presentation.CommonsList;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;

@AccessScoped
@Component("mBeanManterRota")
public class MBeanManterRota extends AbstractManter {

	/** */
	private static final long serialVersionUID = -5631303420837131653L;
	Logger logger = LoggerFactory.getLogger(MBeanManterRota.class);

	@Resource(name = "controllerCadastro")
	CadastroFacade facade;

	@Resource
	CommonsList commonsList;

	private Rota entity;

	private Trecho trecho;

	private BasicFiltroTrecho filtro;

	private Terminal terminalOrigem;
	private Terminal terminalDestino;

	private boolean edicao;
	private boolean edicaoTrecho;
	private boolean desabilitarDestino;
	private Integer index;

	/**
	 * Combos Rota
	 */
	private List<Pais> comboPaisOrigem;
	private List<Pais> comboPaisDestino;
	private List<Cidade> comboCidadeOrigem;
	private List<Cidade> comboCidadeDestino;

	/**
	 * Combos de trecho
	 */
	private List<Cidade> comboTrechoCidadeOrigem;
	private List<Cidade> comboTrechoCidadeDestino;

	private List<SelectItem> comboTrechoCidadeOrigemSI;
	private List<SelectItem> comboTrechoCidadeDestinoSI;
	private EntityConverter<Cidade> converterCidadeOrigem;
	private EntityConverter<Cidade> converterCidadeDestino;

	private List<Terminal> comboTerminalOrigem;
	private List<Terminal> comboTerminalDestino;

	private List<SelectItem> comboTerminalOrigemSI;
	private List<SelectItem> comboTerminalDestinoSI;

	private EntityConverter<Terminal> converterTerminalOrigem;
	private EntityConverter<Terminal> converterTerminalDestino;

	private List<PessoaJuridica> comboAgenteCargas;

	private List<Modal> comboModal;

	private List<SelectItem> comboStatus;

	private List<SelectItem> comboExpresso;

	@PostConstruct
	public void init() {
		inicializarObjetos();
	}

	/**
	 * Inicializar os objetos de tela.
	 */
	@Override
	public void inicializarObjetos() {
		entity = new Rota();
		entity.setTrechos(new ArrayList<Trecho>());

		edicao = false;

		comboExpresso = commonsList.listaSimNao();
		comboStatus = commonsList.listaBooleanAtivoInativo();

		popularComboAgente();
		popularComboPaises();
		popularComboCidadeDestino();
		popularComboCidadeOrigem();
		popularComboModal();

		inicializarObjetosTrechos();
	}

	/**
	 * Metodo de nova Rota.
	 * 
	 * @return
	 */
	public String novo() {

		inicializarObjetos();

		return "rota.jsf";
	}

	/**
	 * Inicializar componentes de trechos.
	 */
	private void inicializarObjetosTrechos() {

		filtro = new BasicFiltroTrecho();

		trecho = new Trecho();
		edicaoTrecho = false;

		popularComboTrechoCidadeDestino();
		popularComboTrechoCidadeOrigem();
		popularComboTerminalOrigem();
		popularComboTerminalDestino();

	}

	/**
	 * Verifica se deve aparecer o link para exclusao de trecho.
	 * 
	 * @param index
	 * @return
	 */
	public boolean deleteTrecho(Integer index) {
		if (index != null && (entity.getTrechos().size() == index + 1)) {
			this.index = index;
			return true;
		}
		return false;
	}

	/**
	 * Metodo para remocao do trecho de rota
	 * 
	 * @param event
	 */
	public void removerTrecho(ActionEvent event) {

		if (index != null) {
			entity.getTrechos().remove(Integer.parseInt(index.toString()));

			FacesContext fc = FacesContext.getCurrentInstance();
			Messages.adicionaMensagemDeInfo(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG_EXCLUIR_SUCESSO", fc
							.getViewRoot().getLocale()));
		}
	}

	/**
	 * Recupearar o trecho a ser editado.
	 */
	public void editarTrecho(int i) {

		index = i;

		trecho = entity.getTrechos().get(index);
		filtro = new BasicFiltroTrecho(trecho);

		popularComboTerminalOrigem();
		popularComboTerminalDestino();

		edicaoTrecho = true;
		desabilitarDestino = true;

		// apenaa desabilita o destino caso seja o unico ou o ultimo trecho da
		// rota.
		if (index.equals(entity.getTrechos().size() - 1)) {
			desabilitarDestino = false;

		}
	}

	/**
	 * Salva as alteracoes da edição.
	 */
	public void salvarTrecho() {

		trecho = entity.getTrechos().get(index);

		FacesContext fc = FacesContext.getCurrentInstance();
		ResourceBundle resource = TemplateMessageHelper.getResourceBundle(
				MensagensSistema.ROTA, fc.getViewRoot().getLocale());
		List<String> erros = ValidatorHelper.valida(
				carregarTrechoDoFiltro(null, filtro), TemplateMessageHelper
						.getResourceBundle(MensagensSistema.SISTEMA, fc
								.getViewRoot().getLocale()), resource);

		if (erros.isEmpty()) {
			trecho = carregarTrechoDoFiltro(trecho, filtro);
			RequestContext.getCurrentInstance().addCallbackParam("closeModal",
					true);
		} else {
			Messages.adicionaMensagensDeErro(erros);
			RequestContext.getCurrentInstance().addCallbackParam("closeModal",
					false);
		}
	}

	/**
	 * Adiciona na lista de trechos de rota.
	 * 
	 * @param event
	 */
	public void adicionarTrecho(ActionEvent event) {

		// se nao carregar o trecho, retorna a mensagem
		trecho = carregarTrechoDoFiltro(trecho, filtro);

		FacesContext fc = FacesContext.getCurrentInstance();
		ResourceBundle resource = TemplateMessageHelper.getResourceBundle(
				MensagensSistema.ROTA, fc.getViewRoot().getLocale());
		List<String> erros = ValidatorHelper
				.valida(trecho,
						TemplateMessageHelper.getResourceBundle(
								MensagensSistema.SISTEMA, fc.getViewRoot()
										.getLocale()), resource);

		if (erros.isEmpty()) {
			entity.getTrechos().add(trecho);
			RequestContext.getCurrentInstance().addCallbackParam("closeModal",
					true);

		} else {
			Messages.adicionaMensagensDeErro(erros);
			RequestContext.getCurrentInstance().addCallbackParam("closeModal",
					false);
		}
	}

	/**
	 * Adicionar um novo trecho
	 */
	public void novoTrecho() {

		edicaoTrecho = false;

		RequestContext.getCurrentInstance().addCallbackParam("edicaoTrecho",
				edicaoTrecho);

		if (!carregarNovoTrecho()) {
			RequestContext.getCurrentInstance().addCallbackParam("openModal",
					false);
		} else {
			// trecho = new Trecho();

			popularComboTerminalOrigem();
			popularComboTrechoCidadeDestino();
			popularComboTerminalDestino();

			RequestContext.getCurrentInstance().addCallbackParam("openModal",
					true);
		}
	}

	/**
	 * Converte de um filtro para o trecho, ou novo trecho
	 * 
	 * @param filtro
	 */
	private Trecho carregarTrechoDoFiltro(Trecho trecho,
			BasicFiltroTrecho filtro) {

		if (trecho == null) {
			trecho = new Trecho();
		}
		trecho.setRota(filtro.getRota());
		trecho.setId(filtro.getId());
		trecho.setPaisOrigem(filtro.getPaisOrigem());
		trecho.setCidadeOrigem(filtro.getCidadeOrigem());
		trecho.setTerminalOrigem(filtro.getTerminalOrigem());
		trecho.setPaisDestino(filtro.getPaisDestino());
		trecho.setCidadeDestino(filtro.getCidadeDestino());
		trecho.setTerminalDestino(filtro.getTerminalDestino());
		trecho.setTipoTransporte(filtro.getModal());
		trecho.setQuantidadeDias(filtro.getQuantidadeDias());

		return trecho;
	}

	/**
	 * Carrega a cidade origem para o proximo trecho.
	 * 
	 * @param event
	 */
	public boolean carregarNovoTrecho() {

		trecho = new Trecho();
		trecho.setRota(this.getEntity());

		terminalOrigem = new Terminal();
		terminalDestino = new Terminal();

		FacesContext fc = FacesContext.getCurrentInstance();

		// recupera a origem para o novo trecho, caso contrario retorna com a
		// mensagem
		if (entity.getTrechos() != null && !entity.getTrechos().isEmpty()) {
			trecho.setCidadeOrigem(entity.getTrechos()
					.get(entity.getTrechos().size() - 1).getCidadeDestino());
			trecho.setPaisOrigem(entity.getTrechos()
					.get(entity.getTrechos().size() - 1).getPaisDestino());

		} else if (entity.getCidadeOrigem() != null) {
			trecho.setCidadeOrigem(entity.getCidadeOrigem());
			trecho.setPaisOrigem(entity.getPaisOrigem());

		} else {
			String msg = TemplateMessageHelper.getMessage(
					MensagensSistema.ROTA, "MSG020", fc.getViewRoot()
							.getLocale());
			Messages.adicionaMensagemDeInfo(msg);

			return false;
		}

		filtro = new BasicFiltroTrecho(trecho);

		return true;
	}

	/**
	 * Cancelar a operacao de edicao de rota.
	 */
	public String cancelar() {
		return "rotas.jsf";
	}

	/**
	 * Persiste as alteracoes na base.
	 */
	public String salvar() {

		FacesContext fc = FacesContext.getCurrentInstance();
		ResourceBundle resource = TemplateMessageHelper.getResourceBundle(
				MensagensSistema.ROTA, fc.getViewRoot().getLocale());
		List<String> erros = ValidatorHelper
				.valida(entity,
						TemplateMessageHelper.getResourceBundle(
								MensagensSistema.SISTEMA, fc.getViewRoot()
										.getLocale()), resource);

		// Verificar se o ultimo trecho da rota é o destino.
		if (erros.isEmpty()) {

			try {
				if (entity.getTrechos() != null
						&& !entity.getTrechos().isEmpty()) {
					if (entity.getTrechos().get(entity.getTrechos().size() - 1)
							.getCidadeDestino()
							.equals(entity.getCidadeDestino())) {

						/*
						 * facade.validarRota(entity);
						 * 
						 * if (!rota.getTrechos().isEmpty()) { int diasTrecho =
						 * 0; for (Trecho tr : rota.getTrechos()) { diasTrecho
						 * += tr.getQuantidadeDias(); }
						 * rota.setQuantidadeDiasTrecho(diasTrecho);
						 * 
						 * if (!rota.getQuantidadeDias().equals(diasTrecho)) {
						 * UIModalPanel modal = (UIModalPanel) RichFunction
						 * .findComponent("modalConfirmaSLA"); if (modal !=
						 * null) { modal.setShowWhenRendered(true);
						 * AjaxContext.getCurrentInstance()
						 * .addComponentToAjaxRender(modal); } return null; } }
						 */

						if (edicao) {
							facade.alterarRota(entity);
						} else {
							facade.cadastrarRota(entity);
						}

						String message = TemplateMessageHelper.getMessage(
								MensagensSistema.SISTEMA, "MSG001", fc
										.getViewRoot().getLocale());
						Messages.adicionaMensagemDeInfo(message);
						refazerPesquisa();

						return "rotas.jsf";

						// } catch (BusinessException e) {
						//
						// e.printStackTrace();
						// if (!edicao) {
						// entity.setId(null);
						// entity.setTrechos(null);
						// }
						//
						// ConstraintViolationException exc =
						// (ConstraintViolationException) e.getCause();
						//
						// //Throwable lastCause =
						// ExceptionFiltro.getLastException(e);
						// if
						// (e.getMessage().contains("ConstraintViolationException"))
						// {
						// if
						// (exc.getSQLException().getNextException().getMessage()
						// .contains("_duplicado")) {
						// Messages.adicionaMensagemDeErro(TemplateMessageHelper
						// .getMessage(MensagensSistema.ROTA,
						// "msgCheckRota", fc.getViewRoot()
						// .getLocale()));
						// }
						// return null;
						// } else {
						// Messages.adicionaMensagemDeErro(TemplateMessageHelper
						// .getMessage(MensagensSistema.ROTA, ExceptionFiltro
						// .recursiveException(e)));
						// }
						//
						// e.printStackTrace();
						// return null;
					} else {
						Messages.adicionaMensagemDeErro(TemplateMessageHelper
								.getMessage(MensagensSistema.ROTA, "MSG017"));
						return null;
					}
				} else {
					Messages.adicionaMensagemDeErro(TemplateMessageHelper
							.getMessage(MensagensSistema.ROTA, "msgSemTrechos"));
					return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
				if (!edicao) {
					if (entity.getId() != null) {
						entity.setId(null);
						if (entity.getTrechos() != null) {
							for (Trecho t : entity.getTrechos())
								t.setId(null);
						}

					}
				}
				ConstraintViolationException exc = new ConstraintViolationException(
						null, null, null);
				if (e.getMessage().contains("ConstraintViolationException"))
					exc = (ConstraintViolationException) e.getCause();

				Throwable lastCause = ExceptionFiltro.getLastException(e);
				if (e.getMessage().contains("ConstraintViolationException")) {
					if (exc.getSQLException().getNextException().getMessage()
							.contains("uk_rota_code")) {

						Messages.adicionaMensagemDeErro(TemplateMessageHelper
								.getMessage(MensagensSistema.ROTA,
										"msgCheckRota", fc.getViewRoot()
												.getLocale()));
					}
					return null;
				} else {
					Messages.adicionaMensagemDeErro(TemplateMessageHelper
							.getMessage(MensagensSistema.ROTA,
									ExceptionFiltro.recursiveException(e)));
					return null;
				}
			}

		} else {
			Messages.adicionaMensagensDeErro(erros);
			return null;
		}

	}

	/**
	 * Metodo para edicao de rota
	 */
	@Override
	public String editar() {

		Long idRegistro = Long.valueOf(JSFRequestBean.getParameter("id"));

		try {

			entity = facade.getRotaById(idRegistro);

		} catch (BusinessException e) {
			logger.error("erro: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, e));
			return null;
		}

		edicao = true;

		return "rota.jsf";
	}

	/**
	 * Metodo para exclusao de uma rota.
	 */
	@Override
	public String excluir() {
		try {
			facade.excluirRota(entity);

			FacesContext fc = FacesContext.getCurrentInstance();
			Messages.adicionaMensagemDeInfo(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG_EXCLUIR_SUCESSO", fc
							.getViewRoot().getLocale()));

			return "rotas.jsf";

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("erro: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper
					.getMessage(ExceptionFiltro.recursiveException(e)));

			return null;
		}
	}

	/**
	 * Preenche combo de pais origem e destino
	 */
	public void popularComboPaises() {
		comboPaisDestino = Collections.emptyList();
		comboPaisOrigem = Collections.emptyList();
		try {
			List<Pais> paises = facade.listarPaises();

			comboPaisDestino = paises;
			comboPaisOrigem = paises;

		} catch (BusinessException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 */
	public void popularComboCidadeOrigem() {
		comboCidadeOrigem = new ArrayList<Cidade>();

		try {
			comboCidadeOrigem = facade.getCidadesByPais(entity.getPaisOrigem());

		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	public void popularComboTrechoCidadeOrigem() {
		comboTrechoCidadeOrigem = new ArrayList<Cidade>();
		comboTrechoCidadeOrigemSI = new ArrayList<SelectItem>();

		try {
			comboTrechoCidadeOrigem = facade.getCidadesByPais(filtro
					.getPaisOrigem());
			for (Cidade cidade : comboTrechoCidadeOrigem) {
				comboTrechoCidadeOrigemSI.add(new SelectItem(cidade, cidade
						.getNome() + " - " + cidade.getEstado().getSigla()));

			}
			converterCidadeOrigem = new EntityConverter<Cidade>(
					comboCidadeOrigem);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	public void popularComboCidadeDestino() {
		comboCidadeDestino = new ArrayList<Cidade>();
		try {
			comboCidadeDestino = facade.getCidadesByPais(entity
					.getPaisDestino());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	public void popularComboTrechoCidadeDestino() {
		comboTrechoCidadeDestino = Collections.emptyList();
		comboTrechoCidadeDestinoSI = new ArrayList<SelectItem>();

		try {
			comboTrechoCidadeDestino = facade.getCidadesByPais(filtro
					.getPaisDestino());
			for (Cidade cidade : comboTrechoCidadeDestino) {
				comboTrechoCidadeDestinoSI.add(new SelectItem(cidade, cidade
						.getNome() + " - " + cidade.getEstado().getSigla()));
			}
			converterCidadeDestino = new EntityConverter<Cidade>(
					comboTrechoCidadeDestino);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Preeche combo de terminal
	 */
	public void popularComboTerminalOrigem() {
		comboTerminalOrigem = Collections.emptyList();

		comboTerminalOrigemSI = new ArrayList<SelectItem>();
		try {

			comboTerminalOrigem = facade
					.listarTerminais(new BasicFiltroTerminal(filtro
							.getPaisOrigem(), null, filtro.getCidadeOrigem()));
			for (Terminal term : comboTerminalOrigem) {
				comboTerminalOrigemSI.add(new SelectItem(term, term.getNome()));
			}

			converterTerminalOrigem = new EntityConverter<Terminal>(
					comboTerminalOrigem);

		} catch (BusinessException e) {
		}
	}

	/**
	 * Preeche combo de terminal
	 */
	public void popularComboTerminalDestino() {

		comboTerminalDestinoSI = new ArrayList<SelectItem>();
		comboTerminalDestino = Collections.emptyList();
		try {

			comboTerminalDestino = facade
					.listarTerminais(new BasicFiltroTerminal(filtro
							.getPaisDestino(), null, filtro.getCidadeDestino()));
			for (Terminal term : comboTerminalDestino) {
				comboTerminalDestinoSI
						.add(new SelectItem(term, term.getNome()));
			}
			converterTerminalDestino = new EntityConverter<Terminal>(
					comboTerminalDestino);

		} catch (BusinessException e) {
		}
	}

	/**
	 * 
	 */
	public void popularComboCidadeTerminalDestino() {
		popularComboTrechoCidadeDestino();
		popularComboTerminalDestino();
	}

	/**
	 * 
	 */
	public void popularComboAgente() {
		comboAgenteCargas = new ArrayList<PessoaJuridica>();

		try {
			comboAgenteCargas = facade
					.listarAllPersons(new BasicFiltroPessoaJuridica(
							new TipoPessoa(TipoPessoaEnum.A_CARGA.toString()),
							true));

		} catch (BusinessException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 */
	public void popularComboModal() {
		comboModal = new ArrayList<Modal>();
		try {
			comboModal = facade.listarModals();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void refazerPesquisa() {
	}

	/**
	 * @return the entity
	 */
	public Rota getEntity() {
		return entity;
	}

	/**
	 * @param entity
	 *            the entity to set
	 */
	public void setEntity(Rota entity) {
		this.entity = entity;
	}

	/**
	 * @return the edicao
	 */
	public boolean isEdicao() {
		return edicao;
	}

	/**
	 * @param edicao
	 *            the edicao to set
	 */
	public void setEdicao(boolean edicao) {
		this.edicao = edicao;
	}

	/**
	 * @return the comboPaisOrigem
	 */
	public List<Pais> getComboPaisOrigem() {
		return comboPaisOrigem;
	}

	/**
	 * @param comboPaisOrigem
	 *            the comboPaisOrigem to set
	 */
	public void setComboPaisOrigem(List<Pais> comboPaisOrigem) {
		this.comboPaisOrigem = comboPaisOrigem;
	}

	/**
	 * @return the comboPaisDestino
	 */
	public List<Pais> getComboPaisDestino() {
		return comboPaisDestino;
	}

	/**
	 * @param comboPaisDestino
	 *            the comboPaisDestino to set
	 */
	public void setComboPaisDestino(List<Pais> comboPaisDestino) {
		this.comboPaisDestino = comboPaisDestino;
	}

	/**
	 * @return the comboCidadeOrigem
	 */
	public List<Cidade> getComboCidadeOrigem() {
		return comboCidadeOrigem;
	}

	/**
	 * @param comboCidadeOrigem
	 *            the comboCidadeOrigem to set
	 */
	public void setComboCidadeOrigem(List<Cidade> comboCidadeOrigem) {
		this.comboCidadeOrigem = comboCidadeOrigem;
	}

	/**
	 * @return the comboCidadeDestino
	 */
	public List<Cidade> getComboCidadeDestino() {
		return comboCidadeDestino;
	}

	/**
	 * @param comboCidadeDestino
	 *            the comboCidadeDestino to set
	 */
	public void setComboCidadeDestino(List<Cidade> comboCidadeDestino) {
		this.comboCidadeDestino = comboCidadeDestino;
	}

	/**
	 * @return the comboAgenteCargas
	 */
	public List<PessoaJuridica> getComboAgenteCargas() {
		return comboAgenteCargas;
	}

	/**
	 * @param comboAgenteCargas
	 *            the comboAgenteCargas to set
	 */
	public void setComboAgenteCargas(List<PessoaJuridica> comboAgenteCargas) {
		this.comboAgenteCargas = comboAgenteCargas;
	}

	/**
	 * @return the comboModal
	 */
	public List<Modal> getComboModal() {
		return comboModal;
	}

	/**
	 * @param comboModal
	 *            the comboModal to set
	 */
	public void setComboModal(List<Modal> comboModal) {
		this.comboModal = comboModal;
	}

	/**
	 * @return the comboStatus
	 */
	public List<SelectItem> getComboStatus() {
		return comboStatus;
	}

	/**
	 * @param comboStatus
	 *            the comboStatus to set
	 */
	public void setComboStatus(List<SelectItem> comboStatus) {
		this.comboStatus = comboStatus;
	}

	/**
	 * @return the comboExpresso
	 */
	public List<SelectItem> getComboExpresso() {
		return comboExpresso;
	}

	/**
	 * @param comboExpresso
	 *            the comboExpresso to set
	 */
	public void setComboExpresso(List<SelectItem> comboExpresso) {
		this.comboExpresso = comboExpresso;
	}

	/**
	 * @return the trecho
	 */
	public Trecho getTrecho() {
		return trecho;
	}

	/**
	 * @param trecho
	 *            the trecho to set
	 */
	public void setTrecho(Trecho trecho) {
		this.trecho = trecho;
	}

	/**
	 * @return the edicaoTrecho
	 */
	public boolean isEdicaoTrecho() {
		return edicaoTrecho;
	}

	/**
	 * @param edicaoTrecho
	 *            the edicaoTrecho to set
	 */
	public void setEdicaoTrecho(boolean edicaoTrecho) {
		this.edicaoTrecho = edicaoTrecho;
	}

	/**
	 * @return the comboTrechoCidadeOrigem
	 */
	public List<Cidade> getComboTrechoCidadeOrigem() {
		return comboTrechoCidadeOrigem;
	}

	/**
	 * @param comboTrechoCidadeOrigem
	 *            the comboTrechoCidadeOrigem to set
	 */
	public void setComboTrechoCidadeOrigem(List<Cidade> comboTrechoCidadeOrigem) {
		this.comboTrechoCidadeOrigem = comboTrechoCidadeOrigem;
	}

	/**
	 * @return the comboTrechoCidadeDestino
	 */
	public List<Cidade> getComboTrechoCidadeDestino() {
		return comboTrechoCidadeDestino;
	}

	/**
	 * @param comboTrechoCidadeDestino
	 *            the comboTrechoCidadeDestino to set
	 */
	public void setComboTrechoCidadeDestino(
			List<Cidade> comboTrechoCidadeDestino) {
		this.comboTrechoCidadeDestino = comboTrechoCidadeDestino;
	}

	/**
	 * @return the comboTerminalOrigem
	 */
	public List<Terminal> getComboTerminalOrigem() {
		return comboTerminalOrigem;
	}

	/**
	 * @param comboTerminalOrigem
	 *            the comboTerminalOrigem to set
	 */
	public void setComboTerminalOrigem(List<Terminal> comboTerminalOrigem) {
		this.comboTerminalOrigem = comboTerminalOrigem;
	}

	/**
	 * @return the comboTerminalDestino
	 */
	public List<Terminal> getComboTerminalDestino() {
		return comboTerminalDestino;
	}

	/**
	 * @param comboTerminalDestino
	 *            the comboTerminalDestino to set
	 */
	public void setComboTerminalDestino(List<Terminal> comboTerminalDestino) {
		this.comboTerminalDestino = comboTerminalDestino;
	}

	/**
	 * @return the filtro
	 */
	public BasicFiltroTrecho getFiltro() {
		return filtro;
	}

	/**
	 * @param filtro
	 *            the filtro to set
	 */
	public void setFiltro(BasicFiltroTrecho filtro) {
		this.filtro = filtro;
	}

	/**
	 * @return the desabilitarDestino
	 */
	public boolean isDesabilitarDestino() {
		return desabilitarDestino;
	}

	/**
	 * @param desabilitarDestino
	 *            the desabilitarDestino to set
	 */
	public void setDesabilitarDestino(boolean desabilitarDestino) {
		this.desabilitarDestino = desabilitarDestino;
	}

	/**
	 * @return the terminalOrigem
	 */
	public Terminal getTerminalOrigem() {
		return terminalOrigem;
	}

	/**
	 * @param terminalOrigem
	 *            the terminalOrigem to set
	 */
	public void setTerminalOrigem(Terminal terminalOrigem) {
		this.terminalOrigem = terminalOrigem;
	}

	/**
	 * @return the terminalDestino
	 */
	public Terminal getTerminalDestino() {
		return terminalDestino;
	}

	/**
	 * @param terminalDestino
	 *            the terminalDestino to set
	 */
	public void setTerminalDestino(Terminal terminalDestino) {
		this.terminalDestino = terminalDestino;
	}

	/**
	 * @return the comboTerminalOrigemSI
	 */
	public List<SelectItem> getComboTerminalOrigemSI() {
		return comboTerminalOrigemSI;
	}

	/**
	 * @param comboTerminalOrigemSI
	 *            the comboTerminalOrigemSI to set
	 */
	public void setComboTerminalOrigemSI(List<SelectItem> comboTerminalOrigemSI) {
		this.comboTerminalOrigemSI = comboTerminalOrigemSI;
	}

	/**
	 * @return the comboTerminalDestinoSI
	 */
	public List<SelectItem> getComboTerminalDestinoSI() {
		return comboTerminalDestinoSI;
	}

	/**
	 * @param comboTerminalDestinoSI
	 *            the comboTerminalDestinoSI to set
	 */
	public void setComboTerminalDestinoSI(
			List<SelectItem> comboTerminalDestinoSI) {
		this.comboTerminalDestinoSI = comboTerminalDestinoSI;
	}

	/**
	 * @return the convererTerminalOrigem
	 */
	public EntityConverter<Terminal> getConverterTerminalOrigem() {
		return converterTerminalOrigem;
	}

	/**
	 * @param convererTerminalOrigem
	 *            the convererTerminalOrigem to set
	 */
	public void setConverterTerminalOrigem(
			EntityConverter<Terminal> convererTerminalOrigem) {
		this.converterTerminalOrigem = convererTerminalOrigem;
	}

	/**
	 * @return the convererTerminalDestino
	 */
	public EntityConverter<Terminal> getConverterTerminalDestino() {
		return converterTerminalDestino;
	}

	/**
	 * @param convererTerminalDestino
	 *            the convererTerminalDestino to set
	 */
	public void setConverterTerminalDestino(
			EntityConverter<Terminal> convererTerminalDestino) {
		this.converterTerminalDestino = convererTerminalDestino;
	}

	/**
	 * @return the comboTrechoCidadeOrigemSI
	 */
	public List<SelectItem> getComboTrechoCidadeOrigemSI() {
		return comboTrechoCidadeOrigemSI;
	}

	/**
	 * @param comboTrechoCidadeOrigemSI
	 *            the comboTrechoCidadeOrigemSI to set
	 */
	public void setComboTrechoCidadeOrigemSI(
			List<SelectItem> comboTrechoCidadeOrigemSI) {
		this.comboTrechoCidadeOrigemSI = comboTrechoCidadeOrigemSI;
	}

	/**
	 * @return the comboTrechoCidadeDestinoSI
	 */
	public List<SelectItem> getComboTrechoCidadeDestinoSI() {
		return comboTrechoCidadeDestinoSI;
	}

	/**
	 * @param comboTrechoCidadeDestinoSI
	 *            the comboTrechoCidadeDestinoSI to set
	 */
	public void setComboTrechoCidadeDestinoSI(
			List<SelectItem> comboTrechoCidadeDestinoSI) {
		this.comboTrechoCidadeDestinoSI = comboTrechoCidadeDestinoSI;
	}

	/**
	 * @return the converterCidadeOrigem
	 */
	public EntityConverter<Cidade> getConverterCidadeOrigem() {
		return converterCidadeOrigem;
	}

	/**
	 * @param converterCidadeOrigem
	 *            the converterCidadeOrigem to set
	 */
	public void setConverterCidadeOrigem(
			EntityConverter<Cidade> converterCidadeOrigem) {
		this.converterCidadeOrigem = converterCidadeOrigem;
	}

	/**
	 * @return the converterCidadeDestino
	 */
	public EntityConverter<Cidade> getConverterCidadeDestino() {
		return converterCidadeDestino;
	}

	/**
	 * @param converterCidadeDestino
	 *            the converterCidadeDestino to set
	 */
	public void setConverterCidadeDestino(
			EntityConverter<Cidade> converterCidadeDestino) {
		this.converterCidadeDestino = converterCidadeDestino;
	}

}
