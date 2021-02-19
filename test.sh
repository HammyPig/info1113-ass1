#! /usr/bin/env sh
echo "##########################"
echo "### Running e2e tests! ###"
echo "##########################"
echo ""
count=0
passed=0

javac Library.java

for folder in `ls -d tests/*/ | sort -V`; do
    name=`basename "$folder"`

    echo "Running test: $name..."
    
    desc=`<tests/$name/$name.txt`
    input=tests/$name/$name.in
    expected=tests/$name/$name.out

    if java Library < $input | diff -w - $expected; then
        passed=$((passed+1))
        echo "Test $name: ($desc) Passed."
        echo ""
    else
        echo "Test $name: ($desc) Failed!"
        echo "---------------------------"
        echo "Unexpected Output:"
        echo ""
        java Library < $input
        echo ""
    fi

    count=$((count+1))
done

echo "Finished running $count tests!"
echo "Tests passed: $passed/$count"