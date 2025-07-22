import java.util.*;

class GlobalVariable{
    static Map<Integer, Double> itemPrices = Map.of(
            1, 10.0,
            2, 25.0,
            3, 60.0
    );
}

class User{
    String userId;
    String name;
    String email;

    User(String id, String name){
        this.userId = id;
        this.name = name;
    }
}

class Cart{
    Set<Integer> items;
    Map<Integer, Integer> itemsToQty;

    Cart(){
        this.items = new HashSet<>();
        this.itemsToQty = new HashMap<>();
    }


    void addItem(int itemId, int quantity) throws Exception{
        if(!GlobalVariable.itemPrices.containsKey(itemId)){
            throw new IllegalArgumentException("InValid Item id: " + itemId);
        }
        items.add(itemId);
        itemsToQty.put(itemId, itemsToQty.getOrDefault(itemId, 0) + quantity);
    }

    void removeItem(int itemId){
        items.remove(itemId);
        itemsToQty.remove(itemId);
    }

    double getCartTotal(){
        double rawTotal = 0;
        double tax = 0;
        for(Map.Entry<Integer, Integer> entry: itemsToQty.entrySet()){

            double unitPrice = GlobalVariable.itemPrices.get(entry.getKey());
            double itemTotal = unitPrice * entry.getValue();

            rawTotal += itemTotal;
        }

        System.out.println(" Raw Total " + rawTotal);
        return rawTotal;

    };

}


class OrderManagementSystem {

    Map<String, Cart> userToCart;

    public OrderManagementSystem(){
        this.userToCart = new HashMap<>();
    }

    void addItemToCart(String userId, int itemId, int quantity) throws Exception {
        Cart usersCart = userToCart.getOrDefault(userId, new Cart());
        usersCart.addItem(itemId, quantity);
        userToCart.put(userId, usersCart);
    };
    void removeItemFromCart(String userId, int itemId){
        if(!userToCart.containsKey(userId)){
            System.out.println(" User Id not present !");
            return;
        }
        Cart usersCart = userToCart.get(userId);
        usersCart.removeItem(itemId);

    };
    double calculateTotal(String userId){
        if(!userToCart.containsKey(userId)){
            System.out.println(" User Id not present !");
            return -1.0;
        }
        Cart usersCart = userToCart.get(userId);
        return usersCart.getCartTotal();
    };
    double checkout(String userId){

        if(!userToCart.containsKey(userId)){
            System.out.println(" User Id not present !");
            return -1.0;
        }
        Cart usersCart = userToCart.get(userId);
        double rawTotal = usersCart.getCartTotal();
        double discount = 0;
        if(rawTotal > 100){
            discount = 0.05 * rawTotal;
        }
        rawTotal -= discount;
        double tax = 0.1 * rawTotal;
        System.out.println("Tax: " + tax);

        return rawTotal + tax;
    };
}



public class Driver {
    public static void main(String[] args) throws Exception {


        OrderManagementSystem system = new OrderManagementSystem();
        User user1 = new User("user1", "User1");
        User user2 = new User("user2", "User2");
        User user3 = new User("user3", "User3");

        // Test Case 1: Valid cart operations
        system.addItemToCart("user1", 1, 3); // 3 * 10 = 30
        system.addItemToCart("user1", 2, 2); // 2 * 25 = 50
        System.out.println(system.calculateTotal("user1")); // Output: 80.0

        // Test Case 2: Apply discount and tax
        system.addItemToCart("user1", 3, 1); // +60 => total becomes 140
        System.out.println(system.checkout("user1")); // Output: (140 * 0.95) * 1.10 = 146.3

        // Test Case 3: Remove item and recalculate
        system.addItemToCart("user2", 2, 1);
        system.removeItemFromCart("user2", 2);
        System.out.println(system.calculateTotal("user2")); // Output: 0.0

        // Test Case 4: Invalid item ID
        try {
            system.addItemToCart("user3", 99, 1); // Invalid
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage()); // Output: Invalid item ID: 99
        }
    }
}
