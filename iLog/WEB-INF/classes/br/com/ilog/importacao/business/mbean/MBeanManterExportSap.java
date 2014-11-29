package br.com.ilog.importacao.business.mbean;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citspresentation.mbeans.AbstractManter;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.TipoPacote;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;
import br.com.ilog.importacao.business.dto.SapDI;
import br.com.ilog.importacao.business.dto.SapItemDI;
import br.com.ilog.importacao.business.dto.SapItemDIFatura;
import br.com.ilog.importacao.business.dto.SapItemDITaxa;
import br.com.ilog.importacao.business.entity.Carga;
import br.com.ilog.importacao.business.entity.CustoDI;
import br.com.ilog.importacao.business.entity.Invoice;
import br.com.ilog.importacao.business.entity.ItemInvoice;
import br.com.ilog.importacao.business.entity.ProcBroker;
import br.com.ilog.importacao.business.entity.ProcItem;
import br.com.ilog.importacao.business.entity.ProcItemPo;
import br.com.ilog.importacao.business.facade.ImportacaoFacade;

/**
 * @author Heber Santiago
 *
 */
@AccessScoped
@Controller( "mBeanManterExportSap" )
public class MBeanManterExportSap extends AbstractManter {

	/** */
	private static final long serialVersionUID = 2589896867166460115L;

	@Resource( name = "controllerImportacao" )
	ImportacaoFacade importacaoFacade;
	
	@Resource( name = "controllerCadastro" )
	CadastroFacade cadastroFacade;
	
	/**
	 * Atributos Static Final que identifica os tipos de aplicacao;
	 */
	protected static final int COMERCIALIZACAO = 0;
	protected static final int INDUSTRIALIZACAO = 1;
	protected static final int USO_PROPRIO = 2;
	protected static final int OUTRAS = 3;
	protected static final int PROEX = 4;
	
	private static Logger logger = LoggerFactory.getLogger(MBeanManterExportSap.class);

	/**
	 * Processo a ser exportado
	 */
	private Carga processo;
	
	/**
	 * lista de todos os custos cadastrados.
	 */
	private List<CustoDI> custosDI;
	/**
	 * lista de despesas com custos cadastrados.
	 */
	private List<CustoDI> depesas;
	
	/**
	 * lista de fretes com custos cadastrados.
	 */
	private List<CustoDI> fretes;
	
	/**
	 * 
	 */
	private SapDI di;
	
	private SapItemDI sapItemDI;
	
	private SapItemDITaxa sapItemDITaxa;
	
	/**
	 * Mapa de Itens PO do Broker
	 * Chave:  item PO e PO
	 */
	private Map<String, ProcItemPo> itensPOBroker;
	
	/**
	 * Mapa de itens de Invoice
	 * Chave:  item PO e PO
	 */
	private Map<String, List<ItemInvoice>> itensInvoice;

	private List<String> listaNrInvoices;
	
	/**
	 * Valor total dos itens de invoice.
	 */
	private BigDecimal totalItensInvoice;
	
	/**
	 * Total das despesas de Frete em BRL.
	 */
	private BigDecimal valorFretes;
	
	/**
	 * Total de outras despesas em BRL;
	 */
	private BigDecimal totalDespesas;
	
	/**
	 * Total da despesa Collect Fee em BRL
	 */
	private BigDecimal valorCollectFee;
	
	/**
	 * Total dos itens em BRL.
	 */
	private BigDecimal valorFobBrl;
	
	/**
	 * Total dos itens na moeda;
	 */
	private BigDecimal valorFob;
	
	private boolean pisConfins = false;
	
	@PostConstruct
	public void inicializar() {
		edicao = false;
		processo = new Carga();
		itensInvoice = new HashMap<String, List<ItemInvoice>>(0);
		itensPOBroker = new HashMap<String, ProcItemPo>(0);
		totalItensInvoice = BigDecimal.ZERO;
		
		listaNrInvoices = new ArrayList<String>();
		
	}
	
	/**
	 * Metodo para exportar arquivo.
	 */
	public void exportSap() {
		
		//TODO: Before Export, must be done the data validation. It must be mandatory.
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) facesContext
				.getExternalContext().getResponse();

		ServletOutputStream output;
		try {
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(out);
			BufferedWriter bw = new BufferedWriter(osw);
			
			this.escreverPrimeiroBlocoArquivo( bw );
			this.escreverSegundoBlocoArquivo( bw );
			this.escreverTerceiroBlocoArquivo( bw );
			this.escreverQuartoBlocoArquivo( bw );
			
			bw.close();
			
			output = response.getOutputStream();
			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ processo.getProcBroker().getNrDI() + ".txt");
			
			byte[] bytes = out.toByteArray();
			output.write(bytes);
			
