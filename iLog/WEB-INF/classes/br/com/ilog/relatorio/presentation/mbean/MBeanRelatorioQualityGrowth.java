package br.com.ilog.relatorio.presentation.mbean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citspresentation.mbeans.AbstractPaginacao;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.Pais;
import br.com.ilog.cadastro.business.entity.PessoaJuridica;
import br.com.ilog.cadastro.business.entity.TipoPessoa;
import br.com.ilog.cadastro.business.entity.TipoPessoaEnum;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroPessoaJuridica;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;
import br.com.ilog.importacao.business.entity.Carga;
import br.com.ilog.importacao.business.entity.FollowUpImport;
import br.com.ilog.importacao.business.entity.StatusCarga;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroFollowUp;
import br.com.ilog.relatorio.business.facade.RelatorioFacade;
import br.com.ilog.relatorio.util.ExportarRelatorio;
import br.com.ilog.relatorio.util.FiltroVisualizacaoCampos;
import br.com.ilog.seguranca.business.facade.SegurancaFacade;

@AccessScoped
@Controller( "mBeanRelatorioQualityGrowth" )
public class MBeanRelatorioQualityGrowth extends AbstractPaginacao {

	/** */
	private static final long serialVersionUID = -8408038179153318644L;

	static Logger logger = LoggerFactory.getLogger(MBeanRelatorioQualityGrowth.class);
	
	
	/**
	 * @coment fachada de relatorio
	 */
	@Resource(name = "controllerRelatorio")
	RelatorioFacade relatorioFacade;
	
	/**
	 * @coment Fachada de segurança
	 */
	@Resource(name = "controleUsuario")
	SegurancaFacade seguranca;

	/**
	 * @coment fachada de cadastros
	 */
	@Resource(name = "controllerCadastro")
	CadastroFacade cadastroFacade;
	
	/**
	 * combo de fornecedores.
	 */
	private List<PessoaJuridica> fornecedores;
	
	/**
	 * @coment Parametros da tela para o relatorio
	 * 
	 */
	private Map<String, Object> parametros;
	private List<FollowUpImport> listaRelatorio;
	
	/**
	 * @coment Filtro 
	 * 
	 */
	private BasicFiltroFollowUp filtro;
	private FiltroVisualizacaoCampos camposExibicao;

	/**
	 * Combo de paises.
	 */
	private List<Pais> comboPais;
	
	/**
	 * combo de agente de cargas.
	 */
	private List<PessoaJuridica> comboAgenteCargas;
	
	@PostConstruct
	void inicializar() {
		listaRelatorio = new ArrayList<FollowUpImport>();
		comboPais = new ArrayList<Pais>();
		filtro = new BasicFiltroFollowUp();
		camposExibicao = new FiltroVisualizacaoCampos();

		doPesquisar( null );
		popularCombos();
	}
	
