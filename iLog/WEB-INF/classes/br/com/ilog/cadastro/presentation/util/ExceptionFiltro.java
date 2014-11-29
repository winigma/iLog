package br.com.ilog.cadastro.presentation.util;

import br.com.ilog.geral.business.CodigoErroEspecifico;

public class ExceptionFiltro {

	private static Throwable throwable;

	public static String recursiveException(Throwable ex) {
		if (ex != null) {
			setThrowable(ex);
			recursiveException(ex.getCause());
		}
		return analiseMensagem(getThrowable().getMessage());
	}

	public static Throwable getLastException(Throwable e) {
		Throwable last = e;
		while (last.getCause() != null) {
			last = last.getCause();
		}
		return last;
	}

	private static Throwable getThrowable() {
		return throwable;
	}

	private static void setThrowable(Throwable throwable) {
		ExceptionFiltro.throwable = throwable;
	}

	private static String analiseMensagem(String mensagem) {
		if (mensagem.toLowerCase().contains("insert into")) {
			return CodigoErroEspecifico.MSG_UNIQUE_VIOLADA.getIdBundle();
		} else if (mensagem.toUpperCase().contains("PRIMARY")) {
			return CodigoErroEspecifico.MSG_KEY_VIOLADA.getIdBundle();
		} else if (mensagem.toLowerCase().contains("delete from")) {
			return CodigoErroEspecifico.MSG_FK_VIOLADA.getIdBundle();
		} else if(mensagem.toLowerCase().contains("ConstraintViolationException")){
			return CodigoErroEspecifico.MSG_UNIQUE_VIOLADA.getIdBundle();
		}
		return mensagem;
	}

}