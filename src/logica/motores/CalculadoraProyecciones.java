package logica.motores;

import modelo.datos.PerfilFisico;
import modelo.datos.Sexo;

public class CalculadoraProyecciones {

    public static double calcularGastoCaloricoDiario(PerfilFisico p) {
        double tmb;
        if (p.getSexo() == Sexo.MASCULINO) {
            tmb = (10 * p.getPesoKg()) + (6.25 * p.getAlturaCm()) - (5 * p.getEdad()) + 5;
        } else {
            tmb = (10 * p.getPesoKg()) + (6.25 * p.getAlturaCm()) - (5 * p.getEdad()) - 161;
        }
        return tmb * p.getFactorActividad();
    }

    public static double proyectarPeso(PerfilFisico p, String objetivo, int meses) {
        double ajusteCalorico = objetivo.equalsIgnoreCase("BAJAR") ? -500 : 500;
        double cambioMensualKg = (ajusteCalorico * 30) / 7700;
        return p.getPesoKg() + (cambioMensualKg * meses);
    }
}
