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

    public static void anadirProveedor(String nombre, String email, String cif) {
        Session session = Conexion.getSession();
        Transaction transaction = session.beginTransaction();

        try {

            Proveedor proveedor = new Proveedor(nombre, email, cif);
            session.persist(proveedor);
            System.out.println("Proveedor añadido con ID:" + proveedor.getId());

            transaction.commit();
            System.out.println("PRoveedor modificado con exito");
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            session.close();
        }

    }

    public static void modificarProveedor(int id, String nombre, String email, String cif) {
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

    }

    public static void borrarProveedor(int id) {
        Session session = Conexion.getSession();
        Transaction transaction = session.beginTransaction();

        try {
            Proveedor proveedor = session.get(Proveedor.class, id);
            if (proveedor == null) {
                System.out.println("Proveedor no encontrado.");
                throw new Exception("Proveedor no encontrado.");
            }

            List<Actividad> actividades = proveedor.getActividadList();

            String fechaActual = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            for (Actividad actividad : actividades) {
                if (actividad.getFecha().compareTo(fechaActual) > 0) {
                    System.out.println("No se puede borrar el proveedor porque tiene actividades pendientes.");
                    throw new Exception("No se puede borrar el proveedor porque tiene actividades pendientes.");

                }
            }
            session.remove(proveedor);

            transaction.commit();
            System.out.println("Proveedor y sus actividades asociadas eliminados correctamente.");
        } catch (Exception e) {
            transaction.rollback();
            System.err.println("Error al intentar borrar el proveedor: " + e.getMessage());
        } finally {
            session.close();
        }
    }

    public static void listarDetallesProveedor(int id) {
        try (Session session = Conexion.getSession()) {
            Proveedor proveedor = session.get(Proveedor.class, id);
            System.out.println("Id proveedor: " + proveedor.getId());
            System.out.println("Nombre: " + proveedor.getNombre());
            System.out.println("Email: " + proveedor.getEmail());
            System.out.println("Cif: " + proveedor.getCif());
            System.out.println("---------------------------");

            List<Actividad> actividads = proveedor.getActividadList();

            for (Actividad actividad : actividads) {
                System.out.println("---------------------------");
                System.out.println("Id actividad: " + actividad.getId());
                System.out.println("Nombre: " + actividad.getNombre());
                System.out.println("Fecha: " + actividad.getFecha());
                System.out.println("Ubicacion: " + actividad.getUbicacion());
                System.out.println("Plazas disponibles: " + actividad.getPlazasDisponibles());
            }
        } catch (Exception e) {
        }
    }

}
