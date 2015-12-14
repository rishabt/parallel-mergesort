import os, sys
import subprocess
import matplotlib

args = sys.argv
args.remove(sys.argv[0])
file_name = " ".join(args)

i = 100

while (i < 10000001):
#    subprocess.check_output("ruby -e \'a=STDIN.readlines;" + str(i) + ".times do;b=[];9.times do; b << a[rand(a.size)].chomp end; puts b.join(\"\"); end\' < /usr/share/nums > input.txt", shell=True)

    l = []
    for n in range(20):
        num = float(subprocess.check_output(file_name + " " + str(i), shell=True))
	l.append(num)

    avg = reduce(lambda x, y: x + y, l) / len(l)
    print str(i) + " ->  " + str(avg) + "; max -> " + str(max(l)) + "; min -> " + str(min(l)) 

    i = i * 10;
