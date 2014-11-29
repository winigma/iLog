package br.com.ilog.seguranca.presentation.mbean;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;
import org.primefaces.model.DualListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.hibernate.validator.ValidatorHelper;
import br.cits.commons.citsbusiness.util.Utils;
import br.cits.commons.citspresentation.mbeans.AbstractManter;
import br.cits.commons.citspresentation.messages.Messages;
import br.cits.commons.citspresentation.util.JSFRequestBean;
import br.cits.myview.spring.scope.AccessScoped;
import br.com.ilog.cadastro.business.entity.PessoaJuridica;
import br.com.ilog.cadastro.business.entity.TipoPessoaEnum;
import br.com.ilog.cadastro.business.facade.CadastroFacade;
import br.com.ilog.cadastro.presentation.util.ConverterUtil;
import br.com.ilog.cadastro.presentation.util.ExceptionFiltro;
import br.com.ilog.geral.converter.ConverterSimNao;
import br.com.ilog.geral.presentation.CommonsList;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;
import br.com.ilog.seguranca.business.entity.Perfil;
import br.com.ilog.seguranca.business.entity.StatusUsuario;
import br.com.ilog.seguranca.business.entity.TipoUsuario;
import br.com.ilog.seguranca.business.entity.Usuario;
import br.com.ilog.seguranca.business.facade.SegurancaFacade;
import br.com.ilog.seguranca.utilities.SenhaUtilities;

/**
 * @author Heber Santiago.
 * @date 20/06/2011
 * 
 * @brief MBean de cadastro de usuario.
 * 
 */

@Controller("mBeanManterUsuario")
@AccessScoped
public class MBeanManterUsuario extends AbstractManter {

	/** */
	private static final long serialVersionUID = -5213753702738597617L;

	@Resource(name = "controleUsuario")
	SegurancaFacade seguranca;

	@Resource(name = "controllerCadastro")
	CadastroFacade cadastro;

	@Resource
	MBeanSessaoUsuario sessao;

	private static Logger logger = LoggerFactory
			.getLogger(MBeanManterUsuario.class);

	/**
	 * Usuario a ser editado/inserido.
	 */
	private Usuario usuario;

	private boolean admin;

	private String confPassword;

	private String password;

	private String senhaAtual;

	private String senhaTemp;

	private String tipoAlteracao;

	private String email;

	private Usuario selectUser;

	/**
	 * Flag verifca se é edicao.
	 */
	private boolean edicao;

	private boolean houveErro;

	/**
	 * Flag verifica se é novo usuario.
	 */
	private boolean novoUsuario;

	/**
	 * Itens de status de usuario.
	 */
	private List<SelectItem> statusUsuario;

	/**
	 * Itens de usuario
	 */
	private List<SelectItem> tipoUsuario;

	/**
	 * Itens de perfis de usuario.
	 */
	private List<SelectItem> usuarioPerfils;

	/**
	 * converter para perfis.
	 */
	private ConverterUtil<Perfil> converterPerfis;

	private List<SelectItem> comboSimNao;
	private ConverterSimNao converterSimNao;

	// piclist

	private DualListModel<Perfil> perfisUsers;

	private List<PessoaJuridica> comboPessoaJuridica;

	private List<Perfil> source;
	private List<Perfil> target;

	@Resource(name = "commonsList")
	CommonsList commonsList;

	private String mensagemErro;

	private Boolean resetarSenha;

	private List<PessoaJuridica> agentesCargas;

	private List<PessoaJuridica> despachante;

	private PessoaJuridica pessoaJuridica;

	/**
	 * Inicializar os objetos da pagina.
	 */
	@Override
	@PostConstruct
	public void inicializarObjetos() {
		usuario = new Usuario();
		novoUsuario = false;
		pessoaJuridica = new PessoaJuridica();

		inicializarCombos();

	}

