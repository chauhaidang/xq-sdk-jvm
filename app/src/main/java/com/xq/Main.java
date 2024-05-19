package com.xq;

public class Main {
    public static void main(String[] args) {
        MessageModel model = new MessageModel(" Hello David Chau  1");
        PrintService printService = new PrintService();
        printService.print(model);
    }
}