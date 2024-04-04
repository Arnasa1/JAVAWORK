package com.kursinis.kursinis.hibernateControllers;

import com.kursinis.kursinis.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaQuery;

import java.util.ArrayList;
import java.util.List;

public class GenericHib {
    public EntityManagerFactory entityManagerFactory;
    public EntityManager em;

    public GenericHib(EntityManagerFactory entityManagerFactory) {

        this.entityManagerFactory = entityManagerFactory;
    }

    public EntityManager getEntityManager() {

        return entityManagerFactory.createEntityManager();
    }

    public <T> void create(T entity) {
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
        }
    }

    public <T> void update(T entity) {
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
        }
    }

    public <T> void delete(T entity) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.remove(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
        }
    }


    public <T> T getEntityById(Class<T> entityClass, int id) {
        T result = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            result = em.find(entityClass, id);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public <T> List<T> getAllRecords(Class<T> entityClass) {
        EntityManager em = null;
        List<T> result = new ArrayList<>();
        try {
            em = getEntityManager();
            CriteriaQuery query = em.getCriteriaBuilder().createQuery();
            query.select(query.from(entityClass));
            Query q = em.createQuery(query);
            result = q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
        }
        return result;
    }
    public void deleteComment(int id) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            var comment = em.find(Comment.class, id);
            Product product = comment.getProduct();
            if (product != null) {
                product.getComments().remove(comment);
                comment.setProduct(null);
                em.merge(product);
            }
            Comment parentComment = comment.getParentComment();
            if (parentComment != null) {
                parentComment.getReplies().remove(comment);
                em.merge(parentComment);
            }

            User user = comment.getUser();
            user.getMyComments().remove(comment);
            em.merge(user);

            comment.getReplies().clear();
            em.merge(comment);
            em.remove(comment);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
        }
    }

    public void deleteFeedback(int id){
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            var feedback = em.find(Review.class, id);
            OrderItem order = feedback.getOrder();
            if (order != null) {
                order.getReviews().remove(feedback);
                feedback.setOrder(null);
                em.merge(order);
            }
            Review parentReview = feedback.getParentReview();
            if (parentReview != null) {
                parentReview.getReviewReply().remove(feedback);
                em.merge(parentReview);
            }

            User user = feedback.getUser();
            user.getMyReviews().remove(feedback);
            em.merge(user);

            feedback.getReviewReply().clear();
            em.merge(feedback);
            em.remove(feedback);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
        }
    }
    public void deleteProduct(int id) {

        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            var product = em.getReference(Product.class, id);
            Cart cart = product.getCart();
            if (cart != null) {
                cart.getItemsInCart().remove(product);
                em.merge(cart);
            }
            Warehouse warehouse = product.getWarehouse();
            if (warehouse != null) {
                warehouse.getProductsInStock().remove(product);
                em.merge(warehouse);
            }

            for (Comment c : product.getComments()) {
                deleteComment(c.getId());
            }
            product.getComments().clear();
            em.merge(product);
            em.remove(product);
            em.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void deleteUser(int id) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            var user = em.getReference(User.class, id);
            Cart cart = user.getMyCart();
            if (cart != null) {
                cart.getItemsInCart().clear(); // Assuming items need to be removed from cart
                em.merge(cart);
            }

            for (Comment c : user.getMyComments()) {
                deleteComment(c.getId());
            }
            user.getMyComments().clear();

            for (Review f : user.getMyReviews()) {
                em.remove(f); // Assuming feedbacks need to be removed individually
            }
            user.getMyReviews().clear();

            em.merge(user);
            em.remove(user);
            em.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}