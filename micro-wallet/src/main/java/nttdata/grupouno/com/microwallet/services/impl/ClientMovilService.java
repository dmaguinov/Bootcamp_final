package nttdata.grupouno.com.microwallet.services.impl;

import nttdata.grupouno.com.microwallet.models.ClientMovilModel;
import nttdata.grupouno.com.microwallet.services.IClientMovilService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ClientMovilService implements IClientMovilService {
    @Override
    public Mono<ClientMovilModel> findByIdCelular(String celular) {
        return Mono.just(new ClientMovilModel("1","46668322","999999999","1234","prueba@gmail.com","DNI"));
    }
}
