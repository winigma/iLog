package br.com.ilog.importacao.business.mbean;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.springframework.stereotype.Controller;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.hibernate.validator.ValidatorHelper;
import br.cits.commons.citspresentation.mbeans.AbstractManter;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.commons.citspresentation.util.JSFRequestBean;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.CheckList;
import br.com.ilog.cadastro.business.entity.ItemChecklist;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroCheckList;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.geral.business.CodigoErroEspecifico;
import br.com.ilog.geral.presentation.Constantes;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;
import br.com.ilog.importacao.business.entity.HistStatusItensInvoice;
import br.com.ilog.importacao.business.entity.HistoricoStatusInvoice;
import br.com.ilog.importacao.business.entity.Invoice;
import br.com.ilog.importacao.business.entity.InvoiceChecklist;
import br.com.ilog.importacao.business.entity.InvoiceItemChecklist;
import br.com.ilog.importacao.business.entity.StatusInvoice;
import br.com.ilog.importacao.business.facade.ImportacaoFacade;
import br.com.ilog.importacao.business.util.SendMail;
import br.com.ilog.seguranca.business.entity.Usuario;
import br.com.ilog.seguranca.business.facade.SegurancaFacade;

/**
 * 
 * @author Wisley Souza
 * @coment Bean de pesquisa que atua na fase de validação da invoice
 * 
 */

@Controller("mBeanManterValidarInvoice")
@AccessScoped
public class MBeanManterValidarInvoice extends AbstractManter {

	private static final long serialVersionUID = 1L;

	/**
	 * @coment fachada de importação
	 */
	@Resource(name = "controllerImportacao")
	ImportacaoFacade validarInvoiceFacade;

	/**
	 * @coment fachada de cadastros
	 */
	@Resource(name = "controllerCadastro")
	CadastroFacade cadastrotFacade;

	/**
	 * 
	 * @coment Fachada para auxiliar na busca de compradores
	 */
	@Resource(name = "controleUsuario")
	SegurancaFacade compradoresFacade;

	/**
	 * @coment obejeto de invoice
	 */
	private Invoice invoice;
	private CheckList checklist;
	private InvoiceChecklist respostasCheckList;
	private Boolean novaValidacao;

	private List<Invoice> invoicesSelected;

	private HistoricoStatusInvoice historicoInvoice;
	private List<HistoricoStatusInvoice> listHistorico;

	private Invoice selectInvoice;

	/**
	 * @coment Objeto de subprocesso
	 */
	// private SubProcesso subProcesso;

	/**
	 * @coment String que captura todos numeros de invoices...
	 */
	private String numInvoices;

	/**
	 * @coment método contrutor
	 */
	@PostConstruct
	void inicializar() {
		historicoInvoice = new HistoricoStatusInvoice();
		historicoInvoice.setItens(new ArrayList<HistStatusItensInvoice>());
		respostasCheckList = new InvoiceChecklist();
		respostasCheckList
				.setRespostasChecklist(new ArrayList<InvoiceItemChecklist>());
		listHistorico = new ArrayList<HistoricoStatusInvoice>();
		novaValidacao = false;
		inicializarObjetos();
		edicao = false;
		numInvoices = "";
		required = false;

	}

