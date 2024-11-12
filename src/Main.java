import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Запускать тесты по отдельности, т.к. вывод в
        // консоль от каждого теста выполняется параллельно(всё будет смешано)

        // тест на количество мест < количество человек
        test(6, 2, 12);
        // тест на количество мест = количество человек
        //test(6, 3, 6);
        // тест на количество мест > количество человек
        //test(6, 3, 7);
    }


    /**
     * Тестовый сценарий
     *
     * @param countPlaces - количество мест в зале
     * @param countTerminals - количество кассовых аппаратов
     * @param countPersons - количество посетителей
     */
    static void test(int countPlaces, int countTerminals, int countPersons) {
        // инициализируем все параметры
        CentralComputer cc = new CentralComputer(countPlaces);

        List<Terminal> terminals = new ArrayList<>();
        for (int i = 0; i < countTerminals; i++) {
            terminals.add(new Terminal(cc));
        }

        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < countPersons; i++) {
            persons.add(new Person(terminals, i + 1));
        }

        // запускаем центральный компьютер
        cc.start();

        // запускаем пользователей
        for(Person p: persons){
            p.start();
        }

        // Если CentralComputer жив и все persons забронировали места, значит надо завершать бронирование
        boolean flag = true;
        while(cc.isAlive() && flag){
            flag = false;
            for(Person p: persons){
                if (p.isAlive()){
                    flag = true;
                }
            }
        }
        if (cc.isAlive()){
            cc.interrupt();
            System.out.println("Бронирование мест закончено");
        }
    }
}