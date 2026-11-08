package service.aprobacion;

import model.Empleado;
import model.PoliticaCredito;
import model.Prestamo;

public interface AprobadorPrestamo {

    void setSiguiente(AprobadorPrestamo siguiente);

    boolean procesar(Prestamo prestamo, Empleado empleado, PoliticaCredito politica, boolean aprobar, String observacion);
}
