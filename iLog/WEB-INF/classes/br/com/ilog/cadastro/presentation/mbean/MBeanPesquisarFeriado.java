package br.com.ilog.cadastro.presentation.mbean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.hibernate.validator.ValidatorHelper;
import br.cits.commons.citspresentation.converter.EntityConverter;
import br.cits.commons.citspresentation.mbeans.AbstractPaginacao;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.Feriado;
import br.com.ilog.cadastro.business.entity.Pais;
import br.com.ilog.cadastro.business.entity.TipoFeriado;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroFeriado;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.cadastro.presentation.util.ExceptionFiltro;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;

@Controller("mBeanPesquisarFeriado")
@AccessScoped
public class MBeanPesquisarFeriado extends AbstractPaginacao {

	private static final long serialVersionUID = 4207044734877323666L;
	private static Logger logger = LoggerFactory
			.getLogger(MBeanPesquisarFeriado.class);

	@Resource(name = "controllerCadastro")
	CadastroFacade feriadoFacade;

	private List<Feriado> feriados;
	private BasicFiltroFeriado filtro;
	private List<Pais> comboPaises;
	private EntityConverter<Feriado> converterFeriado;
	private EntityConverter<Pais> converterPais;
	/**
	 * combo com o tipod e feriado
	 */
	private List<SelectItem> typeFeriado;

	/**
	 * metodo inicializador
	 */
	@SuppressWarnings("unused")
	@PostConstruct
	private void inicializar() {
		filtro = new BasicFiltroFeriado();
		feriados = Collections.emptyList();
		comboPaises = new ArrayList<Pais>();
		typeFeriado = new ArrayList<SelectItem>();
		FacesContext fc = FacesContext.getCurrentInstance();
		try {

			doPesquisar(null);
			popularComboPais();
			for (TipoFeriado type : TipoFeriado.values()) {
				typeFeriado.add(new SelectItem(type, TemplateMessageHelper
						.getMessage(MensagensSistema.FERIADO, type.toString(),
								fc.getViewRoot().getLocale())));
			}
		} catch (Exception e) {
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG001", fc.getViewRoot()
							.getLocale()));
			e.printStackTrace();
		}
	}

	/**
	 * Método que carrega a combo de paises
	 */
	public void popularComboPais() {
		// metodo que popula a como de paises na pagina de pesquisa...:)

		comboPaises = new ArrayList<Pais>();
		try {
			List<Pais> paisesAux = feriadoFacade.listarPaises();
			comboPaises =  paisesAux;

		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Método que faz a pesquisa nos registros
	 */

	@Override
	public void doPesquisar(ActionEvent arg0) {
		FacesContext fc = FacesContext.getCurrentInstance();
		ResourceBundle resourceBundle = TemplateMessageHelper
				.getResourceBundle(MensagensSistema.FERIADO, fc.getViewRoot()
						.getLocale());
		List<String> errorMessages = ValidatorHelper.valida(filtro,
				resourceBundle, resourceBundle);

		if (errorMessages.isEmpty()) {
			try {
				feriados = feriadoFacade.listarFeriado(filtro);
				/**
				 * for (Feriado fe : feriados) { String datastr = "01/01/2008";
				 * DateFormat formata = new SimpleDateFormat("dd/MM"); Calendar
				 * calendar = new GregorianCalendar(); calendar.set(2010, 11,
				 * 11); Date data = (java.util.Date)formata.parse(datastr);
				 * //data = formata.parse(calendar); String dataformatada =
				 * formata.format(data);
				 * System.out.println("Data String:>>>>"+dataformatada);
				 * System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				 * System.out.println("Data Date:>>>"+data);
				 * 
				 * }
				 **/

				if (feriados.isEmpty()) {
					String msg = TemplateMessageHelper.getMessage(
							MensagensSistema.SISTEMA, "MSG008", fc
									.getViewRoot().getLocale());
					Messages.adicionaMensagemDeInfo(msg);
				}
				setPaginaAtual(1);
			} catch (BusinessException e1) {
				logger.error("erro: {}", e1);
				System.out.println(e1);
				e1.printStackTrace();
				Messages.adicionaMensagemDeErro(TemplateMessageHelper
						.getMessage(MensagensSistema.SISTEMA, e1));

			}
		} else {
			Messages.adicionaMensagensDeErro(errorMessages);
		}
	}

	// Long que captura o id
	private Long idObjeto;

	/**
	 * @coment captura o id do item selecionados
	 * @param index
	 */

	public void capturarId(int index) {
		Feriado objeto = feriados.get(index);
		idObjeto = objeto.getId();
	}

	/**
	 * Excluir feriado.
	 * 
	 * @param id
	 */
	public void excluir() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			feriadoFacade.excluirFeriado(new Feriado(idObjeto));
			Messages.adicionaMensagemDeInfo(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG_EXCLUIR_SUCESSO", fc
							.getViewRoot().getLocale()));
			this.refazerPesquisa();

		} catch (Exception e) {
			logger.error("error: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper
					.getMessage(ExceptionFiltro.recursiveException(e)));
			return;
		}
	}

	@Override
	public int getTotalRegistros() {
		if (feriados != null)
			return feriados.size();
		else
			return 0;
	}

	@Override
	public void limpar() {
		setPaginaAtual(1);
		filtro =  new BasicFiltroFeriado();
		feriados =  Collections.emptyList();;
	}

	@Override
	public void refazerPesquisa() {
		if (filtro == null)
			filtro = new BasicFiltroFeriado();
		// Se a lista estava vazia antes não é necessário
		// fazer uma nova pesquisa
		if (feriados.isEmpty())
			return;

		doPesquisar(null);
	}

	public List<Feriado> getFeriados() {
		return feriados;
	}

	public void setFeriados(List<Feriado> feriados) {
		this.feriados = feriados;
	}

	public BasicFiltroFeriado getFiltro() {
		return filtro;
	}

	public void setFiltro(BasicFiltroFeriado filtro) {
		this.filtro = filtro;
	}

	public List<Pais> getComboPaises() {
		return comboPaises;
	}

	public void setComboPaises(List<Pais> comboPaises) {
		this.comboPaises = comboPaises;
	}

	public EntityConverter<Feriado> getConverterFeriado() {
		return converterFeriado;
	}

	public void setConverterFeriado(EntityConverter<Feriado> converterFeriado) {
		this.converterFeriado = converterFeriado;
	}

	public EntityConverter<Pais> getConverterPais() {
		return converterPais;
	}

	public void setConverterPais(EntityConverter<Pais> converterPais) {
		this.converterPais = converterPais;
	}

	public List<SelectItem> getTypeFeriado() {
		typeFeriado = new ArrayList<SelectItem>();
		FacesContext fc = FacesContext.getCurrentInstance();

		for (TipoFeriado type : TipoFeriado.values()) {
			typeFeriado.add(new SelectItem(type, TemplateMessageHelper
					.getMessage(MensagensSistema.FERIADO, type.toString(), fc
							.getViewRoot().getLocale())));
		}
		return typeFeriado;
	}

	public void setTypeFeriado(List<SelectItem> typeFeriado) {
		this.typeFeriado = typeFeriado;
	}

	public Long getIdObjeto() {
		return idObjeto;
	}

	public void setIdObjeto(Long idObjeto) {
		this.idObjeto = idObjeto;
	}

}
