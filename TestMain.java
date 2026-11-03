import java.util.Date;

//【is-a inheritance relationship】Superclass: Card
class Card {
    private String cardId;
    private double balance;
    private boolean isActive;

    public Card(String cardId, double balance) {
        this.cardId = cardId;
        this.balance = balance;
        this.isActive = true;
    }

    // Parent class specification: deduct money, overridden by subclasses, signature unchanged
    public boolean deductMoney(double amount) {
        if (!isActive || amount <= 0 || balance < amount) {
            return false;
        }
        balance -= amount;
        return true;
    }

    public double getBalance() {
        return balance;
    }

    public String getCardId() {
        return cardId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    protected void setBalance(double balance) {
        this.balance = balance;
    }
}

// Subclass: TeacherCard (extends Card)
class TeacherCard extends Card {
    private double discount; // New attribute for discount

    public TeacherCard(String cardId, double balance, double discount) {
        super(cardId, balance);
        this.discount = discount;
    }

    @Override
    public boolean deductMoney(double amount) {
        if (!isActive()) return false;
        double actualCost = amount * discount;
        if (getBalance() >= actualCost) {
            setBalance(getBalance() - actualCost);
            return true;
        }
        return false;
    }

    public double getDiscount() {
        return discount;
    }
}

// Subclass: TemporaryCard (extends Card)
class TemporaryCard extends Card {
    private int remainingTimes;
    private String expireDate;

    public TemporaryCard(String cardId, int remainingTimes, String expireDate) {
        super(cardId, 0);
        this.remainingTimes = remainingTimes;
        this.expireDate = expireDate;
    }

    @Override
    public boolean deductMoney(double amount) {
        if (!isActive()) return false;
        if (remainingTimes <= 0) return false;
        remainingTimes--;
        return true;
    }

    public int getRemainingTimes() {
        return remainingTimes;
    }

    public String getExpireDate() {
        return expireDate;
    }
}

// Subclass: SubsidyCard (extends Card)
class SubsidyCard extends Card {
    private double subsidyBalance;

    public SubsidyCard(String cardId, double balance, double subsidyBalance) {
        super(cardId, balance);
        this.subsidyBalance = subsidyBalance;
    }

    @Override
    public boolean deductMoney(double amount) {
        if (!isActive()) return false;
        if (subsidyBalance >= amount) {
            subsidyBalance -= amount;
            return true;
        } else {
            double cashNeeded = amount - subsidyBalance;
            if (getBalance() >= cashNeeded) {
                setBalance(getBalance() - cashNeeded);
                subsidyBalance = 0;
                return true;
            }
            return false;
        }
    }

    public double getSubsidyBalance() {
        return subsidyBalance;
    }

    public void rechargeSubsidy(double amount) {
        if (amount > 0) subsidyBalance += amount;
    }
}

class Student {
    private String studentId;
    private String name;
    private Card card;

    public Student(String studentId, String name, Card card) {
        this.studentId = studentId;
        this.name = name;
        this.card = card;
    }

    public void showCard() {
        System.out.println(name + " shows card, card id: " + card.getCardId());
    }

    public void showResult(boolean success) {
        if (success) {
            System.out.println(name + ": Payment succeeded!");
        } else {
            System.out.println(name + ": Payment failed!");
        }
    }

    public Card getCard() {
        return card;
    }
}

class CanteenWindow {
    private String windowId;
    private String windowName;

    public CanteenWindow(String windowId, String windowName) {
        this.windowId = windowId;
        this.windowName = windowName;
    }

    public boolean order(Card card, String dishName, double money) {
        System.out.println("\nWindow[" + windowName + "] receives order: " + dishName + ", cost: " + money);
        boolean paySuccess = card.deductMoney(money);
        if (paySuccess) {
            System.out.println("Deduction successful, serve meal!");
        } else {
            System.out.println("Deduction failed, reject order!");
        }
        return paySuccess;
    }
}

public class TestMain {
    public static void main(String[] args) {
        // Test1: Ordinary Card (original case)
        Card ordinaryCard = new Card("C001", 100);
        Student student1 = new Student("S001", "XiaoMing", ordinaryCard);
        CanteenWindow window = new CanteenWindow("W01", "Window 1");
        student1.showCard();
        boolean res1 = ordinaryCard.deductMoney(20);
        student1.showResult(res1);

        // Test2: TeacherCard (polymorphism test)
        TeacherCard teacherCard = new TeacherCard("T001", 200, 0.8);
        Student teacherUser = new Student("T01", "LaoZhang", teacherCard);
        teacherUser.showCard();
        boolean res2 = window.order(teacherCard, "Braised Pork", 50);
        teacherUser.showResult(res2);

        // Test3: TemporaryCard (polymorphism test)
        TemporaryCard tempCard = new TemporaryCard("TEMP01", 3, "2026-12-31");
        Student tempUser = new Student("U01", "Visitor", tempCard);
        tempUser.showCard();
        boolean res3_1 = window.order(tempCard, "Box Meal", 10);
        tempUser.showResult(res3_1);
        boolean res3_2 = window.order(tempCard, "Box Meal", 10);
        tempUser.showResult(res3_2);
        boolean res3_3 = window.order(tempCard, "Box Meal", 10);
        tempUser.showResult(res3_3);
        boolean res3_4 = window.order(tempCard, "Box Meal", 10);
        tempUser.showResult(res3_4);

        // Test4: SubsidyCard (polymorphism test)
        SubsidyCard subCard = new SubsidyCard("SUB01", 50, 30);
        Student subUser = new Student("S02", "XiaoLi", subCard);
        subUser.showCard();
        boolean res4 = window.order(subCard, "Set Meal", 40);
        subUser.showResult(res4);
    }
}