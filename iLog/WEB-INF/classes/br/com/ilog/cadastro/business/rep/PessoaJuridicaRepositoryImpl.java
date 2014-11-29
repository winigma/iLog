package br.com.ilog.cadastro.business.rep;

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
import br.com.ilog.cadastro.business.entity.Endereco;
import br.com.ilog.cadastro.business.entity.PessoaJuridica;
import br.com.ilog.cadastro.business.entity.TipoPessoaEnum;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroPessoaJuridica;

@Component
public class PessoaJuridicaRepositoryImpl extends
		CrudRepositoryJPA<PessoaJuridica> implements PessoaJuridicaRepository {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public PessoaJuridica cadastrar(PessoaJuridica pessoa)
			throws BusinessException {
		
		pessoa.setAtivo( true );
		if (pessoa.getId() == null) {
			getEntityManager().persist(pessoa);
		}
		getEntityManager().flush();
		return pessoa;
	}

	@Override
	public PessoaJuridica getById(Serializable primaryKey) {
		PessoaJuridica pessoa = super.getById(primaryKey);

		Hibernate.initialize(pessoa.getTiposPessoa());

		Hibernate.initialize(pessoa.getEnderecos());
		for (Endereco end : pessoa.getEnderecos()) {
			Hibernate.initialize(end.getPais());
			Hibernate.initialize(end.getCidade());

		}
		Hibernate.initialize(pessoa.getContatos());

		return pessoa;
	}

	@Override
	public EntityManager getEntityManager() {

		return this.entityManager;
	}

	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;

	}

	@Override
	public List<PessoaJuridica> listarPessoasByType(
			BasicFiltroPessoaJuridica filtro) throws BusinessException {
		HQLQuery<PessoaJuridica> hql = new HQLQuery<PessoaJuridica>(
				entityManager);
		hql.append("select distinct pj from PessoaJuridica pj");
		hql.append("left join fetch pj.tiposPessoa pessoa");
		if (filtro != null) {

			if (filtro.getPessoa() != null) {
				hql.appendEqual("pessoa.tipo", filtro.getPessoa().getTipo());
			}

			if (filtro.getNomeFantasia() != null) {
				hql.appendCondicao(" lower(pj.nomeFantasia) like lower(:nomefantasia) ");
				hql.setParameter("nomefantasia", filtro.getNomeFantasia() + "%");

			}
			if (filtro.getRazaoSocial() != null) {
				hql.appendCondicao(" lower(pj.razaoSocial) like lower(:razaosocial) ");
				hql.setParameter("razaosocial", filtro.getRazaoSocial() + "%");

			}
			// hql.appendLike( "pj.nomeFantasia", filtro.getNomeFantasia() );
			// hql.appendLike( "pj.razaoSocial", filtro.getRazaoSocial() );
			hql.appendEqual("pj.vendorSap", filtro.getVendorSap());
			hql.appendEqual("pj.ativo", filtro.getAtivo());
		}
		hql.append("order by pj.nomeFantasia");
		return hql.getResultList();
	}

	@Override
	public List<PessoaJuridica> listaPessoasByFilter(
			BasicFiltroPessoaJuridica filtro) throws BusinessException {
		HQLQuery<PessoaJuridica> hql = new HQLQuery<PessoaJuridica>(
				entityManager);
		hql.append("select distinct pj from PessoaJuridica pj");
		hql.append("inner join fetch pj.tiposPessoa p");

		if (filtro.getPessoa() != null) {
			if (filtro.getPessoa().getTipo() != null) {
				hql.appendEqual("p.tipo", filtro.getPessoa().getTipo());
			}
			hql.appendEqual("pj.ativo", filtro.getAtivo());
		}

		hql.append("order by pj.nomeFantasia");

		return hql.getResultList();
	}

	@Override
	public List<PessoaJuridica> listarAllFornecedores(String fornecedor)
			throws BusinessException {
		HQLQuery<PessoaJuridica> person = new HQLQuery<PessoaJuridica>(
				entityManager);
		person.append("from PessoaJuridica pj");
		person.append("inner join fetch pj.tiposPessoa pessoa");
		person.appendEqual("pessoa.tipo", fornecedor);
		return person.getResultList();
	}

	@Override
	public List<PessoaJuridica> listarPessoasByTipo(TipoPessoaEnum tipo)
			throws BusinessException {

		HQLQuery<PessoaJuridica> hql = new HQLQuery<PessoaJuridica>(
				entityManager);
		hql.append("from PessoaJuridica pj");
		if (tipo != null) {
			hql.append("inner join fetch pj.tiposPessoa p");
			hql.appendEqual("p.tipo", tipo.name());
		}
		hql.appendEqual("pj.ativo", true);
		hql.append("order by pj.nomeFantasia");
		return hql.getResultList();
	}

	@Override
	public List<PessoaJuridica> listarAllFornecedoresByStatus(Boolean status)
			throws BusinessException {

		HQLQuery<PessoaJuridica> hql = new HQLQuery<PessoaJuridica>(
				getEntityManager());
		hql.append("select distinct pj from PessoaJuridica pj");
		hql.append("inner join fetch pj.tiposPessoa p");
		// hql.appendEqual("p.tipo", TipoPessoaEnum.FORNECEDOR.name());

		if (status != null)
			hql.appendEqual("pj.ativo", status);

		hql.append("order by pj.nomeFantasia");

		return hql.getResultList();
	}

	@Override
	public List<PessoaJuridica> listarAllImportadoresByStatus(Boolean status)
			throws BusinessException {

		HQLQuery<PessoaJuridica> hql = new HQLQuery<PessoaJuridica>(
				getEntityManager());
		hql.append("from PessoaJuridica pj");
		hql.append("inner join fetch pj.tiposPessoa p");
		// hql.appendEqual("p.tipo", TipoPessoaEnum.IMPORTADOR.name());
		hql.appendEqual("pj.ativo", status);
		hql.append("order by pj.nomeFantasia");

		return hql.getResultList();
	}

	@Override
	public PessoaJuridica getPessoaBySap(BasicFiltroPessoaJuridica filtro)
			throws BusinessException {
		try{
			
		HQLQuery<PessoaJuridica> hql = new HQLQuery<PessoaJuridica>(
				entityManager);
		hql.append("from PessoaJuridica pj");
		if (filtro != null) {
			hql.appendEqual("pj.vendorSap", filtro.getVendorSap());
		}
		return hql.getSingleResult();
		}catch (NoResultException e) {
			return null;
		}catch (Exception e) {
			return null;
		}
	}

	@Override
	public PessoaJuridica getPessoaJuridicaBySapVendor(String s)
			throws BusinessException {
		HQLQuery<PessoaJuridica> hql = new HQLQuery<PessoaJuridica>(
				entityManager);
		hql.append("from PessoaJuridica pj");
		Long i = Long.parseLong(s);
		hql.appendEqual("pj.vendorSap", i);
		return hql.getSingleResult();
	}
}
