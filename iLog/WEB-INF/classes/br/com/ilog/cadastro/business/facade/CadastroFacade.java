package br.com.ilog.cadastro.business.facade;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.List;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.com.ilog.cadastro.business.entity.Canal;
import br.com.ilog.cadastro.business.entity.CheckList;
import br.com.ilog.cadastro.business.entity.Cidade;
import br.com.ilog.cadastro.business.entity.Contato;
import br.com.ilog.cadastro.business.entity.DataVigencia;
import br.com.ilog.cadastro.business.entity.Departamento;
import br.com.ilog.cadastro.business.entity.Endereco;
import br.com.ilog.cadastro.business.entity.Estado;
import br.com.ilog.cadastro.business.entity.Feriado;
import br.com.ilog.cadastro.business.entity.Filial;
import br.com.ilog.cadastro.business.entity.FormaPagamento;
import br.com.ilog.cadastro.business.entity.Frete;
import br.com.ilog.cadastro.business.entity.Incoterm;
import br.com.ilog.cadastro.business.entity.ItemChecklist;
import br.com.ilog.cadastro.business.entity.MateriaPrima;
import br.com.ilog.cadastro.business.entity.Modal;
import br.com.ilog.cadastro.business.entity.Moeda;
import br.com.ilog.cadastro.business.entity.Motivo;
import br.com.ilog.cadastro.business.entity.Nivel;
import br.com.ilog.cadastro.business.entity.Pais;
import br.com.ilog.cadastro.business.entity.ParametroCanal;
import br.com.ilog.cadastro.business.entity.ParametroContinente;
import br.com.ilog.cadastro.business.entity.ParametroImposto;
import br.com.ilog.cadastro.business.entity.PessoaJuridica;
import br.com.ilog.cadastro.business.entity.Processo;
import br.com.ilog.cadastro.business.entity.Projeto;
import br.com.ilog.cadastro.business.entity.Rota;
import br.com.ilog.cadastro.business.entity.Terminal;
import br.com.ilog.cadastro.business.entity.TipoOcorrencia;
import br.com.ilog.cadastro.business.entity.TipoPacote;
import br.com.ilog.cadastro.business.entity.TipoPessoa;
import br.com.ilog.cadastro.business.entity.TipoPessoaEnum;
import br.com.ilog.cadastro.business.entity.Trecho;
import br.com.ilog.cadastro.business.entity.UnidadeMedida;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroCheckList;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroCidade;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroDepartamento;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroEstado;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroFeriado;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroFilial;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroFrete;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroIncoterm;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroMateriaPrima;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroModal;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroMoeda;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroMotivo;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroNivel;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroPais;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroPessoaJuridica;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroProjeto;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroRota;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroTerminal;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroTipoOcorrencia;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroTipoPacote;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroUnidMedida;
import br.com.ilog.importacao.business.entity.FollowUpImport;
import br.com.ilog.importacao.business.entity.Invoice;

public interface CadastroFacade {
	
	
	/**
	 * 
	 * @param Unidade Medida
	 * @return
	 * @throws BusinessException
	 */
	
	public UnidadeMedida cadsatrarUnidMedida(UnidadeMedida obj)throws BusinessException;
	public UnidadeMedida alterarUnidMedida(UnidadeMedida obj) throws BusinessException;
	public void excluirUnidMedida(UnidadeMedida obj)throws BusinessException;
	public UnidadeMedida getUnidMedidaById(Long id)throws BusinessException;
	public List<UnidadeMedida> listarUnidMedidaByFiltro(BasicFiltroUnidMedida filtro) throws BusinessException;
	public UnidadeMedida getUnidMedidaByCodigo(String codigo) throws BusinessException;

	
	/**
	 * 
	 * @param Nivel
	 * @return
	 * @throws BusinessException
	 */
	
	public Nivel cadastrarNivel(Nivel obj)throws BusinessException;
	public Nivel alterarNivel(Nivel obj) throws BusinessException;
	public void excluirNivel(Nivel obj) throws BusinessException;
	public Nivel getNivelById(Long id)throws BusinessException;
	public List<Nivel> listarNivelByFiltro(BasicFiltroNivel filtro) throws BusinessException;
	
	
	/**
	 * 
	 * @param Filial
	 * @return
	 * @throws BusinessException
	 */
	
