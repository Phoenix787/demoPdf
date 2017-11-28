package functions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class Function {

    public static void main(String[] args) {
        List<Integer> values = new ArrayList<>();
        values.addAll(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));

        values.forEach(v-> System.out.println(v + " "));

        IFuncTest ifunc = v -> v > 5;

//        for (Integer value : values) {
//            if (ifunc.test(value)) {
//                System.out.print(value +  " ");
//            }
//        }

        values.stream()
                .filter(ifunc::test)
                .forEach(v-> System.out.print(v + " "));

        //с использованием встроенного функционального интерфейса Predicate

        values.stream().filter(v-> v > 3).forEach(v-> System.out.print(v + " "));

        Predicate<Integer> predicate = v -> v > 5;
        values.removeIf(predicate);
        values.forEach(v-> System.out.print(v + " "));

        LocalDate date = LocalDate.of(2017, 11, 14);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        System.out.println(date.format(formatter));
    }
}

@FunctionalInterface
interface IFuncTest{
    boolean test(Integer value);
}
