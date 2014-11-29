package br.com.ilog.seguranca.business.facade;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.cits.commons.citsSecurity.business.entity.BasicPerfilFiltro;
import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.exception.CodigoErro;
import br.com.ilog.geral.business.CodigoErroEspecifico;
import br.com.ilog.seguranca.business.entity.BasicFiltroUsuario;
import br.com.ilog.seguranca.business.entity.Funcionalidade;
import br.com.ilog.seguranca.business.entity.Perfil;
import br.com.ilog.seguranca.business.entity.StatusUsuario;
import br.com.ilog.seguranca.business.entity.Usuario;
import br.com.ilog.seguranca.business.rep.FuncionalidadeRepository;
import br.com.ilog.seguranca.business.rep.PerfilRepository;
import br.com.ilog.seguranca.business.rep.UsuarioRepository;
import br.com.ilog.seguranca.utilities.SenhaUtilities;

/**
 * @author Heber Santiago
 *
 */
@Component("controleUsuario")
@Transactional(propagation=Propagation.REQUIRED,rollbackFor=BusinessException.class)
public class SegurancaFacadeImpl implements SegurancaFacade{

	/**
	 * 
	 */

	@Resource
	UsuarioRepository usuarioRep;
	
	@Resource
	FuncionalidadeRepository funcionalidadeRep;
	
	@Resource
	PerfilRepository perfilRepository;
	
	@Override
	public CodigoErro contarTentativaAcesso(String login, String senhaDigitada)
			throws BusinessException {
				
		Usuario usuario = usuarioRep.getUsuarioByLogin(login);
		if (usuario == null) {
			return CodigoErroEspecifico.LOGIN_NAO_EXISTE;		
		}
		
		if (usuario.getStatus().equals(StatusUsuario.I) || usuario.isBloqueado()) {
			return CodigoErroEspecifico.LOGIN_BLOQUEADO;
		}
		
		boolean senhaValida = usuario.getPassword().equals(senhaDigitada);
		
		usuario = getUsuarioById(usuario.getId());
		CodigoErro erro;
		
		if (senhaValida) {
			erro = null;
		}  else {
			erro = CodigoErroEspecifico.LOGIN_SENHA_INVALIDA;								
		}
		
		return erro;
		
	}

	@Override
	public Usuario getUsuarioById(Long id) throws BusinessException {
		Usuario u = usuarioRep.getUsuarioById(id); 
		return (Usuario) u;
	}

	@Override
	public Usuario getUsuarioByLogin(String login, String senha)
			throws BusinessException {
		return usuarioRep.getUsuarioByLogin(login, senha);
	}

	@Override
	public Usuario getUsuarioByLogin(String login) throws BusinessException {
		return usuarioRep.getUsuarioByLogin(login);
	}

	@Override
	public void atualizarFuncionalidades() throws BusinessException {
		funcionalidadeRep.atualizarFuncionalidades();
	}

	@Override
	public List<Usuario> listarUsuarios(BasicFiltroUsuario filtro) throws BusinessException {
		return usuarioRep.listarUsuarios( filtro );
	}

	@Override
	public List<Perfil> listarPerfil() throws BusinessException {
		return perfilRepository.listar();
	}

	@Override
	public List<Funcionalidade> listarFuncionalidades() throws BusinessException {
		return funcionalidadeRep.listarFuncionalidades();
	}

	@Override
	public Perfil getPerfilById(Long id) throws BusinessException {
		return perfilRepository.getPerfilById(id);
	}

	@Override
	public void excluirPerfil(Long id) throws BusinessException {
		Perfil perfil  = perfilRepository.getById(id);		
		perfilRepository.excluirPerfil(perfil);
	}

	@Override
	public void alterarPerfil(Perfil perfil) throws BusinessException {
		perfilRepository.alterar(perfil);
		
	}

	@Override
	public void cadastrarPerfil(Perfil perfil) throws BusinessException {
		perfilRepository.cadastrar(perfil);
	}

	@Override
	public void alterarUsuario(Usuario usuario) throws BusinessException {
		usuarioRep.alterar(usuario);
	}

	@Override
	public void cadastrarUsuario(Usuario usuario) throws BusinessException {
		usuarioRep.cadastrar(usuario);
	}

	@Override
	public List<Perfil> listarPerfil(BasicPerfilFiltro filtro)
			throws BusinessException {
		return perfilRepository.listarPerfil(filtro);
	}

	@Override
	public Usuario alterarSenha(Usuario usuario) throws BusinessException {
	
		try {
			
			String senhaCriptografada = SenhaUtilities.criptografaSenha( usuario.getPassword(), null );			
			usuario.setPassword(senhaCriptografada);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		usuario = usuarioRep.alterar(usuario);
	
		return usuario;
	}

	@Override
	public void excluirUsuario(Long id) throws BusinessException {
		usuarioRep.excluirById( id );
	}

	@Override
	public List<Usuario> listarCompradores(StatusUsuario tipo)
			throws BusinessException {

		return usuarioRep.listarCompradores(tipo);
	}

	@Override
	public List<Usuario> listarImportadores(StatusUsuario tipo)
			throws BusinessException {
		return usuarioRep.listarImportadores(tipo);
	}

}
