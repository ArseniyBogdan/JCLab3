import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Как всё работает
 * У нас есть центральный компьютер, он отвечает за бронирование мест
 * Есть терминал, через который пользователи могут взять и выбрать место
 */

public class Terminal {

    private ReadWriteLock rwlock = new ReentrantReadWriteLock();

    private TerminalState state = TerminalState.FREE;

    private CentralComputer centralComputer;

    Terminal(CentralComputer centralComputer){
        this.centralComputer = centralComputer;
    }

    /**
     * Бронирование терминала в личное
     * пользование человеком
     *
     * @return получилось ли забронировать терминал или нет
     */
    public boolean reserveTerminal(){
        rwlock.writeLock().lock();
        if (state != TerminalState.FREE){
            rwlock.writeLock().unlock();
            return false;
        }
        state = TerminalState.RESERVED;
        rwlock.writeLock().unlock();
        return true;
    }

    /**
     * Получение текущего состояния терминала
     *
     * @return текущее состояние терминала
     */
    public TerminalState getTerminalState(){
        try{
            rwlock.readLock().lock();
            return state;
        }
        finally {
            rwlock.readLock().unlock();
        }
    }

    /**
     * Бронирование места человеком у терминала
     *
     * @param placeNumber - номер места
     * @return забронировано место или нет
     */
    public boolean reservePlace(int placeNumber){
        boolean isSelected = centralComputer.reservePlace(placeNumber);
        if (isSelected){
            state = TerminalState.FREE;
        }
        return isSelected;
    }

    /**
     * Если места закончились, то надо освобододить терминал
     */
    public void unreserveTerminal(){
        state = TerminalState.FREE;
    }

    /**
     * @return список мест
     */
    public boolean[] getPlaces(){
        return centralComputer.getPlaceSelectedFlagArray();
    }
}
