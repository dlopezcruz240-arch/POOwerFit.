package logica;

import java.util.Random;

public class GeneradorRutinas {
    private final Random rnd = new Random();

    public GeneradorRutinas() {
        // ...existing code...
    }

    // Genera un plan básico (sólo para demo) según somatotipo y objetivo
    public PlanEntrenamiento generarRutinaBasica(String somatotipo, String objetivo) {
        PlanEntrenamiento plan = new PlanEntrenamiento("Plan " + somatotipo + " - " + objetivo);

        if (somatotipo == null) somatotipo = "GENERIC";
        if (objetivo == null) objetivo = "MANTENER";

        // Cada día será una descripción más detallada
        if (somatotipo.equalsIgnoreCase("ECTOMORFO")) {
            plan.agregarDia("Full body fuerza", detalleFuerza("Full body fuerza", 3, 6), "Musculacion");
            plan.agregarDia("Cardio intervalos cortos", detalleCardio("Cardio intervalos cortos", 20), "Cardio");
            plan.agregarDia("Movilidad y recuperación", detalleMovilidad("Movilidad y recuperación", 30), "Movilidad");
        } else if (somatotipo.equalsIgnoreCase("MESOMORFO")) {
            plan.agregarDia("Torso + Core", detalleFuerza("Torso + Core", 4, 8), "Musculacion");
            plan.agregarDia("Cardio moderado + sprints", detalleCardio("Cardio moderado + sprints", 30), "Cardio");
            plan.agregarDia("Flexibilidad específica", detalleFlexibilidad("Flexibilidad específica", 25), "Flexibilidad");
        } else {
            // ENDOMORFO
            plan.agregarDia("Cardio continuo moderado", detalleCardio("Cardio continuo moderado", 35), "Cardio");
            plan.agregarDia("Circuito de bajo impacto", detalleCircuito("Circuito de bajo impacto", 3, 12, 45), "Circuito");
            plan.agregarDia("Movilidad y fortalecimiento estabilizador", detalleMovilidad("Movilidad y fortalecimiento estabilizador", 30), "Movilidad");
        }

        // Introducir ligera variación extra aleatoria: cambiar la secuencia o añadir microvariantes
        if (rnd.nextBoolean()) {
            plan.agregarDia("Consejo rápido", detalleConsejoExtra(), "Consejo");
        }

        return plan;
    }

    private String detalleFuerza(String titulo, int series, int reps) {
        // En lugar de notación tempo, recomiendo un rango de descanso claro: 60-180s (1-3 min) según objetivo
        String descansoRecomendado = "60-180 s (1-3 min, ajustar según objetivo: fuerza->120-180s, hipertrofia->60-90s)";

        // Calentamiento + lista de ejercicios (nombres y explicación breve)
        StringBuilder sb = new StringBuilder();
        sb.append(titulo).append("\n- Tipo: Musculación\n");
        sb.append(String.format("- Series x Reps objetivo (ej.): %dx%d\n", series, reps));
        sb.append(String.format("- Descanso recomendado entre series: %s\n", descansoRecomendado));
        sb.append("- Calentamiento: 8-10 min (movilidad articular y activación específica 2-3 ejercicios)\n");
        sb.append("- Guía técnica: mantener espalda neutra, control en la fase excéntrica; evitar bloqueos articulares.\n");
        sb.append("- Señales de alarma: dolor agudo, mareo, ardor en articulación -> parar y consultar.\n\n");

        sb.append("Ejercicios ejemplo:\n");
        String[][] ejercicios = seleccionarEjerciciosPorTitulo(titulo);
        for (String[] e : ejercicios) {
            String nombre = e[0];
            String exc = e[1];
            sb.append(String.format("- %s: %s (Series: %d, Reps: %d, Descanso: %s)\n", nombre, exc, series, reps, descansoRecomendado));
        }

        sb.append("\nProgresión: aumentar carga 2.5-5% cuando completes todas las series con buena técnica.\n");
        return sb.toString();
    }

