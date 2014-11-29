package br.com.ilog.cadastro.business.facade;

import java.io.Serializable;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.NoResultException;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
import br.com.ilog.cadastro.business.rep.CanalRepository;
import br.com.ilog.cadastro.business.rep.CheckListRepository;
import br.com.ilog.cadastro.business.rep.CidadeRepository;
import br.com.ilog.cadastro.business.rep.ContinenteRepository;
import br.com.ilog.cadastro.business.rep.DepartamentoRepository;
import br.com.ilog.cadastro.business.rep.EnderecoRepository;
import br.com.ilog.cadastro.business.rep.EstadoRepository;
import br.com.ilog.cadastro.business.rep.FeriadoRepository;
import br.com.ilog.cadastro.business.rep.FilialRepository;
import br.com.ilog.cadastro.business.rep.FreteRepository;
import br.com.ilog.cadastro.business.rep.IncotermRepository;
import br.com.ilog.cadastro.business.rep.MateriaPrimaRepository;
import br.com.ilog.cadastro.business.rep.ModalRepository;
import br.com.ilog.cadastro.business.rep.MoedaRepository;
import br.com.ilog.cadastro.business.rep.MotivoRepository;
import br.com.ilog.cadastro.business.rep.NivelRepository;
import br.com.ilog.cadastro.business.rep.PaisRepository;
import br.com.ilog.cadastro.business.rep.ParametroImpostoRepository;
import br.com.ilog.cadastro.business.rep.PessoaJuridicaRepository;
import br.com.ilog.cadastro.business.rep.ProcessoRepository;
import br.com.ilog.cadastro.business.rep.ProjetoRepository;
import br.com.ilog.cadastro.business.rep.RotaRepository;
import br.com.ilog.cadastro.business.rep.TerminalRepository;
import br.com.ilog.cadastro.business.rep.TipoPacoteRepository;
import br.com.ilog.cadastro.business.rep.TipoPessoaRepository;
import br.com.ilog.cadastro.business.rep.TrechoRepository;
import br.com.ilog.cadastro.business.rep.UnidadeMedidaRepository;
import br.com.ilog.importacao.business.entity.FollowUpImport;
import br.com.ilog.importacao.business.entity.Invoice;
import br.com.ilog.importacao.business.rep.FormaPagamentoRepository;

@Component("controllerCadastro")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = BusinessException.class)
public class CadastroFacadeImpl implements CadastroFacade, Serializable {

	/** */
	private static final long serialVersionUID = -3831130939388543469L;

	@Resource
	EstadoRepository estadoRepository;

	@Resource
	CidadeRepository cidadeRepository;

	@Resource
	TerminalRepository terminalRepository;

	@Resource
	ProjetoRepository projetoRepository;
	@Resource
	ModalRepository modalRepository;

	@Resource
	MoedaRepository moedaRepository;

	@Resource
	MateriaPrimaRepository materiaPrimaRepository;

	@Resource
	IncotermRepository incotermRepository;

	@Resource
	DepartamentoRepository departamentoRepository;

	@Resource
	PaisRepository paisReposytory;

	@Resource
	TipoPacoteRepository tipoPacoteRepository;

	@Resource
	TipoOcorrenciaRepository tipoOcorrenciaRepository;

	@Resource
	ProcessoRepository processoRepository;

	@Resource
	MotivoRepository motivoRepository;

	@Resource
	FeriadoRepository feriadoRepository;

	@Resource
	CheckListRepository checkListRepository;

	@Resource
	PessoaJuridicaRepository pessoaRepository;

	@Resource
	TipoPessoaRepository typePersonRepository;

	@Resource
	RotaRepository rotaRepository;

	@Resource
	TrechoRepository trechoRepository;

	@Resource
	FreteRepository freteRepository;

	@Resource
	EnderecoRepository enderecoRepository;

	@Resource
	CanalRepository parametroCanalRepository;
	
	@Resource
	CanalRepository canalRepository;
	
	@Resource
	ContinenteRepository continenteRepository;
	
	@Resource
	FilialRepository filialRepository;
	
	@Resource
	NivelRepository nivelRepository;
	
	@Resource
	UnidadeMedidaRepository unidMedidaRepository;
	
	@Resource
	FormaPagamentoRepository formaPagamentoRepository;

	@Resource
	ParametroImpostoRepository parametroImpostoRepository;

