package br.com.ilog.cadastro.business.entityFilter;

import java.io.Serializable;
import java.util.Date;

import br.com.ilog.cadastro.business.entity.Cidade;
import br.com.ilog.cadastro.business.entity.Motivo;
import br.com.ilog.cadastro.business.entity.TipoOcorrencia;
import br.com.ilog.importacao.business.entity.Carga;

public class BasicFiltroOcorrencia implements Serializable {
	
	private static final long serialVersionUID = 6499552766153569869L;
	
	private String descricao;
	private Date dtOcorrencia;
	private Motivo motivo;
	private TipoOcorrencia tipoOcorrencia;
	private String aps;
	private String hawb;
	private Date dtInicioOcorrencia;
    private Date dtFimOcorrencia;
    private Carga carga;
    private Cidade cidade;
    private Boolean ativo;
    private Boolean lom;
    private Boolean canal;
    
    public BasicFiltroOcorrencia() {
    	super();
    }
    
    public BasicFiltroOcorrencia( Carga carga ) {
    	this.carga = carga;
    }
    
    public BasicFiltroOcorrencia( Carga carga, boolean ativo ) {
    	this.carga = carga;
    	this.ativo = ativo;
    }
    
    /**
     * Construtor do filtro de ocorrencia import.
     * @param carga
     * @param cidade
     * @param lom
     * @param canal
     * @param ativo
     */
    public BasicFiltroOcorrencia( Carga carga, Cidade cidade, Boolean lom, Boolean canal, Boolean ativo ) { this.carga = carga;
    	this.cidade = cidade;
    	this.lom = lom;
    	this.canal = canal;
    	this.ativo = ativo;
    }
    
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getDtOcorrencia() {
		return dtOcorrencia;
	}

	public void setDtOcorrencia(Date dtOcorrencia) {
		this.dtOcorrencia = dtOcorrencia;
	}

	public Motivo getMotivo() {
		return motivo;
	}

	public void setMotivo(Motivo motivo) {
		this.motivo = motivo;
	}

	public TipoOcorrencia getTipoOcorrencia() {
		return tipoOcorrencia;
	}

	public void setTipoOcorrencia(TipoOcorrencia tipoOcorrencia) {
		this.tipoOcorrencia = tipoOcorrencia;
	}

	public String getAps() {
		return aps;
	}

	public void setAps(String aps) {
		this.aps = aps;
	}

	public String getHawb() {
		return hawb;
	}

	public void setHawb(String hawb) {
		this.hawb = hawb;
	}

	public Date getDtInicioOcorrencia() {
		return dtInicioOcorrencia;
	}

	public void setDtInicioOcorrencia(Date dtInicioOcorrencia) {
		this.dtInicioOcorrencia = dtInicioOcorrencia;
	}

	public Date getDtFimOcorrencia() {
		return dtFimOcorrencia;
	}

	public void setDtFimOcorrencia(Date dtFimOcorrencia) {
		this.dtFimOcorrencia = dtFimOcorrencia;
	}

	public Carga getCarga() {
		return carga;
	}

	public void setCarga(Carga carga) {
		this.carga = carga;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	/**
	 * @return the ativo
	 */
	public Boolean getAtivo() {
		return ativo;
	}

	/**
	 * @param ativo the ativo to set
	 */
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Boolean getLom() {
		return lom;
	}

	public void setLom(Boolean lom) {
		this.lom = lom;
	}

	public Boolean getCanal() {
		return canal;
	}

	public void setCanal(Boolean canal) {
		this.canal = canal;
	}

}
