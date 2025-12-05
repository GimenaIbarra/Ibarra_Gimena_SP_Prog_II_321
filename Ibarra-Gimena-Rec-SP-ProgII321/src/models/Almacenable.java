
package models;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;


public interface Almacenable<T> {
    void agregar(T elemento);

    void eliminarSegun(Predicate<T> criterio);

    List<T> obtenerTodos();

    T buscar(Predicate<T> criterio);

    void ordenar();

    void ordenar(Comparator<? super T> comparador);

    List<T> filtrar(Predicate<? super T> criterio);

    List<T> transformar(Function<T, T> operador);

    int contar(Predicate<T> criterio);

    void guardarEnBinario(String ruta) throws Exception;

    void cargarDesdeBinario(String ruta) throws Exception;

    void guardarEnCSV(String ruta) throws Exception;

    void cargarDesdeCSV(String path, Function<String, ? extends T> mapper) throws IOException;

}
