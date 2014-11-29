package br.com.ilog.cadastro.presentation.mbean;

import java.io.Serializable;
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
import br.com.ilog.cadastro.business.entity.Filial;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroFilial;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.geral.presentation.CommonsList;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;


@Component("mBeanPesquisarFilial")
@AccessScoped
public class MBeanPesquisarFilial extends AbstractPaginacao implements Serializable {

	
	private static final long serialVersionUID = 6420500814981358901L;
	
	private static Logger logger = LoggerFactory.getLogger(MBeanPesquisarFilial.class);
	
	
	@Resource(name="controllerCadastro")
	CadastroFacade filialFacade;
	
	private List<Filial> filiais;
	private BasicFiltroFilial filtro;
	private List<SelectItem> comboAtivo;
	private Boolean teste;
	
	private List<SelectItem> comboTeste;
	
	@Resource( name = "commonsList")
	CommonsList commonsList;
	
	
	
	
	@SuppressWarnings("unused")
	@PostConstruct
	private void inicializar() {
		try {
			
			filtro = new BasicFiltroFilial();
			filiais =  Collections.emptyList();
			comboAtivo = commonsList.listaBooleanAtivoInativo();
			comboTeste =   commonsList.listaBooleanAtivoInativo();
			filiais = Collections.emptyList();
			doPesquisar(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void doPesquisar(ActionEvent arg0) {
		FacesContext fc = FacesContext.getCurrentInstance();

		try {
			filiais = filialFacade.listarFilialByFilter(filtro);

			if (filiais.isEmpty()) {
				String msg = TemplateMessageHelper.getMessage(
						MensagensSistema.SISTEMA, "MSG008", fc.getViewRoot()
								.getLocale());
				Messages.adicionaMensagemDeInfo(msg);
			}
		} catch (BusinessException e) {
			e.printStackTrace();

			logger.error("erro: {} " + e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.FILIAL, e));
		} catch (Exception e) {
			e.printStackTrace();

			logger.error("erro: {} " + e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG_001", fc.getViewRoot()
							.getLocale()));
		}

	}

	@Override
	public int getTotalRegistros() {
		if ( filiais != null )
			return filiais.size();
		else {
			return 0;
		}
	}

	@Override
	public void limpar() {
		filtro =  new BasicFiltroFilial();
		filiais.clear();
		
	}

	@Override
	public void refazerPesquisa() {
		doPesquisar(null);
		
	}

	public List<Filial> getFiliais() {
		return filiais;
	}

	public void setFiliais(List<Filial> filiais) {
		this.filiais = filiais;
	}

	public BasicFiltroFilial getFiltro() {
		return filtro;
	}

	public void setFiltro(BasicFiltroFilial filtro) {
		this.filtro = filtro;
	}


	public List<SelectItem> getComboAtivo() {
		return comboAtivo;
	}


	public void setComboAtivo(List<SelectItem> comboAtivo) {
		this.comboAtivo = comboAtivo;
	}


	public Boolean getTeste() {
		return teste;
	}


	public void setTeste(Boolean teste) {
		this.teste = teste;
	}


	public List<SelectItem> getComboTeste() {
		return comboTeste;
	}


	public void setComboTeste(List<SelectItem> comboTeste) {
		this.comboTeste = comboTeste;
	}

}
