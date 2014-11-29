package br.com.ilog.cadastro.business.rep;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.cits.commons.citsbusiness.util.HQLQuery;
import br.com.ilog.cadastro.business.entity.Feriado;
import br.com.ilog.cadastro.business.entity.Pais;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroFeriado;

@Component
public class FeriadoRepositoryImpl extends CrudRepositoryJPA<Feriado> implements
		FeriadoRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public Feriado getById(Serializable primaryKey) {

		Feriado feriado = super.getById(primaryKey);
		Hibernate.initialize(feriado.getPais());

		return feriado;
	}

	@Override
	public Feriado alterar(Feriado feriado) throws BusinessException {
		if (feriado.getTipo().equals("F")) {

		}
		return super.alterar(feriado);
	}

	@Override
	public Feriado cadastrar(Feriado feriado) throws BusinessException {

		feriado = super.cadastrar(feriado);
		getEntityManager().flush();
		return feriado;

	}

	@Override
	public List<Feriado> cadastrarVarios(List<Feriado> feriados)
			throws BusinessException {
		for (Feriado feriado : feriados) {
			getEntityManager().persist(feriado);
		}
		// getEntityManager().persist(feriados);
		getEntityManager().flush();
		return feriados;
	}

	@Override
	public List<Feriado> listaFeriados(BasicFiltroFeriado filtro)
			throws BusinessException {
		HQLQuery<Feriado> hql = new HQLQuery<Feriado>(entityManager);
		hql.append("from Feriado f");
		hql.append("inner join fetch f.pais  pais");
		if (filtro != null) {
			if (!StringUtils.isBlank(filtro.getNome())) {
				hql.appendCondicao(" lower(f.nome) like lower(:nome) ");
				hql.setParameter("nome", filtro.getNome() + "%");
			}
			if (!StringUtils.isBlank(filtro.getTipo())) {
				hql.appendLike("f.tipo", filtro.getTipo());
			}

			if (filtro.getPais() != null) {
				hql.appendEqual("pais.nome", filtro.getPais().getNome());

			}
			
			if ( filtro.getPaises() != null ) {
				List<Pais> paises = new ArrayList<Pais>();
				paises.addAll( filtro.getPaises() );
				
				hql.appendIn("pais",  paises );
			}
			
			if (filtro.getDataInicio() != null) {

				Calendar c = Calendar.getInstance();
				c.setTime(filtro.getDataInicio());
				c.set(Calendar.YEAR, 1970);

				Date date = c.getTime();

				hql
						.appendCondicao("( ((f.data >= :dataInicio) and (year(f.data) is not 1970))"
								+ "   or "
								+ "((f.data >= :dataInicioI ) and (year(f.data) is 1970)  )  )");

				hql.setParameter("dataInicio", filtro.getDataInicio());
				hql.setParameter("dataInicioI", date);
			}
		}
		if (filtro.getDataFim() != null) {

			Calendar c = Calendar.getInstance();

			c.setTime(filtro.getDataFim());
			if (filtro.getDataInicio() != null) {
				Calendar dtIni = Calendar.getInstance();
				dtIni.setTime(filtro.getDataInicio());
				if (c.get(Calendar.YEAR) == dtIni.get(Calendar.YEAR)) {
					c.set(Calendar.YEAR, 1970);
				} else if (c.get(Calendar.YEAR) > dtIni.get(Calendar.YEAR)) {
					c.set(Calendar.YEAR, 1971);
				}
			} else {

				c.set(Calendar.YEAR, 1971);
			}
			Date date = c.getTime();

			hql
					.appendCondicao(" ( ((f.data <= :dataFim ) and (year(f.data) is not 1970) )"
							+ "  or "
							+ "((f.data <= :dataFimF)and (year(f.data) is 1970)))");

			hql.setParameter("dataFim", filtro.getDataFim());
			hql.setParameter("dataFimF", date);
		}

		return hql.getResultList();
	}

	@Override
	public void excluirById(Serializable id) throws BusinessException {
		super.excluirById(id);
	}

}
