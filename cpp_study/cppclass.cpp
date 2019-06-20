#include <iostream>
#include <cstring>
#include <exception>
#include <pthread.h>
#include <vector>

// 表示在下一个using namespace时(也就是重新定义命名空间时)，这期间的所有代码默认命名空间为std
// 如果不嫌麻烦，可以不用using namespace std，在需要调用的地方使用std::来指定命名空间，stupid！！！
using namespace std;

// 命名空间
namespace achao
{
void printf(const char *value)
{
    cout << "it is achao\'s printf method:" << value << endl;
}
// 命名空间是可以嵌套的
namespace achao_child
{
void printf(const char *value)
{
    cout << "it is achao_child\'s printf method:" << value << endl;
}
} // namespace achao_child
} // namespace achao

// 不连续的命名空间：该命名空间已经存在，再次定义，会在该空间中进行增加元素
namespace achao
{
void hehe()
{
    cout << "it is achao\'s hehe method" << endl;
}
} // namespace achao

class Person
{

    // 除了自己和友元函数，外部无法访问private
private:
    int idCard;
    // 在private的基础上还能够在派生类中访问protected
protected:
    // 静态成员变量，被多个对象共同享用
    // 静态成员函数，通过范围解析运算符直接访问
    static int gender;

public:
    string name;
    int age;
    // 在类的内部定义成员函数，也可以使用范围解析运算符::在外部来定义成员函数
    void printPerson();

    // const表示在该函数中，隐含的this指针为const指针，无法修改成员变量
    // noexcept表示该方法不会抛出异常,这对应了throw 一个异常，从而优化了函数的效率
    void printPerson1() const noexcept
    {
        cout << "printPerson1111111" << endl;
    }

    // 构造函数
    Person(void)
    {
        cout << "use constructor to create Person object" << endl;
    }

    // 构造函数
    Person(int age);

    // 拷贝构造函数
    Person(const Person &obj)
    {
        this->name = obj.name;
        cout << "use copy constructor" << endl;
    }

    // 析构函数
    ~Person()
    {
        cout << "the Person object will destory" << endl;
    }

    // 友元函数
    friend void printPerson(Person per);

    // 友元类
    friend class Alien;

    // 重载运算符
    bool operator>(const Person &p)
    {
        return this->age > p.age;
    }

    // 虚函数，表明该方法可以被派生类重写
    virtual void printClassName()
    {
        cout << "the class is Person" << endl;
    }
    // 如果声明成纯虚函数，Person类就是一个抽象类，并且不能实例化
    virtual void pureVirtual(){};
};

class Friend
{
};

// 三种权限的继承
// 多继承
// 使用virtual处理多继承冲突的问题，那样Worker对象就只会创建一个Person对象而不是二个
class Man : virtual public Person, protected Friend
{
public:
    void printClassName()
    {
        cout << "the class is Man" << endl;
    }

    void pureVirtual()
    {
    }
};

class Woman : virtual public Person
{
public:
    void printClassName()
    {
        cout << "the class is Woman" << endl;
    }

    void pureVirtual()
    {
    }
};

class Worker : public Man, Woman
{
    void printClassName()
    {
    }

    void pureVirtual()
    {
    }
};

void Person::printPerson()
{
    cout << "printPerson" << endl;
};

Person::Person(int age)
{
    Person::age = age;
    cout << "use outline constructor to create Person object" << endl;
}

// 友元函数和友元类
void printPerson(Person per)
{
    cout << "the person is " << per.name << endl;
};

class Alien
{
public:
    void beYourFriend(Person p)
    {
        // friend class can access private member
        //p.idCard;
        cout << "the alien be " << p.name << "\'s friend!!!" << endl;
    }
};

// 内联函数：编译器会将调用该方法的地方替换成该代码块，提高函数调用效率，适用于行数较少（<10 line），不含有递归，循环的方法
// class 中的所有成员函数都是内联函数
inline void printInline()
{
    cout << "i\'am inline function" << endl;
}

class FanException : public exception
{
public:
    const char *what() const throw()
    {
        return "it is fanException";
    }
};

