package br.com.ilog.seguranca.business.rep;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.cits.commons.citsbusiness.util.HQLQuery;
import br.com.ilog.seguranca.business.entity.Funcionalidade;
import br.com.ilog.seguranca.business.entity.Role;

@Component
public class FuncionalidadeRepositoryImpl extends CrudRepositoryJPA<Funcionalidade> implements FuncionalidadeRepository, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2148484363767252935L;
	@PersistenceContext
	EntityManager entityManager;
	
	@Override
	public void atualizarFuncionalidades() throws BusinessException {

		//Listar todas as roles.
		List<Funcionalidade> funcionalidades = listarFuncionalidades();
		
		for (Role role : Role.getValores()) {
			alterar(role, funcionalidades );
		}
	}
	
	@Override
	public List<Funcionalidade> listarFuncionalidades() throws BusinessException {

		HQLQuery<Funcionalidade> hql=new HQLQuery<Funcionalidade>(getEntityManager());
		
		hql.append("from Funcionalidade f");
		hql.appendIn( "f.descricao", Role.getValoresLista() );
		
		hql.append("order by f.descricao");
		
		return hql.getResultList();
	}

	
	/**
	 * @brief cadastrar roles caso nao esteja cadastrada.
	 * @author Heber Santiago
	 * @data 31/05/2011
	 * 
	 * @param role
	 * @param funcionalidades
	 * @throws BusinessException
	 */
	private void alterar( Role role, List<Funcionalidade> funcionalidades ) throws BusinessException {
		
		Funcionalidade func = new Funcionalidade();
		func.setDescricao( role );
		
		boolean notReg = true;
		for( Funcionalidade f : funcionalidades ) {
			if ( f.getDescricao().equals( role ) ) {
				notReg = false;
			}
		}
		if ( notReg ) {
			try {
				cadastrar(func);
				flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	class ComparatorRole implements Comparator<Funcionalidade> {

		@Override
		public int compare(Funcionalidade arg0, Funcionalidade arg1) {
			return arg0.getDescricao().compareTo( arg1.getDescricao() );
		}
	}

	@Override
	public Funcionalidade cadastrar(Funcionalidade entity)
			throws BusinessException {
		
		entityManager.persist( entity );
		
		return entity;
	}
	
	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public void setEntityManager(EntityManager arg0) {
		this.entityManager = arg0;
	}


}
