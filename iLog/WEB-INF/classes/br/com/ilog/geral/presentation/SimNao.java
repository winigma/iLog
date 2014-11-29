package br.com.ilog.geral.presentation;



/**
 * @author eugenio.filho
 *Enum para utilização de Sim ou Não.
 */
public enum SimNao {
	SIM(TemplateMessageHelper.getMessage("lblSim")),
	NAO(TemplateMessageHelper.getMessage("lblNao"));
	private String label;
	private SimNao(String label) {
		this.label = label;
	}
	
	/**
	 * @return String
	 * Retorna a opção label das propriedades do Enum
	 */
	public String getLabel(){
		return this.label;
	}
	
	/**
	 * @return String
	 * Retorna a opção label das propriedades do Enum
	 * com base em um boolean passado por parâmetro
	 */
	public static String getLabel(boolean sim){
		if(sim) return SIM.label;
		return NAO.label;
	}
	
	@Override
	public String toString() {
		return this.label;
	}
}
