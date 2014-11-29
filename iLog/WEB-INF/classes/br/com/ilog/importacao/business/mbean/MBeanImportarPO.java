package br.com.ilog.importacao.business.mbean;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.hibernate.validator.ValidatorHelper;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.commons.citspresentation.util.JSFRequestBean;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.cadastro.presentation.util.ExceptionFiltro;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;
import br.com.ilog.geral.presentation.importar.excel.ImportarPO;
import br.com.ilog.geral.presentation.util.ConverterTexto;
import br.com.ilog.importacao.business.entity.ItemPO;
import br.com.ilog.importacao.business.entity.PO;
import br.com.ilog.importacao.business.facade.ImportacaoFacade;

/**
 * @author Manoel Neto
 * @date 02/08/2012
 * @coment Classe responsavel pela importação dos dados do arquivo de PO do SAP
 * @file foxconn - UC38
 * 
 */
@Controller("mBeanImportarPO")
@AccessScoped
public class MBeanImportarPO implements Serializable {
	private static final long serialVersionUID = 6686725568471200046L;
	private static Logger logger = LoggerFactory
			.getLogger(MBeanImportarPO.class);
	private PO po;
	private ItemPO itemPO;
	private byte[] file;

	@Resource(name = "controllerImportacao")
	private ImportacaoFacade importacaoFacade;

	@Resource(name = "controllerCadastro")
	private CadastroFacade cadastroFacade;
	private String nomeArquivo;

	@PostConstruct
	void inicializar() {
		po = new PO();
		itemPO = null;
		file = null;
		nomeArquivo = null;
	}

	/**
	 * @Fluxo FA1. Importar Dados
	 */
	public String importarDados() {

		return "importarPO.jsf?faces-redirect=true";
	}

	/**
	 * @coment Gerar dados do PO
	 * 
	 */
	public void obterPO() {
		try {
			po = new ImportarPO().importarPO(file, po, importacaoFacade,
					cadastroFacade);
		} catch (BusinessException e) {
			po = new PO();
			itemPO = null;
			file = null;
			nomeArquivo = null;
			e.printStackTrace();
		}catch (Exception e) {
			po = new PO();
			itemPO = null;
			file = null;
			nomeArquivo = null;
			FacesContext fc = FacesContext.getCurrentInstance();
			Messages.adicionaMensagemDeErro(TemplateMessageHelper
					.getMessage(MensagensSistema.SISTEMA,
							"MSG022", fc.getViewRoot()
									.getLocale()));
		}
	}

	/**
	 * @coment Metódo responsavel por salvar as importaçoes PO
	 * 
	 */
	public String salvar() {
		FacesContext fc = FacesContext.getCurrentInstance();
		ResourceBundle resourceBundle = TemplateMessageHelper
				.getResourceBundle(MensagensSistema.PO,
						JSFRequestBean.getLocale());
		List<String> errorMessages = ValidatorHelper.valida(po, resourceBundle);
		if (errorMessages.isEmpty()) {
			try {
				importacaoFacade.cadastrarPO(po);
			} catch (BusinessException e) {
				
				logger.error("error: {}", e);

				/**
				 * VERIFICACAO DE CONSTRAINTS
				 */
				Throwable lastCause = ExceptionFiltro.getLastException(e);
				if (lastCause.getMessage().contains("unique")) {
					if (lastCause.getMessage().contains("'UK_PO'")) {
						Messages.adicionaMensagemDeErro(TemplateMessageHelper
								.getMessage(MensagensSistema.PO,
										"msgPOExistente", fc.getViewRoot()
												.getLocale()));
					}

					return null;
				} else {

					Messages.adicionaMensagemDeErro(TemplateMessageHelper
							.getMessage(ExceptionFiltro.recursiveException(e)));
					e.printStackTrace();
					return null;
				}
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return null;
			}
		}
		return "importacoesPO.jsf";
	}

	/**
	 * @coment verifica se a PO já existe no BD
	 */
	public Boolean consultarDadosPO(String po) {
		try {
			if (importacaoFacade.getPO(po) != null) {
				return true;
			}
		} catch (BusinessException e) {
			e.printStackTrace();
			FacesContext fc = FacesContext.getCurrentInstance();
			String message = TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG022", fc.getViewRoot()
							.getLocale());
			Messages.adicionaMensagemDeInfo(message);
		}
		return false;
	}
	public void limpar(){
		inicializar();
	}

	
	/**
	 * @coment Metódo para obter o arquivo do compomente fileupload da tela
	 */
	public void obterUpload(FileUploadEvent event) {
		FacesContext fc = FacesContext.getCurrentInstance();

		
		nomeArquivo = ConverterTexto.paraIso(event.getFile().getFileName());
		
		file = event.getFile().getContents();
		
		po = new PO();
		try {
			if (consultarDadosPO(new ImportarPO().obterNumPO(file))) {
				nomeArquivo = null;
				po = null;
				String message = TemplateMessageHelper.getMessage(
						MensagensSistema.SISTEMA, "MSG022", fc.getViewRoot()
								.getLocale());
				Messages.adicionaMensagemDeInfo(message);
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the po
	 */
	public PO getPo() {
		return po;
	}

	/**
	 * @param po
	 *            the po to set
	 */
	public void setPo(PO po) {
		this.po = po;
	}

	/**
	 * @return the itemPO
	 */
	public ItemPO getItemPO() {
		return itemPO;
	}

	/**
	 * @param itemPO
	 *            the itemPO to set
	 */
	public void setItemPO(ItemPO itemPO) {
		this.itemPO = itemPO;
	}

	/**
	 * @return the nomeArquivo
	 */
	public String getNomeArquivo() {
		return nomeArquivo;
	}

	/**
	 * @param nomeArquivo
	 *            the nomeArquivo to set
	 */
	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	/**
	 * GETTERS e SETTERS
	 */
}