	@Override
	public Pais cadastrarPais(Pais pais) throws BusinessException {
		// UPPER CASE
		pais.setNome(pais.getNome().toUpperCase());
		pais.setSigla(pais.getSigla().toUpperCase());

		return paisReposytory.cadastrar(pais);
	}

	@Override
	public Pais alterarPais(Pais pais) throws BusinessException {

		// UPPER CASE
		pais.setNome(pais.getNome().toUpperCase());
		pais.setSigla(pais.getSigla().toUpperCase());

		return paisReposytory.alterar(pais);
	}

	@Override
	public void excluirPais(Pais pais) throws BusinessException {
		paisReposytory.excluirById(pais.getId());
	}

	@Override
	public List<Pais> listarPaises() throws BusinessException {
		return paisReposytory.listarPaises(null);
	}

	@Override
	public List<Pais> listarPaises(BasicFiltroPais filtroPais)
			throws BusinessException {
		return paisReposytory.listarPaises(filtroPais);

	}

	@Override
	public Pais getPaisById(Long id) throws BusinessException {

		return paisReposytory.getById(id);
	}

	@Override
	public Pais getPaisByPadrao() throws BusinessException {

		return paisReposytory.getByPadrao();
	}

	/**
	 * fachada de endereços
	 */

	@Override
	public Endereco alterarEndereco(Endereco endereco) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Endereco cadastrarEndereco(Endereco endereco)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void excluirEndereco(Endereco endereco) throws BusinessException {
		// TODO Auto-generated method stub

	}

	@Override
	public Cidade cadastrarCidade(Cidade cidade) throws BusinessException {

		return cidadeRepository.cadastrar(cidade);
	}

	@Override
	public Cidade alterarCidade(Cidade cidade) throws BusinessException {
		// TODO Auto-generated method stub
		return cidadeRepository.alterar(cidade);
	}

	@Override
	public void excluirCidade(Cidade cidade) throws BusinessException {
		cidadeRepository.excluirById( cidade.getId() );
	}

	@Override
	public List<Cidade> listarCidades(BasicFiltroCidade filtro)
			throws BusinessException {
		return cidadeRepository.listarCidades(filtro);
	}

	@Override
	public Cidade getCidadeById(Long id) throws BusinessException {
		return cidadeRepository.getById(id);
	}

	@Override
	public List<TipoOcorrencia> listTypeOccurrenceImport(
			BasicFiltroTipoOcorrencia filtro) throws BusinessException {
		return tipoOcorrenciaRepository.listarTipoOcorrenciaImportacao(filtro);
	}

	@Override
	public Feriado cadastrarFeriado(Feriado feriado) throws BusinessException {
		return feriadoRepository.cadastrar(feriado);
	}

	@Override
	public Feriado alterarFeriado(Feriado feriado) throws BusinessException {
		// TODO Auto-generated method stub
		return feriadoRepository.alterar(feriado);
	}

	@Override
	public Feriado getFeriadoById(Long id) throws BusinessException {
		
		return feriadoRepository.getById( id );
	}

	@Override
	public List<Feriado> listarFeriado(BasicFiltroFeriado filtro)
			throws BusinessException {
		// TODO Auto-generated method stub
		return feriadoRepository.listaFeriados(filtro);
	}

	@Override
	public void excluirFeriado(Feriado feriado) throws BusinessException {
		feriadoRepository.excluirById(feriado.getId());

	}

	@Override
	public Endereco getEnderecoByid(Long id) throws BusinessException {
		return enderecoRepository.getById(id);
	}

	@Override
	public Contato cadastrarContato(Contato contato) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Contato alterarContato(Contato contato) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Contato getContatoById(Long id) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void excluirContato(Contato contato) throws BusinessException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Contato> listarContatosPorPessoa(PessoaJuridica pessoa)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataVigencia cadastrarDataVigente(DataVigencia data)
			throws BusinessException, SQLIntegrityConstraintViolationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void rollback(DataVigencia data) throws BusinessException,
			SQLIntegrityConstraintViolationException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Departamento> listarDepartamentos(BasicFiltroDepartamento filtro)
			throws BusinessException {
		return departamentoRepository.listDepartamentos(filtro);
	}

	@Override
	public Departamento getDepartamentoById(Long id) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Departamento alterarDepartamento(Departamento obj)
			throws BusinessException {
		return departamentoRepository.alterar(obj);
	}

	@Override
	public Departamento cadastrarDepartamento(Departamento obj)
			throws BusinessException {
		return departamentoRepository.cadastrar(obj);
	}

