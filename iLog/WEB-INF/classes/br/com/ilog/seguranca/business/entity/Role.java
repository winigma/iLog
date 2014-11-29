package br.com.ilog.seguranca.business.entity;

import java.util.ArrayList;
import java.util.List;

public enum Role {
	
	/**
	 * Legend: 
	 * C - Cadastrar; V - Visualizar
	 * REL - Relatorio
	 */

	C_PAIS, V_PAIS,					// PAIS
	C_FILIAL, V_FILIAL,				// FILIAL
	C_NIVEL, V_NIVEL,				// NIVEL
	C_UMD,V_UMD,					// UNIDADE MEDIDA
	C_MP, V_MP, 					// MATERIA PRIMA
	C_PRJ, V_PRJ, 					// PROJETO
	C_EST, V_EST,					// ESTADO
	C_CID, V_CID,					// CIDADE
	C_TERM, V_TERM,					// TERMINAL
	C_PJ, V_PJ,						// PESSOA JURIDICA
	C_CKLST, V_CKLST,				// CHEKLIST
	C_USR, V_USR,					// USUARIO
	C_PRFL, V_PRFL,					// PERFIL
	C_T_OCOR, V_T_OCOR,				// TIPO OCORRENCIA
	C_MOT, V_MOT,					// MOTIVO
	C_ROTA, V_ROTA,					// ROTA
	C_FRETE, V_FRETE,				// FRETE
	C_FRDO, V_FRDO,					// FERIADO
	C_P_CNL,V_P_CNL,				// PARAMENTRO CANAL
	C_INCOT,V_INCOT,				// INCOTERM
	C_PCT,V_PCT,					// PACOTE
	C_INVC,V_INVC,					// INVOICE
	C_OCOR,V_OCOR,					// OCORRENCIA
	C_DPTO,V_DPTO,					// DEPARTAMENTO
	C_FLWUP,V_FLWUP,				// FOLLOW UP
	C_INV_EXP, V_INV_EXP,			// INVOICE EXPORT
	C_TNAC,							// TRANSPORTE NACIONAL
	V_TNAC,
	C_FLWUP_EXP,					// FOLLOW EXP
	V_FLWUP_EXP,
	C_MODAL, V_MODAL,				// MODAL
	C_MOEDA, V_MOEDA,				// MOEDA
	C_OCOR_TN,V_OCOR_TN, 			// OCORRENCIA TRANSPORTE NACIONAL
	C_OCOR_EXP, V_OCOR_EXP,			// OCORRENCIA EXPORTACAO
	C_PNAC, V_PNAC,					// PEDIDO NACIONAL
	C_P_CONT, V_P_CONT,				// PARAMETRO CONTINENTE
	AP_INV,V_AP_INV,				// APROVAR INVOICE
	VA_INV,V_VA_INV,				// VALIDAR INVOICE
	CO_INV,V_CO_INV,				// CONSOLIDAR
	SIM_TRNS,						// SIMULAR TRANSPORTE 
	PNL_FLWUP, 						// PAINEL FOLLOW UP
	RSTR_CARGA,						// RASTREAR CARGA
	REL_T_MED, 						// RELATORIO TEMPO MEDIO
	REL_IMP,						// RELATORIO IMPORTACAO
	REL_EXP, 						// EXPORTACAO
	REL_C_TN, 						// CUSTO TRANSPORTE NACIONAL
	REL_A_TN,						// ATRASO TRANSPORTE NACIONAL
	REL_TN, 						// RELATORIO TRANSPORTE NACIONAL
	CNF_CARGA,						// CONFIRMA CARGA
	REL_QGROW, 						// QUALITY GROWTH
	REL_WKBAS,						// WEEKLY BASIS 
	REL_PNAC,						// PEDIDO NACIONAL
	V_P_EMB,C_P_EMB,				// Planejar embarque
	V_E_EMB,C_E_EMB,				// EDITAR embarque
	V_IMP_PO,C_IMP_PO,				// IMPORTAR PO
	V_IMP_BRK,C_IMP_BRK,			// IMPORTAR BROKER
	V_CUSTO,C_CUSTO,				// CUSTO IMPORTACAO
	
