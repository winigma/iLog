package br.com.ilog.importacao.business.rep;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.cits.commons.citsbusiness.util.HQLQuery;
import br.com.ilog.cadastro.business.entity.CheckList;
import br.com.ilog.importacao.business.entity.Invoice;
import br.com.ilog.importacao.business.entity.InvoiceChecklist;
import br.com.ilog.importacao.business.entity.InvoiceItemChecklist;

@Component
public class InvoiceChecklistRepositoryImpl extends
		CrudRepositoryJPA<InvoiceChecklist> implements
		InvoiceChecklistRepository {

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
	public InvoiceChecklist getById(Serializable primaryKey) {

		InvoiceChecklist InvCheck = super.getById(primaryKey);

		return InvCheck;
	}

	@Override
	public InvoiceChecklist cadastrar(InvoiceChecklist entity)
			throws BusinessException {
		return super.cadastrar(entity);
	}

	@Override
	public InvoiceChecklist getInvoiceChecklist(Invoice invoice,
			CheckList checklist) throws BusinessException {
		HQLQuery<InvoiceChecklist> hql = new HQLQuery<InvoiceChecklist>(
				getEntityManager());

		hql.append("select ic from InvoiceChecklist ic ");
		hql.append("inner join fetch ic.invoice inv");
		hql.append("inner join fetch ic.checkList check");

		if (invoice.getId() != null && checklist.getId() != null) {
			hql.appendEqual("inv", invoice);
			hql.appendEqual("check", checklist);
		} else {
			return null;
		}
		try {

			InvoiceChecklist ic = hql.getSingleResult();
			if (ic != null) {
				Hibernate.initialize(ic.getCheckList());
				Hibernate.initialize(ic.getInvoice());
				Hibernate.initialize(ic.getRespostasChecklist());
				for (InvoiceItemChecklist iic : ic.getRespostasChecklist()) {
					Hibernate.initialize(iic.getItemCheckList());
				}
			}

			return ic;
		} catch (NoResultException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public InvoiceChecklist getInvoiceChecklist(Invoice invoice)
			throws BusinessException {
		HQLQuery<InvoiceChecklist> hql = new HQLQuery<InvoiceChecklist>(
				getEntityManager());

		hql.append("from InvoiceChecklist ic ");
		hql.append("inner join fetch ic.invoice inv");
		hql.append("inner join fetch ic.checkList check");
		if (invoice.getId() != null) {
			hql.appendEqual("inv", invoice);
		} else {
			return null;
		}
		try {

			InvoiceChecklist ic = hql.getSingleResult();
			if (ic != null) {
				Hibernate.initialize(ic.getCheckList());
				Hibernate.initialize(ic.getInvoice());
				Hibernate.initialize(ic.getRespostasChecklist());
				for (InvoiceItemChecklist iic : ic.getRespostasChecklist()) {
					Hibernate.initialize(iic.getItemCheckList());
				}
			}

			return ic;
		} catch (NoResultException e) {
			e.printStackTrace();
		}

		return null;
	}

}
