package br.com.ilog.importacao.business.rep;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.cits.commons.citsbusiness.util.HQLQuery;
import br.com.ilog.cadastro.presentation.util.NumberDIException;
import br.com.ilog.geral.business.CodigoErroEspecifico;
import br.com.ilog.importacao.business.entity.Carga;
import br.com.ilog.importacao.business.entity.ProcBroker;
import br.com.ilog.importacao.business.entity.ProcItem;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroProcBroker;

@Component
public class ProcBrokerRepositoryImpl extends CrudRepositoryJPA<ProcBroker> implements ProcBrokerRepository {

	@PersistenceContext
	EntityManager entityManager;
	
	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public ProcBroker getById(Serializable primaryKey) {
		
		ProcBroker broker = super.getById(primaryKey); 
		
		if ( broker != null ) {
			Hibernate.initialize( broker.getIncoterm() );
		}
		
		return broker;
	}
	
	@Override
	public ProcBroker getById(Serializable primaryKey, boolean iniciarListas) {
		
		ProcBroker broker = this.getById(primaryKey);
		if ( broker != null && iniciarListas ) {
			
			Hibernate.initialize( broker.getItens() );
			for (ProcItem item : broker.getItens() ) {
				Hibernate.initialize( item.getItensPo() );
			}
			
			Hibernate.initialize( broker.getInvoices() );
		}
		return broker;
	}
	
	@Override
	public List<ProcBroker> listar(BasicFiltroProcBroker filtro)
			throws BusinessException {
		
		HQLQuery<ProcBroker> hql = new HQLQuery<ProcBroker>(getEntityManager());
		hql.append("from ProcBroker pb");
		hql.append("inner join fetch pb.incoterm incoterm" );
		
		if ( filtro != null ) {
			hql.appendEqual( "pb.hawb", filtro.getHawb() );
			hql.appendEqual( "pb.nrDI", filtro.getNumeroDI() );
			hql.appendEqual( "pb.ufrDespacho", filtro.getUfrDespacho() );
			hql.appendEqual( "incoterm", filtro.getIncoterm() );
		}
		
		hql.append( "order by pb.nrDI" );
		
		return hql.getResultList();
	}

	public ProcBroker getByNrDi( String nrDi ) {
		
		try {
			HQLQuery<ProcBroker> hql = new HQLQuery<ProcBroker>( getEntityManager() );
			hql.append("from ProcBroker pb");
			hql.appendEqual("pb.nrDI", nrDi);
			
			return hql.getSingleResult();
			
		} catch (NoResultException e) {
			
			return null;
		}
	}
	
	@Override
	public void salvarBroker(List<ProcBroker> brokersImport)
			throws BusinessException {
		
		List<String> disErro = new ArrayList<String>();
		
		for (ProcBroker procBroker : brokersImport) {
			
			//verifica se ja possui registro e exclui
			ProcBroker pb = getByNrDi( procBroker.getNrDI() );
			if ( pb != null ) {
				if ( pb.getCargas() != null ) {
					disErro.add( pb.getNrDI() );
				} else {
					excluirById( pb.getId() );
				}
			}
			if ( disErro.isEmpty() ) 
				cadastrar( procBroker );
		}
		
		//GERA EXCESAO CASO TENHA ALGUMA DI UTILIZANDO NO SISTEMA
		if ( !disErro.isEmpty() ) 
			throw new NumberDIException(CodigoErroEspecifico.MSG024, disErro);
		
	}

	@Override
	public List<ProcBroker> listarSemCarga(Carga carga) throws BusinessException {
		
		try {
			HQLQuery<ProcBroker> hql = new HQLQuery<ProcBroker>( getEntityManager() );
			hql.append("select pb from ProcBroker pb");
			hql.append("left join pb.cargasImport cargas");
			
			hql.appendCondicao( "(cargas is null or cargas.id = :idCarga)" );
			hql.setParameter( "idCarga", carga.getId() );
			
			return hql.getResultList();
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