	A_CARGA,DESPACH,				//PERFIL PARA TIPOS DE USER: AGENTE DE CARGAS E DESPACHANTE
	
	EXP_SAP							//EXPORTACAO ARQUIVO SAP
	,V_CARGAEXP, C_CARGAEXP         //EXPORTAÇÃO
	;
	
	/**
	 * Retorna as Roles do utilizadas no sistema.
	 * @return
	 */
	public static Role[] getValores() {
		
		Role [] role = { 
				C_PAIS, V_PAIS,					// PAIS
				C_EST, V_EST,					// ESTADO
				C_CID, V_CID,					// CIDADE
				C_TERM, V_TERM,					// TERMINAL
				C_PRJ, V_PRJ, 					// PROJETO
				C_FRDO, V_FRDO,					// FERIADO
				C_CKLST, V_CKLST,				// CHEKLIST
				C_PJ, V_PJ,						// PESSOA JURIDICA
				C_MODAL, V_MODAL,				// MODAL
				C_DPTO,V_DPTO,					// DEPARTAMENTO
				C_MP, V_MP, 					// MATERIA PRIMA
				C_INCOT,V_INCOT,				// INCOTERM
				C_PCT,V_PCT,					// PACOTE
				C_MOEDA, V_MOEDA,				// MOEDA
				C_ROTA, V_ROTA,					// ROTA
				C_T_OCOR, V_T_OCOR,				// TIPO OCORRENCIA
				C_MOT, V_MOT,					// MOTIVO
				C_FILIAL, V_FILIAL,				// FILIAL
				C_NIVEL, V_NIVEL,				// NIVEL
				C_UMD,V_UMD,					// UNIDADE MEDIDA
				C_P_CNL,V_P_CNL,				// PARAMENTRO CANAL
				C_P_CONT, V_P_CONT,				// PARAMETRO CONTINENTE
				
				V_P_EMB,C_P_EMB,				// PLANNING SHIPMENT
				V_IMP_PO,C_IMP_PO,				// IMPORTAR PO
				C_INVC,V_INVC,					// INVOICE
				V_IMP_BRK,C_IMP_BRK,			// IMPORTAR BROKER
				V_E_EMB,C_E_EMB,				// EDITAR embarque
				SIM_TRNS,						// SIMULAR TRANSPORTE 
				C_FLWUP,V_FLWUP,				// FOLLOW UP
				C_OCOR,V_OCOR,					// OCORRENCIA
				PNL_FLWUP, 						// PAINEL FOLLOW UP
				RSTR_CARGA,						// RASTREAR CARGA
				V_CUSTO,C_CUSTO,				// CUSTO IMPORTACAO
				
				C_USR, V_USR,					// USUARIO
				C_PRFL, V_PRFL,					// PERFIL
				
				REL_T_MED, 						// RELATORIO TEMPO MEDIO
				REL_IMP,						// RELATORIO IMPORTACAO
				REL_QGROW, 						// QUALITY GROWTH
				REL_WKBAS,						// WEEKLY BASIS
				A_CARGA,DESPACH,				//PERFIL PARA TIPOS DE USER: AGENTE DE CARGAS E DESPACHANTE
				EXP_SAP,
				V_CARGAEXP, C_CARGAEXP          //CARGA EXPORTAÇÃO
				};
		
		return role;
	}
	
	/**
	 * Retorna a String das roles.
	 * @return
	 */
	public static List<String> getValoresString() {
		
		List<String> roles = new ArrayList<String>();
		for (Role role : getValores()) {
			roles.add( role.toString() );
		}
		
		return roles;
	}
	public static List<Role> getValoresLista() {
		List<Role> roles = new ArrayList<Role>();
		
		for (Role role : getValores()) {
			roles.add( role );
		}
		
		return roles;
	}
}
