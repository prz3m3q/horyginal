package pl.com.bottega.hrs.instractructure;

import org.junit.After;
import org.junit.BeforeClass;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.function.Consumer;

public abstract class InfrastructureTest {

    protected static EntityManagerFactory emf;

    /**
     * tylko raz przed każdym testem
     */
    @BeforeClass
    public static void setUp() {
        emf = Persistence.createEntityManagerFactory("HRS-TEST");
    }

    @After
    public void cleanUp() {
        executeInTransaction((em) -> {
            em.createNativeQuery("DELETE FROM employees").executeUpdate();
        });
    }

    protected void executeInTransaction(Consumer<EntityManager> consumer) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        consumer.accept(em);
        em.close();
        em.getTransaction().commit();
    }

    protected EntityManager createEntityManager() {
        return emf.createEntityManager();
    }
}
