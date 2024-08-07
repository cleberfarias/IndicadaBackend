package serverIndicada.Indicada.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;
import serverIndicada.Indicada.Entity.Produto;
import serverIndicada.Indicada.Repository.ProdutoRepository;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/produto")
@CrossOrigin(origins = "*")

public class ProdutoContreller {

    @Autowired
    private ProdutoRepository produtoRepository;

    @GetMapping
    public List<Produto> getAllProdutos() {
        return produtoRepository.findAll();
    }

    @PostMapping("/criarProduto")
    public Produto createProduto(@RequestBody Produto produto) {
        return produtoRepository.save(produto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> obterProdutoPorId(@PathVariable Long id) {
        Optional<Produto> produto = produtoRepository.findById(id);
        return produto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizarProduto(@PathVariable Long id, @RequestBody Produto produtoDetalhes) {
        Optional<Produto> produtoOptional = produtoRepository.findById(id);

        if (produtoOptional.isPresent()) {
            Produto produto = produtoOptional.get();
            produto.setNome(produtoDetalhes.getNome());
            produto.setPreco(produtoDetalhes.getPreco());
            produto.setDescricao(produtoDetalhes.getDescricao());
            produto.setEstoque(produtoDetalhes.getEstoque());
            produto.setImagem(produtoDetalhes.getImagem());

            Produto produtoAtualizado = produtoRepository.save(produto);
            return ResponseEntity.ok(produtoAtualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Produto> atualizarProdutoParcial(@PathVariable Long id, @RequestBody Map<String, Object> atualizacoes) {
        Optional<Produto> produtoOptional = produtoRepository.findById(id);

        if (produtoOptional.isPresent()) {
            Produto produto = produtoOptional.get();

            atualizacoes.forEach((chave, valor) -> {
                Field campo = ReflectionUtils.findField(Produto.class, chave);
                if (campo != null) {
                    campo.setAccessible(true);
                    ReflectionUtils.setField(campo, produto, valor);
                }
            });

            Produto produtoAtualizado = produtoRepository.save(produto);
            return ResponseEntity.ok(produtoAtualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirProduto(@PathVariable Long id) {
        if (produtoRepository.existsById(id)) {
            produtoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}