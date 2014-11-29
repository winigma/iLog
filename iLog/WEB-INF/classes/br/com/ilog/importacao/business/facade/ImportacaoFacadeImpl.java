package br.com.ilog.importacao.business.facade;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.com.ilog.cadastro.business.entity.CheckList;
import br.com.ilog.cadastro.business.entity.Cidade;
import br.com.ilog.cadastro.business.entity.Endereco;
import br.com.ilog.cadastro.business.entity.Incoterm;
import br.com.ilog.cadastro.business.entity.Ocorrencia;
import br.com.ilog.cadastro.business.entity.ParametroCanal;
import br.com.ilog.cadastro.business.entity.TipoPacote;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroIncoterm;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroOcorrencia;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroTipoPacote;
import br.com.ilog.cadastro.business.rep.IncotermRepository;
import br.com.ilog.cadastro.business.rep.TipoPacoteRepository;
import br.com.ilog.geral.business.CodigoErroEspecifico;
import br.com.ilog.importacao.business.entity.AnexoFollowUpImp;
import br.com.ilog.importacao.business.entity.AnexoInvoice;
import br.com.ilog.importacao.business.entity.Carga;
import br.com.ilog.importacao.business.entity.CargaConfirmadaHistorico;
import br.com.ilog.importacao.business.entity.CustoDI;
import br.com.ilog.importacao.business.entity.CustoImportacao;
import br.com.ilog.importacao.business.entity.FollowUpEstimado;
import br.com.ilog.importacao.business.entity.FollowUpImport;
import br.com.ilog.importacao.business.entity.FollowUpImportTrecho;
import br.com.ilog.importacao.business.entity.HistStatusItensInvoice;
import br.com.ilog.importacao.business.entity.HistoricoStatusInvoice;
import br.com.ilog.importacao.business.entity.Invoice;
import br.com.ilog.importacao.business.entity.InvoiceChecklist;
import br.com.ilog.importacao.business.entity.ItemInvoice;
import br.com.ilog.importacao.business.entity.ItemPO;
import br.com.ilog.importacao.business.entity.PO;
import br.com.ilog.importacao.business.entity.ProcBroker;
import br.com.ilog.importacao.business.entity.RelatarFollowUpImport;
import br.com.ilog.importacao.business.entity.StatusCarga;
import br.com.ilog.importacao.business.entity.StatusInvoice;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroCarga;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroCargaConfirmada;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroInvoice;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroPO;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroPlanejarCarga;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroProcBroker;
import br.com.ilog.importacao.business.rep.AnexoFollowUpImpRepository;
import br.com.ilog.importacao.business.rep.CargaRepository;
import br.com.ilog.importacao.business.rep.CustoDIRepository;
import br.com.ilog.importacao.business.rep.CustoImportacaoRepository;
import br.com.ilog.importacao.business.rep.FollowUpRepository;
import br.com.ilog.importacao.business.rep.FollowUpTrechoRepository;
import br.com.ilog.importacao.business.rep.InvoiceChecklistRepository;
import br.com.ilog.importacao.business.rep.InvoiceRepository;
import br.com.ilog.importacao.business.rep.ItemInvoiceRepository;
import br.com.ilog.importacao.business.rep.ItemPORepository;
import br.com.ilog.importacao.business.rep.OcorrenciaRepository;
import br.com.ilog.importacao.business.rep.PORepository;
import br.com.ilog.importacao.business.rep.ProcBrokerRepository;
import br.com.ilog.importacao.business.rep.RelatarRepository;

@Component("controllerImportacao")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = BusinessException.class)
public class ImportacaoFacadeImpl implements ImportacaoFacade {

	@Resource
	IncotermRepository inconTermsRepository;

	@Resource
	TipoPacoteRepository typePackageRepository;

	@Resource
	InvoiceRepository invoiceRepository;
	
	@Resource
	ItemInvoiceRepository itemInvoiceRepository;
	
	
	@Resource
	ProcBrokerRepository procBrokerRepository;
	
	@Resource
	InvoiceChecklistRepository validarInvoice;
	
	@Resource
	CargaRepository cargaRepository;
	
