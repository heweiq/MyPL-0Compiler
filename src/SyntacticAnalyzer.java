import com.sun.security.jgss.InquireSecContextPermission;
import javafx.util.Pair;

import java.util.ArrayList;

/*
<A> → <B>
<B> → [<C>][<E>][<F>]<H>
<C> → CONST<D>{ ,<D>}；
<D> → <标识符>=<无符号整数>
<E> → VAR<标识符>{ ,<标识符>}；
<F> → <G><B>；{<F>}
<G> → procedure<标识符>；
<H> → <I>|<R>|<T>|<S>|<U>|<V>|<J>|<空>
<I> → <标识符>:=<L>
<J> → begin<H>{ ；<H>}<end>
<K> → <L><Q><L>|odd<L>
<L> → [+|-]<M>{<O><M>}
<M> → <N>{<P><N>}
<N> → <标识符>|<无符号整数>|(<L>)
<O> → +|-
<P> → *|/
<Q> → =|#|<|<=|>|>=
<R> → if<K>then<H>
<S> → call<标识符>
<T> → while<K>do<H>
<U> → read(<标识符>{ ，<标识符>})
<V> → write(<标识符>{，<标识符>})
 */
public class SyntacticAnalyzer
{
    static private int p;
    static private int lev; //当前的层次
    static private int cnt_B;
    static private String treeString;
    static ArrayList<Pair<Integer,String>> words = new ArrayList<>();
    public static void syntacticAnalyse(String writeFileName, String str)
    {
        String rows[] = str.split("\\r\\n");
        for(int i = 0; i < rows.length; i++)
        {
            String tmpString[] = rows[i].split(",");
            words.add(new Pair<>(Integer.parseInt(tmpString[0].substring(1)),tmpString[1].substring(0,tmpString[1].length()-1)));
        }
        p = 0;
        lev = 0;
        treeString = "";
        A();
        FileHandler.writeToFile(writeFileName,treeString);
    }
    //得到p位置二元式的首项
    private static int theNum() {
        if(p >= words.size()) return -1;
        return words.get(p).getKey();
    }
    //得到p位置二元式的第二项
    private static String theStr() {
        if(p >= words.size()) return "-";
        return words.get(p).getValue();
    }
    //接受一个词，若没有则报错
    private static void read(int x) {
        if(p >= words.size())
        {
            System.out.println("Error in SyntacticAnalyzer!(1)");
            System.exit(0);
        }
        else if(theNum() == x)
        {
            treeString += "[" + theNum() + " " + theStr() + "]";
            ++p;
        }
        else
        {
            System.out.println("[" + theNum() + " " + theStr() + "]");
            System.out.println("Error in SyntacticAnalyzer!(2)");
            System.exit(0);
        }
    }
    //一个范围内接受一个词，若不在范围内则报错
    private static void read(int x, int y) {
        if(p >= words.size())
        {
            System.out.println("Error in SyntacticAnalyzer! 越界");
            System.exit(0);
        }
        else if(theNum() >= x && theNum() <= y)
        {
            treeString += "[" + theNum() + " " + theStr() + "]";
            ++p;
        }
        else
        {
            System.out.println("[" + theNum() + " " + theStr() + "]");
            System.out.println("Error in SyntacticAnalyzer! 错误");
            System.exit(0);
        }
    }
    private static void A() {
        treeString +="A{";
        B();
        treeString +="}";
    }
    private static void B() {
        if(lev >= 3)
        {
            System.out.println("程序嵌套不能超过3层！");
            System.exit(0);
        }
        treeString +="B{";
        if(theNum() == 1)
            C();
        if(theNum() == 2)
            E();
        if(theNum() == 3)
            F();
        H();
        treeString +="}";
    }
    private static void C() {
        treeString +="C{";
        read(1);
        D();
        while(theNum() == 27)
        {
            read(27);
            D();
        }
        read(28);
        treeString +="}";
    }
    private static void D() {
        treeString +="D{";
        read(14);
        read(20);
        read(15);
        treeString +="}";
    }
    private static void E() {
        treeString +="E{";
        read(2);
        read(14);
        while(theNum() == 27)
        {
            read(27);
            read(14);
        }
        read(28);
        treeString +="}";
    }
    private static void F() {
        treeString +="F{";
        G();
        ++lev;B();--lev;
        read(28);
        while (theNum() == 3)
            F();
        treeString +="}";
    }
    private static void G() {
        treeString +="G{";
        read(3);
        read(14);
        read(28);
        treeString +="}";
    }
    private static void H() {
        treeString +="H{";
        switch (theNum())
        {
            case 14: I();break;
            case 7: R();break;
            case 9: T();break;
            case 11: S();break;
            case 12: U();break;
            case 13: V();break;
            case 4: J();break;
        }
        treeString +="}";
    }
    private static void I() {
        treeString +="I{";
        read(14);
        read(26);
        L();
        treeString +="}";
    }
    private static void J() {
        treeString +="J{";
        read(4);
        H();
        while (theNum() == 28)
        {
            read(28);
            H();
        }
        read(5);
        treeString +="}";
    }
    private static void K() {
        treeString +="K{";
        if(theNum() == 6)
        {
            read(6);
            L();
        }
        else
        {
            L();
            Q();
            L();
        }
        treeString +="}";
    }
    private static void L() {
        treeString +="L{";
        if(theNum() == 16)
            read(16);
        else if(theNum() == 17)
            read(17);
        M();
        while (theNum() == 16 || theNum() == 17)
        {
            O();
            M();
        }
        treeString +="}";
    }
    private static void M() {
        treeString +="M{";
        N();
        while (theNum() == 18 || theNum() == 19)
        {
            P();
            N();
        }
        treeString +="}";
    }
    private static void N() {
        treeString +="N{";
        if(theNum() == 14)
            read(14);
        else if(theNum() == 15)
            read(15);
        else
        {
            read(29);
            L();
            read(30);
        }
        treeString +="}";
    }
    private static void O() {
        treeString +="O{";
        read(16,17);
        treeString +="}";
    }
    private static void P() {
        treeString +="P{";
        read(18,19);
        treeString +="}";
    }
    private static void Q() {
        treeString +="Q{";
        read(20,25);
        treeString +="}";
    }
    private static void R() {
        treeString +="R{";
        read(7);
        K();
        read(8);
        H();
        treeString +="}";
    }
    private static void S() {
        treeString +="S{";
        read(11);
        read(14);
        treeString +="}";
    }
    private static void T() {
        treeString +="T{";
        read(9);
        K();
        read(10);
        H();
        treeString +="}";
    }
    private static void U() {
        treeString +="U{";
        read(12);
        read(29);
        read(14);
        while(theNum() == 27)
        {
            read(27);
            read(14);
        }
        read(30);
        treeString +="}";
    }
    private static void V() {
        treeString +="V{";
        read(13);
        read(29);
        read(14);
        while(theNum() == 27)
        {
            read(27);
            read(14);
        }
        read(30);
        treeString +="}";
    }
}