    private String[][] seleccionarEjerciciosPorTitulo(String titulo) {
        // Retorna una lista de pares {nombre, explicación corta}
        if (titulo.toLowerCase().contains("torso")) {
            return new String[][]{
                    {"Press banca", "Empuje horizontal para pectoral y tríceps"},
                    {"Remo con barra", "Tirar con patrón de espalda para dorsal y bíceps"},
                    {"Plancha lateral", "Trabajo de core y estabilidad"}
            };
        } else if (titulo.toLowerCase().contains("full body") || titulo.toLowerCase().contains("full")) {
            return new String[][]{
                    {"Sentadilla", "Patrón de sentadilla para cuádriceps y glúteo"},
                    {"Peso muerto rumano", "Hip thrust isquiotibiales y cadena posterior"},
                    {"Press militar", "Empuje vertical para hombros"}
            };
        } else if (titulo.toLowerCase().contains("circuito")) {
            return new String[][]{
                    {"Burpees modificados", "Cardio + fuerza de cuerpo completo"},
                    {"Kettlebell swings", "Explosividad de cadena posterior"},
                    {"Step-ups", "Trabajo unilateral y estabilidad"}
            };
        } else {
            // por defecto ejercicios de torso/core
            return new String[][]{
                    {"Dominadas asistidas", "Tirar vertical para espalda"},
                    {"Fondos asistidos", "Empuje vertical para pectoral/tríceps"},
                    {"Crunches", "Trabajo abdominal"}
            };
        }
    }

    private String detalleCardio(String titulo, int minutos) {
        int intensidad = 50 + rnd.nextInt(30); // 50-80%
        StringBuilder sb = new StringBuilder();
        sb.append(titulo).append("\n- Tipo: Cardio\n");
        sb.append(String.format("- Duración: %d min\n", minutos));
        sb.append(String.format("- Intensidad objetivo: %d%% FC aproximada\n", intensidad));
        sb.append("- Calentamiento: 5-8 min ritmo suave\n");
        sb.append("- Ejercicio: combinar ritmo constante con 3-6 sprints de 20s o intervalos 30/30 según capacidad\n");
        sb.append("- Recuperación entre sprints: caminar 1-3 min\n");
        sb.append("- Enfriamiento: 5 min trote suave y estiramientos\n");
        sb.append("- Señales de alarma: mareo o palpitaciones anormales -> detener.\n");
        return sb.toString();
    }

    private String detalleCircuito(String titulo, int rondas, int reps, int tiempoMin) {
        return String.format("%s\n- Tipo: Circuito funcional\n- Rondas: %d\n- Reps por estación: %d (o 40-60s por estación)\n- Tiempo estimado: %d min\n- Descanso entre rondas: 90-120 seg\n- Técnica: priorizar rango de movimiento correcto sobre velocidad.\n- Recomendación: mantener respiración controlada.", titulo, rondas, reps, tiempoMin);
    }

    private String detalleFlexibilidad(String titulo, int minutos) {
        return String.format("%s\n- Tipo: Flexibilidad y movilidad\n- Duración: %d min\n- Contenido: estiramientos dinámicos + estáticos 60/40\n- Recomendación: mantener cada estiramiento 20-30s, respirar profundamente\n- Atención: evitar rebotar en el estiramiento si hay lesión previa.", titulo, minutos);
    }

    private String detalleMovilidad(String titulo, int minutos) {
        return String.format("%s\n- Tipo: Movilidad\n- Duración: %d min\n- Objetivo: mejorar rango articular y estabilidad\n- Ejemplos: movilidad de cadera, hombro, patrones de respiración\n- Recomendación: realizar movimientos controlados y sin dolor.", titulo, minutos);
    }

    private String detalleConsejoExtra() {
        String[] extras = new String[]{
                "Consejo: enfócate en calidad de movimiento. Añadir 5-10 min de activación antes de fuerza.",
                "Consejo: prioriza sueño y nutrición para recuperación; aumenta proteína si buscas ganar masa.",
                "Consejo: hidrátate bien y evita entrenamiento intenso si tienes fiebre o síntomas agudos."};
        return extras[rnd.nextInt(extras.length)];
    }
}
