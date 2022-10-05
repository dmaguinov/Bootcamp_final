package nttdata.grupouno.com.microwallet.services.implementation;

import nttdata.grupouno.com.microwallet.models.WalletMovementModel;
import nttdata.grupouno.com.microwallet.models.dto.WalletMovementDto;
import nttdata.grupouno.com.microwallet.repositories.IClientWalletRepositories;
import nttdata.grupouno.com.microwallet.repositories.IWalletMovementRepository;
import nttdata.grupouno.com.microwallet.services.IClientWalletService;
import nttdata.grupouno.com.microwallet.services.IWalletMovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class WalletMovementService implements IWalletMovementService {
    @Autowired
    private IWalletMovementRepository IWalletMovementRepository;
    @Autowired
    private IClientWalletRepositories clientWalletRepositories;

    @Override
    public Mono<WalletMovementModel> registerMovement(WalletMovementDto walletMovementDto) {
        return clientWalletRepositories.findByNumberPhone(walletMovementDto.getCelular())
                .flatMap(x -> {
                    if(walletMovementDto.getMovementType().equals("E")){
                        return clientWalletRepositories.findById(x.getId()).flatMap(y -> {
                            if(y.getAmount()<walletMovementDto.getAmount()){
                                return Mono.empty();
                            }
                            return Mono.just(x);
                        }).switchIfEmpty(Mono.empty());
                    }
                    return Mono.just(x);
                })
                .map(x ->  new WalletMovementModel(UUID.randomUUID().toString(), null, null, null, null, null, null, null ))
                .flatMap(x -> IWalletMovementRepository.save(x))
                .switchIfEmpty(Mono.empty());
    }
}
