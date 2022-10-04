package nttdata.grupouno.com.operations.services.implementation;

import lombok.RequiredArgsConstructor;
import nttdata.grupouno.com.operations.models.AccountClientModel;
import nttdata.grupouno.com.operations.models.DebtClientModel;
import nttdata.grupouno.com.operations.repositories.implementation.AccountClientRepositorio;
import nttdata.grupouno.com.operations.repositories.implementation.DebitClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class DebtClientServiceTest {

    @Mock
    DebitClientRepository debitClientRepository;
    @Mock
    private AccountClientRepositorio clientRepositorio;
    @InjectMocks
    private DebtClientService debtClientService;
    @Autowired
    private DebtClientModel model;
    @Autowired
    private Mono<DebtClientModel> modelMono;
    @Autowired
    private Flux<DebtClientModel> modelFlux;
    @Autowired
    private DebtClientModel modelUpdate;
    @Autowired
    private Mono<DebtClientModel> modelUpdateMono;
    @Autowired
    private AccountClientModel accountClientModel;
    @Autowired
    private Flux<AccountClientModel> accountClientModelFlux;

    @BeforeEach
    void init() {
        model=new DebtClientModel("8f849bfe-7ff8-4c0b-a1f9-a1c4bd3dfb98","408535551732729",300.0,"P","2022.10.15","2022.10.01","2022.10.03","CRE1","362a3f8d-f176-489a-b112-a2cf352a36b5");
        modelMono=Mono.just(model);
        modelFlux=modelMono.flux();

        modelUpdate=new DebtClientModel("8f849bfe-7ff8-4c0b-a1f9-a1c4bd3dfb98","408535551732729",550.0,"P","2022.10.15","2022.10.01","2022.10.03","CRE1","362a3f8d-f176-489a-b112-a2cf352a36b5");
        modelUpdateMono=Mono.just(model);

        accountClientModel=new AccountClientModel("1","50c6924b-67ec-4e53-97ce-d633ea05f1f7","50c6924b-67ec-4e53-97ce-d633ea05f1f7","N","T","CRE1","123456");
        accountClientModelFlux = Flux.just(accountClientModel);
    }

    @Test
    void findAll() {
        Mockito.when(debitClientRepository.findAll()).thenReturn(modelFlux);
        debtClientService.findAll().subscribe(a -> {
            assertEquals(model.getId(), a.getId());
            assertEquals(model.getNumberAccount(), a.getNumberAccount());
            assertEquals(model.getAmount(), a.getAmount());
            assertEquals(model.getState(), a.getState());
            assertEquals(model.getExpirationDate(), a.getExpirationDate());
            assertEquals(model.getIssueDate(), a.getIssueDate());
            assertEquals(model.getPaymentDate(), a.getPaymentDate());
            assertEquals(model.getCodeCredit(), a.getCodeCredit());
            assertEquals(model.getPaidBy(), a.getPaidBy());
        });
    }

    @Test
    void findById() {
        Mockito.when(debitClientRepository.findById("8f849bfe-7ff8-4c0b-a1f9-a1c4bd3dfb98")).thenReturn(modelMono);
        debtClientService.findById("8f849bfe-7ff8-4c0b-a1f9-a1c4bd3dfb98")
                .subscribe(a -> {
                    assertEquals(model.getId(), a.getId());
                    assertEquals(model.getNumberAccount(), a.getNumberAccount());
                    assertEquals(model.getAmount(), a.getAmount());
                    assertEquals(model.getState(), a.getState());
                    assertEquals(model.getExpirationDate(), a.getExpirationDate());
                    assertEquals(model.getIssueDate(), a.getIssueDate());
                    assertEquals(model.getPaymentDate(), a.getPaymentDate());
                    assertEquals(model.getCodeCredit(), a.getCodeCredit());
                    assertEquals(model.getPaidBy(), a.getPaidBy());
                });
    }

    @Test
    void findPendingDebt() {
        Mockito.when(clientRepositorio.findByCodeClient("50c6924b-67ec-4e53-97ce-d633ea05f1f7")).thenReturn(accountClientModelFlux);
        Mockito.when(debitClientRepository.findByNumberAccountAndState("408535551732729","P")).thenReturn(modelFlux);
        debtClientService.findPendingDebt("50c6924b-67ec-4e53-97ce-d633ea05f1f7")
                .subscribe(a -> {
                    assertEquals(model.getId(), a.getId());
                    assertEquals(model.getNumberAccount(), a.getNumberAccount());
                    assertEquals(model.getAmount(), a.getAmount());
                    assertEquals(model.getState(), a.getState());
                    assertEquals(model.getExpirationDate(), a.getExpirationDate());
                    assertEquals(model.getIssueDate(), a.getIssueDate());
                    assertEquals(model.getPaymentDate(), a.getPaymentDate());
                    assertEquals(model.getCodeCredit(), a.getCodeCredit());
                    assertEquals(model.getPaidBy(), a.getPaidBy());
                });
    }

    @Test
    void createdDebt() {
        Mockito.when(debitClientRepository.save(model)).thenReturn(modelMono);
        debtClientService.createdDebt(model).subscribe(a -> {
            assertEquals(model.getId(), a.getId());
            assertEquals(model.getNumberAccount(), a.getNumberAccount());
            assertEquals(model.getAmount(), a.getAmount());
            assertEquals(model.getState(), a.getState());
            assertEquals(model.getExpirationDate(), a.getExpirationDate());
            assertEquals(model.getIssueDate(), a.getIssueDate());
            assertEquals(model.getPaymentDate(), a.getPaymentDate());
            assertEquals(model.getCodeCredit(), a.getCodeCredit());
            assertEquals(model.getPaidBy(), a.getPaidBy());
        });

    }

    @Test
    void updatedDebt() {
        Mockito.when(debitClientRepository.save(modelUpdate)).thenReturn(modelUpdateMono);
        Mockito.when(debitClientRepository.findById("8f849bfe-7ff8-4c0b-a1f9-a1c4bd3dfb98")).thenReturn(modelMono);
        debtClientService.updatedDebt("8f849bfe-7ff8-4c0b-a1f9-a1c4bd3dfb98",modelUpdate)
                .subscribe(a -> {
                    assertEquals(modelUpdate.getId(), a.getId());
                    assertEquals(modelUpdate.getNumberAccount(), a.getNumberAccount());
                    System.out.println("a.getAmount()" + a.getAmount());
                    assertEquals(modelUpdate.getAmount(), a.getAmount());
                    assertEquals(modelUpdate.getState(), a.getState());
                    assertEquals(modelUpdate.getExpirationDate(), a.getExpirationDate());
                    assertEquals(modelUpdate.getIssueDate(), a.getIssueDate());
                    assertEquals(modelUpdate.getPaymentDate(), a.getPaymentDate());
                    assertEquals(modelUpdate.getCodeCredit(), a.getCodeCredit());
                    assertEquals(modelUpdate.getPaidBy(), a.getPaidBy());
                });
    }

    @Test
    void deleteDebtById() {
        Mockito.when(debitClientRepository.deleteById("8f849bfe-7ff8-4c0b-a1f9-a1c4bd3dfb98")).thenReturn(Mono.empty());
        Mono<Void> response = debtClientService.deleteDebtById("8f849bfe-7ff8-4c0b-a1f9-a1c4bd3dfb98");
        assertNotNull(response);
    }
}