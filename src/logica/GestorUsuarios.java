package logica;

import modelo.datos.Usuario;

import java.util.ArrayList;
import java.util.List;

public class GestorUsuarios {
    private List<Usuario> usuariosRegistrados;

    public GestorUsuarios() {
        this.usuariosRegistrados = new ArrayList<>();
    }

    public boolean registrarUsuario(Usuario u) {
        if (u == null) return false;

        String cedula = (u.getDatosPersonales() != null) ? u.getDatosPersonales().getCedula() : null;
        String email = (u.getCredenciales() != null) ? u.getCredenciales().getEmail() : null;

        if (cedula == null || cedula.isBlank()) return false;
        if (email == null || email.isBlank()) return false;

        if (existeCedula(cedula) || existeEmail(email)) return false;

        usuariosRegistrados.add(u);
        return true;
    }

    public boolean existeCedula(String cedula) {
        for (Usuario usr : usuariosRegistrados) {
            if (usr.getDatosPersonales() != null &&
                cedula.equals(usr.getDatosPersonales().getCedula())) {
                return true;
            }
        }
        return false;
    }

    public boolean existeEmail(String email) {
        for (Usuario usr : usuariosRegistrados) {
            if (usr.getCredenciales() != null &&
                email.equalsIgnoreCase(usr.getCredenciales().getEmail())) {
                return true;
            }
        }
        return false;
    }

    public List<Usuario> getUsuariosRegistrados() {
        return usuariosRegistrados;
    }
}
