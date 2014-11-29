package br.com.ilog.importacao.business.rep;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.cits.commons.citsbusiness.util.HQLQuery;
import br.com.ilog.importacao.business.entity.Carga;
import br.com.ilog.importacao.business.entity.ItemInvoice;

@Component
public class ItemInvoiceRepositoryImpl extends
		CrudRepositoryJPA<ItemInvoice> implements
		ItemInvoiceRepository{

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

	/* (non-Javadoc)
	 * @see br.com.ilog.importacao.business.rep.ItemInvoiceRepository#listarItemInvoicesByCarga(br.com.ilog.importacao.business.entity.Carga)
	 */
	@Override
	public List<ItemInvoice> listarItemInvoicesByCarga(Carga carga)
			throws BusinessException {
		HQLQuery<ItemInvoice> hql = new HQLQuery<ItemInvoice>(entityManager);
		hql.append("from ItemInvoice item ");
		hql.append("inner join fetch item.invoice inv");
		hql.append("inner join fetch inv.carga carga");
		hql.appendEqual("carga", carga);
		
		return hql.getResultList();
	}

	

}
