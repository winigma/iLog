package br.com.ilog.seguranca.business.rep;

import java.util.List;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.seguranca.business.entity.BasicFiltroUsuario;
import br.com.ilog.seguranca.business.entity.StatusUsuario;
import br.com.ilog.seguranca.business.entity.Usuario;

/**
 * @author Heber Santiago
 *
 */
public interface UsuarioRepository extends CrudRepository<Usuario> {

	public Usuario getUsuarioById(Long id) throws BusinessException;
	public Usuario getUsuarioByLogin(String login , String senha) throws BusinessException;
	public Usuario getUsuarioByLogin(String login) throws BusinessException;
	public List<Usuario> listarUsuarios(BasicFiltroUsuario filtro) throws BusinessException ;
	
	public List<Usuario> listarCompradores(StatusUsuario tipo)throws BusinessException;
	
	public List<Usuario> listarImportadores(StatusUsuario tipo)throws BusinessException;

}
