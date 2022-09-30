package nttdata.grupouno.com.clients.services.dto;

import nttdata.grupouno.com.clients.models.MasterAccount;
import nttdata.grupouno.com.clients.models.MovementDetail;
import nttdata.grupouno.com.clients.models.dto.NaturalClients;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClientsNaturalService {

    Mono<NaturalClients> findAllById(String id);
    Mono<NaturalClients> findByDocumentNumber(Long documentNumber);
    Flux<NaturalClients> findByNames(String names);
    Flux<NaturalClients> findByLastNames(String lastNames);
    Flux<MasterAccount> findAccountByDocumentNumber(Long documentNumber);
    Flux<MovementDetail> findMovementByDocumentNumber(Long documentNumber);
}
