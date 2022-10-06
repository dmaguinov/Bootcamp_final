package nttdata.grupouno.com.microwallet.services.implementation;

import nttdata.grupouno.com.microwallet.models.ClientWalletModel;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    /*@KafkaListener(topics = "Kafka_Example", groupId = "group_id")
    public void consume(String menssage){
        System.out.println("Consumer: " + menssage);
    }*/
    @KafkaListener(topics = "Kafka_target", groupId = "group_json", containerFactory = "userKafkaListenerFactory")
    public ClientWalletModel consumeJsonTarjet(ClientWalletModel user) {
        System.out.println("Consumed JSON Message: " + user);
        return user;
    }
}
