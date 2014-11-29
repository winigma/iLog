package br.com.ilog.cadastro.presentation.mbean;

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
import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.hibernate.validator.ValidatorHelper;
import br.cits.commons.citspresentation.mbeans.AbstractManter;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.commons.citspresentation.util.JSFRequestBean;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.Processo;
import br.com.ilog.cadastro.business.entity.TipoOcorrencia;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.cadastro.presentation.util.ConverterUtil;
import br.com.ilog.cadastro.presentation.util.ExceptionFiltro;
import br.com.ilog.geral.presentation.CommonsList;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;

@AccessScoped
@Component( "mBeanManterTipoOcorrencia" )
public class MBeanManterTipoOcorrencia extends AbstractManter {

	/** */
	private static final long serialVersionUID = -4115730754083410314L;
	Logger logger = LoggerFactory.getLogger( MBeanManterTipoOcorrencia.class );
	
	@Resource( name = "controllerCadastro" )
	CadastroFacade facade;
	
	@Resource
	CommonsList commonsList;
	
	private boolean edicao;
	
	private TipoOcorrencia entity;
	
	private List<SelectItem> comboAtivo;
	
	private List<Processo> processosSource;
	
	private DualListModel<Processo> sourceProcessos;
	private ConverterUtil<Processo> converterProcesso;
	private List<Processo> source;
	private List<Processo> target;
	
	/**
	 * componente de picklist recebe a lista de processos.
	 */
	private DualListModel<Processo> processos;
	
	@PostConstruct
	public void inicializarObjetos() {
		edicao = false;
		comboAtivo = commonsList.listaBooleanAtivoInativo();
		
		entity = new TipoOcorrencia();
		entity.setProcessos( new ArrayList<Processo>() );
		
		popularProcessos();
		
	}
	
