package br.com.ilog.importacao.business.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.cits.commons.citsbusiness.util.Identificavel;

@Entity
@Table( name = "PROC_CARGA" )
public class ProcCarga implements Identificavel<Long> {

	/** */
	private static final long serialVersionUID = -5418636921163056062L;

	@Id
	@SequenceGenerator( name = "gen", sequenceName = "proc_carga_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	private Long id;
	
	@JoinColumn(name = "ID_PROC", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private ProcBroker procBroker;
	
	@Column( name = "NR_DOC_CARGA", length = 30 )
	private String nrDocCarga;
	
	@Column( name = "NR_CONTAINER", length = 30 )
	private String nrContainer;
	
	@Column( name = "CAPACIDADE")
	private Integer capacidade;
	
	@Column( name = "SELO", length = 20 )
	private String selo;
	
	@Override
	public Long getPK() {
		return id;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the procBroker
	 */
	public ProcBroker getProcBroker() {
		return procBroker;
	}

	/**
	 * @param procBroker the procBroker to set
	 */
	public void setProcBroker(ProcBroker procBroker) {
		this.procBroker = procBroker;
	}

	/**
	 * @return the nrDocCarga
	 */
	public String getNrDocCarga() {
		return nrDocCarga;
	}

	/**
	 * @param nrDocCarga the nrDocCarga to set
	 */
	public void setNrDocCarga(String nrDocCarga) {
		this.nrDocCarga = nrDocCarga;
	}

	/**
	 * @return the nrContainer
	 */
	public String getNrContainer() {
		return nrContainer;
	}

	/**
	 * @param nrContainer the nrContainer to set
	 */
	public void setNrContainer(String nrContainer) {
		this.nrContainer = nrContainer;
	}

	/**
	 * @return the capacidade
	 */
	public Integer getCapacidade() {
		return capacidade;
	}

	/**
	 * @param capacidade the capacidade to set
	 */
	public void setCapacidade(Integer capacidade) {
		this.capacidade = capacidade;
	}

	/**
	 * @return the selo
	 */
	public String getSelo() {
		return selo;
	}

	/**
	 * @param selo the selo to set
	 */
	public void setSelo(String selo) {
		this.selo = selo;
	}

}
