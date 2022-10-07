package nttdata.grupouno.com.operations.repositories;

import nttdata.grupouno.com.operations.models.BootCoinModel;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface IBootCoinRepository extends ReactiveMongoRepository<BootCoinModel, String> {

    Mono<BootCoinModel> findByNumberPhone(String numberPhone);

}
