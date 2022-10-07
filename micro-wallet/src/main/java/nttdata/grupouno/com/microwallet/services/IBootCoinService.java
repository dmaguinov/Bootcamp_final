package nttdata.grupouno.com.microwallet.services;

import nttdata.grupouno.com.microwallet.models.BootCoinModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IBootCoinService {

    Mono<BootCoinModel> register(BootCoinModel model);

    Mono<BootCoinModel> findByNumberPhone(String number);

    Mono<BootCoinModel> findById(String id);

    Flux<BootCoinModel> findAll();

}
