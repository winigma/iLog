package br.com.ilog.cadastro.business.rep;

import java.util.List;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.cadastro.business.entity.Endereco;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroEndereco;

public interface EnderecoRepository extends CrudRepository<Endereco>{
  public List<Endereco> listarFiltraEnderecos(BasicFiltroEndereco filtro) throws BusinessException;
}
