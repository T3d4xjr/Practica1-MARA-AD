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
import java.util.logging.Level;
import java.util.logging.Logger;

public class Menu {
    
    public static Scanner scanner = new Scanner(System.in);
    public static ProveedorDAO proveedorDAO = new ProveedorDAO();
    public static ActividadDAO actividadDAO = new ActividadDAO();
    public static ClienteDAO clienteDAO = new ClienteDAO();
    public static CompraDAO compraDAO = new CompraDAO();
    
    public static void main(String[] args) {
        boolean menu = true;

        while (menu) {
            System.out.println("1.Añadir proveedor");
            System.out.println("2.Modificar proveedor");
            System.out.println("3.Borrar proveedor");
            System.out.println("4.Añadir actividad");
            System.out.println("5.Borrar actividad");
            System.out.println("6.Añadir un cliente");
            System.out.println("7.Modificar un cliente");
            System.out.println("8.Borrar un cliente");
            System.out.println("9.Comprar actividad");
            System.out.println("10.Cancelar compra");
            System.out.println("11.Listar todos los clientes");
            System.out.println("12.Listar todas las actividades futuras");
            System.out.println("13.Listar detalles de un cliente");
            System.out.println("14.Listar detalles de un proveedor");
            System.out.println("15.Listar detalles de una actividad");
            System.out.println("16.Salir");
            System.out.println("Elige una opcion: ");
            int opcion = scanner.nextInt();
            scanner.nextLine();

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
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida");
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

        ProveedorDAO.anadirProveedor(nombre, email, cif);
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

        ProveedorDAO.modificarProveedor(id, nombre, email, cif);
    }

    private static void borrarProveedor() {
        System.out.println("ID del proveedor a borrar: ");
        int id = scanner.nextInt();

        ProveedorDAO.borrarProveedor(id);
    }

    private static void anadirActividad() {
        try {
            System.out.println("Nombre de la actividad: ");
            String nombre = scanner.nextLine();
            System.out.println("Fecha de la actividad (YYYY-MM-DD): ");
            String fecha = scanner.nextLine();
            System.out.println("Ubicación: ");
            String ubicacion = scanner.nextLine();
            System.out.println("Plazas disponibles: ");
            int plazas = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer
            System.out.println("CIF del proveedor: ");
            String cifProveedor = scanner.nextLine();
            
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaa = sdf.parse(fecha);
            ActividadDAO.anadirActividad(nombre, fechaa, ubicacion, plazas, cifProveedor);
        } catch (ParseException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void borrarActividad() {
        System.out.println("ID de la actividad a borrar: ");
        int id = scanner.nextInt();

        ActividadDAO.borrarActividad(id);
    }

    private static void anadirCliente() {
        System.out.println("Nombre del cliente: ");
        String nombre = scanner.nextLine();
        System.out.println("Email del cliente: ");
        String email = scanner.nextLine();
        
        ClienteDAO.anadirCliente(nombre, email);
        
    }

    private static void modificarCliente() {
        System.out.println("ID del cliente a modificar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer
        System.out.println("Nuevo nombre: ");
        String nombre = scanner.nextLine();
        System.out.println("Nuevo email: ");
        String email = scanner.nextLine();

        ClienteDAO.modificarCliente(id, nombre, email);
    }

    private static void borrarCliente() {
        System.out.println("ID del cliente a borrar: ");
        int id = scanner.nextInt();

        ClienteDAO.borrarCliente(id);
    }

    private static void comprarActividad() {
        System.out.println("ID de la actividad: ");
        int idActividad = scanner.nextInt();
        System.out.println("ID del cliente: ");
        int idCliente = scanner.nextInt();

        CompraDAO.comprarActividad(idActividad, idCliente);
    }

    private static void cancelarCompra() {
        System.out.println("ID de la actividad: ");
        int idActividad = scanner.nextInt();
        System.out.println("ID del cliente: ");
        int idCliente = scanner.nextInt();

        CompraDAO.cancelarCompra(idActividad, idCliente);
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

