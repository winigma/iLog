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
import br.com.ilog.cadastro.business.entity.Nivel;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroNivel;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.geral.presentation.CommonsList;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;

@Component("mBeanPesquisarNivel")
@AccessScoped
public class MBeanPesquisarNivel extends AbstractPaginacao implements
		Serializable {

	private static final long serialVersionUID = -8040402863226624253L;
	private static Logger logger = LoggerFactory
			.getLogger(MBeanPesquisarNivel.class);

	@Resource(name = "controllerCadastro")
	CadastroFacade nivelFacade;

	@Resource(name = "commonsList")
	CommonsList commonsList;

	private List<SelectItem> comboAtivo;
	private BasicFiltroNivel filtro;
	private List<Nivel> niveis;

	@SuppressWarnings("unused")
	@PostConstruct
	private void inicializar() {
		filtro = new BasicFiltroNivel();
		niveis = Collections.emptyList();
		comboAtivo = commonsList.listaBooleanAtivoInativo();

		doPesquisar(null);
	}

	@Override
	public void doPesquisar(ActionEvent arg0) {
		FacesContext fc = FacesContext.getCurrentInstance();
		try {
			
			niveis =  nivelFacade.listarNivelByFiltro(filtro);
			if (niveis.isEmpty()) {
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
		}

	}

	@Override
	public int getTotalRegistros() {
		return 0;
	}

	@Override
	public void limpar() {
       filtro =  new BasicFiltroNivel();
       niveis.clear();
	}

	@Override
	public void refazerPesquisa() {
		// TODO Auto-generated method stub

	}

	public List<SelectItem> getComboAtivo() {
		return comboAtivo;
	}

	public void setComboAtivo(List<SelectItem> comboAtivo) {
		this.comboAtivo = comboAtivo;
	}

	public BasicFiltroNivel getFiltro() {
		return filtro;
	}

	public void setFiltro(BasicFiltroNivel filtro) {
		this.filtro = filtro;
	}

	public List<Nivel> getNiveis() {
		return niveis;
	}

	public void setNiveis(List<Nivel> niveis) {
		this.niveis = niveis;
	}

}
