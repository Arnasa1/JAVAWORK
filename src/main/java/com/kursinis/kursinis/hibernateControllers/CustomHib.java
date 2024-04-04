package com.kursinis.kursinis.hibernateControllers;

import com.kursinis.kursinis.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

public class CustomHib extends GenericHib {
    public CustomHib(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    public User getUserByCredentials(String login, String password) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<User> query = cb.createQuery(User.class);
            Root<User> root = query.from(User.class);
            query.select(root).where(cb.and(cb.like(root.get("login"), login), cb.like(root.get("password"), password)));
            Query q;

            q = em.createQuery(query);
            return (User) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            if (em != null) em.close();
        }
    }

    public void deleteProduct(int id) {

        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            var product = em.find(Product.class, id);


            Warehouse warehouse = product.getWarehouse();
            if (warehouse != null) {
                em.merge(warehouse);
            }

            em.remove(product);
            em.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Product> getAvailableProducts() {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Product> query = cb.createQuery(Product.class);
            Root<Product> root = query.from(Product.class);
            query.select(root).where(cb.isNull(root.get("cart")));
            Query q;

            q = em.createQuery(query);
            return q.getResultList();
        } catch (NoResultException e) {
            return null;
        } finally {
            if (em != null) em.close();
        }
    }




}
