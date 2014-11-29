package br.com.ilog.cadastro.business.rep;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.cits.commons.citsbusiness.util.HQLQuery;
import br.com.ilog.cadastro.business.entity.Nivel;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroNivel;

@Component
public class NivelRepositoryImpl extends CrudRepositoryJPA<Nivel> implements NivelRepository{

	@PersistenceContext
	EntityManager entityManager;
	
	
	
	

	@Override
	public EntityManager getEntityManager() {
		return this.entityManager;
	}

	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<Nivel> listarNivelByFiltro(BasicFiltroNivel filtro)
			throws BusinessException {
		HQLQuery<Nivel> hql =  new HQLQuery<Nivel>(entityManager);
		hql.append("from Nivel n");
		if(filtro != null){
			if(filtro.getCodigo() != null && !filtro.getCodigo().equals("")){
				hql.appendCondicao(" lower(n.codigo) like :code ");
				hql.setParameter("code", filtro.getCodigo().toLowerCase() + "%");
			}
			if(filtro.getDescricao() != null &&  !filtro.getDescricao().equals(" ")){
				hql.appendCondicao(" lower(n.descricao) like :desc ");
				hql.setParameter("desc", filtro.getDescricao().toLowerCase() + "%");
			}
			hql.appendEqual( "n.ativo", filtro.getAtivo() );
		}
		
		return hql.getResultList();
	}

}
