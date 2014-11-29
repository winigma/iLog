package br.com.ilog.security;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.exception.CodigoErro;
import br.cits.commons.citspresentation.messages.Messages;
import br.com.ilog.geral.presentation.TemplateMessageHelper;
import br.com.ilog.seguranca.business.entity.Usuario;
import br.com.ilog.seguranca.business.facade.SegurancaFacade;
import br.com.ilog.seguranca.presentation.mbean.MBeanSessaoUsuario;
import br.com.ilog.seguranca.utilities.SenhaUtilities;

/**
 * 
 * @author Wisley
 *
 */
@Component("mBeanLogin")
@Scope("request")
public class MBeanLogin implements Serializable {

	private static final long serialVersionUID = 8764972105092169157L;

	@Resource(name = "mBeanSessaoUsuario")
	MBeanSessaoUsuario sessaoUsuario;

	@Resource(name = "controleUsuario")
	private SegurancaFacade facade;

	private String login;
	private String senha;

	@PostConstruct
	public void inicializar() {

		try {
			FacesContext facesContext = FacesContext.getCurrentInstance(); 
			SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, (ServletContext)facesContext.getExternalContext().getContext() );
			facade.atualizarFuncionalidades();
		} catch (BusinessException e) {
			e.printStackTrace();
			return ;
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}

	}

	public String doLogin() throws IOException, BusinessException {

		Usuario usuario = facade.getUsuarioByLogin(login);
		if (usuario == null) {
			String message = TemplateMessageHelper.getMessage("senhaInvalida");
			Messages.adicionaMensagemDeInfo(message);
			return "";
		}

		String senhaCriptografada = "";
		try {

			senhaCriptografada = SenhaUtilities.criptografaSenha(senha, null);

		} catch (Exception e1) {
			e1.printStackTrace();
		}

		CodigoErro codigoErro = facade.contarTentativaAcesso(login,
				senhaCriptografada);
		if (codigoErro != null) {
			String message = TemplateMessageHelper.getMessage(codigoErro
					.getIdBundle());
			Messages.adicionaMensagemDeInfo(message);
			return "";
		}

		HttpServletResponse response = (HttpServletResponse) FacesContext
				.getCurrentInstance().getExternalContext().getResponse();

		String req = "j_security_check?j_username=" + login + "&j_password="
				+ senha;
		try {
			sessaoUsuario.setUsuario(usuario);
			response.sendRedirect(response.encodeRedirectURL(req));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "pages/index.jsf";
		// return "";

	}

	public String doValidarLogin() {
		return null;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	/**
	 * @param facade
	 *            the facade to set
	 */
	public void setFacade(SegurancaFacade facade) {
		this.facade = facade;
	}
}
