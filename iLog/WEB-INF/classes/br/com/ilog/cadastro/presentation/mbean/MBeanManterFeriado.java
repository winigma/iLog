package br.com.ilog.cadastro.presentation.mbean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.hibernate.validator.ValidatorHelper;
import br.cits.commons.citspresentation.mbeans.AbstractManter;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.commons.citspresentation.util.JSFRequestBean;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.Feriado;
import br.com.ilog.cadastro.business.entity.Pais;
import br.com.ilog.cadastro.business.entity.TipoFeriado;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.cadastro.presentation.util.ConverterUtil;
import br.com.ilog.cadastro.presentation.util.ExceptionFiltro;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;

/**
 * 
 * @author Wisley Souza
 * @since 09/06/2011
 * @comentario MBean de manuteir Feriados
 * 
 */

@Controller("mBeanManterFeriado")
@AccessScoped
public class MBeanManterFeriado extends AbstractManter {

	private static final long serialVersionUID = 4383882472219067536L;
	private static Logger logger = LoggerFactory
			.getLogger(MBeanManterFeriado.class);

	private Feriado feriado;
	private Pais pais;
	private List<Pais> paises;
	private Feriado selectferiado;
	private Boolean novoFeriado;
	private List<Pais> comboPaises;
	// private EntityConverter<Pais> converterPais;
	// novo converter
	private ConverterUtil<Pais> converterPais;

	/**
	 * combo com o tipod e feriado
	 */
	private List<SelectItem> typeFeriado;

	private List<Feriado> fe;
	/**
	 * fachada
	 */
	@Resource(name = "controllerCadastro")
	CadastroFacade feriadoFacade;

	/**
	 * string que pega data do tipo fixo
	 */
	private String data;
	private Boolean verifica;

	private String dia;
	private String mes;
	private String ano;
	private String datePattern;

