package nttdata.grupouno.com.clients.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import nttdata.grupouno.com.clients.models.NaturalPerson;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NaturalPersonRepository extends ReactiveMongoRepository<NaturalPerson,String> {

    Mono<NaturalPerson> findByDocumentNumber(Long documentNumber);

    Flux<NaturalPerson> findByNames(String names);

    Flux<NaturalPerson> findByLastNames(String lastNames);
}
