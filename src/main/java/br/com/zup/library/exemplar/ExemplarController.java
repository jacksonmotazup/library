package br.com.zup.library.exemplar;

import br.com.zup.library.livro.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import javax.validation.Valid;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/api/v1")
public class ExemplarController {

    private final LivroRepository livroRepository;
    private final ExemplarRepository exemplarRepository;

    @Autowired
    public ExemplarController(LivroRepository livroRepository, ExemplarRepository exemplarRepository) {
        this.livroRepository = livroRepository;
        this.exemplarRepository = exemplarRepository;
    }

    @PostMapping("/livros/{isbn}/exemplares")
    @Transactional
    public ResponseEntity<Long> cadastra(@PathVariable String isbn,
                                         @Valid @RequestBody NovoExemplarRequest request) {

        var livro = livroRepository.findByIsbn(isbn)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Isbn não encontrado"));

        var novoExemplar = request.toModel(livro);
        exemplarRepository.save(novoExemplar);

        return ResponseEntity.ok().body(novoExemplar.getId());
    }
}
