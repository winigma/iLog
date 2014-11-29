package br.com.ilog.importacao.business.mbean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.Moeda;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;
import br.com.ilog.importacao.business.dto.Custos;
import br.com.ilog.importacao.business.dto.ItemInvoiceMapaCusto;
import br.com.ilog.importacao.business.dto.MapaCusto;
import br.com.ilog.importacao.business.entity.Carga;
import br.com.ilog.importacao.business.entity.CustoDI;
import br.com.ilog.importacao.business.entity.ItemInvoice;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroMapaCusto;
import br.com.ilog.importacao.business.facade.ImportacaoFacade;
import br.com.ilog.relatorio.util.ExportarRelatorio;
import br.com.ilog.seguranca.presentation.mbean.MBeanSessaoUsuario;

/**
 * @author Manoel Neto
 * @date 15/09/2012
 * @coment
 * 
 */
@AccessScoped
@Controller("mBeanGerarMapaCusto")
public class MBeanGerarMapaCusto implements Serializable {
	private static final long serialVersionUID = -3978658466530613670L;

	@Resource(name = "controllerImportacao")
	ImportacaoFacade facade;

	@Resource(name = "controllerCadastro")
	CadastroFacade cadastro;

	private static Logger logger = LoggerFactory
			.getLogger(MBeanGerarMapaCusto.class);

	@Resource(name = "mBeanSessaoUsuario")
	MBeanSessaoUsuario sessaoUsuario;

	/**
	 * Carga a ser editado os valores de custos.
	 */
	private Carga carga;

	private MapaCusto mapaCusto;

	private BasicFiltroMapaCusto filtroMapaCusto;

	private List<MapaCusto> list;
	
	private List<ItemInvoice> listItemInvoices;
	
	private BigDecimal totalExpenseWithoutFreight;
	
	private BigDecimal totalFreightForward;
	
	private BigDecimal totalStorage;
	
	private BigDecimal totalExpense;
	
	private BigDecimal totalValue;

	private BigDecimal importCost;
	
	private BigDecimal totalExpenseWithoutFreightUSD;
	
	private BigDecimal totalFreightForwardUSD;
	
	private BigDecimal totalStorageUSD;
	
	private BigDecimal totalExpenseUSD;
	
	private BigDecimal totalValueUSD;
	
	private Map<String, Object> parametros;

	private List<Moeda> moedas;
	
	
	

