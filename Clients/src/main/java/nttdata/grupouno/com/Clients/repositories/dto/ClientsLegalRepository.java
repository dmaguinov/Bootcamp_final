package nttdata.grupouno.com.Clients.repositories.dto;

import nttdata.grupouno.com.Clients.models.dto.ClientsLegal;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ClientsLegalRepository  extends ReactiveMongoRepository<ClientsLegal,String> {
}
