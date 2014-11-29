package br.com.ilog.cadastro.presentation.mbean;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
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

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.hibernate.validator.ValidatorHelper;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.commons.citspresentation.util.JSFRequestBean;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.CheckList;
import br.com.ilog.cadastro.business.entity.DataVigencia;
import br.com.ilog.cadastro.business.entity.ItemChecklist;
import br.com.ilog.cadastro.business.entity.Status;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroCheckList;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.cadastro.presentation.util.ExceptionFiltro;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;
import br.com.ilog.geral.presentation.mbean.AbstractManterPaginacao;

/**
 * 
 * @author Wisley Souza
 * @comentario Bean de manter checklists
 * 
 */

@Controller("mBeanManterCheckList")
@AccessScoped
public class MBeanManterCheckList extends AbstractManterPaginacao {

	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory
			.getLogger(MBeanManterCheckList.class);

	@Resource(name = "controllerCadastro")
	CadastroFacade checkListFacade;

	private CheckList checkList;
	private CheckList selectCheckList;

	private ItemChecklist selectItem;

	/**
	 * @param Atributos
	 *            de ItemCheckList
	 */
	private Boolean error;
	private ItemChecklist itemCheckList;

	private BasicFiltroCheckList filtro;

	private String itemChecklistNome;

	/**
	 * combo de subProcessos
	 */
	private List<SelectItem> subProcessos;
	// private EntityConverter<SubProcesso> converterSubProcesso;
	// private ConverterUtil<SubProcesso> converterSubProcesso;
	/**
	 * novo checklList
	 */
	private boolean novoCheckList;

	/**
	 * Dados da data de vigencia
	 */
	private DataVigencia dataVigencia;

	/**
	 * @param edicaoItem
	 * @coment variavel para verificar edicao de item
	 */
	private Boolean edicaoItem;

	/**
	 * @comente List de datavigencias, pequeno teste
	 */
	private List<DataVigencia> datasVigentes;

	protected Map<Long, DataVigencia> mapaVigencias;

	private String subProcess;

	private List<SelectItem> statusCheckList;

	public String getSubProcess() {
		return subProcess;
	}

	public void setSubProcess(String subProcess) {
		this.subProcess = subProcess;
	}

