package nttdata.grupouno.com.clients.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import nttdata.grupouno.com.clients.models.Clients;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClientesRepository extends ReactiveMongoRepository<Clients,String> {

    Flux<Clients> findByIdTypePerson(Long idTypePerson);
    Mono<Clients> findByIdPerson(String idPerson);
}
