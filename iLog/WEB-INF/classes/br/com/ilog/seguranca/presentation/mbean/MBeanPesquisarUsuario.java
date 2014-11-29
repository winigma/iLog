package br.com.ilog.seguranca.presentation.mbean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citspresentation.mbeans.AbstractPaginacao;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.presentation.util.ExceptionFiltro;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;
import br.com.ilog.seguranca.business.entity.BasicFiltroUsuario;
import br.com.ilog.seguranca.business.entity.StatusUsuario;
import br.com.ilog.seguranca.business.entity.TipoUsuario;
import br.com.ilog.seguranca.business.entity.Usuario;
import br.com.ilog.seguranca.business.facade.SegurancaFacade;

/**
 * @author Wisley Souza
 * @date 19/06/2012
 * 
 * @brief Managed Bean de Pesquisar Usuario.
 * 
 */
@Controller("mBeanPesquisarUsuario")
@AccessScoped
public class MBeanPesquisarUsuario extends AbstractPaginacao {

	/**  */
	private static final long serialVersionUID = -8230564185986019391L;

	private static Logger logger = LoggerFactory
			.getLogger(MBeanPesquisarUsuario.class);

	/**
	 * itens da combo de status de usuario.
	 */
	private List<SelectItem> comboStatusUsuario;

	/**
	 * itens do tipo de usuario
	 */
	private List<SelectItem> comboTypesUsers;

	private List<Usuario> usuarios;

	/**
	 * Filtro de usuario.
	 */
	private BasicFiltroUsuario filtro;

	/**
	 * Fachada de seguranca.
	 */
	@Resource(name = "controleUsuario")
	SegurancaFacade seguranca;

	// Long que captura o id
	private Long idObjeto;

	/**
	 * @coment captura o id do item selecionados
	 * @param index
	 */

	public void capturarId(int index) {
		Usuario objeto = usuarios.get(index);
		idObjeto = objeto.getId();
	}

	public void excluir() {

		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			Usuario admin = seguranca.getUsuarioById(idObjeto);
			if (admin != null && !admin.getLogin().equals("cits\\admin")) {
				seguranca.excluirUsuario(idObjeto);

				Messages.adicionaMensagemDeInfo(TemplateMessageHelper
						.getMessage(MensagensSistema.SISTEMA,
								"MSG_EXCLUIR_SUCESSO", fc.getViewRoot()
										.getLocale()));
				this.refazerPesquisa();
			} else {
				Messages.adicionaMensagemDeInfo(TemplateMessageHelper
						.getMessage(MensagensSistema.USUARIOS,
								"msfFailDeleteAdmin", fc.getViewRoot()
										.getLocale()));

			}
		} catch (BusinessException e) {
			logger.error("error: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper
					.getMessage(ExceptionFiltro.recursiveException(e)));
			return;
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

	}

	public void excluir(Long idRegistro) {

		try {
			FacesContext fc = FacesContext.getCurrentInstance();

			Usuario admin = seguranca.getUsuarioById(idRegistro);
			if (admin != null && admin.getLogin().equals("cits\\admin"))
				throw new UnsupportedOperationException();

			seguranca.excluirUsuario(idRegistro);
			Messages.adicionaMensagemDeInfo(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG_EXCLUIR_SUCESSO", fc
							.getViewRoot().getLocale()));

		} catch (UnsupportedOperationException e) {
			FacesContext fc = FacesContext.getCurrentInstance();

			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG_GEN_007", fc.getViewRoot()
							.getLocale()));
			return;

		} catch (Exception e) {
			logger.error("error: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper
					.getMessage(ExceptionFiltro.recursiveException(e)));
			return;
		}
		this.refazerPesquisa();

	}

	/**
	 * @author Heber Santiago
	 * @data 20/06/2011
	 * @brief Metodo de pesquisar usuários.
	 * 
	 * @param ae
	 */
	public void doPesquisar(ActionEvent ae) {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();

			usuarios = seguranca.listarUsuarios(filtro);

			if (usuarios.isEmpty()) {
				String mensagem = TemplateMessageHelper.getMessage(
						MensagensSistema.SISTEMA, "MSG008", fc.getViewRoot()
								.getLocale());
				Messages.adicionaMensagemDeInfo(mensagem);
			}

			setPaginaAtual(1);
		} catch (BusinessException e1) {
			e1.printStackTrace();
			logger.error("erro: {}", e1);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.USUARIOS, e1));
		}
	}

	/**
	 * @brief inicializa os objetos da pagina.
	 */
	@PostConstruct
	public void inicializarObjetos() {

		filtro = new BasicFiltroUsuario();
		usuarios = new ArrayList<Usuario>();
		doPesquisar(null);

	}

	@Override
	public int getTotalRegistros() {
		if (usuarios != null)
			return usuarios.size();
		else
			return 0;
	}

	@Override
	public void refazerPesquisa() {
		if (filtro == null)
			filtro = new BasicFiltroUsuario();

		// Se a lista estava vazia antes não é necessário
		// fazer uma nova pesquisa
		if (usuarios.isEmpty())
			return;

		doPesquisar(null);

	}

	@Override
	public void limpar() {
		filtro = new BasicFiltroUsuario();
		usuarios.clear();
	}

	public BasicFiltroUsuario getFiltro() {
		return filtro;
	}

	public void setFiltro(BasicFiltroUsuario filtro) {
		this.filtro = filtro;
	}

	public List<SelectItem> getComboStatusUsuario() {
		comboStatusUsuario = new ArrayList<SelectItem>();
		for (StatusUsuario status : StatusUsuario.values()) {
			comboStatusUsuario.add(new SelectItem(status, TemplateMessageHelper
					.getMessage(MensagensSistema.USUARIOS, status.toString())));
		}

		return comboStatusUsuario;
	}

	public void setComboStatusUsuario(List<SelectItem> comboStatusUsuario) {
		this.comboStatusUsuario = comboStatusUsuario;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public List<SelectItem> getComboTypesUsers() {
		comboTypesUsers = new ArrayList<SelectItem>();
		for (TipoUsuario tipo : TipoUsuario.values()) {
			comboTypesUsers.add(new SelectItem(tipo, TemplateMessageHelper
					.getMessage(MensagensSistema.USUARIOS, tipo.toString())));
		}
		return comboTypesUsers;
	}

	public void setComboTypesUsers(List<SelectItem> comboTypesUsers) {
		this.comboTypesUsers = comboTypesUsers;
	}

	public Long getIdObjeto() {
		return idObjeto;
	}

	public void setIdObjeto(Long idObjeto) {
		this.idObjeto = idObjeto;
	}

}
