package br.com.ilog.importacao.business.mbean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citspresentation.mbeans.AbstractPaginacao;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.commons.citspresentation.util.JSFRequestBean;
import br.com.ilog.cadastro.business.entity.Modal;
import br.com.ilog.cadastro.business.entity.Ocorrencia;
import br.com.ilog.cadastro.business.entity.Rota;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroOcorrencia;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;
import br.com.ilog.geral.presentation.util.DataUtils;
import br.com.ilog.importacao.business.dto.PainelLogistica;
import br.com.ilog.importacao.business.entity.Carga;
import br.com.ilog.importacao.business.entity.FollowUpImportTrecho;
import br.com.ilog.importacao.business.entity.OcorrenciaImport;
import br.com.ilog.importacao.business.entity.StatusCarga;
import br.com.ilog.importacao.business.facade.ImportacaoFacade;

@Controller("mBeanPainelLogistica")
@Scope("session")
public class MBeanPainelLogistica extends AbstractPaginacao {

	/** */
	private static final long serialVersionUID = 2199562534326294583L;

	@Resource(name = "controllerImportacao")
	ImportacaoFacade facade;

	private static Logger logger = LoggerFactory
			.getLogger(MBeanPainelLogistica.class);

	/**
	 * @coment fachada de cadastros
	 */
	@Resource(name = "controllerCadastro")
	CadastroFacade cadastrotFacade;

	private List<Carga> resultPainel;

	private List<PainelLogistica> result;

	private List<Carga> listAps;

	private int tamanhoBordaIniFim = 5;

	private int tamanhoFundoPadrao = 64;

	private int deslocamentoEsquerda = 0;
	private int deslocamentoMeio = 0;
	private int deslocamentoDireita = 0;
	private int tamanhoBorda = 14;
	private int espacoTrechos = 420;
	int tamanhoCanal = 64;
	private int ESQUERDA = 1;
	private int MEIO = 2;
	private int DIREITA = 3;
	/**
	 * quantidade de linhas do painel de follow up de transporte.
	 */
	private int rows;

	int paginaAtualPainel;

	private List<Ocorrencia> ocorrenciasTrecho;

	private int paginaAtualOcorrencias = 0;

	private String tituloModalOocorrencias;

	private PainelLogistica painelLogistica;

	private List<Ocorrencia> listaOcorrenciasFollow;

	private Integer paginaAtualOcorrenciaFollow;

	@Override
	public void doPesquisar(ActionEvent arg0) {
	}

