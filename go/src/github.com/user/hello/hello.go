package main

import (
	"os"
	"log"
	"fmt"
	"time"
    	"strconv"
    	"strings"
	"io/ioutil"
	"bufio"
)

func main() {
        nums, err := read("/Users/rishabhtandon/Documents/parallel-mergesort/input.txt", 1000000)
   	if err != nil { panic(err) }

        fmt.Println("Normal Mergesort")
        st1 := time.Now()
        r := Mergesort(nums)
	_ = r
        e1 := time.Now()
        //fmt.Println(r)
        fmt.Println("Time: ", e1.UnixNano() - st1.UnixNano())

	fmt.Println("Mergesort with Goroutines")
        st2 := time.Now()
        resultChan := make(chan []int, 1)
        MergeSortAsync(nums, resultChan)
        k := <- resultChan
	_ = k
        e2 := time.Now()
        //fmt.Println(k)
        fmt.Println("Time: ", e2.UnixNano() - st2.UnixNano())
}

func read(f string, limit int) (nums []int, err error) {
    file, err := os.Open(f)
    if err != nil {
        log.Fatal(err)
    }

    defer file.Close()

    nums = make([]int, 0, limit)
    cnt := 0
    scanner := bufio.NewScanner(file)
    for scanner.Scan() {
        line := scanner.Text()

	n, err := strconv.Atoi(line)
	if err != nil { return nil, err }
        nums = append(nums, n)

        cnt = cnt + 1
        if (cnt == limit) {
            break
        }
    }

    if err := scanner.Err(); err != nil {
        log.Fatal(err)
    }

    return nums, nil
}

func readFile(fname string) (nums []int, err error) {
    b, err := ioutil.ReadFile(fname)
    if err != nil { return nil, err }

    lines := strings.Split(string(b), "\n")
    // Assign cap to avoid resize on every append.
    nums = make([]int, 0, len(lines))

    cnt := 0
    for i, l := range lines {
        // Empty line occurs at the end of the file when we use Split.
        if len(l) == 0 { continue }
        // Atoi better suits the job when we know exactly what we're dealing
        // with. Scanf is the more general option.
	_ = i
        n, err := strconv.Atoi(l)
        if err != nil { return nil, err }
        nums = append(nums, n)
	cnt = cnt + 1

	if (cnt == 5) {
	    break
	}
    }

    return nums, nil
}

func Mergesort(numbers []int) []int {
	l := len(numbers)
	if l <= 1 {
		return numbers
	}

	m := l/2

	sortedLeft := Mergesort(numbers[0:m])
	sortedRight := Mergesort(numbers[m:l])

	return Merge(sortedLeft, sortedRight)
}

func Merge(left []int, right []int) []int {
	leftLength := len(left)
	rightLength := len(right)

	if leftLength == 0 {
		return right
	}
	if rightLength == 0 {
		return left
	}

	result := make([]int, (leftLength+rightLength))

	li := 0
	ri := 0
	resulti := 0
	var rnum, lnum int

	for li < leftLength || ri < rightLength {
		if li < leftLength  && ri < rightLength {
			lnum = left[li]
			rnum = right[ri]

			if lnum <= rnum {
				result[resulti] = lnum
				li++
			}else{
				result[resulti] = rnum
				ri++
			}

		} else if li < leftLength {
			lnum = left[li]
			result[resulti] = lnum
			li++
		} else if ri < rightLength {
			rnum = right[ri]
			result[resulti] = rnum
			ri++
		}

		resulti++
	}

	return result
}

func MergeSortAsync(numbers [] int, resultChan chan []int)  {
	l := len(numbers)
	if l <= 1 {
		resultChan <- numbers
		return
	}

	m := l/2

	lchan := make(chan []int, 1)
	rchan := make(chan []int, 1)

	go MergeSortAsync(numbers[0:m], lchan)
	go MergeSortAsync(numbers[m:l], rchan)
	go MergeAsync(<- lchan, <- rchan, resultChan)
}

func MergeAsync(left []int, right []int, resultChannel chan []int) {
	leftLength := len(left)
	rightLength := len(right)

	if leftLength == 0 {
		resultChannel <- right
		return
	}
	if rightLength == 0 {
		resultChannel <- left
		return
	}

	result := make([]int, (leftLength+rightLength))
	li := 0
	ri := 0
	resulti := 0
	var rnum, lnum int

	for li < leftLength || ri < rightLength {
		if li < leftLength  && ri < rightLength {
			lnum = left[li]
			rnum = right[ri]

			if lnum <= rnum {
				result[resulti] = lnum
				li++
			}else{
				result[resulti] = rnum
				ri++
			}

		} else if li < leftLength {
			lnum = left[li]
			result[resulti] = lnum
			li++
		} else if ri < rightLength {
			rnum = right[ri]
			result[resulti] = rnum
			ri++
		}

		resulti++
	}

	resultChannel <- result
}

func MergesortDemo(){
	lim := 5
	largeArr := make([]int, lim)

	for i := 0; i < lim; i++{
		largeArr[i] = lim - i
	}

	fmt.Println(largeArr)

	fmt.Println("Normal Mergesort")
	st1 := time.Now()
	r := Mergesort(largeArr)
	e1 := time.Now()
	fmt.Println(r)
	fmt.Println("Time: ", e1.UnixNano() - st1.UnixNano())


	fmt.Println("Mergesort with Goroutines")
	st2 := time.Now()
	resultChan := make(chan []int, 1)
	MergeSortAsync(largeArr, resultChan)
	k := <- resultChan
	e2 := time.Now()
	fmt.Println(k)
	fmt.Println("Time: ", e2.UnixNano() - st2.UnixNano())
}
