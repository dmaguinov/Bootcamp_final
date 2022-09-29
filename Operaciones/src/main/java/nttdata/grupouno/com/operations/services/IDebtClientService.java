package nttdata.grupouno.com.operations.services;

import nttdata.grupouno.com.operations.models.DebtClientModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IDebtClientService {

    Flux<DebtClientModel> findAll();

    Mono<DebtClientModel> findById(String id);

    Flux<DebtClientModel> findPendingDebt(String id);

    Mono<DebtClientModel> createdDebt(DebtClientModel debt);

    Mono<DebtClientModel> updatedDebt(String id);

    Mono<Void> deleteDebtById(String id);
}