	/**
	 * 
	 */
	public void inicializarCombos() {
		comboSimNao = commonsList.listaSimNao();
		try {

			agentesCargas = cadastro
					.listarPessoasByTipo(TipoPessoaEnum.A_CARGA);
			despachante = cadastro.listarPessoasByTipo(TipoPessoaEnum.DESPACH);

		} catch (Exception e) {
		}

	}

	public void ativarResetSenha(ActionEvent arg) {
		this.resetarSenha = true;
	}

	public void desativarResetSenha(ActionEvent arg) {
		password = null;
		confPassword = null;

		this.resetarSenha = false;
	}

	/**
	 * Metodo utilizado na tela de edicao de usuario para mudanca de senha.
	 */
	public void mudarSenha() {

		if (password == null || password.equals("")) {
			return;
		}
		if (confPassword == null || confPassword.equals("")) {
			return;
		}

		if (password.equals(usuario.getPassword())) {
			return;
		}

		String validaSenha = SenhaUtilities.validaSenha(password, usuario);

		List<String> errorMessages = new ArrayList<String>();
		if (!StringUtils.isBlank(validaSenha)) {

			errorMessages.add(validaSenha);

			Messages.adicionaMensagensDeErro(errorMessages);
			return;
		} else {
			try {
				usuario.setPassword(SenhaUtilities.criptografaSenha(password,
						null));
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}

	}

	/**
	 * 			
	 */
	@Override
	public String editar() {

		// Long idRegistro = selectUser.getId();

		try {
			admin = false;

			source = new ArrayList<Perfil>();
			target = new ArrayList<Perfil>();

			usuario = (Usuario) seguranca.getUsuarioById(selectUser.getId());

			pessoaJuridica = usuario.getPessoaJuridica();

			admin = usuario.getLogin().equals("cits\\admin");

			// populaUsuarioPerfils();
			source = seguranca.listarPerfil();
			converterPerfis = new ConverterUtil<Perfil>(source);
			if (usuario.getPerfis() != null)
				target = usuario.getPerfis();

			source.removeAll(target);

			perfisUsers = new DualListModel<Perfil>(source, target);

			popularComboPessoaJuridica();

		} catch (BusinessException e) {
			logger.error("erro: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.USUARIOS, e));
		}

		edicao = true;

		return "usuario.jsf";
	}

	/**
	 * Verificar se o usuario eh uma usuario de pessoa juridica.
	 * 
	 * @return
	 */
	public boolean verificarUsuarioPJ() {
		if (usuario.getTipo() != null
				&& (usuario.getTipo().equals(TipoUsuario.A_CARGA) || usuario
						.getTipo().equals(TipoUsuario.DESPACH))) {
			return true;
		}
		return false;
	}

	/**
	 * Popular combo conforme tipo usuario.
	 */
	public void popularComboPessoaJuridica() {
		comboPessoaJuridica = new ArrayList<PessoaJuridica>();

		if (verificarUsuarioPJ()) {
			if (usuario.getTipo().equals(TipoUsuario.A_CARGA))
				comboPessoaJuridica = agentesCargas;
			if (usuario.getTipo().equals(TipoUsuario.DESPACH))
				comboPessoaJuridica = despachante;
		}
	}

	/**
	 * 
	 */
	public void populaUsuarioPerfils() {

		source = new ArrayList<Perfil>();
		target = new ArrayList<Perfil>();
		try {

			List<Perfil> perfils = seguranca.listarPerfil();

			if (perfils != null) {

				source = perfils;
				perfisUsers = new DualListModel<Perfil>(this.source,
						this.target);

				converterPerfis = new ConverterUtil<Perfil>(perfils);
			}

		} catch (BusinessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Direcionar para a pagina de novo cadastro.
	 * 
	 * @return
	 */
	public String novoUsuario() {

		usuario = new Usuario();
		usuario.setPerfis(new ArrayList<Perfil>());
		edicao = false;
		usuario.setStatus(StatusUsuario.A);
		novoUsuario = true;

		inicializarCombos();

		populaUsuarioPerfils();

		return "usuario.jsf";
	}

	@Override
	public String cancelar() {
		return "usuarios.jsf";
	}

	public void salvarNovaSenha() {

		boolean erro = false;
		this.mensagemErro = "";
		try {
			if (this.senhaAtual != null && this.senhaAtual.isEmpty()) {
				this.mensagemErro = TemplateMessageHelper.getMessage(
						MensagensSistema.USUARIOS, "msgUsuarioSenhaAtualNula");
				erro = true;
			}
			if ((this.password != null && this.password.isEmpty())
					|| (this.confPassword != null && this.confPassword
							.isEmpty())) {
				this.mensagemErro = TemplateMessageHelper.getMessage(
						MensagensSistema.USUARIOS, "msgUsuarioSenhaNula");
				erro = true;
			}
			if (!erro
					&& !getSessao()
							.getUsuario()
							.getPassword()
							.equals(SenhaUtilities.criptografaSenha(
									this.senhaAtual, null))) {
				this.mensagemErro = TemplateMessageHelper.getMessage(
						MensagensSistema.USUARIOS,
						"msgUsuarioSenhaAtualNaoConfere");
				erro = true;
			}
			String validacao = SenhaUtilities.validaSenha(this.password,
					getSessao().getUsuario());
			if (validacao == null)
				validacao = "";
			if (!erro && !validacao.isEmpty()) {
				this.mensagemErro = validacao;
				erro = true;
			}
			if (!erro && !this.confPassword.equals(this.password)) {
				this.mensagemErro = TemplateMessageHelper.getMessage(
						MensagensSistema.USUARIOS, "lblMsgEqualTo");
				erro = true;
			}
			if (erro && !this.mensagemErro.isEmpty()) {
				Messages.adicionaMensagemDeErro(this.mensagemErro);
			}
			if (!erro) {
				try {
					String pwd = SenhaUtilities.criptografaSenha(this.password,
							null);
					Usuario usuarioSalvar = seguranca
							.getUsuarioById(getSessao().getUsuario().getId());
					usuarioSalvar.setPassword(pwd);

					seguranca.alterarUsuario(usuarioSalvar);

					this.mensagemErro = TemplateMessageHelper.getMessage(
							MensagensSistema.USUARIOS, "lblSenhaModificada");
					Messages.adicionaMensagemDeInfo(this.mensagemErro);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("erro: {}", e);

					Messages.adicionaMensagemDeErro(TemplateMessageHelper
							.getMessage(MensagensSistema.USUARIOS,
									ExceptionFiltro.recursiveException(e)));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Metodo para redefinir a senha do usuário.
	 * 
	 * @param ae
	 */
	public void resetarSenha() {

		if (resetarSenha) {
			FacesContext fc = FacesContext.getCurrentInstance();
			List<String> errorMessages = new ArrayList<String>();

			if (password == null || password.equals("")) {
				return;
			}
			if (confPassword == null || confPassword.equals("")) {
				return;
			}

			if (!password.equals(confPassword)) {
				errorMessages.add(TemplateMessageHelper.getMessage(
						MensagensSistema.USUARIOS, "msgUsuarioSenhasNaoCorr",
						fc.getViewRoot().getLocale()));
			}

			if (errorMessages.isEmpty()) {
				String validaSenha = SenhaUtilities.validaSenha(password,
						usuario);

				if (!StringUtils.isBlank(validaSenha)) {

					errorMessages.add(validaSenha);
				} else {
					try {
						usuario.setPassword(password);
						usuario.setPassword(SenhaUtilities.criptografaSenha(
								usuario.getPassword(), null));

						seguranca.alterarUsuario(usuario);

						String message = TemplateMessageHelper.getMessage(
								MensagensSistema.SISTEMA, "MSG001", fc
										.getViewRoot().getLocale());
						Messages.adicionaMensagemDeInfo(message);

					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
				}
			}
		}

	}

	@Override
	public String salvar() {

		FacesContext fc = FacesContext.getCurrentInstance();

		usuario.setPerfis(perfisUsers.getTarget());
		try {

			ResourceBundle resourceBundle = TemplateMessageHelper
					.getResourceBundle(MensagensSistema.USUARIOS,
							JSFRequestBean.getLocale());

			List<String> errorMessages = ValidatorHelper.valida(usuario,
					TemplateMessageHelper.getResourceBundle(
							MensagensSistema.SISTEMA, fc.getViewRoot()
									.getLocale()), resourceBundle);

			// validar se as senhas correspondem
			// if (usuario.getId() == null
			// && !usuario.getPassword().equals(confPassword)) {
			// errorMessages.add(TemplateMessageHelper.getMessage(
			// MensagensSistema.USUARIOS, "msgUsuarioSenhasNaoCorr", fc
			// .getViewRoot().getLocale()));
			// }
			if (errorMessages.isEmpty()) {
				if (usuario.getId() == null) {

					// valida se a senha informada esta no padrao do sistema

					String validaSenha = SenhaUtilities.validaSenha(
							usuario.getPassword(), usuario);

					if (!StringUtils.isBlank(validaSenha)) {

						errorMessages.add(validaSenha);
					} else {
						try {
							usuario.setPassword(SenhaUtilities
									.criptografaSenha(usuario.getPassword(),
											null));
						} catch (Exception e) {
							e.printStackTrace();
							return null;
						}
					}
				}

				if (errorMessages.isEmpty()) {
					try {
						if (edicao)
							seguranca.alterarUsuario(usuario);
						else
							seguranca.cadastrarUsuario(usuario);

					} catch (Exception e) {
						// ConstraintViolationException exc =
						// (ConstraintViolationException) e.getCause();
						e.printStackTrace();
						logger.error("erro: {}", e);

						if (!edicao) {
							usuario.setId(null);
						}
						// Throwable last = ExceptionFiltro.getLastException(e);
						if (e.getMessage().contains(
								"ConstraintViolationException")) {

							String mensagem = TemplateMessageHelper.getMessage(
									MensagensSistema.USUARIOS,
									"msgUsuarioCadastrado", fc.getViewRoot()
											.getLocale());
							Messages.adicionaMensagemDeErro(mensagem);
						} else {
							Messages.adicionaMensagemDeErro(TemplateMessageHelper
									.getMessage(MensagensSistema.USUARIOS,
											ExceptionFiltro
													.recursiveException(e)));
						}

						return null;
					}

				} else {
					Messages.adicionaMensagensDeErro(errorMessages);
					return null;
				}
			} else {
				Messages.adicionaMensagensDeErro(errorMessages);
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		itemAtualizado = true;

		String message = TemplateMessageHelper.getMessage(
				MensagensSistema.SISTEMA, "MSG001", fc.getViewRoot()
						.getLocale());
		Messages.adicionaMensagemDeInfo(message);

		refazerPesquisa();

		return "usuarios.jsf";
	}

	@Override
	public String excluir() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();

			seguranca.excluirUsuario(usuario.getId());

			Messages.adicionaMensagemDeInfo(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, "MSG_EXCLUIR_SUCESSO", fc
							.getViewRoot().getLocale()));

		} catch (Exception e) {
			logger.error("error: {}", e);
			Messages.adicionaMensagemDeErro(TemplateMessageHelper
					.getMessage(ExceptionFiltro.recursiveException(e)));
			return null;
		}

		return "usuarios.jsf";
	}

	@Override
	protected void refazerPesquisa() {
		// atualiza a tela de pesquisa para atualizar possiveis modificacoes nos
		// campos utilizados na tabela de tela de pesquisa
		MBeanPesquisarUsuario managedBean = (MBeanPesquisarUsuario) JSFRequestBean
				.getManagedBean("mBeanPesquisarUsuario");

		managedBean.refazerPesquisa();

	}

	public void abrirModalRedefinirSenha() {

		if (edicao) {
			tipoAlteracao = "";

		} else {
			tipoAlteracao = "USUARIO_LOGADO";
		}

	}

	/**
	 * @brief Redefinicao de senha do user.
	 * @return
	 */
	public String redefinirSenha() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();

			List<String> errorMessages = new ArrayList<String>();

			if (edicao) {

				usuario = seguranca.getUsuarioById(usuario.getId());

			} else {

				usuario = seguranca.getUsuarioById(getSessao().getUsuario()
						.getId());
				if (!getSessao().getUsuario().getPassword()
						.equals(Utils.md5(senhaAtual))) {

					errorMessages.add(TemplateMessageHelper.getMessage(
							MensagensSistema.USUARIOS, "msgSenhaInvalida", fc
									.getViewRoot().getLocale()));
				}
			}

			if (!senhaTemp.equals(confPassword)) {

				errorMessages.add(TemplateMessageHelper.getMessage(
						MensagensSistema.USUARIOS, "msgUsuarioSenhasNaoCorr",
						fc.getViewRoot().getLocale()));
			}

			// valida se a senha informada esta no padrao do sistema

			String validaSenha = SenhaUtilities.validaSenha(senhaTemp, usuario);

			if (!StringUtils.isBlank(validaSenha)) {

				errorMessages.add(validaSenha);
			}

			if (errorMessages.isEmpty()) {

				usuario.setPassword(senhaTemp);

				seguranca.alterarSenha(usuario);

				Messages.adicionaMensagemDeInfo(TemplateMessageHelper
						.getMessage(MensagensSistema.SISTEMA,
								"msgSenhaAlteradaSucesso", fc.getViewRoot()
										.getLocale()));

				houveErro = false;

				return "";
			} else {

				houveErro = true;
				Messages.adicionaMensagensDeErro(errorMessages);
				return null;
			}

		} catch (BusinessException e) {
			e.printStackTrace();
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, e));
			houveErro = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	public void limparAlterarSenha() {
		senhaAtual = "";
		senhaTemp = "";
		confPassword = "";
	}

	public void limparEsqSenha() {
		email = "";
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public boolean isEdicao() {
		return edicao;
	}

	public void setEdicao(boolean edicao) {
		this.edicao = edicao;
	}

	public boolean isNovoUsuario() {
		return novoUsuario;
	}

	public void setNovoUsuario(boolean novoUsuario) {
		this.novoUsuario = novoUsuario;
	}

	public List<SelectItem> getStatusUsuario() {

		statusUsuario = new ArrayList<SelectItem>();
		for (StatusUsuario status : StatusUsuario.values()) {

			statusUsuario.add(new SelectItem(status, TemplateMessageHelper
					.getMessage(MensagensSistema.USUARIOS, status.toString())));
		}

		return statusUsuario;
	}

	public void setStatusUsuario(List<SelectItem> statusUsuario) {
		this.statusUsuario = statusUsuario;
	}

	public List<SelectItem> getUsuarioPerfils() {
		return usuarioPerfils;
	}

	public void setUsuarioPerfils(List<SelectItem> usuarioPerfils) {
		this.usuarioPerfils = usuarioPerfils;
	}

	public List<SelectItem> getComboSimNao() {
		comboSimNao = commonsList.listaSimNao();
		return comboSimNao;
	}

	public void setComboSimNao(List<SelectItem> comboSimNao) {
		this.comboSimNao = comboSimNao;
	}

	public ConverterSimNao getConverterSimNao() {
		return converterSimNao;
	}

	public void setConverterSimNao(ConverterSimNao converterSimNao) {
		this.converterSimNao = converterSimNao;
	}

	public String getConfPassword() {
		return confPassword;
	}

	public void setConfPassword(String confPassword) {
		this.confPassword = confPassword;
	}

	public String getTipoAlteracao() {
		return tipoAlteracao;
	}

	public void setTipoAlteracao(String tipoAlteracao) {
		this.tipoAlteracao = tipoAlteracao;
	}

	public MBeanSessaoUsuario getSessao() {
		return sessao;
	}

	public String getSenhaAtual() {
		return senhaAtual;
	}

	public void setSenhaAtual(String senhaAtual) {
		this.senhaAtual = senhaAtual;
	}

	public String getSenhaTemp() {
		return senhaTemp;
	}

	public void setSenhaTemp(String senhaTemp) {
		this.senhaTemp = senhaTemp;
	}

	public boolean isHouveErro() {
		return houveErro;
	}

	public void setHouveErro(boolean houveErro) {
		this.houveErro = houveErro;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<SelectItem> getTipoUsuario() {
		FacesContext fc = FacesContext.getCurrentInstance();

		tipoUsuario = new ArrayList<SelectItem>();
		for (TipoUsuario tipo : TipoUsuario.values()) {
			tipoUsuario.add(new SelectItem(tipo, TemplateMessageHelper
					.getMessage(MensagensSistema.USUARIOS, tipo.toString(), fc
							.getViewRoot().getLocale())));
		}
		return tipoUsuario;
	}

	public void setTipoUsuario(List<SelectItem> tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	public String getMensagemErro() {
		return mensagemErro;
	}

	public void setMensagemErro(String mensagemErro) {
		this.mensagemErro = mensagemErro;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getResetarSenha() {
		return resetarSenha;
	}

	public void setResetarSenha(Boolean resetarSenha) {
		this.resetarSenha = resetarSenha;
	}

	public Usuario getSelectUser() {
		return selectUser;
	}

	public void setSelectUser(Usuario selectUser) {
		this.selectUser = selectUser;
	}

	public DualListModel<Perfil> getPerfisUsers() {
		return perfisUsers;
	}

	public void setPerfisUsers(DualListModel<Perfil> perfisUsers) {
		this.perfisUsers = perfisUsers;
	}

	public List<Perfil> getSource() {
		return source;
	}

	public void setSource(List<Perfil> source) {
		this.source = source;
	}

	public List<Perfil> getTarget() {
		return target;
	}

	public void setTarget(List<Perfil> target) {
		this.target = target;
	}

	public ConverterUtil<Perfil> getConverterPerfis() {
		return converterPerfis;
	}

	public void setConverterPerfis(ConverterUtil<Perfil> converterPerfis) {
		this.converterPerfis = converterPerfis;
	}

	/**
	 * @return the admin
	 */
	public boolean isAdmin() {
		return admin;
	}

	/**
	 * @param admin
	 *            the admin to set
	 */
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	/**
	 * @return the comboPessoaJuridica
	 */
	public List<PessoaJuridica> getComboPessoaJuridica() {
		return comboPessoaJuridica;
	}

	/**
	 * @param comboPessoaJuridica
	 *            the comboPessoaJuridica to set
	 */
	public void setComboPessoaJuridica(List<PessoaJuridica> comboPessoaJuridica) {
		this.comboPessoaJuridica = comboPessoaJuridica;
	}

	/**
	 * @return the agentesCargas
	 */
	public List<PessoaJuridica> getAgentesCargas() {
		return agentesCargas;
	}

	/**
	 * @param agentesCargas
	 *            the agentesCargas to set
	 */
	public void setAgentesCargas(List<PessoaJuridica> agentesCargas) {
		this.agentesCargas = agentesCargas;
	}

	/**
	 * @return the despachante
	 */
	public List<PessoaJuridica> getDespachante() {
		return despachante;
	}

	/**
	 * @param despachante
	 *            the despachante to set
	 */
	public void setDespachante(List<PessoaJuridica> despachante) {
		this.despachante = despachante;
	}

	/**
	 * @return the pessoaJuridica
	 */
	public PessoaJuridica getPessoaJuridica() {
		return pessoaJuridica;
	}

	/**
	 * @param pessoaJuridica
	 *            the pessoaJuridica to set
	 */
	public void setPessoaJuridica(PessoaJuridica pessoaJuridica) {
		this.pessoaJuridica = pessoaJuridica;
	}

}
