package modelo.datos;

public class Credenciales {
    private String usuario;
    private String password;
    private String email;

    public Credenciales(String usuario, String password, String email) {
        this.usuario = usuario;
        this.password = password;
        this.email = email;
    }

    public boolean validar(String inputUser, String inputPass) {
        if (inputUser == null || inputPass == null) return false;
        return inputUser.equals(usuario) && inputPass.equals(password);
    }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
