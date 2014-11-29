package br.com.ilog.importacao.business.rep;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.cits.commons.citsbusiness.util.HQLQuery;
import br.com.ilog.importacao.business.entity.Carga;
import br.com.ilog.importacao.business.entity.CustoDI;
import br.com.ilog.importacao.business.entity.Invoice;
import br.com.ilog.importacao.business.entity.ItemInvoice;
import br.com.ilog.importacao.business.entity.StatusCarga;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroCarga;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroPlanejarCarga;

@Component
public class CargaRepositoryImpl extends CrudRepositoryJPA<Carga> implements
		CargaRepository {

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
	public Carga cadastrar(Carga entity) throws BusinessException {

		entity = preencheSequencial(entity);
		entity.setDtConsolidacao(new Date());
		entity.setStatus( StatusCarga.P );
		
		return super.cadastrar(entity);
	}

	/**
	 * Preenche o numero sequencial da entidade.
	 * 
	 * @param carga
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public Carga preencheSequencial(Carga carga) throws BusinessException {

		Calendar c = Calendar.getInstance();
		String aux = Integer.toString(c.get(Calendar.YEAR));
		int ano = Integer.valueOf(aux.substring(2));

		int lastCode = this.getLastCodeByYear(ano);

		carga.setNumSequencia(lastCode + 1);
		carga.setAnoSequencia(ano);

		return carga;
	}

	/**
	 * Recuperar o ultimo sequencial do ano por parametro.
	 * 
	 * @param ano
	 * @return
	 * @throws BusinessException
	 */
	private Integer getLastCodeByYear(int ano) throws BusinessException {

		HQLQuery<Integer> hql = new HQLQuery<Integer>(getEntityManager());

		hql.append("select MAX(i.numSequencia) from Carga i");
		hql.appendEqual("i.anoSequencia", ano);
		hql.append("group by i.anoSequencia");

		try {
			Integer result = hql.getSingleResult();

			if (result != null) {
				return result;
			}
		} catch (NoResultException e) {
			e.printStackTrace();
			return 0;
		}

		return 0;
	}

	@Override
	public Carga alterar(Carga entity) throws BusinessException {

		if (entity.getNumSequencia() == null || entity.getNumSequencia() == 0) {
			entity = preencheSequencial(entity);
		}

		return super.alterar(entity);
	}

	@Override
	public Carga getById(Serializable primaryKey) {
		Carga carga = super.getById(primaryKey);
		Hibernate.initialize(carga.getAgenteCarga());
		Hibernate.initialize(carga.getFilial());
		Hibernate.initialize(carga.getRota());
		Hibernate.initialize(carga.getCidadeAtual());
		Hibernate.initialize(carga.getImportador());
		Hibernate.initialize(carga.getProcBroker());
		Hibernate.initialize(carga.getListaDeInvoices());
		

		return carga;
	}

	/**
	 * @param carga
	 * @return
	 */
	@Override
	public Carga carregarProcBroker(Carga carga) {
		if (carga != null)
			Hibernate.initialize(carga.getProcBroker());

		return carga;
	}

	@Override
	public Carga getByIdRelatorio(Long primaryKey) {
		Carga carga = super.getById(primaryKey);
		Hibernate.initialize(carga.getAgenteCarga());
		Hibernate.initialize(carga.getRota());
		Hibernate.initialize(carga.getCidadeAtual());
		Hibernate.initialize(carga.getImportador());
		Hibernate.initialize(carga.getProcBroker());
		Hibernate.initialize(carga.getListaDeInvoices());

		if (carga.getRota() != null) {
			Hibernate.initialize(carga.getRota().getPaisDestino());
			Hibernate.initialize(carga.getRota().getCidadeDestino());
			Hibernate.initialize(carga.getRota().getPaisOrigem());
			Hibernate.initialize(carga.getRota().getCidadeOrigem());
			Hibernate.initialize(carga.getRota().getTipoTransporte());
		}
		if (carga.getRota() != null) {
			if (carga.getRota().getTrechos() != null) {
				Hibernate.initialize(carga.getRota().getTrechos());
			}
		}

		Hibernate.initialize(carga.getCanal());
		return carga;
	}

	/**
	 * Verifica os campos nulos de uma invoice.
	 * 
	 * @param inv
	 * @return
	 */
	private boolean verificarInvoice(Invoice inv) {
		if (inv == null) {
			return false;
		} else if (inv.getNumeroInvoice() != null
				&& !inv.getNumeroInvoice().equals(""))
			return true;

		return false;
	}

	/**
	 * Verificar os campo nulos de um item
	 * 
	 * @param item
	 * @return
	 */
	private boolean verificarItemInvoice(ItemInvoice item) {
		if (item == null) {
			return false;
		} else if (item.getItemPO() != null 
				&& item.getItemPO().getPo() != null 
				&& item.getItemPO().getPo().getNumeroPO() != null )
			return true;

		return false;
	}

	@Override
	public Carga listCargaPlussInvoice(Carga carga) throws BusinessException {
		HQLQuery<Carga> hql = new HQLQuery<Carga>(this.entityManager);
		hql.append("select distinct c from Carga c");
		hql.append("inner join fetch c.listaDeInvoices invoice");
		hql.append("inner join fetch invoice.exportador fornecedor");
		hql.appendEqual("c", carga);
		Carga c = hql.getSingleResult();
		return c;

	}

	@Override
	public List<Carga> listCargaByFilter(BasicFiltroCarga filtro)
			throws BusinessException {
		HQLQuery<Carga> hql = new HQLQuery<Carga>(this.entityManager);
		hql.append("select distinct c from Carga c");
		hql.append("inner join fetch c.filial filial");
		hql.append("left join fetch c.rota ro");

		hql.append("left join fetch c.agenteCarga agente");
		hql.append("left join fetch ro.paisOrigem origem");
		hql.append("left join fetch ro.paisDestino destino");

		if (filtro != null) {
			if (verificarInvoice(filtro.getInvoice())
					&& !verificarItemInvoice(filtro.getItemInvoice())) {
				hql.append("inner join fetch c.listaDeInvoices invoice");
				hql.appendEqual("invoice.numeroInvoice", filtro.getInvoice()
						.getNumeroInvoice());

			} else if (verificarInvoice(filtro.getInvoice())
					&& verificarItemInvoice(filtro.getItemInvoice())) {
				hql.append("inner join fetch c.listaDeInvoices invoice");
				hql.append("inner join  invoice.itensInvoice itens");

				if (filtro.getInvoice().getNumeroInvoice() != null
						&& !filtro.getInvoice().getNumeroInvoice().isEmpty()) {
					hql.appendCondicao(" lower(invoice.numeroInvoice) like lower(:numInv) ");
					hql.setParameter("numInv", filtro.getInvoice()
							.getNumeroInvoice() + "%");
				}

				hql.appendEqual("itens.itemPO.po.poNumero", filtro.getItemInvoice()
						.getItemPO().getPo().getNumeroPO());

			} else if (verificarItemInvoice(filtro.getItemInvoice())) {
				hql.append("inner join fetch c.listaDeInvoices invoice");
				hql.append("inner join  invoice.itensInvoice itens");

				hql.appendEqual("itens.itemPO.po.numeroPO", filtro.getItemInvoice()
						.getItemPO().getPo().getNumeroPO());
			}

			if (filtro.getAgente() != null) {
				hql.appendEqual("agente", filtro.getAgente());
			}
			if (filtro.getRota() != null) {
				if (filtro.getRota().getPaisOrigem() != null) {
					hql.appendEqual("origem", filtro.getRota().getPaisOrigem());
				}
				if (filtro.getRota().getPaisDestino() != null) {
					hql.appendEqual("destino", filtro.getRota()
							.getPaisDestino());
				}

			}
			if (filtro.getHawb() != null && !filtro.getHawb().isEmpty()) {

				hql.appendCondicao(" lower(c.hawb) like lower(:hawb) ");
				hql.setParameter("hawb", filtro.getHawb() + "%");
			}

			hql.appendBetween("c.dtColeta", filtro.getDtInicioColeta(),
					filtro.getDtFimColeta());
			hql.appendBetween("c.dtConsolidacao", filtro.getDtConsolidacao(),
					filtro.getDtFimConsolidacao());

			hql.appendEqual("c.status", filtro.getStatus());
			if (filtro.getStatus() != null
					&& (filtro.getStatus().equals(StatusCarga.ITT) || filtro
							.getStatus().equals(StatusCarga.OHI))) {
				hql.appendEqual("c.cidadeAtual", filtro.getCidadeAtual());
			}

			if (filtro.getNumMaster() != null
					&& !filtro.getNumMaster().isEmpty()) {
				hql.appendCondicao(" lower(c.numMaster) like lower(:numMaster) ");
				hql.setParameter("numMaster", filtro.getNumMaster() + "%");
			}
			
			
			if ( filtro.getProcesso() != null ) {
				
				hql = pesquisarPorProcesso(filtro, hql);
				
				if ( hql == null ) 
					return new ArrayList<Carga>(0);
				
			}

		}
		hql.append("order by c.filial.codigo, c.anoSequencia, c.numSequencia ");

		return hql.getResultList();
	}

	@Override
	public List<Carga> listCargasFollowUp(BasicFiltroCarga filtro)
			throws BusinessException {
		HQLQuery<Carga> hql = new HQLQuery<Carga>(this.entityManager);
		hql.append("select distinct c from Carga c");
		hql.append("inner join fetch c.rota ro");
		hql.append("inner join fetch c.filial filial");
		hql.append("inner join fetch c.agenteCarga agente");
		hql.append("inner join fetch ro.paisOrigem origem");
		hql.append("inner join fetch ro.paisDestino destino");
		hql.append("left join fetch c.listaDeFollowUps listaDeFollowUps");

		hql.append("inner join fetch c.listaDeInvoices invoice");

		if (filtro != null) {
			
			if (filtro.getNumPO() != null || filtro.getNumberPart() != null) {
				hql.append("inner join invoice.itensInvoice item");
				if (filtro.getNumPO() != null && !filtro.getNumPO().equals("")) {
					
					hql.appendLike("item.poNumero", filtro.getNumPO());
					
				}
				
				if (filtro.getNumberPart()!= null && !filtro.getNumberPart().isEmpty()) {
					
					//hql.appendLike("item.vendorPartNum", filtro.getNumberPart());
					hql.appendCondicao(" lower(item.vendorPartNum) like lower(:vendor) ");
					hql.setParameter("vendor", filtro.getNumberPart() + "%");
					
				}
			}
			hql.appendEqual("c.importador", filtro.getImportador());

			if (filtro.getAgente() != null) {
				hql.appendEqual("agente", filtro.getAgente());
			}
			if (filtro.getRota() != null) {
				hql.appendEqual("origem", filtro.getRota().getPaisOrigem());
				hql.appendEqual("destino", filtro.getRota().getPaisDestino());
				hql.appendLike("codigo", filtro.getRota().getCodigo());

			}
			hql.appendLike("invoice.numeroInvoice", filtro.getNumInvoice());
			hql.appendEqual("invoice.exportador", filtro.getExportador());

			hql.appendLike("c.hawb", filtro.getHawb());
			hql.appendLike("c.numAPS", filtro.getAps());

			hql.appendBetween("c.dtColeta", filtro.getDtInicioColeta(),
					filtro.getDtFimColeta());
			hql.appendBetween("c.dtConsolidacao", filtro.getDtConsolidacao(),
					filtro.getDtFimConsolidacao());

			hql.appendEqual("c.status", filtro.getStatus());
			if (filtro.getStatus() != null
					&& (filtro.getStatus().equals(StatusCarga.ITT) || filtro
							.getStatus().equals(StatusCarga.OHI))) {
				hql.appendEqual("c.cidadeAtual", filtro.getCidadeAtual());
			}
			if (filtro.getAtivo() != null) {
				hql.appendEqual("listaDeFollowUps.ativo", filtro.getAtivo());
			}
		}

		hql.append("order by filial.codigo, c.anoSequencia, c.numSequencia");

		return (ArrayList<Carga>) hql.getResultList();
	}

	@Override
	public List<Carga> listCargasPainel(BasicFiltroCarga filtro)
			throws BusinessException {

		HQLQuery<Carga> hql = new HQLQuery<Carga>(this.entityManager);
		hql.append("select distinct c from Carga c");
		hql.append("inner join fetch c.rota ro");
		hql.append("left join fetch c.canal canal");
		hql.append("inner join fetch ro.paisOrigem origem");
		hql.append("inner join c.listaDeInvoices lo");

		if (filtro != null) {
			hql.appendBetween("c.dtPrevista", filtro.getDtInicioPrevista(),
					filtro.getDtFimPrevista());
			hql.appendEqual("c.status", filtro.getStatus());
			// hql.appendLike("c.numAPS", filtro.getAps());
			hql.appendEqual("c.visivel", filtro.getVisivel());

		}

		// capturando a data
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, -1);
		hql.appendNotEqual("c.status", StatusCarga.C);

		// hql.appendNotEqual("dtChegada", StatusCarga.F);

		// hql.appendNotEqual("c.status", StatusCarga.AT);
		hql.appendCondicao(" (c.dtChegada >= :dtChegada OR c.dtChegada is null )");
		// hql.setParameter("status", StatusCarga.AT);
		hql.setParameter("dtChegada", c.getTime());
		hql.append("order by c.dtPrevista asc, c.status ");

		return hql.getResultList();
	}

	@Override
	public List<Carga> listCargaRelatorio(BasicFiltroCarga filtro)
			throws BusinessException {
		HQLQuery<Carga> hql = new HQLQuery<Carga>(this.entityManager);
		hql.append("select distinct c from Carga c");
		hql.append("left join fetch c.listaDeInvoices lo");
		hql.append("left join fetch lo.exportador ex");
		hql.append("left join fetch lo.comprador co");
		hql.append("left join fetch c.rota ro");
		hql.append("left outer join fetch c.canal canal");
		hql.append("inner join fetch ro.paisOrigem po");

		if (filtro != null) {
			if (filtro.getDtInicioColeta() != null
					&& filtro.getDtFimColeta() != null) {

				hql.appendBetween("c.dtColeta", filtro.getDtInicioColeta(),
						filtro.getDtFimColeta());
			}
			if (filtro.getCarga().getNumAPS() != null) {
				hql.appendLike("c.numAPS", filtro.getCarga().getNumAPS());
			}
			if (filtro.getRota().getPaisOrigem() != null) {
				hql.appendEqual("po.id", filtro.getRota().getPaisOrigem()
						.getId());
			}
			if (filtro.getRota().getTipoTransporte() != null) {
				hql.appendEqual("ro.tipoTransporte", filtro.getRota()
						.getTipoTransporte());
			}
			if (filtro.getCarga().getStatus() != null) {
				hql.appendEqual("c.status", filtro.getCarga().getStatus());
			}
			if (filtro.getCarga().getCanal() != null) {
				hql.appendEqual("c.canal", filtro.getCarga().getCanal());
			}
			if (filtro.getInvoice().getComprador() != null) {
				hql.appendEqual("co.id", filtro.getInvoice().getComprador()
						.getId());
			}
			if (filtro.getInvoice().getExportador() != null) {
				hql.appendEqual("ex.id", filtro.getInvoice().getExportador()
						.getId());
			}

		}
		hql.append("order by c.numAPS asc, c.dtConsolidacao ");

		return hql.getResultList();
	}

	@Override
	public List<Carga> listarCargas(BasicFiltroCarga filtro)
			throws BusinessException {
		HQLQuery<Carga> hql = new HQLQuery<Carga>(getEntityManager());
		hql.append("from Carga c ");
		hql.append("left join fetch c.canal canal");
		hql.append("inner join fetch c.rota ro");
		hql.append("inner join fetch ro.paisOrigem origem");
		hql.append("inner join fetch ro.paisDestino destino");
		hql.append("inner join fetch c.agenteCarga agente");

		if (filtro != null) {

			if (filtro.getAgente() != null) {
				hql.appendEqual("agente.id", filtro.getAgente());
			}
			if (filtro.getRota() != null) {
				hql.appendEqual("origem", filtro.getRota().getPaisOrigem());
				hql.appendEqual("destino", filtro.getRota().getPaisDestino());

			}

			hql.appendLike("c.hawb", filtro.getHawb());
			hql.appendLike("c.numAPS", filtro.getAps());

			hql.appendBetween("c.dtColeta", filtro.getDtInicioColeta(),
					filtro.getDtFimColeta());
			hql.appendBetween("c.dtConsolidacao", filtro.getDtConsolidacao(),
					filtro.getDtFimConsolidacao());

			hql.appendEqual("c.status", filtro.getStatus());
			if (filtro.getStatus() != null
					&& (filtro.getStatus().equals(StatusCarga.ITT) || filtro
							.getStatus().equals(StatusCarga.OHI))) {
				hql.appendEqual("c.cidadeAtual", filtro.getCidadeAtual());
			}

			hql.appendEqual("c.canal", filtro.getCanal());
		}

		hql.append("order by c.numAPS");

		return hql.getResultList();
	}

	@Override
	public List<Carga> listCargasPainelLogistica(BasicFiltroCarga filtro)
			throws BusinessException {

		HQLQuery<Carga> hql = new HQLQuery<Carga>(this.entityManager);
		hql.append("select distinct c from Carga c");
		hql.append("inner join fetch c.rota ro");
		hql.append("inner join fetch c.filial f");
		hql.append("inner join c.listaDeInvoices lo");

		if (filtro != null) {
			hql.appendBetween("c.dtPrevista", filtro.getDtInicioPrevista(),
					filtro.getDtFimPrevista());
			hql.appendEqual("c.status", filtro.getStatus());
			hql.appendLike("c.numAPS", filtro.getAps());
		}

		hql.appendNotEqual("c.status", StatusCarga.C);
		hql.appendNotEqual("c.status", StatusCarga.F);

		hql.append("order by c.numAPS, c.dtPrevista asc, c.status ");

		return hql.getResultList();
	}

	// Embarques Planejados

	@Override
	public List<Carga> listarEmbarquesPlenejados(BasicFiltroPlanejarCarga filtro)
			throws BusinessException {
		HQLQuery<Carga> hql = new HQLQuery<Carga>(entityManager);
		hql.append("select distinct c from Carga c");
		hql.append("inner join fetch c.filial filial");
		hql.append("inner join fetch c.agenteCarga a");
		hql.append("inner join fetch c.importador resp");

		if (filtro != null) {

			hql.appendEqual("c.status", filtro.getStatus() );
			
			if (filtro.getNumInvoice() != null
					&& !filtro.getNumInvoice().equals("")) {
				hql.append("inner join c.listaDeInvoices i");

			}

			if (filtro.getNumInvoice() != null
					&& !filtro.getNumInvoice().equals("")) {
				hql.appendEqual("i.numeroInvoice", filtro.getNumInvoice());

			}
			hql.appendBetween("c.dtConsolidacao", filtro.getPeriodoInicio(),
					filtro.getPeriodoFim());

			if (filtro.getAgenteCarga() != null) {
				hql.appendEqual("a", filtro.getAgenteCarga());

			}

			if (filtro.getResposavelUsuario() != null) {
				hql.appendEqual("resp", filtro.getResposavelUsuario());

			}

		}

		hql.append("order by c.filial.codigo, c.anoSequencia, c.numSequencia");
		return hql.getResultList();
	}

	@Override
	public List<Carga> listarCargasBroker(BasicFiltroCarga filtro)
			throws BusinessException {
		HQLQuery<Carga> hql = new HQLQuery<Carga>(getEntityManager());
		hql.append("select distinct c from Carga c");
		hql.append("inner join fetch c.filial filial");
		hql.append("inner join fetch c.procBroker broker");
		hql.append("left join fetch c.rota ro");

		hql.append("left join fetch c.agenteCarga agente");
		hql.append("left join fetch ro.paisOrigem origem");
		hql.append("left join fetch ro.paisDestino destino");

		hql.appendCondicao("broker is not null");

		if (filtro != null) {

			hql.appendLike("broker.nrDI", filtro.getNumeroDI());

			if (filtro.getAgenteCarga()!= null) {
				hql.appendEqual("agente", filtro.getAgenteCarga());
			}
			if (filtro.getRota() != null) {
				hql.appendEqual("origem", filtro.getRota().getPaisOrigem());
				hql.appendEqual("destino", filtro.getRota().getPaisDestino());

			}

			// hql.appendLike("c.hawb", filtro.getHawb());
			if (filtro.getHawb() != null && !filtro.getHawb().isEmpty()) {
				hql.appendCondicao(" lower(c.hawb) like lower(:hawb) ");
				hql.setParameter("hawb", filtro.getHawb() + "%");
			}

			hql.appendLike("c.numAPS", filtro.getAps());

			hql.appendBetween("c.dtColeta", filtro.getDtInicioColeta(),
					filtro.getDtFimColeta());
			hql.appendBetween("c.dtConsolidacao", filtro.getDtConsolidacao(),
					filtro.getDtFimConsolidacao());

			hql.appendEqual("c.status", filtro.getStatus());
			if (filtro.getStatus() != null
					&& (filtro.getStatus().equals(StatusCarga.ITT) || filtro
							.getStatus().equals(StatusCarga.OHI))) {
				hql.appendEqual("c.cidadeAtual", filtro.getCidadeAtual());
			}

			hql.appendEqual("c.canal", filtro.getCanal());

			// Filtro por numero de sequencia da invoice
			if (filtro.getProcesso() != null
					&& !filtro.getProcesso().equals("")) {

				String filial[] = { "" };
				String sequencia[] = { "" };
				String f = "";

				// Verificar se possui filial e sequencia
				if (filtro.getProcesso().contains(".")
						&& filtro.getProcesso().contains("/")) {
					filial = filtro.getProcesso().split("\\.");
					f = filial[0];
					hql.appendEqual("filial.codigo", f);

					if (filial.length > 1) {
						sequencia = filial[1].split("/");
						if (sequencia.length > 1) {
							if (!sequencia[0].equals(""))
								hql.appendEqual("c.numSequencia",
										Integer.parseInt(sequencia[0]), true);
							hql.appendEqual("c.anoSequencia",
									Integer.parseInt(sequencia[1]), true);
						}

					}

					// verificar apenas filial
				} else if (filtro.getProcesso().contains(".")
						&& !filtro.getProcesso().contains("/")) {
					filial = filtro.getProcesso().split("\\.");
					if (filial.length >= 1) {
						f = filial[0];
						hql.appendEqual("filial.codigo", f);
					}

					if (filial.length > 1) {
						sequencia[0] = filial[1];
						if (sequencia.length >= 1) {
							if (!sequencia[0].equals(""))
								hql.appendEqual("c.numSequencia",
										Integer.parseInt(sequencia[0]), true);
						}
					}
					// verificar apenas sequencia
				} else if (!filtro.getProcesso().contains(".")
						&& filtro.getProcesso().contains("/")) {

					sequencia = filtro.getProcesso().split("/");
					if (sequencia.length > 1) {
						if (!sequencia[0].equals("")) {
							hql.appendEqual("c.numSequencia",
									Integer.parseInt(sequencia[0]), true);
						}
						hql.appendEqual("c.anoSequencia",
								Integer.parseInt(sequencia[1]), true);

					} else {

						try {
							hql.appendCondicao("c.anoSequencia = :ano or c.numSequencia = :num");
							int ano = Integer.parseInt(sequencia[0]);
							if (sequencia[0].length() == 2) {
								ano = Integer.parseInt("20" + sequencia[0]);
							}

							hql.setParameter("ano", ano);
							hql.setParameter("num",
									Integer.parseInt(sequencia[0]));

						} catch (Exception e) {
							return new ArrayList<Carga>(0);
						}
					}

					// verificar em todos os registros: codigo, numero e ano de
					// sequencia
				} else {
					try {
						hql.appendCondicao("( c.anoSequencia = :ano or c.numSequencia = :num or filial.codigo =:codigo )");
						hql.setParameter("codigo", filtro.getProcesso());

						try {
							int ano = Integer.parseInt(filtro.getProcesso());
							if (filtro.getProcesso().length() == 2) {
								ano = Integer.parseInt("20"
										+ filtro.getProcesso());
							}

							hql.setParameter("ano", ano);
							hql.setParameter("num",
									Integer.parseInt(filtro.getProcesso()));
						} catch (Exception e) {
						}
					} catch (Exception e) {
						return new ArrayList<Carga>(0);
					}
				}
			}
		}
		hql.append("order by c.filial.codigo, c.anoSequencia, c.numSequencia ");

		return hql.getResultList();
	}

	@Override
	public Carga carregarCustos(Carga carga) throws BusinessException {

		if (carga.getCustosDi() != null) {
			for (CustoDI custoDI : carga.getCustosDi()) {
				Hibernate.initialize(custoDI);
				Hibernate.initialize(custoDI.getCustoImportacao());
				Hibernate.initialize(custoDI.getMoeda());
				Hibernate.initialize(custoDI.getFornecedor());
			}
		}

		return carga;
	}

	@Override
	public Carga alterarCusto(Carga carga) throws BusinessException {

		return super.alterar(carga);

	}

	@Override
	public List<Carga> listarLastUpdates(BasicFiltroCarga filtro) throws BusinessException {
		HQLQuery<Carga> hql = new HQLQuery<Carga>(this.entityManager);
		hql.append("select distinct c from Carga c");
		hql.append("inner join fetch c.rota ro");
		hql.append("left join fetch c.canal canal");
		hql.append("inner join fetch ro.paisOrigem origem");
		// hql.append("inner join c.listaDeInvoices lo");

		if (filtro != null) {
		//	hql.appendBetween("c.dtPrevista", filtro.getDtInicioPrevista(),	filtro.getDtFimPrevista());
		//	hql.appendEqual("c.status", filtro.getStatus());
			// hql.appendLike("c.numAPS", filtro.getAps());
			hql.appendEqual("c.visivel", filtro.getVisivel());

		}

		// capturando a data
//		Calendar c = Calendar.getInstance();
//		c.setTime(new Date());
//		c.add(Calendar.DATE, -1);
		hql.appendNotEqual("c.status", StatusCarga.C);

		// hql.appendNotEqual("dtChegada", StatusCarga.F);

		// hql.appendNotEqual("c.status", StatusCarga.AT);
		//hql.appendCondicao(" (c.dtChegada >= :dtChegada OR c.dtChegada is null )");
		// hql.setParameter("status", StatusCarga.AT);
		
		//hql.setParameter("dtChegada", c.getTime());
		
		hql.appendCondicao("c.lastAtualizacao is not null");
		
		hql.append("order by c.lastAtualizacao desc");
		
		List<Carga> cargas =  new ArrayList<Carga>();
		
				cargas = hql.getResultList();
		
		
		

				return cargas;
	}

	public HQLQuery<Carga> pesquisarPorProcesso( BasicFiltroCarga filtro, HQLQuery<Carga> hql ) {
		// Filtro por numero de sequencia da invoice
		if (filtro.getProcesso() != null
				&& !filtro.getProcesso().equals("")) {

			String filial[] = { "" };
			String sequencia[] = { "" };
			String f = "";

			// Verificar se possui filial e sequencia
			if (filtro.getProcesso().contains(".")
					&& filtro.getProcesso().contains("/")) {
				filial = filtro.getProcesso().split("\\.");
				f = filial[0];
				hql.appendEqual("filial.codigo", f);

				if (filial.length > 1) {
					sequencia = filial[1].split("/");
					if (sequencia.length > 1) {
						if (!sequencia[0].equals(""))
							hql.appendEqual("c.numSequencia",
									Integer.parseInt(sequencia[0]), true);
						hql.appendEqual("c.anoSequencia",
								Integer.parseInt(sequencia[1]), true);
					}

				}

				// verificar apenas filial
			} else if (filtro.getProcesso().contains(".")
					&& !filtro.getProcesso().contains("/")) {
				filial = filtro.getProcesso().split("\\.");
				if (filial.length >= 1) {
					f = filial[0];
					hql.appendEqual("filial.codigo", f);
				}

				if (filial.length > 1) {
					sequencia[0] = filial[1];
					if (sequencia.length >= 1) {
						if (!sequencia[0].equals(""))
							hql.appendEqual("c.numSequencia",
									Integer.parseInt(sequencia[0]), true);
					}
				}
				// verificar apenas sequencia
			} else if (!filtro.getProcesso().contains(".")
					&& filtro.getProcesso().contains("/")) {

				sequencia = filtro.getProcesso().split("/");
				if (sequencia.length > 1) {
					if (!sequencia[0].equals("")) {
						hql.appendEqual("c.numSequencia",
								Integer.parseInt(sequencia[0]), true);
					}
					hql.appendEqual("c.anoSequencia",
							Integer.parseInt(sequencia[1]), true);

				} else {

					try {
						hql.appendCondicao("c.anoSequencia = :ano or c.numSequencia = :num");
						int ano = Integer.parseInt(sequencia[0]);
						if (sequencia[0].length() == 2) {
							ano = Integer.parseInt("20" + sequencia[0]);
						}

						hql.setParameter("ano", ano);
						hql.setParameter("num",
								Integer.parseInt(sequencia[0]));

					} catch (Exception e) {
						return null;
					}
				}

				// verificar em todos os registros: codigo, numero e ano de
				// sequencia
			} else {
				try {
					hql.appendCondicao("( c.anoSequencia = :ano or c.numSequencia = :num or filial.codigo =:codigo )");
					hql.setParameter("codigo", filtro.getProcesso());

					try {
						int ano = Integer.parseInt(filtro.getProcesso());
						if (filtro.getProcesso().length() == 2) {
							ano = Integer.parseInt("20"
									+ filtro.getProcesso());
						}

						hql.setParameter("ano", ano);
						hql.setParameter("num",
								Integer.parseInt(filtro.getProcesso()));
					} catch (Exception e) {
					}
				} catch (Exception e) {
					return null;
				}
			}
		}
		
		return hql;
	}

	@Override
	public List<Carga> listarProcessosDI(BasicFiltroCarga filtro)
			throws BusinessException {
		
		HQLQuery<Carga> hql = new HQLQuery<Carga>( getEntityManager() );
		hql.append("select distinct c from Carga c");
		hql.append("inner join fetch c.filial filial");
		hql.append("inner join fetch c.procBroker broker");
		hql.append("left join fetch c.rota ro");

		hql.append("left join fetch c.agenteCarga agente");
		hql.append("left join fetch ro.paisOrigem origem");
		hql.append("left join fetch ro.paisDestino destino");

		hql.appendCondicao("broker is not null");

		if (filtro != null) {
			hql.appendLike("broker.nrDI", filtro.getNumeroDI());
		}
		
		
		return hql.getResultList();
	}
	
}
