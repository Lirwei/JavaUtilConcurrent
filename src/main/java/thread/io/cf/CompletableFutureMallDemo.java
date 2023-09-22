package thread.io.cf;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CompletableFutureMallDemo {
    static List<NetMall> list = Arrays.asList(
            new NetMall("jd"),
            new NetMall("dangdang"),
            new NetMall("taobao")
    );

    /**
     * 一家一家的搜索
     *
     * @param list
     * @param productName
     * @return
     */
    public static List<String> getPrice(List<NetMall> list, String productName) {
        return list
                .stream()
                .map(netMall ->
                        String
                                .format(productName + " in %s price is %.2f",
                                        netMall.getNetMallName(),
                                        netMall.calPrice(productName)))
                .collect(Collectors.toList());
    }

    public static List<String> getPriceByCompletableFuture(List<NetMall> list, String productName) {
        return list
                .stream()
                .map(netMall -> CompletableFuture.supplyAsync(() -> String
                        .format(productName + " in %s price is %.2f",
                                netMall.getNetMallName(),
                                netMall.calPrice(productName))))
                .collect(Collectors.toList())
                .stream()
                .map(s -> s.join())
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
//        List<String> prices = getPrice(list, "MySQL");
        List<String> prices = getPriceByCompletableFuture(list, "MySQL");
        for (String price : prices) {
            System.out.println(price);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("cost time: " + (endTime - startTime) + "ms");
    }
}

class NetMall {
    @Getter
    private String netMallName;

    public NetMall(String netMallName) {
        this.netMallName = netMallName;
    }

    public double calPrice(String productName) {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return ThreadLocalRandom.current().nextDouble() * 2 + productName.charAt(0);
    }
}
