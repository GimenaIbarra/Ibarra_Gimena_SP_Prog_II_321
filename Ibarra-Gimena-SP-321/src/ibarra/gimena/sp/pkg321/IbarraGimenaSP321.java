package ibarra.gimena.sp.pkg321;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import models.InventarioDeModificacion;
import models.ModificacionDeLorean;
import models.TipoModificacion;
import config.RutaArchivos;
import Service.ServiceModificaciones;
/**
 *
 * @author Usuario
 */
public class IbarraGimenaSP321 {

    public static void main(String[] args) {
        
        try {
            InventarioDeModificacion<ModificacionDeLorean> inv = new InventarioDeModificacion<>();

            inv.agregar(new ModificacionDeLorean(1, "Flux Capacitor Mk1", "Doc Brown", TipoModificacion.TIEMPO));
            inv.agregar(new ModificacionDeLorean(2, "Mr. Fusion", "Doc Brown", TipoModificacion.ENERGIA));
            inv.agregar(new ModificacionDeLorean(3, "Hover Conversion", "Los Libios", TipoModificacion.CONVERSION_HOVER));
            inv.agregar(new ModificacionDeLorean(4, "Temporal Circuit V3", "Clara Clayton", TipoModificacion.TIEMPO));
            inv.agregar(new ModificacionDeLorean(5, "Shield Upgrade", "Marty McFly", TipoModificacion.SEGURIDAD));

            System.out.println("Modificaciones del DeLorean:");
            ServiceModificaciones.listarModificaciones(inv.getElementos());

            System.out.println("\nModificaciones tipo TIEMPO:");
            List<ModificacionDeLorean> tiempo =
                    inv.filtrar(m -> m.getTipo() == TipoModificacion.TIEMPO);
            ServiceModificaciones.listarModificaciones(tiempo);

            System.out.println("\nModificaciones que contienen 'hover':");
            List<ModificacionDeLorean> hover =
                    inv.filtrar(m -> m.getNombre().toLowerCase().contains("hover"));
            ServiceModificaciones.listarModificaciones(hover);

            System.out.println("\nModificaciones ordenadas por ID:");
            inv.ordenar();
            ServiceModificaciones.listarModificaciones(inv.getElementos());

            System.out.println("\nModificaciones ordenadas por nombre:");
            inv.ordenar(Comparator.comparing(ModificacionDeLorean::getNombre));
            ServiceModificaciones.listarModificaciones(inv.getElementos());

            // RUTA BINARIO
            String rutaBin = RutaArchivos.getRutaBINString();
            inv.guardarEnArchivo(rutaBin);

            InventarioDeModificacion<ModificacionDeLorean> cargado =
                    new InventarioDeModificacion<>();
            cargado.cargarDesdeArchivo(rutaBin);

            System.out.println("\nModificaciones cargadas desde archivo binario:");
            ServiceModificaciones.listarModificaciones(cargado.getElementos());

            // RUTA CSV
            String rutaCSV = RutaArchivos.getRutaCSVString();
            inv.guardarEnCSV(rutaCSV);

            cargado.cargarDesdeCSV(rutaCSV, ModificacionDeLorean::fromCSV);

            System.out.println("\nModificaciones cargadas desde archivo CSV:");
            ServiceModificaciones.listarModificaciones(cargado.getElementos());

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ocurrió un error: " + e.getMessage());
        }
    }
    
}
