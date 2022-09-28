package nttdata.grupouno.com.operations.controllers;

import lombok.RequiredArgsConstructor;
import nttdata.grupouno.com.operations.models.AccountClientModel;
import nttdata.grupouno.com.operations.models.MasterAccountModel;
import nttdata.grupouno.com.operations.models.TypeModel;
import nttdata.grupouno.com.operations.models.dto.RegisterAccountDto;
import nttdata.grupouno.com.operations.services.IAccountClientService;
import nttdata.grupouno.com.operations.services.IMasterAccountServices;
import nttdata.grupouno.com.operations.services.ITypeAccountService;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
public class MasterAccountControllerTest {
    @InjectMocks
    private MasterAccountController masterAccountController;
    @Mock
    private IMasterAccountServices accountServices;
    @Mock
    private ITypeAccountService typeAccountService;
    @Mock
    private IAccountClientService accountClientService;
    @Autowired
    private TypeModel typeModel;
    @Autowired
    private RegisterAccountDto modelRegister;
    @Autowired
    private Mono<RegisterAccountDto> requestModel;
    @Autowired 
    private MasterAccountModel masteModel;
    @Autowired
    private AccountClientModel accountModel;

    @BeforeEach
    void init(){
        typeModel = new TypeModel("AHO1", "Ahorro", "A", 1, 0.0, 1, 1, 10.00, null,null,null);
        masteModel = new MasterAccountModel("1", "12", typeModel, "2022.09.23", "A", null, 5.0, "PEN");
        accountModel = new AccountClientModel("123", null, "N", "T", null);
    }

    @Test
    void createAccountBank(){
        Map<String, Object> responseBody = new HashMap<>();
        Mono<ResponseEntity<Map<String, Object>>> response;
        Mockito.when(typeAccountService.findById("AHO1")).thenReturn(Mono.just(typeModel));
        Mockito.when(accountClientService.countByCodeClientAndTypeAccountAndTypeClient("123", "AHO1", "N")).thenReturn(Mono.just(Long.valueOf("1")));
        Mockito.when(accountServices.findByAccount("12")).thenReturn(Mono.just(masteModel));
        Mockito.when(accountServices.createAccount(masteModel)).thenReturn(Mono.empty());
        Mockito.when(accountServices.findByAccount("123")).thenReturn(Mono.empty());

        typeModel.setCode("AHO");
        modelRegister = new RegisterAccountDto(masteModel, accountModel);
        requestModel = Mono.just(modelRegister);
        
        /// Valid Type Account
        response = masterAccountController.createAccountBank(requestModel);
        response.subscribe(x -> assertEquals(x.getStatusCode(), HttpStatus.BAD_REQUEST));

        ///  Valid Limit amount start of a account
        typeModel.setCode("AHO1");
        responseBody.put("typeAccount", typeModel);
        response = masterAccountController.createAccountBank(requestModel);
        response.subscribe(x -> {
            assertEquals(x.getStatusCode(), HttpStatus.BAD_REQUEST);
            assertNotNull(x.getBody().get("limit"));
            assertEquals(x.getBody().get("typeAccount"), typeModel);
        });

        /// Valid number limit of type person and type account
        masteModel.setAmount(25.50);
        response = masterAccountController.createAccountBank(requestModel);
        response.subscribe(x -> {
            assertEquals(x.getStatusCode(), HttpStatus.BAD_REQUEST);
            assertNotNull(x.getBody().get("limit"));
            assertEquals(x.getBody().get("typeAccount"), typeModel);
        });

        /// Valid duplicit account
        typeModel.setCountPerson(3);
        response = masterAccountController.createAccountBank(requestModel);
        response.subscribe(x -> {
            assertEquals(x.getStatusCode(), HttpStatus.BAD_REQUEST);
            assertEquals(x.getBody().get("duplicit"), masteModel);
            assertEquals(x.getBody().get("typeAccount"), typeModel);
        });

        /// Valid create account and register client-account
        masteModel.setNumberAccount("123");
        Mockito.when(accountServices.createAccount(masteModel)).thenReturn(Mono.just(masteModel));
        Mockito.when(accountClientService.registerClient(accountModel)).thenReturn(Mono.just(accountModel));

        response = masterAccountController.createAccountBank(requestModel);
        response.subscribe(x -> {
            assertEquals(x.getStatusCode(), HttpStatus.CREATED);
            assertEquals(x.getBody().get("account"), masteModel);
            assertEquals(x.getBody().get("clients"), accountModel);
            assertEquals(x.getBody().get("typeAccount"), typeModel);
        });
    }
}