	@Override
	public void excluirDepartamento(Departamento obj) throws BusinessException {
		departamentoRepository.excluirById(obj.getId());

	}

	@Override
	public List<Cidade> getCidadesByRota(Rota rota) throws BusinessException {
		rota = getRotaById(rota.getId());

		return cidadeRepository.getCidadesByRota(rota);
	}

	@Override
	public List<Frete> listarFrete(BasicFiltroFrete filtro)
			throws BusinessException {
		// TODO Auto-generated method stub
		return freteRepository.listar(filtro);
	}

	@Override
	public void excluirFrete(Long id) throws BusinessException {
		freteRepository.excluirById(id);

	}

	@Override
	public Frete getFreteById(Long id) throws BusinessException {
	
		return freteRepository.getById(id);
	}

	@Override
	public Frete getFreteByRota(Rota rota) throws BusinessException {
		
		return freteRepository.getByRota(rota);
	}

	@Override
	public Frete cadastrarFrete(Frete frete) throws BusinessException {
		frete.setAtivo(true);
		return freteRepository.cadastrar(frete);
	}

	@Override
	public Frete alterarFrete(Frete frete) throws BusinessException {
		// TODO Auto-generated method stub
		return freteRepository.alterar(frete);
	}

	@Override
	public int getFeriadosUteis(Date inicial, Date dtFinal, Long idPais)
			throws BusinessException {
		Pais pais = this.getPaisById(idPais);

		return getFeriadosUteis(inicial, dtFinal, pais);
	}

	/**
	 * Rota, metodos para relizacao de operacaoes de persistencia e consultas.
	 * 
	 * @author Heber Santiago
	 */
	@Override
	public List<Rota> listarRotas(BasicFiltroRota filtro)
			throws BusinessException {
		return rotaRepository.listar(filtro);
	}

	@Override
	public Rota alterarRota(Rota rota) throws BusinessException {
		return rotaRepository.alterar(rota);
	}

	@Override
	public Rota cadastrarRota(Rota rota) throws BusinessException {
		rota.setAtivo(true);
		return rotaRepository.cadastrar(rota);
	}

	@Override
	public void excluirRota(Rota rota) throws BusinessException {
		rotaRepository.excluirById(rota.getId());
	}

	@Override
	public void excluirTrecho(Long idTrecho) throws BusinessException {
		// TODO Auto-generated method stub

	}

	@Override
	public Rota getRotaById(Long idRegistro) throws BusinessException {
		return rotaRepository.getById(idRegistro);
	}

	@Override
	public Trecho getTrechoById(Long id) throws BusinessException {
		return trechoRepository.getById(id);
	}

	/**
	 * Tipo Ocorrencia, metodos para relizacao de operacaoes de persistencia e
	 * consultas.
	 * 
	 * @author Heber Santiago
	 */
	@Override
	public TipoOcorrencia cadastrarTipoOcorrencia(TipoOcorrencia tipoOcorrencia)
			throws BusinessException {
		tipoOcorrencia.setAtivo(true);
		return tipoOcorrenciaRepository.cadastrar(tipoOcorrencia);
	}

	@Override
	public TipoOcorrencia alterarTipoOcorrencia(TipoOcorrencia tipoOcorrencia)
			throws BusinessException {
		return tipoOcorrenciaRepository.alterar(tipoOcorrencia);
	}

	@Override
	public void excluirTipoOcorrencia(TipoOcorrencia tipoOcorrencia)
			throws BusinessException {
		tipoOcorrenciaRepository.excluirById(tipoOcorrencia.getId());
	}

	@Override
	public TipoOcorrencia getTipoOcorrenciaById(Long id)
			throws BusinessException {
		return tipoOcorrenciaRepository.getById(id);
	}

	@Override
	public List<TipoOcorrencia> listarTipoOcorrencias()
			throws BusinessException {
		return tipoOcorrenciaRepository.listar();
	}

	@Override
	public List<TipoOcorrencia> listarTipoOcorrencias(
			BasicFiltroTipoOcorrencia filtroTipoOcorrencia)
			throws BusinessException {
		return tipoOcorrenciaRepository.listar(filtroTipoOcorrencia);
	}

	/**
	 * Processo, metodos para relizacao de operacaoes de persistencia e
	 * consultas.
	 * 
	 * @author Heber Santiago
	 */
	@Override
	public Processo getProcessoById(Long id) throws BusinessException {
		return processoRepository.getById(id);
	}

