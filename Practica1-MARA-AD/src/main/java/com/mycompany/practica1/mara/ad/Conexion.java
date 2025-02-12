/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.mara.ad;


import java.io.File;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author DAM1
 */
public class Conexion {

    private static Configuration configuration = null;

    private static SessionFactory sessionFactory = null;

    private static Session session = null;

    private static void init() {
        if (configuration == null) {
            configuration = new Configuration().configure(new File("hibernate.cfg.xml"));

            configuration.addAnnotatedClass(Actividad.class);
            configuration.addAnnotatedClass(Cliente.class);
            configuration.addAnnotatedClass(Proveedor.class);
            configuration.addAnnotatedClass(Compra.class);

            sessionFactory = configuration.buildSessionFactory();
        }

    }
    public static Transaction startTransaction(){
        return getSession().beginTransaction();
    }

    public static Session getSession() {
        if (session == null || !session.isOpen()) {
            init();
            session = sessionFactory.openSession();

        }
        return session;
    }

    public static void close() {
        session.close();
        sessionFactory.close();
    }
}
