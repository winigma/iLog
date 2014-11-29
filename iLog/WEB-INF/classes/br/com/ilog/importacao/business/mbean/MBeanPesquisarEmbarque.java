package br.com.ilog.importacao.business.mbean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citspresentation.converter.EntityConverter;
import br.cits.commons.citspresentation.mbeans.AbstractPaginacao;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.Cidade;
import br.com.ilog.cadastro.business.entity.Pais;
import br.com.ilog.cadastro.business.entity.PessoaJuridica;
import br.com.ilog.cadastro.business.entity.Rota;
import br.com.ilog.cadastro.business.entity.TipoPessoaEnum;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;
import br.com.ilog.importacao.business.entity.Carga;
import br.com.ilog.importacao.business.entity.Invoice;
import br.com.ilog.importacao.business.entity.ItemInvoice;
import br.com.ilog.importacao.business.entity.ItemPO;
import br.com.ilog.importacao.business.entity.PO;
import br.com.ilog.importacao.business.entity.StatusCarga;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroCarga;
import br.com.ilog.importacao.business.facade.ImportacaoFacade;
import br.com.ilog.seguranca.business.entity.TipoUsuario;
import br.com.ilog.seguranca.business.entity.Usuario;
import br.com.ilog.seguranca.business.facade.SegurancaFacade;

/**
 * 
 * @author Heber Santiago
 * @coment Bean de pesquisa que atua na fase edicao de Embarque
 * 
 */

@Controller("mBeanPesquisarEmbarque")
@AccessScoped
public class MBeanPesquisarEmbarque extends AbstractPaginacao {

	private static final long serialVersionUID = 7757524520363693321L;
	private static Logger logger = LoggerFactory
			.getLogger(MBeanPesquisarEmbarque.class);

	/**
	 * @coment fachada de importação
	 */
	@Resource(name = "controllerImportacao")
	ImportacaoFacade facade;

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
	private List<SelectItem> comboFornecedores;
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
	private List<SelectItem> comboCompradores;
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
	 * @coment Combo dos países de destinos
	 * 
	 */
	private List<Pais> comboDestinos;
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
	private BasicFiltroCarga filtro;

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
	
	
	
	
	private List<Carga> cargas;
	private List<Cidade> comboCidades;
	private EntityConverter<Cidade> cidadeConverter;
	
