package br.com.ilog.importacao.business.facade;

import java.util.List;

import org.springframework.scripting.bsh.BshScriptUtils.BshExecutionException;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.com.ilog.cadastro.business.entity.CheckList;
import br.com.ilog.cadastro.business.entity.Cidade;
import br.com.ilog.cadastro.business.entity.Incoterm;
import br.com.ilog.cadastro.business.entity.Ocorrencia;
import br.com.ilog.cadastro.business.entity.ParametroCanal;
import br.com.ilog.cadastro.business.entity.TipoPacote;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroIncoterm;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroOcorrencia;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroTipoPacote;
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
import br.com.ilog.importacao.business.entityFilter.BasicFiltroCarga;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroCargaConfirmada;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroInvoice;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroPO;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroPlanejarCarga;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroProcBroker;

public interface ImportacaoFacade {

	/**
	 * 
	 * IncoTerms
	 * 
	 */
	public List<Incoterm> listIncoterms(BasicFiltroIncoterm filtro)
			throws BusinessException;

	public Incoterm cadastrarIncoTerms(Incoterm incoterms)
			throws BusinessException;

	public Incoterm alterarIncoTerms(Incoterm incoTerms)
			throws BusinessException;

	public Incoterm getIncoTermsById(Long id) throws BusinessException;

	public void excluirIncoTerms(Incoterm incoTerms) throws BusinessException;

	/**
	 * 
	 * Tipo de Pacote
	 * 
	 */
	public TipoPacote cadastrarTipoPacote(TipoPacote type)
			throws BusinessException;

	public List<TipoPacote> listTypesPackages(BasicFiltroTipoPacote filter)
			throws BusinessException;

	public TipoPacote alterarTypePackage(TipoPacote type)
			throws BusinessException;

	public TipoPacote getTypePackageById(Long id) throws BusinessException;

	public void excluirTypePackage(TipoPacote type) throws BusinessException;

	/**
	 * @coment Fachada de invoices ... Inclui Validação e Aprovação de
	 *         Invoices...
	 */

	public List<Invoice> listInvoice(BasicFiltroInvoice filtro)
			throws BusinessException;

	public List<Invoice> listInvoiceOfValidate(BasicFiltroInvoice filtro)
			throws BusinessException;

	public List<Invoice> listInvoiceOfApproved(BasicFiltroInvoice filtro)
			throws BusinessException;

	public List<Invoice> listarInvoices(BasicFiltroInvoice filtro)
			throws BusinessException;
	
	public List<Invoice> listarInvoices(BasicFiltroCarga filtro)
			throws BusinessException;

	public void excluirInvoice(Invoice invoice) throws BusinessException;

	public Invoice alterarInvoice(Invoice invoice) throws BusinessException;

	public Invoice cadastrarInvoice(Invoice invoice) throws BusinessException;

	public Invoice getInvoiceById(Long id, Boolean incializar)
			throws BusinessException;

	public Invoice getInvoiceByIdWithFile(Long id, Boolean incializar,
			Boolean arquivo) throws BusinessException;

	public Invoice alterarInvoice(Invoice invoice, AnexoInvoice anexo)
			throws BusinessException;

	public List<Invoice> listarInvoicesByCarga(Carga carga)
			throws BusinessException;
	
	public List<Invoice> listarInvoicesByCargaPlanejada(Carga carga)
			throws BusinessException;

	public List<Invoice> listarInvoicesByCarga(Carga carga,
			boolean carregarItens) throws BusinessException;

	public void inicializarExportador(Invoice invoice) throws BusinessException;

	/**
	 * @coment Fachada de InvoiceChecklist ... Inclui Validação e Aprovação de
	 *         Invoices...
	 */

	public InvoiceChecklist cadastrarRespostas(InvoiceChecklist entity)
			throws BusinessException;

	public InvoiceChecklist getInvoiceChecklist(Invoice invoice,
			CheckList checklist) throws BusinessException;

	public InvoiceChecklist alterarRespostas(InvoiceChecklist entity)
			throws BusinessException;

	public InvoiceChecklist getInvoiceChecklist(Invoice invoice)
			throws BusinessException;

	/**
	 * @coment Fachada de Historicode Invoice
	 */
	public List<HistoricoStatusInvoice> gethistoricos(Invoice invoice)
			throws BusinessException;

