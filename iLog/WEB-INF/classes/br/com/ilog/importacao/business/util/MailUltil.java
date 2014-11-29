package br.com.ilog.importacao.business.util;

import br.com.ilog.importacao.business.entity.MailFollowUpImport;
import br.com.ilog.importacao.business.entity.RelatarFollowUpImport;

public class MailUltil extends Thread {

	public MailUltil(String assunto, String mensagem, RelatarFollowUpImport mail) {

		this.mail = mail;
		this.assunto = assunto;
		this.mensagem = mensagem;
	}

	private RelatarFollowUpImport mail;
	private String assunto;
	private String mensagem;

	@Override
	public void run() {
		// Capturando todos os e-mails cadastrados para aviso
		SendMail enviarEmail = new SendMail();

		String destinatarios = "";
		for (MailFollowUpImport email : this.mail.getMails()) {
			// if (usuario.isAvisoMII())
			destinatarios += email.getMail().trim() + ";";
		}
		if (destinatarios.length() > 0) {
			destinatarios = destinatarios.substring(0,
					destinatarios.length() - 1);
			try {
				enviarEmail.postMail(assunto, mensagem, destinatarios);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.assunto = null;
		this.mensagem = null;
		this.mail = null;
	}
}
