package nttdata.grupouno.com.microwallet.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "clientMovil")
public class ClientMovilModel {

    @Id
    private String id;
    @NotEmpty
    private String documento;

    @NotEmpty
    private String celular;

    @NotEmpty
    private String imei;

    @NotEmpty
    private String correo;

    @NotEmpty
    private String documentType; //  DNI , CEX , PASS
}