	public List<HistoricoStatusInvoice> getLastHistoricos(Invoice invoice)
			throws BusinessException;

	public HistoricoStatusInvoice getHistoricoStatusInvoiceById(Long id)
			throws BusinessException;

	public HistoricoStatusInvoice cadastrarHistorico(
			HistoricoStatusInvoice entidade) throws BusinessException;

	public List<HistStatusItensInvoice> getItensHistorico(
			HistoricoStatusInvoice entity) throws BusinessException;

	/**
	 * @coment Fachada de Historicode Invoice
	 */
	public List<Carga> listCargasConsolidadas(BasicFiltroCarga filtro)
			throws BusinessException;

	/**
	 * Listar as cargas para followup
	 * 
	 * @param filtro
	 * @return
	 * @throws BusinessException
	 */
	public List<Carga> listCargasFollowUp(BasicFiltroCarga filtro)
			throws BusinessException;

	/*
	 * FOLLOW UP
	 */

	public FollowUpImport getFollowUpByCarga(Carga carga)
			throws BusinessException;

	public FollowUpImport cadastrarFollowUp(FollowUpImport followUp)
			throws BusinessException;

	public FollowUpImport getFollowUpImportById(Long id)
			throws BusinessException;

	public FollowUpImportTrecho getFollowUpImportTrechoById(Long id)
			throws BusinessException;

	public FollowUpEstimado cadastrarEstimado(FollowUpEstimado entity)
			throws BusinessException;

	public List<FollowUpEstimado> listarEstimadosByFollowUpTrecho(
			FollowUpImportTrecho follow) throws BusinessException;

	FollowUpEstimado alterarEstimado(FollowUpEstimado entity)
			throws BusinessException;

	FollowUpImport alterarFollowUp(FollowUpImport followUp)
			throws BusinessException;

	public Carga getCargaById(Long id) throws BusinessException;

	public Carga cadastrarCarga(Carga carga) throws BusinessException;

	public Carga alterarCarga(Carga carga) throws BusinessException;

	public FollowUpEstimado getFollowUpstimadoById(Long id) throws BusinessException;
	
	public Carga preencherSequencial( Carga carga ) throws BusinessException;

	public List<Carga> listCargasPainel(BasicFiltroCarga filtro)
			throws BusinessException;
	public List<Carga> listCargasPainelIndividual(BasicFiltroCarga filtro)
			throws BusinessException;

	/**
	 * Fachada de ocorrências
	 * 
	 * @author Wisley P.Souza
	 */

	public void excluirOcorrencia(Ocorrencia ocorrencia)
			throws BusinessException;

	public List<Ocorrencia> listarOcorrencia(BasicFiltroOcorrencia filtro)
			throws BusinessException;

	public Ocorrencia cadastrarOcorrencia(Ocorrencia entity)
			throws BusinessException;

	public Ocorrencia alterarOcorrencia(Ocorrencia entity)
			throws BusinessException;

	public Ocorrencia getOcorrenciaById(Long id) throws BusinessException;

	public boolean possuiOcorrencia(Carga carga) throws BusinessException;

	public List<Carga> possuiOcorrenciaNaoLida() throws BusinessException;

	public ParametroCanal getCanalCarga(Carga carga) throws BusinessException;

	public List<Carga> listarCargas(BasicFiltroCarga filtro)
			throws BusinessException;
	public List<Carga> listarTodasAsCargas()throws BusinessException;
	

	Integer getLastCodeByYear(int ano) throws BusinessException;

	public FollowUpImport carregarFollowUpEstimados(FollowUpImport followUp)
			throws BusinessException;

	public List<AnexoFollowUpImp> getAnexosFollowUp(FollowUpImport followUp)
			throws BusinessException;

	public void excluirAnexoFollowUp(List<AnexoFollowUpImp> removeListAnexos2)
			throws BusinessException;

	public void adicionarAnexosFollowUp(List<AnexoFollowUpImp> anexosFollowUp2)
			throws BusinessException;

	public List<AnexoInvoice> getAnexosInvoice(Invoice invoice)
			throws BusinessException;

	public void excluirAnexoInvoice(List<AnexoInvoice> removeListAnexos)
			throws BusinessException;

	public void adicionarAnexosInvoice(List<AnexoInvoice> anexos)
			throws BusinessException;

