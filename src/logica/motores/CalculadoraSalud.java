package logica.motores;

import modelo.datos.PerfilFisico;
import modelo.datos.Sexo;

public class CalculadoraSalud {

    public static double calcularIMC(PerfilFisico p) {
        double alturaMetros = p.getAlturaCm() / 100;
        return p.getPesoKg() / (alturaMetros * alturaMetros);
    }

    public static double calcularGrasaCorporal(PerfilFisico p) {
        double grasa = 0;
        double altura = p.getAlturaCm();
        double cintura = p.getCinturaCm();
        double cuello = p.getCuelloCm();

        if (p.getSexo() == Sexo.MASCULINO) {
            grasa = 495 / (1.03248 - 0.19077 * Math.log10(cintura - cuello) + 0.15456 * Math.log10(altura)) - 450;
        } else {
            double cadera = (p.getCaderaCm() != null) ? p.getCaderaCm() : 0;
            grasa = 495 / (1.29579 - 0.35004 * Math.log10(cintura + cadera - cuello) + 0.22100 * Math.log10(altura)) - 450;
        }
        return grasa;
    }

    public static String determinarSomatotipo(PerfilFisico p) {
        double imc = calcularIMC(p);
        double grasa = calcularGrasaCorporal(p);

        if (imc < 20 && grasa < 12) {
            return "ECTOMORFO";
        } else if (imc >= 20 && imc <= 26 && grasa <= 18) {
            return "MESOMORFO";
        } else {
            return "ENDOMORFO";
        }
    }
}
