package br.com.zup.library.emprestimo;

import br.com.zup.library.livro.LivroRepository;
import br.com.zup.library.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/emprestimos")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;
    private final UsuarioRepository usuarioRepository;
    private final LivroRepository livroRepository;

    @Autowired
    public EmprestimoController(EmprestimoService emprestimoService, UsuarioRepository usuarioRepository, LivroRepository livroRepository) {
        this.emprestimoService = emprestimoService;
        this.usuarioRepository = usuarioRepository;
        this.livroRepository = livroRepository;
    }

    @PostMapping
    public NovoEmprestimoResponse cadastra(@Valid @RequestBody NovoEmprestimoRequest request) {
        var solicitacaoEmprestimo = request.toModel(usuarioRepository, livroRepository);
        return emprestimoService.realizaEmprestimo(solicitacaoEmprestimo);
    }
}
