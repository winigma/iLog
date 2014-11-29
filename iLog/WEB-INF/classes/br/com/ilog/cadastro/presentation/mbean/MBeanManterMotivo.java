package br.com.ilog.cadastro.presentation.mbean;

import java.util.ArrayList;
import java.util.Collections;
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
import br.com.ilog.cadastro.business.entity.Motivo;
import br.com.ilog.cadastro.business.entity.Processo;
import br.com.ilog.cadastro.business.entity.TipoOcorrencia;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroTipoOcorrencia;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.cadastro.presentation.util.ConverterUtil;
import br.com.ilog.cadastro.presentation.util.ExceptionFiltro;
import br.com.ilog.geral.converter.SelectManyConverter;
import br.com.ilog.geral.presentation.CommonsList;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;

@AccessScoped
@Controller( "mBeanManterMotivo" )
public class MBeanManterMotivo extends AbstractManter {

	/** */
	private static final long serialVersionUID = 7251090114172441419L;
	Logger logger = LoggerFactory.getLogger( MBeanManterMotivo.class );
	
	@Resource( name = "controllerCadastro" )
	CadastroFacade facade;
	
	@Resource
	CommonsList commonsList;
	
	private Motivo entity;
	
	private List<SelectItem> comboAtivo;
	
	private List<TipoOcorrencia> comboTipoOcorrencia;
	
	private boolean edicao;
	
	private List<Processo> processosSource;
	
	private DualListModel<Processo> sourceProcessos;
	private ConverterUtil<Processo> converterProcesso;
	private List<Processo> source;
	private List<Processo> target;
	/**
	 * componente de picklist recebe a lista de processos.
	 */
	private List<Processo> processos;
	
	private SelectManyConverter<Processo> selecionar;

