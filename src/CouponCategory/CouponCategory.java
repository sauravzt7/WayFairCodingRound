package CouponCategory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CouponCategory {
    static class Coupon {
        String couponName;
        Date dateModified;

        Coupon(String couponName) {
            this.couponName = couponName;
            this.dateModified = Date.from(Instant.now());
        }

        Coupon(String couponName, Date dateModified) {
            this.couponName = couponName;
            this.dateModified = dateModified;
        }

        @Override
        public String toString() {
            return "Coupon{" + "couponName='" + couponName + '\'' + ", dateModified=" + dateModified + '}';
        }
    }

    private static final Map<String, List<Coupon>> categoryToCouponsMap = new HashMap<String, List<Coupon>>();
    private static final Map<String, String> categoryToParentMap = new HashMap<String, String>();

    private static final String dateFormat = "yyyy-MM-dd";

    //this is O(1)
    private static String findParent(String category) {

        String parentCategory = categoryToParentMap.get(category);
        if (parentCategory.equals("Null")) return parentCategory;
        // if a category has a coupon it should not move up the hierarchy
        else if (categoryToCouponsMap.containsKey(parentCategory)) return parentCategory;

        categoryToParentMap.put(category, findParent(parentCategory));
        return categoryToParentMap.get(category);
    }

    private static String getCouponForCategory(String category) {

        if (categoryToCouponsMap.containsKey(category)) {
            return categoryToCouponsMap.get(category).getFirst().couponName;
        }

        String categoryWhichHasCoupon = findParent(category);
        if (categoryWhichHasCoupon.equals("Null")) {
            return categoryWhichHasCoupon;
        }
        return categoryToCouponsMap.get(categoryWhichHasCoupon).getFirst().couponName;
    }

    public static void main(String[] args) throws ParseException {

        List<String[]> coupons = List.of(
                new String[]{"CategoryName:Comforter Sets", "CouponName:Comforters Sale", "DateModified:2022-01-01"},
                new String[]{"CategoryName:Comforter Sets", "CouponName:Cozy Comforter Coupon", "DateModified:2018-01-01"},
                new String[]{"CategoryName:Bedding", "CouponName:Best Bedding Bargains", "DateModified:2019-01-01"},
                new String[]{"CategoryName:Bedding", "CouponName:Savings on Bedding", "DateModified:2022-01-01"},
                new String[]{"CategoryName:Bed & Bath", "CouponName:Low price for Bed & Bath", "DateModified:2018-01-01"},
                new String[]{"CategoryName:Bed & Bath", "CouponName:Bed & Bath extravaganza", "DateModified:2019-01-01"}
        );

//        List<String[]> coupons = List.of(
//                new String[]{"CategoryName:Comforter Sets", "CouponName:Comforters Sale"},
//                new String[]{"CategoryName:Bedding", "CouponName:Savings on Bedding"},
//                new String[]{"CategoryName:Bed & Bath", "CouponName:Low price for Bed & Bath"}
//        );

        List<String[]> categories = List.of(
                new String[]{"CategoryName:Comforter Sets", "CategoryParentName:Bedding"},
                new String[]{"CategoryName:Bedding", "CategoryParentName:Bed & Bath"},
                new String[]{"CategoryName:Bed & Bath", "CategoryParentName:None"},
                new String[]{"CategoryName:Soap Dispensers", "CategoryParentName:Bathroom Accessories"},
                new String[]{"CategoryName:Bathroom Accessories", "CategoryParentName:Bed & Bath"},
                new String[]{"CategoryName:Toy Organizers", "CategoryParentName:Baby And Kids"},
                new String[]{"CategoryName:Baby And Kids", "CategoryParentName:Null"}
        );

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

        for (String[] coupon : coupons) {
            categoryToCouponsMap.computeIfAbsent(coupon[0].split(":")[1], k -> new ArrayList<>());
            categoryToCouponsMap.get(coupon[0].split(":")[1]).add(new Coupon(coupon[1].split(":")[1], simpleDateFormat.parse(coupon[2].split(":")[1])));
//            categoryToCouponsMap.get(coupon[0].split(":")[1]).add(new Coupon(coupon[1].split(":")[1]));
        }
// sorting to get the most recent coupons every time
        for (Map.Entry<String, List<Coupon>> couponsEntry : categoryToCouponsMap.entrySet()) {
            couponsEntry.getValue().sort((a, b) ->
                    b.dateModified.compareTo(a.dateModified));
        }

        for (String[] category : categories) {
            categoryToParentMap.put(category[0].split(":")[1], category[1].split(":")[1]);
        }

        categoryToCouponsMap.entrySet().forEach(System.out::println);

        System.out.print("Comforter Sets => ");
        System.out.println(getCouponForCategory("Comforter Sets"));
        System.out.print(("Bedding => "));
        System.out.println(getCouponForCategory("Bedding"));
        System.out.print(("Bathroom Accessories => "));
        System.out.println(getCouponForCategory("Bathroom Accessories"));
        System.out.print(("Soap Dispensers => "));
        System.out.println(getCouponForCategory("Soap Dispensers"));
        System.out.print(("Toy Organizers => "));
        System.out.println(getCouponForCategory("Toy Organizers"));
    }
}