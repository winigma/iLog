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
import br.com.ilog.importacao.business.entity.ItemPO;
import br.com.ilog.importacao.business.entity.PO;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroPO;

@Component
public class PORepositoryImpl extends CrudRepositoryJPA<PO> implements
		PORepository {

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
	public PO getById(Serializable primaryKey) {
		PO re = super.getById(primaryKey);
		return re;
	}

	@Override
	public PO getPO(String obj) throws BusinessException {
		HQLQuery<PO> hql = new HQLQuery<PO>(entityManager);
		hql.append("from PO po");

		try {
			if (obj != null) {
				hql.appendEqual("po.numeroPO", obj);
				PO rel = (PO) hql.getSingleResult();
				return rel;
			}
		} catch (NoResultException ex) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see br.com.ilog.importacao.business.rep.PORepository#listarPO(br.com.ilog.importacao.business.entityFilter.BasicFiltroPO)
	 */
	@Override
	public List<PO> listarPO(BasicFiltroPO filtroPO) throws BusinessException {
		HQLQuery<PO> hql = new HQLQuery<PO>(entityManager);
		List<PO> list;
		hql.append("select distinct(po) from  PO po");
		hql.append("inner join fetch po.fornecedor fornecedor");
		hql.append("inner join fetch po.formaPagamento formaPagamento");
		hql.append("inner join fetch po.filial filial");
		hql.append("left join fetch po.itens itens");
		try {
			if (filtroPO != null) {
				if(filtroPO.getNumPO()!=null){
					hql.appendEqual("po.numeroPO", filtroPO.getNumPO());					
				}
				if(filtroPO.getPartNumber()!=null){
					hql.appendEqual("itens.partNumber", filtroPO.getPartNumber());					
				}
				
				if(filtroPO.getFormaPagamento()!=null){
					hql.appendEqual("formaPagamento", filtroPO.getFormaPagamento());					
				}
				if(filtroPO.getFornecedor()!=null){
					hql.appendEqual("fornecedor", filtroPO.getFornecedor());					
				}
				
			}
			list = hql.getResultList();
			for(PO po : list){
				for(ItemPO itemPO : po.getItens()){
					Hibernate.initialize(itemPO.getUnidadeMedida());
					Hibernate.initialize(itemPO.getMoeda());
				}
			}
			return list;
		} catch (NoResultException ex) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public ItemPO getItemPO(ItemPO itemPO) throws BusinessException {
		HQLQuery<ItemPO> hql = new HQLQuery<ItemPO>(entityManager);
		hql.append("from ItemPO itemPo");
		hql.append("inner join fetch itemPo.po po");
		hql.appendEqual("itemPo.partNumber", itemPO.getPartNumber());
		hql.appendEqual("po", itemPO.getPo());
		return hql.getSingleResult();
	}
	
}