	@Resource
	FollowUpRepository followUpRepository;
	
	@Resource
	OcorrenciaRepository ocorrenciaRepository;
	
	@Resource
	RelatarRepository relatarRepository;
	
	@Resource
	PORepository poRepository;
	
	@Resource
	CustoImportacaoRepository custoImportRepository;
	
	@Resource
	FollowUpTrechoRepository followUpTrechoRepository;
	
	@Resource
	AnexoFollowUpImpRepository anexoFollowUpRepository;

	@Resource
	ItemPORepository itemPORepository;

	@Resource
	CustoDIRepository custoDIRepository;
	
	/**
	 * @coment Fachada de Incoterms
	 * @coment TERMOS INTERNACIONAIS DE COMÉRCIO - INCOTERMS
	 * @author Wisley Souza
	 */
	@Override
	public Incoterm alterarIncoTerms(Incoterm incoterms)
			throws BusinessException {

		// uppder case
		incoterms.setDescricao(incoterms.getDescricao().toUpperCase());
		incoterms.setSigla(incoterms.getSigla().toUpperCase());

		return inconTermsRepository.alterar(incoterms);
	}

	@Override
	public Incoterm cadastrarIncoTerms(Incoterm incoterms)
			throws BusinessException {

		// uppder case
		incoterms.setDescricao(incoterms.getDescricao().toUpperCase());
		incoterms.setSigla(incoterms.getSigla().toUpperCase());

		return inconTermsRepository.cadastrar(incoterms);
	}

	@Override
	public void excluirIncoTerms(Incoterm incoTerms) throws BusinessException {
		inconTermsRepository.excluirById(incoTerms.getId());

	}

	@Override
	@Transactional(readOnly=true)
	public Incoterm getIncoTermsById(Long id) throws BusinessException {

		return inconTermsRepository.getById(id);
	}

	@Override
	@Transactional(readOnly=true)
	public List<Incoterm> listIncoterms(BasicFiltroIncoterm filtro)
			throws BusinessException {

		return inconTermsRepository.listar(filtro);
	}

	/**
	 * @coment Fachada de Tipos de pacotes
	 * @author Wisley Souza
	 */

	@Override
	public TipoPacote alterarTypePackage(TipoPacote type)
			throws BusinessException {

		return typePackageRepository.alterar(type);
	}

	@Override
	public TipoPacote cadastrarTipoPacote(TipoPacote type)
			throws BusinessException {
		return typePackageRepository.cadastrar(type);
	}

	@Override
	public void excluirTypePackage(TipoPacote type) throws BusinessException {
		typePackageRepository.excluirById(type.getId());

	}

	@Override
	public TipoPacote getTypePackageById(Long id) throws BusinessException {
		return typePackageRepository.getById(id);
	}

	/**
	 * 
	 * @coment fachada de Invoice
	 * 
	 * 
	 */

	/**
	 * método de listagem de invoice
	 */
	@Override
	public List<Invoice> listInvoice(BasicFiltroInvoice filtro)
			throws BusinessException {
		return invoiceRepository.listarInvoices(filtro);
	}

	/**
	 * @coment Método que retorna as Invoices em validação
	 */
	@Override
	public List<Invoice> listInvoiceOfValidate(BasicFiltroInvoice filtro)
			throws BusinessException {

		return invoiceRepository.listarInvoicesOfValidation(filtro);
	}

	@Override
	public List<Invoice> listarInvoices(BasicFiltroInvoice filtro)
			throws BusinessException {
		return invoiceRepository.listarInvoices(filtro);
	}

