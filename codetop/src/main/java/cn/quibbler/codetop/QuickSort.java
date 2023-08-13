package cn.quibbler.codetop;

public class QuickSort {

    public int[] sortArray(int[] nums) {
        if (nums == null) return nums;

        quickSort(nums);
        return nums;
    }
    public static void quickSort(int[] array){
        quick(array,0,array.length-1);
    }

    private static void quick(int[] array, int left, int right) {
        //递归结束条件:这里代表只有一个根   大于号：有可能没有子树  1  2  3   4  1为基准，pivot-1就越界了
        if(left >= right){
            return;
        }

        int pivot = partition(array, left, right);
        quick(array,left,pivot-1);
        quick(array,pivot+1,right);
    }

    public static int partition(int[] array,int start, int end){
        int i = start;//这里存开始时基准的下标，为了循环结束后，相遇值和基准值交换时能找到基准值的下标
        int key = array[start];//这是基准
        while (start < end){
            while (start < end && array[end] >= key){
                end--;
            }
            while (start < end && array[start] <= key){
                start++;
            }
            swap(array,start,end);
        }
        //到这里s和e相遇，把相遇值和基准值交换
        swap(array,start,i);
        return start;//返回基准下标
    }

    public static void swap(int[] array, int n, int m){
        int temp = array[n];
        array[n] = array[m];
        array[m] = temp;
    }

}
