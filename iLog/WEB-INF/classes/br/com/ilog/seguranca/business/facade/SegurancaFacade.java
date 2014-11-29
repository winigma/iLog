package br.com.ilog.seguranca.business.facade;

import java.util.List;

import br.cits.commons.citsSecurity.business.entity.BasicPerfilFiltro;
import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.exception.CodigoErro;
import br.com.ilog.seguranca.business.entity.BasicFiltroUsuario;
import br.com.ilog.seguranca.business.entity.Funcionalidade;
import br.com.ilog.seguranca.business.entity.Perfil;
import br.com.ilog.seguranca.business.entity.StatusUsuario;
import br.com.ilog.seguranca.business.entity.Usuario;

/**
 * @author Heber Santiago
 *
 */
public interface SegurancaFacade {

	public Usuario getUsuarioByLogin(String login , String senha) throws BusinessException;
	
	public Usuario getUsuarioByLogin(String login) throws BusinessException;
	
	public Usuario getUsuarioById(Long id) throws BusinessException;
	
	public CodigoErro contarTentativaAcesso(String login, String senhaDigitada) throws BusinessException;
	
	public void atualizarFuncionalidades() throws BusinessException;

	public List<Usuario> listarUsuarios(BasicFiltroUsuario filtro) throws BusinessException;

	public List<Perfil> listarPerfil() throws BusinessException;

	public List<Funcionalidade> listarFuncionalidades() throws BusinessException;

	public Perfil getPerfilById(Long idRegistro) throws BusinessException;

	public void excluirPerfil(Long id) throws BusinessException;

	public void alterarPerfil(Perfil perfil) throws BusinessException;
	
	public void cadastrarPerfil(Perfil perfil) throws BusinessException;

	public void alterarUsuario(Usuario usuario) throws BusinessException;
	
	public void cadastrarUsuario(Usuario usuario) throws BusinessException;

	public void excluirUsuario(Long id) throws BusinessException;
	
	public List<Perfil> listarPerfil(BasicPerfilFiltro filtro) throws BusinessException;

	public Usuario alterarSenha(Usuario usuario) throws BusinessException;
	
	
	//compradores
	public List<Usuario> listarCompradores(StatusUsuario tipo)throws BusinessException;
	
	public List<Usuario> listarImportadores(StatusUsuario tipo)
			throws BusinessException;
}


