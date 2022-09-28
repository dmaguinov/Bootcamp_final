package nttdata.grupouno.com.operations.models;

import javax.validation.constraints.NotEmpty;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@EntityScan
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "accountClient")
public class AccountClientModel {
    @NotEmpty
    private String codeClient;
    private String numberAccount;
    @NotEmpty
    private String typeClient; // Persona : N - Empresa: J
    @NotEmpty
    private String status; // T : Titular - F : Firmante
    private String typeAccount;
}
