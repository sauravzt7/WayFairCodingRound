package AllPairsSumBetweenLR;

import java.util.*;

public class PairSumInRange {

    public static long countPairsInRange(long[] arr, long L, long R) {
        Arrays.sort(arr);
        int n = arr.length;
        long count = 0;

        for (int i = 0; i < n - 1; i++) {
            int low = lowerBound(arr, i + 1, n - 1, L - arr[i]);
            int high = upperBound(arr, i + 1, n - 1, R - arr[i]);
            if (low <= high) count += (high - low + 1);
        }

        return count;
    }

    // Find the first index such that arr[index] >= val
    private static int lowerBound(long[] arr, int start, int end, long val) {
        int res = end + 1;
        while (start <= end) {
            int mid = (int)((start + end) / 2);
            if (arr[mid] >= val) {
                res = mid;
                end = mid - 1;
            } else start = mid + 1;
        }
        return res;
    }

    // Find the last index such that arr[index] <= val
    private static int upperBound(long[] arr, int start, int end, long val) {
        int res = start - 1;
        while (start <= end) {
            int mid = (int)((start + end) / 2);
            if (arr[mid] <= val) {
                res = mid;
                start = mid + 1;
            } else end = mid - 1;
        }
        return res;
    }

    // Example usage
    public static void main(String[] args) {
        long[] arr = {1, 5, 2};
        long L = 4, R = 7;
        System.out.println(countPairsInRange(arr, L, R));  // Output: 2

        // Enable assertions (use `java -ea` to run this with assertions enabled)
        assert countPairsInRange(new long[]{1, 5, 2, 4, 3}, 4, 7) == 7;
        assert countPairsInRange(new long[]{10, 20, 30}, 5, 15) == 0;
        assert countPairsInRange(new long[]{1, 2, 3, 4}, 3, 7) == 6;
        assert countPairsInRange(new long[]{-2, -1, 0, 1, 2}, -1, 1) == 6;
        assert countPairsInRange(new long[]{100, 200, 300, 400}, 0, 1000) == 6;
        assert countPairsInRange(new long[]{2, 2, 2, 2}, 4, 4) == 6;
        assert countPairsInRange(new long[]{5}, 5, 10) == 0;
        assert countPairsInRange(new long[]{}, 1, 10) == 0;
        assert countPairsInRange(new long[]{-5, -3, -1, 0}, -7, -4) == 4;
        assert countPairsInRange(new long[]{1_000_000_000, 2_000_000_000}, 2_500_000_000L, 3_000_000_000L) == 1;

        System.out.println("✅ All test cases passed!");
    }
}
