package nttdata.grupouno.com.microwallet.services.implementation;

import nttdata.grupouno.com.microwallet.models.ClientWalletModel;
import nttdata.grupouno.com.microwallet.repositories.IClientWalletRepositories;
import nttdata.grupouno.com.microwallet.services.IClientWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class ClientWalletService implements IClientWalletService {
    @Autowired
    private IClientWalletRepositories clientWalletRepositories;

    @Override
    public Mono<ClientWalletModel> register(ClientWalletModel model) {
        if(model.getNumberDocument() == null || model.getNumberDocument().isBlank())
            return Mono.empty();
        model.setId(UUID.randomUUID().toString());
        return clientWalletRepositories.save(model);
    }

    @Override
    public Mono<ClientWalletModel> findByNumberDocumentAndTypeDocument(String numberDocument, String typeDocument) {
        return clientWalletRepositories.findByNumberDocumentAndTypeDocument(numberDocument, typeDocument);
    }
}
