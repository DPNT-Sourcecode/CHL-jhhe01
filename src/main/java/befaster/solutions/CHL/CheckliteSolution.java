package befaster.solutions.CHL;

import java.math.BigDecimal;
import java.util.*;

public class CheckliteSolution {

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
+------+-------+------------------------+
     */
    public CheckliteSolution() {
        Offer offerA1 = new Offer(3, BigDecimal.valueOf(130));
        Offer offerA2 = new Offer(5, BigDecimal.valueOf(200));
        List<Offer> offerA = new ArrayList<>();
        // Offers are ordered such that offers with higher value will be in the front of the list
        offerA.add(offerA2);
        offerA.add(offerA1);

        ItemPrice priceA = new ItemPrice("A", BigDecimal.valueOf(50), offerA);
        priceMap.put("A", priceA);

        Offer offerB = new Offer(2, BigDecimal.valueOf(45));
        ItemPrice priceB = new ItemPrice("B", BigDecimal.valueOf(30), Collections.singletonList(offerB));
        priceMap.put("B", priceB);

        ItemPrice priceC = new ItemPrice("C", BigDecimal.valueOf(20), Collections.emptyList());
        priceMap.put("C", priceC);

        ItemPrice priceD = new ItemPrice("D", BigDecimal.valueOf(15), Collections.emptyList());
        priceMap.put("D", priceD);

        Offer offerE = new Offer(2, "B");
        ItemPrice priceE = new ItemPrice("E", BigDecimal.valueOf(40), Collections.singletonList(offerE));
        priceMap.put("E", priceE);
    }

    public Integer checklite(String skus) {
        if(skus == null) {
            return -1;
        } else if(skus.trim().isEmpty()) {
            return 0;
        }

        Map<Character, Integer> input = parseInput(skus);

//        int total = 0;
        Map<String, Integer> cartItemTotals = new HashMap<>();
        Map<String, Integer> freeItemTotals = new HashMap<>();

        for (Map.Entry<Character, Integer> itemQuantity : input.entrySet()) {
            String sku = itemQuantity.getKey().toString();
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

                int remainingQuantity = quantity;

                for (Offer offer: offers) {
                    if(offer.price != null) {
                        if (remainingQuantity >= offer.quantity) {
                            int priceForIncludedInOffer = BigDecimal.valueOf(remainingQuantity / offer.quantity)
                                    .multiply(offer.price).intValue();
                            remainingQuantity = quantity % offer.quantity;
                            updateCartItemTotals(cartItemTotals, sku, priceForIncludedInOffer);
                        }
                    } else {
                        int numberOfItemsFree = quantity / offer.quantity;
                        if(input.containsKey(offer.item.charAt(0))) {
                            int offerItemQuantity = input.get(offer.item.charAt(0));

                            if(offerItemQuantity >= numberOfItemsFree) {
                                updateFreeItemTotals(freeItemTotals, offer.item, numberOfItemsFree);

                            } else {
                                updateFreeItemTotals(freeItemTotals, offer.item, offerItemQuantity);
                            }
                        }
                    }
                }

                if(remainingQuantity > 0) {
                    updateCartItemTotals(cartItemTotals, sku, itemPrice.price.multiply(
                            BigDecimal.valueOf(remainingQuantity)).intValue());
                }
            }

        }

        int total = 0;
        for (String item : cartItemTotals.keySet()) {
            if(freeItemTotals.containsKey(item)) {
                int numOfFree = freeItemTotals.get(item);
                int itemCount = input.get(item.charAt(0));
                int remainingItems = itemCount - numOfFree;
                int itemTotal = 0;
                if(remainingItems > 0) {
                    ItemPrice itemPrice = priceMap.get(item);
                    List<Offer> offers = itemPrice.offers;
                    for (Offer offer: offers) {
                        if(offer.price != null) {
                            if (remainingItems >= offer.quantity) {
                                int priceForIncludedInOffer = BigDecimal.valueOf(remainingItems / offer.quantity)
                                        .multiply(offer.price).intValue();
                                int balanceItems  = remainingItems % offer.quantity;
                                int priceForRest = itemPrice.price.multiply(BigDecimal.valueOf(balanceItems)).intValue();
                                itemTotal = priceForIncludedInOffer + priceForRest;
                            }
                        }
                    }
                }
                total += itemTotal;
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

    private Map<Character, Integer> parseInput(String skus) {
        Map<Character, Integer> input = new HashMap<>();
        for (Character c : skus.toCharArray()) {
            if(input.containsKey(c)) {
                int quantity = input.get(c);
                quantity++;
                input.put(c, quantity);
            } else {
                input.put(c, 1);
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

    class  Offer {
        private int quantity;
        private BigDecimal price;
        private String item;

        Offer(int quantity, BigDecimal price) {
            this.quantity = quantity;
            this.price = price;
        }

        public Offer(int quantity, String item) {
            this.quantity = quantity;
            this.item = item;
        }
    }

}

