package br.com.ilog.relatorio.util;

import java.awt.Color;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignFrame;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.StretchTypeEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import br.com.ilog.cadastro.business.entity.Canal;
import br.com.ilog.importacao.business.entity.StatusInvoice;

public class ExportarRelatorio<T> {

	/**
	 * Parametros das informações que serao listadas no relatorio.
	 */

	private boolean selecioneTodos;
	/**
	 * CAMPOS INVOICE
	 */
	private boolean processoInvoice;
	private boolean invoice;
	private boolean observacaoInvoice;
	private boolean dtInvoice;
	private boolean exportadorInvoice;
	private boolean compradorInvoice;
	private boolean origemInvoice;
	private boolean status;
	private boolean incoterm;
	private boolean critico;
	private boolean pliNumber;
	private boolean totalPesoLiqInvoice;
	private boolean totalPesoBrutoInvoice;

	private boolean numAps;
	
	private boolean master;
	private boolean hawb;
	/**
	 * dtPrevista de coleta
	 */
	private boolean dtColeta;
	/**
	 * data do pickup (follow Up)
	 */
	private boolean dtColetaReal;
	/**
	 * data prevista de chegada
	 */
	private boolean dtPrevista;
	/**
	 * data de chegada real
	 */
	private boolean dtReal;
	private boolean responsavel;
	private boolean cidadeOrigem;
	private boolean cidadeDestino;
	private boolean pesoBruto;
	private boolean pesoCubado;
	private boolean numeroDi;
	private boolean dtRegistroDi;
	private boolean dtConsolidacao;
	private boolean rota;
	private boolean modal;
	private boolean agenteCargas;
	private boolean canal;
	private boolean exportFornecedor;
	private boolean totalDiasEst;
	private boolean totalDiasReal;

	private JasperPrint print;

	private static final int ALTURA_CELULA = 35;

	private static final int ALTURA_CABECALHO = 33;
	
	private static final int ALTURA_TITULO = 50;

	private static final int TAMANHO_CAMPO_DATA = 80;
	
	private Color backColor = new Color(0, 82, 94);

	private void montarRelatorio(List<T> lista, Map<String, Object> parametros,
			String relatorio) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ServletContext servletContext = (ServletContext) facesContext
				.getExternalContext().getContext();
		String pathRel = servletContext.getRealPath(relatorio);

		String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		Calendar c1 = Calendar.getInstance(); // today

		// parametros
		parametros.put("logo", servletContext
				.getRealPath("/pub/img/logo_ilog.png"));
		parametros.put("titulo", parametros.get("titulo").toString() + " "
				+ sdf.format(c1.getTime()));

		// recebe por parametro uma lista da propia classe jah populada
		JRDataSource jrds = new JRBeanCollectionDataSource(lista);

