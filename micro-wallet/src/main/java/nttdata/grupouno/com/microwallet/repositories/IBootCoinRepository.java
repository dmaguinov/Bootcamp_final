package nttdata.grupouno.com.microwallet.repositories;

import nttdata.grupouno.com.microwallet.models.BootCoinModel;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface IBootCoinRepository extends ReactiveMongoRepository<BootCoinModel, String> {

    Mono<BootCoinModel> findByNumberPhone(String numberPhone);

}
