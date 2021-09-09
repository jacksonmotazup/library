package br.com.zup.library.exemplar;

import br.com.zup.library.livro.Livro;
import org.springframework.util.Assert;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class NovoExemplarRequest {

    @NotBlank
    @Pattern(regexp = "(LIVRE|RESTRITA)", message = "Circulação deve ser LIVRE ou RESTRITA")
    private String circulacao;

    public String getCirculacao() {
        return circulacao;
    }

    public Exemplar toModel(Livro livro) {
        Assert.notNull(livro, "Livro não pode ser nulo");
        return new Exemplar(Circulacao.valueOf(circulacao), livro);
    }
}
