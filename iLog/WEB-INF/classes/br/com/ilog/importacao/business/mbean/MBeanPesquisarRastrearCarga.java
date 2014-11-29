package br.com.ilog.importacao.business.mbean;

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
import br.cits.commons.citspresentation.util.JSFRequestBean;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;
import br.com.ilog.importacao.business.entity.Carga;
import br.com.ilog.importacao.business.entity.Invoice;
import br.com.ilog.importacao.business.entity.ItemInvoice;
import br.com.ilog.importacao.business.entity.ItemPO;
import br.com.ilog.importacao.business.entity.PO;
import br.com.ilog.importacao.business.entity.StatusCarga;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroCarga;
import br.com.ilog.importacao.business.facade.ImportacaoFacade;

/**
 * @coment Bean de rastreamento de carga...
 * @author Wisley P. SOuza
 * 
 */

@Controller("mBeanPesquisarRastrearCarga")
@AccessScoped
public class MBeanPesquisarRastrearCarga extends AbstractPaginacao {

	private static final long serialVersionUID = -7809762111599598285L;
	private static Logger logger = LoggerFactory
			.getLogger(MBeanPesquisarRastrearCarga.class);

	/**
	 * @coment fachada de importação
	 */
	@Resource(name = "controllerImportacao")
	ImportacaoFacade rastrearCargaFacade;

	/**
	 * @coment fachada de cadastros
	 */
	@Resource(name = "controllerCadastro")
	CadastroFacade cadastrotFacade;

	private BasicFiltroCarga filtro;

	private List<Carga> statusCarga;

	private List<SelectItem> comboStatus;

	@SuppressWarnings("unused")
	@PostConstruct
	private void inicializar() {
		statusCarga = Collections.emptyList();
		filtro = new BasicFiltroCarga();
		filtro.setInvoice( new Invoice() );
		filtro.setItemInvoice(new ItemInvoice());
		filtro.getItemInvoice().setItemPO( new ItemPO() );
		filtro.getItemInvoice().getItemPO().setPo( new PO() );
		setPaginaAtual(1);
		

		try {
			popularComboStatus();
			doPesquisar(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void popularComboStatus() {
		comboStatus = new ArrayList<SelectItem>();

		try {
			FacesContext fc = FacesContext.getCurrentInstance();

			for (StatusCarga status : StatusCarga.getValores()) {
				comboStatus.add(new SelectItem(status, TemplateMessageHelper
						.getMessage(MensagensSistema.CARGA, status.name(), fc
								.getViewRoot().getLocale())));

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doPesquisar(ActionEvent arg0) {

		FacesContext fc = FacesContext.getCurrentInstance();

		ResourceBundle resourceBundle = TemplateMessageHelper
		.getResourceBundle(MensagensSistema.CARGA, JSFRequestBean
				.getLocale());
		List<String> errorMessages = ValidatorHelper.valida(filtro,
				resourceBundle, resourceBundle);
		if (errorMessages.isEmpty()) {
			try {
				statusCarga = rastrearCargaFacade.listCargasConsolidadas(filtro);
				if (statusCarga.isEmpty()) {
					String message = TemplateMessageHelper.getMessage(MensagensSistema.SISTEMA,"MSG008",fc.getViewRoot().getLocale());
					Messages.adicionaMensagemDeInfo(message);
				}
			
			} catch (BusinessException e) {
				logger.error("error: {} " + e);
				e.printStackTrace();
				Messages.adicionaMensagemDeErro(TemplateMessageHelper
						.getMessage(MensagensSistema.SISTEMA, e));
			}
			
		}else{
			Messages.adicionaMensagensDeErro(errorMessages);
		}
		
		
	}

	@Override
	public int getTotalRegistros() {
		if (statusCarga != null) {
			return statusCarga.size();
		}
		return 0;
	}

	@Override
	public void limpar() {
		
		filtro = new BasicFiltroCarga();
		filtro.setInvoice( new Invoice() );
		filtro.setItemInvoice(new ItemInvoice());
		filtro.getItemInvoice().setItemPO( new ItemPO() );
		
		filtro.getItemInvoice().getItemPO().setPo( new PO() );
		statusCarga.clear();
	}

	@Override
	public void refazerPesquisa() {
		if (filtro == null) {
			filtro = new BasicFiltroCarga();
		}
		doPesquisar(null);

	}

	public BasicFiltroCarga getFiltro() {
		return filtro;
	}

	public void setFiltro(BasicFiltroCarga filtro) {
		this.filtro = filtro;
	}

	public List<Carga> getStatusCarga() {
		return statusCarga;
	}

	public void setStatusCarga(List<Carga> statusCarga) {
		this.statusCarga = statusCarga;
	}

	public List<SelectItem> getComboStatus() {
		return comboStatus;
	}

	public void setComboStatus(List<SelectItem> comboStatus) {
		this.comboStatus = comboStatus;
	}

}