	@SuppressWarnings("unused")
	@PostConstruct
	private void inicializar() {
		filtro = new BasicFiltroCarga();
		filtro.setInvoice( new Invoice() );
		filtro.setItemInvoice(new ItemInvoice());
		filtro.setRota(new Rota());
		filtro.setCidadeAtual( new Cidade() );
		filtro.getItemInvoice().setItemPO( new ItemPO() );
		filtro.getItemInvoice().getItemPO().setPo( new PO() );
		
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
			popularCidades();
			doPesquisar(null);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	/**
	 * @coment Método que constroi o relátorio em excel
	
	public void exportRelatorioExcel( ActionEvent arg ) {
          ExportCargasExcel export =  new ExportCargasExcel();
          if(cargas.isEmpty()){
        	  String mensagem = "Nao ha registros";
              Messages.adicionaMensagemDeInfo(mensagem);
        	  
          }else{
        	  try{
        		  this.doPesquisar(null);
        		  export.exportarRelatorioExcel(cargas, filtro.getDtInicioColeta(), filtro.getDtFimColeta());
        	  }catch (IOException  e) {
				e.printStackTrace();
			}
          }
	}
	 */
	
	

	public void popularComboStatus() {
		comboStatus = new ArrayList<SelectItem>();

		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			
			for (StatusCarga status : StatusCarga.getValores()) {
				comboStatus.add(new SelectItem(status,
						TemplateMessageHelper.getMessage(MensagensSistema.CARGA,
								status.name(),
								fc.getViewRoot().getLocale() ) ) );
				
			}

				} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @coment Método que popula a combo de fornecedores
	 */
	public void popularForncedores() {
		comboFornecedores = new ArrayList<SelectItem>();
		try {

			List<PessoaJuridica> fornecedorAux = cadastrotFacade
					.listarPessoasByTipo(TipoPessoaEnum.FORNEC);
			if (fornecedorAux != null) {
				for (PessoaJuridica pessoaJuridica : fornecedorAux) {
					comboFornecedores.add(new SelectItem(pessoaJuridica,
							pessoaJuridica.getNomeFantasia()));
				}
				converterForncedor = new EntityConverter<PessoaJuridica>(
						fornecedorAux);
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	TipoUsuario tipoUser;

	/**
	 * @coment Método que popula o combo de compradores
	 */
	public void popularComboCompradores() {
		comboCompradores = new ArrayList<SelectItem>();
		try {

			List<Usuario> compradoresAux = compradoresFacade
					.listarCompradores(null);
			if (compradoresAux != null) {
				for (Usuario usuario : compradoresAux) {
					comboCompradores.add(new SelectItem(usuario, usuario
							.getNome()));
				}
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
		comboDestinos =  new ArrayList<Pais>();
		try {
			List<Pais> paisesAux = cadastrotFacade.listarPaises(null);
			if (paisesAux != null) {
				
				comboOrigens =  paisesAux;
				comboDestinos = paisesAux;
				converterOrigem = new EntityConverter<Pais>(paisesAux);
			}

		} catch (BusinessException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	  
	
	
	

	@Override
	public void doPesquisar(ActionEvent arg0) {
		FacesContext fc = FacesContext.getCurrentInstance();

		try {
			cargas = facade.listCargasConsolidadas(filtro);
			if (cargas.isEmpty()) {
				String message = TemplateMessageHelper.getMessage(MensagensSistema.SISTEMA,"MSG008",fc.getViewRoot().getLocale());
				Messages.adicionaMensagemDeInfo(message);
			}
			//setPaginaAtual(1);
		} catch (BusinessException e) {
			logger.error("error: {} " + e);
			e.printStackTrace();
			Messages.adicionaMensagemDeErro(TemplateMessageHelper
					.getMessage(MensagensSistema.SISTEMA, e));
		}
		
	}

	/**
	 * Popular o combo de cidades.
	 */
	public void popularCidades() {
		comboCidades = new ArrayList<Cidade>();
		try {
			comboCidades = cadastrotFacade.listarCidades(null);
			
			cidadeConverter = new EntityConverter<Cidade>( comboCidades );
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Verifica se carrega o combo de cidades
	 * @return
	 */
	public boolean carregarCidades() {
		if ( StatusCarga.ITT.equals( filtro.getStatus() ) 
				|| StatusCarga.OHI.equals( filtro.getStatus() ) )
			return true;
		return false;
	}
	
	@Override
	public int getTotalRegistros() {
		if (cargas != null) {
			return cargas.size();
		} else
			return 0;
	}

	/**
	 * @coment método limpar
	 */
	@Override
	public void limpar() {
		
		filtro = new BasicFiltroCarga();
		filtro.setInvoice( new Invoice() );
		filtro.setItemInvoice(new ItemInvoice());
		filtro.getItemInvoice().setItemPO(new ItemPO());
		filtro.getItemInvoice().getItemPO().setPo(new PO());
		filtro.setRota(new Rota());
		filtro.setCidadeAtual( new Cidade() );
		
		invoices = Collections.emptyList();
		compradores = Collections.emptyList();
		fornecedores = Collections.emptyList();
		origens = Collections.emptyList();
		invoiceDetail = new Invoice();
		
		cargas.clear();
		

	}

	/**
	 * @coment método para refazer a pesquisa
	 */
	@Override
	public void refazerPesquisa() {
		if (filtro == null) {
			filtro = new BasicFiltroCarga();
		}
		if (invoices == null) {
			return;
		}
		doPesquisar(null);

	}
	
	/**
	 * Retorna a Cidade onde se encontra a carga, dependendo do Status
	 * @param index
	 * @return
	 */
	public String getCidadeAtual( int index ) {
		
		Carga carga = cargas.get(index);
		if ( carga.getStatus() != null && ( carga.getStatus().equals(StatusCarga.OHI)
				|| carga.getStatus().equals(StatusCarga.ITT) ) ) {
			if ( carga.getCidadeAtual() != null ) {
				return carga.getCidadeAtual().getSigla().toUpperCase();
			}
		}
		
		return "";
	}

	public List<SelectItem> getComboFornecedores() {
		return comboFornecedores;
	}

	public void setComboFornecedores(List<SelectItem> comboFornecedores) {
		this.comboFornecedores = comboFornecedores;
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

	public List<SelectItem> getComboCompradores() {
		return comboCompradores;
	}

	public void setComboCompradores(List<SelectItem> comboCompradores) {
		this.comboCompradores = comboCompradores;
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

	public List<Pais> getComboOrigens() {
		return comboOrigens;
	}

	public void setComboOrigens(List<Pais> comboOrigens) {
		this.comboOrigens = comboOrigens;
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

	public List<SelectItem> getComboStatus() {
		return comboStatus;
	}

	public void setComboStatus(List<SelectItem> comboStatus) {
		this.comboStatus = comboStatus;
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

	public List<Carga> getCargas() {
		return cargas;
	}

	public void setCargas(List<Carga> cargas) {
		this.cargas = cargas;
	}

	public BasicFiltroCarga getFiltro() {
		return filtro;
	}

	public void setFiltro(BasicFiltroCarga filtro) {
		this.filtro = filtro;
	}

	public List<Pais> getComboDestinos() {
		return comboDestinos;
	}

	public void setComboDestinos(List<Pais> comboDestinos) {
		this.comboDestinos = comboDestinos;
	}


	public List<Cidade> getComboCidades() {
		return comboCidades;
	}


	public void setComboCidades(List<Cidade> comboCidades) {
		this.comboCidades = comboCidades;
	}


	public EntityConverter<Cidade> getCidadeConverter() {
		return cidadeConverter;
	}


	public void setCidadeConverter(EntityConverter<Cidade> cidadeConverter) {
		this.cidadeConverter = cidadeConverter;
	}

}
