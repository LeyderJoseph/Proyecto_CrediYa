package view.imprimir;

import java.util.List;
import model.Cliente;

public class ImprimirCliente {

    public void entidad(Cliente cliente) {
        System.out.println("ID: " + cliente.getIdPersona() + " | Nombre: " + cliente.getNombre() + " | Documento: " + cliente.getDocumento() + " | Telefono: " + cliente.getTelefono() + " | Activo: " + cliente.isActivo());
    }

    public void lista(List<Cliente> clientes) {
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }
        clientes.forEach(this::entidad);
    }
}
