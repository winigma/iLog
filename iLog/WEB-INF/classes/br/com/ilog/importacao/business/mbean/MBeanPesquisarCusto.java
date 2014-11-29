package br.com.ilog.importacao.business.mbean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citspresentation.mbeans.AbstractPaginacao;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.Cidade;
import br.com.ilog.cadastro.business.entity.PessoaJuridica;
import br.com.ilog.cadastro.business.entity.Rota;
import br.com.ilog.cadastro.business.entity.TipoPessoa;
import br.com.ilog.cadastro.business.entity.TipoPessoaEnum;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroPessoaJuridica;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;
import br.com.ilog.importacao.business.entity.Carga;
import br.com.ilog.importacao.business.entity.Invoice;
import br.com.ilog.importacao.business.entity.ItemInvoice;
import br.com.ilog.importacao.business.entity.StatusCarga;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroCarga;
import br.com.ilog.importacao.business.facade.ImportacaoFacade;

@AccessScoped
@Controller("mBeanPesquisarCusto")
public class MBeanPesquisarCusto extends AbstractPaginacao {

	/** */
	private static final long serialVersionUID = -1718327874762419227L;
	
	@Resource( name = "controllerImportacao" )
	ImportacaoFacade facade;
	
	@Resource( name = "controllerCadastro" )
	CadastroFacade cadastro;

	private static Logger logger = LoggerFactory.getLogger(MBeanPesquisarCusto.class);
	
	/**
	 * Filtro da pesquisa.
	 */
	private BasicFiltroCarga filtro;
	
	/**
	 * resultado da pesquisa.
	 */
	private List<Carga> result;
	
	/**
	 * combo de agente de cargas.
	 */
	private List<PessoaJuridica> comboAgenteCargas;

	/**
	 * Primeiro metodo executado
	 */
	@PostConstruct
	public void inicializar() {
		inicializarObjetos();		
		doPesquisar( null );
	}
	
	/**
	 * Inicializa os objetos de tela.
	 */
	public void inicializarObjetos() {
		filtro = new BasicFiltroCarga();
		filtro.setInvoice( new Invoice() );
		filtro.setItemInvoice(new ItemInvoice());
		filtro.setRota(new Rota());
		filtro.setCidadeAtual( new Cidade() );
		
		result = new ArrayList<Carga>();
		
		popularComboAgenteCargas();
		
	}
	
	/**
	 * Popular o combobox de agente de cargas.
	 */
	private void popularComboAgenteCargas() {
		comboAgenteCargas = new ArrayList<PessoaJuridica>();
		
		try {
			comboAgenteCargas = cadastro.listarAllPersons( 
					new BasicFiltroPessoaJuridica( new TipoPessoa( TipoPessoaEnum.A_CARGA.toString() ), null ) );

		} catch (BusinessException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Metodo de Pesquisa
	 */
	@Override
	public void doPesquisar(ActionEvent arg0) {
		FacesContext fc = FacesContext.getCurrentInstance();

		try {
			//LISTAR APENAS CARGAS COM STATUS NA FABRICA
			filtro.setStatus( StatusCarga.AT );
			
			result = facade.listCargasBroker(filtro);
			if (result.isEmpty()) {
				String message = TemplateMessageHelper.getMessage(MensagensSistema.SISTEMA,"MSG008",fc.getViewRoot().getLocale());
				Messages.adicionaMensagemDeInfo(message);
			}
			setPaginaAtual(1);
		} catch (BusinessException e) {
			logger.error("error: {} " + e);
			e.printStackTrace();
			Messages.adicionaMensagemDeErro(TemplateMessageHelper
					.getMessage(MensagensSistema.SISTEMA, e));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public int getTotalRegistros() {
		if ( result != null )
			return result.size();
		return 0;
	}

	@Override
	public void limpar() {
		
		filtro = new BasicFiltroCarga();
		filtro.setInvoice( new Invoice() );
		filtro.setItemInvoice(new ItemInvoice());
		filtro.setRota(new Rota());
		filtro.setCidadeAtual( new Cidade() );
		
		result = new ArrayList<Carga>();
		
	}

	@Override
	public void refazerPesquisa() {
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
	 * @return the result
	 */
	public List<Carga> getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(List<Carga> result) {
		this.result = result;
	}

	/**
	 * @return the filtro
	 */
	public BasicFiltroCarga getFiltro() {
		return filtro;
	}

	/**
	 * @param filtro the filtro to set
	 */
	public void setFiltro(BasicFiltroCarga filtro) {
		this.filtro = filtro;
	}

}
