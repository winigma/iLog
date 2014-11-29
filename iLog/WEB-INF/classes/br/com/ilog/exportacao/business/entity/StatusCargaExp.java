package br.com.ilog.exportacao.business.entity;

public enum StatusCargaExp {
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
	 C, CA, F;

	/**
	 * Lista de Status para pesquisa de embarque
	 * @return
	 */
	public static StatusCargaExp[] getValuesCargas() {
		
		StatusCargaExp[] value = { C,CA, F };
		
		return value;
	}
	
	/**
	 * Recupera todos os possiveis valores para o Status de Carga.
	 * @return
	 */
	public static StatusCargaExp[] getValores() {
		StatusCargaExp[] value = { C };
		return value;
	}
	
	/**
	 * Retorna os valores possiveis para FollowUp
	 * @return
	 */
	public static StatusCargaExp[] getValuesFollowUp() {

		StatusCargaExp[] value = {  F };
		return value;
	}
	
	/**
	 * Retorna os valores possiveis para o painel de FollowUp.
	 * @return
	 */
	public static StatusCargaExp[] getValuesPainelFollowUp() {
		StatusCargaExp[] value = {C };
		return value;
	}
}
