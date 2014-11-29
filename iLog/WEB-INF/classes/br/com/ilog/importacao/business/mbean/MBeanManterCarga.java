package br.com.ilog.importacao.business.mbean;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
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
import br.cits.commons.citspresentation.converter.EntityConverter;
import br.cits.commons.citspresentation.mbeans.AbstractManter;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.commons.citspresentation.util.JSFRequestBean;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.Cidade;
import br.com.ilog.cadastro.business.entity.Pais;
import br.com.ilog.cadastro.business.entity.PessoaJuridica;
import br.com.ilog.cadastro.business.entity.Projeto;
import br.com.ilog.cadastro.business.entity.Rota;
import br.com.ilog.cadastro.business.entity.TipoPessoa;
import br.com.ilog.cadastro.business.entity.TipoPessoaEnum;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroCidade;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroPessoaJuridica;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroRota;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.cadastro.presentation.util.ConverterUtil;
import br.com.ilog.cadastro.presentation.util.ExceptionFiltro;
import br.com.ilog.geral.business.CodigoErroEspecifico;
import br.com.ilog.geral.presentation.Constantes;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;
import br.com.ilog.importacao.business.entity.Carga;
import br.com.ilog.importacao.business.entity.Invoice;
import br.com.ilog.importacao.business.entity.ProcBroker;
import br.com.ilog.importacao.business.entity.StatusCarga;
import br.com.ilog.importacao.business.entity.StatusInvoice;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroInvoice;
import br.com.ilog.importacao.business.facade.ImportacaoFacade;
import br.com.ilog.seguranca.business.entity.StatusUsuario;
import br.com.ilog.seguranca.business.entity.Usuario;
import br.com.ilog.seguranca.business.facade.SegurancaFacade;

/**
 * 
 * @author Wisley Souza
 * @coment Bean de pesquisa que atua na fase de consolidacao das invoice
 * 
 */

/**
 * @author cits
 *
 */
@Controller("mBeanManterCarga")
@AccessScoped
public class MBeanManterCarga extends AbstractManter {

	private static final long serialVersionUID = -7643749988727933526L;
	private static Logger logger = LoggerFactory
			.getLogger(MBeanManterCarga.class);

	/**
	 * @coment fachada de importação
	 */
	@Resource(name = "controllerImportacao")
	ImportacaoFacade consolidarInvoiceFacade;

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

	private Boolean novaConsolidacao;

	private List<Usuario> importadores;
	private ConverterUtil<Usuario> importadoresConverter;

	/**
	 * @coment dados de agente de cargas
	 */
	private BasicFiltroPessoaJuridica filtroAgente;
	private List<PessoaJuridica> comboAgentes;
	private EntityConverter<PessoaJuridica> converterAgente;
	
	private List<ProcBroker> comboProcBroker;
	
	/**
	 * @coment atributos para rotas
	 */
	private List<Rota> listRotas;
	private Rota rota;
	private List<Cidade> comboCidadesOrigem;
	private ConverterUtil<Cidade> converterCidadeOrigem;
	private Pais paisDestinoAnterior;
	private List<Pais> comboPaisesOrigem;
	private ConverterUtil<Pais> converterPaisOrigem;
	private List<Pais> comboPaisesDestino;
	private EntityConverter<Pais> converterPaisDestino;
	private List<Cidade> comboCidadesDestino;
	private EntityConverter<Cidade> converterCidadeDestino;
	private BasicFiltroRota filtroRota;
	private List<SelectItem> comboFornecedores;
	private EntityConverter<PessoaJuridica> converterForncedor;

	/**
	 * fim dos atributos de rotas..
	 */

	/**
	 * @coment Atributos de Invoices
	 */
	private Invoice invoice;
	private List<Invoice> listInvoices;
	private List<SelectItem> comboOrigens;
	private List<SelectItem> comboStatus;
	private EntityConverter<Pais> converterOrigem;
	private List<SelectItem> comboCompradores;
	private EntityConverter<Usuario> converterComprador;
	private List<Invoice> listaInvoices;
	private List<Invoice> listaInvoicesRemovidas;

	// string que vai carregar quando rota nao for selecionado
	private String msgRota;

	private List<Projeto> projetos;
	private Integer paginaAtualProjetos;

	private Integer paginaAtualInvoicesCarga;

	// invoices selecionadas

	private List<Invoice> selectedInvoices;

	/**
	 * fim dos atributos de invoices..
	 */

