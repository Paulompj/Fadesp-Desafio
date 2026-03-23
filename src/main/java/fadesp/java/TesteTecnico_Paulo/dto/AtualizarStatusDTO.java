package fadesp.java.TesteTecnico_Paulo.dto;

import fadesp.java.TesteTecnico_Paulo.domain.enums.StatusPagamento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record AtualizarStatusDTO(
        @Schema(description = "Novo status desejado para o pagamento", example = "PROCESSADO_COM_SUCESSO")
        @NotNull(message = "O novo status é obrigatório")
        StatusPagamento status
) {
}