	@Override
	public Invoice alterarInvoice(Invoice invoice, AnexoInvoice anexo)
			throws BusinessException {

		// REMOCAO DE ANEXO DE INVOICE
		// if ((invoice.getAnexo() == null || invoice.getAnexo().getId() ==
		// null)
		// && anexo != null && anexo.getId() != null) {
		// invoiceRepository.getEntityManager().remove(
		// invoiceRepository.getEntityManager().find(
		// AnexoInvoice.class, anexo.getId()));
		// invoice.setAnexo(null);
		//
		// }

		invoice = alterarInvoiceComAnexo(invoice);

		// VERIFICAR SE A INVOICE ESTA CONSOLIDADA EM UMA CARGA.
		if (invoice.getCarga() != null
				&& (invoice.getStatus().equals(StatusInvoice.CO)
						|| invoice.getStatus().equals(StatusInvoice.AC)
						|| invoice.getStatus().equals(StatusInvoice.TBS)
						|| invoice.getStatus().equals(StatusInvoice.ITT)
						|| invoice.getStatus().equals(StatusInvoice.AT)
						|| invoice.getStatus().equals(StatusInvoice.OHI) || invoice
						.getStatus().equals(StatusInvoice.ICC))) {

			Carga carga = getCargaById(invoice.getCarga().getId());
			if (invoice.isCritico()) {
				carga.setCritico(invoice.isCritico());

			} else {
				boolean critical = false;
				if (carga.getListaDeInvoices() != null) {
					for (Invoice inv : carga.getListaDeInvoices()) {
						if (inv.isCritico()) {
							critical = true;
							break;
						}
					}
				}
				carga.setCritico(critical);
			}

			alterarCarga(carga);
		}

		return invoice;
	}

	private Invoice alterarInvoiceComAnexo(Invoice invoice)
			throws BusinessException {

		// REMOCAO DE ANEXO DE INVOICE
		// if (invoice.getAnexo() != null) {
		// if (invoice.getAnexo().getId() != null) {
		// invoiceRepository.getEntityManager().merge(invoice.getAnexo());
		// } else {
		// invoiceRepository.getEntityManager()
		// .persist(invoice.getAnexo());
		// }
		// }

		return invoiceRepository.alterar(invoice);
	}

	@Override
	public Invoice alterarInvoice(Invoice invoice) throws BusinessException {
		return invoiceRepository.alterar(invoice);
	}
	
	@Override
	public Invoice cadastrarInvoice(Invoice invoice) throws BusinessException {

		// REMOCAO DE ANEXO DE INVOICE
		// if (invoice.getAnexo() != null) {
		// invoiceRepository.getEntityManager().persist(invoice.getAnexo());
		// }

		if (invoice.getDtEmissao() == null)
			invoice.setDtEmissao(new Date());

		gerarCodigoSequencial(invoice);

		return invoiceRepository.cadastrar(invoice);
	}

	/**
	 * Gerar codigo sequencial para Invoice. Deve ser usado apenas no cadastro
	 * 
	 * @param invoice
	 */
	private void gerarCodigoSequencial(Invoice invoice) {
		if (invoice != null && invoice.getId() == null) {
			Calendar c = Calendar.getInstance();

			String aux = Integer.toString(c.get(Calendar.YEAR));
			int ano = Integer.valueOf(aux.substring(2));

			int lastCode = getLastCodeByYear(ano);

			invoice.setNumSequencia(lastCode + 1);
			invoice.setAnoSequencia(ano);
		}
	}

	@Override
	public Integer getLastCodeByYear(int ano) {
		try {
			return invoiceRepository.getLastCodeByYear(ano);
		} catch (BusinessException e) {

			e.printStackTrace();

			return 1;
		}
	}

	/**
	 * Metodo para excluir uma invoice
	 */
	@Override
	public void excluirInvoice(Invoice invoice) throws BusinessException {

		// REMOCAO DE ANEXO DE INVOICE
		// if (invoice.getAnexo() != null && invoice.getAnexo().getId() != null)
		// {
		// invoiceRepository.removerAnexoInvoice(invoice.getAnexo());
		// invoice.setAnexo(null);
		// }

		invoiceRepository.excluirById(invoice.getId());
	}

	@Override
	@Transactional(readOnly=true)
	public Invoice getInvoiceByIdWithFile(Long id, Boolean incializar,
			Boolean arquivo) throws BusinessException {
		Invoice invoice = getInvoiceById(id, incializar);

		if (invoice != null) {

			Hibernate.initialize(invoice.getCarga());

			// REMOCAO DE ANEXO DE INVOICE
			// if ( arquivo )
			// Hibernate.initialize(invoice.getAnexo());

		}
		return invoice;

	}
	
