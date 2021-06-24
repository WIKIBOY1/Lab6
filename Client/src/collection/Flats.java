package collection;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

/**
 * Класс, с помощью которого осуществляется перевод из xml в java object и обратно, и в котором хранятся данные.
 */
@XmlRootElement(name = "flats")
@XmlAccessorType(XmlAccessType.FIELD)
public class Flats implements Serializable {
    @XmlTransient
    private IdComparator IdComparator = new IdComparator();
    @XmlElement(name = "flat")
    private TreeSet<Flat> flats = new TreeSet<>(IdComparator);

    @XmlTransient
    private java.time.LocalDate creationDate;
    @XmlTransient
    private String creationDateString;

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreationDateString() {
        return creationDateString;
    }

    public TreeSet<Flat> getFlats(){
        return this.flats;
    }

    public Flats(){
        setCreationDate(LocalDate.now());
        setCreationDateString(creationDate.toString());
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDateString(String creationDateString) {
        this.creationDateString = creationDateString;
    }

    public void setFlats(TreeSet<Flat> flats){
        if(flats != null) this.flats = flats;
    }

    @Override
    public String toString() {
        String flatsString = "";
        for (Flat flat : flats){
            flatsString += flat.toString() + "\n" ;
        }
        return flatsString;
    }
}
