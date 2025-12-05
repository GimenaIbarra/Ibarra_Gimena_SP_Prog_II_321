
package models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/*
Implementar la interfaz Almacenable
 Usar internamente una lista
 NO USAR Streams
 Implementar:
o filtrado, transformación y conteo usando bucles tradicionales
o ordenamientos (natural y por Comparator)
 Persistencias:
o Guardar en binario
o Cargar desde binario
o Guardar en CSV escribiendo SIEMPRE el encabezado
o Cargar desde CSV ignorando la primera línea (el header)

 */
public class Inventario<T extends CSVConvertible & Comparable<T> & java.io.Serializable> implements Almacenable<T> {
    private final List<T> elementos = new ArrayList<>();
    
    public List<T> getElementos() {
        return new ArrayList<>(elementos);
    }

    public void agregar(T elemento) {
        Objects.requireNonNull(elemento, "No se puede agregar un elemento nulo.");
        elementos.add(elemento);
    }

    public T obtener(int indice) {
        validarIndice(indice);
        return elementos.get(indice);
    }
    
    public T eliminar(int indice) {
        validarIndice(indice);
        return elementos.remove(indice);
    }

    
    private void validarIndice(int indice) {
        if (indice < 0 || indice >= elementos.size()) {
            throw new IndexOutOfBoundsException("Indice fuera de rango: " + indice);
        }
    }

    @Override
    public int contar(Predicate<T> criterio) {
    int c = 0;
    for (T elem : elementos) {
        if (criterio.test(elem)) {
            c++;
        }
    }
    return c;
    }

    @Override
    public List<T> obtenerTodos() {
    return new ArrayList<>(elementos);
}


    public List<T> filtrar(Predicate<? super T> criterio) {
        Objects.requireNonNull(criterio, "El criterio no puede ser nulo.");
        List<T> filtrados = new ArrayList<>();
        for (T e : elementos) {
            if (criterio.test(e)) {
                filtrados.add(e);
            }
        }
        return filtrados;
    }

    public void ordenar() {
        Collections.sort(elementos);
    }
    
    public void ordenar(Comparator<? super T> comparador) {
        Objects.requireNonNull(comparador, "El comparador no puede ser nulo.");
        elementos.sort(comparador);
    }


    public int tamanio() {
        return elementos.size();
    }

    public boolean estaVacio() {
        return elementos.isEmpty();
    }

    public void paraCadaElemento(Consumer<? super T> accion) {
        Objects.requireNonNull(accion, "La acción no puede ser nula.");
        elementos.forEach(accion);
    }

  
    public void guardarEnBinario(String path) throws IOException {
        Objects.requireNonNull(path, "La ruta no puede ser nula.");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            // Escribo el tamaño y luego cada elemento
            oos.writeInt(elementos.size());
            for (T e : elementos) {
                oos.writeObject(e);
            }
        }
    }

    @SuppressWarnings("unchecked")

    public void cargarDesdeBinario(String path) throws IOException, ClassNotFoundException {
        Objects.requireNonNull(path, "La ruta no puede ser nula.");
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
            int size = ois.readInt();
            List<T> cargados = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                Object obj = ois.readObject();
                try {
                    cargados.add((T) obj);
                } catch (ClassCastException ex) {
                    throw new IOException("El objeto leído no es del tipo esperado", ex);
                }
            }
            elementos.clear();
            elementos.addAll(cargados);
        }
    }
    
    
 
    public void guardarEnCSV(String path) throws IOException {
        Objects.requireNonNull(path, "La ruta no puede ser nula.");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            String header = (elementos.isEmpty() ? null : obtenerEncabezado(elementos.get(0)));
            if (header != null) {
                bw.write(header);
                bw.newLine();
            }
            for (T e : elementos) {
                bw.write(e.toCSV());
                bw.newLine();
            }
        }
    }

    private String obtenerEncabezado(T ejemplo) {
        try {
            java.lang.reflect.Method m = ejemplo.getClass().getMethod("csvHeader");
            return (String) m.invoke(null);
        } catch (NoSuchMethodException ignored) {
            return null;
        } catch (Exception ex) {
            return null;
        }
    }

    
    public void cargarDesdeCSV(String path, Function<String, ? extends T> mapper) throws IOException {
        Objects.requireNonNull(path, "La ruta no puede ser nula.");
        Objects.requireNonNull(mapper, "La función de mapeo no puede ser nula");
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String linea;
            // Lee primera línea (encabezado) y descartarla
            br.readLine();
            List<T> cargados = new ArrayList<>();
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) {
                    continue;
                }
                cargados.add(mapper.apply(linea));
            }
            elementos.clear();
            elementos.addAll(cargados);
        }

    /*
    @Override
    public void guardarEnJSON(String ruta) throws Exception {
        // Pretty printing JSON. Si T es DispositivoDomotico, construimos objetos con campos.
        try (PrintWriter pw = new PrintWriter(new FileWriter(ruta))) {
            pw.println("[");
            for (int i = 0; i < lista.size(); i++) {
                T t = lista.get(i);
                String jsonObj = toJsonObject(t);
                pw.println(jsonObj + (i < lista.size() - 1 ? "," : ""));
            }
            pw.println("]");
        }
    }

    // Método auxiliar para convertir un objeto a JSON de forma "pretty"
    private String toJsonObject(T t) {
        // Si el objeto es DispositivoDomotico, construimos JSON con sus getters
        if (t instanceof DispositivoDomotico) {
            DispositivoDomotico d = (DispositivoDomotico) t;
            StringBuilder sb = new StringBuilder();
            sb.append("  {");
            sb.append("\"codigo\": ").append(d.getCodigo()).append(", ");
            sb.append("\"nombreModelo\": ").append(escapeJson(d.getNombreModelo())).append(", ");
            sb.append("\"categoria\": ").append(escapeJson(d.getCategoria().name())).append(", ");
            sb.append("\"consumoWatts\": ").append(d.getConsumoWatts()).append(", ");
            sb.append("\"anioFabricacion\": ").append(d.getAnioFabricacion());
            sb.append("}");
            return sb.toString();
        } else {
            // Fallback: intentar usar toCSV y convertir a JSON con campos genéricos
            String csv = null;
            try {
                java.lang.reflect.Method m = t.getClass().getMethod("toCSV");
                Object r = m.invoke(t);
                csv = r == null ? "" : r.toString();
            } catch (Exception ex) {
                csv = t.toString();
            }
            // crear objeto simple con una propiedad "value"
            return "  {\"value\": " + escapeJson(csv) + "}";
        }
    }

    private String escapeJson(String s) {
        if (s == null) return "null";
        String esc = s.replace("\\", "\\\\").replace("\"", "\\\"");
        return "\"" + esc + "\"";
    }
    */
    }
}