	@Override
	public ItemPO alterarItemPO(ItemPO itemPO) throws BusinessException {
		return itemPORepository.alterar(itemPO);
	}

	@Override
	public List<Invoice> listarInvoicesByCarga(Carga carga)
			throws BusinessException {
		return invoiceRepository.listarInvoicesPorCarga(carga);
	}

	@Override
	public List<Invoice> listarInvoicesByCarga(Carga carga, boolean itens)
			throws BusinessException {
		return invoiceRepository.listarInvoicesPorCarga(carga, itens);
	}
	
	@Override
	public List<ItemInvoice> listarItemInvoiceByCarga(Carga carga) throws BusinessException{
		return itemInvoiceRepository.listarItemInvoicesByCarga(carga);
	}

	@Override
	public Invoice getInvoiceById(Long id, Boolean incializar)
			throws BusinessException {
		Invoice invoice = invoiceRepository.getById(id);
		if (incializar) {
			Hibernate.initialize(invoice.getExportador());
			Hibernate.initialize(invoice.getComprador());
			Hibernate.initialize(invoice.getIncoterm());
			Hibernate.initialize(invoice.getPaisOrigem());
			Hibernate.initialize(invoice.getTerminal());
			Hibernate.initialize(invoice.getTipoPacote());
			if (invoice.getTipoPacote2() != null)
				Hibernate.initialize(invoice.getTipoPacote2());
			Hibernate.initialize(invoice.getItensInvoice());
		}
		return invoice;
	}

	@Override
	public void inicializarExportador(Invoice invoice) throws BusinessException {

		Hibernate.initialize(invoice.getExportador());
		Hibernate.initialize(invoice.getExportador().getEnderecos());
		if (invoice.getExportador().getEnderecos() != null
				&& !invoice.getExportador().getEnderecos().isEmpty()) {
			for (Endereco end : invoice.getExportador().getEnderecos()) {
				Hibernate.initialize(end.getPais());
			}
		}
		Hibernate.initialize(invoice.getExportador().getContatos());
	}

	@Override
	public List<TipoPacote> listTypesPackages(BasicFiltroTipoPacote filter)
			throws BusinessException {
		// TODO Auto-generated method stub
		return typePackageRepository.listar(filter);
	}

	@Override
	public List<Invoice> listInvoiceOfApproved(BasicFiltroInvoice filtro)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Invoice> listarInvoices(BasicFiltroCarga filtro)
			throws BusinessException {
		return invoiceRepository.listarInvoices(filtro);
	}

	@Override
	public InvoiceChecklist cadastrarRespostas(InvoiceChecklist entity)
			throws BusinessException {
		// TODO Auto-generated method stub
		return validarInvoice.cadastrar(entity);
	}

	@Override
	public InvoiceChecklist getInvoiceChecklist(Invoice invoice,
			CheckList checklist) throws BusinessException {
	
		return validarInvoice.getInvoiceChecklist(invoice, checklist);
	}

	@Override
	public InvoiceChecklist alterarRespostas(InvoiceChecklist entity)
			throws BusinessException {
		// TODO Auto-generated method stub
		return validarInvoice.alterar(entity);
	}

	@Override
	public InvoiceChecklist getInvoiceChecklist(Invoice invoice)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HistoricoStatusInvoice> gethistoricos(Invoice invoice)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HistoricoStatusInvoice> getLastHistoricos(Invoice invoice)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HistoricoStatusInvoice getHistoricoStatusInvoiceById(Long id)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HistoricoStatusInvoice cadastrarHistorico(
			HistoricoStatusInvoice entidade) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HistStatusItensInvoice> getItensHistorico(
			HistoricoStatusInvoice entity) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Carga> listCargasConsolidadas(BasicFiltroCarga filtro)
			throws BusinessException {
		
		return cargaRepository.listCargaByFilter(filtro);
	}

	@Override
	public List<Carga> listCargasFollowUp(BasicFiltroCarga filtro)
			throws BusinessException {
		
		return cargaRepository.listCargasFollowUp(filtro);
	}

	@Override
	public FollowUpImport getFollowUpByCarga(Carga carga)
			throws BusinessException {
		return followUpRepository.getFollowUpByCarga(carga);
	}

