package br.com.ilog.importacao.business.mbean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.hibernate.validator.ValidatorHelper;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.commons.citspresentation.util.JSFRequestBean;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.Incoterm;
import br.com.ilog.cadastro.business.entity.Modal;
import br.com.ilog.cadastro.business.entity.Moeda;
import br.com.ilog.cadastro.business.entity.PessoaJuridica;
import br.com.ilog.cadastro.business.entity.Terminal;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroIncoterm;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.cadastro.presentation.util.ConverterUtil;
import br.com.ilog.cadastro.presentation.util.ExceptionFiltro;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;
import br.com.ilog.geral.presentation.mbean.AbstractManterPaginacao;
import br.com.ilog.importacao.business.entity.Carga;
import br.com.ilog.importacao.business.entity.Invoice;
import br.com.ilog.importacao.business.entity.ItemInvoice;
import br.com.ilog.importacao.business.entity.ItemPO;
import br.com.ilog.importacao.business.entity.PO;
import br.com.ilog.importacao.business.entity.StatusInvoice;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroPO;
import br.com.ilog.importacao.business.facade.ImportacaoFacade;
import br.com.ilog.seguranca.business.facade.SegurancaFacade;

/**
 */
@Controller("mBeanManterInvoice")
@AccessScoped
public class MBeanManterInvoice extends AbstractManterPaginacao {

	/** */
	private static final long serialVersionUID = -2425337259281397155L;

	@Resource(name = "controllerImportacao")
	ImportacaoFacade facade;

	@Resource(name = "controllerCadastro")
	CadastroFacade cadastroFacade;

	@Resource(name = "controleUsuario")
	SegurancaFacade seguranca;

	private static Logger logger = LoggerFactory
			.getLogger(MBeanManterInvoice.class);

	private Invoice invoice;

	private Integer totalQtde;

	private BigDecimal totalValorItens;

	private List<PessoaJuridica> fornecedores;
	private ConverterUtil<PessoaJuridica> fornecedoresConverter;

	private List<Moeda> moedas;
	private ConverterUtil<Moeda> moedaConverter;

	private List<Modal> tiposTransporte;

	private List<Incoterm> incoterms;
	private ConverterUtil<Incoterm> incotermsConverter;

	private List<Terminal> terminais;
	private ConverterUtil<Terminal> terminaisConverter;

	// Atributos de anexos da invoice

	private ItemInvoice itemInvoice;

	private Invoice selectInvoice;

	private ItemPO itemPO;

	private List<PO> listPos;
	private ConverterUtil<PO> poConverter;

	private List<ItemPO> listItemPOs;
	private ConverterUtil<ItemPO> itemPOConverter;

	private String partNumber;

	private PO po;

	private Boolean required;

	/**
	 * Atributos Item Invoice
	 */
	private BigDecimal totalItem;
	private Boolean editarItem;
	private int saldoInicial;
	private int saldoParcial;
	private int tmp;

	public String novaInvoice() {
		required = false;
		edicao = false;

		inicializarObjetos();

		invoice.setDtEmissao(new Date());
		invoice.setStatus(StatusInvoice.C);
		invoice.setItensInvoice(new ArrayList<ItemInvoice>());

		renderizaDadosFornecedor();
		inicializaCombos();
		return "invoice.jsf";
	}

	private void preencherPO() throws BusinessException {
		getPo().setFilial(invoice.getCarga().getFilial());
		if (editarItem) {
			itemPO = new ItemPO();
			itemPO = itemInvoice.getItemPO();
		}
		// itemPO.setPartNumber(partNumber);
		itemPO.setPo(getPo());
		itemPO = facade.getItemInvoice(itemPO);
		itemPOConverter = new ConverterUtil<ItemPO>(po.getItens());
	}

