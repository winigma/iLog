package br.com.ilog.importacao.business.mbean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.hibernate.validator.ValidatorHelper;
import br.cits.commons.citspresentation.mbeans.AbstractManter;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.Cidade;
import br.com.ilog.cadastro.business.entity.Moeda;
import br.com.ilog.cadastro.business.entity.PessoaJuridica;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroMoeda;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.cadastro.presentation.util.ConverterUtil;
import br.com.ilog.cadastro.presentation.util.ExceptionFiltro;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;
import br.com.ilog.importacao.business.entity.Carga;
import br.com.ilog.importacao.business.entity.CustoDI;
import br.com.ilog.importacao.business.entity.CustoImportacao;
import br.com.ilog.importacao.business.facade.ImportacaoFacade;

/**
 * Classe para manter os custos de uma DI.
 * @author Heber Santiago
 *
 */
@AccessScoped
@Controller( "mBeanManterCusto" )
public class MBeanManterCusto extends AbstractManter {

	/** */
	private static final long serialVersionUID = -3978658466530613670L;

	@Resource( name = "controllerImportacao" )
	ImportacaoFacade facade;
	
	@Resource( name = "controllerCadastro" )
	CadastroFacade cadastro;
	
	private static Logger logger = LoggerFactory.getLogger(MBeanManterCusto.class);
	
	/**
	 * Carga a ser editado os valores de custos.
	 */
	private Carga carga;
	
	private CustoDI custoDI;
	
	private Integer index;
	
	private List<PessoaJuridica> fornecedores;
	
	private List<Moeda> comboMoedas;
	private ConverterUtil<PessoaJuridica> converterPJ;
	private PessoaJuridica fornecedor;
	
	/**
	public  List<PessoaJuridica> listaSapVendor(String query){
		try{
			List<PessoaJuridica> lista = cadastro.listarAllPersons(null);
			List<PessoaJuridica> sugestoes =  new ArrayList<PessoaJuridica>();
			for (PessoaJuridica pj : lista) {
				String sapVendor = String.valueOf(pj.getVendorSap());
				if(sapVendor.startsWith(query)){
					sugestoes.add(pj);
				}
			}
			converterPJ = new ConverterUtil<PessoaJuridica>(sugestoes);
			if(fornecedor != null)
			System.out.println(fornecedor.getVendorSap());
			return sugestoes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	**/
	
