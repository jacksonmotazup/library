package br.com.zup.library.livro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/livros")
public class LivroController {

    private final LivroRepository livroRepository;

    @Autowired
    public LivroController(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    @PostMapping
    @Transactional
    public Long cadastra(@Valid @RequestBody NovoLivroRequest request) {
        var novoLivro = request.toModel();

        livroRepository.save(novoLivro);

        return novoLivro.getId();
    }
}
