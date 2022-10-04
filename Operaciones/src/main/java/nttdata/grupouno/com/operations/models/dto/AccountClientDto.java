package nttdata.grupouno.com.operations.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nttdata.grupouno.com.operations.models.AccountClientModel;
import nttdata.grupouno.com.operations.models.MasterAccountModel;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.constraints.NotEmpty;

@Data
@EntityScan
@AllArgsConstructor
@NoArgsConstructor
public class AccountClientDto {

    private String codeClient;
    private String numberAccount;
    private String idCartClient;
}
