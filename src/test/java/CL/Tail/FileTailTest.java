package CL.Tail;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

class FileTailTest {

    private String requiredOutput1 = // input1 последние 3 строки
            "которые он периодически получает, и сигналами, которые он периодически посылает другим процессорам.\n"
            + "И, тем не менее, будучи соединёнными в достаточно большую сеть с управляемым взаимодействием,\n"
            + "такие по отдельности простые процессоры вместе способны выполнять довольно сложные задачи.";

    private String requiredOutput2 = // input2 последние 213 символов
            "посылает другим процессорам.\n"
            + "И, тем не менее, будучи соединёнными в достаточно большую сеть с управляемым взаимодействием,\n"
            + "такие по отдельности простые процессоры вместе способны выполнять довольно сложные задачи.";

    private String requiredOutput3 = // input2 и input1 без флагов -c и -n (т.е последние 10 строк из каждого файла)
            "input2.txt\n"
            + "33\n"
            + "44\n"
            + "\n"
            + "Нейронные сети не программируются в привычном смысле этого слова, они обучаются. Возможность\n"
            + "обучения — одно из главных преимуществ нейронных сетей перед традиционными алгоритмами.\n"
            + "Технически обучение заключается в нахождении коэффициентов связей между нейронами. В процессе\n"
            + "обучения нейронная сеть способна выявлять сложные зависимости между входными данными и\n"
            + "выходными, а также выполнять обобщение. Это значит, что в случае успешного обучения сеть\n"
            + "сможет вернуть верный результат на основании данных, которые отсутствовали в обучающей\n"
            + "выборке, а также неполных и/или «зашумленных», частично искажённых данных.\n"
            + "input1.txt\n"
            + "2\n"
            + "3\n"
            + "4\n"
            + "\n"
            + "ИНС представляет собой систему соединённых и взаимодействующих между собой простых процессоров\n"
            + "(искусственных нейронов). Такие процессоры обычно довольно просты (особенно в сравнении с процессорами,\n"
            + "используемыми в персональных компьютерах). Каждый процессор подобной сети имеет дело только с сигналами,\n"
            + "которые он периодически получает, и сигналами, которые он периодически посылает другим процессорам.\n"
            + "И, тем не менее, будучи соединёнными в достаточно большую сеть с управляемым взаимодействием,\n"
            + "такие по отдельности простые процессоры вместе способны выполнять довольно сложные задачи.";



    private String[] testArguments = new String[] {
            "-n 3 -o output.txt input1.txt",
            "-c 213 -o output.txt input1.txt",
            "-o output.txt input2.txt input1.txt" };

    private String[] requiredOutputs = new String[] { requiredOutput1, requiredOutput2, requiredOutput3 };

    @Test
    void test() throws IOException {
        for (int i = 0; i < 3; i++) {
            Main.main(testArguments[i].split(" "));
            assertEquals(
                    requiredOutputs[i],
                    new String(Files.readAllBytes(Paths.get("output.txt"))).replaceAll("\r", ""));
        }
    }

}