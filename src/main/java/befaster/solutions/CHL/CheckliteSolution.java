package befaster.solutions.CHL;

import java.math.BigDecimal;
import java.util.*;

public class CheckliteSolution {

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
    public CheckliteSolution() {
        List<ItemPrice> prices = new ArrayList<>();
        Offer offerA = new Offer(3, BigDecimal.valueOf(130));
        ItemPrice priceA = new ItemPrice("A", BigDecimal.valueOf(50), Collections.singletonList(offerA));
        prices.add(priceA);
        priceMap.put("A", priceA);

        Offer offerB = new Offer(2, BigDecimal.valueOf(45));
        ItemPrice priceB = new ItemPrice("B", BigDecimal.valueOf(30), Collections.singletonList(offerB));
        prices.add(priceB);
        priceMap.put("B", priceB);

        ItemPrice priceC = new ItemPrice("C", BigDecimal.valueOf(20), Collections.emptyList());
        prices.add(priceC);
        priceMap.put("C", priceC);

        ItemPrice priceD = new ItemPrice("D", BigDecimal.valueOf(15), Collections.emptyList());
        prices.add(priceD);
        priceMap.put("D", priceD);
    }

    public Integer checklite(String skus) {
        String[] itemQuantityArray = skus.split("[, ]+");
        int total = 0;
        for (String itemQuantity : itemQuantityArray) {
            String sku = itemQuantity.substring(itemQuantity.length() - 1);
            int quantity;

            if(itemQuantity.length() == 1) {
                quantity = 1;
            } else {
                try {
                    quantity = Integer.valueOf(itemQuantity.substring(0, itemQuantity.length() - 1));
                } catch (NumberFormatException e) {
                    return -1;
                }
            }

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
        private int quantity;
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



