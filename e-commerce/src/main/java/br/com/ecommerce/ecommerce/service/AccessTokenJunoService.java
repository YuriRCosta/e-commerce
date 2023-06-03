package br.com.ecommerce.ecommerce.service;

import br.com.ecommerce.ecommerce.model.AccessTokenAPIPagamento;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

@Service
public class AccessTokenJunoService {

    @PersistenceContext
    private EntityManager entityManager;

    public AccessTokenAPIPagamento buscaTokenAtivo() {
        try {
            return entityManager.createQuery("select a from AccessTokenAPIPagamento a", AccessTokenAPIPagamento.class).setMaxResults(1).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
