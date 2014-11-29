package br.com.ilog.relatorio.business.dto;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.faces.context.FacesContext;

import br.com.ilog.cadastro.business.entity.PessoaJuridica;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;
import br.com.ilog.importacao.business.entity.Carga;
import br.com.ilog.importacao.business.entity.FollowUpImport;
import br.com.ilog.importacao.business.entity.FollowUpImportTrecho;
import br.com.ilog.importacao.business.entity.Invoice;
import br.com.ilog.importacao.business.entity.StatusCarga;

/**
 * Classe auxiliar para itens do relatorio dinamico de importacao
 * 
 * @author Heber Santiago
 * 
 */
public class CargaRelatorioDinamicoImport {

	private Carga carga;

	private Invoice invoice;

	private PessoaJuridica exportadorInvoice;

	private List<Invoice> listaDeInvoices;

	private FollowUpImport followUp;

	private FollowUpImportTrecho trechoInicial;

	private FollowUpImportTrecho trechoFinal;

	private String pesoLiquido;

	private String pesoCubado;

	private String pesoBrutoHawbCarga;

	private String pesoCubadoCarga;

	private String ultimoTrechoReal;
	private String ultimoTrechoEstimado;

	private String statusCarga;

	public Carga getCarga() {
		return carga;
	}

	public void setCarga(Carga carga) {
		this.carga = carga;
	}

	public List<Invoice> getListaDeInvoices() {
		return listaDeInvoices;
	}

	public void setListaDeInvoices(List<Invoice> listaDeInvoices) {
		this.listaDeInvoices = listaDeInvoices;
	}

	public FollowUpImport getFollowUp() {
		return followUp;
	}

	public void setFollowUp(FollowUpImport followUp) {
		this.followUp = followUp;

		if (followUp != null && followUp.getTrechosFollowUp() != null
				&& !followUp.getTrechosFollowUp().isEmpty()) {
			setTrechoInicial(followUp.getTrechosFollowUp().get(0));
			setTrechoFinal(followUp.getTrechosFollowUp().get(
					followUp.getTrechosFollowUp().size() - 3));
		}
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public PessoaJuridica getExportadorInvoice() {
		return exportadorInvoice;
	}

	public void setExportadorInvoice(PessoaJuridica exportadorInvoice) {
		this.exportadorInvoice = exportadorInvoice;
	}

	public FollowUpImportTrecho getTrechoInicial() {
		return trechoInicial;
	}

	public void setTrechoInicial(FollowUpImportTrecho trechoInicial) {
		this.trechoInicial = trechoInicial;
	}

	public FollowUpImportTrecho getTrechoFinal() {
		return trechoFinal;
	}

	public void setTrechoFinal(FollowUpImportTrecho trechoFinal) {
		this.trechoFinal = trechoFinal;
	}

	public String getPesoLiquido() {
		if (invoice.getPesoLiquido() != null) {
			pesoLiquido = invoice.getPesoLiquido().toString();
			pesoLiquido = pesoLiquido.replace(".", ",");
		} else
			pesoLiquido = "";

		return pesoLiquido;
	}

	public void setPesoLiquido(String pesoLiquido) {
		this.pesoLiquido = pesoLiquido;
	}

	public String getPesoCubado() {
		if (invoice.getPesoBruto() != null) {
			pesoCubado = invoice.getPesoBruto().toString();
			pesoCubado = pesoCubado.replace(".", ",");
		} else
			pesoCubado = "";
		return pesoCubado;
	}

	public void setPesoCubado(String pesoCubado) {
		this.pesoCubado = pesoCubado;
	}

	public String getPesoCubadoCarga() {
		if (carga != null && carga.getPesoCubadoHawb() != null) {
			pesoCubadoCarga = carga.getPesoCubadoHawb().toString();
			pesoCubadoCarga = pesoCubadoCarga.replace(".", ",");
		} else {
			pesoCubadoCarga = "";
		}
		return pesoCubadoCarga;
	}

	public void setPesoCubadoCarga(String pesoCubadoCarga) {
		this.pesoCubadoCarga = pesoCubadoCarga;
	}

	public String getPesoBrutoHawbCarga() {
		if (carga != null && carga.getPesoBrutoHawb() != null) {
			pesoBrutoHawbCarga = carga.getPesoBrutoHawb().toString();
			pesoBrutoHawbCarga = pesoBrutoHawbCarga.replace(".", ",");
		} else {
			pesoBrutoHawbCarga = "";
		}

		return pesoBrutoHawbCarga;
	}

	public void setPesoBrutoHawbCarga(String pesoBrutoHawbCarga) {
		this.pesoBrutoHawbCarga = pesoBrutoHawbCarga;
	}

	public String getUltimoTrechoEstimado() {
		if (followUp != null) {
			if (followUp.getTrechosFollowUp() != null) {
				SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy");
				FollowUpImportTrecho trechoFim = followUp.getTrechosFollowUp()
						.get(followUp.getTrechosFollowUp().size() - 3);
				String data = " ";
				if (trechoFim.getDtPlanejado() != null)
					data = out.format(trechoFim.getDtPlanejado());
				ultimoTrechoEstimado = data;

			}
		} else {
			ultimoTrechoEstimado = " ";
		}

		return ultimoTrechoEstimado;
	}

	public String getUltimoTrechoReal() {
		if (followUp != null) {
			if (followUp.getTrechosFollowUp() != null) {
				SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy");
				FollowUpImportTrecho trechoFim = followUp.getTrechosFollowUp()
						.get(followUp.getTrechosFollowUp().size() - 3);
				String data = " ";
				if (trechoFim.getDtRealizado() != null)
					data = out.format(trechoFim.getDtRealizado());

				ultimoTrechoReal = data;
			}

		} else {
			ultimoTrechoReal = " ";
		}

		return ultimoTrechoReal;
	}

	public void setUltimoTrechoReal(String ultimoTrechoReal) {
		this.ultimoTrechoReal = ultimoTrechoReal;
	}

	public void setUltimoTrechoEstimado(String ultimoTrechoEstimado) {
		this.ultimoTrechoEstimado = ultimoTrechoEstimado;
	}

	public String getStatusCarga() {
		FacesContext fc = FacesContext.getCurrentInstance();
		if (carga != null) {
			if (carga.getStatus().equals(StatusCarga.OHI)
					|| carga.getStatus().equals(StatusCarga.ITT)) {
				if (carga.getCidadeAtual() != null) {
					return TemplateMessageHelper.getMessage(
							MensagensSistema.CARGA, carga.getStatus().name(),
							fc.getViewRoot().getLocale())
							+ " "
							+ carga.getCidadeAtual().getSigla().toUpperCase();
				}
			} else {
				return TemplateMessageHelper.getMessage(MensagensSistema.CARGA,
						carga.getStatus().name(), fc.getViewRoot().getLocale());
			}
		} else
			statusCarga = " ";
		return statusCarga;
	}

	public void setStatusCarga(String statusCarga) {
		this.statusCarga = statusCarga;
	}

}
