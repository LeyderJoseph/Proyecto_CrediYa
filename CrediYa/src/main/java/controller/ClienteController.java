package controller;

import dao.ClienteDAO;
import java.util.List;
import model.Cliente;
import view.ingreso.IngresoCliente;
import view.imprimir.ImprimirCliente;
import view.imprimir.Mensajes;

public class ClienteController {

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final IngresoCliente ingreso = new IngresoCliente();
    private final ImprimirCliente imprimir = new ImprimirCliente();
    private final Mensajes mensajes = new Mensajes();

    public void registrar() {
        Cliente cliente = ingreso.pedirCliente();
        if (clienteDAO.agregar(cliente)) {
            mensajes.exito("Cliente registrado correctamente.");
        } else {
            mensajes.error("No fue posible registrar el cliente. El documento puede estar asociado a un cliente existente.");
        }
    }

    public void listar() {
        List<Cliente> clientes = clienteDAO.listar();
        imprimir.lista(clientes);
    }

    public void actualizar() {
        listar();
        int idCliente = ingreso.pedirIdCliente();
        Cliente cliente = clienteDAO.buscar(idCliente);
        if (cliente == null) {
            mensajes.error("El cliente no existe.");
            return;
        }
        switch (ingreso.pedirCampoActualizacion()) {
            case 1 -> cliente.setNombre(ingreso.pedirNombre());
            case 2 -> cliente.setCorreo(ingreso.pedirCorreo());
            case 3 -> cliente.setClave(ingreso.pedirClave());
            case 4 -> cliente.setTelefono(ingreso.pedirTelefono());
            case 5 -> {
                mensajes.informativo("Actualizacion cancelada.");
                return;
            }
            default -> {
                return;
            }
        }
        if (clienteDAO.actualizar(cliente)) {
            mensajes.exito("Cliente actualizado correctamente.");
        } else {
            mensajes.error("No fue posible actualizar el cliente.");
        }
    }

    public void desactivar() {
        listar();
        int idCliente = ingreso.pedirIdCliente();
        Cliente cliente = clienteDAO.buscar(idCliente);
        if (cliente == null) {
            mensajes.error("El cliente no existe.");
            return;
        }
        if (clienteDAO.tienePrestamosActivosOVencidos(idCliente)) {
            mensajes.error("No se puede desactivar un cliente con prestamos activos o vencidos.");
            return;
        }
        if (clienteDAO.desactivar(idCliente)) {
            mensajes.exito("Cliente desactivado correctamente.");
        } else {
            mensajes.error("No fue posible desactivar el cliente.");
        }
    }

    public void mostrarInformacion(Cliente cliente) {
        imprimir.entidad(cliente);
    }
}
