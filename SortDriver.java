import java.util.*;
public class SortDriver
{
	public static void main(String[] args) {
	    Random gen = new Random();
	    Runtime runtime = Runtime.getRuntime();
	    int size = gen.nextInt(20) + 1;
	    int arr[] = new int[size];
	    for(int i = 0; i < size; i++){
	        arr[i] = gen.nextInt(200);
	    }
        
	   double start = System.nanoTime();
	  //  int temp[size];
		Sort sortedArray = new Sort(arr);
       runtime.gc();
		long before = runtime.totalMemory() - runtime.freeMemory();
		sortedArray.heapSort(arr);
       long after = runtime.totalMemory() - runtime.freeMemory();
        long usedBytes = after - before;
        System.out.println("Memory: " + usedBytes + " bytes");
	double end = System.nanoTime();
	System.out.println("Sort time: " + (end-start)/1000000 + "ms");
	for(int x: arr)
		    System.out.print(x + " ");
	
	}
}
class Sort{
   // int arr[];
    int buffer[];
    Sort(int []temp){
    //   arr = new int [temp.length+1];
        buffer = new int[temp.length];
      /*  for(int i = 1; i < temp.length+1; i++)
            arr[i] = temp[i-1];
        System.out.println(arr.length);*/
    }
    public void insertionSort(int []array){
        insertionSort(array, 0, array.length-1);
    }
    private void insertionSort(int []arr, int start, int end){
        for(int i = start; i <= end; i++){
            for(int j = i+1; j <= end; j++){
                if(arr[i] > arr[j]){
                    int temp = arr[j];
                    arr[j] = arr[i];
                    arr[i] = temp;
                }
            }
        }
    }
    //this is a min heap
    public void heapSort(int []arr){
        buildHeap(arr, ((arr.length-1)/2));
        deleteMin(arr, arr.length-1);
      //  for(int x: buffer)
        //    System.out.print(x + " ");
    }
    public void buildHeapWrapper(int []arr){
        buildHeap(arr, ((arr.length-1)/2));
        	for(int temp: arr){
		    System.out.print(temp + " ");
		}
    }
    public void buildHeap(int []array, int i){
        if(i < 0)
            return;
            percolateDown(array, i, array.length-1);
            buildHeap(array, i-1);
    }
    private void percolateDown(int []array, int i, int lastIndex){
        int right = i*2 + 2;
        int left = i*2 + 1;
        if(left > lastIndex)
            return;
       if(right <= lastIndex && array[i] > array[right]){
            int temp = array[right];
           array[right] = array[i];
           array[i] = temp;
           percolateDown(array, right, lastIndex);
       }
       if(array[i] > array[left]){
            int temp = array[left];
           array[left] = array[i];
           array[i] = temp;
           percolateDown(array, left, lastIndex);
       }
    }
    private void deleteMin(int []array, int lastIndex){
        if(lastIndex == 0)
        {
             buffer[buffer.length-1] = array[0];
             return;
        }
        buffer[buffer.length-lastIndex] = array[0];
        array[0] = array[lastIndex];
        array[lastIndex] = -1000;
        percolateDown(array, 0, lastIndex-1);
        deleteMin(array, lastIndex-1);
    }
    public void mergeSort(int []arr){
        mergeSort(arr, 0, arr.length-1);
    }
    public void mergeSort(int []array, int start, int end){
        int middle = (start+end)/2;
        if(start < end){
            mergeSort(array, start, middle);
            mergeSort(array, middle+1, end);
        }
        merge(array, start, middle, end);
    }
    private void merge(int []array, int start, int mid, int end){
        int temp[] = new int[end-start+1];
        int i = start, j = mid+1, cur =  0;
        while(i <= mid && j <= end){
            if(array[i] <= array[j]){
                temp[cur] = array[i];
                i++;
            }
            else{
                temp[cur] = array[j];
                j++;
            }
            cur++;
        }
        while(i <= mid){
            temp[cur] = array[i];
            cur += 1; i++;
        }
        while(j <= end){
            temp[cur] = array[j];
            cur ++; j++;
        }
        for(i = start; i <= end; i++)
            array[i] = temp[i-start];
    }
    public void quickSort(int []array, boolean cutoff){
        quickSort(array, 0, array.length-1, cutoff);
    }
    public void quickSort(int []array, int left, int right, boolean cutoff){
        
        int size = right-left + 1;
         if(size < 2)
                return;
        if(cutoff && size <=50){
            insertionSort(array, left, right);
        }
        else{
           
        int median = medianof3(array, left, right);
        int partition = partitionArray(array, left, right, median);
        quickSort(array, left, partition - 1, cutoff);
        quickSort(array, partition + 1, right, cutoff);
        }
        
    }
    private int medianof3(int[] array, int left, int right){
        int center = (left+right)/2;
        if(array[left] > array[center])
            swap(array, left, center);
        if(array[right] < array[left])
            swap(array, right, left);
        if(array[right] < array[center])
            swap(array, right, center);
        swap(array, center, right - 1);
        return array[right-1];
        
    }
    private void swap(int []array, int index1, int index2){
        int temp = array[index2];
        array[index2] = array[index1];
        array[index1] = temp;
    }
  /*  private int partitionArray(int []arr, int left, int right, int median){
        int leftPtr = left, int rightPtr = right-1;
    }*/
    private int partitionArray(int []arr, int start, int end, int median){
        int i = start-1, j = end-1;
        while(true){
         while (i < end && arr[++i] < median) ; 
       while ( j > start && arr[--j] > median) ; 
       if(i >= j)
        break;
        else
        swap(arr,i, j);
   /*         if(arr[j] < median)
            swap(arr, ++i, j);
        j++;*/
        }
         swap(arr, i, end - 1);
        return i;
    }
}

