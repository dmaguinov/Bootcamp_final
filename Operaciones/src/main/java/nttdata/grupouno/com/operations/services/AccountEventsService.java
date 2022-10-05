package nttdata.grupouno.com.operations.services;

import nttdata.grupouno.com.operations.events.AccountCreatedEvent;
import nttdata.grupouno.com.operations.events.Event;
import nttdata.grupouno.com.operations.events.EventType;
import nttdata.grupouno.com.operations.models.MasterAccountModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.UUID;

@Component
public class AccountEventsService {

    private static final Logger logger = LogManager.getLogger(AccountEventsService.class);

    private final KafkaTemplate<String, Mono<Event<?>>> kafkaTemplate;

    public AccountEventsService(@Qualifier("kafkaAccountTemplate") KafkaTemplate<String, Mono<Event<?>>> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(MasterAccountModel accountModel) {
        logger.info("%%%%%%%%%%%publishh ");
        AccountCreatedEvent created = new AccountCreatedEvent();
        created.setData(accountModel);
        created.setId(UUID.randomUUID().toString());
        created.setType(EventType.CREATED);
        created.setDate(new Date());

        this.kafkaTemplate.send("account-topic", Mono.just(created));
    }
}
