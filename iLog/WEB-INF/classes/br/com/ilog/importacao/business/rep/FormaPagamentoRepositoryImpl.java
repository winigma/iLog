package br.com.ilog.importacao.business.rep;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.cits.commons.citsbusiness.util.HQLQuery;
import br.com.ilog.cadastro.business.entity.FormaPagamento;

@Component
public class FormaPagamentoRepositoryImpl extends CrudRepositoryJPA<FormaPagamento> implements
		FormaPagamentoRepository{

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public EntityManager getEntityManager() {
		// TODO Auto-generated method stub
		return this.entityManager;
	}

	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;

	}

	@Override
	public FormaPagamento getById(Serializable primaryKey) {
		FormaPagamento re = super.getById(primaryKey);
		return re;
	}

	@Override
	public FormaPagamento getBycodeSAP(String codeSAP) throws BusinessException {
		HQLQuery<FormaPagamento> hql = new HQLQuery<FormaPagamento>(entityManager);
		hql.append("from FormaPagamento fp");

		try {
			
				if (codeSAP != null) {
					hql.appendCondicao(" lower(fp.codeSAP) like lower(:codeSAP) ");
					hql.setParameter("codeSAP", codeSAP + "%");
				}
				FormaPagamento rel = (FormaPagamento) hql.getSingleResult();
				return rel;
		} catch (NoResultException ex) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public List<FormaPagamento> listarFormasPagamentoAtivos() throws BusinessException {
		HQLQuery<FormaPagamento> hql = new HQLQuery<FormaPagamento>(entityManager);
		hql.append("from FormaPagamento fp");

		try {
			
				hql.appendEqual("fp.ativo", true);
				
				return hql.getResultList();
		} catch (NoResultException ex) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
}
