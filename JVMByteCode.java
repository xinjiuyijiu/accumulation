import javax.print.PrintException;

public class JVMByteCode {
    private int m;

    public static void testStaticc() {
    }

    public static void main(String[] args) {
        // new
        // dup
        // invokespecial ����init private super ����
        // ��Щ���������ڱ���ʱ����������ȷ֪���˵��ö���
        // astore_1
        JVMByteCode main = new JVMByteCode();
        // aload1
        // invokevirtual �޷��ڱ���ʱ��ȷ�����ö����п��������ĸ��෽��
        // �����inc������Ϊprivate ,����Ҫʹ�� invokespecial
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
        // �����ǳ�Ա������Ĭ�ϵ�һ��������this!!!
        int a = 1;
        // aload_0
        // astore_2
        JVMByteCode b = this;
        // aload_2
        // astore_3
        JVMByteCode c = b;
        // iconst_5 ��������2��3��ӵĽ��ֱ�ӱ���ó�����������ֱ����ջ5
        // istore_1
        a = 2 + 3;
        // new �ڶ��ڴ���ΪҪ�����Ķ�������ڴ�ռ䣬����ַ��ջ
        // dup ����ջ���ĵ�ַ����ջ
        // invokespecial ����ջ������Ĺ��췽������ʼ������
        // astore ��ջ���Ķ������ý�������d
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

    // ���������
    private void process() {
        // iconst_0
        // istore_1
        int a = 0;
        // iload_1
        // ifge ���ջ�����ִ��ڵ���0������ָ��λ�ã�else�Ĵ���飩
        if (a < 0) {
            // getstatic ��ȡ��ľ�̬��Ա������������System�е�out����
            // ldc ���������е��ַ�����ջ
            // invokevirtual
            System.out.println("it is >0");
            // goto ���е��������if...else
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
        // ificmple 50   ���ջ��ǰ����� <=88 ,��ת����50�У�Ҳ���ǽ���whileѭ��
        while (a > 88) {
            // iinc 1 -1 // ����1������1�� ��һ������ 1��ʾ�ڼ��������� -1��ʾ��ȥ1
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
        // if_icmpgt 50 ���ջ��ǰ�����>88 ����ת����50�У�Ҳ���Ǽ���ѭ��
        // ע������while��do while���жϷ�ʽ�෴������do whileû��goto�Ĳ���
        while (a > 88);
        // iload_1
        // iconst_1
        // if_icmple 83
        // iload_1
        // iconst_2
        // if_icmple 83
        // ��������жϣ������������ε�if�Ƚ�
        if (a > 1 && a < 2) {
            System.out.println(a);
        }

        // iload_1
        // iconst_3
        // if_icmpgt ���ջ��ǰ����� >3 ��ֱ�ӽ��뵽if������
        // iload_1
        // iconst_4
        // if_icmpge ���ջ��ǰ�����>=4����ֱ������ָ���У�������if�������Է��ֲ�ͬ�Ķ�·��������ж��߼�������
        if (a > 3 || a < 4) {
            System.out.println(a);
        }

        // iconst_0
        // istore_2
        // iload_2
        // bipush
        // if_icmpge ���ջ��ǰ���Ԫ��>= 10 ����ѭ��
        for (int i = 0; i < 10; i++) {
            // getstatic
            // iload_2
            // invokevirtual
            // iinc 2 1
            // goto ÿ�εķ���������󣬶���ֱ����ת��ѭ����ʼ��������i<10���ж�
            System.out.println(i);
        }

        // iconst_1
        // newarray ����һ������Ϊջ��Ԫ�ش�С������
        // dup      
        // iconst_0
        // bipush 11
        // iastore  ��ָ��ֵ�洢��ָ������λ��
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
        // if_icmpge ���j>=���鳤�ȣ�����ѭ��
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
       
        // ͨ������Ĵ�����Կ�����foreach����ͨforѭ����ʵ�ַ�ʽ�������Բ���
        for(int ar:arrays){
            System.out.println(ar);
        }

        // iconst_3
        // istore
        int d = 3;
        // iload
        // tableswitch{
        //        -1:   case -1 ֱ����ת��default�Ĵ����
        //         0:   ֱ����ת��default�Ĵ����
        //         1:   case 1 ֱ����ת��case1�Ĵ����
        //         ......
        //         default:  
        // }
        // lookupswitch{
       
        // }
        // ���case��ֵ��������Сֵ������10����ʹ��tableswitch��ͨ���������в��ң�����case -1,1,3 �����ܲ���������������
        // case -1,0,1,2,3,default ��������ʽ ; ���������10����ʹ��lookupswitch��ͨ����ֵ���в���
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

        // ������û�����default�����ڱ���ʱ���������Զ�������default�Ĵ����
        switch(d){
            case 100:
                System.out.println("case 100");
            break;
            case 200:
                System.out.println("case 200");
            break;
        }
    }

    // һ�������ķ���
    private void methodParams(int a) {

    }

    // ���������ķ���
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
        // ������Ĵ�����Կ�������ʹ�� +=��-=��++��--����������ֱ��ͨ��iinc������ͨ����ͨ+�ķ�ʽ������Ҫͨ������ε���ջ��ջ����
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
        // ����+��-�����������Ķ�û���ṩ����iinc�ı�������
        a = a * 2;
        // fconst_2
        // fstore_2
        float b = 2f;
        // fload_2
        // fconst_1
        // fadd
        // fstore_2
        // ��������ʹ��++�⣬���������Ͷ��޷�ʹ��iinc
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
        // floatֵ+����ֵ������ʹ��ʲô��ʽ��������ֵ����һ��float���͵�ֵ
        b = b + 0x1;

    }

    private void arrays() {
        // iconst_3 ��ջ����ĳ���
        // newarray Ϊ��Ҫ�����������������ڴ�ռ䣬������ַ������ջ
        // dup ����һ��ջ��������
        // iconst_0 ��������0
        // bipush 10 ��������ֵ
        // iastore ��ֵ�����Ӧ������
        // dup ��������ջ�����ã�����������������Ԫ��
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
            // goto ֱ�������쳣�����߼�
        }
        catch(NumberFormatException e){
            System.err.println(e);
        }
        catch(Exception e){
            // astore_1   Ϊʲô���б��浽�����Ĳ����� try�в�����쳣�����ݸ�����e��
            // getstatic
            // aload_1
            // invokevirtual
            System.err.println(e);
        }
        // Exception table  �쳣��
        // ������ÿ����Ҫ������쳣�� from ... to ��ʾ�쳣�����������
        // target��ʾ�쳣��������÷�Χ��Ҳ����cache���λ��
        // type �������쳣������
        // from 0 to 4 target  7  type Exception  
        // from 0 to 4 target  18
    }
}