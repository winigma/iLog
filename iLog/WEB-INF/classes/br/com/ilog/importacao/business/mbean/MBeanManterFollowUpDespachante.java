package br.com.ilog.importacao.business.mbean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.hibernate.validator.ValidatorHelper;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.commons.citspresentation.util.JSFRequestBean;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.Cidade;
import br.com.ilog.cadastro.business.entity.Motivo;
import br.com.ilog.cadastro.business.entity.Ocorrencia;
import br.com.ilog.cadastro.business.entity.ParametroCanal;
import br.com.ilog.cadastro.business.entity.Processo;
import br.com.ilog.cadastro.business.entity.TipoOcorrencia;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroMotivo;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroOcorrencia;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroTipoOcorrencia;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.cadastro.presentation.util.ConverterUtil;
import br.com.ilog.cadastro.presentation.util.ExceptionFiltro;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;
import br.com.ilog.geral.presentation.mbean.AbstractManterPaginacao;
import br.com.ilog.importacao.business.entity.Carga;
import br.com.ilog.importacao.business.entity.FollowUpEstimado;
import br.com.ilog.importacao.business.entity.FollowUpImport;
import br.com.ilog.importacao.business.entity.FollowUpImportTrecho;
import br.com.ilog.importacao.business.entity.Invoice;
import br.com.ilog.importacao.business.entity.ProcBroker;
import br.com.ilog.importacao.business.entity.StatusCarga;
import br.com.ilog.importacao.business.entity.StatusInvoice;
import br.com.ilog.importacao.business.facade.ImportacaoFacade;
import br.com.ilog.seguranca.presentation.mbean.MBeanSessaoUsuario;

@Controller("mBeanManterFollowUpDespachante")
@AccessScoped
public class MBeanManterFollowUpDespachante extends AbstractManterPaginacao {

	private static final long serialVersionUID = -1292236422078579623L;

	@Resource(name = "controllerImportacao")
	private ImportacaoFacade importacaoFacade;

	@Resource(name = "controllerCadastro")
	private CadastroFacade cadastro;

	@Resource(name = "mBeanSessaoUsuario")
	private MBeanSessaoUsuario sessao;

	private static Logger logger = LoggerFactory
			.getLogger(MBeanManterFollowUpDespachante.class);

	private FollowUpImport followUp;

	private Date dtCidadeOrigem, dtCidadeAtual;
	private Carga carga;
	private Boolean editarCanal; // Libera a edição na tela do canal
	
	
	private List<ParametroCanal> comboCanais;
	private ConverterUtil<ParametroCanal> canalConverter;

	private List<Ocorrencia> ocorrencias;

	private FollowUpImportTrecho followTrecho;
	
	private List<ProcBroker> comboDIs;
	
	
	/**
	 * atributo para adicionar/editar uma ocorrencia de um trecho.
	 */
	private Ocorrencia ocorrencia;
	private TipoOcorrencia tipoOcorrencia;
	private ConverterUtil<TipoOcorrencia> converterTipoOcorrencia;
	private List<Motivo> comboMotivo;
	private ConverterUtil<Motivo> converterMotivo;

	private List<TipoOcorrencia> comboTipoOcorrencia;
	
	private Boolean editarOcorrencia;
	private String descricao;
	
	public String cancelar(){
		return "followups.jsf";
	}


