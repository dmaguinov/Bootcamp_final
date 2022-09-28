package nttdata.grupouno.com.Clients.repositories;

import nttdata.grupouno.com.Clients.models.NaturalPerson;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NaturalPersonRepository extends ReactiveMongoRepository<NaturalPerson,String> {

    Mono<NaturalPerson> findByDocumentNumber(Long documentNumber);

    Flux<NaturalPerson> findByNames(String names);

    Flux<NaturalPerson> findByLastNames(String lastNames);
}