	public void calculaTaxas() {
		mapaCusto = new MapaCusto();
		Custos custos = new Custos();
		BigDecimal valorFOB = BigDecimal.ZERO.setScale( 2 );
		mapaCusto.setCustosDI(new ArrayList<Custos>());
		mapaCusto.setItemInvoice(new ArrayList<ItemInvoiceMapaCusto>());
		
		try {
			listItemInvoices=facade.listarItemInvoiceByCarga(carga);
		} catch (BusinessException e1) {
			e1.printStackTrace();
		}
		for (CustoDI custoDI : carga.getCustosDi()) {
			mapaCusto.setCarga(carga);
			custos.setMoeda(filtroMapaCusto.getMoeda());
			//mapaCusto.setMoeda();
			custos.setCustoDI(custoDI);
			//mapaCusto.setCustoDI(custoDI);
			
			
			
			try {

				if (custoDI.getValorCusto().equals(BigDecimal.ZERO.setScale(5))) {
					custos.setValorReal(new BigDecimal(0));
					//mapaCusto.setValorReal(new BigDecimal(0));
				} else {
					custos.setValorReal(custoDI.getValorCusto().multiply(
							custoDI.getTaxaCambio() ) );
//					mapaCusto.setValorReal( custoDI.getValorCusto().multiply(
//							custoDI.getTaxaCambio() ) );
				}
				
				BigDecimal subResult = BigDecimal.ONE;
				if ( !custoDI.getMoeda().getSigla().equalsIgnoreCase( "USD" ) ) {
					subResult = custoDI.getTaxaCambio().divide( filtroMapaCusto.getTxCambioUSD(), RoundingMode.HALF_UP );
				}
				custos.setValorUSD( subResult.multiply( custoDI.getValorCusto() ) );
//				mapaCusto.setValorUSD( subResult.multiply( custoDI.getValorCusto() ) );
				
				//Adiciona nos totais
//				totalValueUSD = totalValueUSD.add( mapaCusto.getValorUSD().setScale( 2, RoundingMode.HALF_UP) );
				totalValueUSD = totalValueUSD.add( custos.getValorUSD().setScale( 2, RoundingMode.HALF_UP) );
//				totalValue = totalValue.add( mapaCusto.getValorReal().setScale( 2, RoundingMode.HALF_UP) );
				totalValue = totalValue.add( custos.getValorReal().setScale( 2, RoundingMode.HALF_UP) );
				
				if ( custoDI.getCustoImportacao().getDescricao().equalsIgnoreCase( "FOB" ) ) {
//					valorFOB = mapaCusto.getValorUSD();
					valorFOB = custos.getValorUSD();
				}
				if ( custoDI.getCustoImportacao().isDespesa() ) {
					
					if ( !custoDI.getCustoImportacao().getGrupoCusto().equalsIgnoreCase("FI") ) {
//						totalExpenseWithoutFreight = totalExpenseWithoutFreight.add( mapaCusto.getValorReal().setScale( 2, RoundingMode.HALF_UP) );
						totalExpenseWithoutFreight = totalExpenseWithoutFreight.add( custos.getValorReal().setScale( 2, RoundingMode.HALF_UP) );
//						totalExpenseWithoutFreightUSD = totalExpenseWithoutFreightUSD.add( mapaCusto.getValorUSD().setScale( 2, RoundingMode.HALF_UP) );
						totalExpenseWithoutFreightUSD = totalExpenseWithoutFreightUSD.add( custos.getValorUSD().setScale( 2, RoundingMode.HALF_UP) );
						totalExpenseUSD = totalExpenseUSD.add(custos.getValorUSD().setScale( 2, RoundingMode.HALF_UP));
					}
					
					if ( custoDI.getCustoImportacao().getGrupoCusto().equalsIgnoreCase("FF") ) {
//						totalFreightForward = totalFreightForward.add( mapaCusto.getValorReal().setScale( 2, RoundingMode.HALF_UP) );
						totalFreightForward = totalFreightForward.add( custos.getValorReal().setScale( 2, RoundingMode.HALF_UP) );
//						totalFreightForwardUSD = totalFreightForwardUSD.add( mapaCusto.getValorUSD().setScale( 2, RoundingMode.HALF_UP) );
						totalFreightForwardUSD = totalFreightForwardUSD.add( custos.getValorUSD().setScale( 2, RoundingMode.HALF_UP) );
					}
					
					if ( custoDI.getCustoImportacao().getGrupoCusto().equalsIgnoreCase("S") ) {
//						totalStorage = totalStorage.add( mapaCusto.getValorReal().setScale( 2, RoundingMode.HALF_UP) );
						totalStorage = totalStorage.add( custos.getValorReal().setScale( 2, RoundingMode.HALF_UP) );
//						totalStorageUSD = totalStorageUSD.add( mapaCusto.getValorUSD().setScale( 2, RoundingMode.HALF_UP) );
						totalStorageUSD = totalStorageUSD.add( custos.getValorUSD().setScale( 2, RoundingMode.HALF_UP) );
					}
					
				}
				if(custoDI.getCustoImportacao().getDescricao().equalsIgnoreCase("freight collect")){
//					totalExpenseUSD = totalExpenseUSD.add(totalExpenseWithoutFreightUSD.setScale(2));
					totalExpenseUSD = totalExpenseUSD.add(custos.getValorUSD().setScale(2,RoundingMode.HALF_UP));
					
				}
				mapaCusto.getCustosDI().add(custos);
				
				custos = new Custos();
			} catch (ArithmeticException e) {
				e.printStackTrace();
			} catch (Exception e) {
			}
		}
		totalExpense = totalExpenseUSD.multiply(filtroMapaCusto.getTxCambio());
		list.add(mapaCusto);
		

		//Calculos valor de despesas dos item da invoice
		for(ItemInvoice itemInvoice: listItemInvoices){
			//totalExpenseUSD;
			ItemInvoiceMapaCusto invoiceMapaCusto = new ItemInvoiceMapaCusto();
			invoiceMapaCusto.setItemInvoice(itemInvoice);
			invoiceMapaCusto.setValorUSD(itemInvoice.getPrecoUnit().multiply(new BigDecimal(itemInvoice.getQuantidade())).setScale( 3, RoundingMode.HALF_UP));
			//totalValueUSD = totalValueUSD.add(invoiceMapaCusto.getValorUSD().setScale( 2, RoundingMode.HALF_UP));
			//invoiceMapaCusto.setPercentil(0);
			mapaCusto.getItemInvoice().add(invoiceMapaCusto);
			invoiceMapaCusto = new ItemInvoiceMapaCusto();
		}
		mapaCusto = new MapaCusto();
		try{
			importCost = totalValueUSD.subtract( valorFOB ).divide( valorFOB, RoundingMode.HALF_UP ).setScale( 4, RoundingMode.HALF_UP).multiply( new BigDecimal( 100 ) );			
		}catch (ArithmeticException e) {
			importCost = new BigDecimal(0);
		}
	}

