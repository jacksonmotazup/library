package br.com.zup.library.exemplar;

import br.com.zup.library.livro.Livro;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.Assert;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class NovoExemplarRequest {

    @JsonProperty("circulacao")
    @NotBlank
    @Pattern(regexp = "(?i)(livre|restrita)", message = "Circulação deve ser livre ou restrita")
    private String circulacao;

    public String getCirculacao() {
        return circulacao;
    }

    public Exemplar toModel(Livro livro) {
        Assert.notNull(livro, "Livro não pode ser nulo");
        return new Exemplar(circulacao.toUpperCase(), livro);
    }
}