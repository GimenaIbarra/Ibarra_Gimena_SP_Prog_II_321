
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


public class InventarioDeModificacion<T extends CSVSerializable & Comparable<T> & java.io.Serializable> {

    private final List<T> elementos = new ArrayList<>();
    
    public List<T> getElementos() {
        return new ArrayList<>(elementos);
    }

    public void agregar(T elemento) {
        Objects.requireNonNull(elemento, "No se puede agregar un elemento nulo");
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
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        }
    }

    public int tamanio() {
        return elementos.size();
    }

    public boolean estaVacio() {
        return elementos.isEmpty();
    }

    public void paraCadaElemento(Consumer<? super T> accion) {
        Objects.requireNonNull(accion, "La acción no puede ser nula");
        elementos.forEach(accion);
    }

    public List<T> filtrar(Predicate<? super T> criterio) {
        Objects.requireNonNull(criterio, "El criterio no puede ser nulo");
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
        Objects.requireNonNull(comparador, "El comparador no puede ser nulo");
        elementos.sort(comparador);
    }

    public void guardarEnArchivo(String path) throws IOException {
        Objects.requireNonNull(path, "La ruta no puede ser nula");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            // Escribimos el tamaño y luego cada elemento
            oos.writeInt(elementos.size());
            for (T e : elementos) {
                oos.writeObject(e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void cargarDesdeArchivo(String path) throws IOException, ClassNotFoundException {
        Objects.requireNonNull(path, "La ruta no puede ser nula");
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
        Objects.requireNonNull(path, "La ruta no puede ser nula");
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
        Objects.requireNonNull(path, "La ruta no puede ser nula");
        Objects.requireNonNull(mapper, "La función de mapeo no puede ser nula");
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String linea;
            // Leer primera línea (encabezado) y descartarla
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
    }
}