	private void popularCombos() {
		comboPais = new ArrayList<Pais>();
		comboAgenteCargas = new ArrayList<PessoaJuridica>();
		fornecedores = new ArrayList<PessoaJuridica>();
		
		try {
			comboPais = cadastroFacade.listarPaises();
			comboAgenteCargas = cadastroFacade.listarAllPersons(
					new BasicFiltroPessoaJuridica( new TipoPessoa(TipoPessoaEnum.A_CARGA.toString()), true));
			fornecedores = cadastroFacade.listarAllPersons( 
					new BasicFiltroPessoaJuridica( new TipoPessoa( TipoPessoaEnum.FORNEC.toString() ), true ) );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void doPesquisar(ActionEvent arg0) {
		
		FacesContext fc = FacesContext.getCurrentInstance();
		listaRelatorio = relatorioFacade.qualityGrowth(filtro);
		if (listaRelatorio.isEmpty()) {
			String message = TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG008", fc.getViewRoot()
					.getLocale());
			Messages.adicionaMensagemDeInfo(message);
		}

	}
	
	/**
	 * preparar parametros do relatorios.
	 */
	private void prepararParametros() {
		parametros = new HashMap<String, Object>();
		FacesContext fc = FacesContext.getCurrentInstance();
		// Configurando os parametros de titulos
		parametros.put("titulo", TemplateMessageHelper.getMessage(
				MensagensSistema.MENU, "lblRelatorioQualityGrowth", fc
						.getViewRoot().getLocale()));
		parametros.put("de", TemplateMessageHelper.getMessage(
				MensagensSistema.RELATORIO, "lblPeriodoDe", fc.getViewRoot()
						.getLocale()));
		parametros.put("ate", TemplateMessageHelper.getMessage(
				MensagensSistema.RELATORIO, "lblAte", fc.getViewRoot()
						.getLocale()));
		parametros.put("Emissao", TemplateMessageHelper.getMessage(
				MensagensSistema.RELATORIO, "lblEmissao", fc.getViewRoot()
						.getLocale()));

		parametros.put("REPORT_RESOURCE_BUNDLE", java.util.ResourceBundle
				.getBundle("br.com.ilog.geral.presentation.relatorio", fc
						.getViewRoot().getLocale()));

		parametros.put("SUBREPORT_DIR_FORNECEDOR", (( ServletContext ) fc.getExternalContext().getContext() ).getRealPath("/relatorios/relatorioQualityGrowth_fornecedor.jasper") );
		parametros.put("SUBREPORT_DIR_INCOTERM", (( ServletContext ) fc.getExternalContext().getContext() ).getRealPath("/relatorios/relatorioQualityGrowth_incoterms.jasper") );
		parametros.put("SUBREPORT_DIR_NUMINVOICE", (( ServletContext ) fc.getExternalContext().getContext() ).getRealPath("/relatorios/relatorioQualityGrowth_numInvoices.jasper") );
		parametros.put("SUBREPORT_DIR_PESO", (( ServletContext ) fc.getExternalContext().getContext() ).getRealPath("/relatorios/relatorioQualityGrowth_peso.jasper") );
		parametros.put("SUBREPORT_DIR_OCORRENCIA", (( ServletContext ) fc.getExternalContext().getContext() ).getRealPath("/relatorios/relatorioQualityGrowth_ocorrencias.jasper") );
		// informando os parametros do filtro

		/**
		 * coleta dtInicio
		 */
		if (filtro.getDtInicioColeta() != null) {
			parametros.put("FiltroDtInicio_coleta", filtro.getDtInicioColeta());
		}
		/**
		 * coleta dtFim
		 */
		if (filtro.getDtFimColeta() != null) {
			parametros.put("FiltroDtFim_coleta", filtro.getDtFimColeta());
		}
		/**
		 *chegada
		 */
		if (filtro.getDtInicio() != null) {
			parametros.put("FiltroDtInicio", filtro.getDtFim());
		}
		if (filtro.getDtFim() != null) {
			parametros.put("FiltroDtFim", filtro.getDtFim());
		}
		/**
		 *aps
		 */
		if(filtro.getAps() != null && !filtro.getAps().isEmpty()){
			parametros.put("aps", filtro.getAps());
		}
		/**
		 *fornecedor
		 */
		if(filtro.getSupplier()!=null){
			parametros.put("exportador",  filtro.getSupplier().getNomeFantasia());
		}
		/**
		 *agente de carga 
		 */
		if(filtro.getAgCarga()!=null ){
			parametros.put("agCarga", filtro.getAgCarga().getNomeFantasia());
		}
		/**
		 *continente
		 */
		if(filtro.getContinente()!=null){
			
			parametros.put("continente", TemplateMessageHelper.getMessage(
					MensagensSistema.CONTINENTE, filtro.getContinente().name(), fc.getViewRoot()
					.getLocale()));
			
		}
		/**
		 *origem
		 */
		if(filtro.getPaisOrigem()!=null){
			parametros.put("FiltroOrigem", filtro.getPaisOrigem().getNome());
		}
		/**
		 *Destino
		 */
		if(filtro.getPaisDestino()!=null){
			parametros.put("FiltroDestino", filtro.getPaisDestino().getNome());
		}
		/**
		 *Responsavel
		 */
		if(filtro.getResponsavel()!=null ){
			parametros.put("responsavel", filtro.getResponsavel().getNome());
		}
		
		
	}
	
	/**
	 * Exportar PDF
	 * @param arg0
	 */
	public void exportarPdf() {

		try {
			prepararParametros();
			parametros.put("tipo", "pdf");
			new ExportarRelatorio<FollowUpImport>().gerarPDF(listaRelatorio, parametros,
					"/relatorios/relatorioQualityGrowth.jasper", "Quality_Growth.pdf");
		} catch (Exception e) {
			logger.error("error: {} " + e);
			e.printStackTrace();
		}

	}

	/**
	 * Exportar XLS
	 * @param arg0
	 */
	public void exportarXls() {
		try {
			prepararParametros();
			parametros.put("tipo", "xls");
			carregarListas();
			new ExportarRelatorio<FollowUpImport>().gerarXLS(listaRelatorio, parametros,
					"/relatorios/relatorioQualityGrowth.jasper", "Quality_Growth.xls");
		} catch (Exception e) {
			logger.error("error: {} " + e);
			e.printStackTrace();
		}

	}
	
	/**
	 * Carrega as sublistas de followuo e carga.
	 */
	private void carregarListas() {
		try {
			
			listaRelatorio = relatorioFacade.carregarListasFollowUps( listaRelatorio );
			
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getTotalRegistros() {
		return 0;
	}

	@Override
	public void limpar() {
		listaRelatorio = new ArrayList<FollowUpImport>();
		comboPais = new ArrayList<Pais>();
		filtro = new BasicFiltroFollowUp();
		camposExibicao = new FiltroVisualizacaoCampos();
	}

	@Override
	public void refazerPesquisa() {
	}

	/**
	 * Retorna a Cidade onde se encontra a carga, dependendo do Status
	 * @param index
	 * @return
	 */
	public String getCidadeAtual( int index ) {
		
		Carga carga = listaRelatorio.get(index).getCarga();
		if ( carga.getStatus().equals(StatusCarga.OHI)
				|| carga.getStatus().equals(StatusCarga.ITT)) {
			if ( carga.getCidadeAtual() != null ) {
				return carga.getCidadeAtual().getSigla().toUpperCase();
			}
		}
		
		return "";
	}
	
	/**
	 * @return the fornecedores
	 */
	public List<PessoaJuridica> getFornecedores() {
		return fornecedores;
	}

	/**
	 * @param fornecedores the fornecedores to set
	 */
	public void setFornecedores(List<PessoaJuridica> fornecedores) {
		this.fornecedores = fornecedores;
	}

	/**
	 * @return the parametros
	 */
	public Map<String, Object> getParametros() {
		return parametros;
	}

	/**
	 * @param parametros the parametros to set
	 */
	public void setParametros(Map<String, Object> parametros) {
		this.parametros = parametros;
	}

	/**
	 * @return the listaRelatorio
	 */
	public List<FollowUpImport> getListaRelatorio() {
		return listaRelatorio;
	}

	/**
	 * @param listaRelatorio the listaRelatorio to set
	 */
	public void setListaRelatorio(List<FollowUpImport> listaRelatorio) {
		this.listaRelatorio = listaRelatorio;
	}

	/**
	 * @return the filtro
	 */
	public BasicFiltroFollowUp getFiltro() {
		return filtro;
	}

	/**
	 * @param filtro the filtro to set
	 */
	public void setFiltro(BasicFiltroFollowUp filtro) {
		this.filtro = filtro;
	}

	/**
	 * @return the camposExibicao
	 */
	public FiltroVisualizacaoCampos getCamposExibicao() {
		return camposExibicao;
	}

	/**
	 * @param camposExibicao the camposExibicao to set
	 */
	public void setCamposExibicao(FiltroVisualizacaoCampos camposExibicao) {
		this.camposExibicao = camposExibicao;
	}

	/**
	 * @return the comboPais
	 */
	public List<Pais> getComboPais() {
		return comboPais;
	}

	/**
	 * @param comboPais the comboPais to set
	 */
	public void setComboPais(ArrayList<Pais> comboPais) {
		this.comboPais = comboPais;
	}

	/**
	 * @return the comboAgenteCargas
	 */
	public List<PessoaJuridica> getComboAgenteCargas() {
		return comboAgenteCargas;
	}

	/**
	 * @param comboAgenteCargas the comboAgenteCargas to set
	 */
	public void setComboAgenteCargas(List<PessoaJuridica> comboAgenteCargas) {
		this.comboAgenteCargas = comboAgenteCargas;
	}

}
