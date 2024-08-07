package serverIndicada.Indicada.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import serverIndicada.Indicada.Entity.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
