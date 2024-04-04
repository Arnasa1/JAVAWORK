package com.kursinis.kursinis.hibernateControllers;

import com.kursinis.kursinis.fxControllers.Hashing;
import com.kursinis.kursinis.model.User;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class UserHib {

    private EntityManagerFactory entityManagerFactory=null;
    public int UserId;
    public UserHib(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }
    private EntityManager getEntityManager(){
        return entityManagerFactory.createEntityManager();
    }

    public void createUser(User user){
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
            em.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(em != null) em.close();
        }
    }
    public User checkForRepeatingNames(String text) {
        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<User> query = cb.createQuery(User.class);
            Root<User> root = query.from(User.class);
            query.select(root).where(cb.and(cb.equal(root.get("login"), text)));

            TypedQuery<User> q = em.createQuery(query);
            User user = q.getSingleResult();
            return user;
        } catch (NoResultException e) {
            return null; // User not found or multiple users found (shouldn't happen)
        }
         finally {
            em.close();
        }
    }
    public User getUserByCredentials(String username, String password) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<User> query = cb.createQuery(User.class);
            Root<User> root = query.from(User.class);

            String hashedPassword = Hashing.hashing(password);
            query.select(root).where(cb.and(cb.equal(root.get("login"), username), cb.equal(root.get("password"), hashedPassword)));
            TypedQuery<User> q = em.createQuery(query);
            User user = q.getSingleResult();
            return user;
        } catch (NoResultException | NonUniqueResultException e) {
            return null; // User not found or multiple users found (shouldn't happen)
        } finally {
            em.close();
        }
    }
}
