import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;


// Merge Sort Parallel Test...


// All the needed functionality to run simple parallel tests on
//   multiple processors.
// Real (pretty good) results with as little as 20,000 elements:
//   java -Xss2M MergeSort 20000 
// Where -Xss2M tells Java to use 2Mb thread stacks (functional lists
//   are tough without proper tail recursion...)
public class ThreadMergesort{

    // Main Method
    public static void main(String[] args) throws Exception{
    	
    	int size = Integer.parseInt(args[0]);
		int[] a = new int[size];

		BufferedReader br = new BufferedReader(new FileReader("./input.txt"));
		try {
			int i = 0;
   			while (i < size) {
        			String line = br.readLine();
				a[i] = Integer.parseInt(line);
				i++;
    			}
		} finally {
    			br.close();
		}

		long startTime = System.currentTimeMillis();

		parallelMergesort(a, 4);

		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;

		System.out.println((float) elapsedTime/1000);

		/*try {
			write("output.txt", a);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    }
    
    public static void write (String filename, int[] x) throws IOException{
        BufferedWriter outputWriter = null;
        outputWriter = new BufferedWriter(new FileWriter(filename));
        for (int i = 0; i < x.length; i++) {
        	outputWriter.write(Integer.toString(x[i]));
			outputWriter.newLine();
        }
        
		outputWriter.flush();  
		outputWriter.close();  
    }
    
    public static void parallelMergesort(int[] a, int NUM_THREADS)
    {
        if(NUM_THREADS <= 1)
        {
            mergeSort(a);
            return;
        }

        int mid = a.length / 2;

        int[] left = Arrays.copyOfRange(a, 0, mid);
        int[] right = Arrays.copyOfRange(a, mid, a.length);

        Thread leftSorter = mergeSortThread(left, NUM_THREADS);
        Thread rightSorter = mergeSortThread(right, NUM_THREADS);

        leftSorter.start();
        rightSorter.start();

        try {
            leftSorter.join();
            rightSorter.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        merge(left, right, a);
    }

    private static Thread mergeSortThread(int[] a, int NUM_THREADS)
    {
        return new Thread()
        {
            @Override
            public void run()
            {
                parallelMergesort(a, NUM_THREADS / 2);
            }
        };
    }

    public static void mergeSort(int[] a)
    {
        if(a.length <= 1) return;

        int mid = a.length / 2;

        int[] left = Arrays.copyOfRange(a, 0, mid);
        int[] right = Arrays.copyOfRange(a, mid, a.length);

        mergeSort(left);
        mergeSort(right);

        merge(left, right, a);
    }


    private static void merge(int[] a, int[] b, int[] r)
    {
        int i = 0, j = 0, k = 0;
        while(i < a.length && j < b.length)
        {
            if(a[i] < b[j])
                r[k++] = a[i++];
            else
                r[k++] = b[j++];
        }

        while(i < a.length)
            r[k++] = a[i++];

        while(j < b.length)
            r[k++] = b[j++];
    }


}
