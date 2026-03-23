package fadesp.java.TesteTecnico_Paulo.service;

import fadesp.java.TesteTecnico_Paulo.domain.entity.Pagamento;
import fadesp.java.TesteTecnico_Paulo.domain.enums.MetodoPagamento;
import fadesp.java.TesteTecnico_Paulo.domain.enums.StatusPagamento;
import fadesp.java.TesteTecnico_Paulo.dto.AtualizarStatusDTO;
import fadesp.java.TesteTecnico_Paulo.dto.PagamentoRequestDTO;
import fadesp.java.TesteTecnico_Paulo.dto.PagamentoResponseDTO;
import fadesp.java.TesteTecnico_Paulo.exception.BusinessException;
import fadesp.java.TesteTecnico_Paulo.exception.ResourceNotFoundException;
import fadesp.java.TesteTecnico_Paulo.repository.PagamentoRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;

    public PagamentoService(PagamentoRepository pagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
    }

    @Transactional
    public PagamentoResponseDTO receberPagamento(PagamentoRequestDTO dto) {
        validarMetodoPagamento(dto.metodoPagamento(), dto.numeroCartao());

        boolean isCartao = dto.metodoPagamento() == MetodoPagamento.CARTAO_CREDITO
                || dto.metodoPagamento() == MetodoPagamento.CARTAO_DEBITO;

        String numeroCartaoParaSalvar = isCartao ? dto.numeroCartao() : "";

        Pagamento pagamento = new Pagamento(
                dto.codigoDebito(),
                dto.cpfCnpj(),
                dto.metodoPagamento(),
                numeroCartaoParaSalvar,
                dto.valor());

        Pagamento saved = pagamentoRepository.save(pagamento);
        return PagamentoResponseDTO.fromEntity(saved);
    }

    @Transactional
    public PagamentoResponseDTO atualizarStatus(Long id, AtualizarStatusDTO dto) {
        Pagamento pagamento = buscarPorId(id);

        StatusPagamento statusAtual = pagamento.getStatus();
        StatusPagamento novoStatus = dto.status();

        validarTransicaoDeStatus(statusAtual, novoStatus);

        pagamento.setStatus(novoStatus);
        pagamento = pagamentoRepository.save(pagamento);

        return PagamentoResponseDTO.fromEntity(pagamento);
    }

    @Transactional(readOnly = true)
    public List<PagamentoResponseDTO> listarPagamentos(Integer codigoDebito, String cpfCnpj, StatusPagamento status, Boolean ativo) {
        Pagamento probe = new Pagamento();
        if (ativo != null) {
            probe.setAtivo(ativo);
        }
        if (codigoDebito != null)
            probe.setCodigoDebito(codigoDebito);
        if (cpfCnpj != null)
            probe.setCpfCnpj(cpfCnpj);
        if (status != null)
            probe.setStatus(status);

        List<Pagamento> pagamentos = pagamentoRepository.findAll(Example.of(probe));
        return pagamentos.stream()
                .map(PagamentoResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void excluirPagamento(Long id) {
        Pagamento pagamento = buscarPorId(id);

        if (pagamento.getStatus() != StatusPagamento.PENDENTE_DE_PROCESSAMENTO) {
            throw new BusinessException(
                    "A exclusão lógica só é permitida para pagamentos com status 'Pendente de Processamento'.");
        }

        pagamento.setAtivo(false);
        pagamentoRepository.save(pagamento);
    }

    private Pagamento buscarPorId(Long id) {
        return pagamentoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pagamento não encontrado ou inativo."));
    }

    private void validarMetodoPagamento(MetodoPagamento metodo, String numeroCartao) {
        boolean exigeCartao = (metodo == MetodoPagamento.CARTAO_CREDITO || metodo == MetodoPagamento.CARTAO_DEBITO);

        if (exigeCartao && (numeroCartao == null || numeroCartao.trim().isEmpty())) {
            throw new BusinessException(
                    "Número do cartão é obrigatório para pagamento via cartão de crédito ou débito.");
        }
    }

    private void validarTransicaoDeStatus(StatusPagamento atual, StatusPagamento novo) {
        if (novo == null) {
            throw new BusinessException("Status de Pagamento inválido ou não informado.");
        }
        boolean statusValido = Arrays.stream(StatusPagamento.values())
                .anyMatch(enumValue -> enumValue == novo);
        if (!statusValido) {
            throw new BusinessException("Status de Pagamento inválido.");
        }
        if (atual == StatusPagamento.PROCESSADO_COM_SUCESSO) {
            throw new BusinessException("Pagamento já processado com sucesso não pode ter o status alterado.");
        }

        if (atual == StatusPagamento.PENDENTE_DE_PROCESSAMENTO) {
            if (novo != StatusPagamento.PROCESSADO_COM_SUCESSO && novo != StatusPagamento.PROCESSADO_COM_FALHA) {
                throw new BusinessException("Transição de status inválida a partir de Pendente de Processamento.");
            }
        } else if (atual == StatusPagamento.PROCESSADO_COM_FALHA) {
            if (novo != StatusPagamento.PENDENTE_DE_PROCESSAMENTO) {
                throw new BusinessException(
                        "Um Pagamento com Falha só pode ser atualizado para Pendente de Processamento.");
            }
        }
    }
}
