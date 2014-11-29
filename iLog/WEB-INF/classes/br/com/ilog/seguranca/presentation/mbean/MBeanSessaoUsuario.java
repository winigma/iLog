
package br.com.ilog.seguranca.presentation.mbean;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.seguranca.business.entity.Usuario;
import br.com.ilog.seguranca.business.facade.SegurancaFacade;
import br.com.vi.security.CustomPrincipal;

@Component("mBeanSessaoUsuario")
//@Scope("session")
@AccessScoped
public class MBeanSessaoUsuario {

	private int sessionTimeout;
	
	@Resource(name="controleUsuario")
	private SegurancaFacade facade;
	
	private CustomPrincipal principal;
	
	private Usuario usuario;
	
	@SuppressWarnings("unused")
	@PostConstruct
	private void inicializador() {
		
		HttpServletRequest request = getRequest();
		if (request != null && request.getUserPrincipal() != null) {
			String login = request.getUserPrincipal().getName();
			
			try {
				
				usuario = facade.getUsuarioByLogin(login);
				
			}catch (Exception e) {
				Logger.getLogger(this.getClass()).error("Erro ao pegar o usuario pelo login",e);
				usuario = null;
			}	
		}else 
			usuario = null;	
		
		/*
		 * Vai setar a variável sessionTimeout com o tempo máximo de vida de uma sessão (em ms) e subtrair 2 segundos
		 * desse valor. Deste modo o pool do Richfaces deverá fazer uma requisição 2 segundos antes de a sessão expirar
		 * fazendo com que a request enviada passe pelo filtro, invalide a sessão e redirecione para uma página com
		 * uma mensagem específica.
		 */
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		sessionTimeout = session.getMaxInactiveInterval() * 1000;
		sessionTimeout = sessionTimeout - 2000;
	}
	
	public String logout() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		session.invalidate();
		
		return "/one.jsf";
	}
	
	public int getPoolTime() {
		return sessionTimeout;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Usuario getUsuario() {
		return usuario;
	}
	
	
	public HttpServletRequest getRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	}

	public void setPrincipal(CustomPrincipal principal) {
		this.principal = principal;
	}

	
	public CustomPrincipal getPrincipal() {
		if (principal == null) {
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			
			// carrega o usuário da sessão
			principal = (CustomPrincipal)request.getUserPrincipal();
		}
		return principal;
	}
}