	@Override
	public FollowUpImport cadastrarFollowUp(FollowUpImport followUp)
			throws BusinessException {
		
		return followUpRepository.cadastrar(followUp);
	}

	@Override
	public FollowUpImport getFollowUpImportById(Long id)
			throws BusinessException {
		return followUpRepository.getById(id);
	}

	@Override
	public FollowUpImportTrecho getFollowUpImportTrechoById(Long id)
			throws BusinessException {
	
		return followUpTrechoRepository.getById(id);
	}

	@Override
	public FollowUpEstimado cadastrarEstimado(FollowUpEstimado entity)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FollowUpEstimado> listarEstimadosByFollowUpTrecho(
			FollowUpImportTrecho follow) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FollowUpEstimado alterarEstimado(FollowUpEstimado entity)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FollowUpImport alterarFollowUp(FollowUpImport followUp)
			throws BusinessException {
		return followUpRepository.alterar(followUp);
	}

	@Override
	@Transactional(readOnly=true)
	public Carga getCargaById(Long id) throws BusinessException {
		return cargaRepository.getById(id);
	}

	@Override
	public Carga carregarProcBroker( Carga carga ) throws BusinessException {
		return cargaRepository.carregarProcBroker(carga);
	}
	
	@Override
	public Carga cadastrarCarga(Carga carga) throws BusinessException {
	
		return cargaRepository.cadastrar(carga);
	}

	@Override
	public Carga alterarCarga(Carga carga) throws BusinessException {
		
		/*
		 * Caso salve uma carga com status diferente de ITT ou OHI
		 * cidade atual deve ser nula.  
		 */
		if ( !carga.getStatus().equals(StatusCarga.ITT) 
				&& !carga.getStatus().equals( StatusCarga.OHI ) ) {
			carga.setCidadeAtual( null );
		}
		
		/*
		 * Caso selecionada o procBroker com a DI, entao salva nos atributos.
		 */
		if ( carga.getProcBroker() != null ) {
			carga.setNumeroDi( carga.getProcBroker().getNrDI() );
			carga.setDtRegistroDi( carga.getProcBroker().getDataDI() );
			
		} else {
			carga.setDtRegistroDi( null );
			carga.setNumeroDi(null);
		}
		
		if ( carga.getListaDeInvoices() != null 
				&& !carga.getListaDeInvoices().isEmpty() ) {
			for ( Invoice invoice : carga.getListaDeInvoices() ) {
				invoice.setStatus( StatusInvoice.getStatus( carga.getStatus() ) );
				invoice.setCidadeAtual( carga.getCidadeAtual() );
				
				invoiceRepository.alterar( invoice );
			}
		}
		carga.setLastAtualizacao(new Date());
		return cargaRepository.alterar(carga);
	}

	@Override
	public FollowUpEstimado getFollowUpstimadoById(Long id)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Carga> listCargasPainel(BasicFiltroCarga filtro)
			throws BusinessException {
		
		return cargaRepository.listCargasPainel(filtro);
	}
	public List<Carga> listCargasPainelIndividual(BasicFiltroCarga filtro)
			throws BusinessException{
		return cargaRepository.listarLastUpdates(filtro);
	}

	@Override
	public void excluirOcorrencia(Ocorrencia ocorrencia)
			throws BusinessException {
		ocorrenciaRepository.excluirById(ocorrencia.getId());
	}

	@Override
	public List<Ocorrencia> listarOcorrencia(BasicFiltroOcorrencia filtro)
			throws BusinessException {
	
		return ocorrenciaRepository.listOcorrencias(filtro);
	}

	@Override
	public Ocorrencia cadastrarOcorrencia(Ocorrencia entity)
			throws BusinessException {
		return ocorrenciaRepository.cadastrar(entity);
	}

	@Override
	public Ocorrencia alterarOcorrencia(Ocorrencia entity)
			throws BusinessException {
		return ocorrenciaRepository.alterar(entity);
	}

	@Override
	public Ocorrencia getOcorrenciaById(Long id) throws BusinessException {
		return ocorrenciaRepository.getById(id);
	}

