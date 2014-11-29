package br.com.ilog.cadastro.business.rep;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.cits.commons.citsbusiness.util.HQLQuery;
import br.com.ilog.cadastro.business.entity.Departamento;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroDepartamento;

@Component
public class DepartamentoRepositoryImpl extends CrudRepositoryJPA<Departamento>
		implements DepartamentoRepository {

	@PersistenceContext(unitName = "ilog")
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
	public List<Departamento> listDepartamentos(BasicFiltroDepartamento filtro)
			throws BusinessException {
		HQLQuery<Departamento> hql = new HQLQuery<Departamento>(entityManager);
		hql.append("from Departamento dep");
		if (filtro != null) {
			if(filtro.getDescricao() != null && !filtro.getDescricao().isEmpty()){
				hql.appendCondicao(" lower(dep.descricao) like lower(:descricao) ");
				hql.setParameter("descricao", filtro.getDescricao() + "%");

			}
			if(filtro.getAtivo()!= null){
				hql.appendEqual("dep.ativo", filtro.getAtivo());
			}
		}
		hql.append("order by dep.descricao");

		return hql.getResultList();
	}

}
