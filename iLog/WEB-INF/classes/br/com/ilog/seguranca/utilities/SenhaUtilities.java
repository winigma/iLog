package br.com.ilog.seguranca.utilities;

import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import br.cits.commons.citsbusiness.util.Utils;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;
import br.com.ilog.seguranca.business.entity.Usuario;

/**
 * @author Heber Santiago
 *
 */
public class SenhaUtilities {

	protected static String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-zA-Z]).{5,15})";

	protected static String REPETICAO_PATTERN = "(\\w)\\1+";

	protected static int SIZE_PATTERN = 5;

	protected static Locale LOCALE = new Locale("pt");
	
	/**
	 * @param senha
	 * @return
	 * @author Heber Santiago
	 * @since 19/05/2011
	 * 
	 *        Metodo auxiliar utilizado para validar a senha de acordo com as
	 *        seguintes regras: _ A senha dever� ter no m�nimo 06 (seis)
	 *        caracteres; _ A senha dever� conter pelo menos uma letra e um
	 *        n�mero; _ A senha n�o poder� conter caracteres repetidos em
	 *        sequ�ncia; _ A senha n�o poder� ser igual ao nome de login; _ A
	 *        senha ser� sens�vel a letras mai�sculas e min�sculas.
	 * 
	 */
	public static String validaSenha(String senha, Usuario usuario) {

		if (!StringUtils.isBlank(senha)) {

			if (usuario.getNome().equals(senha)) {

				return TemplateMessageHelper.getMessage(
						MensagensSistema.SISTEMA, "MSG_SENHA_EQUAL_LOGIN",
						LOCALE);

			}

			if (usuario.getLogin().equals(senha)) {

				return TemplateMessageHelper.getMessage(
						MensagensSistema.SISTEMA, "MSG_SENHA_EQUAL_LOGIN",
						LOCALE);

			}

			if (senha.length() < SIZE_PATTERN) {

				return TemplateMessageHelper.getMessage(
						MensagensSistema.SISTEMA, "MSG_SIZE_SENHA", LOCALE);
			}

			Pattern password = Pattern.compile(PASSWORD_PATTERN);

			Matcher senhaValida = password.matcher(senha);

			if (!senhaValida.find()) {

				return TemplateMessageHelper.getMessage(
						MensagensSistema.SISTEMA, "MSG_SENHA_INVALIDA", LOCALE);
			}

			Pattern repeticaoPattern = Pattern.compile(REPETICAO_PATTERN);

			Matcher repeticao = repeticaoPattern.matcher(senha);

			if (repeticao.find()) {

				return TemplateMessageHelper.getMessage(
						MensagensSistema.SISTEMA, "MSG_CARATERES_REPETIDOS",
						LOCALE);
			}

		}
		return null;

	}

	public static String criptografaSenha(String valor , Date data) throws Exception {		
		return Utils.md5(valor);
	}
	
}