	public Filial cadastrarFilial(Filial obj) throws BusinessException;
	public Filial alterarFilial(Filial obj) throws BusinessException;
	public void excluirFilial(Filial obj)throws BusinessException;
	public Filial getFilialById(Long id)throws BusinessException;
	public List<Filial> listarFilialByFilter(BasicFiltroFilial filtro)throws BusinessException;
	public Filial getFilialByCodigo(String codigo)throws BusinessException;
	
	/*
	 * Motivo novo
	 */
	public Motivo cadastrarMotivo(Motivo motivo) throws BusinessException;

	public Motivo alterarMotivo(Motivo motivo) throws BusinessException;

	public void excluirMotivo(Motivo motivo) throws BusinessException;

	public Motivo getMotivoById(Long id) throws BusinessException;

	public List<Motivo> listarMotivos(BasicFiltroMotivo filtro)
			throws BusinessException;

	public List<Motivo> listarMotivoPorTipoOcorrencia(BasicFiltroMotivo filtro)
			throws BusinessException;

	public List<Motivo> listarMotivosPorTipoOcorrencia(TipoOcorrencia entity)
			throws BusinessException;

	/* Fachada de Paises, Celso O. Junior */
	public Pais cadastrarPais(Pais pais) throws BusinessException;

	public Pais alterarPais(Pais pais) throws BusinessException;

	public void excluirPais(Pais pais) throws BusinessException;

	public Pais getPaisById(Long id) throws BusinessException;

	public Pais getPaisByPadrao() throws BusinessException;

	public List<Pais> listarPaises() throws BusinessException;

	public List<Pais> listarPaises(BasicFiltroPais filtroPais)
			throws BusinessException;

	// Fachada da classe cidades
	/* Fachada de Estados, Celso O. Junior */
	public Estado cadastrarEstado(Estado estado) throws BusinessException;

	public Estado alterarEstado(Estado estado) throws BusinessException;

	public void excluirEstado(Estado estado) throws BusinessException;

	public Estado getEstadoById(Long id) throws BusinessException;

	public List<Estado> listarEstados(BasicFiltroEstado filtro)
			throws BusinessException;

	/* Fachada de Cidades, Celso O. Junior */
	public Cidade cadastrarCidade(Cidade cidade) throws BusinessException;

	public Cidade alterarCidade(Cidade cidade) throws BusinessException;

	public void excluirCidade(Cidade cidade) throws BusinessException;

	public List<Cidade> listarCidades(BasicFiltroCidade filtro)
			throws BusinessException;

	public Cidade getCidadeById(Long id) throws BusinessException;
	
	//obs
	public Cidade getCityByid(Long id) throws BusinessException;
	

	public List<Cidade> getCidadesByPais(Pais paisDestino)
			throws BusinessException;

	// Fachada terminal
	public Terminal cadastrarTerminal(Terminal terminal)
			throws BusinessException;

	public Terminal alterarTerminal(Terminal terminal) throws BusinessException;

	public Terminal getTerminalById(Long id) throws BusinessException;

	public void excluirTerminal(Terminal terminal) throws BusinessException;

	public List<Terminal> listarTerminais(BasicFiltroTerminal filtro)
			throws BusinessException;

	public List<Terminal> listarTerminais() throws BusinessException;

	/* Fachada de Tipo de Ocorrencia, Celso O. Junior */
	public TipoOcorrencia cadastrarTipoOcorrencia(TipoOcorrencia tipoOcorrencia)
			throws BusinessException;

	public TipoOcorrencia alterarTipoOcorrencia(TipoOcorrencia tipoOcorrencia)
			throws BusinessException;

	public void excluirTipoOcorrencia(TipoOcorrencia tipoOcorrencia)
			throws BusinessException;

	public TipoOcorrencia getTipoOcorrenciaById(Long id)
			throws BusinessException;

	public List<TipoOcorrencia> listarTipoOcorrencias()
			throws BusinessException;

	public List<TipoOcorrencia> listarTipoOcorrencias(
			BasicFiltroTipoOcorrencia filtroTipoOcorrencia)
			throws BusinessException;

	public List<TipoOcorrencia> listTypeOccurrenceImport(
			BasicFiltroTipoOcorrencia filtro) throws BusinessException;

	/* Fachada de Processo, Celso O. Junior */
	public Processo getProcessoById(Long id) throws BusinessException;

	public List<Processo> listarProcessos() throws BusinessException;

	// Fachada de Projetos

	public Projeto cadastrarProjeto(Projeto projeto) throws BusinessException;

	public Projeto alterarProjeto(Projeto projeto) throws BusinessException;

