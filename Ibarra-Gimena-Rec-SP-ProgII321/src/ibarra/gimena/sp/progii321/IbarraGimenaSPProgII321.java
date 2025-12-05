
package ibarra.gimena.sp.progii321;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import models.Inventario;
import models.DispositivoDomotico;
import models.Categoria;
import config.RutaArchivos;
import models.Almacenable;

public class IbarraGimenaSPProgII321 {

    public static void main(String[] args) {
        
        try {
            Almacenable<DispositivoDomotico> inv = new Inventario<>();
            
            inv.agregar(new DispositivoDomotico(1001, "Airpure Sl", Categoria.AMBIENTAL, 35, 2023));
            inv.agregar(new DispositivoDomotico(1002, "AssistGo Mini", Categoria.ASISTENCIA, 50, 2019));
            inv.agregar(new DispositivoDomotico(1003, "SecureHome A7", Categoria.SEGURIDAD, 42, 2021));
            inv.agregar(new DispositivoDomotico(1004, "Airpure X2", Categoria.AMBIENTAL, 28, 2022));
            inv.agregar(new DispositivoDomotico(1005, "AssistGo Pro", Categoria.ASISTENCIA, 60, 2020));
            inv.agregar(new DispositivoDomotico(1006, "SafeWatch L3", Categoria.SEGURIDAD, 33, 2024));
            
            System.out.println("=== Inventario original");
            for (DispositivoDomotico d : inv.obtenerTodos()) {
                System.out.println(d);
            }
            
            //1) Orden natural
            inv.ordenar();
            System.out.println("\n=== Orden natural");
            for (DispositivoDomotico d : inv.obtenerTodos()) {
                System.out.println(d);
            }
            
            // 2) Ordenar por nombreModelo → completar Comparator
            inv.ordenar(Comparator.comparing(DispositivoDomotico::getNombreModelo));
            System.out.println("\n=== Ordenados por nombre de modelo ===");
            for (DispositivoDomotico d : inv.obtenerTodos()) {
            System.out.println(d);
         
            
            // 3) Filtrar consumo <= 40W → completar Predicate
            System.out.println("\n=== Dispositivos con consumo <= 40W ===");
            List<DispositivoDomotico> filtrados =
                inv.filtrar(x -> d.getConsumoWatts() <= 40);
            for (DispositivoDomotico x : filtrados) {
                System.out.println(x);
            }
            
            // 4) Transformación: ASISTENCIA con -15% consumo → completar Function
            System.out.println("\n=== Transformación (ASISTENCIA con -15% consumo) ===");
            List<DispositivoDomotico> transformados =
                inv.transformar(/* completar Function */);
            for (DispositivoDomotico d : transformados) {
                System.out.println(d);
            }
            
            // 5) Contar fabricados desde 2020 → completar Predicate
            int recientes = inv.contar(x -> x.getAnioFabricacion() >= 2020);
            System.out.println("\nCantidad fabricados desde 2020: " + recientes);

            

            // 6) Persistencia
            String rutaBin = RutaArchivos.getRutaBINString();
            inv.guardarEnBinario("data/inventario.bin");
            
            Almacenable<DispositivoDomotico> invBIN = new Inventario<>();
            invBIN.cargarDesdeBinario("data/inventario.bin");
           
            System.out.println("\n=== Inventario cargado desde Binario ===");
            for (DispositivoDomotico x : invBIN.obtenerTodos()) {
            System.out.println(x);
            }

            
            String rutaCSV = RutaArchivos.getRutaCSVString();
            inv.guardarEnCSV("data/inventario.csv");
            //inv.guardarEnJSON("data/inventario.json");
            // 7) Cargar desde CSV → completar función fromCSV
            Almacenable<DispositivoDomotico> invCSV = new Inventario<>();
            invCSV.cargarDesdeCSV(rutaCSV, DispositivoDomotico::fromCSV);
            
            System.out.println("\n=== Inventario cargado desde CSV ===");
            for (DispositivoDomotico x : invCSV.obtenerTodos()) {
                System.out.println(x);
            }
            
        
        
            }   
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}