	@PostConstruct
	public void inicializar() {
		try {
			filtroMapaCusto = new BasicFiltroMapaCusto();
			mapaCusto = new MapaCusto();
			list = new ArrayList<MapaCusto>();
			moedas = cadastro.listarMoedas();
			
			inicializarTotais();
			
		} catch (BusinessException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Inicializar os totais do mapa de custo.
	 */
	private void inicializarTotais() {
		totalExpense = BigDecimal.ZERO.setScale( 2 );
		totalExpenseWithoutFreight = BigDecimal.ZERO.setScale( 2 );
		totalFreightForward = BigDecimal.ZERO.setScale( 2 );
		totalStorage = BigDecimal.ZERO.setScale( 2 );
		totalValue = BigDecimal.ZERO.setScale( 2 );
		importCost = BigDecimal.ZERO.setScale( 2 );
		totalExpenseUSD = BigDecimal.ZERO.setScale( 2 );
		totalExpenseWithoutFreightUSD = BigDecimal.ZERO.setScale( 2 );
		totalFreightForwardUSD = BigDecimal.ZERO.setScale( 2 );
		totalStorageUSD = BigDecimal.ZERO.setScale( 2 );
		totalValueUSD = BigDecimal.ZERO.setScale( 2 );
	}

	/**
	 * Prepara os parametros do relatorio.
	 */
	private void prepararParametros() {
		parametros = new HashMap<String, Object>();
		FacesContext fc = FacesContext.getCurrentInstance();
		// Configurando os parametros de titulos
		parametros.put("titulo", TemplateMessageHelper.getMessage(
				MensagensSistema.MAPACUSTO, "lblMapCost", fc.getViewRoot()
						.getLocale()));

		parametros.put("SUBREPORT_DIR", ((ServletContext) fc
				.getExternalContext().getContext()).getRealPath("/relatorios/")
				+ "/");
		
		parametros.put("REPORT_RESOURCE_BUNDLE", java.util.ResourceBundle
				.getBundle("br.com.ilog.geral.presentation.mapacusto", fc
						.getViewRoot().getLocale()));
		
		parametros.put("usuario", sessaoUsuario.getUsuario().getNome());

		parametros.put("cnpj", carga.getProcBroker().getCnpjFoxconn());

		// informando os parametros do filtro
		if (filtroMapaCusto.getMoeda() != null) {
			parametros.put("moeda", filtroMapaCusto.getMoeda().getSigla());
		}
		if (filtroMapaCusto.getTxCambio() != null) {
			parametros.put("custoOriginal", filtroMapaCusto.getTxCambio());
		}
		if (filtroMapaCusto.getTxCambioUSD() != null) {
			parametros.put("custoUSD", filtroMapaCusto.getTxCambioUSD());
		}
		parametros.put("totalExpenseWithoutFreightUSD", totalExpenseWithoutFreightUSD );
		parametros.put("totalExpenseWithoutFreight", totalExpenseWithoutFreight );
		
		parametros.put("totalFreightForwardUSD", totalFreightForwardUSD );
		parametros.put("totalFreightForward", totalFreightForward );
		
		parametros.put("totalStorageUSD", totalStorageUSD );
		parametros.put("totalStorage", totalStorage );
		
		parametros.put("totalExpenseUSD", totalExpenseUSD );
		parametros.put("totalExpense", totalExpense );
		
		parametros.put("totalValueUSD", totalValueUSD );
		parametros.put("totalValue", totalValue );
		
		parametros.put("importCost", importCost );
	}

	/**
	 * Metodo exporta para PDF
	 * 
	 * @param arg0
	 */
	public void exportarPdf(Carga carga) {
		try {
			if(filtroMapaCusto.getMoeda() != null && filtroMapaCusto.getTxCambio() != null && filtroMapaCusto.getTxCambioUSD() != null){
			inicializarTotais();
			
			list = new ArrayList<MapaCusto>();
			
			this.carga =facade.alterarCusto( carga );
			
			calculaTaxas();

			prepararParametros();
			new ExportarRelatorio<MapaCusto>().gerarPDF(list, parametros,
					"/relatorios/costMap.jasper", "MapaCusto.pdf");
			}else{
				Messages.adicionaMensagemDeErro("Campos Obrigatorios não preenchidos");
				return;
			}
		} catch (Exception e) {
			logger.error("error: {} " + e);
			e.printStackTrace();
		}
	}

	/**
	 * Metodo exporta para XSL
	 * 
	 * @param arg0
	 */
	public void exportarXls(Carga carga) {
		try {
			
			inicializarTotais();
			
			this.carga = carga;
			calculaTaxas();

			prepararParametros();
			
			new ExportarRelatorio<MapaCusto>().gerarXLS(list, parametros,
					"/relatorios/costMap.jasper", "MapaCusto.xls");
		} catch (Exception e) {
			logger.error("error: {} " + e);
			e.printStackTrace();
		}
	}

	/**
	 * @return the carga
	 */
	public Carga getCarga() {
		return carga;
	}

	/**
	 * @param carga
	 *            the carga to set
	 */
	public void setCarga(Carga carga) {
		this.carga = carga;
	}

	/**
	 * @return the filtroMapaCusto
	 */
	public BasicFiltroMapaCusto getFiltroMapaCusto() {
		return filtroMapaCusto;
	}

	/**
	 * @param filtroMapaCusto
	 *            the filtroMapaCusto to set
	 */
	public void setFiltroMapaCusto(BasicFiltroMapaCusto filtroMapaCusto) {
		this.filtroMapaCusto = filtroMapaCusto;
	}

	/**
	 * @return the moedas
	 */
	public List<Moeda> getMoedas() {
		return moedas;
	}

	/**
	 * @param moedas
	 *            the moedas to set
	 */
	public void setMoedas(List<Moeda> moedas) {
		this.moedas = moedas;
	}

	/**
	 * @return the totalValueUSD
	 */
	public BigDecimal getTotalValueUSD() {
		return totalValueUSD;
	}

	/**
	 * @param totalValueUSD the totalValueUSD to set
	 */
	public void setTotalValueUSD(BigDecimal totalValueUSD) {
		this.totalValueUSD = totalValueUSD;
	}

}
