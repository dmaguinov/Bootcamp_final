package nttdata.grupouno.com.microwallet.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "clientBootCoin")
public class BootCoinModel {
    @Id
    private String id;
    @NotEmpty
    private String numberDocument;
    @NotEmpty
    private String typeDocument; //V vendedor - C - solo comprador
    @NotEmpty
    @Indexed(unique=true)
    private String numberPhone;
    @Email
    private String email;
    @DecimalMin(value = "0.00", message = "Monto no negativo")
    private Double amount;
    private String targetAssociated;
}
