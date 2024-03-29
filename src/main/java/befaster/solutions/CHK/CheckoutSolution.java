package befaster.solutions.CHK;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class CheckoutSolution {

    private Map<String, ItemPrice> priceMap = new HashMap<>();

    /*
+------+-------+------------------------+
| Item | Price | Special offers         |
+------+-------+------------------------+
| A    | 50    | 3A for 130, 5A for 200 |
| B    | 30    | 2B for 45              |
| C    | 20    |                        |
| D    | 15    |                        |
| E    | 40    | 2E get one B free      |
| F    | 10    | 2F get one F free      |
| G    | 20    |                        |
| H    | 10    | 5H for 45, 10H for 80  |
| I    | 35    |                        |
| J    | 60    |                        |
| K    | 80    | 2K for 150             |
| L    | 90    |                        |
| M    | 15    |                        |
| N    | 40    | 3N get one M free      |
| O    | 10    |                        |
| P    | 50    | 5P for 200             |
| Q    | 30    | 3Q for 80              |
| R    | 50    | 3R get one Q free      |
| S    | 30    |                        |
| T    | 20    |                        |
| U    | 40    | 3U get one U free      |
| V    | 50    | 2V for 90, 3V for 130  |
| W    | 20    |                        |
| X    | 90    |                        |
| Y    | 10    |                        |
| Z    | 50    |                        |
+------+-------+------------------------+
     */
    public CheckoutSolution()  {
        final AtomicInteger lineNumber = new AtomicInteger(1);
        try(Stream<String> stream = Files.lines(Paths.get("/Users/prasad/workspace/accelerate_runner/challenges/CHL_R4.txt"))) {
            stream.forEach(line -> {
                if (lineNumber.intValue() >= 12 && lineNumber.intValue() <= 37) {
                    String[] entries = line.split("\\| ");
                    String sku = entries[1].trim();
                    BigDecimal price = BigDecimal.valueOf(Integer.parseInt(entries[2].trim()));

                    List<Offer> offerList = new ArrayList<>();
                    if(entries[3].contains("for") || entries[3].contains("get")) {
                        String[] offers = entries[3].trim().split(",");
                        for (String offer : offers) {
                            offerList.add(parseOffer(offer));
                        }
                    }
                    ItemPrice itemPrice = new ItemPrice(sku, price, offerList);

                    priceMap.put(sku, itemPrice);
                }
                lineNumber.incrementAndGet();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Offer parseOffer(String str) {
        String[] strs = str.trim().split(" ");
        if(str.contains("for")) {
            int quantity = Integer.parseInt(strs[0].substring(0, strs[0].length() - 1));
            BigDecimal price = BigDecimal.valueOf(Integer.parseInt(strs[2].trim()));
            return new Offer(quantity, price);
        } else {
            int quantity = Integer.parseInt(strs[0].substring(0, strs[0].length() - 1));
            if(strs[3].equals(strs[0].substring(strs[0].length() - 1))) {
                quantity++;
            }
            return new Offer(quantity, strs[3]);
        }
    }

    public Integer checkout(String skus) {
        if(skus == null) {
            return -1;
        } else if(skus.trim().isEmpty()) {
            return 0;
        }

        Map<String, Integer> input = parseInput(skus);

        Map<String, Integer> cartItemTotals = new HashMap<>();
        Map<String, Integer> freeItemTotals = new HashMap<>();

        for (Map.Entry<String, Integer> itemQuantity : input.entrySet()) {
            String sku = itemQuantity.getKey();
            int quantity = itemQuantity.getValue();

            ItemPrice itemPrice = priceMap.get(sku);

            if(itemPrice == null) {
                return -1;
            }

            if(itemPrice.offers.isEmpty()) {
                updateCartItemTotals(cartItemTotals, sku, itemPrice.price.multiply(BigDecimal.valueOf(quantity))
                        .intValue());
            } else {
                List<Offer> offers = itemPrice.offers;

                for (Offer offer: offers) {
                    if(offer.price == null) {
                        int numberOfItemsFree = quantity / offer.quantity;
                        if(input.containsKey(offer.item)) {
                            int offerItemQuantity = input.get(offer.item);

                            if(offerItemQuantity >= numberOfItemsFree) {
                                updateFreeItemTotals(freeItemTotals, offer.item, numberOfItemsFree);

                            } else {
                                updateFreeItemTotals(freeItemTotals, offer.item, offerItemQuantity);
                            }
                        }
                    }
                }

                updateCartItemTotals(cartItemTotals, sku, priceWithOffers(itemPrice, quantity));
            }

        }

        int total = 0;
        for (String item : cartItemTotals.keySet()) {
            if(freeItemTotals.containsKey(item)) {
                int numOfFree = freeItemTotals.get(item);
                int itemCount = input.get(item);
                int remainingItems = itemCount - numOfFree;
                total += priceWithOffers(priceMap.get(item), remainingItems);
            } else {
                total += cartItemTotals.get(item);
            }
        }
        return total;
    }

    private void updateCartItemTotals(Map<String, Integer> cartItemTotals, String sku, int total) {
        if(cartItemTotals.containsKey(sku)) {
            cartItemTotals.put(sku, cartItemTotals.get(sku) + total);
        } else {
            cartItemTotals.put(sku, total);
        }
    }

    private void updateFreeItemTotals(Map<String, Integer> freeItemTotals, String sku, int total) {
        if(freeItemTotals.containsKey(sku)) {
            freeItemTotals.put(sku, freeItemTotals.get(sku) + total);
        } else {
            freeItemTotals.put(sku, total);
        }
    }

    private int priceWithOffers(ItemPrice itemPrice, int quantity) {
        List<Offer> offers = itemPrice.offers;

        // Sort offers such that offers with highest quantity is considered first.
        Collections.sort(offers);
        int remainingQuantity = quantity;
        int total = 0;

        for (Offer offer: offers) {
            if(offer.price != null) {
                if (remainingQuantity >= offer.quantity) {
                    total  += BigDecimal.valueOf(remainingQuantity / offer.quantity)
                            .multiply(offer.price).intValue();
                    remainingQuantity = remainingQuantity % offer.quantity;
                }
            }
        }

        if(remainingQuantity > 0) {
            total += itemPrice.price.multiply(
                    BigDecimal.valueOf(remainingQuantity)).intValue();
        }

        return total;
    }

    private Map<String, Integer> parseInput(String skus) {
        Map<String, Integer> input = new HashMap<>();
        for (Character c : skus.toCharArray()) {
            if(input.containsKey(c.toString())) {
                int quantity = input.get(c.toString());
                quantity++;
                input.put(c.toString(), quantity);
            } else {
                input.put(c.toString(), 1);
            }
        }
        return input;
    }

    class ItemPrice {
        private String sku;
        private BigDecimal price;
        private List<Offer> offers;

        ItemPrice(String sku, BigDecimal price, List<Offer> offers) {
            this.sku = sku;
            this.price = price;
            this.offers = offers;
        }

    }

    class  Offer implements Comparable<Offer> {
        private int quantity;
        private BigDecimal price;
        private String item;

        Offer(int quantity, BigDecimal price) {
            this.quantity = quantity;
            this.price = price;
        }

        Offer(int quantity, String item) {
            this.quantity = quantity;
            this.item = item;
        }

        @Override
        public int compareTo(Offer o) {
            if(this.quantity < o.quantity) {
                return 1;
            } else if(this.quantity > o.quantity) {
                return -1;
            }
            return 0;
        }
    }

}

