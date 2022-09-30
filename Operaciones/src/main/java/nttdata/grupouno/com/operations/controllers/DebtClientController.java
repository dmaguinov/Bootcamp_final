package nttdata.grupouno.com.operations.controllers;

import nttdata.grupouno.com.operations.models.DebtClientModel;
import nttdata.grupouno.com.operations.services.IDebtClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/operation/debt")
public class DebtClientController {

    @Autowired
    IDebtClientService debtClientService;

    @PostMapping
    public Mono<ResponseEntity<Map<String, Object>>> createDebt(@Valid @RequestBody Mono<DebtClientModel> request){
        Map<String, Object> response = new HashMap<>();
        return request.flatMap(debtClientModel -> debtClientService.createdDebt(debtClientModel)
                .flatMap(debtCreated -> {
                    response.put("debt", debtCreated);
                    return Mono.just(ResponseEntity.created(URI.create("/operation/debt"))
                            .body(response));
                }))
                .onErrorResume(ex -> Mono.just(ex).cast(WebExchangeBindException.class)
                        .flatMap(e -> Mono.just(e.getFieldErrors()))
                        .flatMapMany(Flux::fromIterable).map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collectList()
                        .flatMap(list -> {
                            response.put("errors", list);
                            return Mono.just(ResponseEntity.badRequest().body(response));
                        })).log();
    }

    @GetMapping("/all")
    @ResponseBody
    public Flux<DebtClientModel> findAllDebt(){
        return debtClientService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<DebtClientModel> findById(@PathVariable("id") String id){
        return debtClientService.findById(id);
    }

    @GetMapping("/debt/{codCliente}")
    public Mono<ResponseEntity<Flux<DebtClientModel>>> findPendingDebt(@PathVariable("codCliente") String codCliente){
        Flux<DebtClientModel> clientModelFlux = debtClientService.findPendingDebt(codCliente);
        return Mono.just(new ResponseEntity<>(clientModelFlux, clientModelFlux != null ? HttpStatus.OK : HttpStatus.NOT_FOUND));
    }

    @PutMapping("/pay/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> payDebt(@PathVariable("id") final String id){
        Map<String, Object> response = new HashMap<>();
            return debtClientService.updatedDebt(id).flatMap(debt -> {
                response.put("Deuda Cancelada", debt);
                return Mono.just(ResponseEntity.created(URI.create("/operation/debt/pay".concat(debt.getId())))
                        .body(response));
            }).switchIfEmpty(Mono.empty().flatMap(o -> {
                        response.put("No se encontro el id de deuda", o);
                        return Mono.just(ResponseEntity.created(URI.create("/operation/debt/pay".concat(id)))
                                .body(response));
                    }))
                    .onErrorResume(ex -> Mono.just(ex).cast(WebExchangeBindException.class)
                .flatMap(e -> Mono.just(e.getFieldErrors()))
                .flatMapMany(Flux::fromIterable).map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collectList()
                .flatMap(list -> {
                    response.put("errors", list);
                    return Mono.just(ResponseEntity.badRequest().body(response));
                })).log();
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteDebt(@PathVariable("id") final String id){
        return debtClientService.findById(id)
                .flatMap(debtClientModel -> debtClientService.deleteDebtById(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT))))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
