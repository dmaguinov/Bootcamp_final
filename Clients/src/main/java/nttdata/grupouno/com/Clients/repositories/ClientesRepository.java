package nttdata.grupouno.com.Clients.repositories;

import nttdata.grupouno.com.Clients.models.Clients;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClientesRepository extends ReactiveMongoRepository<Clients,String> {

    Flux<Clients> findByIdTypePerson(Long idTypePerson);
    Mono<Clients> findByIdPerson(String idPerson);
}
