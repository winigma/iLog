package br.com.ilog.relatorio.presentation.mbean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.jfree.data.category.DefaultCategoryDataset;
import org.primefaces.model.StreamedContent;
import org.springframework.stereotype.Controller;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.hibernate.validator.ValidatorHelper;
import br.cits.commons.citspresentation.mbeans.AbstractPaginacao;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.commons.citspresentation.util.JSFRequestBean;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;
import br.com.ilog.geral.presentation.util.GerarGrafico;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroFollowUp;
import br.com.ilog.relatorio.business.dto.CargaRelatorioWeeklyBasis;
import br.com.ilog.relatorio.business.facade.RelatorioFacade;

@AccessScoped
@Controller( "mBeanRelatorioWeeklyBasis" )
public class MBeanRelatorioWeeklyBasis extends AbstractPaginacao {

	/** */
	private static final long serialVersionUID = 7388107707568201350L;

	/**
	 * @coment fachada de importação
	 */
	@Resource(name = "controllerRelatorio")
	RelatorioFacade relatorioFacade;

	/**
	 * @coment fachada de cadastros
	 */
	@Resource(name = "controllerCadastro")
	CadastroFacade cadastroFacade;
	
	/**
	 *Filtro utilizados no grafico
	 */
	private int periodoInicio;
	private int periodoFim;
	private int periodoAno;
	
	private int target;
	
	/**
	 * @coment Filtro
	 */
	private BasicFiltroFollowUp filtro;
	
	private List<CargaRelatorioWeeklyBasis> listaRelatorio;
	private List<CargaRelatorioWeeklyBasis> listaContinentes;

	private StreamedContent chart;
	
	@PostConstruct
	public void inicializar() {
		Calendar cal = Calendar.getInstance();
		periodoInicio = 1;
		periodoFim = 52;
		target = 95;
		
		listaRelatorio = new ArrayList<CargaRelatorioWeeklyBasis>();
		listaContinentes = new ArrayList<CargaRelatorioWeeklyBasis>();
		filtro = new BasicFiltroFollowUp();
		filtro.setAno(cal.get(Calendar.YEAR));
		
		try {
			listaRelatorio = relatorioFacade.weeklyBasis(filtro);
			listaContinentes = relatorioFacade.listaFollowUpPorContinente(filtro);
			
			doPesquisar( null );
			
		} catch (BusinessException e) {
			
			e.printStackTrace();

			listaRelatorio = new ArrayList<CargaRelatorioWeeklyBasis>();
			listaContinentes = new ArrayList<CargaRelatorioWeeklyBasis>();
		}
	}
	
	/**
	 * @param titulo
	 * @param base
	 * @return
	 */
	private StreamedContent montarResultado(String titulo, String base) {
		float acumuladorContinente = 0;
		float acumuladorContinentePrazo = 0;
		List<CargaRelatorioWeeklyBasis> list = new ArrayList<CargaRelatorioWeeklyBasis>();
		CargaRelatorioWeeklyBasis crfinal = new CargaRelatorioWeeklyBasis();

		int i = 0;
		for (i = 1; i <= 52; i++) {

			/**
			 * Soma a quantidade total dos continentes
			 */
			for (CargaRelatorioWeeklyBasis continente : listaContinentes) {

				if (continente.getSemana() == i) {
 					acumuladorContinente += continente.getQuantidade();
				}
			}

			/**
			 *Somar o valor por continente acima do prazo
			 */
			for (CargaRelatorioWeeklyBasis continente : listaRelatorio) {

				if (continente.getSemana() == i) {
					acumuladorContinentePrazo += continente.getQuantidade();
				}

			}
			/**
			 * Tirar a media por semaana
			 */
			crfinal.setSemana(i);
			crfinal.setQuantidade((int) acumuladorContinentePrazo);
			try {
				double media = (acumuladorContinentePrazo / acumuladorContinente);
				crfinal.setPercentil(Double.valueOf(media));

			} catch (ArithmeticException e) {
				crfinal.setPercentil(0.0);
			} finally {
				list.add(crfinal);
				crfinal = new CargaRelatorioWeeklyBasis();
				acumuladorContinentePrazo = 0;
				acumuladorContinente = 0;
			}
		}
		//FacesContext fc = FacesContext.getCurrentInstance();
		
		return GerarGrafico.gerarGrafico(titulo, base,
				null,
				createDataset(list), createDSMeta(), 790);

	}
	