	public void popularProcessos() {
		
		source = new ArrayList<Processo>();
		target = new ArrayList<Processo>();
		try {
			List<Processo> procs = facade.listarProcessos();
			
			if (procs != null) {
				source = procs;
				sourceProcessos = new DualListModel<Processo>(source, target);

				converterProcesso = new ConverterUtil<Processo>(
						procs);
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return
	 */
	public String novo() {
		inicializarObjetos();
		return "tipoOcorrencia.jsf";
	}
	
	@Override
	public String editar() {
		Long idRegistro = Long.valueOf(JSFRequestBean.getParameter("id"));

		try {
			
			source = new ArrayList<Processo>();
			target = new ArrayList<Processo>();

			entity = facade.getTipoOcorrenciaById(idRegistro);

			source = facade.listarProcessos();
					
			converterProcesso = new ConverterUtil<Processo>(source);
			if (entity.getProcessos() != null)
				target = entity.getProcessos();

			source.removeAll(target);
			sourceProcessos = new DualListModel<Processo>(source, target);
			
			
		} catch (BusinessException e) {
			e.printStackTrace();
			logger.error("erro: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.TIPO_OCORRENCIA, e));
			return null;
		}
		edicao = true;
		return "tipoOcorrencia.jsf";
	}

	@Override
	public String salvar() {
			
			entity.setProcessos( sourceProcessos.getTarget() );
			
			FacesContext fc = FacesContext.getCurrentInstance();
			ResourceBundle resourceBundle = TemplateMessageHelper
					.getResourceBundle(MensagensSistema.TIPO_OCORRENCIA, fc.getViewRoot()
							.getLocale());
			List<String> errorMessages = ValidatorHelper.valida(entity,
					resourceBundle);
			
			if (errorMessages.isEmpty()) {
				try {
			
					if (edicao) {
						facade.alterarTipoOcorrencia(entity);
					} else {
						facade.cadastrarTipoOcorrencia(entity);
					}
					
				} catch (Exception e) {
	
					logger.error("error: {}", e);
					if (!edicao) {
						entity.setId(null);
						entity.setProcessos(new ArrayList<Processo>());
					}

					//e.printStackTrace();
					ConstraintViolationException ex = (ConstraintViolationException) e.getCause();
					Throwable lastCause = ExceptionFiltro.getLastException(e);
					if (lastCause.getMessage().contains("uk_tipo_ocorrencia")) {
						Messages.adicionaMensagemDeErro(TemplateMessageHelper
								.getMessage(MensagensSistema.TIPO_OCORRENCIA,
										"msgTipoOcorrenciaCadastrado", fc.getViewRoot()
												.getLocale()));
					return null;

					} 
					
					ex.printStackTrace();
					if (e.getMessage().contains("ConstraintViolationException")) {
						if (ex.getSQLException().getNextException().getMessage().contains("uk_tipo_ocorrencia")) {
							Messages.adicionaMensagemDeErro(TemplateMessageHelper
									.getMessage(MensagensSistema.TIPO_OCORRENCIA,
											"msgTipoOcorrenciaCadastrado", fc.getViewRoot()
													.getLocale()));
						return null;

						} else {
							Messages.adicionaMensagemDeErro(TemplateMessageHelper
									.getMessage(MensagensSistema.SISTEMA,
											ExceptionFiltro.recursiveException(e), fc
													.getViewRoot().getLocale()));
						}
					}
				}
			} else {
				Messages.adicionaMensagensDeErro(errorMessages);
				return null;
			}
	
			String message = TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG001", fc.getViewRoot()
							.getLocale());
			Messages.adicionaMensagemDeInfo(message);
			refazerPesquisa();
			return "tiposOcorrencias.jsf";
			
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("error:{}", e);
//			
//			return null;
//		}
	}
	
	public String cancelar() {
		return "tiposOcorrencias.jsf";
	}
	
	@Override
	public String excluir() {
		try {
			
			facade.excluirTipoOcorrencia(entity);
			
			FacesContext fc = FacesContext.getCurrentInstance();
			Messages.adicionaMensagemDeInfo(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG_EXCLUIR_SUCESSO", fc
							.getViewRoot().getLocale()));

			this.refazerPesquisa();
		} catch (Exception e) {

			logger.error("error: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper
					.getMessage(ExceptionFiltro.recursiveException(e)));

			return null;
		}
		return "tiposOcorrencias.jsf";
	}

	@Override
	protected void refazerPesquisa() {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the edicao
	 */
	public boolean isEdicao() {
		return edicao;
	}

	/**
	 * @param edicao the edicao to set
	 */
	public void setEdicao(boolean edicao) {
		this.edicao = edicao;
	}

	/**
	 * @return the entity
	 */
	public TipoOcorrencia getEntity() {
		return entity;
	}

	/**
	 * @param entity the entity to set
	 */
	public void setEntity(TipoOcorrencia entity) {
		this.entity = entity;
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

	/**
	 * @return the processos
	 */
	public DualListModel<Processo> getProcessos() {
		return processos;
	}

	/**
	 * @param processos the processos to set
	 */
	public void setProcessos(DualListModel<Processo> processos) {
		this.processos = processos;
	}

	/**
	 * @return the processosSource
	 */
	public List<Processo> getProcessosSource() {
		return processosSource;
	}

	/**
	 * @param processosSource the processosSource to set
	 */
	public void setProcessosSource(List<Processo> processosSource) {
		this.processosSource = processosSource;
	}

	/**
	 * @return the sourceProcessos
	 */
	public DualListModel<Processo> getSourceProcessos() {
		return sourceProcessos;
	}

	/**
	 * @param sourceProcessos the sourceProcessos to set
	 */
	public void setSourceProcessos(DualListModel<Processo> sourceProcessos) {
		this.sourceProcessos = sourceProcessos;
	}

	/**
	 * @return the converterProcesso
	 */
	public ConverterUtil<Processo> getConverterProcesso() {
		return converterProcesso;
	}

	/**
	 * @param converterProcesso the converterProcesso to set
	 */
	public void setConverterProcesso(ConverterUtil<Processo> converterProcesso) {
		this.converterProcesso = converterProcesso;
	}

}
