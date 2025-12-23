package RogueLike;

import java.util.LinkedList;

public class MessageLog {
    private final int maxSize = 5;
    private LinkedList<String> messages = new LinkedList<>();
    public void add(String message) {
        messages.add(message);
        if(messages.size()>maxSize) {
            messages.removeFirst();
        }
    }
    public void display() {
        System.out.println("--- LOG ---");
        for (String msg : messages) {
            System.out.println(msg);
        }
    }
}