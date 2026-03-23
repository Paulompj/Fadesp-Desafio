package fadesp.java.TesteTecnico_Paulo.controller;

import fadesp.java.TesteTecnico_Paulo.domain.enums.StatusPagamento;
import fadesp.java.TesteTecnico_Paulo.dto.AtualizarStatusDTO;
import fadesp.java.TesteTecnico_Paulo.dto.PagamentoRequestDTO;
import fadesp.java.TesteTecnico_Paulo.dto.PagamentoResponseDTO;
import fadesp.java.TesteTecnico_Paulo.service.PagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

@RestController
@RequestMapping("/api/pagamentos")
@Tag(name = "Pagamentos", description = "Gerencia recebimento e Pagamentos")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @Operation(summary = "Fazer um novo pagamento", description = "Registrará um novo pagamento com status inicial Pendente de Processamento. Retornará o pagamento criado.")
    @PostMapping
    public ResponseEntity<PagamentoResponseDTO> receberPagamento(
            @Parameter(description = "Dados para realização do pagamento") @Valid @RequestBody PagamentoRequestDTO dto) {
        PagamentoResponseDTO response = pagamentoService.receberPagamento(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Atualizar status de um pagamento", description = "Alterar o status do Pagamento")
    @PatchMapping("/{id}/status")
    public ResponseEntity<PagamentoResponseDTO> atualizarStatus(
            @Parameter(description = "ID do pagamento") @PathVariable(name = "id") Long id,
            @Parameter(description = "Dados para atualização de status") @Valid @RequestBody AtualizarStatusDTO dto) {
        PagamentoResponseDTO response = pagamentoService.atualizarStatus(id, dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar e filtrar pagamentos", description = "Lista os pagamentos filtrando pelos parâmetros informados.")
    @GetMapping
    public ResponseEntity<List<PagamentoResponseDTO>> listarPagamentos(
            @Parameter(description = "Código do débito") @RequestParam(name = "Código Débito", required = false) Integer codigoDebito,
            @Parameter(description = "CPF ou CNPJ") @RequestParam(name = "CPF/CNPJ", required = false) String cpfCnpj,
            @Parameter(description = "Status do pagamento") @RequestParam(name = "Status", required = false) StatusPagamento status,
            @Parameter(description = "Situação do pagamento") @RequestParam(name = "Ativo/Inativo", required = false, defaultValue = "true") Boolean ativo) {
        List<PagamentoResponseDTO> response = pagamentoService.listarPagamentos(codigoDebito, cpfCnpj, status, ativo);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Exclusão Lógica do pagamento", description = "Inativa o pagamento caso seu status seja Pendente de Processamento.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirPagamento(
            @Parameter(description = "ID do pagamento") @PathVariable(name = "id") Long id) {
        pagamentoService.excluirPagamento(id);
        return ResponseEntity.noContent().build();
    }
}
