package br.com.ilog.geral.business;

import org.aspectj.lang.annotation.Aspect;
import org.hibernate.exception.ConstraintViolationException;

import br.cits.commons.citsbusiness.exception.CodigoErro;
import br.cits.commons.citsbusiness.exception.spring.SpringInterceptor;

@Aspect
public class BusinessInterceptor extends SpringInterceptor{

	@Override
	protected CodigoErro handleException(CodigoErro erro, String constraintName, ConstraintViolationException ex, boolean exclusao) {
		
		return CodigoErroEspecifico.valueOf(constraintName);
	}

}
