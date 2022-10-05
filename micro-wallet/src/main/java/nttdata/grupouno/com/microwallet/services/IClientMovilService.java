package nttdata.grupouno.com.microwallet.services;

import nttdata.grupouno.com.microwallet.models.ClientMovilModel;
import reactor.core.publisher.Mono;

public interface IClientMovilService {

    Mono<ClientMovilModel> findByIdCelular(String celular);
}