	public List<Projeto> listarProjetos(BasicFiltroProjeto filtro)
			throws BusinessException;

	public Projeto getProjetoById(Long id) throws BusinessException;

	public void excluirProjeto(Projeto projeto) throws BusinessException;

	// Fachada de Feriados

	public Feriado cadastrarFeriado(Feriado feriado) throws BusinessException;

	public Feriado alterarFeriado(Feriado feriado) throws BusinessException;

	public Feriado getFeriadoById(Long id) throws BusinessException;

	public List<Feriado> listarFeriado(BasicFiltroFeriado filtro)
			throws BusinessException;

	public void excluirFeriado(Feriado feriado) throws BusinessException;

	// Fachada de Enderecos

	public Endereco cadastrarEndereco(Endereco endereco)
			throws BusinessException;

	public Endereco alterarEndereco(Endereco endereco) throws BusinessException;

	public Endereco getEnderecoByid(Long id) throws BusinessException;

	public void excluirEndereco(Endereco endereco) throws BusinessException;

	// Fachada de Contatos

	public Contato cadastrarContato(Contato contato) throws BusinessException;

	public Contato alterarContato(Contato contato) throws BusinessException;

	public Contato getContatoById(Long id) throws BusinessException;

	public void excluirContato(Contato contato) throws BusinessException;

	public List<Contato> listarContatosPorPessoa(PessoaJuridica pessoa)
			throws BusinessException;

	// Fachada de Pessoa Juridica

	public PessoaJuridica cadastrarPessoaJuridica(PessoaJuridica pessoa)
			throws BusinessException;

	public PessoaJuridica alterarPessoaJuridica(PessoaJuridica pessoa)
			throws BusinessException;

	public PessoaJuridica getPessoaById(Long id) throws BusinessException;
	
	public PessoaJuridica getPessoaJuridicaBySapVendor(String s) throws BusinessException;
	
	public PessoaJuridica getPessoaBySap(BasicFiltroPessoaJuridica filter) throws BusinessException;

	public void excluirPessoaJuridica(PessoaJuridica pessoa)
			throws BusinessException;

	public List<PessoaJuridica> listarAllPersons(
			BasicFiltroPessoaJuridica filter) throws BusinessException;

	public List<PessoaJuridica> listarByFilter(BasicFiltroPessoaJuridica filter)
			throws BusinessException;

	public List<PessoaJuridica> listarallFornecedores(String fornecedor)
			throws BusinessException;

	public List<PessoaJuridica> listarAllFornecedoresByStatus(Boolean status)
			throws BusinessException;

	public List<PessoaJuridica> listarAllImportadorByStatus(Boolean status)
			throws BusinessException;

	public TipoPessoa cadastrarTypePerson(TipoPessoa type)
			throws BusinessException;

	public TipoPessoa alterarTypePerson(TipoPessoa type)
			throws BusinessException;

	public TipoPessoa getTypePersonById(Long id) throws BusinessException;

	public void excluirTypePerson(TipoPessoa type) throws BusinessException;

	public List<TipoPessoa> listAllTypePersons() throws BusinessException;

