package br.com.ilog.geral.presentation.importar.excel;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citspresentation.messages.Messages;
import br.com.ilog.cadastro.business.entity.Filial;
import br.com.ilog.cadastro.business.entity.FormaPagamento;
import br.com.ilog.cadastro.business.entity.Moeda;
import br.com.ilog.cadastro.business.entity.PessoaJuridica;
import br.com.ilog.cadastro.business.entity.UnidadeMedida;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroPessoaJuridica;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.geral.business.CodigoErroEspecifico;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;
import br.com.ilog.importacao.business.entity.ItemPO;
import br.com.ilog.importacao.business.entity.PO;
import br.com.ilog.importacao.business.facade.ImportacaoFacade;

/**
 * @author Manoel Neto
 * @data 07/08/2012
 * @coment Importa as informaçoes referente ao PO e seus respectivos itens.
 */
public class ImportarPO {

	/**
	 * Dados PO
	 */
	public static final int COLUNA_PO_Number = 1; // Numero PO , coluna B
	public static final int COLUNA_Plant = 7; // Codigo Filial, coluna H
	public static final int COLUNA_VendorCode = 9; // Codigo Fornecedor, coluna
													// J
	public static final int COLUNA_PayTerms = 24; // Codigo Forma Pagamento,
													// coluna Y
	public static final int COLUNA_PO_Date = 18; // Data PO, coluna S
	public static final int COLUNA_Delivery_Date = 19; // Data Entrega, coluna T

	/**
	 * Dados Item PO
	 */
	public static final int COLUNA_NUM_ITEM = 2; // PO Item, coluna C
	public static final int COLUNA_PARTNUMBER = 3; // Part NO, coluna D
	public static final int COLUNA_DESCRICAO_PRODUTO = 5; // Description, coluna
															// F
	public static final int COLUNA_QUANTIDADE = 11; // PO Qty, coluna L
	public static final int COLUNA_QUANTIDADE_SALDO = 11; // PO Qty, coluna L
	public static final int COLUNA_PRECO_UNITARIO = 15; // Unit Price, coluna P
	public static final int COLUNA_VALOR_TOTAL = 7; //
	public static final int COLUNA_VALOR_SALDO = 8; //
	public static final int COLUNA_CURRENCY = 17; // Moeda, coluna R
	public static final int COLUNA_UNIT = 14; // Unidade de Medida, coluna O

	private List<String> mensagensErro = new ArrayList<String>();

	private List<String> mensagensInfo = new ArrayList<String>();

