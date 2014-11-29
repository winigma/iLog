package br.com.ilog.importacao.business.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import br.com.ilog.cadastro.business.entity.Ocorrencia;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;
import br.com.ilog.geral.presentation.util.DataUtils;
import br.com.ilog.importacao.business.entity.Carga;
import br.com.ilog.importacao.business.entity.FollowUpImportTrecho;
import br.com.ilog.importacao.business.entity.StatusCarga;

/**
 * @author Heber Santiago
 * @since 07/12/2011
 * 
 * @brief Classe de auxilio para construcao do painel de logistica.
 *
 */
public class PainelLogistica {

	private Carga carga;
	
	private boolean atradasada;
	
	private Integer dias;
	
	private List<FollowUpImportTrecho> trechos;
	
	private Map<Long, List<Ocorrencia>> ocorrencias;
	
	private int tamanhoFundo;
	
	private int diffTamanho;
	
	private Date dtPrevista;
	
	private Date dtReal;
	
	
	public Carga getCarga() {
		return carga;
	}

	public void setCarga(Carga carga) {
		this.carga = carga;
	}

	public boolean isAtradasada() {
		return atradasada;
	}

	public void setAtradasada(boolean atradasada) {
		this.atradasada = atradasada;
	}

	public List<FollowUpImportTrecho> getTrechos() {
		return trechos;
	}

	public void setTrechos(List<FollowUpImportTrecho> trechos) {
		this.trechos = trechos;
	}

	public Map<Long, List<Ocorrencia>> getOcorrencias() {
		return ocorrencias;
	}

	public void setOcorrencias(Map<Long, List<Ocorrencia>> ocorrencias) {
		this.ocorrencias = ocorrencias;
	}

	/**
	 * @param trecho
	 * @return
	 */
	public String getOcorrenciasTrecho( FollowUpImportTrecho trecho ) {
		if ( !trecho.isCanal() && !trecho.getTxLocal().equals("LOM") ) {
			if ( trecho.getCidade() != null && trecho.getCidade().getId() != null ) {
				
				if ( getOcorrencias().containsKey( trecho.getCidade().getId() ) ){
					return TemplateMessageHelper.getMessage(
							MensagensSistema.CARGA, "lblOcorrencias")
							+ " ("
							+ getOcorrencias().get( trecho.getCidade().getId() ).size() + ")";
				}
			}
			return TemplateMessageHelper.getMessage(
					MensagensSistema.CARGA, "lblOcorrencias")
					+ " (0)";
		}
		return "";
	}
	
	public List<Ocorrencia> getOcorrencias( FollowUpImportTrecho trecho ) {
		
		if ( !trecho.isCanal() && !trecho.getTxLocal().equals("LOM") ) {
			if ( trecho.getCidade() != null && trecho.getCidade().getId() != null ) {
				
				if ( getOcorrencias().containsKey( trecho.getCidade().getId() ) ){
					return getOcorrencias().get( trecho.getCidade().getId() );
				}
			}
			return new ArrayList<Ocorrencia>( 0 );
		}
		
		return null;
	}
	/**
	 * Recupera a quantidade de dias do FollowUp.
	 * @return
	 */
	public Integer getDias() {
		
		if ( trechos != null && !trechos.isEmpty() ) {
			if ( carga != null && !carga.getStatus().equals( StatusCarga.F )  ) {
				Date d = trechos.get( 0 ).getDtRealizado();
				
				Date fim = getDtReal();
				if ( fim != null ) {
					dias = DataUtils.diferencaEmDias( d, fim );
				} else { 
					dias = DataUtils.diferencaEmDias( d, new Date() );
				}
			} else {
				dias = trechos.get( 0 ).getFollowUp().getTotalDiasAtual();
			}
		}
		
		return dias;
	}

	public void setDias(Integer dias) {
		this.dias = dias;
	}

	public int getTamanhoFundo() {
		return tamanhoFundo;
	}

	public void setTamanhoFundo(int tamanhoFundo) {
		this.tamanhoFundo = tamanhoFundo;
	}

	public int getDiffTamanho() {
		return diffTamanho;
	}

	public void setDiffTamanho(int diffTamanho) {
		this.diffTamanho = diffTamanho;
	}

	public Date getDtPrevista() {
		return dtPrevista;
	}

	public void setDtPrevista(Date dtPrevista) {
		this.dtPrevista = dtPrevista;
	}

	public Date getDtReal() {
		return dtReal;
	}

	public void setDtReal(Date dtReal) {
		this.dtReal = dtReal;
	}
	
}
