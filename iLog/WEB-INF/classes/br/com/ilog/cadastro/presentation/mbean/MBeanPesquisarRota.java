package br.com.ilog.cadastro.presentation.mbean;

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
import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citspresentation.mbeans.AbstractPaginacao;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.Cidade;
import br.com.ilog.cadastro.business.entity.Modal;
import br.com.ilog.cadastro.business.entity.Pais;
import br.com.ilog.cadastro.business.entity.PessoaJuridica;
import br.com.ilog.cadastro.business.entity.Rota;
import br.com.ilog.cadastro.business.entity.TipoPessoa;
import br.com.ilog.cadastro.business.entity.TipoPessoaEnum;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroPessoaJuridica;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroRota;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.geral.presentation.CommonsList;
import br.com.ilog.geral.presentation.Constantes;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;

@AccessScoped
@Component( "mBeanPesquisarRota" )
public class MBeanPesquisarRota extends AbstractPaginacao {

	/** */
	private static final long serialVersionUID = -1516114167472741609L;
	Logger logger = LoggerFactory.getLogger( MBeanPesquisarRota.class );
	
	@Resource( name = "controllerCadastro" )
	CadastroFacade facade;
	
	@Resource
	CommonsList commonsList;
	
	private List<Rota> result;
	
	private BasicFiltroRota filtro;
	
	private List<Pais> comboPaisOrigem;
	private List<Pais> comboPaisDestino;
	
	private List<Cidade> comboCidadeOrigem;
	private List<Cidade> comboCidadeDestino;
	
	private List<PessoaJuridica> comboAgenteCargas;
	
	private List<Modal> comboModal;
	
	private List<SelectItem> comboStatus;
	
	private List<SelectItem> comboExpresso;
	
	@PostConstruct
	public void inicializar() {
		inicializarObjetos();
		doPesquisar( null );
	}
	
	/**
	 * 
	 */
	public void inicializarObjetos(){
		
		filtro = new BasicFiltroRota();
		result = Collections.emptyList();
		
		comboStatus = commonsList.listaBooleanAtivoInativo();
		comboExpresso = commonsList.listaSimNao();
		
		popularComboPaises();
		popularComboCidadeDestino();
		popularComboCidadeOrigem();
		popularComboAgente();
		popularComboModal();
		
	}
	
	@Override
	public void doPesquisar(ActionEvent arg0) {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			getRegistrosPagina();
			result = facade.listarRotas(filtro);
			if (result.isEmpty()) {
				String msg = TemplateMessageHelper.getMessage(
						MensagensSistema.SISTEMA, "MSG008", fc.getViewRoot()
								.getLocale());
				Messages.adicionaMensagemDeInfo(msg);
			}
			setPaginaAtual(Constantes.PAGE_INITIAL);

		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Preenche combo de pais origem e destino
	 */
	public void popularComboPaises() {
		comboPaisDestino = new ArrayList<Pais>();
		comboPaisOrigem = new ArrayList<Pais>();
		try {
			List<Pais> paises = facade.listarPaises();
			
			comboPaisDestino = paises;
			comboPaisOrigem = paises;

		} catch (BusinessException e) {
			e.printStackTrace();
		}
	
	}
	
	/**
	 * 
	 */
	public void popularComboCidadeOrigem() {
		comboCidadeOrigem = new ArrayList<Cidade>();

		try {
			comboCidadeOrigem = facade.getCidadesByPais(filtro
					.getPaisOrigem() );
			
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 */
	public void popularComboCidadeDestino() {
		comboCidadeDestino = new ArrayList<Cidade>();

		try {
			comboCidadeDestino = facade.getCidadesByPais(filtro
					.getPaisDestino());
			
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 */
	public void popularComboAgente() {
		comboAgenteCargas = new ArrayList<PessoaJuridica>();
		
		try {
			comboAgenteCargas = facade.listarAllPersons( 
					new BasicFiltroPessoaJuridica( new TipoPessoa( TipoPessoaEnum.A_CARGA.toString() ), null ) );

		} catch (BusinessException e) {
			e.printStackTrace();
		}
	
	}
	
	/**
	 * 
	 */
	public void popularComboModal() {
		comboModal = new ArrayList<Modal>();
		try {
			comboModal = facade.listarModals();

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
		inicializarObjetos();
	}

	@Override
	public void refazerPesquisa() {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the result
	 */
	public List<Rota> getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(List<Rota> result) {
		this.result = result;
	}

	/**
	 * @return the filtro
	 */
	public BasicFiltroRota getFiltro() {
		return filtro;
	}

	/**
	 * @param filtro the filtro to set
	 */
	public void setFiltro(BasicFiltroRota filtro) {
		this.filtro = filtro;
	}

	/**
	 * @return the comboPaisOrigem
	 */
	public List<Pais> getComboPaisOrigem() {
		return comboPaisOrigem;
	}

	/**
	 * @param comboPaisOrigem the comboPaisOrigem to set
	 */
	public void setComboPaisOrigem(List<Pais> comboPaisOrigem) {
		this.comboPaisOrigem = comboPaisOrigem;
	}

	/**
	 * @return the comboPaisDestino
	 */
	public List<Pais> getComboPaisDestino() {
		return comboPaisDestino;
	}

	/**
	 * @param comboPaisDestino the comboPaisDestino to set
	 */
	public void setComboPaisDestino(List<Pais> comboPaisDestino) {
		this.comboPaisDestino = comboPaisDestino;
	}

	/**
	 * @return the comboCidadeOrigem
	 */
	public List<Cidade> getComboCidadeOrigem() {
		return comboCidadeOrigem;
	}

	/**
	 * @param comboCidadeOrigem the comboCidadeOrigem to set
	 */
	public void setComboCidadeOrigem(List<Cidade> comboCidadeOrigem) {
		this.comboCidadeOrigem = comboCidadeOrigem;
	}

	/**
	 * @return the comboCidadeDestino
	 */
	public List<Cidade> getComboCidadeDestino() {
		return comboCidadeDestino;
	}

	/**
	 * @param comboCidadeDestino the comboCidadeDestino to set
	 */
	public void setComboCidadeDestino(List<Cidade> comboCidadeDestino) {
		this.comboCidadeDestino = comboCidadeDestino;
	}

	/**
	 * @return the comboAgenteCargas
	 */
	public List<PessoaJuridica> getComboAgenteCargas() {
		return comboAgenteCargas;
	}

	/**
	 * @param comboAgenteCargas the comboAgenteCargas to set
	 */
	public void setComboAgenteCargas(List<PessoaJuridica> comboAgenteCargas) {
		this.comboAgenteCargas = comboAgenteCargas;
	}

	/**
	 * @return the comboModal
	 */
	public List<Modal> getComboModal() {
		return comboModal;
	}

	/**
	 * @param comboModal the comboModal to set
	 */
	public void setComboModal(List<Modal> comboModal) {
		this.comboModal = comboModal;
	}

	/**
	 * @return the comboStatus
	 */
	public List<SelectItem> getComboStatus() {
		return comboStatus;
	}

	/**
	 * @param comboStatus the comboStatus to set
	 */
	public void setComboStatus(List<SelectItem> comboStatus) {
		this.comboStatus = comboStatus;
	}

	/**
	 * @return the comboExpresso
	 */
	public List<SelectItem> getComboExpresso() {
		return comboExpresso;
	}

	/**
	 * @param comboExpresso the comboExpresso to set
	 */
	public void setComboExpresso(List<SelectItem> comboExpresso) {
		this.comboExpresso = comboExpresso;
	}

}
