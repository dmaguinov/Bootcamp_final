package nttdata.grupouno.com.operations.controllers;

import nttdata.grupouno.com.operations.models.MasterAccountModel;
import nttdata.grupouno.com.operations.models.MovementDetailModel;
import nttdata.grupouno.com.operations.services.implementation.AccountClientService;
import nttdata.grupouno.com.operations.services.implementation.MasterAccountServices;
import nttdata.grupouno.com.operations.services.implementation.MovementDetailService;
import nttdata.grupouno.com.operations.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/operation/movement")
public class MovementDetailController {

    @Autowired
    MovementDetailService movementService;
    @Autowired
    AccountClientService clientService;
    @Autowired
    MasterAccountServices masterAccountServices;

    private final WebClient webClient;

    public MovementDetailController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8010").build();
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public void createMovement(@RequestBody MovementDetailModel movement){
        movementService.createMovement(movement);
    }

    @GetMapping(value = "/all")
    @ResponseBody

    public Flux<MovementDetailModel> findAllAccount() {
        return movementService.findAllMovements();
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Mono<MovementDetailModel>> findMovementById(@PathVariable("id") Integer id){
        Mono<MovementDetailModel> accountM = movementService.findById(id);
        return new ResponseEntity<>(accountM, accountM != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping("/client/{codeClient}")
    public ResponseEntity<Flux<MovementDetailModel>> findMovementByClient(@PathVariable("codeClient") String codeClient){
        Flux<MovementDetailModel> accountM = movementService.findByClient(codeClient);
        return new ResponseEntity<>(accountM, accountM != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping("/account/{account}")
    public ResponseEntity<Flux<MovementDetailModel>> findMovementByAccount(@PathVariable("account") String account){
        Flux<MovementDetailModel> fluxMov = movementService.findByAccount(account);
        return new ResponseEntity<>(fluxMov, HttpStatus.OK);
    }

    @GetMapping("/operation/check/{id}")
    public ResponseEntity<Mono<MasterAccountModel>> checkBalance(@PathVariable("id") String id){
        Mono<MasterAccountModel> masterAccount = movementService.checkBalance(id);
        return new ResponseEntity<>(masterAccount, masterAccount!=null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping("/operation/deposit/{id}/{amount}")
    public ResponseEntity<Mono<MasterAccountModel>> depositAmount(@PathVariable("id") String id, @PathVariable("amount") Double amount){
        Mono<MasterAccountModel> masterAccount = movementService.depositAmount(id,amount);
        return new ResponseEntity<>(masterAccount, masterAccount!=null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping("/operation/withdraw/{id}/{amount}")
    public ResponseEntity<Mono<MasterAccountModel>> withdrawAmount(@PathVariable("id") String id, @PathVariable("amount") Double amount){
        Mono<MasterAccountModel> masterAccount = movementService.withdrawAmount(id,amount);
        return new ResponseEntity<>(masterAccount, masterAccount!=null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @PutMapping("/transfer")
    public Mono<ResponseEntity<Map<String, Object>>> transfer(@RequestBody Map<String, String> body) {
        String rootAccount = body.get("rootAccount");
        String destinationAccount = body.get("destinationAccount");
        Double amount = Double.parseDouble(body.get("amount"));
        MasterAccountModel rootAccount1 = new MasterAccountModel();
        MasterAccountModel destinationAccount1 = new MasterAccountModel();

        Map<String, Object> respuesta = new HashMap<>();
        Flux<MasterAccountModel> master = this.webClient.get().uri("/operation/account/all").retrieve().bodyToFlux(MasterAccountModel.class);
        Flux<MasterAccountModel> master1 = this.webClient.get().uri("/operation/account/all").retrieve().bodyToFlux(MasterAccountModel.class);
        master.filter(a -> a.getNumberAccount().equals(rootAccount)).subscribe(b -> {
            rootAccount1.setNumberAccount(b.getNumberAccount());
            rootAccount1.setId(b.getId());
            rootAccount1.setType(b.getType());
            rootAccount1.setAmount(b.getAmount());
            rootAccount1.setStatus(b.getStatus());
            rootAccount1.setCoinType(b.getCoinType());
            rootAccount1.setEndDate(b.getEndDate());
            rootAccount1.setStartDate(b.getStartDate());

                master1.filter(a -> a.getNumberAccount().equals(destinationAccount))
                        .subscribe(c -> {
                            destinationAccount1.setNumberAccount(c.getNumberAccount());
                            destinationAccount1.setId(c.getId());
                            destinationAccount1.setType(c.getType());
                            destinationAccount1.setAmount(c.getAmount());
                            destinationAccount1.setStatus(c.getStatus());
                            destinationAccount1.setCoinType(c.getCoinType());
                            destinationAccount1.setEndDate(c.getEndDate());
                            destinationAccount1.setStartDate(c.getStartDate());
                            
                            respuesta.put("Cuenta Destino", "Cuenta destino existe");
                            if(rootAccount1.getAmount() >= amount){
                                movementService.withdrawAmount(rootAccount1.getId(),amount).subscribe(d ->System.out.println("Origen"+d.getAmount()));
                                movementService.depositAmount(destinationAccount1.getId(),amount).subscribe(d ->System.out.println("Destino"+d.getAmount()));
                            }

                        });
            Mono.just(ResponseEntity.created(URI.create("/transfer"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(respuesta));
        });

        return Mono.just(ResponseEntity.created(URI.create("/transfer"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(respuesta));

    }

    @GetMapping("/maintenace/{numberAccount}")
    public ResponseEntity<Flux<MasterAccountModel>> chargeMaintenace(@PathVariable("numberAccount") String numberAccount){

        Flux<MasterAccountModel> accountFlux = masterAccountServices.findByAccount(numberAccount).flux().flatMap(account -> {
            if (!account.getType().getCode().equals("AHO2")){
                return Flux.empty();
            }
            return clientService.findByNumBerAccount(account.getNumberAccount()).flatMap(accountClientModel -> {
                if (accountClientModel.getTypeClient().equals("J")){
                    return masterAccountServices.findByClient(accountClientModel.getCodeClient())
                            .filter(masterAccountModel -> masterAccountModel.getStatus().equals("A")
                            ).filter(masterAccountModel -> masterAccountModel.getType().getProduct().equals("C")
                            ).filter(masterAccountModel -> Util.stringToDate(masterAccountModel.getStartDate())
                                        .compareTo(Util.stringToDate(account.getStartDate())) <= 1
                            ).switchIfEmpty(movementService.chargeMaintenace(account.getNumberAccount()).flux());
                }else{
                    return movementService.chargeMaintenace(account.getNumberAccount()).flux();
                }

            });
        });
        return new ResponseEntity<>(accountFlux, accountFlux!=null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
}
