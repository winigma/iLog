package br.com.ilog.importacao.business.rep;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.cits.commons.citsbusiness.util.HQLQuery;
import br.com.ilog.importacao.business.entity.CustoImportacao;

/**
 * @author cits
 *
 */
@Component
public class CustoImportacaoRepositoryImpl extends
		CrudRepositoryJPA<CustoImportacao> implements CustoImportacaoRepository {

	@PersistenceContext
	EntityManager entityManager;
	
	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public void setEntityManager(EntityManager arg0) {
		entityManager = arg0;
	}

	@Override
	public List<CustoImportacao> listar() {
		
		HQLQuery<CustoImportacao> hql = new HQLQuery<CustoImportacao>( getEntityManager() );
		hql.append( "from CustoImportacao custo" );
		
		hql.append( "order by custo.despesa asc, custo.descricao" );
		
		return hql.getResultList();
	}
}
