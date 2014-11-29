package br.com.ilog.importacao.business.rep;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.cits.commons.citsbusiness.util.HQLQuery;
import br.com.ilog.importacao.business.entity.AnexoFollowUpImp;
import br.com.ilog.importacao.business.entity.Carga;
import br.com.ilog.importacao.business.entity.FollowUpImport;
import br.com.ilog.importacao.business.entity.FollowUpImportTrecho;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroFollowUp;
import br.com.ilog.relatorio.business.dto.CargaRelatorioWeeklyBasis;

@Component
public class FollowUpRepositoryImpl extends CrudRepositoryJPA<FollowUpImport>
		implements FollowUpRepository {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public FollowUpImport getFollowUpByCarga(Carga carga)
			throws BusinessException {

		HQLQuery<FollowUpImport> hql = new HQLQuery<FollowUpImport>(
				entityManager);
		hql.append("from FollowUpImport f ");
		hql.appendEqual("f.ativo", true);

		try {
			if (carga != null) {
				hql.appendEqual("f.carga", carga);
				if (!hql.getResultList().isEmpty()) {

					FollowUpImport imp;
					imp = (FollowUpImport) hql.getSingleResult();
					if (imp.getMoeda() != null) {
						Hibernate.initialize(imp.getMoeda());
					}
					Hibernate.initialize(imp.getResponsavel());
					Hibernate.initialize(imp.getTrechosFollowUp());
					for (FollowUpImportTrecho trec : imp.getTrechosFollowUp()) {
						Hibernate.initialize(trec);
						Hibernate.initialize(trec.getCidade());
						Hibernate.initialize(trec.getParametroCanal());
						Hibernate.initialize(trec.getParametroCanalAtual());
					}
					return imp;
				}
			} 

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return null;
	}

	@Override
	public FollowUpImport alterar(FollowUpImport arg0) throws BusinessException {
		return super.alterar(arg0);
	}

	@Override
	public FollowUpImport cadastrar(FollowUpImport arg0)
			throws BusinessException {
		return super.cadastrar(arg0);
	}

	@Override
	public void excluirById(Serializable arg0) throws BusinessException {
		// TODO Auto-generated method stub

	}

	@Override
	public FollowUpImport getById(Serializable arg0) {
		return (FollowUpImport) super.getById(arg0);
	}

	@Override
	public List<FollowUpImport> listar() {
		// TODO Auto-generated method stub
		return null;
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
	public FollowUpImport carregarFollowUpEstimados(FollowUpImport followUp)
			throws BusinessException {

		Hibernate.initialize(followUp.getTrechosFollowUp());

		for (FollowUpImportTrecho trecho : followUp.getTrechosFollowUp()) {

			trecho = getEntityManager().find(FollowUpImportTrecho.class,
					trecho.getId());

			Hibernate.initialize(trecho.getEstimados());
		}

		return followUp;
	}

	@Override
	public List<AnexoFollowUpImp> getAnexosFollowUp(FollowUpImport followUp)
			throws BusinessException {

		if (followUp != null) {
			HQLQuery<AnexoFollowUpImp> hql = new HQLQuery<AnexoFollowUpImp>(
					getEntityManager());

			hql.append("from AnexoFollowUpImp anexo");
			hql.appendEqual("anexo.followUp", followUp);

			return hql.getResultList();
		}

		return new ArrayList<AnexoFollowUpImp>(0);
	}

	@Override
	public List<FollowUpImport> listQualityGrowth(BasicFiltroFollowUp filtro)
			throws BusinessException {
		HQLQuery<FollowUpImport> hql = new HQLQuery<FollowUpImport>(
				this.entityManager);
		hql.append("select distinct followup from FollowUpImport followup");

		hql.append("left join fetch followup.carga carga");
		hql.append("left join fetch followup.responsavel responsavel");
		hql.append("left join fetch carga.listaDeInvoices listInvoices");

		// Junção dos Objetos de carga
		hql.append("left join fetch carga.agenteCarga agCarga");
		hql.append("left join fetch carga.cidadeAtual cidadeAtual");
		hql.append("left join fetch carga.importador importador");
		hql.append("left join fetch carga.rota rota");
		hql.append("left join fetch rota.paisOrigem paisOrigem");
		hql.append("left join fetch rota.paisDestino paisDestino");
		hql.append("left join fetch rota.cidadeOrigem cidadeOrigem");
		hql.append("left join fetch carga.canal canal");
		
		hql.appendEqual("followup.ativo", true);
		hql.appendCondicao( "carga.filial is not null" );
		
		if (filtro != null) {
			// Coleta
			if (filtro.getDtInicioColeta() != null && filtro.getDtFimColeta() != null) {
				hql.appendBetween("carga.dtColeta", filtro.getDtInicioColeta(),
						filtro.getDtFimColeta());
			}
			// Chegada
			if (filtro.getDtInicio() != null && filtro.getDtFim() != null) {
				hql.appendBetween("carga.dtChegada", filtro.getDtInicio(),
						filtro.getDtFim());
			}
			
			// PROCESSO
			if (filtro.getProcesso() != null && !filtro.getProcesso().equals("") ) {
				
				String filial[]= {""};
				String sequencia[] = {""};
				
				//Verificar se possui filial e sequencia
				if ( filtro.getProcesso().contains(".") && filtro.getProcesso().contains("/") ) {
					filial = filtro.getProcesso().split("\\.");
					hql.appendEqual("carga.filial.codigo", filial[0] );
					
					if ( filial.length > 1 ) {
						sequencia = filial[1].split("/");
						if (sequencia.length > 1) {
							if (!sequencia[0].equals(""))
								hql.appendEqual( "carga.numSequencia", Integer.parseInt(sequencia[0]), true );
							hql.appendEqual( "carga.anoSequencia", Integer.parseInt(sequencia[1]), true );
						}
					}
					//verificar apenas filial
				} else if ( filtro.getProcesso().contains(".") && !filtro.getProcesso().contains("/") ) {
					filial = filtro.getProcesso().split("\\.");
					if ( filial.length >=  1 ) {
						hql.appendEqual("carga.filial.codigo", filial[0] );
					}
					
					if ( filial.length > 1 ) {
						sequencia[0] = filial[1];
						if (sequencia.length >= 1) {
							if (!sequencia[0].equals(""))
								hql.appendEqual( "carga.numSequencia", Integer.parseInt(sequencia[0]), true );
						}
					}
					//verificar apenas sequencia
				} else  if ( !filtro.getProcesso().contains(".") && filtro.getProcesso().contains("/") ) {
					
					sequencia = filtro.getProcesso().split( "/" );
					if (sequencia.length > 1) {
						if (!sequencia[0].equals("")){
							hql.appendEqual( "carga.numSequencia", Integer.parseInt(sequencia[0]), true );
						}
						hql.appendEqual( "carga.anoSequencia", Integer.parseInt(sequencia[1]), true );
						
					} else {
						try {
							hql.appendCondicao("carga.anoSequencia = :ano or carga.numSequencia = :num");
							int ano = Integer.parseInt( sequencia[0] );
							if ( sequencia[0].length() == 2 ) {
								ano = Integer.parseInt( "20" + sequencia[0] );
							}
							hql.setParameter("ano", ano );
							hql.setParameter("num", Integer.parseInt(sequencia[0]));
							
						} catch (Exception e) {
							return new ArrayList<FollowUpImport>(0);
						}
					}
					
					//verificar em todos os registros: codigo, numero e ano de sequencia
				} else {
					try {
						hql.appendCondicao("( carga.anoSequencia = :ano or carga.numSequencia = :num or carga.filial.codigo =:codigo )");
						hql.setParameter( "codigo", filtro.getProcesso() );
						try {
							int ano = Integer.parseInt( filtro.getProcesso() );
							if ( filtro.getProcesso().length() == 2 ) {
								ano = Integer.parseInt( "20" + filtro.getProcesso() );
							}
							hql.setParameter("ano", ano );
							hql.setParameter("num", Integer.parseInt( filtro.getProcesso() ) );
						} catch (Exception e) {
						}
					} catch (Exception e) {
						return new ArrayList<FollowUpImport>(0);
					}
				}
			}
			
			// Fornecedor
			if (filtro.getSupplier() != null) {
				hql.appendEqual("listInvoices.exportador.id", filtro
						.getSupplier().getId());
			}
			// Agente Carga
			if (filtro.getAgCarga() != null) {
				hql.appendEqual("agCarga", filtro.getAgCarga());
			}
			// Continente
			if (filtro.getContinente() != null) {
				hql.appendEqual("paisOrigem.regiao", filtro.getContinente());
			}
			// Pais Origem
			if (filtro.getPaisOrigem() != null) {
				hql.appendEqual("paisOrigem.id", filtro.getPaisOrigem()
								.getId());
			}
			// Pais Destino
			if (filtro.getPaisDestino() != null) {
				hql.appendEqual("paisDestino.id", filtro.getPaisDestino()
						.getId());
			}
			// Responsavel
			if (filtro.getResponsavel() != null) {
				hql.appendEqual("responsavel.id", filtro.getResponsavel()
						.getId());

			}
		}
		hql.append("order by carga.id asc");

		return hql.getResultList();
	}

	/**
	 * @author Manoel Neto
	 * @date 21/03/2012
	 * @return List<CargaRelatorioWeeklyBasis>
	 * @Coment Retorna os followUps de todos os continentes de determinado perido agrupado por semana
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CargaRelatorioWeeklyBasis> listFollowUpPorContinente(BasicFiltroFollowUp filtro) throws BusinessException{
		
		Query query = entityManager.createNativeQuery("" +
				"select DATE_PART('week', c.DT_COLETA) as semana, PO.REGIAO, COUNT(*) as quantidade " +
				"from FOLLOW_UP_IMPORT fui " +
				"inner join CARGA as c on c.ID = fui.ID_CARGA " +
				"inner join ROTA as r on  r.ID = c.ID_ROTA " +
				"inner join PAIS as PO on PO.ID = r.ID_PAIS_ORIGEM " +
				"where (c.dt_coleta between '"+filtro.getAno()+"/01/01' and '"+filtro.getAno()+"/12/31') and fui.ATIVO = 't' " +
				"group by DATE_PART('week', c.DT_COLETA) , PO.REGIAO " +
				"order by DATE_PART('week', c.DT_COLETA)");
		List<Object[]> resultList =  query.getResultList();
		List<CargaRelatorioWeeklyBasis> result = new ArrayList<CargaRelatorioWeeklyBasis>();
		CargaRelatorioWeeklyBasis cr = new CargaRelatorioWeeklyBasis();
		
		for (Object object[] : resultList) {
			Integer i =  ((Double) object[0]).intValue();
			String regiao = (String) object[1];
			Integer qtd =  ((BigInteger) object[2]).intValue();
			cr= new CargaRelatorioWeeklyBasis(i,regiao,qtd);
			result.add(cr);
		}
		return result;
	}
	
	/**
	 * @author Heber Santiago
	 * 
	 * Classe que retorna os continentes com os prazos excedidos pelo parametro_continente
	 * filtrando por followup ativo e periodo
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<CargaRelatorioWeeklyBasis> listCargasWeeklyBasis(BasicFiltroFollowUp filtro) throws BusinessException {
		
		String continente = "";
		if(filtro.getContinente()!= null){
			continente = filtro.getContinente().name();
		}		
		Query query = entityManager.createNativeQuery("" +
				"SELECT  DATE_PART('week', CARGA.DT_COLETA), (PAIS.REGIAO), COUNT(*) " +
				"FROM FOLLOW_UP_IMPORT fui, CARGA, ROTA, PAIS " +
				"where (fui.ID_CARGA = CARGA.ID and carga.ID_ROTA = rota.ID and rota.ID_PAIS_ORIGEM = PAIS.ID) " +
				"and fui.ATIVO = 't' " +
				"and PAIS.REGIAO like '%"+continente+"%' " +
				"and CARGA.OTD >= (select pc.PRAZO from PARAMETRO_CONTINENTE as pc where pc.CONTINENTE = PAIS.REGIAO) " +
				"and carga.dt_coleta between '"+filtro.getAno()+"/01/01' and '"+filtro.getAno()+"/12/31' " +
				"group by PAIS.REGIAO, DATE_PART('week', CARGA.DT_COLETA)");
		List<Object[]> resultList =  query.getResultList();
		List<CargaRelatorioWeeklyBasis> result = new ArrayList<CargaRelatorioWeeklyBasis>();
		CargaRelatorioWeeklyBasis cr = new CargaRelatorioWeeklyBasis();
		
		for (Object object[] : resultList) {
			
			Integer i = ((Double) object[0]).intValue();
			String regiao = (String) object[1];
			Integer qtd = ((BigInteger) object[2]).intValue();
			cr= new CargaRelatorioWeeklyBasis(i,regiao,qtd);
			result.add(cr);
		}
		return result; 
		
	}

}
