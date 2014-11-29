package br.com.ilog.importacao.business.mbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citspresentation.mbeans.AbstractPaginacao;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.PessoaJuridica;
import br.com.ilog.cadastro.business.entity.TipoPessoaEnum;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.geral.presentation.CommonsList;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;
import br.com.ilog.importacao.business.entity.Carga;
import br.com.ilog.importacao.business.entity.Invoice;
import br.com.ilog.importacao.business.entity.StatusCarga;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroPlanejarCarga;
import br.com.ilog.importacao.business.facade.ImportacaoFacade;
import br.com.ilog.seguranca.business.entity.Usuario;
import br.com.ilog.seguranca.business.facade.SegurancaFacade;

/**
 * 
 * @author Wisley Souza
 * @coment Bean de pesquisa que atua na fase de plnejamento das cargas
 * @data 24/07/2012
 * 
 */

@Controller("mBeanPesquisarPlanejarEmbarque")
@AccessScoped
public class MBeanPesquisarPlanejarEmbarque extends AbstractPaginacao implements
		Serializable {

	private static final long serialVersionUID = 1L;

	private static Logger logger = LoggerFactory
			.getLogger(MBeanPesquisarPlanejarEmbarque.class);

	/**
	 * @coment fachada de importação
	 */
	@Resource(name = "controllerImportacao")
	ImportacaoFacade planejarInvoiceFacade;

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

	@Resource(name = "commonsList")
	CommonsList commonsList;

	/**
	 * Objetos utilizados
	 */

	private List<Carga> planejadas;
	private BasicFiltroPlanejarCarga filtro;
	private List<Invoice> invoices;
	private List<PessoaJuridica> agentes;
	private List<Usuario> responsaveis;
	private List<SelectItem> comboStatusCarga;

	@SuppressWarnings("unused")
	@PostConstruct
	private void inicializar() {

		try {
			filtro = new BasicFiltroPlanejarCarga();

			planejadas = Collections.emptyList();

			invoices = Collections.emptyList();

			popularComboCompradores();
			
			popularComboStatusCarga();
			
			popularForncedores();
			
			doPesquisar(null);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 */
	private void popularComboStatusCarga() {
		comboStatusCarga = new ArrayList<SelectItem>();
		
		FacesContext fc = FacesContext.getCurrentInstance();
		for (StatusCarga status : StatusCarga.getValores()) {
			comboStatusCarga.add(new SelectItem(status, TemplateMessageHelper
					.getMessage(MensagensSistema.CARGA, status.name(), fc
							.getViewRoot().getLocale())));
		}
		
	}

	/**
	 * @coment Método que popula a combo de fornecedores
	 */
	public void popularForncedores() {
		agentes = new ArrayList<PessoaJuridica>();
		try {

			List<PessoaJuridica> fornecedorAux = cadastrotFacade
					.listarPessoasByTipo(TipoPessoaEnum.A_CARGA);
			if (fornecedorAux != null) {
				agentes = fornecedorAux;

			}
		} catch (BusinessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @coment Método que popula o combo de compradores
	 */
	public void popularComboCompradores() {
		responsaveis = new ArrayList<Usuario>();
		try {

			List<Usuario> compradoresAux = compradoresFacade
					.listarCompradores(null);
			if (compradoresAux != null) {
				responsaveis = compradoresAux;
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doPesquisar(ActionEvent arg0) {

		FacesContext fc = FacesContext.getCurrentInstance();

		try {
			planejadas = planejarInvoiceFacade
					.listarEmbarquesPlanejados(filtro);

			if (planejadas != null) {
				if (planejadas.isEmpty()) {
					String message = TemplateMessageHelper.getMessage(
							MensagensSistema.SISTEMA, "MSG008", fc
									.getViewRoot().getLocale());
					Messages.adicionaMensagemDeInfo(message);
				}
			}

		} catch (BusinessException e) {
			e.printStackTrace();
			logger.error("error: {} " + e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, e));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public int getTotalRegistros() {
		return 0;
	}

	@Override
	public void limpar() {
		filtro = new BasicFiltroPlanejarCarga();
		planejadas.clear();

	}

	@Override
	public void refazerPesquisa() {
	}

	public List<Carga> getPlanejadas() {
		return planejadas;
	}

	public void setPlanejadas(List<Carga> planejadas) {
		this.planejadas = planejadas;
	}

	public BasicFiltroPlanejarCarga getFiltro() {
		return filtro;
	}

	public void setFiltro(BasicFiltroPlanejarCarga filtro) {
		this.filtro = filtro;
	}

	public List<Invoice> getInvoices() {
		return invoices;
	}

	public void setInvoices(List<Invoice> invoices) {
		this.invoices = invoices;
	}

	public List<PessoaJuridica> getAgentes() {
		return agentes;
	}

	public void setAgentes(List<PessoaJuridica> agentes) {
		this.agentes = agentes;
	}

	public List<Usuario> getResponsaveis() {
		return responsaveis;
	}

	public void setResponsaveis(List<Usuario> responsaveis) {
		this.responsaveis = responsaveis;
	}

	/**
	 * @return the comboStatusCarga
	 */
	public List<SelectItem> getComboStatusCarga() {
		return comboStatusCarga;
	}

	/**
	 * @param comboStatusCarga the comboStatusCarga to set
	 */
	public void setComboStatusCarga(List<SelectItem> comboStatusCarga) {
		this.comboStatusCarga = comboStatusCarga;
	}

}
