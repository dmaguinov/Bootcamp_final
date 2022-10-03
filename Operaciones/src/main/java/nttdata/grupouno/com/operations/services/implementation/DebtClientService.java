package nttdata.grupouno.com.operations.services.implementation;

import nttdata.grupouno.com.operations.models.DebtClientModel;
import nttdata.grupouno.com.operations.repositories.implementation.AccountClientRepositorio;
import nttdata.grupouno.com.operations.repositories.implementation.DebitClientRepository;
import nttdata.grupouno.com.operations.services.IDebtClientService;
import nttdata.grupouno.com.operations.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Date;
import java.util.UUID;

@Service
public class DebtClientService implements IDebtClientService {

    @Autowired
    DebitClientRepository debitClientRepository;
    @Autowired
    AccountClientRepositorio clientRepositorio;

    @Override
    public Flux<DebtClientModel> findAll() {
        return debitClientRepository.findAll();
    }

    @Override
    public Mono<DebtClientModel> findById(String id) {
        return debitClientRepository.findById(id);
    }

    @Override
    public Flux<DebtClientModel> findPendingDebt(String codCliente) {
        return clientRepositorio.findByCodeClient(codCliente)
                .flatMap(a -> debitClientRepository.findByNumberAccountAndState(a.getNumberAccount(),"P"));
    }

    @Override
    public Mono<DebtClientModel> createdDebt(DebtClientModel debt) {
        debt.setId(UUID.randomUUID().toString());
        debt.setState("P");
        return debitClientRepository.save(debt);
    }

    @Override
    public Mono<DebtClientModel> updatedDebt(String id) {
        Date today = new Date();
        return debitClientRepository.findById(id).flatMap(debtClientModel -> {
            debtClientModel.setState("C");
            debtClientModel.setPaymentDate(Util.dateToString(today));
            return debitClientRepository.save(debtClientModel);
        }).switchIfEmpty(Mono.empty());
    }

    @Override
    public Mono<Void> deleteDebtById(String id) {
        return debitClientRepository.deleteById(id);
    }
}
