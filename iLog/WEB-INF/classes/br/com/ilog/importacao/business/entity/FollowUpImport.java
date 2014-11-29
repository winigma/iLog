package br.com.ilog.importacao.business.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.NotNull;

import br.cits.commons.citsbusiness.hibernate.validator.Label;
import br.cits.commons.citsbusiness.util.Identificavel;
import br.com.ilog.cadastro.business.entity.Moeda;
import br.com.ilog.seguranca.business.entity.Usuario;

@Entity
@Table( name = "FOLLOW_UP_IMPORT" )
public class FollowUpImport implements Identificavel<Long> {

	/**  */
	private static final long serialVersionUID = -660845538778811328L;

	@Id
	@SequenceGenerator( name = "gen", sequenceName = "follow_up_import_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	private Long id;
	
	@Column( name = "OBSERVACAO", length = 250 )
	private String observacao;
	
	@Temporal( TemporalType.DATE )
	@Column( name = "DT_ENTREGA_PLAN" )
	@NotNull(message="notnull")
	@Label( "lblDtEntregaPlan" )
	private Date dtEntregaPlanejada;
	
	@JoinColumn( name = "ID_CARGA", nullable = false )
	@ManyToOne( fetch = FetchType.LAZY )
	@Label( "lblCarga" )
	@NotNull(message="notnull")
	private Carga carga;
	
	@JoinColumn( name = "ID_MOEDA" )
	@ManyToOne( fetch = FetchType.LAZY )
	private Moeda moeda;

	@JoinColumn( name = "ID_RESPONSAVEL" )
	@ManyToOne( fetch = FetchType.LAZY )
	private Usuario responsavel;
	
	@Column( name = "VL_FRETE", scale = 5, precision = 11 )
	private BigDecimal valorFrete;
	
	@Column( name = "TOTAL_DIAS_ESTIMADO" )
	private Integer totalDiasEstimado;
	
	@Column( name = "TOTAL_DIAS_ATUAL" )
	private Integer totalDiasAtual;
	
	@OneToMany(mappedBy = "followUp", fetch = FetchType.LAZY)
	@Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
	@OnDelete(action = OnDeleteAction.CASCADE)	
	@OrderBy("id")
	private List<FollowUpImportTrecho> trechosFollowUp;
	
	
	
	@Transient
	private Integer otd;
	
	@Transient
	private Date dtRealizado;
	
	@Column( name = "ATIVO" )
	private boolean ativo;
	
	@JoinColumn( name = "ID_AUTOR_CANCELAMENTO" )
	@ManyToOne( fetch = FetchType.LAZY )
	private Usuario autorCancelamento;
	
	@Override
	public Long getPK() {
		return id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDtEntregaPlanejada() {
		return dtEntregaPlanejada;
	}

	public void setDtEntregaPlanejada(Date dtEntregaPlanejada) {
		this.dtEntregaPlanejada = dtEntregaPlanejada;
	}

	public Carga getCarga() {
		return carga;
	}

	public void setCarga(Carga carga) {
		this.carga = carga;
	}

	public List<FollowUpImportTrecho> getTrechosFollowUp() {
		return trechosFollowUp;
	}

	public void setTrechosFollowUp(List<FollowUpImportTrecho> trechosFollowUp) {
		this.trechosFollowUp = trechosFollowUp;
	}

	public Integer getTotalDiasEstimado() {
		return totalDiasEstimado;
	}

	public void setTotalDiasEstimado(Integer totalDiasEstimado) {
		this.totalDiasEstimado = totalDiasEstimado;
	}

	public Integer getTotalDiasAtual() {
		return totalDiasAtual;
	}

	public void setTotalDiasAtual(Integer totalDiasAtual) {
		this.totalDiasAtual = totalDiasAtual;
	}

	public String getStyleDias() {
		if ( totalDiasAtual != null && totalDiasAtual > totalDiasEstimado )
			return "color: RED;";
		return "";
	}

	public Moeda getMoeda() {
		return moeda;
	}

	public void setMoeda(Moeda moeda) {
		this.moeda = moeda;
	}

	public BigDecimal getValorFrete() {
		return valorFrete;
	}

	public void setValorFrete(BigDecimal valorFrete) {
		this.valorFrete = valorFrete;
	}

	/**
	 * @return the responsavel
	 */
	public Usuario getResponsavel() {
		return responsavel;
	}

	/**
	 * @param responsavel the responsavel to set
	 */
	public void setResponsavel(Usuario responsavel) {
		this.responsavel = responsavel;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	/**
	 * @return the otd
	 */
	public Integer getOtd() {
		otd =this.trechosFollowUp.get(this.trechosFollowUp.size()-1).getOtd();
		return otd;
	}

	/**
	 * @param otd the otd to set
	 */
	public void setOtd(Integer otd) {
		this.otd = otd;
	}

	/**
	 * @return the dtRealizado
	 */
	public Date getDtRealizado() {
		dtRealizado = trechosFollowUp.get(trechosFollowUp.size()-1).getDtRealizado();
		return dtRealizado;
	}

	/**
	 * @param dtRealizado the dtRealizado to set
	 */
	public void setDtRealizado(Date dtRealizado) {
		this.dtRealizado = dtRealizado;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public Usuario getAutorCancelamento() {
		return autorCancelamento;
	}

	public void setAutorCancelamento(Usuario autorCancelamento) {
		this.autorCancelamento = autorCancelamento;
	}

	
}
