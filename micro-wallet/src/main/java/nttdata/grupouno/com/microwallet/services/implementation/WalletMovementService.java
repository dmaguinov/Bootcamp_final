package nttdata.grupouno.com.microwallet.services.implementation;

import nttdata.grupouno.com.microwallet.models.WalletMovementModel;
import nttdata.grupouno.com.microwallet.models.dto.WalletMovementDto;
import nttdata.grupouno.com.microwallet.repositories.IWalletMovementRepository;
import nttdata.grupouno.com.microwallet.repositories.IWalletRepository;
import nttdata.grupouno.com.microwallet.services.IClientWalletService;
import nttdata.grupouno.com.microwallet.services.IWalletMovementService;
import nttdata.grupouno.com.microwallet.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.UUID;

@Service
public class WalletMovementService implements IWalletMovementService {

    @Autowired
    private IWalletMovementRepository IWalletMovementRepository;

    @Autowired
    private IWalletRepository IWalletRepository;

    @Autowired
    private IClientWalletService clientWalletService;

    @Override
    public Mono<WalletMovementModel> registerMovement(WalletMovementDto walletMovementDto) {
        return clientWalletService.findByNumberPhone(walletMovementDto.getCelular())
                .flatMap(x -> {
                    if(walletMovementDto.getMovementType().equals("E")){
                        return IWalletRepository.findByCodCliente(x.getId()).flatMap(y -> {
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
