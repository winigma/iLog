package br.com.ilog.relatorio.presentation.mbean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

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
import br.com.ilog.cadastro.business.entity.Pais;
import br.com.ilog.cadastro.business.entity.PessoaJuridica;
import br.com.ilog.cadastro.business.entity.Rota;
import br.com.ilog.cadastro.business.entity.TipoPessoa;
import br.com.ilog.cadastro.business.entity.TipoPessoaEnum;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroPessoaJuridica;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;
import br.com.ilog.importacao.business.entity.Invoice;
import br.com.ilog.importacao.business.entity.StatusInvoice;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroCarga;
import br.com.ilog.importacao.business.facade.ImportacaoFacade;
import br.com.ilog.relatorio.business.dto.CargaRelatorioDinamicoImport;
import br.com.ilog.relatorio.util.ExportarRelatorio;
import br.com.ilog.seguranca.business.facade.SegurancaFacade;

@AccessScoped
@Controller( "mBeanRelDinanicoImport" )
public class MBeanRelDinanicoImport extends AbstractPaginacao {

	/** */
	private static final long serialVersionUID = 2836566513486127806L;

	@Resource( name = "controllerImportacao" )
	ImportacaoFacade facade;
	
	@Resource( name = "controllerCadastro" )
	CadastroFacade cadastro;
	
	@Resource( name = "controleUsuario" )
	SegurancaFacade seguranca;
	
	/**
	 * filtro de pesquisa.
	 */
	private BasicFiltroCarga filtro;
	
	/**
	 * combo de pais
	 */
	private List<Pais> comboPais;
	
	/**
	 * combo agente de cargas
	 */
	private List<PessoaJuridica> comboAgenteCargas;
	
	/**
	 * combo status carga
	 */
	private List<SelectItem> comboStatus;
	
	/**
	 * utilitario para geracao do relatorio.
	 */
	private ExportarRelatorio<CargaRelatorioDinamicoImport> relatorio;
	
	/**
	 * resulta da pesquisa pelo filtro.
	 */
	private List<CargaRelatorioDinamicoImport> result;
	
	/**
	 * mapa com os parematros do relatorio.
	 */
	private Map<String, Object> parametros = new HashMap<String, Object>();
	
	@PostConstruct
	public void inicializar() {
		filtro = new BasicFiltroCarga();
		filtro.setRota( new Rota() );
		relatorio = new ExportarRelatorio<CargaRelatorioDinamicoImport>();
		
		popularComboPais();
		popularComboAgente();
		popularComboStatus();
		
		doPesquisar( null );
		
	}
	
	/**
	 * preenche o combo de status, com status possiveis de carga.
	 */
	private void popularComboStatus() {

		comboStatus = new ArrayList<SelectItem>();
		for (StatusInvoice status : StatusInvoice.valores() ) {
			comboStatus.add(new SelectItem(status, TemplateMessageHelper
					.getMessage(MensagensSistema.INVOICE, status.name())));
		}
	}

