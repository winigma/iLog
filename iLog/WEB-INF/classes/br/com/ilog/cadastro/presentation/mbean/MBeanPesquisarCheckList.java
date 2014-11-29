package br.com.ilog.cadastro.presentation.mbean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.hibernate.validator.ValidatorHelper;
import br.cits.commons.citspresentation.mbeans.AbstractPaginacao;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.CheckList;
import br.com.ilog.cadastro.business.entity.Status;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroCheckList;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.cadastro.presentation.util.ExceptionFiltro;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;

@Controller("mBeanPesquisarCheckList")
@AccessScoped
public class MBeanPesquisarCheckList extends AbstractPaginacao {

	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory
			.getLogger(MBeanPesquisarCheckList.class);

	@Resource(name = "controllerCadastro")
	CadastroFacade checkListFacade;

	// private List<SubProcesso> listProcessos;
	private List<CheckList> listCheckList;
	private List<SelectItem> optionsCheckList;
	private List<SelectItem> comboProcessos;
	// private EntityConverter<SubProcesso> converterProcessos;

	private BasicFiltroCheckList filtro;

	// filtros de autocomplete
	private String currentStateFilterValue;
	private String currentNameFilterValue;

	private List<SelectItem> statusCheckList;

	@SuppressWarnings("unused")
	@PostConstruct
	private void inicializador() {
		filtro = new BasicFiltroCheckList();
		listCheckList = Collections.emptyList();
		// listProcessos = Collections.emptyList();
		optionsCheckList = new ArrayList<SelectItem>();
		statusCheckList = new ArrayList<SelectItem>();
		FacesContext fc = FacesContext.getCurrentInstance();
		doPesquisar(null);
		try {
		//	popularComboProcessos();

			// listProcessos = checkListFacade.listProcessos();

			// for (SubProcesso processo : listProcessos) {
			// optionsCheckList.add(new
			// SelectItem(processo,TemplateMessageHelper.getMessage(MensagensSistema.SUBPROCESSO,
			// processo.getNome(),fc.getViewRoot().getLocale()))) ;
			// }

			/**
			 * for(Status status: Status.values()){ Boolean aux; if
			 * (status.toString().equals("ATIVO")) aux = true; else aux = false;
			 * statusCheckList.add(new
			 * SelectItem(aux,TemplateMessageHelper.getMessage
			 * (MensagensSistema.CHECKLIST
			 * ,status.toString(),fc.getViewRoot().getLocale()))); }
			 **/

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
/**
	public void popularComboProcessos() {
		comboProcessos = new ArrayList<SelectItem>();
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			List<SubProcesso> processoAux = checkListFacade.listProcessos();
			if (processoAux != null) {
				for (SubProcesso processo : processoAux) {
					comboProcessos.add(new SelectItem(processo,
							TemplateMessageHelper.getMessage(
									MensagensSistema.SUBPROCESSO, processo
											.getNome(), fc.getViewRoot()
											.getLocale())));
				}
				converterProcessos = new EntityConverter<SubProcesso>(
						processoAux);
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}**/

	// metodo preenche o autocomplete no jsf
	/**public List<SubProcesso> autocomplete(Object sugestao) {
		String pref = (String) sugestao;
		ArrayList<SubProcesso> result = new ArrayList<SubProcesso>();
		Iterator<SubProcesso> iterator = listProcessos.iterator();
		while (iterator.hasNext()) {
			SubProcesso processo = ((SubProcesso) iterator.next());
			if (processo.getNome() != null
					&& processo.getNome().toLowerCase()
							.indexOf(pref.toLowerCase()) == 0
					|| "".equals(pref)) {
				result.add(processo);
			}
		}
		return result;
	}**/

	public void resetFilter() {
		setCurrentNameFilterValue("");
		setCurrentStateFilterValue("");
	}

	@Override
	public void doPesquisar(ActionEvent arg0) {

		FacesContext fc = FacesContext.getCurrentInstance();
		ResourceBundle resourceBundle = TemplateMessageHelper
				.getResourceBundle(MensagensSistema.CHECKLIST, fc.getViewRoot()
						.getLocale());
		List<String> errorMessages = ValidatorHelper.valida(filtro,
				resourceBundle, resourceBundle);

		if (errorMessages.isEmpty()) {
			try {

				listCheckList = checkListFacade.listCheckListByFilter(filtro);
				if (listCheckList.isEmpty()) {
					String message = TemplateMessageHelper.getMessage(
							MensagensSistema.SISTEMA, "MSG008", fc
									.getViewRoot().getLocale());
					Messages.adicionaMensagemDeInfo(message);
				}
				setPaginaAtual(1);

			} catch (BusinessException e) {
				logger.error("error: {} " + e);
				e.printStackTrace();
				Messages.adicionaMensagemDeErro(TemplateMessageHelper
						.getMessage(MensagensSistema.SISTEMA, e));

			}
		} else {
			Messages.adicionaMensagensDeErro(errorMessages);
		}

	}

	// Long que captura o id
	private Long idObjeto;

	/**
	 * @coment captura o id do item selecionados
	 * @param index
	 */

	public void capturarId(int index) {
		CheckList objeto = listCheckList.get(index);
		idObjeto = objeto.getId();
	}

	/**
	 * Excluir Checklist.
	 * 
	 * @param id
	 */
	public void excluir() {

		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			checkListFacade.excluirCheckList(new CheckList(idObjeto));
			Messages.adicionaMensagemDeInfo(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG_EXCLUIR_SUCESSO", fc
							.getViewRoot().getLocale()));
			this.refazerPesquisa();

		} catch (Exception e) {
			logger.error("error: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper
					.getMessage(ExceptionFiltro.recursiveException(e)));
			return;
		}
	}

