package nttdata.grupouno.com.operations.services.implementation;

import nttdata.grupouno.com.operations.models.ExchangeModel;
import nttdata.grupouno.com.operations.repositories.IBootCoinExchangeRepository;
import nttdata.grupouno.com.operations.services.IBootCoinExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class BootCoinExchangeService implements IBootCoinExchangeService {

    @Autowired
    private IBootCoinExchangeRepository bootCoinExchangeRepository;

    @Override
    public void register(ExchangeModel model) {
        bootCoinExchangeRepository.save(model).subscribe();
    }

    @Override
    public Mono<ExchangeModel> findById(String id) {
        return bootCoinExchangeRepository.findById(id);
    }


}
