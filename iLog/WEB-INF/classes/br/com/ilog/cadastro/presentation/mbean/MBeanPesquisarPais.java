package br.com.ilog.cadastro.presentation.mbean;

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
import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citspresentation.mbeans.AbstractPaginacao;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.Continente;
import br.com.ilog.cadastro.business.entity.Pais;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroPais;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.cadastro.presentation.util.ExceptionFiltro;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;

/**
 * @author WISLEY
 */

@Component("mBeanPesquisarPais")
@AccessScoped
public class MBeanPesquisarPais extends AbstractPaginacao implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8764972105092169157L;

	static Logger logger = LoggerFactory.getLogger(MBeanPesquisarPais.class);

	@Resource(name="controllerCadastro")
	CadastroFacade paisFacade;

	/**
	 * combo de regiao.
	 */
	private List<SelectItem> comboRegiao;
	
	private List<Pais> paises;
	private BasicFiltroPais filtro;

	//private boolean refazerPesquisa = false;
	
	/**
	 * metodo inicializar os componentes da tela de pesquisa.
	 */
	@SuppressWarnings("unused")
	@PostConstruct
	private void inicializar() {
		
		filtro = new BasicFiltroPais();
		paises = Collections.emptyList();

		popularComboRegiao();
		
		doPesquisar( null );
		
	}	
	
	/**
	 * 
	 */
	private void popularComboRegiao () {
		comboRegiao = new ArrayList<SelectItem>();
		
		for (Continente continente : Continente.values()) {
			comboRegiao.add( new SelectItem( 
					continente, TemplateMessageHelper.getMessage(MensagensSistema.CONTINENTE, continente.name() ) ) );
		}
	}
	
	public void chamarPesquisa(){
		doPesquisar(null);
	}

	public String voltar(String param){
		System.out.println(param);
		return param;
	}
	
	Pais pais;
	@Override
	public void doPesquisar(ActionEvent arg0) {
		FacesContext fc = FacesContext.getCurrentInstance();
		try {
			/*Pais paisPadrao = new Pais();
			try{
				paisPadrao = paisFacade.getPaisByPadrao();
				if(paisPadrao == null && !this.refazerPesquisa ){
					String msg = TemplateMessageHelper.getMessage(MensagensSistema.PAIS,"lblNenhumPaisPadraoSelecionado",fc.getViewRoot().getLocale());
					Messages.adicionaMensagemDeInfo(msg);
				}
			}catch (BusinessException e) {
			}*/
			
			try {
				
				if ( filtro != null && filtro.getNomePais() != null ) {
					filtro.setNomePais(filtro.getNomePais().trim());
				}
				paises = paisFacade.listarPaises(filtro);
				
				if (paises.isEmpty()) {
					String msg = TemplateMessageHelper.getMessage(MensagensSistema.SISTEMA,"MSG008",fc.getViewRoot().getLocale());
					Messages.adicionaMensagemDeInfo(msg);
					
				}
			} catch (BusinessException e) {
				e.printStackTrace();
				logger.error("erro: {} " + e);
				Messages.adicionaMensagemDeErro(TemplateMessageHelper
						.getMessage(MensagensSistema.PAIS, e));
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(MensagensSistema.SISTEMA,"MSG_001",fc.getViewRoot().getLocale()));
		}

	}
	
	//Long que captura o id
	private Long idObjeto;
	/**
	 * @coment captura o id do item selecionados
	 * @param index
	 */
	
	public void capturarId(int index){
		Pais objeto  = paises.get(index);
		idObjeto = objeto.getId();
	}
	/**
	 * @param id
	 */
	public void excluir(  ) {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			paisFacade.excluirPais( new Pais( idObjeto ) );
			Messages.adicionaMensagemDeInfo(TemplateMessageHelper.getMessage(MensagensSistema.SISTEMA,"MSG_EXCLUIR_SUCESSO",fc.getViewRoot().getLocale()));
			this.refazerPesquisa();
			
		} catch (Exception e) {
			logger.error("error: {}", e);			
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(ExceptionFiltro.recursiveException(e)));
			return;
		}
	}
	
	@Override
	public int getTotalRegistros() {
		if ( paises != null )
			return paises.size();
		else {
			return 0;
		}
	}

	@Override
	public void limpar() {
		filtro =  new BasicFiltroPais();
		paises.clear();
		
	}

	@Override
	public void refazerPesquisa() {
		//refazerPesquisa = true;
		doPesquisar(null);
		//refazerPesquisa = false;
	}
	
	public BasicFiltroPais getFiltro() {
		return filtro;
	}

	public void setFiltro(BasicFiltroPais filtro) {
		this.filtro = filtro;
	}

	public List<Pais> getPaises() {
		return paises;
	}

	public void setPaises(List<Pais> paises) {
		this.paises = paises;
	}

	public Long getIdObjeto() {
		return idObjeto;
	}

	public void setIdObjeto(Long idObjeto) {
		this.idObjeto = idObjeto;
	}

	/**
	 * @param paisFacade the paisFacade to set
	 */
	public void setPaisFacade(CadastroFacade paisFacade) {
		this.paisFacade = paisFacade;
	}

	/**
	 * @return the pais
	 */
	public Pais getPais() {
		return pais;
	}

	/**
	 * @param pais the pais to set
	 */
	public void setPais(Pais pais) {
		this.pais = pais;
	}

	/**
	 * @return the comboRegiao
	 */
	public List<SelectItem> getComboRegiao() {
		popularComboRegiao();
		return comboRegiao;
	}

	/**
	 * @param comboRegiao the comboRegiao to set
	 */
	public void setComboRegiao(List<SelectItem> comboRegiao) {
		this.comboRegiao = comboRegiao;
	}
}