	@Override
	public List<Processo> listarProcessos() throws BusinessException {
		return processoRepository.listar();
	}

	@Override
	public List<Processo> listarProcessosByOcorrencia(TipoOcorrencia entity)
			throws BusinessException {
		return processoRepository.listarByOcorrencia(entity);
	}

	@Override
	public List<Processo> listarProcessosNotOcorrencia(TipoOcorrencia entity)
			throws BusinessException {
		return processoRepository.listarNotOcorrencia(entity);
	}

	@Override
	public List<Processo> listarProcessosByMotivo(Motivo entity)
			throws BusinessException {
		return processoRepository.listarByMotivo(entity);
	}

	@Override
	public List<Processo> listarProcessosNotMotivo(Motivo entity)
			throws BusinessException {
		return processoRepository.listarNotMotivo(entity);
	}

	/**
	 * Motivo, metodos para relizacao de operacaoes de persistencia e consultas.
	 * 
	 * @author Heber Santiago
	 */
	@Override
	public Motivo cadastrarMotivo(Motivo entity) throws BusinessException {
		entity.setAtivo(true);
		return motivoRepository.cadastrar(entity);
	}

	@Override
	public Motivo alterarMotivo(Motivo motivo) throws BusinessException {
		return motivoRepository.alterar(motivo);
	}

	@Override
	public void excluirMotivo(Motivo motivo) throws BusinessException {
		motivoRepository.excluirById(motivo.getId());
	}

	@Override
	public Motivo getMotivoById(Long id) throws BusinessException {
		return motivoRepository.getById(id);
	}

	@Override
	public List<Motivo> listarMotivos(BasicFiltroMotivo filtro)
			throws BusinessException {
		return motivoRepository.listar(filtro);
	}

	@Override
	public List<Motivo> listarMotivoPorTipoOcorrencia(BasicFiltroMotivo filtro)
			throws BusinessException {
	 return motivoRepository.listarMotivo(filtro);
	}

	@Override
	public List<Motivo> listarMotivosPorTipoOcorrencia(TipoOcorrencia entity)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Terminal, metodos para relizacao de operacaoes de persistencia e
	 * consultas.
	 * 
	 * @author Heber Santiago
	 */
	@Override
	public Terminal cadastrarTerminal(Terminal terminal)
			throws BusinessException {
		return terminalRepository.cadastrar(terminal);
	}

	@Override
	public Terminal alterarTerminal(Terminal terminal) throws BusinessException {
		return terminalRepository.alterar(terminal);
	}

	@Override
	public Terminal getTerminalById(Long id) throws BusinessException {
		return terminalRepository.getById(id);
	}

	@Override
	public void excluirTerminal(Terminal terminal) throws BusinessException {
		terminalRepository.excluirById(terminal.getId());
	}

	@Override
	@Transactional(readOnly=true)
	public List<Terminal> listarTerminais(BasicFiltroTerminal filtro)
			throws BusinessException {
		return terminalRepository.listarTerminais(filtro);
	}

	@Override
	public List<Terminal> listarTerminais() throws BusinessException {
		return terminalRepository.listar();
	}

	/**
	 * Estado, metodos para relizacao de operacaoes de persistencia e consultas.
	 * 
	 * @author Heber Santiago
	 */
	@Override
	public Estado cadastrarEstado(Estado estado) throws BusinessException {
		return estadoRepository.cadastrar(estado);
	}

	@Override
	public Estado alterarEstado(Estado estado) throws BusinessException {
		return estadoRepository.alterar(estado);
	}

	@Override
	public void excluirEstado(Estado estado) throws BusinessException {

		estado = this.getEstadoById(estado.getId());

		estadoRepository.excluirById(estado.getId());
	}

	@Override
	public Estado getEstadoById(Long id) throws BusinessException {
		return estadoRepository.getById(id);
	}

	@Override
	public List<Estado> listarEstados(BasicFiltroEstado filtro)
			throws BusinessException {
		return estadoRepository.listarEstados(filtro);
	}

	/**
	 * Projeto, metodos para relizacao de operacaoes de persistencia e
	 * consultas.
	 * 
	 * @author Heber Santiago
	 */
	@Override
	public Projeto cadastrarProjeto(Projeto projeto) throws BusinessException {
		// Sempre salvar como ativo
		projeto.setAtivo(true);
		return projetoRepository.cadastrar(projeto);
	}

	@Override
	public Projeto alterarProjeto(Projeto projeto) throws BusinessException {
		return projetoRepository.alterar(projeto);
	}

