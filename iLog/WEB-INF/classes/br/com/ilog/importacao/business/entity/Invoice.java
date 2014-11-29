package br.com.ilog.importacao.business.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;

import br.cits.commons.citsbusiness.hibernate.validator.Label;
import br.cits.commons.citsbusiness.util.Identificavel;
import br.com.ilog.cadastro.business.entity.Cidade;
import br.com.ilog.cadastro.business.entity.CondicaoPagamento;
import br.com.ilog.cadastro.business.entity.Incoterm;
import br.com.ilog.cadastro.business.entity.Modal;
import br.com.ilog.cadastro.business.entity.Moeda;
import br.com.ilog.cadastro.business.entity.Nivel;
import br.com.ilog.cadastro.business.entity.Pais;
import br.com.ilog.cadastro.business.entity.PessoaJuridica;
import br.com.ilog.cadastro.business.entity.Terminal;
import br.com.ilog.cadastro.business.entity.TipoPacote;
import br.com.ilog.seguranca.business.entity.Usuario;

@Entity
@Table(name = "INVOICE")
public class Invoice implements Identificavel<Long> {

	/** */
	private static final long serialVersionUID = -1604031386192160967L;

	@Id
	@SequenceGenerator(name = "gen", sequenceName = "invoice_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
	private Long id;

	@Column(name = "NUM_SEQ")
	private int numSequencia;

	@Column(name = "ANO_SEQ")
	private int anoSequencia;

	@Label("lblNumero")
	@Column(name = "NUM_INVOICE", length = 20)
	@NotNull(message = "notnull")
	@NotEmpty
	private String numeroInvoice;

	@Temporal(TemporalType.DATE)
	@Column(name = "DT_EMISSAO")
	@Label("lblDataInvoice")
	@NotNull(message = "notnull")
	private Date dtEmissao;

	@Label("lblMoeda")
	@JoinColumn(name = "ID_MOEDA")
	@ManyToOne(fetch = FetchType.EAGER)
	private Moeda moeda;

	@Enumerated(EnumType.STRING)
	@Label("lblStatus")
	@NotNull(message = "notnull")
	private StatusInvoice status;

	@JoinColumn(name = "ID_CIDADE_ATUAL")
	@ManyToOne(fetch = FetchType.EAGER)
	private Cidade cidadeAtual;

	@Column(name = "PATH_ARQUIVO", length = 255)
	@Label("lblArquivo")
	private String pathArquivo;

	@Column(name = "FORMA_PAGAMENTO")
	@Enumerated(EnumType.STRING)
	@Label("lblFormaPagamento")
	private CondicaoPagamento condicaoPagamento;

	@Column(name = "VALOR_TOTAL", scale = 2, precision = 12)
	@Label("lblValorTotal")
	private BigDecimal valorTotal;

	@Column(name = "PESO_LIQUIDO", scale = 5, precision = 11)
	@Label("lblPesoLiquido")
	private BigDecimal pesoLiquido;

	@Column(name = "PESO_BRUTO", scale = 5, precision = 11)
	@Label("lblPesoBruto")
	private BigDecimal pesoBruto;

	@Column(name = "QTDE_PACOTES")
	@Label("lblQtdePacotes")
	private Integer qtdePacotes;

	@Column(name = "QTDE_PACOTES_2")
	@Label("lblQtdePacotes2")
	private Integer qtdePacotes2;

	@JoinColumn(name = "EXPORTADOR_ID", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	@Label("lblExportador")
	private PessoaJuridica exportador;

	@JoinColumn(name = "CARGA_ID", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	@Label("lblCarga")
	private Carga carga;

	@JoinColumn(name = "ID_INCOTERM", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	@Label("lblIncoterm")
	private Incoterm incoterm;

	@JoinColumn(name = "ID_TIPO_PACOTE", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	@Label("lblTipoPacote")
	private TipoPacote tipoPacote;

	@JoinColumn(name = "ID_TIPO_PACOTE_2", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	@Label("lblTipoPacote2")
	private TipoPacote tipoPacote2;

	@JoinColumn(name = "COMPRADOR_ID")
	@ManyToOne(fetch = FetchType.LAZY)
	@Label("lblComprador")
	private Usuario comprador;

	@JoinColumn(name = "ID_NIVEL")
	@ManyToOne(fetch = FetchType.LAZY)
	@Label("lblNivel")
	private Nivel nivel;

	@JoinColumn(name = "PAIS_ORIGEM_ID", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	@Label("lblOrigem")
	private Pais paisOrigem;

	@JoinColumn(name = "ID_MODAL")
	@ManyToOne(fetch = FetchType.EAGER)
	@Label("lblModal")
	private Modal tipoTransporte;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TERMINAL")
	@Label("lblTerminal")
	private Terminal terminal;

	@Column(name = "CRITICO")
	private boolean critico;

	@Column(name = "PENDENTE")
	private boolean pendente;

	@Column(name = "INVOICE_MANUAL")
	private boolean invoiceManual;

	@Column(name = "FABRICANTE")
	private String fabricante;

	@Column(name = "PLI", nullable = true)
	private Boolean pli;

	@Column(name = "NUM_PLI", nullable = true, length = 10)
	private String numPli;

	/**
	 * @comentario utilizado pra pegar o item selecionado na table
	 */
	@Transient
	private boolean select;

	@OneToMany(mappedBy = "invoice", fetch = FetchType.LAZY)
	@Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
	@OnDelete(action = OnDeleteAction.CASCADE)
	@OrderBy(value = "item vendorPartNum")
	private List<ItemInvoice> itensInvoice;

	@Transient
	private String pos;

	@Column(name = "PALAVRA_CHAVE", length = 250)
	private String palavrasChave;

	@Column(name = "OBSERVACAO", length = 250)
	private String observacao;

	public Invoice() {
		super();
	}

	public Invoice(Long id) {
		this.id = id;
	}

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

	public String getNumeroInvoice() {
		return numeroInvoice;
	}

	public void setNumeroInvoice(String numeroInvoice) {

		this.numeroInvoice = numeroInvoice;
	}

	public Date getDtEmissao() {
		return dtEmissao;
	}

	public void setDtEmissao(Date dtEmissao) {
		this.dtEmissao = dtEmissao;
	}

	public Moeda getMoeda() {
		return moeda;
	}

	public void setMoeda(Moeda moeda) {
		this.moeda = moeda;
	}

	public StatusInvoice getStatus() {
		return status;
	}

	public void setStatus(StatusInvoice status) {
		this.status = status;
	}

	public String getPathArquivo() {
		return pathArquivo;
	}

	public void setPathArquivo(String pathArquivo) {
		this.pathArquivo = pathArquivo;
	}

	public CondicaoPagamento getCondicaoPagamento() {
		return condicaoPagamento;
	}

	public void setCondicaoPagamento(CondicaoPagamento condicaoPagamento) {
		this.condicaoPagamento = condicaoPagamento;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public BigDecimal getPesoLiquido() {
		return pesoLiquido;
	}

	public void setPesoLiquido(BigDecimal pesoLiquido) {
		this.pesoLiquido = pesoLiquido;
	}

	public BigDecimal getPesoBruto() {
		return pesoBruto;
	}

	public void setPesoBruto(BigDecimal pesoBruto) {
		this.pesoBruto = pesoBruto;
	}

	public Integer getQtdePacotes() {
		return qtdePacotes;
	}

	public void setQtdePacotes(Integer qtdePacotes) {
		this.qtdePacotes = qtdePacotes;
	}

	public PessoaJuridica getExportador() {
		return exportador;
	}

	public void setExportador(PessoaJuridica exportador) {
		this.exportador = exportador;
	}

	public Carga getCarga() {
		return carga;
	}

	public void setCarga(Carga carga) {
		this.carga = carga;
	}

	public Incoterm getIncoterm() {
		return incoterm;
	}

	public void setIncoterm(Incoterm incoterm) {
		this.incoterm = incoterm;
	}

	public TipoPacote getTipoPacote() {
		return tipoPacote;
	}

	public void setTipoPacote(TipoPacote tipoPacote) {
		this.tipoPacote = tipoPacote;
	}

	public Usuario getComprador() {
		return comprador;
	}

	public void setComprador(Usuario comprador) {
		this.comprador = comprador;
	}

	public Pais getPaisOrigem() {
		return paisOrigem;
	}

	public void setPaisOrigem(Pais paisOrigem) {
		this.paisOrigem = paisOrigem;
	}

	public boolean getSelect() {
		return select;
	}

	public void setSelect(boolean select) {
		this.select = select;
	}

	public List<ItemInvoice> getItensInvoice() {
		return itensInvoice;
	}

	public void setItensInvoice(List<ItemInvoice> itensInvoice) {
		this.itensInvoice = itensInvoice;
	}

	public Modal getTipoTransporte() {
		return tipoTransporte;
	}

	public void setTipoTransporte(Modal tipoTransporte) {
		this.tipoTransporte = tipoTransporte;
	}

	public Terminal getTerminal() {
		return terminal;
	}

	public void setTerminal(Terminal terminal) {
		this.terminal = terminal;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Invoice))
			return false;
		Invoice other = (Invoice) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public String getSiglaCidadeAtual() {
		if (cidadeAtual != null)
			return cidadeAtual.getSigla().toUpperCase();
		return "";
	}

	public Cidade getCidadeAtual() {
		return cidadeAtual;
	}

	public void setCidadeAtual(Cidade cidadeAtual) {
		this.cidadeAtual = cidadeAtual;
	}

	public String getPoItens() {
		if (itensInvoice != null) {
			pos = "";
			for (ItemInvoice item : itensInvoice) {
				if (item.getItemPO().getPo().getNumeroPO() != null
						&& !item.getItemPO().getPo().getNumeroPO().equals("")) {
					if (!pos.equals("")) {
						pos = pos + ", "
								+ item.getItemPO().getPo().getNumeroPO();
					} else {
						pos = pos + item.getItemPO().getPo().getNumeroPO();
					}
				}
			}
		}

		return "";
	}

	public String getPos() {
		getPoItens();
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	/**
	 * Retorna o sequencial da Invoice.
	 * 
	 * @return
	 */
	public String getSequencial() {

		try {
			String zeros = "0000" + numSequencia;
			return zeros.substring(zeros.length() - 4, zeros.length()) + "/"
					+ anoSequencia;
		} catch (Exception e) {
			return "0000/00";
		}

	}

	public int getNumSequencia() {
		return numSequencia;
	}

	public void setNumSequencia(int numSequencia) {
		this.numSequencia = numSequencia;
	}

	public int getAnoSequencia() {
		return anoSequencia;
	}

	public void setAnoSequencia(int anoSequencia) {
		this.anoSequencia = anoSequencia;
	}

	public boolean isCritico() {
		return critico;
	}

	public void setCritico(boolean critico) {
		this.critico = critico;
	}

	public boolean isPendente() {
		return pendente;
	}

	public void setPendente(boolean pendente) {
		this.pendente = pendente;
	}

	public boolean isInvoiceManual() {
		return invoiceManual;
	}

	public void setInvoiceManual(boolean invoiceManual) {
		this.invoiceManual = invoiceManual;
	}

	public String getFabricante() {
		return fabricante;
	}

	public void setFabricante(String fabricante) {
		this.fabricante = fabricante;
	}

	public Integer getQtdePacotes2() {
		return qtdePacotes2;
	}

	public void setQtdePacotes2(Integer qtdePacotes2) {
		this.qtdePacotes2 = qtdePacotes2;
	}

	public TipoPacote getTipoPacote2() {
		return tipoPacote2;
	}

	public void setTipoPacote2(TipoPacote tipoPacote2) {
		this.tipoPacote2 = tipoPacote2;
	}

	public String getPalavrasChave() {
		return palavrasChave;
	}

	public void setPalavrasChave(String palavrasChave) {
		this.palavrasChave = palavrasChave;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Boolean getPli() {
		return pli;
	}

	public void setPli(Boolean pli) {
		this.pli = pli;
	}

	public String getNumPli() {
		return numPli;
	}

	public void setNumPli(String numPli) {
		this.numPli = numPli;
	}

	/**
	 * @return the nivel
	 */
	public Nivel getNivel() {
		return nivel;
	}

	/**
	 * @param nivel
	 *            the nivel to set
	 */
	public void setNivel(Nivel nivel) {
		this.nivel = nivel;
	}

}