		try {
			// gera o relatorio em print
			print = JasperFillManager.fillReport(pathRel, parametros, jrds);

		} catch (JRException e) {
			e.printStackTrace();
		}
	}

	public void gerarXLS(List<T> lista, Map<String, Object> parametros,
			String relatorio, String fileName) {
		montarRelatorio(lista, parametros, relatorio);

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) facesContext
				.getExternalContext().getResponse();

		ServletOutputStream output;
		try {
			output = response.getOutputStream();
			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ fileName);

			JRXlsExporter xls = new JRXlsExporter();
			print.setLeftMargin(0);
			print.setRightMargin(0);
			xls.setParameter(JRXlsExporterParameter.JASPER_PRINT, print);
			xls.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, output);
			xls.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE,
					Boolean.TRUE);
			xls.setParameter(
							JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,
							Boolean.TRUE);
			xls.setParameter(JRXlsExporterParameter.MAXIMUM_ROWS_PER_SHEET,
					Integer.decode("65000"));
			xls.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,
					Boolean.TRUE);
			xls.setParameter(
					JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
					Boolean.TRUE);
			xls.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS,
					Boolean.FALSE);

			xls.exportReport();
			output.flush();
			output.close();
			
			facesContext.responseComplete();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JRException e) {
			e.printStackTrace();
		} finally {
		}

	}

	public void gerarPDF(List<T> listaRelatorio,
			Map<String, Object> parametros, String relatorio, String fileName)
			throws Exception {

		montarRelatorio(listaRelatorio, parametros, relatorio);

		byte[] bytes = JasperExportManager.exportReportToPdf(print);

		if (bytes == null)
			throw new Exception("Array de bytes nulo.");

		if (fileName == null)
			throw new Exception("Nome do arquivo eh nulo.");

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) facesContext
				.getExternalContext().getResponse();
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ fileName + "\";");
		response.setContentLength(bytes.length);
		ServletOutputStream ouputStream = response.getOutputStream();
		ouputStream.write(bytes, 0, bytes.length);
		facesContext.responseComplete();
	}

	/**
	 * @param listaRelatorio
	 * @param parametros
	 * @param relatorio
	 * @param subreport
	 * @throws Exception
	 */
	private void montarDinamicoImportacao(List<T> listaRelatorio,
			FacesContext facesContext, Map<String, Object> parametros,
			String relatorio, String subreport) throws Exception {
		
		int x = 0;
		// int widthDesign = 860;

		ServletContext servletContext = (ServletContext) facesContext
				.getExternalContext().getContext();
		String pathRel = servletContext.getRealPath(relatorio);

		// carregar o JRXML
		JasperDesign design = JRXmlLoader.load(pathRel);

		// APENAS SE EXCEL
		if (!parametros.get("tipo").equals("pdf")) {
			design.setIgnorePagination(true);
		}

		// TIPO DE RELATORIO: PDF ou EXCEL
		JRDesignParameter parameter = new JRDesignParameter();

		parameter = new JRDesignParameter();
		parameter.setName("SUBREPORT_DIR");
		parameter.setValueClass(java.lang.String.class);
		design.addParameter(parameter);

		// ATRIBUTOS
		JRDesignField campo;
		JRDesignTextField textField;
		JRDesignExpression expr = new JRDesignExpression();
		
		JRDesignBand bandTitulo = new JRDesignBand();
		bandTitulo.setHeight(ALTURA_TITULO);
		
		textField = new JRDesignTextField();
		textField.setBlankWhenNull(true);
		textField.setX(0);
		textField.setY(0);
		textField.setWidth(3240);
		textField.setHeight(ALTURA_TITULO);
		textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
		textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
		textField.setBackcolor( new Color(255, 255, 255) );
		textField.setForecolor( backColor );
		textField.setBold(true);
		textField.setFontSize(18);
		expr = new JRDesignExpression();
		expr.setText("$P{titulo}");
		textField.setExpression(expr);
		
		bandTitulo.addElement( textField );
		
		design.setTitle(bandTitulo);
		
		// BANDS
		// CABECALHO
		JRDesignBand bandColHeader = new JRDesignBand();
		bandColHeader.setHeight(ALTURA_CABECALHO);
		expr = new JRDesignExpression();
		expr.setText("$V{PAGE_NUMBER}.intValue() == 1 || $P{tipo}.equals(\"pdf\")");
		bandColHeader.setPrintWhenExpression(expr);

		// DETAIL
		JRDesignBand bandDetail = new JRDesignBand();
		bandDetail.setHeight(ALTURA_CELULA);
		JRDesignFrame fDetail = new JRDesignFrame();
		fDetail.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
		fDetail.setX(0);
		fDetail.setY(0);
		fDetail.setHeight(ALTURA_CELULA);
		fDetail.setMode(ModeEnum.OPAQUE);

		int tamanho = 0;
		// NUMERO INVOICE
		if (invoice || selecioneTodos) {
			tamanho = 90;

			campo = new JRDesignField();
			campo.setName("invoice.numeroInvoice");
			campo.setValueClass(String.class);
			design.addField(campo);

			textField = new JRDesignTextField();
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CABECALHO);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setBackcolor( backColor );
			textField.setForecolor(new Color(255, 255, 255));
			textField.setBold(true);
			textField.setFontSize(10);
			expr = new JRDesignExpression();
			expr.setText("$R{lblNumInvoice}");
			textField.setExpression(expr);
			bandColHeader.addElement(textField);

			textField = new JRDesignTextField();
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CELULA);
			textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setForecolor(Color.BLACK);
			expr = new JRDesignExpression();
			expr.setText("$F{invoice.numeroInvoice}.toUpperCase()");
			textField.setExpression(expr);
			fDetail.addElement(textField);

			x += tamanho;
		}

		// PROCESSO INVOICE
		if (processoInvoice || selecioneTodos) {
			tamanho = 70;

			campo = new JRDesignField();
			campo.setName("carga.processo");
			campo.setValueClass(String.class);
			design.addField(campo);

			textField = new JRDesignTextField();
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CABECALHO);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setBackcolor( backColor );
			textField.setForecolor(new Color(255, 255, 255));
			textField.setBold(true);
			textField.setFontSize(10);
			expr = new JRDesignExpression();
			expr.setText("$R{lblProcesso}");
			textField.setExpression(expr);
			bandColHeader.addElement(textField);

			textField = new JRDesignTextField();
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CELULA);
			textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setForecolor(Color.BLACK);
			expr = new JRDesignExpression();
			expr.setText("$F{carga.processo}");
			textField.setExpression(expr);
			fDetail.addElement(textField);

			x += tamanho;

		}

		// observacao INVOICE

		if (observacaoInvoice || selecioneTodos) {
			tamanho = 500;

			campo = new JRDesignField();
			campo.setName("followUp.observacao");
			campo.setValueClass(String.class);
			design.addField(campo);

			textField = new JRDesignTextField();
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CABECALHO);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setBackcolor( backColor );
			textField.setForecolor(new Color(255, 255, 255));
			textField.setBold(true);
			textField.setFontSize(10);
			expr = new JRDesignExpression();
			expr.setText("$R{lblObservacao}");
			textField.setExpression(expr);
			bandColHeader.addElement(textField);

			textField = new JRDesignTextField();
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CELULA);
			textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setForecolor(Color.BLACK);
			expr = new JRDesignExpression();
			expr.setText("$F{followUp.observacao}");
			textField.setExpression(expr);
			fDetail.addElement(textField);

			x += tamanho;

		}

		//DATA INVOICE
		if (dtInvoice || selecioneTodos) {
			tamanho = 80;
			campo = new JRDesignField();
			campo.setName("invoice.dtEmissao");
			campo.setValueClass(Date.class);
			design.addField(campo);

			textField = new JRDesignTextField();
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CABECALHO);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setBackcolor( backColor );
			textField.setForecolor(new Color(255, 255, 255));
			textField.setBold(true);
			textField.setFontSize(10);
			expr = new JRDesignExpression();
			expr.setText("$R{lblDtInvoice}");
			textField.setExpression(expr);
			bandColHeader.addElement(textField);

			textField = new JRDesignTextField();
			textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CELULA);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setForecolor(Color.BLACK);
			textField.setPattern("dd/MM/yyyy");
			expr = new JRDesignExpression();
			expr.setText("$F{invoice.dtEmissao}");
			textField.setExpression(expr);
			fDetail.addElement(textField);

			x += tamanho;

		}
		// EXPORTADOR INVOICE
		if (exportadorInvoice || selecioneTodos) {
			tamanho = 200;

			campo = new JRDesignField();
			campo.setName("exportadorInvoice.nomeFantasia");
			campo.setValueClass(String.class);
			design.addField(campo);

			textField = new JRDesignTextField();
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CABECALHO);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setBackcolor( backColor );
			textField.setForecolor(new Color(255, 255, 255));
			textField.setBold(true);
			textField.setFontSize(10);
			expr = new JRDesignExpression();
			expr.setText("$R{lblExportador}");
			textField.setExpression(expr);
			bandColHeader.addElement(textField);

			textField = new JRDesignTextField();
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CELULA);
			textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setForecolor(Color.BLACK);
			expr = new JRDesignExpression();
			expr.setText("$F{exportadorInvoice.nomeFantasia}.toUpperCase()");
			textField.setExpression(expr);
			fDetail.addElement(textField);

			x += tamanho;

		}

		if (compradorInvoice || selecioneTodos) {
			tamanho = 200;

			campo = new JRDesignField();
			campo.setName("invoice.comprador.nome");
			campo.setValueClass(String.class);
			design.addField(campo);

			textField = new JRDesignTextField();
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CABECALHO);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setBackcolor( backColor );
			textField.setForecolor(new Color(255, 255, 255));
			textField.setBold(true);
			textField.setFontSize(10);
			expr = new JRDesignExpression();
			expr.setText("$R{lblComprador}");
			textField.setExpression(expr);
			bandColHeader.addElement(textField);

			textField = new JRDesignTextField();
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CELULA);
			textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setForecolor(Color.BLACK);
			expr = new JRDesignExpression();
			expr.setText("$F{invoice.comprador.nome}.toUpperCase()");
			textField.setExpression(expr);
			fDetail.addElement(textField);

			x += tamanho;
		}

		if (origemInvoice || selecioneTodos) {
			tamanho = 150;

			campo = new JRDesignField();
			campo.setName("invoice.paisOrigem.nome");
			campo.setValueClass(String.class);
			design.addField(campo);
			
			textField = new JRDesignTextField();
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CABECALHO);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setBackcolor( backColor );
			textField.setForecolor(new Color(255, 255, 255));
			textField.setBold(true);
			textField.setFontSize(10);
			expr = new JRDesignExpression();
			expr.setText("$R{lblOrigemInvoice}");
			textField.setExpression(expr);
			bandColHeader.addElement(textField);

			textField = new JRDesignTextField();
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CELULA);
			textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setForecolor(Color.BLACK);
			expr = new JRDesignExpression();
			expr.setText("$F{invoice.paisOrigem.nome}.toUpperCase()");
			textField.setExpression(expr);
			fDetail.addElement(textField);

			x += tamanho;
		}

		//TODO STATUS
		if (status || selecioneTodos) {
			tamanho = 100;

			campo = new JRDesignField();
			campo.setName("invoice.status");
			campo.setValueClass(StatusInvoice.class);
			design.addField(campo);

			campo = new JRDesignField();
			campo.setName("carga.cidadeAtual.sigla");
			campo.setValueClass(String.class);
			design.addField(campo);

			textField = new JRDesignTextField();
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CABECALHO);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setBackcolor( backColor );
			textField.setForecolor(new Color(255, 255, 255));
			textField.setBold(true);
			textField.setFontSize(10);
			expr = new JRDesignExpression();
			expr.setText("$R{lblStatus}");
			textField.setExpression(expr);
			bandColHeader.addElement(textField);

			//CADASTRADA
			textField = new JRDesignTextField();
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CELULA);
			textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setForecolor(Color.BLACK);
			expr = new JRDesignExpression();
			expr.setText("$R{C_I}.toUpperCase()");
			textField.setExpression(expr);
			expr = new JRDesignExpression();
			expr.setText("$F{invoice.status}.name().equals(\"C\")");
			textField.setPrintWhenExpression(expr);
			fDetail.addElement(textField);
			
			//AGUARDANDO COLETA
			textField = new JRDesignTextField();
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CELULA);
			textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setForecolor(Color.BLACK);
			expr = new JRDesignExpression();
			expr.setText("$R{AC}.toUpperCase()");
			textField.setExpression(expr);
			expr = new JRDesignExpression();
			expr.setText("$F{invoice.status}.name().equals(\"AC\")");
			textField.setPrintWhenExpression(expr);
			fDetail.addElement(textField);
			
			//A SER COLETADA
			textField = new JRDesignTextField();
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CELULA);
			textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setForecolor(Color.BLACK);
			expr = new JRDesignExpression();
			expr.setText("$R{TBS}.toUpperCase()");
			textField.setExpression(expr);
			expr = new JRDesignExpression();
			expr.setText("$F{invoice.status}.name().equals(\"TBS\")");
			textField.setPrintWhenExpression(expr);
			fDetail.addElement(textField);
			
			//A CAMINHO DE
			textField = new JRDesignTextField();
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CELULA);
			textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setForecolor(Color.BLACK);
			expr = new JRDesignExpression();
			expr.setText("$R{ITT}.toUpperCase() + \" \" + $F{carga.cidadeAtual.sigla}.toUpperCase()");
			textField.setExpression(expr);
			expr = new JRDesignExpression();
			expr.setText("$F{invoice.status}.name().equals(\"ITT\")");
			textField.setPrintWhenExpression(expr);
			fDetail.addElement(textField);
			
			//NA FÁBRICA
			textField = new JRDesignTextField();
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CELULA);
			textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setForecolor(Color.BLACK);
			expr = new JRDesignExpression();
			expr.setText("$R{AT}.toUpperCase()");
			textField.setExpression(expr);
			expr = new JRDesignExpression();
			expr.setText("$F{invoice.status}.name().equals(\"AT\")");
			textField.setPrintWhenExpression(expr);
			fDetail.addElement(textField);
			
			//EM LIBERAÇÃO
			textField = new JRDesignTextField();
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CELULA);
			textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setForecolor(Color.BLACK);
			expr = new JRDesignExpression();
			expr.setText("$R{ICC}.toUpperCase()");
			textField.setExpression(expr);
			expr = new JRDesignExpression();
			expr.setText("$F{invoice.status}.name().equals(\"ICC\")");
			textField.setPrintWhenExpression(expr);
			fDetail.addElement(textField);
			
			//FECHADO
			textField = new JRDesignTextField();
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CELULA);
			textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setForecolor(Color.BLACK);
			expr = new JRDesignExpression();
			expr.setText("$R{F}.toUpperCase()");
			textField.setExpression(expr);
			expr = new JRDesignExpression();
			expr.setText("$F{invoice.status}.name().equals(\"F\")");
			textField.setPrintWhenExpression(expr);
			fDetail.addElement(textField);
			
			//PLANEJADA
			textField = new JRDesignTextField();
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CELULA);
			textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setForecolor(Color.BLACK);
			expr = new JRDesignExpression();
			expr.setText("$R{P}.toUpperCase()");
			textField.setExpression(expr);
			expr = new JRDesignExpression();
			expr.setText("$F{invoice.status}.name().equals(\"P\")");
			textField.setPrintWhenExpression(expr);
			fDetail.addElement(textField);

			//COLETADA
			textField = new JRDesignTextField();
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CELULA);
			textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setForecolor(Color.BLACK);
			expr = new JRDesignExpression();
			expr.setText("$R{CL}.toUpperCase()");
			textField.setExpression(expr);
			expr = new JRDesignExpression();
			expr.setText("$F{invoice.status}.name().equals(\"CL\")");
			textField.setPrintWhenExpression(expr);
			fDetail.addElement(textField);
			
			x += tamanho;

		}

		if (incoterm || selecioneTodos) {
			tamanho = 60;

			campo = new JRDesignField();
			campo.setName("invoice.incoterm.sigla");
			campo.setValueClass(String.class);
			design.addField(campo);

			textField = new JRDesignTextField();
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CABECALHO);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setBackcolor( backColor );
			textField.setForecolor(new Color(255, 255, 255));
			textField.setBold(true);
			textField.setFontSize(10);
			expr = new JRDesignExpression();
			expr.setText("$R{lblIncoterm}");
			textField.setExpression(expr);
			bandColHeader.addElement(textField);

			textField = new JRDesignTextField();
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CELULA);
			textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setForecolor(Color.BLACK);
			expr = new JRDesignExpression();
			expr.setText("$F{invoice.incoterm.sigla}.toUpperCase()");
			textField.setExpression(expr);
			fDetail.addElement(textField);

			x += tamanho;
		}

		if (critico || selecioneTodos) {
			tamanho = 50;
			campo = new JRDesignField();
			campo.setName("invoice.critico");
			campo.setValueClass(Boolean.class);
			design.addField(campo);

			textField = new JRDesignTextField();
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CABECALHO);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setBackcolor( backColor );
			textField.setForecolor(new Color(255, 255, 255));
			textField.setBold(true);
			textField.setFontSize(10);
			expr = new JRDesignExpression();
			expr.setText("$R{lblCritico}");
			textField.setExpression(expr);
			bandColHeader.addElement(textField);

			textField = new JRDesignTextField();
			textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CELULA);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setForecolor(Color.BLACK);
			expr = new JRDesignExpression();
			expr.setText("$R{lblSim}.toUpperCase()");
			textField.setExpression(expr);
			expr = new JRDesignExpression();
			expr.setText("$F{invoice.critico}");
			textField.setPrintWhenExpression(expr);
			fDetail.addElement(textField);

			textField = new JRDesignTextField();
			textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CELULA);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setForecolor(Color.BLACK);
			expr = new JRDesignExpression();
			expr.setText("$R{lblNao}.toUpperCase()");
			textField.setExpression(expr);
			expr = new JRDesignExpression();
			expr.setText("!$F{invoice.critico}");
			textField.setPrintWhenExpression(expr);
			fDetail.addElement(textField);

			x = x + tamanho;
		}

		if (pliNumber || selecioneTodos) {
			tamanho = 70;

			campo = new JRDesignField();
			campo.setName("invoice.numPli");
			campo.setValueClass(String.class);
			design.addField(campo);

			textField = new JRDesignTextField();
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CABECALHO);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setBackcolor( backColor );
			textField.setForecolor(new Color(255, 255, 255));
			textField.setBold(true);
			textField.setFontSize(10);
			expr = new JRDesignExpression();
			expr.setText("$R{lblPliNumero}");
			textField.setExpression(expr);
			bandColHeader.addElement(textField);

			textField = new JRDesignTextField();
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CELULA);
			textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setForecolor(Color.BLACK);
			expr = new JRDesignExpression();
			expr.setText("$F{invoice.numPli}.toUpperCase()");
			textField.setExpression(expr);
			fDetail.addElement(textField);

			x += tamanho;
		}

		if (totalPesoLiqInvoice || selecioneTodos) {
			/**
			 * campo = new JRDesignField(); textField = new JRDesignTextField();
			 * tamanho = 70;
			 * 
			 * adicionarCampoPeso( campo, "pesoLiquido", "lblPesoLiquido",
			 * design, textField, tamanho, x, expr, bandColHeader, fDetail );
			 **/
			tamanho = 70;

			campo = new JRDesignField();
			campo.setName("pesoLiquido");
			campo.setValueClass(String.class);
			design.addField(campo);

			textField = new JRDesignTextField();
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CABECALHO);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setBackcolor( backColor );
			textField.setForecolor(new Color(255, 255, 255));
			textField.setBold(true);
			textField.setFontSize(10);
			expr = new JRDesignExpression();
			expr.setText("$R{lblPesoLiquido}");
			textField.setExpression(expr);
			bandColHeader.addElement(textField);

			textField = new JRDesignTextField();
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CELULA);
			textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setForecolor(Color.BLACK);
			expr = new JRDesignExpression();
			expr.setText("$F{pesoLiquido}");
			textField.setExpression(expr);
			fDetail.addElement(textField);

			x += tamanho;

		}

		if (totalPesoBrutoInvoice || selecioneTodos) {
			/**
			 * campo = new JRDesignField(); textField = new JRDesignTextField();
			 * tamanho = 70; adicionarCampoPeso(campo, "invoice.pesoBruto",
			 * "lblPesoBruto", design, textField, tamanho, x, expr,
			 * bandColHeader, fDetail);
			 * 
			 * x += tamanho;
			 **/
			tamanho = 70;

			campo = new JRDesignField();
			campo.setName("pesoCubado");
			campo.setValueClass(String.class);
			design.addField(campo);

			textField = new JRDesignTextField();
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CABECALHO);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setBackcolor( backColor );
			textField.setForecolor(new Color(255, 255, 255));
			textField.setBold(true);
			textField.setFontSize(10);
			expr = new JRDesignExpression();
			expr.setText("$R{lblPesoBruto}");
			textField.setExpression(expr);
			bandColHeader.addElement(textField);

			textField = new JRDesignTextField();
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CELULA);
			textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setForecolor(Color.BLACK);
			expr = new JRDesignExpression();
			expr.setText("$F{pesoCubado}");
			textField.setExpression(expr);
			fDetail.addElement(textField);

			x += tamanho;

		}
		
		// MASTER
		if (master || selecioneTodos) {
			tamanho = 100;
			campo = new JRDesignField();
			textField = new JRDesignTextField();

			adicionarCampoString(campo, "carga.numMaster", "lblNumMaster", design,
					textField, tamanho, x, expr, bandColHeader, fDetail);
			x += tamanho;

		}

		// HAWB
		if (hawb || selecioneTodos) {
			tamanho = 100;
			campo = new JRDesignField();
			textField = new JRDesignTextField();

			adicionarCampoString(campo, "carga.hawb", "lblHawb", design,
					textField, tamanho, x, expr, bandColHeader, fDetail);
			x += tamanho;

		}

		// DATA CAD CONSOLIDACAO
		if (dtConsolidacao || selecioneTodos) {
			tamanho = 80;
			campo = new JRDesignField();
			textField = new JRDesignTextField();

			adicionarCampoData(campo, "carga.dtConsolidacao",
					"lblDtConsolidacao", design, textField, tamanho, x, expr,
					bandColHeader, fDetail);
			x += tamanho;

		}

		// DATA ETA COLLECTION
		if (dtColeta || selecioneTodos) {
			tamanho = 80;
			campo = new JRDesignField();
			textField = new JRDesignTextField();

			adicionarCampoData(campo, "carga.dtColeta", "lblColetaPrevista",
					design, textField, tamanho, x, expr, bandColHeader, fDetail);
			x += tamanho;

		}

		// DATA ATA COLLECTION
		if (dtColetaReal || selecioneTodos) {
			tamanho = 80;
			campo = new JRDesignField();
			textField = new JRDesignTextField();

			adicionarCampoData(campo, "trechoInicial.dtRealizado",
					"lblColetaReal", design, textField, tamanho, x, expr,
					bandColHeader, fDetail);
			x += tamanho;

		}

		// TODO DATA CHEGADA FABRICA
		if (dtReal || selecioneTodos) {
			
			tamanho = TAMANHO_CAMPO_DATA;
			
			campo = new JRDesignField();
			campo.setName("ultimoTrechoEstimado");
			campo.setValueClass(String.class);
			design.addField(campo);

			textField = new JRDesignTextField();
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CABECALHO);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setBackcolor( backColor );
			textField.setForecolor(new Color(255, 255, 255));
			textField.setBold(true);
			textField.setFontSize(10);
			expr = new JRDesignExpression();
			expr.setText("$R{lblEstmaMao}");
			textField.setExpression(expr);
			bandColHeader.addElement(textField);

			textField = new JRDesignTextField();
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CELULA);
			textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setForecolor(Color.BLACK);
			expr = new JRDesignExpression();
			expr.setText("$F{ultimoTrechoEstimado}");
			textField.setExpression(expr);
			fDetail.addElement(textField);

			x += tamanho;

			
		}
		
		// TODO DATA CHEGADA Manaus real
		if (dtReal || selecioneTodos) {
			
			tamanho = 80;

			campo = new JRDesignField();
			campo.setName("ultimoTrechoReal");
			campo.setValueClass(String.class);
			design.addField(campo);

			textField = new JRDesignTextField();
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CABECALHO);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setBackcolor( backColor );
			textField.setForecolor(new Color(255, 255, 255));
			textField.setBold(true);
			textField.setFontSize(10);
			expr = new JRDesignExpression();
			expr.setText("$R{lblDtRealMao}");
			textField.setExpression(expr);
			bandColHeader.addElement(textField);

			textField = new JRDesignTextField();
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CELULA);
			textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setForecolor(Color.BLACK);
			expr = new JRDesignExpression();
			expr.setText("$F{ultimoTrechoReal}");
			textField.setExpression(expr);
			fDetail.addElement(textField);

			x += tamanho;

			
		}
		// TODO DATA ETA LOM
		if (dtPrevista || selecioneTodos) {

			tamanho = 80;
			campo = new JRDesignField();
			textField = new JRDesignTextField();

			adicionarCampoData(campo, "carga.dtPrevista", "lblEtaF", design,
					textField, tamanho, x, expr, bandColHeader, fDetail);
			x += tamanho;
		}

		//DATA CHEGADA LOM
		if (dtReal || selecioneTodos) {
			tamanho = 80;
			campo = new JRDesignField();
			textField = new JRDesignTextField();

			adicionarCampoData(campo, "carga.dtChegada", "lblRealF", design,
					textField, tamanho, x, expr, bandColHeader, fDetail);

			x += tamanho;
		}

		if (rota || selecioneTodos) {
			tamanho = 50;
			campo = new JRDesignField();
			textField = new JRDesignTextField();

			adicionarCampoString(campo, "carga.rota.codigo", "lblRota", design,
					textField, tamanho, x, expr, bandColHeader, fDetail);

			x += tamanho;
		}

		if (agenteCargas || selecioneTodos) {
			tamanho = 200;
			campo = new JRDesignField();
			textField = new JRDesignTextField();

			adicionarCampoString(campo, "carga.agenteCarga.nomeFantasia",
					"lblAgenteCarga", design, textField, tamanho, x, expr,
					bandColHeader, fDetail);

			x += tamanho;
		}

		if (cidadeOrigem || selecioneTodos) {
			tamanho = 150;
			campo = new JRDesignField();
			textField = new JRDesignTextField();

			adicionarCampoString(campo, "carga.rota.cidadeOrigem.nome",
					"lblCidadeOrigem", design, textField, tamanho, x, expr,
					bandColHeader, fDetail);

			x += tamanho;

		}
		if (cidadeDestino || selecioneTodos) {
			tamanho = 150;
			campo = new JRDesignField();
			textField = new JRDesignTextField();

			adicionarCampoString(campo, "carga.rota.cidadeDestino.nome",
					"lblCidadeDestino", design, textField, tamanho, x, expr,
					bandColHeader, fDetail);

			x += tamanho;

		}

		if (modal || selecioneTodos) {
			tamanho = 100;
			campo = new JRDesignField();
			textField = new JRDesignTextField();

			adicionarCampoString(campo, "carga.rota.tipoTransporte.descricao",
					"lblModal", design, textField, tamanho, x, expr,
					bandColHeader, fDetail);

			x += tamanho;
		}
		if (pesoBruto || selecioneTodos) {
			/**
			 * tamanho = 90; campo = new JRDesignField(); textField = new
			 * JRDesignTextField();
			 * 
			 * adicionarCampoPeso(campo, "carga.pesoBrutoHawb",
			 * "lblPesoBrutoHawb", design, textField, tamanho, x, expr,
			 * bandColHeader, fDetail);
			 * 
			 * x += tamanho;
			 **/

			tamanho = 90;

			campo = new JRDesignField();
			campo.setName("pesoBrutoHawbCarga");
			campo.setValueClass(String.class);
			design.addField(campo);

			textField = new JRDesignTextField();
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CABECALHO);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setBackcolor( backColor );
			textField.setForecolor(new Color(255, 255, 255));
			textField.setBold(true);
			textField.setFontSize(10);
			expr = new JRDesignExpression();
			expr.setText("$R{lblPesoBrutoHawb}");
			textField.setExpression(expr);
			bandColHeader.addElement(textField);

			textField = new JRDesignTextField();
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CELULA);
			textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setForecolor(Color.BLACK);
			expr = new JRDesignExpression();
			expr.setText("$F{pesoBrutoHawbCarga}");
			textField.setExpression(expr);
			fDetail.addElement(textField);

			x += tamanho;

		}

		if (pesoCubado || selecioneTodos) {
			/**
			 * tamanho = 90; campo = new JRDesignField(); textField = new
			 * JRDesignTextField();
			 * 
			 * adicionarCampoPeso(campo, "carga.pesoCubadoHawb",
			 * "lblPesoCubadoHawb", design, textField, tamanho, x, expr,
			 * bandColHeader, fDetail);
			 * 
			 * x += tamanho;
			 **/

			tamanho = 90;

			campo = new JRDesignField();
			campo.setName("pesoCubadoCarga");
			campo.setValueClass(String.class);
			design.addField(campo);

			textField = new JRDesignTextField();
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CABECALHO);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setBackcolor( backColor );
			textField.setForecolor(new Color(255, 255, 255));
			textField.setBold(true);
			textField.setFontSize(10);
			expr = new JRDesignExpression();
			expr.setText("$R{lblPesoCubadoHawb}");
			textField.setExpression(expr);
			bandColHeader.addElement(textField);

			textField = new JRDesignTextField();
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CELULA);
			textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setForecolor(Color.BLACK);
			expr = new JRDesignExpression();
			expr.setText("$F{pesoCubadoCarga}");
			textField.setExpression(expr);
			fDetail.addElement(textField);

			x += tamanho;

		}

		if (responsavel || selecioneTodos) {
			tamanho = 200;
			campo = new JRDesignField();
			textField = new JRDesignTextField();

			adicionarCampoString(campo, "carga.importador.nome",
					"lblResponsavel", design, textField, tamanho, x, expr,
					bandColHeader, fDetail);

			x += tamanho;
		}

		if (numeroDi || selecioneTodos) {
			tamanho = 90;
			campo = new JRDesignField();
			textField = new JRDesignTextField();

			adicionarCampoString(campo, "carga.procBroker.nrDI", "lblNumeroDI",
					design, textField, tamanho, x, expr, bandColHeader, fDetail);

			x += tamanho;

		}

		if (dtRegistroDi || selecioneTodos) {
			tamanho = 80;
			campo = new JRDesignField();
			textField = new JRDesignTextField();

			adicionarCampoData(campo, "carga.procBroker.dataDI", "lblDataDI",
					design, textField, tamanho, x, expr, bandColHeader, fDetail);

			x += tamanho;

		}

		// CANAL
		if (canal || selecioneTodos) {
			tamanho = 100;
			campo = new JRDesignField();
			campo.setName("carga.canal.canal");
			campo.setValueClass(Canal.class);
			design.addField(campo);

			textField = new JRDesignTextField();
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CABECALHO);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setBackcolor( backColor );
			textField.setForecolor(new Color(255, 255, 255));
			textField.setBold(true);
			textField.setFontSize(10);
			expr = new JRDesignExpression();
			expr.setText("$R{lblCanal}");
			textField.setExpression(expr);
			bandColHeader.addElement(textField);

			textField = new JRDesignTextField();
			textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CELULA);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setForecolor(Color.BLACK);
			expr = new JRDesignExpression();
			expr.setText("$R{VERMELHO}.toUpperCase()");
			textField.setExpression(expr);
			expr = new JRDesignExpression();
			expr.setText("$F{carga.canal.canal}.name().equals(\"VERMELHO\")");
			textField.setPrintWhenExpression(expr);
			fDetail.addElement(textField);
			textField = new JRDesignTextField();
			textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CELULA);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setForecolor(Color.BLACK);
			expr = new JRDesignExpression();
			expr.setText("$R{VERDE}.toUpperCase()");
			textField.setExpression(expr);
			expr = new JRDesignExpression();
			expr.setText("$F{carga.canal.canal}.name().equals(\"VERDE\")");
			textField.setPrintWhenExpression(expr);
			fDetail.addElement(textField);
			textField = new JRDesignTextField();
			textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CELULA);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setForecolor(Color.BLACK);
			expr = new JRDesignExpression();
			expr.setText("$R{CINZA}.toUpperCase()");
			textField.setExpression(expr);
			expr = new JRDesignExpression();
			expr.setText("$F{carga.canal.canal}.name().equals(\"CINZA\")");
			textField.setPrintWhenExpression(expr);
			fDetail.addElement(textField);
			textField = new JRDesignTextField();
			textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CELULA);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setForecolor(Color.BLACK);
			expr = new JRDesignExpression();
			expr.setText("$R{AMARELO}.toUpperCase()");
			textField.setExpression(expr);
			expr = new JRDesignExpression();
			expr.setText("$F{carga.canal.canal}.name().equals(\"AMARELO\")");
			textField.setPrintWhenExpression(expr);
			fDetail.addElement(textField);
			textField = new JRDesignTextField();
			textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
			textField.setBlankWhenNull(true);
			textField.setX(x);
			textField.setY(0);
			textField.setWidth(tamanho);
			textField.setHeight(ALTURA_CELULA);
			textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			textField.setForecolor(Color.BLACK);
			expr = new JRDesignExpression();
			expr.setText("");
			textField.setExpression(expr);
			expr = new JRDesignExpression();
			expr.setText("$F{carga.canal.canal} == (null)");
			textField.setPrintWhenExpression(expr);
			fDetail.addElement(textField);

			x = x + tamanho;
		}

		// TOTAL DIAS ESTIMADO
		if (totalDiasEst || selecioneTodos) {
			tamanho = 50;
			campo = new JRDesignField();
			textField = new JRDesignTextField();

			adicionarCampoInteiro(campo, "followUp.totalDiasEstimado",
					"lblTlEst", design, textField, tamanho, x, expr,
					bandColHeader, fDetail);

			x += tamanho;

		}

		if (totalDiasReal || selecioneTodos) {
			tamanho = 50;
			campo = new JRDesignField();
			textField = new JRDesignTextField();

			adicionarCampoInteiro(campo, "followUp.totalDiasAtual",
					"lblTlReal", design, textField, tamanho, x, expr,
					bandColHeader, fDetail);

			x += tamanho;
		}

		// seta o tamanho da pagina do relatorio.
		fDetail.setWidth(x);
		bandDetail.addElement(fDetail);
		
		design.setColumnHeader(bandColHeader);

		((JRDesignSection) design.getDetailSection()).addBand(bandDetail);

		// COMPILA O REPORT
		JasperReport report = JasperCompileManager.compileReport(design);
		JRDataSource jrds = new JRBeanCollectionDataSource(listaRelatorio);

		parametros.put("logo", servletContext
				.getRealPath("/pub/img/logo_lom.gif"));

		String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		Calendar c1 = Calendar.getInstance(); // today

		// parametros
		parametros.put("titulo", parametros.get("titulo").toString() + " "
				+ sdf.format(c1.getTime()));
		
		print = JasperFillManager.fillReport(report, parametros, jrds);

	}

	/**
	 * Adicionar campo BigDecimal com 3 de scala.
	 * 
	 * @param campo
	 * @param nomeCampo
	 * @param label
	 * @param design
	 * @param textField
	 * @param tamanho
	 * @param x
	 * @param expr
	 * @param bandColHeader
	 * @param fDetail
	 * @throws Exception
	 */
	public void adicionarCampoPeso(JRDesignField campo, String nomeCampo,
			String label, JasperDesign design, JRDesignTextField textField,
			Integer tamanho, Integer x, JRDesignExpression expr,
			JRDesignBand bandColHeader, JRDesignFrame fDetail) throws Exception {

		// tamanho = 200;
		campo = new JRDesignField();
		campo.setName(nomeCampo);
		campo.setValueClass(BigDecimal.class);
		design.addField(campo);

		textField = new JRDesignTextField();
		textField.setBlankWhenNull(true);
		textField.setX(x);

		textField.setY(0);
		textField.setWidth(tamanho);
		textField.setHeight(ALTURA_CABECALHO);
		textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
		textField.setBackcolor( backColor );
		textField.setForecolor(new Color(255, 255, 255));
		textField.setBold(true);
		textField.setFontSize(10);
		expr = new JRDesignExpression();
		expr.setText("$R{" + label + "}");
		textField.setExpression(expr);
		bandColHeader.addElement(textField);

		textField = new JRDesignTextField();
		textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
		textField.setBlankWhenNull(true);
		textField.setX(x);
		textField.setY(0);
		textField.setWidth(tamanho);
		textField.setHeight(ALTURA_CELULA);
		textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
		textField.setForecolor(Color.BLACK);
		textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
		expr = new JRDesignExpression();
		expr.setText("$F{" + nomeCampo
				+ "}.setScale(3, RoundingMode.UNNECESSARY)");

		textField.setExpression(expr);
		fDetail.addElement(textField);

	}

	/**
	 * Adicionar campo BigDecimal com 3 de scala.
	 * 
	 * @param campo
	 * @param nomeCampo
	 * @param label
	 * @param design
	 * @param textField
	 * @param tamanho
	 * @param x
	 * @param expr
	 * @param bandColHeader
	 * @param fDetail
	 * @throws Exception
	 */
	public void adicionarCampoMonetario(JRDesignField campo, String nomeCampo,
			String label, JasperDesign design, JRDesignTextField textField,
			Integer tamanho, Integer x, JRDesignExpression expr,
			JRDesignBand bandColHeader, JRDesignFrame fDetail) throws Exception {

		// tamanho = 200;
		campo = new JRDesignField();
		campo.setName(nomeCampo);
		campo.setValueClass(BigDecimal.class);
		design.addField(campo);

		textField = new JRDesignTextField();
		textField.setBlankWhenNull(true);
		textField.setX(x);
		textField.setY(0);
		textField.setWidth(tamanho);
		textField.setHeight(ALTURA_CABECALHO);
		textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
		textField.setBackcolor( backColor );
		textField.setForecolor(new Color(255, 255, 255));
		textField.setBold(true);
		textField.setFontSize(10);
		expr = new JRDesignExpression();
		expr.setText("$R{" + label + "}");
		textField.setExpression(expr);
		bandColHeader.addElement(textField);

		textField = new JRDesignTextField();
		textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
		textField.setBlankWhenNull(true);
		textField.setX(x);
		textField.setY(0);
		textField.setWidth(tamanho);
		textField.setHeight(ALTURA_CELULA);
		textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
		textField.setForecolor(Color.BLACK);
		textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
		expr = new JRDesignExpression();
		expr.setText("$F{" + nomeCampo
				+ "}.setScale(2, RoundingMode.UNNECESSARY)");

		textField.setExpression(expr);
		fDetail.addElement(textField);

	}

	/**
	 * Adicionar campo BigDecimal com 3 de scala.
	 * 
	 * @param campo
	 * @param nomeCampo
	 * @param label
	 * @param design
	 * @param textField
	 * @param tamanho
	 * @param x
	 * @param expr
	 * @param bandColHeader
	 * @param fDetail
	 * @throws Exception
	 */
	public void adicionarCampoString(JRDesignField campo, String nomeCampo,
			String label, JasperDesign design, JRDesignTextField textField,
			Integer tamanho, Integer x, JRDesignExpression expr,
			JRDesignBand bandColHeader, JRDesignFrame fDetail) throws Exception {

		campo = new JRDesignField();
		campo.setName(nomeCampo);
		campo.setValueClass(String.class);
		design.addField(campo);

		textField = new JRDesignTextField();
		textField.setBlankWhenNull(true);
		textField.setX(x);
		textField.setY(0);
		textField.setWidth(tamanho);
		textField.setHeight(ALTURA_CABECALHO);
		textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
		textField.setBackcolor( backColor );
		textField.setForecolor(new Color(255, 255, 255));
		textField.setBold(true);
		textField.setFontSize(10);
		expr = new JRDesignExpression();
		expr.setText("$R{" + label + "}");
		textField.setExpression(expr);
		bandColHeader.addElement(textField);

		textField = new JRDesignTextField();
		textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
		textField.setStretchWithOverflow(true);
		textField.setBlankWhenNull(true);
		textField.setX(x);
		textField.setY(0);
		textField.setWidth(tamanho);
		textField.setHeight(ALTURA_CELULA);
		textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
		textField.setForecolor(Color.BLACK);
		textField.setStretchWithOverflow(true);
		textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
		expr = new JRDesignExpression();
		expr.setText("$F{" + nomeCampo + "}.toUpperCase()");

		textField.setExpression(expr);
		fDetail.addElement(textField);

	}

	/**
	 * Adicionar campo BigDecimal com 3 de scala.
	 * 
	 * @param campo
	 * @param nomeCampo
	 * @param label
	 * @param design
	 * @param textField
	 * @param tamanho
	 * @param x
	 * @param expr
	 * @param bandColHeader
	 * @param fDetail
	 * @throws Exception
	 */
	public void adicionarCampoInteiro(JRDesignField campo, String nomeCampo,
			String label, JasperDesign design, JRDesignTextField textField,
			Integer tamanho, Integer x, JRDesignExpression expr,
			JRDesignBand bandColHeader, JRDesignFrame fDetail) throws Exception {

		// tamanho = 200;
		campo = new JRDesignField();
		campo.setName(nomeCampo);
		campo.setValueClass(Integer.class);
		design.addField(campo);

		textField = new JRDesignTextField();
		textField.setBlankWhenNull(true);
		textField.setX(x);
		textField.setY(0);
		textField.setWidth(tamanho);
		textField.setHeight(ALTURA_CABECALHO);
		textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
		textField.setBackcolor( backColor );
		textField.setForecolor(new Color(255, 255, 255));
		textField.setBold(true);
		textField.setFontSize(10);
		expr = new JRDesignExpression();
		expr.setText("$R{" + label + "}");
		textField.setExpression(expr);
		bandColHeader.addElement(textField);

		textField = new JRDesignTextField();
		textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
		textField.setBlankWhenNull(true);
		textField.setX(x);
		textField.setY(0);
		textField.setWidth(tamanho);
		textField.setHeight(ALTURA_CELULA);
		textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
		textField.setForecolor(Color.BLACK);
		textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
		expr = new JRDesignExpression();
		expr.setText("$F{" + nomeCampo + "}");

		textField.setExpression(expr);
		fDetail.addElement(textField);

	}

	public void adicionarCampoData(JRDesignField campo, String nomeCampo,
			String label, JasperDesign design, JRDesignTextField textField,
			Integer tamanho, Integer x, JRDesignExpression expr,
			JRDesignBand bandColHeader, JRDesignFrame fDetail) throws Exception {

		campo = new JRDesignField();
		campo.setName(nomeCampo);
		campo.setValueClass(Date.class);
		design.addField(campo);

		textField = new JRDesignTextField();
		textField.setBlankWhenNull(true);
		textField.setX(x);
		textField.setY(0);
		textField.setWidth(TAMANHO_CAMPO_DATA);
		textField.setHeight(ALTURA_CABECALHO);
		textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
		textField.setBackcolor( backColor );
		textField.setForecolor(new Color(255, 255, 255));
		textField.setBold(true);
		textField.setFontSize(10);
		expr = new JRDesignExpression();
		expr.setText("$R{" + label + "}");
		textField.setExpression(expr);
		bandColHeader.addElement(textField);

		textField = new JRDesignTextField();
		textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
		textField.setBlankWhenNull(true);
		textField.setX(x);
		textField.setY(0);
		textField.setWidth(tamanho);
		textField.setHeight(ALTURA_CELULA);
		textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
		textField.setForecolor(Color.BLACK);
		textField.setPattern("dd/MM/yyyy");
		expr = new JRDesignExpression();
		expr.setText("$F{" + nomeCampo + "}");
		textField.setExpression(expr);
		fDetail.addElement(textField);

	}

	public void adicionarCampoBoolean(JRDesignField campo, String nomeCampo,
			String label, String labelVerdadeiro, String labelFalso,
			JasperDesign design, JRDesignTextField textField, Integer tamanho,
			Integer x, JRDesignExpression expr, JRDesignBand bandColHeader,
			JRDesignFrame fDetail) throws Exception {

		campo = new JRDesignField();
		campo.setName(nomeCampo);
		campo.setValueClass(Boolean.class);
		design.addField(campo);

		textField = new JRDesignTextField();
		textField.setBlankWhenNull(true);
		textField.setX(x);
		textField.setY(0);
		textField.setWidth(tamanho);
		textField.setHeight(ALTURA_CABECALHO);
		textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
		textField.setBackcolor( backColor );
		textField.setForecolor(new Color(255, 255, 255));
		textField.setBold(true);
		textField.setFontSize(10);
		expr = new JRDesignExpression();
		expr.setText("$R{" + label + "}");
		textField.setExpression(expr);
		bandColHeader.addElement(textField);

		textField = new JRDesignTextField();
		textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
		textField.setBlankWhenNull(true);
		textField.setX(x);
		textField.setY(0);
		textField.setWidth(tamanho);
		textField.setHeight(ALTURA_CELULA);
		textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
		textField.setForecolor(Color.BLACK);
		expr = new JRDesignExpression();
		expr.setText("$R{" + labelVerdadeiro + "}.toUpperCase()");
		textField.setExpression(expr);
		expr = new JRDesignExpression();
		expr.setText("$F{" + nomeCampo + "}");
		textField.setPrintWhenExpression(expr);
		fDetail.addElement(textField);

		textField = new JRDesignTextField();
		textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
		textField.setBlankWhenNull(true);
		textField.setX(x);
		textField.setY(0);
		textField.setWidth(tamanho);
		textField.setHeight(ALTURA_CELULA);
		textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
		textField.setForecolor(Color.BLACK);
		expr = new JRDesignExpression();
		expr.setText("$R{" + labelFalso + "}.toUpperCase()");
		textField.setExpression(expr);
		expr = new JRDesignExpression();
		expr.setText("!$F{" + nomeCampo + "}");
		textField.setPrintWhenExpression(expr);
		fDetail.addElement(textField);

	}

	/**
	 * @param campo
	 * @param nomeCampo
	 * @param label
	 * @param design
	 * @param textField
	 * @param tamanho
	 * @param x
	 * @param expr
	 * @param bandColHeader
	 * @param fDetail
	 * @throws Exception
	 */
	public void adicionarCampoBoolean(JRDesignField campo, String nomeCampo,
			String label, JasperDesign design, JRDesignTextField textField,
			Integer tamanho, Integer x, JRDesignExpression expr,
			JRDesignBand bandColHeader, JRDesignFrame fDetail) throws Exception {

		adicionarCampoBoolean(campo, nomeCampo, label, "lblSim", "lblNao",
				design, textField, tamanho, x, expr, bandColHeader, fDetail);

	}

	public void gerarXLSPedido(List<T> listaRelatorio,
			Map<String, Object> parametros, String relatorio, String subreport,
			String fileName) throws Exception {

		FacesContext facesContext = FacesContext.getCurrentInstance();

		parametros.put("tipo", "xls");
		montarRelatorioPedido(listaRelatorio, facesContext, parametros,
				relatorio, subreport);

		HttpServletResponse response = (HttpServletResponse) facesContext
				.getExternalContext().getResponse();

		ServletOutputStream output;
		try {
			output = response.getOutputStream();
			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ fileName + ".xls");

			JRXlsExporter xls = new JRXlsExporter();

			xls.setParameter(JRXlsExporterParameter.JASPER_PRINT, print);
			xls.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, output);
			xls.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE,
					Boolean.FALSE);
			xls.setParameter(JRXlsExporterParameter.MAXIMUM_ROWS_PER_SHEET,
					Integer.decode("65000"));
			xls.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,
					Boolean.TRUE);
			xls
					.setParameter(
							JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,
							Boolean.TRUE);
			xls.setParameter(
					JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
					Boolean.TRUE);
			xls.exportReport();
			output.flush();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JRException e) {
			e.printStackTrace();
		} finally {
			facesContext.responseComplete();
		}
	}

	/**
	 * Monta o relatorio de Pedido Nacional / Local
	 * 
	 * @param listaRelatorio
	 * @param facesContext
	 * @param parametros
	 * @param relatorio
	 * @param subreport
	 * @throws Exception
	 */
	private void montarRelatorioPedido(List<T> listaRelatorio,
			FacesContext facesContext, Map<String, Object> parametros,
			String relatorio, String subreport) throws Exception {/*

		int x = 0;

		ServletContext servletContext = (ServletContext) facesContext
				.getExternalContext().getContext();
		String pathRel = servletContext.getRealPath(relatorio);

		// carregar o JRXML
		JasperDesign design = JRXmlLoader.load(pathRel);

		// APENAS SE EXCEL
		if (!parametros.get("tipo").equals("pdf")) {
			design.setIgnorePagination(true);
		}

		// TIPO DE RELATORIO: PDF ou EXCEL
		JRDesignParameter parameter = new JRDesignParameter();

		parameter = new JRDesignParameter();
		parameter.setName("SUBREPORT_DIR");
		parameter.setValueClass(java.lang.String.class);
		design.addParameter(parameter);

		// ATRIBUTOS
		JRDesignField campo;
		JRDesignTextField textField;

		// BANDS
		// CABECALHO
		JRDesignBand bandColHeader = new JRDesignBand();
		bandColHeader.setHeight(ALTURA_CABECALHO);
		JRDesignExpression expr = new JRDesignExpression();
		expr
				.setText("$V{PAGE_NUMBER}.intValue() == 1 || $P{tipo}.equals(\"pdf\")");
		bandColHeader.setPrintWhenExpression(expr);

		// DETAIL
		JRDesignBand bandDetail = new JRDesignBand();
		bandDetail.setHeight(ALTURA_CELULA);
		JRDesignFrame fDetail = new JRDesignFrame();
		fDetail.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
		fDetail.setX(0);
		fDetail.setY(0);
		fDetail.setHeight(ALTURA_CELULA);
		fDetail.setMode(ModeEnum.OPAQUE);

		int tamanho = 0;
		// TIPO: NACIONAL / LOCAL / IMPORTAÇÃO
		tamanho = 70;
		campo = new JRDesignField();
		campo.setName("pedido.tipoTransPedido");
		campo.setValueClass(TipoTransportePedido.class);
		design.addField(campo);

		textField = new JRDesignTextField();
		textField.setBlankWhenNull(true);
		textField.setX(x);
		textField.setY(0);
		textField.setWidth(tamanho);
		textField.setHeight(ALTURA_CABECALHO);
		textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
		textField.setBackcolor( backColor );
		textField.setForecolor(new Color(255, 255, 255));
		textField.setBold(true);
		textField.setFontSize(10);
		expr = new JRDesignExpression();
		expr.setText("$R{lblTipo}");
		textField.setExpression(expr);
		bandColHeader.addElement(textField);

		textField = new JRDesignTextField();
		textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
		textField.setBlankWhenNull(true);
		textField.setX(x);
		textField.setY(0);
		textField.setWidth(tamanho);
		textField.setHeight(ALTURA_CELULA);
		textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
		textField.setForecolor(Color.BLACK);
		expr = new JRDesignExpression();
		expr.setText("$R{N}.toUpperCase()");
		textField.setExpression(expr);
		expr = new JRDesignExpression();
		expr.setText("$F{pedido.tipoTransPedido}.name().equals(\"N\")");
		textField.setPrintWhenExpression(expr);
		fDetail.addElement(textField);

		textField = new JRDesignTextField();
		textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
		textField.setBlankWhenNull(true);
		textField.setX(x);
		textField.setY(0);
		textField.setWidth(tamanho);
		textField.setHeight(ALTURA_CELULA);
		textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
		textField.setForecolor(Color.BLACK);
		expr = new JRDesignExpression();
		expr.setText("$R{L}.toUpperCase()");
		textField.setExpression(expr);
		expr = new JRDesignExpression();
		expr.setText("$F{pedido.tipoTransPedido}.name().equals(\"L\")");
		textField.setPrintWhenExpression(expr);
		fDetail.addElement(textField);

		textField = new JRDesignTextField();
		textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
		textField.setBlankWhenNull(true);
		textField.setX(x);
		textField.setY(0);
		textField.setWidth(tamanho);
		textField.setHeight(ALTURA_CELULA);
		textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
		textField.setForecolor(Color.BLACK);
		expr = new JRDesignExpression();
		expr.setText("$R{I}.toUpperCase()");
		textField.setExpression(expr);
		expr = new JRDesignExpression();
		expr.setText("$F{pedido.tipoTransPedido}.name().equals(\"I\")");
		textField.setPrintWhenExpression(expr);
		fDetail.addElement(textField);

		x = x + tamanho;

		// DATA CADASTRO
		tamanho = 80;
		campo = new JRDesignField();
		textField = new JRDesignTextField();
		adicionarCampoData(campo, "pedido.dtCadastro", "lblDataCadastro",
				design, textField, tamanho, x, expr, bandColHeader, fDetail);
		x += tamanho;

		// Tipo Pedido
		tamanho = 80;
		campo = new JRDesignField();
		campo.setName("pedido.tipoPedido");
		campo.setValueClass(TipoPedido.class);
		design.addField(campo);

		textField = new JRDesignTextField();
		textField.setBlankWhenNull(true);
		textField.setX(x);
		textField.setY(0);
		textField.setWidth(tamanho);
		textField.setHeight(ALTURA_CABECALHO);
		textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
		textField.setBackcolor( backColor );
		textField.setForecolor(new Color(255, 255, 255));
		textField.setBold(true);
		textField.setFontSize(10);
		expr = new JRDesignExpression();
		expr.setText("$R{lblTipoPedido}");
		textField.setExpression(expr);
		bandColHeader.addElement(textField);

		textField = new JRDesignTextField();
		textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
		textField.setBlankWhenNull(true);
		textField.setX(x);
		textField.setY(0);
		textField.setWidth(tamanho);
		textField.setHeight(ALTURA_CELULA);
		textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
		textField.setForecolor(Color.BLACK);
		expr = new JRDesignExpression();
		expr.setText("$R{MA}.toUpperCase()");
		textField.setExpression(expr);
		expr = new JRDesignExpression();
		expr.setText("$F{pedido.tipoPedido}.name().equals(\"MA\")");
		textField.setPrintWhenExpression(expr);
		fDetail.addElement(textField);

		textField = new JRDesignTextField();
		textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
		textField.setBlankWhenNull(true);
		textField.setX(x);
		textField.setY(0);
		textField.setWidth(tamanho);
		textField.setHeight(ALTURA_CELULA);
		textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
		textField.setForecolor(Color.BLACK);
		expr = new JRDesignExpression();
		expr.setText("$R{SE}.toUpperCase()");
		textField.setExpression(expr);
		expr = new JRDesignExpression();
		expr.setText("$F{pedido.tipoPedido}.name().equals(\"SE\")");
		textField.setPrintWhenExpression(expr);
		fDetail.addElement(textField);

		x = x + tamanho;

		// ESTADO
		tamanho = 200;
		campo = new JRDesignField();
		textField = new JRDesignTextField();
		adicionarCampoString(campo, "pedido.cidadeOrigem.estado.nome",
				"lblEstado", design, textField, tamanho, x, expr,
				bandColHeader, fDetail);
		x += tamanho;

		// CIDADE
		tamanho = 200;
		campo = new JRDesignField();
		textField = new JRDesignTextField();
		adicionarCampoString(campo, "pedido.cidadeOrigem.nome",
				"lblCidadeOrigem", design, textField, tamanho, x, expr,
				bandColHeader, fDetail);
		x += tamanho;

		// CRITICO
		campo = new JRDesignField();
		textField = new JRDesignTextField();
		tamanho = 70;
		adicionarCampoBoolean(campo, "pedido.critico", "lblCritico", design,
				textField, tamanho, x, expr, bandColHeader, fDetail);
		x += tamanho;

		// Descricao
		tamanho = 250;
		campo = new JRDesignField();
		textField = new JRDesignTextField();
		adicionarCampoString(campo, "pedido.descricao", "lblObservacao",
				design, textField, tamanho, x, expr, bandColHeader, fDetail);
		x += tamanho;

		// Motivo Atraso
		tamanho = 200;
		campo = new JRDesignField();
		textField = new JRDesignTextField();
		adicionarCampoString(campo, "pedido.motivoAtraso", "lblMotivoAtraso",
				design, textField, tamanho, x, expr, bandColHeader, fDetail);
		x += tamanho;

		// COTACAO
		// Processo
		tamanho = 90;
		campo = new JRDesignField();
		textField = new JRDesignTextField();
		adicionarCampoString(campo, "pedido.sequencial", "lblCotacao", design,
				textField, tamanho, x, expr, bandColHeader, fDetail);
		x += tamanho;

		// Data Recebimento
		tamanho = TAMANHO_CAMPO_DATA;
		campo = new JRDesignField();
		textField = new JRDesignTextField();
		adicionarCampoData(campo, "pedido.dtRecebimentoCotacao",
				"lblRecebimentoCO", design, textField, tamanho, x, expr,
				bandColHeader, fDetail);
		x += tamanho;

		// Data Prev. Conclusao
		tamanho = TAMANHO_CAMPO_DATA;
		campo = new JRDesignField();
		textField = new JRDesignTextField();
		adicionarCampoData(campo, "pedido.dtPrevConclusao", "lblPrevConclusao",
				design, textField, tamanho, x, expr, bandColHeader, fDetail);
		x += tamanho;

		// Data Prev. Conclusao2
		tamanho = TAMANHO_CAMPO_DATA;
		campo = new JRDesignField();
		textField = new JRDesignTextField();
		adicionarCampoData(campo, "pedido.dtPrevConclusao2",
				"lblPrevConclusao2", design, textField, tamanho, x, expr,
				bandColHeader, fDetail);
		x += tamanho;

		// Data REAL Conclusao
		tamanho = TAMANHO_CAMPO_DATA;
		campo = new JRDesignField();
		textField = new JRDesignTextField();
		adicionarCampoData(campo, "pedido.dtRealConclusao", "lblRealConclusao",
				design, textField, tamanho, x, expr, bandColHeader, fDetail);
		x += tamanho;

		// REQUISITANTE
		tamanho = 150;
		campo = new JRDesignField();
		textField = new JRDesignTextField();
		adicionarCampoString(campo, "pedido.requisitante", "lblRequisitante",
				design, textField, tamanho, x, expr, bandColHeader, fDetail);
		x += tamanho;

		// RESPONSAVEL
		tamanho = 200;
		campo = new JRDesignField();
		textField = new JRDesignTextField();
		adicionarCampoString(campo, "pedido.compradorResponsavel.nome",
				"lblResponsavel", design, textField, tamanho, x, expr,
				bandColHeader, fDetail);
		x += tamanho;

		// PROJETO
		tamanho = 150;
		campo = new JRDesignField();
		textField = new JRDesignTextField();
		adicionarCampoString(campo, "pedido.projeto.nome", "lblProjeto",
				design, textField, tamanho, x, expr, bandColHeader, fDetail);
		x += tamanho;

		// DEPARTAMENTO
		tamanho = 150;
		campo = new JRDesignField();
		textField = new JRDesignTextField();
		adicionarCampoString(campo, "pedido.dpm.descricao", "lblDepartamento",
				design, textField, tamanho, x, expr, bandColHeader, fDetail);
		x += tamanho;

		// STATUS
		tamanho = 90;
		campo = new JRDesignField();
		campo.setName("pedido.statusCotacao");
		campo.setValueClass(StatusPedido.class);
		design.addField(campo);

		textField = new JRDesignTextField();
		textField.setBlankWhenNull(true);
		textField.setX(x);
		textField.setY(0);
		textField.setWidth(tamanho);
		textField.setHeight(ALTURA_CABECALHO);
		textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
		textField.setBackcolor( backColor );
		textField.setForecolor(new Color(255, 255, 255));
		textField.setBold(true);
		textField.setFontSize(10);
		expr = new JRDesignExpression();
		expr.setText("$R{lblStatusCot}");
		textField.setExpression(expr);
		bandColHeader.addElement(textField);

		textField = new JRDesignTextField();
		textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
		textField.setBlankWhenNull(true);
		textField.setX(x);
		textField.setY(0);
		textField.setWidth(tamanho);
		textField.setHeight(ALTURA_CELULA);
		textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
		textField.setForecolor(Color.BLACK);
		expr = new JRDesignExpression();
		expr.setText("$R{EA_P}.toUpperCase()");
		textField.setExpression(expr);
		expr = new JRDesignExpression();
		expr.setText("$F{pedido.statusCotacao}.name().equals(\"EA\")");
		textField.setPrintWhenExpression(expr);
		fDetail.addElement(textField);

		textField = new JRDesignTextField();
		textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
		textField.setBlankWhenNull(true);
		textField.setX(x);
		textField.setY(0);
		textField.setWidth(tamanho);
		textField.setHeight(ALTURA_CELULA);
		textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
		textField.setForecolor(Color.BLACK);
		expr = new JRDesignExpression();
		expr.setText("$R{AP}.toUpperCase()");
		textField.setExpression(expr);
		expr = new JRDesignExpression();
		expr.setText("$F{pedido.statusCotacao}.name().equals(\"AP\")");
		textField.setPrintWhenExpression(expr);
		fDetail.addElement(textField);

		textField = new JRDesignTextField();
		textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
		textField.setBlankWhenNull(true);
		textField.setX(x);
		textField.setY(0);
		textField.setWidth(tamanho);
		textField.setHeight(ALTURA_CELULA);
		textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
		textField.setForecolor(Color.BLACK);
		expr = new JRDesignExpression();
		expr.setText("$R{FAR}.toUpperCase()");
		textField.setExpression(expr);
		expr = new JRDesignExpression();
		expr.setText("$F{pedido.statusCotacao}.name().equals(\"FAR\")");
		textField.setPrintWhenExpression(expr);
		fDetail.addElement(textField);

		textField = new JRDesignTextField();
		textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
		textField.setBlankWhenNull(true);
		textField.setX(x);
		textField.setY(0);
		textField.setWidth(tamanho);
		textField.setHeight(ALTURA_CELULA);
		textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
		textField.setForecolor(Color.BLACK);
		expr = new JRDesignExpression();
		expr.setText("$R{F}.toUpperCase()");
		textField.setExpression(expr);
		expr = new JRDesignExpression();
		expr.setText("$F{pedido.statusCotacao}.name().equals(\"F\")");
		textField.setPrintWhenExpression(expr);
		fDetail.addElement(textField);

		textField = new JRDesignTextField();
		textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
		textField.setBlankWhenNull(true);
		textField.setX(x);
		textField.setY(0);
		textField.setWidth(tamanho);
		textField.setHeight(ALTURA_CELULA);
		textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
		textField.setForecolor(Color.BLACK);
		expr = new JRDesignExpression();
		expr.setText("$R{E}.toUpperCase()");
		textField.setExpression(expr);
		expr = new JRDesignExpression();
		expr.setText("$F{pedido.statusCotacao}.name().equals(\"E\")");
		textField.setPrintWhenExpression(expr);
		fDetail.addElement(textField);

		textField = new JRDesignTextField();
		textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
		textField.setBlankWhenNull(true);
		textField.setX(x);
		textField.setY(0);
		textField.setWidth(tamanho);
		textField.setHeight(ALTURA_CELULA);
		textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
		textField.setForecolor(Color.BLACK);
		expr = new JRDesignExpression();
		expr.setText("$R{PR}.toUpperCase()");
		textField.setExpression(expr);
		expr = new JRDesignExpression();
		expr.setText("$F{pedido.statusCotacao}.name().equals(\"PR\")");
		textField.setPrintWhenExpression(expr);
		fDetail.addElement(textField);

		textField = new JRDesignTextField();
		textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
		textField.setBlankWhenNull(true);
		textField.setX(x);
		textField.setY(0);
		textField.setWidth(tamanho);
		textField.setHeight(ALTURA_CELULA);
		textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
		textField.setForecolor(Color.BLACK);
		expr = new JRDesignExpression();
		expr.setText("$R{CA}.toUpperCase()");
		textField.setExpression(expr);
		expr = new JRDesignExpression();
		expr.setText("$F{pedido.statusCotacao}.name().equals(\"C\")");
		textField.setPrintWhenExpression(expr);
		fDetail.addElement(textField);

		x += tamanho;

		// OBSERVACAO COTACAO
		tamanho = 450;
		campo = new JRDesignField();
		textField = new JRDesignTextField();
		textField.setStretchWithOverflow(true);
		textField.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
		adicionarCampoString(campo, "pedido.observacaoCotacao",
				"lblObservacaoCO", design, textField, tamanho, x, expr,
				bandColHeader, fDetail);
		x += tamanho;

		// PR
		// Number
		tamanho = 90;
		campo = new JRDesignField();
		textField = new JRDesignTextField();
		adicionarCampoString(campo, "pedido.numeroRequisicao", "lblPr", design,
				textField, tamanho, x, expr, bandColHeader, fDetail);
		x += tamanho;

		// DATA RECEBIMENTO PR
		tamanho = TAMANHO_CAMPO_DATA;
		campo = new JRDesignField();
		textField = new JRDesignTextField();
		adicionarCampoData(campo, "pedido.dtRecebimento", "lblRecebimentoPr",
				design, textField, tamanho, x, expr, bandColHeader, fDetail);
		x += tamanho;

		// DATA Prevista PR
		tamanho = TAMANHO_CAMPO_DATA;
		campo = new JRDesignField();
		textField = new JRDesignTextField();
		adicionarCampoData(campo, "pedido.dtPrevistaEmissao",
				"lblPrevEmissaoPR", design, textField, tamanho, x, expr,
				bandColHeader, fDetail);
		x += tamanho;

		// STATUS PR
		tamanho = 90;
		campo = new JRDesignField();
		campo.setName("pedido.statusPr");
		campo.setValueClass(StatusPedido.class);
		design.addField(campo);

		textField = new JRDesignTextField();
		textField.setBlankWhenNull(true);
		textField.setX(x);
		textField.setY(0);
		textField.setWidth(tamanho);
		textField.setHeight(ALTURA_CABECALHO);
		textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
		textField.setBackcolor( backColor );
		textField.setForecolor(new Color(255, 255, 255));
		textField.setBold(true);
		textField.setFontSize(10);
		expr = new JRDesignExpression();
		expr.setText("$R{lblStatusPR}");
		textField.setExpression(expr);
		bandColHeader.addElement(textField);

		textField = new JRDesignTextField();
		textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
		textField.setBlankWhenNull(true);
		textField.setX(x);
		textField.setY(0);
		textField.setWidth(tamanho);
		textField.setHeight(ALTURA_CELULA);
		textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
		textField.setForecolor(Color.BLACK);
		expr = new JRDesignExpression();
		expr.setText("$R{PRA}.toUpperCase()");
		textField.setExpression(expr);
		expr = new JRDesignExpression();
		expr.setText("$F{pedido.statusPr}.name().equals(\"PRA\")");
		textField.setPrintWhenExpression(expr);
		fDetail.addElement(textField);

		textField = new JRDesignTextField();
		textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
		textField.setBlankWhenNull(true);
		textField.setX(x);
		textField.setY(0);
		textField.setWidth(tamanho);
		textField.setHeight(ALTURA_CELULA);
		textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
		textField.setForecolor(Color.BLACK);
		expr = new JRDesignExpression();
		expr.setText("$R{PRC}.toUpperCase()");
		textField.setExpression(expr);
		expr = new JRDesignExpression();
		expr.setText("$F{pedido.statusPr}.name().equals(\"PRC\")");
		textField.setPrintWhenExpression(expr);
		fDetail.addElement(textField);

		textField = new JRDesignTextField();
		textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
		textField.setBlankWhenNull(true);
		textField.setX(x);
		textField.setY(0);
		textField.setWidth(tamanho);
		textField.setHeight(ALTURA_CELULA);
		textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
		textField.setForecolor(Color.BLACK);
		expr = new JRDesignExpression();
		expr.setText("$R{AP}.toUpperCase()");
		textField.setExpression(expr);
		expr = new JRDesignExpression();
		expr.setText("$F{pedido.statusPr}.name().equals(\"AP\")");
		textField.setPrintWhenExpression(expr);
		fDetail.addElement(textField);

		textField = new JRDesignTextField();
		textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
		textField.setBlankWhenNull(true);
		textField.setX(x);
		textField.setY(0);
		textField.setWidth(tamanho);
		textField.setHeight(ALTURA_CELULA);
		textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
		textField.setForecolor(Color.BLACK);
		expr = new JRDesignExpression();
		expr.setText("$R{PE}.toUpperCase()");
		textField.setExpression(expr);
		expr = new JRDesignExpression();
		expr.setText("$F{pedido.statusPr}.name().equals(\"PE\")");
		textField.setPrintWhenExpression(expr);
		fDetail.addElement(textField);

		x = x + tamanho;

		// DESCRICAO PR
		tamanho = 150;
		campo = new JRDesignField();
		textField = new JRDesignTextField();
		adicionarCampoString(campo, "pedido.observacaoPr", "lblObservacaoPR",
				design, textField, tamanho, x, expr, bandColHeader, fDetail);
		x += tamanho;

		// PO
		tamanho = 90;
		campo = new JRDesignField();
		textField = new JRDesignTextField();
		adicionarCampoString(campo, "pedido.numeroPo", "lblPo", design,
				textField, tamanho, x, expr, bandColHeader, fDetail);
		x += tamanho;

		// DATA PO
		tamanho = TAMANHO_CAMPO_DATA;
		campo = new JRDesignField();
		textField = new JRDesignTextField();
		adicionarCampoData(campo, "pedido.dtPo", "lblDataPo", design,
				textField, tamanho, x, expr, bandColHeader, fDetail);
		x += tamanho;

		// PREVISAO APROVACAO E-FLOW
		tamanho = TAMANHO_CAMPO_DATA;
		campo = new JRDesignField();
		textField = new JRDesignTextField();
		adicionarCampoData(campo, "pedido.dtPrevistaEFlow",
				"lblPrevisaoAprovEFlow", design, textField, tamanho, x, expr,
				bandColHeader, fDetail);
		x += tamanho;

		// APROVACAO E-FLOW
		tamanho = TAMANHO_CAMPO_DATA;
		campo = new JRDesignField();
		textField = new JRDesignTextField();
		adicionarCampoData(campo, "pedido.dtAprovacaoEFlow", "lblAprovEFlow",
				design, textField, tamanho, x, expr, bandColHeader, fDetail);
		x += tamanho;

		// PREV. ENTRADA
		tamanho = TAMANHO_CAMPO_DATA;
		campo = new JRDesignField();
		textField = new JRDesignTextField();
		adicionarCampoData(campo, "pedido.dtPrevistaEntrada", "lblPrevEntrada",
				design, textField, tamanho, x, expr, bandColHeader, fDetail);
		x += tamanho;

		// NOVA PREV. ENTRADA
		tamanho = TAMANHO_CAMPO_DATA;
		campo = new JRDesignField();
		textField = new JRDesignTextField();
		adicionarCampoData(campo, "pedido.dtPrevistaEntrada2",
				"lblNovaPrevEntrada", design, textField, tamanho, x, expr,
				bandColHeader, fDetail);
		x += tamanho;

		// REAL ENTRADA
		tamanho = TAMANHO_CAMPO_DATA;
		campo = new JRDesignField();
		textField = new JRDesignTextField();
		adicionarCampoData(campo, "pedido.dtRealEntrada", "lblEntrada", design,
				textField, tamanho, x, expr, bandColHeader, fDetail);
		x += tamanho;

		// FORNECEDOR
		tamanho = 150;
		campo = new JRDesignField();
		textField = new JRDesignTextField();
		adicionarCampoString(campo, "pedido.fornecedor.nomeFantasia",
				"lblFornecedor", design, textField, tamanho, x, expr,
				bandColHeader, fDetail);
		x += tamanho;

		// NF FORNECEDOR
		tamanho = 150;
		campo = new JRDesignField();
		textField = new JRDesignTextField();
		adicionarCampoString(campo, "pedido.numeroNotaFiscal",
				"lblNFFornecedor", design, textField, tamanho, x, expr,
				bandColHeader, fDetail);
		x += tamanho;

		// VALOR FRETE
		tamanho = 200;
		campo = new JRDesignField();
		textField = new JRDesignTextField();
		adicionarCampoMonetario(campo, "pedido.valorPo", "lblValorFrete", design,
				textField, tamanho, x, expr, bandColHeader, fDetail);
		x += tamanho;

		// Status
		tamanho = 90;
		campo = new JRDesignField();
		campo.setName("pedido.status");
		campo.setValueClass(StatusPedido.class);
		design.addField(campo);

		textField = new JRDesignTextField();
		textField.setBlankWhenNull(true);
		textField.setX(x);
		textField.setY(0);
		textField.setWidth(tamanho);
		textField.setHeight(ALTURA_CABECALHO);
		textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
		textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
		textField.setBackcolor( backColor );
		textField.setForecolor(new Color(255, 255, 255));
		textField.setBold(true);
		textField.setFontSize(10);
		expr = new JRDesignExpression();
		expr.setText("$R{lblStatusPO}");
		textField.setExpression(expr);
		bandColHeader.addElement(textField);

		textField = new JRDesignTextField();
		textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
		textField.setBlankWhenNull(true);
		textField.setX(x);
		textField.setY(0);
		textField.setWidth(tamanho);
		textField.setHeight(ALTURA_CELULA);
		textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
		textField.setForecolor(Color.BLACK);
		expr = new JRDesignExpression();
		expr.setText("$R{POP}.toUpperCase()");
		textField.setExpression(expr);
		expr = new JRDesignExpression();
		expr.setText("$F{pedido.status}.name().equals(\"POP\")");
		textField.setPrintWhenExpression(expr);
		fDetail.addElement(textField);

		textField = new JRDesignTextField();
		textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
		textField.setBlankWhenNull(true);
		textField.setX(x);
		textField.setY(0);
		textField.setWidth(tamanho);
		textField.setHeight(ALTURA_CELULA);
		textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
		textField.setForecolor(Color.BLACK);
		expr = new JRDesignExpression();
		expr.setText("$R{POA}.toUpperCase()");
		textField.setExpression(expr);
		expr = new JRDesignExpression();
		expr.setText("$F{pedido.status}.name().equals(\"POA\")");
		textField.setPrintWhenExpression(expr);
		fDetail.addElement(textField);

		textField = new JRDesignTextField();
		textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
		textField.setBlankWhenNull(true);
		textField.setX(x);
		textField.setY(0);
		textField.setWidth(tamanho);
		textField.setHeight(ALTURA_CELULA);
		textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
		textField.setForecolor(Color.BLACK);
		expr = new JRDesignExpression();
		expr.setText("$R{POC}.toUpperCase()");
		textField.setExpression(expr);
		expr = new JRDesignExpression();
		expr.setText("$F{pedido.status}.name().equals(\"POC\")");
		textField.setPrintWhenExpression(expr);
		fDetail.addElement(textField);

		textField = new JRDesignTextField();
		textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
		textField.setBlankWhenNull(true);
		textField.setX(x);
		textField.setY(0);
		textField.setWidth(tamanho);
		textField.setHeight(ALTURA_CELULA);
		textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
		textField.setForecolor(Color.BLACK);
		expr = new JRDesignExpression();
		expr.setText("$R{PRA}.toUpperCase()");
		textField.setExpression(expr);
		expr = new JRDesignExpression();
		expr.setText("$F{pedido.status}.name().equals(\"PRA\")");
		textField.setPrintWhenExpression(expr);
		fDetail.addElement(textField);

		textField = new JRDesignTextField();
		textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
		textField.setBlankWhenNull(true);
		textField.setX(x);
		textField.setY(0);
		textField.setWidth(tamanho);
		textField.setHeight(ALTURA_CELULA);
		textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
		textField.setForecolor(Color.BLACK);
		expr = new JRDesignExpression();
		expr.setText("$R{PF}.toUpperCase()");
		textField.setExpression(expr);
		expr = new JRDesignExpression();
		expr.setText("$F{pedido.status}.name().equals(\"PEF\")");
		textField.setPrintWhenExpression(expr);
		fDetail.addElement(textField);

		x += tamanho;

		// FORNECEDORES COTADOS
		tamanho = 200;
		campo = new JRDesignField();
		textField = new JRDesignTextField();
		adicionarCampoString(campo, "pedido.fornecedoresCotados",
				"lblFornecedorCotados", design, textField, tamanho, x, expr,
				bandColHeader, fDetail);
		x += tamanho;

		// OBSERVACAO
		tamanho = 450;
		campo = new JRDesignField();
		textField = new JRDesignTextField();
		adicionarCampoString(campo, "pedido.observacaoPo", "lblObservacaoPO",
				design, textField, tamanho, x, expr, bandColHeader, fDetail);
		textField.setStretchWithOverflow(true);
		textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
		x += tamanho;

		// seta o tamanho da pagina do relatorio.
		fDetail.setWidth(x);
		bandDetail.addElement(fDetail);

		design.setColumnHeader(bandColHeader);

		((JRDesignSection) design.getDetailSection()).addBand(bandDetail);

		// COMPILA O REPORT
		JasperReport report = JasperCompileManager.compileReport(design);
		JRDataSource jrds = new JRBeanCollectionDataSource(listaRelatorio);

		parametros.put("logo", servletContext
				.getRealPath("/pub/img/logo_lom.gif"));

		print = JasperFillManager.fillReport(report, parametros, jrds);

	*/}

	public void gerarPDFPedido(List<T> listaRelatorio,
			Map<String, Object> parametros, String relatorio, String subreport,
			String fileName) throws Exception {
		FacesContext facesContext = FacesContext.getCurrentInstance();

		parametros.put("tipo", "pdf");
		montarRelatorioPedido(listaRelatorio, facesContext, parametros,
				relatorio, subreport);

		byte[] bytes = JasperExportManager.exportReportToPdf(print);

		if (bytes == null)
			throw new Exception("Array de bytes nulo.");

		if (fileName == null)
			throw new Exception("Nome do arquivo eh nulo.");
		HttpServletResponse response = (HttpServletResponse) facesContext
				.getExternalContext().getResponse();
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ fileName + "\";");
		response.setContentLength(bytes.length);
		ServletOutputStream ouputStream = response.getOutputStream();
		ouputStream.write(bytes, 0, bytes.length);
		facesContext.responseComplete();
	}

	/**
	 * @param listaRelatorio
	 * @param parametros
	 * @param relatorio
	 * @param subreport
	 * @param fileName
	 * @throws Exception
	 */
	public void gerarXLSDinamico(List<T> listaRelatorio,
			Map<String, Object> parametros, String relatorio, String subreport,
			String fileName) throws Exception {

		FacesContext facesContext = FacesContext.getCurrentInstance();

		parametros.put("tipo", "xls");
		montarDinamicoImportacao(listaRelatorio, facesContext, parametros,
				relatorio, subreport);

		HttpServletResponse response = (HttpServletResponse) facesContext
				.getExternalContext().getResponse();

		ServletOutputStream output;
		try {
			output = response.getOutputStream();
			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ fileName + ".xls");

			JRXlsExporter xls = new JRXlsExporter();

			xls.setParameter(JRXlsExporterParameter.JASPER_PRINT, print);
			xls.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, output);
			xls.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE,
					Boolean.FALSE);
			xls.setParameter(JRXlsExporterParameter.MAXIMUM_ROWS_PER_SHEET,
					Integer.decode("65000"));
			xls.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,
					Boolean.TRUE);
			xls
					.setParameter(
							JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,
							Boolean.TRUE);
			xls.setParameter(
					JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
					Boolean.TRUE);
			xls.exportReport();
			output.flush();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JRException e) {
			e.printStackTrace();
		} finally {
			facesContext.responseComplete();
		}

	}

	/**
	 * @param listaRelatorio
	 * @param parametros
	 * @param relatorio
	 * @param subreport
	 * @param fileName
	 * @throws Exception
	 */
	public void gerarPDFDinamico(List<T> listaRelatorio,
			Map<String, Object> parametros, String relatorio, String subreport,
			String fileName) throws Exception {

		FacesContext facesContext = FacesContext.getCurrentInstance();

		parametros.put("tipo", "pdf");
		montarDinamicoImportacao(listaRelatorio, facesContext, parametros,
				relatorio, subreport);

		byte[] bytes = JasperExportManager.exportReportToPdf(print);

		if (bytes == null)
			throw new Exception("Array de bytes nulo.");

		if (fileName == null)
			throw new Exception("Nome do arquivo eh nulo.");

		HttpServletResponse response = (HttpServletResponse) facesContext
				.getExternalContext().getResponse();
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ fileName + ".pdf\";");
		response.setContentLength(bytes.length);
		ServletOutputStream ouputStream = response.getOutputStream();
		ouputStream.write(bytes, 0, bytes.length);
		facesContext.responseComplete();

	}

	public boolean isNumAps() {
		return numAps;
	}

	public void setNumAps(boolean numAps) {
		this.numAps = numAps;
	}

	public boolean isHawb() {
		return hawb;
	}

	public void setHawb(boolean hawb) {
		this.hawb = hawb;
	}

	public boolean isDtColeta() {
		return dtColeta;
	}

	public void setDtColeta(boolean dtColeta) {
		this.dtColeta = dtColeta;
	}

	public boolean isDtPrevista() {
		return dtPrevista;
	}

	public void setDtPrevista(boolean dtPrevista) {
		this.dtPrevista = dtPrevista;
	}

	public boolean isDtReal() {
		return dtReal;
	}

	public void setDtReal(boolean dtReal) {
		this.dtReal = dtReal;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public boolean isRota() {
		return rota;
	}

	public void setRota(boolean rota) {
		this.rota = rota;
	}

	public boolean isModal() {
		return modal;
	}

	public void setModal(boolean modal) {
		this.modal = modal;
	}

	public boolean isAgenteCargas() {
		return agenteCargas;
	}

	public void setAgenteCargas(boolean agenteCargas) {
		this.agenteCargas = agenteCargas;
	}

	public boolean isCanal() {
		return canal;
	}

	public void setCanal(boolean canal) {
		this.canal = canal;
	}

	public boolean isInvoice() {
		return invoice;
	}

	public void setInvoice(boolean invoice) {
		this.invoice = invoice;
	}

	public boolean isExportFornecedor() {
		return exportFornecedor;
	}

	public void setExportFornecedor(boolean exportFornecedor) {
		this.exportFornecedor = exportFornecedor;
	}

	public boolean isTotalDiasEst() {
		return totalDiasEst;
	}

	public void setTotalDiasEst(boolean totalDiasEst) {
		this.totalDiasEst = totalDiasEst;
	}

	public boolean isTotalDiasReal() {
		return totalDiasReal;
	}

	public void setTotalDiasReal(boolean totalDiasReal) {
		this.totalDiasReal = totalDiasReal;
	}

	public void setAll() {
		numAps = true;
		hawb = true;
		dtColeta = true;
		dtPrevista = true;
		dtReal = true;
		status = true;
		rota = true;
		modal = true;
		agenteCargas = true;
		canal = true;
		invoice = true;
		exportFornecedor = true;
		totalDiasEst = true;
		totalDiasReal = true;
	}

	public boolean isProcessoInvoice() {
		return processoInvoice;
	}

	public void setProcessoInvoice(boolean processoInvoice) {
		this.processoInvoice = processoInvoice;
	}

	public boolean isDtInvoice() {
		return dtInvoice;
	}

	public void setDtInvoice(boolean dtInvoice) {
		this.dtInvoice = dtInvoice;
	}

	public boolean isExportadorInvoice() {
		return exportadorInvoice;
	}

	public void setExportadorInvoice(boolean exportadorInvoice) {
		this.exportadorInvoice = exportadorInvoice;
	}

	public boolean isCompradorInvoice() {
		return compradorInvoice;
	}

	public void setCompradorInvoice(boolean compradorInvoice) {
		this.compradorInvoice = compradorInvoice;
	}

	public boolean isOrigemInvoice() {
		return origemInvoice;
	}

	public void setOrigemInvoice(boolean origemInvoice) {
		this.origemInvoice = origemInvoice;
	}

	public boolean isIncoterm() {
		return incoterm;
	}

	public void setIncoterm(boolean incoterm) {
		this.incoterm = incoterm;
	}

	public boolean isCritico() {
		return critico;
	}

	public void setCritico(boolean critico) {
		this.critico = critico;
	}

	public boolean isPliNumber() {
		return pliNumber;
	}

	public void setPliNumber(boolean pliNumber) {
		this.pliNumber = pliNumber;
	}

	public boolean isTotalPesoLiqInvoice() {
		return totalPesoLiqInvoice;
	}

	public void setTotalPesoLiqInvoice(boolean totalPesoLiqInvoice) {
		this.totalPesoLiqInvoice = totalPesoLiqInvoice;
	}

	public boolean isTotalPesoBrutoInvoice() {
		return totalPesoBrutoInvoice;
	}

	public void setTotalPesoBrutoInvoice(boolean totalPesoBrutoInvoice) {
		this.totalPesoBrutoInvoice = totalPesoBrutoInvoice;
	}

	public boolean isDtColetaReal() {
		return dtColetaReal;
	}

	public void setDtColetaReal(boolean dtColetaReal) {
		this.dtColetaReal = dtColetaReal;
	}

	public boolean isResponsavel() {
		return responsavel;
	}

	public void setResponsavel(boolean responsavel) {
		this.responsavel = responsavel;
	}

	public boolean isCidadeOrigem() {
		return cidadeOrigem;
	}

	public void setCidadeOrigem(boolean cidadeOrigem) {
		this.cidadeOrigem = cidadeOrigem;
	}

	public boolean isCidadeDestino() {
		return cidadeDestino;
	}

	public void setCidadeDestino(boolean cidadeDestino) {
		this.cidadeDestino = cidadeDestino;
	}

	public boolean isPesoBruto() {
		return pesoBruto;
	}

	public void setPesoBruto(boolean pesoBruto) {
		this.pesoBruto = pesoBruto;
	}

	public boolean isPesoCubado() {
		return pesoCubado;
	}

	public void setPesoCubado(boolean pesoCubado) {
		this.pesoCubado = pesoCubado;
	}

	public boolean isNumeroDi() {
		return numeroDi;
	}

	public void setNumeroDi(boolean numeroDi) {
		this.numeroDi = numeroDi;
	}

	public boolean isDtRegistroDi() {
		return dtRegistroDi;
	}

	public void setDtRegistroDi(boolean dtRegistroDi) {
		this.dtRegistroDi = dtRegistroDi;
	}

	public JasperPrint getPrint() {
		return print;
	}

	public void setPrint(JasperPrint print) {
		this.print = print;
	}

	public boolean isDtConsolidacao() {
		return dtConsolidacao;
	}

	public void setDtConsolidacao(boolean dtConsolidacao) {
		this.dtConsolidacao = dtConsolidacao;
	}

	public boolean isSelecioneTodos() {
		return selecioneTodos;
	}

	public void setSelecioneTodos(boolean selecioneTodos) {
		this.selecioneTodos = selecioneTodos;
	}

	public boolean isObservacaoInvoice() {
		return observacaoInvoice;
	}

	public void setObservacaoInvoice(boolean observacaoInvoice) {
		this.observacaoInvoice = observacaoInvoice;
	}

	/**
	 * @return the master
	 */
	public boolean isMaster() {
		return master;
	}

	/**
	 * @param master the master to set
	 */
	public void setMaster(boolean master) {
		this.master = master;
	}
}
