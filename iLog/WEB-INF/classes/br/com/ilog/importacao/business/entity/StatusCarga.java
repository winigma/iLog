package br.com.ilog.importacao.business.entity;

public enum StatusCarga {
	/**
	 * @Legend CO =  Consolidada
	 *         C = Cancelado
	 *         AC = aguardando Coleta
	 *         CL = Coletada
	 *         TBS = A ser Coletada/To Be Collected
	 *         OHI = Em Espera em/On Hold in
	 *         ITT = A Caminho de/In Transit to
	 *         ICC = In ... Customs Clearance/Desembaraco
	 *         AT = Em/At
	 *         F = Fechado
	 *         P = Planejada
	 **/
	CO, TBS, OHI,
	
	ICC, C, CL, ITT, AT, F, P, AC;

	/**
	 * Lista de Status para pesquisa de embarque
	 * @return
	 */
	public static StatusCarga[] getValuesPsqEmbarque() {
		
		StatusCarga[] value = { P, AC, CL };
		
		return value;
	}
	
	/**
	 * Recupera todos os possiveis valores para o Status de Carga.
	 * @return
	 */
	public static StatusCarga[] getValores() {
		StatusCarga[] value = { P, AC, CL, ITT, OHI, ICC, AT, F, C };
		return value;
	}
	
	/**
	 * Retorna os valores possiveis para FollowUp
	 * @return
	 */
	public static StatusCarga[] getValuesFollowUp() {

		StatusCarga[] value = { TBS, OHI, ITT, ICC, AT, F };
		return value;
	}
	
	/**
	 * Retorna os valores possiveis para o painel de FollowUp.
	 * @return
	 */
	public static StatusCarga[] getValuesPainelFollowUp() {
		StatusCarga[] value = { CO, TBS, OHI, ITT, ICC, AT };
		return value;
	}
}
