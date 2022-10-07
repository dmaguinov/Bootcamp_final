package nttdata.grupouno.com.operations.services;

import nttdata.grupouno.com.operations.models.BootCoinModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IBootCoinService {

    Mono<BootCoinModel> register(BootCoinModel model);

    Mono<BootCoinModel> findByNumberPhone(String number);

    Mono<BootCoinModel> findById(String id);

    Flux<BootCoinModel> findAll();

    Mono<BootCoinModel> getBootCoin(String id, Double amount);

}
