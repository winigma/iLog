package br.com.ilog.seguranca.business.rep;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.cits.commons.citsbusiness.util.HQLQuery;
import br.com.ilog.seguranca.business.entity.BasicFiltroUsuario;
import br.com.ilog.seguranca.business.entity.StatusUsuario;
import br.com.ilog.seguranca.business.entity.TipoUsuario;
import br.com.ilog.seguranca.business.entity.Usuario;

@Component
public class UsuarioRepositoryImpl extends CrudRepositoryJPA<Usuario> implements UsuarioRepository {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public Usuario getUsuarioById(Long id) throws BusinessException {
		Usuario usuario = super.getById(id);				
		Hibernate.initialize(usuario.getPerfis());
		Hibernate.initialize(usuario.getPessoaJuridica());
		return usuario;	
		
	}

	@Override
	public Usuario getUsuarioByLogin(String login, String senha) throws BusinessException {

		HQLQuery<Usuario> hql=new HQLQuery<Usuario>(getEntityManager());
		hql.append("from Usuario u");
		hql.append("inner join fetch u.perfils p");
		hql.appendEqual("u.login", login);
		hql.appendEqual("u.password", senha);		
		try {
			return  hql.getSingleResult();
		} catch(NoResultException ex) {
			return null;
		}		
	
	}

	@Override
	public Usuario getUsuarioByLogin(String login) throws BusinessException {
		HQLQuery<Usuario> hql=new HQLQuery<Usuario>(getEntityManager());
		hql.append("from Usuario u");
		hql.appendEqual("u.login", login);
		try {
			return  hql.getSingleResult();
		} catch(NoResultException ex) {
			return null;
		}
		
	}

	
	@Override
	public Usuario alterar(Usuario usuario) throws BusinessException {		

		try {
			
			if ( !usuario.getTipo().equals( TipoUsuario.DESPACH ) 
					&& !usuario.getTipo().equals( TipoUsuario.A_CARGA ) ) {
				usuario.setPessoaJuridica( null );
			}
			
			getEntityManager().merge(usuario);
			
			getEntityManager().flush();
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		return usuario;
	}


	@Override
	public Usuario cadastrar(Usuario usuario) throws BusinessException {		
		
		getEntityManager().persist( usuario );
		getEntityManager().flush();
		
		return usuario;
		
	}

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<Usuario> listarUsuarios(BasicFiltroUsuario filtro) throws BusinessException  {
		
		HQLQuery<Usuario> hql=new HQLQuery<Usuario>(getEntityManager());
		hql.append("from Usuario u");
		if ( filtro != null ) {
			hql.appendLike("u.nome", filtro.getNome());
			hql.appendLike("u.email", filtro.getEmail());
			hql.appendLike("u.login", filtro.getLogin());
			hql.appendEqual("u.tipo", filtro.getTipo());
			hql.appendEqual("u.status", filtro.getStatus());
			
		}
		hql.append("order by u.nome, u.login");
		
		return hql.getResultList();
	}

	@Override
	public List<Usuario> listarCompradores(StatusUsuario status)
			throws BusinessException {
		HQLQuery<Usuario> hql =  new HQLQuery<Usuario>(entityManager);
		
		hql.append("from Usuario u");
		hql.appendEqual("u.tipo", TipoUsuario.COMPRADOR);
		
		if ( status != null ) {
			hql.appendEqual("u.status", status );
		}
		
		return hql.getResultList();
	}

	@Override
	public List<Usuario> listarImportadores(StatusUsuario status)
			throws BusinessException {
		HQLQuery<Usuario> hql = new HQLQuery<Usuario>(entityManager);

		hql.append("from Usuario u");
		hql.appendEqual("u.tipo", TipoUsuario.IMPORTADOR);

		if (status != null) {
			hql.appendEqual("u.status", status);
		}

		return hql.getResultList();
	}
}
