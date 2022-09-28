package nttdata.grupouno.com.operations.services.implementation;

import nttdata.grupouno.com.operations.models.MasterAccountModel;
import nttdata.grupouno.com.operations.models.TypeModel;
import nttdata.grupouno.com.operations.repositories.implementation.AccountClientRepositorio;
import nttdata.grupouno.com.operations.repositories.implementation.MasterAccountRepository;
import nttdata.grupouno.com.operations.repositories.implementation.TypeAccountRepository;
import nttdata.grupouno.com.operations.services.IMasterAccountServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class MasterAccountServices implements IMasterAccountServices {
    @Autowired
    private MasterAccountRepository accountRepository;
    @Autowired
    private TypeAccountRepository typeAccountRepository;
    @Autowired
    private AccountClientRepositorio accountClientRepositorio;

    @Override
    public Mono<MasterAccountModel> createAccount(MasterAccountModel account) {
        account.setId(UUID.randomUUID().toString());
        account.setType(new TypeModel(account.getType().getCode(), null, null, null, null, null, null, null, null,null,null));

        return accountRepository.save(account)
                .flatMap(c -> typeAccountRepository.findById(c.getType().getCode()).flatMap(x -> {
                    c.setType(x);
                    return Mono.just(c);
                }));
    }

    @Override
    public Mono<MasterAccountModel> findById(String id) {
        System.out.println(">>>>>>>>>>> " + id);
        return accountRepository.findById(id)
                .flatMap(c -> typeAccountRepository.findById(c.getType().getCode()).flatMap(x -> {
                    System.out.println(">>>>>>>>>>>>>:: " + c.getNumberAccount());
                    c.setType(x);
                    return Mono.just(c);
                }));
    }

    @Override
    public Flux<MasterAccountModel> findAllAccount() {
        return accountRepository.findAll()
                .flatMap(c -> typeAccountRepository.findById(c.getType().getCode()).flatMap(x -> {
                    c.setType(x);
                    return Mono.just(c);
                }));
    }

    @Override
    public Mono<MasterAccountModel> updateAccount(MasterAccountModel account, String id) {
        return accountRepository.findById(id).flatMap(c -> accountRepository.save(account));
    }

    @Override
    public Mono<Void> deleteBydId(String id) {
        return accountRepository.deleteById(id);
    }

    @Override
    public Flux<MasterAccountModel> findStartDate(String date) {
        return accountRepository.findByStartDate(date);
    }

    @Override
    public Mono<MasterAccountModel> findByAccount(String numberAccount) {
        return accountRepository.findByNumberAccount(numberAccount)
                .flatMap(masterAccountModel -> typeAccountRepository.findById(masterAccountModel.getType().getCode())
                        .flatMap(typeModel -> {
                            masterAccountModel.setType(typeModel);
                            return Mono.just(masterAccountModel);
                        }));
    }

    @Override
    public Flux<MasterAccountModel> findByClient(String codeClient) {
        return accountClientRepositorio.findByCodeClient(codeClient)
                .flatMap(accountClientModel -> findByAccount(accountClientModel.getNumberAccount()));
    }
}
