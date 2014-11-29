package br.com.ilog.cadastro.business.rep;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.cits.commons.citsbusiness.util.HQLQuery;
import br.com.ilog.cadastro.business.entity.Modal;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroModal;

/**
 * @author Heber Santiago
 *
 */
@Component
public class ModalRepositoryImpl extends CrudRepositoryJPA<Modal> implements ModalRepository {

	@PersistenceContext( name = "ilog" )
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
	public List<Modal> listar(BasicFiltroModal filtroModal)
			throws BusinessException {
		HQLQuery<Modal> hql = new HQLQuery<Modal>(entityManager);
		hql.append("from Modal md");
		
		if ( filtroModal != null ) {
			if(filtroModal.getDescricao() != null && !filtroModal.getDescricao().isEmpty()){
				hql.appendCondicao(" lower(md.descricao) like lower(:descricao) ");
				hql.setParameter("descricao", filtroModal.getDescricao() + "%");
			}
			
			hql.appendEqual( "md.ativo", filtroModal.getAtivo() );
			
		}		
		hql.append("order by md.descricao");
		
		return hql.getResultList();
	}

}
