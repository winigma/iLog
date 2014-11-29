package br.com.ilog.exportacao.business.mbean;

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

import br.cits.commons.citsbusiness.hibernate.validator.ValidatorHelper;
import br.cits.commons.citspresentation.mbeans.AbstractPaginacao;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.commons.citspresentation.util.JSFRequestBean;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.Modal;
import br.com.ilog.cadastro.business.entity.Moeda;
import br.com.ilog.cadastro.business.entity.PessoaJuridica;
import br.com.ilog.cadastro.business.entity.TipoPessoaEnum;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.exportacao.business.entity.CargaExp;
import br.com.ilog.exportacao.business.entity.StatusCargaExp;
import br.com.ilog.exportacao.business.entityFilter.BasicFiltroCargaExp;
import br.com.ilog.exportacao.business.facade.ExportacaoFacade;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;

/**
 * 
 * @author Wisley Souza
 * @coment Bean de pesquisa de cargas de exportcao
 * 
 */

@Controller("mBeanPesquisarCargaExp")
@AccessScoped
public class MBeanPesquisarCargaExportacao extends AbstractPaginacao {

	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory
			.getLogger(MBeanPesquisarCargaExportacao.class);

	/**
	 * @coment fachada de exportacao
	 */
	@Resource(name = "controllerExportacao")
	ExportacaoFacade exportacaoFacade;

	/**
	 * @coment fachada de cadastros
	 */
	@Resource(name = "controllerCadastro")
	CadastroFacade cadastrotFacade;

	private List<PessoaJuridica> clientes;
	private List<Moeda> moedas;
	private List<Modal> modais;
	private List<SelectItem> comboStatusCarga;

	private List<CargaExp> cargas;

	private BasicFiltroCargaExp filtro;

	@SuppressWarnings("unused")
	@PostConstruct
	private void inicializar() {
		filtro = new BasicFiltroCargaExp();
		cargas = Collections.emptyList();

		try {
			popularComboCliente();
			popularComboModal();
			popularComboMoeda();
			popularComboStatus();
			doPesquisar(null);
		} catch (Exception e) {

		}

	}

	public void popularComboMoeda() {
		moedas = new ArrayList<Moeda>();
		try {
			moedas = cadastrotFacade.listarMoedas();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void popularComboModal() {
		modais = new ArrayList<Modal>();
		try {
			modais = cadastrotFacade.listarModals();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void popularComboCliente() {

		clientes = new ArrayList<PessoaJuridica>();
		try {
			List<PessoaJuridica> fornecedorAux = cadastrotFacade
					.listarPessoasByTipo(TipoPessoaEnum.A_CARGA);
			if (fornecedorAux != null) {
				clientes = fornecedorAux;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void popularComboStatus() {
		comboStatusCarga = new ArrayList<SelectItem>();
		FacesContext fc = FacesContext.getCurrentInstance();
		for (StatusCargaExp status : StatusCargaExp.getValuesCargas()) {
			comboStatusCarga.add(new SelectItem(status, TemplateMessageHelper
					.getMessage(MensagensSistema.CARGA_EXP, status.name(), fc
							.getViewRoot().getLocale())));
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
				
				cargas = exportacaoFacade.listarCargasExportacaoByFiltro(filtro);
				if(cargas.isEmpty()){
					String message = TemplateMessageHelper.getMessage(
							MensagensSistema.SISTEMA, "MSG024", fc
									.getViewRoot().getLocale());
					Messages.adicionaMensagemDeInfo(message);
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
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

	public List<PessoaJuridica> getClientes() {
		return clientes;
	}

	public void setClientes(List<PessoaJuridica> clientes) {
		this.clientes = clientes;
	}

	public BasicFiltroCargaExp getFiltro() {
		return filtro;
	}

	public void setFiltro(BasicFiltroCargaExp filtro) {
		this.filtro = filtro;
	}

	public List<Moeda> getMoedas() {
		return moedas;
	}

	public void setMoedas(List<Moeda> moedas) {
		this.moedas = moedas;
	}

	public List<Modal> getModais() {
		return modais;
	}

	public void setModais(List<Modal> modais) {
		this.modais = modais;
	}

	public List<CargaExp> getCargas() {
		return cargas;
	}

	public void setCargas(List<CargaExp> cargas) {
		this.cargas = cargas;
	}

	public List<SelectItem> getComboStatusCarga() {
		return comboStatusCarga;
	}

	public void setComboStatusCarga(List<SelectItem> comboStatusCarga) {
		this.comboStatusCarga = comboStatusCarga;
	}

}
