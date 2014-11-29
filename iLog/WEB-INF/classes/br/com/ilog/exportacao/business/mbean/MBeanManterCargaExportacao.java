package br.com.ilog.exportacao.business.mbean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import br.cits.commons.citspresentation.mbeans.AbstractManter;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.commons.citspresentation.util.JSFRequestBean;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.Cidade;
import br.com.ilog.cadastro.business.entity.Modal;
import br.com.ilog.cadastro.business.entity.Moeda;
import br.com.ilog.cadastro.business.entity.PessoaJuridica;
import br.com.ilog.cadastro.business.entity.Projeto;
import br.com.ilog.cadastro.business.entity.TipoPessoaEnum;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.exportacao.business.entity.CargaExp;
import br.com.ilog.exportacao.business.entity.Followup;
import br.com.ilog.exportacao.business.entity.InvoiceExp;
import br.com.ilog.exportacao.business.entity.ItemFollowup;
import br.com.ilog.exportacao.business.entity.ItemInvoiceExp;
import br.com.ilog.exportacao.business.entity.StatusCargaExp;
import br.com.ilog.exportacao.business.facade.ExportacaoFacade;
import br.com.ilog.geral.presentation.CommonsList;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;

/**
 * 
 * @author Wisley P Souza
 * 
 */

@Controller("mBeanManterCargaExp")
@AccessScoped
public class MBeanManterCargaExportacao extends AbstractManter {

	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory
			.getLogger(MBeanManterCargaExportacao.class);

	/**
	 * @coment fachada de exportacao
	 */
	@Resource(name = "controllerExportacao")
	ExportacaoFacade exportacaoFacade;

	/**
	 * @coment fachada de cadastros
	 */
	@Resource(name = "controllerCadastro")
	CadastroFacade cadastrotFacade;
	
	
	@Resource( name = "commonsList")
	CommonsList commonsList;

	private List<PessoaJuridica> clientes;
	private List<Moeda> moedas;
	private List<Modal> modais;
	private List<Projeto> projetos;
	private List<SelectItem> comboStatusCarga;
	private List<InvoiceExp> listaInvoices;
	// objeto principal
	private CargaExp carga;
	private CargaExp selectCarga;
	private Boolean novaCarga;

	// Invoice auxiliar
	private InvoiceExp invoice;
	private ItemInvoiceExp item;

	// Cidade de followup
	private List<Cidade> cidades;
	private ItemFollowup itemFollow1;
	private ItemFollowup itemFollow2;
	private Followup followUp;
	
	//combo ativo
	private List<SelectItem> comboAtivo;


	@PostConstruct
	void inicializar() {
		popularComboStatus();
		popularComboCliente();
		popularComboModal();
		popularComboMoeda();
		popularComboProjeto();
		popularCidades();
		comboAtivo = commonsList.listaSimNao();

	}

	public void addInvoice() {
		invoice = new InvoiceExp();
		invoice.setItens(new ArrayList<ItemInvoiceExp>());
		invoice.setCarga(carga);
	}

	public void addItem() {
		item = new ItemInvoiceExp();
	}

	public void inserirItem() {
		item.setInvoice(invoice);
		if (invoice.getItens() != null)
			invoice.getItens().add(item);
		else {
			invoice.setItens(new ArrayList<ItemInvoiceExp>());
			invoice.getItens().add(item);
		}
		item = new ItemInvoiceExp();
	}

	public void inserirInvoice() {
		RequestContext context = RequestContext.getCurrentInstance();
		FacesMessage msg = null;
		boolean loggedIn = false;
		if (invoice.getItens().size() > 0
				&& !invoice.getNumeroInvoice().isEmpty()) {
			loggedIn = true;
			// msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
			// "Adicionado com sucesso.", "wisley");
			if (listaInvoices != null) {
				listaInvoices.add(invoice);
			} else {
				listaInvoices = new ArrayList<InvoiceExp>();
				listaInvoices.add(invoice);

			}

		} else {
			loggedIn = false;
			// msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Login Error",
			// "Invalid credentials");
			FacesContext fc = FacesContext.getCurrentInstance();
			Messages.adicionaMensagemDeInfo(TemplateMessageHelper.getMessage(
					MensagensSistema.CARGA_EXP, "msgNoAddInvoice", fc
							.getViewRoot().getLocale()));
		}
		// FacesContext.getCurrentInstance().addMessage(null, msg);
		context.addCallbackParam("loggedIn", loggedIn);
	}

	public void addFollowUp() {
		if (followUp == null)
			followUp = new Followup();
		itemFollow1 = new ItemFollowup();
		itemFollow2 = new ItemFollowup();
	}
	
	

