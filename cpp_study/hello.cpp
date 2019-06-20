#include <stdio.h>
#include <iostream>
#include <cmath>
#include <string>
#include <ctime>

using namespace std;

int &add(int &a);

void timeOp();

int main(int a, char *args[])
{
    printf("hello worldf\n");
    cout << "hahahaha" << endl;
    int i = 0;
    while (args[i] != NULL)
    {
        printf("the main method param a is %d and param args is %s \n", a, args[i]);
        i++;
    
    }

    // 逗号运算符
    int kk, j;
    j = 10;
    kk = (j++, j + 100, 999 + j);
    cout << "the kk value is:" << kk << endl;
    string stra;
    int *p;

    // 引用
    int yin = 1;
    int &yinyin = yin;
    printf("yinyin is %d\n", yinyin);
    printf("yin address is %p and yinyin address is %p\n", &yin, &yinyin);

    int addV = 88;
    int &addVYin = addV;
    printf("addV address is %p \n", &addVYin);
    // 如果使用引用接受值，就是同一个内存；
    int &addVRes = add(addVYin);
    printf("addV address is %p and addVRes address is %p\n", &addVYin, &addVRes);
    timeOp();
    return 0;
}

/**
 *  使用引用作为形参，能达到和使用指针的同样效果
 *  另外返回类型也可以使用引用来表示
 * */
void swap(int &a, int &b)
{
}

int &add(int &a)
{
    a++;
    return a;
}

void timeOp()
{
    // time_t 是一个秒时间戳
    time_t now = time(NULL);
    cout << "from 1900 to now is:" << now << endl;
    tm *tt = localtime(&now);
    cout << "now year is:" << 1900 + tt->tm_year << endl;
    cout << "now month is:" << 1 + tt->tm_mon << endl;
    cout<< "now day is:"<<tt->tm_mday<<endl;
}
