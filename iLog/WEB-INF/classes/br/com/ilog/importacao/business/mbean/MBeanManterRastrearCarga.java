package br.com.ilog.importacao.business.mbean;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;

import org.jfree.data.gantt.TaskSeriesCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import br.cits.commons.citspresentation.mbeans.AbstractManter;
import br.cits.commons.citspresentation.util.JSFRequestBean;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.Cidade;
import br.com.ilog.cadastro.business.entity.Ocorrencia;
import br.com.ilog.cadastro.business.entity.PessoaJuridica;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroOcorrencia;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;
import br.com.ilog.geral.presentation.util.DataUtils;
import br.com.ilog.importacao.business.entity.Carga;
import br.com.ilog.importacao.business.entity.FollowUpImport;
import br.com.ilog.importacao.business.entity.FollowUpImportTrecho;
import br.com.ilog.importacao.business.entity.Invoice;
import br.com.ilog.importacao.business.facade.ImportacaoFacade;

@AccessScoped
@Controller("mBeanManterRastrearCarga")
public class MBeanManterRastrearCarga extends AbstractManter {

	private static final long serialVersionUID = -6149415601829224108L;
	static Logger logger = LoggerFactory
			.getLogger(MBeanManterRastrearCarga.class);

	/**
	 * @coment fachada de importação
	 */
	@Resource(name = "controllerImportacao")
	ImportacaoFacade rastrearCargaFacade;

	/**
	 * @coment fachada de cadastros
	 */
	@Resource(name = "controllerCadastro")
	CadastroFacade cadastroFacade;

	
	/**
	 * Dados para o grafico
	 */
	BufferedImage pageChart1;

	private Carga carga;
	private Carga  selectCarga;
	private Ocorrencia ocorrencia;
	private List<Ocorrencia> listOcorrencias;
	private List<Ocorrencia> listOcorrencias2;
	private TaskSeriesCollection collectionTasks;
	private BasicFiltroOcorrencia filtroOcorrencia;

	private List<Invoice> listaInvoices;
	private FollowUpImport followUp;

	private FollowUpImportTrecho followTrecho;

	private boolean ativar;
	// tamanho do zoom
	private Integer sizeZoom;
	private String address2;

	/*
	 * Arquivos referentes a geração do gráfico - Date 06/03/2012
	 */

	private String xml;

	/*
	 * *************************************************************
	 */

