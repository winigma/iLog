package br.com.ilog.importacao.business.mbean;

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
import br.com.ilog.cadastro.business.entity.TipoPessoaEnum;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.geral.business.CodigoErroEspecifico;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;
import br.com.ilog.importacao.business.entity.Invoice;
import br.com.ilog.importacao.business.entity.StatusInvoice;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroInvoice;
import br.com.ilog.importacao.business.facade.ImportacaoFacade;
import br.com.ilog.seguranca.business.entity.TipoUsuario;
import br.com.ilog.seguranca.business.entity.Usuario;
import br.com.ilog.seguranca.business.facade.SegurancaFacade;

/**
 * 
 * @author Wisley Souza
 * @coment Bean de pesquisa que atua na fase de validação da invoice
 * 
 */

@Controller("mBeanPesquisarValidarInvoice")
@AccessScoped
public class MBeanPesquisarValidarInvoice extends AbstractPaginacao {

	private static final long serialVersionUID = 838286841332899795L;

	private static Logger logger = LoggerFactory
			.getLogger(MBeanPesquisarValidarInvoice.class);

	/**
	 * @coment fachada de importação
	 */
	@Resource(name = "controllerImportacao")
	ImportacaoFacade validarInvoiceFacade;

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
	 * @coment Combo de forneceddores
	 * 
	 */
	private List<PessoaJuridica> comboFornecedores;
	/**
	 * @coment Lista de forncedores
	 */
	private List<PessoaJuridica> fornecedores;
	/**
	 * @coment Converter de fornecedores
	 */
	private EntityConverter<PessoaJuridica> converterForncedor;

	/**
	 * @coment Combo de compradores
	 * 
	 */
	private List<Usuario> comboCompradores;
	/**
	 * @coment Lista de compradores
	 */
	private List<Usuario> compradores;
	/**
	 * @coment Converter de compradores
	 */
	private EntityConverter<Usuario> converterComprador;

	/**
	 * @coment Combo dos países de origem
	 * 
	 */
	private List<Pais> comboOrigens;
	/**
	 * @coment Lista com as origens
	 */
	private List<Pais> origens;
	/**
	 * @coment Converte de origens
	 */
	private EntityConverter<Pais> converterOrigem;

	/**
	 * @coment Combo de Status
	 */
	private List<SelectItem> comboStatus;

	/**
	 * @coment Filtro de Invoice
	 */
	private BasicFiltroInvoice filtro;

	/**
	 * @coment Objeto que contém a lista de invoices
	 */
	private List<Invoice> invoices;

	/**
	 * @coment Objeto de invoice para o detail.
	 */
	private Invoice invoiceDetail;

	/**
	 * @coment Enum respónsavel pela filtragem do tipo fornecedor
	 */
	TipoPessoaEnum tipo;

	/**
	 * metodo inicializador
	 */
	@SuppressWarnings("unused")
	@PostConstruct
	private void inicializar() {
		filtro = new BasicFiltroInvoice();
		
		//BasicFiltroInvoice fi =  new BasicFiltroInvoice();
		invoices = Collections.emptyList();
		compradores = Collections.emptyList();
		fornecedores = Collections.emptyList();
		origens = Collections.emptyList();
		invoiceDetail = new Invoice();
		
		setPaginaAtual(1);
		try {

			popularComboPais();
			popularForncedores();
			popularComboCompradores();
			popularComboStatus();
			doPesquisar(null);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void detailInvoice(Long idRegistro) {
		// Long idRegistro = id;
		invoiceDetail = new Invoice();
		try {
			invoiceDetail = (Invoice) validarInvoiceFacade.getInvoiceById(
					idRegistro, true);
		} catch (Exception e) {
			logger.error("erro: {}", e);
			e.printStackTrace();
		}

	}

	/**
	 * @brief Metodo que realiza o download de um arquivo a partir de um evento
	 *        do JSP
	 * @param ActionEvent
	 *            event - Evento da actionListener da pagina JSF
	 */
	public void baixarAnexo(int index) {

		try {
//			Invoice invoice = new Invoice();
//			invoice = invoices.get(index);
//			invoice = (Invoice) validarInvoiceFacade.getInvoiceByIdWithFile(
//					invoice.getId(), true, true);
//
//			if (invoice.getAnexo() != null) {
//
//				File file = new File();
//				file.setData(invoice.getAnexo().getAnexo());
//				file.setName(invoice.getAnexo().getNomeArquivo().replace(" ",
//						"_"));
//				file.setMime(invoice.getAnexo().getMimeType());
//
//				File.fileDonwload(file);
//
//			}
		} catch (Exception e) {
			BusinessException be = new BusinessException(
					CodigoErroEspecifico.MSG_ERRO_DONWLOAD_ARQUIVO);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, be));
		}
	}

