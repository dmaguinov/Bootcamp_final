package nttdata.grupouno.com.operations.services;

import nttdata.grupouno.com.operations.models.CartClientModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICartClientService {
    Mono<CartClientModel> findById(String id);
    Mono<CartClientModel> findByHashCartNumber(String hashCartNumber);
    Flux<CartClientModel> findByCodeClientAndTypeCartAndCodeStatus(String codeClient, String typeCart, String codeStatus);
    Mono<CartClientModel> registerCardNumber(CartClientModel cartClientModel);
}
