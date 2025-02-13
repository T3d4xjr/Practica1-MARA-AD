/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.mara.ad;

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

    public static void anadirActividad(String nombre, Date fechaa, String ubicacion,
            int plazas, String cifProveedor) {

        Session session = Conexion.getSession();
        Transaction transaction = session.beginTransaction();

        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String fecha = sdf.format(fechaa);

            Actividad actividad = new Actividad(nombre, fecha, ubicacion, plazas);

            Proveedor proveedor
                    = session.createQuery("FROM Proveedor WHERE cif =:cifProveedor", Proveedor.class)
                            .setParameter("cifProveedor", cifProveedor).uniqueResult();
            actividad.setIdProveedor(proveedor);
            session.persist(actividad);

            System.out.println("Actividad añadido con ID:" + actividad.getId());

            transaction.commit();
        } catch (Exception e) {
            System.out.println("ERROR:" + e.getMessage());
            transaction.rollback();
        } finally {
            session.close();
        }
    }

    public static void borrarActividad(int id) {
        Session session = Conexion.getSession();
        Transaction transaction = session.beginTransaction();

        try {
            Actividad actividad = session.get(Actividad.class, id);
            if (actividad == null) {
                System.out.println("Actividad no encontrada.");
                throw new Exception("Actividad no encontrada.");
            }

            session.remove(actividad);

            transaction.commit();
            System.out.println("Actividad y sus compras asociadas eliminadas correctamente.");
        } catch (Exception e) {
            transaction.rollback();
            System.err.println("Error al intentar borrar la actividad: " + e.getMessage());
        } finally {
            // Cerrar la sesión
            session.close();
        }
    }

    public static void listarDetallesActividad(int id) {
        try (Session session = Conexion.getSession()) {
            Actividad actividad = session.get(Actividad.class, id);
            System.out.println("Nombre: " + actividad.getNombre());
            System.out.println("Fecha: " + actividad.getFecha());
            System.out.println("Ubicacion: " + actividad.getUbicacion());
            System.out.println("Plazas disponibles: " + actividad.getPlazasDisponibles());
            System.out.println("---------------------------");
            System.out.println("Id proveedor: " + actividad.getIdProveedor().getId());
            System.out.println("Nombre: " + actividad.getIdProveedor().getNombre());

            for (Compra compra : actividad.getCompraList()) {
                System.out.println("---------------------------");
                System.out.println("ID cliente: " + compra.getIdCliente().getId());
                System.out.println("Nombre: " + compra.getIdCliente().getNombre());
                System.out.println("Email: " + compra.getIdCliente().getEmail());
                System.out.println("Fecha compra: " + compra.getFechaCompra());

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static List<Actividad> listarActividadesFuturas() {
        try (Session session = Conexion.getSession()) {
            String fechaActual = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            List<Actividad> actividads = session.createQuery("FROM Actividad WHERE fecha >:fechaActual ", Actividad.class)
                    .setParameter("fechaActual", fechaActual).getResultList();
            System.out.println("WHERE :fechaActual BETWEEN fechaInicio an");
            return actividads;
        } catch (Exception e) {
            return null;
        }
    }

}
