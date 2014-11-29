package br.com.ilog.importacao.business.rep;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.cits.commons.citsbusiness.util.HQLQuery;
import br.com.ilog.cadastro.business.entity.Ocorrencia;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroOcorrencia;
import br.com.ilog.importacao.business.entity.Carga;
import br.com.ilog.importacao.business.entity.OcorrenciaImport;

@Component
public class OcorrenciaRepositoryImpl extends CrudRepositoryJPA<Ocorrencia>
		implements OcorrenciaRepository {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public Ocorrencia getById(Serializable primaryKey) {

		Ocorrencia oco = new Ocorrencia();
		try {

			HQLQuery<Ocorrencia> hql = new HQLQuery<Ocorrencia>(entityManager);
			hql.append("from Ocorrencia o");
			hql.appendEqual("o.id", primaryKey);
			hql.appendEqual("o.ativo", true);
			oco = hql.getSingleResult();
			if (oco != null) {
				Hibernate.initialize(oco.getAutor());
				Hibernate.initialize(oco.getCarga());
				Hibernate.initialize(oco.getMotivo());
				Hibernate.initialize(oco.getCidade());
			}
		} catch (NoResultException e) {
			oco = null;
		}

		return oco;
	}

	@Override
	public EntityManager getEntityManager() {
		return this.entityManager;
	}

	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;

	}

	@Override
	public List<Ocorrencia> listOcorrencias(BasicFiltroOcorrencia filtro)
			throws BusinessException {
		HQLQuery<Ocorrencia> hql = new HQLQuery<Ocorrencia>(entityManager);
		hql.append("from Ocorrencia o ");
		hql.append("inner join fetch o.cidade cidade");
		hql.append("inner join fetch o.carga carga");
		hql.append("inner join fetch o.motivo mo");
		hql.append("inner join fetch mo.tipoOcorrencia to");
		if (filtro != null) {

			if (filtro.getMotivo() != null) {
				hql.appendEqual("mo.id", filtro.getMotivo().getId());
			}
			if (filtro.getTipoOcorrencia() != null) {
				hql.appendEqual("to.id", filtro.getTipoOcorrencia().getId());
			}
			if (filtro.getAps() != null && !filtro.getAps().equals("")) {
				hql.appendLike("carga.numAPS", filtro.getAps());
			}
			if (filtro.getHawb() != null && !filtro.getHawb().equals("")) {
				hql.appendLike("carga.hawb", filtro.getHawb());
			}

			hql.appendEqual("o.isLom", filtro.getLom());
			hql.appendEqual("o.canal", filtro.getCanal());
			hql.appendEqual("o.ativo", filtro.getAtivo());
			hql.appendEqual("carga", filtro.getCarga());
			hql.appendEqual("cidade", filtro.getCidade());

			hql.appendBetween("o.dtOcorrencia", filtro.getDtInicioOcorrencia(),
					filtro.getDtFimOcorrencia());

		}
		return hql.getResultList();
	}

	@Override
	public boolean possuiOcorrencia(Carga carga) throws BusinessException {

		if (carga != null) {
			HQLQuery<OcorrenciaImport> hql = new HQLQuery<OcorrenciaImport>(
					entityManager);
			hql.append("from Ocorrencia o ");

			hql.appendEqual("o.carga", carga);
			hql.appendEqual("o.ativo", true);

			List<OcorrenciaImport> result = hql.getResultList();
			if (result != null && result.size() > 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<Carga> possuiOcorrenciaNaoLida() throws BusinessException {

		HQLQuery<Carga> hql = new HQLQuery<Carga>(entityManager);
		hql.append("select distinct (o.carga) from Ocorrencia o ");
		// hql.append("inner join fetch o.carga carga");

		hql.appendEqual("o.lido", false);
		hql.appendEqual("o.ativo", true);

		List<Carga> result = hql.getResultList();

		return result;
	}

}
