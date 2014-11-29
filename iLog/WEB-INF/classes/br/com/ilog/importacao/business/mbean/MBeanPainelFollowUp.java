package br.com.ilog.importacao.business.mbean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citspresentation.mbeans.AbstractPaginacao;
import br.com.ilog.cadastro.business.entity.Canal;
import br.com.ilog.cadastro.business.entity.Cidade;
import br.com.ilog.cadastro.business.entity.Estado;
import br.com.ilog.cadastro.business.entity.Modal;
import br.com.ilog.cadastro.business.entity.ParametroCanal;
import br.com.ilog.cadastro.business.entity.PessoaJuridica;
import br.com.ilog.cadastro.business.entity.Rota;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;
import br.com.ilog.geral.presentation.util.DataUtils;
import br.com.ilog.importacao.business.entity.Carga;
import br.com.ilog.importacao.business.entity.FollowUpImport;
import br.com.ilog.importacao.business.entity.FollowUpImportTrecho;
import br.com.ilog.importacao.business.entity.Invoice;
import br.com.ilog.importacao.business.entity.StatusCarga;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroCarga;
import br.com.ilog.importacao.business.facade.ImportacaoFacade;

/**
 * Manager Bean para o painel de FollowUp de Transporte
 * 
 * @author Wisley
 * @date 23/08/2012
 * 
 */
@Controller("mBeanPainelFollowUp")
@Scope("session")
public class MBeanPainelFollowUp extends AbstractPaginacao {

	/** */
	private static final long serialVersionUID = -999040678362540028L;

	@Resource(name = "controllerImportacao")
	ImportacaoFacade facade;

	/**
	 * @coment fachada de cadastros
	 */
	@Resource(name = "controllerCadastro")
	CadastroFacade cadastrotFacade;

	// private static Logger logger =
	// LoggerFactory.getLogger(MBeanPainelFollowUp.class);

	private List<Carga> resultPainel;
	private List<Carga> lastUpdates;

	/**
	 * quantidade de linhas do painel de follow up de transporte.
	 */
	private int rows;

	private int paginaAtualPainel;

	private List<PessoaJuridica> fornecedores;
	
	public void increment(){
		rows++;
	}

	private Integer progress;
	
	@PostConstruct
	public void inicializar() {
		progress  =  10;
	}

	@Override
	public void doPesquisar(ActionEvent arg0) {
	}

	public String getFornecedor(Carga carga) {
		try {
			Carga c = facade.getCargaById(carga.getId());
			List<Invoice> inv = c.getListaDeInvoices();
			Invoice invo = inv.get(0);
			invo = facade.getInvoiceById(invo.getId(), true);
			return invo.getExportador().getNomeFantasia();
		} catch (Exception e) {
			return "";
		}
	}

	private String statusFollowUp;

	/**
	 * 
	 * @param rota
	 * @return tipo de mnodal
	 */
	public String getTipoModal(Carga carga) {
		try {
			Rota r = cadastrotFacade.getRotaById(carga.getRota().getId());
			Modal m = new Modal();
			if (r.getTipoTransporte() != null)
				m = cadastrotFacade.getModalById(r.getTipoTransporte().getId());
			if (!m.getSrcImg().equals(""))
				return m.getSrcImg();
			else
				return "";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		/**
		 * FacesContext fc = FacesContext.getCurrentInstance(); FollowUpImport
		 * follow = new FollowUpImport(); Rota rota = new Rota(); statusFollowUp
		 * = ""; try { // carregando followUp follow =
		 * facade.getFollowUpByCarga(carga); rota =
		 * cadastrotFacade.getRotaById(carga.getRota().getId());
		 * 
		 * check = true; if (follow != null) {
		 * 
		 * // Carregando trechos List<FollowUpImportTrecho> trechos = new
		 * ArrayList<FollowUpImportTrecho>(); trechos =
		 * follow.getTrechosFollowUp(); // carregando pickup
		 * FollowUpImportTrecho trecho1 = trechos.get(0); if
		 * (trecho1.getDtRealizado() != null) {
		 * 
		 * // verifica se a data realizada e menor que a estimada if
		 * (DataUtils.compararApenasData(trecho1.getDtPlanejado(),
		 * trecho1.getDtRealizado()) == 0 || DataUtils
		 * .compararApenasData(trecho1 .getDtPlanejado(), trecho1
		 * .getDtRealizado()) > 0) { check = true;
		 * 
		 * } else { check = false; }
		 * 
		 * // terminou verificação de pickup for (int j = 1; j < trechos.size();
		 * j++) { FollowUpImportTrecho t = trechos.get(j);
		 * 
		 * // verifica se é canal if (t.isCanal()) { break; } else { // verifica
		 * se o realizado nao e nulo if (t.getDtRealizado() != null) { //
		 * verifica se a data realizada e menor que a // estimada if
		 * (DataUtils.compararApenasData(t .getDtPlanejado(),
		 * t.getDtRealizado()) == 0 || DataUtils.compararApenasData(t
		 * .getDtPlanejado(), t .getDtRealizado()) > 0) { check = true;
		 * 
		 * } else { check = false; }
		 * 
		 * }
		 * 
		 * } }
		 * 
		 * // termina verificação de trechos
		 * 
		 * // começa verificação de canal... // verificar DESEMBARACO E LOM //
		 * setar o bg no index 2 e 3 FollowUpImportTrecho trecho = trechos
		 * .get(trechos.size() - 2);//
		 * follow.getTrechosFollowUp().get(follow.getTrechosFollowUp().size() if
		 * (trecho.isCanal()) { if (trecho.getDtRealizado() != null) { if
		 * (DataUtils.compararApenasData(trecho .getDtPlanejado(),
		 * trecho.getDtRealizado()) == 0 || DataUtils.compararApenasData(trecho
		 * .getDtPlanejado(), trecho .getDtRealizado()) > 0) { check = true; }
		 * else { check = false; } } } trecho = trechos.get(trechos.size() - 1);
		 * if (trecho.getDtRealizado() != null) { check = false; if
		 * (DataUtils.compararApenasData(trecho .getDtPlanejado(),
		 * trecho.getDtRealizado()) == 0 || DataUtils.compararApenasData(trecho
		 * .getDtPlanejado(), trecho .getDtRealizado()) > 0) { check = true;
		 * 
		 * } else { check = false; }
		 * 
		 * }
		 * 
		 * }
		 * 
		 * }
		 * 
		 * } catch (Exception e) { e.printStackTrace(); } if (check) { if
		 * (rota.getTipoTransporte() == TipoTransporte.AEREO) { statusFollowUp =
		 * TemplateMessageHelper.getMessage( MensagensSistema.CARGA,
		 * "lblPrevisto", fc.getViewRoot() .getLocale()); return "aereo.png"; }
		 * else if (rota.getTipoTransporte() == TipoTransporte.MAR_AR) {
		 * statusFollowUp = TemplateMessageHelper.getMessage(
		 * MensagensSistema.CARGA, "lblPrevisto", fc.getViewRoot()
		 * .getLocale());
		 * 
		 * return "aereo_maritimo.png"; } else if (rota.getTipoTransporte() ==
		 * TipoTransporte.MARITMO) { statusFollowUp =
		 * TemplateMessageHelper.getMessage( MensagensSistema.CARGA,
		 * "lblPrevisto", fc.getViewRoot() .getLocale());
		 * 
		 * return "maritimo.png"; } else { statusFollowUp =
		 * TemplateMessageHelper.getMessage( MensagensSistema.CARGA,
		 * "lblPrevisto", fc.getViewRoot() .getLocale());
		 * 
		 * return "terrestre.png"; } } else { if (rota.getTipoTransporte() ==
		 * TipoTransporte.AEREO) { statusFollowUp =
		 * TemplateMessageHelper.getMessage( MensagensSistema.CARGA,
		 * "lblAtrasado", fc.getViewRoot() .getLocale());
		 * 
		 * return "aereo.png"; } else if (rota.getTipoTransporte() ==
		 * TipoTransporte.MAR_AR) { statusFollowUp =
		 * TemplateMessageHelper.getMessage( MensagensSistema.CARGA,
		 * "lblAtrasado", fc.getViewRoot() .getLocale());
		 * 
		 * return "aereo_maritimo.png"; } else if (rota.getTipoTransporte() ==
		 * TipoTransporte.MARITMO) { statusFollowUp =
		 * TemplateMessageHelper.getMessage( MensagensSistema.CARGA,
		 * "lblAtrasado", fc.getViewRoot() .getLocale());
		 * 
		 * return "maritimo.png"; } else statusFollowUp =
		 * TemplateMessageHelper.getMessage( MensagensSistema.CARGA,
		 * "lblAtrasado", fc.getViewRoot() .getLocale());
		 * 
		 * return "terrestre.png"; }
		 **/
	}

	public Date getDataPrevista(Date dataSaida, int diasSLA) {
		Date minhaData = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dataSaida);

		// incrementa minha data mais sete dias
		calendar.add(Calendar.DAY_OF_MONTH, diasSLA);

		minhaData = calendar.getTime();

		return minhaData;
	}

