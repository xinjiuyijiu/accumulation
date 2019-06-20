#include <stdio.h>
#include <string.h>
#include <error.h>
#include <stdarg.h>

#ifndef NAME
#define NAME lc
#endif

#if !defined(NAME)
#define NAME achao
#endif

#define MAX(x, y) ((x) > (y) ? (x) : (y))

#define PRINT(s)                           \
    printf("i print content is " #s "\n"); \
    printf(" is end\n");

enum DAY
{
    MONDAY = 1,
};

int *getPointer(int *a);

void copy()
{
    char s1[6] = "hello";
    char s2[20] = "worldworld";
    strcpy(s1, s2);
    printf("s1 is %s\n", s1);
    printf("s2 is %s\n", s2);
}

struct Person
{
    int year;
    char *name;
};

void s_struct()
{
    struct Person p;
    p.year = 20;
    p.name = "hello";
    printf(" Person year is %d and name is %s \n ", p.year, p.name);
    printf("\n");
    struct Person *j;
    j = &p;
    j->name = "haha";
    printf(" Person year is %d and name is %s \n ", j->year, j->name);
    printf(" Person year is %d and name is %s \n ", p.year, p.name);
    printf("person size is %d \n  year size is %d \n name size is %d \n", sizeof(p), sizeof(p.year), sizeof(p.name));
}

/**
 * 位域
 * 
 * */
struct KongYu
{
    int year : 32;
    int leg : 3;

} _KongYu;

struct KongYu1
{
    char a : 9;
    char b;

} _KongYu1;

/**
 *  位域size的计算：以最大成员数据类型的尺寸来进行对齐：比如char a是9位，最大对其类型为char，也就是是一个字节，也就是a，b各自需要2个字节来进行对齐
 * */
void yu_struct()
{
    _KongYu.year = 3;
    _KongYu.leg = 3;
    printf(" _KongYu year is %d and leg is %d\n ", _KongYu.year, _KongYu.leg);
    printf("_kongyu size is %d\n", sizeof(_KongYu));
    printf("_kongyu1 size is %d\n", sizeof(_KongYu1));
}

union GO_UNION {
    int a;
    float b;
    char *c;
};

// 共用体
void s_union()
{
    union GO_UNION g_union;
    g_union.a = 100;
    printf("g_union a is %d\n", g_union.a);
    printf("g_union a address is %p\n", &g_union.a);
    g_union.b = 20.f;
    printf("g_union b is %f\n", g_union.b);
    printf("g_union b address is %p\n", &g_union.b);
    g_union.c = "hello world";
    printf("g_union c is %s\n", g_union.c);
    printf("g_union c address is %p\n", &g_union.c);
}

typedef struct Student
{
    char *name;
} STU;

#define PI 3.14

// it is error
// #define Yellow struct DING {

// };

// typedefine 定义别名 #define 给数字,表达式等定义别名
void t_typedefine()
{
    typedef int INT;
    INT a = 10;
    STU stu;
    stu.name = "jack";
}

// 输入输出
void inAndOut()
{
    printf("input some thing:");
    char ss;
    ss = getchar();
    //printf("ss is %s\n", ss);
    putchar(ss);
}

// 文件操作
void file_op()
{
    char *filePath = "hello.cpp";
    char *openMode = "a+";
    FILE *f = fopen(filePath, openMode);
    char content[255];
    while (fgets(content, 255, f) != NULL)
    {
        printf("file content is %s\n", content);
    }
    // int result = fputs("dingdingding\n", f);
    // printf("file input result is %d\n", result);
    fclose(f);
}

// 预定义的宏
void pre_define()
{
    printf("cuurent time is %s\n", __TIME__);
    printf("cuurent date is %s\n", __DATE__);
    printf("cuurent file is %s\n", __FILE__);
    printf("cuurent line is %d\n", __LINE__);
}

// 递归
int digui(int n)
{
    if (n == 0)
    {
        return 1;
    }
    else
    {
        return digui(n - 1) * n;
    }
}

// 可变参数
int maxInts(int num, ...)
{
    va_list va;
    va_start(va, num);
    int max;
    for (int i = 0; i < num; i++)
    {
        int cc = va_arg(va, int);
        if (cc > max)
        {
            max = cc;
        }
    }
    va_end(va);
    return max;
}

int main()
{
    printf("today is %d\n", MONDAY);
    int a = 0;
    int *b = NULL;
    if (!b)
    {
        printf("b is null pointer\n");
    }
    else
    {
        printf("b is not null pointer\n");
    }
    // printf("b  is:%p\n", b);  空指针
    // printf(" *b  is:%d\n", *b);
    b = &a;
    if (!b)
    {
        printf("b is null pointer\n");
    }
    else
    {
        printf("b is not null pointer\n");
    }
    printf("a is %d\n", a);            // 数据
    printf("a's address is:%p\n", &a); // 数据所在地址
    printf("b  is:%p\n", b);           // 数据所在地址（指针内容）
    printf(" *b  is:%d\n", *b);        // 指针指向的内容
    printf(" &b  is:%p\n", &b);        // 存储指针地址的地址
    printf(" &*b  is:%p\n", &*b);      // 数据所在地址

    // 指针的运算
    int var[] = {12, 55, 77};
    int *ptr = NULL;
    ptr = var;
    for (int i = 0; i < 3; i++)
    {
        printf("var[%d] address is %p\n ", i, ptr);
        printf("var[%d] value is %d \n", i, *ptr);
        ptr++;
    }

    const char *names[] = {
        "Zara Ali",
        "Hina Ali",
        "Nuha Ali",
        "Sara Ali",
    };
    int i = 0;

    for (i = 0; i < 4; i++)
    {
        printf("Value of names[%d] = %s\n", i, names[i]);
        printf("address of names[%d] = %p\n", i, *names[i]);
    }

    char *dd = "hello";
    printf("dd address %p", dd);
    printf("\n");
    dd++;
    printf(" *dd is %d", *dd);
    printf("\n");

    // int *gg[] = {1,2,3};
    // printf(gg);
    // printf("\n");
    // printf("*gg is %p", *gg);
    int gp = 10;
    printf("getPointer is %p", getPointer(&gp));
    printf("\n");
    copy();

    char uuu[] = "abcd";
    printf("uuu is %s", uuu);
    char *up = uuu;
    printf("\n");
    printf("up is %d", *up);
    printf("\n");
    up++;
    printf("up is %d", *up);

    char lc1[] = "jack";
    char lc2[] = "rose";
    printf("\n");
    printf("lc1 length is %d\n", strlen(lc1));
    printf("lc1 ccompare lc2 is %d\n", strcmp(lc1, lc2));
    printf("the char \'c\' first appear location is %p in lc1\n ", strchr(lc1, 'c'));
    printf("the string \"os\" fist appear location is %p in lc2 \n", strstr(lc2, "os"));
    // no str will return 0 pointer
    printf("the string \"ors\" fist appear location is %p in lc2 \n", strstr(lc2, "ors"));
    s_struct();
    yu_struct();
    s_union();
    //inAndOut();
    //file_op();
    pre_define();
    printf("  3 and 4 max is %d\n", MAX(3, 4));
    PRINT(hahahah);
    printf("digui(5) is %d\n", digui(5));
    printf("the max number in the list is %d\n",maxInts(6,1,4,6,8,0,11));
    return 0;
}

int *getPointer(int *a)
{
    return a;
}