	@Override
	public boolean possuiOcorrencia(Carga carga) throws BusinessException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Carga> possuiOcorrenciaNaoLida() throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParametroCanal getCanalCarga(Carga carga) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Carga> listarCargas(BasicFiltroCarga filtro)
			throws BusinessException {
		return cargaRepository.listarCargas(filtro);
	}

	@Override
	public FollowUpImport carregarFollowUpEstimados(FollowUpImport followUp)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AnexoFollowUpImp> getAnexosFollowUp(FollowUpImport followUp)
			throws BusinessException {
	
		return followUpRepository.getAnexosFollowUp(followUp);
	}

	@Override
	public void excluirAnexoFollowUp(List<AnexoFollowUpImp> removeListAnexos2)
			throws BusinessException {
		anexoFollowUpRepository.excluir(removeListAnexos2);

	}

	@Override
	public void adicionarAnexosFollowUp(List<AnexoFollowUpImp> anexosFollowUp2)
			throws BusinessException {
		anexoFollowUpRepository.adicionarAnexosFollowUp(anexosFollowUp2);

	}

	@Override
	public List<AnexoInvoice> getAnexosInvoice(Invoice invoice)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void excluirAnexoInvoice(List<AnexoInvoice> removeListAnexos)
			throws BusinessException {
		// TODO Auto-generated method stub

	}

	@Override
	public void adicionarAnexosInvoice(List<AnexoInvoice> anexos)
			throws BusinessException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<FollowUpImportTrecho> listarFollowUpTrechosByCarga(Carga carga)
			throws BusinessException {
	
		return followUpTrechoRepository.listarByCarga(carga);

	}

	@Override
	public List<Carga> listCargasPainelLogistica(BasicFiltroCarga filtro)
			throws BusinessException {
	
		return cargaRepository.listCargasPainelLogistica(filtro);
	}

	@Override
	public CargaConfirmadaHistorico cadastrarCargaConfHist(
			CargaConfirmadaHistorico obj) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CargaConfirmadaHistorico> listarCargasConfirmadas(
			BasicFiltroCargaConfirmada filtro) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Carga listCargaPlussInvoice(Carga carga) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FollowUpImportTrecho> listTrechoByCity(Cidade city)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RelatarFollowUpImport getRelatorByFollowUp(FollowUpImport obj)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RelatarFollowUpImport cadastrarRelatarMail(RelatarFollowUpImport obj)
			throws BusinessException {

		return  relatarRepository.cadastrar(obj);
	}

	@Override
	public RelatarFollowUpImport alterarRelatarMail(RelatarFollowUpImport obj)
			throws BusinessException {

		return relatarRepository.alterar(obj);
	}

	@Override
	public Carga getCargaByIdRelatorio(Long id) throws BusinessException {
		return cargaRepository.getByIdRelatorio(id);
	}
	
	
	
	
	/**
	 * Embarques planejados
	 */

	@Override
	public List<Carga> listarEmbarquesPlanejados(BasicFiltroPlanejarCarga filtro)
			throws BusinessException {
		return cargaRepository.listarEmbarquesPlenejados(filtro);
	}

	@Override
	public List<Invoice> listarInvoicesByCargaPlanejada(Carga carga)
			throws BusinessException {
	
		return invoiceRepository.listarInvoicesPorCargaPlanejada(carga);
	}

	@Override
	public List<Carga> listarTodasAsCargas() throws BusinessException {
		return cargaRepository.listar();
	}

	@Override
	public List<ProcBroker> listarDadosBroker(BasicFiltroProcBroker filtro)
			throws BusinessException {
		return procBrokerRepository.listar( filtro );
	}

	@Override
	public Invoice getInvoiceByNumero(String nrInvoice)
			throws BusinessException {
		return invoiceRepository.getByNumero( nrInvoice );
	}

	@Override
	public void salvarImportacaoBroker(List<ProcBroker> brokersImport)
			throws BusinessException {
		procBrokerRepository.salvarBroker(brokersImport);
	}

	@Override
	public ProcBroker getProcBrokerById(Long idRegistro, boolean iniciarListas)
			throws BusinessException {
		return procBrokerRepository.getById( idRegistro, iniciarListas );
	}

