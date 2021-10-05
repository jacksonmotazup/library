package br.com.zup.library.usuario;

import br.com.zup.library.compartilhado.exception.RequestInvalidoException;

import java.util.Objects;

public enum TipoUsuario {

    PADRAO {
        @Override
        public Integer calculaPrazoDeDevolucaoMaximo(Integer prazoDevolucaoDias) {
            if (prazoDevolucaoDias != null) {
                return prazoDevolucaoDias;
            }
            throw new RequestInvalidoException("Prazo deve ser preenchido");
        }
    },

    PESQUISADOR {
        @Override
        public Integer calculaPrazoDeDevolucaoMaximo(Integer prazoDevolucaoDias) {
            return Objects.requireNonNullElse(prazoDevolucaoDias, 60);
        }
    };

    public abstract Integer calculaPrazoDeDevolucaoMaximo(Integer prazoDevolucaoDias);
}
