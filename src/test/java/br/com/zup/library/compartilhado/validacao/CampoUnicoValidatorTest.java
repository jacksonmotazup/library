package br.com.zup.library.compartilhado.validacao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
class CampoUnicoValidatorTest {

    @Mock
    private EntityManager entityManager;
    @Mock
    private Query query;
    @InjectMocks
    private CampoUnicoValidator validator;

    @Test
    @DisplayName("Deve ser válido quando não houver esse campo cadastrado")
    void deveSerValidoQuandoNaoHouverOutroCadastrado() {
        setField(validator, "campo", "campo");
        setField(validator, "klass", Object.class);

        when(entityManager.createQuery(Mockito.anyString())).thenReturn(query);
        when(query.setParameter(anyInt(), anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of());

        Assertions.assertTrue(validator.isValid("isbn", null));
    }

    @Test
    @DisplayName("Não deve ser válido quando houver esse campo cadastrado")
    void naoDeveSerValidoQuandoHouverOutroCadastrado() {
        setField(validator, "campo", "campo");
        setField(validator, "klass", Object.class);

        when(entityManager.createQuery(Mockito.anyString())).thenReturn(query);
        when(query.setParameter(anyInt(), anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(Object.class));

        Assertions.assertFalse(validator.isValid("isbn", null));
    }
}