	@Override
	public List<Projeto> listarProjetos(BasicFiltroProjeto filtro)
			throws BusinessException {
		return projetoRepository.listar(filtro);
	}

	@Override
	public List<Projeto> listarAtivos() throws BusinessException {
		return projetoRepository.listarAtivos();
	}

	@Override
	public Projeto getProjetoById(Long id) throws BusinessException {
		return projetoRepository.getById(id);
	}

	@Override
	public void excluirProjeto(Projeto projeto) throws BusinessException {
		projetoRepository.excluirById(projeto.getId());
	}

	/**
	 * Moeda, metodos para relizacao de operacaoes de persistencia e consultas.
	 * 
	 * @author Heber Santiago
	 */
	@Override
	public Moeda cadastrarMoeda(Moeda moeda) throws BusinessException {
		moeda.setSigla(moeda.getSigla().toUpperCase());
		moeda.setDescricao(moeda.getDescricao().toUpperCase());
		moeda.setAtivo(true);
		moeda.setMoedaPadrao( false );
		
		return moedaRepository.cadastrar(moeda);
	}

	@Override
	public Moeda alterarMoeda(Moeda moeda) throws BusinessException {
		moeda.setSigla(moeda.getSigla().toUpperCase());
		moeda.setDescricao(moeda.getDescricao().toUpperCase());

		if ( moeda.getMoedaPadrao() == null ) {
			moeda.setMoedaPadrao( false );
		}
		return moedaRepository.alterar(moeda);
	}

	@Override
	public void excluirMoeda(Moeda moeda) throws BusinessException {
		moedaRepository.excluirById(moeda.getId());
	}

	@Override
	@Transactional(readOnly=true)
	public List<Moeda> listarMoedas() throws BusinessException {
		return moedaRepository.listar();
	}

	@Override
	public List<Moeda> listarMoedas(BasicFiltroMoeda filtroMoeda)
			throws BusinessException {
		return moedaRepository.listarMoedas(filtroMoeda);
	}

	@Override
	public Moeda getMoedaById(Long id) throws BusinessException {
		Moeda moeda = moedaRepository.getById(id);
		if ( moeda.getMoedaPadrao() == null ) {
			moeda.setMoedaPadrao( false );
		}
		return moeda;
	}
	@Override
	public Moeda getMoedaBySigla(String sigla) throws BusinessException {
		return moedaRepository.getBySigla(sigla);
	}

	/**
	 * Modal, metodos para relizacao de operacaoes de persistencia e consultas.
	 * 
	 * @author Heber Santiago
	 */
	@Override
	public Modal cadastrarModal(Modal modal) throws BusinessException {
		modal.setAtivo(true);
		return modalRepository.cadastrar(modal);
	}

	@Override
	public Modal alterarModal(Modal modal) throws BusinessException {
		return modalRepository.alterar(modal);
	}

	@Override
	public void excluirModal(Modal modal) throws BusinessException {

		modalRepository.excluirById(modal.getId());
	}

	@Override
	public Modal getModalById(Long id) throws BusinessException {
		return modalRepository.getById(id);
	}

	@Override
	@Transactional(readOnly=true)
	public List<Modal> listarModals() throws BusinessException {
		return modalRepository.listar();
	}

	@Override
	public List<Modal> listarModals(BasicFiltroModal filtroModal)
			throws BusinessException {
		return modalRepository.listar(filtroModal);
	}

	/**
	 * Materia Prima, metodos para relizacao de operacaoes de persistencia e
	 * consultas.
	 * 
	 * @author Heber Santiago
	 */
	@Override
	public MateriaPrima cadastrarMateriaPrima(MateriaPrima entity)
			throws BusinessException {

		entity.setAtivo(true);
		return materiaPrimaRepository.cadastrar(entity);
	}

	@Override
	public MateriaPrima alterarMateriaPrima(MateriaPrima entity)
			throws BusinessException {
		return materiaPrimaRepository.alterar(entity);
	}

	@Override
	public void excluirMateriaPrima(MateriaPrima entity)
			throws BusinessException {

		materiaPrimaRepository.excluirById(entity.getId());
	}

	@Override
	public MateriaPrima getMateriaPrimaById(Long id) throws BusinessException {
		return materiaPrimaRepository.getById(id);
	}

	@Override
	public List<MateriaPrima> listarMateriaPrima(BasicFiltroMateriaPrima filtro)
			throws BusinessException {
		return materiaPrimaRepository.listar(filtro);
	}

