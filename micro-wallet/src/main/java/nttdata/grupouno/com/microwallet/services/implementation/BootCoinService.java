package nttdata.grupouno.com.microwallet.services.implementation;

import nttdata.grupouno.com.microwallet.models.BootCoinModel;
import nttdata.grupouno.com.microwallet.repositories.IBootCoinRepository;
import nttdata.grupouno.com.microwallet.services.IBootCoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

@Service
public class BootCoinService implements IBootCoinService {

    @Autowired
    private IBootCoinRepository bootCoinRepository;

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
}
