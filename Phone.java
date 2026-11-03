public class Phone { // public的Class名必须和文件名一致
    protected String number; // protected 可以自己类用，自己的子类也是可用
    // 没写构造方法，生成默认的构造方  public Phone() {  }

    public void call(){
        System.out.println("This is call()");
    }

    public static void main(String[] args) { // Java程序运行 => Java虚拟机调用main方法
        MobilePhone aMobilePhone = new MobilePhone("1234567890");
        //类型名 对象名 = new 创建对象，调用对象的构造方法（参数）
        aMobilePhone.play(); // 对象名.方法名
    }
}

class MobilePhone extends Phone{
    public MobilePhone(String number) { //构造方法 Constructor
        this.number = number; // 重名的变量，小范围的变量优先，大范围的需要用限定符，例如this是Phone继承来的变量
    }

    public void play(){
        System.out.println(this.number);
        call(); // 使用从Phone继承来的方法call
    }
}