	public List<FollowUpImportTrecho> listarFollowUpTrechosByCarga(Carga carga)
			throws BusinessException;

	public List<Carga> listCargasPainelLogistica(BasicFiltroCarga filtro)
			throws BusinessException;

	/**
	 * Cargas confirmaadas
	 */

	public CargaConfirmadaHistorico cadastrarCargaConfHist(
			CargaConfirmadaHistorico obj) throws BusinessException;

	public List<CargaConfirmadaHistorico> listarCargasConfirmadas(
			BasicFiltroCargaConfirmada filtro) throws BusinessException;

	// carga
	public Carga listCargaPlussInvoice(Carga carga) throws BusinessException;

	public List<FollowUpImportTrecho> listTrechoByCity(Cidade city)
			throws BusinessException;

	/**
	 * Relatar por email
	 */

	public RelatarFollowUpImport getRelatorByFollowUp(FollowUpImport obj)
			throws BusinessException;

	public RelatarFollowUpImport cadastrarRelatarMail(RelatarFollowUpImport obj)
			throws BusinessException;

	public RelatarFollowUpImport alterarRelatarMail(RelatarFollowUpImport obj)
			throws BusinessException;

	public Carga getCargaByIdRelatorio(Long id) throws BusinessException;
	
	
	
	

	/**
	 * @coment Embarques planejados
	 */
	
	public List<Carga> listarEmbarquesPlanejados(BasicFiltroPlanejarCarga filtro)throws BusinessException;

	public List<ProcBroker> listarDadosBroker(BasicFiltroProcBroker filtro) throws BusinessException;

	public Invoice getInvoiceByNumero(String nrInvoice) throws BusinessException;

	/**
	 * Salvar a lista de Entidades de Broker com as informações de DI
	 * @param brokersImport
	 * @return 
	 * @throws BusinessException
	 */
	public void salvarImportacaoBroker(List<ProcBroker> brokersImport) throws BusinessException;

	public ProcBroker getProcBrokerById(Long idRegistro, boolean iniciarListas) throws BusinessException;

	public void excluirProcBroker(ProcBroker detalheBroker) throws BusinessException;

	public List<CustoImportacao> listarCustosImportacao() throws BusinessException;
	
	/**
	 * @coment PO
	 */
	public PO getPO(String po) throws BusinessException;

	/**
	 * @param filtroPO
	 * @return
	 */
	public List<PO> listarPO(BasicFiltroPO filtroPO) throws BusinessException;

	/**
	 * @param po
	 */
	public void cadastrarPO(PO po)throws BusinessException;

	/**
	 * @param po
	 * @throws BusinessException
	 */
	public void excluirPO(PO po) throws BusinessException;

	public List<Carga> listCargasBroker(BasicFiltroCarga filtro) throws BusinessException;

	public Carga carregarCustos(Carga carga) throws BusinessException;

	public Carga alterarCusto(Carga carga) throws BusinessException;

	public List<ProcBroker> listarBrokerSemCarga(Carga carga) throws BusinessException;

	public Carga carregarProcBroker(Carga carga) throws BusinessException;

	
	/**
	 * @param itemPO
	 * @return
	 * @throws BusinessException 
	 */
	public ItemPO getItemInvoice(ItemPO itemPO) throws BusinessException;

	/**
	 * @param carga
	 * @throws BusinessException
	 */
	public void excluirCarga(Carga carga) throws BusinessException;

	/**
	 * @param carga
	 * @return
	 * @throws BusinessException
	 */
	public List<ItemInvoice> listarItemInvoiceByCarga(Carga carga)
			throws BusinessException;

	/**
	 * @param filtro
	 * @return
	 * @throws BusinessException
	 */
	public List<Invoice> listarInvoicesComCarga(BasicFiltroInvoice filtro)
			throws BusinessException;

	/**
	 * @param itemPO
	 * @return
	 * @throws BusinessException
	 */
	public ItemPO alterarItemPO(ItemPO itemPO) throws BusinessException;

	/**
	 * Listar as cargas acessociadas a DI do sistema.
	 * @param filtro
	 * @return
	 * @throws BshExecutionException
	 */
	public List<Carga> listarProcessosDI(BasicFiltroCarga filtro) throws BusinessException;

	List<CustoDI> listarCustos(Carga carga, String tipoFatura)
			throws BusinessException;
	
}
