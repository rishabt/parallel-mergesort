import java.util.*;
import java.nio.file.*;
import java.io.*;

public class MergeSort
{
	public static void main(String[] args) throws Exception
	{
		int size = Integer.parseInt(args[0]);
		Integer[] a = new Integer[size];

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

		mergeSort(a);

		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;

		System.out.println(elapsedTime);

		write("output.txt", a);
	}

        public static void write (String filename, Integer[] x) throws IOException{
                BufferedWriter outputWriter = null;
  		outputWriter = new BufferedWriter(new FileWriter(filename));
  		for (int i = 0; i < x.length; i++) {
    			outputWriter.write(Integer.toString(x[i]));
    			outputWriter.newLine();
  		}
  		outputWriter.flush();  
  		outputWriter.close();  
	}

	public static void mergeSort(Comparable [ ] a)
	{
		Comparable[] tmp = new Comparable[a.length];
		mergeSort(a, tmp,  0,  a.length - 1);
	}


	private static void mergeSort(Comparable [ ] a, Comparable [ ] tmp, int left, int right)
	{
		if( left < right )
		{
			int center = (left + right) / 2;
			mergeSort(a, tmp, left, center);
			mergeSort(a, tmp, center + 1, right);
			merge(a, tmp, left, center + 1, right);
		}
	}


    private static void merge(Comparable[ ] a, Comparable[ ] tmp, int left, int right, int rightEnd )
    {
        int leftEnd = right - 1;
        int k = left;
        int num = rightEnd - left + 1;

        while(left <= leftEnd && right <= rightEnd)
            if(a[left].compareTo(a[right]) <= 0)
                tmp[k++] = a[left++];
            else
                tmp[k++] = a[right++];

        while(left <= leftEnd)    // Copy rest of first half
            tmp[k++] = a[left++];

        while(right <= rightEnd)  // Copy rest of right half
            tmp[k++] = a[right++];

        // Copy tmp back
        for(int i = 0; i < num; i++, rightEnd--)
            a[rightEnd] = tmp[rightEnd];
    }
 }
