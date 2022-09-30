package nttdata.grupouno.com.clients.models.dto;

import lombok.Getter;
import lombok.Setter;
import nttdata.grupouno.com.clients.models.Clients;
import nttdata.grupouno.com.clients.models.NaturalPerson;

public class NaturalClients extends Clients {

    @Getter
    @Setter
    private NaturalPerson person;
    public NaturalClients(){
        super();
        person = new NaturalPerson();
    }
}
