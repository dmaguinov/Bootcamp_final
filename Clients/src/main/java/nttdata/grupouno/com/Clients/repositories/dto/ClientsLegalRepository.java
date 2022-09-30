package nttdata.grupouno.com.clients.repositories.dto;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import nttdata.grupouno.com.clients.models.dto.ClientsLegal;

public interface ClientsLegalRepository  extends ReactiveMongoRepository<ClientsLegal,String> {
}
