package nttdata.grupouno.com.microwallet.repositories;

import nttdata.grupouno.com.microwallet.models.WalletModel;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface IWalletRepository extends ReactiveMongoRepository<WalletModel, String> {

    Mono<WalletModel> findByCodCliente(String codCliente);
}