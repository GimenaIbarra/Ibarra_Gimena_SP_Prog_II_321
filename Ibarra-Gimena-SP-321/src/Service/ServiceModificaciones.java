
package Service;

import java.util.List;
import models.ModificacionDeLorean;


public class ServiceModificaciones {
    public static void listarModificaciones(List<ModificacionDeLorean> lista) {
        lista.forEach(e -> System.out.println(e));
    }
}
