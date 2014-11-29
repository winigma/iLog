package br.com.ilog.importacao.business.mbean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;

import org.hibernate.exception.ConstraintViolationException;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citspresentation.mbeans.AbstractManter;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.commons.citspresentation.util.JSFRequestBean;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.Incoterm;
import br.com.ilog.cadastro.business.entity.UnidadeMedida;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;
import br.com.ilog.geral.presentation.util.BigDecimalUtils;
import br.com.ilog.geral.presentation.util.DataUtils;
import br.com.ilog.importacao.business.entity.Invoice;
import br.com.ilog.importacao.business.entity.ProcBroker;
import br.com.ilog.importacao.business.entity.ProcCarga;
import br.com.ilog.importacao.business.entity.ProcInvoice;
import br.com.ilog.importacao.business.entity.ProcItem;
import br.com.ilog.importacao.business.entity.ProcItemPo;
import br.com.ilog.importacao.business.entity.ProcStatus;
import br.com.ilog.importacao.business.facade.ImportacaoFacade;

@AccessScoped
@Controller("mBeanDadosBroker")
public class MBeanDadosBroker extends AbstractManter {
	
	/** */
	private static final long serialVersionUID = -308130357656651553L;

	@Resource( name = "controllerImportacao" )
	ImportacaoFacade facade;
	
	@Resource(name = "controllerCadastro")
	CadastroFacade cadastro;
	
	Logger logger = LoggerFactory.getLogger( MBeanDadosBroker.class );
	
	private static final int BROKER = 0;
	private static final int PROC_ITEM = 1;
	private static final int PROC_CARGA = 2;
	private static final int PROC_INVOICE = 3;
	private static final int PROC_STATUS = 4;
	
	/**
	 * Objeto que recebe os dados de detalhe do broker;
	 */
	private ProcBroker detalheBroker;
	private List<ProcItemPo> itensPo;
	
	/**
	 * objeto que recebe as informacoes do broker de importacao
	 */
	private ProcBroker dadosBroker;

	/**
	 * arquivo importado
	 */
	private UploadedFile fileImport;
	
	/**
	 * lista de Proc Brokers extraido do arquivo.
	 */
	private List<ProcBroker> brokersImport;
	
