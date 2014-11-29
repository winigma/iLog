package br.com.ilog.importacao.business.rep;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.cits.commons.citsbusiness.util.HQLQuery;
import br.com.ilog.importacao.business.entity.CustoDI;

/**
 * Classe que implementa CustoDIRepository
 * @author Heber Santiago
 *
 */
@Component
public class CustoDIRepositoryImpl extends CrudRepositoryJPA<CustoDI> implements CustoDIRepository {

	@PersistenceContext
	EntityManager entityManager;
	
	public List<CustoDI> listarCustos( Long idCarga, String tipoFatura ) throws BusinessException {
		
		HQLQuery<CustoDI> hql = new HQLQuery<CustoDI>(getEntityManager());
		hql.append("from CustoDI cdi ");
		hql.append("inner join fetch cdi.custoImportacao ci ");
		hql.append("inner join fetch cdi.carga carga ");
		
		//apenas custos inseridos.
		hql.appendCondicao( "cdi.valorCusto > 0" );
		
		hql.appendEqual("carga.id", idCarga);
		
		if ( tipoFatura != null && !tipoFatura.equals("") ) {
			hql.appendCondicao(" lower(ci.tipoFatura) like lower(:tipoFatura) ");
			hql.setParameter("tipoFatura", tipoFatura );
		}
		
		return hql.getResultList();
		
	}
	
	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}
