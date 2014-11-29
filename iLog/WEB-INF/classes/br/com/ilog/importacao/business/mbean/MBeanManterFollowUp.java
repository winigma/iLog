package br.com.ilog.importacao.business.mbean;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.hibernate.validator.ValidatorHelper;
import br.cits.commons.citspresentation.converter.EntityConverter;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.commons.citspresentation.util.JSFRequestBean;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.Canal;
import br.com.ilog.cadastro.business.entity.Cidade;
import br.com.ilog.cadastro.business.entity.Criterio;
import br.com.ilog.cadastro.business.entity.Frete;
import br.com.ilog.cadastro.business.entity.Moeda;
import br.com.ilog.cadastro.business.entity.Motivo;
import br.com.ilog.cadastro.business.entity.Ocorrencia;
import br.com.ilog.cadastro.business.entity.ParametroCanal;
import br.com.ilog.cadastro.business.entity.PessoaJuridica;
import br.com.ilog.cadastro.business.entity.Processo;
import br.com.ilog.cadastro.business.entity.Rota;
import br.com.ilog.cadastro.business.entity.TaxaFrete;
import br.com.ilog.cadastro.business.entity.TipoOcorrencia;
import br.com.ilog.cadastro.business.entity.Trecho;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroMotivo;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroOcorrencia;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroTipoOcorrencia;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.cadastro.presentation.util.ConverterUtil;
import br.com.ilog.cadastro.presentation.util.ExceptionFiltro;
import br.com.ilog.geral.business.CodigoErroEspecifico;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;
import br.com.ilog.geral.presentation.mbean.AbstractManterPaginacao;
import br.com.ilog.geral.presentation.util.ConverterTexto;
import br.com.ilog.geral.presentation.util.DataUtils;
import br.com.ilog.geral.presentation.util.File;
import br.com.ilog.importacao.business.entity.AnexoFollowUpImp;
import br.com.ilog.importacao.business.entity.Carga;
import br.com.ilog.importacao.business.entity.FollowUpEstimado;
import br.com.ilog.importacao.business.entity.FollowUpImport;
import br.com.ilog.importacao.business.entity.FollowUpImportTrecho;
import br.com.ilog.importacao.business.entity.Invoice;
import br.com.ilog.importacao.business.entity.MailFollowUpImport;
import br.com.ilog.importacao.business.entity.ProcBroker;
import br.com.ilog.importacao.business.entity.RelatarFollowUpImport;
import br.com.ilog.importacao.business.entity.StatusCarga;
import br.com.ilog.importacao.business.entity.StatusInvoice;
import br.com.ilog.importacao.business.facade.ImportacaoFacade;
import br.com.ilog.importacao.business.util.MailUltil;
import br.com.ilog.importacao.business.util.SendMail;
import br.com.ilog.seguranca.business.entity.StatusUsuario;
import br.com.ilog.seguranca.business.entity.Usuario;
import br.com.ilog.seguranca.business.facade.SegurancaFacade;
import br.com.ilog.seguranca.presentation.mbean.MBeanSessaoUsuario;

@Controller("mBeanManterFollowUp")
@AccessScoped
public class MBeanManterFollowUp extends AbstractManterPaginacao {

	private static final long serialVersionUID = -1292236422078579623L;

	@Resource(name = "controllerImportacao")
	ImportacaoFacade facade;

	@Resource(name = "controllerCadastro")
	CadastroFacade cadastro;

	@Resource(name = "controleUsuario")
	SegurancaFacade seguranca;

	@Resource(name = "mBeanSessaoUsuario")
	MBeanSessaoUsuario sessao;

	private static Logger logger = LoggerFactory
			.getLogger(MBeanManterFollowUp.class);

	private FollowUpImport followUp;

	private List<SelectItem> moedas;
	private ConverterUtil<Moeda> moedaConverter;

	private List<SelectItem> responsaveis;
	private ConverterUtil<Usuario> responsavelConverter;

	private FollowUpImportTrecho followTrecho;

	private boolean habilitaData;

	private int indexTrecho;

	private List<Date> datasAtuais;

	private Carga carga;

	private ProcBroker broker;

	private List<SelectItem> comboStatus;

	private List<ProcBroker> comboProcBroker;

	private List<SelectItem> comboBroker;
	private EntityConverter<ProcBroker> converterBroker;

	private List<ParametroCanal> comboCanais;
	private ConverterUtil<ParametroCanal> canalConverter;

	private List<Cidade> comboCidades;
	private EntityConverter<Cidade> cidadeConverter;

	private Date dataEstimadaSelecionada;

	private ParametroCanal canalEstimadoSelecionado;

	private Map<Long, List<FollowUpEstimado>> trechosEstimado = new HashMap<Long, List<FollowUpEstimado>>();

	private Map<Long, List<Ocorrencia>> ocorrencias;

	private boolean ocorrenciaCanal;

	private boolean ocorrenciaLom;

	private List<Ocorrencia> ocorrenciasCanal;

	private List<Ocorrencia> ocorrenciasLom;

	/**
	 * atributo para adicionar/editar uma ocorrencia de um trecho.
	 */
	private Ocorrencia ocorrenciaModal;
	private TipoOcorrencia tipoOcorrencia;
	private ConverterUtil<TipoOcorrencia> converterTipoOcorrencia;
	private List<Motivo> comboMotivo;
	private ConverterUtil<Motivo> converterMotivo;

	private List<Ocorrencia> ocorrenciasTrecho;
	private List<TipoOcorrencia> comboTipoOcorrencia;

	private Integer paginaAtualOcorrencias;

	private Cidade cidadeOcorrencia;

	private List<Integer> totalEstimados;
	private List<Integer> totalAtuais;

	private List<FollowUpEstimado> estimados;

	private List<FollowUpEstimado> estimadosSistema;
	private Map<Long, FollowUpEstimado> mapaEstimado = new HashMap<Long, FollowUpEstimado>();
	private Usuario user;

	/**
	 * Datas do follow estimadas, antes da modificacao.
	 */
	private FollowUpImportTrecho trechoAnterior;
	private Map<Long, Date> datasFollowUp = new HashMap<Long, Date>();

	private List<AnexoFollowUpImp> anexosFollowUp;

	private int paginaAtualAnexos;

	private List<AnexoFollowUpImp> removeListAnexos;

	private List<PessoaJuridica> fornecedores;

	private boolean excluir;

	private RelatarFollowUpImport mail;

	private String email;

	private Carga selectCarga;

	public void salvarMail() {

	}

	/**
	 * @author Manoel.Neto
	 * @date 07/11/2012
	 * @comment evento responsavel por transformar o byte em stream para efetuar
	 *          o donwload
	 */
	public StreamedContent getBaixarArquivo(AnexoFollowUpImp arq) {
		StreamedContent file;

		file = new DefaultStreamedContent(new ByteArrayInputStream(arq.getAnexo()), arq.getMimeType(),
				arq.getNomeArquivo());
		return file;

	}

	public String cancelar() {
		return "followups.jsf";
	}

