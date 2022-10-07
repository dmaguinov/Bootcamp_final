package nttdata.grupouno.com.operations.controllers;

import nttdata.grupouno.com.operations.models.BootCoinModel;
import nttdata.grupouno.com.operations.services.IBootCoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/bootcoin/client")
public class BootCoinController {

    @Autowired
    private IBootCoinService bootCoinService;

    @PostMapping("/")
    public Mono<Map<String, Object>> registerClient(@RequestBody Mono<BootCoinModel> request){
        Map<String, Object> response = new HashMap<>();

        return request
                .flatMap(x -> {
                    if(x.getEmail() == null || x.getEmail().isBlank())
                    {
                        response.put("valid", "No se logró identificar el tipo de documento");
                        return Mono.just(response);
                    }
                    return bootCoinService.findByNumberPhone (x.getNumberPhone())
                            .flatMap(y -> {
                                response.put("validWallet", "El cliente Wallet ya existe");
                                response.put("clientWallet", y);
                                return Mono.just(response);
                            })
                            .switchIfEmpty(
                                    bootCoinService.register(x).flatMap(
                                            y -> {
                                                response.put("clientWallet", y);
                                                return Mono.just(response);
                                            }
                                    )
                            );
                });
    }

    @GetMapping("/getBootCoin/{id}/{amount}")
    public ResponseEntity<Mono<BootCoinModel>> getBootCoins(@PathVariable("id") String id, @PathVariable("amount") Double amount){
        Mono<BootCoinModel> bootCoin = bootCoinService.getBootCoin(id, amount);
        return new ResponseEntity<>(bootCoin, bootCoin!=null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

}
