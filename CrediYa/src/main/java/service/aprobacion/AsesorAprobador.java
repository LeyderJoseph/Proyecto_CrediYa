package service.aprobacion;

import model.PoliticaCredito;
import model.Prestamo;
import model.RolEmpleado;

public class AsesorAprobador extends AprobadorBase {

    public AsesorAprobador() {
        super(RolEmpleado.ASESOR);
    }

    @Override
    protected boolean puedeGestionar(Prestamo prestamo, PoliticaCredito politica) {
        return prestamo.getCapital() <= politica.getTopeAsesor();
    }
}
