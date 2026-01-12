package logica;

import java.util.ArrayList;
import java.util.List;

public class PlanEntrenamiento {
    private String nombre;
    private List<Dia> dias; // lista de días con título y detalle

    public static class Dia {
        private String titulo;
        private String detalle;
        private String categoria; // p.ej. "Musculacion", "Cardio", "Flexibilidad"

        public Dia(String titulo, String detalle, String categoria) {
            this.titulo = titulo;
            this.detalle = detalle;
            this.categoria = categoria;
        }

        public String getTitulo() { return titulo; }
        public String getDetalle() { return detalle; }
        public String getCategoria() { return categoria; }

        @Override
        public String toString() {
            return titulo;
        }
    }

    public PlanEntrenamiento(String nombre) {
        this.nombre = nombre;
        this.dias = new ArrayList<>();
    }

    public void agregarDia(String titulo, String detalle, String categoria) {
        dias.add(new Dia(titulo, detalle, categoria));
    }

    public String getNombre() { return nombre; }
    public List<Dia> getDias() { return dias; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Plan: ").append(nombre).append("\n");
        for (int i = 0; i < dias.size(); i++) {
            sb.append("D").append(i+1).append(": ").append(dias.get(i).getTitulo()).append("\n");
        }
        return sb.toString();
    }
}
