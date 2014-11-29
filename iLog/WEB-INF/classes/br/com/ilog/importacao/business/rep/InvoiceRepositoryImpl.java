package br.com.ilog.importacao.business.rep;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.aspectj.weaver.patterns.ParserException;
import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.cits.commons.citsbusiness.util.HQLQuery;
import br.com.ilog.importacao.business.entity.AnexoInvoice;
import br.com.ilog.importacao.business.entity.Carga;
import br.com.ilog.importacao.business.entity.Invoice;
import br.com.ilog.importacao.business.entity.StatusInvoice;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroCarga;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroInvoice;

@Component
public class InvoiceRepositoryImpl extends CrudRepositoryJPA<Invoice> implements
		InvoiceRepository {

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
	public List<Invoice> listarInvoices(BasicFiltroInvoice filtro)
			throws BusinessException {
		HQLQuery<Invoice> hql = new HQLQuery<Invoice>(entityManager);
		hql.append("from Invoice inv");
		hql.append("left join fetch inv.exportador fornecedor");
		hql.append("left join fetch inv.paisOrigem origem");
		hql.append("left join fetch inv.comprador comprador");
		hql.append("left join fetch inv.terminal terminal");
		hql.append("left join fetch inv.incoterm incoterm");
		hql.append("left join fetch inv.carga carga");
		hql.append("left join fetch carga.filial filial");
		hql.append("left join fetch carga.rota rota");
		
		hql.append("left join fetch rota.tipoTransporte modal");
		

		if (filtro != null) {
			
			hql.appendEqual("inv.status", filtro.getStatusInvoice());
			if (filtro.getStatusInvoice() != null
					&& (filtro.getStatusInvoice().equals(StatusInvoice.ITT) || filtro
							.getStatusInvoice().equals(StatusInvoice.OHI))) {
				hql.appendEqual("c.cidadeAtual", filtro.getCidadeAtual());
			}

			hql.appendLike("inv.numeroInvoice", filtro.getNumeroInvoice());

			hql.appendEqual("origem", filtro.getOrigem());
			hql.appendEqual("comprador", filtro.getComprador());
			hql.appendEqual("fornecedor", filtro.getFornecedor());
			hql.appendEqual("filial", filtro.getFilial());
			hql.appendEqual("moeda", filtro.getMoeda());
			if(filtro.getModal() != null){
				hql.appendEqual("modal", filtro.getModal());
			}
			hql.appendEqual("incoterm", filtro.getIncoterm());
			hql.appendEqual("terminal", filtro.getTerminal());

			hql.appendBetween("inv.dtEmissao", filtro.getDataInicio(),filtro.getDataFim());
			if (filtro.getDataInicio() != null && filtro.getDataFim() != null) {
				hql.appendCondicao("inv.dtEmissao >= :dataInicio and inv.dtEmissao <= :dataFim");
				hql.setParameter("dataInicio", filtro.getDataInicio());
				hql.setParameter("dataFim", filtro.getDataFim());
			}

			if (filtro.getNumeroSeq() != null) {
				filtro.setNumeroSeq(filtro.getNumeroSeq().trim());

				// Filtro por numero de sequencia da invoice
				if (!filtro.getNumeroSeq().equals("")) {
					String aux[] = filtro.getNumeroSeq().split("/");
					try {
						if (aux[0] != null) {
							if (aux.length > 1) {
								if (!aux[0].equals(""))
									hql.appendEqual("inv.numSequencia",
											Integer.parseInt(aux[0]));
								hql.appendEqual("inv.anoSequencia",
										Integer.parseInt(aux[1]));
							} else {
								hql.appendCondicao("inv.anoSequencia = :ano or inv.numSequencia = :num");
								hql.setParameter("ano",
										Integer.parseInt(aux[0]));
								hql.setParameter("num",
										Integer.parseInt(aux[0]));
							}
						}
					} catch (NumberFormatException e) {
						return new ArrayList<Invoice>(0);

					} catch (ParserException e) {
						return new ArrayList<Invoice>(0);
					}
				}
			}

		}
		return hql.getResultList();
	}

	@Override
	public List<Invoice> listarInvoicesOfValidation(BasicFiltroInvoice filtro)
			throws BusinessException {
		HQLQuery<Invoice> hql = new HQLQuery<Invoice>(entityManager);
		hql.append("from Invoice inv");
		hql.append("inner join fetch inv.exportador fornecedor");
		hql.append("inner join fetch inv.paisOrigem origem");
		hql.append("inner join fetch inv.comprador comprador");

		List<StatusInvoice> validacao = new ArrayList<StatusInvoice>();
		validacao.add(StatusInvoice.C);
		validacao.add(StatusInvoice.EV);
		validacao.add(StatusInvoice.NA);
		validacao.add(StatusInvoice.V);
		validacao.add(StatusInvoice.R);

		hql.appendIn("inv.status", validacao);

		if (filtro != null) {
			if (filtro.getNumeroInvoice() != null
					&& !filtro.getNumeroInvoice().equals("")) {
				hql.appendLike("inv.numeroInvoice", filtro.getNumeroInvoice());
			}
			if (filtro.getPais() != null) {
				hql.appendEqual("origem.id", filtro.getPais().getId());
			}
			if (filtro.getFornecedor() != null) {
				hql.appendEqual("fornecedor.id", filtro.getFornecedor().getId());
			}
			if (filtro.getUser() != null) {
				hql.appendEqual("comprador.id", filtro.getUser().getId());
			}
			if (filtro.getStatusInvoice() != null) {
				hql.appendEqual("inv.status", filtro.getStatusInvoice());
			}
			if (filtro.getDataInicio() != null && filtro.getDataFim() != null) {
				hql.appendCondicao("inv.dtEmissao >= :dataInicio and inv.dtEmissao <= :dataFim");
				hql.setParameter("dataInicio", filtro.getDataInicio());
				hql.setParameter("dataFim", filtro.getDataFim());
			}
			// Filtro por numero de sequencia da invoice
			if (filtro.getNumeroSeq() != null) {
				if (!filtro.getNumeroSeq().isEmpty()
						&& !filtro.getNumeroSeq().equals("")) {
					String aux[] = filtro.getNumeroSeq().split("/");
					try {
						if (aux[0] != null) {
							if (aux.length > 1) {
								if (!aux[0].equals(""))
									hql.appendEqual("inv.numSequencia",
											Integer.parseInt(aux[0]));
								hql.appendEqual("inv.anoSequencia",
										Integer.parseInt(aux[1]));
							} else {
								hql.appendCondicao("inv.anoSequencia = :ano or inv.numSequencia = :num");
								hql.setParameter("ano",
										Integer.parseInt(aux[0]));
								hql.setParameter("num",
										Integer.parseInt(aux[0]));
							}
						}
					} catch (NumberFormatException e) {
						return new ArrayList<Invoice>(0);

					} catch (ParserException e) {
						return new ArrayList<Invoice>(0);
					}
				}
			}

		}
		hql.append("order by (case inv.status" + " when 'NA' then 1"
				+ " when 'C' then 2" + " when 'R' then 3"
				+ " when 'EV' then 4 else 5 end) , inv.dtEmissao asc");

		return hql.getResultList();
	}

	@Override
	public Invoice getById(Serializable id) {

		Invoice invoice = super.getById(id);
		return invoice;
	}

	@Override
	public List<Invoice> listarInvoicesOfApproved(BasicFiltroInvoice filtro)
			throws BusinessException {
		HQLQuery<Invoice> hql = new HQLQuery<Invoice>(entityManager);
		hql.append("from Invoice inv");
		hql.append("inner join fetch inv.exportador fornecedor");
		hql.append("inner join fetch inv.paisOrigem origem");
		hql.append("inner join fetch inv.comprador comprador");

		List<StatusInvoice> validacao = new ArrayList<StatusInvoice>();
		validacao.add(StatusInvoice.A);
		validacao.add(StatusInvoice.V);
		validacao.add(StatusInvoice.R);
		hql.appendIn("inv.status", validacao);

		if (filtro != null) {
			if (filtro.getNumeroInvoice() != null
					&& !filtro.getNumeroInvoice().equals("")) {
				hql.appendLike("inv.numeroInvoice", filtro.getNumeroInvoice());
			}
			if (filtro.getPais() != null) {
				hql.appendEqual("origem.id", filtro.getPais().getId());
			}
			if (filtro.getFornecedor() != null) {
				hql.appendEqual("fornecedor.id", filtro.getFornecedor().getId());
			}
			if (filtro.getUser() != null) {
				hql.appendEqual("comprador.id", filtro.getUser().getId());
			}
			if (filtro.getStatusInvoice() != null) {
				hql.appendEqual("inv.status", filtro.getStatusInvoice());
			}
			if (filtro.getDataInicio() != null && filtro.getDataFim() != null) {
				hql.appendCondicao("inv.dtEmissao >= :dataInicio and inv.dtEmissao <= :dataFim");
				hql.setParameter("dataInicio", filtro.getDataInicio());
				hql.setParameter("dataFim", filtro.getDataFim());

			}
			// Filtro por numero de sequencia da invoice
			if (!filtro.getNumeroSeq().equals("")) {
				String aux[] = filtro.getNumeroSeq().split("/");
				try {
					if (aux[0] != null) {
						if (aux.length > 1) {
							if (!aux[0].equals(""))
								hql.appendEqual("inv.numSequencia",
										Integer.parseInt(aux[0]));
							hql.appendEqual("inv.anoSequencia",
									Integer.parseInt(aux[1]));
						} else {
							hql.appendCondicao("inv.anoSequencia = :ano or inv.numSequencia = :num");
							hql.setParameter("ano", Integer.parseInt(aux[0]));
							hql.setParameter("num", Integer.parseInt(aux[0]));
						}
					}
				} catch (NumberFormatException e) {
					return new ArrayList<Invoice>(0);

				} catch (ParserException e) {
					return new ArrayList<Invoice>(0);
				}
			}
		}
		hql.append("order by (case inv.status" + " when 'R' then 1"
				+ " when 'V' then 2" + " when 'A' then 3"
				+ " else 4 end) , inv.dtEmissao asc");
		return hql.getResultList();
	}

	@Override
	public List<Invoice> listarInvoicesPorCarga(Carga carga)
			throws BusinessException {
		HQLQuery<Invoice> hql = new HQLQuery<Invoice>(entityManager);
		hql.append("select distinct inv from Invoice inv");
		hql.append("inner join fetch inv.carga carga");
		hql.append("left join fetch inv.exportador fornecedor");

		hql.append("left join fetch inv.paisOrigem origem");
		hql.append("left join fetch inv.comprador comprador");

		if (carga != null) {
			hql.appendEqual("carga.id", carga.getId());
		}
		return hql.getResultList();
	}

	@Override
	public List<Invoice> listarInvoicesPorCarga(Carga carga,
			boolean carregarItens) throws BusinessException {
		HQLQuery<Invoice> hql = new HQLQuery<Invoice>(entityManager);
		hql.append("select distinct inv from Invoice inv");
		hql.append("inner join fetch inv.carga carga");
		hql.append("inner join fetch inv.exportador fornecedor");

		if (carregarItens) {
			hql.append("left join fetch inv.itensInvoice item");
		}

		hql.append("inner join fetch inv.paisOrigem origem");
		hql.append("inner join fetch inv.comprador comprador");

		if (carga != null) {
			hql.appendEqual("carga.id", carga.getId());
		}

		hql.append("order by inv.numeroInvoice ");

		return hql.getResultList();
	}

	/**
	 * Metodo para remover um anexoInvoice
	 * 
	 * @param anexo
	 * @exception BusinessException
	 */
	@Override
	public void removerAnexoInvoice(AnexoInvoice anexo)
			throws BusinessException {

		anexo = getEntityManager().find(AnexoInvoice.class, anexo.getId());
		getEntityManager().remove(anexo);

	}

	@Override
	public Integer getLastCodeByYear(int ano) throws BusinessException {

		HQLQuery<Integer> hql = new HQLQuery<Integer>(getEntityManager());

		hql.append("select MAX(i.numSequencia) from Invoice i");
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
	public List<AnexoInvoice> getAnexosInvoice(Invoice invoice)
			throws BusinessException {
		if (invoice != null) {
			HQLQuery<AnexoInvoice> hql = new HQLQuery<AnexoInvoice>(
					getEntityManager());
			hql.append("from AnexoInvoice anexo");
			hql.appendEqual("anexo.invoice", invoice);

			return hql.getResultList();
		}

		return new ArrayList<AnexoInvoice>(0);
	}

	@Override
	public List<Invoice> listarInvoices(BasicFiltroCarga filtro)
			throws BusinessException {
		HQLQuery<Invoice> hql = new HQLQuery<Invoice>(getEntityManager());
		hql.append("from Invoice i ");
		hql.append("left join fetch i.carga c");
		hql.append("left join fetch c.canal canal");
		hql.append("left join fetch c.rota ro");
		hql.append("left join fetch ro.paisOrigem origem");
		hql.append("left join fetch ro.paisDestino destino");
		hql.append("left join fetch c.agenteCarga agente");

		if (filtro != null) {

			if (filtro.getAgente() != null) {
				hql.appendEqual("agente.id", filtro.getAgente());
			}
			if (filtro.getRota() != null) {
				hql.appendEqual("origem", filtro.getRota().getPaisOrigem());
				hql.appendEqual("destino", filtro.getRota().getPaisDestino());

			}

			hql.appendLike("c.hawb", filtro.getHawb());

			hql.appendEqual("i.critico", filtro.getCritico());
			hql.appendBetween("c.dtColeta", filtro.getDtInicioColeta(),
					filtro.getDtFimColeta());

			hql.appendBetween("c.dtPrevista", filtro.getDtInicioPrevista(),
					filtro.getDtFimPrevista());

			hql.appendBetween("c.dtChegada", filtro.getDtInicioChegada(),
					filtro.getDtFimChegada());

			hql.appendBetween("c.dtConsolidacao", filtro.getDtConsolidacao(),
					filtro.getDtFimConsolidacao());

			hql.appendEqual("c.importador", filtro.getResponsavel());

			hql.appendEqual("i.status", filtro.getStatusInvoice());
			if (filtro.getStatusInvoice() != null
					&& (filtro.getStatusInvoice().equals(StatusInvoice.ITT) || filtro
							.getStatusInvoice().equals(StatusInvoice.OHI))) {
				hql.appendEqual("c.cidadeAtual", filtro.getCidadeAtual());
			}
			
			if ( filtro.getProcesso() != null && !filtro.getProcesso().equals("") ) {
				
				String filial[]= {""};
				String sequencia[] = {""};
				String f = "";
				
				//Verificar se possui filial e sequencia
				if ( filtro.getProcesso().contains(".") && filtro.getProcesso().contains("/") ) {
					filial = filtro.getProcesso().split("\\.");
					f = filial[0];
					hql.appendEqual("filial.codigo", f );
					
					if ( filial.length > 1 ) {
						sequencia = filial[1].split("/");
						if (sequencia.length > 1) {
							if (!sequencia[0].equals(""))
								hql.appendEqual( "c.numSequencia", Integer.parseInt(sequencia[0]), true );
							hql.appendEqual( "c.anoSequencia", Integer.parseInt(sequencia[1]), true );
						}
						
					}
					
					//verificar apenas filial
				} else if ( filtro.getProcesso().contains(".") && !filtro.getProcesso().contains("/") ) {
					filial = filtro.getProcesso().split("\\.");
					if ( filial.length >=  1 ) {
						f = filial[0];
						hql.appendEqual("filial.codigo", f );
					}
					
					if ( filial.length > 1 ) {
						sequencia[0] = filial[1];
						if (sequencia.length >= 1) {
							if (!sequencia[0].equals(""))
								hql.appendEqual( "c.numSequencia", Integer.parseInt(sequencia[0]), true );
						}
					}
					//verificar apenas sequencia
				} else  if ( !filtro.getProcesso().contains(".") && filtro.getProcesso().contains("/") ) {
					
					sequencia = filtro.getProcesso().split( "/" );
					if (sequencia.length > 1) {
						if (!sequencia[0].equals("")){
							hql.appendEqual( "c.numSequencia", Integer.parseInt(sequencia[0]), true );
						}
						hql.appendEqual( "c.anoSequencia", Integer.parseInt(sequencia[1]), true );
						
					} else {
						
						try {
							hql.appendCondicao("c.anoSequencia = :ano or c.numSequencia = :num");
							int ano = Integer.parseInt( sequencia[0] );
							if ( sequencia[0].length() == 2 ) {
								ano = Integer.parseInt( "20" + sequencia[0] );
							}
							
							hql.setParameter("ano", ano );
							hql.setParameter("num", Integer.parseInt(sequencia[0]));
							
						} catch (Exception e) {
							return new ArrayList<Invoice>(0);
						}
					}
					
					//verificar em todos os registros: codigo, numero e ano de sequencia
				} else {
					try {
						hql.appendCondicao("( c.anoSequencia = :ano or c.numSequencia = :num or filial.codigo =:codigo )");
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
						return new ArrayList<Invoice>(0);
					}
				}
			}
			
			hql.appendEqual("c.canal", filtro.getCanal());
		}

		hql.append("order by c.id");

		return hql.getResultList();
	}
	
	@Override
	public List<Invoice> listarInvoicesComCarga(BasicFiltroInvoice filtro) throws BusinessException{
		HQLQuery<Invoice> hql = new HQLQuery<Invoice>(entityManager);
		hql.append("from Invoice inv");
		hql.append("left join fetch inv.exportador fornecedor");
		hql.append("left join fetch inv.paisOrigem origem");
		hql.append("left join fetch inv.comprador comprador");
		hql.append("left join fetch inv.terminal terminal");
		hql.append("left join fetch inv.incoterm incoterm");
		hql.append("left join fetch inv.carga carga");
		hql.append("left join fetch carga.filial filial");
		hql.append("left join fetch carga.rota rota");
		hql.append("left join fetch rota.tipoTransporte modal");
	if (filtro != null) {
			
			hql.appendEqual("inv.status", filtro.getStatusInvoice());
			if (filtro.getStatusInvoice() != null
					&& (filtro.getStatusInvoice().equals(StatusInvoice.ITT) || filtro
							.getStatusInvoice().equals(StatusInvoice.OHI))) {
				hql.appendEqual("c.cidadeAtual", filtro.getCidadeAtual());
			}

			hql.appendLike("inv.numeroInvoice", filtro.getNumeroInvoice());

			hql.appendEqual("origem", filtro.getOrigem());
			hql.appendEqual("comprador", filtro.getComprador());
			hql.appendEqual("fornecedor", filtro.getFornecedor());
			hql.appendEqual("filial", filtro.getFilial());
			hql.appendEqual("moeda", filtro.getMoeda());
			if(filtro.getModal() != null){
				hql.appendEqual("modal", filtro.getModal());
			}
			hql.appendEqual("incoterm", filtro.getIncoterm());
			hql.appendEqual("terminal", filtro.getTerminal());

			hql.appendBetween("inv.dtEmissao", filtro.getDataInicio(),filtro.getDataFim());
			if (filtro.getDataInicio() != null && filtro.getDataFim() != null) {
				hql.appendCondicao("inv.dtEmissao >= :dataInicio and inv.dtEmissao <= :dataFim");
				hql.setParameter("dataInicio", filtro.getDataInicio());
				hql.setParameter("dataFim", filtro.getDataFim());
			}

			if (filtro.getNumeroSeq() != null) {
				filtro.setNumeroSeq(filtro.getNumeroSeq().trim());

				// Filtro por numero de sequencia da invoice
				if (!filtro.getNumeroSeq().equals("")) {
					String aux[] = filtro.getNumeroSeq().split("/");
					try {
						if (aux[0] != null) {
							if (aux.length > 1) {
								if (!aux[0].equals(""))
									hql.appendEqual("inv.numSequencia",
											Integer.parseInt(aux[0]));
								hql.appendEqual("inv.anoSequencia",
										Integer.parseInt(aux[1]));
							} else {
								hql.appendCondicao("inv.anoSequencia = :ano or inv.numSequencia = :num");
								hql.setParameter("ano",
										Integer.parseInt(aux[0]));
								hql.setParameter("num",
										Integer.parseInt(aux[0]));
							}
						}
					} catch (NumberFormatException e) {
						return new ArrayList<Invoice>(0);

					} catch (ParserException e) {
						return new ArrayList<Invoice>(0);
					}
				}
			}

		}
		hql.append("order by carga.id");
		
		return hql.getResultList();
	}

	@Override
	public List<Invoice> listarInvoicesPorCargaPlanejada(Carga carga)
			throws BusinessException {
		HQLQuery<Invoice> hql = new HQLQuery<Invoice>(entityManager);
		hql.append("from Invoice inv");
		hql.append("inner join fetch inv.carga carga");

	

		if (carga != null) {
			hql.appendEqual("carga.id", carga.getId());
		}
		return hql.getResultList();
	}

	@Override
	public Invoice getByNumero(String nrInvoice) throws BusinessException {
		
		try {
			if (nrInvoice != null) {
				HQLQuery<Invoice> hql = new HQLQuery<Invoice>(entityManager);
				hql.append("from Invoice inv");
				hql.appendEqual("inv.numeroInvoice", nrInvoice);

				return hql.getSingleResult();
			}
		} catch (NoResultException e) {
		}
		
		
		return null;
	}
}
