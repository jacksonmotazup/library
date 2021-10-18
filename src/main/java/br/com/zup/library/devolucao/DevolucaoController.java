package br.com.zup.library.devolucao;

import br.com.zup.library.emprestimo.EmprestimoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/devolucoes")
public class DevolucaoController {

    private final EmprestimoRepository emprestimoRepository;

    @Autowired
    public DevolucaoController(EmprestimoRepository emprestimoRepository) {
        this.emprestimoRepository = emprestimoRepository;
    }

    @PostMapping
    @Transactional
    public void devolve(@RequestBody @Valid NovaDevolucaoRequest request) {
        var emprestimo = emprestimoRepository.findByIdAndUsuarioId(request.getEmprestimoId(), request.getUsuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empréstimo não encontrado"));

        if (emprestimo.foiDevolvido()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Empréstimo já devolvido");
        }

        emprestimo.devolve();
    }
}
