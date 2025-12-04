
package models;

import java.io.Serializable;
import java.util.Objects;


public class ModificacionDeLorean implements Comparable<ModificacionDeLorean>, CSVSerializable, Serializable {

    private static final long serialVersionUID = 1L;

    private final int id;
    private final String nombre;
    private final String responsable;
    private final TipoModificacion tipo;

    public ModificacionDeLorean(int id, String nombre, String responsable, TipoModificacion tipo) {
        this.id = id;
        this.nombre = Objects.requireNonNull(nombre, "El nombre no puede ser nulo");
        this.responsable = Objects.requireNonNull(responsable, "El responsable no puede ser nulo");
        this.tipo = Objects.requireNonNull(tipo, "El tipo no puede ser nulo");
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getResponsable() {
        return responsable;
    }

    public TipoModificacion getTipo() {
        return tipo;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ModificacionDeLorean other = (ModificacionDeLorean) obj;
        return this.id == other.id;
    }

    @Override
    public String toString() {
        return "ModificacionDeLorean{" + "id=" + id + ", nombre='" + nombre + '\'' + ", responsable='" + responsable + '\'' + ", tipo=" + tipo + '}';
    }

    @Override
    public int compareTo(ModificacionDeLorean o) {
        return Integer.compare(this.id, o.id);
    }

    @Override
    public String toCSV() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(',');
        sb.append(nombre).append(',');
        sb.append(responsable).append(',');
        sb.append(tipo);
        return sb.toString();
    }
    
    public static String csvHeader() {
        return "id,nombre,responsable,tipo";
    }

    public static ModificacionDeLorean fromCSV(String linea) {
        Objects.requireNonNull(linea, "La línea CSV no puede ser nula");
        String[] partes = linea.split(",");
        if (partes.length != 4) {
            throw new IllegalArgumentException("La línea CSV debe tener 4 campos: " + linea);
        }
        try {
            int id = Integer.parseInt(partes[0].trim());
            String nombre = partes[1].trim();
            String responsable = partes[2].trim();
            TipoModificacion tipo = TipoModificacion.valueOf(partes[3].trim());
            return new ModificacionDeLorean(id, nombre, responsable, tipo);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Error al parsear línea CSV: " + linea, ex);
        }
    }
}
