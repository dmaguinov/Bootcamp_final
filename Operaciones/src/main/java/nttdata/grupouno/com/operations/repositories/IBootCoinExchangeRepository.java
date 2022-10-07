package nttdata.grupouno.com.operations.repositories;

import nttdata.grupouno.com.operations.models.ExchangeModel;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface IBootCoinExchangeRepository extends ReactiveMongoRepository<ExchangeModel, String> {}
