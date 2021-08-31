package br.com.zup.library.compartilhado.validacao;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {CampoUnicoValidator.class})
public @interface CampoUnico {

    String message () default "Campo existente";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String campo();
    Class<?> classe();
}
