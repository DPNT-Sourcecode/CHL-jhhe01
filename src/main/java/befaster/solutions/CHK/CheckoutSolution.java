package befaster.solutions.CHK;

import java.math.BigDecimal;
import java.util.*;

public class CheckoutSolution {
    
    private Map<String, ItemPrice> priceMap = new HashMap<>();

    /*
    +------+-------+----------------+
    | Item | Price | Special offers |
    +------+-------+----------------+
    | A    | 50    | 3A for 130     |
    | B    | 30    | 2B for 45      |
    | C    | 20    |                |
    | D    | 15    |                |
    +------+-------+----------------+
     */
    public CheckoutSolution() {
        Offer offerA = new Offer(3, BigDecimal.valueOf(130));
        ItemPrice priceA = new ItemPrice("A", BigDecimal.valueOf(50), Collections.singletonList(offerA));
        priceMap.put("A", priceA);

        Offer offerB = new Offer(2, BigDecimal.valueOf(45));
        ItemPrice priceB = new ItemPrice("B", BigDecimal.valueOf(30), Collections.singletonList(offerB));
        priceMap.put("B", priceB);

        ItemPrice priceC = new ItemPrice("C", BigDecimal.valueOf(20), Collections.emptyList());
        priceMap.put("C", priceC);

        ItemPrice priceD = new ItemPrice("D", BigDecimal.valueOf(15), Collections.emptyList());
        priceMap.put("D", priceD);
    }

    public Integer checkout(String skus) {
        if(skus == null) {
            return -1;
        } else if(skus.trim().isEmpty()) {
            return 0;
        }

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

        int total = 0;
        for (Map.Entry<Character, Integer> itemQuantity : input.entrySet()) {
            String sku = itemQuantity.getKey().toString();
            int quantity = itemQuantity.getValue();

            ItemPrice itemPrice = priceMap.get(sku);

            if(itemPrice == null) {
                return -1;
            }

            if(itemPrice.offers.isEmpty()) {
                total += itemPrice.price.multiply(BigDecimal.valueOf(quantity)).intValue();
            } else {
                List<Offer> offers = itemPrice.offers;
                boolean hasMatchingOffer = false;
                for (Offer offer: offers) {
                    if(quantity >= offer.quantity) {
                        // Handle the case when quantity is greater than the offer quantity, use normal price for excess
                        int priceForIncludedInOffer = BigDecimal.valueOf(quantity / offer.quantity)
                                .multiply(offer.price).intValue();
                        int priceForRemainder = BigDecimal.valueOf(quantity % offer.quantity)
                                .multiply(itemPrice.price).intValue();
                        total += priceForIncludedInOffer;
                        total += priceForRemainder;
                        hasMatchingOffer = true;
                    }
                }
                if(!hasMatchingOffer) {
                    total += itemPrice.price.multiply(BigDecimal.valueOf(quantity)).intValue();
                }
            }

        }
        return total;
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

        Offer(int quantity, BigDecimal price) {
            this.quantity = quantity;
            this.price = price;
        }
    }
}