	/**
	 * Metodo prepara para edicao da entidade selecionada.
	 */
	@Override
	public String editar() {

		try {
			
			setCarga((Carga) importacaoFacade.getCargaById(getCarga().getId()));
			
			followUp = importacaoFacade.getFollowUpByCarga(getCarga());
			
			followTrecho = followUp.getTrechosFollowUp().get(followUp.getTrechosFollowUp().size()-3); 
			if(followTrecho.getDtRealizado()!=null && followUp.isAtivo() && carga.getStatus().equals(StatusCarga.ITT)){
				editarCanal = true;
			}
			followTrecho = followUp.getTrechosFollowUp().get(followUp.getTrechosFollowUp().size()-2);
			dtCidadeOrigem = followUp.getTrechosFollowUp().get(0).getDtRealizado();
			dtCidadeAtual= followUp.getTrechosFollowUp().get(followUp.getTrechosFollowUp().size()-3).getDtRealizado();
			
			
			// Carregar as ocorrencias da cargas
			BasicFiltroOcorrencia filtroOcorrencia = new BasicFiltroOcorrencia();
			filtroOcorrencia.setCanal(true);
			filtroOcorrencia.setAtivo(true);
			filtroOcorrencia.setCarga(getCarga());
			setOcorrencias(importacaoFacade.listarOcorrencia(filtroOcorrencia));
			
//			ocorrencias = new HashMap<Long, List<Ocorrencia>>();
//			ocorrenciasCanal = new ArrayList<Ocorrencia>();
//			
//			for (Ocorrencia o : ocorr) {
//				if ((o.getCanal() == null || (o.getCanal() != null && !o
//						.getCanal()))
//						&& (o.getIsLom() == null || (o.getIsLom() != null && !o
//								.getIsLom()))) {
//					List<Ocorrencia> ocorrs = new ArrayList<Ocorrencia>();
//					if (ocorrencias.containsKey(o.getCidade().getId())) {
//						ocorrs = ocorrencias.get(o.getCidade().getId());
//					}
//					ocorrs.add(o);
//					ocorrencias.put(o.getCidade().getId(), ocorrs);
//				} else {
//					if (o.getCanal() != null && o.getCanal()) {
//						ocorrenciasCanal.add(o);
//					} else if (o.getIsLom() != null && o.getIsLom()) {
//						ocorrenciasLom.add(o);
//					}
//				}
//			}

		} catch (BusinessException e) {
			e.printStackTrace();

			return null;
		}

		return "followup.jsf";
	}

		
	/**
	 * Prepara para receber um novo cadastro de ocorrencia.
	 */
	public void novaOcorrencia() {

		inicializarOcorrencia();

		ocorrencia.setAutor(getSessao().getUsuario());
		ocorrencia.setCarga(getCarga());
		ocorrencia.setCidade(carga.getCidadeAtual());
		editarOcorrencia = false;

	}

	

