/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.mara.ad;

/**
 *
 * @author tedax
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Menu {

    private static final ProveedorDAO proveedorDAO = new ProveedorDAO();
    private static final ActividadDAO actividadDAO = new ActividadDAO();
    private static final ClienteDAO clienteDAO = new ClienteDAO();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n--- Menú Gestión de Actividades Culturales ---");
            System.out.println("1. Añadir proveedor");
            System.out.println("2. Modificar proveedor");
            System.out.println("3. Borrar proveedor");
            System.out.println("4. Añadir actividad");
            System.out.println("5. Borrar actividad");
            System.out.println("6. Añadir cliente");
            System.out.println("7. Modificar cliente");
            System.out.println("8. Borrar cliente");
            System.out.println("9. Comprar actividad");
            System.out.println("10. Cancelar compra");
            System.out.println("11. Listar todos los clientes");
            System.out.println("12. Listar todas las actividades futuras");
            System.out.println("13. Listar detalles de un cliente");
            System.out.println("14. Listar detalles de un proveedor");
            System.out.println("15. Listar detalles de una actividad");
            System.out.println("16. Salir");
            System.out.print("Seleccione una opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            try {
                switch (opcion) {
                    case 1:
                        anadirProveedor();
                        break;
                    case 2:
                        modificarProveedor();
                        break;
                    case 3:
                        borrarProveedor();
                        break;
                    case 4:
                        anadirActividad();
                        break;
                    case 5:
                        borrarActividad();
                        break;
                    case 6:
                        anadirCliente();
                        break;
                    case 7:
                        modificarCliente();
                        break;
                    case 8:
                        borrarCliente();
                        break;
                    case 9:
                        comprarActividad();
                        break;
                    case 10:
                        cancelarCompra();
                        break;
                    case 11:
                        listarClientes();
                        break;
                    case 12:
                        listarActividadesFuturas();
                        break;
                    case 13:
                        listarDetallesCliente();
                        break;
                    case 14:
                        listarDetallesProveedor();
                        break;
                    case 15:
                        listarDetallesActividad();
                        break;
                    case 16:
                        salir = true;
                        break;
                    default:
                        System.out.println("Opción no válida, intente de nuevo.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void anadirProveedor() {
        System.out.println("Nombre del proveedor: ");
        String nombre = scanner.nextLine();
        System.out.println("Email del proveedor: ");
        String email = scanner.nextLine();
        System.out.println("CIF del proveedor: ");
        String cif = scanner.nextLine();

        int id = proveedorDAO.anadirProveedor(nombre, email, cif);
        if (id != -1) {
            System.out.println("Proveedor añadido con exito.");
        } else {
            System.out.println("Error al añadir el proveedor.");
        }
    }

    private static void modificarProveedor() {
        System.out.println("ID del proveedor a modificar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer
        System.out.println("Nuevo nombre: ");
        String nombre = scanner.nextLine();
        System.out.println("Nuevo email: ");
        String email = scanner.nextLine();
        System.out.println("Nuevo CIF: ");
        String cif = scanner.nextLine();

        if (proveedorDAO.modificarProveedor(id, nombre, email, cif)) {
            System.out.println("Proveedor modificado exitosamente.");
        } else {
            System.out.println("Error al modificar el proveedor.");
        }
    }

    private static void borrarProveedor() {
        System.out.println("ID del proveedor a borrar: ");
        int id = scanner.nextInt();

        if (proveedorDAO.borrarProveedor(id)) {
            System.out.println("Proveedor borrado exitosamente.");
        } else {
            System.out.println("Error al borrar el proveedor. Puede tener actividades futuras asociadas.");
        }
    }

    private static void anadirActividad() {
        System.out.println("Nombre de la actividad: ");
        String nombre = scanner.nextLine();
        System.out.println("Fecha de la actividad (YYYY-MM-DD): ");
        String fechaStr = scanner.nextLine();
        System.out.println("Ubicación: ");
        String ubicacion = scanner.nextLine();
        System.out.println("Plazas disponibles: ");
        int plazas = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer
        System.out.println("CIF del proveedor: ");
        int cifProveedor = scanner.nextInt();
        scanner.nextLine();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date fecha = format.parse(fechaStr);
            int id = actividadDAO.anadirActividad(nombre, fecha, ubicacion, plazas, cifProveedor);
            if (id != -1) {
                System.out.println("Actividad añadida con exito.");
            } else {
                System.out.println("Error al añadir la actividad.");
            }
        } catch (ParseException e) {
            System.out.println("Formato de fecha incorrecto.");
        }
    }

    private static void borrarActividad() {
        System.out.println("ID de la actividad a borrar: ");
        int id = scanner.nextInt();

        if (actividadDAO.borrarActividad(id)) {
            System.out.println("Actividad borrada exitosamente.");
        } else {
            System.out.println("Error al borrar la actividad.");
        }
    }

    private static void anadirCliente() {
        System.out.println("Nombre del cliente: ");
        String nombre = scanner.nextLine();
        System.out.println("Email del cliente: ");
        String email = scanner.nextLine();

        int id = clienteDAO.anadirCliente(nombre, email);
        if (id != -1) {
            System.out.println("Cliente añadido con exito: ");
        } else {
            System.out.println("Error al añadir el cliente.");
        }
    }

    private static void modificarCliente() {
        System.out.println("ID del cliente a modificar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer
        System.out.println("Nuevo nombre: ");
        String nombre = scanner.nextLine();
        System.out.println("Nuevo email: ");
        String email = scanner.nextLine();

        if (clienteDAO.modificarCliente(id, nombre, email)) {
            System.out.println("Cliente modificado exitosamente.");
        } else {
            System.out.println("Error al modificar el cliente.");
        }
    }

    private static void borrarCliente() {
        System.out.println("ID del cliente a borrar: ");
        int id = scanner.nextInt();

        if (clienteDAO.borrarCliente(id)) {
            System.out.println("Cliente borrado exitosamente.");
        } else {
            System.out.println("Error al borrar el cliente. Puede tener compras de actividades futuras.");
        }
    }

    private static void comprarActividad() {
        System.out.println("ID de la actividad: ");
        int idActividad = scanner.nextInt();
        System.out.println("ID del cliente: ");
        int idCliente = scanner.nextInt();

        if (actividadDAO.comprarActividad(idActividad, idCliente)) {
            System.out.println("Compra realizada con éxito.");
        } else {
            System.out.println("Error al realizar la compra. Puede que no haya plazas disponibles o que la actividad no sea futura.");
        }
    }

    private static void cancelarCompra() {
        System.out.println("ID de la actividad: ");
        int idActividad = scanner.nextInt();
        System.out.println("ID del cliente: ");
        int idCliente = scanner.nextInt();

        if (actividadDAO.cancelarCompra(idActividad, idCliente)) {
            System.out.println("Compra cancelada exitosamente.");
        } else {
            System.out.println("Error al cancelar la compra. Puede que la actividad ya se haya realizado.");
        }
    }

    private static void listarClientes() {
        clienteDAO.listarClientes();
    }

    private static void listarActividadesFuturas() {
        actividadDAO.listarActividadesFuturas();
    }

    private static void listarDetallesCliente() {
        System.out.println("ID del cliente: ");
        int id = scanner.nextInt();

        Cliente cliente = clienteDAO.listarDetallesCliente(id);

    }

    private static void listarDetallesProveedor() {
        System.out.println("ID del proveedor: ");
        int id = scanner.nextInt();

        Proveedor proveedor = proveedorDAO.listarDetallesProveedor(id);
    }

    private static void listarDetallesActividad() {
        System.out.println("ID de la actividad: ");
        int id = scanner.nextInt();

        Actividad actividad = actividadDAO.listarDetallesActividad(id);
    }
}

