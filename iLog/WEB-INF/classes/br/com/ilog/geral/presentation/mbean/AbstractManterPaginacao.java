package br.com.ilog.geral.presentation.mbean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import br.cits.commons.citspresentation.mbeans.AbstractManter;
import br.cits.commons.citspresentation.mbeans.RegistrosPorPagina;
import br.com.ilog.geral.presentation.TemplateMessageHelper;



/**
 * 
 * @author Wisley Souza
 * @param Classe Abstrata que herda AbstractManter
 * @param adiciona os métodos de getScroollinfo para paginação e getTotalRegistros
 *
 */
public abstract class AbstractManterPaginacao extends AbstractManter {

	private static final long serialVersionUID = 1L;
	private Integer paginaAtual;
	private byte registrosPagina = RegistrosPorPagina.r10.getValor();
	
	public abstract int getTotalRegistros();
	
	public String getScrollerInfo() {

		int regInicial = ( this.getPaginaAtual() - 1 ) * this.getRegistrosPagina() + 1;
		int regFinal = Math.min( regInicial + this.getRegistrosPagina() - 1, this.getTotalRegistros());
		
		String retorno = "";
		if ( regFinal != 0 )
			retorno = regInicial + " - " + regFinal + " "
					+ TemplateMessageHelper.getMessage("lblDe") + " "
					+ this.getTotalRegistros();
		
		return retorno;
	}

	public Integer getPaginaAtual() {
		if ( paginaAtual != null )
			return paginaAtual;
		return 0;
	}

	public void setPaginaAtual(Integer paginaAtual) {
		this.paginaAtual = paginaAtual;
	}

	public byte getRegistrosPagina() {
		return registrosPagina;
	}

	public void setRegistrosPagina(byte registrosPagina) {
		this.registrosPagina = registrosPagina;
	}

	public List<SelectItem> getCboRegistrosPagina() {
		List<SelectItem> lista = new ArrayList<SelectItem>();
		
		for (RegistrosPorPagina reg : RegistrosPorPagina.values()) {
			lista.add( new SelectItem( reg.getValor() ) );
		}
		return lista;
	}
	
	
	
}
