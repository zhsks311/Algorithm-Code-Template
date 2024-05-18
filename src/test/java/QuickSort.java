import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
public class QuickSort {
    private static final String DESCRIPTION
            = """
              퀵소트를 이용해 주어진 값들의 나열에서 값을 정렬.
              pivot : 정렬의 기준으로 사용할 값. 만약 배열의 중앙값을 지정하게 되면 첫 정렬때 pivot은 중앙에 위치하게 됨.
              이 후 divide and conquer.

              pivot 값을 최적화하는 것이 시간복잡도 최적화의 키
              이를 위해 Median of Three 라는 방법이 있다.
              
              최소값, 최대값을 찾을 때도 활용할 수 있음
              
              시간 복잡도 : O(n log n). 최악의 경우 O(n)
              공간 복잡도 : 평균 O(lon n)
              
              """;

    Stream<Arguments> inputArrayMax() {
        return Stream.of(
                Arguments.of(new int[][]{{1, 3}, {-2, 2}}, 1, new int[][]{{1, 3}}),
                Arguments.of(new int[][]{{3, 3}, {5, -1}, {-2, 4}}, 2, new int[][]{{-2, 4}, {5, -1}}),
                Arguments.of(new int[][]{{1, 3}, {-2, 2}, {2, -2}}, 2, new int[][]{{-2, 2}, {1, 3}})
                        );
    }

    Stream<Arguments> inputArrayMin() {
        return Stream.of(
                Arguments.of(new int[][]{{1, 3}, {-2, 2}}, 1, new int[][]{{-2, 2}}),
                Arguments.of(new int[][]{{3, 3}, {5, -1}, {-2, 4}}, 2, new int[][]{{3, 3}, {-2, 4}}),
                Arguments.of(new int[][]{{1, 3}, {-2, 2}, {2, -2}}, 2, new int[][]{{2, -2}, {-2, 2}})
                        );
    }

    @ParameterizedTest
    @MethodSource("inputArrayMax")
    public void testMaximum(int[][] arrays,
                            int k,
                            int[][] answer) {
        var result = runBigger(arrays, k);
        assertThat(result).isEqualTo(answer);
    }

    @ParameterizedTest
    @MethodSource("inputArrayMin")
    public void testMinimum(int[][] arrays,
                            int k,
                            int[][] answer) {
        var result = runSmaller(arrays, k);
        assertThat(result).isEqualTo(answer);
    }

    private int[][] runSmaller(int[][] points,
                              int k) {
        new QuickSortArrayCode().quicksort(points, 0, points.length - 1);
        return Arrays.copyOfRange(points, 0, k);
    }

    private int[][] runBigger(int[][] points,
                             int k) {
        int length = points.length;
        new QuickSortArrayCode().quicksort(points, 0, length - 1);
        return Arrays.copyOfRange(points, length - k, length);
    }

    private static class QuickSortArrayCode {
        // 구현부
        public void quicksort(int[][] points,
                               int low,
                               int high) {
            if (low < high) {
                int pivot = partition(points, low, high);
                quicksort(points, low, pivot - 1);
                quicksort(points, pivot + 1, high);
            }
        }

        private int partition(int[][] points,
                              int low,
                              int high) {
            // pivot 결정
            // pivot 값 계산 ( 필요 시 )
            // low ~ high 루프 돌며 pivot 과 비교.
            // pivot 보다 작으면 무조건 맨 왼쪽부터 채우기
            int[] pivot = points[high];
            int value = calculateValue(pivot);
            int left = low - 1;
            for (int i = low; i < high; ++i) {
                if (calculateValue(points[i]) < value) {
                    ++left;
                    swap(points, i, left);
                }
            }
            ++left;
            swap(points, high, left);

            return left;
        }

        int calculateValue(int[] pivot) {
            // 정렬하려는 값
            return pivot[0] * pivot[0] + pivot[1] * pivot[1];
        }

        void swap(int[][] points,
                  int i,
                  int left) {
            int[] temp = points[i];
            points[i] = points[left];
            points[left] = temp;
        }
    }
}
