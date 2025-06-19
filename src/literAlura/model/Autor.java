package literAlura.model;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;

public class Autor {

    private String name;

    @SerializedName("birth_year")
    private Integer birthYear;

    @SerializedName("death_year")
    private Integer deathYear;

    public Autor(String autorNombre) {
        this.name = autorNombre;
    }

    public Autor() {}

    public String getName() {
        return name;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public Integer getDeathYear() {
        return deathYear;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public void setDeathYear(Integer deathYear) {
        this.deathYear = deathYear;
    }

    /**
     * Devuelve la fecha de nacimiento como LocalDate (1 de enero del año si se conoce)
     */
    public LocalDate getFechaNacimiento() {
        return birthYear != null ? LocalDate.of(birthYear, 1, 1) : null;
    }

    /**
     * Devuelve la fecha de muerte como LocalDate (1 de enero del año si se conoce)
     */
    public LocalDate getFechaMuerte() {
        return deathYear != null ? LocalDate.of(deathYear, 1, 1) : null;
    }

    @Override
    public String toString() {
        return name +
                (birthYear != null ? " (" + birthYear + " - " +
                        (deathYear != null ? deathYear : "…") + ")" : "");
    }
}



