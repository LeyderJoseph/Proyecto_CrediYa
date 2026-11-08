package service.aprobacion;

import model.PoliticaCredito;
import model.Prestamo;
import model.RolEmpleado;

public class GerenteAprobador extends AprobadorBase {

    public GerenteAprobador() {
        super(RolEmpleado.GERENTE);
    }

    @Override
    protected boolean puedeGestionar(Prestamo prestamo, PoliticaCredito politica) {
        return prestamo.getCapital() > politica.getTopeSupervisor() && prestamo.getCapital() <= politica.getMontoMaximo();
    }
}
