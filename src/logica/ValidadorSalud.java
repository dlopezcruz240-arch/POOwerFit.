package logica;

import modelo.datos.PerfilFisico;
import logica.motores.CalculadoraSalud;

public class ValidadorSalud {

    // Reutiliza reglas simples de diagnóstico de la versión en motores
    public static String obtenerDiagnosticoSeguridad(PerfilFisico p) {
        double imc = CalculadoraSalud.calcularIMC(p);

        if (imc >= 35) {
            return "ALERTA: Obesidad Grado II o superior. Solo se permiten rutinas de bajo impacto y movilidad.";
        } else if (p.getEdad() > 65) {
            return "PRECAUCIÓN: Usuario de tercera edad. Restringir cargas máximas en musculación.";
        } else if (imc < 16) {
            return "ALERTA: Delgadez severa. Se recomienda aumentar ingesta calórica antes de entrenamiento intenso.";
        }

        return "ESTADO OPTIMO: El usuario puede realizar cualquier tipo de rutina sugerida.";
    }

    public static boolean esAptoParaCargasPesadas(PerfilFisico p) {
        double imc = CalculadoraSalud.calcularIMC(p);
        return imc < 30 && p.getEdad() >= 15 && p.getEdad() <= 60;
    }

    /**
     * Verifica si una proyección de peso es segura según la regla: no más del 1% del peso corporal por semana.
     * @param pesoActual peso inicial en kg (debe ser > 0)
     * @param pesoFuturo peso proyectado en kg
     * @param semanas número de semanas de la proyección (debe ser > 0)
     * @return true si es seguro, false si excede 1% por semana
     * @throws IllegalArgumentException si parámetros inválidos
     */
    public static boolean esProyeccionSegura(double pesoActual, double pesoFuturo, int semanas) {
        if (pesoActual <= 0) throw new IllegalArgumentException("pesoActual debe ser mayor que 0");
        if (semanas <= 0) throw new IllegalArgumentException("semanas debe ser mayor que 0");

        double perdidaTotal = pesoActual - pesoFuturo; // positiva si se pierde peso
        double perdidaPorSemanaKg = perdidaTotal / semanas;
        double porcentajePorSemana = (perdidaPorSemanaKg / pesoActual) * 100.0;

        return porcentajePorSemana <= 1.0;
    }

    public static void validarProyeccionSeguraOrThrow(double pesoActual, double pesoFuturo, int semanas) {
        if (!esProyeccionSegura(pesoActual, pesoFuturo, semanas)) {
            throw new IllegalArgumentException("Proyección no segura: la pérdida de peso proyectada supera el 1% por semana.");
        }
    }
}