	/**
	 * Incoterm, metodos para relizacao de operacaoes de persistencia e
	 * consultas.
	 * 
	 * @author Heber Santiago
	 */
	@Override
	public Incoterm getIncotermById(Long idRegistro) throws BusinessException {
		return incotermRepository.getById(idRegistro);
	}

	@Override
	public void excluirIncotermById(Incoterm entity) throws BusinessException {
		incotermRepository.excluirById(entity.getId());
	}

	@Override
	public Incoterm cadastrarIncoterm(Incoterm entity) throws BusinessException {
		entity.setAtivo(true);
		return incotermRepository.cadastrar(entity);
	}

	@Override
	public Incoterm alterarIncoterm(Incoterm entity) throws BusinessException {
		return incotermRepository.alterar(entity);
	}

	@Override
	public List<Incoterm> listarIncoterm() throws BusinessException {
		return incotermRepository.listar();
	}

	@Override
	public List<Incoterm> listarIncoterm(BasicFiltroIncoterm filtro)
			throws BusinessException {
		return incotermRepository.listar(filtro);
	}

	/**
	 * Tipo Pacote, metodos para relizacao de operacaoes de persistencia e
	 * consultas.
	 * 
	 * @author Heber Santiago
	 */
	@Override
	public TipoPacote getTipoPacoteById(Long idRegistro)
			throws BusinessException {
		return tipoPacoteRepository.getById(idRegistro);
	}

	@Override
	public void excluirTipoPacote(TipoPacote entity) throws BusinessException {
		tipoPacoteRepository.excluirById(entity.getId());
	}

	@Override
	public TipoPacote cadastrarTipoPacote(TipoPacote entity)
			throws BusinessException {
		entity.setAtivo(true);
		return tipoPacoteRepository.cadastrar(entity);
	}

	@Override
	public TipoPacote alterarTipoPacote(TipoPacote entity)
			throws BusinessException {
		return tipoPacoteRepository.alterar(entity);
	}

	@Override
	public List<TipoPacote> listarTipoPacote(BasicFiltroTipoPacote filtro)
			throws BusinessException {
		return tipoPacoteRepository.listar(filtro);
	}

	/**
	 * Cadastro de checklist
	 */
	@Override
	public CheckList cadastrarCheckList(CheckList check)
			throws BusinessException {

		return checkListRepository.cadastrar(check);
	}

	@Override
	public CheckList alterarCheckList(CheckList check) throws BusinessException {

		return checkListRepository.alterar(check);
	}

	@Override
	public CheckList alterarCheckList(CheckList checkList,
			List<DataVigencia> datas) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CheckList getCheckListById(Long id) throws BusinessException {
		// TODO Auto-generated method stub
		return checkListRepository.getById(id);
	}

	@Override
	public void excluirCheckList(CheckList check) throws BusinessException {
		checkListRepository.excluirById(check.getId());
	}

	@Override
	@Transactional(readOnly=true)
	public List<CheckList> listCheckListByFilter(BasicFiltroCheckList check)
			throws BusinessException {
		// TODO Auto-generated method stub
		return checkListRepository.listarTypesOfCheckList(check);
	}

	@Override
	public List<CheckList> listCheckList() throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ItemChecklist> listaItensCheckListByFilter(
			BasicFiltroCheckList filtro) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemChecklist cadastrarItemCheckList(ItemChecklist item)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemChecklist alterarItemCheckList(ItemChecklist item)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemChecklist getItemCheckListById(Long id) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void excluirItemCheckList(ItemChecklist item)
			throws BusinessException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<ItemChecklist> listItensCheckList() throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CheckList> listTypesOfCheckList() throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PessoaJuridica cadastrarPessoaJuridica(PessoaJuridica pessoa)
			throws BusinessException {
		return pessoaRepository.cadastrar(pessoa);
	}

	@Override
	public PessoaJuridica alterarPessoaJuridica(PessoaJuridica pessoa)
			throws BusinessException {
		// TODO Auto-generated method stub
		return pessoaRepository.alterar(pessoa);
	}

	@Override
	@Transactional(readOnly=true)
	public PessoaJuridica getPessoaById(Long id) throws BusinessException {
		// TODO Auto-generated method stub
		return pessoaRepository.getById(id);
	}

	@Override
	public void excluirPessoaJuridica(PessoaJuridica pessoa)
			throws BusinessException {
		pessoaRepository.excluirById(pessoa.getId());
	}