	@PostConstruct
	void inicializar() {
		try {
			inicializarObjetos();
			filtroOcorrencia = new BasicFiltroOcorrencia();
			listaInvoices = Collections.emptyList();
			//collectionTasks = new TaskSeriesCollection();
			followUp = new FollowUpImport();
			followUp.setTrechosFollowUp(new ArrayList<FollowUpImportTrecho>());
			sizeZoom = 11;
			// deixando a variavel false para nao mostrar as cidades das
			// ocorrencias
			ativar = false;
			address = "Brasil";
			address2 = "Brasil";
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getRetornaDateInicio(FollowUpImportTrecho t) {
		SimpleDateFormat format = new SimpleDateFormat("dd/M/yyyy");
		// carrega o locale
		FacesContext fc = FacesContext.getCurrentInstance();
		String tdateStart = TemplateMessageHelper.getMessage(
				MensagensSistema.RASTREAR_CARGA, "lblNoDate", fc.getViewRoot()
						.getLocale());
		if (t.getDtRealizado() != null)
			tdateStart = format.format(t.getDtRealizado());

		return tdateStart;
	}

	public String getRetornaDateFim(FollowUpImportTrecho t) {
		return "";
	}

	/*
	 * Método de carregamento do chart
	 */

	public String getDiaSemanaDescritivo(Date data) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(data);
		String[] diaSemana = new String[] { "Domingo", "D", "S", "T", "Q", "Q",
				"S", "S" };
		return diaSemana[calendar.get(GregorianCalendar.DAY_OF_WEEK)];
	}

	public String gerarChart() {
		try {
			// carrega o locale
			FacesContext fc = FacesContext.getCurrentInstance();
			// carrega o titulo
			String titulo = TemplateMessageHelper.getMessage(
					MensagensSistema.RASTREAR_CARGA, "lblRastrearCarga", fc
							.getViewRoot().getLocale());
			String subTitulo = TemplateMessageHelper.getMessage(
					MensagensSistema.RASTREAR_CARGA, "lblSubTitulo", fc
							.getViewRoot().getLocale());

			// carrega as cidades do trechos

			List<FollowUpImportTrecho> trechos = followUp.getTrechosFollowUp();
			FollowUpImportTrecho tStart = trechos.get(0);
			FollowUpImportTrecho tEnd = trechos.get(trechos.size() - 1);
			// cria o formatador
			SimpleDateFormat format = new SimpleDateFormat("dd/M/yyyy");
			String tdateStart = "";

			// criar duas varieves date para fazer o while de categories
			Date dataInicio;
			Date dataFim;
			if (tStart.getDtRealizado() != null) {
				Calendar c = Calendar.getInstance();
				c.setTime(tStart.getDtRealizado());
				c.add(Calendar.DATE, -5);
				tdateStart = format.format(c.getTime());
				dataInicio = c.getTime();
			} else {
				Calendar c = Calendar.getInstance();
				c.setTime(tStart.getDtPlanejado());
				c.add(Calendar.DATE, -5);
				tdateStart = format.format(c.getTime());
				dataInicio = c.getTime();
			}
			String tdateEnd = "";
			if (tEnd.getDtRealizado() != null) {
				Calendar c = Calendar.getInstance();
				c.setTime(tEnd.getDtRealizado());
				c.add(Calendar.DATE, 5);
				tdateEnd = format.format(c.getTime());
				dataFim = c.getTime();
			}

			else {
				Calendar c = Calendar.getInstance();
				c.setTime(tEnd.getDtPlanejado());
				c.add(Calendar.DATE, 5);

				tdateEnd = format.format(c.getTime());
				dataFim = c.getTime();
			}

			// carrega o cabeçalho do xml
			xml = "<chart dateFormat='dd/mm/yyyy'"
					+ " hoverCapBorderColor='2222ff' hoverCapBgColor='e1f5ff'"
					+ " ganttWidthPercent='63' ganttLineAlpha='100' "
					+ "canvasBorderColor='024455' canvasBorderThickness='0'"
					+ " gridBorderColor='4567aa' gridBorderAlpha='20'>";

			// carrega a primeira categoria que contem o titulo
			xml += "<categories  bgColor='009999'>" + "<category start='"
					+ tdateStart + "' " + "end='" + tdateEnd
					+ "' align='center' " + "name='" + titulo + "'  "
					+ "fontColor='ffffff' isBold='1' fontSize='16' />"
					+ "</categories>";

			// carrega a segunda categoria que contém o subtitulo
			xml += "<categories  bgColor='4567aa' fontColor='ff0000'>"
					+ "	<category start='"
					+ tdateStart
					+ "' end='"
					+ tdateEnd
					+ "'"
					+ " align='center' name='"
					+ subTitulo
					+ "'  alpha='' "
					+ "font='Verdana' fontColor='ffffff' isBold='1' fontSize='16' />"
					+ "</categories>";

			String periodo = "";
			// testando pegar mes
			Calendar ca = Calendar.getInstance();
			ca.setTime(dataInicio);
			ca.setTime(dataFim);
			ca.set(Calendar.MONTH, 1);

			while (dataInicio.compareTo(dataFim) <= 0) {
				String dtPeriodo = format.format(dataInicio);
				periodo += "	<category start='" + dtPeriodo + "' end='"
						+ dtPeriodo + "' align='center' name='"
						+ getDiaSemanaDescritivo(dataInicio)
						+ "'  isBold='1' />";

				Calendar c = Calendar.getInstance();
				c.setTime(dataInicio);
				c.add(Calendar.DATE, 1);
				dataInicio = c.getTime();

			}

			// carrega a terceira categoria
			xml += "<categories  bgColor='ffffff' fontColor='1288dd' fontSize='10' >";

			xml += periodo;
			xml += "</categories>";

			// carregando os processos = tasks, nesse caso cidades do trechos

			// Map<Long, Integer> processos = new HashMap<Long, Integer>();
			String processosCabecalho = "<processes headerText='"
					+ TemplateMessageHelper.getMessage(
							MensagensSistema.RASTREAR_CARGA,
							"lblHeaderProcessChart", fc.getViewRoot()
									.getLocale())
					+ "' fontColor='E0FFFF' fontSize='10' isBold='1' isAnimated='1'"
					+ " bgColor='4567aa'  headerVAlign='right' headerbgColor='4567aa'"
					+ " headerFontColor='ffffff' headerFontSize='16' width='165' align='left'>";
			String processoBody = "";
			int idProcesso = 1;
			for (FollowUpImportTrecho trecho : trechos) {
				if (!trecho.isCanal()) {

					Cidade city = cadastroFacade.getCidadeById(trecho
							.getCidade().getId());
					if (!trecho.getTxLocal().equals("FOXCONN")) {
						// imutando
						// processos.put(trecho.getId(), idProcesso);
						processoBody += "<process Name='" + city.getNome()
								+ "' id='" + idProcesso + "' />";
					} else {
						processoBody += "<process Name='" + "FOXCONN" + "' id='"
								+ idProcesso + "' />";
					}
				} else {
					processoBody += "<process Name='" + "Canal" + "' id='"
							+ idProcesso + "' />";
				}

				idProcesso++;

			}
			String processoFooter = "</processes>";

			xml += processosCabecalho;
			xml += processoBody;
			xml += processoFooter;

			// cria cabeçalho do dataTable
			String dataTableCabelho = "<dataTable showProcessName='1'"
					+ " nameAlign='left' fontColor='000000' fontSize='10' isBold='1'"
					+ " headerBgColor='00ffff' headerFontColor='4567aa' headerFontSize='11'"
					+ " vAlign='right' align='left'>";

			// criando corpo/colunas da data table
			String dataTableBody1 = "";
			dataTableBody1 += "<dataColumn width='80' "
					+ "headerfontcolor='ffffff' headerBgColor='4567aa' bgColor='eeeeee'  "
					+ "headerColor='ffffff' headerText='Start' isBold='0'>";
			for (FollowUpImportTrecho trecho : trechos) {
				dataTableBody1 += "<text label='"
						+ getRetornaDateInicio(trecho) + "' /> ";
			}
			dataTableBody1 += "</dataColumn>";

			String dataTableBody2 = "";
			dataTableBody2 += "<dataColumn width='80' headerfontcolor='ffffff' "
					+ " bgColor='eeeeee' headerbgColor='4567aa'  fontColor='000000' "
					+ "headerText='Finish' isBold='0'>";
			for (int i = 0; i < trechos.size(); i++) {
				FollowUpImportTrecho trecho = new FollowUpImportTrecho();
				// verificar se é ultimo
				if (i < trechos.size() - 1) {
					trecho = trechos.get(i + 1);
				} else {
					trecho = trechos.get(i);
				}
				dataTableBody2 += "<text label='"
						+ getRetornaDateInicio(trecho) + "' /> ";

			}

			dataTableBody2 += "</dataColumn>";

			// ultima coluna da tabela
			/**
			String dataTableBody3 = "";
			dataTableBody3 += "<dataColumn align='center'"
					+ " headerfontcolor='ffffff'  headerbgColor='4567aa' "
					+ " bgColor='eeeeee' headerText='Dur.' width='35' isBold='0'>";

			 * for (FollowUpImportTrecho trecho : trechos) { trecho =
			 * rastrearCargaFacade.getFollowUpImportTrechoById(trecho .getId());
			 * dataTableBody3 += "<text label='" + 1 + "' /> "; }
			
			dataTableBody3 += "</dataColumn>";
			
			 **/

			String dataTableFooter = "";
			dataTableFooter += "</dataTable>";

			// inseri a tabela e as colunas no xml
			xml += dataTableCabelho;
			xml += dataTableBody1;
			xml += dataTableBody2;
			// xml += dataTableBody3;
			xml += dataTableFooter;

			/*
			 * Inserindo os Tasks...
			 */
			// tasks cabecalho e rodape
			String taskCabecalho = "<tasks  width='15' >";
			String taskFooter = "</tasks>";
			String taskBody = "";
			String taskBody2 = "";
			int idTask1 = 1;
			int idTask2 = 1;

			for (FollowUpImportTrecho trecho : trechos) {
				Cidade cNome = cadastroFacade.getCidadeById(trecho.getCidade()
						.getId());

				Calendar data = Calendar.getInstance();
				if (trecho.getDtRealizado() != null)
					data.setTime(trecho.getDtRealizado());
				if (trecho.getDtRealizado() != null) {
					if (!trecho.isCanal()) {
						int i = trechos.indexOf(trecho);
						FollowUpImportTrecho tFim = new FollowUpImportTrecho();
						if (i < trechos.size() - 1) {
							tFim = trechos.get(i+1);
						}

						// cria o formatador
						SimpleDateFormat formatador = new SimpleDateFormat(
								"dd/M/yyyy");

						if (!trecho.getTxLocal().equals("FOXCONN")) {
							String dateEnd = "";
							if (tFim.getDtRealizado() != null) {
								dateEnd = formatador.format(tFim
										.getDtRealizado());
							} else {
								dateEnd = getRetornaDateInicio(trecho);
							}
							taskBody += "<task name='" + cNome.getNome()
									+ "' processId='" + idTask1 + "' start='"
									+ getRetornaDateInicio(trecho) + "' "
									+ " end='" + dateEnd + "' id='" + idTask1
									+ "-" + (idTask1 + 1)
									+ "' color='4567aa' alpha='100' "
									+ " topPadding='5' animation='0'/> ";
						} else {
							String dateEnd = formatador.format(DataUtils
									.somarDias(data.getTime(), trecho
											.getDiasPrevistos()));
							taskBody += "<task name='" + cNome.getNome()
									+ "' processId='" + idTask1 + "' start='"
									+ getRetornaDateInicio(trecho) + "' "
									+ " end='" + dateEnd + "' id='" + idTask1
									+ "-" + (idTask1 + 1)
									+ "' color='4567aa' alpha='100' "
									+ " topPadding='5' animation='0'/> ";

						}

					} else {

						// cria o formatador
						SimpleDateFormat formatador = new SimpleDateFormat(
								"dd/M/yyyy");

						// mudannça
						FollowUpImportTrecho trechoFim = trechos.get(trechos
								.size() - 1);
						String dateEnd = formatador.format(trechoFim
								.getDtRealizado());

						taskBody += "<task name='" + cNome.getNome()
								+ "' processId='" + idTask1 + "' start='"
								+ getRetornaDateInicio(trecho) + "' "
								+ " end='" + dateEnd + "' id='" + idTask1 + "-"
								+ (idTask1 + 1)
								+ "' color='4567aa' alpha='100' "
								+ " topPadding='5' animation='0'/> ";
					}
				}
				idTask1++;

			}
			filtroOcorrencia.setAtivo(true);
			listOcorrencias = rastrearCargaFacade
					.listarOcorrencia(filtroOcorrencia);

			for (FollowUpImportTrecho trecho : trechos) {

				if (!trecho.isCanal()) {

					Cidade cTrecho = cadastroFacade.getCidadeById(trecho
							.getCidade().getId());
					trecho.setCidade(cTrecho);

					for (Ocorrencia oco : listOcorrencias) {
						if (!oco.getIsLom() && !oco.getCanal()) {
							// Cidade cOcorrencia =
							// cadastroFacade.getCidadeById(oco.getCidade().getId());
							if (trecho.getCidade().getId().equals(
									oco.getCidade().getId())
									&& !trecho.getTxLocal().equals("FOXCONN")) {

								SimpleDateFormat formatador = new SimpleDateFormat(
										"dd/MM/yyyy");
								String start = formatador.format(oco
										.getDtOcorrencia());
								taskBody2 += "<task name='Ocurrence' processId='"
										+ idTask2
										+ "' start='"
										+ start
										+ "'"
										+ " end='"
										+ start
										+ "' id='"
										+ idTask2
										+ 1
										+ "' color='cccccc' alpha='100' height='10'"
										+ " topPadding='19'/>";

							} else {
								if (oco.getIsLom()) {
									if (trecho.getTxLocal().equals("FOXCONN")) {
										SimpleDateFormat formatador = new SimpleDateFormat(
												"dd/MM/yyyy");
										String start = formatador.format(oco
												.getDtOcorrencia());
										taskBody2 += "<task name='Ocurrence' processId='"
												+ idTask2
												+ "' start='"
												+ start
												+ "'"
												+ " end='"
												+ start
												+ "' id='"
												+ idTask2
												+ 1
												+ "' color='cccccc' alpha='100' height='10'"
												+ " topPadding='19'/>";

									}
								}
							}
						} else {

							if (oco.getIsLom()) {
								if (trecho.getTxLocal().equals("FOXCONN")) {
									SimpleDateFormat formatador = new SimpleDateFormat(
											"dd/MM/yyyy");
									String start = formatador.format(oco
											.getDtOcorrencia());
									taskBody2 += "<task name='Ocurrence' processId='"
											+ idTask2
											+ "' start='"
											+ start
											+ "'"
											+ " end='"
											+ start
											+ "' id='"
											+ idTask2
											+ 1
											+ "' color='cccccc' alpha='100' height='10'"
											+ " topPadding='19'/>";

								}
							}

						}

					}
				} else {

					Cidade cTrecho = cadastroFacade.getCidadeById(trecho
							.getCidade().getId());
					trecho.setCidade(cTrecho);
					for (Ocorrencia oco : listOcorrencias) {
						if (oco.getCanal()) {
							SimpleDateFormat formatador = new SimpleDateFormat(
									"dd/MM/yyyy");
							String start = formatador.format(oco
									.getDtOcorrencia());
							taskBody2 += "<task name='Ocurrence' processId='"
									+ idTask2
									+ "' start='"
									+ start
									+ "'"
									+ " end='"
									+ start
									+ "' id='"
									+ idTask2
									+ 1
									+ "' color='cccccc' alpha='100' height='10'"
									+ " topPadding='19'/>";

						}

					}

				}
				idTask2++;
			}

			xml += taskCabecalho;
			xml += taskBody;
			xml += taskBody2;
			xml += taskFooter;
			/*
			 * <task name='Planned' processId='8' start='22/6/2005'
			 * end='29/7/2005' id='2-1' color='4567aa' alpha='100' height='10'
			 * topPadding='5' animation='0'/>
			 * 
			 * <task name='Actual' processId='8' start='22/6/2005'
			 * end='5/8/2005' id='2' color='cccccc' alpha='100' height='10'
			 * topPadding='19'/>
			 */
			// preenchendo o body do tasks
			// finalizando xml
			xml += "</chart>";
			// xml+= "</graph";


			return xml;

		} catch (Exception e) {
			e.printStackTrace();
			return "ooh Ohu !!! Jack Está Morto e Desapareceu!!!";
		}
	}

	/*
	 * Fim dos metodos de carregamento do chart
	 */

	public void mostrarOcorrencias(ActionEvent arg) {
		try {
			ativar = true;
			sizeZoom = 2;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void esconderOcorrencias(ActionEvent arg) {
		try {
			ativar = false;
			sizeZoom = 10;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String cancelar() {

		this.address = null;
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.remove("mBeanManterRastrearCarga");
		try {
			return "rastrearcargas.jsf";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Utility method for creating <code>Date</code> objects.
	 * 
	 * @param day
	 *            the date.
	 * @param month
	 *            the month.
	 * @param year
	 *            the year.
	 * 
	 * @return a date.
	 */
	public static Date date(final int day, final int month, final int year) {

		final Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day);
		final Date result = calendar.getTime();
		return result;

	}

	
	
	private String address;

	@Override
	public String editar() {
		//Long id = Long.valueOf(JSFRequestBean.getParameter("id"));
		try {
			this.carga = (Carga) rastrearCargaFacade.getCargaById(selectCarga.getId());

			// address = "Viena";
			// carrega FOLLOWUP
			this.followUp = rastrearCargaFacade.getFollowUpByCarga(this.carga);
			Long idAgente = carga.getAgenteCarga().getId();
			//Rota rota = carga.getRota();
			carga.setRota(cadastroFacade.getRotaById(carga.getRota().getId()));
			PessoaJuridica ag = cadastroFacade.getPessoaById(idAgente);
			this.carga.setAgenteCarga(ag);
			filtroOcorrencia.setCarga(this.carga);

			listOcorrencias2 = rastrearCargaFacade
					.listarOcorrencia(filtroOcorrencia);
			List<FollowUpImportTrecho> trechos = followUp.getTrechosFollowUp();

			for (int i = trechos.size() - 1; i >= 0; i--) {
				FollowUpImportTrecho trecho = trechos.get(i);
				if (trecho.getDtRealizado() != null) {
					Cidade city = cadastroFacade.getCidadeById(trecho
							.getCidade().getId());
					this.address = city.getNome() + ", "
							+ city.getPais().getNome();
					this.address2 = city.getNome() + ", "
							+ city.getPais().getNome();
					break;
				}

			}

			// carregando a lista de invoices
			listaInvoices = rastrearCargaFacade
					.listarInvoicesByCarga(this.carga);

			this.ocorrencia = rastrearCargaFacade.getOcorrenciaById(this.carga
					.getId());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "rastrearcarga.jsf?faces-redirect=true";
	}

	@Override
	public String excluir() {
		return null;
	}

	@Override
	public void inicializarObjetos() {
		this.carga = new Carga();
		this.ocorrencia = new Ocorrencia();

	}

	/**
	 * @coment Método de pesquisa de rotas e captura de objetos de rotas...
	 * 
	 */

	public String getContato() {
		try {
			if (carga.getAgenteCarga().getContatos() != null
					&& !carga.getAgenteCarga().getContatos().isEmpty()) {

				return carga.getAgenteCarga().getContatos().get(0)
						.getNomeResponsavel();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Sem contato";
	}

	public String getEnderecoAgente() {
		try {
			if (carga.getAgenteCarga().getEnderecos() != null
					&& !carga.getAgenteCarga().getEnderecos().isEmpty()) {

				return carga.getAgenteCarga().getEnderecos().get(0).getRua()
						+ ", "
						+ carga.getAgenteCarga().getEnderecos().get(0)
								.getNumero()
						+ ", "
						+ carga.getAgenteCarga().getEnderecos().get(0)
								.getPais().getNome();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Sem Endereco Cadastrado";
	}

	public String getFoneContato() {
		try {
			if (carga.getAgenteCarga().getContatos() != null
					&& !carga.getAgenteCarga().getContatos().isEmpty()) {

				return carga.getAgenteCarga().getContatos().get(0)
						.getTelefone();
			}
		} catch (Exception e) {
		}
		return "Sem Telefone";
	}

	@Override
	protected void refazerPesquisa() {
		MBeanPesquisarRastrearCarga mBean = (MBeanPesquisarRastrearCarga) JSFRequestBean
				.getManagedBean("mBeanPesquisarRastrearCarga");
		mBean.refazerPesquisa();

	}

	public Carga getCarga() {
		return carga;
	}

	public void setCarga(Carga carga) {
		this.carga = carga;
	}

	public Ocorrencia getOcorrencia() {
		return ocorrencia;
	}

	public void setOcorrencia(Ocorrencia ocorrencia) {
		this.ocorrencia = ocorrencia;
	}

	public TaskSeriesCollection getCollectionTasks() {
		return collectionTasks;
	}

	public void setCollectionTasks(TaskSeriesCollection collectionTasks) {
		this.collectionTasks = collectionTasks;
	}

	public List<Ocorrencia> getListOcorrencias() {
		return listOcorrencias;
	}

	public void setListOcorrencias(List<Ocorrencia> listOcorrencias) {
		this.listOcorrencias = listOcorrencias;
	}

	public BasicFiltroOcorrencia getFiltroOcorrencia() {
		return filtroOcorrencia;
	}

	public void setFiltroOcorrencia(BasicFiltroOcorrencia filtroOcorrencia) {
		this.filtroOcorrencia = filtroOcorrencia;
	}

	public FollowUpImport getFollowUp() {
		return followUp;
	}

	public void setFollowUp(FollowUpImport followUp) {
		this.followUp = followUp;
	}

	public FollowUpImportTrecho getFollowTrecho() {
		return followTrecho;
	}

	public void setFollowTrecho(FollowUpImportTrecho followTrecho) {
		this.followTrecho = followTrecho;
	}

	public List<Invoice> getListaInvoices() {
		return listaInvoices;
	}

	public void setListaInvoices(List<Invoice> listaInvoices) {
		this.listaInvoices = listaInvoices;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isAtivar() {
		return ativar;
	}

	public void setAtivar(boolean ativar) {
		this.ativar = ativar;
	}

	public Integer getSizeZoom() {
		return sizeZoom;
	}

	public void setSizeZoom(Integer sizeZoom) {
		this.sizeZoom = sizeZoom;
	}

	public List<Ocorrencia> getListOcorrencias2() {
		return listOcorrencias2;
	}

	public void setListOcorrencias2(List<Ocorrencia> listOcorrencias2) {
		this.listOcorrencias2 = listOcorrencias2;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public Carga getSelectCarga() {
		return selectCarga;
	}

	public void setSelectCarga(Carga selectCarga) {
		this.selectCarga = selectCarga;
	}

}