	/**
	 * @coment método responsavel perlo style da linha do resgitros não
	 *         aprovados
	 * @param invoice
	 * @return
	 */

	public String getStyle(Invoice invoice) {
		if (invoice.getStatus().equals(StatusInvoice.NA)) {
			return "color:RED;";
		}
		return "";
	}

	/**
	 * @coment metodo que retorna verdadeiro se a invoice for diferente de
	 *         Cadastrada para desabilitar o check box
	 */

	public boolean getStatusCheckBox(Invoice invoice) {
		if (invoice.getStatus().equals(StatusInvoice.C)) {
			return false;
		}
		return true;
	}

	/**
	 * @coment Método de pesquisa
	 */
	@Override
	public void doPesquisar(ActionEvent arg0) {
		FacesContext fc = FacesContext.getCurrentInstance();
		ResourceBundle resourceBundle = TemplateMessageHelper
				.getResourceBundle(MensagensSistema.INVOICE, fc.getViewRoot()
						.getLocale());
		List<String> errorMessages = ValidatorHelper.valida(filtro,
				resourceBundle, resourceBundle);

		if (errorMessages.isEmpty()) {
			try {
				invoices = validarInvoiceFacade.listInvoiceOfValidate(filtro);
				if (invoices.isEmpty()) {
					String message = TemplateMessageHelper.getMessage(
							MensagensSistema.SISTEMA, "MSG008", fc
									.getViewRoot().getLocale());
					Messages.adicionaMensagemDeInfo(message);
				}
				setPaginaAtual(1);

			} catch (BusinessException e) {
				logger.error("error: {} " + e);
				e.printStackTrace();
				Messages.adicionaMensagemDeErro(TemplateMessageHelper
						.getMessage(MensagensSistema.SISTEMA, e));

			}
		} else {
			Messages.adicionaMensagensDeErro(errorMessages);

		}

	}

