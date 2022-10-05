package nttdata.grupouno.com.microwallet.services.impl;

import nttdata.grupouno.com.microwallet.models.WalletMovementModel;
import nttdata.grupouno.com.microwallet.models.dto.WalletMovementDto;
import nttdata.grupouno.com.microwallet.repositories.WalletMovementRepository;
import nttdata.grupouno.com.microwallet.repositories.WalletRepository;
import nttdata.grupouno.com.microwallet.services.IClientMovilService;
import nttdata.grupouno.com.microwallet.services.IWalletMovementService;
import nttdata.grupouno.com.microwallet.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class WalletMovementService implements IWalletMovementService {

    @Autowired
    private WalletMovementRepository walletMovementRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private IClientMovilService iClientMovilService;

    @Override
    public Mono<WalletMovementModel> registerMovement(WalletMovementDto walletMovementDto) {
        return iClientMovilService.findByIdCelular(walletMovementDto.getCelular()).flatMap(x -> {
            if(walletMovementDto.getMovementType().equals("E")){
                walletRepository.findByCodCliente(x.getId()).flatMap(y -> {
                    if(y.getAmount()<walletMovementDto.getAmount()){
                        return Mono.empty();
                    }
                    return Mono.just(y);
                }).switchIfEmpty(Mono.empty());
            }
            WalletMovementModel walletMovementModel = new WalletMovementModel();
            walletMovementModel.setId(1);
            walletMovementModel.setMovementType(walletMovementDto.getMovementType());
            walletMovementModel.setDate(Util.dateTimeToString(new Date()));
            walletMovementModel.setCurrency(walletMovementDto.getCurrency());
            walletMovementModel.setMonth(Util.getMonth(new Date()));
            walletMovementModel.setYear(Util.getYear(new Date()));
            return walletMovementRepository.save(walletMovementModel);
        }).switchIfEmpty(Mono.empty());
    }
}