	public String obterNumPO(byte[] data) throws BusinessException {
		InputStream b = new ByteArrayInputStream(data);
		// para leitura de xlsx
		try {
			Workbook wb;
			try {
				wb = WorkbookFactory.create(b);
			} catch (InvalidFormatException e) {

				e.printStackTrace();
				wb = new XSSFWorkbook(b);
				Messages.adicionaMensagemDeErro("Formato Corrompido");
				return null;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				//POIFSFileSystem fs;
				if(e.getMessage().contains("Your InputStream was neither an OLE2 stream, nor an OOXML stream")){
					Messages.adicionaMensagemDeErro("Formato Corrompido");
					return null;
				}
				wb = new XSSFWorkbook(b);
			
			}
			
			catch (Exception e) {
				e.printStackTrace();
				wb = new XSSFWorkbook(b);
			     
			}
			Sheet sheet = wb.getSheetAt(0);
			try {
				return String.valueOf((long) sheet.getRow(4)
						.getCell(COLUNA_PO_Number).getNumericCellValue());
			} catch (Exception e) {
				// TODO: handle exception
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessException(
					CodigoErroEspecifico.FORMATO_INVALIDO_INVOICE);
		}
		return "";
	}

	/**
	 * Metodo para importacao do arquivo EXCEL para preenchimentos dos campos de
	 * ITENS de Invoice.
	 * 
	 * @param data
	 * @param po
	 * @param facade
	 * @return PO
	 * @throws BusinessException
	 */
	public PO importarPO(byte[] data, PO po, ImportacaoFacade facade,
			CadastroFacade cadastroFacade) throws BusinessException {

		/**
		 * Preeche os dados do PO
		 */
		ItemPO itemPO;
		List<ItemPO> itens = new ArrayList<ItemPO>();
		InputStream b = new ByteArrayInputStream(data);
		try {

			// para leitura de xlsx
			Workbook wb;
			try {
				wb = WorkbookFactory.create(b);

			} catch (Exception e) {
				wb = new XSSFWorkbook(b);
			}

			Sheet sheet = wb.getSheetAt(0);

			try {

				/**
				 * Popular dados do PO
				 */
				// recupera o numero PO.
				po.setNumeroPO(String.valueOf((long) sheet.getRow(4)
						.getCell(COLUNA_PO_Number).getNumericCellValue()));
				// Data entrega
				DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
				Date date = (Date) formatter.parse(sheet.getRow(4)
						.getCell(COLUNA_Delivery_Date).toString());
				po.setDataEntrega(date);
				// Data PO
				date = (Date) formatter.parse(sheet.getRow(4)
						.getCell(COLUNA_PO_Date).toString());
				po.setDataPO(date);

				// Verifica Filial
				Filial filial = new Filial();
				filial.setCodigo(sheet.getRow(4).getCell(COLUNA_Plant)
						.toString());
				if (!filial.getCodigo().isEmpty()) {
					filial = cadastroFacade.getFilialByCodigo(filial
							.getCodigo());
					if (filial != null) {
						po.setFilial(filial);
					} else {

						throw new BusinessException(
								CodigoErroEspecifico.FORMATO_INVALIDO_INVOICE);
					}
				}

				// Fornecedor
				PessoaJuridica fornecedor = new PessoaJuridica();
				fornecedor.setVendorSap((long) sheet.getRow(4)
						.getCell(COLUNA_VendorCode).getNumericCellValue());
				if (fornecedor.getVendorSap() != null) {
					BasicFiltroPessoaJuridica filtroPessoaJuridica = new BasicFiltroPessoaJuridica();
					filtroPessoaJuridica
							.setVendorSap(fornecedor.getVendorSap());
					fornecedor = cadastroFacade
							.getPessoaBySap(filtroPessoaJuridica);
					if (fornecedor != null) {
						po.setFornecedor(fornecedor);
					} else {
						throw new BusinessException(
								CodigoErroEspecifico.FORMATO_INVALIDO_INVOICE);
					}
				}

				// Forma de Pagamento
				FormaPagamento formaPagamento = new FormaPagamento();
				formaPagamento.setCodeSAP(sheet.getRow(4)
						.getCell(COLUNA_PayTerms).getStringCellValue());
				if (formaPagamento.getCodeSAP() != null) {
					formaPagamento = cadastroFacade
							.getFormaPagamentoBycodeSAP(formaPagamento
									.getCodeSAP());
					if (formaPagamento != null) {
						po.setFormaPagamento(formaPagamento);
					} else {
						throw new BusinessException(
								CodigoErroEspecifico.FORMATO_INVALIDO_INVOICE);
					}
				}

				/**
				 * Popular os itens do PO
				 */
				for (int i = 4; i < sheet.getPhysicalNumberOfRows(); i++) {
					itemPO = new ItemPO();
					itemPO.setDescricaoProduto(sheet.getRow(i)
							.getCell(COLUNA_DESCRICAO_PRODUTO).toString());
					itemPO.setNumeroItem((int) sheet.getRow(i)
							.getCell(COLUNA_NUM_ITEM).getNumericCellValue());
					itemPO.setPartNumber(sheet.getRow(i)
							.getCell(COLUNA_PARTNUMBER).toString());
					itemPO.setPo(po);
					itemPO.setPrecoUnitario(new BigDecimal(sheet.getRow(i)
							.getCell(COLUNA_PRECO_UNITARIO)
							.getNumericCellValue()).setScale(4,
							RoundingMode.HALF_UP));
					itemPO.setQuantidade((int) sheet.getRow(i)
							.getCell(COLUNA_QUANTIDADE).getNumericCellValue());
					itemPO.setQuantidadeSaldo(itemPO.getQuantidade());
					itemPO.setValorTotal(itemPO.getPrecoUnitario()
							.multiply(new BigDecimal(itemPO.getQuantidade()))
							.setScale(4, RoundingMode.HALF_UP));
					itemPO.setValorTotalSaldo(itemPO.getValorTotal());

					/**
					 * Moeda
					 */
					Moeda moeda = new Moeda();
					moeda.setSigla(sheet.getRow(i).getCell(COLUNA_CURRENCY)
							.toString());
					if (!moeda.getSigla().isEmpty()) {
						moeda = cadastroFacade
								.getMoedaBySigla(moeda.getSigla());
						if (moeda != null) {
							itemPO.setMoeda(moeda);
						} else {
							throw new BusinessException(
									CodigoErroEspecifico.FORMATO_INVALIDO_INVOICE);
						}
					}

					/**
					 * Unidade de Medida
					 */
					UnidadeMedida unidadeMedida = new UnidadeMedida();
					unidadeMedida.setCodigo(sheet.getRow(i)
							.getCell(COLUNA_UNIT).toString());
					if (!unidadeMedida.getCodigo().isEmpty()) {
						unidadeMedida = cadastroFacade
								.getUnidMedidaByCodigo(unidadeMedida
										.getCodigo());
						if (unidadeMedida != null) {
							itemPO.setUnidadeMedida(unidadeMedida);
						} else {
							throw new BusinessException(
									CodigoErroEspecifico.FORMATO_INVALIDO_INVOICE);
						}
					}
					itens.add(itemPO);
				}
				po.setItens(itens);
				return po;

			} catch (BusinessException e) {
				po = null;
				Messages.adicionaMensagemDeErro(TemplateMessageHelper
						.getMessage(MensagensSistema.PO, e));
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				Messages.adicionaMensagemDeErro(TemplateMessageHelper
						.getMessage(MensagensSistema.SISTEMA, "MSG022"));
				po = null;

				throw new BusinessException(
						CodigoErroEspecifico.FORMATO_INVALIDO_INVOICE);
			}

		} catch (IOException e) {
			po = null;
			e.printStackTrace();
			throw new BusinessException(
					CodigoErroEspecifico.FORMATO_INVALIDO_INVOICE);
		}

	}

	public List<String> getMensagensErro() {
		return mensagensErro;
	}

	public void setMensagensErro(List<String> mensagens) {
		this.mensagensErro = mensagens;
	}

	public List<String> getMensagensInfo() {
		return mensagensInfo;
	}

	public void setMensagensInfo(List<String> mensagensInfo) {
		this.mensagensInfo = mensagensInfo;
	}
}
