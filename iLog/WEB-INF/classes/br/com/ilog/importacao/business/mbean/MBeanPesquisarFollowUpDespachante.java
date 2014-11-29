package br.com.ilog.importacao.business.mbean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citspresentation.converter.EntityConverter;
import br.cits.commons.citspresentation.mbeans.AbstractPaginacao;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.Cidade;
import br.com.ilog.cadastro.business.entity.Pais;
import br.com.ilog.cadastro.business.entity.PessoaJuridica;
import br.com.ilog.cadastro.business.entity.Rota;
import br.com.ilog.cadastro.business.entity.Status;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroCidade;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;
import br.com.ilog.importacao.business.entity.Carga;
import br.com.ilog.importacao.business.entity.StatusCarga;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroCarga;
import br.com.ilog.importacao.business.facade.ImportacaoFacade;
import br.com.ilog.seguranca.business.entity.StatusUsuario;
import br.com.ilog.seguranca.business.entity.Usuario;
import br.com.ilog.seguranca.business.facade.SegurancaFacade;
import br.com.ilog.seguranca.presentation.mbean.MBeanSessaoUsuario;

/**
 * 
 * @author Manoel Neto
 * @date 10/12/2012
 *
 */
@Controller("mBeanPesquisarFollowUpDespachante")
@AccessScoped
public class MBeanPesquisarFollowUpDespachante extends AbstractPaginacao {

	/**  */
	private static final long serialVersionUID = -3315394141953827954L;

	@Resource(name = "controllerImportacao")
	ImportacaoFacade facade;

	@Resource(name = "controleUsuario")
	SegurancaFacade segurancaFacade;

	@Resource(name = "controllerCadastro")
	CadastroFacade cadastro;

	@Resource(name = "mBeanSessaoUsuario")
	MBeanSessaoUsuario sessaoUsuario;

	private List<SelectItem> comboStatus;

	private List<Pais> comboPaises;

	private EntityConverter<Pais> converterPais;

	private List<SelectItem> comboCidades;
	private EntityConverter<Cidade> cidadeConverter;

	private List<Usuario> importadores;
	private EntityConverter<Usuario> importadoresConverter;

	private List<PessoaJuridica> exportadores;
	private EntityConverter<PessoaJuridica> exportadoresConverter;

	private List<SelectItem> ativo;
	/**
	 * Filtro de pesquisa.
	 */
	private BasicFiltroCarga filtro;

	/**
	 * resultado da pesquisa.
	 */
	private List<Carga> result;

	private static Logger logger = LoggerFactory
			.getLogger(MBeanPesquisarFollowUpDespachante.class);

