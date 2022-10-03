package nttdata.grupouno.com.operations.controllers;

import nttdata.grupouno.com.operations.models.DebtClientModel;
import nttdata.grupouno.com.operations.services.IDebtClientService;
import nttdata.grupouno.com.operations.services.IMasterAccountServices;
import nttdata.grupouno.com.operations.services.IMovementDetailService;
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
    @Autowired
    IMasterAccountServices accountServices;
    @Autowired
    IMovementDetailService movementDetailService;

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
    public Mono<ResponseEntity<DebtClientModel>> findById(@PathVariable("id") String id){
        return debtClientService.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/pendingDebt/{codCliente}")
    public Mono<ResponseEntity<Flux<DebtClientModel>>> findPendingDebt(@PathVariable("codCliente") String codCliente){
        Flux<DebtClientModel> clientModelFlux = debtClientService.findPendingDebt(codCliente);
        return Mono.just(new ResponseEntity<>(clientModelFlux, clientModelFlux != null ? HttpStatus.OK : HttpStatus.NOT_FOUND));
    }

    @PutMapping("/pay/{id}/{codeClient}")
    public Mono<ResponseEntity<Map<String, Object>>> payDebt(@PathVariable("id") final String id,
                                                             @PathVariable("codeClient") final String codeClient){
        Map<String, Object> response = new HashMap<>();
        String uriPayDebt= "/operation/debt/pay".concat(id);
        return debtClientService.findById(id)
                .filter(debtClientModel -> debtClientModel.getState().equals("P"))
                .flatMap(a -> accountServices.findByClient(codeClient)
                        .filter(model -> model.getAmount() > a.getAmount())
                        .take(1).next()
                        .flatMap(b -> movementDetailService.withdrawAmount(b.getId(),a.getAmount())
                                .flatMap(c -> debtClientService.updatedDebt(id)
                                        .flatMap(debt -> {
                                            response.put("Deuda Cancelada", debt);
                                            return Mono.just(ResponseEntity.created(URI.create(uriPayDebt))
                                                    .body(response));
                                        }).switchIfEmpty(Mono.empty().flatMap(o -> {
                                    response.put("No se pudo realizar el pago", o);
                                    return Mono.just(ResponseEntity.created(URI.create(uriPayDebt))
                                            .body(response));
                                }))).switchIfEmpty(Mono.empty().flatMap(o -> {
                                    response.put("El cliente no tiene saldo suficiente", o);
                                    return Mono.just(ResponseEntity.created(URI.create(uriPayDebt))
                                            .body(response));
                                }))).switchIfEmpty(Mono.empty().flatMap(o -> {
                            response.put("No existe deuda", o);
                            return Mono.just(ResponseEntity.created(URI.create(uriPayDebt))
                                    .body(response));
                        })));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteDebt(@PathVariable("id") final String id){
        return debtClientService.findById(id)
                .flatMap(debtClientModel -> debtClientService.deleteDebtById(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT))))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