	@PostConstruct
	void inicilaizar() {
		novoFeriado = false;
		inicializarObjetos();
		selectferiado = new Feriado();
		paises = Collections.emptyList();
		fe = Collections.emptyList();
		typeFeriado = new ArrayList<SelectItem>();
		FacesContext fc = FacesContext.getCurrentInstance();
		datePattern = "dd/MM/yyyy";

		try {
			popularComboPais();
			for (TipoFeriado type : TipoFeriado.values()) {
				typeFeriado.add(new SelectItem(type, TemplateMessageHelper
						.getMessage(MensagensSistema.FERIADO, type.name(), fc
								.getViewRoot().getLocale())));
			}
		} catch (Exception e) {
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG_001", fc.getViewRoot()
							.getLocale()));
		}
	}

	public String salvarNovo() {
		try {
			if (this.salvar() != null) {
				return this.novoFeriado();

			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public String cancelar() {
		this.refazerPesquisa();
		return "feriados.jsf";

	}

	public void popularComboPais() {
		// metodo que popula a como de paises na pagina de pesquisa...:)
		comboPaises = new ArrayList<Pais>();
		try {
			List<Pais> paisesAux = feriadoFacade.listarPaises();
			comboPaises = paisesAux;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String novoFeriado() {

		feriado = new Feriado();
		novoFeriado = true;
		edicao = false;
		return "feriado.jsf";
	}

	@Override
	public String editar() {
		Long idRegistro = Long.valueOf(JSFRequestBean.getParameter("id"));

		try {
			feriado = (Feriado) feriadoFacade.getFeriadoById(idRegistro);
			Pais pais = feriadoFacade.getPaisById(feriado.getPais().getId());
			data = feriado.getDatas();
			feriado.setPais(pais);
			if (feriado.getTipo() != null && feriado.getTipo().equals("M")) {
				datePattern = "dd/MM/yyyy";
			} else if (feriado.getTipo() != null
					&& feriado.getTipo().equals("F")) {

				datePattern = "dd/MM";
			}

		} catch (BusinessException e) {
			logger.error("erro: {}", e);
			e.printStackTrace();
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.FERIADO, e));
		}
		edicao = true;
		return "feriado.jsf";
	}

	@Override
	public String excluir() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			feriadoFacade.excluirFeriado(feriado);
			Messages.adicionaMensagemDeInfo(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG_EXCLUIR_SUCESSO", fc
							.getViewRoot().getLocale()));

			this.refazerPesquisa();
		} catch (Exception e) {

			logger.error("error: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper
					.getMessage(ExceptionFiltro.recursiveException(e)));

			this.refazerPesquisa();
			return null;
		}
		return "feriados.jsf";
	}

	@Override
	public void inicializarObjetos() {
		paises = new ArrayList<Pais>();
		feriado = new Feriado();
	}

	@Override
	protected void refazerPesquisa() {
		MBeanPesquisarFeriado mBean = (MBeanPesquisarFeriado) JSFRequestBean
				.getManagedBean("mBeanPesquisarFeriado");
		mBean.refazerPesquisa();
	}

	// metodo de salvar
	@Override
	public String salvar() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			ResourceBundle resourceBundle = TemplateMessageHelper
					.getResourceBundle(MensagensSistema.FERIADO, fc
							.getViewRoot().getLocale());
			List<String> errorMessages = ValidatorHelper.valida(feriado,
					resourceBundle);
			fe = new ArrayList<Feriado>();

			if (errorMessages.isEmpty()) {
				try {
					// List<Feriado> fe = new ArrayList<Feriado>();
					// Feriado feriado1 = new Feriado();
					// feriado1.setNome("teste")

					if (edicao) {
						if (feriado.getTipo().equals("F")) {
							data = data + "/1970";
							String formato = "dd/MM/yyyy";
							try {
								feriado.setData(new SimpleDateFormat(formato)
										.parse(data));
							} catch (ParseException e) {

								e.printStackTrace();
							}
						}
						feriadoFacade.alterarFeriado(feriado);
					} else {
						if (feriado.getTipo().equals("F")) {
							data = data + "/1970";
							String formato = "dd/MM/yyyy";
							try {
								feriado.setData(new SimpleDateFormat(formato)
										.parse(data));
							} catch (ParseException e) {

								e.printStackTrace();
							}
						}
						feriadoFacade.cadastrarFeriado(feriado);
					}

				} catch (BusinessException e) {
					logger.error("error:{}", e);
					e.printStackTrace();
					Throwable lastCause = ExceptionFiltro.getLastException(e);
					if (lastCause.getMessage().contains("CHECK constraint")) {
						if (lastCause.getMessage().contains(
								"chk_feriado_duplicado")) {
							Messages.adicionaMensagemDeErro(TemplateMessageHelper
									.getMessage(MensagensSistema.FERIADO,
											"msgCheckFeriado", fc.getViewRoot()
													.getLocale()));
						}
						return null;

					}
					Messages.adicionaMensagemDeErro(TemplateMessageHelper
							.getMessage(MensagensSistema.FERIADO, e));
					return null;
				} catch (Exception e) {

					logger.error("error: {}", e);
					if (!edicao) {
						feriado.setId(null);
					}
					ConstraintViolationException exc = new ConstraintViolationException(
							null, null, null);
					if (e.getMessage().contains("ConstraintViolationException"))
						exc = (ConstraintViolationException) e.getCause();
					e.printStackTrace();

					if (e.getMessage().contains("ConstraintViolationException")) {
						if (exc.getSQLException().getNextException()
								.getMessage().contains("uk_feriado")) {
							Messages.adicionaMensagemDeErro(TemplateMessageHelper
									.getMessage(MensagensSistema.FERIADO,
											"msgCheckFeriado", fc.getViewRoot()
													.getLocale()));
							return null;

						}

						return null;
					} else {

						Messages.adicionaMensagemDeErro(TemplateMessageHelper
								.getMessage(ExceptionFiltro
										.recursiveException(e)));
						return null;
					}

				}
			} else {
				Messages.adicionaMensagensDeErro(errorMessages);
				return null;
			}

			String message = TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG001", fc.getViewRoot()
							.getLocale());
			Messages.adicionaMensagemDeInfo(message);
			refazerPesquisa();
			return "feriados.jsf";
		} catch (Exception e) {
			e.printStackTrace();

			return null;
		}
	}

	public void verificaTipoFeriado() {
		if (feriado.getTipo() != null && feriado.getTipo().equals("M")) {
			verifica = true;

			Calendar f = Calendar.getInstance();
			Calendar cal = GregorianCalendar.getInstance();
			// System.out.println(cal.get(Calendar.YEAR));
			if (feriado.getData() != null)
				f.setTime(feriado.getData());
			if (f.get(Calendar.YEAR) == 1970)
				f.set(Calendar.YEAR, cal.get(Calendar.YEAR));

			feriado.setData(f.getTime());
			datePattern = "dd/MM/yyyy";
		} else if (feriado.getTipo() != null && feriado.getTipo().equals("F")) {
			verifica = false;
			feriado.setData(new Date());
			System.out.println(feriado.getData());
			datePattern = "dd/MM";
		}
	}

	public Feriado getFeriado() {
		return feriado;
	}

	public void setFeriado(Feriado feriado) {
		this.feriado = feriado;
	}

	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	public List<Pais> getPaises() {
		return paises;
	}

	public void setPaises(List<Pais> paises) {
		this.paises = paises;
	}

	public Boolean getNovoFeriado() {
		return novoFeriado;
	}

	public void setNovoFeriado(Boolean novoFeriado) {
		this.novoFeriado = novoFeriado;
	}

	public List<Pais> getComboPaises() {
		return comboPaises;
	}

	public void setComboPaises(List<Pais> comboPaises) {
		this.comboPaises = comboPaises;
	}

	public List<SelectItem> getTypeFeriado() {

		FacesContext fc = FacesContext.getCurrentInstance();
		typeFeriado = new ArrayList<SelectItem>();
		for (TipoFeriado type : TipoFeriado.values()) {
			typeFeriado.add(new SelectItem(type, TemplateMessageHelper
					.getMessage(MensagensSistema.FERIADO, type.name(), fc
							.getViewRoot().getLocale())));
		}
		return typeFeriado;
	}

	public void setTypeFeriado(List<SelectItem> typeFeriado) {
		this.typeFeriado = typeFeriado;
	}

	public List<Feriado> getFe() {
		return fe;
	}

	public void setFe(List<Feriado> fe) {
		this.fe = fe;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Boolean getVerifica() {
		return verifica;
	}

	public void setVerifica(Boolean verifica) {
		this.verifica = verifica;
	}

	public ConverterUtil<Pais> getConverterPais() {
		return converterPais;
	}

	public void setConverterPais(ConverterUtil<Pais> converterPais) {
		this.converterPais = converterPais;
	}

	public String getDia() {
		return dia;
	}

	public void setDia(String dia) {
		this.dia = dia;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getDatePattern() {
		return datePattern;
	}

	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}

	public Feriado getSelectferiado() {
		return selectferiado;
	}

	public void setSelectferiado(Feriado selectferiado) {
		this.selectferiado = selectferiado;
	}

}