	@PostConstruct
	void inicializar() {

		inicializarObjetos();

		listInvoices = new ArrayList<Invoice>();
		listaInvoices = new ArrayList<Invoice>();
		listaInvoicesRemovidas = new ArrayList<Invoice>();
		listRotas = Collections.emptyList();
		filtroAgente = new BasicFiltroPessoaJuridica();
		filtroRota = new BasicFiltroRota();
		filtroInvoices = new BasicFiltroInvoice();
		novaConsolidacao = true;
		cidadeFilter = new Cidade();
		capRota = new Rota();
		origem = new Pais();

		// zerando a msg
		msgRota = "";

		try {
			popularComboAgentes();
			this.popularPaisDestinoOrigem();
			// this.popularCidadeOrigem();
			// this.popularCidadeDestino();
			popularComboPais();
			popularForncedores();
			popularComboCompradores();
			popularComboImportadores();

			// listRotas = cadastrotFacade.listarRotas(null);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Calculo segundo regra de negocio [RN060]
	 */
	public void calcularFrete() {
		
		BigDecimal ptc = BigDecimal.ZERO;
		BigDecimal total = BigDecimal.ZERO;
		
		if ( carga.getPesoBrutoHawb() != null || carga.getPesoCubadoHawb() != null ) {
			if ( carga.getPesoBrutoHawb() == null && carga.getPesoCubadoHawb() != null ) {
				ptc = carga.getPesoCubadoHawb();
			} else if ( carga.getPesoBrutoHawb() != null && carga.getPesoCubadoHawb() == null ) {
				ptc = carga.getPesoBrutoHawb();
			} else {
				if ( carga.getPesoBrutoHawb().compareTo( carga.getPesoCubadoHawb() ) <= 0 ) {
					ptc = carga.getPesoCubadoHawb();
				} else {
					ptc = carga.getPesoBrutoHawb();
				}
			}
		}
		BigDecimal mult = BigDecimal.ZERO;
		if ( carga.getValorPesoImposto() != null ) {
			mult = ptc.multiply( carga.getValorPesoImposto() );
			total = total.add( mult );
		}
		if ( carga.getValorTaxaFrete() != null ) {
			mult = ptc.multiply( carga.getValorTaxaFrete() );
			total = total.add( mult );
		}
		if ( carga.getValorFSC() != null ) {
			mult = ptc.multiply( carga.getValorFSC() );
			total = total.add( mult );
		}
		if ( carga.getValorSEC() != null ) {
			mult = ptc.multiply( carga.getValorSEC() );
			total = total.add( mult );
		}
		if ( carga.getValorISS() != null ) {
			mult = ptc.multiply( carga.getValorISS() );
			total = total.add( mult );
		}
		if ( carga.getValorAMS() != null ) {
			total = total.add( carga.getValorAMS() );
		}
		if ( carga.getValorPickUp() != null ) {
			total = total.add( carga.getValorPickUp() );
		}
		if ( carga.getValorPSS() != null ) {
			total = total.add( carga.getValorPSS() );
		}
		
		carga.setValorFrete( total.setScale(5, RoundingMode.HALF_UP) );
	}
	
	public String salvarNovo() {
		try {
			if (this.salvar() != null) {
				return this.novaConsolidacao();

			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * @coment Método para uma nova consolidação
	 * @return Nova consolidacao
	 */
	public String novaConsolidacao() {

		inicializarObjetos();
		novaConsolidacao = true;
		edicao = false;
		return "carga.jsf";
	}

	/**
	 * @brief Metodo que realiza o download de um arquivo a partir de um evento
	 *        do JSP
	 * @param ActionEvent
	 *            event - Evento da actionListener da pagina JSF
	 */
	public void baixarAnexo(int index) {

		try {
			// Invoice invoice = new Invoice();
			// invoice = listaInvoices.get(index);
			// invoice = (Invoice)
			// consolidarInvoiceFacade.getInvoiceByIdWithFile(
			// invoice.getId(), true, true);

			// if (invoice.getAnexo() != null) {
			//
			// File file = new File();
			// file.setData(invoice.getAnexo().getAnexo());
			// file.setName(invoice.getAnexo().getNomeArquivo().replace(" ",
			// "_"));
			// file.setMime(invoice.getAnexo().getMimeType());
			//
			// File.fileDonwload(file);
			//
			// }
		} catch (Exception e) {
			BusinessException be = new BusinessException(
					CodigoErroEspecifico.MSG_ERRO_DONWLOAD_ARQUIVO);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, be));
		}
	}

	public String cancelarConsolidacao() {

		try {

			this.carga.setCritico(false);
			this.carga.setStatus(StatusCarga.C);

			// this.carga.setDtConsolidacao(new Date());

			ResourceBundle resource = TemplateMessageHelper.getResourceBundle(
					MensagensSistema.CARGA, JSFRequestBean.getLocale());
			List<String> erros = ValidatorHelper.valida(this.carga, resource);
			if (erros.isEmpty()) {
				if (edicao) {
					consolidarInvoiceFacade.alterarCarga(this.carga);
					if (!listaInvoices.isEmpty()) {
						for (Invoice item : listaInvoices) {
							listaInvoicesRemovidas.add(item);
						}
					}
					if (!listaInvoicesRemovidas.isEmpty()) {
						listaInvoices.removeAll(listaInvoicesRemovidas);

						for (Invoice item : listaInvoicesRemovidas) {
							item.setCarga(null);
							item.setStatus(StatusInvoice.A);
							consolidarInvoiceFacade.alterarInvoice(item);

						}
					}

				}
			} else {
				Messages.adicionaMensagensDeErro(erros);
				return null;
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		FacesContext fc = FacesContext.getCurrentInstance();
		String message = TemplateMessageHelper.getMessage(
				MensagensSistema.SISTEMA, "MSG001", fc.getViewRoot()
						.getLocale());
		Messages.adicionaMensagemDeInfo(message);
		refazerPesquisa();
		return Constantes.SALVAR;
	}

	/**
	 * @coment Métodos para captura das invoices a serem consolidadas
	 */

	public void removeInvoice() {
		try {
			for (Invoice item : listaInvoices) {
				if (item.getSelect()) {
					listaInvoicesRemovidas.add(item);

				}
			}
			if (!listaInvoicesRemovidas.isEmpty()) {
				listaInvoices.removeAll(listaInvoicesRemovidas);

			}

			atualizarProjetos();

		} catch (Exception e) {
			e.printStackTrace();
		}
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
			setPaginaAtualInvoicesCarga(1);

			atualizarProjetos();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private BasicFiltroInvoice filtroInvoices;

	public void getInvoices() {
		listInvoices = new ArrayList<Invoice>();
		try {
			FacesContext fc = FacesContext.getCurrentInstance();

			// filtroInvoices.setStatusInvoice(StatusInvoice.V);
			listInvoices = consolidarInvoiceFacade
					.listarInvoices(filtroInvoices);
			if (listInvoices.isEmpty()) {
				String msg = TemplateMessageHelper.getMessage(
						MensagensSistema.SISTEMA, "MSG008", fc.getViewRoot()
								.getLocale());
				Messages.adicionaMensagemDeInfo(msg);
			} else {
				getScrollerInfoInvoices();
			}
			setPaginaAtualInvoices(1);
			if (listaInvoices != null && !listaInvoices.isEmpty())
				listInvoices.removeAll(listaInvoices);
			filtroInvoices = new BasicFiltroInvoice();
		} catch (BusinessException e) {
			e.printStackTrace();

		}

	}

	public int getTotalRegistrosInvoices() {
		if (listInvoices != null)
			return listInvoices.size();
		return 0;
	}

	public int getTotalRegistrosProjetos() {
		if (projetos != null)
			return projetos.size();
		return 0;
	}

	public int getTotalRegistrosInvoicesCarga() {
		if (listaInvoices != null)
			return listaInvoices.size();
		return 0;
	}

	public String getScrollerInfoInvoices() {

		int regInicial = (this.getPaginaAtualInvoices() - 1) * 5 + 1;
		int regFinal = Math.min(regInicial + 5 - 1,
				this.getTotalRegistrosInvoices());

		String retorno = "";
		if (regFinal != 0)
			retorno = regInicial + " - " + regFinal + " "
					+ TemplateMessageHelper.getMessage("lblDe") + " "
					+ this.getTotalRegistrosInvoices();

		return retorno;
	}

	public String getScrollerInfoProjetos() {

		int regInicial = (this.getPaginaAtualProjetos() - 1) * 10 + 1;
		int regFinal = Math.min(regInicial + 10 - 1,
				this.getTotalRegistrosProjetos());

		String retorno = "";
		if (regFinal != 0)
			retorno = regInicial + " - " + regFinal + " "
					+ TemplateMessageHelper.getMessage("lblDe") + " "
					+ this.getTotalRegistrosProjetos();

		return retorno;
	}

	public String getScrollerInfoInvoicesCarga() {

		int regInicial = (this.getPaginaAtualInvoicesCarga() - 1) * 10 + 1;
		int regFinal = Math.min(regInicial + 10 - 1,
				this.getTotalRegistrosInvoicesCarga());

		String retorno = "";
		if (regFinal != 0)
			retorno = regInicial + " - " + regFinal + " "
					+ TemplateMessageHelper.getMessage("lblDe") + " "
					+ this.getTotalRegistrosInvoicesCarga();

		return retorno;
	}

	private Integer paginaAtualInvoices;

	public Integer getPaginaAtualInvoices() {
		if (paginaAtualInvoices != null)
			return this.paginaAtualInvoices;
		return 0;
	}

	public Integer getPaginaAtualProjetos() {
		if (paginaAtualProjetos != null)
			return this.paginaAtualProjetos;
		return 0;
	}

	public void setPaginaAtualInvoices(Integer paginaAtualInvoices) {
		this.paginaAtualInvoices = paginaAtualInvoices;
	}

	public void popularComboProcBroker() {
		comboProcBroker = new ArrayList<ProcBroker>();
		
		try {
			comboProcBroker = consolidarInvoiceFacade.listarBrokerSemCarga( carga );
			
		} catch (BusinessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void popularComboStatus() {
		comboStatus = new ArrayList<SelectItem>();

		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			
			if ( disabledStatus() ) {
				
				for ( StatusCarga status : StatusCarga.getValores() ) {
					comboStatus.add(new SelectItem(status,
							TemplateMessageHelper.getMessage(MensagensSistema.CARGA,
									status.name(),
									fc.getViewRoot().getLocale() ) ) );
					
				}
			} else {
				for ( StatusCarga status : StatusCarga.getValuesPsqEmbarque() ) {
					comboStatus.add(new SelectItem(status,
							TemplateMessageHelper.getMessage(MensagensSistema.CARGA,
									status.name(),
									fc.getViewRoot().getLocale() ) ) );
					
				}
				
			}
			

				} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void popularComboPais() {
		// metodo que popula a como de paises na pagina de pesquisa...:)

		comboOrigens = new ArrayList<SelectItem>();
		try {
			List<Pais> paisesAux = cadastrotFacade.listarPaises(null);
			if (paisesAux != null) {
				for (Pais pais : paisesAux) {
					comboOrigens.add(new SelectItem(pais, pais.getNome()));
				}
				converterOrigem = new EntityConverter<Pais>(paisesAux);
			}

		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

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

	private void popularComboImportadores() {
		importadores = new ArrayList<Usuario>();

		try {
			List<Usuario> users = compradoresFacade
					.listarImportadores(StatusUsuario.A);
			if (users != null)
				importadores = users;
			importadoresConverter = new ConverterUtil<Usuario>(users);

		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

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

	/**
	 * Fim dos metos de invoice
	 */

	/**
	 * @coment Método de pesquisa de rotas e captura de objetos de rotas...
	 * 
	 */

	public String getContato() {
		try {
			if (carga.getAgenteCarga().getContatos() != null
					&& !carga.getAgenteCarga().getContatos().isEmpty()) {

				return carga.getAgenteCarga().getContatos().get(0)
						.getNomeResponsavel();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Sem contato";
	}

	public String getEnderecoAgente() {
		try {
			if (carga.getAgenteCarga().getEnderecos() != null
					&& !carga.getAgenteCarga().getEnderecos().isEmpty()) {

				return carga.getAgenteCarga().getEnderecos().get(0).getRua()
						+ ", "
						+ carga.getAgenteCarga().getEnderecos().get(0)
								.getNumero()
						+ ", "
						+ carga.getAgenteCarga().getEnderecos().get(0)
								.getPais().getNome();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public String getFoneContato() {
		try {
			if (carga.getAgenteCarga().getContatos() != null
					&& !carga.getAgenteCarga().getContatos().isEmpty()) {

				return carga.getAgenteCarga().getContatos().get(0)
						.getTelefone();
			}
		} catch (Exception e) {
		}
		return "";
	}

	public Pais origem;

	public void popularPaisDestinoOrigem() {
		comboPaisesDestino = new ArrayList<Pais>();
		comboPaisesOrigem = new ArrayList<Pais>();
		try {
			List<Pais> paises = cadastrotFacade.listarPaises();

			comboPaisesOrigem = paises;
			comboPaisesDestino = paises;

			converterPaisDestino = new EntityConverter<Pais>(paises);
			converterPaisOrigem = new ConverterUtil<Pais>(paises);

		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param arg
	 */
	public void popularCidadeOrigem() {
		comboCidadesOrigem = new ArrayList<Cidade>();
		System.out.println(origem.getNome());
		try {
			if (filtroRota.getPaisOrigem() != null
					&& filtroRota.getPaisOrigem().getId() != null) {

				paisDestinoAnterior = filtroRota.getPaisOrigem();
				List<Cidade> cidades = cadastrotFacade
						.listarCidades(new BasicFiltroCidade(filtroRota
								.getPaisOrigem(), null));
				comboCidadesOrigem = cidades;
				converterCidadeOrigem = new ConverterUtil<Cidade>(cidades);
			}

		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param arg
	 */
	public void popularCidadeDestino() {
		comboCidadesDestino = new ArrayList<Cidade>();

		try {
			if (filtroRota.getPaisDestino() != null
					&& filtroRota.getPaisDestino().getId() != null) {
				List<Cidade> cidades = cadastrotFacade
						.listarCidades(new BasicFiltroCidade(filtroRota
								.getPaisDestino(), null));
				comboCidadesDestino = cidades;

				converterCidadeDestino = new EntityConverter<Cidade>(cidades);
			}

		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	private Long selectId;

	private Rota rotaAux;

	public void carregarTrechos() {
		rotaAux = new Rota();
		try {
			rotaAux = cadastrotFacade.getRotaById(selectId);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		// rotaAux.getTrechos();
	}

	public void capturar() {
		try {
			this.rota = new Rota();
			// this.rota = listRotas.get(index);
			// Long id =new Long(6);
			this.rota = cadastrotFacade.getRotaById(capRota.getId());
			carga.setAgenteCarga(this.rota.getAgenteCarga());
			this.carregarAgenteCarga();
			msgRota = "";
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Rota capRota;

	private Cidade cidadeFilter;

	public void getRotas() {
		try {
			// BasicFiltroRota filtroRota = new BasicFiltroRota();
			FacesContext fc = FacesContext.getCurrentInstance();
			listRotas = new ArrayList<Rota>();

			// filtroRota.setAgenteCargas(carga.getAgenteCarga());

			this.filtroRota.setAtivo(true);
			if (this.carga.getAgenteCarga() != null) {
				carga.setAgenteCarga(cadastrotFacade.getPessoaById(carga.getAgenteCarga().getId()));
				filtroRota.setAgenteCargas(this.carga.getAgenteCarga());
			}
			listRotas = cadastrotFacade.listarRotas(filtroRota);

			if (listRotas.isEmpty()) {
				String msg = TemplateMessageHelper.getMessage(
						MensagensSistema.SISTEMA, "MSG008", fc.getViewRoot()
								.getLocale());
				Messages.adicionaMensagemDeInfo(msg);
			} else {
				getScrollerInfoRotas();
			}
			setPaginaAtualRotas(1);
			rotaAux = new Rota();
			filtroRota = new BasicFiltroRota();
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	private Integer paginaAtualRotas;
	private byte registrosPagina = 5;

	public int getTotalRegistrosRotas() {
		if (listRotas != null)
			return listRotas.size();
		return 0;
	}

	public String getScrollerInfoRotas() {

		int regInicial = (this.getPaginaAtualRotas() - 1) * 5 + 1;
		int regFinal = Math.min(regInicial + 5 - 1,
				this.getTotalRegistrosRotas());

		String retorno = "";
		if (regFinal != 0)
			retorno = regInicial + " - " + regFinal + " "
					+ TemplateMessageHelper.getMessage("lblDe") + " "
					+ this.getTotalRegistrosRotas();

		return retorno;
	}

	public Integer getPaginaAtualRotas() {
		if (paginaAtualRotas != null)
			return this.paginaAtualRotas;
		return 0;
	}

	public void setPaginaAtualRotas(Integer paginaAtuaRotas) {
		this.paginaAtualRotas = paginaAtuaRotas;
	}

	public byte getRegistrosPagina() {
		return registrosPagina;
	}

	public void setRegistrosPagina(byte registrosPagina) {
		this.registrosPagina = registrosPagina;
	}

	/**
	 * Fim do métodos a respeito de rota
	 */

	@Override
	public String salvar() {
		FacesContext fc = FacesContext.getCurrentInstance();
		
		calcularFrete();
		
		List<String> erros = new ArrayList<String>();
		
		if ( carga.getDtColeta() == null ) {
			erros.add( TemplateMessageHelper.getMessage(MensagensSistema.CARGA,
					"lblDtColetaObrigatoria", fc.getViewRoot().getLocale()) );
			
		}
		if (this.rota != null)
			this.carga.setRota(this.rota);
		else {
			erros.add( TemplateMessageHelper.getMessage(MensagensSistema.CARGA,
					"lblRotaObrigatoria", fc.getViewRoot().getLocale() ) );
			
		}

		ResourceBundle resource = TemplateMessageHelper.getResourceBundle(
				MensagensSistema.CARGA, JSFRequestBean.getLocale());

		erros.addAll( ValidatorHelper
				.valida(this.carga,
						TemplateMessageHelper.getResourceBundle(
								MensagensSistema.SISTEMA, fc.getViewRoot()
										.getLocale()), resource) );

		if (erros.isEmpty()) {
			try {

				if (edicao) {
					consolidarInvoiceFacade.alterarCarga(this.carga);
				}
				
			} catch (BusinessException e) {
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
		//refazerPesquisa();
		
		return "cargas.jsf";
	}

	@Override
	public String editar() {
		edicao = true;
		novaConsolidacao = false;
		Long id = selectCarga.getId();
		
		this.rota = null;
		
		try {
			carga = (Carga) consolidarInvoiceFacade.getCargaById(id);
			
			if ( carga.getRota() != null )
				this.rota = (Rota) cadastrotFacade.getRotaById( carga.getRota().getId() );
			
			carga = consolidarInvoiceFacade.carregarProcBroker( carga );
			
			if (carga.getImportador() != null)
				carga.setImportador(compradoresFacade.getUsuarioById(carga
						.getImportador().getId()));

			listaInvoices = consolidarInvoiceFacade
					.listarInvoicesByCarga(this.carga);
			if (!listaInvoices.isEmpty()) {
				setPaginaAtualInvoicesCarga(1);

				projetos = cadastrotFacade
						.listarProjetosPorInvoices(listaInvoices);
				if (!projetos.isEmpty())
					setPaginaAtualProjetos(1);
			}
			
			popularComboStatus();
			
			popularComboProcBroker();
			
			calcularFrete();
			
			// this.getContato();
		} catch (BusinessException e) {
			logger.error("erro: {}", e);
			e.printStackTrace();
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.CARGA, e));
		}

		return "carga.jsf";
	}

	
	/**
	 * Verifica de desabilida a edicao do status.
	 * @return
	 */
	public boolean disabledStatus() {
		if ( carga.getStatus().equals( StatusCarga.AT ) || 
				carga.getStatus().equals( StatusCarga.ITT ) ||
				carga.getStatus().equals( StatusCarga.ICC ) ||
				carga.getStatus().equals( StatusCarga.F ) || 
				carga.getStatus().equals( StatusCarga.C ) ) {
			return true;
		}
		return false;
	}
	
	/**
	 * Verificar se carrega combo de cidade.
	 * @return
	 */
	public boolean carregarCidades() {
		if (StatusCarga.ITT.equals(carga.getStatus())
				|| StatusCarga.OHI.equals(carga.getStatus()))
			return true;
		return false;
	}
	
	/**
	 * Metodo atualiza os projetos das invoices selecionadas
	 * 
	 * @throws BusinessException
	 */
	private void atualizarProjetos() throws BusinessException {
		projetos = new ArrayList<Projeto>();

		if (!listaInvoices.isEmpty()) {
			projetos = cadastrotFacade.listarProjetosPorInvoices(listaInvoices);
			if (!projetos.isEmpty()) {
				setPaginaAtualProjetos(1);
			}
		}
	}

	private void popularComboAgentes() {
		comboAgentes = new ArrayList<PessoaJuridica>();
		try {
			filtroAgente.setAtivo(true);
			TipoPessoa tp = new TipoPessoa();
			tp.setTipo(TipoPessoaEnum.A_CARGA.name());
			filtroAgente.setPessoa(tp);
			List<PessoaJuridica> agenteAux = cadastrotFacade
					.listarByFilter(filtroAgente);
			if (agenteAux != null) {
				comboAgentes = agenteAux;
				converterAgente = new EntityConverter<PessoaJuridica>(agenteAux);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String excluir() {
		return null;
	}

	@Override
	public void inicializarObjetos() {
		carga = new Carga();
		carga.setVisivel(false);
		rota = null;
		// carga.setInvoices(new ArrayList<Invoice>());
		listaInvoices = new ArrayList<Invoice>();
		// rota = new Rota();
		listInvoices = new ArrayList<Invoice>();
	}

	/**
	 * refazer pesquisa
	 */
	@Override
	protected void refazerPesquisa() {
		MBeanPesquisarEmbarque mBean = (MBeanPesquisarEmbarque) JSFRequestBean
				.getManagedBean("mBeanPesquisarCarga");
		mBean.refazerPesquisa();
	}

	/**
	 * Metodo para inicializar dados
	 * 
	 * @throws BusinessException
	 */
	private void carregarAgenteCarga() throws BusinessException {
		if (carga.getAgenteCarga() != null) {
			PessoaJuridica agente = cadastrotFacade.getPessoaById(carga
					.getAgenteCarga().getId());
			carga.setAgenteCarga(agente);
		}
	}

	public Carga getCarga() {
		return carga;
	}

	public void setCarga(Carga carga) {
		this.carga = carga;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public List<Invoice> getListInvoices() {
		return listInvoices;
	}

	public void setListInvoices(List<Invoice> listInvoices) {
		this.listInvoices = listInvoices;
	}

	public Boolean getNovaConsolidacao() {
		return novaConsolidacao;
	}

	public void setNovaConsolidacao(Boolean novaConsolidacao) {
		this.novaConsolidacao = novaConsolidacao;
	}

	public List<PessoaJuridica> getComboAgentes() {
		return comboAgentes;
	}

	public void setComboAgentes(List<PessoaJuridica> comboAgentes) {
		this.comboAgentes = comboAgentes;
	}

	public EntityConverter<PessoaJuridica> getConverterAgente() {
		return converterAgente;
	}

	public void setConverterAgente(
			EntityConverter<PessoaJuridica> converterAgente) {
		this.converterAgente = converterAgente;
	}

	public BasicFiltroPessoaJuridica getFiltroAgente() {
		return filtroAgente;
	}

	public void setFiltroAgente(BasicFiltroPessoaJuridica filtroAgente) {
		this.filtroAgente = filtroAgente;
	}

	public List<Rota> getListRotas() {
		return listRotas;
	}

	public void setListRotas(List<Rota> listRotas) {
		this.listRotas = listRotas;
	}

	public Rota getRota() {
		return rota;
	}

	public void setRota(Rota rota) {
		this.rota = rota;
	}

	public Long getSelectId() {
		return selectId;
	}

	public void setSelectId(Long selectId) {
		this.selectId = selectId;
	}

	public Rota getRotaAux() {
		return rotaAux;
	}

	public void setRotaAux(Rota rotaAux) {
		this.rotaAux = rotaAux;
	}

	public List<Cidade> getComboCidadesOrigem() {
		return comboCidadesOrigem;
	}

	public void setComboCidadesOrigem(List<Cidade> comboCidadesOrigem) {
		this.comboCidadesOrigem = comboCidadesOrigem;
	}

	public ConverterUtil<Cidade> getConverterCidadeOrigem() {
		return converterCidadeOrigem;
	}

	public void setConverterCidadeOrigem(
			ConverterUtil<Cidade> converterCidadeOrigem) {
		this.converterCidadeOrigem = converterCidadeOrigem;
	}

	public Pais getPaisDestinoAnterior() {
		return paisDestinoAnterior;
	}

	public void setPaisDestinoAnterior(Pais paisDestinoAnterior) {
		this.paisDestinoAnterior = paisDestinoAnterior;
	}

	public List<Pais> getComboPaisesOrigem() {
		return comboPaisesOrigem;
	}

	public void setComboPaisesOrigem(List<Pais> comboPaisesOrigem) {
		this.comboPaisesOrigem = comboPaisesOrigem;
	}

	public ConverterUtil<Pais> getConverterPaisOrigem() {
		return converterPaisOrigem;
	}

	public void setConverterPaisOrigem(ConverterUtil<Pais> converterPaisOrigem) {
		this.converterPaisOrigem = converterPaisOrigem;
	}

	public List<Pais> getComboPaisesDestino() {
		return comboPaisesDestino;
	}

	public void setComboPaisesDestino(List<Pais> comboPaisesDestino) {
		this.comboPaisesDestino = comboPaisesDestino;
	}

	public EntityConverter<Pais> getConverterPaisDestino() {
		return converterPaisDestino;
	}

	public void setConverterPaisDestino(
			EntityConverter<Pais> converterPaisDestino) {
		this.converterPaisDestino = converterPaisDestino;
	}

	public BasicFiltroRota getFiltroRota() {
		return filtroRota;
	}

	public void setFiltroRota(BasicFiltroRota filtroRota) {
		this.filtroRota = filtroRota;
	}

	public List<Cidade> getComboCidadesDestino() {
		return comboCidadesDestino;
	}

	public void setComboCidadesDestino(List<Cidade> comboCidadesDestino) {
		this.comboCidadesDestino = comboCidadesDestino;
	}

	public EntityConverter<Cidade> getConverterCidadeDestino() {
		return converterCidadeDestino;
	}

	public void setConverterCidadeDestino(
			EntityConverter<Cidade> converterCidadeDestino) {
		this.converterCidadeDestino = converterCidadeDestino;
	}

	public BasicFiltroInvoice getFiltroInvoices() {
		return filtroInvoices;
	}

	public List<SelectItem> getComboOrigens() {
		return comboOrigens;
	}

	public void setComboOrigens(List<SelectItem> comboOrigens) {
		this.comboOrigens = comboOrigens;
	}

	public EntityConverter<Pais> getConverterOrigem() {
		return converterOrigem;
	}

	public void setConverterOrigem(EntityConverter<Pais> converterOrigem) {
		this.converterOrigem = converterOrigem;
	}

	public void setFiltroInvoices(BasicFiltroInvoice filtroInvoices) {
		this.filtroInvoices = filtroInvoices;
	}

	public List<SelectItem> getComboCompradores() {
		return comboCompradores;
	}

	public void setComboCompradores(List<SelectItem> comboCompradores) {
		this.comboCompradores = comboCompradores;
	}

	public EntityConverter<Usuario> getConverterComprador() {
		return converterComprador;
	}

	public void setConverterComprador(
			EntityConverter<Usuario> converterComprador) {
		this.converterComprador = converterComprador;
	}

	public List<SelectItem> getComboFornecedores() {
		return comboFornecedores;
	}

	public void setComboFornecedores(List<SelectItem> comboFornecedores) {
		this.comboFornecedores = comboFornecedores;
	}

	public EntityConverter<PessoaJuridica> getConverterForncedor() {
		return converterForncedor;
	}

	public void setConverterForncedor(
			EntityConverter<PessoaJuridica> converterForncedor) {
		this.converterForncedor = converterForncedor;
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

	public String getMsgRota() {
		return msgRota;
	}

	public void setMsgRota(String msgRota) {
		this.msgRota = msgRota;
	}

	public List<Usuario> getImportadores() {
		return importadores;
	}

	public void setImportadores(List<Usuario> importadores) {
		this.importadores = importadores;
	}

	public List<Projeto> getProjetos() {
		return projetos;
	}

	public void setProjetos(List<Projeto> projetos) {
		this.projetos = projetos;
	}

	public Integer getPaginaAtualInvoicesCarga() {
		if (paginaAtualInvoicesCarga != null) {
			return paginaAtualInvoicesCarga;
		}
		return 0;
	}

	public void setPaginaAtualInvoicesCarga(Integer paginaAtualInvoicesCarga) {
		this.paginaAtualInvoicesCarga = paginaAtualInvoicesCarga;
	}

	public void setPaginaAtualProjetos(Integer paginaAtualProjetos) {
		this.paginaAtualProjetos = paginaAtualProjetos;
	}

	public Carga getSelectCarga() {
		return selectCarga;
	}

	public void setSelectCarga(Carga selectCarga) {
		this.selectCarga = selectCarga;
	}

	public Cidade getCidadeFilter() {
		return cidadeFilter;
	}

	public void setCidadeFilter(Cidade cidadeFilter) {
		this.cidadeFilter = cidadeFilter;
	}

	public Rota getCapRota() {
		return capRota;
	}

	public void setCapRota(Rota capRota) {
		this.capRota = capRota;
	}

	public List<Invoice> getSelectedInvoices() {
		return selectedInvoices;
	}

	public void setSelectedInvoices(List<Invoice> selectedInvoices) {
		this.selectedInvoices = selectedInvoices;
	}

	public ConverterUtil<Usuario> getImportadoresConverter() {
		return importadoresConverter;
	}

	public void setImportadoresConverter(
			ConverterUtil<Usuario> importadoresConverter) {
		this.importadoresConverter = importadoresConverter;
	}

	public Pais getOrigem() {
		return origem;
	}

	public void setOrigem(Pais origem) {
		this.origem = origem;
	}

	/**
	 * @return the comboStatus
	 */
	public List<SelectItem> getComboStatus() {
		return comboStatus;
	}

	/**
	 * @param comboStatus the comboStatus to set
	 */
	public void setComboStatus(List<SelectItem> comboStatus) {
		this.comboStatus = comboStatus;
	}

	/**
	 * @return the comboProcBroker
	 */
	public List<ProcBroker> getComboProcBroker() {
		return comboProcBroker;
	}

	/**
	 * @param comboProcBroker the comboProcBroker to set
	 */
	public void setComboProcBroker(List<ProcBroker> comboProcBroker) {
		this.comboProcBroker = comboProcBroker;
	}

}
