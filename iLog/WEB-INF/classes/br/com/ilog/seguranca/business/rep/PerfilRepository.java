package br.com.ilog.seguranca.business.rep;

import java.util.List;

import br.cits.commons.citsSecurity.business.entity.BasicPerfilFiltro;
import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.seguranca.business.entity.Perfil;

/**
 * @author Heber Santiago
 * @date 21/06/2011
 *
 */
public interface PerfilRepository extends CrudRepository<Perfil> {

	public void excluirPerfil(Perfil perfil) throws BusinessException;

	public Perfil getPerfilById(Long id) throws BusinessException;

	public List<Perfil> listarPerfil( BasicPerfilFiltro filtro ) throws BusinessException;
}
