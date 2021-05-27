package collection;

import java.io.Serializable;
import java.util.Comparator;

public class IdComparator implements Comparator<Flat>, Serializable {
    @Override
    public int compare(Flat o1, Flat o2) {
        return o1.getId() - o2.getId();
    }
}
