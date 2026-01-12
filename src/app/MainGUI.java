package app;

import modelo.datos.*;
import logica.GestorUsuarios;
import logica.GeneradorRutinas;
import logica.PlanEntrenamiento;
import logica.ValidadorSalud;
import logica.motores.CalculadoraProyecciones;
import logica.motores.CalculadoraSalud;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class MainGUI extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);
    private final GestorUsuarios gestor = new GestorUsuarios();

    public MainGUI() {
        setTitle("POOwerFit — Entrenamiento Inteligente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(820, 600);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(720, 520));

        // Look & Feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        cards.add(createWelcomePanel(), "welcome");
        cards.add(createRegisterPanel(), "register");
        cards.add(createLoginPanel(), "login");

        getContentPane().add(cards);
    }

    // Helper para crear botones con contraste y estilo coherente y efecto hover
    private JButton styledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        // Forzar estilo claro en todos los botones: fondo claro y texto negro
        Color baseLight = new Color(245, 245, 245);
        btn.setBackground(baseLight);
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(true);
        btn.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200,200,200)), BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        // efecto hover: oscurecer ligeramente el fondo claro para dar feedback
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(235,235,235));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(baseLight);
            }
        });
        return btn;
    }

    private JLabel boldLabel(String text, int size) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, size));
        return l;
    }

    private JLabel valueLabel(String text, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.PLAIN, 14));
        l.setForeground(color);
        return l;
    }

    // Chip visual para resaltar valores del perfil
    private JLabel createChip(String text, Color bg) {
        JLabel l = new JLabel(text);
        l.setOpaque(true);
        // usar fondo claro neutral para que el texto siempre sea legible
        Color chipBg = new Color(245,245,245);
        l.setBackground(chipBg);
        // Texto menos intenso que los títulos: gris oscuro
        l.setForeground(new Color(60,60,60));
        l.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(220,220,220)), BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        l.setFont(new Font("SansSerif", Font.PLAIN, 13));
        l.setHorizontalAlignment(SwingConstants.LEFT);
        return l;
    }

    // Panel simple que dibuja un logo conceptual
    private JPanel logoPanel(int size) {
        return new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth();
                int h = getHeight();
                // fondo circular
                int r = Math.min(w, h) - 12;
                int x = (w - r) / 2; int y = (h - r) / 2;
                g2.setColor(new Color(34, 139, 230));
                g2.fillOval(x, y, r, r);
                // elemento P estilizado
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int cx = w/2; int cy = h/2;
                g2.drawLine(cx-10, cy-18, cx-10, cy+18);
                g2.drawArc(cx-18, cy-22, 28, 36, -90, 180);
                g2.dispose();
            }
        };
    }

    private JPanel createWelcomePanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new EmptyBorder(24, 24, 24, 24));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel title = new JLabel("POOwerFit");
        title.setFont(new Font("SansSerif", Font.BOLD, 36));
        title.setForeground(new Color(20, 60, 120));
        top.add(title, BorderLayout.WEST);
        JPanel logoHolder = logoPanel(60);
        logoHolder.setPreferredSize(new Dimension(72,72));
        top.add(logoHolder, BorderLayout.EAST);
        p.add(top, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout());
        center.setBorder(new EmptyBorder(18, 18, 18, 18));

        JTextPane info = new JTextPane();
        info.setEditable(false);
        info.setBackground(center.getBackground());
        info.setFont(new Font("SansSerif", Font.PLAIN, 15));
        info.setText("Entrenamiento inteligente y seguro.\n\n" +
                "Calcula tu somatotipo, genera rutinas básicas y proyecta el progreso a 3/6/9 meses.\n\n" +
                "Pulsa Registrarse para crear una cuenta o Iniciar sesión para ver tu panel.");
        info.setBorder(new EmptyBorder(6,6,6,6));
        center.add(info, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        buttons.setBorder(new EmptyBorder(16,0,0,0));

        // botones principales en negro para alto contraste
        JButton btnRegister = styledButton("Registrarse", Color.BLACK);
        JButton btnLogin = styledButton("Iniciar sesión", Color.BLACK);

        btnRegister.setToolTipText("Crear una nueva cuenta");
        btnLogin.setToolTipText("Acceder con usuario y contraseña");

        btnRegister.addActionListener(e -> cardLayout.show(cards, "register"));
        btnLogin.addActionListener(e -> cardLayout.show(cards, "login"));

        buttons.add(btnRegister);
        buttons.add(Box.createRigidArea(new Dimension(12,0)));
        buttons.add(btnLogin);

        center.add(buttons, BorderLayout.SOUTH);
        p.add(center, BorderLayout.CENTER);

        return p;
    }

    private JPanel createRegisterPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new EmptyBorder(12, 12, 12, 12));

        JLabel header = boldLabel("Registro de usuario", 20);
        header.setBorder(new EmptyBorder(6, 6, 12, 6));
        p.add(header, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        JTextField tfNombre = new JTextField();
        JTextField tfCedula = new JTextField();
        JTextField tfTelefono = new JTextField();
        JTextField tfDireccion = new JTextField();
        JTextField tfFecha = new JTextField("1990-01-01");

        // Asegurar contraste: texto negro sobre fondo blanco en campos de entrada
        JTextField[] regFields = new JTextField[]{tfNombre, tfCedula, tfTelefono, tfDireccion, tfFecha};
        for (JTextField f : regFields) { f.setForeground(Color.BLACK); f.setBackground(Color.WHITE); f.setCaretColor(Color.BLACK); f.setOpaque(true); }

        JTextField tfUsuario = new JTextField();
        JPasswordField pfPass = new JPasswordField();
        JTextField tfEmail = new JTextField();

        tfUsuario.setForeground(Color.BLACK); tfUsuario.setBackground(Color.WHITE); tfUsuario.setCaretColor(Color.BLACK); tfUsuario.setOpaque(true);
        pfPass.setForeground(Color.BLACK); pfPass.setBackground(Color.WHITE); pfPass.setCaretColor(Color.BLACK); pfPass.setOpaque(true);
        tfEmail.setForeground(Color.BLACK); tfEmail.setBackground(Color.WHITE); tfEmail.setCaretColor(Color.BLACK); tfEmail.setOpaque(true);

        JTextField tfPeso = new JTextField("70");
        JTextField tfAltura = new JTextField("170");
        JTextField tfEdad = new JTextField("30");
        JComboBox<String> cbSexo = new JComboBox<>(new String[]{"MASCULINO", "FEMENINO"});
        JTextField tfCintura = new JTextField("80");
        JTextField tfCuello = new JTextField("38");
        JTextField tfCadera = new JTextField("95");
        JTextField tfFactor = new JTextField("1.2");

        // Asegurar contraste en campos numéricos
        JTextField[] numFields = new JTextField[]{tfPeso, tfAltura, tfEdad, tfCintura, tfCuello, tfCadera, tfFactor};
        for (JTextField f : numFields) { f.setForeground(Color.BLACK); f.setBackground(Color.WHITE); f.setCaretColor(Color.BLACK); f.setOpaque(true); }

        c.gridx = 0; c.gridy = row; form.add(new JLabel("Nombre completo:"), c);
        c.gridx = 1; form.add(tfNombre, c); row++;

        c.gridx = 0; c.gridy = row; form.add(new JLabel("Cédula:"), c);
        c.gridx = 1; form.add(tfCedula, c); row++;

        c.gridx = 0; c.gridy = row; form.add(new JLabel("Teléfono:"), c);
        c.gridx = 1; form.add(tfTelefono, c); row++;

        c.gridx = 0; c.gridy = row; form.add(new JLabel("Dirección:"), c);
        c.gridx = 1; form.add(tfDireccion, c); row++;

        c.gridx = 0; c.gridy = row; form.add(new JLabel("Fecha de nacimiento (YYYY-MM-DD):"), c);
        c.gridx = 1; form.add(tfFecha, c); row++;

        c.gridx = 0; c.gridy = row; form.add(new JLabel("Usuario:"), c);
        c.gridx = 1; form.add(tfUsuario, c); row++;

        c.gridx = 0; c.gridy = row; form.add(new JLabel("Password:"), c);
        c.gridx = 1; form.add(pfPass, c); row++;

        c.gridx = 0; c.gridy = row; form.add(new JLabel("Email:"), c);
        c.gridx = 1; form.add(tfEmail, c); row++;

        c.gridx = 0; c.gridy = row; form.add(new JLabel("Peso (kg):"), c);
        c.gridx = 1; form.add(tfPeso, c); row++;

        c.gridx = 0; c.gridy = row; form.add(new JLabel("Altura (cm):"), c);
        c.gridx = 1; form.add(tfAltura, c); row++;

        c.gridx = 0; c.gridy = row; form.add(new JLabel("Edad:"), c);
        c.gridx = 1; form.add(tfEdad, c); row++;

        c.gridx = 0; c.gridy = row; form.add(new JLabel("Sexo:"), c);
        c.gridx = 1; form.add(cbSexo, c); row++;

        c.gridx = 0; c.gridy = row; form.add(new JLabel("Cintura (cm):"), c);
        c.gridx = 1; form.add(tfCintura, c); row++;

        c.gridx = 0; c.gridy = row; form.add(new JLabel("Cuello (cm):"), c);
        c.gridx = 1; form.add(tfCuello, c); row++;

        c.gridx = 0; c.gridy = row; form.add(new JLabel("Cadera (cm) (mujeres):"), c);
        c.gridx = 1; form.add(tfCadera, c); row++;

        c.gridx = 0; c.gridy = row; form.add(new JLabel("Factor de actividad:"), c);
        c.gridx = 1; form.add(tfFactor, c); row++;

        JScrollPane scroll = new JScrollPane(form);
        p.add(scroll, BorderLayout.CENTER);

        JPanel footer = new JPanel();
        JButton btnBack = styledButton("Volver", new Color(127,140,141));
        JButton btnSubmit = styledButton("Registrar", Color.BLACK);
        btnBack.addActionListener(e -> cardLayout.show(cards, "welcome"));

        btnSubmit.addActionListener(e -> {
            try {
                LocalDate fecha = LocalDate.parse(tfFecha.getText().trim());
                DatosPersonales dp = new DatosPersonales(tfNombre.getText().trim(), tfCedula.getText().trim(),
                        tfTelefono.getText().trim(), tfDireccion.getText().trim(), fecha);

                Credenciales cred = new Credenciales(tfUsuario.getText().trim(), new String(pfPass.getPassword()), tfEmail.getText().trim());

                String selSexo = (String) cbSexo.getSelectedItem();
                if (selSexo == null) throw new IllegalArgumentException("Sexo no seleccionado");
                Sexo sexo = Sexo.valueOf(selSexo.toUpperCase());
                Double caderaVal = null;
                if (sexo == Sexo.FEMENINO) caderaVal = Double.parseDouble(tfCadera.getText().trim());

                PerfilFisico pf = new PerfilFisico(Double.parseDouble(tfPeso.getText().trim()),
                        Double.parseDouble(tfAltura.getText().trim()), Integer.parseInt(tfEdad.getText().trim()),
                        sexo, Double.parseDouble(tfCintura.getText().trim()), Double.parseDouble(tfCuello.getText().trim()),
                        caderaVal, Double.parseDouble(tfFactor.getText().trim()));

                Usuario u = new Usuario(dp, cred, pf);
                boolean ok = gestor.registrarUsuario(u);
                if (!ok) JOptionPane.showMessageDialog(this, "Error: cédula o email duplicado o datos inválidos.", "Registro", JOptionPane.WARNING_MESSAGE);
                else {
                    JOptionPane.showMessageDialog(this, "Usuario registrado correctamente.", "Registro", JOptionPane.INFORMATION_MESSAGE);
                    cardLayout.show(cards, "login");
                }
            } catch (DateTimeParseException dtpe) {
                JOptionPane.showMessageDialog(this, "Fecha inválida. Use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException iae) {
                JOptionPane.showMessageDialog(this, "Entrada inválida: " + iae.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error inesperado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        footer.add(btnBack);
        footer.add(Box.createRigidArea(new Dimension(12,0)));
        footer.add(btnSubmit);
        p.add(footer, BorderLayout.SOUTH);

        return p;
    }

    private JPanel createLoginPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new EmptyBorder(12,12,12,12));

        JLabel header = boldLabel("Iniciar sesión", 20);
        header.setBorder(new EmptyBorder(6,6,12,6));
        p.add(header, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8,8,8,8);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx=0; c.gridy=0; form.add(new JLabel("Usuario:"), c);
        JTextField tfUser = new JTextField(); tfUser.setForeground(Color.BLACK); tfUser.setBackground(Color.WHITE); tfUser.setCaretColor(Color.BLACK); tfUser.setOpaque(true); tfUser.setColumns(20); tfUser.setPreferredSize(new Dimension(220, 28)); c.gridx=1; form.add(tfUser, c);
        c.gridx=0; c.gridy=1; form.add(new JLabel("Password:"), c);
        JPasswordField pfPass = new JPasswordField(); pfPass.setForeground(Color.BLACK); pfPass.setBackground(Color.WHITE); pfPass.setCaretColor(Color.BLACK); pfPass.setOpaque(true); pfPass.setColumns(20); pfPass.setPreferredSize(new Dimension(220, 28)); c.gridx=1; form.add(pfPass, c);

        p.add(form, BorderLayout.CENTER);

        JPanel footer = new JPanel();
        JButton btnBack = styledButton("Volver", new Color(127,140,141));
        JButton btnLogin = styledButton("Entrar", Color.BLACK);

        btnBack.addActionListener(e -> cardLayout.show(cards, "welcome"));

        btnLogin.addActionListener(e -> {
            String user = tfUser.getText().trim();
            String pass = new String(pfPass.getPassword());

            Usuario encontrado = null;
            for (Usuario u : gestor.getUsuariosRegistrados()) {
                if (u.getCredenciales() != null && u.getCredenciales().validar(user, pass)) {
                    encontrado = u; break;
                }
            }

            if (encontrado == null) {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas.", "Login", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // abrir panel de usuario en nueva ventana
            JFrame userFrame = new JFrame("POOwerFit - " + encontrado.getDatosPersonales().getNombreCompleto());
            userFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            userFrame.setSize(720, 520);
            userFrame.setLocationRelativeTo(this);
            userFrame.add(createUserPanel(encontrado));
            userFrame.setVisible(true);
        });

        footer.add(btnBack);
        footer.add(btnLogin);
        p.add(footer, BorderLayout.SOUTH);

        return p;
    }

    // Actualizo createUserPanel para usar chips y añadir gráfico
    private JPanel createUserPanel(Usuario u) {
        JPanel p = new JPanel(new BorderLayout());
        JTabbedPane tabs = new JTabbedPane();

        // Datos
        DatosPersonales dp = u.getDatosPersonales();
        PerfilFisico pf = u.getPerfilFisico();

        // --- PERFIL: tarjetas de resumen + detalles en lista ---
        JPanel perfil = new JPanel(new BorderLayout());
        perfil.setBorder(new EmptyBorder(12,12,12,12));

        // tarjetas de estadisticas
        JPanel stats = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
        stats.setBackground(perfil.getBackground());

        double imc = CalculadoraSalud.calcularIMC(pf);
        double grasa = CalculadoraSalud.calcularGrasaCorporal(pf);
        String somatotipo = CalculadoraSalud.determinarSomatotipo(pf);

        stats.add(createStatCard("IMC", String.format("%.1f", imc), new Color(52, 152, 219)));
        stats.add(createStatCard("Somatotipo", somatotipo, new Color(46, 204, 113)));
        stats.add(createStatCard("% Grasa", String.format("%.1f%%", grasa), new Color(231, 76, 60)));

        perfil.add(stats, BorderLayout.NORTH);

        // detalles clave presentado en panel con valores resaltados (chips)
        JPanel details = new JPanel(new GridBagLayout());
        GridBagConstraints dc = new GridBagConstraints();
        dc.insets = new Insets(8,8,8,8);
        dc.anchor = GridBagConstraints.WEST;
        dc.gridx = 0; dc.gridy = 0;

        details.add(boldLabel("Nombre:" , 14), dc);
        dc.gridx = 1; details.add(createChip(dp.getNombreCompleto(), Color.BLACK), dc);
        dc.gridx = 0; dc.gridy++;

        details.add(boldLabel("Email:", 14), dc);
        dc.gridx = 1; details.add(createChip(u.getCredenciales().getEmail(), Color.BLACK), dc);
        dc.gridx = 0; dc.gridy++;

        details.add(boldLabel("Edad:", 14), dc);
        dc.gridx = 1; details.add(createChip(String.valueOf(pf.getEdad()), Color.BLACK), dc);
        dc.gridx = 0; dc.gridy++;

        details.add(boldLabel("Peso:", 14), dc);
        dc.gridx = 1; details.add(createChip(String.format("%.2f kg", pf.getPesoKg()), Color.BLACK), dc);
        dc.gridx = 0; dc.gridy++;

        details.add(boldLabel("Altura:", 14), dc);
        dc.gridx = 1; details.add(createChip(String.format("%.0f cm", pf.getAlturaCm()), Color.BLACK), dc);
        dc.gridx = 0; dc.gridy++;

        details.add(boldLabel("Cintura:", 14), dc);
        dc.gridx = 1; details.add(createChip(String.format("%.1f cm", pf.getCinturaCm()), Color.BLACK), dc);
        dc.gridx = 0; dc.gridy++;

        if (pf.getSexo() == Sexo.FEMENINO) {
            details.add(boldLabel("Cadera:", 14), dc);
            dc.gridx = 1; details.add(createChip(String.format("%.1f cm", pf.getCaderaCm()), Color.BLACK), dc);
            dc.gridx = 0; dc.gridy++;
        }

        details.add(boldLabel("Sexo:", 14), dc);
        dc.gridx = 1; details.add(createChip(pf.getSexo().toString(), Color.BLACK), dc);
        dc.gridx = 0; dc.gridy++;

        perfil.add(details, BorderLayout.CENTER);

        tabs.addTab("Perfil", perfil);

        // --- PROYECCIONES: barras visuales + gráfico sencillo ---
        JPanel proy = new JPanel(new BorderLayout());
        proy.setBorder(new EmptyBorder(12,12,12,12));

        double current = pf.getPesoKg();
        double proj3 = CalculadoraProyecciones.proyectarPeso(pf, "BAJAR", 3);
        double proj6 = CalculadoraProyecciones.proyectarPeso(pf, "BAJAR", 6);
        double proj9 = CalculadoraProyecciones.proyectarPeso(pf, "BAJAR", 9);

        JPanel bars = new JPanel(new GridLayout(4,1,6,6));
        bars.add(new JLabel(String.format("Peso actual: %.2f kg", current)));

        JProgressBar bar3 = new JProgressBar(); bar3.setValue((int)Math.round(proj3)); bar3.setString("3 meses: " + String.format("%.2f kg", proj3)); bar3.setStringPainted(true);
        JProgressBar bar6 = new JProgressBar(); bar6.setValue((int)Math.round(proj6)); bar6.setString("6 meses: " + String.format("%.2f kg", proj6)); bar6.setStringPainted(true);
        JProgressBar bar9 = new JProgressBar(); bar9.setValue((int)Math.round(proj9)); bar9.setString("9 meses: " + String.format("%.2f kg", proj9)); bar9.setStringPainted(true);

        bars.add(bar3); bars.add(bar6); bars.add(bar9);
        proy.add(bars, BorderLayout.NORTH);

        // gráfico simple de línea + tabla detallada de proyecciones
        double[] data = new double[]{current, proj3, proj6, proj9};
        String[] labels = new String[]{"Ahora","3m","6m","9m"};
        JPanel chart = new LineChartPanel(data, labels);
        chart.setPreferredSize(new Dimension(400, 180));

        String[] colNames = new String[]{"Periodo","Peso (kg)","Cambio (kg)","Cambio/sem (kg)","%/sem"};
        Object[][] tableData = new Object[4][5];
        double[] projs = new double[]{current, proj3, proj6, proj9};
        for (int i=0;i<4;i++) {
            int meses = (i==0)?0:(i==1?3:(i==2?6:9));
            String periodo = (meses==0) ? "Ahora" : (meses + " meses");
            double pesoVal = projs[i];
            double cambio = pesoVal - current;
            int semanasTotal = Math.max(1, meses*4);
            double cambioSem = (meses==0)?0.0:(cambio / semanasTotal);
            double pctSem = (meses==0)?0.0:((cambioSem / current) * 100.0);
            tableData[i][0] = periodo;
            tableData[i][1] = String.format("%.2f", pesoVal);
            tableData[i][2] = String.format("%.2f", cambio);
            tableData[i][3] = String.format("%.3f", cambioSem);
            tableData[i][4] = String.format("%.3f", pctSem);
        }

        javax.swing.table.DefaultTableModel dtm = new javax.swing.table.DefaultTableModel(tableData, colNames) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable projTable = new JTable(dtm);
        projTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        projTable.setRowHeight(26);
        // renderer para colorear cambio: negativo (pérdida) en verde, aumento en rojo
        projTable.getColumnModel().getColumn(2).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                try {
                    double v = Double.parseDouble(table.getValueAt(row,2).toString());
                    c.setForeground(v < 0 ? new Color(46,204,113) : new Color(231,76,60));
                } catch (Exception ex) { c.setForeground(Color.DARK_GRAY); }
                setHorizontalAlignment(SwingConstants.RIGHT);
                return c;
            }
        });

        JPanel mid = new JPanel(new BorderLayout());
        mid.add(chart, BorderLayout.NORTH);
        mid.add(new JScrollPane(projTable), BorderLayout.CENTER);
        proy.add(mid, BorderLayout.CENTER);

        JButton btnValidar = styledButton("Validar proyección 9 meses", new Color(41, 128, 185));
        btnValidar.addActionListener(e -> {
            int semanas = 9 * 4; // aproximación
            double pesoActual = pf.getPesoKg();
            double perdidaTotal = pesoActual - proj9;
            double perdidaPorSemanaKg = perdidaTotal / semanas;
            double porcentajePorSemana = (perdidaPorSemanaKg / pesoActual) * 100.0;
            boolean segura = ValidadorSalud.esProyeccionSegura(pesoActual, proj9, semanas);

            StringBuilder sb = new StringBuilder();
            sb.append(String.format("Peso actual: %.2f kg\nPeso proyectado (9 meses): %.2f kg\n", pesoActual, proj9));
            sb.append(String.format("Pérdida total estimada: %.2f kg\nSemanas (aprox): %d\nPérdida por semana: %.3f kg\nPorcentaje por semana: %.3f %%\n\n", perdidaTotal, semanas, perdidaPorSemanaKg, porcentajePorSemana));

            if (segura) {
                sb.append("Resultado: SEGURO. La pérdida proyectada está dentro del límite recomendado (<= 1% del peso corporal por semana).\n");
                sb.append("Interpretación: a este ritmo la pérdida es gradual; mantén déficit calórico moderado y prioriza proteínas para preservar masa muscular.\n");
            } else {
                sb.append("Resultado: NO SEGURO. La proyección excede el 1% del peso corporal por semana.\n");
                sb.append("Recomendación: reducir el déficit calórico, aumentar periodos de mantenimiento y consultar a profesional si se busca una pérdida más rápida.\n");
            }

            sb.append("\nJustificación técnica:\n");
            sb.append("Se calcula la pérdida semanal como (peso_actual - peso_proyectado) / semanas. La regla 1%/sem evita pérdidas rápidas que pueden afectar masa magra y función metabólica.\n");

            // Usar dialog con icono acorde
            if (segura) JOptionPane.showMessageDialog(this, sb.toString(), "Validación proyección", JOptionPane.INFORMATION_MESSAGE);
            else JOptionPane.showMessageDialog(this, sb.toString(), "Validación proyección", JOptionPane.WARNING_MESSAGE);
        });
        JPanel proyF = new JPanel(new FlowLayout(FlowLayout.RIGHT)); proyF.add(btnValidar);
        proy.add(proyF, BorderLayout.SOUTH);

        tabs.addTab("Proyecciones", proy);

        // --- DIAGNÓSTICO ---
        JPanel diag = new JPanel(new BorderLayout()); diag.setBorder(new EmptyBorder(12,12,12,12));
        // indicador visual de riesgo
        double imcValor = CalculadoraSalud.calcularIMC(pf);
        double grasaValor = CalculadoraSalud.calcularGrasaCorporal(pf);
        String diagnostico = ValidadorSalud.obtenerDiagnosticoSeguridad(pf);

        JPanel topDiag = new JPanel(new BorderLayout());
        topDiag.setBorder(new EmptyBorder(6,6,12,6));
        JLabel lblTitle = new JLabel("Diagnóstico de seguridad");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        topDiag.add(lblTitle, BorderLayout.WEST);

        // badge
        JLabel badge = new JLabel();
        badge.setOpaque(true);
        badge.setBorder(BorderFactory.createEmptyBorder(6,12,6,12));
        if (imcValor >= 35) { badge.setText("ALTO RIESGO"); badge.setBackground(new Color(192,57,43)); }
        else if (imcValor >= 30) { badge.setText("RIESGO MODERADO"); badge.setBackground(new Color(230,126,34)); }
        else { badge.setText("RIESGO BAJO"); badge.setBackground(new Color(46,204,113)); }
        badge.setForeground(Color.WHITE);
        topDiag.add(badge, BorderLayout.EAST);

        diag.add(topDiag, BorderLayout.NORTH);

        // mostrar datos del usuario y validaciones específicas
        JPanel datosPanel = new JPanel(new GridLayout(0,2,8,8));
        datosPanel.setBorder(new EmptyBorder(8,8,8,8));
        datosPanel.add(boldLabel("Peso (kg):", 14)); datosPanel.add(valueLabel(String.format("%.2f", pf.getPesoKg()), Color.BLACK));
        datosPanel.add(boldLabel("Altura (cm):", 14)); datosPanel.add(valueLabel(String.format("%.0f", pf.getAlturaCm()), Color.BLACK));
        datosPanel.add(boldLabel("Edad:", 14)); datosPanel.add(valueLabel(String.valueOf(pf.getEdad()), Color.BLACK));
        datosPanel.add(boldLabel("Sexo:", 14)); datosPanel.add(valueLabel(pf.getSexo().toString(), Color.BLACK));
        datosPanel.add(boldLabel("Cintura (cm):", 14)); datosPanel.add(valueLabel(String.format("%.1f", pf.getCinturaCm()), Color.BLACK));
        if (pf.getCaderaCm() != null) { datosPanel.add(boldLabel("Cadera (cm):", 14)); datosPanel.add(valueLabel(String.format("%.1f", pf.getCaderaCm()), Color.BLACK)); }
        datosPanel.add(new JSeparator()); datosPanel.add(new JSeparator());

        JTextArea taDiag = new JTextArea();
        taDiag.setEditable(false); taDiag.setLineWrap(true); taDiag.setWrapStyleWord(true);
        StringBuilder sbDiag = new StringBuilder();
        sbDiag.append(diagnostico).append("\n\n");
        sbDiag.append(String.format("IMC: %.2f    |    %s\n", imcValor, CalculadoraSalud.determinarSomatotipo(pf)));
        sbDiag.append(String.format("%s: %.2f%%\n\n", "Grasa corporal estimada", grasaValor));
        sbDiag.append("Recomendaciones prácticas:\n");
        sbDiag.append("- Si riesgo alto: priorizar movilidad y baja intensidad; contactar a un profesional.\n");
        sbDiag.append("- Vigilar signos clínicos (mareos, dolor torácico, desmayos).\n");
        sbDiag.append("- Iniciar con sesiones cortas y aumentar progresivamente.\n");
        sbDiag.append("\nProyección y seguridad:\n");
        double ejemploProj = CalculadoraProyecciones.proyectarPeso(pf, "BAJAR", 9);
        int semanas = 9 * 4;
        double perdidaTotal = pf.getPesoKg() - ejemploProj;
        double perdidaPorSemanaKg = perdidaTotal / semanas;
        double porcentajePorSemana = (perdidaPorSemanaKg / pf.getPesoKg()) * 100.0;
        sbDiag.append(String.format("- Pérdida estimada en 9 meses: %.2f kg (%.3f kg/sem = %.3f%%/sem)\n", perdidaTotal, perdidaPorSemanaKg, porcentajePorSemana));
        if (ValidadorSalud.esAptoParaCargasPesadas(pf)) sbDiag.append("- El usuario es apto para cargas graduales con supervisión.\n");
        else sbDiag.append("- Restricciones: evitar cargas pesadas; priorizar movilidad y ejercicios de bajo impacto.\n");
        taDiag.setText(sbDiag.toString());
        taDiag.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JPanel centerDiag = new JPanel(new BorderLayout());
        centerDiag.add(datosPanel, BorderLayout.NORTH);
        centerDiag.add(new JScrollPane(taDiag), BorderLayout.CENTER);
        diag.add(centerDiag, BorderLayout.CENTER);

        tabs.addTab("Seguridad", diag);

        // --- RUTINA: lista interactiva con títulos coloreados y vista detalle a pantalla completa ---
        JPanel rut = new JPanel(new BorderLayout()); rut.setBorder(new EmptyBorder(12,12,12,12));
        GeneradorRutinas gen = new GeneradorRutinas();
        PlanEntrenamiento plan = gen.generarRutinaBasica(somatotipo, "MANTENER");

        DefaultListModel<PlanEntrenamiento.Dia> lm = new DefaultListModel<>();
        for (PlanEntrenamiento.Dia d : plan.getDias()) lm.addElement(d);

        JList<PlanEntrenamiento.Dia> list = new JList<>(lm);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setVisibleRowCount(8);
        list.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                PlanEntrenamiento.Dia dia = (PlanEntrenamiento.Dia) value;
                JPanel panel = new JPanel(new BorderLayout());
                panel.setBorder(new EmptyBorder(6,6,6,6));
                panel.setBackground(isSelected ? new Color(230,230,230) : Color.WHITE);
                JLabel lbl = new JLabel(dia.getTitulo());
                lbl.setFont(new Font("SansSerif", Font.BOLD, 14));
                // color por categoría
                Color catColor;
                String catStr = dia.getCategoria();
                if ("Musculacion".equalsIgnoreCase(catStr)) catColor = new Color(52,152,219);
                else if ("Cardio".equalsIgnoreCase(catStr)) catColor = new Color(231,76,60);
                else if ("Flexibilidad".equalsIgnoreCase(catStr)) catColor = new Color(155,89,182);
                else if ("Movilidad".equalsIgnoreCase(catStr)) catColor = new Color(46,204,113);
                else if ("Circuito".equalsIgnoreCase(catStr)) catColor = new Color(241,196,15);
                else catColor = new Color(149,165,166);
                lbl.setForeground(catColor.darker());
                panel.add(lbl, BorderLayout.CENTER);
                JLabel cat = new JLabel(dia.getCategoria());
                cat.setForeground(catColor);
                cat.setFont(new Font("SansSerif", Font.PLAIN, 12));
                panel.add(cat, BorderLayout.SOUTH);
                return panel;
            }
        });

        JPanel leftList = new JPanel(new BorderLayout());
        leftList.add(new JScrollPane(list), BorderLayout.CENTER);
        leftList.setPreferredSize(new Dimension(360, 280));

        // panel detalle y botones
        JPanel rightPanel = new JPanel(new BorderLayout());
        JTextArea taDetalles = new JTextArea(); taDetalles.setEditable(false); taDetalles.setLineWrap(true); taDetalles.setWrapStyleWord(true);
        taDetalles.setFont(new Font("SansSerif", Font.PLAIN, 14));
        rightPanel.add(new JScrollPane(taDetalles), BorderLayout.CENTER);

        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnOpenFull = styledButton("Abrir en pantalla completa", new Color(41, 128, 185));
        JButton btnGen = styledButton("Regenerar rutina", new Color(39, 174, 96));
        JButton btnCalendar = styledButton("Ver calendario (mes)", new Color(155, 89, 182));
        actionRow.add(btnGen);
        actionRow.add(btnOpenFull);
        actionRow.add(btnCalendar);
        rightPanel.add(actionRow, BorderLayout.SOUTH);

        // al seleccionar, mostrar detalle resumido
        list.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                PlanEntrenamiento.Dia sel = list.getSelectedValue();
                if (sel == null) taDetalles.setText("");
                else taDetalles.setText(sel.getDetalle());
            }
        });

        // abrir detalle fullscreen
        btnOpenFull.addActionListener(e -> {
            PlanEntrenamiento.Dia sel = list.getSelectedValue();
            if (sel == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un día de la rutina primero.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), sel.getTitulo(), true);
            dlg.setSize(800, 600);
            dlg.setLocationRelativeTo(this);
            JPanel content = new JPanel(new BorderLayout());
            JTextArea ta = new JTextArea(sel.getDetalle()); ta.setEditable(false); ta.setLineWrap(true); ta.setWrapStyleWord(true); ta.setFont(new Font("SansSerif", Font.PLAIN, 16));
            content.add(new JScrollPane(ta), BorderLayout.CENTER);
            JButton close = styledButton("Cerrar", new Color(127,140,141));
            close.addActionListener(ev -> dlg.dispose());
            JPanel foot = new JPanel(new FlowLayout(FlowLayout.RIGHT)); foot.add(close);
            content.add(foot, BorderLayout.SOUTH);
            dlg.getContentPane().add(content);
            dlg.setVisible(true);
        });

        // regenerar rutina: reemplaza modelo con nueva lista y selecciona la primera
        btnGen.addActionListener(e -> {
            // generar 3 alternativas y mostrar diálogo para seleccionar una
            GeneradorRutinas gen2 = new GeneradorRutinas();
            PlanEntrenamiento[] opciones = new PlanEntrenamiento[3];
            for (int i = 0; i < 3; i++) opciones[i] = gen2.generarRutinaBasica(somatotipo, "MANTENER");

            // crear panel con botones por opción
            JPanel optPanel = new JPanel(new BorderLayout());
            DefaultListModel<String> titles = new DefaultListModel<>();
            JList<String> optList = new JList<>(titles);
            optList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            for (int i = 0; i < opciones.length; i++) {
                String summary = opciones[i].toString();
                // trunca para mostrar resumen
                String first = summary.length() > 180 ? summary.substring(0, 180) + "..." : summary;
                titles.addElement("Opción " + (i+1) + ": " + opciones[i].getNombre() + " - " + first);
            }
            optList.setVisibleRowCount(6);
            optPanel.add(new JScrollPane(optList), BorderLayout.CENTER);

            int sel = JOptionPane.showConfirmDialog(this, optPanel, "Elige una alternativa de rutina", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (sel == JOptionPane.OK_OPTION && optList.getSelectedIndex() >= 0) {
                PlanEntrenamiento elegido = opciones[optList.getSelectedIndex()];
                DefaultListModel<PlanEntrenamiento.Dia> lm2 = new DefaultListModel<>();
                for (PlanEntrenamiento.Dia d : elegido.getDias()) lm2.addElement(d);
                list.setModel(lm2);
                if (!lm2.isEmpty()) list.setSelectedIndex(0);
            }
        });

        // mostrar calendario modal
        btnCalendar.addActionListener(e -> showCalendarDialog(plan));

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftList, rightPanel);
        split.setResizeWeight(0.35);
        rut.add(split, BorderLayout.CENTER);

        tabs.addTab("Rutina", rut);

        p.add(tabs, BorderLayout.CENTER);
        return p;
    }

    // Helper para tarjetas de estadistica
    private JPanel createStatCard(String title, String value, Color bg) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(bg);
        card.setPreferredSize(new Dimension(150,70));
        card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(bg.darker()), new EmptyBorder(8,12,8,12)));

        JLabel t = new JLabel(title);
        t.setForeground(Color.WHITE);
        t.setFont(new Font("SansSerif", Font.PLAIN, 12));
        JLabel v = new JLabel(value, SwingConstants.RIGHT);
        v.setForeground(Color.WHITE);
        v.setFont(new Font("SansSerif", Font.BOLD, 18));

        card.add(t, BorderLayout.NORTH);
        card.add(v, BorderLayout.CENTER);
        return card;
    }

    // Panel simple para dibujar una línea de proyección
    private static class LineChartPanel extends JPanel {
        private final double[] values;
        private final String[] labels;
        LineChartPanel(double[] values, String[] labels) { this.values = values; this.labels = labels; }
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(); int h = getHeight();
            // margins
            int ml = 40, mr = 20, mt = 10, mb = 30;
            double min = Double.MAX_VALUE, max = -Double.MAX_VALUE;
            for (double v : values) { min = Math.min(min, v); max = Math.max(max, v); }
            if (Math.abs(max-min) < 1e-6) { max = min + 1; }
            // draw axes
            g2.setColor(new Color(200,200,200));
            g2.drawLine(ml, h-mb, w-mr, h-mb);
            // points
            int n = values.length;
            int plotW = w - ml - mr;
            int plotH = h - mt - mb;
            int[] xs = new int[n]; int[] ys = new int[n];
            for (int i=0;i<n;i++) {
                xs[i] = ml + (int)Math.round((double)i/(n-1) * plotW);
                ys[i] = mt + (int)Math.round((1 - (values[i]-min)/(max-min)) * plotH);
            }
            // draw line
            g2.setStroke(new BasicStroke(2f));
            g2.setColor(new Color(41, 128, 185));
            for (int i=0;i<n-1;i++) g2.drawLine(xs[i], ys[i], xs[i+1], ys[i+1]);
            // draw points
            g2.setColor(new Color(231, 76, 60));
            for (int i=0;i<n;i++) g2.fillOval(xs[i]-4, ys[i]-4, 8,8);
            // labels
            g2.setColor(Color.DARK_GRAY);
            for (int i=0;i<n;i++) {
                String lbl = labels[i] + " (" + String.format("%.1f", values[i]) + ")";
                g2.drawString(lbl, xs[i]-20, h-8);
            }
            g2.dispose();
        }
    }

    // Genera una explicación extendida y práctica para una entrada de rutina
    private String generateDetalleExtendido(String sel, PerfilFisico pf) {
        StringBuilder sb = new StringBuilder();
        sb.append(sel).append("\n\n");

        // Extraer valores si existen
        String descanso = "--";
        String seriesReps = "--";
        String tempo = "--";
        for (String line : sel.split("\\n")) {
            if (line.toLowerCase().contains("descanso")) descanso = line.substring(line.indexOf(":")+1).trim();
            if (line.toLowerCase().contains("series x reps")) seriesReps = line.substring(line.indexOf(":")+1).trim();
            if (line.toLowerCase().contains("tempo")) tempo = line.substring(line.indexOf(":")+1).trim();
        }

        sb.append("Guía técnica detallada:\n");
        if (!seriesReps.equals("--")) sb.append("- Volumen objetivo: ").append(seriesReps).append(". Ajusta la carga para llegar a fallo técnico en las últimas 1-2 reps.").append("\n");
        if (!descanso.equals("--")) sb.append("- Descanso entre series: ").append(descanso).append(". Si buscas fuerza, aumentar descanso; si buscas resistencia, reducirlo.").append("\n");
        if (!tempo.equals("--")) sb.append("- Tempo recomendado: ").append(tempo).append(" (ej.: 2-0-1 = 2s excéntrica, 0s pausa, 1s concéntrica). Mantén el control.").append("\n");

        sb.append("- Signos de buena técnica: movimiento fluido, control respiratorio y activación del grupo objetivo.\n");
        sb.append("- Señales para parar: dolor agudo, mareo, ardor intenso en articulación o pérdida de forma técnica.\n");
        sb.append("- Tiempos de descanso adicionales: entre ejercicios compuestos 90-120s; entre ejercicios accesorios 45-60s.\n");
        sb.append("- Recomendación de progresión: aumentar carga un 2.5-5% cuando completes todas las series con buena técnica.\n");

        sb.append("\nConsejos para validar el gesto técnico:\n");
        sb.append("- Filma una serie (ángulo lateral) y revisa alineación de columna.\n");
        sb.append("- Controla respiración: exhala en la fase concéntrica.\n");
        sb.append("- Evita compensaciones: hombros elevados, rodillas que colapsan hacia dentro.\n");

        sb.append("\nNota: ajusta intensidad según edad y condición; ante dudas, consulta a profesional de salud/entrenador.");
        return sb.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainGUI g = new MainGUI();
            g.setVisible(true);
        });
    }

    // Mostrar calendario simple: asigna entradas del plan a los días del mes (ciclando si necesario)
    private void showCalendarDialog(PlanEntrenamiento plan) {
        YearMonth ym = YearMonth.now();
        int daysInMonth = ym.lengthOfMonth();
        JDialog dlg = new JDialog((Frame) null, "Calendario - " + ym.getMonth().toString() + " " + ym.getYear(), true);
        dlg.setSize(900, 600);
        dlg.setLocationRelativeTo(this);
        JPanel content = new JPanel(new BorderLayout());

        JPanel header = new JPanel(new GridLayout(1,7));
        String[] dias = new String[]{"Lun","Mar","Mié","Jue","Vie","Sáb","Dom"};
        for (String d : dias) { JLabel l = new JLabel(d, SwingConstants.CENTER); l.setFont(new Font("SansSerif", Font.BOLD, 12)); header.add(l); }
        content.add(header, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(0,7));
        int startDow = ym.atDay(1).getDayOfWeek().getValue(); // 1=Mon
        int pad = (startDow + 6) % 7; // convert so Monday=0
        // create empty pads
        for (int i=0;i<pad;i++) grid.add(new JPanel());

        // assign plan days cyclically
        int planSize = Math.max(1, plan.getDias().size());
        for (int day=1; day<=daysInMonth; day++) {
            int planIndex = (day-1) % planSize;
            PlanEntrenamiento.Dia diaEntry = plan.getDias().get(planIndex);
            JPanel cell = new JPanel(new BorderLayout());
            cell.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            JLabel num = new JLabel(String.valueOf(day)); num.setHorizontalAlignment(SwingConstants.LEFT);
            num.setBorder(new EmptyBorder(4,6,0,0));
            cell.add(num, BorderLayout.NORTH);
            JTextArea ta = new JTextArea(diaEntry.getTitulo());
            ta.setLineWrap(true);
            ta.setWrapStyleWord(true);
            ta.setEditable(false);
            ta.setBackground(getBackground()); ta.setFont(new Font("SansSerif", Font.PLAIN, 12));
            // color indicator bar
            Color barColor = Color.GRAY;
            String cat = diaEntry.getCategoria();
            if ("Musculacion".equalsIgnoreCase(cat)) barColor = new Color(52,152,219);
            else if ("Cardio".equalsIgnoreCase(cat)) barColor = new Color(231,76,60);
            else if ("Flexibilidad".equalsIgnoreCase(cat)) barColor = new Color(155,89,182);
            else if ("Movilidad".equalsIgnoreCase(cat)) barColor = new Color(46,204,113);
            else if ("Circuito".equalsIgnoreCase(cat)) barColor = new Color(241,196,15);
            JPanel colorBar = new JPanel(); colorBar.setBackground(barColor); colorBar.setPreferredSize(new Dimension(4,40));
            JPanel center = new JPanel(new BorderLayout()); center.add(ta, BorderLayout.CENTER);
            cell.add(center, BorderLayout.CENTER);
            cell.add(colorBar, BorderLayout.WEST);
            // click to open detail
            cell.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    JDialog d = new JDialog((Frame) SwingUtilities.getWindowAncestor(MainGUI.this), diaEntry.getTitulo(), true);
                    d.setSize(700,500); d.setLocationRelativeTo(MainGUI.this);
                    JTextArea t = new JTextArea(diaEntry.getDetalle()); t.setEditable(false); t.setLineWrap(true); t.setWrapStyleWord(true); t.setFont(new Font("SansSerif", Font.PLAIN, 14));
                    d.getContentPane().add(new JScrollPane(t));
                    JButton close = styledButton("Cerrar", new Color(127,140,141)); close.addActionListener(ev -> d.dispose());
                    JPanel f = new JPanel(new FlowLayout(FlowLayout.RIGHT)); f.add(close); d.getContentPane().add(f, BorderLayout.SOUTH);
                    d.setVisible(true);
                }
            });
            grid.add(cell);
        }

        // fill trailing cells
        int totalCells = pad + daysInMonth;
        int trailing = (7 - (totalCells % 7)) % 7;
        for (int i=0;i<trailing;i++) grid.add(new JPanel());

        content.add(new JScrollPane(grid), BorderLayout.CENTER);
        dlg.getContentPane().add(content);
        dlg.setVisible(true);
    }
}
