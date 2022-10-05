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

import java.math.BigDecimal;
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
        return null;
    }
}