	/**
	 * @brief Metodo que realiza o download de um arquivo a partir de um evento
	 *        do JSP
	 * @param ActionEvent
	 *            event - Evento da actionListener da pagina JSF
	 */
	public void baixarAnexo(ActionEvent event) {

		try {
			// if (invoice.getAnexo() != null) {
			// File file = new File();
			// file.setData(invoice.getAnexo().getAnexo());
			// file.setName(invoice.getAnexo().getNomeArquivo());
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

	public void EnviarEmail(String assunto, String mensagem) {
		try {
			// Capturando todos os e-mails cadastrados para aviso
			SendMail enviarEmail = new SendMail();

			List<Usuario> compradoresAux = compradoresFacade
					.listarCompradores(null);
			String destinatarios = "";
			for (Usuario usuario : compradoresAux) {
				// if (usuario.isAvisoMII())
				destinatarios += usuario.getEmail().trim() + ";";
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

	/**
	 * @coment Nova validacao
	 * @return
	 */
	public String novaValidacao() {
		inicializarObjetos();
		novaValidacao = true;
		edicao = false;
		return Constantes.NOVO;
	}

	private Boolean res;

	public void optionQuestion() {
		if (res) {
			this.marcarRespostaSim();

		} else if (!res) {
			this.marcarRespostaNao();
		}
	}

	public void marcarRespostaSim() {

		for (InvoiceItemChecklist item : respostasCheckList
				.getRespostasChecklist()) {

			if (item.getResposta() == null) {
				item.setResposta(true);

			}
			if (!item.getResposta()) {
				item.setResposta(true);

			}

		}

	}

	public void marcarRespostaNao() {

		for (InvoiceItemChecklist item : respostasCheckList
				.getRespostasChecklist()) {

			if (item.getResposta() == null) {
				item.setResposta(false);

			}
			if (item.getResposta()) {
				item.setResposta(false);

			}

		}

	}

	public String capturarInvoices() {
		try {
			// FacesContext fc = FacesContext.getCurrentInstance();

			Boolean verifica = false;
			MBeanPesquisarValidarInvoice pesq = (MBeanPesquisarValidarInvoice) JSFRequestBean
					.getManagedBean("mBeanPesquisarValidarInvoice");

			invoicesSelected = new ArrayList<Invoice>();

			for (Invoice invoice : pesq.getInvoices()) {
				if (invoice.getSelect()) {
					if (invoice.getStatus() == StatusInvoice.C) {
						invoicesSelected.add(invoice);
						verifica = true;
					}

				}
			}
			if (invoicesSelected.isEmpty()) {

				FacesContext fc = FacesContext.getCurrentInstance();

				String message = TemplateMessageHelper.getMessage(
						MensagensSistema.INVOICE, "lblNoSelectInvoice", fc
								.getViewRoot().getLocale());
				Messages.adicionaMensagemDeErro(message);
				return null;
			}

			if (!verifica) {
				FacesContext fc = FacesContext.getCurrentInstance();
				String message = TemplateMessageHelper
						.getMessage(MensagensSistema.CHECKLIST_INVOICE,
								"msgInvoicesSelecionadas", fc.getViewRoot()
										.getLocale());
				Messages.adicionaMensagemDeErro(message);
				return null;

			}

			for (Invoice item : invoicesSelected) {
				if (numInvoices.equals(""))
					numInvoices = item.getNumeroInvoice() + " ";
				else
					numInvoices = numInvoices + ", " + item.getNumeroInvoice();
			}

			/**
			 * Ver ess parte de pois de processo que estou tirando agora........
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 */
			// SubProcesso sub = new SubProcesso();
			// sub.setNome(CheckLists.VALIDAR_INVOICE.name());

			List<CheckList> checks = cadastrotFacade
					.listCheckListByFilter(new BasicFiltroCheckList());
			if (checks != null && !checks.isEmpty()) {
				checklist = checks.get(0);
			}
			// checklist = cadastrotFacade.getCheckListById(checklist.getId());
			if (checklist.getId() != null)
				checklist = cadastrotFacade.getCheckListById(checklist.getId());
			else {
				FacesContext fc = FacesContext.getCurrentInstance();

				String message = TemplateMessageHelper.getMessage(
						MensagensSistema.CHECKLIST, "msgNoCheckListAtivo", fc
								.getViewRoot().getLocale());
				Messages.adicionaMensagemDeErro(message);
				return null;
			}
			respostasCheckList = validarInvoiceFacade.getInvoiceChecklist(
					invoice, checklist);
			if (respostasCheckList == null) {
				edicao = false;

				respostasCheckList = new InvoiceChecklist();
				respostasCheckList
						.setRespostasChecklist(new ArrayList<InvoiceItemChecklist>());

				for (ItemChecklist item : checklist.getItem()) {
					InvoiceItemChecklist invItemCheck = new InvoiceItemChecklist();
					invItemCheck.setItemCheckList(item);
					invItemCheck.setInvoiceChecklist(respostasCheckList);
					invItemCheck.setResposta(null);
					respostasCheckList.getRespostasChecklist()
							.add(invItemCheck);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return Constantes.EDITARVARIOS;
	}

	public InvoiceChecklist cloneResposta(InvoiceChecklist clone) {
		InvoiceChecklist invChek = new InvoiceChecklist();

		invChek.setJustificativa(clone.getJustificativa());
		invChek.setRespostasChecklist(new ArrayList<InvoiceItemChecklist>());

		for (InvoiceItemChecklist itemClone : clone.getRespostasChecklist()) {
			InvoiceItemChecklist item = new InvoiceItemChecklist();
			item.setInvoiceChecklist(invChek);
			item.setReprovado(itemClone.getReprovado());
			item.setResposta(itemClone.getResposta());
			item.setItemCheckList(itemClone.getItemCheckList());

			invChek.getRespostasChecklist().add(item);
		}

		return invChek;
	}

	private Boolean required;

	/**
	 * @coment Método que valida invoice(semelhante ao salvar, porém muda o
	 *         status da invoice para validada)
	 * @return
	 */
	public String validarInvoice() {

		FacesContext fc = FacesContext.getCurrentInstance();

		required = true;
		respostasCheckList.setCheckList(checklist);
		respostasCheckList.setInvoice(invoice);

		ResourceBundle resource = TemplateMessageHelper.getResourceBundle(
				MensagensSistema.CHECKLIST_INVOICE, fc.getViewRoot()
						.getLocale());
		List<String> erros = ValidatorHelper.valida(respostasCheckList,
				resource);

		if (erros.isEmpty()) {
			try {
				Boolean checked = false;
				for (InvoiceItemChecklist item : respostasCheckList
						.getRespostasChecklist()) {
					if (item.getResposta() == null) {
						checked = true;
					}
				}
				if (checked) {
					String message = TemplateMessageHelper.getMessage(
							MensagensSistema.SISTEMA, "msgNoResposta", fc
									.getViewRoot().getLocale());
					Messages.adicionaMensagemDeInfo(message);
					return null;

				} else {
					for (InvoiceItemChecklist item : respostasCheckList
							.getRespostasChecklist()) {
						if (item.getResposta() == false) {
							if (respostasCheckList.getJustificativa() == null
									|| respostasCheckList.getJustificativa()
											.equals("")) {
								String message = TemplateMessageHelper
										.getMessage(
												MensagensSistema.CHECKLIST_INVOICE,
												"msgJustificados", fc
														.getViewRoot()
														.getLocale());
								Messages.adicionaMensagemDeInfo(message);
								return null;
							}

						}
					}
				}

				if (edicao) {
					validarInvoiceFacade.alterarRespostas(respostasCheckList);
					if (invoice.getStatus() == StatusInvoice.C
							|| invoice.getStatus() == StatusInvoice.EV) {
						invoice.setStatus(StatusInvoice.V);
						validarInvoiceFacade.alterarInvoice(invoice);
					}
					if (invoice.getStatus() == StatusInvoice.NA) {
						invoice.setStatus(StatusInvoice.R);
						validarInvoiceFacade.alterarInvoice(invoice);
					}

				} else {
					validarInvoiceFacade.cadastrarRespostas(respostasCheckList);
					if (invoice.getStatus() == StatusInvoice.C
							|| invoice.getStatus() == StatusInvoice.EV) {
						invoice.setStatus(StatusInvoice.V);
						validarInvoiceFacade.alterarInvoice(invoice);
					}
					if (invoice.getStatus() == StatusInvoice.NA) {
						invoice.setStatus(StatusInvoice.R);
						validarInvoiceFacade.alterarInvoice(invoice);
					}
				}

				EnviarEmail(
						TemplateMessageHelper.getMessage(
								MensagensSistema.INVOICE, "lblAssuntoEmail",
								JSFRequestBean.getLocale()),
						TemplateMessageHelper.getMessage(
								MensagensSistema.INVOICE, "lblEmAprovacao",
								JSFRequestBean.getLocale())
								+ " " + invoice.getNumeroInvoice());
				checked = false;

			} catch (BusinessException e) {
				e.printStackTrace();
			}
		} else {
			Messages.adicionaMensagensDeErro(erros);
			return null;
		}
		String message = TemplateMessageHelper.getMessage(
				MensagensSistema.SISTEMA, "MSG001", fc.getViewRoot()
						.getLocale());
		Messages.adicionaMensagemDeInfo(message);
		refazerPesquisa();
		return "validarinvoices.jsf";
	}

	public String validarVariosInvoices() {
		FacesContext fc = FacesContext.getCurrentInstance();

		required = true;
		respostasCheckList.setCheckList(checklist);
		respostasCheckList.setInvoice(invoice);

		String numberInvoices = "";
		ResourceBundle resource = TemplateMessageHelper.getResourceBundle(
				MensagensSistema.CHECKLIST_INVOICE, fc.getViewRoot()
						.getLocale());
		List<String> erros = ValidatorHelper.valida(respostasCheckList,
				resource);
		if (erros.isEmpty()) {
			Boolean checked = false;
			for (InvoiceItemChecklist item : respostasCheckList
					.getRespostasChecklist()) {
				// verifica se ha algum item nao marcado
				if (item.getResposta() == null) {
					checked = true;
				}
			}
			// se houver ele cancela a operacao
			if (checked) {

				String message = TemplateMessageHelper.getMessage(
						MensagensSistema.SISTEMA, "msgNoResposta", fc
								.getViewRoot().getLocale());
				Messages.adicionaMensagemDeInfo(message);
				return null;

			}
			// Se não verifica se ha alguma marcada como nao e exige uma
			// justificativa
			else {
				for (InvoiceItemChecklist item : respostasCheckList
						.getRespostasChecklist()) {
					if (item.getResposta() == false) {
						if (respostasCheckList.getJustificativa() == null
								|| respostasCheckList.getJustificativa()
										.equals("")) {
							String message = TemplateMessageHelper.getMessage(
									MensagensSistema.CHECKLIST_INVOICE,
									"msgJustificados", fc.getViewRoot()
											.getLocale());
							Messages.adicionaMensagemDeInfo(message);
							return null;
						}

					}
				}
			}
			try {
				if (edicao) {

					if (!invoicesSelected.isEmpty()) {
						for (Invoice item : invoicesSelected) {

							respostasCheckList.setCheckList(checklist);
							respostasCheckList.setInvoice(item);
							validarInvoiceFacade
									.alterarRespostas(respostasCheckList);
							if (item.getStatus() == StatusInvoice.C) {
								item.setStatus(StatusInvoice.V);
								item = validarInvoiceFacade
										.alterarInvoice(item);
							}

						}

					}
				} else {

					if (!invoicesSelected.isEmpty()) {

						for (Invoice item : invoicesSelected) {

							respostasCheckList = cloneResposta(respostasCheckList);

							respostasCheckList.setCheckList(checklist);
							respostasCheckList.setInvoice(item);
							validarInvoiceFacade
									.cadastrarRespostas(respostasCheckList);
							if (item.getStatus() == StatusInvoice.C) {
								item.setStatus(StatusInvoice.V);
								item = validarInvoiceFacade
										.alterarInvoice(item);
								if (numberInvoices.equals("")) {
									numberInvoices = item.getNumeroInvoice();
								} else {
									numberInvoices = numberInvoices + ", "
											+ item.getNumeroInvoice();
								}
							}

						}

					}

				}

				EnviarEmail(
						TemplateMessageHelper.getMessage(
								MensagensSistema.INVOICE, "lblAssuntoEmail", fc
										.getViewRoot().getLocale()),
						TemplateMessageHelper.getMessage(
								MensagensSistema.INVOICE, "lblEmAprovacao", fc
										.getViewRoot().getLocale())
								+ " " + numberInvoices);
			} catch (BusinessException e) {
				e.printStackTrace();

			}

		} else {
			Messages.adicionaMensagensDeErro(erros);
			return null;
		}

		String message = TemplateMessageHelper.getMessage(
				MensagensSistema.SISTEMA, "MSG001", fc.getViewRoot()
						.getLocale());
		Messages.adicionaMensagemDeInfo(message);
		refazerPesquisa();
		return Constantes.SALVAR;
	}

	public String salvarVarios() {

		try {
			if (edicao) {
				if (!invoicesSelected.isEmpty()) {
					for (Invoice item : invoicesSelected) {

						respostasCheckList.setCheckList(checklist);
						respostasCheckList.setInvoice(item);
						validarInvoiceFacade
								.alterarRespostas(respostasCheckList);
						if (item.getStatus() == StatusInvoice.C) {
							item.setStatus(StatusInvoice.EV);
							item = validarInvoiceFacade.alterarInvoice(item);
						}

					}

				}
			} else {
				if (!invoicesSelected.isEmpty()) {

					for (Invoice item : invoicesSelected) {

						respostasCheckList = cloneResposta(respostasCheckList);

						respostasCheckList.setCheckList(checklist);
						respostasCheckList.setInvoice(item);
						validarInvoiceFacade
								.cadastrarRespostas(respostasCheckList);
						if (item.getStatus() == StatusInvoice.C) {
							item.setStatus(StatusInvoice.EV);
							item = validarInvoiceFacade.alterarInvoice(item);
						}

					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		FacesContext fc = FacesContext.getCurrentInstance();

		String message = TemplateMessageHelper.getMessage(
				MensagensSistema.SISTEMA, "MSG001", fc.getViewRoot()
						.getLocale());
		Messages.adicionaMensagemDeInfo(message);
		refazerPesquisa();
		return Constantes.SALVAR;
	}

	/**
	 * @coment Método Salvar
	 */
	@Override
	public String salvar() {
		FacesContext fc = FacesContext.getCurrentInstance();

		respostasCheckList.setCheckList(checklist);
		respostasCheckList.setInvoice(invoice);

		ResourceBundle resource = TemplateMessageHelper.getResourceBundle(
				MensagensSistema.CHECKLIST_INVOICE, JSFRequestBean.getLocale());
		List<String> erros = ValidatorHelper
				.valida(respostasCheckList, TemplateMessageHelper
						.getResourceBundle(MensagensSistema.SISTEMA, fc
								.getViewRoot().getLocale()), resource);

		if (erros.isEmpty()) {
			try {
				if (edicao) {
					validarInvoiceFacade.alterarRespostas(respostasCheckList);
					if (invoice.getStatus() == StatusInvoice.C) {
						invoice.setStatus(StatusInvoice.EV);
						invoice = validarInvoiceFacade.alterarInvoice(invoice);
					}

				} else {
					validarInvoiceFacade.cadastrarRespostas(respostasCheckList);
					if (invoice.getStatus() == StatusInvoice.C) {
						invoice.setStatus(StatusInvoice.EV);
						validarInvoiceFacade.alterarInvoice(invoice);
					}
				}
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		} else {
			Messages.adicionaMensagensDeErro(erros);
			return null;
		}
		String message = TemplateMessageHelper.getMessage(
				MensagensSistema.SISTEMA, "MSG001", fc.getViewRoot()
						.getLocale());
		Messages.adicionaMensagemDeInfo(message);
		refazerPesquisa();
		return super.salvar();
	}

	private List<HistStatusItensInvoice> itensHistorico;

	public void listarItensHistorico(int index) {
		try {
			itensHistorico = new ArrayList<HistStatusItensInvoice>();
			HistoricoStatusInvoice his = new HistoricoStatusInvoice();
			his = listHistorico.get(index);
			Long id = his.getId();
			his = validarInvoiceFacade.getHistoricoStatusInvoiceById(id);
			itensHistorico = validarInvoiceFacade.getItensHistorico(his);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String editar() {
		Long idRegistro = selectInvoice.getId();
		FacesContext fc = FacesContext.getCurrentInstance();

		try {
			edicao = true;

			invoice = (Invoice) validarInvoiceFacade.getInvoiceById(idRegistro,
					true);

			// SubProcesso sub = new SubProcesso();
			// sub.setNome(CheckLists.VALIDAR_INVOICE.name());

			List<CheckList> checks = cadastrotFacade
					.listCheckListByFilter(new BasicFiltroCheckList(null));
			if (checks != null && !checks.isEmpty()) {
				checklist = checks.get(0);
			}
			if (checklist.getId() != null)
				checklist = cadastrotFacade.getCheckListById(checklist.getId());
			else {
				String message = TemplateMessageHelper.getMessage(
						MensagensSistema.CHECKLIST, "msgNoCheckListAtivo", fc
								.getViewRoot().getLocale());
				Messages.adicionaMensagemDeErro(message);
				return null;
			}
			respostasCheckList = validarInvoiceFacade.getInvoiceChecklist(
					invoice, checklist);
			if (respostasCheckList == null) {
				edicao = false;

				respostasCheckList = new InvoiceChecklist();
				respostasCheckList
						.setRespostasChecklist(new ArrayList<InvoiceItemChecklist>());

				for (ItemChecklist itens : checklist.getItem()) {
					InvoiceItemChecklist invItemCheck = new InvoiceItemChecklist();
					invItemCheck.setItemCheckList(itens);
					invItemCheck.setInvoiceChecklist(respostasCheckList);
					invItemCheck.setResposta(null);
					respostasCheckList.getRespostasChecklist()
							.add(invItemCheck);
				}
			}

			List<HistoricoStatusInvoice> listaHistorico = validarInvoiceFacade
					.getLastHistoricos(invoice);

			if (listaHistorico != null && listaHistorico.size() > 0
					&& !listaHistorico.isEmpty())
				historicoInvoice = listaHistorico.get(0);
			this.listHistorico = validarInvoiceFacade.gethistoricos(invoice);
			if (listHistorico != null && listHistorico.size() > 0)
				size = true;
			else
				size = false;
		} catch (BusinessException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "validarinvoice.jsf";
	}

	public String cancelar() {
		return "validarinvoices.jsf";
	}

	private boolean size;

	@Override
	public String excluir() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void inicializarObjetos() {
		checklist = new CheckList();
		invoice = new Invoice();

	}

	@Override
	protected void refazerPesquisa() {
		MBeanPesquisarValidarInvoice bean = (MBeanPesquisarValidarInvoice) JSFRequestBean
				.getManagedBean("mBeanPesquisarValidarInvoice");
		bean.refazerPesquisa();
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public CheckList getChecklist() {
		return checklist;
	}

	public void setChecklist(CheckList checklist) {
		this.checklist = checklist;
	}

	public InvoiceChecklist getRespostasCheckList() {
		return respostasCheckList;
	}

	public void setRespostasCheckList(InvoiceChecklist respostasCheckList) {
		this.respostasCheckList = respostasCheckList;
	}

	public Boolean getNovaValidacao() {
		return novaValidacao;
	}

	public void setNovaValidacao(Boolean novaValidacao) {
		this.novaValidacao = novaValidacao;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public List<Invoice> getInvoicesSelected() {
		return invoicesSelected;
	}

	public void setInvoicesSelected(List<Invoice> invoicesSelected) {
		this.invoicesSelected = invoicesSelected;
	}

	public HistoricoStatusInvoice getHistoricoInvoice() {
		return historicoInvoice;
	}

	public void setHistoricoInvoice(HistoricoStatusInvoice historicoInvoice) {
		this.historicoInvoice = historicoInvoice;
	}

	public String getNumInvoices() {
		return numInvoices;
	}

	public void setNumInvoices(String numInvoices) {
		this.numInvoices = numInvoices;
	}

	public Boolean getRes() {
		return res;
	}

	public void setRes(Boolean res) {
		this.res = res;
	}

	public List<HistoricoStatusInvoice> getListHistorico() {
		return listHistorico;
	}

	public void setListHistorico(List<HistoricoStatusInvoice> listHistorico) {
		this.listHistorico = listHistorico;
	}

	public boolean isSize() {
		return size;
	}

	public void setSize(boolean size) {
		this.size = size;
	}

	public List<HistStatusItensInvoice> getItensHistorico() {
		return itensHistorico;
	}

	public void setItensHistorico(List<HistStatusItensInvoice> itensHistorico) {
		this.itensHistorico = itensHistorico;
	}

	public Invoice getSelectInvoice() {
		return selectInvoice;
	}

	public void setSelectInvoice(Invoice selectInvoice) {
		this.selectInvoice = selectInvoice;
	}

}
