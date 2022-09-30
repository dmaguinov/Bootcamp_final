package nttdata.grupouno.com.clients.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nttdata.grupouno.com.clients.models.Clients;
import nttdata.grupouno.com.clients.models.NaturalPerson;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class ClientsNatural extends Clients {
    @Getter
    @Setter
    List<NaturalPerson> naturalPersonList;

}
