package br.com.ilog.importacao.business.mbean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citspresentation.converter.EntityConverter;
import br.cits.commons.citspresentation.mbeans.AbstractPaginacao;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.Filial;
import br.com.ilog.cadastro.business.entity.Incoterm;
import br.com.ilog.cadastro.business.entity.Modal;
import br.com.ilog.cadastro.business.entity.Moeda;
import br.com.ilog.cadastro.business.entity.PessoaJuridica;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroFilial;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;
import br.com.ilog.importacao.business.entity.Invoice;
import br.com.ilog.importacao.business.entity.StatusInvoice;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroInvoice;
import br.com.ilog.importacao.business.facade.ImportacaoFacade;

/**
 * @author Manoel Neto
 * @date 23/08/2012
 * @coment Classe responsável para realizar a pesquisa, exclusão da Invoice 
 * @file foxconn - UC21
 * 
 */

@Controller("mBeanPesquisarInvoice")
@AccessScoped
public class MBeanPesquisarInvoice extends AbstractPaginacao {

	private static final long serialVersionUID = -2526344383115494492L;

	private static Logger logger = LoggerFactory.getLogger(MBeanPesquisarInvoice.class);
	
	@Resource(name = "controllerImportacao")
	private ImportacaoFacade facade;

	@Resource(name = "controllerCadastro")
	private CadastroFacade cadastroFacade;

	private BasicFiltroInvoice filtro;

	private List<PessoaJuridica> fornecedores;
	private EntityConverter<PessoaJuridica> fornecedoresConverter;
	
	private List<Filial> filiais;
	private EntityConverter<Filial> filiaisConverter;
	
	private List<StatusInvoice> status;
	
	private List<Moeda> moedas;
	private EntityConverter<Moeda> moedasConverter;
	
	private List<Modal> modais;
	private EntityConverter<Modal> modaisConverter;
	
	private List<Incoterm> incoterms;
	private EntityConverter<Incoterm> incotermsConverter;
	
	private List<Invoice> resultado;

	@PostConstruct
	public void inicializar() {
		resultado = Collections.emptyList();
		filtro = new BasicFiltroInvoice();
		this.inicializarCombos();
		doPesquisar(null);

	}

