package nttdata.grupouno.com.operations.services.implementation;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nttdata.grupouno.com.operations.models.CartClientModel;
import nttdata.grupouno.com.operations.repositories.implementation.AccountClientRepositorio;
import nttdata.grupouno.com.operations.repositories.implementation.CartClientRepositorio;
import nttdata.grupouno.com.operations.services.ICartClientService;
import nttdata.grupouno.com.operations.services.IWebClientApiService;
import nttdata.grupouno.com.operations.util.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CartClientService implements ICartClientService {
    @Autowired
    private CartClientRepositorio cartClientRepositorio;
    @Autowired
    private AccountClientRepositorio accountClientRepositorio;
    @Autowired
    private IWebClientApiService webClient;

    @Override
    public Mono<CartClientModel> findById(String id) {
        return cartClientRepositorio.findById(id);
    }

    @Override
    public Mono<CartClientModel> findByHashCartNumber(String hashCartNumber) {
        return cartClientRepositorio.findByHashCartNumber(hashCartNumber);
    }

    @Override
    public Flux<CartClientModel> findByCodeClientAndTypeCartAndCodeStatus(String codeClient, String typeCart,
            String codeStatus) {
        return cartClientRepositorio.findByCodeClientAndTypeCartAndCodeStatus(codeClient, typeCart, codeStatus);
    }

    @Override
    public Mono<CartClientModel> registerCardNumber(CartClientModel cartClientModel) {
        return webClient.findClient(cartClientModel.getCodeClient()).flatMap(
            z -> {
                cartClientModel.setId(UUID.randomUUID().toString());
                cartClientModel.setCartNumber(Util.generateCartNumber());
                cartClientModel.setHashCartNumber("");
                cartClientModel.setCodeStatus("A");
                cartClientModel.setStartDate(Util.dateToString(new Date()));

                return cartClientRepositorio.findByCodeClientAndTypeCartAndCodeStatus(
                    cartClientModel.getCodeClient(), cartClientModel.getTypeCart(), "A"
                )
                .flatMap(a -> {
                    a.setCodeStatus("C");
                    return cartClientRepositorio.save(a).flatMap(y -> cartClientRepositorio.save(cartClientModel));
                })
                .switchIfEmpty(cartClientRepositorio.save(cartClientModel))
                .single()
                .flatMap(b -> accountClientRepositorio.findByCodeClient(cartClientModel.getCodeClient())
                        .filter(c -> c.getTypeAccount().contains(b.getTypeCart()))
                        .flatMap(d -> {
                            d.setIdCartClient(b.getId());
                            return accountClientRepositorio.save(d).map(e -> e);
                        })
                        .flatMap(e -> Mono.just(b))
                        .switchIfEmpty(f -> Mono.just(b))
                        .next()
                );
            }
        ).switchIfEmpty(Mono.empty());
    }

}
