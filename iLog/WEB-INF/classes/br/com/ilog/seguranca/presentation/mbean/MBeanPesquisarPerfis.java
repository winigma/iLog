package br.com.ilog.seguranca.presentation.mbean;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import br.cits.commons.citsSecurity.business.entity.BasicPerfilFiltro;
import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.hibernate.validator.ValidatorHelper;
import br.cits.commons.citspresentation.mbeans.AbstractPaginacao;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.commons.citspresentation.util.JSFRequestBean;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.presentation.util.ExceptionFiltro;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;
import br.com.ilog.seguranca.business.entity.Perfil;
import br.com.ilog.seguranca.business.facade.SegurancaFacade;


@Controller( "mBeanPesquisarPerfis" )
@AccessScoped
public class MBeanPesquisarPerfis extends AbstractPaginacao {

	/** */
	private static final long serialVersionUID = -7818981486289487986L;

	private static Logger logger = LoggerFactory.getLogger(MBeanPesquisarPerfis.class);
	
	private List<Perfil> perfis;
	private BasicPerfilFiltro filtro;
	
	@Resource(name="controleUsuario")
	SegurancaFacade facade;
	
	
	@PostConstruct
	public void inicializar() {
		perfis = new ArrayList<Perfil>();
		
		filtro = new BasicPerfilFiltro();
		doPesquisar(null);
	}
	
	@Override
	public void doPesquisar(ActionEvent arg0) {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();

			ResourceBundle resourceBundle = TemplateMessageHelper.getResourceBundle(MensagensSistema.PERFIS, JSFRequestBean.getLocale());
			List<String> errorMessages = ValidatorHelper.valida(filtro, resourceBundle, resourceBundle);

			if(errorMessages.isEmpty()) {
				try {
					perfis = facade.listarPerfil( filtro );
					
					if( perfis.isEmpty()) {
						String idBundle = TemplateMessageHelper.getMessage(MensagensSistema.SISTEMA,"MSG008",fc.getViewRoot().getLocale());
						Messages.adicionaMensagemDeInfo(idBundle);
					}
					
					setPaginaAtual(1);
				} catch (BusinessException e1) {
					logger.error("erro: {}", e1);
					String idBundle = TemplateMessageHelper.getMessage(e1.getCodigo().getIdBundle());
					Messages.adicionaMensagemDeErro(idBundle);
				}
			}
		} catch (Exception e) {
			FacesContext fc = FacesContext.getCurrentInstance();

			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(MensagensSistema.SISTEMA,"MSG_001",fc.getViewRoot().getLocale()));
		}
		
	}

	// Long que captura o id
	private Long idObjeto;

	/**
	 * @coment captura o id do item selecionados
	 * @param index
	 */

	public void capturarId(int index) {
		Perfil objeto = perfis.get(index);
		idObjeto = objeto.getId();
	}
	
	
	
	/**
	 * @param id
	 */
	public void excluir() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();

			facade.excluirPerfil(idObjeto);
			Messages.adicionaMensagemDeInfo(TemplateMessageHelper.getMessage(MensagensSistema.SISTEMA,"MSG_EXCLUIR_SUCESSO",fc.getViewRoot().getLocale()));
			this.refazerPesquisa();
		}catch (BusinessException e) {
				e.printStackTrace();
				Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(ExceptionFiltro.recursiveException(e)));
				return;
				
		} catch (Exception e) {
			logger.error("error: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(ExceptionFiltro.recursiveException(e)));
			return;
		}
	}
	
	@Override
	public int getTotalRegistros() {
		if ( perfis != null )
			return perfis.size();
		else
			return 0;
	}

	@Override
	public void limpar() {
		
		filtro =  new BasicPerfilFiltro();
		perfis.clear();
	}

	@Override
	public void refazerPesquisa() {
		if(filtro == null)
			filtro = new BasicPerfilFiltro();
		
		// Se a lista estava vazia antes não é necessário
		// fazer uma nova pesquisa
		if(perfis.isEmpty())
			return;
		
		doPesquisar(null);

	}

	public List<Perfil> getPerfis() {
		return perfis;
	}

	public void setPerfis(List<Perfil> perfis) {
		this.perfis = perfis;
	}

	public BasicPerfilFiltro getFiltro() {
		return filtro;
	}

	public void setFiltro(BasicPerfilFiltro filtro) {
		this.filtro = filtro;
	}

	public Long getIdObjeto() {
		return idObjeto;
	}

	public void setIdObjeto(Long idObjeto) {
		this.idObjeto = idObjeto;
	}

}
