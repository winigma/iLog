package br.com.ilog.importacao.business.mbean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.springframework.stereotype.Controller;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citspresentation.mbeans.AbstractPaginacao;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.Canal;
import br.com.ilog.cadastro.business.entity.Cidade;
import br.com.ilog.cadastro.business.entity.Modal;
import br.com.ilog.cadastro.business.entity.Pais;
import br.com.ilog.cadastro.business.entity.ParametroCanal;
import br.com.ilog.cadastro.business.entity.PessoaJuridica;
import br.com.ilog.cadastro.business.entity.Rota;
import br.com.ilog.cadastro.business.entity.TipoPessoaEnum;
import br.com.ilog.cadastro.business.entity.Trecho;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroFeriado;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroModal;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroRota;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.geral.presentation.CommonsList;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;
import br.com.ilog.geral.presentation.util.DataUtils;
import br.com.ilog.importacao.business.dto.SimulacaoTransporte;

@AccessScoped
@Controller("mBeanSimularTransporte")
public class MBeanSimularTransporte extends AbstractPaginacao {

	/** */
	private static final long serialVersionUID = -7676512336422816155L;

	/**
	 * fachada de cadastro, para as consultas.
	 */
	@Resource( name = "controllerCadastro" )
	CadastroFacade facade;
	
	@Resource( name = "commonsList" )
	CommonsList commonsList;
	
	/**
	 * data base da simulacao.
	 */
	private Date dtBaseSimulacao;
	
	/**
	 * filtro de pesquisa.
	 */
	private BasicFiltroRota filtro;
	
	/**
	 * resultado da pesquisa.
	 */
	private List<Rota> result;
	
	private List<Pais> comboPaises;
	
	private List<Cidade> comboCidadesOrigem;
	
	private List<Cidade> comboCidadesDestino;
	
	private List<PessoaJuridica> comboAgentes;
	
	private List<SelectItem> comboCritico;
	
	private List<Modal> comboModal;
	
	/**
	 * mapa para armazenar os dados de simulações e mostrar no sistema.
	 */
	private Map<Long, SimulacaoTransporte> simulacoesMap;
	
	private SimulacaoTransporte detalhe;
	
	/**
	 * Metodo incializador do MBean.
	 */
	@PostConstruct
	public void inicializar() {
		
		filtro = new BasicFiltroRota();
		//listar apenas as rotas ativas
		filtro.setAtivo( true );
		
		result = new ArrayList<Rota>();
		
		comboCritico = commonsList.listaSimNao();
		
		//Inicializar a data com a data atual.
		dtBaseSimulacao = Calendar.getInstance().getTime();
		
		this.popularAgentes();
		this.popularModal();
		this.popularPais();
		this.popularCidadeDestino();
		this.popularCidadeOrigem();
	}
	
	/**
	 * 
	 */
	private void popularModal() {
		comboModal = new ArrayList<Modal>();
		
		try {
			comboModal = facade.listarModals( new BasicFiltroModal( true ) );
			
		} catch (BusinessException e) {
		}
		
	}

