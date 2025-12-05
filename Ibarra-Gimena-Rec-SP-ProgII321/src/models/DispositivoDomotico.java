
package models;

import java.io.Serializable;
import java.util.Objects;

public class DispositivoDomotico implements CSVConvertible, Serializable, Comparable<DispositivoDomotico> {

    private int codigo;
    private String nombreModelo;
    private Categoria categoria;
    private int consumoWatts;
    private int anioFabricacion;

    public DispositivoDomotico(int codigo, String nombreModelo, Categoria categoria, int consumoWatts, int anioFabricacion) {
        this.codigo = codigo;
        this.nombreModelo = nombreModelo;
        this.categoria = categoria;
        this.consumoWatts = consumoWatts;
        this.anioFabricacion = anioFabricacion;
    }

    // getters necesarios para JSON en Inventario
    public int getCodigo() { return codigo; }
    public String getNombreModelo() { return nombreModelo; }
    public Categoria getCategoria() { return categoria; }
    public int getConsumoWatts() { return consumoWatts; }
    public int getAnioFabricacion() { return anioFabricacion; }

    // Orden natural:
    // 1) más moderno primero => anioFabricacion DESC
    // 2) a igual año, menor consumo primero => consumoWatts ASC
    @Override
    public int compareTo(DispositivoDomotico o) {
        if (this.anioFabricacion != o.anioFabricacion) {
            return Integer.compare(o.anioFabricacion, this.anioFabricacion); // descendente
        }
        return Integer.compare(this.consumoWatts, o.consumoWatts); // ascendente
    }
    
    @Override
    public String toString() {
        return "DispositivoDomotico{" + "id=" + codigo + ", nombre='" + nombreModelo + '\'' + ", categoria='" + categoria + '\'' + ", consumoWatts=" + consumoWatts + "anioFabricacion='" + anioFabricacion +'}';
    }

    // CSV: separador coma
    @Override
    public String toCSV() {
        StringBuilder sb = new StringBuilder();
        sb.append(codigo).append(',');
        sb.append(nombreModelo).append(',');
        sb.append(categoria).append(',');
        sb.append(consumoWatts).append(',');
        sb.append(anioFabricacion);
        return sb.toString();
    }

    public static String toHeaderCSV() {
        return "codigo,nombreModelo,categoria,consumoWatts,anioFabricacion";
    }

    public static DispositivoDomotico fromCSV(String linea) {
        Objects.requireNonNull(linea, "La línea CSV no puede ser nula.");
        String[] partes = linea.split(",");
        if (partes.length != 5) {
            throw new IllegalArgumentException("La línea CSV debe tener 4 campos: " + linea);
        }
        try {
            int codigo = Integer.parseInt(partes[0].trim());
            String nombreModelo = partes[1].trim();
            Categoria categoria = Categoria.valueOf(partes[2].trim());
            int consumoWatts = Integer.parseInt(partes[3].trim());
            int anioFabricacion = Integer.parseInt(partes[4].trim());
            return new DispositivoDomotico(codigo, nombreModelo, categoria, consumoWatts, anioFabricacion);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Error al parsear línea CSV: " + linea, ex);
        }
    }

}
