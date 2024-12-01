import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.abs;

/**
 * Пользователи - потоки
 * единственная логика которых, выбирать рандомно место, пока они его успешно не забронируют
 * после чего могут уходить и придёт следующий пользователь
 */

public class Person extends Thread {

    private List<Terminal> terminals;
    private int number;

    Person(List<Terminal> terminals, int number){
        this.terminals = terminals;
        this.number = number;
    }

    @Override
    public void run() {
        // выбирает свободный терминал
        int counter = 0;
        Terminal selectedTerminal;
        while (true) {
            selectedTerminal = terminals.get(counter % terminals.size());
            if (selectedTerminal.reserveTerminal()) {
                System.out.println("Посетитель №" + number + " подошёл к терминалу №"+ counter % terminals.size());
                break;
            }
            counter++;
            // задержка между выборами терминалов
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        while(true){
            ArrayList<Integer> unselectedPlaces = new ArrayList<>();

            // просматривает все места
            boolean[] places = selectedTerminal.getPlaces();
            for (int i = 0; i < places.length; i++) {
                if (!places[i]) {
                    unselectedPlaces.add(i);
                }
            }

            // если места закончились, выходим из цикла
            if (unselectedPlaces.isEmpty()){
                System.out.println("Места закончились. Посетитель №" + number + " ушёл");
                selectedTerminal.unreserveTerminal();
                break;
            }
            // выбирает место
            int placeNumber = unselectedPlaces.get(abs((new Random()).nextInt()) % unselectedPlaces.size());

            // если место уже забронировано, нужно пробовать снова
            if (selectedTerminal.reservePlace(placeNumber)) {
                System.out.println("Посетитель №" + number + " забронировал место с №"+ placeNumber);
                break;
            }
        }
    }
}