			output.flush();
			output.close();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Neste metodo deve ser colocado toda as informações referentes ao REGISTO '03' do layout do SAP.
	 * @param bw 
	 * @throws IOException 
	 */
	private void escreverQuartoBlocoArquivo(BufferedWriter bw) throws IOException {
		
		String linha = "";
		
		for (SapItemDIFatura fatura : di.getItensFatura()) {
			
			bw.newLine();
			linha = "03";
			//DI
			linha = this.escreveStringLinha( linha, fatura.getNrDI(), 10 );
			//numero po
			linha = this.escreveStringLinha( linha, fatura.getNumeroPO(), 10 );
			//item po
			linha = this.escreveStringLinha( linha, fatura.getItemPO(), 5, true );
			//tipo fatura
			linha = this.escreveStringLinha( linha, fatura.getCusto().getCustoImportacao().getTipoFatura(), 1 );
			//codigo fatura
			linha = this.escreveStringLinha( linha, fatura.getCusto().getCustoImportacao().getCodigoFatura(), 10 );
			
			//CODIGO FORNECEDOR DO CUSTO
			linha = this.escreveStringLinha( linha, fatura.getCodigoFornecedor(), 10, true );
			
			//referencia documento
			linha = this.escreveStringLinha( linha, fatura.getReferenciaDocumento(), 16 );
			//tipo transacao
			linha = this.escreveStringLinha( linha, fatura.getTransacao(), 1 );
			
			//data lancamento
			linha = this.escreveDataLinha( linha, fatura.getDtLancamento(), "yyyyMMdd" );
			//data documento
			/**
			 * Data da DI
			 */
			linha = this.escreveDataLinha( linha, fatura.getDtDocumento(), "yyyyMMdd" );
			//SIGLA MOEDA
			linha = this.escreveStringLinha( linha, fatura.getMoeda().getSigla(), 5 );
			//texto lancamento
			linha = this.escreveStringLinha( linha, fatura.getTextoLancamento(), 60 );
			//data base vencimento
			/**
			 * Data da invoice
			 */
			linha = this.escreveDataLinha( linha, fatura.getDtVencimento(), "yyyyMMdd" );
			//condicao pagamento
			/**
			 * Por padrao AS00, mas poderá ser alterado
			 */
			linha = this.escreveStringLinha( linha, fatura.getCondicaoPagamento(), 4 );
			//metodo pagamento
			linha = this.escreveStringLinha( linha, fatura.getMetodoPagamento(), 1 );
			//bloqueio pagamento
			linha = this.escreveStringLinha( linha, fatura.getBloqueioPagamento(), 1 );
			//texto cab. documento
			linha = this.escreveStringLinha( linha, fatura.getTextoCabecalho(), 25 );
			//chave alocacao
			linha = this.escreveStringLinha( linha, fatura.getChaveAlocacao(), 18 );
			//taxa cambio
			linha = escreveDecimalLinha( linha, fatura.getTaxaCambial(), 9, 5, true );
			//Quantidade
			/**
			 * TODO
			 * Verifica com JEAN,
			 * pois se o custo nao for FOB a quantidade é outra.
			 */
			linha = escreveDecimalLinha( linha, fatura.getQuantidade(), 13, 3, true );
			//Valor unitario
			/**
			 * TODO
			 * Verificar com Jean
			 * Detalhes desse valor unitario, pois nao consegui chegar ao valor
			 * com os calculos anotados
			 */
			linha = escreveDecimalLinha( linha, fatura.getVlUnitario(), 9, 5, true );
			//sem cobertura cambial
			linha = escreveStringLinha(linha, fatura.getSemCobertura(), 1);
			//despesa complementar
			linha = escreveStringLinha(linha, fatura.getDespesaComplementar(), 1);
			
			//taxlaw4
			linha = escreveStringLinha(linha, fatura.getTaxlaw4(), 3);
			//taxlaw5
			linha = escreveStringLinha(linha, fatura.getTaxlaw5(), 3);

			bw.write( linha );
		}
	}

	/**
	 * Metodo retorna os numeros das invoices da DI separados por '|'
	 * @return
	 */
	private String getNumeroInvoices() {
		String nrInvoices = "";
		if ( listaNrInvoices != null && !listaNrInvoices.isEmpty() ) {
			for (int i = 0; i < listaNrInvoices.size(); i++) {
				
				if ( nrInvoices.length() > 1 ) {
					nrInvoices += " | ";
				}
				nrInvoices += listaNrInvoices.get(i);
			}
		}
		return nrInvoices;
	}

	/**
	 * Neste metodo deve ser colocado toda as informações referentes ao REGISTO '02' do layout do SAP.
	 * @param bw 
	 * @throws IOException 
	 */
	private void escreverTerceiroBlocoArquivo(BufferedWriter bw) throws IOException {

		String linha = "";
		for (SapItemDITaxa sapItemDITaxa : di.getItensTaxa()) {
			bw.newLine();
			linha = "02";
			//DI
			linha = this.escreveStringLinha( linha, sapItemDITaxa.getNrDI(), 10 );
			//numero po
			linha = this.escreveStringLinha( linha, sapItemDITaxa.getNumeroPO(), 10 );
			//item po
			linha = this.escreveStringLinha( linha, sapItemDITaxa.getItemPO().toString(), 5, true );
			//tipo imposto
			linha = this.escreveStringLinha( linha, sapItemDITaxa.getTipoImposto(), 4 );
			//base normal
			linha = this.escreveDecimalLinha( linha, sapItemDITaxa.getBaseNormal(), 15, 2, true );
			//base excluida
			linha = this.escreveDecimalLinha( linha, sapItemDITaxa.getBaseExcluida(), 15, 2, true );
			//base outras
			linha = this.escreveDecimalLinha( linha, sapItemDITaxa.getBaseOutras(), 15, 2, true );
			//aliquota
			linha = this.escreveDecimalLinha( linha, sapItemDITaxa.getAliquota(), 6, 2, true );
			//imposto
			linha = this.escreveDecimalLinha( linha, sapItemDITaxa.getValorImposto(), 15, 2, true );
			//lancar imposto
			linha = this.escreveStringLinha( linha, sapItemDITaxa.getLancarImposto(), 1 );
			//conta contabil
			linha = this.escreveStringLinha( linha, sapItemDITaxa.getContaContabil(), 10 );
			
			bw.write( linha );
		}
	}

