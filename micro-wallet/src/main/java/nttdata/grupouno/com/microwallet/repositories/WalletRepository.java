package nttdata.grupouno.com.microwallet.repositories;

import nttdata.grupouno.com.microwallet.models.ClientMovilModel;
import nttdata.grupouno.com.microwallet.models.WalletModel;
import nttdata.grupouno.com.microwallet.models.WalletMovementModel;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface WalletRepository extends ReactiveMongoRepository<WalletModel, String> {

    Mono<WalletModel> findByCodCliente(String codCliente);
}