	@Override
	public String editar() {
		
		edicao = true;
		Long id = carga.getId();
		fornecedor =  new PessoaJuridica();
		
		try {
			carga = (Carga) facade.getCargaById(id);
			
			if ( carga.getRota() != null ) {
				carga.setRota( cadastro.getRotaById( carga.getRota().getId() ) );
			}
			
			carregarCustos();
			
			return "editarCustos.jsf";
			
		} catch (BusinessException e) {
			logger.error("erro: {}", e);
			e.printStackTrace();
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.CARGA, e));
		}
		
		return null;
	}
	
	/**
	 * Popular combo de moedas;
	 */
	private void popularComboMoedas() {
		comboMoedas = new ArrayList<Moeda>();
		
		try {
			
			comboMoedas = cadastro.listarMoedas( new BasicFiltroMoeda( true ) );
			
		} catch (BusinessException e) {
		}
		
	}

	/**
	 * Popular combo de Fornecedores
	 */
	private ConverterUtil<PessoaJuridica> conPJ;
	private Cidade cidade;
	private List<Cidade> listaCidade;
	private ConverterUtil<Cidade> cidadeConverter;
	private void popularComboFornecedores() {
		
		fornecedores = new ArrayList<PessoaJuridica>();
		
		try {
			
			fornecedores = cadastro.listarAllFornecedoresByStatus(true);
			//listaCidade =  cadastro.listarCidades(null);
			//cidadeConverter = new ConverterUtil<Cidade>(listaCidade);
			//cidade =  new Cidade();
			converterPJ= new ConverterUtil<PessoaJuridica>(fornecedores);
			
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	
	
	/**
	 * Carrega os custos para os custos de DI.
	 * @throws BusinessException
	 */
	private void carregarCustos() throws BusinessException {
		if (carga != null) {
			if ( carga.getCustosDi() == null || carga.getCustosDi().isEmpty() ) {
				
				List<CustoImportacao> custosImport = facade.listarCustosImportacao();
				Moeda moedaPadrao = cadastro.getMoedaPadrao();
				
				carga.setCustosDi( new ArrayList<CustoDI>() );
				for (CustoImportacao cost : custosImport) {
					CustoDI custoDI = new CustoDI();
					custoDI.setCarga( carga );
					custoDI.setCustoImportacao( cost );
					custoDI.setMoeda( moedaPadrao );
					custoDI.setTaxaCambio( BigDecimal.ZERO );
					custoDI.setValorCusto( BigDecimal.ZERO );
					
					carga.getCustosDi().add( custoDI );
				}
			} else {
				carga = facade.carregarCustos( carga );
			}
		}
	}

	/**
	 * Recupearar o trecho a ser editado.
	 */
	public void editarCusto() {
		if ( custoDI != null ) {
			System.err.println("TESTE");
		}
		//index = i;
		//custoDI = carga.getCustosDi().get( index );
		
	}
	
	@Override
	public String excluir() {
		//Nao será permitido excluir registro nesta tela.
		return null;
	}

	@Override
	@PostConstruct
	public void inicializarObjetos() {
		carga = new Carga();
		custoDI = new CustoDI();
		custoDI.setFornecedor( new PessoaJuridica() );
		custoDI.setMoeda( new Moeda() );
		
		popularComboFornecedores();
		
		popularComboMoedas();
		
		edicao = false;
	}

	public void salvarCusto() {
		System.out.println();
	}
	
	public String salvar() {
		
		FacesContext fc = FacesContext.getCurrentInstance();
		ResourceBundle resource = TemplateMessageHelper.getResourceBundle(
				MensagensSistema.ROTA, fc.getViewRoot().getLocale());
		List<String> erros = ValidatorHelper.valida( carga, TemplateMessageHelper
				.getResourceBundle(MensagensSistema.SISTEMA, fc.getViewRoot()
						.getLocale()), resource);

		if (erros.isEmpty()) {

			try {
				
				if (edicao) {	
					carga = facade.alterarCusto( carga );
				}

			} catch (BusinessException e) {
				e.printStackTrace();
				logger.error("erro: {}", e);
				Messages.adicionaMensagemDeErro(TemplateMessageHelper
						.getMessage(ExceptionFiltro.recursiveException(e)));
				return null;
			}
		} else {
			Messages.adicionaMensagensDeErro(erros);
			return null;
		}

		String message = TemplateMessageHelper.getMessage(
				MensagensSistema.SISTEMA, "MSG001", fc.getViewRoot()
						.getLocale());
		Messages.adicionaMensagemDeInfo(message);
		
		return "custos.jsf";
	}
	
	@Override
	protected void refazerPesquisa() {
	}

	public String getNomeAgenteCarga() {
		try {
			if (carga != null && carga.getRota() != null) {
				return carga.getRota().getAgenteCarga().getNomeFantasia();
			}
		} catch (Exception e) {
		}
		return "";
	}
	
	/**
	 * @return the selectCarga
	 */
	public Carga getCarga() {
		return carga;
	}

	/**
	 * @param selectCarga the selectCarga to set
	 */
	public void setCarga(Carga selectCarga) {
		this.carga = selectCarga;
	}

	/**
	 * @return the index
	 */
	public Integer getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(Integer index) {
		this.index = index;
	}

	/**
	 * @return the custoDI
	 */
	public CustoDI getCustoDI() {
		return custoDI;
	}

	/**
	 * @param custoDI the custoDI to set
	 */
	public void setCustoDI(CustoDI custoDI) {
		this.custoDI = custoDI;
	}

	/**
	 * @return the fornecedores
	 */
	public List<PessoaJuridica> getFornecedores() {
		return fornecedores;
	}

	/**
	 * @param fornecedores the fornecedores to set
	 */
	public void setFornecedores(List<PessoaJuridica> fornecedores) {
		this.fornecedores = fornecedores;
	}

	/**
	 * @return the comboMoedas
	 */
	public List<Moeda> getComboMoedas() {
		return comboMoedas;
	}

	/**
	 * @param comboMoedas the comboMoedas to set
	 */
	public void setComboMoedas(List<Moeda> comboMoedas) {
		this.comboMoedas = comboMoedas;
	}


	public ConverterUtil<PessoaJuridica> getConverterPJ() {
		return converterPJ;
	}


	public void setConverterPJ(ConverterUtil<PessoaJuridica> converterPJ) {
		this.converterPJ = converterPJ;
	}


	public PessoaJuridica getFornecedor() {
		return fornecedor;
	}


	public void setFornecedor(PessoaJuridica fornecedor) {
		this.fornecedor = fornecedor;
	}

	public ConverterUtil<PessoaJuridica> getConPJ() {
		return conPJ;
	}

	public void setConPJ(ConverterUtil<PessoaJuridica> conPJ) {
		this.conPJ = conPJ;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public ConverterUtil<Cidade> getCidadeConverter() {
		return cidadeConverter;
	}

	public void setCidadeConverter(ConverterUtil<Cidade> cidadeConverter) {
		this.cidadeConverter = cidadeConverter;
	}

	public List<Cidade> getListaCidade() {
		return listaCidade;
	}

	public void setListaCidade(List<Cidade> listaCidade) {
		this.listaCidade = listaCidade;
	}

}