	// Fachada de Tipo de contato
	/**
	 * public TipoContato cadastrarTypeContato(TipoContato type) throws
	 * BusinessException;
	 * 
	 * public TipoContato alterarTypeContato(TipoContato type) throws
	 * BusinessException;
	 * 
	 * public TipoContato getTypeContatoById(Long id) throws BusinessException;
	 * 
	 * public List<TipoContato> listAllContacts() throws BusinessException;
	 * 
	 * public void excluirTypeContato(TipoContato type) throws
	 * BusinessException;
	 * 
	 * 
	 * // Fachada de Pessoa Juridica
	 * 
	 * public PessoaJuridica cadastrarPessoaJuridica(PessoaJuridica pessoa)
	 * throws BusinessException;
	 * 
	 * public PessoaJuridica alterarPessoaJuridica(PessoaJuridica pessoa) throws
	 * BusinessException;
	 * 
	 * public PessoaJuridica getPessoaById(Long id) throws BusinessException;
	 * 
	 * public void excluirPessoaJuridica(PessoaJuridica pessoa) throws
	 * BusinessException;
	 * 
	 * public List<PessoaJuridica> listarAllPersons( BasicFiltroPessoaJuridica
	 * filter) throws BusinessException;
	 * 
	 * public List<PessoaJuridica> listarPessoasByTipo(TipoPessoaEnum tipo)
	 * throws BusinessException;
	 * 
	 * public List<PessoaJuridica> listarByFilter(BasicFiltroPessoaJuridica
	 * filter) throws BusinessException;
	 * 
	 * public List<PessoaJuridica> listarallFornecedores(String fornecedor)
	 * throws BusinessException;
	 * 
	 * public List<PessoaJuridica> listarAllFornecedoresByStatus(Boolean status)
	 * throws BusinessException;
	 * 
	 * public List<PessoaJuridica> listarAllImportadorByStatus(Boolean status)
	 * throws BusinessException;
	 * 
	 * // Fachada de CheckList's
	 * 
	 * public CheckList cadastrarCheckList(CheckList check) throws
	 * BusinessException;
	 * 
	 * public CheckList alterarCheckList(CheckList check) throws
	 * BusinessException;
	 * 
	 * public CheckList alterarCheckList(CheckList checkList, List<DataVigencia>
	 * datas) throws BusinessException;
	 * 
	 * public CheckList getCheckListById(Long id) throws BusinessException;
	 * 
	 * public void excluirCheckList(CheckList check) throws BusinessException;
	 * 
	 * public List<CheckList> listCheckListByFilter(BasicFiltroCheckList check)
	 * throws BusinessException;
	 * 
	 * public List<CheckList> listCheckList() throws BusinessException;
	 * 
	 * public List<ItemCheckList> listaItensCheckListByFilter(
	 * BasicFiltroCheckList filtro) throws BusinessException;
	 * 
	 * // fachada de item checkList
	 * 
	 * public ItemCheckList cadastrarItemCheckList(ItemCheckList item) throws
	 * BusinessException;
	 * 
	 * public ItemCheckList alterarItemCheckList(ItemCheckList item) throws
	 * BusinessException;
	 * 
	 * public ItemCheckList getItemCheckListById(Long id) throws
	 * BusinessException;
	 * 
	 * public void excluirItemCheckList(ItemCheckList item) throws
	 * BusinessException;
	 * 
	 * public List<ItemCheckList> listItensCheckList() throws BusinessException;
	 * 
	 * // mini Fachada de tipo de check List @wisley.souza
	 * 
	 * public List<CheckList> listTypesOfCheckList() throws BusinessException;
	 * 
	 * /**
	 * 
	 * @comentario fachada de Subprocessos
	 * @return
	 * @throws BusinessException
	 */
	/**
	 * public List<SubProcesso> listProcessos() throws BusinessException;
	 * 
	 * public SubProcesso getSubProcessoById(Long id) throws BusinessException;
	 * 
	 * public SubProcesso getSubProcessoByNome(String s) throws
	 * BusinessException;
	 * 
	 * public List<Cidade> getCidadesByPais(Pais paisDestino) throws
	 * BusinessException;
	 * 
	 * /**
	 * 
	 * @comentario fachada de DataVigencia
	 * 
	 */
	public DataVigencia cadastrarDataVigente(DataVigencia data)
			throws BusinessException, SQLIntegrityConstraintViolationException;

	public void rollback(DataVigencia data) throws BusinessException,
			SQLIntegrityConstraintViolationException;

	/**
	 * ROTAS
	 */
	public List<Rota> listarRotas(BasicFiltroRota filtro)
			throws BusinessException;

	public Rota alterarRota(Rota rota) throws BusinessException;

	public Rota cadastrarRota(Rota rota) throws BusinessException;

	public void excluirRota(Rota rota) throws BusinessException;

	public void excluirTrecho(Long idTrecho) throws BusinessException;

	public Rota getRotaById(Long idRegistro) throws BusinessException;

	/**
	 * 
	 * Trechos
	 * 
	 */

	public Trecho getTrechoById(Long id) throws BusinessException;

	/*
	 * Parametros Canais
	 */
	/**
	 * public List<ParametroCanal> listarParametroCanais() throws
	 * BusinessException;
	 * 
	 * public ParametroCanal getParametroCanais(Canal canal) throws
	 * BusinessException;
	 * 
	 * public ParametroCanal alterarParametroCanal(ParametroCanal entity) throws
	 * BusinessException;
	 * 
	 * public void validarRota(Rota rota) throws BusinessException;
	 * 
	 * public List<Cidade> getCidadesByFollowUp(FollowUpImport followUp) throws
	 * BusinessException;
	 * 
	 * public List<Rota> listarRotasTrechos(BasicFiltroRota filtro) throws
	 * BusinessException;
	 * 
	 * /** Departamento
	 */