	public String novaCarga() {
		carga = new CargaExp();
		carga.setInvoices(new ArrayList<InvoiceExp>());
		carga.setFollowups(new ArrayList<Followup>());
		listaInvoices = new ArrayList<InvoiceExp>();
		selectCarga = new CargaExp();
		novaCarga = true;
		edicao = false;
		return "carga.jsf";
	}

	public String cancelar() {
		return "cargas.jsf";
	}

	public void popularCidades() {
		cidades = new ArrayList<Cidade>();
		try {
			cidades = cadastrotFacade.listarCidades(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void popularComboMoeda() {
		moedas = new ArrayList<Moeda>();
		try {
			moedas = cadastrotFacade.listarMoedas();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void popularComboModal() {
		modais = new ArrayList<Modal>();
		try {
			modais = cadastrotFacade.listarModals();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void popularComboProjeto() {
		projetos = new ArrayList<Projeto>();
		try {
			projetos = cadastrotFacade.listarProjetos(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void popularComboCliente() {

		clientes = new ArrayList<PessoaJuridica>();
		try {
			List<PessoaJuridica> fornecedorAux = cadastrotFacade
					.listarPessoasByTipo(TipoPessoaEnum.A_CARGA);
			if (fornecedorAux != null) {
				clientes = fornecedorAux;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public String salvar() {

		try {

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return super.salvar();
	}

	public void popularComboStatus() {
		comboStatusCarga = new ArrayList<SelectItem>();
		FacesContext fc = FacesContext.getCurrentInstance();
		for (StatusCargaExp status : StatusCargaExp.getValuesCargas()) {
			comboStatusCarga.add(new SelectItem(status, TemplateMessageHelper
					.getMessage(MensagensSistema.CARGA_EXP, status.name(), fc
							.getViewRoot().getLocale())));
		}
	}

	@Override
	public String editar() {
		return "carga.jsf";
	}

	@Override
	public String excluir() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void inicializarObjetos() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void refazerPesquisa() {
		MBeanPesquisarCargaExportacao mBean = (MBeanPesquisarCargaExportacao) JSFRequestBean
				.getManagedBean("mBeanPesquisarCargaExp");
		mBean.refazerPesquisa();

	}

	public List<PessoaJuridica> getClientes() {
		return clientes;
	}

	public void setClientes(List<PessoaJuridica> clientes) {
		this.clientes = clientes;
	}

	public List<Moeda> getMoedas() {
		return moedas;
	}

	public void setMoedas(List<Moeda> moedas) {
		this.moedas = moedas;
	}

	public List<Modal> getModais() {
		return modais;
	}

	public void setModais(List<Modal> modais) {
		this.modais = modais;
	}

	public List<SelectItem> getComboStatusCarga() {
		return comboStatusCarga;
	}

	public void setComboStatusCarga(List<SelectItem> comboStatusCarga) {
		this.comboStatusCarga = comboStatusCarga;
	}

	public CargaExp getCarga() {
		return carga;
	}

	public void setCarga(CargaExp carga) {
		this.carga = carga;
	}

	public CargaExp getSelectCarga() {
		return selectCarga;
	}

	public void setSelectCarga(CargaExp selectCarga) {
		this.selectCarga = selectCarga;
	}

	public Boolean getNovaCarga() {
		return novaCarga;
	}

	public void setNovaCarga(Boolean novaCarga) {
		this.novaCarga = novaCarga;
	}

	public List<Projeto> getProjetos() {
		return projetos;
	}

	public void setProjetos(List<Projeto> projetos) {
		this.projetos = projetos;
	}

	public List<InvoiceExp> getListaInvoices() {
		return listaInvoices;
	}

	public void setListaInvoices(List<InvoiceExp> listaInvoices) {
		this.listaInvoices = listaInvoices;
	}

	public InvoiceExp getInvoice() {
		return invoice;
	}

	public void setInvoice(InvoiceExp invoice) {
		this.invoice = invoice;
	}

	public ItemInvoiceExp getItem() {
		return item;
	}

	public void setItem(ItemInvoiceExp item) {
		this.item = item;
	}

	public List<Cidade> getCidades() {
		return cidades;
	}

	public void setCidades(List<Cidade> cidades) {
		this.cidades = cidades;
	}

	public ItemFollowup getItemFollow1() {
		return itemFollow1;
	}

	public void setItemFollow1(ItemFollowup itemFollow1) {
		this.itemFollow1 = itemFollow1;
	}

	public ItemFollowup getItemFollow2() {
		return itemFollow2;
	}

	public void setItemFollow2(ItemFollowup itemFollow2) {
		this.itemFollow2 = itemFollow2;
	}

	public Followup getFollowUp() {
		return followUp;
	}

	public void setFollowUp(Followup followUp) {
		this.followUp = followUp;
	}

	public List<SelectItem> getComboAtivo() {
		return comboAtivo;
	}

	public void setComboAtivo(List<SelectItem> comboAtivo) {
		this.comboAtivo = comboAtivo;
	}

}
