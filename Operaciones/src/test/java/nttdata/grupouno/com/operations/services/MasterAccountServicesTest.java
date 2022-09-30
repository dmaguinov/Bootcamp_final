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
import nttdata.grupouno.com.operations.models.MasterAccountModel;
import nttdata.grupouno.com.operations.models.TypeModel;
import nttdata.grupouno.com.operations.repositories.implementation.MasterAccountRepository;
import nttdata.grupouno.com.operations.repositories.implementation.TypeAccountRepository;
import nttdata.grupouno.com.operations.services.implementation.MasterAccountServices;
import nttdata.grupouno.com.operations.services.implementation.WebClientApiService;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class MasterAccountServicesTest {
    @Mock
    private MasterAccountRepository masterAccountRepository;
    @Mock
    private TypeAccountRepository typeAccountRepository;
    @InjectMocks
    private MasterAccountServices masterAccountServices;
    @Mock
    private WebClientApiService webClientApiService;
    @Autowired
    private MasterAccountModel modelMaster;
    @Autowired
    private Mono<MasterAccountModel> masterAccountModel;
    @Autowired
    private Mono<TypeModel> typeModel;
    //@Autowired
    //private Flux<MasterAccountModel> masterAccountModels;

    @BeforeEach
    void init(){
        typeModel = Mono.just(new TypeModel("AHO1", "Ahorro", "A", 10, 0.0, 1, 1, 20.0, null,null,null));
        modelMaster = new MasterAccountModel("1", "12", new TypeModel("AHO1", null, null, null, null, null, null, null, null,null,null), "2021.01.02", "A", null, 20.0, "PEN");
        masterAccountModel = Mono.just(modelMaster);
    }

    @Test
    void createAccount(){
        Mockito.when(typeAccountRepository.findById("AHO1")).thenReturn(typeModel);
        Mockito.when(masterAccountRepository.save(modelMaster)).thenReturn(masterAccountModel);

        Mono<MasterAccountModel> creater = masterAccountServices.createAccount(modelMaster, null);
        creater.subscribe(
            x -> {
                assertEquals(x.getNumberAccount(), modelMaster.getNumberAccount());
                assertEquals("AHO1", x.getType().getCode());
                assertEquals("Ahorro", x.getType().getDescription());
            }
        );
    }
}
