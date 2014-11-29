package br.com.ilog.cadastro.presentation.mbean;

import java.util.ArrayList;
import java.util.Collections;
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
import br.com.ilog.cadastro.business.entity.Frete;
import br.com.ilog.cadastro.business.entity.Modal;
import br.com.ilog.cadastro.business.entity.Pais;
import br.com.ilog.cadastro.business.entity.PessoaJuridica;
import br.com.ilog.cadastro.business.entity.TipoPessoaEnum;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroFrete;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.cadastro.presentation.util.ConverterUtil;
import br.com.ilog.cadastro.presentation.util.ExceptionFiltro;
import br.com.ilog.geral.presentation.CommonsList;
import br.com.ilog.geral.presentation.Constantes;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;

@Controller("mBeanPesquisarFrete")
@AccessScoped
public class MBeanPesquisarFrete extends AbstractPaginacao {

	/** */
	private static final long serialVersionUID = -1675893233022464849L;

	/**
	 * Fachada de Cadastro
	 */
	@Resource(name = "controllerCadastro")
	CadastroFacade cadastro;

	@Resource(name = "commonsList")
	CommonsList commonsList;

	private static Logger logger = LoggerFactory
			.getLogger(MBeanPesquisarFrete.class);
	/**
	 * Filtro de pesquisa.
	 */
	private BasicFiltroFrete filtro;

	/**
	 * resultado da pesquisa.
	 */
	private List<Frete> result;

	/**
	 * Atributos para combo de paises destino
	 */
	private List<Pais> comboPaisesDestino;
	private EntityConverter<Pais> converterPaisDestino;

	/**
	 * Atributos para combo de paises origem
	 */
	private List<Pais> comboPaisesOrigem;
	private EntityConverter<Pais> converterPaisOrigem;

	/**
	 * Atributos para combo de cidade origem
	 */
	private List<Cidade> comboCidadesOrigem;
	private EntityConverter<Cidade> converterCidadeOrigem;

	/**
	 * Atributos para combo de cidade destino
	 */
	private List<Cidade> comboCidadesDestino;
	private EntityConverter<Cidade> converterCidadeDestino;

	/**
	 * Atributos para combo de agente de cargas
	 */
	private List<PessoaJuridica> comboAgentes;
	private EntityConverter<PessoaJuridica> converterAgente;

	/**
	 * combo de tipo de transporte.
	 */
	private List<Modal> tipoTransporte;

	/**
	 * combo de Expresso, itens booleans
	 */
	private List<SelectItem> comboCritico;

	/**
	 * combo de Status, itens booleans
	 */
	private List<SelectItem> comboStatus;

	/**
	 * object's id to remove
	 */
	private Long idObject;

	private ConverterUtil<Modal> converterModal;

	@PostConstruct
	public void inicializar() {
		filtro = new BasicFiltroFrete();

		result = Collections.emptyList();

		inicializarCombos();
		doPesquisar(null);
	}

	private void inicializarCombos() {

		this.popularAgentes();
		this.popularPaisDestinoOrigem();
		this.popularCidadeDestino();
		this.popularCidadeOrigem();

	}

