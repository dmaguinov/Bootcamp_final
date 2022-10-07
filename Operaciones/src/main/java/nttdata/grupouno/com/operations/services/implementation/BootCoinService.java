package nttdata.grupouno.com.operations.services.implementation;

import nttdata.grupouno.com.operations.models.BootCoinModel;
import nttdata.grupouno.com.operations.models.ExchangeModel;
import nttdata.grupouno.com.operations.repositories.IBootCoinExchangeRepository;
import nttdata.grupouno.com.operations.repositories.IBootCoinRepository;
import nttdata.grupouno.com.operations.services.IBootCoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class BootCoinService implements IBootCoinService {

    @Autowired
    private IBootCoinRepository bootCoinRepository;

    @Autowired
    private IBootCoinExchangeRepository bootCoinExchangeRepository;

    //@Autowired
    //@Qualifier("KafkaProducerTemplate")
    //private KafkaTemplate<String, BootCoinModel> kafkaTemplate;

    //private static final String TOPIC = "Kafka_target";

    @Override
    public Mono<BootCoinModel> register(BootCoinModel model) {

        if(model.getNumberDocument() == null || model.getNumberDocument().isBlank())
            return Mono.empty();
        BootCoinModel data = new BootCoinModel();
        data.setTargetAssociated(model.getTargetAssociated());
        data.setId(UUID.randomUUID().toString());
        model.setId(data.getId());

        return bootCoinRepository.save(model)
                .doOnSuccess(
                        x ->
                        {
                            if(data.getTargetAssociated() != null && !data.getTargetAssociated().isBlank())
                            {
                                //kafkaTemplate .send(TOPIC, data);
                            }
                        }
                );

    }

    @Override
    public Mono<BootCoinModel> findByNumberPhone(String number) {
        return bootCoinRepository.findByNumberPhone(number);
    }

    @Override
    public Mono<BootCoinModel> findById(String id) {
        return bootCoinRepository.findById(id);
    }

    @Override
    public Flux<BootCoinModel> findAll() {
        return bootCoinRepository.findAll();
    }

    public Mono<BootCoinModel> update(BootCoinModel bootCoin, String id){
        return bootCoinRepository.findById(id).flatMap(c -> {
            c.setAmount(bootCoin.getAmount());
            c.setEmail(bootCoin.getEmail());
            c.setNumberPhone(bootCoin.getNumberPhone());
            c.setNumberDocument(bootCoin.getNumberDocument());
            c.setTypeDocument(bootCoin.getTypeDocument());
            c.setTargetAssociated(bootCoin.getTargetAssociated());
            return bootCoinRepository.save(c);
        });
    }

    public Mono<ExchangeModel> exchangeValue(){
        System.out.println("exchangeValue");
        return bootCoinExchangeRepository.findById("1");
    }

    @Override
    public Mono<BootCoinModel> getBootCoin(String id, Double amount) {
        try {
            System.out.println("try");
            return bootCoinRepository.findById(id).flatMap(b -> {
                System.out.println("return 1");
                exchangeValue().map(r -> {
                    System.out.println("exchangeValue 1: "+ (amount / r.getExchangeRate()));
                    b.setAmount( b.getAmount() + (amount / r.getExchangeRate()));
                    System.out.println("exchangeValue 2");
                    return b;
                }).subscribe();
                System.out.println("return 2");
                return update(b,b.getId());
            });
        }
        catch(Exception e){
            System.out.println("catch");
            return null;
        }
    }
}