	/**
	 * Neste metodo deve ser colocado toda as informações referentes ao REGISTO '01' do layout do SAP.
	 * @param bw 
	 * @throws IOException 
	 */
	private void escreverSegundoBlocoArquivo(BufferedWriter bw) throws IOException {
		
		for (SapItemDI sapItemDI : di.getItensDis()) {
			
			bw.newLine();
			String linha = "01";
			linha = this.escreveStringLinha( linha, processo.getProcBroker().getNrDI(), 10 );
			linha = this.escreveStringLinha( linha, sapItemDI.getNumeroPO(), 10 );
			linha = this.escreveStringLinha( linha, sapItemDI.getItemPO().toString(), 5, true );
			linha = this.escreveStringLinha( linha, sapItemDI.getAdicaoDI(), 3, true );
			//ITEM ADICAO
			linha = this.escreveStringLinha( linha, sapItemDI.getAdicaoDI(), 2, true );
			linha = this.escreveDecimalLinha( linha, sapItemDI.getQuantidade(), 13, 3, true );
			linha = this.escreveDecimalLinha( linha, sapItemDI.getPrecoUnitario(), 16, 6, true );
			
			//Desconto unitario
			linha = this.escreveDecimalLinha( linha, BigDecimal.ZERO, 15, 2, true );
			//Frete unitario
			linha = this.escreveDecimalLinha( linha, 
					sapItemDI.getPercentual().multiply(valorCollectFee).setScale( 2, RoundingMode.FLOOR ), 15, 2, true );
			//Seguro unitario
			linha = this.escreveDecimalLinha( linha, BigDecimal.ZERO, 15, 2, true );
			//Outras despesas
			linha = this.escreveDecimalLinha( linha, 
					sapItemDI.getPercentual().multiply( totalDespesas ).setScale( 2, RoundingMode.FLOOR), 15, 2, true );
			//CFOP
			linha = this.escreveStringLinha( linha, sapItemDI.getCfop(), 6 );
			//NCM
			linha = this.escreveStringLinha( linha, sapItemDI.getNcm(), 16 );
			//LEGAL ICMS
			linha = this.escreveStringLinha( linha, sapItemDI.getTextoIcms(), 3 );
			//LEGAL IPI
			linha = this.escreveStringLinha( linha, sapItemDI.getTextoIpi(), 3 );
			//ORIGEM MATERIAL
			linha = this.escreveStringLinha( linha, "1", 1 );
			//Uso material
			linha = this.escreveStringLinha( linha, sapItemDI.getUsoMaterial(), 1 );
			//TAXLW4
			linha = this.escreveStringLinha( linha, sapItemDI.getTaxLW4(), 3 );
			//TAXLW5
			linha = this.escreveStringLinha( linha, sapItemDI.getTaxLW5(), 3 );
			//ADICAO DI
			linha = this.escreveStringLinha( linha, sapItemDI.getAdicaoDI(), 3, true );
			//SEQUENCIAL DI
			linha = this.escreveStringLinha( linha, sapItemDI.getSequencialDI(), 2, true );
			//FABRICANTE
			linha = this.escreveStringLinha( linha, sapItemDI.getFabricante(), 60 );
			
			bw.write( linha );
		}
		
	}
	
	/**
	 * Com base no processo monta as informacoes em um mapa, para facilitar o acesso aos dados.
	 * @throws BusinessException 
	 */
	private void prepararInformacoes() throws BusinessException {
		ProcBroker broker = importacaoFacade.getProcBrokerById( processo.getProcBroker().getId(), true );
		
		//A CHAVE DO MAPA SERÁ O NUMERO PO + ITEM PO
		itensPOBroker = new HashMap<String, ProcItemPo>();
		String keyMap = "";
		
		for (ProcItem itemBroker : broker.getItens()) {
			for (ProcItemPo itemPoBroker : itemBroker.getItensPo()) {
				keyMap = itemPoBroker.getNrPo() + itemPoBroker.getItemPo();
				if ( !itensPOBroker.containsKey( keyMap ) ) {
					itensPOBroker.put(keyMap, itemPoBroker);
				}
			}
		}
		
		//MAPA DE ITENS INVOICE, AGRUPAR OS ITENS POR NUMERO PO
		itensInvoice = new HashMap<String, List<ItemInvoice>>();
		keyMap = "";
		List<ItemInvoice> itens;
		
		for (Invoice inv : processo.getListaDeInvoices()) {

			//LISTA DE INVOICES DA DI
			if ( listaNrInvoices.isEmpty() ) {
				listaNrInvoices.add( inv.getNumeroInvoice() );
			} else if ( !listaNrInvoices.contains( inv.getNumeroInvoice() ) ){
				listaNrInvoices.add( inv.getNumeroInvoice() );
			}
			
			for (ItemInvoice item : inv.getItensInvoice()) {
				
				totalItensInvoice = totalItensInvoice.add( item.getTotal() );
				
				//CHAVE DO MAPA: NUMERO PO + ITEM PO
				keyMap = item.getItemPO().getPo().getNumeroPO() + item.getItemPO().getNumeroItem();
				if( !itensInvoice.containsKey(keyMap) ) {
					itens = new ArrayList<ItemInvoice>();
					itens.add(item);
					itensInvoice.put(keyMap, itens);
					
				} else {
					itens = itensInvoice.get(keyMap);
					itens.add(item);
					
				}
			}
		}
		
		//preenche valores de despesas, PIS e CONFINS
		pisConfins = false;
		totalDespesas = BigDecimal.ZERO;
		valorCollectFee = BigDecimal.ZERO;
		valorFretes = BigDecimal.ZERO;
		
		for (CustoDI custo : custosDI) {
			
			if ( custo.getCustoImportacao().getDescricao().equalsIgnoreCase("PIS") 
					|| custo.getCustoImportacao().getDescricao().equalsIgnoreCase("COFINS")) {
				pisConfins = true;
			}
			if ( custo.getCustoImportacao().getTipoFatura().equalsIgnoreCase("I") ) {
				valorFobBrl = custo.getValorReal();
				valorFob = custo.getValorCusto();
			}
			if ( custo.getCustoImportacao().getTipoFatura().equalsIgnoreCase("F") ) {
				valorFretes = valorCollectFee.add( custo.getValorReal() );
				
				if ( custo.getCustoImportacao().getDescricao().equalsIgnoreCase("Collect Fee") ) {
					valorCollectFee = custo.getValorReal();
				}
			}
			if ( custo.getCustoImportacao().getTipoFatura().equalsIgnoreCase("D") 
					|| custo.getCustoImportacao().getTipoFatura().equalsIgnoreCase("S")) {
				totalDespesas = totalDespesas.add( custo.getValorReal() );
			}
		}
		
	}

