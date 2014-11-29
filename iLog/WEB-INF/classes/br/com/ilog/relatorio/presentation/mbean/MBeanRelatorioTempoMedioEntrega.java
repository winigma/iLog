package br.com.ilog.relatorio.presentation.mbean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import br.cits.commons.citspresentation.mbeans.AbstractPaginacao;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.Pais;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroFollowUp;
import br.com.ilog.relatorio.business.dto.TempoMedioEntrega;
import br.com.ilog.relatorio.business.facade.RelatorioFacade;
import br.com.ilog.relatorio.util.ExportarRelatorio;

/**
 * @author Heber Santiago
 *
 */
@AccessScoped
@Controller( "mBeanRelatorioTempoMedioEntrega" )
public class MBeanRelatorioTempoMedioEntrega extends AbstractPaginacao {

	/** */
	private static final long serialVersionUID = -5921266016748130416L;
	
	static Logger logger = LoggerFactory.getLogger(MBeanRelatorioTempoMedioEntrega.class);
	
	/**
	 * @coment fachada de relatorio
	 */
	@Resource(name = "controllerRelatorio")
	RelatorioFacade relatorioFacade;

	/**
	 * @coment fachada de cadastros
	 */
	@Resource(name = "controllerCadastro")
	CadastroFacade cadastroFacade;
	
	/**
	 * @coment FiltroFollowup
	 * 
	 */
	private BasicFiltroFollowUp filtro;
	
	/**
	 * @coment Parametros da tela para o relatorio
	 * 
	 */
	private Map<String, Object> parametros;
	
	/**
	 * Combo de paises
	 */
	private List<Pais> comboPaises;
	
	/**
	 * @coment Lista de Rotas
	 */
	private List<TempoMedioEntrega> listaRelatorio;
	
	@PostConstruct
	void inicializar() {
		
		try {
			filtro = new BasicFiltroFollowUp();
			listaRelatorio = new ArrayList<TempoMedioEntrega>();
			
			popularComboPais();
			doPesquisar( null );
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * popular combo de paises. 
	 */
	private void popularComboPais() {
		comboPaises = new ArrayList<Pais>();
		
		try {
			comboPaises = cadastroFacade.listarPaises();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Prepara os parametros do relatorio.
	 */
	private void prepararParametrosTempoMedio(){
		parametros = new HashMap<String, Object>();
		FacesContext fc = FacesContext.getCurrentInstance();
		//Configurando os parametros de titulos
		parametros.put("titulo", TemplateMessageHelper.getMessage(
				MensagensSistema.MENU,"lblRelatorioTempoMedioEntrega",fc.getViewRoot().getLocale()));
		
		parametros.put("de", TemplateMessageHelper.getMessage(
				MensagensSistema.RELATORIO,"lblPeriodoDe",fc.getViewRoot().getLocale()));
		
		parametros.put("ate", TemplateMessageHelper.getMessage(
				MensagensSistema.RELATORIO,"lblAte",fc.getViewRoot().getLocale()));
		
		parametros.put("origem",TemplateMessageHelper.getMessage(
				MensagensSistema.RELATORIO,"lblOrigem",fc.getViewRoot().getLocale()));
		
		parametros.put("destino", TemplateMessageHelper.getMessage(
				MensagensSistema.RELATORIO,"lblDestino",fc.getViewRoot().getLocale()));
		
		parametros.put("totalDias", TemplateMessageHelper.getMessage(
				MensagensSistema.RELATORIO,"lblTotalDias",fc.getViewRoot().getLocale()));
		
		parametros.put("qtdCarga", TemplateMessageHelper.getMessage(
				MensagensSistema.RELATORIO,"lblQtdCarga",fc.getViewRoot().getLocale()));
		
		parametros.put("Emissao", TemplateMessageHelper.getMessage(
				MensagensSistema.RELATORIO,"lblEmissao",fc.getViewRoot().getLocale()));
		
		// informando os parametros do filtro
		if(filtro.getPaisDestino()!= null){
			parametros.put("FiltroDestino", filtro.getPaisDestino().getNome());
		}
		if(filtro.getPaisOrigem()!=null){
			parametros.put("FiltroOrigem", filtro.getPaisOrigem().getNome());
		}
		if(filtro.getDtInicio()!= null ){
			parametros.put("FiltroDtInicio", filtro.getDtInicio());
		}
		if(filtro.getDtFim()!= null ){
			parametros.put("FiltroDtFim", filtro.getDtFim());
		}
	}
	
	/**
	 * Metodo exporta para PDF
	 * @param arg0
	 */
	public void exportarPdf(){
		try {
			if ( listaRelatorio != null && !listaRelatorio.isEmpty() ) {
				prepararParametrosTempoMedio();
				parametros.put("tipo", "pdf");
				new ExportarRelatorio<TempoMedioEntrega>().gerarPDF(listaRelatorio, parametros, "/relatorios/relatorioTempoMedioEntrega.jasper", "TempoMedioEntregaOrigem.pdf");
			} else {
				FacesContext fc = FacesContext.getCurrentInstance();
				String message = TemplateMessageHelper.getMessage(MensagensSistema.SISTEMA,"MSG008",fc.getViewRoot().getLocale());
				Messages.adicionaMensagemDeInfo(message);
			}
		} catch (Exception e) {
			logger.error("error: {} " + e);
			e.printStackTrace();
		}
	}

	/**
	 * Metodo exporta para XSL
	 * @param arg0
	 */
	public void exportarXls(){
		try {
			if ( listaRelatorio != null && !listaRelatorio.isEmpty() ) {
				prepararParametrosTempoMedio();			
				parametros.put("tipo", "xls");
				new ExportarRelatorio<TempoMedioEntrega>().gerarXLS(listaRelatorio, parametros, "/relatorios/relatorioTempoMedioEntrega.jasper", "TempoMedioEntregaOrigem.xls");
			} else {
				FacesContext fc = FacesContext.getCurrentInstance();
				String message = TemplateMessageHelper.getMessage(MensagensSistema.SISTEMA,"MSG008",fc.getViewRoot().getLocale());
				Messages.adicionaMensagemDeInfo(message);
			}
		} catch (Exception e) {
			logger.error("error: {} " + e);
			e.printStackTrace();
		}
	}
	
	@Override
	public void doPesquisar(ActionEvent arg0) {
		
		FacesContext fc = FacesContext.getCurrentInstance();
		listaRelatorio = relatorioFacade.tempoMedioEntrega(filtro);
		
		if (listaRelatorio.isEmpty()) {
			String message = TemplateMessageHelper.getMessage(MensagensSistema.SISTEMA,"MSG008",fc.getViewRoot().getLocale());
			Messages.adicionaMensagemDeInfo(message);
		}

	}

	@Override
	public int getTotalRegistros() {
		return 0;
	}

	@Override
	public void limpar() {
		filtro = new BasicFiltroFollowUp();
		listaRelatorio = new ArrayList<TempoMedioEntrega>();
		
		popularComboPais();

	}

	@Override
	public void refazerPesquisa() {
		// TODO Auto-generated method stub

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
	 * @return the listaRelatorio
	 */
	public List<TempoMedioEntrega> getListaRelatorio() {
		return listaRelatorio;
	}

	/**
	 * @param listaRelatorio the listaRelatorio to set
	 */
	public void setListaRelatorio(List<TempoMedioEntrega> listaRelatorio) {
		this.listaRelatorio = listaRelatorio;
	}

}
