package br.com.ilog.importacao.business.entity;

public enum StatusInvoice {
	/**
	 * @Legend 
	 *         V = Coleta Autorizada
	 *         A = Aprovada
	 *         EV = Autorizao Incompleta
	 *         NA = Não Aprovada
	 *         EA = Em Aprovação
	 *         R = Revalidada
	 *         CO = Consolidada
	 *         TBS = A ser enviado/To Be Shipped
	 *         OHI = Em Espera em/ On Hold in
	 *         
	 *         C =  Cadastrada
	 *         P = Planning
	 * 		   CL = Coletada
	 *         AC = aguardando Coleta
	 *         ITT = A Caminho de/In Transit to
	 *         AT = Em/At Factory
	 *         ICC = In ... Customs Clearence/Liberacao
	 *         F = Fechado
	 */
			
	V, A, EA, EV, NA, R, CO, 
	
	TBS, OHI, C, CL, ITT, AT, ICC, F, P, AC;
	
	
	public static StatusInvoice getStatus( StatusCarga status ) {
		for (StatusInvoice item : StatusInvoice.values()) {
			if ( item.name().equals( status.name() ) ) {
				return item;
			}
		}
		return null;
	}
	
	public static StatusInvoice[] valores() {
		StatusInvoice[] values = { C, P, AC, CL, AT, TBS, OHI, ITT, ICC, F };
		return values;
	}
	 
}
