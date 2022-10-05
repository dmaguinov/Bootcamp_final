package nttdata.grupouno.com.operations.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import nttdata.grupouno.com.operations.models.MasterAccountModel;

@Data
@EqualsAndHashCode(callSuper = true)
public class AccountCreatedEvent extends Event<MasterAccountModel>{
}
