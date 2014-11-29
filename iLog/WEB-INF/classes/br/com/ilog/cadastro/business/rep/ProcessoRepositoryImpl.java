package br.com.ilog.cadastro.business.rep;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.cits.commons.citsbusiness.util.HQLQuery;
import br.com.ilog.cadastro.business.entity.Motivo;
import br.com.ilog.cadastro.business.entity.Processo;
import br.com.ilog.cadastro.business.entity.TipoOcorrencia;

@Component
public class ProcessoRepositoryImpl extends CrudRepositoryJPA<Processo> implements ProcessoRepository {

	@PersistenceContext( name = "ilog" )
	EntityManager entityManager;
	
	@Override
	public List<Processo> listar() {
		HQLQuery<Processo> hql = new HQLQuery<Processo>(getEntityManager());
		hql.append( "from Processo p" );
		
		hql.append( "order by p.nome " );
		return hql.getResultList();
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
	public List<Processo> listarByOcorrencia(TipoOcorrencia entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Processo> listarNotOcorrencia(TipoOcorrencia entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Processo> listarByMotivo(Motivo entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Processo> listarNotMotivo(Motivo entity) {
		// TODO Auto-generated method stub
		return null;
	}

}
