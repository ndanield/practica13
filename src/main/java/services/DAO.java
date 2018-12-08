package services;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Id;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import java.lang.reflect.Field;
import java.util.List;

public class DAO<T> {
    private static EntityManagerFactory emf;
    private Class<T> entityClass;


    public DAO(Class<T> entityClass) {
        if(emf == null) {
            emf = Persistence.createEntityManagerFactory("Persistencia");
        }
        this.entityClass = entityClass;

    }

    public EntityManager getEntityManager(){
        return emf.createEntityManager();
    }

    private Object getKeyValue(T entity){
        if(entity == null){
            return null;
        }
        //aplicando la clase de reflexión.
        for(Field f : entity.getClass().getDeclaredFields()) {  //tomando todos los campos privados.
            if (f.isAnnotationPresent(Id.class)) {
                try {
                    f.setAccessible(true);
                    Object valorCampo = f.get(entity);

                    System.out.println("Name of the field: "+f.getName());
                    System.out.println("Type of the field: "+f.getType().getName());
                    System.out.println("Value of the field: "+valorCampo );

                    return valorCampo;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
    
    public void create(T entity){
        EntityManager em = getEntityManager();

        try {
            if (em.find(entityClass, getKeyValue(entity)) != null) {
                System.out.println("   /!\\ This entity is already persisted. Creation canceled.");
                return;
            }
        }catch (IllegalArgumentException ie){
            //
            System.out.println("Illegal parameter.");
        }

        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        }catch (Exception ex){
            em.getTransaction().rollback();
            System.out.println(ex.getMessage());
            throw  ex;
        } finally {
            em.close();
        }
    }


    public void edit(T entity){
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        }catch (Exception ex){
            em.getTransaction().rollback();
            throw  ex;
        } finally {
            em.close();
        }
    }
    
    public void delete(Object  entityId){
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            T entity = em.find(entityClass, entityId);
            em.remove(entity);
            em.getTransaction().commit();
        }catch (Exception ex){
            em.getTransaction().rollback();
            throw  ex;
        } finally {
            em.close();
        }
    }

    public T find(Object id) {
        EntityManager em = getEntityManager();
        try{
            return em.find(entityClass, id);
        } finally {
            em.close();
        }
    }

    public List<T> findAll(){
        EntityManager em = getEntityManager();
        try{
            CriteriaQuery<T> criteriaQuery = em.getCriteriaBuilder().createQuery(entityClass);
            criteriaQuery.select(criteriaQuery.from(entityClass));
            return em.createQuery(criteriaQuery).getResultList();
        } finally {
            em.close();
        }
    }
}
