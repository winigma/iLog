package br.com.ilog.cadastro.business.rep;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.com.ilog.cadastro.business.entity.Endereco;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroEndereco;

@Component
public class EnderecoRepositoryImpl extends CrudRepositoryJPA<Endereco>
		implements EnderecoRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public EntityManager getEntityManager() {

		return this.entityManager;
	}

	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;

	}

	
	@Override
	public Endereco alterar(Endereco entity) throws BusinessException {
		return super.alterar(entity);
	}
	
	@Override
	public Endereco getById(Serializable primaryKey) {
		//HQLQuery<Endereco> hql = new HQLQuery<Endereco>(entityManager);
        //hql.append("from Endereco e");
        //hql.appendEqual("e.id", primaryKey );
        
        Endereco endereco =  super.getById(primaryKey);
        Hibernate.initialize(endereco.getPais());
        Hibernate.initialize(endereco.getCidade());

		return endereco;
	}

	@Override
	public Endereco cadastrar(Endereco endereco) throws BusinessException {
		if (endereco.getId() == null) {
			getEntityManager().persist(endereco);
		}
		getEntityManager().flush();
		return endereco;
	}

	@Override
	public List<Endereco> listarFiltraEnderecos(BasicFiltroEndereco filtro)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