	@Override
	public void excluirProcBroker(ProcBroker detalheBroker)
			throws BusinessException {
		procBrokerRepository.excluirById( detalheBroker.getId() );
	}
	
	/**
	 * PO
	 */
	/* (non-Javadoc)
	 * @see br.com.ilog.importacao.business.facade.ImportacaoFacade#getPO(java.lang.String)
	 */
	@Override
	@Transactional(readOnly=true)
	public PO getPO(String po) throws BusinessException {
		return poRepository.getPO(po);
	}

	/* (non-Javadoc)
	 * @see br.com.ilog.importacao.business.facade.ImportacaoFacade#listarPO(br.com.ilog.importacao.business.entityFilter.BasicFiltroPO)
	 */
	@Override
	@Transactional(readOnly=true)
	public List<PO> listarPO(BasicFiltroPO filtroPO) throws BusinessException {
		return poRepository.listarPO(filtroPO);
	}

	/* (non-Javadoc)
	 * @see br.com.ilog.importacao.business.facade.ImportacaoFacade#cadastrarPO(br.com.ilog.importacao.business.entity.PO)
	 */
	@Override
	public void cadastrarPO(PO po) throws BusinessException {
		poRepository.cadastrar(po);
	}
	
	/* (non-Javadoc)
	 * @see br.com.ilog.importacao.business.facade.ImportacaoFacade#cadastrarPO(br.com.ilog.importacao.business.entity.PO)
	 */
	@Override
	public void excluirPO(PO po) throws BusinessException {
		poRepository.excluirById(po.getId());
	}

	@Override
	public List<CustoImportacao> listarCustosImportacao()
			throws BusinessException {
		return custoImportRepository.listar();
	}

	@Override
	public List<Carga> listCargasBroker(BasicFiltroCarga filtro)
			throws BusinessException {
		return cargaRepository.listarCargasBroker(filtro);
	}

	@Override
	public Carga carregarCustos(Carga carga) throws BusinessException {
		return cargaRepository.carregarCustos( carga );
	}

	@Override
	public Carga alterarCusto(Carga carga) throws BusinessException {
		return cargaRepository.alterarCusto(carga);
	}

	@Override
	public List<ProcBroker> listarBrokerSemCarga(Carga carga)
			throws BusinessException {
		return procBrokerRepository.listarSemCarga( carga );
	}

	/* (non-Javadoc)
	 * @see br.com.ilog.importacao.business.facade.ImportacaoFacade#getItemInvoice(br.com.ilog.importacao.business.entity.ItemInvoice)
	 */
	@Override
	@Transactional(readOnly=true)
	public ItemPO getItemInvoice(ItemPO itemPO) throws BusinessException{
		// TODO Auto-generated method stub
		return poRepository.getItemPO(itemPO);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Invoice> listarInvoicesComCarga(BasicFiltroInvoice filtro) throws BusinessException{
		return invoiceRepository.listarInvoicesComCarga(filtro);
	}

	@Override
	public void excluirCarga(Carga carga) throws BusinessException {
		
		if ( carga.getStatus() == null || carga.getStatus().equals( StatusCarga.P ) ) {
			for (Invoice invoice : carga.getListaDeInvoices()) {
				invoice.setStatus( StatusInvoice.C );
				invoice.setCarga( null );
				invoiceRepository.alterar( invoice );
			}
			cargaRepository.excluirById( carga.getId() );
		} else {
			throw new BusinessException( CodigoErroEspecifico.MSG_NAO_EXCLUIR );
		}
	}

	@Override
	public Carga preencherSequencial(Carga carga) throws BusinessException {
		return cargaRepository.preencheSequencial(carga);
	}

	@Override
	public List<Carga> listarProcessosDI(BasicFiltroCarga filtro)
			throws BusinessException {
		return cargaRepository.listarProcessosDI( filtro );
	}

	@Override
	public List<CustoDI> listarCustos( Carga carga, String tipoFatura ) throws BusinessException {
		return custoDIRepository.listarCustos(carga.getId(), tipoFatura);
	}
	
}
