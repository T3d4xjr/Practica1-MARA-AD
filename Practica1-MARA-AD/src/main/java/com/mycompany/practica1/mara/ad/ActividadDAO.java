/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.mara.ad;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author tedax
 */
public class ActividadDAO {

    public Actividad listarDetallesActividad(int id) {
        Session session = Conexion.getSession();
        Transaction transaction = session.beginTransaction();

        try {
            Actividad actividad = session.get(Actividad.class, id);
            System.out.println("Nombre: " + actividad.getNombre());
            System.out.println("Fecha: " + actividad.getFecha());
            System.out.println("Ubicacion: " + actividad.getUbicacion());
            System.out.println("Plazas disponibles: " + actividad.getPlazasDisponibles());
            System.out.println("---------------------------");
            System.out.println("Id proveedor: " + actividad.getIdProveedor().getId());
            System.out.println("Nombre: " + actividad.getIdProveedor().getNombre());

            List<Compra> compras = actividad.getCompraList();

            for (Compra compra : compras) {
                System.out.println("---------------------------");
                System.out.println("ID cliente: " + compra.getIdCliente().getId());
                System.out.println("Nombre: " + compra.getIdCliente().getNombre());
                System.out.println("Email: " + compra.getIdCliente().getEmail());
                System.out.println("Fecha compra: " + compra.getFechaCompra());

            }

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            session.close();
        }
        return null;

    }

    public void listarActividadesFuturas() {
        Session session = Conexion.getSession();
        Transaction transaction = session.beginTransaction();

        try {
            String fechaActual = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            List<Actividad> actividads = session.createQuery("FROM Actividad WHERE fecha >:fechaActual ", Actividad.class)
                    .setParameter("fechaActual", fechaActual).getResultList();
            for (Actividad actividad : actividads) {
                System.out.println("---------------------------");
                System.out.println("Nombre: " + actividad.getNombre());
                System.out.println("Fecha: " + actividad.getFecha());
                System.out.println("Ubicacion: " + actividad.getUbicacion());
                System.out.println("Plazas disponibles: " + actividad.getPlazasDisponibles());
                System.out.println("Nombre proveedor: " + actividad.getIdProveedor().getNombre());
            }

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            session.close();
        }
    }

    public boolean cancelarCompra(int idActividad, int idCliente) {
        Session session = Conexion.getSession();
        Transaction transaction = session.beginTransaction();

        try {
            Actividad actividad = session.get(Actividad.class, idActividad);
            if (actividad == null) {
                System.out.println("Actividad no encontrada.");
                return false;
            }
            String fechaActual = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (actividad.getFecha().compareTo(fechaActual) > 0) {
                System.out.println("No se puede borrar el proveedor porque tiene actividades pendientes.");
                transaction.rollback();
                return false;
            }
            Compra compra= session.createQuery("FROM Compra WHERE idActividad = :idActividad AND idCliente = :idCliente", Compra.class)
                    .setParameter("idActividad", idActividad)
                    .setParameter("idCliente", idCliente).uniqueResult();
       
            if (compra == null) {
                System.out.println("Compra no encontrada para este cliente en la actividad especificada.");
                return false;
            }

            session.remove(compra);

            actividad.setPlazasDisponibles(actividad.getPlazasDisponibles() + 1);
            session.merge(actividad);

            // Confirmar la transacción
            transaction.commit();
            System.out.println("Compra cancelada correctamente.");
            return true;
        } catch (Exception e) {
            // Manejar errores y revertir la transacción
            transaction.rollback();
            System.err.println("Error al intentar cancelar la compra: " + e.getMessage());
            return false;
        } finally {
            // Cerrar la sesión
            session.close();
        }
    }

    public boolean comprarActividad(int idActividad, int idCliente) {
        Session session = Conexion.getSession();
        Transaction transaction = session.beginTransaction();

        try {
            // Obtener la actividad por ID
            Actividad actividad = session.get(Actividad.class, idActividad);
            if (actividad == null) {
                System.out.println("Actividad no encontrada.");
                return false;
            }

            // Validar si la actividad tiene plazas disponibles
            if (actividad.getPlazasDisponibles() <= 0) {
                System.out.println("No hay plazas disponibles para esta actividad.");
                return false;
            }

            String fechaActual = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (actividad.getFecha().compareTo(fechaActual) > 0) {
                System.out.println("No se puede borrar el proveedor porque tiene actividades pendientes.");
                transaction.rollback();
                return false;
            }

            // Obtener al cliente por ID
            Cliente cliente = session.get(Cliente.class, idCliente);
            if (cliente == null) {
                System.out.println("Cliente no encontrado.");
                return false;
            }

            Compra nuevaCompra = new Compra();
            nuevaCompra.setIdActividad(actividad);
            nuevaCompra.setIdCliente(cliente);
            nuevaCompra.setFechaCompra(fechaActual);

            session.persist(nuevaCompra);

            actividad.setPlazasDisponibles(actividad.getPlazasDisponibles() - 1);
            session.merge(actividad);

            transaction.commit();
            System.out.println("Compra realizada correctamente.");
            return true;
        } catch (Exception e) {
            // Manejar errores y revertir la transacción
            transaction.rollback();
            System.err.println("Error al intentar realizar la compra: " + e.getMessage());
            return false;
        } finally {
            // Cerrar la sesión
            session.close();
        }
    }

    public boolean borrarActividad(int id) {
        Session session = Conexion.getSession();
        Transaction transaction = session.beginTransaction();

        try {
            Actividad actividad = session.get(Actividad.class, id);
            if (actividad == null) {
                System.out.println("Actividad no encontrada.");
                return false;
            }

            List<Compra> compras = actividad.getCompraList();

            for (Compra compra : compras) {
                session.remove(compra);
            }

            session.remove(actividad);

            transaction.commit();
            System.out.println("Actividad y sus compras asociadas eliminadas correctamente.");
            return true;
        } catch (Exception e) {
            transaction.rollback();
            System.err.println("Error al intentar borrar la actividad: " + e.getMessage());
            return false;
        } finally {
            // Cerrar la sesión
            session.close();
        }
    }

    public int anadirActividad(String nombre, Date fecha, String ubicacion, int plazas, int idProveedor) {

        Session session = Conexion.getSession();
        Transaction transaction = session.beginTransaction();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String fechaa = sdf.format(fecha);

            Actividad actividad = new Actividad(nombre, fechaa, ubicacion, plazas);

            Proveedor proveedor = session.get(Proveedor.class, idProveedor);
            actividad.setIdProveedor(proveedor);
            session.persist(actividad);

            System.out.println("Actividad añadido con ID:" + actividad.getId());

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            session.close();
        }

        return 1;
    }

}
