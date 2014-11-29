package br.com.ilog.seguranca.presentation.mbean;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.hibernate.exception.ConstraintViolationException;
import org.primefaces.model.DualListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.hibernate.validator.ValidatorHelper;
import br.cits.commons.citspresentation.mbeans.AbstractManter;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.commons.citspresentation.util.JSFRequestBean;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.presentation.util.ConverterUtil;
import br.com.ilog.cadastro.presentation.util.ExceptionFiltro;
import br.com.ilog.geral.business.CodigoErroEspecifico;
import br.com.ilog.geral.presentation.Constantes;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;
import br.com.ilog.seguranca.business.entity.Funcionalidade;
import br.com.ilog.seguranca.business.entity.Perfil;
import br.com.ilog.seguranca.business.facade.SegurancaFacade;

@Controller("mBeanManterPerfis")
@AccessScoped
public class MBeanManterPerfis extends AbstractManter {

	/** */
	private static final long serialVersionUID = -4991745840084596248L;

	private static Logger logger = LoggerFactory
			.getLogger(MBeanManterPerfis.class);

	@Resource(name = "controleUsuario")
	SegurancaFacade facade;

	private Perfil perfil;
	private List<SelectItem> funcionalidadesDisponiveis;
	private ConverterUtil<Funcionalidade> converterFuncionalidade;

	private boolean novoPerfil;
	private boolean desabilita;

	private Perfil selectPerfil;

	private DualListModel<Funcionalidade> funcionalidadesPickList;

	private List<Funcionalidade> source;
	private List<Funcionalidade> target;

	@Override
	public String editar() {

		Long idRegistro = selectPerfil.getId();

		try {

			perfil = (Perfil) facade.getPerfilById(idRegistro);
			popularFuncionalidades();

			if (Constantes.ADMINISTRADOR.equals(perfil.getNome())) {
				desabilita = true;
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}

		edicao = true;
		return "perfil.jsf";
	}

	/**
	 * Desabilita as funções da tela para o caso de perfil de administrador.
	 * 
	 * @return
	 */
	public boolean disabilitarAdministrador() {

		if (perfil != null && perfil.getId() != null
				&& perfil.getNome().equals("ADMINISTRADOR"))
			return true;

		return false;
	}

	/**
	 * 
	 */
	public void popularFuncionalidades() {

		// Atributos da picklist
		source = new ArrayList<Funcionalidade>();
		target = new ArrayList<Funcionalidade>();

		try {

			List<Funcionalidade> funcionalidadesAux = facade
					.listarFuncionalidades();
			if (funcionalidadesAux != null) {

				source = funcionalidadesAux;
				converterFuncionalidade = new ConverterUtil<Funcionalidade>(
						source);

				target = this.perfil.getPerfilFuncionalidades();

				source.removeAll(target);
				funcionalidadesPickList = new DualListModel<Funcionalidade>(
						source, target);
			}

		} catch (BusinessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String cancelar() {
		return "perfis.jsf";
	}

	@Override
	public String salvar() {
		if(!funcionalidadesPickList.getTarget().isEmpty())
		perfil.setPerfilFuncionalidades(funcionalidadesPickList.getTarget());
		else{
			FacesContext fc = FacesContext.getCurrentInstance();
			String message = TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG069", fc.getViewRoot()
							.getLocale());
			Messages.adicionaMensagemDeInfo(message);
			return null;
		}
			
		try {
			if (Constantes.ADMINISTRADOR.equals(perfil.getNome())) {
				throw new UnsupportedOperationException();
			}
		} catch (UnsupportedOperationException e) {
			FacesContext fc = FacesContext.getCurrentInstance();

			e.printStackTrace();
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG_GEN_007", fc.getViewRoot()
							.getLocale()));
			return null;
		}
		FacesContext fc = FacesContext.getCurrentInstance();

		ResourceBundle resourceBundle = TemplateMessageHelper
				.getResourceBundle(MensagensSistema.PERFIS,
						JSFRequestBean.getLocale());
		List<String> errorMessages = ValidatorHelper
				.valida(perfil,
						TemplateMessageHelper.getResourceBundle(
								MensagensSistema.SISTEMA, fc.getViewRoot()
										.getLocale()), resourceBundle);

		if (errorMessages.isEmpty()) {

			try {

				if (edicao) {
					facade.alterarPerfil(perfil);
				} else {
					facade.cadastrarPerfil(perfil);
				}

			} catch (Exception e) {
				if (!edicao) {
					perfil.setId(null);
					// perfil.setPerfilFuncionalidades(new
					// ArrayList<Funcionalidade>());
					for (Funcionalidade f : perfil.getPerfilFuncionalidades()) {
						if (f.getId() != null)
							f.setId(null);
					}
				}

				ConstraintViolationException ex = new ConstraintViolationException(
						null, null, null);
				if (e.getMessage().contains("ConstraintViolationException"))
					ex = (ConstraintViolationException) e.getCause();
				
				if (e.getMessage().contains("ConstraintViolationException")) {

					if (ex.getSQLException().getNextException().getMessage()
							.contains("uk_perfil")) {
						Messages.adicionaMensagemDeErro(TemplateMessageHelper
								.getMessage(MensagensSistema.PERFIS,
										"msgUnique", fc.getViewRoot()
												.getLocale()));
					}

					return null;

				}
				return null;
			}

		} else {
			Messages.adicionaMensagensDeErro(errorMessages);
			return null;
		}

		itemAtualizado = true;

		String message = TemplateMessageHelper.getMessage(
				MensagensSistema.SISTEMA, "MSG001", fc.getViewRoot()
						.getLocale());
		Messages.adicionaMensagemDeInfo(message);

		refazerPesquisa();

		return "perfis.jsf";
	}

