package br.com.ilog.cadastro.presentation.mbean;

import java.util.ArrayList;
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
import br.cits.commons.citsbusiness.hibernate.validator.ValidatorHelper;
import br.cits.commons.citspresentation.converter.EntityConverter;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.commons.citspresentation.util.JSFRequestBean;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.Cidade;
import br.com.ilog.cadastro.business.entity.Criterio;
import br.com.ilog.cadastro.business.entity.Frete;
import br.com.ilog.cadastro.business.entity.Moeda;
import br.com.ilog.cadastro.business.entity.Pais;
import br.com.ilog.cadastro.business.entity.PessoaJuridica;
import br.com.ilog.cadastro.business.entity.Rota;
import br.com.ilog.cadastro.business.entity.TaxaFrete;
import br.com.ilog.cadastro.business.entity.TipoPessoa;
import br.com.ilog.cadastro.business.entity.TipoPessoaEnum;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroCidade;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroPessoaJuridica;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroRota;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.cadastro.presentation.util.ConverterUtil;
import br.com.ilog.cadastro.presentation.util.ExceptionFiltro;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;
import br.com.ilog.geral.presentation.mbean.AbstractManterPaginacao;

@Controller("mBeanManterFrete")
@AccessScoped
public class MBeanManterFrete extends AbstractManterPaginacao {

	/** */
	private static final long serialVersionUID = 988897868241083020L;

	private Frete frete;

	private TaxaFrete taxaFrete;

	private List<SelectItem> comboCriterio;

	private boolean novoFrete;

	private boolean edicao;

	private boolean error;

	private List<Moeda> comboMoedas;
	private ConverterUtil<Moeda> moedaConverter;

	/**
	 * Fachada de cadastro
	 */
	@Resource(name = "controllerCadastro")
	CadastroFacade cadastro;

	private static Logger logger = LoggerFactory
			.getLogger(MBeanManterFrete.class);

	/**
	 * filtro de rotas.
	 */
	private BasicFiltroRota filtroRota;

	private Integer paginaAtualRotas;
	private byte registrosPagina = 5;

	private List<Cidade> comboCidadesOrigem;
	private EntityConverter<Cidade> converterCidadeOrigem;
	private List<Pais> comboPaisesOrigem;
	private EntityConverter<Pais> converterPaisOrigem;
	private List<Pais> comboPaisesDestino;
	private EntityConverter<Pais> converterPaisDestino;
	private List<Cidade> comboCidadesDestino;
	private EntityConverter<Cidade> converterCidadeDestino;

	/**
	 * Resultado da pesquisa de Rotas.
	 */
	private List<Rota> listRotas;

	private Long selectId;

	private Rota rota;

	private List<PessoaJuridica> comboAgentes;
	private EntityConverter<PessoaJuridica> converterAgente;

	/**
	 * string que vai carregar quando rota nao for selecionado
	 */
	private String msgRota;

	private String msgObrigatorioTaxa;

	@PostConstruct
	public void inicializar() {
		inicializarObjetos();
	}

	@Override
	public void inicializarObjetos() {
		frete = new Frete();
		frete.setRota(new Rota());
		frete.setTaxasFrete(new ArrayList<TaxaFrete>());

		taxaFrete = new TaxaFrete();

		filtroRota = new BasicFiltroRota();

		popularMoedas();
		popularComboAgentes();
		popularPaisDestinoOrigem();
		popularCidadeDestino();
		popularCidadeOrigem();
		
		capRota = new Rota();

		msgRota = null;
		msgObrigatorioTaxa = null;
	}

