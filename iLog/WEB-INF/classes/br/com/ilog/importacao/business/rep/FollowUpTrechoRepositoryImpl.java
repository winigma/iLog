package br.com.ilog.importacao.business.rep;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.cits.commons.citsbusiness.util.HQLQuery;
import br.com.ilog.cadastro.business.entity.Cidade;
import br.com.ilog.cadastro.business.entity.ParametroCanal;
import br.com.ilog.importacao.business.entity.Carga;
import br.com.ilog.importacao.business.entity.FollowUpEstimado;
import br.com.ilog.importacao.business.entity.FollowUpImport;
import br.com.ilog.importacao.business.entity.FollowUpImportTrecho;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroFollowUp;
import br.com.ilog.relatorio.business.dto.TempoMedioEntrega;

@Component
public class FollowUpTrechoRepositoryImpl extends
		CrudRepositoryJPA<FollowUpImportTrecho> implements
		FollowUpTrechoRepository {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public FollowUpImportTrecho alterar(FollowUpImportTrecho arg0)
			throws BusinessException {
		return super.alterar(arg0);
	}

	@Override
	public FollowUpImportTrecho cadastrar(FollowUpImportTrecho arg0)
			throws BusinessException {
		return super.cadastrar(arg0);
	}

	@Override
	public void excluirById(Serializable arg0) throws BusinessException {
		super.excluirById(arg0);
	}

	@Override
	public void flush() {
		getEntityManager().flush();
	}

	@Override
	public FollowUpImportTrecho getById(Serializable arg0) {

		FollowUpImportTrecho imp = super.getById(arg0);
		Hibernate.initialize(imp.getCidade());
		Hibernate.initialize(imp.getParametroCanal());
		Hibernate.initialize(imp.getParametroCanalAtual());
		Hibernate.initialize(imp.getEstimados());

		for (FollowUpEstimado est : imp.getEstimados()) {
			Hibernate.initialize(est.getCanalEstimado());
		}

		return imp;
	}

	@Override
	public List<FollowUpImportTrecho> listar() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TempoMedioEntrega> listarTempoMedioEntregapoByOrigem(
			BasicFiltroFollowUp filtro) throws BusinessException {
		HQLQuery<TempoMedioEntrega> hql = new HQLQuery<TempoMedioEntrega>(
				getEntityManager());

		hql.append("select new br.com.ilog.relatorio.business.dto.TempoMedioEntrega( po.nome, pd.nome,");

		hql.append("avg(followUp.totalDiasAtual),");
		hql.append("count(carga.id),");
		hql.append("followUpTrecho.txLocal,");
		hql.append("po.id, pd.id)");

		hql.append("from FollowUpImportTrecho followUpTrecho");
		hql.append("inner join  followUpTrecho.followUp followUp");
		hql.append("inner join followUp.carga carga");
		hql.append("inner join carga.rota rota");
		hql.append("inner join rota.paisOrigem po");
		hql.append("inner join rota.paisDestino pd");
		hql.append("inner join carga.importador responsavel");
		
		hql.appendCondicao("followUp.ativo = true");
		hql.appendCondicao("followUpTrecho.dtRealizado is not null");


		if (filtro != null) {
			if (filtro.getDtFim() != null && filtro.getDtInicio() != null) {
				SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
				hql.append(" and (followUpTrecho.dtRealizado BETWEEN '"
						+ formatador.format(filtro.getDtInicio()) + "' AND '"
						+ formatador.format(filtro.getDtFim()) + "') ");
			}
			if (filtro.getResponsavel() != null) {
				hql.append("and (responsavel.id =" + filtro.getResponsavel().getId()
						+ ") ");
			}
			hql.append("GROUP BY po.nome, pd.nome, followUpTrecho.txLocal, po.id, pd.id ");
			hql.append("having followUpTrecho.txLocal = 'FOXCONN' ");
			if (filtro.getPaisDestino() != null) {
				hql.append("and (pd.id =" + filtro.getPaisDestino().getId()
						+ ") ");
			}
			if (filtro.getPaisOrigem() != null) {
				hql.append("and (po.id =" + filtro.getPaisOrigem().getId()
						+ ") ");
			}

		} else {
			hql.append("GROUP BY po.nome, pd.nome, followUpTrecho.txLocal, po.id, pd.id ");
			hql.append("having followUpTrecho.txLocal = 'FOXCONN' ");
		}

		return (List<TempoMedioEntrega>) hql.getResultList();
	}

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public void setEntityManager(EntityManager arg0) {
		entityManager = arg0;
	}

	@Override
	public ParametroCanal getCanalCarga(Carga carga) throws BusinessException {

		HQLQuery<ParametroCanal> hql = new HQLQuery<ParametroCanal>(
				getEntityManager());
		hql.append("select f.parametroCanalAtual from FollowUpImportTrecho f");
		hql.append("inner join f.followUp fu");

		hql.appendEqual("fu.carga", carga);
		hql.appendEqual("f.canal", true);
		hql.appendCondicao("f.parametroCanalAtual is not null");

		try {
			return hql.getSingleResult();
		} catch (Exception e) {
		}

		return null;
	}

	/**
	 * Retorna os trecho do followUp da carga selecionada.
	 */
	@Override
	public List<FollowUpImportTrecho> listarByCarga(Carga carga)
			throws BusinessException {

		if (carga != null) {
			HQLQuery<FollowUpImportTrecho> hql = new HQLQuery<FollowUpImportTrecho>(
					getEntityManager());
			hql.append("select distinct trec from FollowUpImportTrecho trec ");
			hql.append("inner join trec.followUp followUp");
			hql.append("inner join fetch trec.cidade cidade");

			hql.appendEqual( "followUp.ativo", true );
			hql.appendEqual( "followUp.carga", carga );

			hql.append( "order by trec.id" );
			return hql.getResultList();
		}

		return new ArrayList<FollowUpImportTrecho>(0);

	}

	@Override
	public List<FollowUpImportTrecho> listTrechoByCity(Cidade city)
			throws BusinessException {
		if (city != null) {
			HQLQuery<FollowUpImportTrecho> hql = new HQLQuery<FollowUpImportTrecho>(
					getEntityManager());
			hql.append("from FollowUpImportTrecho ft ");
			hql.append("inner join fetch ft.cidade cidade");
			hql.appendEqual("cidade.id", city.getId());
			return hql.getResultList();
		}
		return new ArrayList<FollowUpImportTrecho>(0);
	}

	@Override
	public List<FollowUpImportTrecho> listarByFollowUp(FollowUpImport followUpImport)
			throws BusinessException {
		
		HQLQuery<FollowUpImportTrecho> hql = new HQLQuery<FollowUpImportTrecho>(
				getEntityManager());
		hql.append("from FollowUpImportTrecho ft ");
		hql.append("inner join fetch ft.followUp followUp");
		hql.appendEqual("followUp.id", followUpImport.getId());
		
		return hql.getResultList();
	}

}
