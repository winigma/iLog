package br.com.ilog.cadastro.business.rep;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.cits.commons.citsbusiness.util.HQLQuery;
import br.com.ilog.cadastro.business.entity.TipoPacote;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroTipoPacote;

@Component
public class TipoPacoteRepositoryImpl extends CrudRepositoryJPA<TipoPacote> implements TipoPacoteRepository {

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
	public List<TipoPacote> listar(BasicFiltroTipoPacote filtro) throws BusinessException {
		
		HQLQuery<TipoPacote> hql = new HQLQuery<TipoPacote>( getEntityManager() );
		hql.append( "from TipoPacote i" );
				
		if (filtro != null) {

			if (filtro.getDescricao() != null
					&& !filtro.getDescricao().isEmpty()) {
				hql.appendCondicao(" lower(i.descricao) like lower(:descricao) ");
				hql.setParameter("descricao", filtro.getDescricao() + "%");

			}
			if (filtro.getIdSap() != null && !filtro.getIdSap().isEmpty()) {
				hql.appendCondicao(" lower(i.idSap) like lower(:idSap) ");
				hql.setParameter("idSap", filtro.getIdSap() + "%");

			}
			if (filtro.getSigla() != null && !filtro.getSigla().isEmpty()) {
				hql.appendCondicao(" lower(i.sigla) like lower(:sigla) ");
				hql.setParameter("sigla", filtro.getSigla() + "%");

			}
			if (filtro.getShpUnt() != null && !filtro.getShpUnt().isEmpty()) {
				hql.appendCondicao(" lower(i.shpUnt) like lower(:shpUnt) ");
				hql.setParameter("shpUnt", filtro.getShpUnt() + "%");

			}
			

			hql.appendEqual("i.ativo", filtro.getAtivo());
		}
		hql.append( "order by i.descricao" );
		
		return hql.getResultList();
	}

	@Override
	public TipoPacote getByCodigo(String string) throws BusinessException {
		
		HQLQuery<TipoPacote> hql = new HQLQuery<TipoPacote>( getEntityManager() );
		hql.append( "from TipoPacote tp" );
		hql.appendEqual("tp.sigla",  string );
		
		return hql.getSingleResult();
	}

	@Override
	public List<TipoPacote> getByCategoria(String string) throws BusinessException {
		return null;
	}

}
