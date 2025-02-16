/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.mara.ad;

import jakarta.persistence.Query;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author tedax
 */
public class ClienteDAO {

    public static void anadirCliente(String nombre, String email) {
        Session session = Conexion.getSession();
        Transaction transaction = session.beginTransaction();

        try {
            if (nombre == null || nombre.isEmpty() || email == null || email.isEmpty()) {
                throw new Exception("Los campos nombre y email no pueden ser nulos ni vacíos");
            }

            Cliente clienteExistente = session.createQuery("FROM Cliente WHERE email =:email ", Cliente.class)
                    .setParameter("email", email)
                    .uniqueResult();

            if (clienteExistente != null) {
                throw new Exception("Cliente con email existente");
            }

            Cliente cliente = new Cliente(nombre, email);

            if (cliente == null) {
                throw new Exception("Cliente no añadido");
            }

            session.persist(cliente);

            System.out.println("Cliente añadido con ID: " + cliente.getId());

            transaction.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            transaction.rollback();
        } finally {
            session.close();
        }
    }

    public static void modificarCliente(int id, String nombre, String email) {
        Session session = Conexion.getSession();
        Transaction transaction = session.beginTransaction();

        try {
            if (nombre == null || nombre.isEmpty() || email == null || email.isEmpty()) {
                throw new Exception("Los campos nombre y email no pueden ser nulos ni vacíos");
            }

            Cliente clienteExistente = session.createQuery("FROM Cliente WHERE email =:email ", Cliente.class)
                    .setParameter("email", email)
                    .uniqueResult();

            if (clienteExistente != null) {
                throw new Exception("Cliente con email existente");
            }

            Cliente cliente = new Cliente(nombre, email);
            cliente.setId(id);
            session.merge(cliente);

            transaction.commit();
            System.out.println("Cliente modificado con exito");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            transaction.rollback();
        } finally {
            session.close();
        }
    }

    public static void borrarCliente(int id) {

        Session session = Conexion.getSession();
        Transaction transaction = session.beginTransaction();

        try {
            Cliente cliente = session.get(Cliente.class, id);
            if (cliente == null) {
                System.out.println("Cliente no encontrada.");
                throw new Exception("Cliente no encontrada.");
            }
            List<Compra> compras = cliente.getCompraList();

            for (Compra compra : compras) {

                Actividad actividad = compra.getIdActividad();

                String fechaActual = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                if (actividad.getFecha().compareTo(fechaActual) > 0) {
                    System.out.println("No se puede borrar el cliente porque tiene actividades pendientes.");
                    throw new Exception("No se puede borrar el cliente porque tiene actividades pendientes.");
                }

            }

            session.remove(cliente);
            transaction.commit();
            System.out.println("Actividad y sus compras asociadas eliminadas correctamente.");
        } catch (Exception e) {
            transaction.rollback();
            System.err.println("Error al intentar borrar la actividad: " + e.getMessage());
        } finally {
            session.close();
        }

    }

    public static List<Cliente> listarClientes() {
        try (Session session = Conexion.getSession()) {

            List<Cliente> clientes = session.createQuery("FROM Cliente", Cliente.class).getResultList();

            return clientes;
        } catch (Exception e) {
            return null;
        }

    }

    public static void listarDetallesCliente(int id) {
        try (Session session = Conexion.getSession()) {
            Cliente cliente = session.get(Cliente.class, id);
           
            
            if (cliente != null) {
                System.out.println("Id proveedor: " + cliente.getId());
                System.out.println("Nombre: " + cliente.getNombre());
                System.out.println("Email: " + cliente.getEmail());
                System.out.println("---------------------------");
                for (Compra compra : cliente.getCompraList()) {
                    System.out.println("---------------------------");
                    System.out.println("ID compra" + compra.getIdActividad().getId());
                    System.out.println("Nombre Actividad: " + compra.getIdActividad().getNombre());
                    System.out.println("Ubicacion: " + compra.getIdActividad().getUbicacion());
                    System.out.println("Nombre Proveedor: " + compra.getIdActividad().getIdProveedor().getNombre());
                    System.out.println("Fecha actividad: " + compra.getIdActividad().getFecha());
                    System.out.println("Fecha compra: " + compra.getFechaCompra());
                }

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