	@PostConstruct
	public void inicializar() {
		filtro = new BasicFiltroCarga();
		filtro.setRota(new Rota());

		result = new ArrayList<Carga>();

		popularComboPaises();
		popularComboImportadores();
		popularComboExportadores();
		popularCidades();

		ativo = new ArrayList<SelectItem>();
		try {
			FacesContext fc = FacesContext.getCurrentInstance();

			for (Status status : Status.values()) {
				Boolean aux;
				if (status.toString().equals("ATIVO"))
					aux = true;
				else
					aux = false;
				ativo.add(new SelectItem(aux, TemplateMessageHelper.getMessage(
						MensagensSistema.FOLLOW_UP, status.toString(), fc
								.getViewRoot().getLocale())));
			}
			
			doPesquisar(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	private void popularComboExportadores() {
		exportadores = new ArrayList<PessoaJuridica>();

		try {
			List<PessoaJuridica> pjs = cadastro
					.listarAllFornecedoresByStatus(null);
			if (pjs != null)
				exportadores = pjs;
			exportadoresConverter = new EntityConverter<PessoaJuridica>(pjs);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	private void popularComboImportadores() {
		importadores = new ArrayList<Usuario>();

		try {
			importadores = segurancaFacade
					.listarImportadores(StatusUsuario.A);

			// importadoresConverter = new EntityConverter<Usuario>(users);

		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doPesquisar(ActionEvent arg0) {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			
			result = facade.listCargasFollowUp(filtro);

			if (result.isEmpty()) {
				String message = TemplateMessageHelper.getMessage(
						MensagensSistema.SISTEMA, "MSG008", fc.getViewRoot()
								.getLocale());
				Messages.adicionaMensagemDeInfo(message);
			}

		} catch (BusinessException e1) {
			logger.error("error: {} " + e1);
			e1.printStackTrace();
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, e1));
		}

	}

	public void popularComboStatus() {
		FacesContext fc = FacesContext.getCurrentInstance();

		comboStatus = new ArrayList<SelectItem>();
		for (StatusCarga status : StatusCarga.getValores()) {
			comboStatus.add(new SelectItem(status, TemplateMessageHelper
					.getMessage(MensagensSistema.CARGA, status.name(), fc
							.getViewRoot().getLocale())));
		}

	}

	public void popularComboPaises() {
		comboPaises = new ArrayList<Pais>();
		try {
			comboPaises = cadastro.listarPaises(null);

			// converterPais = new EntityConverter<Pais>( paises );

		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Popular o combo de cidades.
	 */
	public void popularCidades() {
		comboCidades = new ArrayList<SelectItem>();
		try {
			List<Cidade> cidades = cadastro
					.listarCidades(new BasicFiltroCidade());

			for (Cidade cidade : cidades) {
				comboCidades.add(new SelectItem(cidade, cidade.getSigla()));
			}
			cidadeConverter = new EntityConverter<Cidade>(cidades);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return
	 */
	public boolean carregarCidades() {

		if (StatusCarga.ITT.equals(filtro.getStatus())
				|| StatusCarga.OHI.equals(filtro.getStatus()))
			return true;

		filtro.setCidadeAtual(new Cidade());
		return false;
	}

	@Override
	public int getTotalRegistros() {
		if (result != null) {
			return result.size();
		}
		return 0;
	}

	/**
	 * Retorna a Cidade onde se encontra a carga, dependendo do Status
	 * 
	 * @param index
	 * @return
	 */
	public String getCidadeAtual(int index) {

		Carga carg = result.get(index);
		if (carg.getStatus() != null) {
			if (carg.getStatus().equals(StatusCarga.OHI)
					|| carg.getStatus().equals(StatusCarga.ITT)) {
				if (carg.getCidadeAtual() != null) {
					return carg.getCidadeAtual().getSigla().toUpperCase();
				}
			}
		}

		return "";
	}

	@Override
	public void limpar() {
		
		filtro = new BasicFiltroCarga();
		filtro.setRota(new Rota());

		result = new ArrayList<Carga>();
		
	}

	@Override
	public void refazerPesquisa() {
		if (filtro == null)
			filtro = new BasicFiltroCarga();
		// Se a lista estava vazia antes não é necessário
		// fazer uma nova pesquisa
		if (result.isEmpty())
			return;

		doPesquisar(null);
	}

	public BasicFiltroCarga getFiltro() {
		return filtro;
	}

	public void setFiltro(BasicFiltroCarga filtro) {
		this.filtro = filtro;
	}

	public List<Carga> getResult() {
		return result;
	}

	public void setResult(List<Carga> result) {
		this.result = result;
	}

	public List<SelectItem> getComboStatus() {
		popularComboStatus();
		return comboStatus;
	}

	public void setComboStatus(List<SelectItem> comboStatus) {
		this.comboStatus = comboStatus;
	}

	public List<Pais> getComboPaises() {
		return comboPaises;
	}

	public void setComboPaises(List<Pais> comboPaises) {
		this.comboPaises = comboPaises;
	}

	public EntityConverter<Pais> getConverterPais() {
		return converterPais;
	}

	public void setConverterPais(EntityConverter<Pais> converterPais) {
		this.converterPais = converterPais;
	}

	public List<SelectItem> getComboCidades() {
		return comboCidades;
	}

	public void setComboCidades(List<SelectItem> comboCidades) {
		this.comboCidades = comboCidades;
	}

	public EntityConverter<Cidade> getCidadeConverter() {
		return cidadeConverter;
	}

	public void setCidadeConverter(EntityConverter<Cidade> cidadeConverter) {
		this.cidadeConverter = cidadeConverter;
	}

	public EntityConverter<Usuario> getImportadoresConverter() {
		return importadoresConverter;
	}

	public void setImportadoresConverter(
			EntityConverter<Usuario> importadoresConverter) {
		this.importadoresConverter = importadoresConverter;
	}

	public List<Usuario> getImportadores() {
		return importadores;
	}

	public void setImportadores(List<Usuario> importadores) {
		this.importadores = importadores;
	}

	public List<PessoaJuridica> getExportadores() {
		return exportadores;
	}

	public void setExportadores(List<PessoaJuridica> exportadores) {
		this.exportadores = exportadores;
	}

	public EntityConverter<PessoaJuridica> getExportadoresConverter() {
		return exportadoresConverter;
	}

	public void setExportadoresConverter(
			EntityConverter<PessoaJuridica> exportadoresConverter) {
		this.exportadoresConverter = exportadoresConverter;
	}

	/**
	 * @return the ativo
	 */
	public List<SelectItem> getAtivo() {
		return ativo;
	}

	/**
	 * @param ativo
	 *            the ativo to set
	 */
	public void setAtivo(List<SelectItem> ativo) {
		this.ativo = ativo;
	}

}