	/**
	 * Neste metodo deve ser colocado toda as informações referentes ao REGISTO '00' do layout do SAP.
	 * @param bw 
	 * @throws IOException 
	 */
	private void escreverPrimeiroBlocoArquivo(BufferedWriter bw) throws IOException {
		String linha = "00";
		
		linha = this.escreveStringLinha( linha, di.getNrDI(), 10 );
		linha = this.escreveStringLinha( linha, di.getCompanyCode(), 4 );
		linha = this.escreveStringLinha( linha, di.getBranchFilial(), 4, true );
		linha = this.escreveDataLinha( linha, di.getDtDesembaracoDI(), "yyyyMMdd" );
		linha = this.escreveDataLinha( linha, di.getDtDesembaracoDI(), "yyyyMMdd" );
		linha = this.escreveStringLinha( linha, di.getIncoterms(), 3 );
		linha = this.escreveStringLinha( linha, di.getAwb(), 15 );
		linha = this.escreveDecimalLinha( linha, di.getPesoLiquido(), 15, 3, true );
		linha = this.escreveDecimalLinha( linha, di.getPesoBruto(), 15, 3, true );
		linha = this.escreveStringLinha( linha, di.getUnidadeMedida(), 3 );
		
		if ( di.getVolume() != null ) {
			linha = this.escreveStringLinha( linha, di.getVolume().getIdSap(), 4);
			linha = this.escreveStringLinha( linha, di.getVolume().getDescricao(), 20);
		} else {
			linha = this.escreveStringLinha( linha, null, 4);
			linha = this.escreveStringLinha( linha, null, 20);
		}
		
		linha = this.escreveDecimalLinha( linha, di.getQtdPacotes(), 5, 0, true );
		linha = this.escreveStringLinha( linha, di.getTipoNotaFiscal(), 2 );
		linha = this.escreveStringLinha( linha, di.getAgenteCargas().getVendorSap().toString(), 10, true );
		
		String categoriaVolume = "";
		if ( di.getVolume().getId()  != null ) {
			categoriaVolume = di.getVolume().getShpUnt();
		}
		linha = escreveStringLinha(linha, categoriaVolume, 3);
		
		linha = escreveStringLinha(linha, di.getUfDespachante(), 2);
		linha = escreveStringLinha(linha, di.getLocalDespachante(), 60);
		
		bw.write( linha );
	}

	/**
	 * Escreve na linha os decimais conforme os parametros.
	 * @param linha
	 * @param pesoLiqTotal
	 * @param tamanho
	 * @param escala
	 * @param preencherComZero
	 * @return
	 */
	private String escreveDecimalLinha(String linha, BigDecimal valor, int tamanho, int escala, boolean preencherComZero) {
		
		if ( valor != null && escala > 0 ) {
			valor = valor.setScale( escala );
			linha = escreveStringLinha(linha, valor.toString(), tamanho, preencherComZero);
			
		} else {
			linha = escreveStringLinha(linha, "", tamanho, preencherComZero );
		}
		
		return linha;
	}