	@Override
	public int getTotalRegistros() {
		if (listCheckList == null) {
			return 0;
		}
		return listCheckList.size();
	}

	@Override
	public void limpar() {
	//	setPaginaAtual(1);
		listCheckList.clear();
		filtro =  new BasicFiltroCheckList();
	}

	@Override
	public void refazerPesquisa() {
		if (filtro == null) {
			filtro = new BasicFiltroCheckList();
		}
		if (listCheckList == null) {
			return;
		}

		doPesquisar(null);
	}

/**	public List<SubProcesso> getListProcessos() {
		return listProcessos;
	}

	public void setListProcessos(List<SubProcesso> listProcessos) {
		this.listProcessos = listProcessos;
	}**/

	public List<CheckList> getListCheckList() {
		return listCheckList;
	}

	public void setListCheckList(List<CheckList> listCheckList) {
		this.listCheckList = listCheckList;
	}

	public List<SelectItem> getOptionsCheckList() {
		return optionsCheckList;
	}

	public void setOptionsCheckList(List<SelectItem> optionsCheckList) {
		this.optionsCheckList = optionsCheckList;
	}

	public BasicFiltroCheckList getFiltro() {
		return filtro;
	}

	public void setFiltro(BasicFiltroCheckList filtro) {
		this.filtro = filtro;
	}

	public String getCurrentStateFilterValue() {
		return currentStateFilterValue;
	}

	public void setCurrentStateFilterValue(String currentStateFilterValue) {
		this.currentStateFilterValue = currentStateFilterValue;
	}

	public String getCurrentNameFilterValue() {
		return currentNameFilterValue;
	}

	public void setCurrentNameFilterValue(String currentNameFilterValue) {
		this.currentNameFilterValue = currentNameFilterValue;
	}

	public List<SelectItem> getComboProcessos() {
		return comboProcessos;
	}

	public void setComboProcessos(List<SelectItem> comboProcessos) {
		this.comboProcessos = comboProcessos;
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

	public Long getIdObjeto() {
		return idObjeto;
	}

	public void setIdObjeto(Long idObjeto) {
		this.idObjeto = idObjeto;
	}

}
