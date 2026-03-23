package fadesp.java.TesteTecnico_Paulo.dto;

import fadesp.java.TesteTecnico_Paulo.domain.entity.Pagamento;
import fadesp.java.TesteTecnico_Paulo.domain.enums.MetodoPagamento;
import fadesp.java.TesteTecnico_Paulo.domain.enums.StatusPagamento;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public record PagamentoResponseDTO(
        @Schema(description = "ID único do pagamento") Long id,
        @Schema(description = "Código do débito") Integer codigoDebito,
        @Schema(description = "CPF ou CNPJ do pagador") String cpfCnpj,
        @Schema(description = "Método de Pagamento") MetodoPagamento metodoPagamento,
        @Schema(description = "Número do cartão") String numeroCartao,
        @Schema(description = "Valor do Pagamento") BigDecimal valor,
        @Schema(description = "Status do Pagamento") StatusPagamento status,
        @Schema(description = "Situação do Pagamento") Boolean ativo) {
    public static PagamentoResponseDTO fromEntity(Pagamento pagamento) {
        return new PagamentoResponseDTO(
                pagamento.getId(),
                pagamento.getCodigoDebito(),
                pagamento.getCpfCnpj(),
                pagamento.getMetodoPagamento(),
                pagamento.getNumeroCartao(),
                pagamento.getValor(),
                pagamento.getStatus(),
                pagamento.getAtivo());
    }
}
