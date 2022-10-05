package nttdata.grupouno.com.microwallet.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "wallet")
public class WalletModel {
    @Id
    private Integer id;
    @NotEmpty
    private String codCliente;
    @DecimalMin(value = "0.00", message = "Monto no negativo")
    private Double amount;
}

