package service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import javax.swing.JFileChooser;

public class ArchivoBackup {

    public boolean exportar(Object datos, String nombreSugerido) {
        JFileChooser selector = new JFileChooser();
        selector.setDialogTitle("Seleccione la ubicacion del respaldo");
        selector.setSelectedFile(new File(nombreSugerido));
        int opcion = selector.showSaveDialog(selector);
        if (opcion != JFileChooser.APPROVE_OPTION) {
            return false;
        }
        File destino = selector.getSelectedFile();
        try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(destino))) {
            salida.writeObject(datos);
            System.out.println("Respaldo exportado en: " + destino.getAbsolutePath());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
