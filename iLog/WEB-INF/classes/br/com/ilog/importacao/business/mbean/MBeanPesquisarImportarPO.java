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
import br.com.ilog.cadastro.business.entity.FormaPagamento;
import br.com.ilog.cadastro.business.entity.PessoaJuridica;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.cadastro.presentation.util.ExceptionFiltro;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;
import br.com.ilog.importacao.business.entity.ItemPO;
import br.com.ilog.importacao.business.entity.PO;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroPO;
import br.com.ilog.importacao.business.facade.ImportacaoFacade;

/**
 * @author Manoel Neto
 * @date   03/08/2012
 * @coment Classe destinada a pesquisa das PO importadas do SAP
 * @file foxconn - UC38
 *
 */
@Controller("mBeanPesquisarImportarPO")
@AccessScoped
public class MBeanPesquisarImportarPO extends AbstractPaginacao {
	
	/**
	 * long / MBeanPesquisarImportarPO.java
	 */
	private static final long serialVersionUID = 3388444932638271631L;
	private static Logger logger = LoggerFactory.getLogger(MBeanPesquisarImportarPO.class);
	private List<PO> result;
	private List<PessoaJuridica> listFornecedores;
	private List<FormaPagamento> listFormaPagamentos;
	private PO po;
	
	
	@Resource(name = "controllerImportacao")
	private ImportacaoFacade importacaoFacade;
	@Resource(name = "controllerCadastro")
	private CadastroFacade cadastroFacade;
		
	private BasicFiltroPO filtroPO;
	private List<ItemPO> itemPOs;
	
	/**
	 *  Metodo para inicializar os objetos da pesquisa
	 */
	@PostConstruct
	private void inicializarObjetos() {
		po = new PO();
		filtroPO = new BasicFiltroPO();
		result = new ArrayList<PO>();
		itemPOs = new ArrayList<ItemPO>();
		/*
		 * Preenche o combo Forma de Pagamento
		 */
		try {
			listFormaPagamentos = cadastroFacade.listarFormasPagamentoAtivos();
			listFornecedores = cadastroFacade.listarAllFornecedoresByStatus(true);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		doPesquisar(null);
	}
	
	public void excluir(ActionEvent ev){
		try {
			FacesContext fc = FacesContext.getCurrentInstance();

			importacaoFacade.excluirPO(po);
			Messages.adicionaMensagemDeInfo(TemplateMessageHelper
					.getMessage(MensagensSistema.SISTEMA,"MSG_EXCLUIR_SUCESSO", fc.getViewRoot().getLocale()));
			inicializarObjetos();

		} catch (Exception e) {
			logger.error("error: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper
					.getMessage(ExceptionFiltro.recursiveException(e)));
		}
	}


	/* (non-Javadoc)
	 * @see br.cits.commons.citspresentation.mbeans.AbstractPaginacao#doPesquisar(javax.faces.event.ActionEvent)
	 */
	@Override
	public void doPesquisar(ActionEvent arg0) {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();

			result = importacaoFacade.listarPO(filtroPO);
			if (result.isEmpty()) {
				String message = TemplateMessageHelper.getMessage(
						MensagensSistema.SISTEMA, "MSG008", fc.getViewRoot()
								.getLocale());
				Messages.adicionaMensagemDeInfo(message);
			}

		} catch (BusinessException e1) {
			logger.error("error: {} " + e1);
			e1.printStackTrace();
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, e1));
		}

		
	}

	/* (non-Javadoc)
	 * @see br.cits.commons.citspresentation.mbeans.AbstractPaginacao#getTotalRegistros()
	 */
	@Override
	public int getTotalRegistros() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see br.cits.commons.citspresentation.mbeans.AbstractPaginacao#limpar()
	 */
	@Override
	public void limpar() {
		inicializarObjetos();
		result.clear();
		
	}

	
	/* (non-Javadoc)
	 * @see br.cits.commons.citspresentation.mbeans.AbstractPaginacao#refazerPesquisa()
	 */
	@Override
	public void refazerPesquisa() {
		// TODO Auto-generated method stub
		
	}


	/**
	 * @return the result
	 */
	public List<PO> getResult() {
		return result;
	}


	/**
	 * @param result the result to set
	 */
	public void setResult(List<PO> result) {
		this.result = result;
	}


	/**
	 * @return the filtroPO
	 */
	public BasicFiltroPO getFiltroPO() {
		return filtroPO;
	}


	/**
	 * @param filtroPO the filtroPO to set
	 */
	public void setFiltroPO(BasicFiltroPO filtroPO) {
		this.filtroPO = filtroPO;
	}
	/**
	 * @return the itemPOs
	 */
	public List<ItemPO> getItemPOs() {
		return itemPOs;
	}
	/**
	 * @param itemPOs the itemPOs to set
	 */
	public void setItemPOs(List<ItemPO> itemPOs) {
		this.itemPOs = itemPOs;
	}



	/**
	 * @return the listFornecedores
	 */
	public List<PessoaJuridica> getListFornecedores() {
		return listFornecedores;
	}



	/**
	 * @param listFornecedores the listFornecedores to set
	 */
	public void setListFornecedores(List<PessoaJuridica> listFornecedores) {
		this.listFornecedores = listFornecedores;
	}



	/**
	 * @return the listFormaPagamentos
	 */
	public List<FormaPagamento> getListFormaPagamentos() {
		return listFormaPagamentos;
	}



	/**
	 * @param listFormaPagamentos the listFormaPagamentos to set
	 */
	public void setListFormaPagamentos(List<FormaPagamento> listFormaPagamentos) {
		this.listFormaPagamentos = listFormaPagamentos;
	}



	/**
	 * @return the po
	 */
	public PO getPo() {
		return po;
	}



	/**
	 * @param po the po to set
	 */
	public void setPo(PO po) {
		this.po = po;
	}
}