	@Override
	public String excluir() {
		try {

			if (Constantes.ADMINISTRADOR.equals(perfil.getNome())) {
				throw new UnsupportedOperationException();
			}
		} catch (UnsupportedOperationException e) {
			FacesContext fc = FacesContext.getCurrentInstance();

			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG_GEN_007", fc.getViewRoot()
							.getLocale()));
			return null;
		}
		try {
			FacesContext fc = FacesContext.getCurrentInstance();

			facade.excluirPerfil(perfil.getId());
			refazerPesquisa();

			String message = TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG_EXCLUIR_SUCESSO", fc
							.getViewRoot().getLocale());
			Messages.adicionaMensagemDeInfo(message);

		} catch (BusinessException e) {
			logger.error("erro: {}", e);
			String idBundle = e.getCodigo().getIdBundle();
			Messages.adicionaMensagemDeErro(TemplateMessageHelper
					.getMessage(idBundle));
			return null;
		} catch (Exception e) {

			e.printStackTrace();
			logger.error("error: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper
					.getMessage(ExceptionFiltro.recursiveException(e)));
			return null;

		}

		return "perfis.jsf";
	}

	public String novoPerfil() {

		perfil = new Perfil();
		perfil.setPerfilFuncionalidades(new ArrayList<Funcionalidade>());
		novoPerfil = true;
		popularFuncionalidades();

		return "perfil.jsf";
	}

	@Override
	@PostConstruct
	public void inicializarObjetos() {
		perfil = new Perfil();
		perfil.setPerfilFuncionalidades(new ArrayList<Funcionalidade>());
		desabilita = false;
		edicao = false;
	}

	@Override
	protected void refazerPesquisa() {
		// atualiza a tela de pesquisa para atualizar possiveis modificacoes nos
		// campos utilizados na tabela de tela de pesquisa
		MBeanPesquisarPerfis managedBean = (MBeanPesquisarPerfis) JSFRequestBean
				.getManagedBean("mBeanPesquisarPerfis");

		managedBean.refazerPesquisa();

	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public List<SelectItem> getFuncionalidadesDisponiveis() {
		return funcionalidadesDisponiveis;
	}

	public void setFuncionalidadesDisponiveis(
			List<SelectItem> funcionalidadesDisponiveis) {
		this.funcionalidadesDisponiveis = funcionalidadesDisponiveis;
	}

	public boolean isNovoPerfil() {
		return novoPerfil;
	}

	public void setNovoPerfil(boolean novoPerfil) {
		this.novoPerfil = novoPerfil;
	}

	public boolean isDesabilita() {
		return desabilita;
	}

	public void setDesabilita(boolean desabilita) {
		this.desabilita = desabilita;
	}

	public Perfil getSelectPerfil() {
		return selectPerfil;
	}

	public void setSelectPerfil(Perfil selectPerfil) {
		this.selectPerfil = selectPerfil;
	}

	public DualListModel<Funcionalidade> getFuncionalidadesPickList() {
		return funcionalidadesPickList;
	}

	public void setFuncionalidadesPickList(
			DualListModel<Funcionalidade> funcionalidadesPickList) {
		this.funcionalidadesPickList = funcionalidadesPickList;
	}

	public List<Funcionalidade> getSource() {
		return source;
	}

	public void setSource(List<Funcionalidade> source) {
		this.source = source;
	}

	public List<Funcionalidade> getTarget() {
		return target;
	}

	public void setTarget(List<Funcionalidade> target) {
		this.target = target;
	}

	public ConverterUtil<Funcionalidade> getConverterFuncionalidade() {
		return converterFuncionalidade;
	}

	public void setConverterFuncionalidade(
			ConverterUtil<Funcionalidade> converterFuncionalidade) {
		this.converterFuncionalidade = converterFuncionalidade;
	}

}
