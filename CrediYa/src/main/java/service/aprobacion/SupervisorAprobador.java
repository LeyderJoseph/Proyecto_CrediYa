package service.aprobacion;

import model.PoliticaCredito;
import model.Prestamo;
import model.RolEmpleado;

public class SupervisorAprobador extends AprobadorBase {

    public SupervisorAprobador() {
        super(RolEmpleado.SUPERVISOR);
    }

    @Override
    protected boolean puedeGestionar(Prestamo prestamo, PoliticaCredito politica) {
        return prestamo.getCapital() > politica.getTopeAsesor() && prestamo.getCapital() <= politica.getTopeSupervisor();
    }
}
