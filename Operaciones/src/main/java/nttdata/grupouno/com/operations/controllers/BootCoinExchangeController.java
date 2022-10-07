package nttdata.grupouno.com.operations.controllers;

import nttdata.grupouno.com.operations.models.ExchangeModel;
import nttdata.grupouno.com.operations.services.implementation.BootCoinExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bootcoin/exchange")
public class BootCoinExchangeController {

    @Autowired
    private BootCoinExchangeService bootCoinExchangeService;

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public void createMovement(@RequestBody ExchangeModel model){bootCoinExchangeService.register(model);}


}