	@Override
	public List<PessoaJuridica> listarAllPersons(
			BasicFiltroPessoaJuridica filter) throws BusinessException {
		return pessoaRepository.listarPessoasByType(filter);
	}

	@Override
	public List<PessoaJuridica> listarByFilter(BasicFiltroPessoaJuridica filter)
			throws BusinessException {

		return pessoaRepository.listaPessoasByFilter(filter);
	}
	@Override
	public PessoaJuridica getPessoaBySap(BasicFiltroPessoaJuridica filter)
			throws BusinessException {
		
		return pessoaRepository.getPessoaBySap(filter);
	}

	@Override
	public List<PessoaJuridica> listarallFornecedores(String fornecedor)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(readOnly=true)
	public List<PessoaJuridica> listarAllFornecedoresByStatus(Boolean status)
			throws BusinessException {
		// TODO Auto-generated method stub
		return pessoaRepository.listarAllFornecedoresByStatus(status);
	}

	@Override
	public List<PessoaJuridica> listarAllImportadorByStatus(Boolean status)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TipoPessoa cadastrarTypePerson(TipoPessoa type)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TipoPessoa alterarTypePerson(TipoPessoa type)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TipoPessoa getTypePersonById(Long id) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void excluirTypePerson(TipoPessoa type) throws BusinessException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<TipoPessoa> listAllTypePersons() throws BusinessException {

		return typePersonRepository.listar();
	}

	@Override
	public List<Cidade> getCidadesByPais(Pais paisDestino)
			throws BusinessException {
		return cidadeRepository.getCidadesByPais(paisDestino);
	}

	@Override
	public List<PessoaJuridica> listarPessoasByTipo(TipoPessoaEnum tipo)
			throws BusinessException {
		return pessoaRepository.listarPessoasByTipo(tipo);
	}

	@Override
	public List<MateriaPrima> listarMateriaPrima(MateriaPrima materiaPrima)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Projeto> listarProjetosPorInvoices(List<Invoice> listaInvoices)
			throws BusinessException {
		return projetoRepository.listarProjetosPorInvoices(listaInvoices);
	}

	@Override
	public List<Cidade> getCidadesByFollowUp(FollowUpImport followUp)
			throws BusinessException {
		return cidadeRepository.getCidadesByFollowUp( followUp );
	}

	@Override
	public List<ParametroCanal> listarParametroCanais()
			throws BusinessException {
		return canalRepository.listar();
	}

	@Override
	public ParametroCanal getParametroCanais(Canal canal)
			throws BusinessException {

		return canalRepository.getCanal(canal);
	}

	@Override
	public ParametroCanal alterarParametroCanal(ParametroCanal entity)
			throws BusinessException {
		return canalRepository.alterar( entity );	}
	
	@Override
	public ParametroContinente alterarParametroContinente(
			ParametroContinente continente) throws BusinessException {
		
		return continenteRepository.alterar( continente );
		
	}
	
	
	@Override
	public void validarRota(Rota rota) throws BusinessException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Rota> listarRotasTrechos(BasicFiltroRota filtro)
			throws BusinessException {
		
		List<Rota> result = rotaRepository.listarComTrechos(filtro);
		
//		for (Rota rota : result) {
//
//			rota = rotaRepository.getByIdTrechos(rota.getId());
//
//		}

		return result;
	}

	@Override
	public List<ParametroContinente> listarParametroContinente()
			throws BusinessException {
		return continenteRepository.listar();

	}

	@Override
	public int getFeriadosUteis(Date inicial, Date dtFinal, Pais idPais)
			throws BusinessException {
	
		return 0;
	}

	@Override
	public Filial cadastrarFilial(Filial obj) throws BusinessException {
		obj.setAtivo( true );
		return filialRepository.cadastrar(obj);
	}

	@Override
	public Filial alterarFilial(Filial obj) throws BusinessException {
	
		return filialRepository.alterar(obj);
	}

	@Override
	public void excluirFilial(Filial obj) throws BusinessException {
	           filialRepository.excluirById(obj.getId());
		
	}

	@Override
	public Filial getFilialById(Long id) throws BusinessException {
		return filialRepository.getById(id);
	}
	@Override
	public Filial  getFilialByCodigo(String codigo) throws BusinessException {
		return filialRepository.getFilialByCodigo(codigo);
	}

	@Override
	public List<Filial> listarFilialByFilter(BasicFiltroFilial filtro) throws BusinessException {
		return filialRepository.listarFiliais(filtro);
	}

