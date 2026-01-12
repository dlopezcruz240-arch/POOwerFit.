package modelo.datos;

public class Usuario {
    private DatosPersonales datosPersonales;
    private Credenciales credenciales;
    private PerfilFisico perfilFisico;

    public Usuario(DatosPersonales datosPersonales, Credenciales credenciales, PerfilFisico perfilFisico) {
        this.datosPersonales = datosPersonales;
        this.credenciales = credenciales;
        this.perfilFisico = perfilFisico;
    }

    public DatosPersonales getDatosPersonales() { return datosPersonales; }
    public void setDatosPersonales(DatosPersonales datosPersonales) { this.datosPersonales = datosPersonales; }

    public Credenciales getCredenciales() { return credenciales; }
    public void setCredenciales(Credenciales credenciales) { this.credenciales = credenciales; }

    public PerfilFisico getPerfilFisico() { return perfilFisico; }
    public void setPerfilFisico(PerfilFisico perfilFisico) { this.perfilFisico = perfilFisico; }
}