	public List<Departamento> listarDepartamentos(BasicFiltroDepartamento filtro)
			throws BusinessException;

	public Departamento getDepartamentoById(Long id) throws BusinessException;

	public Departamento alterarDepartamento(Departamento obj)
			throws BusinessException;

	public Departamento cadastrarDepartamento(Departamento obj)
			throws BusinessException;

	public void excluirDepartamento(Departamento obj) throws BusinessException;

	/**
	 * Moeda
	 * 
	 * @author Francisco Januario
	 * @date 24/11/2011
	 */

	public Moeda cadastrarMoeda(Moeda moeda) throws BusinessException;

	public Moeda alterarMoeda(Moeda moeda) throws BusinessException;

	public void excluirMoeda(Moeda moeda) throws BusinessException;

	public Moeda getMoedaById(Long id) throws BusinessException;
	
	public Moeda getMoedaBySigla(String sigla) throws BusinessException;

	public List<Moeda> listarMoedas() throws BusinessException;

	public List<Moeda> listarMoedas(BasicFiltroMoeda filtroMoeda)
			throws BusinessException;

	/**
	 * Modal
	 * 
	 * @author Francisco Januario
	 * @date 24/11/2011
	 */

	public Modal cadastrarModal(Modal modal) throws BusinessException;

	public Modal alterarModal(Modal modal) throws BusinessException;

	public void excluirModal(Modal modal) throws BusinessException;

	public Modal getModalById(Long id) throws BusinessException;

	public List<Modal> listarModals() throws BusinessException;

	public List<Modal> listarModals(BasicFiltroModal filtroModal)
			throws BusinessException;

	public List<Cidade> getCidadesByRota(Rota rota) throws BusinessException;

	// public List<Projeto> listarProjetosPorInvoices(List<Invoice>
	// listaInvoices) throws BusinessException;

	public List<Frete> listarFrete(BasicFiltroFrete filtro)
			throws BusinessException;

	public void excluirFrete(Long id) throws BusinessException;

	public Frete getFreteById(Long id) throws BusinessException;

	public Frete getFreteByRota(Rota rota) throws BusinessException;

	public Frete cadastrarFrete(Frete frete) throws BusinessException;

	public Frete alterarFrete(Frete frete) throws BusinessException;

	public int getFeriadosUteis(Date inicial, Date dtFinal, Pais idPais)
			throws BusinessException;

	/**
	 * Materia Prima
	 * 
	 * @author Manoel Neto
	 * @date 15/02/2012
	 */

	public MateriaPrima cadastrarMateriaPrima(MateriaPrima entity)
			throws BusinessException;

	public MateriaPrima alterarMateriaPrima(MateriaPrima entity)
			throws BusinessException;

	public void excluirMateriaPrima(MateriaPrima entity)
			throws BusinessException;

	public MateriaPrima getMateriaPrimaById(Long id) throws BusinessException;

	public List<MateriaPrima> listarMateriaPrima(
			BasicFiltroMateriaPrima materiaPrima) throws BusinessException;
	
	public List<MateriaPrima> listarMateriaPrima(MateriaPrima materiaPrima)
			throws BusinessException;
	
	
	
	
	

	public List<Projeto> listarAtivos() throws BusinessException;

	/**
	 * Metodos de Incoterms para o CRUD.
	 * 
	 * @param idRegistro
	 * @return
	 * @throws BusinessException
	 */
	public Incoterm getIncotermById(Long idRegistro) throws BusinessException;

	public void excluirIncotermById(Incoterm entity) throws BusinessException;

	public Incoterm cadastrarIncoterm(Incoterm entity) throws BusinessException;

	public Incoterm alterarIncoterm(Incoterm entity) throws BusinessException;

	public List<Incoterm> listarIncoterm() throws BusinessException;

	public List<Incoterm> listarIncoterm(BasicFiltroIncoterm filtro)
			throws BusinessException;

	public TipoPacote getTipoPacoteById(Long idRegistro)
			throws BusinessException;

	public TipoPacote cadastrarTipoPacote(TipoPacote entity)
			throws BusinessException;

	public TipoPacote alterarTipoPacote(TipoPacote entity)
			throws BusinessException;

	public List<TipoPacote> listarTipoPacote(BasicFiltroTipoPacote filtro)
			throws BusinessException;

	public void excluirTipoPacote(TipoPacote entity) throws BusinessException;

	// Fachada de CheckList's

	public CheckList cadastrarCheckList(CheckList check)
			throws BusinessException;

