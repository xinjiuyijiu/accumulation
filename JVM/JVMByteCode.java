import javax.print.PrintException;

public class JVMByteCode {
    private int m;

    public static void testStaticc() {
    }

    public static void main(String[] args) {
        // new
        // dup
        // invokespecial 调用init private super 方法
        // 这些方法都是在编译时被编译器明确知道了调用对象
        // astore_1
        JVMByteCode main = new JVMByteCode();
        // aload1
        // invokevirtual 无法在编译时候确定调用对象，有可能是他的父类方法
        // 如果将inc方法改为private ,则需要使用 invokespecial
        main.inc();
        // invokestatic
        testStaticc();
        // aload_1
        // invokespecial
        main.process();


    }

    private void inc() {
        // iconst_1
        // astore_1
        // 这里是成员方法，默认第一个参数是this!!!
        int a = 1;
        // aload_0
        // astore_2
        JVMByteCode b = this;
        // aload_2
        // astore_3
        JVMByteCode c = b;
        // iconst_5 编译器将2和3相加的结果直接编译得出，所以这里直接入栈5
        // istore_1
        a = 2 + 3;
        // new 在堆内存中为要创建的对象分配内存空间，将地址入栈
        // dup 复制栈顶的地址，入栈
        // invokespecial 调用栈顶对象的构造方法，初始化对象
        // astore 将栈顶的对象引用交给变量d
        JVMByteCode d = new JVMByteCode();
        // aload
        // iconst_0
        // invokespecial
        d.methodParams(0);
        // aload
        // iconst_2
        // iconst_3
        // invokespecial
        d.methodParams2(2, 3);
    }

    // 代码控制流
    private void process() {
        // iconst_0
        // istore_1
        int a = 0;
        // iload_1
        // ifge 如果栈顶数字大于等于0，调到指定位置（else的代码块）
        if (a < 0) {
            // getstatic 获取类的静态成员变量，这里是System中的out对象
            // ldc 将常量池中的字符串入栈
            // invokevirtual
            System.out.println("it is >0");
            // goto 运行到这里，跳出if...else
        } else {
            // getstatic 
            // ldc 
            // invokevirtual
            System.out.println("it is <0");
        }

        // iload_1
        // bipush byte immediately push
        // ificmple
        if (a > 100) {

        }
        // iload_1
        // bipush
        // ificmple 50   如果栈顶前面的数 <=88 ,跳转到第50行，也就是结束while循环
        while (a > 88) {
            // iinc 1 -1 // 变量1的数减1， 第一个参数 1表示第几个变量， -1表示减去1
            a--;
            // getstatic
            // aload_1
            // invokevirtual
            System.out.println(a);
            // goto
        }

        do {
            // iinc 1 -1
            a--;
            // getstatic
            // iload_1
            // invokevirtual
            System.out.println(a);
        }
        // iload_1
        // bipush
        // if_icmpgt 50 如果栈顶前面的数>88 则跳转到第50行，也就是继续循环
        // 注意这里while和do while的判断方式相反：另外do while没有goto的操作
        while (a > 88);
        // iload_1
        // iconst_1
        // if_icmple 83
        // iload_1
        // iconst_2
        // if_icmple 83
        // 多个条件判断，就是连续几次的if比较
        if (a > 1 && a < 2) {
            System.out.println(a);
        }

        // iload_1
        // iconst_3
        // if_icmpgt 如果栈顶前面的数 >3 ，直接进入到if方法中
        // iload_1
        // iconst_4
        // if_icmpge 如果栈顶前面的数>=4，则直接跳到指定行（不进入if），可以发现不同的短路运算符的判断逻辑有区别
        if (a > 3 || a < 4) {
            System.out.println(a);
        }

        // iconst_0
        // istore_2
        // iload_2
        // bipush
        // if_icmpge 如果栈顶前面的元素>= 10 跳出循环
        for (int i = 0; i < 10; i++) {
            // getstatic
            // iload_2
            // invokevirtual
            // iinc 2 1
            // goto 每次的方法体结束后，都会直接跳转到循环开始处，进行i<10的判断
            System.out.println(i);
        }

        // iconst_1
        // newarray 创建一个长度为栈顶元素大小的数组
        // dup      
        // iconst_0
        // bipush 11
        // iastore  将指定值存储到指定索引位置
        // astore_2
        int[] arrays = {11};
        // aload2
        // astore_3
        int[] ars = arrays;
        // aload_3
        // arraylength
        // istore_4
        int length = ars.length;
        // iconst_0
        // istore_5
        int j=0;
        // iload_5
        // iload_4
        // if_icmpge 如果j>=数组长度，结束循环
        for(;j<arrays.length;j++){
            // aload_3
            // iload_5
            // iaload
            // istore_6
            int ar=ars[j];
            // getstatic
            // iload6
            // invokevirtual
            System.out.println(ar);
            // iinc 5,1
            // goto
        }
       
        // 通过上面的代码可以看出，foreach和普通for循环的实现方式并无明显差异
        for(int ar:arrays){
            System.out.println(ar);
        }

        // iconst_3
        // istore
        int d = 3;
        // iload
        // tableswitch{
        //        -1:   case -1 直接跳转到default的代码块
        //         0:   直接跳转到default的代码块
        //         1:   case 1 直接跳转到case1的代码块
        //         ......
        //         default:  
        // }
        // lookupswitch{
       
        // }
        // 如果case的值，最大和最小值相差不超过10，则使用tableswitch，通过索引进行查找，比如case -1,1,3 ，尽管不连续，但会编译成
        // case -1,0,1,2,3,default 这样的形式 ; 如果相差大于10，则使用lookupswitch，通过键值进行查找
        switch(d){
            case 1:
                // getstatic
                // ldc
                // invokevirtual
                System.out.println("case 1");
            // goto
            break;
            case -6:
                System.out.println("case -1");
            break;
            case 2:
                System.out.println("case 2");
            case 4:
                System.out.println("case 3");
                break;
            default:
                System.out.println("default");
        }

        // 代码中没有添加default，但在编译时，编译器自动加上了default的代码块
        switch(d){
            case 100:
                System.out.println("case 100");
            break;
            case 200:
                System.out.println("case 200");
            break;
        }
    }

