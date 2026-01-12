package app;

import modelo.datos.*;
import logica.GestorUsuarios;
import logica.GeneradorRutinas;
import logica.motores.CalculadoraSalud;
import logica.motores.CalculadoraProyecciones;
import logica.motores.ValidadorSalud;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {
    private static GestorUsuarios gestor = new GestorUsuarios();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Si se pasa --console forzamos modo consola, si no, iniciamos GUI
        if (args != null && args.length > 0 && "--console".equalsIgnoreCase(args[0])) {
            runConsole();
        } else {
            // arrancar GUI por defecto
            MainGUI.main(args);
        }
    }

    private static void runConsole() {
        boolean running = true;
        while (running) {
            System.out.println("=== POOwerFit ===");
            System.out.println("1. Registrar usuario");
            System.out.println("2. Iniciar sesión");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");

            String opcion = scanner.nextLine();
            switch (opcion) {
                case "1":
                    registrarUsuarioFlow();
                    break;
                case "2":
                    iniciarSesionFlow();
                    break;
                case "3":
                    running = false;
                    break;
                default:
                    System.out.println("Opción inválida, intente de nuevo.");
            }
        }
        System.out.println("Saliendo...");
    }

    private static void registrarUsuarioFlow() {
        try {
            System.out.println("--- Registro de usuario ---");
            System.out.print("Nombre completo: ");
            String nombre = scanner.nextLine();
            System.out.print("Cédula: ");
            String cedula = scanner.nextLine();
            System.out.print("Teléfono: ");
            String telefono = scanner.nextLine();
            System.out.print("Dirección: ");
            String direccion = scanner.nextLine();
            System.out.print("Fecha de nacimiento (YYYY-MM-DD): ");
            String fechaStr = scanner.nextLine();
            LocalDate fecha = LocalDate.parse(fechaStr);

            DatosPersonales dp = new DatosPersonales(nombre, cedula, telefono, direccion, fecha);

            System.out.println("--- Credenciales ---");
            System.out.print("Usuario: ");
            String user = scanner.nextLine();
            System.out.print("Password: ");
            String pass = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();
            Credenciales cred = new Credenciales(user, pass, email);

            System.out.println("--- Perfil físico ---");
            System.out.print("Peso (kg): ");
            double peso = Double.parseDouble(scanner.nextLine());
            System.out.print("Altura (cm): ");
            double altura = Double.parseDouble(scanner.nextLine());
            System.out.print("Edad: ");
            int edad = Integer.parseInt(scanner.nextLine());
            System.out.print("Sexo (MASCULINO/FEMENINO): ");
            Sexo sexo = Sexo.valueOf(scanner.nextLine().toUpperCase());
            System.out.print("Cintura (cm): ");
            double cintura = Double.parseDouble(scanner.nextLine());
            System.out.print("Cuello (cm): ");
            double cuello = Double.parseDouble(scanner.nextLine());
            Double cadera = null;
            if (sexo == Sexo.FEMENINO) {
                System.out.print("Cadera (cm): ");
                cadera = Double.parseDouble(scanner.nextLine());
            }
            System.out.print("Factor de actividad (ej. 1.2): ");
            double factor = Double.parseDouble(scanner.nextLine());

            PerfilFisico pf = new PerfilFisico(peso, altura, edad, sexo, cintura, cuello, cadera, factor);

            Usuario u = new Usuario(dp, cred, pf);

            boolean ok = gestor.registrarUsuario(u);
            if (ok) System.out.println("Usuario registrado correctamente.");
            else System.out.println("Error al registrar: cédula o email ya existente o datos inválidos.");

        } catch (DateTimeParseException dtpe) {
            System.out.println("Fecha inválida. Registro cancelado.");
        } catch (IllegalArgumentException iae) {
            System.out.println("Entrada inválida. Registro cancelado.");
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }
    }

    private static void iniciarSesionFlow() {
        System.out.println("--- Iniciar sesión ---");
        System.out.print("Usuario: ");
        String user = scanner.nextLine();
        System.out.print("Password: ");
        String pass = scanner.nextLine();

        Usuario encontrado = null;
        for (Usuario u : gestor.getUsuariosRegistrados()) {
            if (u.getCredenciales() != null && u.getCredenciales().validar(user, pass)) {
                encontrado = u;
                break;
            }
        }

        if (encontrado == null) {
            System.out.println("Credenciales incorrectas.");
            return;
        }

        System.out.println("Bienvenido, " + encontrado.getDatosPersonales().getNombreCompleto());
        mostrarMenuUsuario(encontrado);
    }

    private static void mostrarMenuUsuario(Usuario u) {
        boolean back = false;
        while (!back) {
            System.out.println("--- Menú Usuario ---");
            System.out.println("1. Ver perfil");
            System.out.println("2. Ver proyecciones (3/6/9 meses)");
            System.out.println("3. Ver diagnósticos de seguridad");
            System.out.println("4. Generar rutina básica (esqueleto)");
            System.out.println("5. Cerrar sesión");
            System.out.print("Seleccione: ");

            String op = scanner.nextLine();
            switch (op) {
                case "1":
                    mostrarPerfil(u);
                    break;
                case "2":
                    mostrarProyecciones(u);
                    break;
                case "3":
                    mostrarDiagnosticoSeguridad(u);
                    break;
                case "4":
                    generarRutinaEsqueleto(u);
                    break;
                case "5":
                    back = true;
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    private static void mostrarPerfil(Usuario u) {
        DatosPersonales dp = u.getDatosPersonales();
        PerfilFisico pf = u.getPerfilFisico();
        System.out.println("--- Perfil ---");
        System.out.println("Nombre: " + dp.getNombreCompleto());
        System.out.println("Cédula: " + dp.getCedula());
        System.out.println("Email: " + u.getCredenciales().getEmail());
        System.out.println("Edad: " + pf.getEdad());
        System.out.println("Peso: " + pf.getPesoKg() + " kg");
        System.out.println("Altura: " + pf.getAlturaCm() + " cm");
        System.out.println("Sexo: " + pf.getSexo());
        System.out.println("Factor actividad: " + pf.getFactorActividad());
    }

    private static void mostrarProyecciones(Usuario u) {
        PerfilFisico pf = u.getPerfilFisico();
        System.out.println("--- Proyecciones ---");
        double gasto = CalculadoraProyecciones.calcularGastoCaloricoDiario(pf);
        System.out.printf("Gasto calórico estimado: %.2f kcal/día\n", gasto);

        double p3 = CalculadoraProyecciones.proyectarPeso(pf, "BAJAR", 3);
        double p6 = CalculadoraProyecciones.proyectarPeso(pf, "BAJAR", 6);
        double p9 = CalculadoraProyecciones.proyectarPeso(pf, "BAJAR", 9);

        System.out.printf("Peso proyectado a 3 meses: %.2f kg\n", p3);
        System.out.printf("Peso proyectado a 6 meses: %.2f kg\n", p6);
        System.out.printf("Peso proyectado a 9 meses: %.2f kg\n", p9);
    }

    private static void mostrarDiagnosticoSeguridad(Usuario u) {
        PerfilFisico pf = u.getPerfilFisico();
        String diag = ValidadorSalud.obtenerDiagnosticoSeguridad(pf);
        System.out.println("--- Diagnóstico de seguridad ---");
        System.out.println(diag);
    }

    private static void generarRutinaEsqueleto(Usuario u) {
        PerfilFisico pf = u.getPerfilFisico();
        String somatotipo = CalculadoraSalud.determinarSomatotipo(pf);
        GeneradorRutinas gen = new GeneradorRutinas();
        Object plan = gen.generarRutinaBasica(somatotipo, "MANTENER");
        System.out.println("Rutina generada (esqueleto): " + (plan == null ? "Pendiente implementación" : plan.toString()));
    }
}
