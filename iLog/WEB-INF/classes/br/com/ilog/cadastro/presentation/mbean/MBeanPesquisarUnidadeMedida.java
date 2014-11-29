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
import br.com.ilog.cadastro.business.entity.UnidadeMedida;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroUnidMedida;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.geral.presentation.CommonsList;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;


@Component("mBeanPesquisarUnidadeMedida")
@AccessScoped
class MBeanPesquisarUnidadeMedida extends AbstractPaginacao implements Serializable{

	
	private static final long serialVersionUID = -1049278265038312511L;
	private static Logger logger = LoggerFactory
			.getLogger(MBeanPesquisarUnidadeMedida.class);
	
	@Resource(name = "controllerCadastro")
	CadastroFacade unidadeFacade;

	@Resource(name = "commonsList")
	CommonsList commonsList;
	

	private List<SelectItem> comboAtivo;
	private BasicFiltroUnidMedida filtro;
	private List<UnidadeMedida> unidades;
	
	@SuppressWarnings("unused")
	@PostConstruct
	private void inicializar() {
		filtro = new BasicFiltroUnidMedida();
		unidades =  Collections.emptyList();
		comboAtivo = commonsList.listaBooleanAtivoInativo();

		doPesquisar(null);
	}
	
	
	@Override
	public void doPesquisar(ActionEvent arg0) {
		FacesContext fc = FacesContext.getCurrentInstance();

		try {
			unidades = unidadeFacade.listarUnidMedidaByFiltro(filtro);
			if (unidades.isEmpty()) {
				String msg = TemplateMessageHelper.getMessage(
						MensagensSistema.SISTEMA, "MSG008", fc.getViewRoot()
								.getLocale());
				Messages.adicionaMensagemDeInfo(msg);
			}
		} catch (BusinessException e) {
			e.printStackTrace();
			logger.error("erro: {} " + e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.UNIDADE_MEDIDA, e));
		}
	}

	@Override
	public int getTotalRegistros() {
		return 0;
	}

	@Override
	public void limpar() {
		filtro = new BasicFiltroUnidMedida();
		unidades.clear();
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

	public BasicFiltroUnidMedida getFiltro() {
		return filtro;
	}

	public void setFiltro(BasicFiltroUnidMedida filtro) {
		this.filtro = filtro;
	}

	public List<UnidadeMedida> getUnidades() {
		return unidades;
	}

	public void setUnidades(List<UnidadeMedida> unidades) {
		this.unidades = unidades;
	}

}
