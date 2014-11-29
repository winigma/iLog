package br.com.ilog.importacao.business.mbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.hibernate.validator.ValidatorHelper;
import br.cits.commons.citspresentation.mbeans.AbstractManter;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.commons.citspresentation.util.JSFRequestBean;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.Contato;
import br.com.ilog.cadastro.business.entity.Filial;
import br.com.ilog.cadastro.business.entity.PessoaJuridica;
import br.com.ilog.cadastro.business.entity.TipoPessoaEnum;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.cadastro.presentation.util.ExceptionFiltro;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;
import br.com.ilog.importacao.business.entity.Carga;
import br.com.ilog.importacao.business.entity.Invoice;
import br.com.ilog.importacao.business.entity.StatusCarga;
import br.com.ilog.importacao.business.entity.StatusInvoice;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroInvoice;
import br.com.ilog.importacao.business.facade.ImportacaoFacade;
import br.com.ilog.importacao.business.util.SendMail;
import br.com.ilog.seguranca.business.entity.Usuario;
import br.com.ilog.seguranca.business.facade.SegurancaFacade;

/**
 * 
 * @author Wisley Souza
 * @coment Bean de manter planejar embarque...
 * 
 */

@Controller("mBeanManterPlanejarEmbarque")
@AccessScoped
public class MBeanManterPlanejarEmbarque extends AbstractManter implements
		Serializable {

	private static final long serialVersionUID = 1L;

	private static Logger logger = LoggerFactory
			.getLogger(MBeanManterPlanejarEmbarque.class);

	/**
	 * @coment fachada de importação
	 */
	@Resource(name = "controllerImportacao")
	ImportacaoFacade planejarEmbarqueFacade;

	/**
	 * @coment fachada de cadastros
	 */
	@Resource(name = "controllerCadastro")
	CadastroFacade cadastrotFacade;

	/**
	 * 
	 * @coment Fachada para auxiliar na busca de compradores
	 */
	@Resource(name = "controleUsuario")
	SegurancaFacade compradoresFacade;

	/**
	 * @coment objeto da classe carga
	 */
	private Carga carga;
	private Carga selectCarga;

	// private List<Invoice> invoices;
	private List<PessoaJuridica> agentes;
	private List<Usuario> responsaveis;
	private List<SelectItem> comboStatus;
	private List<Invoice> listaInvoices;
	private List<Invoice> listaInvoicesRemovidas;
	private List<Invoice> listInvoices;
	private BasicFiltroInvoice filtroInvoices;
	private List<Filial> filiais;

	private Invoice newInvoice;

	private boolean novaInvoice;

	@PostConstruct
	void inicializar() {

		inicializarObjetos();
		listInvoices = new ArrayList<Invoice>();
		listaInvoices = new ArrayList<Invoice>();
		listaInvoicesRemovidas = new ArrayList<Invoice>();
		filtroInvoices = new BasicFiltroInvoice();
		newInvoice = new Invoice();
		novaInvoice = false;

		popularComboCompradores();
		popularForncedores();
		popularStatus();
		popularFilial();

	}

	public String cancelar() {
		return "planejadas.jsf";
	}

	/**
	 * Prepara para adicionar um novo planejamento de embarque.
	 * 
	 * @return
	 */
	public String novoPlanejamento() {

		inicializarObjetos();
		edicao = false;

		try {
			carga = planejarEmbarqueFacade.preencherSequencial(carga);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "planejada.jsf";
	}

	/**
	 * 
	 */
	public void carregarFilial() {
		System.out.println();
	}

	private void enviarEmail(String msg, String destinatarios) {
		SendMail mail = new SendMail();
		try {
			mail.postMail("Invoices Cadastradas", msg, destinatarios);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String salvar() {
		FacesContext fc = FacesContext.getCurrentInstance();
		ResourceBundle resource = TemplateMessageHelper.getResourceBundle(
				MensagensSistema.CARGA, JSFRequestBean.getLocale());

		List<String> erros = ValidatorHelper
				.valida(this.carga,
						TemplateMessageHelper.getResourceBundle(
								MensagensSistema.SISTEMA, fc.getViewRoot()
										.getLocale()), resource);

		if (erros.isEmpty()) {
			try {
				if (edicao) {

					if (!listaInvoices.isEmpty()) {
						planejarEmbarqueFacade.alterarCarga(this.carga);

						for (Invoice item : listaInvoices) {
							item.setCarga(this.carga);
							item.setStatus(StatusInvoice.getStatus(carga
									.getStatus()));
							item.setCidadeAtual(carga.getCidadeAtual());

							planejarEmbarqueFacade.alterarInvoice(item);
						}
					} else {
						Messages.adicionaMensagemDeErro(TemplateMessageHelper
								.getMessage(MensagensSistema.CARGA,
										"lblInvoiceObrigatoria", fc
												.getViewRoot().getLocale()));
						return null;
					}
					if (!listaInvoicesRemovidas.isEmpty()) {
						for (Invoice item : listaInvoicesRemovidas) {
							item.setCarga(null);
							item.setStatus(StatusInvoice.C);
							item.setCidadeAtual(null);
							planejarEmbarqueFacade.alterarInvoice(item);

						}
					}
				} else {
					if (listaInvoices.isEmpty()) {

						Messages.adicionaMensagemDeErro(TemplateMessageHelper
								.getMessage(MensagensSistema.CARGA,
										"lblInvoiceObrigatoria", fc
												.getViewRoot().getLocale()));
						return null;

					} else {
						planejarEmbarqueFacade.cadastrarCarga(this.carga);
						for (Invoice item : listaInvoices) {
							item.setCarga(this.carga);
							item.setStatus(StatusInvoice.getStatus(carga
									.getStatus()));
							planejarEmbarqueFacade.alterarInvoice(item);
						}

						List<String> params = new ArrayList<String>();
						params.add(carga.getProcesso().toUpperCase());
						String message = TemplateMessageHelper.getMessage(
								MensagensSistema.CARGA, "msgProcessoCriado",
								params, fc.getViewRoot().getLocale());
						Messages.adicionaMensagemDeInfo(message);

						if (carga.getAgenteCarga().getContatos() != null
								|| !carga.getAgenteCarga().getContatos()
										.isEmpty()) {

							for (Contato item : carga.getAgenteCarga()
									.getContatos()) {
								if (item != null) {
									if (item.getEmail() != null
											|| !item.getEmail().isEmpty()) {
										enviarEmail(message, item.getEmail());
									}
								}
							}
							

						}
						if (carga.getImportador().getEmail() != null) {
							enviarEmail(message, carga.getImportador()
									.getEmail());
						}
						return "planejadas.jsf";
					}
				}
			} catch (BusinessException e) {
				e.printStackTrace();
				logger.error("erro: {}", e);
				Messages.adicionaMensagemDeErro(TemplateMessageHelper
						.getMessage(ExceptionFiltro.recursiveException(e)));
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("erro: {}", e);
				Messages.adicionaMensagemDeErro(TemplateMessageHelper
						.getMessage(ExceptionFiltro.recursiveException(e)));
				return null;
			}

		} else {
			Messages.adicionaMensagensDeErro(erros);
			return null;
		}
		String message = TemplateMessageHelper.getMessage(
				MensagensSistema.SISTEMA, "MSG001", fc.getViewRoot()
						.getLocale());
		Messages.adicionaMensagemDeInfo(message);
		refazerPesquisa();
		return "planejadas.jsf";
	}

	public void addInvoices() {
		try {
			for (Invoice item : listInvoices) {
				if (item.getSelect()) {
					if (listaInvoices.size() == 0
							|| !listaInvoices.contains(item)) {
						item.setSelect(false);
						listaInvoices.add(item);
						listaInvoicesRemovidas.remove(item);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void pesquisarInvoices() {
		filtroInvoices.setStatusInvoice(StatusInvoice.C);
		try {
			listInvoices = planejarEmbarqueFacade
					.listarInvoices(filtroInvoices);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	public void addNewInvoice() {
		newInvoice = new Invoice();
		newInvoice.setStatus(StatusInvoice.C);

		filtroInvoices.setNumeroInvoice(null);

		novaInvoice = true;
	}

	public void salvarInvoice() {
		try {
			newInvoice.setNumeroInvoice(filtroInvoices.getNumeroInvoice());

			newInvoice.setDtEmissao(new Date());

			FacesContext fc = FacesContext.getCurrentInstance();
			ResourceBundle resource = TemplateMessageHelper.getResourceBundle(
					MensagensSistema.INVOICE, JSFRequestBean.getLocale());

			List<String> erros = ValidatorHelper.valida(this.newInvoice,
					TemplateMessageHelper.getResourceBundle(
							MensagensSistema.SISTEMA, fc.getViewRoot()
									.getLocale()), resource);

			if (erros.isEmpty()) {

				newInvoice = planejarEmbarqueFacade
						.cadastrarInvoice(newInvoice);

				filtroInvoices.setNumeroInvoice(null);

				String message = TemplateMessageHelper.getMessage(
						MensagensSistema.SISTEMA, "MSG001", fc.getViewRoot()
								.getLocale());
				Messages.adicionaMensagemDeInfo(message);

				novaInvoice = false;

				getInvoices();

			} else {
				Messages.adicionaMensagensDeErro(erros);
			}

		} catch (Exception e) {
			newInvoice.setId(null);
			novaInvoice = true;

			e.printStackTrace();
			logger.error("error: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper
					.getMessage(ExceptionFiltro.recursiveException(e)));

		}
	}

	public void cancelarInvoice() {
		newInvoice = new Invoice();

		filtroInvoices.setNumeroInvoice(null);

		this.getInvoices();
	}

	public void getInvoices() {
		listInvoices = new ArrayList<Invoice>();
		novaInvoice = false;

		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			filtroInvoices.setStatusInvoice(StatusInvoice.C);
			listInvoices = planejarEmbarqueFacade
					.listarInvoices(filtroInvoices);

			if (listInvoices.isEmpty()) {
				String msg = TemplateMessageHelper.getMessage(
						MensagensSistema.SISTEMA, "MSG008", fc.getViewRoot()
								.getLocale());
				Messages.adicionaMensagemDeInfo(msg);
			}
			if (listaInvoices != null && !listaInvoices.isEmpty())
				listInvoices.removeAll(listaInvoices);
			filtroInvoices = new BasicFiltroInvoice();
		} catch (BusinessException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @coment Método que popula a combo de Status
	 */
	public void popularStatus() {
		FacesContext fc = FacesContext.getCurrentInstance();

		comboStatus = new ArrayList<SelectItem>();

		for (StatusCarga status : StatusCarga.getValores()) {
			comboStatus.add(new SelectItem(status, TemplateMessageHelper
					.getMessage(MensagensSistema.CARGA, status.name(), fc
							.getViewRoot().getLocale())));
		}

	}

	/**
	 * Verificar se carrega combo de cidade.
	 * 
	 * @return
	 */
	public boolean carregarCidades() {
		if (StatusCarga.ITT.equals(carga.getStatus())
				|| StatusCarga.OHI.equals(carga.getStatus()))
			return true;
		return false;
	}

	public void popularFilial() {
		filiais = new ArrayList<Filial>();
		try {
			filiais = cadastrotFacade.listarFilialByFilter(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @coment Método que popula a combo de fornecedores
	 */
	public void popularForncedores() {
		agentes = new ArrayList<PessoaJuridica>();
		try {

			List<PessoaJuridica> fornecedorAux = cadastrotFacade
					.listarPessoasByTipo(TipoPessoaEnum.A_CARGA);
			if (fornecedorAux != null) {
				agentes = fornecedorAux;

			}
		} catch (BusinessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @coment Método que popula o combo de compradores
	 */
	public void popularComboCompradores() {
		responsaveis = new ArrayList<Usuario>();
		try {

			List<Usuario> compradoresAux = compradoresFacade
					.listarCompradores(null);
			if (compradoresAux != null) {
				responsaveis = compradoresAux;
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String editar() {
		try {
			carga = (Carga) planejarEmbarqueFacade.getCargaById(selectCarga
					.getId());

			listaInvoices = planejarEmbarqueFacade
					.listarInvoicesByCargaPlanejada(this.carga);
			edicao = true;

			return "planejada.jsf";

		} catch (BusinessException e) {
			logger.error("erro: {}", e);
			e.printStackTrace();
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.CARGA, e));
			return null;
		} catch (Exception e) {
			logger.error("erro: {}", e);
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 
	 * @return
	 */
	public boolean getRenderExcluir() {
		if (edicao && carga.getStatus().equals(StatusCarga.P)) {
			return true;
		}

		return false;
	}

	@Override
	public String excluir() {
		FacesContext fc = FacesContext.getCurrentInstance();

		try {
			planejarEmbarqueFacade.excluirCarga(this.carga);

			Messages.adicionaMensagemDeInfo(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG_EXCLUIR_SUCESSO", fc
							.getViewRoot().getLocale()));

		} catch (BusinessException e) {

			logger.error("error: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, e, fc.getViewRoot().getLocale()));

			return null;

		} catch (Exception e) {

			logger.error("error: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper
					.getMessage(ExceptionFiltro.recursiveException(e)));

			return null;
		}
		return "planejadas.jsf";

	}

	@Override
	public void inicializarObjetos() {
		carga = new Carga();
		carga.setVisivel(false);
		carga.setStatus(StatusCarga.P);
		carga.setDtConsolidacao(new Date());

	}

	@Override
	protected void refazerPesquisa() {
		// TODO Auto-generated method stub

	}

	public Carga getCarga() {
		return carga;
	}

	public void setCarga(Carga carga) {
		this.carga = carga;
	}

	public Carga getSelectCarga() {
		return selectCarga;
	}

	public void setSelectCarga(Carga selectCarga) {
		this.selectCarga = selectCarga;
	}

	public List<PessoaJuridica> getAgentes() {
		return agentes;
	}

	public void setAgentes(List<PessoaJuridica> agentes) {
		this.agentes = agentes;
	}

	public List<Usuario> getResponsaveis() {
		return responsaveis;
	}

	public void setResponsaveis(List<Usuario> responsaveis) {
		this.responsaveis = responsaveis;
	}

	public List<SelectItem> getComboStatus() {
		return comboStatus;
	}

	public void setComboStatus(List<SelectItem> comboStatus) {
		this.comboStatus = comboStatus;
	}

	public List<Invoice> getListaInvoices() {
		return listaInvoices;
	}

	public void setListaInvoices(List<Invoice> listaInvoices) {
		this.listaInvoices = listaInvoices;
	}

	public List<Invoice> getListaInvoicesRemovidas() {
		return listaInvoicesRemovidas;
	}

	public void setListaInvoicesRemovidas(List<Invoice> listaInvoicesRemovidas) {
		this.listaInvoicesRemovidas = listaInvoicesRemovidas;
	}

	public List<Invoice> getListInvoices() {
		return listInvoices;
	}

	public void setListInvoices(List<Invoice> listInvoices) {
		this.listInvoices = listInvoices;
	}

	public BasicFiltroInvoice getFiltroInvoices() {
		return filtroInvoices;
	}

	public void setFiltroInvoices(BasicFiltroInvoice filtroInvoices) {
		this.filtroInvoices = filtroInvoices;
	}

	public Invoice getNewInvoice() {
		return newInvoice;
	}

	public void setNewInvoice(Invoice newInvoice) {
		this.newInvoice = newInvoice;
	}

	public List<Filial> getFiliais() {
		return filiais;
	}

	public void setFiliais(List<Filial> filiais) {
		this.filiais = filiais;
	}

	/**
	 * @return the novaInvoice
	 */
	public boolean isNovaInvoice() {
		return novaInvoice;
	}

	/**
	 * @param novaInvoice
	 *            the novaInvoice to set
	 */
	public void setNovaInvoice(boolean novaInvoice) {
		this.novaInvoice = novaInvoice;
	}

}
