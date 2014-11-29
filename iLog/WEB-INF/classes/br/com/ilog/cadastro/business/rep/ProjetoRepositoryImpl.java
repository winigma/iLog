package br.com.ilog.cadastro.business.rep;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.cits.commons.citsbusiness.util.HQLQuery;
import br.com.ilog.cadastro.business.entity.Projeto;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroProjeto;
import br.com.ilog.importacao.business.entity.Invoice;

@Component
public class ProjetoRepositoryImpl extends CrudRepositoryJPA<Projeto> implements
		ProjetoRepository {

	@PersistenceContext(unitName = "ilog")
	private EntityManager entityManager;
	
	@Override
	public void excluirById(Serializable arg0) throws BusinessException {
		super.excluirById(arg0);
	}

	/**
	 * @return
	 */
	@Override
	public List<Projeto> listar() {
		
		HQLQuery<Projeto> hql = new HQLQuery<Projeto>(entityManager);
		hql.append("from Projeto p");
		hql.append("order by p.nome");
		return hql.getResultList();
	}
	
	/**
	 * @return
	 */
	@Override
	public List<Projeto> listarAtivos() {
		
		HQLQuery<Projeto> hql = new HQLQuery<Projeto>(entityManager);
		hql.append("from Projeto p");
		hql.appendEqual( "p.ativo", true );
		hql.append("order by p.nome");
		
		return hql.getResultList();
	}

	@Override
	public Projeto getById(Serializable primaryKey) {
		Projeto p =  super.getById(primaryKey);
		
		return p;
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
	public List<Projeto> listar(BasicFiltroProjeto filtro)
			throws BusinessException {
		
		HQLQuery<Projeto> hql = new HQLQuery<Projeto>(entityManager);
		hql.append("from Projeto p");
		
		if (filtro != null) {
			if (filtro.getNome() != null) {
				hql.appendCondicao(" lower(p.nome) like lower(:nome) ");
				hql.setParameter("nome", filtro.getNome() + "%");
			}
			hql.appendEqual("p.ativo", filtro.getAtivo());

		}
		hql.append("order by p.nome");
		
		return hql.getResultList();
	}

	@Override
	public List<Projeto> listarProjetosPorInvoices(List<Invoice> listaInvoices)
			throws BusinessException {
		if ( listaInvoices != null && !listaInvoices.isEmpty() ) {
			HQLQuery<Projeto> hql = new HQLQuery<Projeto>(entityManager);
			hql.append("select distinct item.projeto from Invoice i");
			hql.append("left join i.itensInvoice item");
			
			hql.appendIn("i", listaInvoices );
			
			return hql.getResultList();
			
		} else {
			return new ArrayList<Projeto>();
		}
	}

}
