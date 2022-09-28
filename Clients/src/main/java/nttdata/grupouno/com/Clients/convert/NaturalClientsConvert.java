package nttdata.grupouno.com.Clients.convert;

import nttdata.grupouno.com.Clients.models.Clients;
import nttdata.grupouno.com.Clients.models.dto.NaturalClients;
import org.springframework.stereotype.Component;

@Component
public class NaturalClientsConvert {

    public NaturalClients convertNaturalClient(Clients clients){
        NaturalClients naturalClients = new NaturalClients();
        naturalClients.setId(clients.getId());
        naturalClients.setIdTypePerson(clients.getIdTypePerson());
        naturalClients.setIdPerson(clients.getIdPerson());

        return naturalClients;
    }

    public Clients convertClient(NaturalClients clients){
        Clients naturalClients = new Clients();
        naturalClients.setId(clients.getId());
        naturalClients.setIdTypePerson(clients.getIdTypePerson());
        naturalClients.setIdPerson(clients.getIdPerson());

        return naturalClients;
    }
}
