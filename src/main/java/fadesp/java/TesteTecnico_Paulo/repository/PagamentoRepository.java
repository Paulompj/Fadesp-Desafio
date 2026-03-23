package fadesp.java.TesteTecnico_Paulo.repository;

import fadesp.java.TesteTecnico_Paulo.domain.entity.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
    Optional<Pagamento> findByIdAndAtivoTrue(Long id);
}
