package collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;

/**
 * Class Venue with fields id, name, capacity and type
 */
@XmlRootElement(name = "house")
@XmlAccessorType(XmlAccessType.FIELD)
public class House implements Serializable {
    @XmlElement(name = "nameHouse")
    private String name; //Поле может быть null

    private Long year; //Значение поля должно быть больше
    private Long numberOfFlatsOnFloor; //Значение поля должно быть больше 0

    public House(String name, Long year, Long numberOfFlatsOnFloor) {
        this.setName(name);
        this.setYear(year);
        this.setNumberOfFlatsOnFloor(numberOfFlatsOnFloor);
    }

    public String getName() {
        return name;
    }

    public Long getNumberOfFlatsOnFloor() {
        return numberOfFlatsOnFloor;
    }

    public Long getYear() {
        return year;
    }

    public House(){}

    public void setName(String name) {
        if (name != null) {
            this.name = name;
        }
        else{
            System.out.println("Имя дома должно иметь знаение");
        }
    }

    public void setNumberOfFlatsOnFloor(Long numberOfFlatsOnFloor) {
        this.numberOfFlatsOnFloor = numberOfFlatsOnFloor;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "House{" +
                "name='" + name + '\'' +
                ", year=" + year +
                ", numberOfFlatsOnFloor=" + numberOfFlatsOnFloor +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        House house = (House) o;
        return name.equals(house.name) &&
                year.equals(house.year) &&
                numberOfFlatsOnFloor.equals(house.numberOfFlatsOnFloor);
    }

    @Override
    public int hashCode() {
        return (int) (year + numberOfFlatsOnFloor);
    }
}