	public void pesquisarItem() {
		try {
			this.tmp = 0;
			preencherPO();

			if (itemPO != null) {
				itemInvoice = new ItemInvoice();
				itemInvoice.setItem(itemPO.getNumeroItem());
				itemInvoice.setUnidadeMedida(itemPO.getUnidadeMedida()
						.getCodigo());
				itemInvoice.setPrecoUnit(itemPO.getPrecoUnitario());
				itemInvoice.setDescricao(itemPO.getDescricaoProduto());
				itemInvoice.setVendorPartNum(itemPO.getPartNumber());
				itemInvoice.setItemPO(itemPO);
				saldoParcial = itemPO.getQuantidadeSaldo();
				saldoInicial = 0;
			}

			// Verificar se o itemPO já esta na lista
			if (!invoice.getItensInvoice().isEmpty()) {
				for (ItemInvoice iv : invoice.getItensInvoice()) {
					if (iv.getItemPO() == itemInvoice.getItemPO()
							&& iv.getVendorPartNum().equals(
									itemInvoice.getVendorPartNum())) {
						itemInvoice = iv;
						saldoInicial = itemPO.getQuantidadeSaldo();
						saldoParcial = itemPO.getQuantidadeSaldo()
								- itemInvoice.getQuantidade();
						editarItem = true;
						totalItem = itemPO.getPrecoUnitario().multiply(
								new BigDecimal(itemInvoice.getQuantidade()));
						break;
					}
				}
			}

		} catch (NoResultException e) {
			itemInvoice = null;
			inicializarItemInvoice();
		} catch (BusinessException e1) {
			logger.error("error: {} " + e1);
			e1.printStackTrace();
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, e1));
		}

	}

	public void popularItemPO() {
		setListItemPOs(new ArrayList<ItemPO>());
		setListItemPOs(po.getItens());
		itemPOConverter = new ConverterUtil<ItemPO>(po.getItens());

	}

	public void editarItem() {
		try {

			po = itemInvoice.getItemPO().getPo();
			partNumber = itemInvoice.getVendorPartNum();

			required = true;
			editarItem = true;
			preencherPO();
			saldoInicial = itemPO.getQuantidadeSaldo();
			saldoParcial = itemPO.getQuantidadeSaldo()
					- itemInvoice.getQuantidade();
			totalItem = itemPO.getPrecoUnitario().multiply(
					new BigDecimal(itemInvoice.getQuantidade()));
		} catch (BusinessException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @comment Calcular o valor total do item e saldo
	 */
	public void calcularTotalItem() {

		if (editarItem) {
			if (saldoInicial >= saldoParcial) {
				setSaldoParcial(itemPO.getQuantidadeSaldo()
						- itemInvoice.getQuantidade());

			} else {
				setSaldoParcial(itemPO.getQuantidadeSaldo()
						+ itemInvoice.getQuantidade());
			}
		} else {
			setSaldoParcial(itemPO.getQuantidadeSaldo()
					- itemInvoice.getQuantidade());
		}
		if (saldoParcial <= -1) {
			saldoParcial = 0;
			itemInvoice.setQuantidade(itemPO.getQuantidadeSaldo());
			FacesContext fc = FacesContext.getCurrentInstance();
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.INVOICE, "lblQtdMaioSaldo", fc
							.getViewRoot().getLocale()));
		}
		this.tmp = itemInvoice.getQuantidade();
		totalItem = itemPO.getPrecoUnitario().multiply(
				new BigDecimal(itemInvoice.getQuantidade()));
	}

	public String salvar() {

		if (invoice.getNumeroInvoice().trim().equals("")) {
			invoice.setNumeroInvoice(null);
		}

		invoice.setItensInvoice(invoice.getItensInvoice());

		FacesContext fc = FacesContext.getCurrentInstance();
		ResourceBundle resourceBundle = TemplateMessageHelper
				.getResourceBundle(MensagensSistema.INVOICE, fc.getViewRoot()
						.getLocale());
		List<String> errorMessages = ValidatorHelper
				.valida(invoice,
						TemplateMessageHelper.getResourceBundle(
								MensagensSistema.SISTEMA, fc.getViewRoot()
										.getLocale()), resourceBundle);

		if (errorMessages.isEmpty()) {
			try {
				if (edicao) {
					if (getListItemPOs() != null && !getListItemPOs().isEmpty()) {
						for (ItemPO itemPO : getListItemPOs()) {
							if (itemPO != null)
								facade.alterarItemPO(itemPO);
						}
					}
					invoice = facade.alterarInvoice(invoice);

				}
			} catch (Exception e) {
				e.printStackTrace();

				logger.error("error: {}", e);
				Throwable lastCause = ExceptionFiltro.getLastException(e);
				if (lastCause.getMessage().contains("CHECK constraint")) {
					if (lastCause.getMessage()
							.contains("chk_invoice_duplicado")) {
						Messages.adicionaMensagemDeErro(TemplateMessageHelper
								.getMessage(MensagensSistema.INVOICE,
										"msgCheckInvoice", fc.getViewRoot()
												.getLocale()));
					}
					return null;
				} else {

					Messages.adicionaMensagemDeErro(TemplateMessageHelper
							.getMessage(ExceptionFiltro.recursiveException(e)));
					e.printStackTrace();
					this.refazerPesquisa();
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
		refazerPesquisa();

		return "invoices.jsf";
	}

	@Override
	public String editar() {

		try {

			inicializaCombos();

			invoice = (Invoice) facade.getInvoiceByIdWithFile(
					selectInvoice.getId(), true, true);

			inicializarItemInvoice();
			// anexo = invoice.getAnexo();

			carregarTotais();

			carregarInvoice();

			renderizaDadosFornecedor();
			required = false;

		} catch (BusinessException e) {
			logger.error("erro: {}", e);
			e.printStackTrace();
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.INVOICE, e));
			return null;
		}

		edicao = true;
		return "invoice.jsf";
	}

	private void carregarTotais() {
		totalQtde = null;
		totalValorItens = null;
		if (invoice.getItensInvoice() != null) {
			for (ItemInvoice it : invoice.getItensInvoice()) {
				if (totalQtde == null) {
					totalQtde = 0;
				}
				totalQtde = totalQtde + it.getQuantidade();
				if (totalValorItens == null) {
					totalValorItens = BigDecimal.ZERO;
				}
				totalValorItens = totalValorItens.add(it.getTotal());
			}
		}
	}

	/**
	 * Metodo para inicializar dados de exportador da invoice.
	 * 
	 * @throws BusinessException
	 *             TODO: Verificar
	 */
	private void carregarInvoice() throws BusinessException {

		// REMOCAO DE ANEXO DE INVOICE
		// if (invoice.getAnexo() != null) {
		// nomeArquivo = invoice.getAnexo().getNomeArquivo();
		// }

		if (invoice.getIncoterm() != null) {
			Incoterm inco = facade.getIncoTermsById(invoice.getIncoterm()
					.getId());
			invoice.setIncoterm(inco);
		}
		renderizaDadosFornecedor();

	}

	Long idItemInvoice;

	public void capturarId(int index) {
		idItemInvoice = Long.valueOf(Integer.toString(index));
	}

	/**
	 * Inicializa o objeto item invoice e seus objetos dependentes
	 */
	public void inicializarItemInvoice() {
		try {
			editarItem = false;
			required = true;
			po = new PO();
			listPos = new ArrayList<PO>();
			itemInvoice = null;
			itemPO = null;
			partNumber = null;
			BasicFiltroPO filtroPO = new BasicFiltroPO();

			Carga c = facade.getCargaById(invoice.getCarga().getId());
			filtroPO.setFilial(c.getFilial());
			listPos = facade.listarPO(filtroPO);
			poConverter = new ConverterUtil<PO>(listPos);

		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * TODO: Verificar aqui Adiciona um item manualmente a lista de itens da
	 * invoice
	 */
	public void addItem() {
		FacesContext fc = FacesContext.getCurrentInstance();
		if (itemInvoice.getQuantidade() <= itemPO.getQuantidadeSaldo()) {
			BigDecimal total = itemInvoice.getPrecoUnit().multiply(
					new BigDecimal(itemInvoice.getQuantidade()));
			itemInvoice.setTotal(total);
			if (!editarItem) {

				if (itemInvoice.getPrecoUnit() == null) {
					itemInvoice.setPrecoUnit(new BigDecimal(0.0));
				}
				if (itemInvoice.getQuantidade() == null) {
					itemInvoice.setQuantidade(0);
				}
				itemInvoice.setInvoice(invoice);

				invoice.getItensInvoice().add(itemInvoice);

			} else {
				invoice.getItensInvoice().set(
						invoice.getItensInvoice().indexOf(itemInvoice),
						itemInvoice);
			}
			carregarTotais();
			inicializarItemInvoice();
			required = false;
		} else {

			Messages.adicionaMensagemDeInfo(TemplateMessageHelper.getMessage(
					MensagensSistema.INVOICE, "lblQtdMaioSaldo", fc
							.getViewRoot().getLocale()));
		}
	}

	public void excluirItem() {
		itemPO = itemInvoice.getItemPO();
		// itemPO.setQuantidadeSaldo(itemInvoice.getQuantidade()+itemInvoice.getItemPO().getQuantidadeSaldo());
		// atualizarItemPO(itemPO);
		invoice.getItensInvoice().remove(itemInvoice);
		carregarTotais();
	}

	@Override
	public String excluir() {

		try {

			FacesContext fc = FacesContext.getCurrentInstance();

			// remover os anexos da invoice, caso possua
			// facade.excluirAnexoInvoice(facade.getAnexosInvoice(invoice));
			if (invoice.getStatus().equals(StatusInvoice.C)) {

				facade.excluirInvoice(invoice);
				Messages.adicionaMensagemDeInfo(TemplateMessageHelper
						.getMessage(MensagensSistema.SISTEMA,
								"MSG_EXCLUIR_SUCESSO", fc.getViewRoot()
										.getLocale()));
				this.refazerPesquisa();
			} else {

				Messages.adicionaMensagemDeErro(TemplateMessageHelper
						.getMessage(MensagensSistema.INVOICE,
								"msgInfoErro", fc.getViewRoot()
										.getLocale()));
				return null;
			}

		} catch (BusinessException e) {
			e.printStackTrace();
			logger.error("error: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper
					.getMessage(ExceptionFiltro.recursiveException(e)));
			return null;
		} catch (UnsupportedOperationException e) {
			FacesContext fc = FacesContext.getCurrentInstance();
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG_GEN_007", fc.getViewRoot()
							.getLocale()));
			return null;
		}

		return "invoices.jsf";
	}

	@Override
	@PostConstruct
	public void inicializarObjetos() {

		invoice = new Invoice();
		required = false;
		setListItemPOs(new ArrayList<ItemPO>());

		// inicializarItemInvoice();
	}

	public void renderizaDadosFornecedor() {
		if (invoice.getExportador() != null
				&& invoice.getExportador().getId() != null) {
			try {
				PessoaJuridica fornecedor = cadastroFacade
						.getPessoaById(invoice.getExportador().getId());
				invoice.setExportador(fornecedor);
				facade.inicializarExportador(invoice);
			} catch (BusinessException e) {
				invoice.setExportador(null);
				e.printStackTrace();
			}
		} else {
			invoice.setExportador(null);
		}
	}

	private void inicializaCombos() {
		try {

			fornecedores = new ArrayList<PessoaJuridica>();
			List<PessoaJuridica> fornecs = cadastroFacade
					.listarAllFornecedoresByStatus(true);

			if (fornecs != null)
				fornecedores = fornecs;
			fornecedoresConverter = new ConverterUtil<PessoaJuridica>(fornecs);

			// ...............................................................................
			incoterms = new ArrayList<Incoterm>();
			List<Incoterm> incotermsAux = facade
					.listIncoterms(new BasicFiltroIncoterm(true));
			if (incotermsAux != null)
				incoterms = incotermsAux;

			incotermsConverter = new ConverterUtil<Incoterm>(incotermsAux);

			terminais = new ArrayList<Terminal>();
			List<Terminal> terminaisAux = cadastroFacade.listarTerminais(null);
			if (terminaisAux != null)
				terminais = terminaisAux;

			terminaisConverter = new ConverterUtil<Terminal>(terminaisAux);

		} catch (BusinessException e) {
		}

	}

	@Override
	protected void refazerPesquisa() {

		MBeanPesquisarInvoice bean = (MBeanPesquisarInvoice) JSFRequestBean
				.getManagedBean("mBeanPesquisarInvoice");

		bean.refazerPesquisa();

	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public ImportacaoFacade getFacade() {
		return facade;
	}

	public void setFacade(ImportacaoFacade facade) {
		this.facade = facade;
	}

	public List<Moeda> getMoedas() {
		try {
			moedas = new ArrayList<Moeda>();
			List<Moeda> moedasAux;
			moedasAux = cadastroFacade.listarMoedas();
			if (moedasAux != null)
				moedas = moedasAux;
			moedaConverter = new ConverterUtil<Moeda>(moedasAux);
		} catch (BusinessException e) {
			e.printStackTrace();
		}

		return moedas;
	}

	public void setMoedas(List<Moeda> moedas) {
		this.moedas = moedas;
	}

	private ConverterUtil<Modal> converterModal;

	public List<Modal> getTiposTransporte() {
		try {
			tiposTransporte = new ArrayList<Modal>();
			List<Modal> modais = cadastroFacade.listarModals();
			if (modais != null)
				tiposTransporte = modais;
			converterModal = new ConverterUtil<Modal>(modais);

			return tiposTransporte;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int getTotalRegistros() {
		if (invoice.getItensInvoice() != null) {
			return invoice.getItensInvoice().size();
		}
		return 0;
	}

	public Integer getTotalQtde() {
		return totalQtde;
	}

	public void setTotalQtde(Integer totalQtde) {
		this.totalQtde = totalQtde;
	}

	public BigDecimal getTotalValorItens() {
		return totalValorItens;
	}

	public void setTotalValorItens(BigDecimal totalValorItens) {
		this.totalValorItens = totalValorItens;
	}

	public ConverterUtil<PessoaJuridica> getFornecedoresConverter() {
		return fornecedoresConverter;
	}

	public void setFornecedoresConverter(
			ConverterUtil<PessoaJuridica> fornecedoresConverter) {
		this.fornecedoresConverter = fornecedoresConverter;
	}

	public ConverterUtil<Incoterm> getIncotermsConverter() {
		return incotermsConverter;
	}

	public void setIncotermsConverter(ConverterUtil<Incoterm> incotermsConverter) {
		this.incotermsConverter = incotermsConverter;
	}

	public ConverterUtil<Terminal> getTerminaisConverter() {
		return terminaisConverter;
	}

	public void setTerminaisConverter(ConverterUtil<Terminal> terminaisConverter) {
		this.terminaisConverter = terminaisConverter;
	}

	public ConverterUtil<Moeda> getMoedaConverter() {
		return moedaConverter;
	}

	public void setMoedaConverter(ConverterUtil<Moeda> moedaConverter) {
		this.moedaConverter = moedaConverter;
	}

	/**
	 * @return the itemInvoice
	 */
	public ItemInvoice getItemInvoice() {
		return itemInvoice;
	}

	/**
	 * @param itemInvoice
	 *            the itemInvoice to set
	 */
	public void setItemInvoice(ItemInvoice itemInvoice) {
		this.itemInvoice = itemInvoice;
	}

	public ConverterUtil<Modal> getConverterModal() {
		return converterModal;
	}

	public void setConverterModal(ConverterUtil<Modal> converterModal) {
		this.converterModal = converterModal;
	}

	public Invoice getSelectInvoice() {
		return selectInvoice;
	}

	public void setSelectInvoice(Invoice selectInvoice) {
		this.selectInvoice = selectInvoice;
	}

	public List<PessoaJuridica> getFornecedores() {
		return fornecedores;
	}

	public void setFornecedores(List<PessoaJuridica> fornecedores) {
		this.fornecedores = fornecedores;
	}

	public List<Terminal> getTerminais() {
		return terminais;
	}

	public void setTerminais(List<Terminal> terminais) {
		this.terminais = terminais;
	}

	public List<Incoterm> getIncoterms() {
		return incoterms;
	}

	public void setIncoterms(List<Incoterm> incoterms) {
		this.incoterms = incoterms;
	}

	public void setTiposTransporte(List<Modal> tiposTransporte) {
		this.tiposTransporte = tiposTransporte;
	}

	/**
	 * @return the itemPO
	 */
	public ItemPO getItemPO() {
		return itemPO;
	}

	/**
	 * @param itemPO
	 *            the itemPO to set
	 */
	public void setItemPO(ItemPO itemPO) {
		this.itemPO = itemPO;
	}

	/**
	 * @return the partNumber
	 */
	public String getPartNumber() {
		return partNumber;
	}

	/**
	 * @param partNumber
	 *            the partNumber to set
	 */
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	/**
	 * @return the listPos
	 */
	public List<PO> getListPos() {
		return listPos;
	}

	/**
	 * @param listPos
	 *            the listPos to set
	 */
	public void setListPos(List<PO> listPos) {
		this.listPos = listPos;
	}

	/**
	 * @return the po
	 */
	public PO getPo() {
		return po;
	}

	/**
	 * @param po
	 *            the po to set
	 */
	public void setPo(PO po) {
		this.po = po;
	}

	/**
	 * @return the poConverter
	 */
	public ConverterUtil<PO> getPoConverter() {
		return poConverter;
	}

	/**
	 * @param poConverter
	 *            the poConverter to set
	 */
	public void setPoConverter(ConverterUtil<PO> poConverter) {
		this.poConverter = poConverter;
	}

	/**
	 * @return the totalItem
	 */
	public BigDecimal getTotalItem() {
		return totalItem;
	}

	/**
	 * @param totalItem
	 *            the totalItem to set
	 */
	public void setTotalItem(BigDecimal totalItem) {
		this.totalItem = totalItem;
	}

	/**
	 * @return the required
	 */
	public Boolean getRequired() {
		return required;
	}

	/**
	 * @param required
	 *            the required to set
	 */
	public void setRequired(Boolean required) {
		this.required = required;
	}

	/**
	 * @return the saldoParcial
	 */
	public int getSaldoParcial() {
		return saldoParcial;
	}

	/**
	 * @param saldoParcial
	 *            the saldoParcial to set
	 */
	public void setSaldoParcial(int saldoParcial) {
		this.saldoParcial = saldoParcial;
	}

	/**
	 * @return the tmp
	 */
	public int getTmp() {
		return tmp;
	}

	/**
	 * @param tmp
	 *            the tmp to set
	 */
	public void setTmp(int tmp) {
		this.tmp = tmp;
	}

	/**
	 * @return the itemPOConverter
	 */
	public ConverterUtil<ItemPO> getItemPOConverter() {
		return itemPOConverter;
	}

	/**
	 * @param itemPOConverter
	 *            the itemPOConverter to set
	 */
	public void setItemPOConverter(ConverterUtil<ItemPO> itemPOConverter) {
		this.itemPOConverter = itemPOConverter;
	}

	/**
	 * @return the listItemPOs
	 */
	public List<ItemPO> getListItemPOs() {
		return listItemPOs;
	}

	/**
	 * @param listItemPOs
	 *            the listItemPOs to set
	 */
	public void setListItemPOs(List<ItemPO> listItemPOs) {
		this.listItemPOs = listItemPOs;
	}

}
