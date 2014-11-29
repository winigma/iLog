package br.com.ilog.cadastro.business.rep;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Hibernate;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.cits.commons.citsbusiness.util.HQLQuery;
import br.com.ilog.cadastro.business.entity.CheckList;
import br.com.ilog.cadastro.business.entity.ItemChecklist;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroCheckList;

/**
 * 
 * @author Wisley SOUZA
 * 
 * 
 */
@Component
public class CheckListRepositoryImpl extends CrudRepositoryJPA<CheckList>
		implements CheckListRepository {

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
	public CheckList getById(Serializable primaryKey) {
		// HQLQuery<CheckList> hql = new HQLQuery<CheckList>(entityManager);
		CheckList check = super.getById(primaryKey);
		Hibernate.initialize(check.getItem());
		//Hibernate.initialize(check.getProcesso());
		// hql.append("from CheckList cl");
		// hql.appendEqual("cl.id", primaryKey);

		return check;
	}

	@Override
	@Transactional( rollbackFor = DataIntegrityViolationException.class)
	public CheckList cadastrar(CheckList item) throws BusinessException {
		if (item.getId() == null) {
			getEntityManager().persist(item);
		}
		getEntityManager().flush();
		return item;
	}

	@Override
	public CheckList alterar(CheckList entity) throws BusinessException {
		return super.alterar(entity);
	}

	@Override
	@Transactional(readOnly=true )
		public List<CheckList> listarTypesOfCheckList(BasicFiltroCheckList check)
			throws BusinessException {
		HQLQuery<CheckList> hql = new HQLQuery<CheckList>(entityManager);
		hql.append("from CheckList cl");
		//hql.append("inner join fetch cl.processo processo");

		if (check != null) {

			if (check.getParametro() != null
					&& !check.getParametro().equals("")) {
				hql.appendCondicao("lower(cl.nome) like lower(:nome) ");
				hql.setParameter("nome", check.getParametro() + "%");

			}
			//if (check.getProcesso() != null) {
			//	hql.appendEqual("processo.nome", check.getProcesso().getNome());

			//}
			if (check.getAtivo() != null) {
				if (check.getAtivo()) {
					hql.appendCondicao("cl.ativo =:satusAtivo");
					hql.setParameter("satusAtivo", true);

				} else {
					hql.appendCondicao("cl.ativo =:satusInativo");
					hql.setParameter("satusInativo", false);

				}

			}

		}
		// hql.append("");
		// List<CheckList> result = new ArrayList<CheckList>();
		// for ( Object[] obj : hql.getResultList() ) {

		// }
		return hql.getResultList();
	}

	// metodo retorna os itens de check list a partir de um checklist
	// selecionado para edicao
	@Override
	public List<ItemChecklist> listarItensCheckListByFilter(
			BasicFiltroCheckList filtro) throws BusinessException {

		HQLQuery<ItemChecklist> hql = new HQLQuery<ItemChecklist>(entityManager);
		hql.append("from ItemCheckList item");
		hql.append("inner join fetch item.checklist checklist");
		if (filtro != null) {
			if (filtro.getCheckList() != null) {
				hql.appendEqual("checklist.id", filtro.getCheckList().getId());
			}
		}

		return hql.getResultList();
	}

}
