package nttdata.grupouno.com.microwallet.services.implementation;

import nttdata.grupouno.com.microwallet.models.ClientWalletModel;
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
<<<<<<< HEAD
    private IWalletRepository IWalletRepository;

    @Autowired
=======
>>>>>>> bbefdc6efc3f98ad28e87b8a483f09d94763abb7
    private IClientWalletRepositories clientWalletRepositories;

    @Override
    public Mono<WalletMovementModel> registerMovement(WalletMovementDto walletMovementDto) {
<<<<<<< HEAD
        return clientWalletRepositories.findByNumberPhone(walletMovementDto.getNumberPhone())
                .flatMap(x -> {
                     return IWalletRepository.findByCodCliente(x.getId()).flatMap(y -> {
                         if(walletMovementDto.getMovementType().equals("E")){
=======
        return clientWalletRepositories.findByNumberPhone(walletMovementDto.getCelular())
                .flatMap(x -> {
                    if(walletMovementDto.getMovementType().equals("E")){
                        return clientWalletRepositories.findById(x.getId()).flatMap(y -> {
>>>>>>> bbefdc6efc3f98ad28e87b8a483f09d94763abb7
                            if(y.getAmount()<walletMovementDto.getAmount()){
                                return Mono.empty();
                            }
                         }
                         return Mono.just(y);
                        }).switchIfEmpty(Mono.empty());
                })
                .map(x -> new ClientWalletModel(null,null,null,null,null,null))
                .flatMap(x -> clientWalletRepositories.save(x))
                .map(x ->  new WalletMovementModel(UUID.randomUUID().toString(), x.getId() , Util.dateTimeToString(new Date()), walletMovementDto.getAmount(), walletMovementDto.getMovementType(), walletMovementDto.getCurrency(), Util.getMonth(new Date()), Util.getYear(new Date()) ))
                .flatMap(x -> IWalletMovementRepository.save(x))
                .switchIfEmpty(Mono.empty());
    }
}