	public void editarOcorrencia() {
		try {
			ocorrencia.setCidade(cadastro.getCidadeById(ocorrencia.getCidade().getId()));
			ocorrencia.setMotivo(cadastro.getMotivoById(ocorrencia.getMotivo().getId()));
			tipoOcorrencia=cadastro.getTipoOcorrenciaById(ocorrencia.getMotivo().getTipoOcorrencia().getId());
			popularMotivos();
			popularTipoOcorrencias();
			editarOcorrencia = true;
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

	}

	public void excluirOcorrencia() {
		
		FacesContext fc = FacesContext.getCurrentInstance();
		
		try {
			//ocorrencia = new Ocorrencia();
			//ocoEditar = ocorrenciasTrecho.get(index);
			//ocorrenciasTrecho.remove(index);
			
			importacaoFacade.excluirOcorrencia(ocorrencia);
			ocorrencias.remove(ocorrencia);
			
			Messages.adicionaMensagemDeInfo(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG_EXCLUIR_SUCESSO", fc
							.getViewRoot().getLocale()));

		} catch (Exception e) {
			e.printStackTrace();
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG_GEN_007", fc.getViewRoot()
							.getLocale()));
			return;
		}

	}

	

	/**
	 * Salvar a ocorencia do trecho
	 */
	
	public void salvarOcorrencia() {
		//FacesContext fc = FacesContext.getCurrentInstance();
		try {
						
			ocorrencia.setCanal(true);
			ocorrencia.setIsLom(false);
			ocorrencia.setAtivo(true);
			List<Processo> p = new ArrayList<Processo>();
			p = cadastro.listarProcessos();
			ocorrencia.setProcesso(p.get(0));
			if (editarOcorrencia && editarOcorrencia != null){
				importacaoFacade.alterarOcorrencia(ocorrencia);
				ocorrencias.set(ocorrencias.indexOf(ocorrencia), ocorrencia);
			}
			else{
				importacaoFacade.cadastrarOcorrencia(ocorrencia);
				ocorrencias.add(ocorrencia);
			}

//			List<Ocorrencia> ocorrs = new ArrayList<Ocorrencia>();
//			if (!ocorrenciaCanal && !ocorrenciaLom) {
//				if (ocorrencias.containsKey(ocorrenciaModal.getCidade()
//						.getId())) {
//					ocorrs = ocorrencias.get(ocorrenciaModal.getCidade()
//							.getId());
//				}
//				if (!editarOcorrencia || editarOcorrencia == null)
//					ocorrs.add(ocorrenciaModal);
//				ocorrencias.put(ocorrenciaModal.getCidade().getId(),
//						ocorrs);
//
//				getOcorrenciasTrecho(ocorrenciaModal.getCidade());
//			} else {
//				if (ocorrenciaCanal) {
//					if (!editarOcorrencia || editarOcorrencia == null)
//						ocorrenciasCanal.add(ocorrenciaModal);
//					getOcorrenciasCanal(ocorrenciaModal.getCidade());
//				} else if (ocorrenciaLom) {
//					if (!editarOcorrencia || editarOcorrencia == null)
//						ocorrenciasLom.add(ocorrenciaModal);
//
//				//	getOcorrenciasLom(ocorrenciaModal.getCidade());
//				}
//			}

//			fc = FacesContext.getCurrentInstance();
//			String message = TemplateMessageHelper.getMessage(
//					MensagensSistema.SISTEMA, "MSG001", fc.getViewRoot()
//							.getLocale());
//			Messages.adicionaMensagemDeInfo(message);
//
//			if (mail != null) {
//				if (mail.getOcorrencia() != null && mail.getOcorrencia()) {
//					String msg = TemplateMessageHelper.getMessage(
//							MensagensSistema.FOLLOW_UP, "lblMsgEmail", fc
//									.getViewRoot().getLocale())
//							+ ": "
//							+ carga.getNumAPS()
//							+ "\n Status:"
//							+ TemplateMessageHelper.getMessage(
//									MensagensSistema.FOLLOW_UP,
//									"msgNewOccurence", fc.getViewRoot()
//											.getLocale())
//							+ ": "
//							+ desOcorrencia;
//
//					String ass = TemplateMessageHelper.getMessage(
//							MensagensSistema.FOLLOW_UP, "lblAtualizacaoStatus",
//							fc.getViewRoot().getLocale())
//							+ ": " + carga.getNumAPS();
//					// EnviarEmail(ass, msg);
//					MailUltil em = new MailUltil(ass, msg, this.mail);
//					em.start();
//				}
//			}

			editarOcorrencia = false;
		} catch (BusinessException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Salvar a ocorencia do trecho
	 */
//	public void salvarEditarOcorrencia() {
//		FacesContext fc = FacesContext.getCurrentInstance();
//		try {
//			String desOcorrencia = this.getOcoEditar().getDescricao();
//			this.getOcoEditar().setCanal(ocorrenciaCanal);
//			this.getOcoEditar().setIsLom(ocorrenciaLom);
//
//			importacaoFacade.alterarOcorrencia(this.getOcorrenciaModal());
//
//			List<Ocorrencia> ocorrs = new ArrayList<Ocorrencia>();
//			if (!ocorrenciaCanal && !ocorrenciaLom) {
//				if (ocorrencias.containsKey(getOcorrenciaModal().getCidade()
//						.getId())) {
//					ocorrs = ocorrencias.get(getOcorrenciaModal().getCidade()
//							.getId());
//				}
//				ocorrs.add(getOcorrenciaModal());
//				ocorrencias.put(getOcorrenciaModal().getCidade().getId(),
//						ocorrs);
//
//				getOcorrenciasTrecho(getOcorrenciaModal().getCidade());
//			} else {
//				if (ocorrenciaCanal) {
//					ocorrenciasCanal.add(this.getOcorrenciaModal());
//					getOcorrenciasCanal(getOcorrenciaModal().getCidade());
//				} else if (ocorrenciaLom) {
//					ocorrenciasLom.add(this.getOcorrenciaModal());
//					getOcorrenciasLom(getOcorrenciaModal().getCidade());
//				}
//			}
//
//			fc = FacesContext.getCurrentInstance();
//			String message = TemplateMessageHelper.getMessage(
//					MensagensSistema.SISTEMA, "MSG001", fc.getViewRoot()
//							.getLocale());
//			Messages.adicionaMensagemDeInfo(message);
//
//			/**
//			 * UIModalPanel modal = (UIModalPanel) RichFunction
//			 * .findComponent("modalNovaOcorrencia"); if (modal != null) {
//			 * modal.setShowWhenRendered(false);
//			 * AjaxContext.getCurrentInstance()
//			 * .addComponentToAjaxRender(modal); }
//			 * 
//			 * modal = (UIModalPanel) RichFunction
//			 * .findComponent("modalOcorrencias"); if (modal != null) {
//			 * modal.setShowWhenRendered(true); AjaxContext.getCurrentInstance()
//			 * .addComponentToAjaxRender(modal); }
//			 **/
//			if (mail != null) {
//				if (mail.getOcorrencia() != null && mail.getOcorrencia()) {
//					String msg = TemplateMessageHelper.getMessage(
//							MensagensSistema.FOLLOW_UP, "lblMsgEmail", fc
//									.getViewRoot().getLocale())
//							+ ": "
//							+ carga.getNumAPS()
//							+ "\n Status:"
//							+ TemplateMessageHelper.getMessage(
//									MensagensSistema.FOLLOW_UP,
//									"msgNewOccurence", fc.getViewRoot()
//											.getLocale())
//							+ ": "
//							+ desOcorrencia;
//
//					String ass = TemplateMessageHelper.getMessage(
//							MensagensSistema.FOLLOW_UP, "lblAtualizacaoStatus",
//							fc.getViewRoot().getLocale())
//							+ ": " + carga.getNumAPS();
//					EnviarEmail(ass, msg);
//				}
//			}
//
//		} catch (BusinessException e) {
//			e.printStackTrace();
//		}
//
//	}

	

	/**
	 * Salvar carga e follow up com as regras especificas.
	 */
	public String salvar() {
		/*
		 * salvar: follow up, carga e historicos
		 */
		
		//carga.setCidadeAtual(cidadeAtual);
		
		FacesContext fc = FacesContext.getCurrentInstance();

		ResourceBundle resourceBundle = TemplateMessageHelper
				.getResourceBundle(MensagensSistema.CARGA, JSFRequestBean.getLocale());
		List<String> errorMessages = ValidatorHelper
				.valida(getCarga(), TemplateMessageHelper.getResourceBundle(
								MensagensSistema.SISTEMA, fc.getViewRoot().getLocale()), resourceBundle);

		if (errorMessages.isEmpty()) {
			try {
				getCarga().setStatus(StatusCarga.ICC);
				
				
				// Fazer a soma OTD				
				followTrecho.setOtd(followTrecho.getParametroCanalAtual().getPrazo()
						- followTrecho.getParametroCanal().getPrazo());
				
				setCarga(importacaoFacade.alterarCarga(getCarga()));

				//ALTERAR STATUS DAS INVOICES
				List<Invoice> invoices = importacaoFacade.listarInvoicesByCarga(getCarga());
				for (Invoice item : invoices) {

					item.setStatus(StatusInvoice.getStatus(getCarga().getStatus()));
					item.setCidadeAtual(getCarga().getCidadeAtual());

					importacaoFacade.alterarInvoice(item);
				}

				if (followUp.getId() != null) {
					followUp.getTrechosFollowUp().set(followUp.getTrechosFollowUp().size()-2, followTrecho);
					
					// List<FollowUpEstimado> estimados = new
					// ArrayList<FollowUpEstimado>();
					// persiste os estimados
					// Date dataAlteracao = Calendar.getInstance().getTime();
					// for (FollowUpEstimado estimado : mapaEstimado.values()) {

					// estimado.setDtAlteracao(dataAlteracao);
					// estimado.setId(null);
					// estimados.add(estimado);

					// }
					/**
					 * for (FollowUpEstimado followEstimado : estimados) { if
					 * (!followTrecho.getEstimados().contains( followEstimado))
					 * { followTrecho.getEstimados().add(followEstimado); }
					 * 
					 * }
					 **/

					followUp = importacaoFacade.alterarFollowUp(followUp);

				} 

			} catch (BusinessException e) {
				e.printStackTrace();
				logger.error("error: {}", e);
				Messages.adicionaMensagemDeErro(TemplateMessageHelper
						.getMessage(ExceptionFiltro.recursiveException(e)));

				return null;
			}
		} else {
			Messages.adicionaMensagensDeErro(errorMessages);
			return null;
		}
		String message = TemplateMessageHelper.getMessage(
				MensagensSistema.SISTEMA, "MSG001", fc.getViewRoot()
						.getLocale());
		Messages.adicionaMensagemDeInfo(message);

		return "followups.jsf?faces-redirect=true";
	}


	@Override
	public String excluir() {
		return "followups.jsf";
	}

	@Override
	@PostConstruct
	public void inicializarObjetos() {

		setCarga(new Carga());
		getCarga().setCidadeAtual( new Cidade() );
		editarOcorrencia = false;

		followUp = new FollowUpImport();
		followUp.setTrechosFollowUp(new ArrayList<FollowUpImportTrecho>());
		
		this.inicializarOcorrencia();
		popularComboCanais();
		popularComboDIs();

	}

	/**
	 * Metodo para inicializar os atributos de ocorrencia.
	 */
	private void inicializarOcorrencia() {
		ocorrencia = new Ocorrencia();
		ocorrencia.setMotivo(new Motivo());
		ocorrencia.setCanal(true);
		ocorrencia.setIsLom(null);
		//ocorrencia.setDescricao("");
		
		setTipoOcorrencia(new TipoOcorrencia());

		popularTipoOcorrencias();
		popularMotivos();

	}

	/**
	 * metodo que popula combo tipo ocorrencias
	 */
	public void popularTipoOcorrencias() {
		setComboTipoOcorrencia(new ArrayList<TipoOcorrencia>());
		try {
			List<Processo> processos = cadastro.listarProcessos();
			BasicFiltroTipoOcorrencia filtroTipoOcorrencia = new BasicFiltroTipoOcorrencia();

			for (Processo processo : processos) {
				if (processo.getNome().equals("IMP")) {
					filtroTipoOcorrencia.setProcesso(processo);
				}
			}
			List<TipoOcorrencia> tipoAux = cadastro.listTypeOccurrenceImport(filtroTipoOcorrencia);
			if (!tipoAux.isEmpty()) {
				setComboTipoOcorrencia(tipoAux);
				setConverterTipoOcorrencia(new ConverterUtil<TipoOcorrencia>(
						tipoAux));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * metodo ue popula a combo de motivos...
	 */
	public void popularMotivos() {
		setComboMotivo(new ArrayList<Motivo>());

		try {
			if (getTipoOcorrencia() != null && getTipoOcorrencia().getId() != null) {

				BasicFiltroMotivo filtroMotivo = new BasicFiltroMotivo();
				filtroMotivo.setTipoOcorrencia(getTipoOcorrencia());
				List<Motivo> motivoAux =  new ArrayList<Motivo>();
			     motivoAux = cadastro
						.listarMotivoPorTipoOcorrencia(new BasicFiltroMotivo(
								filtroMotivo.getTipoOcorrencia()));
				setComboMotivo(motivoAux);
				//converterMotivo = new ConverterUtil<Motivo>(motivoAux);

			} else {
				setComboMotivo(new ArrayList<Motivo>());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void popularComboCanais() {
		setComboCanais(new ArrayList<ParametroCanal>());
		try {

			List<ParametroCanal> canais = cadastro.listarParametroCanais();
			
			if(canais != null){
				setComboCanais(canais);
			}			
			setCanalConverter(new ConverterUtil<ParametroCanal>(canais));
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}
	
	public void popularComboDIs() {
		setComboDIs(new ArrayList<ProcBroker>());
		
		try {
			setComboDIs(importacaoFacade.listarBrokerSemCarga( carga ));
			
		} catch (BusinessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	protected void refazerPesquisa() {
		MBeanPesquisarFollowUp bean = (MBeanPesquisarFollowUp) JSFRequestBean
				.getManagedBean("mBeanPesquisarFollowUp");
		bean.refazerPesquisa();
	}

	public String getNomeAutor(FollowUpEstimado item) {
		try {
			FollowUpEstimado aux = importacaoFacade.getFollowUpstimadoById(item.getId());
			return aux.getAutor().getNome();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public String getCanal(FollowUpEstimado item) {
		try {
			FollowUpEstimado aux = importacaoFacade.getFollowUpstimadoById(item.getId());
			return aux.getCanalEstimado().getCanal().name();
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return "";
	}


	/* (non-Javadoc)
	 * @see br.com.ilog.geral.presentation.mbean.AbstractManterPaginacao#getTotalRegistros()
	 */
	@Override
	public int getTotalRegistros() {
		// TODO Auto-generated method stub
		return 0;
	}


	/**
	 * @return the carga
	 */
	public Carga getCarga() {
		return carga;
	}


	/**
	 * @param carga the carga to set
	 */
	public void setCarga(Carga carga) {
		this.carga = carga;
	}


	/**
	 * @return the ocorrencias
	 */
	public List<Ocorrencia> getOcorrencias() {
		return ocorrencias;
	}


	/**
	 * @param ocorrencias the ocorrencias to set
	 */
	public void setOcorrencias(List<Ocorrencia> ocorrencias) {
		this.ocorrencias = ocorrencias;
	}


	/**
	 * @return the followTrecho
	 */
	public FollowUpImportTrecho getFollowTrecho() {
		return followTrecho;
	}


	/**
	 * @param followTrecho the followTrecho to set
	 */
	public void setFollowTrecho(FollowUpImportTrecho followTrecho) {
		this.followTrecho = followTrecho;
	}


	/**
	 * @return the editarCanal
	 */
	public Boolean getEditarCanal() {
		return editarCanal;
	}


	/**
	 * @param editarCanal the editarCanal to set
	 */
	public void setEditarCanal(Boolean editarCanal) {
		this.editarCanal = editarCanal;
	}


	/**
	 * @return the comboCanais
	 */
	public List<ParametroCanal> getComboCanais() {
		return comboCanais;
	}


	/**
	 * @param comboCanais the comboCanais to set
	 */
	public void setComboCanais(List<ParametroCanal> comboCanais) {
		this.comboCanais = comboCanais;
	}


	/**
	 * @return the canalConverter
	 */
	public ConverterUtil<ParametroCanal> getCanalConverter() {
		return canalConverter;
	}


	/**
	 * @param canalConverter the canalConverter to set
	 */
	public void setCanalConverter(ConverterUtil<ParametroCanal> canalConverter) {
		this.canalConverter = canalConverter;
	}


	/**
	 * @return the converterTipoOcorrencia
	 */
	public ConverterUtil<TipoOcorrencia> getConverterTipoOcorrencia() {
		return converterTipoOcorrencia;
	}


	/**
	 * @param converterTipoOcorrencia the converterTipoOcorrencia to set
	 */
	public void setConverterTipoOcorrencia(ConverterUtil<TipoOcorrencia> converterTipoOcorrencia) {
		this.converterTipoOcorrencia = converterTipoOcorrencia;
	}


	/**
	 * @return the comboMotivo
	 */
	public List<Motivo> getComboMotivo() {
		return comboMotivo;
	}


	/**
	 * @param comboMotivo the comboMotivo to set
	 */
	public void setComboMotivo(List<Motivo> comboMotivo) {
		this.comboMotivo = comboMotivo;
	}


	/**
	 * @return the converterMotivo
	 */
	public ConverterUtil<Motivo> getConverterMotivo() {
		return converterMotivo;
	}


	/**
	 * @param converterMotivo the converterMotivo to set
	 */
	public void setConverterMotivo(ConverterUtil<Motivo> converterMotivo) {
		this.converterMotivo = converterMotivo;
	}


	/**
	 * @return the comboTipoOcorrencia
	 */
	public List<TipoOcorrencia> getComboTipoOcorrencia() {
		return comboTipoOcorrencia;
	}


	/**
	 * @param comboTipoOcorrencia the comboTipoOcorrencia to set
	 */
	public void setComboTipoOcorrencia(List<TipoOcorrencia> comboTipoOcorrencia) {
		this.comboTipoOcorrencia = comboTipoOcorrencia;
	}


	/**
	 * @return the ocorrencia
	 */
	public Ocorrencia getOcorrencia() {
		return ocorrencia;
	}


	/**
	 * @param ocorrencia the ocorrencia to set
	 */
	public void setOcorrencia(Ocorrencia ocorrencia) {
		this.ocorrencia = ocorrencia;
	}


	/**
	 * @return the tipoOcorrencia
	 */
	public TipoOcorrencia getTipoOcorrencia() {
		return tipoOcorrencia;
	}


	/**
	 * @param tipoOcorrencia the tipoOcorrencia to set
	 */
	public void setTipoOcorrencia(TipoOcorrencia tipoOcorrencia) {
		this.tipoOcorrencia = tipoOcorrencia;
	}


	/**
	 * @return the sessao
	 */
	public MBeanSessaoUsuario getSessao() {
		return sessao;
	}


	/**
	 * @param sessao the sessao to set
	 */
	public void setSessao(MBeanSessaoUsuario sessao) {
		this.sessao = sessao;
	}


	/**
	 * @return the comboDIs
	 */
	public List<ProcBroker> getComboDIs() {
		return comboDIs;
	}


	/**
	 * @param comboDIs the comboDIs to set
	 */
	public void setComboDIs(List<ProcBroker> comboDIs) {
		this.comboDIs = comboDIs;
	}


	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}


	/**
	 * @param descricao the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}


	/**
	 * @return the dtCidadeAtual
	 */
	public Date getDtCidadeAtual() {
		return dtCidadeAtual;
	}


	/**
	 * @param dtCidadeAtual the dtCidadeAtual to set
	 */
	public void setDtCidadeAtual(Date dtCidadeAtual) {
		this.dtCidadeAtual = dtCidadeAtual;
	}


	/**
	 * @return the dtCidadeOrigem
	 */
	public Date getDtCidadeOrigem() {
		return dtCidadeOrigem;
	}


	/**
	 * @param dtCidadeOrigem the dtCidadeOrigem to set
	 */
	public void setDtCidadeOrigem(Date dtCidadeOrigem) {
		this.dtCidadeOrigem = dtCidadeOrigem;
	}

}