	/**
	 * Popular combo de agentes de cargas.
	 */
	private void popularAgentes() {
		comboAgentes = new ArrayList<PessoaJuridica>();

		try {
			List<PessoaJuridica> pessoas = cadastro
					.listarPessoasByTipo(TipoPessoaEnum.A_CARGA);
			if (pessoas != null) {
				comboAgentes =  pessoas;
			}
			
			
		} catch (BusinessException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Popular combos de Paises de destinos e origem
	 */
	public void popularPaisDestinoOrigem() {
		comboPaisesDestino = new ArrayList<Pais>();
		comboPaisesOrigem = new ArrayList<Pais>();
		try {
			List<Pais> paises = cadastro.listarPaises();
			comboPaisesOrigem = paises;
			comboPaisesDestino = paises;
			// for (Pais pais : paises) {
			// comboPaisesDestino.add(new SelectItem(pais, pais.getNome()));
			// comboPaisesOrigem.add(new SelectItem(pais, pais.getNome()));
			// }
			converterPaisDestino = new EntityConverter<Pais>(paises);
			converterPaisOrigem = new EntityConverter<Pais>(paises);

		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Popular combo de cidades Destino conforme pais selecionado.
	 * 
	 * @param arg
	 */
	public void popularCidadeDestino() {
		comboCidadesDestino = new ArrayList<Cidade>();

		try {
			List<Cidade> cidades = cadastro.getCidadesByPais(filtro
					.getPaisDestino());
			if (cidades != null) {
				comboCidadesDestino = cidades;
			}
			// for (Cidade c : cidades) {
			// comboCidadesDestino.add(new SelectItem(c, c.getNome()));
			// }
			// converterCidadeDestino = new EntityConverter<Cidade>(cidades);

		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Popular combo de cidades origem, conforme pais selecionado.
	 * 
	 * @param arg
	 */
	public void popularCidadeOrigem() {
		comboCidadesOrigem = new ArrayList<Cidade>();

		try {
			List<Cidade> cidades = cadastro.getCidadesByPais(filtro
					.getPaisOrigem());
			if (cidades != null) {

				comboCidadesOrigem = cidades;
			}
			// for (Cidade c : cidades) {
			// comboCidadesOrigem.add(new SelectItem(c, c.getNome()));
			// }
			// converterCidadeOrigem = new EntityConverter<Cidade>(cidades);

		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doPesquisar(ActionEvent ae) {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			getRegistrosPagina();
			result = cadastro.listarFrete(filtro);
			if (result.isEmpty()) {
				String msg = TemplateMessageHelper.getMessage(
						MensagensSistema.SISTEMA, "MSG008", fc.getViewRoot()
								.getLocale());
				Messages.adicionaMensagemDeInfo(msg);
			}

			setPaginaAtual(Constantes.PAGE_INITIAL);

		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getTotalRegistros() {
		if (result != null)
			return result.size();
		return 0;
	}

	/**
	 * @coment captura o id do item selecionado
	 * @param index
	 */
	public void capturarId(int index) {
		Frete objeto = result.get(index);
		idObject = objeto.getId();
	}

	/**
	 * Excluir rota.
	 * 
	 */
	public void excluir() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			cadastro.excluirFrete(idObject);
			Messages.adicionaMensagemDeInfo(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG_EXCLUIR_SUCESSO", fc
							.getViewRoot().getLocale()));

			refazerPesquisa();

		} catch (Exception e) {
			logger.error("eror: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper
					.getMessage(ExceptionFiltro.recursiveException(e)));
			return;
		}
	}

	@Override
	public void limpar() {
		filtro =  new BasicFiltroFrete();
	}

	@Override
	public void refazerPesquisa() {
		if (filtro == null)
			filtro = new BasicFiltroFrete();

		if (result.isEmpty())
			return;

		doPesquisar(null);

	}

	public CommonsList getCommonsList() {
		return commonsList;
	}

	public void setCommonsList(CommonsList commonsList) {
		this.commonsList = commonsList;
	}

	public BasicFiltroFrete getFiltro() {
		return filtro;
	}

	public void setFiltro(BasicFiltroFrete filtro) {
		this.filtro = filtro;
	}

	public List<Frete> getResult() {
		return result;
	}

	public void setResult(List<Frete> result) {
		this.result = result;
	}

	public List<Pais> getComboPaisesDestino() {
		return comboPaisesDestino;
	}

	public void setComboPaisesDestino(List<Pais> comboPaisesDestino) {
		this.comboPaisesDestino = comboPaisesDestino;
	}

	public EntityConverter<Pais> getConverterPaisDestino() {
		return converterPaisDestino;
	}

	public void setConverterPaisDestino(
			EntityConverter<Pais> converterPaisDestino) {
		this.converterPaisDestino = converterPaisDestino;
	}

	public List<Pais> getComboPaisesOrigem() {
		return comboPaisesOrigem;
	}

	public void setComboPaisesOrigem(List<Pais> comboPaisesOrigem) {
		this.comboPaisesOrigem = comboPaisesOrigem;
	}

	public EntityConverter<Pais> getConverterPaisOrigem() {
		return converterPaisOrigem;
	}

	public void setConverterPaisOrigem(EntityConverter<Pais> converterPaisOrigem) {
		this.converterPaisOrigem = converterPaisOrigem;
	}

	public List<Cidade> getComboCidadesOrigem() {
		return comboCidadesOrigem;
	}

	public void setComboCidadesOrigem(List<Cidade> comboCidadesOrigem) {
		this.comboCidadesOrigem = comboCidadesOrigem;
	}

	public EntityConverter<Cidade> getConverterCidadeOrigem() {
		return converterCidadeOrigem;
	}

	public void setConverterCidadeOrigem(
			EntityConverter<Cidade> converterCidadeOrigem) {
		this.converterCidadeOrigem = converterCidadeOrigem;
	}

	public List<Cidade> getComboCidadesDestino() {
		return comboCidadesDestino;
	}

	public void setComboCidadesDestino(List<Cidade> comboCidadesDestino) {
		this.comboCidadesDestino = comboCidadesDestino;
	}

	public EntityConverter<Cidade> getConverterCidadeDestino() {
		return converterCidadeDestino;
	}

	public void setConverterCidadeDestino(
			EntityConverter<Cidade> converterCidadeDestino) {
		this.converterCidadeDestino = converterCidadeDestino;
	}

	public List<PessoaJuridica> getComboAgentes() {
		return comboAgentes;
	}

	public void setComboAgentes(List<PessoaJuridica> comboAgentes) {
		this.comboAgentes = comboAgentes;
	}

	public EntityConverter<PessoaJuridica> getConverterAgente() {
		return converterAgente;
	}

	public void setConverterAgente(
			EntityConverter<PessoaJuridica> converterAgente) {
		this.converterAgente = converterAgente;
	}

	public List<Modal> getTipoTransporte() {
		// FacesContext fc = FacesContext.getCurrentInstance();
		try {
			tipoTransporte = new ArrayList<Modal>();
			List<Modal> modais = cadastro.listarModals();
			tipoTransporte = modais;
			//converterModal = new ConverterUtil<Modal>(modais);

			return tipoTransporte;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void setTipoTransporte(List<Modal> tipoTransporte) {
		this.tipoTransporte = tipoTransporte;
	}

	public List<SelectItem> getComboCritico() {
		comboCritico = commonsList.listaSimNao();
		return comboCritico;
	}

	public void setComboCritico(List<SelectItem> comboCritico) {
		this.comboCritico = comboCritico;
	}

	public List<SelectItem> getComboStatus() {
		comboStatus = commonsList.listaBooleanAtivoInativo();
		return comboStatus;
	}

	public void setComboStatus(List<SelectItem> comboStatus) {
		this.comboStatus = comboStatus;
	}

	public Long getIdObject() {
		return idObject;
	}

	public void setIdObject(Long idObject) {
		this.idObject = idObject;
	}

	public ConverterUtil<Modal> getConverterModal() {
		return converterModal;
	}

	public void setConverterModal(ConverterUtil<Modal> converterModal) {
		this.converterModal = converterModal;
	}

}
