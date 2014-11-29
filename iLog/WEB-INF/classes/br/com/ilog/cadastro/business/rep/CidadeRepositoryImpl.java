package br.com.ilog.cadastro.business.rep;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.cits.commons.citsbusiness.util.HQLQuery;
import br.com.ilog.cadastro.business.entity.Cidade;
import br.com.ilog.cadastro.business.entity.Pais;
import br.com.ilog.cadastro.business.entity.Rota;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroCidade;
import br.com.ilog.importacao.business.entity.FollowUpImport;

/**
 * @author Wisley P. Souza
 */

@Component
public class CidadeRepositoryImpl extends CrudRepositoryJPA<Cidade> implements
		CidadeRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Cidade getById(Serializable arg) {
		HQLQuery<Cidade> hql = new HQLQuery<Cidade>(entityManager);
		hql.append("from Cidade c");
		hql.appendEqual("c.id", arg);

		Cidade cidade = hql.getSingleResult();
		if (!Hibernate.isInitialized(cidade))
			Hibernate.initialize(cidade);
		Hibernate.initialize(cidade.getEstado());
	    Hibernate.initialize(cidade.getPais());

		return cidade;
	}

	@Override
	public Cidade alterar(Cidade entity) throws BusinessException {
		return super.alterar(entity);
	}

	@Override
	public Cidade cadastrar(Cidade cidade) throws BusinessException {
		if (cidade.getId() == null) {
			getEntityManager().persist(cidade);
		}
		getEntityManager().flush();
		return cidade;

	}

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;

	}

	@Override
	public List<Cidade> listarCidades(BasicFiltroCidade filtro) {
		HQLQuery<Cidade> hql = new HQLQuery<Cidade>(entityManager);
		hql.append("from Cidade c");
		hql.append("inner join fetch c.pais pais");
		hql.append("inner join fetch c.estado estado");

		if (filtro != null) {
			if (filtro.getEstado() != null
					&& filtro.getEstado().getId() != null) {
				hql.appendEqual("estado.id", filtro.getEstado().getId());
			}
			if (filtro.getPais() != null) {
				hql.appendEqual("pais.id", filtro.getPais().getId());
			}
			if (filtro.getNomeCidade() != null) {
				hql.appendCondicao(" lower(c.nome) like lower(:nome) ");
				hql.setParameter("nome", filtro.getNomeCidade() + "%");
			}
			if(filtro.getSiglaCidade() != null){
				hql.appendCondicao(" lower(c.sigla) like lower(:sigla) ");
				hql.setParameter("sigla", filtro.getSiglaCidade() + "%");
			}
		}

		hql.append("order by c.nome, estado.sigla");
		return hql.getResultList();
	}

	@Override
	public List<Cidade> getCidadesByPais(Pais pais) throws BusinessException {
		HQLQuery<Cidade> hql = new HQLQuery<Cidade>(getEntityManager());

		hql.append("from Cidade c");

		if (pais != null) {
			hql.append("inner join fetch c.pais pais");
			hql.appendLike("pais.nome", pais.getNome());
		}
		hql.append("order by c.nome");

		return hql.getResultList();
	}

	@Override
	public List<Cidade> getCidadesByFollowUp(FollowUpImport follow)
			throws BusinessException {

		HQLQuery<Cidade> hql = new HQLQuery<Cidade>(getEntityManager());
		hql.append("select distinct c from Cidade c ");
		hql.append("inner join c.trechos trecho ");
		hql.append("inner join trecho.followUp fu ");

		hql.appendEqual("fu", follow);

		return hql.getResultList();
	}

	/**
	 * @Override public List<Cidade> getCidadesByFollowUp( FollowUpExport
	 *           follow) throws BusinessException {
	 * 
	 *           HQLQuery<Cidade> hql = new HQLQuery<Cidade>( getEntityManager()
	 *           ); hql.append("select distinct c from Cidade c ");
	 *           hql.append("inner join fetch c.trechosExp trecho ");
	 *           hql.append("inner join trecho.followUp fu ");
	 * 
	 *           hql.appendEqual("fu", follow);
	 * 
	 *           return hql.getResultList(); }
	 **/

	@Override
	public List<Cidade> getCidadesByFollowUpMoreCarga(FollowUpImport follow)
			throws BusinessException {

		HQLQuery<Cidade> hql = new HQLQuery<Cidade>(getEntityManager());
		hql.append("select distinct c from Cidade c ");
		hql.append("inner join fetch c.trechos trecho ");
		hql.append("inner join trecho.followUp fu ");
		hql.append("inner join fu.carga carga ");

		hql.appendEqual("fu", follow);

		return hql.getResultList();
	}

	@Override
	public List<Cidade> getCidadesByRota(Rota rota) throws BusinessException {

		if (rota != null) {

			HQLQuery<Cidade> hql = new HQLQuery<Cidade>(getEntityManager());
			hql.append("select cidade from Cidade cidade ");
			hql.append("where cidade.id in ( select t.cidadeOrigem.id from Trecho t where t.rota = :rota) ");
			hql.append(" or cidade.id in ( select r.cidadeDestino.id from Rota r where r = :rota ) ");

			hql.setParameter("rota", rota);

			return hql.getResultList();

		}
		return null;

	}

	@Override
	public Cidade cityById(Serializable arg) throws BusinessException {
		HQLQuery<Cidade> hql = new HQLQuery<Cidade>(entityManager);
		hql.append("from Cidade c");
		hql.appendEqual("c.id", arg);

		Cidade cidade = hql.getSingleResult();
		

		return cidade;
	}

}
