package view;

import java.util.Scanner;

public class Validaciones {

    private static final Scanner scanner = new Scanner(System.in);

    public String pedirTexto(String mensaje) {
        while (true) {
            System.out.println(mensaje);
            String dato = scanner.nextLine().strip();
            if (dato.isBlank()) {
                System.out.println("No se aceptan espacios en blanco.");
                continue;
            }
            boolean valido = true;
            for (int i = 0; i < dato.length(); i++) {
                char caracter = dato.charAt(i);
                if (!Character.isLetter(caracter) && caracter != ' ') {
                    valido = false;
                    break;
                }
            }
            if (valido) {
                return dato;
            }
            System.out.println("Solo se aceptan letras y espacios.");
        }
    }

    public String pedirDocumento(String mensaje) {
        return pedirSoloNumeros(mensaje, "El documento solo puede contener numeros.");
    }

    public String pedirTelefono(String mensaje) {
        return pedirSoloNumeros(mensaje, "El telefono solo puede contener numeros.");
    }

    public String pedirCorreo(String mensaje) {
        while (true) {
            System.out.println(mensaje);
            String correo = scanner.nextLine().strip();
            if (!correo.isBlank() && correo.contains("@") && correo.indexOf('@') > 0 && correo.indexOf('@') < correo.length() - 1) {
                return correo;
            }
            System.out.println("Ingrese un correo valido.");
        }
    }

    public String pedirClave(String mensaje) {
        while (true) {
            System.out.println(mensaje);
            String clave = scanner.nextLine();
            if (clave.length() >= 4) {
                return clave;
            }
            System.out.println("La clave debe tener minimo 4 caracteres.");
        }
    }

    public String pedirCadenaNoVacia(String mensaje) {
        while (true) {
            System.out.println(mensaje);
            String dato = scanner.nextLine().strip();
            if (!dato.isBlank()) {
                return dato;
            }
            System.out.println("No se aceptan espacios en blanco.");
        }
    }

    public double pedirDecimalMayorQueCero(String mensaje) {
        while (true) {
            System.out.println(mensaje);
            try {
                double valor = Double.parseDouble(scanner.nextLine().strip());
                if (valor > 0) {
                    return valor;
                }
            } catch (NumberFormatException e) {
            }
            System.out.println("Ingrese un valor decimal mayor que cero.");
        }
    }

    public double pedirDecimalEnRango(String mensaje, double minimo, double maximo) {
        while (true) {
            double valor = pedirDecimalMayorQueCero(mensaje);
            if (valor >= minimo && valor <= maximo) {
                return valor;
            }
            System.out.println("Ingrese un valor entre " + minimo + " y " + maximo + ".");
        }
    }

    public int pedirEnteroEnRango(String mensaje, int minimo, int maximo) {
        while (true) {
            System.out.println(mensaje);
            try {
                int valor = Integer.parseInt(scanner.nextLine().strip());
                if (valor >= minimo && valor <= maximo) {
                    return valor;
                }
            } catch (NumberFormatException e) {
            }
            System.out.println("Ingrese un valor entre " + minimo + " y " + maximo + ".");
        }
    }

    private String pedirSoloNumeros(String mensaje, String error) {
        while (true) {
            System.out.println(mensaje);
            String dato = scanner.nextLine().strip();
            if (!dato.isBlank() && dato.chars().allMatch(Character::isDigit)) {
                return dato;
            }
            System.out.println(error);
        }
    }
}