	public void obterUpload( FileUploadEvent evn ) {
		try {
			
			fileImport = evn.getFile();
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
	
	/**
	 * Recupera o nome do arquivo
	 * @return
	 */
	public String getNomeArquivo() {
		
		//apenas se estiver carregado o arquivo que retorna
		if ( fileImport != null ) {
			return fileImport.getFileName();
		}
		
		return "";
	}
	
	/**
	 * 
	 */
	public void limparImport() {
		fileImport = null;
	}
	
	/**
	 * metodo para ler cada linha e extrair as informacoes necessarias.
	 */
	public void importarArquivo() {
		
		try {
			
			RequestContext.getCurrentInstance().addCallbackParam("closeImportModal", false);

			InputStream is = fileImport.getInputstream();
			InputStreamReader isr = new InputStreamReader( is );
			BufferedReader bufferedReader = new BufferedReader( isr );
			
			String linha = "";
			
			ProcBroker broker = new ProcBroker();
			Map<String, ProcItem> mapaItens = new HashMap<String, ProcItem>();
			
			brokersImport = new ArrayList<ProcBroker>();
			
			int nrLinha = 0;
			
            while ( ( linha = bufferedReader.readLine() ) != null) {
            	
            	int key = Integer.parseInt( linha.substring(0, 1) );
            	
				if ( nrLinha == 0 && key != BROKER ) {
					//Erro do arquivo
					throw new Exception( "Arquivo não iniciou com digito 0, como no padrão" );
				}
            	switch (key) {
				case BROKER:
					//extrair informacoes de broker
					broker = extrairDadosBroker( linha );
					mapaItens = new HashMap<String, ProcItem>();
					brokersImport.add( broker );
					break;
					
				case PROC_ITEM:
					//extrair informacoes de item
					extrairDadosItem( linha, broker, mapaItens );
					break;
					
				case PROC_CARGA:
					//extrair informacoes de carga
					extrairDadosCarga( linha, broker );
					break;
					
				case PROC_INVOICE:
					//extrair informacoes de invoice
					extrairDadosInvoice( linha, broker );
					break;
					
				case PROC_STATUS:
					//extrair informacoes de status
					extrairDadosStatus( linha, broker );
					break;
					
				default:
					throw new Exception();
				}
            	nrLinha++;
            }

            facade.salvarImportacaoBroker( brokersImport );
            
            FacesContext fc = FacesContext.getCurrentInstance();
            String message = TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG001", fc.getViewRoot()
							.getLocale());
			Messages.adicionaMensagemDeInfo(message);
			
			RequestContext.getCurrentInstance().addCallbackParam("closeImportModal", true);
			
			fileImport = null;
			
			refazerPesquisa();
			
		} catch (IOException e) {
			e.printStackTrace();
			RequestContext.getCurrentInstance().addCallbackParam("closeImportModal", true);
			logger.error("erro: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.BROKER, "MSG022"));
			
		} catch (Exception e) {
			
			e.printStackTrace();
			RequestContext.getCurrentInstance().addCallbackParam("closeImportModal", true);
			logger.error("erro: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.BROKER, "MSG022"));
			
		}
		
	}
	
	private String extrairSubstring( String str, int inicio, int tamanho ) {
		
		String s = str.substring(inicio, inicio + tamanho).trim();
		
		return s;
	}
	
	/**
	 * Extrai as informacoes de broker da string que representa uma linha.
	 * @param linha
	 * @return
	 * @throws BusinessException 
	 */
	private ProcBroker extrairDadosBroker( String linha ) throws BusinessException {

		ProcBroker broker = new ProcBroker();
		
		broker.setDespachante( Integer.parseInt( extrairSubstring( linha, 1, 1) ) );
		broker.setCnpjFoxconn( extrairSubstring(linha, 2, 14) );
		broker.setNrDI( extrairSubstring( linha, 16, 10) );
		broker.setDataDI( DataUtils.converteData( extrairSubstring(linha, 26, 8), "ddMMyyyy") );
		broker.setTipoTransporte( extrairSubstring( linha, 34, 1 ) );
		
		Incoterm i = cadastro.getIncotermBySigla( extrairSubstring( linha, 35, 3), true );
		broker.setIncoterm( i );
		
		broker.setCdMoedaFOB( Integer.parseInt( extrairSubstring( linha, 38, 3) ) );
		broker.setPesoLiqTotal( BigDecimalUtils.parserToBigDecimal( extrairSubstring( linha, 41, 11), 3) );
		broker.setPesoBrutoTotal( BigDecimalUtils.parserToBigDecimal( extrairSubstring( linha, 52, 11), 3) );
		broker.setDataPLI( DataUtils.converteData(extrairSubstring(linha, 63, 8), "ddMMyyyy") );
		broker.setDataLI( DataUtils.converteData( extrairSubstring(linha, 71, 8), "ddMMyyyy") );
		broker.setTipoDeclaracao( Integer.parseInt( extrairSubstring( linha, 79, 1) ) );
		broker.setUfrEntrada( Integer.parseInt( extrairSubstring( linha, 80, 7) ) );
		broker.setUfrDespacho( Integer.parseInt( extrairSubstring( linha, 87, 7) ) );
		broker.setHawb( extrairSubstring( linha, 94, 20) );
		broker.setTipoVolume( Long.parseLong( extrairSubstring( linha, 114, 3) ) );
		broker.setQtdVolume( BigDecimalUtils.parserToBigDecimal( extrairSubstring( linha, 117, 16), 4 ) );
		broker.setDtChegadaETA( DataUtils.converteData( extrairSubstring( linha, 133, 8), "ddMMyyyy") );
		broker.setNumMaster( extrairSubstring( linha, 141, 20) );
		broker.setLocalEmbarque( extrairSubstring( linha, 161, 20) );
		broker.setNomeTransportador( extrairSubstring( linha, 181, 20) );
		broker.setDtEmbarqueETD( DataUtils.converteData( extrairSubstring( linha, 201, 8), "ddMMyyyy") );
		broker.setDtMantra( DataUtils.converteData( extrairSubstring( linha, 209, 8), "ddMMyyyy") );
		broker.setCanal( extrairSubstring( linha, 217, 1) );
		broker.setDtParametrizacao( DataUtils.converteData( extrairSubstring( linha, 218, 8), "ddMMyyyy") );
		broker.setDtDistribuicao( DataUtils.converteData( extrairSubstring( linha, 226, 8), "ddMMyyyy" ) );
		broker.setNomeFiscal( extrairSubstring( linha, 234, 20) );
		broker.setTranspUrbano( extrairSubstring( linha, 254, 40) );
		broker.setDtEntrega( DataUtils.converteData( extrairSubstring( linha, 294, 8), "ddMMyyyy") );
		broker.setVlFOBLocEmbarque( BigDecimalUtils.parserToBigDecimal( extrairSubstring( linha, 302, 16), 4) );
		broker.setVlFretePrepaid( BigDecimalUtils.parserToBigDecimal( extrairSubstring( linha, 318, 16), 4) );
		broker.setVlFreteColect( BigDecimalUtils.parserToBigDecimal( extrairSubstring( linha, 334, 16), 4) );
		broker.setCdMoedaFrete( Long.parseLong( extrairSubstring( linha, 350, 3) ) );
		broker.setVlTaxaFrete( BigDecimalUtils.parserToBigDecimal( extrairSubstring( linha, 353, 16), 4) );
		broker.setVlSeguroNegociada( BigDecimalUtils.parserToBigDecimal( extrairSubstring(linha, 369, 16), 4) );
		broker.setCdMoedaSeguro( Long.parseLong( extrairSubstring( linha, 385, 3) ) );
		broker.setVlTaxaSeguro( BigDecimalUtils.parserToBigDecimal( extrairSubstring( linha, 388, 16), 4) );
		broker.setVlII( BigDecimalUtils.parserToBigDecimal( extrairSubstring( linha, 404, 16), 4) );
		broker.setVlII( BigDecimalUtils.parserToBigDecimal( extrairSubstring( linha, 404, 16), 4) );
		broker.setVlIPI( BigDecimalUtils.parserToBigDecimal( extrairSubstring( linha, 420, 16), 4) );
		broker.setVlPIS( BigDecimalUtils.parserToBigDecimal( extrairSubstring( linha, 436, 16), 4) );
		broker.setVlConfins( BigDecimalUtils.parserToBigDecimal( extrairSubstring( linha, 452, 16), 4) );
		broker.setVlConfins( BigDecimalUtils.parserToBigDecimal( extrairSubstring( linha, 452, 16), 4) );
		broker.setVlTaxaSiscomex( BigDecimalUtils.parserToBigDecimal( extrairSubstring( linha, 468, 16), 4) );
		broker.setVlTaxaSuframa( BigDecimalUtils.parserToBigDecimal( extrairSubstring( linha, 484, 16), 4) );
		broker.setVlMulta( BigDecimalUtils.parserToBigDecimal( extrairSubstring( linha, 500, 16), 4) );
		broker.setVlTaxaFOB( BigDecimalUtils.parserToBigDecimal( extrairSubstring( linha, 516, 15), 5) );
		broker.setRecinto( extrairSubstring( linha, 531, 20) );
		broker.setVlArmazenagem( BigDecimalUtils.parserToBigDecimal( extrairSubstring( linha, 551, 16), 4) );
		broker.setVlCapatazia( BigDecimalUtils.parserToBigDecimal(extrairSubstring( linha, 575, 16), 4) );
		broker.setDtCapatazia( DataUtils.converteData( extrairSubstring( linha, 591, 8), "ddMMyyyy") );
		broker.setVlDesova( BigDecimalUtils.parserToBigDecimal( extrairSubstring( linha, 599, 16), 4) );
		broker.setDtDesova( DataUtils.converteData( extrairSubstring(linha, 615, 8), "ddMMyyyy") );
		broker.setVlComissaoDespachante( BigDecimalUtils.parserToBigDecimal( extrairSubstring( linha, 623, 16), 4) );
		broker.setCdPais( Long.parseLong( extrairSubstring( linha, 639, 3) ) );
		broker.setVlTaxaDiUsd( BigDecimalUtils.parserToBigDecimal( extrairSubstring( linha, 642, 16), 4) );
		
		return broker;
	}
	
	/**
	 * Extrai e Adiciona item ao Broker.
	 * @param linha
	 * @param broker
	 * @param mapaItens 
	 * @return
	 * @throws BusinessException 
	 */
	private ProcBroker extrairDadosItem( String linha, ProcBroker broker, Map<String, ProcItem> mapaItens ) throws BusinessException {
		
		String ncmNrAdicao = extrairSubstring( linha, 1, 20) + extrairSubstring( linha, 21, 3);
		
		boolean novoItem = true;
		
		ProcItem item = new ProcItem();
		item.setProcBroker( broker );
		item.setItensPo( new ArrayList<ProcItemPo>() );
		
		if ( mapaItens.get( ncmNrAdicao ) != null ) {
			item = mapaItens.get( ncmNrAdicao );
			novoItem = false;
		} else {
			mapaItens.put( ncmNrAdicao, item);
		}
		
		item.setNcm( extrairSubstring( linha, 1, 20) );
		item.setNrAdicao( Integer.parseInt( extrairSubstring( linha, 21, 3) ) );
		item.setNrLi( extrairSubstring( linha, 509, 10) );
		item.setDtLi( DataUtils.converteData( extrairSubstring( linha, 534, 8), "ddMMyyyy") );
		item.setAplicacao( Integer.parseInt( extrairSubstring( linha, 542, 1) ) );
		item.setCoberturaCambial( Integer.parseInt( extrairSubstring(linha, 543, 2) ) );
		item.setFundamentoLegal( Integer.parseInt( extrairSubstring( linha, 545, 2) ) );
		item.setRegimeTributario( Integer.parseInt( extrairSubstring( linha, 547, 1)) );
		item.setPeriodicidade( Integer.parseInt( extrairSubstring( linha, 548, 3) ) );
		
		ProcItemPo itemPo = new ProcItemPo();
		itemPo.setProcItem( item );
		itemPo.setProdutoSuframa( extrairSubstring( linha, 24, 11) );
		itemPo.setDetalheSuframa( extrairSubstring( linha, 35, 14) );
		itemPo.setDescricaoSuframa( extrairSubstring( linha, 49, 390) );
		itemPo.setQuantidade( BigDecimalUtils.parserToBigDecimal( extrairSubstring( linha, 439, 16), 4) );
		
		UnidadeMedida ume = cadastro.getUnidMedidaByCodigo( extrairSubstring( linha, 455, 4), true );
		itemPo.setUnidadeMedida( ume );
		
		itemPo.setUme( extrairSubstring( linha, 455, 4) );
		itemPo.setPesoLiquido( BigDecimalUtils.parserToBigDecimal( extrairSubstring( linha, 459, 16), 4) );
		itemPo.setVlUnitario( BigDecimalUtils.parserToBigDecimal( extrairSubstring( linha, 475, 16), 4) );
		itemPo.setCdMoedaFOB( Long.parseLong( extrairSubstring(linha, 491, 3) ) );
		itemPo.setTxMoedaFob( BigDecimalUtils.parserToBigDecimal( extrairSubstring( linha, 494, 15), 5) );
		itemPo.setPartNumber( extrairSubstring( linha, 519, 15) );
		itemPo.setNrItem( Integer.parseInt( extrairSubstring( linha, 551, 2) ) );
		itemPo.setNomeFabricante( extrairSubstring(linha, 553, 60 ) );
		itemPo.setItemPo( Integer.parseInt( extrairSubstring( linha, 613, 3 ) ) );
		itemPo.setNrPo( extrairSubstring( linha, 616, 10 ) );
		
		item.getItensPo().add( itemPo );
		
		if ( broker.getItens() == null ) {
			broker.setItens( new ArrayList<ProcItem>() );
		}
		
		if ( novoItem )
			broker.getItens().add( item );
		
		
		return broker;
	}
	
	/**
	 * Extrair dados de ProcCarga.
	 * @param linha
	 * @param broker
	 */
	private void extrairDadosCarga( String linha, ProcBroker broker ) {
		ProcCarga carga = new ProcCarga();
		
		carga.setProcBroker( broker );
		carga.setNrDocCarga( extrairSubstring( linha, 1, 30) );
		carga.setNrContainer( extrairSubstring( linha, 31, 30) );
		carga.setCapacidade( Integer.parseInt( extrairSubstring( linha, 61, 4) ) ) ;
		carga.setSelo( extrairSubstring( linha, 65, 20) );
		
		if ( broker.getCargas() == null ) {
			broker.setCargas( new ArrayList<ProcCarga>() );
		}
		
		broker.getCargas().add( carga );
		
	}
	
	/**
	 * Extrair dados de Proc Invoice
	 * @param linha
	 * @param broker
	 * @throws BusinessException 
	 */
	private void extrairDadosInvoice( String linha, ProcBroker broker ) throws BusinessException {
		
		ProcInvoice invoice = new ProcInvoice();
		
		invoice.setProcBroker(broker);
		invoice.setNrInvoice( extrairSubstring( linha, 1, 30) );
		
		Invoice inv = facade.getInvoiceByNumero( invoice.getNrInvoice() );
		invoice.setInvoice( inv );
		
		invoice.setExportador( extrairSubstring( linha, 31, 60) );
		invoice.setDtInvoice( DataUtils.converteData( extrairSubstring( linha, 91, 8), "ddMMyyyy") );
		
		if ( broker.getInvoices() == null ) {
			broker.setInvoices( new ArrayList<ProcInvoice>() );
		}
		
		broker.getInvoices().add( invoice );
	}
	
	/**
	 * Extrair e carregar dados de Status
	 * @param linha
	 * @param broker
	 */
	private void extrairDadosStatus( String linha, ProcBroker broker ) {
		
		ProcStatus status = new ProcStatus();
		status.setDtAbertura( DataUtils.converteData( extrairSubstring( linha, 1, 8), "ddMMyyyy") );
		status.setDescricao( extrairSubstring( linha, 9, 120) );
		status.setStatus( extrairSubstring( linha, 129, 1) );
		status.setDtFechamento( DataUtils.converteData( extrairSubstring( linha, 130, 8), "ddMMyyyy") );
		
		if ( broker.getStatus() == null ) {
			broker.setStatus( new ArrayList<ProcStatus>() );
		}
		broker.getStatus().add( status );
		
	}
	
	@Override
	public String editar() {
		
		Long idRegistro = Long.valueOf(JSFRequestBean.getParameter("id"));
		
		try {
			detalheBroker = facade.getProcBrokerById( idRegistro, true );
			
			carrregarDadosBroker();
			
		} catch (BusinessException e) {
			e.printStackTrace();
			
			logger.error( "error: {} ", e );
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.MOTIVO, e));
			return "";
		}
		
		return "dadosBroker.jsf";
	}

	private void carrregarDadosBroker() {

		itensPo = new ArrayList<ProcItemPo>();
			
		if ( detalheBroker != null ) {
			for (ProcItem item : detalheBroker.getItens() ) {
				itensPo.addAll( item.getItensPo() );
			}
			
		}
	}

	@Override
	public String excluir() {
		
		try {
			facade.excluirProcBroker( detalheBroker );

		} catch (Exception e) {
			logger.error("erro: {}", e);

			ConstraintViolationException exc = (ConstraintViolationException) e.getCause();
			if(exc.getSQLException().getNextException().getMessage().contains("exclusão")){
				Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
						MensagensSistema.SISTEMA,"MSG_GEN_004" ));				
			}
			
			e.printStackTrace();
			return null;
		}
		FacesContext fc = FacesContext.getCurrentInstance();
		Messages.adicionaMensagemDeInfo(TemplateMessageHelper.getMessage(
				MensagensSistema.SISTEMA, "MSG_EXCLUIR_SUCESSO", fc
						.getViewRoot().getLocale()));
		
		
		return "pesquisa.jsf";
	}

	@Override
	@PostConstruct
	public void inicializarObjetos() {
		detalheBroker = new ProcBroker();
		itensPo = new ArrayList<ProcItemPo>();
		fileImport = null;
		

	}

	@Override
	protected void refazerPesquisa() {
		
	}

	/**
	 * @return the dadosBroker
	 */
	public ProcBroker getDadosBroker() {
		return dadosBroker;
	}

	/**
	 * @param dadosBroker the dadosBroker to set
	 */
	public void setDadosBroker(ProcBroker dadosBroker) {
		this.dadosBroker = dadosBroker;
	}

	/**
	 * @return the fileImport
	 */
	public UploadedFile getFileImport() {
		return fileImport;
	}

	/**
	 * @param fileImport the fileImport to set
	 */
	public void setFileImport(UploadedFile fileImport) {
		this.fileImport = fileImport;
	}

	/**
	 * @return the detalheBroker
	 */
	public ProcBroker getDetalheBroker() {
		return detalheBroker;
	}

	/**
	 * @param detalheBroker the detalheBroker to set
	 */
	public void setDetalheBroker(ProcBroker detalheBroker) {
		this.detalheBroker = detalheBroker;
	}

	/**
	 * @return the brokersImport
	 */
	public List<ProcBroker> getBrokersImport() {
		return brokersImport;
	}

	/**
	 * @param brokersImport the brokersImport to set
	 */
	public void setBrokersImport(List<ProcBroker> brokersImport) {
		this.brokersImport = brokersImport;
	}

	/**
	 * @return the itensPo
	 */
	public List<ProcItemPo> getItensPo() {
		return itensPo;
	}

	/**
	 * @param itensPo the itensPo to set
	 */
	public void setItensPo(List<ProcItemPo> itensPo) {
		this.itensPo = itensPo;
	}

	/**
	 * Retorna uma sbustring de uma string.
	 * @param item
	 * @param inicial
	 * @param fim
	 * @return
	 */
	public String getSubstring( String item, int inicial, int fim) {
		if ( item.length() <= fim )
			return item;
		else 
			return item.substring(inicial, fim - 3) + "...";
	}
}