	private Boolean verificador;

	/**
	 * retorna o status da carga
	 * 
	 * @return Atrasado
	 * @return EM DIA
	 */
	public String getRetornaStatusCarga(Carga carga) {
		FacesContext fc = FacesContext.getCurrentInstance();
		FollowUpImport follow = new FollowUpImport();
		verificador = null;
		boolean isLOM = false;
		Rota rota = new Rota();
		int qntFeriados = 0;
		int incluiSabEDom = 0;
		try {
			// carregando followUp
			Date dataPrevisao = new Date();
			// Date dataReal = new Date();

			follow = facade.getFollowUpByCarga(carga);
			// ta = cadastrotFacade.getRotaById(carga.getRota().getId());

			if (follow != null) {

				// Carrega rota
				rota = cadastrotFacade.getRotaById(carga.getRota().getId());

				// Carregando trechos
				List<FollowUpImportTrecho> trechos = new ArrayList<FollowUpImportTrecho>();
				trechos = follow.getTrechosFollowUp();

				// carregando pickup
				FollowUpImportTrecho trecho1 = trechos.get(0);
				if (trecho1.getDtRealizado() != null) {

					// ver a data real de previsao
					dataPrevisao = getDataPrevista(trecho1.getDtRealizado(),
							rota.getQuantidadeDias());
					/**
					 * // verifica se a data realizada e menor que a estimada if
					 * (DataUtils.compararApenasData(dataPrevisao, dataReal) ==
					 * 0 || DataUtils.compararApenasData(dataPrevisao, dataReal)
					 * > 0) { verificador = true;
					 * 
					 * } else { verificador = false; }
					 **/

					// terminou verificação de pickup
					for (int j = 1; j < trechos.size(); j++) {
						FollowUpImportTrecho t = trechos.get(j);

						// verifica se é canal
						if (t.isCanal()) {
							break;
						} else {
							// verifica se o realizado nao e nulo
							if (t.getDtRealizado() != null) {
								// verifica se a data realizada e menor que a
								// estimada
								/**
								 * if
								 * (DataUtils.compararApenasData(dataPrevisao,
								 * dataReal) == 0 ||
								 * DataUtils.compararApenasData( dataPrevisao,
								 * dataReal) > 0) { verificador = true;
								 * 
								 * } else { verificador = false; }
								 **/

							}

						}
					}

					// termina verificação de trechos

					// começa verificação de canal...
					// verificar DESEMBARACO E LOM
					// setar o bg no index 2 e 3
					FollowUpImportTrecho trecho = trechos
							.get(trechos.size() - 2);// follow.getTrechosFollowUp().get(follow.getTrechosFollowUp().size()
					if (trecho.isCanal()) {
						ParametroCanal pr = new ParametroCanal();
						if (trecho.getParametroCanal() != null) {
							pr = trecho.getParametroCanal();
							pr = cadastrotFacade.getParametroCanais(pr
									.getCanal());
						}
						if (pr != null)
							dataPrevisao = getDataPrevista(dataPrevisao, pr
									.getPrazo());
						if (trecho.getParametroCanalAtual() != null) {
							/**
							 * if (DataUtils.compararApenasData(dataPrevisao,
							 * dataReal) == 0 || DataUtils.compararApenasData(
							 * dataPrevisao, dataReal) > 0) { verificador =
							 * true; } else { verificador = false; }
							 **/
						}

					}

					trecho = trechos.get(trechos.size() - 1);
					if (trecho.getDtRealizado() != null) {
						verificador = false;
						isLOM = true;
						/**
						 * if (DataUtils .compararApenasData(dataPrevisao,
						 * dataReal) == 0 ||
						 * DataUtils.compararApenasData(dataPrevisao, dataReal)
						 * > 0) { verificador = true;
						 * 
						 * } else { verificador = false; }
						 **/

					}

				}

				FollowUpImportTrecho trecho = trechos.get(trechos.size() - 3);

				// captura o id do pais

				Cidade cidade = cadastrotFacade.getCidadeById(trecho
						.getCidade().getId());
				Estado estado = cadastrotFacade.getEstadoById(cidade
						.getEstado().getId());
				// pega o canal
				ParametroCanal canal = new ParametroCanal();
				canal = cadastrotFacade.getParametroCanais(Canal.VM);
				Calendar calendarInicial = Calendar.getInstance();
				if (trecho.getDtRealizado() != null) {
					calendarInicial.setTime(trecho.getDtRealizado());
					calendarInicial
							.add(Calendar.DAY_OF_MONTH, canal.getPrazo());
					Date dataFim = calendarInicial.getTime();
					// int dia =
					// calendarInicial.get(Calendar.DAY_OF_MONTH);

					qntFeriados = cadastrotFacade.getFeriadosUteis(trecho
							.getDtRealizado(), dataFim, estado.getPais()
							.getId());

					// verifica sabdos e domingos
					calendarInicial.setTime(trecho.getDtRealizado());

					for (int i = 1; i <= canal.getPrazo(); i++) {
						// Date data = ca
						if (isSabadoOuDomingo(calendarInicial.getTime()))
							incluiSabEDom += 1;
						calendarInicial.add(Calendar.DAY_OF_MONTH, 1);
					}
				}

			}

			int i = rota.getQuantidadeDias();
			ParametroCanal parametroCanalVermlho = new ParametroCanal();
			// parametroCanalVermlho.setCanal(Canal.VERMELHO);
			try {
				parametroCanalVermlho = cadastrotFacade
						.getParametroCanais(Canal.VM);
			} catch (Exception e) {
				e.printStackTrace();
			}
			i += parametroCanalVermlho.getPrazo();
			i += qntFeriados;
			i += incluiSabEDom;
			if (i >= follow.getTotalDiasAtual())
				verificador = true;
			else
				verificador = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (verificador != null) {
			if (!isLOM) {
				if (verificador) {

					return TemplateMessageHelper.getMessage(
							MensagensSistema.CARGA, "lblPrevisto", fc
									.getViewRoot().getLocale());
				} else {
					return TemplateMessageHelper.getMessage(
							MensagensSistema.CARGA, "lblAtrasado", fc
									.getViewRoot().getLocale());
				}
			} else {
				return TemplateMessageHelper.getMessage(MensagensSistema.CARGA,
						"lblRegular", fc.getViewRoot().getLocale());
			}
		} else {
			return " ";
		}

	}

	public static boolean isSabadoOuDomingo(Date data) {
		Calendar gc = Calendar.getInstance();
		gc.setTime(data);

		int diaSemana = gc.get(GregorianCalendar.DAY_OF_WEEK);
		return diaSemana == GregorianCalendar.SATURDAY
				|| diaSemana == GregorianCalendar.SUNDAY;
	}

	/**
	 * retorna o status da carga
	 * 
	 * @return Atrasado
	 * @return EM DIA
	 */
	public String getRetornaStatus(Carga carga) {
		FacesContext fc = FacesContext.getCurrentInstance();
		FollowUpImport follow = new FollowUpImport();
		verificador = null;
		String status = TemplateMessageHelper.getMessage(
				MensagensSistema.CARGA, "lblNoColetado", fc.getViewRoot()
						.getLocale());
		// Rota rota = new Rota();
		try {
			follow = facade.getFollowUpByCarga(carga);
			// ta = cadastrotFacade.getRotaById(carga.getRota().getId());
			if (follow != null) {

				// Carregando trechos
				List<FollowUpImportTrecho> trechos = new ArrayList<FollowUpImportTrecho>();
				trechos = follow.getTrechosFollowUp();
				// carregando pickup
				FollowUpImportTrecho trecho1 = trechos.get(0);
				if (trecho1.getDtRealizado() != null) {

					status = "Pickup";

					// terminou verificação de pickup
					for (int j = 1; j < trechos.size(); j++) {
						FollowUpImportTrecho t = trechos.get(j);

						// verifica se é canal
						if (t.isCanal()) {
							break;
						} else {
							// verifica se o realizado nao e nulo
							if (t.getDtRealizado() != null) {
								status = "Transit";

							}

						}
					}

					// termina verificação de trechos

					// começa verificação de canal...
					// verificar DESEMBARACO E LOM
					// setar o bg no index 2 e 3
					FollowUpImportTrecho trecho = trechos
							.get(trechos.size() - 2);// follow.getTrechosFollowUp().get(follow.getTrechosFollowUp().size()
					if (trecho.isCanal()) {

						if (trecho.getParametroCanalAtual() != null) {
							status = "Custom";

						}
					}
					trecho = trechos.get(trechos.size() - 1);
					if (trecho.getDtRealizado() != null) {
						status = "Lom";

					}

				}

			}

			// verificador = true;
		} catch (Exception e) {
			e.printStackTrace();

		}

		if (status != null && status.equals("Pickup")) {
			return "corstatus2";
		}
		return "corstatus1";

	}

	/**
	 * retorna o status da carga
	 * 
	 * @return Atrasado
	 * @return EM DIA
	 */
	public String getRetornaStatusCustom(Carga carga) {
		FacesContext fc = FacesContext.getCurrentInstance();
		FollowUpImport follow = new FollowUpImport();
		verificador = null;
		String status = TemplateMessageHelper.getMessage(
				MensagensSistema.CARGA, "lblNoColetado", fc.getViewRoot()
						.getLocale());
		// Rota rota = new Rota();
		try {
			follow = facade.getFollowUpByCarga(carga);
			// ta = cadastrotFacade.getRotaById(carga.getRota().getId());
			if (follow != null) {

				// Carregando trechos
				List<FollowUpImportTrecho> trechos = new ArrayList<FollowUpImportTrecho>();
				trechos = follow.getTrechosFollowUp();
				// carregando pickup
				FollowUpImportTrecho trecho1 = trechos.get(0);
				if (trecho1.getDtRealizado() != null) {

					status = "Pickup";

					// terminou verificação de pickup
					for (int j = 1; j < trechos.size(); j++) {
						FollowUpImportTrecho t = trechos.get(j);

						// verifica se é canal
						if (t.isCanal()) {
							break;
						} else {
							// verifica se o realizado nao e nulo
							if (t.getDtRealizado() != null) {
								status = "Transit";

							}

						}
					}

					// termina verificação de trechos

					// começa verificação de canal...
					// verificar DESEMBARACO E LOM
					// setar o bg no index 2 e 3
					FollowUpImportTrecho trecho = trechos
							.get(trechos.size() - 2);// follow.getTrechosFollowUp().get(follow.getTrechosFollowUp().size()
					if (trecho.isCanal()) {

						if (trecho.getParametroCanalAtual() != null) {
							status = "Custom";

						}
					}
					trecho = trechos.get(trechos.size() - 1);
					if (trecho.getDtRealizado() != null) {
						status = "Lom";

					}

				}

			}

			// verificador = true;
		} catch (Exception e) {
			e.printStackTrace();

		}

		if (status != null && status.equals("Custom")) {
			return "corstatus2";
		}
		return "corstatus1";

	}

	/**
	 * retorna o status da carga
	 * 
	 * @return Atrasado
	 * @return EM DIA
	 */
	public String getRetornaStatusLom(Carga carga) {
		FacesContext fc = FacesContext.getCurrentInstance();
		FollowUpImport follow = new FollowUpImport();
		verificador = null;
		String status = TemplateMessageHelper.getMessage(
				MensagensSistema.CARGA, "lblNoColetado", fc.getViewRoot()
						.getLocale());
		// Rota rota = new Rota();
		try {
			follow = facade.getFollowUpByCarga(carga);
			// ta = cadastrotFacade.getRotaById(carga.getRota().getId());
			if (follow != null) {

				// Carregando trechos
				List<FollowUpImportTrecho> trechos = new ArrayList<FollowUpImportTrecho>();
				trechos = follow.getTrechosFollowUp();
				// carregando pickup
				FollowUpImportTrecho trecho1 = trechos.get(0);
				if (trecho1.getDtRealizado() != null) {

					status = "Pickup";

					// terminou verificação de pickup
					for (int j = 1; j < trechos.size(); j++) {
						FollowUpImportTrecho t = trechos.get(j);

						// verifica se é canal
						if (t.isCanal()) {
							break;
						} else {
							// verifica se o realizado nao e nulo
							if (t.getDtRealizado() != null) {
								status = "Transit";

							}

						}
					}

					// termina verificação de trechos

					// começa verificação de canal...
					// verificar DESEMBARACO E LOM
					// setar o bg no index 2 e 3
					FollowUpImportTrecho trecho = trechos
							.get(trechos.size() - 2);// follow.getTrechosFollowUp().get(follow.getTrechosFollowUp().size()
					if (trecho.isCanal()) {

						if (trecho.getParametroCanalAtual() != null) {
							status = "Custom";

						}
					}
					trecho = trechos.get(trechos.size() - 1);
					if (trecho.getDtRealizado() != null) {
						status = "Lom";

					}

				}

			}

			// verificador = true;
		} catch (Exception e) {
			e.printStackTrace();

		}

		if (status != null && status.equals("Lom")) {
			return "corstatus2";
		}
		return "corstatus1";

	}

	/**
	 * retorna o status da carga
	 * 
	 * @return Atrasado
	 * @return EM DIA
	 */
	public String getRetornaStatusTransit(Carga carga) {
		FacesContext fc = FacesContext.getCurrentInstance();
		FollowUpImport follow = new FollowUpImport();
		verificador = null;
		String status = TemplateMessageHelper.getMessage(
				MensagensSistema.CARGA, "lblNoColetado", fc.getViewRoot()
						.getLocale());
		// Rota rota = new Rota();
		try {
			follow = facade.getFollowUpByCarga(carga);
			// ta = cadastrotFacade.getRotaById(carga.getRota().getId());
			if (follow != null) {

				// Carregando trechos
				List<FollowUpImportTrecho> trechos = new ArrayList<FollowUpImportTrecho>();
				trechos = follow.getTrechosFollowUp();
				// carregando pickup
				FollowUpImportTrecho trecho1 = trechos.get(0);
				if (trecho1.getDtRealizado() != null) {

					status = "Pickup";

					// terminou verificação de pickup
					for (int j = 1; j < trechos.size(); j++) {
						FollowUpImportTrecho t = trechos.get(j);

						// verifica se é canal
						if (t.isCanal()) {
							break;
						} else {
							// verifica se o realizado nao e nulo
							if (t.getDtRealizado() != null) {
								status = "Transit";

							}

						}
					}

					// termina verificação de trechos

					// começa verificação de canal...
					// verificar DESEMBARACO E LOM
					// setar o bg no index 2 e 3
					FollowUpImportTrecho trecho = trechos
							.get(trechos.size() - 2);// follow.getTrechosFollowUp().get(follow.getTrechosFollowUp().size()
					if (trecho.isCanal()) {

						if (trecho.getParametroCanalAtual() != null) {
							status = "Custom";

						}
					}
					trecho = trechos.get(trechos.size() - 1);
					if (trecho.getDtRealizado() != null) {
						status = "Lom";

					}

				}

			}

			// verificador = true;
		} catch (Exception e) {
			e.printStackTrace();

		}

		if (status != null && status.equals("Transit")) {
			return "corstatus2";
		}
		return "corstatus1";

	}

	/**
	 * public String returnStyle(Carga carga, String tipo) { FacesContext fc =
	 * FacesContext.getCurrentInstance();
	 * 
	 * // String status = getRetornaStatus(carga);
	 * 
	 * if (status != null && status.equals("Pickup")) { if
	 * (tipo.equals("pickup")) { return "corstatus2"; } else { return
	 * "corstatus1"; } } else if (status != null && status.equals("Transit")) {
	 * if (tipo.equals("pickup")) { return "corstatus2"; } else { return
	 * "corstatus1"; } } else if (status != null && status.equals("pickup")) {
	 * if (tipo.equals("")) { return "corstatus2"; } else { return "corstatus1";
	 * } } else if (status != null && status.equals("pickup")) { if
	 * (tipo.equals("")) { return "corstatus2"; } else { return "corstatus1"; }
	 * } return "corstatus1"; }
	 **/

	
	
	@SuppressWarnings("unused")
	public String getRetornaStyle(Carga carga) {
		// FacesContext fc = FacesContext.getCurrentInstance();
		FollowUpImport follow = new FollowUpImport();
		Boolean verifica = new Boolean(null);
		Rota rota = new Rota();
		boolean isLOM = false;
		int qntFeriados = 0;
		int incluiSabEDom = 0;
		try {
			// carregando followUp
			Date dataPrevisao = new Date();
			// Date dataReal = new Date();

			follow = facade.getFollowUpByCarga(carga);
			// ta = cadastrotFacade.getRotaById(carga.getRota().getId());

			if (follow != null) {

				// Carrega rota
				rota = cadastrotFacade.getRotaById(carga.getRota().getId());

				// Carregando trechos
				List<FollowUpImportTrecho> trechos = new ArrayList<FollowUpImportTrecho>();
				trechos = follow.getTrechosFollowUp();

				// carregando pickup
				FollowUpImportTrecho trecho1 = trechos.get(0);
				if (trecho1.getDtRealizado() != null) {

					// ver a data real de previsao
					dataPrevisao = getDataPrevista(trecho1.getDtRealizado(),
							rota.getQuantidadeDias());
					/**
					 * // verifica se a data realizada e menor que a estimada if
					 * (DataUtils.compararApenasData(dataPrevisao, dataReal) ==
					 * 0 || DataUtils.compararApenasData(dataPrevisao, dataReal)
					 * > 0) { verificador = true;
					 * 
					 * } else { verificador = false; }
					 **/

					// terminou verificação de pickup
					for (int j = 1; j < trechos.size(); j++) {
						FollowUpImportTrecho t = trechos.get(j);

						// verifica se é canal
						if (t.isCanal()) {
							break;
						} else {
							// verifica se o realizado nao e nulo
							if (t.getDtRealizado() != null) {
								// verifica se a data realizada e menor que a
								// estimada
								/**
								 * if
								 * (DataUtils.compararApenasData(dataPrevisao,
								 * dataReal) == 0 ||
								 * DataUtils.compararApenasData( dataPrevisao,
								 * dataReal) > 0) { verificador = true;
								 * 
								 * } else { verificador = false; }
								 **/

							}

						}
					}

					// termina verificação de trechos

					// começa verificação de canal...
					// verificar DESEMBARACO E LOM
					// setar o bg no index 2 e 3
					FollowUpImportTrecho trecho = trechos
							.get(trechos.size() - 2);// follow.getTrechosFollowUp().get(follow.getTrechosFollowUp().size()
					if (trecho.isCanal()) {
						ParametroCanal pr = new ParametroCanal();
						if (trecho.getParametroCanal() != null) {
							pr = trecho.getParametroCanal();
							pr = cadastrotFacade.getParametroCanais(pr
									.getCanal());
						}
						if (pr != null)
							dataPrevisao = getDataPrevista(dataPrevisao, pr
									.getPrazo());
						if (trecho.getParametroCanalAtual() != null) {
							/**
							 * if (DataUtils.compararApenasData(dataPrevisao,
							 * dataReal) == 0 || DataUtils.compararApenasData(
							 * dataPrevisao, dataReal) > 0) { verificador =
							 * true; } else { verificador = false; }
							 **/
						}

					}

					trecho = trechos.get(trechos.size() - 1);
					if (trecho.getDtRealizado() != null) {
						verifica = false;
						isLOM = true;
						/**
						 * if (DataUtils .compararApenasData(dataPrevisao,
						 * dataReal) == 0 ||
						 * DataUtils.compararApenasData(dataPrevisao, dataReal)
						 * > 0) { verificador = true;
						 * 
						 * } else { verificador = false; }
						 **/

					}

				}

				FollowUpImportTrecho trecho = trechos.get(trechos.size() - 3);

				// captura o id do pais

				Cidade cidade = cadastrotFacade.getCidadeById(trecho
						.getCidade().getId());
				Estado estado = cadastrotFacade.getEstadoById(cidade
						.getEstado().getId());
				// pega o canal
				ParametroCanal canal = new ParametroCanal();
				canal = cadastrotFacade.getParametroCanais(Canal.VM);
				Calendar calendarInicial = Calendar.getInstance();
				if (trecho.getDtRealizado() != null) {
					calendarInicial.setTime(trecho.getDtRealizado());
					calendarInicial
							.add(Calendar.DAY_OF_MONTH, canal.getPrazo());
					Date dataFim = calendarInicial.getTime();
					// int dia =
					// calendarInicial.get(Calendar.DAY_OF_MONTH);

					qntFeriados = cadastrotFacade.getFeriadosUteis(trecho
							.getDtRealizado(), dataFim, estado.getPais()
							.getId());

					// verifica sabdos e domingos
					calendarInicial.setTime(trecho.getDtRealizado());

					for (int i = 1; i <= canal.getPrazo(); i++) {
						// Date data = ca
						if (isSabadoOuDomingo(calendarInicial.getTime()))
							incluiSabEDom += 1;
						calendarInicial.add(Calendar.DAY_OF_MONTH, 1);
					}
				}

			}

			int i = rota.getQuantidadeDias();
			ParametroCanal parametroCanalVermlho = new ParametroCanal();
			// parametroCanalVermlho.setCanal(Canal.VERMELHO);
			try {
				parametroCanalVermlho = cadastrotFacade
						.getParametroCanais(Canal.VM);
			} catch (Exception e) {
				e.printStackTrace();
			}
			i += parametroCanalVermlho.getPrazo();
			i += qntFeriados;
			i += incluiSabEDom;
			if (i >= follow.getTotalDiasAtual())
				verifica = true;
			else
				verifica = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!isLOM) {
			if (verifica != null) {

				if (verifica) {
					return "fonte-painel-Status-green";

				} else {
					return "fonte-painel-Status";

				}
			}
			return "";
			
		} else {
			return "fonte-painel-Status-green-lom";
		}

	}

	/**
	
	public String getRetornaStyle(Carga carga) {
		// FacesContext fc = FacesContext.getCurrentInstance();
		FollowUpImport follow = new FollowUpImport();
		Boolean verifica = new Boolean(null);
		Rota rota = new Rota();
		boolean isLOM = false;
		try {
			// carregando followUp
			Date dataPrevisao = new Date();
			Date dataReal = new Date();
			// carregando followUp
			follow = facade.getFollowUpByCarga(carga);
			// ta = cadastrotFacade.getRotaById(carga.getRota().getId());
			if (follow != null) {
				// Carrega rota
				rota = cadastrotFacade.getRotaById(carga.getRota().getId());

				// Carregando trechos
				List<FollowUpImportTrecho> trechos = new ArrayList<FollowUpImportTrecho>();
				trechos = follow.getTrechosFollowUp();
				// carregando pickup
				FollowUpImportTrecho trecho1 = trechos.get(0);
				if (trecho1.getDtRealizado() != null) {

					// vera data real de previsao
					dataPrevisao = getDataPrevista(trecho1.getDtRealizado(),
							rota.getQuantidadeDias());
					// verifica se a data realizada e menor que a estimada
					if (DataUtils.compararApenasData(dataPrevisao, dataReal) == 0
							|| DataUtils.compararApenasData(dataPrevisao,
									dataReal) > 0) {
						verifica = true;

					} else {
						verifica = false;
					}

					// terminou verificação de pickup
					for (int j = 1; j < trechos.size(); j++) {
						FollowUpImportTrecho t = trechos.get(j);

						// verifica se é canal
						if (t.isCanal()) {
							break;
						} else {
							// verifica se o realizado nao e nulo
							if (t.getDtRealizado() != null) {
								// verifica se a data realizada e menor que a
								// estimada
								if (DataUtils.compararApenasData(dataPrevisao,
										dataReal) == 0
										|| DataUtils.compararApenasData(
												dataPrevisao, dataReal) > 0) {
									verifica = true;

								} else {
									verifica = false;
								}

							}

						}
					}

					// termina verificação de trechos

					// começa verificação de canal...
					// verificar DESEMBARACO E LOM
					// setar o bg no index 2 e 3
					FollowUpImportTrecho trecho = trechos
							.get(trechos.size() - 2);// follow.getTrechosFollowUp().get(follow.getTrechosFollowUp().size()
					if (trecho.isCanal()) {
						ParametroCanal pr = new ParametroCanal();
						if (trecho.getParametroCanal() != null) {
							pr = trecho.getParametroCanal();
							pr = cadastrotFacade.getParametroCanais(pr
									.getCanal());
						}
						if (pr != null)
							dataPrevisao = getDataPrevista(dataPrevisao, pr
									.getPrazo());
						if (trecho.getParametroCanalAtual() != null) {
							if (DataUtils.compararApenasData(dataPrevisao,
									dataReal) == 0
									|| DataUtils.compararApenasData(
											dataPrevisao, dataReal) > 0) {
								verifica = true;
							} else {
								verifica = false;
							}
						}
					}
					trecho = trechos.get(trechos.size() - 1);
					if (trecho.getDtRealizado() != null) {
						verifica = false;
						isLOM = true;
						if (DataUtils
								.compararApenasData(dataPrevisao, dataReal) == 0
								|| DataUtils.compararApenasData(dataPrevisao,
										dataReal) > 0) {
							verifica = true;

						} else {
							verifica = false;
						}

					}

				}

			}

			// verificador = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!isLOM) {
			if (verifica != null) {

				if (verifica) {
					return "fonte-painel-Status-green";

				} else {
					return "fonte-painel-Status";

				}
			} else {
				return "";
			}
		} else {
			return "fonte-painel-Status-green";
		}

	}
**/
	private boolean statusPoll;

	public boolean getRetornaStatus() {

		statusPoll = !statusPoll;

		return statusPoll;
	}

	/**
	 * Metodo que retorna O PERCENT atual do progresso da carga
	 */

	private boolean check;

	public Integer getPercentCarga(Carga carga) {

		FollowUpImport follow = new FollowUpImport();

		// valor do percentagem
		double percent = 0.0;
		try {
			// carregando followUp
			follow = facade.getFollowUpByCarga(carga);
			// check = false;
			if (follow != null) {
				// check = true;

				// Carregando trechos
				List<FollowUpImportTrecho> trechos = new ArrayList<FollowUpImportTrecho>();
				trechos = follow.getTrechosFollowUp();

				// carregando pickup
				FollowUpImportTrecho trecho1 = trechos.get(0);
				if (trecho1.getDtRealizado() != null) {
					percent += 10;
				}

				Integer cont = trechos.size() - 3;
				double contPercent = (double) 60 / cont;
				for (int j = 1; j < trechos.size(); j++) {
					FollowUpImportTrecho t = trechos.get(j);

					// verifica se é canal
					if (t.isCanal()) {
						break;
					} else {
						// verifica se o realizado nao e nulo
						if (t.getDtRealizado() != null) {
							percent += contPercent;
						}

					}
				}

				// verificar DESEMBARACO E LOM
				// setar o bg no index 2 e 3
				FollowUpImportTrecho trecho = trechos.get(trechos.size() - 2);// follow.getTrechosFollowUp().get(follow.getTrechosFollowUp().size()
				if (trecho.isCanal()) {
					if (trecho.getParametroCanalAtual() != null) {
						percent += 20;
					}
				}
				trecho = trechos.get(trechos.size() - 1);
				if (trecho.getDtRealizado() != null) {
					// check = false;
					percent += 10;
				}

			} else {
				return 0;
			}

		} catch (BusinessException e) {
			e.printStackTrace();
			return 0;
		}
		int s = (int)percent;
		Integer o = s;
		return o;
	}

	public String getPercentCargaTrechoUm(Carga carga) {

		FollowUpImport follow = new FollowUpImport();

		// valor do percentagem
		double percent = 0.0;
		try {
			// carregando followUp
			follow = facade.getFollowUpByCarga(carga);
			// check = false;
			if (follow != null) {
				// check = true;

				// Carregando trechos
				List<FollowUpImportTrecho> trechos = new ArrayList<FollowUpImportTrecho>();
				trechos = follow.getTrechosFollowUp();

				// carregando pickup
				FollowUpImportTrecho trecho1 = trechos.get(0);
				if (trecho1.getDtRealizado() != null) {
					percent += 10;
				}

				Integer cont = trechos.size() - 3;
				double contPercent = (double) 60 / cont;
				for (int j = 1; j < trechos.size(); j++) {
					FollowUpImportTrecho t = trechos.get(j);

					// verifica se é canal
					if (t.isCanal()) {
						break;
					} else {
						// verifica se o realizado nao e nulo
						if (t.getDtRealizado() != null) {
							percent += contPercent;
						}

					}
				}

				// verificar DESEMBARACO E LOM
				// setar o bg no index 2 e 3
				FollowUpImportTrecho trecho = trechos.get(trechos.size() - 2);// follow.getTrechosFollowUp().get(follow.getTrechosFollowUp().size()
				if (trecho.isCanal()) {
					if (trecho.getParametroCanalAtual() != null) {
						percent += 20;
					}
				}
				trecho = trechos.get(trechos.size() - 1);
				if (trecho.getDtRealizado() != null) {
					// check = false;
					percent += 10;
				}

			} else {
				return "0";
			}

		} catch (BusinessException e) {
			e.printStackTrace();
		}
		String s = String.valueOf(percent);
		return s;
	}

	public String[] getColorFundo(Carga carga) {
		FollowUpImport follow = new FollowUpImport();
		Map<Integer, String> bg = new HashMap<Integer, String>();
		try {
			follow = facade.getFollowUpByCarga(carga);
			if (follow != null) {
				int i = 0;
				for (FollowUpImportTrecho item : follow.getTrechosFollowUp()) {
					if (i == 0) {
						// verificar data de pickup
						// setar o bg no index 0
						if (item.getDtRealizado() != null) {
							if (DataUtils.compararApenasData(item
									.getDtPlanejado(), item.getDtRealizado()) == 0
									|| DataUtils.compararApenasData(item
											.getDtPlanejado(), item
											.getDtRealizado()) > 0) {

								bg
										.put(
												0,
												"background: green;font-size: 15px; font-weight: bold;	color: white;	height: 20px;	margin-right: 5px;");

							} else {
								bg
										.put(
												0,
												"background: red;font-size: 15px;font-weight: bold;	color: #008080;	height: 20px;	margin-right: 5px;");
							}
						} else {
							bg.put(0, "white");
						}

						i++;
					} else {
						break;
					}
				}
				List<FollowUpImportTrecho> trechos = follow
						.getTrechosFollowUp();
				for (int j = 1; j < trechos.size(); j++) {
					FollowUpImportTrecho t = trechos.get(j);
					if (t.isCanal()) {
						break;
					} else {
						if (t.getDtRealizado() != null) {
							if (DataUtils.compararApenasData(
									t.getDtPlanejado(), t.getDtRealizado()) == 0
									|| DataUtils.compararApenasData(t
											.getDtPlanejado(), t
											.getDtRealizado()) > 0) {

								bg
										.put(
												0,
												"background: green;font-size: 15px;font-weight: bold;	color: white;	height: 20px;	margin-right: 5px;");
								bg
										.put(
												1,
												"background: green;font-size: 15px;font-weight: bold;	color: white;	height: 20px;	margin-right: 5px;");

							} else {

								bg
										.put(
												0,
												"background: red;font-size: 15px;font-weight: bold;	color: #008080;	height: 20px;	margin-right: 5px;");
								bg
										.put(
												1,
												"background: red;font-size: 15px;font-weight: bold;	color: #008080;	height: 20px;	margin-right: 5px;");
							}
						} else {
							bg
									.put(
											1,
											"background: white;font-size: 15px;font-weight: bold;	color: #008080;	height: 20px;	margin-right: 5px;");
						}
					}

				}
				// verificar DESEMBARACO E LOM
				// setar o bg no index 2 e 3
				FollowUpImportTrecho trecho = trechos.get(trechos.size() - 2);// follow.getTrechosFollowUp().get(follow.getTrechosFollowUp().size()
				// -
				// 2
				// );
				if (trecho.isCanal()) {
					if (trecho.getDtRealizado() != null) {

						bg
								.put(
										0,
										"background: green;font-size: 15px;font-weight: bold;	color: white;	height: 20px;	margin-right: 5px;");
						bg
								.put(
										1,
										"background: green;font-size: 15px;font-weight: bold;	color: white;	height: 20px;	margin-right: 5px;");
						bg
								.put(
										2,
										"background: green;font-size: 15px;font-weight: bold;	color: white;	height: 20px;	margin-right: 5px;");
					} else {
						bg
								.put(
										2,
										"background: white;font-size: 15px;font-weight: bold;	color: #008080;	height: 20px;	margin-right: 5px;");
					}
				}
				trecho = trechos.get(trechos.size() - 1);
				if (trecho.getDtRealizado() != null) {

					if (DataUtils.compararApenasData(trecho.getDtPlanejado(),
							trecho.getDtRealizado()) == 0
							|| DataUtils.compararApenasData(trecho
									.getDtPlanejado(), trecho.getDtRealizado()) > 0) {

						bg
								.put(
										3,
										"background: green;font-size: 15px;font-weight: bold;	color: white;	height: 20px;	margin-right: 5px;");
						bg
								.put(
										2,
										"background: green;font-size: 15px;font-weight: bold;	color: white;	height: 20px;	margin-right: 5px;");
						bg
								.put(
										1,
										"background: green;font-size: 15px;font-weight: bold;	color: white;	height: 20px;	margin-right: 5px;");
						bg
								.put(
										0,
										"background: green;font-size: 15px;font-weight: bold;	color: white;	height: 20px;	margin-right: 5px;");
					} else {
						bg
								.put(
										3,
										"background: red;font-size: 15px;font-weight: bold;	color: #008080;	height: 20px;	margin-right: 5px;");
						bg
								.put(
										2,
										"background: red;font-size: 15px;font-weight: bold;	color: #008080;	height: 20px;	margin-right: 5px;");
						bg
								.put(
										1,
										"background: red;font-size: 15px;font-weight: bold;	color: #008080;	height: 20px;	margin-right: 5px;");
						bg
								.put(
										0,
										"background: red;font-size: 15px;font-weight: bold;	color: #008080;	height: 20px;	margin-right: 5px;");

					}
				} else {
					bg
							.put(
									3,
									"background: white;font-size: 15px;font-weight: bold;	color: #008080;	height: 20px;	margin-right: 5px;");
				}
			} else {
				for (int j = 0; j < 4; j++) {
					bg
							.put(
									j,
									"background: white;font-size: 15px;font-weight: bold;	color: #008080;	height: 20px;	margin-right: 5px;");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		String[] values = new String[4];

		for (int i = 0; i < bg.size(); i++) {
			values[i] = bg.get(i);
		}

		return values;
	}

	/**
	 * metodo para carregar o painel de followup de transporte.
	 */
	public void carregarPainel() {
		paginaAtualPainel++;

		rows = 8;

		int finalIndexSubList = paginaAtualPainel * rows - 1;
		resultPainel = new ArrayList<Carga>();

		try {
			BasicFiltroCarga filtro = new BasicFiltroCarga();
			filtro.setVisivel(true);
			// filtro.setStatus(StatusCarga.F);
			resultPainel = facade.listCargasPainel(filtro);

			if ((resultPainel.size() - 1) > finalIndexSubList) {
				resultPainel = resultPainel.subList((paginaAtualPainel - 1)
						* rows, finalIndexSubList + 1);
			} else {
				resultPainel = resultPainel.subList(rows
						* (paginaAtualPainel - 1), resultPainel.size());
				paginaAtualPainel = 0;
			}

			contador++;
			
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}
	private int contador = 0;
    private Carga lastCargaModificada;
	//METODO OFF PARA PAINEL FOLLOWUP INDIVIDUAL
    
	public String destinoCarga(Carga carga) {
		try {
			Rota rota = new Rota();
			// rota = cadastrotFacade.getRotaById(carga.getRota().getId());
			rota = cadastrotFacade.getRotaById(carga.getRota().getId());
			
			Cidade c = cadastrotFacade.getCidadeById(rota.getCidadeDestino()
					.getId());
			return c.getNome().toUpperCase();
		} catch (Exception e) {
			e.printStackTrace();
			return "erro na busca";
		}
	}
	
	public String atualCarga(Carga carga) {
		String cidadeAtual;
		try {
			cidadeAtual = "";
			FollowUpImport follow = facade.getFollowUpByCarga(carga);
			if (follow != null) {
				if (follow.getTrechosFollowUp() != null) {
					FollowUpImportTrecho t = follow.getTrechosFollowUp().get(0);
					if (t.getDtRealizado() != null) {
						for (FollowUpImportTrecho trecho : follow
								.getTrechosFollowUp()) {
							if (trecho != null)
								if (trecho.getDtRealizado() != null)
									cidadeAtual = trecho.getTxLocal();

						}
					} else {
						cidadeAtual = t.getTxLocal();
					}
				}
			}
			return cidadeAtual;
		} catch (Exception e) {
			cidadeAtual = "Erro na cidade atual";
			e.printStackTrace();
			return cidadeAtual;
		}

	}

	public void modificaLastUpdate() {
		
		if (contador < 10) {
			if(lastUpdates== null)
				carregarPainelIndividual();
			else
			if(lastUpdates.isEmpty()){
				carregarPainelIndividual();

			}
			lastCargaModificada = new Carga();
			if (lastUpdates.size() >= 10) {
				lastCargaModificada = lastUpdates.get(contador);
				contador++;
			} else {
				if (contador < lastUpdates.size()){
					lastCargaModificada = lastUpdates.get(contador);
				contador++;}else{
					lastCargaModificada = lastUpdates.get(0);
					contador = 0;}
			}

		} else {
			contador = 0;
		}
	}
	
	public void carregarPainelIndividual(){
		lastUpdates =  new ArrayList<Carga>();
		try {
			BasicFiltroCarga filtro = new BasicFiltroCarga();
			filtro.setVisivel(true);
			lastUpdates = facade.listCargasPainelIndividual(filtro);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Verificar se a carga esta no prazo.
	 * 
	 * @param index
	 * @return
	 */
	public String getSinalizacao(int index) {
		if (resultPainel.get(index) != null) {
			Carga carga = resultPainel.get(index);
			if (carga.getDtChegada() != null) {
				if (carga.getDtPrevista() != null
						&& DataUtils.compararApenasData(carga.getDtPrevista(),
								carga.getDtChegada()) < 0) {
					return "VERMELHO";
				}
			} else {
				if (carga.getDtPrevista() != null
						&& DataUtils.compararApenasData(carga.getDtPrevista(),
								new Date()) < 0) {
					return "VERMELHO";
				}
			}
		}
		return "VERDE";
	}

	/**
	 * Retorna a Cidade onde se encontra a carga, dependendo do Status
	 * 
	 * @param index
	 * @return
	 */
	public String getCidadeAtual(int index) {

		Carga carga = resultPainel.get(index);
		if (carga.getStatus().equals(StatusCarga.OHI)
				|| carga.getStatus().equals(StatusCarga.ITT)) {
			if (carga.getCidadeAtual() != null) {
				return carga.getCidadeAtual().getSigla().toUpperCase();
			}
		}

		return "";
	}

	/**
	 * Retorna a Cidade onde se encontra a carga, dependendo do Status
	 * 
	 * @param index
	 * @return
	 */
	public String getOrigemPainel(int index) {

		Carga carga = resultPainel.get(index);
		if (carga.getRota() != null && carga.getRota().getPaisOrigem() != null) {
			return carga.getRota().getPaisOrigem().getNome();
		}

		return "";
	}

	@Override
	public void limpar() {
		// TODO Auto-generated method stub

	}

	@Override
	public void refazerPesquisa() {
		// TODO Auto-generated method stub

	}

	public List<Carga> getResultPainel() {
		return resultPainel;
	}

	public void setResultPainel(List<Carga> resultPainel) {
		this.resultPainel = resultPainel;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getPaginaAtualPainel() {
		return paginaAtualPainel;
	}

	public void setPaginaAtualPainel(int paginaAtualPainel) {
		this.paginaAtualPainel = paginaAtualPainel;
	}

	@Override
	public int getTotalRegistros() {
		return 0;
	}

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public String getStatusFollowUp() {
		return statusFollowUp;
	}

	public void setStatusFollowUp(String statusFollowUp) {
		this.statusFollowUp = statusFollowUp;
	}

	public boolean isStatusPoll() {
		return statusPoll;
	}

	public void setStatusPoll(boolean statusPoll) {
		this.statusPoll = statusPoll;
	}

	public Boolean getVerificador() {
		return verificador;
	}

	public void setVerificador(Boolean verificador) {
		this.verificador = verificador;
	}

	public List<PessoaJuridica> getFornecedores() {
		return fornecedores;
	}

	public void setFornecedores(List<PessoaJuridica> fornecedores) {
		this.fornecedores = fornecedores;
	}

	public Integer getProgress() {
		if(progress == null)  
            progress = 10;  
        
          
        return progress;  
	}

	public void setProgress(Integer progress) {
		this.progress = progress;
	}

	public List<Carga> getLastUpdates() {
		return lastUpdates;
	}

	public void setLastUpdates(List<Carga> lastUpdates) {
		this.lastUpdates = lastUpdates;
	}

	public int getContador() {
		return contador;
	}

	public void setContador(int contador) {
		this.contador = contador;
	}

	public Carga getLastCargaModificada() {
		return lastCargaModificada;
	}

	public void setLastCargaModificada(Carga lastCargaModificada) {
		this.lastCargaModificada = lastCargaModificada;
	}

}
