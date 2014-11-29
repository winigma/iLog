package br.com.ilog.exportacao.business.rep;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.cits.commons.citsbusiness.util.HQLQuery;
import br.com.ilog.exportacao.business.entity.CargaExp;
import br.com.ilog.exportacao.business.entityFilter.BasicFiltroCargaExp;

/**
 * 
 * @author Wisley P. Souza
 * 
 */
@Component("cargaExport")
public class CargaRepositoryImpl extends CrudRepositoryJPA<CargaExp> implements
		CargaRepository {

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
	public CargaExp getById(Serializable primaryKey) {

		return super.getById(primaryKey);
	}

	@Override
	public List<CargaExp> listarCargaByFilter(BasicFiltroCargaExp filtro)
			throws BusinessException {
		HQLQuery<CargaExp> hql = new HQLQuery<CargaExp>(entityManager);
		hql.append("from CargaExp carga ");

		hql.append("left join fetch carga.cliente cliente");
		hql.append("left join fetch carga.moeda moeda");
		hql.append("left join fetch carga.modal modal");
		// hql.append("left join fetch");
		if (filtro != null) {
			if (filtro.getCliente() != null) {
				hql.appendEqual("cliente", filtro.getCliente());
			}
			if (filtro.getModal() != null) {
				hql.appendEqual("modal", filtro.getModal());
			}

			if (filtro.getModal() != null) {
				hql.appendEqual("moeda", filtro.getMoeda());
			}

			if (filtro.getDanfe() != null && !filtro.getDanfe().isEmpty()) {
				hql.appendCondicao("lower(carga.danfe) like  lower(:danfe)");
				hql.setParameter("danfe", filtro.getDanfe() + "%");
			}

		}

		return hql.getResultList();
	}

}