	@Override
	public Nivel cadastrarNivel(Nivel obj) throws BusinessException {
		return nivelRepository.cadastrar(obj);
	}

	@Override
	public Nivel alterarNivel(Nivel obj) throws BusinessException {
		return nivelRepository.alterar(obj);
	}

	@Override
	public void excluirNivel(Nivel obj) throws BusinessException {
		nivelRepository.excluirById(obj.getId());

	}

	@Override
	public Nivel getNivelById(Long id) throws BusinessException {

		return nivelRepository.getById(id);
	}

	@Override
	public List<Nivel> listarNivelByFiltro(BasicFiltroNivel filtro)
			throws BusinessException {
		return nivelRepository.listarNivelByFiltro(filtro);
	}

	@Override
	public UnidadeMedida cadsatrarUnidMedida(UnidadeMedida obj)
			throws BusinessException {
		
		obj.setAtivo( true );
		return unidMedidaRepository.cadastrar(obj);
	}

	@Override
	public UnidadeMedida alterarUnidMedida(UnidadeMedida obj)
			throws BusinessException {
		return unidMedidaRepository.alterar(obj);
	}

	@Override
	public void excluirUnidMedida(UnidadeMedida obj) throws BusinessException {

		unidMedidaRepository.excluirById(obj.getId());
	}

	@Override
	public UnidadeMedida getUnidMedidaById(Long id) throws BusinessException {
		return unidMedidaRepository.getById(id);
	}

	@Override
	public UnidadeMedida getUnidMedidaByCodigo(String codigo) throws BusinessException {
		return unidMedidaRepository.getByCodigo(codigo);
	}

	@Override
	public List<UnidadeMedida> listarUnidMedidaByFiltro(
			BasicFiltroUnidMedida filtro) throws BusinessException {
		return unidMedidaRepository.listarUnidMedidaBysFiltro(filtro);
	}
	
	/**
	 * Forma Pagamento 
	 */

	@Override
	public FormaPagamento getFormaPagamentoBycodeSAP(String codeSAP) throws BusinessException{
		return formaPagamentoRepository.getBycodeSAP(codeSAP);
	}
	@Override
	public List<FormaPagamento> listarFormasPagamentoAtivos() throws BusinessException{
		return formaPagamentoRepository.listarFormasPagamentoAtivos();
	}
	@Override
	public Incoterm getIncotermBySigla(String sigla, boolean cadastro)
			throws BusinessException {
		
		try {
			return incotermRepository.getBySigla(sigla);

		} catch (NoResultException e) {
				
			Incoterm i  = new Incoterm();
			i.setDescricao(sigla);
			i.setSigla(sigla);
			i.setAtivo(true);
			
			i = cadastrarIncoterm(i);
			return i;
		}
		
	}

	@Override
	public UnidadeMedida getUnidMedidaByCodigo(String codigo, boolean cadastro)
			throws BusinessException {
		
		try {
			
			UnidadeMedida ume = unidMedidaRepository.getByCodigo(codigo);
			return ume;
			
		} catch (NoResultException e) {
			if ( cadastro ) {
				UnidadeMedida ume = new UnidadeMedida();
				ume.setCodigo(codigo);
				ume.setAtivo( true );
				ume.setDescricao(codigo);
				
				return unidMedidaRepository.cadastrar( ume );
			}
		}
		
		
		return null;
	}

	@Override
	public Moeda getMoedaPadrao() throws BusinessException {
		return moedaRepository.getMoedaPadrao();
	}

	@Override
	public Cidade getCityByid(Long id) throws BusinessException {
		return cidadeRepository.cityById(id);
	}
	
	
	
	
	//método  novo que retorna pessoa pelo sap vendor

	@Override
	public PessoaJuridica getPessoaJuridicaBySapVendor(String s)
			throws BusinessException {
		return pessoaRepository.getPessoaJuridicaBySapVendor(s);
	}

	@Override
	public TipoPacote getTipoPacoteByCodigo(String string)
			throws BusinessException {
		return tipoPacoteRepository.getByCodigo( string );
	}

	@Override
	public List<TipoPacote> getTipoPacoteByCategoria(String string)
			throws BusinessException {
		return tipoPacoteRepository.getByCategoria( string );
	}

	@Override
	public ParametroImposto getParametroImposto( String descricao ) throws BusinessException {
		return parametroImpostoRepository.getParametro(descricao);
	}

}
