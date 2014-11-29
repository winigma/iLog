package br.com.ilog.cadastro.business.rep;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.cits.commons.citsbusiness.util.HQLQuery;
import br.com.ilog.cadastro.business.entity.MateriaPrima;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroMateriaPrima;

@Component
public class MateriaPrimaRepositoryImpl extends CrudRepositoryJPA<MateriaPrima> implements MateriaPrimaRepository {

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
	public List<MateriaPrima> listar(BasicFiltroMateriaPrima filtro)
			throws BusinessException {
		HQLQuery<MateriaPrima> hql = new HQLQuery<MateriaPrima>(getEntityManager());
		hql.append( "from MateriaPrima mp" );
				
		if ( filtro != null ) {
			
			if(filtro.getDescricao() != null && !filtro.getDescricao().isEmpty()){
				hql.appendCondicao(" lower(mp.descricao) like lower(:descricao) ");
				hql.setParameter("descricao", filtro.getDescricao() + "%");

			}
			if(filtro.getCodigo() != null && !filtro.getCodigo().isEmpty()){
				hql.appendCondicao(" lower(mp.codigo) like lower(:cod) ");
				hql.setParameter("cod", filtro.getCodigo() + "%");

			}
			
			hql.appendEqual( "mp.ativo", filtro.getAtivo() );
			
		}
		hql.append( "order by mp.codigo" );
		
		return hql.getResultList();
	}

}
