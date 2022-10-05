package nttdata.grupouno.com.microwallet.controllers;

import nttdata.grupouno.com.microwallet.models.ClientWalletModel;
import nttdata.grupouno.com.microwallet.services.IClientWalletService;
import nttdata.grupouno.com.microwallet.services.ITypeDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/wallet/client")
public class ClientWalletController {
    @Autowired
    private IClientWalletService clientWalletService;
    @Autowired
    private ITypeDocumentService typeDocumentService;

    @PostMapping("/")
    public Mono<ClientWalletModel> registerClient(@RequestBody @Valid Mono<ClientWalletModel> request){
        return request
                .flatMap(x ->
                        typeDocumentService.getOne(x.getTypeDocument())
                                .flatMap(y -> Mono.just(x))
                                .switchIfEmpty(Mono.empty())
                )
                .flatMap(x ->
                        clientWalletService.findByNumberDocumentAndTypeDocument(x.getNumberDocument(), x.getTypeDocument())
                                .flatMap(y -> Mono.just(new ClientWalletModel()))
                                .switchIfEmpty(clientWalletService.register(x))
                )
                .switchIfEmpty(Mono.empty());
    }
}
