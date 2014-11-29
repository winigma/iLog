package br.com.ilog.cadastro.presentation.mbean;

import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.hibernate.validator.ValidatorHelper;
import br.cits.commons.citspresentation.mbeans.AbstractPaginacao;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.Departamento;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroDepartamento;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.cadastro.presentation.util.ExceptionFiltro;
import br.com.ilog.geral.presentation.CommonsList;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;

/**
 * @author Wisley Bean de pesquisar departamento
 */

@Controller("mBeanPesquisarDepartamento")
@AccessScoped
public class MBeanPesquisarDepartamento extends AbstractPaginacao {

	private static final long serialVersionUID = -4849437144081570859L;
	private static Logger logger = LoggerFactory
			.getLogger(MBeanPesquisarDepartamento.class);

	@Resource(name = "controllerCadastro")
	CadastroFacade departamentoFacade;

	@Resource
	CommonsList commonsList;
	
	private List<Departamento> departamentos;
	private BasicFiltroDepartamento filtro;

	private List<SelectItem> comboAtivo;

	@SuppressWarnings("unused")
	@PostConstruct
	private void inicializar() {
		filtro = new BasicFiltroDepartamento();
		departamentos = Collections.emptyList();
		
		comboAtivo = commonsList.listaBooleanAtivoInativo();
		
		doPesquisar(null);
	}
	
	

	// Long que captura o id
	private Long idObjeto;

	/**
	 * @coment captura o id do item selecionados
	 * @param index
	 */
	public void capturarId(int index) {
		Departamento objeto = departamentos.get(index);
		idObjeto = objeto.getId();
	}

	/**
	 * @param id
	 */
	public void excluir() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			departamentoFacade.excluirDepartamento(new Departamento(idObjeto));
			Messages.adicionaMensagemDeInfo(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG_EXCLUIR_SUCESSO", fc
							.getViewRoot().getLocale()));
			this.refazerPesquisa();
		} catch (Exception e) {
			logger.error("error: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper
					.getMessage(ExceptionFiltro.recursiveException(e)));
			e.printStackTrace();
			return;
		}
	}
	
	public void popularComboBox(){
		
	}

	@Override
	public void doPesquisar(ActionEvent arg0) {
		FacesContext fc = FacesContext.getCurrentInstance();

		try {
			ResourceBundle resourceBundle = TemplateMessageHelper
					.getResourceBundle(MensagensSistema.DEPARTAMENTO, fc
							.getViewRoot().getLocale());
			List<String> errorMessages = ValidatorHelper.valida(filtro,
					resourceBundle, resourceBundle);

			if (errorMessages.isEmpty()) {

				try {
					if (filtro != null && filtro.getDescricao() != null) {
						filtro.setDescricao(filtro.getDescricao().trim());
					}
					departamentos = departamentoFacade
							.listarDepartamentos(filtro);

					if (departamentos.isEmpty() || departamentos == null) {
						String msg = TemplateMessageHelper.getMessage(
								MensagensSistema.SISTEMA, "MSG008", fc
										.getViewRoot().getLocale());
						Messages.adicionaMensagemDeInfo(msg);
					}
					setPaginaAtual(1);

				} catch (BusinessException e) {
					e.printStackTrace();
					logger.error("erro: {} " + e);
					Messages.adicionaMensagemDeErro(TemplateMessageHelper
							.getMessage(MensagensSistema.DEPARTAMENTO, e));

				}

			} else {
				Messages.adicionaMensagensDeErro(errorMessages);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG_001", fc.getViewRoot()
							.getLocale()));

		}

	}

	@Override
	public int getTotalRegistros() {
		if (departamentos != null)
			return departamentos.size();
		return 0;
	}

	@Override
	public void limpar() {
		setPaginaAtual(1);
		departamentos.clear();
		filtro = new BasicFiltroDepartamento();

	}

	@Override
	public void refazerPesquisa() {
		doPesquisar(null);
	}

	public List<Departamento> getDepartamentos() {
		return departamentos;
	}

	public void setDepartamentos(List<Departamento> departamentos) {
		this.departamentos = departamentos;
	}

	public BasicFiltroDepartamento getFiltro() {
		return filtro;
	}

	public void setFiltro(BasicFiltroDepartamento filtro) {
		this.filtro = filtro;
	}

	public Long getIdObjeto() {
		return idObjeto;
	}

	public void setIdObjeto(Long idObjeto) {
		this.idObjeto = idObjeto;
	}

	/**
	 * @return the comboAtivo
	 */
	public List<SelectItem> getComboAtivo() {
		return comboAtivo;
	}

	/**
	 * @param comboAtivo the comboAtivo to set
	 */
	public void setComboAtivo(List<SelectItem> comboAtivo) {
		this.comboAtivo = comboAtivo;
	}

}