	/**
	 * Popular combo de moedas
	 * 
	 * @author Heber Santiago
	 */
	private void popularMoedas() {
		try {
			comboMoedas = new ArrayList<Moeda>();
			List<Moeda> moedasAux;
			moedasAux = cadastro.listarMoedas();
			if (moedasAux != null)
				comboMoedas = moedasAux;
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	
	public String cancelar() {
		this.refazerPesquisa();
		return "fretes.jsf";
        
		
		
		
	}
	
	
	
	
	private void popularComboAgentes() {
		comboAgentes = new ArrayList<PessoaJuridica>();
		try {
			BasicFiltroPessoaJuridica filtroAgente = new BasicFiltroPessoaJuridica();
			filtroAgente.setAtivo(true);

			TipoPessoa tp = new TipoPessoa();
			tp.setTipo(TipoPessoaEnum.A_CARGA.name());
			filtroAgente.setPessoa(tp);

			List<PessoaJuridica> agenteAux = cadastro
					.listarByFilter(filtroAgente);
			if (agenteAux != null) {
				comboAgentes = agenteAux;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void popularPaisDestinoOrigem() {
		comboPaisesDestino = new ArrayList<Pais>();
		comboPaisesOrigem = new ArrayList<Pais>();
		try {
			List<Pais> paises = cadastro.listarPaises();
			if (paises != null) {
				comboPaisesDestino = paises;
				comboPaisesOrigem = paises;
			}

		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param arg
	 */
	public void popularCidadeOrigem() {
		comboCidadesOrigem = new ArrayList<Cidade>();

		try {
			if (filtroRota != null && filtroRota.getPaisOrigem() != null
					&& filtroRota.getPaisOrigem().getId() != null) {

				List<Cidade> cidades = cadastro
						.listarCidades(new BasicFiltroCidade(filtroRota
								.getPaisOrigem(), null));
				if (cidades != null)
					comboCidadesOrigem = cidades;

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
			if (filtroRota != null && filtroRota.getPaisDestino() != null
					&& filtroRota.getPaisDestino().getId() != null) {
				List<Cidade> cidades = cadastro
						.listarCidades(new BasicFiltroCidade(filtroRota
								.getPaisDestino(), null));
				if (cidades != null)
					comboCidadesDestino = cidades;
			}

		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	public void carregarTrechos() {
		rota = new Rota();
		try {
			rota = cadastro.getRotaById(selectId);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		// rota.getTrechos();
	}

	/**
	 * @return
	 */
	public String novoFrete() {

		novoFrete = true;
		edicao = false;

		inicializarObjetos();

		return "frete.jsf";
	}

	/**
	 * Adiciona taxa ao frete.
	 */
	public void adcionarTaxa(ActionEvent event) {
		error = false;
		FacesContext fc = FacesContext.getCurrentInstance();
		List<String> erros = ValidatorHelper
				.valida(taxaFrete,
						TemplateMessageHelper.getResourceBundle(
								MensagensSistema.SISTEMA, fc.getViewRoot()
										.getLocale()), TemplateMessageHelper
								.getResourceBundle(MensagensSistema.FRETE, fc
										.getViewRoot().getLocale()));

		if (erros.isEmpty()) {
			taxaFrete.setFrete(frete);
			frete.getTaxasFrete().add(taxaFrete);

			/**
			 * UIModalPanel modal = (UIModalPanel)RichFunction.findComponent(
			 * "modalTaxaFrete" ); if ( modal != null ) {
			 * modal.setShowWhenRendered( false );
			 * AjaxContext.getCurrentInstance().addComponentToAjaxRender(modal);
			 * }
			 **/
		} else {
			error = true;
			Messages.adicionaMensagensDeErro(erros);
			return;
		}

		taxaFrete = new TaxaFrete();

	}

	/**
	 * Apos a insercao limapa os valores de Taxa.
	 * 
	 * @param event
	 */
	public void limparTaxa(ActionEvent event) {
		if (!error) {
			taxaFrete = new TaxaFrete();
		}
	}

	
	private Rota capRota;
	
	public void capturarRota( ) {
		try {
			this.frete.setRota(new Rota());
			this.frete.setRota(cadastro.getRotaById(capRota.getId()));

			this.carregarAgenteCarga();
			msgRota = "";

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metodo para inicializar dados
	 * 
	 * @throws BusinessException
	 */
	private void carregarAgenteCarga() throws BusinessException {
		if (frete.getRota().getAgenteCarga() != null) {
			PessoaJuridica agente = cadastro.getPessoaById(frete.getRota()
					.getAgenteCarga().getId());
			frete.getRota().setAgenteCarga(agente);
		}
	}

	public void carregarRotas() {

		filtroRota = new BasicFiltroRota();

		getRotas();
	}

	public void getRotas() {
		try {
			// BasicFiltroRota filtroRota = new BasicFiltroRota();
			FacesContext fc = FacesContext.getCurrentInstance();

			listRotas = new ArrayList<Rota>();

			this.filtroRota.setAtivo(true);
			listRotas = cadastro.listarRotas(filtroRota);

			if (listRotas.isEmpty()) {
				String msg = TemplateMessageHelper.getMessage(
						MensagensSistema.SISTEMA, "MSG008", fc.getViewRoot()
								.getLocale());
				Messages.adicionaMensagemDeInfo(msg);
			} else {
				getScrollerInfoRotas();
			}

			setPaginaAtualRotas(1);

			rota = new Rota();
			filtroRota = new BasicFiltroRota();

		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * retorna o contato do agente de cargas de uma rota.
	 */
	public String getContato() {
		return "";
	}

	public String getFoneContato() {
		return "";
	}

	public String getEnderecoAgente() {
		return "";
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

	public int getTotalRegistrosRotas() {
		if (listRotas != null)
			return listRotas.size();
		return 0;
	}

	/**
	 * Metodo recupera uma taxa para edicao
	 * 
	 * @param index
	 */
	public void editarTaxa(int index) {
		taxaFrete = frete.getTaxasFrete().get(index);
	}

	/**
	 * Exclui taxa selecionada do frete
	 * 
	 * @param index
	 *            indice do registro na lista.
	 */
	public void excluirTaxa(int index) {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();

			frete.getTaxasFrete().remove(index);
			Messages.adicionaMensagemDeInfo(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG_EXCLUIR_SUCESSO", fc
							.getViewRoot().getLocale()));

		} catch (Exception e) {
			FacesContext fc = FacesContext.getCurrentInstance();
			e.printStackTrace();
			Messages.adicionaMensagemDeInfo(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG_GEN_002", fc.getViewRoot()
							.getLocale()));
		}
	}

	/**
	 * Metodo salva registro
	 */
	@Override
	public String salvar() {
		FacesContext fc = FacesContext.getCurrentInstance();
		List<String> erros = ValidatorHelper
				.valida(frete,
						TemplateMessageHelper.getResourceBundle(
								MensagensSistema.SISTEMA, fc.getViewRoot()
										.getLocale()), TemplateMessageHelper
								.getResourceBundle(MensagensSistema.FRETE, fc
										.getViewRoot().getLocale()));

		if (this.frete.getRota() == null
				|| this.frete.getRota().getId() == null) {
			msgRota = TemplateMessageHelper.getMessage(MensagensSistema.CARGA,
					"lblRotaObrigatoria", fc.getViewRoot().getLocale());
			// erros.add( msgRota );
		}

		if (frete.getTaxasFrete() == null || frete.getTaxasFrete().isEmpty()) {
			msgObrigatorioTaxa = TemplateMessageHelper.getMessage(
					MensagensSistema.FRETE, "msgTaxasObrigatorio", fc
							.getViewRoot().getLocale());
			erros.add(msgObrigatorioTaxa);
		}

		if (erros.isEmpty()) {
			try {
				if (edicao) {
					frete = cadastro.alterarFrete(frete);
				} else {
					frete = cadastro.cadastrarFrete(frete);
				}

				String message = TemplateMessageHelper.getMessage(
						MensagensSistema.SISTEMA, "MSG001", fc.getViewRoot()
								.getLocale());
				Messages.adicionaMensagemDeInfo(message);
				refazerPesquisa();

				return "fretes.jsf";

			} catch (BusinessException e) {

				Throwable lastCause = ExceptionFiltro.getLastException(e);
				if (lastCause.getMessage().contains("CHECK constraint")) {
					if (lastCause.getMessage().contains("chk_frete_duplicado")) {
						Messages.adicionaMensagemDeErro(TemplateMessageHelper
								.getMessage(MensagensSistema.FRETE,
										"msgCheckFrete", fc.getViewRoot()
												.getLocale()));
					}
					return null;

				}

				Messages.adicionaMensagemDeErro(TemplateMessageHelper
						.getMessage(MensagensSistema.FRETE,
								ExceptionFiltro.recursiveException(e)));
				return null;
			}

		} else {
			Messages.adicionaMensagensDeErro(erros);
			return null;
		}

	}
	
	private Frete selectFrete;

	@Override
	public String editar() {

		//Long idRegistro = Long.valueOf(JSFRequestBean.getParameter("id"));

		try {
			frete = cadastro.getFreteById(selectFrete.getId());

			frete.setMoeda(cadastro.getMoedaById(frete.getMoeda().getId()));

			taxaFrete = new TaxaFrete();

			error = false;

			this.setPaginaAtual(1);

		} catch (BusinessException e) {
			logger.error("erro: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, e));
			return null;
		}
		edicao = true;
		novoFrete = false;

		return "frete.jsf";
	}

	@Override
	public String excluir() {
		return "fretes.jsf";
	}

	/**
	 * Metodo de salvar registro e adicionar um novo
	 * 
	 * @author Heber Santiago
	 * @since 14/02/2012
	 * 
	 * @return
	 */
	public String salvarNovo() {

		try {
			if (this.salvar() != null) {
				return this.novoFrete();

			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	protected void refazerPesquisa() {
		MBeanPesquisarFrete mBean = (MBeanPesquisarFrete) JSFRequestBean
				.getManagedBean("mBeanPesquisarFrete");
		mBean.refazerPesquisa();
	}

	public Frete getFrete() {
		return frete;
	}

	public void setFrete(Frete frete) {
		this.frete = frete;
	}

	public boolean isNovoFrete() {
		return novoFrete;
	}

	public void setNovoFrete(boolean novoFrete) {
		this.novoFrete = novoFrete;
	}

	public List<Moeda> getComboMoedas() {
		return comboMoedas;
	}

	public void setComboMoedas(List<Moeda> comboMoedas) {
		this.comboMoedas = comboMoedas;
	}

	public ConverterUtil<Moeda> getMoedaConverter() {
		return moedaConverter;
	}

	public void setMoedaConverter(ConverterUtil<Moeda> moedaConverter) {
		this.moedaConverter = moedaConverter;
	}

	public boolean isEdicao() {
		return edicao;
	}

	public void setEdicao(boolean edicao) {
		this.edicao = edicao;
	}

	public BasicFiltroRota getFiltroRota() {
		return filtroRota;
	}

	public void setFiltroRota(BasicFiltroRota filtroRota) {
		this.filtroRota = filtroRota;
	}

	public List<Rota> getListRotas() {
		return listRotas;
	}

	public void setListRotas(List<Rota> listRotas) {
		this.listRotas = listRotas;
	}

	public byte getRegistrosPagina() {
		return registrosPagina;
	}

	public void setRegistrosPagina(byte registrosPagina) {
		this.registrosPagina = registrosPagina;
	}

	public Rota getRotaAux() {
		return rota;
	}

	public void setRotaAux(Rota rotaAux) {
		this.rota = rotaAux;
	}

	public String getMsgRota() {
		return msgRota;
	}

	public void setMsgRota(String msgRota) {
		this.msgRota = msgRota;
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

	public List<Cidade> getComboCidadesOrigem() {
		return comboCidadesOrigem;
	}

	public void setComboCidadesOrigem(List<Cidade> comboCidadesOrigem) {
		this.comboCidadesOrigem = comboCidadesOrigem;
	}

	public EntityConverter<Cidade> getConverterCidadeOrigem() {
		return converterCidadeOrigem;
	}

	public void setConverterCidadeOrigem(
			EntityConverter<Cidade> converterCidadeOrigem) {
		this.converterCidadeOrigem = converterCidadeOrigem;
	}

	public List<Pais> getComboPaisesOrigem() {
		return comboPaisesOrigem;
	}

	public void setComboPaisesOrigem(List<Pais> comboPaisesOrigem) {
		this.comboPaisesOrigem = comboPaisesOrigem;
	}

	public EntityConverter<Pais> getConverterPaisOrigem() {
		return converterPaisOrigem;
	}

	public void setConverterPaisOrigem(EntityConverter<Pais> converterPaisOrigem) {
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

	public Long getSelectId() {
		return selectId;
	}

	public void setSelectId(Long selectId) {
		this.selectId = selectId;
	}

	public TaxaFrete getTaxaFrete() {
		return taxaFrete;
	}

	public void setTaxaFrete(TaxaFrete taxaFrete) {
		this.taxaFrete = taxaFrete;
	}

	public List<SelectItem> getComboCriterio() {
		comboCriterio = new ArrayList<SelectItem>();

		// TODO Add properties
		comboCriterio.add(new SelectItem(Criterio.MEQ, "Ate"));
		comboCriterio.add(new SelectItem(Criterio.MAQ, "Maior que"));

		return comboCriterio;
	}

	public void setComboCriterio(List<SelectItem> comboCriterio) {
		this.comboCriterio = comboCriterio;
	}

	@Override
	public int getTotalRegistros() {
		if (frete.getTaxasFrete() != null) {
			return frete.getTaxasFrete().size();
		}
		return 0;
	}

	public String getMsgObrigatorioTaxa() {
		return msgObrigatorioTaxa;
	}

	public void setMsgObrigatorioTaxa(String msgObrigatorioTaxa) {
		this.msgObrigatorioTaxa = msgObrigatorioTaxa;
	}

	public Frete getSelectFrete() {
		return selectFrete;
	}

	public void setSelectFrete(Frete selectFrete) {
		this.selectFrete = selectFrete;
	}

	public Rota getCapRota() {
		return capRota;
	}

	public void setCapRota(Rota capRota) {
		this.capRota = capRota;
	}

}