	public CheckList alterarCheckList(CheckList check) throws BusinessException;

	public CheckList alterarCheckList(CheckList checkList,
			List<DataVigencia> datas) throws BusinessException;

	public CheckList getCheckListById(Long id) throws BusinessException;

	public void excluirCheckList(CheckList check) throws BusinessException;

	public List<CheckList> listCheckListByFilter(BasicFiltroCheckList check)
			throws BusinessException;

	public List<CheckList> listCheckList() throws BusinessException;

	public List<ItemChecklist> listaItensCheckListByFilter(
			BasicFiltroCheckList filtro) throws BusinessException;

	// fachada de item checkList

	public ItemChecklist cadastrarItemCheckList(ItemChecklist item)
			throws BusinessException;

	public ItemChecklist alterarItemCheckList(ItemChecklist item)
			throws BusinessException;

	public ItemChecklist getItemCheckListById(Long id) throws BusinessException;

	public void excluirItemCheckList(ItemChecklist item)
			throws BusinessException;

	public List<ItemChecklist> listItensCheckList() throws BusinessException;

	// mini Fachada de tipo de check List @wisley.souza

	public List<CheckList> listTypesOfCheckList() throws BusinessException;

	public List<PessoaJuridica> listarPessoasByTipo(TipoPessoaEnum tipo)
			throws BusinessException;

	public List<Processo> listarProcessosNotMotivo(Motivo entity)
			throws BusinessException;

	public List<Processo> listarProcessosByOcorrencia(TipoOcorrencia entity)
			throws BusinessException;

	public List<Processo> listarProcessosNotOcorrencia(TipoOcorrencia entity)
			throws BusinessException;

	public List<Processo> listarProcessosByMotivo(Motivo entity)
			throws BusinessException;

	/**
	 * @author Manoel Neto date 19/03/2012 Parametros Continentes
	 */
	/**
	 * public List<ParametroContinente> listarParametroContinentes() throws
	 * BusinessException;
	 * 
	 * public ParametroContinente getParametroContinente(Continente continente)
	 * throws BusinessException;
	 * 
	 * public ParametroContinente alterarParametroContinente(ParametroContinente
	 * entity) throws BusinessException;
	 **/

	
	
	
	//Carga
	public List<Projeto> listarProjetosPorInvoices(List<Invoice> listaInvoices)
			throws BusinessException;
	
	
	
	public List<Cidade> getCidadesByFollowUp(FollowUpImport followUp)
			throws BusinessException;
	
	
	/*
	 * Parametros Canais
	 */
	public List<ParametroCanal> listarParametroCanais()
			throws BusinessException;

	public ParametroCanal getParametroCanais(Canal canal)
			throws BusinessException;

	public ParametroCanal alterarParametroCanal(ParametroCanal entity)
			throws BusinessException;

	public void validarRota(Rota rota) throws BusinessException;

	public List<Rota> listarRotasTrechos(BasicFiltroRota filtro)
			throws BusinessException;

	public List<ParametroContinente> listarParametroContinente() throws BusinessException;

	public ParametroContinente alterarParametroContinente(ParametroContinente continente ) throws BusinessException;

	public int getFeriadosUteis(Date inicial, Date dtFinal, Long pais)
			throws BusinessException;
	
	/**
	 * FORMA DE PAGAMENTO
	 */
	/**
	 * @param codeSAP
	 * @return
	 * @throws BusinessException 
	 */
	public FormaPagamento getFormaPagamentoBycodeSAP(String codeSAP) throws BusinessException;
	/**
	 * @return
	 * @throws BusinessException
	 */
	public List<FormaPagamento> listarFormasPagamentoAtivos() throws BusinessException;
	/**
	 * @param codigo
	 * @param cadastro
	 * @return
	 * @throws BusinessException
	 */
	public UnidadeMedida getUnidMedidaByCodigo(String codigo, boolean cadastro)
			throws BusinessException;
	/**
	 * @param sigla
	 * @param cadastro
	 * @return
	 * @throws BusinessException
	 */
	public Incoterm getIncotermBySigla(String sigla, boolean cadastro)
			throws BusinessException;
	
	public Moeda getMoedaPadrao() throws BusinessException;
	
	public TipoPacote getTipoPacoteByCodigo(String string) throws BusinessException;
	
	public List<TipoPacote> getTipoPacoteByCategoria(String string) throws BusinessException;
	public ParametroImposto getParametroImposto(String descricao)
			throws BusinessException;
	
	
	
}
