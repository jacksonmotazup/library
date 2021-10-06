package br.com.zup.library.usuario;

import br.com.zup.library.compartilhado.exception.RequestInvalidoException;

import java.util.Objects;

public enum TipoUsuario {

    PADRAO {
        @Override
        public Long calculaPrazoDeDevolucaoMaximo(Long prazoDevolucaoDias) {
            if (prazoDevolucaoDias != null) {
                return prazoDevolucaoDias;
            }
            throw new RequestInvalidoException("Prazo deve ser preenchido");
        }
    },

    PESQUISADOR {
        @Override
        public Long calculaPrazoDeDevolucaoMaximo(Long prazoDevolucaoDias) {
            return Objects.requireNonNullElse(prazoDevolucaoDias, 60L);
        }
    };

    public abstract Long calculaPrazoDeDevolucaoMaximo(Long prazoDevolucaoDias);
}
