package br.com.zup.library.compartilhado.validacao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CampoUnicoValidator implements ConstraintValidator<CampoUnico, Object> {

    private String campo;
    private Class<?> classe;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void initialize(CampoUnico parametros) {
        campo = parametros.campo();
        classe = parametros.classe();
    }

    @Override
    public boolean isValid(Object valor, ConstraintValidatorContext context) {
        var resultado = entityManager.createQuery("SELECT 1 FROM " + classe.getName() + " WHERE " + campo + "=?1")
                .setParameter(1, valor)
                .getResultList();

        return resultado.isEmpty();
    }
}