	@PostConstruct
	void inicializar() {
		inicializarObjetos();
		datasVigentes = Collections.emptyList();
		novoCheckList = false;
		itemCheckList = new ItemChecklist();
		// checkList.setItem(new ArrayList<ItemCheckList>());
		setPaginaAtual(1);
		subProcessos = new ArrayList<SelectItem>();
		// subProcessos.

		try {
			// checkList.setProcesso(checkListFacade.getSubProcessoByNome(null));
			// popularSubProcessos();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String cancelar() {
		this.refazerPesquisa();
		return "checklists.jsf";
	}

	public String salvarNovo() {
		try {
			if (this.salvar() != null) {
				return this.novoCheckList();

			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * @comentario excluir item de checklis da tabela
	 * @param index
	 */

	public void excluirItem(int index) {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			checkList.getItem().remove(index);

			// apos a exclusao seta a paginacao para a primeira pagina.
			setPaginaAtual(1);

			Messages.adicionaMensagemDeInfo(TemplateMessageHelper.getMessage(
					MensagensSistema.CHECKLIST, "msgRemoveItem", fc
							.getViewRoot().getLocale()));

		} catch (Exception e) {
			FacesContext fc = FacesContext.getCurrentInstance();
			e.printStackTrace();
			Messages.adicionaMensagemDeInfo(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG_GEN_002", fc.getViewRoot()
							.getLocale()));
		}
	}

	/**
	 * @comentario Método que exclui os itens selecionados
	 */

	public void excluirSelecionados() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			List<ItemChecklist> itList = new ArrayList<ItemChecklist>();
			// itList = checkList.getItem();
			for (ItemChecklist item : checkList.getItem()) {
				if (item.getSelect()) {
					// checkList.getItem().remove(item);
					itList.add(item);
				}
			}
			if (!itList.isEmpty()) {
				checkList.getItem().removeAll(itList);

			} else {
				Messages.adicionaMensagemDeInfo(TemplateMessageHelper
						.getMessage(MensagensSistema.CHECKLIST,
								"msgNoSelectItens", fc.getViewRoot()
										.getLocale()));

			}
			// Seta a paginacao para a primeira pagina, apos a exclusao
			setPaginaAtual(1);

		} catch (ConstraintViolationException cve) {
			cve.printStackTrace();
			Messages.adicionaMensagemDeErro("Ocorreu um erro de violacao no banco: "
					+ cve.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @comentario método que inativa os itens selecionados na tabela de itens
	 */
	public void inativarSelecionados() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			List<ItemChecklist> itList = new ArrayList<ItemChecklist>();
			int i = 0;
			for (ItemChecklist item : checkList.getItem()) {
				if (item.getSelect()) {
					if (item.getAtivo()) {
						item.setAtivo(false);
						item.setVigencia_final(new Date());
						i++;
					}
					itList.add(item);
				}
			}

			if (itList.isEmpty())
				Messages.adicionaMensagemDeInfo(TemplateMessageHelper
						.getMessage(MensagensSistema.CHECKLIST,
								"msgNoSelectItens", fc.getViewRoot()
										.getLocale()));
			else if (i == 0)
				Messages.adicionaMensagemDeInfo(TemplateMessageHelper
						.getMessage(MensagensSistema.CHECKLIST,
								"msgItensSelectsYesInativados", fc
										.getViewRoot().getLocale()));
			else
				Messages.adicionaMensagemDeInfo(TemplateMessageHelper
						.getMessage(MensagensSistema.CHECKLIST,
								"msgItensSelectsInativados", fc.getViewRoot()
										.getLocale()));

		} catch (ConstraintViolationException cve) {
			cve.printStackTrace();
			Messages.adicionaMensagemDeErro("Ocorreu um erro de violacao no banco: "
					+ cve.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @comentario Método que ativa itens selecionados na tabela
	 */
	public void reativarSelecionados() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			Boolean checke = false;
			int i = 0;
			for (ItemChecklist item : checkList.getItem()) {
				if (item.getSelect()) {
					if (!item.getAtivo()) {
						item.setAtivo(true);
						item.setVigencia_final(null);
						item.setVigencia_inicial(new Date());
						i++;

					}
					checke = true;
				}
			}

			if (!checke)
				Messages.adicionaMensagemDeInfo(TemplateMessageHelper
						.getMessage(MensagensSistema.CHECKLIST,
								"msgNoSelectItens", fc.getViewRoot()
										.getLocale()));
			else if (i == 0)
				Messages.adicionaMensagemDeInfo(TemplateMessageHelper
						.getMessage(MensagensSistema.CHECKLIST,
								"msgItensSelectsYesAtivados", fc.getViewRoot()
										.getLocale()));
			else
				Messages.adicionaMensagemDeInfo(TemplateMessageHelper
						.getMessage(MensagensSistema.CHECKLIST,
								"msgItensSelectsAtivados", fc.getViewRoot()
										.getLocale()));

		} catch (ConstraintViolationException cve) {
			cve.printStackTrace();
			Messages.adicionaMensagemDeErro("Ocorreu um erro de violacao no banco: "
					+ cve.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author Wisley Souza
	 * @comentario Método de Reativar Item de CheckList
	 */

	public void reativarItem(int index) {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			dataVigencia = new DataVigencia();
			itemCheckList = checkList.getItem().get(index);
			itemCheckList.setAtivo(true);
			itemCheckList.setVigencia_final(null);
			itemCheckList.setVigencia_inicial(new Date());
			checkList.getItem().set(index, itemCheckList);

			try {
				// carregando as Datas Vigentes
				dataVigencia.setVigenciaInical(itemCheckList
						.getVigencia_inicial());
				dataVigencia.setVigenciaFinal(new Date());
				dataVigencia.setItemCheckList(itemCheckList);

				if (itemCheckList.getId() != null) {
					if (mapaVigencias.get(itemCheckList.getId()) != null) {
						mapaVigencias.remove(itemCheckList.getId());
					}
					mapaVigencias.put(itemCheckList.getId(), dataVigencia);
				}

				// Boolean check = false;
				// for (DataVigencia datas : datasVigentes) {
				// if (datas != dataVigencia) {
				// check = true;
				// }
				// }
				// if (check) {
				// datasVigentes.add(dataVigencia);
				// }
				// checkListFacade.cadastrarDataVigente(dataVigencia);
			} catch (Exception sql) {
				sql.printStackTrace();
				// sql.getSQLState();
				Messages.adicionaMensagemDeErro("Ocorreu um erro de violação no banco"
						+ sql.getMessage());
			}
			Messages.adicionaMensagemDeInfo(TemplateMessageHelper.getMessage(
					MensagensSistema.CHECKLIST, "msgItemReativado", fc
							.getViewRoot().getLocale()));

		} catch (ConstraintViolationException cve) {
			System.out.println(cve.getMessage());
			System.out.println(cve.getCause());
			System.out.println("O nome da constrainte eh: "
					+ cve.getConstraintName());
			// String erros = new ArrayList<String>();
			String erros = cve.getConstraintName();
			Messages.adicionaMensagemDeErro("Ocorreu um erro de violação no banco"
					+ erros);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void teste() throws SQLIntegrityConstraintViolationException {

	}

	/**
	 * @comentario Método de inativar um item..., Além de inserir as vigencias
	 *             das datas na tabela data de vigencia.
	 * @param index
	 * @author Wisley Souza
	 */
	public void inativarItem(int index) {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();

			itemCheckList = checkList.getItem().get(index);
			itemCheckList.setAtivo(false);
			itemCheckList.setVigencia_final(new Date());

			System.out.println(checkList.getItem().get(index));
			checkList.getItem().set(index, itemCheckList);

			if (itemCheckList.getId() != null
					&& mapaVigencias.get(itemCheckList.getId()) != null) {
				mapaVigencias.remove(itemCheckList.getId());
			}

			Messages.adicionaMensagemDeInfo(TemplateMessageHelper.getMessage(
					MensagensSistema.CHECKLIST, "msgItemInativado", fc
							.getViewRoot().getLocale()));
		} catch (Exception e) {

			System.out.println(e.getCause().getCause().getMessage());

			e.printStackTrace();
		}

	}

	/**
	 * @coment metodo de edicao de item
	 * @param index
	 */
	public void editItemCheckList(int index) {

		itemChecklistNome = null;
		itemCheckList = checkList.getItem().get(index);
		itemChecklistNome = itemCheckList.getItem();
		edicaoItem = true;

	}

	/**
	 * @param Método
	 *            event que adciona novo item no checkList
	 */
	public void addItemCheckList(ActionEvent evento) {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			if (!edicaoItem) {

				Integer i = 0;
				error = false;
				itemChecklistNome = itemChecklistNome.trim();
				itemCheckList.setItem(itemChecklistNome);
				itemCheckList.setChecklist(checkList);
				itemCheckList.setVigencia_inicial(new Date());
				itemCheckList.setAtivo(true);
				itemCheckList.setSelect(false);
				ResourceBundle resource = TemplateMessageHelper
						.getResourceBundle(MensagensSistema.CHECKLIST, fc
								.getViewRoot().getLocale());
				List<String> erros = ValidatorHelper.valida(itemCheckList,
						resource);
				if (erros.isEmpty()) {
					for (ItemChecklist item : checkList.getItem()) {
						if (item.getItem().equals(itemChecklistNome)) {
							i++;
						}
					}
					if (i <= 0) {
						checkList.getItem().add(itemCheckList);
						itemCheckList = new ItemChecklist();
						itemChecklistNome = null;

					} else {
						Messages.adicionaMensagemDeInfo(TemplateMessageHelper
								.getMessage(MensagensSistema.CHECKLIST,
										"msgInfoItemExistente", fc
												.getViewRoot().getLocale()));
					}
				} else {
					Messages.adicionaMensagensDeErro(erros);
					error = true;
					// Messages.adicionaMensagemDeErro(mensagem);
					return;

				}

				// mudei aqui!!!....................
				itemChecklistNome = "";
			} else if (edicaoItem) {
				error = false;
				ResourceBundle resource = TemplateMessageHelper
						.getResourceBundle(MensagensSistema.CHECKLIST, fc
								.getViewRoot().getLocale());
				List<String> erros = ValidatorHelper.valida(itemCheckList,
						resource);
				if (erros.isEmpty()) {
					itemChecklistNome = itemChecklistNome.trim();
					itemCheckList.setItem(itemChecklistNome);
					Messages.adicionaMensagemDeInfo(TemplateMessageHelper
							.getMessage(MensagensSistema.CHECKLIST,
									"msgEditItem", fc.getViewRoot().getLocale()));
				} else {
					Messages.adicionaMensagensDeErro(erros);
					error = true;
					// Messages.adicionaMensagemDeErro(mensagem);
					return;

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			e.getCause().getCause();
		}
		setPaginaAtual(1);
	}

	/**
	 * @param Método
	 *            que verifica erros e inicializa um novo objeto.
	 */

	public void limpar(ActionEvent arg) {
		if (!error) {
			itemCheckList = new ItemChecklist();
			itemChecklistNome = null;

		}
		error = false;
	}

	/**
	 * @comentario Novo CheckList
	 * @return novo
	 */

	public String novoCheckList() {
		inicializarObjetos();
		novoCheckList = true;
		edicao = false;
		return "checklist.jsf";

	}

	/**
	 * @comentario Método que popula os subProcessos do qual o CheckList pode
	 *             fazer parte
	 */
	public void popularSubProcessos() {
		subProcessos = new ArrayList<SelectItem>();

	}

	@Override
	public int getTotalRegistros() {
		if (checkList.getItem() != null) {
			return checkList.getItem().size();
		} else {
			return 0;
		}
	}

	/**
	 * @comentario Método de edicao
	 */
	@Override
	public String editar() {
		// Long idRegistro = Long.valueOf(JSFRequestBean.getParameter("id"));
		try {
			mapaVigencias = new HashMap<Long, DataVigencia>();

			checkList = (CheckList) checkListFacade
					.getCheckListById(selectCheckList.getId());

			/**
			 * @param Captura
			 *            de de SubProcesso
			 */

			// checkList.setProcesso(null);

			itemCheckList = new ItemChecklist();
			filtro = new BasicFiltroCheckList();
			filtro.setCheckList(checkList);

		} catch (BusinessException e) {
			logger.error("erro: {}", e);
			e.printStackTrace();
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.CHECKLIST, e));
		}
		edicao = true;
		return "checklist.jsf";
	}

	@Override
	public String salvar() {
		FacesContext fc = FacesContext.getCurrentInstance();

		ResourceBundle resource = TemplateMessageHelper.getResourceBundle(
				MensagensSistema.CHECKLIST, fc.getViewRoot().getLocale());
		List<String> erros = ValidatorHelper.valida(checkList, resource);

		if (erros.isEmpty()) {
			try {
				if (edicao) {
					if (checkList.getAtivo()) {
						List<CheckList> itens = new ArrayList<CheckList>();
						BasicFiltroCheckList filter = new BasicFiltroCheckList();
						filter.setAtivo(true);
						itens = checkListFacade.listCheckListByFilter(filter);
						if (itens.isEmpty()) {
							List<DataVigencia> datas = new ArrayList<DataVigencia>();
							datas.addAll(mapaVigencias.values());

							checkListFacade.alterarCheckList(checkList, datas);
						} else {
							Messages.adicionaMensagemDeInfo(TemplateMessageHelper
									.getMessage(MensagensSistema.CHECKLIST,
											"msgChecklistsAtivos", fc
													.getViewRoot().getLocale()));
							return null;
						}
					} else {
						List<DataVigencia> datas = new ArrayList<DataVigencia>();
						datas.addAll(mapaVigencias.values());

						checkListFacade.alterarCheckList(checkList, datas);
					}
				} else {
					List<CheckList> itens = new ArrayList<CheckList>();
					BasicFiltroCheckList filter = new BasicFiltroCheckList();
					filter.setAtivo(true);
					itens = checkListFacade.listCheckListByFilter(filter);
					if (itens.isEmpty()) {
						checkList.setAtivo(true);
						checkListFacade.cadastrarCheckList(checkList);
					} else {
						Messages.adicionaMensagemDeInfo(TemplateMessageHelper
								.getMessage(MensagensSistema.CHECKLIST,
										"msgChecklistsAtivos", fc.getViewRoot()
												.getLocale()));
						return null;
					}
				}
			} catch (BusinessException e) {
				e.printStackTrace();
				logger.error("erro: {}", e);

				/**
				 * verifica constraints
				 */
				Throwable lastCause = ExceptionFiltro.getLastException(e);
				if (lastCause.getMessage().contains("CHECK constraint")) {
					if (lastCause.getMessage().contains(
							"chk_checklist_proc_duplicado")) {
						Messages.adicionaMensagemDeErro(TemplateMessageHelper
								.getMessage(MensagensSistema.CHECKLIST,
										"msgCheckCheckList"));
					}
					return null;

				} else {

					Messages.adicionaMensagemDeErro(TemplateMessageHelper
							.getMessage(ExceptionFiltro.recursiveException(e)));
					e.printStackTrace();
					return null;
				}
			} catch (DataIntegrityViolationException e) {
				Throwable lastCause = ExceptionFiltro.getLastException(e);
				if (lastCause.getMessage().contains("CHECK constraint")) {
					if (lastCause.getMessage().contains(
							"chk_checklist_proc_duplicado")) {
						Messages.adicionaMensagemDeErro(TemplateMessageHelper
								.getMessage(MensagensSistema.CHECKLIST,
										"msgCheckCheckList", fc.getViewRoot()
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
		} else {
			Messages.adicionaMensagensDeErro(erros);
			return null;
		}

		String message = TemplateMessageHelper.getMessage(
				MensagensSistema.SISTEMA, "MSG001", fc.getViewRoot()
						.getLocale());
		Messages.adicionaMensagemDeInfo(message);
		refazerPesquisa();
		return "checklists.jsf";
	}

	/**
	 * @comentario Métod de exclusao
	 * @param Excluir
	 */
	@Override
	public String excluir() {
		try {
			checkListFacade.excluirCheckList(checkList);
		} catch (BusinessException e) {
			e.printStackTrace();
			logger.error("erro: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.CHECKLIST, e));
			return null;

		} catch (DataIntegrityViolationException e) {
			FacesContext fc = FacesContext.getCurrentInstance();
			e.printStackTrace();
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG_GEN_004", fc.getViewRoot()
							.getLocale()));
			return null;
			
		} catch (Exception e) {
			
			logger.error("error: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper
					.getMessage(ExceptionFiltro.recursiveException(e)));
			return null;
			
		}
		FacesContext fc = FacesContext.getCurrentInstance();
		Messages.adicionaMensagemDeInfo(TemplateMessageHelper.getMessage(
				MensagensSistema.SISTEMA, "MSG_EXCLUIR_SUCESSO", fc
						.getViewRoot().getLocale()));
		return "checklists.jsf";
	}

	@Override
	public void inicializarObjetos() {
		// FacesContext fc = FacesContext.getCurrentInstance();

		checkList = new CheckList();
		checkList.setItem(new ArrayList<ItemChecklist>());
		try {
			// checkList.setProcesso(checkListFacade.getSubProcessoByNome(null));
			// popularSubProcessos();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// subProcess = TemplateMessageHelper
		// .getMessage(MensagensSistema.SUBPROCESSO, checkList
		// .getProcesso().getNome(), fc.getViewRoot().getLocale());
		itemCheckList = new ItemChecklist();
		filtro = new BasicFiltroCheckList();
		error = false;
		edicaoItem = false;
		setPaginaAtual(1);
	}

	@Override
	protected void refazerPesquisa() {
		MBeanPesquisarCheckList mBean = (MBeanPesquisarCheckList) JSFRequestBean
				.getManagedBean("mBeanPesquisarCheckList");

		mBean.refazerPesquisa();

	}

	public CheckList getCheckList() {
		return checkList;
	}

	public void setCheckList(CheckList checkList) {
		this.checkList = checkList;
	}

	public ItemChecklist getItemCheckList() {
		return itemCheckList;
	}

	public void setItemCheckList(ItemChecklist itemCheckList) {
		this.itemCheckList = itemCheckList;
	}

	public BasicFiltroCheckList getFiltro() {
		return filtro;
	}

	public void setFiltro(BasicFiltroCheckList filtro) {
		this.filtro = filtro;
	}

	public boolean isNovoCheckList() {
		return novoCheckList;
	}

	public void setNovoCheckList(boolean novoCheckList) {
		this.novoCheckList = novoCheckList;
	}

	public List<SelectItem> getSubProcessos() {
		return subProcessos;
	}

	public void setSubProcessos(List<SelectItem> subProcessos) {
		this.subProcessos = subProcessos;
	}

	public Boolean getError() {
		return error;
	}

	public void setError(Boolean error) {
		this.error = error;
	}

	public String getItemChecklistNome() {
		return itemChecklistNome;
	}

	public void setItemChecklistNome(String itemChecklistNome) {
		this.itemChecklistNome = itemChecklistNome;
	}

	public DataVigencia getDataVigencia() {
		return dataVigencia;
	}

	public void setDataVigencia(DataVigencia dataVigencia) {
		this.dataVigencia = dataVigencia;
	}

	public Boolean getEdicaoItem() {
		return edicaoItem;
	}

	public void setEdicaoItem(Boolean edicaoItem) {
		this.edicaoItem = edicaoItem;
	}

	public List<DataVigencia> getDatasVigentes() {
		return datasVigentes;
	}

	public void setDatasVigentes(List<DataVigencia> datasVigentes) {
		this.datasVigentes = datasVigentes;
	}

	public CheckList getSelectCheckList() {
		return selectCheckList;
	}

	public void setSelectCheckList(CheckList selectCheckList) {
		this.selectCheckList = selectCheckList;
	}

	public ItemChecklist getSelectItem() {
		return selectItem;
	}

	public void setSelectItem(ItemChecklist selectItem) {
		this.selectItem = selectItem;
	}

	public List<SelectItem> getStatusCheckList() {
		statusCheckList = new ArrayList<SelectItem>();
		FacesContext fc = FacesContext.getCurrentInstance();
		for (Status status : Status.values()) {
			Boolean aux;
			if (status.toString().equals("ATIVO"))
				aux = true;
			else
				aux = false;
			statusCheckList.add(new SelectItem(aux, TemplateMessageHelper
					.getMessage(MensagensSistema.CHECKLIST, status.toString(),
							fc.getViewRoot().getLocale())));
		}
		return statusCheckList;
	}

	public void setStatusCheckList(List<SelectItem> statusCheckList) {
		this.statusCheckList = statusCheckList;
	}

}