	private DefaultCategoryDataset createDataset(
			List<CargaRelatorioWeeklyBasis> list) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		FacesContext fc = FacesContext.getCurrentInstance();
		for (int i = periodoInicio-1; i <= periodoFim-1; i++) {
			dataset.addValue(list.get(i).getPercentil(),
					TemplateMessageHelper.getMessage( MensagensSistema.RELATORIO, "lblForaDoPrazo", fc.getViewRoot().getLocale() ), 
					"W"+(i + 1));
		}
		return dataset;
	}

	private DefaultCategoryDataset createDSMeta() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		FacesContext fc = FacesContext.getCurrentInstance();
		for (int i = periodoInicio-1; i <= periodoFim-1; i++) {
			dataset.addValue((target/100.0),
					TemplateMessageHelper.getMessage( MensagensSistema.RELATORIO, "lblObjetivo", fc.getViewRoot().getLocale() ), 
					"W" + (i + 1));
		}
		return dataset;
	}
	
	@Override
	public void doPesquisar(ActionEvent arg0) {
		
		
		FacesContext fc = FacesContext.getCurrentInstance();

		ResourceBundle resourceBundle = TemplateMessageHelper
			.getResourceBundle(MensagensSistema.RELATORIO, JSFRequestBean.getLocale());
		List<String> errorMessages = ValidatorHelper.valida(filtro,	resourceBundle, resourceBundle);
		
		if (errorMessages.isEmpty()) {
			try {
				listaRelatorio = relatorioFacade.weeklyBasis(filtro);

				if (listaRelatorio.isEmpty()) {

					chart = null;
					String message = TemplateMessageHelper.getMessage(
							MensagensSistema.SISTEMA, "MSG008", fc.getViewRoot().getLocale());
					Messages.adicionaMensagemDeInfo(message);
				} else {
					chart = montarResultado(TemplateMessageHelper.getMessage(
							MensagensSistema.RELATORIO, "lblSemanalOTD", fc.getViewRoot().getLocale()),
							TemplateMessageHelper.getMessage(
									MensagensSistema.RELATORIO, "lblSemana", fc
											.getViewRoot().getLocale()));
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

	/**
	 * @return the periodoInicio
	 */
	public int getPeriodoInicio() {
		return periodoInicio;
	}

	/**
	 * @param periodoInicio the periodoInicio to set
	 */
	public void setPeriodoInicio(int periodoInicio) {
		this.periodoInicio = periodoInicio;
	}

	/**
	 * @return the periodoFim
	 */
	public int getPeriodoFim() {
		return periodoFim;
	}

	/**
	 * @param periodoFim the periodoFim to set
	 */
	public void setPeriodoFim(int periodoFim) {
		this.periodoFim = periodoFim;
	}

	/**
	 * @return the periodoAno
	 */
	public int getPeriodoAno() {
		return periodoAno;
	}

	/**
	 * @param periodoAno the periodoAno to set
	 */
	public void setPeriodoAno(int periodoAno) {
		this.periodoAno = periodoAno;
	}

	/**
	 * @return the target
	 */
	public int getTarget() {
		return target;
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(int target) {
		this.target = target;
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
	 * @return the listaRelatorio
	 */
	public List<CargaRelatorioWeeklyBasis> getListaRelatorio() {
		return listaRelatorio;
	}

	/**
	 * @param listaRelatorio the listaRelatorio to set
	 */
	public void setListaRelatorio(List<CargaRelatorioWeeklyBasis> listaRelatorio) {
		this.listaRelatorio = listaRelatorio;
	}

	/**
	 * @return the listaContinentes
	 */
	public List<CargaRelatorioWeeklyBasis> getListaContinentes() {
		return listaContinentes;
	}

	/**
	 * @param listaContinentes the listaContinentes to set
	 */
	public void setListaContinentes(List<CargaRelatorioWeeklyBasis> listaContinentes) {
		this.listaContinentes = listaContinentes;
	}

	/**
	 * @return the chart
	 */
	public StreamedContent getChart() {
		return chart;
	}

	/**
	 * @param chart the chart to set
	 */
	public void setChart(StreamedContent chart) {
		this.chart = chart;
	}

}
