package pl.com.bottega.hrs.model;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.function.Consumer;

public class HrsModel {
    private EntityManagerFactory emf;
    private EntityManager em;

    public EntityManagerFactory getEmf() {
        if (emf != null) {
            return emf;
        }
        emf = Persistence.createEntityManagerFactory("HRS");
        return emf;
    }

    public EntityManager getEm() {
        if (em != null) {
            return em;
        }
        em = getEmf().createEntityManager();
        return em;
    }

    protected void executeInTransaction(Consumer<EntityManager> consumer) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        consumer.accept(em);
        em.close();
        em.getTransaction().commit();
    }
}