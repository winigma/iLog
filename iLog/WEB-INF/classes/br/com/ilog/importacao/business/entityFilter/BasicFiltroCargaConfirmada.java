package br.com.ilog.importacao.business.entityFilter;

import java.io.Serializable;

import br.com.ilog.importacao.business.entity.Carga;
import br.com.ilog.seguranca.business.entity.Usuario;

/**
 * 
 * @author Heber Santiago
 *
 */
public class BasicFiltroCargaConfirmada implements Serializable{

	
	private static final long serialVersionUID = 1L;
	private Carga carga;

	private Usuario user;

	public Carga getCarga() {
		return carga;
	}

	public void setCarga(Carga carga) {
		this.carga = carga;
	}

	public Usuario getUser() {
		return user;
	}

	public void setUser(Usuario user) {
		this.user = user;
	}
	
	

}
