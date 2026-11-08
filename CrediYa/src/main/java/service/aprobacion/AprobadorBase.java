package service.aprobacion;

import dao.PrestamoDAO;
import model.Empleado;
import model.PoliticaCredito;
import model.Prestamo;
import model.RolEmpleado;

public abstract class AprobadorBase implements AprobadorPrestamo {

    private final RolEmpleado rol;
    private final PrestamoDAO prestamoDAO = new PrestamoDAO();
    private AprobadorPrestamo siguiente;

    protected AprobadorBase(RolEmpleado rol) {
        this.rol = rol;
    }

    @Override
    public void setSiguiente(AprobadorPrestamo siguiente) {
        this.siguiente = siguiente;
    }

    @Override
    public boolean procesar(Prestamo prestamo, Empleado empleado, PoliticaCredito politica, boolean aprobar, String observacion) {
        if (puedeGestionar(prestamo, politica)) {
            if (empleado.getRol() != rol || prestamo.getCliente().getIdPersona() == empleado.getIdPersona()) {
                return false;
            }
            return prestamoDAO.decidir(prestamo, empleado, aprobar, observacion);
        }
        if (siguiente == null) {
            return false;
        }
        return siguiente.procesar(prestamo, empleado, politica, aprobar, observacion);
    }

    protected abstract boolean puedeGestionar(Prestamo prestamo, PoliticaCredito politica);
}
