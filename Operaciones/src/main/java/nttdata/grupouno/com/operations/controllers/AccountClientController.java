package nttdata.grupouno.com.operations.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import nttdata.grupouno.com.operations.models.AccountClientModel;
import nttdata.grupouno.com.operations.services.IAccountClientService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/operation/accountClient")
public class AccountClientController {
    @Autowired
    private IAccountClientService accountClientService;

    @PostMapping("/")
    public Mono<ResponseEntity<Mono<AccountClientModel>>> addAccountClient(@Valid @RequestBody AccountClientModel request){
        Mono<AccountClientModel> account = accountClientService.registerClient(request);
        return Mono.just(new ResponseEntity<>(account, account != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST));
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Flux<AccountClientModel> findAll(){
        return accountClientService.findAll();
    }
}
