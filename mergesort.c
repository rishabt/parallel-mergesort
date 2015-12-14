/* C program for merge sort */
#include<stdlib.h>
#include<stdio.h>
#include<time.h>
 
/* Function to merge the two haves arr[l..m] and arr[m+1..r] of array arr[] */
void merge(int arr[], int l, int m, int r)
{
    int i, j, k;
    int n1 = m - l + 1;
    int n2 =  r - m;
 
    /* create temp arrays */
    int L[n1], R[n2];
 
    /* Copy data to temp arrays L[] and R[] */
    for(i = 0; i < n1; i++)
        L[i] = arr[l + i];
    for(j = 0; j < n2; j++)
        R[j] = arr[m + 1+ j];
 
    /* Merge the temp arrays back into arr[l..r]*/
    i = 0;
    j = 0;
    k = l;
    while (i < n1 && j < n2)
    {
        if (L[i] <= R[j])
        {
            arr[k] = L[i];
            i++;
        }
        else
        {
            arr[k] = R[j];
            j++;
        }
        k++;
    }
 
    /* Copy the remaining elements of L[], if there are any */
    while (i < n1)
    {
        arr[k] = L[i];
        i++;
        k++;
    }
 
    /* Copy the remaining elements of R[], if there are any */
    while (j < n2)
    {
        arr[k] = R[j];
        j++;
        k++;
    }
}


void readArray(char* file_name, long long int size, int arr[])
{
    FILE *myFile;
    myFile = fopen(file_name, "r");

    long long int i;

    for (i = 0; i < size; i++)
    {
        fscanf(myFile, "%d", &arr[i]);
    }
}
 
/* l is for left index and r is right index of the sub-array
  of arr to be sorted */
void mergeSort(int arr[], int l, int r)
{
    if (l < r)
    {
        int m = l+(r-l)/2; //Same as (l+r)/2, but avoids overflow for large l and h
        mergeSort(arr, l, m);
        mergeSort(arr, m+1, r);
        merge(arr, l, m, r);
    }
}
 
 
/* UITLITY FUNCTIONS */
/* Function to print an array */
void printArray(int A[], int size)
{
    FILE *f = fopen("output.data", "wb");

    int n;
    for(n = 0; n < size; n++) 
    {
        fprintf(f,"%d\n",A[n]);
    }

    fclose(f);
}
 
/* Driver program to test above functions */
int main(int argc, char *argv[])
{
    //int arr[] = {12, 11, 13, 5, 6, 7};
    //int arr_size = sizeof(arr)/sizeof(arr[0]);

    char* file_name = "input.txt";
    char* input_size = argv[1];
    char* e;

    long long int size = strtoll(input_size, &e, 0);
    int arr[size];

    readArray(file_name, size, arr);

    clock_t begin, end;
    double time_spent;

    begin = clock();

    mergeSort(arr, 0, size - 1);

    end = clock();
    time_spent = (double)(end - begin) / CLOCKS_PER_SEC; 

    printf("%f\n", time_spent);

    printArray(arr, size);
    return 0;
}
