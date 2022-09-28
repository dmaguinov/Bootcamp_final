package nttdata.grupouno.com.operations.repositories.implementation;

import nttdata.grupouno.com.operations.models.MovementDetailModel;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface MovementDetailRepository extends ReactiveMongoRepository<MovementDetailModel, Integer> {

    Mono<Long> countByNumberAccountAndMovementTypeAndMonthAndYear(String numberAccount, Character movementType, String month, String year);
}
