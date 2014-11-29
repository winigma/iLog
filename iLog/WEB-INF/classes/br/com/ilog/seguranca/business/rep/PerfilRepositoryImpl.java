package br.com.ilog.seguranca.business.rep;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

import br.cits.commons.citsSecurity.business.entity.BasicPerfilFiltro;
import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.cits.commons.citsbusiness.util.HQLQuery;
import br.cits.commons.citsbusiness.util.PersistenceHelper;
import br.com.ilog.geral.business.CodigoErroEspecifico;
import br.com.ilog.seguranca.business.entity.Perfil;

/**
 * @author Heber Santiago
 * @date 21/06/2011
 *
 */
@Component
public class PerfilRepositoryImpl extends CrudRepositoryJPA<Perfil> implements PerfilRepository {

	@PersistenceContext
	private EntityManager entityManager;
	
	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Override
	public Perfil alterar(Perfil perfil) throws BusinessException {
		if(perfil.getPerfilFuncionalidades() == null || perfil.getPerfilFuncionalidades().isEmpty()){
			throw new BusinessException(CodigoErroEspecifico.SIZE_FUNCIONALIDADE_PERFIL);
		}
		
		PersistenceHelper.getReferences(perfil.getClass(), perfil, getEntityManager());
		
		getEntityManager().merge(perfil);
		
		getEntityManager().flush();
		
		return perfil;
	}

	@Override
	public Perfil cadastrar(Perfil perfil) throws BusinessException {
		if(perfil.getPerfilFuncionalidades() == null || perfil.getPerfilFuncionalidades().isEmpty()){
			throw new BusinessException(CodigoErroEspecifico.SIZE_FUNCIONALIDADE_PERFIL);
		}
		
		super.cadastrar( perfil );
		
		return perfil;
	}

	@Override
	public void excluirPerfil(Perfil perfil) throws BusinessException {
	
		getEntityManager().remove(perfil);
		
		getEntityManager().flush();
	}

	@Override
	public Perfil getPerfilById(Long id) throws BusinessException {
		Perfil perfil = super.getById(id);
		Hibernate.initialize(perfil.getPerfilFuncionalidades());		
		return perfil;
	}

	@Override
	public List<Perfil> listarPerfil(BasicPerfilFiltro filtro)
			throws BusinessException {
		
		HQLQuery<Perfil> hql=new HQLQuery<Perfil>(getEntityManager());
		hql.append("from Perfil p");
		
		if ( filtro != null ) {
			hql.appendLike("nome", filtro.getNome());
		}
		hql.append("order by p.nome");
		
		return hql.getResultList();
	}
	
	@Override
	public List<Perfil> listar() {
		
		HQLQuery<Perfil> hql = new HQLQuery<Perfil>(getEntityManager());
		hql.append("from Perfil p");
		hql.append("order by p.nome");
		
		return hql.getResultList();
	}

}
