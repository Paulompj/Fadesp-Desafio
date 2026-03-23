package fadesp.java.TesteTecnico_Paulo.dto;

import fadesp.java.TesteTecnico_Paulo.domain.enums.MetodoPagamento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record PagamentoRequestDTO(
                @Schema(description = "Código identificador do débito", example = "12345") @NotNull(message = "Código do débito é obrigatório") Integer codigoDebito,

                @Schema(description = "CPF ou CNPJ do pagador", example = "12345678909") @NotBlank(message = "CPF ou CNPJ é obrigatório") String cpfCnpj,

                @Schema(description = "Método de Pagamento", example = "PIX") @NotNull(message = "Método de pagamento é obrigatório") MetodoPagamento metodoPagamento,

                @Schema(description = "Número do cartão. Obs: Obrigatório apenas para pagamentos com cartão de crédito ou débito", example = "1234567812345678") String numeroCartao,

                @Schema(description = "Valor do Pagamento", example = "150.50") @NotNull(message = "Valor do pagamento é obrigatório") BigDecimal valor) {
}
