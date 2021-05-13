package collection;

import java.io.Serializable;
import java.util.Comparator;

public class IdComparator implements Comparator<Ticket>, Serializable {
    @Override
    public int compare(Ticket o1, Ticket o2) {
        return o1.getId() - o2.getId();
    }
}
