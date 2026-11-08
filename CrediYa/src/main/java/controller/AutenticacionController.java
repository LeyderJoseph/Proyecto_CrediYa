package controller;

import dao.EmpleadoDAO;
import dao.ClienteDAO;
import dao.PersonaDAO;
import model.Cliente;
import model.Empleado;
import model.RolEmpleado;

public class AutenticacionController {

    private final PersonaDAO personaDAO = new PersonaDAO();
    private final EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();

    public boolean requiereRegistroInicial() {
        return !personaDAO.existeGerenteActivo();
    }

    public boolean registrarGerenteInicial(Empleado gerente) {
        if (!requiereRegistroInicial()) {
            return false;
        }
        gerente.setRol(RolEmpleado.GERENTE);
        return empleadoDAO.agregar(gerente);
    }

    public Empleado autenticarEmpleado(String documento, String clave) {
        return empleadoDAO.buscarPorCredenciales(documento, clave);
    }

    public Cliente autenticarCliente(String documento, String clave) {
        return clienteDAO.buscarPorCredenciales(documento, clave);
    }
}
