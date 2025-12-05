
package config;

import java.nio.file.Path;
import java.nio.file.Paths;


public class RutaArchivos {
    static final String BASE = "src/resources/";
    static final String FILE_CSV = "modificaciones.csv";
    static final String FILE_BIN = "modificaciones.dat";

    public static Path getRutaCSV() {
        return Paths.get(BASE, FILE_CSV);
    }

    public static Path getRutaBIN() {
        return Paths.get(BASE, FILE_BIN);
    }

    public static String getRutaCSVString() {
        return getRutaCSV().toString();
    }

    public static String getRutaBINString() {
        return getRutaBIN().toString();
    }
}
