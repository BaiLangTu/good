import java.util.ArrayList;
import java.util.List;

public class TestController {

    List<String> numbers;

    public TestController() {
        numbers = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            numbers.add("1");
        }
    }



    public static void main(String[] args) {

        TestController test = new TestController(); // 创建实例
        Thread stringBuilderThread = new Thread(new Runnable() {

            @Override
            public void run() {
                System.out.println("StringBuilder thread start: " + System.currentTimeMillis());
                // 执行 StringBuilder 拼接
                long startTime = System.currentTimeMillis();
                StringBuilder sb = new StringBuilder();
                for (String number : test.numbers) {
                    sb.append(number);
                }

                long endTime = System.currentTimeMillis();
                System.out.println("StringBuilder length: " + sb.length());
                System.out.println("StringBuilder time: " + (endTime - startTime) + "ms");

                System.out.println("StringBuilder thread end: " + System.currentTimeMillis());
            }
        });
        stringBuilderThread.start();

        Thread stringBufferThread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("StringBuffer thread start: " + System.currentTimeMillis());
                // 执行 StringBuffer 拼接
                long startTime = System.currentTimeMillis();
                StringBuffer sbf = new StringBuffer();
                for (String number : test.numbers) {
                    sbf.append(number);
                }
                long endTime = System.currentTimeMillis();
                System.out.println("StringBuffer length: " + sbf.length());
                System.out.println("StringBuffer time: " + (endTime - startTime) + "ms");

                System.out.println("StringBuffer thread end: " + System.currentTimeMillis());
            }
        });
        stringBufferThread.start();


    }
}
