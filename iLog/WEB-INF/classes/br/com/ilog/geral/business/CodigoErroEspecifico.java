package br.com.ilog.geral.business;

import br.cits.commons.citsbusiness.exception.CodigoErro;

/**
 * @class CodigoErroEspecificos
 * @date 19/05/2011
 * @brief Definição da classe CodigoErroEspecificos
 */
public enum CodigoErroEspecifico implements CodigoErro {
		
	SENHA_INVALIDA("msgSenhaInvalida"),
	SENHAS_DIFERENTES("msgSenhasDiferentes"),
	USUARIO_IMPOSSIVEL_MUDAR_SENHA("msgUsuarioImpossivelMudarSenha"),
	PESSOA_REPETIDA("MSG0003"),	
	LOGIN_NAO_EXISTE("loginNExist"),
	LOGIN_SENHA_INVALIDA("senhaInvalida"),
	LOGIN_BLOQUEADO("loginBloqueado"),
	USUARIO_EMAIL_NAO_ENCONTRADO("msgEmailUsuarioNaoEncontrado"),
	SENHA_NAO_EXISTE("MSG9411"),
	SENHA_UTILIZADA("MSG_SENHA_UTILIZADA"), 
	SENHA_EXPIRADA("MSG9413"),	
	REGISTRO_NAO_ENCONTRADO("msgRegistroNaoEncontrado"),
	
	ERRO_DECONHECIDO( "MSG_GEN_002" ),
	MSG_ERRO_DONWLOAD_ARQUIVO( "msgErroDownloadArquivo" ),
	
	//MSG_FK_VIOLADA("msgFKViolada"),
	MSG_FK_VIOLADA("MSG009"),
	MSG_NAO_EXCLUIR("MSG009"),
	
	MSG_UNIQUE_VIOLADA("msgUniqueViolada"),
	MSG_KEY_VIOLADA("msgKeyViolada"),
	USUARIO_PK("MSG_USUARIO_PK"),
	UQ_USUARIO_LOGIN("MSG_USUARIO_LOGIN_UNIQUE"),
	UQ_USUARIO_EMAIL("MSG_USUARIO_EMAIL_UNIQUE"),	
	PERFIL_PK("MSG_PERFIL_PK"),
	PERFIL_ROLE_PK("MSG_PERFIL_ROLE_PK"),
	UQ_T008_PERFIL_ROLE__UN("MSG_PERFIL_ROLE_UNIQUE"),
	USUARIO_PERFIL_PK("MSG_USUARIO_PERFIL_PK"),
	CONEXAO_BANCO("MSG_001"),
	EMAIL_NAO_CADASTRADO("MSG054"),
	SIZE_FUNCIONALIDADE_PERFIL("MSG069"),
	
	PELO_MENOS_UM_PROCESSO			("mgsProcessObrigatorio"),
	
	PELO_MENOS_UM_TRECHO_ROTA		("msgSemTrechos"),
	TRECHO_INI_DIF_TRECHO_FIM		("MSG015"),
	TRECHO_INICIAL_DIF_ROTA			("MSG016"),
	TRECHO_FINAL_DIF_ROTA			("MSG017"),
	CAMPO_NOT_NULL	                ("notnull"),
	FORMATO_INVALIDO_INVOICE		("MSG022"),
	
	MSG022							("MSG022"),
	MSG024							("MSG024");
	
	private String idBundle;
	
	private CodigoErroEspecifico(String idBundle) {
		this.idBundle = idBundle;
	}
	
	@Override
	public String getIdBundle() {
		return idBundle;
	}
}
