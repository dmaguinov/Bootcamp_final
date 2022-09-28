package nttdata.grupouno.com.operations.controllers;

import nttdata.grupouno.com.operations.models.MasterAccountModel;
import nttdata.grupouno.com.operations.models.dto.RegisterAccountDto;
import nttdata.grupouno.com.operations.services.IAccountClientService;
import nttdata.grupouno.com.operations.services.IMasterAccountServices;
import nttdata.grupouno.com.operations.services.ITypeAccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/operation/account")
public class MasterAccountController {
    @Autowired
    private IMasterAccountServices accountServices;
    @Autowired
    private ITypeAccountService typeAccountService;
    @Autowired
    private IAccountClientService accountClientService;

    public Mono<ResponseEntity<Map<String, Object>>> fallbackBank(RuntimeException runtimeException){
        Map<String, Object> response = new HashMap<>();
        response.put("unavaible", "El servicio para crear cuentas no se encuentra disponible.");
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response));
    }

    /**
     * @param request
     * @return
     */
    @PostMapping("/bank")
    @CircuitBreaker(name = "bank", fallbackMethod = "fallbackBank")
    public Mono<ResponseEntity<Map<String, Object>>> createAccountBank(
            @Valid @RequestBody Mono<RegisterAccountDto> request) {
        Map<String, Object> response = new HashMap<>();

        return request.flatMap(a -> 
            typeAccountService.findById(a.getAccountModel().getType().getCode()).flatMap(b -> {
                response.put("typeAccount", b);
                if(b.getAmountStart() > a.getAccountModel().getAmount()){
                    response.put("limit", "El monto mínimo de apertura para este producto es de ".concat(b.getAmountStart().toString()));
                    return Mono.just(ResponseEntity.badRequest().body(response));
                }
                return accountClientService.countByCodeClientAndTypeAccountLike(a.getClientModel().getCodeClient(), b.getCodeRequired()).flatMap(bb -> {
                    if(bb == 0){
                        response.put("limit", "Para este producto es requisito tener una(s) cuenta(s) del tipo: ".concat(b.getCodeRequired()));
                        return Mono.just(ResponseEntity.badRequest().body(response));
                    }
                    return accountClientService.countByCodeClientAndTypeAccountAndTypeClient(a.getClientModel().getCodeClient(), b.getCode(), a.getClientModel().getTypeClient()).flatMap(c -> {
                        if(c.intValue() >= b.getCountPerson() && a.getClientModel().getTypeClient().equals("N")){
                            response.put("limit", "El máximo de cuentas del tipo <<".concat(b.getDescription()).concat(">> es ").concat(b.getCountPerson().toString()));
                            return Mono.just(ResponseEntity.badRequest().body(response));
                        }
                        if(c.intValue() >= b.getCountBusiness() && a.getClientModel().getTypeClient().equals("J")){
                            response.put("limit", "El máximo de cuentas del tipo <<".concat(b.getDescription()).concat(">> es ").concat(b.getCountBusiness().toString()));
                            return Mono.just(ResponseEntity.badRequest().body(response));
                        }
                        return accountServices.findByAccount(a.getAccountModel().getNumberAccount()).flatMap(d -> {
                            response.put("duplicit", d);
                            return Mono.just(ResponseEntity.badRequest()
                                    .body(response));
                        })
                        .switchIfEmpty(accountServices.createAccount(a.getAccountModel()).flatMap(e -> {
                            response.put("account", e);
                            a.getClientModel().setNumberAccount(e.getNumberAccount());
                            a.getClientModel().setTypeAccount(b.getCode());
    
                            return accountClientService.registerClient(a.getClientModel()).flatMap(f -> {
                                response.put("clients", f);
                                return Mono.just(ResponseEntity.created(URI.create("/api/account/bank"))
                                        .body(response));
                            })
                            .switchIfEmpty(accountServices.deleteBydId(e.getId()).flatMap(ff -> {
                                response.put("clientsError", "El cliente no pudo ser verificado para la creación de la cuenta");
                                return Mono.just(ResponseEntity.badRequest()
                                    .body(response));
                            }))
                            .retry(1)
                            .onErrorResume(g -> {
                                response.put("clientsError", a.getClientModel());
                                response.put("error", g.getMessage());
    
                                return accountServices.deleteBydId(e.getId()).flatMap(h -> {
                                    return Mono.just(ResponseEntity.badRequest()
                                    .body(response));
                                });
                            });
                        }));
                    });
                });
            }).defaultIfEmpty(ResponseEntity.badRequest().body(response))
        )
        .onErrorResume(ex -> Mono.just(ex).cast(WebExchangeBindException.class)
        .flatMap(e -> Mono.just(e.getFieldErrors()))
        .flatMapMany(Flux::fromIterable).map(DefaultMessageSourceResolvable::getDefaultMessage)
        .collectList()
        .flatMap(list -> {
            response.put("errors", list);
            return Mono.just(ResponseEntity.badRequest().body(response));
        })).log();
    }

    /**
     * @return
     */
    @GetMapping(value = "/all")
    @ResponseBody
    public Flux<MasterAccountModel> findAllAccount() {
        return accountServices.findAllAccount();
    }

    /**
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Mono<MasterAccountModel>>> findAccountById(@PathVariable("id") final String id) {
        Mono<MasterAccountModel> accountMono = accountServices.findById(id);
        return Mono.just(new ResponseEntity<>(accountMono, accountMono != null ? HttpStatus.OK : HttpStatus.NOT_FOUND));
    }

    /**
     * @param account
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<MasterAccountModel>> update(@Valid @RequestBody final MasterAccountModel account,
            @PathVariable final String id) {
        return accountServices.updateAccount(account, id)
                .map(c -> ResponseEntity.created(URI.create("/account/".concat(c.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(c))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable("id") final String id) {
        return accountServices.findById(id).flatMap(c -> accountServices.deleteBydId(c.getId())
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT))))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * @param date
     * @return
     */
    @GetMapping("/findStartDate/{date}")
    @ResponseBody
    public Flux<MasterAccountModel> findStartDate(@PathVariable("date") final String date) {
        return accountServices.findStartDate(date);
    }

    /**
     * @param codeClient
     * @return
     */
    @GetMapping("/client/{codeClient}")
    public Mono<ResponseEntity<Flux<MasterAccountModel>>> findByClient(@PathVariable("codeClient") final String codeClient) {
        Flux<MasterAccountModel> accountFlux = accountServices.findByClient(codeClient);
        return Mono.just(new ResponseEntity<>(accountFlux, accountFlux != null ? HttpStatus.OK : HttpStatus.NOT_FOUND));
    }
}
