package logica.motores;

import modelo.datos.PerfilFisico;

public class ValidadorSalud {

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
}
