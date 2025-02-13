/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.mara.ad;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author DAM1
 */
public class CompraDAO {

    public static void cancelarCompra(int idActividad, int idCliente) {
        Session session = Conexion.getSession();
        Transaction transaction = session.beginTransaction();

        try {
            Actividad actividad = session.get(Actividad.class, idActividad);
            if (actividad == null) {
                System.out.println("Actividad no encontrada.");
                throw new Exception("Actividad no encontrada.");
            }
            Cliente cliente = session.get(Cliente.class, idCliente);
            if (cliente == null) {
                System.out.println("Cliente no encontrado.");
                throw new Exception("Cliente no encontrada.");
            }

            LocalDate date = LocalDate.now();
            String hoy = date.toString();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaActividad = sdf.parse(actividad.getFecha());
            Date fechaHoy = sdf.parse(hoy);
            if (fechaActividad.before(fechaHoy)) {

                System.out.println("No se puede borrar el proveedor porque tiene actividades pendientes.");
                throw new Exception("No se puede borrar el proveedor porque tiene actividades pendientes.");
            }

            Compra compra = session.createQuery(
                    "FROM Compra WHERE idActividad.id = :actividad AND idCliente.id = :cliente", Compra.class)
                    .setParameter("actividad", idActividad) // Pasamos el objeto Actividad completo
                    .setParameter("cliente", idCliente) // Pasamos el objeto Cliente completo
                    .getSingleResult();

            if (compra == null) {
                System.out.println("Compra no encontrada para este cliente en la actividad especificada.");
                throw new Exception("Compra no encontrada para este cliente en la actividad especificada.");
            }

            session.remove(compra);

            actividad.setPlazasDisponibles(actividad.getPlazasDisponibles() + 1);
            session.merge(actividad);

            // Confirmar la transacción
            transaction.commit();
            System.out.println("Compra cancelada correctamente.");
        } catch (Exception e) {
            // Manejar errores y revertir la transacción
            transaction.rollback();
            System.err.println("Error al intentar cancelar la compra: " + e.getMessage());
        } finally {
            // Cerrar la sesión
            session.close();
        }
    }

    public static void comprarActividad(int idActividad, int idCliente) {
        Session session = Conexion.getSession();
        Transaction transaction = session.beginTransaction();

        try {
            Cliente cliente = session.get(Cliente.class, idCliente);
            if (cliente == null) {
                System.out.println("Cliente no encontrado.");
                throw new Exception("Cliente no encontrada.");
            }

            Actividad actividad = session.get(Actividad.class, idActividad);
            if (actividad == null) {
                System.out.println("Actividad no encontrada.");
                throw new Exception("Actividad no encontrada.");
            }

            if (actividad.getPlazasDisponibles() <= 0) {
                System.out.println("No hay plazas disponibles para esta actividad.");
                throw new Exception("No hay plazas disponibles para esta actividad.");
            }
            LocalDate date = LocalDate.now();
            String hoy = date.toString();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaActividad = sdf.parse(actividad.getFecha());
            Date fechaHoy = sdf.parse(hoy);
            if (fechaActividad.before(fechaHoy)) {

                System.out.println("No se puede borrar el proveedor porque tiene actividades pendientes.");
                throw new Exception("No se puede borrar el proveedor porque tiene actividades pendientes.");
            }

            LocalDateTime fechaActualCompra = LocalDateTime.now();

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
            String fechaCompra = dtf.format(fechaActualCompra);

            Compra nuevaCompra = new Compra();
            nuevaCompra.setIdActividad(actividad);
            nuevaCompra.setIdCliente(cliente);
            nuevaCompra.setFechaCompra(fechaCompra);

            session.persist(nuevaCompra);

            actividad.setPlazasDisponibles(actividad.getPlazasDisponibles() - 1);
            session.merge(actividad);

            transaction.commit();
            System.out.println("Compra realizada correctamente.");
        } catch (Exception e) {
            // Manejar errores y revertir la transacción
            transaction.rollback();
            System.err.println("Error al intentar realizar la compra: " + e.getMessage());
        } finally {
            // Cerrar la sesión
            session.close();
        }
    }
}
