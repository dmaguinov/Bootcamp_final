package nttdata.grupouno.com.operations.services;

import nttdata.grupouno.com.operations.models.MasterAccountModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IMasterAccountServices {
    Mono<MasterAccountModel> createAccount(MasterAccountModel account);
    Mono<MasterAccountModel> findById(String id);
    Flux<MasterAccountModel> findAllAccount();
    Mono<MasterAccountModel> updateAccount(MasterAccountModel account, String id);
    Mono<Void> deleteBydId(String id);
    Flux<MasterAccountModel> findStartDate(String date);
    Mono<MasterAccountModel> findByAccount(String numberAccount);
    Flux<MasterAccountModel> findByClient(String codeClient);
}
