package br.com.ilog.geral.presentation;

import br.cits.commons.citspresentation.messages.ArquivosMensagens;

public enum MensagensSistema implements ArquivosMensagens {
	
	USUARIOS("usuarios"),
	PERFIS("perfis"),
	MENU("menu"),
	SISTEMA("sistema"),
	
	CONTINENTE("continente"),
	PAIS("pais"),
	CIDADE("cidade"),
	ESTADO("estado"),
	TERMINAL("terminal"),
	PROJETO("projeto"),
	INCOTERM("incoterms"),
	MODAL("modal"),
	MOEDA("moeda"),
	MATERIA_PRIMA("materiaprima"),
	MOTIVO("motivos"),
	STATUS("status"),
	TIPO_PACOTE("tipopacote"),
	TIPO_OCORRENCIA("tipoocorrencia"),
	LOGIN("login"),
	DEPARTAMENTO("departamento"),
	FERIADO("feriado"),
	CHECKLIST("checklist"),
	TIPOPESSOA("tipopessoa"),
	PESSOA_JURIDICA("pessoajuridica"),
	ROTA("rota"),
	CONDICAO_PAGAMENTO("condicao_pagamento"),
	FRETE("frete"),
	CARGA("carga"),
	PARAMETROS_CANAL("parametros_canal"),
	SIMULAR("simular"),
	ROLES("roles"),
	EMAIL("email"),
	FILIAL("filial"),
	NIVEL("nivel"),
	BROKER("broker"),
	UNIDADE_MEDIDA("unidademedida"),
	OCORRENCIAS("ocorrencia"),
	RASTREAR_CARGA("rastrearcarga"),
	RELATORIO("relatorio"),
	CHECKLIST_INVOICE("checklistinvoice"),
	INVOICE("invoice"),
	FOLLOW_UP("followup"),
	PO("po"), 
	CARGA_EXP("cargaexp"), 
	MAPACUSTO("mapacusto"),
	EXP_SAP("expSap");

	private String path = "br.com.ilog.geral.presentation";
	private String nomeArquivo;

	private MensagensSistema(String str) {
		this.nomeArquivo = path + "." + str;
	}

	public static String getDefault() {
		return "br.com.frisbee.geral.presentation";
	}

	@Override
	public String getCaminhoDoArquivo() {
		return nomeArquivo;
	}

}