	/**
	 * @param arg
	 */
	public void popularCidadeOrigem() {
		comboCidadesOrigem = new ArrayList<Cidade>();
		
		try {
			comboCidadesOrigem = facade.getCidadesByPais( filtro.getPaisOrigem() );
			
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * metodo para popular combo de cidades.
	 * @param arg
	 */
	public void popularCidadeDestino() {
		comboCidadesDestino = new ArrayList<Cidade>();
		
		try {
			comboCidadesDestino = facade.getCidadesByPais( filtro.getPaisDestino() );
			
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}
	
	private void popularPais() {
		comboPaises = new ArrayList<Pais>();
		
		try {
			comboPaises = facade.listarPaises();
			
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metodo para popular a combo de Agente de Cargas.
	 */
	private void popularAgentes() {
		comboAgentes = new ArrayList<PessoaJuridica>();
		
		try {
			comboAgentes = facade.listarPessoasByTipo( TipoPessoaEnum.A_CARGA );
			
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void doPesquisar(ActionEvent arg0) {
		FacesContext fc = FacesContext.getCurrentInstance();
		result = new ArrayList<Rota>();
		
		if ( dtBaseSimulacao == null ) {
			//FacesContext fc = FacesContext.getCurrentInstance();
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
							MensagensSistema.SIMULAR, "msgDtColetaObrigatorio", fc.getViewRoot()
									.getLocale()));
		} else {
			try {
				result = facade.listarRotasTrechos( filtro );
				
				simularTransporte();
				if (result.isEmpty()) {
					String msg = TemplateMessageHelper.getMessage(MensagensSistema.SISTEMA,"MSG008",fc.getViewRoot().getLocale());
					Messages.adicionaMensagemDeInfo(msg);
					
				}
				
			} catch (BusinessException e) {
				
				e.printStackTrace();
			}
		}
	}

	/**
	 * Calculo da simulacao de transporte conforme os parametros informados.
	 */
	private void simularTransporte() {

		simulacoesMap = new HashMap<Long, SimulacaoTransporte>( 0 );
		
		Set<Pais> paisesFeriados = new HashSet<Pais>( 0 );
		
		for ( Rota rota : getResult() ) {
			Date dataAtual = dtBaseSimulacao;
			
			SimulacaoTransporte simulacao = new SimulacaoTransporte();
			simulacao.setTrechos( rota.getTrechos() );
			
			//recupera para cada trecho, a quantidade de dias.
			for ( Trecho t : rota.getTrechos() ) {
				dataAtual = DataUtils.somarDias( dataAtual, t.getQuantidadeDias() );
				paisesFeriados.add( t.getPaisOrigem() );
			}
			paisesFeriados.add( rota.getPaisDestino() );
			
			try {
				//Calculo dos dias uteis em canal
				ParametroCanal canal = facade.getParametroCanais( Canal.VM );
				//Cidade cidade = facade.getCidadeById( rota.getCidadeDestino().getId() );
				Date dtFinal = DataUtils.somarDiasUteis(dataAtual, canal.getPrazo());
				
				int qtdFeriados = facade.getFeriadosUteis( dataAtual, dtFinal, rota.getPaisDestino().getId() );
				
				simulacao.setDataPrevista( DataUtils.somarDiasUteis(dtFinal, qtdFeriados) );
				
				simulacao.setFeridados( facade.listarFeriado(
						new BasicFiltroFeriado( dataAtual, dtFinal, rota.getPaisDestino() ) ) );
				
			} catch (BusinessException e) {
				e.printStackTrace();
			}
			
			//seta a quantidade de dias e a data da chegada simulada.
			simulacao.setQtdTotalDias(DataUtils.diferencaEmDias( dtBaseSimulacao, dataAtual ) );
			simulacao.setDataChegada(dataAtual);
			
			simulacoesMap.put(rota.getId(), simulacao);
			
		}
	}

	/**
	 * metodo para recuperar os detalhes para o modal.
	 * @param id
	 */
	public void getDadosDetalhados( Rota rota ) {
		
		detalhe = simulacoesMap.get( rota.getId() );
		
	}
	
	/**
	 * Retorna a quantidade de dias que a rota leva para fazer.
	 * @param id
	 * @return
	 */
	public String getQtdeTotalDias( Long id ) {
		
		String str = "";
		
		if ( simulacoesMap.get( id ) != null ) {
			FacesContext fc = FacesContext.getCurrentInstance();
			
			str = simulacoesMap.get(id).getQtdTotalDias()
					+ " "
					+ TemplateMessageHelper.getMessage("lblDia_s", fc
							.getViewRoot().getLocale());
		}
		
		return str;
	}
	
	/**
	 * Retorna a quantidade de dias que a rota leva para fazer.
	 * @param id
	 * @return
	 */
	public String getQtdeDiasCanal( Long id ) {
		
		String str = "";
		
		if ( simulacoesMap.get( id ) != null ) {
			FacesContext fc = FacesContext.getCurrentInstance();
			
			str = DataUtils.diferencaEmDias(simulacoesMap.get(id)
					.getDataChegada(), simulacoesMap.get(id)
					.getDataPrevista())
			+ " "
			+ TemplateMessageHelper.getMessage("lblDia_s", fc
					.getViewRoot().getLocale());
		}
		
		return str;
	}
	
	@Override
	public int getTotalRegistros() {
		return 0;
	}

	@Override
	public void limpar() {
		filtro = new BasicFiltroRota();
		result = new ArrayList<Rota>();
		dtBaseSimulacao =  null ;

	}

	@Override
	public void refazerPesquisa() {
	}

	/**
	 * @return the dtBaseSimulacao
	 */
	public Date getDtBaseSimulacao() {
		return dtBaseSimulacao;
	}

	/**
	 * @param dtBaseSimulacao the dtBaseSimulacao to set
	 */
	public void setDtBaseSimulacao(Date dtBaseSimulacao) {
		this.dtBaseSimulacao = dtBaseSimulacao;
	}

	/**
	 * @return the filtro
	 */
	public BasicFiltroRota getFiltro() {
		return filtro;
	}

	/**
	 * @param filtro the filtro to set
	 */
	public void setFiltro(BasicFiltroRota filtro) {
		this.filtro = filtro;
	}

	/**
	 * @return the comboPaises
	 */
	public List<Pais> getComboPaises() {
		return comboPaises;
	}

	/**
	 * @param comboPaises the comboPaises to set
	 */
	public void setComboPaises(List<Pais> comboPaises) {
		this.comboPaises = comboPaises;
	}

	/**
	 * @return the comboCidadesOrigem
	 */
	public List<Cidade> getComboCidadesOrigem() {
		return comboCidadesOrigem;
	}

	/**
	 * @param comboCidadesOrigem the comboCidadesOrigem to set
	 */
	public void setComboCidadesOrigem(List<Cidade> comboCidadesOrigem) {
		this.comboCidadesOrigem = comboCidadesOrigem;
	}

	/**
	 * @return the comboCidadesDestino
	 */
	public List<Cidade> getComboCidadesDestino() {
		return comboCidadesDestino;
	}

	/**
	 * @param comboCidadesDestino the comboCidadesDestino to set
	 */
	public void setComboCidadesDestino(List<Cidade> comboCidadesDestino) {
		this.comboCidadesDestino = comboCidadesDestino;
	}

	/**
	 * @return the comboAgentes
	 */
	public List<PessoaJuridica> getComboAgentes() {
		return comboAgentes;
	}

	/**
	 * @param comboAgentes the comboAgentes to set
	 */
	public void setComboAgentes(List<PessoaJuridica> comboAgentes) {
		this.comboAgentes = comboAgentes;
	}

	/**
	 * @return the comboCritico
	 */
	public List<SelectItem> getComboCritico() {
		return comboCritico;
	}

	/**
	 * @param comboCritico the comboCritico to set
	 */
	public void setComboCritico(List<SelectItem> comboCritico) {
		this.comboCritico = comboCritico;
	}

	/**
	 * @return the detalhe
	 */
	public SimulacaoTransporte getDetalhe() {
		return detalhe;
	}

	/**
	 * @param detalhe the detalhe to set
	 */
	public void setDetalhe(SimulacaoTransporte detalhe) {
		this.detalhe = detalhe;
	}

	/**
	 * @return the result
	 */
	public List<Rota> getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(List<Rota> result) {
		this.result = result;
	}

	/**
	 * @return the simulacoesMap
	 */
	public Map<Long, SimulacaoTransporte> getSimulacoesMap() {
		return simulacoesMap;
	}

	/**
	 * @param simulacoesMap the simulacoesMap to set
	 */
	public void setSimulacoesMap(Map<Long, SimulacaoTransporte> simulacoesMap) {
		this.simulacoesMap = simulacoesMap;
	}

	/**
	 * @return the comboModal
	 */
	public List<Modal> getComboModal() {
		return comboModal;
	}

	/**
	 * @param comboModal the comboModal to set
	 */
	public void setComboModal(List<Modal> comboModal) {
		this.comboModal = comboModal;
	}

}