	/**
	 * Escrever uma data na linha conforme o padrao descrito.
	 * @param linha
	 * @param data
	 * @param padraoData
	 * @return
	 */
	private String escreveDataLinha(String linha, Date data, String padraoData) {
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(padraoData);
			sdf.format(data);
			
			linha += sdf.format(data);
			
			return linha;
			
		} catch (NullPointerException e) {
			
			return escreveStringLinha(linha, "", 8);
		}
	}

	/**
	 * @param texto
	 * @param i
	 * @return
	 */
	private String escreveStringLinha(String linha, String texto, int i) {
		return escreveStringLinha(linha, texto, i, false);
	}

	/**
	 * Metodo para escrever uma string de um texto na linha correspondente, preencher com zero ou com espaços caso necessário.
	 * 
	 * @param texto
	 * @param tamanho
	 * @param preencheComZero
	 * @return
	 */
	private String escreveStringLinha(String linha, String texto, int tamanho, boolean preencheComZero) {
		if ( texto == null )
			texto = "";
		
		if ( texto.length() < tamanho ) {
			int diff = texto.length();
			while ( diff < tamanho ) {
				if ( preencheComZero )
					texto = "0" + texto;
				else
					texto += " ";
				diff++;
			}
			linha += texto.toUpperCase();
			return linha;
		}
		
		linha += texto.substring(0, tamanho).toUpperCase();
		return linha;
	}

	@Override
	public String editar() {
		
		try {
			processo = importacaoFacade.getCargaById( processo.getId() );
			
			//preenche a lista de despesas e fretes.
			custosDI = importacaoFacade.listarCustos( processo, null );
			depesas = importacaoFacade.listarCustos( processo, "D" );
			fretes = importacaoFacade.listarCustos( processo, "F" );
			
			/*
			piPIS = cadastroFacade.getParametroImposto( "PIS" );
			if ( piPIS == null ) 
				piPIS = new ParametroImposto();
			
			piConfins = cadastroFacade.getParametroImposto( "CONFINS" );
			if ( piConfins == null ) 
				piConfins = new ParametroImposto();
			*/
			preencherValores();
			
		} catch (BusinessException e) {
			
			logger.error("erro: {}", e);
			e.printStackTrace();
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.EXP_SAP, e));
			return null;
		}
		
		edicao = true;
		
		return "processo.jsf";
		
	}

	/**
	 * Preencher valores para exibicao em tela.
	 */
	private void preencherValores() {
		
		//METODO PREPARA AS INFORMACOES
		try {
			this.prepararInformacoes();
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		
		preencherPrimeiroBloco();
		preencherSegundoBloco();
		preencherTerceiroBloco();
		preencherQuartoBloco();
	}

	/**
	 * Preencher em memoria para exibir na tela.
	 */
	private void preencherPrimeiroBloco() {
		
		di = new SapDI();
		di.setItensDis( new ArrayList<SapItemDI>() );
		di.setItensTaxa( new ArrayList<SapItemDITaxa>() );
		di.setItensFatura( new ArrayList<SapItemDIFatura>() );
		
		di.setNrDI( processo.getProcBroker().getNrDI() );
		di.setCompanyCode( "bzfm" );
		di.setBranchFilial( processo.getFilial().getIdSap() );
		di.setDtRegistroDI( processo.getProcBroker().getDataDI() );
		di.setDtDesembaracoDI( processo.getProcBroker().getDtParametrizacao() );
		di.setIncoterms( processo.getProcBroker().getIncoterm().getSigla() );
		di.setAwb( processo.getProcBroker().getHawb() );
		di.setPesoLiquido( processo.getProcBroker().getPesoLiqTotal() );
 		di.setPesoBruto( processo.getProcBroker().getPesoBrutoTotal() );
		di.setUnidadeMedida( "KG" );
		
		try {
			TipoPacote volume = new TipoPacote();
			volume = cadastroFacade.getTipoPacoteByCodigo(escreveStringLinha(
					"", processo.getProcBroker().getTipoVolume().toString(), 3,
					true));

			di.setVolume(volume);
		} catch (BusinessException e) {
		}
		
		di.setQtdPacotes( processo.getProcBroker().getQtdVolume() );
		di.setTipoNotaFiscal( "B1" );
		di.setAgenteCargas( processo.getAgenteCarga() );
		di.setUfDespachante( "" );
		di.setLocalDespachante( processo.getProcBroker().getRecinto() );
		
	}

	/**
	 * Preencher em memoria para exibir na tela
	 */
	private void preencherSegundoBloco() {
		SapItemDI sapItemDI;
		
		for (String chave : itensInvoice.keySet()) {
			//preenche conforme os dados do item po e po do broker.
			if ( itensPOBroker.containsKey(chave) ) {
			
				sapItemDI = new SapItemDI();
				sapItemDI.setNrDI( processo.getProcBroker().getNrDI() );
				sapItemDI.setNumeroPO( itensInvoice.get(chave).get( 0 ).getItemPO().getPo().getNumeroPO() );
				sapItemDI.setItemPO( itensInvoice.get(chave).get( 0 ).getItemPO().getNumeroItem() );
				
				sapItemDI.setPrecoUnitario( itensInvoice.get(chave).get( 0 ).getItemPO().getPrecoUnitario() );
				sapItemDI.setQuantidade(BigDecimal.ZERO);
				for ( ItemInvoice itemInv : itensInvoice.get(chave) ) {
					sapItemDI.setQuantidade( sapItemDI.getQuantidade().add( BigDecimal.valueOf( itemInv.getQuantidade() ) ) );
				}
				
				//setar o percentual de rateamento para o itemDI: VL_TOTAL_ITEM / VL_TOTAL_ITENS_INVOICE
				sapItemDI.setPercentual( sapItemDI.getTotal().divide(totalItensInvoice, 5, RoundingMode.FLOOR) );
			
				if ( itensPOBroker.get(chave).getNrItem() != null ) {
					sapItemDI.setSequencialDI( itensPOBroker.get(chave).getNrItem().toString() );
				}
				
				if ( itensPOBroker.get(chave).getProcItem() != null ) {
					if ( itensPOBroker.get(chave).getProcItem().getNrAdicao() != null ) { 
						sapItemDI.setAdicaoDI( itensPOBroker.get(chave).getProcItem().getNrAdicao().toString() );
					}
					sapItemDI.setNcm( itensPOBroker.get(chave).getProcItem().getNcm() );
				}
				
				sapItemDI.setFabricante( itensPOBroker.get(chave).getNomeFabricante() );
				
				if ( itensPOBroker.get(chave).getProcItem().getAplicacao() != null )
					sapItemDI.setUsoMaterial( itensPOBroker.get(chave).getProcItem().getAplicacao().toString() );
				
				if ( itensPOBroker.get(chave).getProcItem().getAplicacao() == INDUSTRIALIZACAO ) {
					sapItemDI.setCfop("3101AA");
					sapItemDI.setTextoIcms("ZCO");
					sapItemDI.setTextoIpi("IP3");
					sapItemDI.setTaxLW4("C12");
					sapItemDI.setTaxLW5("PB2");
				}
				if ( itensPOBroker.get(chave).getProcItem().getAplicacao() == USO_PROPRIO ) {
					sapItemDI.setCfop("3556AA");
					sapItemDI.setTextoIcms("ICP");
					sapItemDI.setTextoIpi("IP3");

					if (pisConfins) {
						sapItemDI.setTaxLW4("CC9");
						sapItemDI.setTaxLW5("PC3");
					} else {
						sapItemDI.setTaxLW4("C10");
						sapItemDI.setTaxLW5("P05");
					}
				}
				if ( itensPOBroker.get(chave).getProcItem().getAplicacao() == OUTRAS ) {
					sapItemDI.setCfop("3551AA");
					sapItemDI.setTextoIcms("ICG");
					sapItemDI.setTextoIpi("ZPR");
					
					if (pisConfins) {
						sapItemDI.setTaxLW4("CD3");
						sapItemDI.setTaxLW5("PC5");
					} else {
						sapItemDI.setTaxLW4("C13");
						sapItemDI.setTaxLW5("PB3");
					}
				}
				di.getItensDis().add( sapItemDI );
			}
		}
	}

	/**
	 * Preenche o objeto para exibir na tela.
	 */
	private void preencherTerceiroBloco() {
		
		BigDecimal baseOutras = BigDecimal.ZERO;
		
		SapItemDITaxa sapItemDITaxa;
		
		for (String chave : itensInvoice.keySet()) {
			if ( itensPOBroker.containsKey(chave) ) {
				
				BigDecimal valorRateio = new BigDecimal( 0 );
				BigDecimal valorItens = BigDecimal.ZERO;
				
				for (ItemInvoice item : itensInvoice.get(chave)) {
					valorItens = valorItens.add( item.getPrecoUnit().multiply( new BigDecimal( item.getQuantidade() ) ) );
				}
				
				valorRateio = valorItens.multiply( new BigDecimal( 100 ) ).divide( valorFob, 10, RoundingMode.FLOOR ).divide( new BigDecimal( 100 ), 10, RoundingMode.FLOOR);
				//calcular base outras
				if ( itensPOBroker.get(chave).getProcItem().getAplicacao() != USO_PROPRIO ) {
					baseOutras = valorFretes.add( valorFobBrl ).add( totalDespesas ).multiply( valorRateio );
				}
				
				//ICM3
				sapItemDITaxa = new SapItemDITaxa();
				sapItemDITaxa.setNrDI( processo.getProcBroker().getNrDI() );
				sapItemDITaxa.setNumeroPO( itensInvoice.get(chave).get( 0 ).getItemPO().getPo().getNumeroPO() );
				sapItemDITaxa.setItemPO( itensInvoice.get(chave).get( 0 ).getItemPO().getNumeroItem().toString() );
				sapItemDITaxa.setTipoImposto( "ICM3" );
				sapItemDITaxa.setBaseNormal( BigDecimal.ZERO );
				sapItemDITaxa.setBaseExcluida( BigDecimal.ZERO );
				sapItemDITaxa.setBaseOutras( baseOutras.setScale( 2, RoundingMode.HALF_UP) );
				sapItemDITaxa.setAliquota( BigDecimal.ZERO );
				sapItemDITaxa.setValorImposto( BigDecimal.ZERO );        
				sapItemDITaxa.setLancarImposto( "N" );
				
				di.getItensTaxa().add(sapItemDITaxa);
				
				//IPI3
				sapItemDITaxa = new SapItemDITaxa();
				sapItemDITaxa.setNrDI( processo.getProcBroker().getNrDI() );
				sapItemDITaxa.setNumeroPO( itensInvoice.get(chave).get( 0 ).getItemPO().getPo().getNumeroPO() );
				sapItemDITaxa.setItemPO( itensInvoice.get(chave).get( 0 ).getItemPO().getNumeroItem().toString() );
				sapItemDITaxa.setTipoImposto( "IPI3" );
				sapItemDITaxa.setBaseNormal( BigDecimal.ZERO );
				sapItemDITaxa.setBaseExcluida( BigDecimal.ZERO );
				sapItemDITaxa.setBaseOutras( baseOutras.setScale( 2, RoundingMode.HALF_UP) );
				sapItemDITaxa.setAliquota( BigDecimal.ZERO );
				sapItemDITaxa.setValorImposto( BigDecimal.ZERO );
				sapItemDITaxa.setLancarImposto( "N" );
				
				di.getItensTaxa().add(sapItemDITaxa);
				
				if ( itensPOBroker.get(chave).getProcItem().getAplicacao() == INDUSTRIALIZACAO 
						|| itensPOBroker.get(chave).getProcItem().getAplicacao() == OUTRAS ) {
					if ( pisConfins ) {
						//ICON
						sapItemDITaxa = new SapItemDITaxa();
						sapItemDITaxa.setNrDI( processo.getProcBroker().getNrDI() );
						sapItemDITaxa.setNumeroPO( itensInvoice.get(chave).get( 0 ).getItemPO().getPo().getNumeroPO() );
						sapItemDITaxa.setItemPO( itensInvoice.get(chave).get( 0 ).getItemPO().getNumeroItem().toString() );
						sapItemDITaxa.setTipoImposto( "ICON" );
						sapItemDITaxa.setBaseNormal( BigDecimal.ZERO );
						sapItemDITaxa.setBaseExcluida( BigDecimal.ZERO );
						sapItemDITaxa.setBaseOutras( baseOutras.setScale( 2, RoundingMode.HALF_UP) );
						sapItemDITaxa.setAliquota( BigDecimal.ZERO );
						sapItemDITaxa.setValorImposto( BigDecimal.ZERO );
						sapItemDITaxa.setLancarImposto( "N" );
						
						di.getItensTaxa().add( sapItemDITaxa );
						
						//IPSN
						sapItemDITaxa = new SapItemDITaxa();
						sapItemDITaxa.setNrDI( processo.getProcBroker().getNrDI() );
						sapItemDITaxa.setNumeroPO( itensInvoice.get(chave).get( 0 ).getItemPO().getPo().getNumeroPO() );
						sapItemDITaxa.setItemPO( itensInvoice.get(chave).get( 0 ).getItemPO().getNumeroItem().toString() );
						sapItemDITaxa.setTipoImposto( "IPSN" );
						sapItemDITaxa.setBaseNormal( BigDecimal.ZERO );
						sapItemDITaxa.setBaseExcluida( BigDecimal.ZERO );
						sapItemDITaxa.setBaseOutras( baseOutras.setScale( 2, RoundingMode.HALF_UP) );
						sapItemDITaxa.setAliquota( BigDecimal.ZERO );
						sapItemDITaxa.setValorImposto( BigDecimal.ZERO );
						sapItemDITaxa.setLancarImposto( "N" );
						
						di.getItensTaxa().add( sapItemDITaxa );
						
					}
				} else if ( itensPOBroker.get(chave).getProcItem().getAplicacao() == USO_PROPRIO ) {
					
					if ( pisConfins ) {
						baseOutras = BigDecimal.ZERO;
					} else {
						baseOutras = valorItens;
					}
					
					//ICON
					sapItemDITaxa = new SapItemDITaxa();
					sapItemDITaxa.setNrDI( processo.getProcBroker().getNrDI() );
					sapItemDITaxa.setNumeroPO( itensInvoice.get(chave).get( 0 ).getItemPO().getPo().getNumeroPO() );
					sapItemDITaxa.setItemPO( itensInvoice.get(chave).get( 0 ).getItemPO().getNumeroItem().toString() );
					sapItemDITaxa.setTipoImposto( "ICON" );
					sapItemDITaxa.setBaseNormal( totalItensInvoice.setScale( 2, RoundingMode.HALF_UP) );
					sapItemDITaxa.setBaseExcluida( BigDecimal.ZERO );
					sapItemDITaxa.setBaseOutras( baseOutras.setScale( 2, RoundingMode.HALF_UP) );
					
					sapItemDITaxa.setAliquota( new BigDecimal( "7.60" ) );
					
					BigDecimal valorImposto = sapItemDITaxa.getAliquota().divide( new BigDecimal( 100 ), 3, RoundingMode.FLOOR ).add(BigDecimal.ONE);
					valorImposto = valorImposto.multiply(totalItensInvoice).setScale( 2, RoundingMode.HALF_UP);
					sapItemDITaxa.setValorImposto( valorImposto );
					
					sapItemDITaxa.setLancarImposto( "Y" );
					sapItemDITaxa.setContaContabil( "131210" );
					
					di.getItensTaxa().add( sapItemDITaxa );
					
					//IPSN
					sapItemDITaxa = new SapItemDITaxa();
					sapItemDITaxa.setNrDI( processo.getProcBroker().getNrDI() );
					sapItemDITaxa.setNumeroPO( itensInvoice.get(chave).get( 0 ).getItemPO().getPo().getNumeroPO() );
					sapItemDITaxa.setItemPO( itensInvoice.get(chave).get( 0 ).getItemPO().getNumeroItem().toString() );
					sapItemDITaxa.setTipoImposto( "IPSN" );
					sapItemDITaxa.setBaseNormal( totalItensInvoice.setScale( 2, RoundingMode.HALF_UP) );
					sapItemDITaxa.setBaseExcluida( BigDecimal.ZERO );
					sapItemDITaxa.setBaseOutras( baseOutras.setScale( 2, RoundingMode.HALF_UP) );
					
					sapItemDITaxa.setAliquota( new BigDecimal( "1.65" ) );
					valorImposto = sapItemDITaxa.getAliquota().divide( new BigDecimal( 100 ), 3, RoundingMode.FLOOR ).add(BigDecimal.ONE);
					valorImposto = valorImposto.multiply(totalItensInvoice).setScale( 2, RoundingMode.HALF_UP);
					sapItemDITaxa.setValorImposto( valorImposto );
					
					sapItemDITaxa.setLancarImposto( "Y" );
					sapItemDITaxa.setContaContabil( "131209" );
					
					di.getItensTaxa().add( sapItemDITaxa );
					
				}
			}
		}
		
	}

	/**
	 * Preenche para exibir na tela.
	 */
	private void preencherQuartoBloco() {
		
		SapItemDIFatura fatura;
		
		for (String chave : itensInvoice.keySet()) {
			if ( itensPOBroker.containsKey(chave) ) {
				
				BigDecimal valorRateio = new BigDecimal( 0 );
				BigDecimal valorItens = BigDecimal.ZERO;
				Integer qtdItens = 0;
				for ( ItemInvoice itemInv : itensInvoice.get(chave) ) {
					qtdItens += itemInv.getQuantidade();
					valorItens = valorItens.add( itemInv.getPrecoUnit().multiply( new BigDecimal( itemInv.getQuantidade() ) ) );
				}
				valorRateio = valorItens.multiply( new BigDecimal( 100 ) ).divide( 
						valorFob, 10, RoundingMode.FLOOR ).divide( new BigDecimal( 100 ), 10, RoundingMode.FLOOR);
				
				for (CustoDI custo : custosDI) {
					
					fatura = new SapItemDIFatura();
					fatura.setNrDI( processo.getProcBroker().getNrDI() );
					fatura.setNumeroPO( itensInvoice.get(chave).get( 0 ).getItemPO().getPo().getNumeroPO() );
					fatura.setItemPO( itensInvoice.get(chave).get( 0 ).getItemPO().getNumeroItem().toString() );
					fatura.setCusto(custo);
					
					if (custo.getFornecedor() != null) 
						fatura.setCodigoFornecedor( custo.getFornecedor().getVendorSap().toString() );
					
					fatura.setReferenciaDocumento( getNumeroInvoices() );

					//tipo transacao e QUANTIDADE
					if ( custo.getCustoImportacao().getCodigoFatura().equals("FOB") ) {
						fatura.setTransacao( "1" );
						fatura.setQuantidade( new BigDecimal(qtdItens ) );
					}
					else {
						fatura.setTransacao( "3" );
						fatura.setQuantidade( new BigDecimal( 1 ) );
					}
					
					fatura.setDtLancamento( null );
					//data documento
					/**
					 * Data da DI
					 */
					fatura.setDtDocumento( processo.getProcBroker().getDataDI() );
					//SIGLA MOEDA
					fatura.setMoeda( custo.getMoeda() );
					
					//texto lancamento
					fatura.setTextoLancamento( getNumeroInvoices() );

					//data base vencimento
					/**
					 * TODO
					 * Confirmar com Jean, pois foi dito que essa eh a data da invoice
					 * porém a data da invoice, pois não estao batendo os arquivos.
					 */
					fatura.setDtVencimento( itensInvoice.get( chave ).get( 0 ).getInvoice().getDtEmissao() );
					
					//condicao pagamento
					/**
					 * TODO
					 * Confirma, pois não esta batendo, conforme conversa com JEAN
					 * foi dito que esse campo é a condição da invoice, porém para
					 * cada custo tem uma condicao de pagamento diferente.
					 */
					fatura.setCondicaoPagamento( "AS00" );
					//metodo pagamento
					fatura.setMetodoPagamento( "U" );
					//bloqueio pagamento
					fatura.setBloqueioPagamento( "A" );
					//texto cab. documento
					fatura.setTextoCabecalho( processo.getProcBroker().getNrDI() );
					//chave alocacao
					fatura.setChaveAlocacao( processo.getProcBroker().getNrDI() );
					
					
					//Valor unitario
					/**
					 * TODO
					 * Verificar com Jean
					 * Detalhes desse valor unitario, pois nao consegui chegar ao valor
					 * com os calculos anotados
					 */
					BigDecimal valorUnitario = custo.getValorCusto().multiply( valorRateio );
					BigDecimal taxaCambio = BigDecimal.ZERO;
					
					if ( custo.getMoeda().getSigla().equalsIgnoreCase("BRL") ) {
						valorUnitario = custo.getValorCusto().multiply( valorRateio );
					} else {
						
						taxaCambio = custo.getTaxaCambio(); 
						if ( !custo.getCustoImportacao().getTipoFatura().equalsIgnoreCase( "F" ) ) {
							valorUnitario = custo.getValorCusto().multiply( valorRateio ).multiply( custo.getValorReal() );
						}
					}
					
					if ( custo.getCustoImportacao().getTipoFatura().equals( "I" ) ) {
						taxaCambio = custo.getTaxaCambio(); 
						if ( custo.getMoeda().getSigla().equals("JPY") ) {
							valorUnitario = valorItens;
						} else {
							valorUnitario = custo.getValorCusto().multiply( valorRateio );
						}
					}
					fatura.setVlUnitario( valorUnitario.setScale( 2, RoundingMode.HALF_UP) );
					
					//taxa cambio
					fatura.setTaxaCambial( taxaCambio.setScale( 5, RoundingMode.HALF_UP) );
					
					//sem cobertura cambial
					fatura.setSemCobertura( "N" );
					//despesa complementar
					fatura.setDespesaComplementar( "N" );
					
					if ( itensPOBroker.get(chave).getProcItem().getAplicacao() == INDUSTRIALIZACAO ) {
						//taxlaw4
						fatura.setTaxlaw4( "C12" );
						//taxlaw5
						fatura.setTaxlaw5( "PB2" );
					}
					
					if ( itensPOBroker.get(chave).getProcItem().getAplicacao() == USO_PROPRIO ) {
						if ( pisConfins ) {
							//taxlaw4
							fatura.setTaxlaw4( "CC9" );
							//taxlaw5
							fatura.setTaxlaw5( "PC2" );
						} else {
							//taxlaw4
							fatura.setTaxlaw4( "C10" );
							//taxlaw5
							fatura.setTaxlaw5( "P05" );
						}
					}
					if ( itensPOBroker.get(chave).getProcItem().getAplicacao() == OUTRAS ) {
						if ( pisConfins ) {
							//taxlaw4
							fatura.setTaxlaw4( "CD3" );
							//taxlaw5
							fatura.setTaxlaw5( "PC5" );
						} else {
							//taxlaw4
							fatura.setTaxlaw4( "C13" );
							//taxlaw5
							fatura.setTaxlaw5( "PB3" );
						}
					}
					//Adiciona aos itens de fatura da DI a ser exportada.
					di.getItensFatura().add( fatura );
				}
			}
		}
	}

	@Override
	public void inicializarObjetos() {
	}

	@Override
	public String excluir() {
		return null;
	}

	@Override
	protected void refazerPesquisa() {

	}

	/**
	 * @return the processo
	 */
	public Carga getProcesso() {
		return processo;
	}

	/**
	 * @param processo the processo to set
	 */
	public void setProcesso(Carga processo) {
		this.processo = processo;
	}

	/**
	 * @return the depesas
	 */
	public List<CustoDI> getDepesas() {
		return depesas;
	}

	/**
	 * @param depesas the depesas to set
	 */
	public void setDepesas(List<CustoDI> depesas) {
		this.depesas = depesas;
	}

	/**
	 * @return the fretes
	 */
	public List<CustoDI> getFretes() {
		return fretes;
	}

	/**
	 * @param fretes the fretes to set
	 */
	public void setFretes(List<CustoDI> fretes) {
		this.fretes = fretes;
	}

	/**
	 * @return the custo
	 */
	public List<CustoDI> getCustosDI() {
		return custosDI;
	}

	/**
	 * @param custo the custo to set
	 */
	public void setCustosDI(List<CustoDI> custo) {
		this.custosDI = custo;
	}

	/**
	 * @return the di
	 */
	public SapDI getDi() {
		return di;
	}

	/**
	 * @param di the di to set
	 */
	public void setDi(SapDI di) {
		this.di = di;
	}

	/**
	 * @return the sapItemDI
	 */
	public SapItemDI getSapItemDI() {
		return sapItemDI;
	}

	/**
	 * @param sapItemDI the sapItemDI to set
	 */
	public void setSapItemDI(SapItemDI sapItemDI) {
		this.sapItemDI = sapItemDI;
	}

	/**
	 * @return the sapItemDITaxa
	 */
	public SapItemDITaxa getSapItemDITaxa() {
		return sapItemDITaxa;
	}

	/**
	 * @param sapItemDITaxa the sapItemDITaxa to set
	 */
	public void setSapItemDITaxa(SapItemDITaxa sapItemDITaxa) {
		this.sapItemDITaxa = sapItemDITaxa;
	}

}