	public void removermail(int index) {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			mail.getMails().remove(index);

			Messages.adicionaMensagemDeInfo(TemplateMessageHelper.getMessage(
					MensagensSistema.FOLLOW_UP, "msgRemoveMail", fc
							.getViewRoot().getLocale()));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void EnviarEmail(String assunto, String mensagem) {
		try {
			// Capturando todos os e-mails cadastrados para aviso
			SendMail enviarEmail = new SendMail();

			String destinatarios = "";
			for (MailFollowUpImport email : mail.getMails()) {
				// if (usuario.isAvisoMII())
				destinatarios += email.getMail().trim() + ";";
			}
			if (destinatarios.length() > 0) {
				destinatarios = destinatarios.substring(0,
						destinatarios.length() - 1);
				enviarEmail.postMail(assunto, mensagem, destinatarios);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addMail(ActionEvent args) {
		MailFollowUpImport m = new MailFollowUpImport();
		if (!email.equals("")) {
			m.setContato(email);
			m.setRelatar(mail);
			m.setMail(email);
			mail.getMails().add(m);
		}
		email = "";
	}

	public void habilitarMail() {
		if (mail == null) {
			mail = new RelatarFollowUpImport();
			mail.setMails(new ArrayList<MailFollowUpImport>());
			mail.setFollowUp(followUp);
		} else {
			if (mail.getMails() != null) {

			} else {
				mail.setMails(new ArrayList<MailFollowUpImport>());
			}
		}
	}

	/**
	 * Metodo prepara para edicao da entidade selecionada.
	 */
	@Override
	public String editar() {

		Long idCarga = selectCarga.getId();

		try {

			file = new DefaultStreamedContent();
			carregado = false;
			fornecedores = new ArrayList<PessoaJuridica>();
			canalEstimadoSelecionado = null;
			estimadosSistema = new ArrayList<FollowUpEstimado>();
			totalEstimados = new ArrayList<Integer>();

			popularComboStatus();

			carga = (Carga) facade.getCargaById(idCarga);

			popularComboProcBroker();

			if (carga.getProcBroker() != null) {
				broker = carga.getProcBroker();
			}

			List<Invoice> listaInvoices = facade.listarInvoicesByCarga(
					this.carga, true);
			if (!listaInvoices.isEmpty()) {
				for (Invoice invoice : listaInvoices) {
					Invoice inv = facade.getInvoiceById(invoice.getId(), true);
					PessoaJuridica exp = new PessoaJuridica();
					exp = cadastro.getPessoaById(inv.getExportador().getId());
					if (!fornecedores.contains(exp)) {
						System.out.println(fornecedores.contains(exp));
						fornecedores.add(inv.getExportador());
					}
				}
			}

			followUp = facade.getFollowUpByCarga(carga);

			estimados = new ArrayList<FollowUpEstimado>();
			mapaEstimado = new HashMap<Long, FollowUpEstimado>();
			datasFollowUp = new HashMap<Long, Date>();

			datasAtuais = new ArrayList<Date>();

			anexosFollowUp = new ArrayList<AnexoFollowUpImp>();
			removeListAnexos = new ArrayList<AnexoFollowUpImp>();

			if (followUp == null) {
				excluir = false;
				edicao = false;
				followUp = new FollowUpImport();
				followUp.setAtivo(true);
				// mail.setFollowUp(followUp);
				// followUp.setMoeda(new Moeda());

				carregarFollowUp();

			} else {
				mail = facade.getRelatorByFollowUp(followUp);
				if (mail == null)
					mail = new RelatarFollowUpImport();
				edicao = true;
				if (followUp.isAtivo() == true) {
					excluir = true;
				} else {
					excluir = false;
				}

				preencheListaDatas();
				if (followUp.getMoeda() != null) {
					followUp.setMoeda(cadastro.getMoedaById(followUp.getMoeda()
							.getId()));
				}

				if (followUp.getResponsavel() != null) {
					followUp.setResponsavel(seguranca.getUsuarioById(followUp
							.getResponsavel().getId()));
				}
				// carregar os estimados apenas se estiver salvo o followUp
				// TODO para o caso de nao ter salvo
				trechosEstimado = new HashMap<Long, List<FollowUpEstimado>>();
				for (FollowUpImportTrecho tre : followUp.getTrechosFollowUp()) {
					tre = facade.getFollowUpImportTrechoById(tre.getId());
					if (tre.getEstimados() != null
							&& !tre.getEstimados().isEmpty())
						trechosEstimado.put(tre.getId(), tre.getEstimados());
					else
						trechosEstimado.put(tre.getId(),
								new ArrayList<FollowUpEstimado>());

					if (datasFollowUp != null
							&& !datasFollowUp.containsKey(tre.getId())) {
						datasFollowUp.put(tre.getId(), tre.getDtPlanejado());
					}
				}

				// INICIALIZA LISTA DE ANEXOS E REMOCAO
				anexosFollowUp = facade.getAnexosFollowUp(followUp);
				if (anexosFollowUp != null) {
					if (anexosFollowUp.isEmpty()) {
						anexosFollowUp = new ArrayList<AnexoFollowUpImp>();
					}
				}

			}

			calcularTotaisDias();

			// Carregar as ocorrencias da cargas
			BasicFiltroOcorrencia filtroOcorrencia = new BasicFiltroOcorrencia();
			filtroOcorrencia.setAtivo(true);
			filtroOcorrencia.setCarga(carga);
			List<Ocorrencia> ocorr = facade.listarOcorrencia(filtroOcorrencia);
			ocorrencias = new HashMap<Long, List<Ocorrencia>>();
			ocorrenciasCanal = new ArrayList<Ocorrencia>();
			ocorrenciasLom = new ArrayList<Ocorrencia>();

			for (Ocorrencia o : ocorr) {
				if ((o.getCanal() == null || (o.getCanal() != null && !o
						.getCanal()))
						&& (o.getIsLom() == null || (o.getIsLom() != null && !o
								.getIsLom()))) {
					List<Ocorrencia> ocorrs = new ArrayList<Ocorrencia>();
					if (ocorrencias.containsKey(o.getCidade().getId())) {
						ocorrs = ocorrencias.get(o.getCidade().getId());
					}
					ocorrs.add(o);
					ocorrencias.put(o.getCidade().getId(), ocorrs);
				} else {
					if (o.getCanal() != null && o.getCanal()) {
						ocorrenciasCanal.add(o);
					} else if (o.getIsLom() != null && o.getIsLom()) {
						ocorrenciasLom.add(o);
					}
				}
			}
			setPaginaAtualOcorrencias(1);

		} catch (BusinessException e) {
			e.printStackTrace();

			return null;
		}

		edicao = true;

		return "followup.jsf";
	}

	/**
	 * Metodo para realizar o calculo do frete para a carga.
	 */
	public void calcularFreteCarga() {
		try {

			FacesContext fc = FacesContext.getCurrentInstance();
			if (carga.getPesoBrutoHawb() != null
					&& carga.getPesoCubadoHawb() != null) {
				Frete frete = cadastro
						.getFreteByRota(this.getCarga().getRota());
				if (frete != null) {
					BigDecimal peso;
					if (carga.getPesoBrutoHawb().compareTo(
							carga.getPesoCubadoHawb()) > 0) {
						peso = carga.getPesoBrutoHawb();
					} else {
						peso = carga.getPesoCubadoHawb();
					}
					BigDecimal valorFrete = null;
					for (TaxaFrete taxa : frete.getTaxasFrete()) {
						if (taxa.getCriterio().equals(Criterio.MEQ)
								&& peso.compareTo(taxa.getPeso()) < 0) {

							valorFrete = BigDecimal.ZERO;
							valorFrete = valorFrete.multiply(taxa.getValor());

						} else if (taxa.getCriterio().equals(Criterio.MAQ)
								&& peso.compareTo(taxa.getPeso()) > 0) {

							valorFrete = BigDecimal.ZERO;
							valorFrete = peso.multiply(taxa.getValor());
						}
					}
					if (valorFrete != null) {

						BigDecimal taxasExtras = BigDecimal.ZERO;
						taxasExtras = peso.multiply(frete.getTxCombustivel())
								.add(peso.multiply(frete.getTxSeguro()));

						// taxasExtras = taxasExtras.add( peso.multiply(
						// frete.getTxSeguro() ) );
						if (valorFrete.compareTo(frete.getVlMinimo()) < 0) {
							followUp.setValorFrete(frete.getVlMinimo().add(
									taxasExtras));
						} else {
							followUp.setValorFrete(valorFrete.add(taxasExtras));
						}
						followUp.setMoeda(cadastro.getMoedaById(frete
								.getMoeda().getId()));
					} else {
						String message = TemplateMessageHelper.getMessage(
								MensagensSistema.FRETE,
								"MSG_TAXA_FRETE_NOT_FOUND", fc.getViewRoot()
										.getLocale());
						Messages.adicionaMensagemDeErro(message);
						return;
					}
				} else {
					String message = TemplateMessageHelper.getMessage(
							MensagensSistema.FRETE, "MSG_FRETE_NOT_FOUND", fc
									.getViewRoot().getLocale());
					Messages.adicionaMensagemDeErro(message);
					return;
				}
			} else {
				String message = TemplateMessageHelper.getMessage(
						MensagensSistema.FRETE, "MSG_PESOS_REQUIRED", fc
								.getViewRoot().getLocale());
				Messages.adicionaMensagemDeErro(message);
				return;
			}

		} catch (BusinessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getSupliers(List<PessoaJuridica> pj) {
		String nome = "";
		if (pj != null) {
			for (PessoaJuridica p : pj) {
				if (nome.equals(""))
					nome = p.getNomeFantasia();
				else
					nome = nome + ", " + p.getNomeFantasia();
			}
			return nome;
		} else
			return "";
	}

	/**
	 * Prenche a lista de datas atuais para que o sistema tenha um controle
	 * sobre quais datas podem ser atualizadas.
	 */
	private void preencheListaDatas() {
		datasAtuais = new ArrayList<Date>();

		if (followUp.getTrechosFollowUp() != null) {
			Date real = null;
			for (FollowUpImportTrecho fit : followUp.getTrechosFollowUp()) {
				real = fit.getDtRealizado();
				if (!fit.isCanal()) {
					if (fit.getDtRealizado() != null) {
						datasAtuais.add(real);
					} else {
						break;
					}
				} else {
					if (fit.getParametroCanalAtual() != null) {
						datasAtuais.add(real);
					} else
						break;
				}
			}
		}

	}

	private void calcularTotaisDiasEstimados() {

		if (followUp.getId() != null) {
			Long idPrimeiro = followUp.getTrechosFollowUp().get(0).getId();
			Long idUltimo = followUp.getTrechosFollowUp()
					.get(followUp.getTrechosFollowUp().size() - 1).getId();

			for (int i = 0; i < trechosEstimado.get(idPrimeiro).size(); i++) {
				
				try {
					totalEstimados.add(DataUtils.diferencaEmDias(trechosEstimado
							.get(idPrimeiro).get(i).getDtEstimada(),
							trechosEstimado.get(idUltimo).get(i).getDtEstimada()));
				} catch (Exception e) {
					
				}
				
			}

		} else {
			totalEstimados.add(DataUtils.diferencaEmDias(estimadosSistema
					.get(0).getDtEstimada(),
					estimadosSistema.get(estimadosSistema.size() - 1)
							.getDtEstimada()));

		}
	}

	/**
	 * Calculo dos dias de followUp
	 */
	private void calcularTotaisDias() {
		//totalEstimados = new ArrayList<Integer>();

		if (followUp.getTrechosFollowUp() != null
				&& !followUp.getTrechosFollowUp().isEmpty()) {
			int size = followUp.getTrechosFollowUp().size();

			//datas estimadas
			Date inicio = followUp.getTrechosFollowUp().get(0).getDtPlanejado();
			Date fim = followUp.getTrechosFollowUp().get(size - 1)
					.getDtPlanejado();

			//total de dias estimados
			followUp.setTotalDiasEstimado(DataUtils
					.diferencaEmDias(inicio, fim));
							
			totalEstimados.add( followUp.getTotalDiasEstimado() );
			
			//calcula as datas reais
			calcularDatasAtuais();
		}

	}

	/**
	 * Metodo faz o calculo dos dias reais para os trechos do follow up.
	 * 
	 */
	public void calcularDatasAtuais() {
		Date inicio = null;
		
		Date fim = null;
		for (FollowUpImportTrecho t : followUp.getTrechosFollowUp()) {
			
			t.setOtd(null);
			if (!t.isCanal() && t.getDtRealizado() != null) {
				
				if ( inicio == null )
					inicio = t.getDtRealizado();
				
				fim = t.getDtRealizado();
				
				t.setOtd(DataUtils.diferencaEmDias(t.getDtPlanejado(),
						t.getDtRealizado()));
				
			} else if (t.isCanal() && t.getParametroCanal() != null
					&& t.getParametroCanalAtual() != null) {
				
				t.setOtd(t.getParametroCanalAtual().getPrazo()
						- t.getParametroCanal().getPrazo());
				try {
					Cidade cidade = cadastro.getCidadeById(t.getCidade()
							.getId());
					
					Date dtFinal = DataUtils.somarDiasUteis(fim, t
							.getParametroCanalAtual().getPrazo());
					int feriados = getFeriadosUteis(fim, dtFinal, cidade
							.getPais().getId());
					fim = DataUtils.somarDiasUteis(dtFinal, feriados);
					t.setDtRealizado(fim);
					
				} catch (BusinessException e) {
					e.printStackTrace();
				}
			}
		}
		followUp.setTotalDiasAtual(DataUtils.diferencaEmDias(inicio, fim));
		
	}
	
	/**
	 * Preenche a combobox com as DIS sem carga associadas
	 */
	public void popularComboProcBroker() {
		comboProcBroker = new ArrayList<ProcBroker>();
		comboBroker = new ArrayList<SelectItem>();

		try {
			comboProcBroker = facade.listarBrokerSemCarga(carga);
			for (ProcBroker pb : comboProcBroker) {
				comboBroker.add(new SelectItem(pb, pb.getNrDI()));
			}
			converterBroker = new EntityConverter<ProcBroker>(comboProcBroker);

		} catch (BusinessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Popular o combo de cidades.
	 */
	public void popularCidades() {
		comboCidades = new ArrayList<Cidade>();
		try {

			comboCidades = cadastro.listarCidades(null);

		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metodo carrega e salva um follow up.
	 * 
	 * @throws BusinessException
	 * 
	 */
	public void carregarFollowUp() throws BusinessException {
		if (carga != null && carga.getRota() != null) {

			followUp.setCarga(carga);

			followUp.setTrechosFollowUp(new ArrayList<FollowUpImportTrecho>());

			Date dataAtual = carga.getDtColeta();

			Rota rota = cadastro.getRotaById(carga.getRota().getId());

			FollowUpImportTrecho trechoFollow;

			Trecho trecho = new Trecho();
			for (Trecho t : rota.getTrechos()) {

				trechoFollow = new FollowUpImportTrecho();
				trechoFollow.setTrecho(t);
				trechoFollow.setCidade(t.getCidadeOrigem());
				trechoFollow.setTxLocal(t.getCidadeOrigem().getNome());
				trechoFollow.setFollowUp(followUp);
				trechoFollow.setDtPlanejado(dataAtual);
				trechoFollow.setDiasPrevistos(t.getQuantidadeDias());

				followUp.getTrechosFollowUp().add(trechoFollow);

				dataAtual = DataUtils.somarDias(dataAtual,
						t.getQuantidadeDias());
				trecho = t;

			}

			// DESTINO
			trechoFollow = new FollowUpImportTrecho();
			trechoFollow.setFollowUp(followUp);
			trechoFollow.setTrecho(null);
			trechoFollow.setCidade(rota.getCidadeDestino());
			trechoFollow.setTxLocal(rota.getCidadeDestino().getNome());
			trechoFollow.setDtPlanejado(dataAtual);

			followUp.getTrechosFollowUp().add(trechoFollow);

			// VERIFICA SE A CIDADE DESTINO TEM DESEMBARACO
			if (true) {
				// if ( trecho.getCidadeDestino().getDesembaraco() != null
				// && trecho.getCidadeDestino().getDesembaraco() ){

				// Recupera com o maior tempo para a simulação
				ParametroCanal canal = cadastro.getParametroCanais(Canal.VM);

				// DESEMBARACO
				trechoFollow = new FollowUpImportTrecho();
				trechoFollow.setCanal(true);
				trechoFollow.setFollowUp(followUp);
				trechoFollow.setTrecho(null);
				trechoFollow.setTxLocal("CANAL");
				trechoFollow.setCidade(trecho.getCidadeDestino());
				trechoFollow.setParametroCanal(canal);

				// recupera as datas incio e fim, fim conforme o prazo da
				// receita
				Date inicial = dataAtual;
				Date dtFinal = DataUtils.somarDiasUteis(dataAtual,
						canal.getPrazo());

				// VERIFICA SE POSSUI FERIADO PARA O PERIODO QUE FICA NO CANAL
				int qtdFeriados = getFeriadosUteis(inicial, dtFinal,
						trechoFollow.getCidade().getPais().getId());

				/*
				 * recupera a proximo data uteis somando a quantidade de
				 * feriados.
				 */
				dtFinal = DataUtils.somarDiasUteis(dtFinal, qtdFeriados);

				/*
				 * Dias previstos, recupera o dia em que chegou no canal
				 * 'inicial' e a diferenca de dias até a data final, pois
				 * recupera os feriados e dias úteis.
				 */
				trechoFollow.setDiasPrevistos(DataUtils.diferencaEmDias(
						inicial, dtFinal));

				/*
				 * A data planejada do DESEMBARACO é a data que vai sair para
				 * chegar na LOM.
				 */
				trechoFollow.setDtPlanejado(dtFinal);

				followUp.getTrechosFollowUp().add(trechoFollow);

				// PROXIMA DATA PARA LOM CONTANDO COM A QUANTIDADE DE DIAS DO
				// CANAL
				dataAtual = dtFinal;

			}

			if (true) {
				// if ( trecho.getCidadeDestino().getFilial() != null &&
				// trecho.getCidadeDestino().getFilial() ) {
				// LOM
				trechoFollow = new FollowUpImportTrecho();
				trechoFollow.setFollowUp(followUp);
				trechoFollow.setTrecho(null);
				trechoFollow.setCidade(trecho.getCidadeDestino());
				trechoFollow.setTxLocal("FOXCONN");

				// MESMA DATA PARA O DESTINO FINAL
				trechoFollow.setDtPlanejado(dataAtual);

				followUp.getTrechosFollowUp().add(trechoFollow);
			}
			// SETA A DATA PREVISTA DA ENTREGA
			followUp.setDtEntregaPlanejada(dataAtual);

			// salva o followup
			// facade.cadastrarFollowUp( followUp );

			// salva a data prevista para a carga
			carga.setDtPrevista(dataAtual);
			// facade.alterarCarga(carga);

			// salvar historico do followUp para o sistema
			// TODO para o caso de novo followUp
			trechosEstimado = new HashMap<Long, List<FollowUpEstimado>>();
			Date dtAlteracao = Calendar.getInstance().getTime();

			for (FollowUpImportTrecho tre : followUp.getTrechosFollowUp()) {
				FollowUpEstimado estimado = new FollowUpEstimado();
				estimado.setFollowUpTrecho(tre);
				estimado.setSitema(true);
				estimado.setDtEstimada(tre.getDtPlanejado());
				estimado.setDtAlteracao(dtAlteracao);

				if (tre.isCanal()) {
					estimado.setCanalEstimado(tre.getParametroCanal());
				}
				tre.setEstimados(new ArrayList<FollowUpEstimado>());
				tre.getEstimados().add(estimado);

				estimadosSistema.add(estimado);

			}

		}
	}

	/**
	 * metodo retorna para as datas e um pais a quantidade de feriados em dias
	 * uteis.
	 * 
	 * @param inicial
	 * @param dtFinal
	 * @param pais
	 * @return
	 * @throws BusinessException
	 */
	private int getFeriadosUteis(Date inicial, Date dtFinal, Long idPais)
			throws BusinessException {

		int qtdFeriados = cadastro.getFeriadosUteis(inicial, dtFinal, idPais);

		return qtdFeriados;
	}

	/**
	 * Selecionar o item para abrir o modal.
	 * 
	 * @param index
	 */
	public void selecionarItemFollowUp(int index) {
		try {
			indexTrecho = index;

			habilitaData = false;
			trechoAnterior = new  FollowUpImportTrecho();
			
			if (indexTrecho == datasAtuais.size()
					|| indexTrecho == datasAtuais.size() - 1) {
				habilitaData = true;
			}
			followTrecho = followUp.getTrechosFollowUp().get(indexTrecho);
			
			if(index >0){
				trechoAnterior = followUp.getTrechosFollowUp().get(indexTrecho-1);
				
				if(trechoAnterior.getDtRealizado()!=null){
					habilitaData = false;
				}else
					habilitaData = true;
			}else{
				habilitaData = false;
			}
			

			if (followTrecho.isCanal()
					&& followTrecho.getParametroCanal() != null) {
				ParametroCanal canal = cadastro.getParametroCanais(followTrecho
						.getParametroCanal().getCanal());
				followTrecho.setParametroCanal(canal);
				if (followTrecho.getParametroCanalAtual() != null) {
					canal = cadastro.getParametroCanais(followTrecho
							.getParametroCanalAtual().getCanal());
					followTrecho.setParametroCanalAtual(canal);
				}
				// if (canalEstimadoSelecionado == null)
				canalEstimadoSelecionado = followTrecho.getParametroCanal();
			}

			dataEstimadaSelecionada = followTrecho.getDtPlanejado();
			setPaginaAtual(1);

			/**
			 * UIModalPanel modal = (UIModalPanel)
			 * RichFunction.findComponent("modalItinerario"); if (modal != null)
			 * { modal.setShowWhenRendered(true);
			 * AjaxContext.getCurrentInstance()
			 * .addComponentToAjaxRender(modal); }
			 **/

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Prepara para receber um novo cadastro de ocorrencia.
	 */
	public void novaOcorrencia() {

		inicializarOcorrencia();

		MBeanSessaoUsuario bean = (MBeanSessaoUsuario) JSFRequestBean
				.getManagedBean("mBeanSessaoUsuario");
		user = bean.getUsuario();
		getOcorrenciaModal().setAutor(this.getUser());

		getOcorrenciaModal().setCarga(this.getCarga());

		try {
			cidadeOcorrencia = cadastro.getCidadeById(this
					.getCidadeOcorrencia().getId());
		} catch (BusinessException e) {
		}
		getOcorrenciaModal().setCidade(this.getCidadeOcorrencia());

	}

	private Ocorrencia ocoEditar;

	public void editarOcorrencia(int index) {
		try {
			ocoEditar = new Ocorrencia();
			ocoEditar = ocorrenciasTrecho.get(index);
			ocoEditar.setCidade(cadastro.getCidadeById(ocoEditar.getCidade()
					.getId()));
			Motivo moti = cadastro.getMotivoById(ocoEditar.getMotivo().getId());
			ocoEditar.setMotivo(moti);
			tipoOcorrencia = cadastro.getTipoOcorrenciaById(ocoEditar
					.getMotivo().getTipoOcorrencia().getId());
			editarOcorrencia = true;
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

	}

	public void excluirOcorrencia(int index) {

		FacesContext fc = FacesContext.getCurrentInstance();

		try {
			ocoEditar = new Ocorrencia();
			ocoEditar = ocorrenciasTrecho.get(index);
			ocorrenciasTrecho.remove(index);
			facade.excluirOcorrencia(ocoEditar);

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

	private Boolean editarOcorrencia;

	/**
	 * Salvar a ocorencia do trecho
	 */

	private String description;

	public void salvarOcorrencia() {
		FacesContext fc = FacesContext.getCurrentInstance();
		try {
			if (editarOcorrencia && editarOcorrencia != null) {
				ocorrenciaModal = new Ocorrencia();
				ocorrenciaModal = ocoEditar;
			}
			String desOcorrencia = description;
			this.getOcorrenciaModal().setDescricao(description);
			this.getOcorrenciaModal().setCanal(ocorrenciaCanal);
			this.getOcorrenciaModal().setIsLom(ocorrenciaLom);
			this.getOcorrenciaModal().setAtivo(true);
			List<Processo> p = new ArrayList<Processo>();
			p = cadastro.listarProcessos();
			this.getOcorrenciaModal().setProcesso(p.get(0));
			if (editarOcorrencia && editarOcorrencia != null)
				facade.alterarOcorrencia(ocorrenciaModal);
			else
				facade.cadastrarOcorrencia(this.getOcorrenciaModal());

			List<Ocorrencia> ocorrs = new ArrayList<Ocorrencia>();
			if (!ocorrenciaCanal && !ocorrenciaLom) {
				if (ocorrencias.containsKey(getOcorrenciaModal().getCidade()
						.getId())) {
					ocorrs = ocorrencias.get(getOcorrenciaModal().getCidade()
							.getId());
				}
				if (!editarOcorrencia || editarOcorrencia == null)
					ocorrs.add(getOcorrenciaModal());
				ocorrencias.put(getOcorrenciaModal().getCidade().getId(),
						ocorrs);

				getOcorrenciasTrecho(getOcorrenciaModal().getCidade());
			} else {
				if (ocorrenciaCanal) {
					if (!editarOcorrencia || editarOcorrencia == null)
						ocorrenciasCanal.add(this.getOcorrenciaModal());
					getOcorrenciasCanal(getOcorrenciaModal().getCidade());
				} else if (ocorrenciaLom) {
					if (!editarOcorrencia || editarOcorrencia == null)
						ocorrenciasLom.add(this.getOcorrenciaModal());

					getOcorrenciasLom(getOcorrenciaModal().getCidade());
				}
			}

			fc = FacesContext.getCurrentInstance();
			String message = TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG001", fc.getViewRoot()
							.getLocale());
			Messages.adicionaMensagemDeInfo(message);

			// UIModalPanel modal = (UIModalPanel)
			// RichFunction.findComponent("modalNovaOcorrencia");
			// if (modal != null) {
			// modal.setShowWhenRendered(false);
			// AjaxContext.getCurrentInstance().addComponentToAjaxRender(modal);
			// }

			// modal = (UIModalPanel)
			// RichFunction.findComponent("modalOcorrencias");
			// if (modal != null) {
			// modal.setShowWhenRendered(true);
			// AjaxContext.getCurrentInstance().addComponentToAjaxRender(modal);
			// }

			if (mail != null) {
				if (mail.getOcorrencia() != null && mail.getOcorrencia()) {
					String msg = TemplateMessageHelper.getMessage(
							MensagensSistema.FOLLOW_UP, "lblMsgEmail", fc
									.getViewRoot().getLocale())
							+ ": "
							+ carga.getNumAPS()
							+ "\n Status:"
							+ TemplateMessageHelper.getMessage(
									MensagensSistema.FOLLOW_UP,
									"msgNewOccurence", fc.getViewRoot()
											.getLocale())
							+ ": "
							+ desOcorrencia;

					String ass = TemplateMessageHelper.getMessage(
							MensagensSistema.FOLLOW_UP, "lblAtualizacaoStatus",
							fc.getViewRoot().getLocale())
							+ ": " + carga.getNumAPS();
					// EnviarEmail(ass, msg);
					MailUltil em = new MailUltil(ass, msg, this.mail);
					em.start();
				}
			}

			editarOcorrencia = false;
		} catch (BusinessException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Salvar a ocorencia do trecho
	 */
	public void salvarEditarOcorrencia() {
		FacesContext fc = FacesContext.getCurrentInstance();
		try {
			String desOcorrencia = this.getOcoEditar().getDescricao();
			this.getOcoEditar().setCanal(ocorrenciaCanal);
			this.getOcoEditar().setIsLom(ocorrenciaLom);

			facade.alterarOcorrencia(this.getOcorrenciaModal());

			List<Ocorrencia> ocorrs = new ArrayList<Ocorrencia>();
			if (!ocorrenciaCanal && !ocorrenciaLom) {
				if (ocorrencias.containsKey(getOcorrenciaModal().getCidade()
						.getId())) {
					ocorrs = ocorrencias.get(getOcorrenciaModal().getCidade()
							.getId());
				}
				ocorrs.add(getOcorrenciaModal());
				ocorrencias.put(getOcorrenciaModal().getCidade().getId(),
						ocorrs);

				getOcorrenciasTrecho(getOcorrenciaModal().getCidade());
			} else {
				if (ocorrenciaCanal) {
					ocorrenciasCanal.add(this.getOcorrenciaModal());
					getOcorrenciasCanal(getOcorrenciaModal().getCidade());
				} else if (ocorrenciaLom) {
					ocorrenciasLom.add(this.getOcorrenciaModal());
					getOcorrenciasLom(getOcorrenciaModal().getCidade());
				}
			}

			fc = FacesContext.getCurrentInstance();
			String message = TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG001", fc.getViewRoot()
							.getLocale());
			Messages.adicionaMensagemDeInfo(message);

			/**
			 * UIModalPanel modal = (UIModalPanel) RichFunction
			 * .findComponent("modalNovaOcorrencia"); if (modal != null) {
			 * modal.setShowWhenRendered(false);
			 * AjaxContext.getCurrentInstance()
			 * .addComponentToAjaxRender(modal); }
			 * 
			 * modal = (UIModalPanel) RichFunction
			 * .findComponent("modalOcorrencias"); if (modal != null) {
			 * modal.setShowWhenRendered(true); AjaxContext.getCurrentInstance()
			 * .addComponentToAjaxRender(modal); }
			 **/
			if (mail != null) {
				if (mail.getOcorrencia() != null && mail.getOcorrencia()) {
					String msg = TemplateMessageHelper.getMessage(
							MensagensSistema.FOLLOW_UP, "lblMsgEmail", fc
									.getViewRoot().getLocale())
							+ ": "
							+ carga.getNumAPS()
							+ "\n Status:"
							+ TemplateMessageHelper.getMessage(
									MensagensSistema.FOLLOW_UP,
									"msgNewOccurence", fc.getViewRoot()
											.getLocale())
							+ ": "
							+ desOcorrencia;

					String ass = TemplateMessageHelper.getMessage(
							MensagensSistema.FOLLOW_UP, "lblAtualizacaoStatus",
							fc.getViewRoot().getLocale())
							+ ": " + carga.getNumAPS();
					EnviarEmail(ass, msg);
				}
			}

		} catch (BusinessException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 */
	public void getOcorrenciasTrecho(Cidade cidade) {

		this.inicializarOcorrencia();

		ocorrenciasTrecho = new ArrayList<Ocorrencia>();
		Long i = cidade.getId();
		try {
			cidadeOcorrencia = cadastro.getCidadeById(i);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (ocorrencias.containsKey(cidade.getId())) {
			ocorrenciasTrecho = ocorrencias.get(cidade.getId());
			setPaginaAtualOcorrencias(1);
		}

		ocorrenciaCanal = false;
		ocorrenciaLom = false;
	}

	public void getOcorrenciasCanal(Cidade cidade) {

		this.inicializarOcorrencia();

		ocorrenciasTrecho = new ArrayList<Ocorrencia>();
		cidadeOcorrencia = cidade;
		ocorrenciaModal.setCanal(true);

		if (ocorrenciasCanal != null && !ocorrenciasCanal.isEmpty()) {
			ocorrenciasTrecho = getOcorrenciasCanal();
			setPaginaAtualOcorrencias(1);
		}

		ocorrenciaCanal = true;
		ocorrenciaLom = false;

	}

	public void getOcorrenciasLom(Cidade cidade) {

		this.inicializarOcorrencia();

		ocorrenciasTrecho = new ArrayList<Ocorrencia>();
		cidadeOcorrencia = cidade;
		ocorrenciaModal.setIsLom(true);

		if (ocorrenciasLom != null && !ocorrenciasLom.isEmpty()) {
			ocorrenciasTrecho = getOcorrenciasLom();
			setPaginaAtualOcorrencias(1);
		}

		ocorrenciaCanal = false;
		ocorrenciaLom = true;

	}

	public List<Ocorrencia> getOcorrenciasTrecho() {
		return ocorrenciasTrecho;
	}

	/**
	 * Retorna o numero de registros da ocorrencia.
	 * 
	 * @param cidade
	 * @return
	 */
	public int getQtdadeOcorrencias(Cidade cidade) {
		if (ocorrencias.containsKey(cidade.getId())) {
			return ocorrencias.get(cidade.getId()).size();
		}
		return 0;
	}

	/**
	 * Total de registros do ocorrencias por cidade do trecho selecionado.
	 * 
	 * @return
	 */
	public Integer getTotalRegistrosOcorrencia() {
		if (cidadeOcorrencia != null
				&& ocorrencias.containsKey(cidadeOcorrencia.getId())) {
			return ocorrencias.get(cidadeOcorrencia.getId()).size();
		}
		return 0;
	}

	/**
	 * motar o scroll infor para a lista de ocorrencias.
	 * 
	 * @return
	 */
	public String getScrollerInfoOcorrencias() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();

			int regInicial = (this.getPaginaAtualOcorrencias() - 1) * 5 + 1;
			int regFinal = Math.min(regInicial + 5 - 1,
					this.getTotalRegistrosOcorrencia());

			String retorno = "";
			if (regFinal != 0)
				retorno = regInicial
						+ " - "
						+ regFinal
						+ " "
						+ TemplateMessageHelper.getMessage("lblDe", fc
								.getViewRoot().getLocale()) + " "
						+ this.getTotalRegistrosOcorrencia();

			return retorno;
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}

	/**
	 * montar o scroll infor.
	 */
	public String getScrollerInfo() {
		FacesContext fc = FacesContext.getCurrentInstance();

		int regInicial = (this.getPaginaAtual() - 1) * 3 + 1;
		int regFinal = Math.min(regInicial + 3 - 1, this.getTotalRegistros());

		String retorno = "";
		if (regFinal != 0)
			retorno = regInicial
					+ " - "
					+ regFinal
					+ " "
					+ TemplateMessageHelper.getMessage("lblDe", fc
							.getViewRoot().getLocale()) + " "
					+ this.getTotalRegistros();

		return retorno;
	}

	/**
	 * calcula a altura para o modal de ocorrencias
	 * 
	 * @return
	 */
	public String getAlturaModalOcorrencias() {

		if (ocorrenciasTrecho == null) {
			return "200";
		}
		// tamanho inicial para o rodape e cabecalho
		int inicial = 190;
		int fim = 0;
		// base para ser multiplicado conforme a quantidade de registos
		int base = 51;
		if (ocorrenciasTrecho.size() < 5) {

			fim = base * ocorrenciasTrecho.size();
			fim = fim + inicial;

		} else {
			fim = 400;
		}

		return fim + "";
	}

	/**
	 * Faz a verificacao do followup
	 * 
	 * @param arg
	 */
	public void verificarFollowUp() {
		FacesContext fc = FacesContext.getCurrentInstance();
		MBeanSessaoUsuario bean = (MBeanSessaoUsuario) JSFRequestBean
				.getManagedBean("mBeanSessaoUsuario");

		fc = FacesContext.getCurrentInstance();

		if (bean != null)
			user = bean.getUsuario();

		if (habilitaData) {
			// int index =
			// followUp.getTrechosFollowUp().lastIndexOf(followTrecho);

			if (!followTrecho.isCanal()) {
				if (followTrecho.getDtRealizado() == null) {
					// remove da lista apenas se for a ultima data preenchida
					if (indexTrecho + 1 == datasAtuais.size()) {
						datasAtuais.remove(indexTrecho);
					}
				} else {
					// add na lista
					if (indexTrecho == datasAtuais.size()) {
						datasAtuais.add(indexTrecho,
								followTrecho.getDtPlanejado());
					}
					if (followTrecho.getTxLocal().equals("LOM")) {
						carga.setStatus(StatusCarga.AT);

					}
					if (indexTrecho == 0) {
					}
					
					calcularDatasAtuais();

				}
			} else {
				if (followTrecho.getParametroCanalAtual() == null) {
					carga.setCanal(null);
					if (indexTrecho + 1 == datasAtuais.size()) {
						datasAtuais.remove(indexTrecho);
					}
				} else {
					// add na lista
					if (indexTrecho == datasAtuais.size()) {
						datasAtuais.add(indexTrecho, new Date());
					}
					carga.setCanal(followTrecho.getParametroCanalAtual());
					calcularDatasAtuais();
				}
			}

		}

		// VERIFICAR O CAMPO DA DATA ATUAL
		if (followTrecho.getDtRealizado() != null
				&& DataUtils.compararApenasData(followTrecho.getDtRealizado(),
						new Date()) > 0) {
			followTrecho.setDtRealizado(null);
			Messages.adicionaMensagemDeInfo(TemplateMessageHelper.getMessage(
					MensagensSistema.FOLLOW_UP, "msgDataMaiorHoje", fc
							.getViewRoot().getLocale()));

			/**
			 * UIModalPanel modal = (UIModalPanel)
			 * RichFunction.findComponent("modalItinerario"); if (modal != null)
			 * { modal.setShowWhenRendered(true);
			 * AjaxContext.getCurrentInstance()
			 * .addComponentToAjaxRender(modal); }
			 **/

		} else {

			// CHAMAR O MODAL APENAS SE HOUVE MUDANCA
			if (DataUtils.compararApenasData(followTrecho.getDtPlanejado(),
					dataEstimadaSelecionada) != 0
					|| (followTrecho.isCanal() && !followTrecho
							.getParametroCanal().equals(
									canalEstimadoSelecionado))) {
				if(followTrecho.getDtPlanejado()!=null){
					org.primefaces.context.RequestContext.getCurrentInstance().execute("modalConfirmacao.show();");
				}
				/**
				 * UIModalPanel modal = (UIModalPanel)
				 * RichFunction.findComponent("modalConfirModifData"); if (modal
				 * != null) { modal.setShowWhenRendered(true);
				 * AjaxContext.getCurrentInstance().addComponentToAjaxRender(
				 * modal);
				 * 
				 * }
				 **/

				// Cria os estimados para cada trecho com as data planejadas.
//				for (FollowUpImportTrecho item : followUp.getTrechosFollowUp()) {
//
//					FollowUpEstimado followEstimado = new FollowUpEstimado();
//					followEstimado.setFollowUpTrecho(item);
//					followEstimado.setDtEstimada(item.getDtPlanejado());
//					followEstimado.setAutor(user);
//					followEstimado.setSitema(false);
//					// dataCargaEstimada = followEstimado.getDtEstimada();
//					// carga.setDtPrevista(dataCargaEstimada);
//					if (item.isCanal()) {
//						followEstimado.setCanalEstimado(item
//								.getParametroCanal());
//					}
//					if (followUp.getId() != null) {
//						mapaEstimado.put(item.getId(), followEstimado);
//					} else {
//						estimados.add(followEstimado);
//					}
//				}

			}
			//calcularTotaisDias();
			calcularDatasAtuais();

		}

		//modificarProximos();
	}

	/**
	 * Modificar apenas o atual.
	 * 
	 * @param ae
	 */
	public void modificarAtual() {

		// VERIFICAR SE O CANAL FOI MODIFICADO, E FAZ O CALCULO DE DIAS
		// PREVISTOS
		if (followTrecho.isCanal() && canalEstimadoSelecionado != null) {
			try {
				if (!canalEstimadoSelecionado.equals(followTrecho
						.getParametroCanal())) {

					/*
					 * recupera a data inicial subtraindo os dias previstos.
					 */
					Date inicial = DataUtils.somarDias(
							followTrecho.getDtPlanejado(),
							-followTrecho.getDiasPrevistos());
					Date dtFinal = DataUtils.somarDiasUteis(inicial,
							followTrecho.getParametroCanal().getPrazo());
					Cidade cidade = cadastro.getCidadeById(followTrecho
							.getCidade().getId());

					int feriados = getFeriadosUteis(inicial, dtFinal, cidade
							.getPais().getId());
					dtFinal = DataUtils.somarDiasUteis(dtFinal, feriados);

					followTrecho.setDiasPrevistos(DataUtils.diferencaEmDias(
							inicial, dtFinal));
					followTrecho.setDtPlanejado(dtFinal);

				}
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		}

		if (!followTrecho.isCanal()
				&& DataUtils.compararApenasData(followTrecho.getDtPlanejado(),
						datasFollowUp.get(followTrecho.getId())) != 0) {
			// ALTERAR O HISTORICO
			FollowUpEstimado followEstimado = mapaEstimado.get(followTrecho
					.getId());
			followEstimado.setDtEstimada(followTrecho.getDtPlanejado());
		}

		// Apos a modificacao de dias calcular novamente
		calcularTotaisDias();
	}

	/**
	 * Modifica os valores de data para os proximos trechos
	 * 
	 * @param ae
	 */
	public void modificarProximos() {
		int i = indexTrecho;// followUp.getTrechosFollowUp().lastIndexOf(followTrecho);

		
		Date dataEstimada = followTrecho.getDtPlanejado();
		

		for (int j = i; j < followUp.getTrechosFollowUp().size(); j++) {

			FollowUpImportTrecho trechoAux = followUp.getTrechosFollowUp().get(j);
			trechoAux.setDtPlanejado(dataEstimada);
			FollowUpEstimado estimado = new FollowUpEstimado();
			estimado.setAutor(user);
			estimado.setDtAlteracao(new Date());
			estimado.setFollowUpTrecho(trechoAux);
			estimado.setDtEstimada(dataEstimada);

			// adicionar historico
			// VERIFICAR SE O CANAL FOI MODIFICADO, E FAZ O CALCULO DE DIAS
			// PREVISTOS
			if (trechoAux.isCanal() /* && canalEstimadoSelecionado != null */) {
				try {
					/*
					 * recupera a data inicial subtraindo os dias previstos,
					 * apenas se tiver sido o canal que esteja sofrendo
					 * alteração.
					 */
					Date inicial = trechoAux.getDtPlanejado();
					estimado.setCanalEstimado( trechoAux.getParametroCanal() );
					
					if (canalEstimadoSelecionado != null) {
						estimado.setCanalEstimado(canalEstimadoSelecionado);
						inicial = DataUtils.somarDias(
								trechoAux.getDtPlanejado(),
								-trechoAux.getDiasPrevistos());

					}
					Date dtFinal = DataUtils.somarDiasUteis(inicial, trechoAux
							.getParametroCanal().getPrazo());

					Cidade cidade = cadastro.getCidadeById(trechoAux
							.getCidade().getId());
					int feriados = getFeriadosUteis(inicial, dtFinal, cidade
							.getPais().getId());

					dtFinal = DataUtils.somarDiasUteis(dtFinal, feriados);

					trechoAux.setDiasPrevistos(DataUtils.diferencaEmDias(
							inicial, dtFinal));
					trechoAux.setDtPlanejado(dtFinal);
					estimado.setDtEstimada(dtFinal);
					/*
					 * A estimada deve voltar a data inicial, pois faz o calculo
					 * abaixo novamente.
					 */
					dataEstimada = inicial;

					/*
					 * if ( !canalEstimadoSelecionado.equals(
					 * trechoAux.getParametroCanal() ) ) { }
					 */
				} catch (BusinessException e) {
					e.printStackTrace();
				}
			}

			// SE A DATA MUDOU MODIFICA O HISTORICO
//			if (trechoAux.getId() != null
//					&& DataUtils.compararApenasData(trechoAux.getDtPlanejado(),
//							datasFollowUp.get(trechoAux.getId())) != 0) {
//				// ALTERAR O HISTORICO
//				FollowUpEstimado followEstimado = mapaEstimado.get(trechoAux.getId());
//				followEstimado.setDtEstimada(dataEstimada);
//			}

			dataEstimada = DataUtils.somarDias(dataEstimada,
					trechoAux.getDiasPrevistos());

//			if (mapaEstimado.containsKey(trechoAux.getId()) || true) {
//			}
//			if (followUp.getId() != null) {
//			} else {
//			}
			if (trechoAux.getEstimados() == null) {
				trechoAux.setEstimados(new ArrayList<FollowUpEstimado>());
				trechoAux.getEstimados().add(estimado);
			} else
				trechoAux.getEstimados().add(estimado);

			//trechoAux.setDtPlanejado(dataEstimada);

			followUp.getTrechosFollowUp().set(j, trechoAux);

		}

		// Calcular os dias apos mudanca das datas
		calcularTotaisDias();
	}

	public void limparFollowUpTrecho(ActionEvent event) {
		followTrecho = new FollowUpImportTrecho();
	}

	/**
	 * Salvar carga e follow up com as regras especificas.
	 */
	@Override
	public String salvar() {
		/*
		 * salvar: follow up, carga e historicos
		 */

		carga.setProcBroker(broker);

		FacesContext fc = FacesContext.getCurrentInstance();

		ResourceBundle resourceBundle = TemplateMessageHelper
				.getResourceBundle(MensagensSistema.CARGA,
						JSFRequestBean.getLocale());
		List<String> errorMessages = ValidatorHelper
				.valida(carga,
						TemplateMessageHelper.getResourceBundle(
								MensagensSistema.SISTEMA, fc.getViewRoot()
										.getLocale()), resourceBundle);

		if (errorMessages.isEmpty()) {
			try {

				verificarDataRealizadaFinal();
				carga.setOtd(followUp.getTotalDiasAtual());

				// carga.setProcBroker( broker );

				carga = facade.alterarCarga(carga);

				// ALTERAR STATUS DAS INVOICES
				List<Invoice> invoices = facade.listarInvoicesByCarga(carga);
				for (Invoice item : invoices) {

					item.setStatus(StatusInvoice.getStatus(carga.getStatus()));
					item.setCidadeAtual(carga.getCidadeAtual());

					facade.alterarInvoice(item);
				}

				if (followUp.getId() != null) {
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

					followUp = facade.alterarFollowUp(followUp);

					// atualizar lista de anexos.
					atualizarAnexos(anexosFollowUp, removeListAnexos);
					if (mail.getId() == null && mail != null) {
						if (mail.getFollowUp() == null)
							mail.setFollowUp(followUp);
						facade.cadastrarRelatarMail(mail);
					} else {
						if (mail.getId() != null)
							facade.alterarRelatarMail(mail);
					}
				} else {
					// SALVAR NOVO FOLLOWUP
					followUp = facade.cadastrarFollowUp(followUp);
					if (mail.getId() == null && mail != null)
						facade.cadastrarRelatarMail(mail);
					carga = facade.alterarCarga(carga);
					/**
					 * for (FollowUpImportTrecho t :
					 * followUp.getTrechosFollowUp()) { FollowUpEstimado
					 * estimado = estimadosSistema
					 * .get(followUp.getTrechosFollowUp().indexOf(t));
					 * estimado.setFollowUpTrecho(t);
					 * 
					 * // Salvar o estimado para o sistema //
					 * facade.cadastrarEstimado(estimado); }
					 **/
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
		// refazerPesquisa();

		return "followups.jsf";
	}

	/**
	 * Atualizacao dos anexos do follow up de importacao.
	 * 
	 * @author Heber Santiago
	 * @since 05/12/2011
	 * 
	 * @param anexosFollowUp2
	 * @param removeListAnexos2
	 * @throws BusinessException
	 */
	private void atualizarAnexos(List<AnexoFollowUpImp> anexosFollowUp2,
			List<AnexoFollowUpImp> removeListAnexos2) throws BusinessException {

		facade.excluirAnexoFollowUp(removeListAnexos2);
		facade.adicionarAnexosFollowUp(anexosFollowUp2);

	}

	/**
	 * Atualiza a data de chegada de carga no destino final e seta ultima data
	 * prevista em carga.
	 */
	private void verificarDataRealizadaFinal() {
		if (followUp != null && followUp.getTrechosFollowUp() != null) {
			FollowUpImportTrecho fuit = followUp.getTrechosFollowUp().get(
					followUp.getTrechosFollowUp().size() - 1);
			if (fuit != null) {
				carga.setDtChegada(fuit.getDtRealizado());
				carga.setDtPrevista(fuit.getDtPlanejado());
			}

		}
	}

	/**
	 * @coment mensagem de erro caso o tipo de arquivo nao seja o especificado
	 */
	public void messagemTipoNaoEspecificado(ActionEvent args) {
		FacesContext fc = FacesContext.getCurrentInstance();
		Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
				MensagensSistema.INVOICE, "msgTypeFileReject", fc.getViewRoot()
						.getLocale()));
	}

	/**
	 * @brief Metodo que realiza o download de um arquivo a partir de um evento
	 *        do JSP
	 * @param ActionEvent
	 *            event - Evento da actionListener da pagina JSF
	 */
	public void baixarAnexoFollowUp(Integer index) {

		try {
			if (anexosFollowUp.get(index) != null) {
				File file = new File();
				file.setData(anexosFollowUp.get(index).getAnexo());
				file.setName(anexosFollowUp.get(index).getNomeArquivo());
				file.setMime(anexosFollowUp.get(index).getMimeType());

				File.fileDonwload(file);

			}
		} catch (Exception e) {
			BusinessException be = new BusinessException(
					CodigoErroEspecifico.MSG_ERRO_DONWLOAD_ARQUIVO);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, be));
		}
	}

	/**
	 * Remocao do item da lista de anexos do follow up.
	 * 
	 * @param index
	 */
	public void removerAnexo(int index) {
		if (anexosFollowUp != null && !anexosFollowUp.isEmpty()) {

			// verifica se precisa remover do banco.
			if (anexosFollowUp.get(index).getId() != null) {
				if (!removeListAnexos.contains(anexosFollowUp.get(index))) {
					removeListAnexos.add(anexosFollowUp.get(index));
				}
			}

			// remove da lista de anexos
			anexosFollowUp.remove(index);
		}
	}

	public String getScrollerInfoAnexos() {
		FacesContext fc = FacesContext.getCurrentInstance();

		int regInicial = (this.getPaginaAtualAnexos() - 1) * 5 + 1;
		int regFinal = Math.min(regInicial + 5 - 1, this.getTotalAnexos());

		String retorno = "";
		if (regFinal != 0)
			retorno = regInicial
					+ " - "
					+ regFinal
					+ " "
					+ TemplateMessageHelper.getMessage("lblDe", fc
							.getViewRoot().getLocale()) + " "
					+ this.getTotalAnexos();

		return retorno;
	}

	/**
	 * Retorna o tamanho da lista de anexos.
	 * 
	 * @return
	 */
	public int getTotalAnexos() {
		if (anexosFollowUp != null) {
			return anexosFollowUp.size();
		}
		return 0;
	}

	@Override
	public String excluir() {
		try {
			followUp.setAutorCancelamento(sessao.getUsuario());
			carga.setStatus(StatusCarga.CO);
			followUp.setAtivo(false);
			carga.getListaDeOcorrencias();
			carga.setCanal(null);
			carga.setOtd(null);
			facade.alterarCarga(carga);

			// apaga os trechos de followup
			for (FollowUpImportTrecho trecho : followUp.getTrechosFollowUp()) {
				trecho.setTrecho(null);
			}

			facade.alterarFollowUp(followUp);
			for (List<Ocorrencia> listOcorrenciaImport : ocorrencias.values()) {
				for (Ocorrencia ocorrenciaImport : listOcorrenciaImport) {
					ocorrenciaImport.setAtivo(false);
					ocorrenciaImport.setLido(true);
					facade.alterarOcorrencia(ocorrenciaImport);

				}
			}
			if (!ocorrenciasCanal.isEmpty() && ocorrenciasCanal != null) {
				for (Ocorrencia ocorrenciaImport : ocorrenciasCanal) {
					ocorrenciaImport.setAtivo(false);
					ocorrenciaImport.setLido(true);
					facade.alterarOcorrencia(ocorrenciaImport);

				}
			}
			if (!ocorrenciasLom.isEmpty() && ocorrenciasLom != null) {
				for (Ocorrencia ocorrenciaImport : ocorrenciasLom) {
					ocorrenciaImport.setAtivo(false);
					ocorrenciaImport.setLido(true);
					facade.alterarOcorrencia(ocorrenciaImport);
				}
			}
		} catch (BusinessException e) {
			e.printStackTrace();
			return null;

		}
		return "followups.jsf";
	}

	@Override
	@PostConstruct
	public void inicializarObjetos() {
		edicao = false;

		email = "";

		broker = new ProcBroker();

		carga = new Carga();
		carga.setCidadeAtual(new Cidade());
		editarOcorrencia = false;

		followUp = new FollowUpImport();
		followUp.setTrechosFollowUp(new ArrayList<FollowUpImportTrecho>());
		mail = new RelatarFollowUpImport();
		mail.setMails(new ArrayList<MailFollowUpImport>());
		// mail.setFollowUp(followUp);

		followTrecho = new FollowUpImportTrecho();

		this.popularCidades();
		this.inicializarOcorrencia();
		popularComboCanais();

	}

	/**
	 * Metodo para inicializar os atributos de ocorrencia.
	 */
	private void inicializarOcorrencia() {
		ocorrenciaModal = new Ocorrencia();
		ocorrenciaModal.setMotivo(new Motivo());
		ocorrenciaModal.setCanal(null);
		ocorrenciaModal.setIsLom(null);

		tipoOcorrencia = new TipoOcorrencia();

		popularTipoOcorrencias();
		popularMotivos();

	}

	/**
	 * metodo que popula combo tipo ocorrencias
	 */
	public void popularTipoOcorrencias() {
		comboTipoOcorrencia = new ArrayList<TipoOcorrencia>();
		// filtroTipoOcorrencia.setProcesso()
		try {
			List<Processo> processos = cadastro.listarProcessos();
			BasicFiltroTipoOcorrencia filtroTipoOcorrencia = new BasicFiltroTipoOcorrencia();

			for (Processo processo : processos) {
				if (processo.getNome().equals("IMP")) {
					filtroTipoOcorrencia.setProcesso(processo);
				}
			}
			List<TipoOcorrencia> tipoAux = cadastro
					.listTypeOccurrenceImport(filtroTipoOcorrencia);
			if (!tipoAux.isEmpty()) {
				comboTipoOcorrencia = tipoAux;
				converterTipoOcorrencia = new ConverterUtil<TipoOcorrencia>(
						tipoAux);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * metodo ue popula a combo de motivos...
	 */
	public void popularMotivos() {
		comboMotivo = new ArrayList<Motivo>();

		try {
			if (tipoOcorrencia != null && tipoOcorrencia.getId() != null) {

				BasicFiltroMotivo filtroMotivo = new BasicFiltroMotivo();
				filtroMotivo.setTipoOcorrencia(tipoOcorrencia);
				List<Motivo> motivoAux = new ArrayList<Motivo>();
				motivoAux = cadastro
						.listarMotivoPorTipoOcorrencia(new BasicFiltroMotivo(
								filtroMotivo.getTipoOcorrencia()));
				comboMotivo = motivoAux;
				// converterMotivo = new ConverterUtil<Motivo>(motivoAux);

			} else {
				comboMotivo = new ArrayList<Motivo>();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * campo moedas
	 */
	public List<SelectItem> getMoedas() {
		try {
			moedas = new ArrayList<SelectItem>();
			List<Moeda> moedasAux;
			moedasAux = cadastro.listarMoedas();
			for (Moeda m : moedasAux) {
				moedas.add(new SelectItem(m, m.getSigla()));
			}
			moedaConverter = new ConverterUtil<Moeda>(moedasAux);
		} catch (BusinessException e) {
			e.printStackTrace();
		}

		return moedas;
	}

	/*
	 * campo Responsaveis
	 */
	public List<SelectItem> getResponsaveis() {
		try {
			responsaveis = new ArrayList<SelectItem>();
			List<Usuario> listResponsaveis;
			listResponsaveis = seguranca.listarImportadores(StatusUsuario.A);
			for (Usuario u : listResponsaveis) {
				responsaveis.add(new SelectItem(u, u.getNome()));
			}
			responsavelConverter = new ConverterUtil<Usuario>(listResponsaveis);
		} catch (BusinessException e) {
			e.printStackTrace();
		}

		return responsaveis;
	}

	public void popularComboStatus() {
		FacesContext fc = FacesContext.getCurrentInstance();

		comboStatus = new ArrayList<SelectItem>();
		for (StatusCarga status : StatusCarga.getValuesFollowUp()) {
			comboStatus.add(new SelectItem(status, TemplateMessageHelper
					.getMessage(MensagensSistema.CARGA, status.name(), fc
							.getViewRoot().getLocale())));
		}
	}

	public void popularComboCanais() {
		comboCanais = new ArrayList<ParametroCanal>();
		try {
			// FacesContext fc = FacesContext.getCurrentInstance();

			List<ParametroCanal> canais = cadastro.listarParametroCanais();

			if (canais != null)
				comboCanais = canais;

			canalConverter = new ConverterUtil<ParametroCanal>(canais);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	public boolean carregarCidades() {
		if (StatusCarga.ITT.equals(carga.getStatus())
				|| StatusCarga.OHI.equals(carga.getStatus()))
			return true;
		return false;
	}

	/**
	 * Retorna a Cidade onde se encontra a carga, dependendo do Status
	 * 
	 * @param index
	 * @return
	 */
	public String getCidadeAtual(int index) {

		Invoice inv = carga.getListaDeInvoices().get(index);
		if (inv.getStatus() != null) {
			if ((inv.getStatus().equals(StatusInvoice.OHI) || inv.getStatus()
					.equals(StatusInvoice.ITT))) {
				if (carga.getCidadeAtual() != null) {
					return carga.getCidadeAtual().getSigla().toUpperCase();
				}
			}
		}

		return "";
	}

	@Override
	protected void refazerPesquisa() {
		MBeanPesquisarFollowUp bean = (MBeanPesquisarFollowUp) JSFRequestBean
				.getManagedBean("mBeanPesquisarFollowUp");
		bean.refazerPesquisa();
	}

	public String getNomeAutor(FollowUpEstimado item) {
		try {
			FollowUpEstimado aux = facade.getFollowUpstimadoById(item.getId());
			return aux.getAutor().getNome();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public String getCanal(FollowUpEstimado item) {
		try {
			FollowUpEstimado aux = facade.getFollowUpstimadoById(item.getId());
			return aux.getCanalEstimado().getCanal().name();
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * @brief Metodo que realiza o download de um arquivo a partir de um evento
	 *        do JSP
	 * @param ActionEvent
	 *            event - Evento da actionListener da pagina JSF
	 */

	private Boolean carregado;

	private StreamedContent file;

	public void handleFileUpload(FileUploadEvent event) {

		try {
			carregado = true;

			file = new DefaultStreamedContent(event.getFile().getInputstream());

			byte[] arquivo = event.getFile().getContents();
			String nomeArquivo = ConverterTexto.paraIso(event.getFile().getFileName());
			// Long tamArqui = event.getFile().getSize();
			String mime = event.getFile().getContentType();

			AnexoFollowUpImp anexo = new AnexoFollowUpImp();
			anexo.setAnexo(arquivo);
			anexo.setFollowUp(followUp);
			anexo.setMimeType(mime);
			anexo.setNomeArquivo(nomeArquivo);
			anexosFollowUp.add(anexo);

			// this.user.setAvatar(foto);
			// avatar = foto;
		} catch (IOException ex) {

			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void baixarAnexo(int index) {

		try {
			// Invoice invoice = new Invoice();
			// invoice = listaInvoices.get(index);
			// invoice = (Invoice)
			// facade.getInvoiceByIdWithFile(invoice.getId(), true, true);
			//
			// if (invoice.getAnexo() != null) {
			//
			// File file = new File();
			// file.setData(invoice.getAnexo().getAnexo());
			// file.setName(invoice.getAnexo().getNomeArquivo().replace(" ",
			// "_"));
			// file.setMime(invoice.getAnexo().getMimeType());
			//
			// File.fileDonwload(file);
			//
			// }
		} catch (Exception e) {
			BusinessException be = new BusinessException(
					CodigoErroEspecifico.MSG_ERRO_DONWLOAD_ARQUIVO);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, be));
		}
	}

	public FollowUpImport getFollowUp() {
		return followUp;
	}

	public void setFollowUp(FollowUpImport followUp) {
		this.followUp = followUp;
	}

	public Carga getCarga() {
		return carga;
	}

	public void setCarga(Carga carga) {
		this.carga = carga;
	}

	public List<SelectItem> getComboStatus() {
		popularComboStatus();
		return comboStatus;
	}

	public void setComboStatus(List<SelectItem> comboStatus) {
		this.comboStatus = comboStatus;
	}

	public FollowUpImportTrecho getFollowTrecho() {
		return followTrecho;
	}

	public void setFollowTrecho(FollowUpImportTrecho followTrecho) {
		this.followTrecho = followTrecho;
	}

	public List<ParametroCanal> getComboCanais() {
		// popularComboCanais();
		return comboCanais;
	}

	public void setComboCanais(List<ParametroCanal> comboCanais) {
		this.comboCanais = comboCanais;
	}

	public ConverterUtil<ParametroCanal> getCanalConverter() {
		return canalConverter;
	}

	public void setCanalConverter(ConverterUtil<ParametroCanal> canalConverter) {
		this.canalConverter = canalConverter;
	}

	public List<FollowUpEstimado> getEstimados() {
		return estimados;
	}

	public void setEstimados(List<FollowUpEstimado> estimados) {
		this.estimados = estimados;
	}

	@Override
	public int getTotalRegistros() {
		if (followTrecho != null
				&& trechosEstimado.get(followTrecho.getId()) != null)
			return trechosEstimado.get(followTrecho.getId()).size();
		return 0;
	}

	public List<Cidade> getComboCidades() {
		return comboCidades;
	}

	public void setComboCidades(List<Cidade> comboCidades) {
		this.comboCidades = comboCidades;
	}

	public EntityConverter<Cidade> getCidadeConverter() {
		return cidadeConverter;
	}

	public void setCidadeConverter(EntityConverter<Cidade> cidadeConverter) {
		this.cidadeConverter = cidadeConverter;
	}

	public boolean isHabilitaData() {
		return habilitaData;
	}

	public void setHabilitaData(boolean habilitaData) {
		this.habilitaData = habilitaData;
	}

	public Map<Long, List<FollowUpEstimado>> getTrechosEstimado() {
		return trechosEstimado;
	}

	public void setTrechosEstimado(
			Map<Long, List<FollowUpEstimado>> trechosEstimado) {
		this.trechosEstimado = trechosEstimado;
	}

	public Map<Long, List<Ocorrencia>> getOcorrencias() {
		return ocorrencias;
	}

	public void setOcorrencias(Map<Long, List<Ocorrencia>> ocorrencias) {
		this.ocorrencias = ocorrencias;
	}

	public void setOcorrenciasTrecho(List<Ocorrencia> ocorrenciasTrecho) {
		this.ocorrenciasTrecho = ocorrenciasTrecho;
	}

	public Cidade getCidadeOcorrencia() {
		return cidadeOcorrencia;
	}

	public void setCidadeOcorrencia(Cidade cidadeOcorrencia) {
		this.cidadeOcorrencia = cidadeOcorrencia;
	}

	public Integer getPaginaAtualOcorrencias() {
		return paginaAtualOcorrencias;
	}

	public void setPaginaAtualOcorrencias(Integer paginaAtualOcorrencias) {
		this.paginaAtualOcorrencias = paginaAtualOcorrencias;
	}

	public List<AnexoFollowUpImp> getAnexosFollowUp() {
		return anexosFollowUp;
	}

	public void setAnexosFollowUp(List<AnexoFollowUpImp> anexosFollowUp) {
		this.anexosFollowUp = anexosFollowUp;
	}

	public List<AnexoFollowUpImp> getRemoveListAnexos() {
		return removeListAnexos;
	}

	public void setRemoveListAnexos(List<AnexoFollowUpImp> removeListAnexos) {
		this.removeListAnexos = removeListAnexos;
	}

	public int getPaginaAtualAnexos() {
		return paginaAtualAnexos;
	}

	public void setPaginaAtualAnexos(int paginaAtualAnexos) {
		this.paginaAtualAnexos = paginaAtualAnexos;
	}

	public Ocorrencia getOcorrenciaModal() {
		return ocorrenciaModal;
	}

	public void setOcorrenciaModal(Ocorrencia ocorrenciaModal) {
		this.ocorrenciaModal = ocorrenciaModal;
	}

	public TipoOcorrencia getTipoOcorrencia() {
		return tipoOcorrencia;
	}

	public void setTipoOcorrencia(TipoOcorrencia tipoOcorrencia) {
		this.tipoOcorrencia = tipoOcorrencia;
	}

	public ConverterUtil<TipoOcorrencia> getConverterTipoOcorrencia() {
		return converterTipoOcorrencia;
	}

	public void setConverterTipoOcorrencia(
			ConverterUtil<TipoOcorrencia> converterTipoOcorrencia) {
		this.converterTipoOcorrencia = converterTipoOcorrencia;
	}

	public ConverterUtil<Motivo> getConverterMotivo() {
		return converterMotivo;
	}

	public void setConverterMotivo(ConverterUtil<Motivo> converterMotivo) {
		this.converterMotivo = converterMotivo;
	}

	public List<Motivo> getComboMotivo() {
		return comboMotivo;
	}

	public void setComboMotivo(ArrayList<Motivo> comboMotivo) {
		this.comboMotivo = comboMotivo;
	}

	public List<TipoOcorrencia> getComboTipoOcorrencia() {
		return comboTipoOcorrencia;
	}

	public void setComboTipoOcorrencia(List<TipoOcorrencia> comboTipoOcorrencia) {
		this.comboTipoOcorrencia = comboTipoOcorrencia;
	}

	public Usuario getUser() {
		return user;
	}

	public void setUser(Usuario user) {
		this.user = user;
	}

	public List<FollowUpEstimado> getEstimadosSistema() {
		return estimadosSistema;
	}

	public void setEstimadosSistema(List<FollowUpEstimado> estimadosSistema) {
		this.estimadosSistema = estimadosSistema;
	}

	public List<Ocorrencia> getOcorrenciasCanal() {
		return ocorrenciasCanal;
	}

	public void setOcorrenciasCanal(List<Ocorrencia> ocorrencaisCanal) {
		this.ocorrenciasCanal = ocorrencaisCanal;
	}

	public List<Ocorrencia> getOcorrenciasLom() {
		return ocorrenciasLom;
	}

	public void setOcorrenciasLom(List<Ocorrencia> ocorrencaisLom) {
		this.ocorrenciasLom = ocorrencaisLom;
	}

	public ConverterUtil<Moeda> getMoedaConverter() {
		return moedaConverter;
	}

	public void setMoedaConverter(ConverterUtil<Moeda> moedaConverter) {
		this.moedaConverter = moedaConverter;
	}

	public void setMoedas(List<SelectItem> moedas) {
		this.moedas = moedas;
	}

	public List<Integer> getTotalEstimados() {
		Collections.reverse(totalEstimados);
		return totalEstimados;
	}

	public void setTotalEstimados(List<Integer> totalEstimados) {
		this.totalEstimados = totalEstimados;
	}

	public List<Integer> getTotalAtuais() {
		return totalAtuais;
	}

	public void setTotalAtuais(List<Integer> totalAtuais) {
		this.totalAtuais = totalAtuais;
	}

	public List<PessoaJuridica> getFornecedores() {
		return fornecedores;
	}

	public void setFornecedores(List<PessoaJuridica> fornecedores) {
		this.fornecedores = fornecedores;
	}

	/**
	 * @return the responsavelConverter
	 */
	public ConverterUtil<Usuario> getResponsavelConverter() {
		return responsavelConverter;
	}

	/**
	 * @param responsavelConverter
	 *            the responsavelConverter to set
	 */
	public void setResponsavelConverter(
			ConverterUtil<Usuario> responsavelConverter) {
		this.responsavelConverter = responsavelConverter;
	}

	/**
	 * @param responsaveis
	 *            the responsaveis to set
	 */
	public void setResponsaveis(List<SelectItem> responsaveis) {
		this.responsaveis = responsaveis;
	}

	/**
	 * @return the excluir
	 */
	public boolean isExcluir() {
		return excluir;
	}

	/**
	 * @param excluir
	 *            the excluir to set
	 */
	public void setExcluir(boolean excluir) {
		this.excluir = excluir;
	}

	public RelatarFollowUpImport getMail() {
		return mail;
	}

	public void setMail(RelatarFollowUpImport mail) {
		this.mail = mail;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Ocorrencia getOcoEditar() {
		return ocoEditar;
	}

	public void setOcoEditar(Ocorrencia ocoEditar) {
		this.ocoEditar = ocoEditar;
	}

	public Boolean getEditarOcorrencia() {
		return editarOcorrencia;
	}

	public void setEditarOcorrencia(Boolean editarOcorrencia) {
		this.editarOcorrencia = editarOcorrencia;
	}

	public Carga getSelectCarga() {
		return selectCarga;
	}

	public void setSelectCarga(Carga selectCarga) {
		this.selectCarga = selectCarga;
	}

	public Boolean getCarregado() {
		return carregado;
	}

	public void setCarregado(Boolean carregado) {
		this.carregado = carregado;
	}

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ParametroCanal getCanalEstimadoSelecionado() {
		return canalEstimadoSelecionado;
	}

	public void setCanalEstimadoSelecionado(
			ParametroCanal canalEstimadoSelecionado) {
		this.canalEstimadoSelecionado = canalEstimadoSelecionado;
	}

	/**
	 * @return the comboProcBroker
	 */
	public List<ProcBroker> getComboProcBroker() {
		return comboProcBroker;
	}

	/**
	 * @param comboProcBroker
	 *            the comboProcBroker to set
	 */
	public void setComboProcBroker(List<ProcBroker> comboProcBroker) {
		this.comboProcBroker = comboProcBroker;
	}

	/**
	 * @return the broker
	 */
	public ProcBroker getBroker() {
		return broker;
	}

	/**
	 * @param broker
	 *            the broker to set
	 */
	public void setBroker(ProcBroker broker) {
		this.broker = broker;
	}

	/**
	 * @return the comboBroker
	 */
	public List<SelectItem> getComboBroker() {
		return comboBroker;
	}

	/**
	 * @param comboBroker
	 *            the comboBroker to set
	 */
	public void setComboBroker(List<SelectItem> comboBroker) {
		this.comboBroker = comboBroker;
	}

	/**
	 * @return the converterBroker
	 */
	public EntityConverter<ProcBroker> getConverterBroker() {
		return converterBroker;
	}

	/**
	 * @param converterBroker
	 *            the converterBroker to set
	 */
	public void setConverterBroker(EntityConverter<ProcBroker> converterBroker) {
		this.converterBroker = converterBroker;
	}

	/**
	 * @return the trechoAnterior
	 */
	public FollowUpImportTrecho getTrechoAnterior() {
		return trechoAnterior;
	}

	/**
	 * @param trechoAnterior the trechoAnterior to set
	 */
	public void setTrechoAnterior(FollowUpImportTrecho trechoAnterior) {
		this.trechoAnterior = trechoAnterior;
	}

}
