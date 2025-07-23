package AllPairsSumBetweenLR;

import java.util.*;

public class PairSumInRange {

    public static int countPairsInRange(int[] arr, int L, int R) {
        Arrays.sort(arr);
        int n = arr.length;
        int count = 0;

        for (int i = 0; i < n - 1; i++) {
            int low = lowerBound(arr, i + 1, n - 1, L - arr[i]);
            int high = upperBound(arr, i + 1, n - 1, R - arr[i]);
            if (low <= high) count += (high - low + 1);
        }

        return count;
    }

    // Find the first index such that arr[index] >= val
    private static int lowerBound(int[] arr, int start, int end, int val) {
        int res = end + 1;
        while (start <= end) {
            int mid = (start + end) / 2;
            if (arr[mid] >= val) {
                res = mid;
                end = mid - 1;
            } else start = mid + 1;
        }
        return res;
    }

    // Find the last index such that arr[index] <= val
    private static int upperBound(int[] arr, int start, int end, int val) {
        int res = start - 1;
        while (start <= end) {
            int mid = (start + end) / 2;
            if (arr[mid] <= val) {
                res = mid;
                start = mid + 1;
            } else end = mid - 1;
        }
        return res;
    }

    // Example usage
    public static void main(String[] args) {
        int[] arr = {1, 5, 2};
        int L = 4, R = 7;
        System.out.println(countPairsInRange(arr, L, R));  // Output: 7
    }
}
