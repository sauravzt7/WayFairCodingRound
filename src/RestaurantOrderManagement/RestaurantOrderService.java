package RestaurantOrderManagement;


import java.util.*;

enum OrderCategory{
    CHEAP,
    MODERATE,
    EXPENSIVE
}

interface IOrder{
    String getName();
    int getPrice();
    OrderCategory getCategory();
}


interface IOrderSystem {
    void addToCart(IOrder order);
    void removeFromCart(IOrder order);
    int calculateTotalAmount();
    Map<String, Integer> categoryDiscounts();
    Map<String, Integer> cartItems();
}


class Order implements IOrder{
    String name;
    int price;
    OrderCategory category;
    Order(String name, int price){
        this.name = name;
        this.price = price;
        if(price <= 10)this.category = OrderCategory.CHEAP;
        else if(price <= 20)this.category = OrderCategory.MODERATE;
        else this.category = OrderCategory.EXPENSIVE;
    }


    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getPrice() {
        return this.price;
    }

    @Override
    public OrderCategory getCategory() {
        return this.category;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return price == order.price && Objects.equals(name, order.name) && category == order.category;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, category);
    }
}



class OrderSystem implements IOrderSystem{
    private static Map<Integer, List<IOrder>> cartToOrder = new HashMap<>();
    private static Map<String, Integer> orderFreq = new HashMap<>();
    Set<IOrder> orderList = new HashSet<>();

    @Override
    public void addToCart(IOrder order) {
        orderList.add(order);
        orderFreq.put(order.getName(), orderFreq.getOrDefault(order.getName(), 0) + 1);
    }

    @Override
    public void removeFromCart(IOrder order) {
        // find the index where this order belongs and remove that order;

        orderFreq.put(order.getName(), orderFreq.getOrDefault(order.getName(), 0) - 1);
        if(orderFreq.get(order.getName()) == 0) orderList.remove(order);
    }

    @Override
    public int calculateTotalAmount() {
        int total = 0;
        for(IOrder order: orderList){
            int discountedPrice = 0;
            switch(order.getCategory()){
                case CHEAP -> discountedPrice += (int) (0.1 * order.getPrice());
                case MODERATE -> discountedPrice += (int)(0.2 * order.getPrice());
                case EXPENSIVE -> discountedPrice += (int)(0.3 * order.getPrice());
                default -> throw new IllegalArgumentException(" Invalid Category type");
            }
            int times = orderFreq.get(order.getName());
            total += times * (order.getPrice() - discountedPrice);
        }
        return total;
    }

    @Override
    public Map<String, Integer> categoryDiscounts() {
        Map<String, Integer> discountMap = new HashMap<>();
        Map<String, Integer> categoryGroupToDiscount = new HashMap<>();

        for(IOrder order: orderList){
            int discountedPrice = 0;
            switch(order.getCategory()){
                case CHEAP -> discountedPrice += (int) (0.1 * order.getPrice());
                case MODERATE -> discountedPrice += (int)(0.2 * order.getPrice());
                case EXPENSIVE -> discountedPrice += (int)(0.3 * order.getPrice());
                default -> throw new IllegalArgumentException(" Invalid Category type");
            }
            int times = orderFreq.get(order.getName());
            categoryGroupToDiscount.put(order.getCategory().toString(),
                    categoryGroupToDiscount.getOrDefault(order.getCategory().toString(), 0) + times * discountedPrice);
        }

        return categoryGroupToDiscount;
    }

    @Override
    public Map<String, Integer> cartItems() {
        Map<String, Integer> cartItemsMap = new TreeMap<>();

        for(Map.Entry<String, Integer> entrySet : orderFreq.entrySet()){
            cartItemsMap.put(entrySet.getKey(), cartItemsMap.getOrDefault(entrySet.getKey(), 0) + entrySet.getValue());
        }

        return cartItemsMap;
    }
}


public class RestaurantOrderService {
    public static void main(String[] args) {

        OrderSystem system = new OrderSystem();

        system.addToCart(new Order("Order-1", 49));
        system.addToCart(new Order("Order-2", 31));
        system.addToCart(new Order("Order-3", 74));
        system.addToCart(new Order("Order-4", 21));
        system.addToCart(new Order("Order-5", 64));
        system.addToCart(new Order("Order-6", 94));
        system.addToCart(new Order("Order-7", 23));
        system.addToCart(new Order("Order-8", 23));
        system.addToCart(new Order("Order-9", 71));

        System.out.println("Total Amount: " + system.calculateTotalAmount());
        Map<String, Integer> discounts = system.categoryDiscounts();
        for (String category : discounts.keySet()) {
            System.out.println(category + " Category Discount: " + discounts.get(category));
        }
        Map<String, Integer> items = system.cartItems();
        for (String item : items.keySet()) {
            System.out.println(item + " (" + items.get(item) + " items)");
        }
    }
}
