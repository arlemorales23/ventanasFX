package org.arle.proyectofx.dao;



import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.arle.proyectofx.model.Producto;
import org.arle.proyectofx.util.HibernateUtil;


import java.util.List;

public class ProductoDAO {

    public void guardar(Producto producto) {
        EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(producto);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    public void actualizar(Producto producto) {
        EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.merge(producto);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    public void eliminar(Producto producto) {
        EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            // Si el producto no está managed, primero hay que mergearlo
            producto = entityManager.merge(producto);
            entityManager.remove(producto);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    public List<Producto> listarTodos() {
        EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
        try {
            return entityManager.createQuery("SELECT p FROM Producto p", Producto.class)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    public Producto encontrarPorId(Long id) {
        EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
        try {
            return entityManager.find(Producto.class, id);
        } finally {
            entityManager.close();
        }
    }
}