	public void popularComboStatus() {
		comboStatus = new ArrayList<SelectItem>();

		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			comboStatus.add(new SelectItem(StatusInvoice.C,
					TemplateMessageHelper.getMessage(MensagensSistema.INVOICE,
							StatusInvoice.C.name(), fc.getViewRoot()
									.getLocale())));
			comboStatus.add(new SelectItem(StatusInvoice.EV,
					TemplateMessageHelper.getMessage(MensagensSistema.INVOICE,
							StatusInvoice.EV.name(), fc.getViewRoot()
									.getLocale())));
			comboStatus.add(new SelectItem(StatusInvoice.NA,
					TemplateMessageHelper.getMessage(MensagensSistema.INVOICE,
							StatusInvoice.NA.name(), fc.getViewRoot()
									.getLocale())));
			comboStatus.add(new SelectItem(StatusInvoice.V,
					TemplateMessageHelper.getMessage(MensagensSistema.INVOICE,
							StatusInvoice.V.name(), fc.getViewRoot()
									.getLocale())));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// public MBeanPesquisarValidarInvoice(TipoPessoaEnum tipo) {
	// this.tipo = tipo;
	// }

	/**
	 * @coment Método que popula a combo de fornecedores
	 */
	public void popularForncedores() {
		comboFornecedores = new ArrayList<PessoaJuridica>();
		try {

			List<PessoaJuridica> fornecedorAux = cadastrotFacade
					.listarPessoasByTipo(TipoPessoaEnum.FORNEC);
			if (fornecedorAux != null) {
				comboFornecedores =  fornecedorAux;
				converterForncedor = new EntityConverter<PessoaJuridica>(
						fornecedorAux);
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	TipoUsuario tipoUser;

	/**
	 * @coment Método que popula o combo de compradores
	 */
	public void popularComboCompradores() {
		comboCompradores = new ArrayList<Usuario>();
		try {

			List<Usuario> compradoresAux = compradoresFacade
					.listarCompradores(null);
			if (compradoresAux != null) {
				comboCompradores = compradoresAux;
				converterComprador = new EntityConverter<Usuario>(
						compradoresAux);
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @coment Método que popula a combo de países
	 */

	public void popularComboPais() {
		// metodo que popula a como de paises na pagina de pesquisa...:)

		comboOrigens = new ArrayList<Pais>();
		try {
			List<Pais> paisesAux = cadastrotFacade.listarPaises(null);
			if (paisesAux != null) {
				comboOrigens =  paisesAux;
				converterOrigem = new EntityConverter<Pais>(paisesAux);
			}

		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @coment verifica o total de registros
	 */
	private List<Invoice> invoicesAux;

	public void selecionarInvoices() {
		try {
			invoicesAux = new ArrayList<Invoice>();
			for (Invoice invoice : this.invoices) {
				if (invoice.getSelect()) {
					invoicesAux.add(invoice);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @coment verifica o total de registros
	 */
	@Override
	public int getTotalRegistros() {
		if (invoices != null) {
			return invoices.size();
		} else
			return 0;
	}

	/**
	 * @coment método limpar
	 */
	@Override
	public void limpar() {
		setPaginaAtual(1);
		invoices.clear();
		filtro =  new BasicFiltroInvoice();

	}

	/**
	 * @coment método para refazer a pesquisa
	 */
	@Override
	public void refazerPesquisa() {
		if (filtro == null) {
			filtro = new BasicFiltroInvoice();
		}
		if (invoices == null) {
			return;
		}
		doPesquisar(null);

	}

	public List<PessoaJuridica> getComboFornecedores() {
		return comboFornecedores;
	}

	public void setComboFornecedores(List<PessoaJuridica> comboFornecedores) {
		this.comboFornecedores = comboFornecedores;
	}

	public List<Usuario> getComboCompradores() {
		return comboCompradores;
	}

	public void setComboCompradores(List<Usuario> comboCompradores) {
		this.comboCompradores = comboCompradores;
	}

	public List<Pais> getComboOrigens() {
		return comboOrigens;
	}

	public void setComboOrigens(List<Pais> comboOrigens) {
		this.comboOrigens = comboOrigens;
	}

	public List<SelectItem> getComboStatus() {
		return comboStatus;
	}

	public void setComboStatus(List<SelectItem> comboStatus) {
		this.comboStatus = comboStatus;
	}

	
	public List<PessoaJuridica> getFornecedores() {
		return fornecedores;
	}

	public void setFornecedores(List<PessoaJuridica> fornecedores) {
		this.fornecedores = fornecedores;
	}

	public EntityConverter<PessoaJuridica> getConverterForncedor() {
		return converterForncedor;
	}

	public void setConverterForncedor(
			EntityConverter<PessoaJuridica> converterForncedor) {
		this.converterForncedor = converterForncedor;
	}

	public List<Usuario> getCompradores() {
		return compradores;
	}

	public void setCompradores(List<Usuario> compradores) {
		this.compradores = compradores;
	}

	public EntityConverter<Usuario> getConverterComprador() {
		return converterComprador;
	}

	public void setConverterComprador(
			EntityConverter<Usuario> converterComprador) {
		this.converterComprador = converterComprador;
	}

	public List<Pais> getOrigens() {
		return origens;
	}

	public void setOrigens(List<Pais> origens) {
		this.origens = origens;
	}

	public EntityConverter<Pais> getConverterOrigem() {
		return converterOrigem;
	}

	public void setConverterOrigem(EntityConverter<Pais> converterOrigem) {
		this.converterOrigem = converterOrigem;
	}

	public List<Invoice> getInvoices() {
		return invoices;
	}

	public void setInvoices(List<Invoice> invoices) {
		this.invoices = invoices;
	}

	public Invoice getInvoiceDetail() {
		return invoiceDetail;
	}

	public void setInvoiceDetail(Invoice invoiceDetail) {
		this.invoiceDetail = invoiceDetail;
	}

	public List<Invoice> getInvoicesAux() {
		return invoicesAux;
	}

	public void setInvoicesAux(List<Invoice> invoicesAux) {
		this.invoicesAux = invoicesAux;
	}

	public BasicFiltroInvoice getFiltro() {
		return filtro;
	}

	public void setFiltro(BasicFiltroInvoice filtro) {
		this.filtro = filtro;
	}

}
