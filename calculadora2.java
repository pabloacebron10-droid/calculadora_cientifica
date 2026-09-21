package calculadora2;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Locale;

public class calculadora2 extends JFrame {

    private final JTextField operacion = new JTextField();
    private final JTextField resultado = new JTextField();

    private final JRadioButton grados = new JRadioButton("Grados", true);
    private final JRadioButton radianes = new JRadioButton("Radianes");

    private final DecimalFormat formato = new DecimalFormat("0.##########");

    private static final Color FONDO = new Color(15, 18, 24);
    private static final Color PANTALLA = new Color(24, 29, 38);
    private static final Color BOTON = new Color(34, 40, 50);
    private static final Color BORDE = new Color(55, 64, 78);
    private static final Color TEXTO = new Color(235, 238, 242);
    private static final Color AZUL = new Color(48, 125, 180);
    private static final Color MORADO = new Color(120, 82, 170);
    private static final Color VERDE = new Color(48, 155, 105);
    private static final Color ROJO = new Color(175, 65, 75);

    public calculadora2() {
        setTitle("Calculadora");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 620);
        setLocationRelativeTo(null);
        setResizable(false);
        crearInterfaz();
    }

    private void crearInterfaz() {
        setLayout(new GridBagLayout());
        getContentPane().setBackground(FONDO);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);

        configurarPantalla(operacion, 21, false);
        configurarPantalla(resultado, 27, true);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 5;
        gbc.weightx = 1;
        gbc.weighty = 0.10;
        add(operacion, gbc);

        gbc.gridy = 1;
        gbc.weighty = 0.10;
        add(resultado, gbc);

        JPanel modo = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        modo.setOpaque(false);

        ButtonGroup grupo = new ButtonGroup();
        grupo.add(grados);
        grupo.add(radianes);

        grados.setOpaque(false);
        radianes.setOpaque(false);
        grados.setForeground(TEXTO);
        radianes.setForeground(TEXTO);

        modo.add(grados);
        modo.add(radianes);

        gbc.gridy = 2;
        gbc.weighty = 0.07;
        add(modo, gbc);

        crearBoton(gbc, "7", 0, 3, 1);
        crearBoton(gbc, "8", 1, 3, 1);
        crearBoton(gbc, "9", 2, 3, 1);
        crearBoton(gbc, "/", 3, 3, 1);
        crearBoton(gbc, "C", 4, 3, 1);

        crearBoton(gbc, "4", 0, 4, 1);
        crearBoton(gbc, "5", 1, 4, 1);
        crearBoton(gbc, "6", 2, 4, 1);
        crearBoton(gbc, "*", 3, 4, 1);
        crearBoton(gbc, "⌫", 4, 4, 1);

        crearBoton(gbc, "1", 0, 5, 1);
        crearBoton(gbc, "2", 1, 5, 1);
        crearBoton(gbc, "3", 2, 5, 1);
        crearBoton(gbc, "-", 3, 5, 1);
        crearBoton(gbc, "(", 4, 5, 1);

        crearBoton(gbc, "0", 0, 6, 1);
        crearBoton(gbc, ".", 1, 6, 1);
        crearBoton(gbc, "π", 2, 6, 1);
        crearBoton(gbc, "+", 3, 6, 1);
        crearBoton(gbc, ")", 4, 6, 1);

        crearBoton(gbc, "sin", 0, 7, 1);
        crearBoton(gbc, "cos", 1, 7, 1);
        crearBoton(gbc, "tan", 2, 7, 1);
        crearBoton(gbc, "=", 3, 7, 2);
    }

    private void configurarPantalla(JTextField pantalla, int tamaño, boolean esResultado) {
        pantalla.setFont(new Font("Monospaced", Font.BOLD, tamaño));
        pantalla.setHorizontalAlignment(SwingConstants.RIGHT);
        pantalla.setEditable(false);
        pantalla.setBackground(PANTALLA);
        pantalla.setForeground(esResultado ? VERDE : TEXTO);
        pantalla.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE, 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
    }

    private void crearBoton(GridBagConstraints gbc, String texto, int columna, int fila, int ancho) {
        JButton boton = new JButton(texto);

        boton.setFont(new Font("SansSerif", Font.BOLD, 17));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(BORDE, 1));
        boton.setBackground(BOTON);
        boton.setForeground(TEXTO);

        if (texto.equals("=")) {
            boton.setBackground(VERDE);
            boton.setForeground(Color.WHITE);
        } else if (texto.equals("C") || texto.equals("CE") || texto.equals("⌫")) {
            boton.setBackground(ROJO);
            boton.setForeground(Color.WHITE);
        } else if (texto.equals("+") || texto.equals("-") || texto.equals("*") || texto.equals("/")) {
            boton.setBackground(AZUL);
            boton.setForeground(Color.WHITE);
        } else if (texto.equals("sin") || texto.equals("cos") || texto.equals("tan") || texto.equals("π")) {
            boton.setBackground(MORADO);
            boton.setForeground(Color.WHITE);
        }

        boton.addActionListener(e -> procesarBoton(texto));

        gbc.gridx = columna;
        gbc.gridy = fila;
        gbc.gridwidth = ancho;
        gbc.weightx = ancho;
        gbc.weighty = 1;

        add(boton, gbc);
    }

    private void procesarBoton(String texto) {
        switch (texto) {
            case "=":
                calcular();
                break;

            case "C":
            case "CE":
                operacion.setText("");
                resultado.setText("");
                break;

            case "⌫":
                String actual = operacion.getText();

                if (!actual.isEmpty()) {
                    operacion.setText(actual.substring(0, actual.length() - 1));
                }
                break;

            case "π":
                añadirTexto("π");
                break;

            case "sin":
                añadirTexto("sin(");
                break;

            case "cos":
                añadirTexto("cos(");
                break;

            case "tan":
                añadirTexto("tan(");
                break;

            default:
                añadirTexto(texto);
        }
    }

    private void añadirTexto(String texto) {
        operacion.setText(operacion.getText() + texto);
    }

    private void calcular() {
        String visible = operacion.getText();

        if (visible.trim().isEmpty()) {
            return;
        }

        try {
            String expresion = visible.replace("π", "pi");

            Function seno = new Function("sin", 1) {
                @Override
                public double apply(double... args) {
                    double valor = args[0];

                    return grados.isSelected()
                            ? Math.sin(Math.toRadians(valor))
                            : Math.sin(valor);
                }
            };

            Function coseno = new Function("cos", 1) {
                @Override
                public double apply(double... args) {
                    double valor = args[0];

                    return grados.isSelected()
                            ? Math.cos(Math.toRadians(valor))
                            : Math.cos(valor);
                }
            };

            Function tangente = new Function("tan", 1) {
                @Override
                public double apply(double... args) {
                    double valor = args[0];

                    return grados.isSelected()
                            ? Math.tan(Math.toRadians(valor))
                            : Math.tan(valor);
                }
            };

            Expression expression = new ExpressionBuilder(expresion)
                    .functions(seno, coseno, tangente)
                    .build();

            double valor = expression.evaluate();

            if (Double.isNaN(valor) || Double.isInfinite(valor)) {
                throw new ArithmeticException();
            }

            if (Math.abs(valor) < 0.000000000001) {
                valor = 0;
            }

            resultado.setText(formato.format(valor));

        } catch (Exception ex) {
            resultado.setText("ERROR");
        }
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        SwingUtilities.invokeLater(() -> {
            calculadora2 calculadora = new calculadora2();
            calculadora.setVisible(true);
        });
    }
}