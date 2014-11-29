package br.com.ilog.cadastro.business.rep;

import java.util.List;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.cadastro.business.entity.Departamento;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroDepartamento;

public interface DepartamentoRepository extends CrudRepository<Departamento>{

	
	public List<Departamento> listDepartamentos( BasicFiltroDepartamento filtro)throws BusinessException;
}