void throwExp()
{
    FanException e;
    throw e;
}

void *printThread(void *argv)
{
    char *p = (char *)argv;
    cout << "thread params is :" << p << endl;
    //cout << "thread params is :" << *(p + 1) << endl;
    pthread_exit(NULL);
}

void threadOp()
{
    pthread_t thread;
    char *ss[] = {"haha6666666666666666", "jay"};
    cout << "the origin thread params is:" << ss << endl;
    cout << "the origin thread params first is:" << *(ss) << endl;
    char *gg = *ss;
    cout << "the origin thread params charpp is:" << gg << endl;

    int ret = pthread_create(&thread, NULL, printThread, (void *)(*ss));
    if (ret != 0)
    {
        cout << "pthread_create error: error_code=" << ret << endl;
    }
}

// 函数模板
template <typename T>
T &maxValue(T &a, T &b)
{
    return a > b ? a : b;
}

// 类模板
template <class T>
class ResponseData
{
private:
    T content;
    int code;
    string msg;

public:
    void setContent(T &content);
    T getContent() const;
};

// 如果要在外部定义类模板的成员方法，需要声明模板类型
template <class T>
void ResponseData<T>::setContent(T &content)
{
    this->content = content;
}

template <class T>
T ResponseData<T>::getContent() const
{
    return this->content;
}

void stlOp()
{
    vector<int> ve;
    for (int i = 0; i < 10; i++)
    {
        ve.push_back(i + 100);
    }

    //ve.empty()  isEmpty

    cout << "the vector ve is " << ve.at(3) << endl;
    cout << "the vector ve[] is " << ve[0] << endl;
}

int main()
{
    Person p;
    p.name = "haha";
    p.age = 18;
    cout << "the age is :" << p.age << endl
         << "the name is:" << p.name << endl;
    p.printPerson();
    p.printPerson1();

    // 创建pp对象 ，等同于 Person pp=Person(50);
    Person pp(50);
    cout << pp.age << endl;
    // 对象复制，会调用拷贝构造函数
    Person ppCopy = pp;
    cout << "after copy Person the age is " << pp.age << endl;
    // 如果printPerson的参数不使用引用，作为形参，会对Person对象进行拷贝(已经在拷贝构造函数中进行了name成员变量的值传递)
    printPerson(p);
    Alien alien;
    alien.beYourFriend(p);

    cout << "Person pp is bigger than Person p ?" << (pp > p) << endl;

    // (静态链接)将Man类型赋给Person基类，printClassName方法会只想Person的该方法而不是Man的方法
    // （动态链接）如果在Person基类中将printClassName方法设为virtual,则会根据实际类型（内存指向的类型）调用方法，（子类一定要实现该virtual方法）
    // 这和java中的abstract抽象方法非常类似，另外java中的引用类型和实际类型？
    Person *vTest;
    Man m;
    vTest = &m;
    //vTest->printClassName();
    // 无法调用纯虚函数，就像无法调用java中的抽象方法一样
    //vTest->pureVirtual();

    Woman woman;
    woman.printClassName();

    // 异常处理
    try
    {
        throwExp();
    }
    // catch的参数类型取决于抛出的异常类型：如果 throw 的是字符串，那么这里就是 const char*
    catch (FanException &e)
    {
        cout << e.what() << endl;
    }

    achao::printf("heheheh");
    achao::achao_child::printf("ggggggggg");
    achao::hehe();

    threadOp();
    // pthread_exit(NULL);

    int i = 3, j = 4;
    int &a = i;
    int &b = j;
    cout << "the max between 3 and 4 is " << maxValue(a, b) << endl;

    ResponseData<string> sData;
    string ssd = "it is class template";
    string &jss = ssd;
    sData.setContent(jss);
    //sData.setContent(100);
    cout << "the content is " << sData.getContent() << endl;

    string strc = "jack D";
    string *strp = &strc;
    cout << "the string pointer is " << strp << endl;
    cout << "the string content is " << *strp << endl;

    stlOp();

    return 0;
}