	/**
	 * preenche combo de agente de cargas. 
	 */
	private void popularComboAgente() {
		comboAgenteCargas = new ArrayList<PessoaJuridica>();
		try {
			comboAgenteCargas = cadastro
					.listarAllPersons(new BasicFiltroPessoaJuridica(
							new TipoPessoa( TipoPessoaEnum.A_CARGA.toString() ), true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * preenche combo de pais.
	 */
	private void popularComboPais() {
		comboPais = new ArrayList<Pais>();
		try {
			
			comboPais = cadastro.listarPaises();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void doPesquisar(ActionEvent arg0) {
		
		result = prepararItensRelatorio();
		
		if (result.isEmpty()) {
			FacesContext fc = FacesContext.getCurrentInstance();
			
			String msg = TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG008", fc.getViewRoot()
							.getLocale());
			Messages.adicionaMensagemDeInfo(msg);
		}
	}

	/**
	 * Prepara os parametros do relatorio.
	 * @return
	 */
	private Map<String, Object> prepararParametros(){
		
		FacesContext fc = FacesContext.getCurrentInstance();
		
		//Configurando os parametros de titulos
		parametros.put("titulo", TemplateMessageHelper.getMessage( MensagensSistema.RELATORIO,"lblRelatorioDinamicoImportacao",fc.getViewRoot().getLocale()));
		parametros.put("de", TemplateMessageHelper.getMessage( MensagensSistema.RELATORIO,"lblPeriodoDe",fc.getViewRoot().getLocale()));
		parametros.put("ate", TemplateMessageHelper.getMessage( MensagensSistema.RELATORIO,"lblAte",fc.getViewRoot().getLocale()));
		parametros.put("origem",TemplateMessageHelper.getMessage(MensagensSistema.RELATORIO,"lblOrigem",fc.getViewRoot().getLocale()));
		parametros.put("destino", TemplateMessageHelper.getMessage( MensagensSistema.RELATORIO,"lblDestino",fc.getViewRoot().getLocale()));
		parametros.put("Emissao", TemplateMessageHelper.getMessage( MensagensSistema.RELATORIO,"lblEmissao",fc.getViewRoot().getLocale()));
		
		//PARAMENTROS DE CARGA
		parametros.put("REPORT_RESOURCE_BUNDLE", ResourceBundle.getBundle("br.com.ilog.geral.presentation.relatorio", fc.getViewRoot().getLocale()));
		
		
		// informando os parametros do filtro
		if ( filtro.getAps() != null && !filtro.getAps().equals("") ) {
			parametros.put( "NumAPS", filtro.getAps() );
		}
		if ( filtro.getHawb() != null && !filtro.getHawb().equals("") ) {
			parametros.put( "Hawb", filtro.getHawb() );
		}
		if ( filtro.getCanal() != null ) {
			parametros.put( "Canal", filtro.getCanal().getCanal().name() );
		}
		if ( filtro.getStatus() != null ) {
			parametros.put("Status", filtro.getStatus().name() );
		}
		if ( filtro.getAgenteCarga() != null  ) {
			parametros.put("AgCargas", filtro.getAgenteCarga().getNomeFantasia() );
		}
		
		if(filtro.getRota()!= null ){
			if ( filtro.getRota().getPaisDestino() != null )
				parametros.put("FiltroDestino", filtro.getRota().getPaisDestino().getNome());
			if(filtro.getRota().getPaisOrigem()!= null){
				parametros.put("FiltroOrigem", filtro.getRota().getPaisOrigem().getNome());
			}
		}
		if(filtro.getDtInicioColeta()!= null ){
			parametros.put("FiltroDtInicio", filtro.getDtInicioColeta());
		}
		if(filtro.getDtFimColeta()!= null ){
			parametros.put("FiltroDtFim", filtro.getDtFimColeta());
		}
		
		return parametros;
	}

	/**
	 * Prepara os itens para o relatorio de importacao.
	 * @return
	 */
	private List<CargaRelatorioDinamicoImport> prepararItensRelatorio() {
		
		List<CargaRelatorioDinamicoImport> resultado = new ArrayList<CargaRelatorioDinamicoImport>();
		try {
			List<Invoice> invoices = facade.listarInvoices(filtro);
			
			CargaRelatorioDinamicoImport relatorioItem;
			
			for ( Invoice invoice : invoices ) {
				relatorioItem = new CargaRelatorioDinamicoImport();
				if ( invoice.getCarga() != null ) {
					relatorioItem.setCarga( facade.getCargaByIdRelatorio( invoice.getCarga().getId() ) );
					relatorioItem.setFollowUp( facade.getFollowUpByCarga( relatorioItem.getCarga() ) );
				}
				relatorioItem.setInvoice(facade.getInvoiceById( invoice.getId() , true) );
				relatorioItem.setExportadorInvoice( relatorioItem.getInvoice().getExportador() );
				
				resultado.add( relatorioItem );
			}
			return resultado;
			
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		
		return new ArrayList<CargaRelatorioDinamicoImport>();
	}

	/**
	 * @param arg
	 */
	public void exportXLS() {
		
		if (verificarSelecaoCampos()) {
			try {
				relatorio.gerarXLSDinamico(prepararItensRelatorio(),
						prepararParametros(),
						"/relatorios/relatorioDinamicoImport.jrxml",
						"/relatorios/relatorioDinamicoImport.jasper",
						"relatorioDinamicoImport");
	
			} catch (BusinessException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Messages.adicionaMensagemDeErro(
					TemplateMessageHelper.getMessage(MensagensSistema.RELATORIO, "msgSelecioneUmCampo") );
		}
	}

	/**
	 * Verificar se selecionou pelo menos um campo.
	 * @return
	 */
	private boolean verificarSelecaoCampos() {
		
		if ( relatorio != null ) {
			if ( relatorio.isSelecioneTodos() ) {
				return true;
			}
			if ( relatorio.isProcessoInvoice() ) {
				return true;
			}
			if ( relatorio.isDtInvoice() ) {
				return true;
			}
			if ( relatorio.isExportadorInvoice() ) {
				return true;
			}
			if ( relatorio.isCompradorInvoice() ) {
				return true;
			}
			if ( relatorio.isOrigemInvoice() ) {
				return true;
			}
			if ( relatorio.isIncoterm() ) {
				return true;
			}
			if ( relatorio.isCritico() ) {
				return true;
			}
			if ( relatorio.isPliNumber() ) {
				return true;
			}
			if ( relatorio.isTotalPesoLiqInvoice() ) {
				return true;
			}
			if ( relatorio.isTotalPesoBrutoInvoice() ) {
				return true;
			}
			if ( relatorio.isCanal() ) {
				return true;
			}
			if ( relatorio.isDtColeta() ) {
				return true;
			}
			if ( relatorio.isDtColetaReal() ) {
				return true;
			}
			if ( relatorio.isDtPrevista() ) {
				return true;
			}
			if ( relatorio.isDtReal() ) {
				return true;
			}
			if ( relatorio.isExportFornecedor() ) {
				return true;
			}
			if ( relatorio.isHawb() ) {
				return true;
			}
			if ( relatorio.isInvoice() ) {
				return true;
			}
			if ( relatorio.isModal() ) {
				return true;
			}
			if ( relatorio.isNumAps() ) {
				return true;
			}
			if ( relatorio.isRota() ) {
				return true;
			}
			if ( relatorio.isStatus() ) {
				return true;
			}
			if ( relatorio.isTotalDiasEst() ) {
				return true;
			}
			if ( relatorio.isTotalDiasReal() ) {
				return true;
			}
			if ( relatorio.isResponsavel() ) {
				return true;
			}
			if ( relatorio.isCidadeOrigem() ) {
				return true;
			}
			if ( relatorio.isPesoBruto() ) {
				return true;
			}
			if ( relatorio.isPesoCubado() ) {
				return true;
			}
			if ( relatorio.isNumeroDi() ) {
				return true;
			}
			if ( relatorio.isDtRegistroDi() ) {
				return true;
			}
			if(relatorio.isObservacaoInvoice()){
				return true;
			}
			
		}
		
		return false;
	}
	
	/**
	 * @param ae
	 */
	public void selecionarTodos() {
		System.out.println();
	}

	@Override
	public int getTotalRegistros() {
		return 0;
	}

	@Override
	public void limpar() {
		
		filtro = new BasicFiltroCarga();
		filtro.setRota( new Rota() );
		
		result = new ArrayList<CargaRelatorioDinamicoImport>();

	}

	@Override
	public void refazerPesquisa() {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the filtro
	 */
	public BasicFiltroCarga getFiltro() {
		return filtro;
	}

	/**
	 * @param filtro the filtro to set
	 */
	public void setFiltro(BasicFiltroCarga filtro) {
		this.filtro = filtro;
	}

	/**
	 * @return the relatorio
	 */
	public ExportarRelatorio<CargaRelatorioDinamicoImport> getRelatorio() {
		return relatorio;
	}

	/**
	 * @param relatorio the relatorio to set
	 */
	public void setRelatorio(
			ExportarRelatorio<CargaRelatorioDinamicoImport> relatorio) {
		this.relatorio = relatorio;
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
	public void setComboPais(List<Pais> comboPais) {
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
	 * @return the result
	 */
	public List<CargaRelatorioDinamicoImport> getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(List<CargaRelatorioDinamicoImport> result) {
		this.result = result;
	}

}