	@Override
	@PostConstruct
	public void inicializarObjetos() {
		edicao = false;
		entity = new Motivo();
		entity.setProcessos( new ArrayList<Processo>() );
		entity.setAtivo( true );
		
		comboAtivo = commonsList.listaBooleanAtivoInativo();
		
		comboTipoOcorrencia = Collections.emptyList();
		try {
			comboTipoOcorrencia = facade.listarTipoOcorrencias( new BasicFiltroTipoOcorrencia( true ) );
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		
		carregarProcessos();
	}
	
	@Override
	public String salvar() {
		try {
			
			entity.setProcessos( sourceProcessos.getTarget() );
			
			FacesContext fc = FacesContext.getCurrentInstance();
			ResourceBundle resourceBundle = TemplateMessageHelper.getResourceBundle(MensagensSistema.MOTIVO, fc.getViewRoot().getLocale());
			List<String> errorMessages = ValidatorHelper.valida(entity, resourceBundle);
			
			if (errorMessages.isEmpty()) {
				try {
			
					if (edicao) {
						facade.alterarMotivo(entity);
					} else {
						facade.cadastrarMotivo(entity);
					}
					
				} catch (Exception e) {
	
					logger.error("error: {}", e);
					//e.printStackTrace();
					ConstraintViolationException ex = (ConstraintViolationException) e
							.getCause();
					ex.printStackTrace();
					Throwable lastCause = ExceptionFiltro.getLastException(e);
					if (e.getMessage().contains("ConstraintViolationException") ) {
						
						
						if (ex.getSQLException().getNextException().getMessage().contains("uk_motivo")) {
							Messages.adicionaMensagemDeErro(TemplateMessageHelper
									.getMessage(MensagensSistema.MOTIVO, "msgCheckMotivo",
											fc.getViewRoot().getLocale()));
						}
						
						return null;

					}else
					if (lastCause.getMessage().contains("CHECK constraint")) {
						if (lastCause.getMessage().contains(
								"chk_motivo_duplicado")) {
							Messages.adicionaMensagemDeErro(TemplateMessageHelper
									.getMessage(MensagensSistema.MOTIVO,
											"msgCheckMotivo", fc.getViewRoot()
													.getLocale()));
						} else {
							Messages.adicionaMensagemDeErro(TemplateMessageHelper
									.getMessage(MensagensSistema.SISTEMA,
											ExceptionFiltro.recursiveException(e), fc
													.getViewRoot().getLocale()));
						}
					}
					return null;
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
			return "motivos.jsf";
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error:{}", e);
			
			return null;
		}
	
	}
	
	@Override
	public String editar() {
		Long idRegistro = Long.valueOf(JSFRequestBean.getParameter("id"));

		try {
			
			entity = facade.getMotivoById(idRegistro);
			
			carregarProcessos();
			
			
		} catch (BusinessException e) {
			e.printStackTrace();
			logger.error("erro: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.MOTIVO, e));
			return null;
		}
		edicao = true;
		return "motivo.jsf";
	}

	@Override
	public String excluir() {
		try {
			
			facade.excluirMotivo( entity );
		
			FacesContext fc = FacesContext.getCurrentInstance();
			Messages.adicionaMensagemDeInfo(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG_EXCLUIR_SUCESSO", fc
							.getViewRoot().getLocale()));
			
		} catch (Exception e) {
			e.printStackTrace();

			logger.error("error: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper
					.getMessage(ExceptionFiltro.recursiveException(e)));
			
			return null;
			
		}
		return "motivos.jsf";
	}

	public String cancelar() {
		return "motivos.jsf";
	}
	
	public String novo() {
		
		inicializarObjetos();
		return "motivo.jsf";
	}

	/**
	 * 
	 */
	public void carregarProcessos() {
		
		source = new ArrayList<Processo>();
		target = new ArrayList<Processo>();
		sourceProcessos = new DualListModel<Processo>( source, target );
		converterProcesso = new ConverterUtil<Processo>( new ArrayList<Processo>() );
		
		try {
			if ( entity.getTipoOcorrencia() != null ) {
				TipoOcorrencia ocorr = facade.getTipoOcorrenciaById(entity.getTipoOcorrencia().getId());
				List<Processo> procs = new ArrayList<Processo>( ocorr.getProcessos() );
				
				if (procs != null) {
					converterProcesso = new ConverterUtil<Processo>( procs );
					
					source = new ArrayList<Processo>( procs );
					
					target = entity.getProcessos();
					source.removeAll( target );
					
					sourceProcessos = new DualListModel<Processo>(source, target);
					
				}
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	protected void refazerPesquisa() {
		// TODO Auto-generated method stub
	}

	/**
	 * @return the entity
	 */
	public Motivo getEntity() {
		return entity;
	}

	/**
	 * @param entity the entity to set
	 */
	public void setEntity(Motivo entity) {
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
	 * @return the comboTipoOcorrencia
	 */
	public List<TipoOcorrencia> getComboTipoOcorrencia() {
		return comboTipoOcorrencia;
	}

	/**
	 * @param comboTipoOcorrencia the comboTipoOcorrencia to set
	 */
	public void setComboTipoOcorrencia(List<TipoOcorrencia> comboTipoOcorrencia) {
		this.comboTipoOcorrencia = comboTipoOcorrencia;
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
	 * @return the processos
	 */
	public List<Processo> getProcessos() {
		return processos;
	}

	/**
	 * @param processos the processos to set
	 */
	public void setProcessos(List<Processo> processos) {
		this.processos = processos;
	}

	/**
	 * @return the selecionar
	 */
	public SelectManyConverter<Processo> getSelecionar() {
		return selecionar;
	}

	/**
	 * @param selecionar the selecionar to set
	 */
	public void setSelecionar(SelectManyConverter<Processo> selecionar) {
		this.selecionar = selecionar;
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

	/**
	 * @return the source
	 */
	public List<Processo> getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(List<Processo> source) {
		this.source = source;
	}

	/**
	 * @return the target
	 */
	public List<Processo> getTarget() {
		return target;
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(List<Processo> target) {
		this.target = target;
	}

}
