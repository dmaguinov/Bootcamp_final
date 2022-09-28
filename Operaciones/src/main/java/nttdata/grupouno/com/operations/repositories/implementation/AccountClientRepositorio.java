package nttdata.grupouno.com.operations.repositories.implementation;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import nttdata.grupouno.com.operations.models.AccountClientModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountClientRepositorio extends ReactiveMongoRepository<AccountClientModel, String>{
    Flux<AccountClientModel> findByCodeClient(String codeClient);
    Flux<AccountClientModel> findByNumberAccount(String numberAccount);
    Flux<AccountClientModel> findByNumberAccountAndTypeAccount(String numberAccount, String typeAccount);
    Mono<Long> countByCodeClientAndTypeAccount(String codeClient, String typeAccount);
    Mono<Long> countByCodeClientAndTypeAccountAndTypeClient(String codeClient, String typeAccount, String typeClient);
    Mono<Long> countByCodeClientAndTypeAccountLike(String codeClient, String typeAccount);
}