    // 一个参数的方法
    private void methodParams(int a) {

    }

    // 二个参数的方法
    private void methodParams2(int a, int b) {

    }

    private void op() {
        // iconst_0
        // istore_1
        int a = 0;

        // iinc 1 1
        a++;
        // iinc 1 10
        a += 10;
        // iinc 1 -10
        a -= 10;
        // iload_1
        // bipush
        // iadd
        // istore_1
        // 从上面的代码可以看出来，使用 +=，-=，++，--操作符，会直接通过iinc处理，而通过普通+的方式，则需要通过更多次的入栈出栈操作
        a = a + 10;
        // iload_1
        // iconst_2
        // imul
        // istore_1
        a *= 2;
        // iload_1
        // iconst_2
        // imul
        // istore_1
        // 除了+和-操作，其他的都没有提供类似iinc的便利操作
        a = a * 2;
        // fconst_2
        // fstore_2
        float b = 2f;
        // fload_2
        // fconst_1
        // fadd
        // fstore_2
        // 出了整形使用++外，其他的类型都无法使用iinc
        b++;
        // fload_2
        // fconst_1
        // fadd
        // fstore_2
        b = b + 1;
        // fload_2
        // fconst_1
        // fadd
        // fstore_2
        // float值+整数值，无论使用什么方式，该整数值都是一个float类型的值
        b = b + 0x1;

    }

    private void arrays() {
        // iconst_3 入栈数组的长度
        // newarray 为将要创建的数组对象分配内存空间，并将地址引用入栈
        // dup 拷贝一份栈顶的引用
        // iconst_0 数组索引0
        // bipush 10 索引处的值
        // iastore 将值存入对应索引处
        // dup 继续拷贝栈顶引用，方便继续给数组添加元素
        // iconst_1
        // bipush 20
        // iastore
        // dup
        // iconst_2
        // bipush 30
        // iastore
        // astore_1 
        int[] a = { 10, 20, 30 };

        // iconst_2
        // newarray
        // astore_2
        int[] b = new int[2];
        // iconst_0
        // bipush 50
        // iastore
        b[0] = 50;
        // aload_2
        // arraylength
        // istore_3
        int length = b.length;
    }

    private void cacheException(){
        try{
            // iconst_2
            // iconst_0
            // idiv
            // istore_1
            int i = 2/0;
            // goto 直接跳出异常捕获逻辑
        }
        catch(NumberFormatException e){
            System.err.println(e);
        }
        catch(Exception e){
            // astore_1   为什么会有保存到变量的操作？ try中捕获的异常，传递给变量e吗
            // getstatic
            // aload_1
            // invokevirtual
            System.err.println(e);
        }
        // Exception table  异常表
        // 定义了每种需要捕获的异常： from ... to 表示异常捕获的作用域
        // target表示异常捕获后，作用范围，也就是cache块的位置
        // type 定义了异常的类型
        // from 0 to 4 target  7  type Exception  
        // from 0 to 4 target  18
    }
}
