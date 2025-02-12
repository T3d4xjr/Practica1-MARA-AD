/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.mara.ad;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author tedax
 */
public class ProveedorDAO {

    public Proveedor listarDetallesProveedor(int id) {
        Session session = Conexion.getSession();
        Transaction transaction = session.beginTransaction();

        try {
            Proveedor proveedor = session.get(Proveedor.class, id);
            System.out.println("Id proveedor: " + proveedor.getId());
            System.out.println("Nombre: " + proveedor.getNombre());
            System.out.println("Email: " + proveedor.getEmail());
            System.out.println("Cif: " + proveedor.getCif());
            System.out.println("---------------------------");

            List<Actividad> actividads = proveedor.getActividadList();

            for (Actividad actividad : actividads) {
                System.out.println("Id actividad: " + actividad.getId());
                System.out.println("Nombre: " + actividad.getNombre());
                System.out.println("Fecha: " + actividad.getFecha());
                System.out.println("Ubicacion: " + actividad.getUbicacion());
                System.out.println("Plazas disponibles: " + actividad.getPlazasDisponibles());
            }

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            session.close();
        }
        return null;
    }

    public boolean borrarProveedor(int id) {
        Session session = Conexion.getSession();
        Transaction transaction = session.beginTransaction();

        try {
            Proveedor proveedor = session.get(Proveedor.class, id);
            if (proveedor == null) {
                System.out.println("Proveedor no encontrado.");
                return false;
            }

            List<Actividad> actividades = proveedor.getActividadList();

            String fechaActual = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            for (Actividad actividad : actividades) {
                if (actividad.getFecha().compareTo(fechaActual) > 0) {
                    System.out.println("No se puede borrar el proveedor porque tiene actividades pendientes.");
                    transaction.rollback();
                    return false;
                }
            }
            session.remove(proveedor);
            
            for (Actividad actividad : actividades) {
                session.remove(actividad);
            }

            transaction.commit();
            System.out.println("Proveedor y sus actividades asociadas eliminados correctamente.");
            return true;
        } catch (Exception e) {
            // Manejar errores y revertir la transacción
            transaction.rollback();
            System.err.println("Error al intentar borrar el proveedor: " + e.getMessage());
            return false;
        } finally {
            // Cerrar la sesión
            session.close();
        }
    }

    public boolean modificarProveedor(int id, String nombre, String email, String cif) {
        Session session = Conexion.getSession();
        Transaction transaction = session.beginTransaction();

        try {

            Proveedor proveedor = new Proveedor(nombre, email, cif);
            proveedor.setId(id);
            session.merge(proveedor);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            session.close();
        }

        return true;

    }

    public int anadirProveedor(String nombre, String email, String cif) {
        Session session = Conexion.getSession();
        Transaction transaction = session.beginTransaction();

        try {

            Proveedor proveedor = new Proveedor(nombre, email, cif);
            session.persist(proveedor);
            System.out.println("Proveedor añadido con ID:" + proveedor.getId());

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            session.close();
        }

        return 1;

    }

}
