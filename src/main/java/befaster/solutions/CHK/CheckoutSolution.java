package befaster.solutions.CHK;

import befaster.runner.SolutionNotImplementedException;
import befaster.solutions.CHL.CheckliteSolution;

import java.math.BigDecimal;
import java.util.*;

public class CheckoutSolution {
    public Integer checkout(String skus) {
        throw new SolutionNotImplementedException();
    }

    private Map<String, CheckliteSolution.ItemPrice> priceMap = new HashMap<>();

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
        List<CheckliteSolution.ItemPrice> prices = new ArrayList<>();
        CheckliteSolution.Offer offerA = new CheckliteSolution.Offer(3, BigDecimal.valueOf(130));
        CheckliteSolution.ItemPrice priceA = new CheckliteSolution.ItemPrice("A", BigDecimal.valueOf(50), Collections.singletonList(offerA));
        prices.add(priceA);
        priceMap.put("A", priceA);

        CheckliteSolution.Offer offerB = new CheckliteSolution.Offer(2, BigDecimal.valueOf(45));
        CheckliteSolution.ItemPrice priceB = new CheckliteSolution.ItemPrice("B", BigDecimal.valueOf(30), Collections.singletonList(offerB));
        prices.add(priceB);
        priceMap.put("B", priceB);

        CheckliteSolution.ItemPrice priceC = new CheckliteSolution.ItemPrice("C", BigDecimal.valueOf(20), Collections.emptyList());
        prices.add(priceC);
        priceMap.put("C", priceC);

        CheckliteSolution.ItemPrice priceD = new CheckliteSolution.ItemPrice("D", BigDecimal.valueOf(15), Collections.emptyList());
        prices.add(priceD);
        priceMap.put("D", priceD);
    }

    public Integer checklite(String skus) {
        if(skus == null || skus.trim().isEmpty()) {
            return -1;
        }
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

            CheckliteSolution.ItemPrice itemPrice = priceMap.get(sku);

            if(itemPrice == null) {
                return -1;
            }

            if(itemPrice.offers.isEmpty()) {
                total += itemPrice.price.multiply(BigDecimal.valueOf(quantity)).intValue();
            } else {
                List<CheckliteSolution.Offer> offers = itemPrice.offers;
                boolean hasMatchingOffer = false;
                for (CheckliteSolution.Offer offer: offers) {
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
        private int quantity;
        private List<CheckliteSolution.Offer> offers;

        ItemPrice(String sku, BigDecimal price, List<CheckliteSolution.Offer> offers) {
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

