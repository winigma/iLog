package br.com.ilog.cadastro.business.rep;

import java.util.List;

import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.cadastro.business.entity.Motivo;
import br.com.ilog.cadastro.business.entity.Processo;
import br.com.ilog.cadastro.business.entity.TipoOcorrencia;

public interface ProcessoRepository extends CrudRepository<Processo> {

	List<Processo> listarByOcorrencia(TipoOcorrencia entity);

	List<Processo> listarNotOcorrencia(TipoOcorrencia entity);

	List<Processo> listarByMotivo(Motivo entity);

	List<Processo> listarNotMotivo(Motivo entity);

}
