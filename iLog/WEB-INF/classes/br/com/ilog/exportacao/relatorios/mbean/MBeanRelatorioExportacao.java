package br.com.ilog.exportacao.relatorios.mbean;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import br.cits.commons.citsbusiness.hibernate.validator.ValidatorHelper;
import br.cits.commons.citspresentation.mbeans.AbstractPaginacao;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.commons.citspresentation.util.JSFRequestBean;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.exportacao.business.entity.CargaExp;
import br.com.ilog.exportacao.business.entityFilter.BasicFiltroCargaExp;
import br.com.ilog.exportacao.business.facade.ExportacaoFacade;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;
import br.com.ilog.relatorio.business.facade.RelatorioFacade;
import br.com.ilog.relatorio.util.ExportarRelatorio;

/**
 * 
 * @author Wisley P. SOuza
 * @coment Bean de Relatórios
 */
@Controller("mBeanRelatorioExportacao")
@AccessScoped
public class MBeanRelatorioExportacao extends AbstractPaginacao {

	private static final long serialVersionUID = 1L;

	private static Logger logger = LoggerFactory
			.getLogger(MBeanRelatorioExportacao.class);

	/**
	 * @coment fachada de cadastros
	 */
	@Resource(name = "controllerCadastro")
	CadastroFacade cadastroFacade;

	@Resource(name = "controllerExportacao")
	ExportacaoFacade exportacaoFacade;

	@Resource(name = "controllerRelatorio")
	RelatorioFacade relatorioFacade;

	private BasicFiltroCargaExp filtro;

	/**
	 * @coment Parametros da tela para o relatorio
	 * 
	 */
	private Map<String, Object> parametros;

	private CargaExp carga;
	private List<CargaExp> relatorio;

	@PostConstruct
	void inicializar() {

		filtro = new BasicFiltroCargaExp();
		relatorio = Collections.emptyList();
		doPesquisar(null);
	}

	private void prepararParametros() {
		parametros = new HashMap<String, Object>();
		FacesContext fc = FacesContext.getCurrentInstance();

		// Configurando os parametros de titulos
		parametros.put("titulo", TemplateMessageHelper.getMessage(
				MensagensSistema.MENU, "lblCargaExportacao", fc.getViewRoot()
						.getLocale()));

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

	}

	public void exportarPdf() {

		try {
			prepararParametros();
			parametros.put("tipo", "pdf");
			new ExportarRelatorio<CargaExp>().gerarPDF(relatorio, parametros,
					"/relatorios/relatorioExportacao.jasper",
					"relatorioExportacao.pdf");
		} catch (Exception e) {
			logger.error("error: {} " + e);
			e.printStackTrace();
		}

	}

	public void exportarXls() {
		try {
			prepararParametros();
			parametros.put("tipo", "xls");
			new ExportarRelatorio<CargaExp>().gerarXLS(relatorio, parametros,
					"/relatorios/relatorioExportacao.jasper",
					"relatorioExportacao.xls");
		} catch (Exception e) {
			logger.error("error: {} " + e);
			e.printStackTrace();
		}

	}

	@Override
	public void doPesquisar(ActionEvent arg0) {
		FacesContext fc = FacesContext.getCurrentInstance();

		ResourceBundle resourceBundle = TemplateMessageHelper
				.getResourceBundle(MensagensSistema.CARGA_EXP,
						JSFRequestBean.getLocale());
		List<String> errorMessages = ValidatorHelper.valida(filtro,
				resourceBundle, resourceBundle);

		if (errorMessages.isEmpty()) {
			try {
				relatorio = relatorioFacade.relatorioExportacao(filtro);
				if (relatorio.isEmpty()) {
					String message = TemplateMessageHelper.getMessage(
							MensagensSistema.SISTEMA, "MSG024", fc
									.getViewRoot().getLocale());
					Messages.adicionaMensagemDeInfo(message);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Messages.adicionaMensagensDeErro(errorMessages);
		}

	}

	@Override
	public int getTotalRegistros() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void limpar() {
		// TODO Auto-generated method stub

	}

	@Override
	public void refazerPesquisa() {
		// TODO Auto-generated method stub

	}

	public BasicFiltroCargaExp getFiltro() {
		return filtro;
	}

	public void setFiltro(BasicFiltroCargaExp filtro) {
		this.filtro = filtro;
	}

	public Map<String, Object> getParametros() {
		return parametros;
	}

	public void setParametros(Map<String, Object> parametros) {
		this.parametros = parametros;
	}

	public CargaExp getCarga() {
		return carga;
	}

	public void setCarga(CargaExp carga) {
		this.carga = carga;
	}

	public List<CargaExp> getRelatorio() {
		return relatorio;
	}

	public void setRelatorio(List<CargaExp> relatorio) {
		this.relatorio = relatorio;
	}

}
