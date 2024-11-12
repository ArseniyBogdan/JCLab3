import java.util.Arrays;

public class CentralComputer extends Thread{

    private boolean[] placeSelectedFlagArray;
    private int selectedPlacesCount;

    CentralComputer(int countPlaces) {
        placeSelectedFlagArray = new boolean[countPlaces];
        this.selectedPlacesCount = 0;
    }

    public boolean reservePlace(int placeNumber){
        synchronized (this){
            if(placeSelectedFlagArray[placeNumber]){
                return false;
            }
            else{
                placeSelectedFlagArray[placeNumber] = true;
                selectedPlacesCount++;
                return true;
            }
        }
    }

    public boolean[] getPlaceSelectedFlagArray() {
        // TODO может тоже сделать блокировку на чтение и запись?
        return placeSelectedFlagArray.clone();
    }

    public int getCountSelectedPlaces(){
        return selectedPlacesCount;
    }

    @Override
    public void run() {
        while (getCountSelectedPlaces() != placeSelectedFlagArray.length){
            // задержка между проверками количества забронированных мест
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Бронирование мест закончено");
    }
}