	/**
	 * inicializar as combos.
	 */
	private void inicializarCombos() {
		try {

			/**
			 * @coment Preenche a lista com as pessoas juridicas do tipo 
			 * Fornecedores.
			 */
			fornecedores = new ArrayList<PessoaJuridica>();
			List<PessoaJuridica> fornecs = cadastroFacade
					.listarAllFornecedoresByStatus(null);
			if (fornecs != null)
				fornecedores = fornecs;

			/**
			 * @Coment Lista de Filiais cadastradas no sistema
			 */
			filiais = new ArrayList<Filial>();
			BasicFiltroFilial basicFiltroFilial = new BasicFiltroFilial();
			basicFiltroFilial.setAtivo(true);
			filiais = cadastroFacade.listarFilialByFilter(basicFiltroFilial);
			filiaisConverter = new EntityConverter<Filial>(filiais); 
			
			if(filiais == null){
				filiais = new ArrayList<Filial>();
			}
			
			/**
			 * @Coment Lista de status: Cadastrada, Planejada e Em Trânsito 
			 */
			status = new ArrayList<StatusInvoice>();
			for (StatusInvoice st : StatusInvoice.valores()) {
				status.add(st);
			}

			/**
			 * @coment lista de moedas cadastradas no sistema
			 */
			moedas = new ArrayList<Moeda>();
			moedas = cadastroFacade.listarMoedas();
			moedasConverter = new EntityConverter<Moeda>(moedas);
			
			/**
			 * @comment lista de modais cadastrados no sistema
			 */
			modais = new ArrayList<Modal>();
			modais = cadastroFacade.listarModals();
			modaisConverter = new EntityConverter<Modal>(modais);
			
			/**
			 * @comment Lista de incoterms cadastrados no sistema
			 */
			incoterms = new ArrayList<Incoterm>();
			incoterms = cadastroFacade.listarIncoterm();
			incotermsConverter = new EntityConverter<Incoterm>(incoterms);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private Integer i; 

	/**
	 * @Fluxo Fluxo básico - Consultar Invoice 
	 */
	@Override
	public void doPesquisar(ActionEvent arg0) {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			
			resultado = facade.listarInvoicesComCarga(filtro);

			if (resultado.isEmpty()) {
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

	@Override
	public int getTotalRegistros() {
		if (resultado != null) {
			return resultado.size();
		} else {
			return 0;
		}
	}


	/**
	 * @param id
	 */
	public void excluir(ActionEvent ev) {
//		try {
//			FacesContext fc = FacesContext.getCurrentInstance();
//
//			Invoice invoice = facade.getInvoiceById(idInvoice, true);
//
//			if (!invoice.getStatus().equals(StatusInvoice.C)) {
//				throw new UnsupportedOperationException();
//			}
//
//			facade.excluirInvoice(invoice);
//			Messages.adicionaMensagemDeInfo(TemplateMessageHelper.getMessage(
//					"MSG_EXCLUIR_SUCESSO", fc.getViewRoot().getLocale()));
//			this.refazerPesquisa();
//
//		} catch (UnsupportedOperationException e) {
//			logger.error("erro: {}", e);
//			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
//					MensagensSistema.INVOICE, "MSG_GEN_001"));
//			return;
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("erro: {}", e);
//			Messages.adicionaMensagemDeErro(TemplateMessageHelper
//					.getMessage(ExceptionFiltro.recursiveException(e)));
//		}
	}

	@Override
	public void limpar() {
		resultado.clear();
		filtro = new BasicFiltroInvoice();
	}

	@Override
	public void refazerPesquisa() {
		if (filtro == null)
			filtro = new BasicFiltroInvoice();
		// Se a lista estava vazia antes não é necessário
		// fazer uma nova pesquisa
		if (resultado.isEmpty())
			return;

		doPesquisar(null);
	}

	public List<Invoice> getResultado() {
		return resultado;
	}

	public void setResultado(List<Invoice> resultado) {
		this.resultado = resultado;
	}

	public BasicFiltroInvoice getFiltro() {
		return filtro;
	}

	public void setFiltro(BasicFiltroInvoice filtro) {
		this.filtro = filtro;
	}
	
	public List<StatusInvoice> getStatus() {
		return status;
	}

	public void setStatus(List<StatusInvoice> status) {
		this.status = status;
	}

	public EntityConverter<PessoaJuridica> getFornecedoresConverter() {
		return fornecedoresConverter;
	}

	public void setFornecedoresConverter(
			EntityConverter<PessoaJuridica> fornecedoresConverter) {
		this.fornecedoresConverter = fornecedoresConverter;
	}

	public List<PessoaJuridica> getFornecedores() {
		return fornecedores;
	}

	public void setFornecedores(List<PessoaJuridica> fornecedores) {
		this.fornecedores = fornecedores;
	}

	/**
	 * @return the filiais
	 */
	public List<Filial> getFiliais() {
		return filiais;
	}

	/**
	 * @param filiais the filiais to set
	 */
	public void setFiliais(List<Filial> filiais) {
		this.filiais = filiais;
	}

	/**
	 * @return the filiaisConverter
	 */
	public EntityConverter<Filial> getFiliaisConverter() {
		return filiaisConverter;
	}

	/**
	 * @param filiaisConverter the filiaisConverter to set
	 */
	public void setFiliaisConverter(EntityConverter<Filial> filiaisConverter) {
		this.filiaisConverter = filiaisConverter;
	}

	/**
	 * @return the moedas
	 */
	public List<Moeda> getMoedas() {
		return moedas;
	}

	/**
	 * @param moedas the moedas to set
	 */
	public void setMoedas(List<Moeda> moedas) {
		this.moedas = moedas;
	}

	/**
	 * @return the moedasConverter
	 */
	public EntityConverter<Moeda> getMoedasConverter() {
		return moedasConverter;
	}

	/**
	 * @param moedasConverter the moedasConverter to set
	 */
	public void setMoedasConverter(EntityConverter<Moeda> moedasConverter) {
		this.moedasConverter = moedasConverter;
	}

	/**
	 * @return the modais
	 */
	public List<Modal> getModais() {
		return modais;
	}

	/**
	 * @param modais the modais to set
	 */
	public void setModais(List<Modal> modais) {
		this.modais = modais;
	}

	/**
	 * @return the modaisConverter
	 */
	public EntityConverter<Modal> getModaisConverter() {
		return modaisConverter;
	}

	/**
	 * @param modaisConverter the modaisConverter to set
	 */
	public void setModaisConverter(EntityConverter<Modal> modaisConverter) {
		this.modaisConverter = modaisConverter;
	}

	/**
	 * @return the incoterms
	 */
	public List<Incoterm> getIncoterms() {
		return incoterms;
	}

	/**
	 * @param incoterms the incoterms to set
	 */
	public void setIncoterms(List<Incoterm> incoterms) {
		this.incoterms = incoterms;
	}

	/**
	 * @return the incotermsConverter
	 */
	public EntityConverter<Incoterm> getIncotermsConverter() {
		return incotermsConverter;
	}

	/**
	 * @param incotermsConverter the incotermsConverter to set
	 */
	public void setIncotermsConverter(EntityConverter<Incoterm> incotermsConverter) {
		this.incotermsConverter = incotermsConverter;
	}

	/**
	 * @return the i
	 */
	public Integer getI() {
		i=25;
		return i;
	}

	/**
	 * @param i the i to set
	 */
	public void setI(Integer i) {
		this.i = i;
	}

}
