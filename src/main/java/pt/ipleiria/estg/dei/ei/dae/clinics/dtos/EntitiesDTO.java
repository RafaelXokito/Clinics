package pt.ipleiria.estg.dei.ei.dae.clinics.dtos;

import javax.swing.text.html.parser.Entity;
import java.util.List;

public class EntitiesDTO<T> {

    private String[] columns;
    private List<T> entities;

    public EntitiesDTO(List<T> entities, String ...columns) {
        this.columns = columns;
        this.entities = entities;
    }

    public String[] getColumns() {
        return columns;
    }

    public void setColumns(String[] columns) {
        this.columns = columns;
    }

    public List<T> getEntities() {
        return entities;
    }

    public void setEntities(List<T> entities) {
        this.entities = entities;
    }
}