	/**
	 * Seta o flag de lido para a ocorrencia.
	 */
	public void leOcorrencia() {
		try {

			int index = Integer.valueOf(JSFRequestBean
					.getParameter("ocorrencia"));
			Ocorrencia ocorrencia = listaOcorrenciasFollow.get(index);

			// Alterar a ocorrencia para lido
			ocorrencia.setLido(true);
			facade.alterarOcorrencia(ocorrencia);

		} catch (BusinessException e) {
			logger.error("erro: {}", e);
			e.printStackTrace();
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.OCORRENCIAS, e));
		}
	}

	@PostConstruct
	public void inicializar() {
		painelLogistica = new PainelLogistica();
		painelLogistica.setCarga(new Carga());
	}

	public void atualizarPainel() {

	}

	/**
	 * metodo para carregar o painel.
	 */
	public void carregarPainel() {

		ocorrenciasTrecho = new ArrayList<Ocorrencia>();
		setPaginaAtualOcorrencias(1);
		setPaginaAtualOcorrenciaFollow(1);

		paginaAtualPainel++;

		rows = 2;

		resultPainel = new ArrayList<Carga>();

		try {
			resultPainel = facade.listCargasPainelLogistica(null);

			result = new ArrayList<PainelLogistica>();

			for (Carga carga : resultPainel) {

				PainelLogistica log = new PainelLogistica();
				log.setOcorrencias(montarMapaOcorrencias(carga));
				log.setCarga(carga);
				log.setTrechos(facade.listarFollowUpTrechosByCarga(carga));

				int tamanhoFundo = 0;
				// fazer os calculos para organizar o tamanho dos divs na tela
				if (log.getTrechos() != null && !log.getTrechos().isEmpty()) {

					int size = log.getTrechos().size() - 3;

					if (size > 0) {
						tamanhoFundo = espacoTrechos / size;
						int diff = espacoTrechos - (tamanhoFundo * size);
						tamanhoFundo -= tamanhoBorda;
						tamanhoCanal += diff;

						log.setTamanhoFundo(tamanhoFundo);
						log.setDiffTamanho(diff);
					}

					log.setDtPrevista(log.getTrechos()
							.get(log.getTrechos().size() - 1).getDtPlanejado());
					log.setDtReal(log.getTrechos()
							.get(log.getTrechos().size() - 1).getDtRealizado());
				}

				result.add(log);
			}

		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	public String getCanalAtual(FollowUpImportTrecho item) {
		if (item.isCanal() && item.getParametroCanalAtual() != null) {
			try {
				return facade.getFollowUpImportTrechoById(item.getId())
						.getParametroCanalAtual().getCanal().name();
			} catch (BusinessException e) {
				// e.printStackTrace();
				return null;
			}
		}
		return null;
	}

	/**
	 * @param obj
	 * @return
	 */
	public String getLocationOcorrencia(OcorrenciaImport obj) {
		FacesContext fc = FacesContext.getCurrentInstance();

		if (obj.getCanal() != null) {
			if (obj.getCanal()) {
				return obj.getCidade().getNome()
						+ " / "
						+ TemplateMessageHelper.getMessage(
								MensagensSistema.OCORRENCIAS, "lblCanal", fc
										.getViewRoot().getLocale());
			}
		}
		if (obj.getIsLom() != null) {
			if (obj.getIsLom()) {
				return obj.getCidade().getNome()
						+ " / "
						+ TemplateMessageHelper.getMessage(
								MensagensSistema.OCORRENCIAS, "lblLom", fc
										.getViewRoot().getLocale());
			}
		}
		return obj.getCidade().getNome();
	}

	/**
	 * verificar se precisa carregar o campo de cidade atual da carga.
	 * 
	 * @return
	 */
	public boolean carregarCidades() {
		if (painelLogistica != null && painelLogistica.getCarga() != null)
			if (StatusCarga.ITT.equals(painelLogistica.getCarga().getStatus())
					|| StatusCarga.OHI.equals(painelLogistica.getCarga()
							.getStatus()))
				return true;
		return false;
	}

	public boolean carregarMensagem() {
		if (painelLogistica != null && painelLogistica.getTrechos() != null
				&& !painelLogistica.getTrechos().isEmpty()) {
			return false;
		}
		return true;
	}

	/**
	 * Montar o mapa de ocorrencias para a carga selecionada, conforme as
	 * cidades.
	 * 
	 * @param carga
	 * @return
	 * @throws BusinessException
	 */
	private Map<Long, List<Ocorrencia>> montarMapaOcorrencias(Carga carga)
			throws BusinessException {

		List<Ocorrencia> ocorr = facade
				.listarOcorrencia(new BasicFiltroOcorrencia(carga, true));

		Map<Long, List<Ocorrencia>> ocorrencias = new HashMap<Long, List<Ocorrencia>>();
		for (Ocorrencia o : ocorr) {
			if ((o.getCanal() == null || !o.getCanal())
					&& (o.getCanal() == null || !o.getIsLom())) {

				List<Ocorrencia> ocorrs = new ArrayList<Ocorrencia>();

				if (ocorrencias.containsKey(o.getCidade().getId())) {
					ocorrs = ocorrencias.get(o.getCidade().getId());
				}
				ocorrs.add(o);
				ocorrencias.put(o.getCidade().getId(), ocorrs);
			}
		}

		return ocorrencias;
	}

	/**
	 * Retorna a Cidade onde se encontra a carga, dependendo do Status
	 * 
	 * @param index
	 * @return
	 */
	public String getOrigemPainel(int index) {

		Carga carga = resultPainel.get(index);
		if (carga.getRota() != null && carga.getRota().getPaisOrigem() != null) {
			return carga.getRota().getPaisOrigem().getNome();
		}

		return "";
	}

	/**
	 * Retorna a Cidade onde se encontra a carga, dependendo do Status
	 * 
	 * @param index
	 * @return
	 */
	public String getCidadeAtual(int index) {

		Carga carga = resultPainel.get(index);
		if (carga.getStatus().equals(StatusCarga.OHI)
				|| carga.getStatus().equals(StatusCarga.ITT)) {
			if (carga.getCidadeAtual() != null) {
				return carga.getCidadeAtual().getSigla().toUpperCase();
			}
		}

		return "";
	}

	@Override
	public int getTotalRegistros() {
		return 0;
	}

	@Override
	public void limpar() {
	}

	@Override
	public void refazerPesquisa() {
	}

	public List<Carga> getResultPainel() {
		return resultPainel;
	}

	public void setResultPainel(List<Carga> resultPainel) {
		this.resultPainel = resultPainel;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public List<PainelLogistica> getResult() {
		return result;
	}

	public void setResult(List<PainelLogistica> result) {
		this.result = result;
	}

	/**
	 * @param item
	 * @return
	 */
	public String getTipoModal(PainelLogistica item) {

		try {
			Carga c = facade.getCargaById(item.getCarga().getId());
			Rota r = cadastrotFacade.getRotaById(c.getRota().getId());
			Modal m = new Modal();
			if (r.getTipoTransporte() != null) {
				m = cadastrotFacade.getModalById(r.getTipoTransporte().getId());
				return m.getSrcImg();
			} else
				return "Onde está Jack? Wall-E Morreu!!!";
		} catch (Exception e) {
			// e.printStackTrace();
			return null;
		}

		/**
		 * boolean atrasado = false;
		 * 
		 * for (FollowUpImportTrecho trecho : item.getTrechos()) { if (
		 * trecho.isCanal() ) { // if ( trecho.getParametroCanalAtual() != null
		 * // && trecho.getParametroCanal().getPrazo() <
		 * trecho.getParametroCanalAtual().getPrazo() ) { // atrasado = true; //
		 * } else { // atrasado = false; // } } else { if (
		 * trecho.getDtRealizado() != null &&
		 * DataUtils.compararApenasData(trecho.getDtPlanejado(),
		 * trecho.getDtRealizado() ) < 0 ) { atrasado = true;
		 * 
		 * } else { if ( trecho.getDtRealizado() == null ) { break; } atrasado =
		 * false; }
		 * 
		 * if ( trecho.getTxLocal().equals("LOM") ) { break; } } }
		 * 
		 * if (!atrasado) { if (item.getCarga().getRota().getTipoTransporte() ==
		 * TipoTransporte.AEREO) { return "aereo_green.png"; } else if
		 * (item.getCarga().getRota().getTipoTransporte() ==
		 * TipoTransporte.MAR_AR) { return "aereo_maritimo_green.png"; } else if
		 * (item.getCarga().getRota().getTipoTransporte() ==
		 * TipoTransporte.MARITMO) { return "maritimo_green.png"; } else return
		 * "terrestre_green.png"; } else { if
		 * (item.getCarga().getRota().getTipoTransporte() ==
		 * TipoTransporte.AEREO) { return "aereo_red.png"; } else if
		 * (item.getCarga().getRota().getTipoTransporte() ==
		 * TipoTransporte.MAR_AR) { return "aereo_maritimo_red.png"; } else if
		 * (item.getCarga().getRota().getTipoTransporte() ==
		 * TipoTransporte.MARITMO) { return "maritimo_red.png"; } else return
		 * "terrestre_red.png"; }
		 **/
	}

	/**
	 * @param index
	 * @return
	 */
	public int getTamanhoFundo(int index, int indexTrecho) {

		if (indexTrecho == 0) {
			return tamanhoFundoPadrao;
		}

		if (!result.isEmpty() && result.get(index) != null
				&& result.get(index).getTrechos() != null
				&& !result.get(index).getTrechos().isEmpty()) {

			if (indexTrecho < result.get(index).getTrechos().size() - 2
					&& result.get(index).getTrechos().get(indexTrecho)
							.getDtRealizado() != null) {
				return result.get(index).getTamanhoFundo();
			}
			if (indexTrecho == result.get(index).getTrechos().size() - 2) {
				return tamanhoFundoPadrao + result.get(index).getDiffTamanho();
			} else {
				return tamanhoFundoPadrao;
			}
		}

		return 0;
	}

	/**
	 * @brief Calcular o tamanho da borda
	 * @author Heber Santiago
	 * 
	 * @param indexTrecho
	 * @return
	 */
	public int getSizetBordaEsquerda(int indexTrecho) {
		if (indexTrecho == 0) {
			return tamanhoBordaIniFim;
		}
		return tamanhoBorda;
	}

	/**
	 * @brief Calcular o tamanho da borda
	 * @param index
	 * @param indexTrecho
	 * @return
	 */
	public int getSizeBordaDireita(int index, int indexTrecho) {

		if (result != null && result.get(index) != null
				&& !result.get(index).getTrechos().isEmpty()) {
			if (indexTrecho == result.get(index).getTrechos().size() - 1) {
				return tamanhoBordaIniFim;
			}
		}

		return tamanhoBorda;
	}

	/**
	 * @brief Calcular o deslocamento da celula a esquerda.
	 * @author Heber Santiago
	 * 
	 * @param index
	 * @return
	 */
	public int getDeslocamentoEsquerda(int index) {

		if (index == 0) {
			deslocamentoEsquerda = 0;
		} else {
			deslocamentoEsquerda = getDeslocamentoDireita();
		}

		return deslocamentoEsquerda;
	}

	/**
	 * @brief calcular o deslocamento da celula do meio.
	 * @author Heber Santiago
	 * @param index
	 * @return
	 */
	public int getDeslocamentoMeio(int index) {
		if (index == 0) {
			deslocamentoMeio = tamanhoBordaIniFim;
		} else {
			deslocamentoMeio = deslocamentoEsquerda + tamanhoBorda;
		}
		return deslocamentoMeio;
	}

	/**
	 * @brief calcular o deslocamento da celula na barra de progresso.
	 * @author Heber Santiago
	 * 
	 * @param indexTrecho
	 * @param index
	 * @return
	 */
	public int getDeslocamentoDireita(int indexTrecho, int index) {
		if (indexTrecho == 0) {
			deslocamentoDireita = deslocamentoMeio + tamanhoFundoPadrao;
		} else if (indexTrecho < result.get(index).getTrechos().size() - 2) {
			deslocamentoDireita = result.get(index).getTamanhoFundo()
					+ deslocamentoMeio;
		} else if (indexTrecho == result.get(index).getTrechos().size() - 2) {
			deslocamentoDireita = result.get(index).getDiffTamanho()
					+ tamanhoFundoPadrao + deslocamentoMeio;
		} else {
			deslocamentoDireita = tamanhoFundoPadrao + deslocamentoMeio;
		}

		return deslocamentoDireita;
	}

	/**
	 * @brief Recuperar o nome/SIGLA da localidade do trecho
	 * @author Heber Santiago
	 * 
	 * @param index
	 * @param indexTrecho
	 * @return
	 */
	public String getLocal(int index, int indexTrecho) {

		if (result != null) {
			if (result.get(index).getTrechos() != null
					&& !result.get(index).getTrechos().isEmpty()) {
				if (indexTrecho == 0) {
					return "Pick Up";
				}
				if (indexTrecho < result.get(index).getTrechos().size() - 2) {
					return result.get(index).getTrechos().get(indexTrecho)
							.getCidade().getSigla().toUpperCase();
				}
				if (indexTrecho >= result.get(index).getTrechos().size() - 2) {
					return result.get(index).getTrechos().get(indexTrecho)
							.getTxLocal();
				}
			}
		}

		return "";
	}

	public Date getDtPrevista() {

		return null;

	}

	/**
	 * @brief Metodo para recuperar a imagem que deve colocar na tela.
	 * @author Heber Santiago
	 * 
	 * @param index
	 *            indice de resultado
	 * @param indexTrecho
	 *            indice na lista de trecho
	 * @param posicao
	 *            na barra de progresso
	 * @return
	 */
	public String getImagemFundo(int indTrecho, int index,
			FollowUpImportTrecho trecho, int posicao) {

		if (carregarFundo(trecho)) {

			boolean atrasado = false;

			if (trecho.isCanal()) {
				// verificar se esta atrasado o canal
				Date d = DataUtils.somarDias(trecho.getDtPlanejado(),
						trecho.getDiasPrevistos());
				if (DataUtils.compararApenasData(d, new Date()) < 0
						&& trecho.getParametroCanalAtual() == null) {
					// ATRASADO
					atrasado = true;
				} else {
					atrasado = false;
				}
			} else {
				// Verificar se o trecho esta atrasado ou nao.
				if ((trecho.getDtRealizado() == null && DataUtils
						.compararApenasData(trecho.getDtPlanejado(), new Date()) < 0)
						|| (trecho.getDtRealizado() != null && DataUtils
								.compararApenasData(trecho.getDtRealizado(),
										trecho.getDtPlanejado()) > 0)) {
					atrasado = true;

				} else {
					atrasado = false;
				}

			}

			if (atrasado) {
				if (posicao == ESQUERDA) {
					if (indTrecho == 0) {
						return "barra_preenchimento_borda_dir";
					}
					return "barra_preenchimento_final";
				}
				if (posicao == MEIO) {
					return "barra_preenchimento";
				}
				if (posicao == DIREITA) {
					if (result.get(index).getTrechos() != null
							&& result.get(index).getTrechos().size() - 1 == indTrecho) {
						return "barra_preenchimento_borda_esq";
					}
					return "barra_preenchimento_inicio";
				}
			} else {
				if (posicao == ESQUERDA) {
					if (indTrecho == 0) {
						return "green_barra_preenchimento_borda_dir";
					}
					return "green_barra_preenchimento_final";
				}
				if (posicao == MEIO) {
					return "green_barra_preenchimento";
				}
				if (posicao == DIREITA) {
					if (result.get(index).getTrechos() != null
							&& result.get(index).getTrechos().size() - 1 == indTrecho) {
						return "green_barra_preenchimento_borda_esq";
					}
					return "green_barra_preenchimento_inicio";
				}
			}

		}
		return "";
	}

	/**
	 * Carrega ou nao o fundo da barra de progresso da situacao da carga
	 * 
	 * @param trecho
	 * @return
	 */
	public boolean carregarFundo(FollowUpImportTrecho trecho) {

		if (trecho != null) {
			if (trecho.isCanal()) {
				if (trecho.getParametroCanalAtual() != null) {
					return true;
				} else {
					return false;
				}
				/*
				 * verificacao de atraso, independente de ter sido realizado ou
				 * nao Date d = DataUtils.somarDias(trecho.getDtPlanejado(),
				 * trecho.getDiasPrevistos() ); if (
				 * DataUtils.compararApenasData(d, new Date() ) < 0 ) { return
				 * true; } if ( trecho.getParametroCanalAtual() != null ) {
				 * return true; }
				 */
			} else {
				if (trecho.getDtRealizado() != null) {
					return true;
				} else {
					return false;
				}
				/*
				 * verificacao de atraso, independente de ter sido realizado ou
				 * nao if ( trecho.getDtRealizado() == null ) { if (
				 * DataUtils.compararApenasData(trecho.getDtPlanejado(), new
				 * Date() ) < 0 ){ return true; } } else { return true; }
				 */
			}
		}

		return false;
	}

	public int getDeslocamentoEsquerda() {
		return deslocamentoEsquerda;
	}

	public void setDeslocamentoEsquerda(int deslocamentoEsquerda) {
		this.deslocamentoEsquerda = deslocamentoEsquerda;
	}

	public int getDeslocamentoMeio() {
		return deslocamentoMeio;
	}

	public void setDeslocamentoMeio(int deslocamentoMeio) {
		this.deslocamentoMeio = deslocamentoMeio;
	}

	public int getDeslocamentoDireita() {
		return deslocamentoDireita;
	}

	public void setDeslocamentoDireita(int deslocamentoDireita) {
		this.deslocamentoDireita = deslocamentoDireita;
	}

	private Boolean exibir = false;

	/**
	 * @param index
	 * @param indTrecho
	 */
	public void getOcorrenciasTrecho(int index, int indTrecho) {
		ocorrenciasTrecho = new ArrayList<Ocorrencia>();
		if (result != null) {
			if (result.get(index).getTrechos() != null
					&& !result.get(index).getTrechos().isEmpty()) {

				FollowUpImportTrecho trecho = result.get(index).getTrechos()
						.get(indTrecho);
				if (!trecho.isCanal()
						&& !trecho.getTxLocal().equals("LOM")
						&& trecho.getCidade() != null
						&& trecho.getCidade().getId() != null
						&& result.get(index).getOcorrencias()
								.containsKey(trecho.getCidade().getId())) {
					ocorrenciasTrecho = result.get(index).getOcorrencias()
							.get(trecho.getCidade().getId());
				}
			}
		}
	}

	public boolean getOcorrenciasLidas(List<OcorrenciaImport> list) {

		if (!list.isEmpty()) {
			for (OcorrenciaImport oi : list) {
				if (!oi.isLido()) {
					return true;
				}
			}
		}

		return false;
	}

	public List<Ocorrencia> getOcorrenciasTrecho() {
		return ocorrenciasTrecho;
	}

	/**
	 * motar o scroll infor para a lista de ocorrencias.
	 * 
	 * @return
	 */
	public String getScrollerInfoOcorrencias() {
		FacesContext fc = FacesContext.getCurrentInstance();

		int regInicial = (this.getPaginaAtualOcorrencias() - 1) * 5 + 1;
		int regFinal = Math.min(regInicial + 5 - 1,
				this.getTotalRegistrosOcorrencia());

		String retorno = "";
		if (regFinal != 0)
			retorno = regInicial
					+ " - "
					+ regFinal
					+ " "
					+ TemplateMessageHelper.getMessage("lblDe", fc
							.getViewRoot().getLocale()) + " "
					+ this.getTotalRegistrosOcorrencia();

		return retorno;
	}

	/**
	 * Total de registros de Ocorrencias.
	 * 
	 * @return
	 */
	private int getTotalRegistrosOcorrencia() {
		if (getOcorrenciasTrecho() != null) {
			return getOcorrenciasTrecho().size();
		}
		return 0;
	}

	public void setOcorrenciasTrecho(String aps, String local,
			List<Ocorrencia> ocorrenciasTrecho) {
		tituloModalOocorrencias = "";

		setOcorrenciasTrecho(new ArrayList<Ocorrencia>());
		if (ocorrenciasTrecho != null) {
			tituloModalOocorrencias = TemplateMessageHelper.getMessage(
					MensagensSistema.CARGA, "lblOcorrencias", FacesContext
							.getCurrentInstance().getViewRoot().getLocale())
					+ ": " + aps.toUpperCase() + " - " + local.toUpperCase();

			setOcorrenciasTrecho(ocorrenciasTrecho);
		}
	}

	/**
	 * @param item
	 */
	public void getFollowUp(PainelLogistica item) {
		painelLogistica = new PainelLogistica();
		painelLogistica = item;

		listaOcorrenciasFollow = new ArrayList<Ocorrencia>();

		if (painelLogistica.getTrechos() != null
				&& !painelLogistica.getTrechos().isEmpty()) {

			for (FollowUpImportTrecho trecho : painelLogistica.getTrechos()) {
				try {
					listaOcorrenciasFollow.addAll(facade
							.listarOcorrencia(new BasicFiltroOcorrencia(item
									.getCarga(), trecho.getCidade(), trecho
									.getTxLocal().equals("LOM"), trecho
									.isCanal(), true)));

				} catch (BusinessException e) {
					// e.printStackTrace();
				}
			}
		}

	}

	/**
	 * montar o scroller de trechos.
	 * 
	 * @return
	 */
	public String getScrollerInfoOcorrenciasFollow() {

		int regInicial = (this.getPaginaAtualOcorrenciaFollow() - 1) * 4 + 1;
		int regFinal = Math.min(regInicial + 4 - 1,
				this.getTotalRegistrosOcorrenciaFollow());

		FacesContext fc = FacesContext.getCurrentInstance();
		String retorno = "";
		if (regFinal != 0)
			retorno = regInicial
					+ " - "
					+ regFinal
					+ " "
					+ TemplateMessageHelper.getMessage("lblDe", fc
							.getViewRoot().getLocale()) + " "
					+ this.getTotalRegistrosOcorrenciaFollow();

		return retorno;
	}

	/**
	 * @return
	 */
	private int getTotalRegistrosOcorrenciaFollow() {
		if (listaOcorrenciasFollow != null) {
			return listaOcorrenciasFollow.size();
		}
		return 0;
	}

	/**
	 * calcula a altura para o modal de ocorrencias
	 * 
	 * @return
	 */
	public String getAlturaModalOcorrencias() {

		if (ocorrenciasTrecho == null) {
			return "200";
		}
		// tamanho inicial para o rodape e cabecalho
		int inicial = 150;
		int fim = 0;
		// base para ser multiplicado conforme a quantidade de registos
		int base = 42;
		if (ocorrenciasTrecho.size() < 5) {

			fim = base * ocorrenciasTrecho.size();
			fim = fim + inicial;

		} else {
			fim = 400;
		}

		return fim + "";
	}

	public void setOcorrenciasTrecho(List<Ocorrencia> ocorrenciasTrecho) {
		this.setPaginaAtualOcorrencias(1);
		this.ocorrenciasTrecho = ocorrenciasTrecho;
	}

	public Integer getPaginaAtualOcorrencias() {
		return paginaAtualOcorrencias;
	}

	public void setPaginaAtualOcorrencias(Integer paginaAtualOcorrencias) {
		this.paginaAtualOcorrencias = paginaAtualOcorrencias;
	}

	public String getTituloModalOocorrencias() {
		return tituloModalOocorrencias;
	}

	public void setTituloModalOocorrencias(String tituloModalOocorrencias) {
		this.tituloModalOocorrencias = tituloModalOocorrencias;
	}

	public Boolean getExibir() {
		try {
			exibir = false;
			listAps = facade.possuiOcorrenciaNaoLida();
			if (listAps != null) {
				if (!listAps.isEmpty() || listAps.size() != 0) {
					exibir = true;
				}
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return exibir;
	}

	public void setExibir(Boolean exibir) {
		this.exibir = exibir;
	}

	public List<Carga> getListAps() {
		return listAps;
	}

	public void setListAps(List<Carga> listAps) {
		this.listAps = listAps;
	}

	public PainelLogistica getPainelLogistica() {
		return painelLogistica;
	}

	public void setPainelLogistica(PainelLogistica painelLogistica) {
		this.painelLogistica = painelLogistica;
	}

	public List<Ocorrencia> getListaOcorrenciasFollow() {
		return listaOcorrenciasFollow;
	}

	public void setListaOcorrenciasFollow(
			List<Ocorrencia> listaOcorrenciasFollow) {
		this.listaOcorrenciasFollow = listaOcorrenciasFollow;
	}

	public Integer getPaginaAtualOcorrenciaFollow() {
		return paginaAtualOcorrenciaFollow;
	}

	public void setPaginaAtualOcorrenciaFollow(
			Integer paginaAtualOcorrenciaFollow) {
		this.paginaAtualOcorrenciaFollow = paginaAtualOcorrenciaFollow;
	}

}
