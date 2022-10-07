package nttdata.grupouno.com.operations.services;

import nttdata.grupouno.com.operations.models.ExchangeModel;
import reactor.core.publisher.Mono;

public interface IBootCoinExchangeService {

    void register(ExchangeModel model);

    Mono<ExchangeModel> findById(String id);

}
