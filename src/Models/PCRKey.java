package Models;

import java.util.UUID;

public class PCRKey implements Comparable<PCRKey>{
    private UUID PCRId;

    public PCRKey(UUID PCRId) {
        this.PCRId = PCRId;
    }

    public UUID getPCRId() {
        return PCRId;
    }

    public void setPCRId(UUID PCRId) {
        this.PCRId = PCRId;
    }

    @Override
    public int compareTo(PCRKey o) {
        //TODO vyriesit porovnanie UUID
        return 0;
        /*
        if (PCRId < o.PCRId){
            return 1;
        }else if (PCRId > o.PCRId ){
            return -1;
        }else {
            return 0;
        }*/
    }
}
