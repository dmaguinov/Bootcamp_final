package nttdata.grupouno.com.operations.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nttdata.grupouno.com.operations.models.MasterAccountModel;
import nttdata.grupouno.com.operations.models.MovementDetailModel;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.List;

@Data
@EntityScan
@AllArgsConstructor
@NoArgsConstructor
public class AccountDetailDto extends MasterAccountModel {
    private List<MovementDetailModel> movements;
}
