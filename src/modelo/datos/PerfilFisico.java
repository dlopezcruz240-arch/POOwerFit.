package modelo.datos;

public class PerfilFisico {
    private double pesoKg;
    private double alturaCm;
    private int edad;
    private Sexo sexo;
    private double cinturaCm;
    private double cuelloCm;
    private Double caderaCm; // obligatorio para mujeres (puede ser null en hombres)
    private double factorActividad;

    public PerfilFisico(double pesoKg, double alturaCm, int edad, Sexo sexo,
                        double cinturaCm, double cuelloCm, Double caderaCm, double factorActividad) {
        this.pesoKg = pesoKg;
        this.alturaCm = alturaCm;
        this.edad = edad;
        this.sexo = sexo;
        this.cinturaCm = cinturaCm;
        this.cuelloCm = cuelloCm;
        this.caderaCm = caderaCm;
        this.factorActividad = factorActividad;
    }

    public double getPesoKg() { return pesoKg; }
    public void setPesoKg(double pesoKg) { this.pesoKg = pesoKg; }

    public double getAlturaCm() { return alturaCm; }
    public void setAlturaCm(double alturaCm) { this.alturaCm = alturaCm; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public Sexo getSexo() { return sexo; }
    public void setSexo(Sexo sexo) { this.sexo = sexo; }

    public double getCinturaCm() { return cinturaCm; }
    public void setCinturaCm(double cinturaCm) { this.cinturaCm = cinturaCm; }

    public double getCuelloCm() { return cuelloCm; }
    public void setCuelloCm(double cuelloCm) { this.cuelloCm = cuelloCm; }

    public Double getCaderaCm() { return caderaCm; }
    public void setCaderaCm(Double caderaCm) { this.caderaCm = caderaCm; }

    public double getFactorActividad() { return factorActividad; }
    public void setFactorActividad(double factorActividad) { this.factorActividad = factorActividad; }
}
