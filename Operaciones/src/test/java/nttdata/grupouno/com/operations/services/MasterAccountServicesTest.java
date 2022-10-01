package nttdata.grupouno.com.operations.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.RequiredArgsConstructor;
import nttdata.grupouno.com.operations.models.AccountClientModel;
import nttdata.grupouno.com.operations.models.MasterAccountModel;
import nttdata.grupouno.com.operations.models.TypeModel;
import nttdata.grupouno.com.operations.repositories.implementation.AccountClientRepositorio;
import nttdata.grupouno.com.operations.repositories.implementation.CartClientRepositorio;
import nttdata.grupouno.com.operations.repositories.implementation.MasterAccountRepository;
import nttdata.grupouno.com.operations.repositories.implementation.TypeAccountRepository;
import nttdata.grupouno.com.operations.services.implementation.MasterAccountServices;
import nttdata.grupouno.com.operations.services.implementation.WebClientApiService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class MasterAccountServicesTest {
    @InjectMocks
    private MasterAccountServices masterAccountServices;
    @Mock
    private MasterAccountRepository masterAccountRepository;
    @Mock
    private TypeAccountRepository typeAccountRepository;
    @Mock
    private AccountClientRepositorio accountClientRepositorio;
    @Mock
    private WebClientApiService webClientApiService;
    @Mock
    private CartClientRepositorio cartClientRepositorio;
    @Autowired
    private MasterAccountModel modelMaster;
    @Autowired
    private AccountClientModel modelAccount;
    @Autowired
    private Mono<MasterAccountModel> masterAccountModel;
    @Autowired
    private Mono<TypeModel> typeModel;
    @Autowired
    private Mono<AccountClientModel> modelClient;
    @Autowired
    private Flux<AccountClientModel> modelClients;
    //@Autowired
    //private Flux<MasterAccountModel> masterAccountModels;

    @BeforeEach
    void init(){
        typeModel = Mono.just(new TypeModel("AHO1", "Ahorro", "A", 10, 0.0, 1, 1, 20.0, null,null,null));
        modelMaster = new MasterAccountModel("123", "12", new TypeModel("AHO1", null, null, null, null, null, null, null, null,null,null), "2021.01.02", "A", null, 20.0, "PEN");
        modelAccount = new AccountClientModel("1", "123", null, "N", null, null, null);
        masterAccountModel = Mono.just(modelMaster);
        modelClient = Mono.just(modelAccount);
        modelClients = Flux.just(modelAccount);

        Mockito.when(webClientApiService.findClient("123")).thenReturn(masterAccountModel);
        Mockito.when(typeAccountRepository.findById("AHO1")).thenReturn(typeModel);
    }

    @Test
    void createAccount(){
        Mono<Long> cantTarjet = Mono.just(Long.valueOf(1));
        Mono<Long> cantTarjetEm = Mono.just(Long.valueOf(0));

        Mockito.when(masterAccountRepository.save(modelMaster)).thenReturn(masterAccountModel);
        Mockito.when(accountClientRepositorio.save(modelAccount)).thenReturn(modelClient);
        Mockito.when(cartClientRepositorio.countByCodeClientAndTypeCartAndCodeStatus("123","AHO","A")).thenReturn(cantTarjet);
        Mockito.when(cartClientRepositorio.countByCodeClientAndTypeCartAndCodeStatus("1234","AHO","A")).thenReturn(cantTarjetEm);

        masterAccountServices.createAccount(modelMaster, modelAccount).subscribe(
            x -> {
                assertEquals(x.getNumberAccount(), modelMaster.getNumberAccount());
                assertEquals("AHO1", x.getType().getCode());
                assertEquals("Ahorro", x.getType().getDescription());
            }
        );
    }
}
