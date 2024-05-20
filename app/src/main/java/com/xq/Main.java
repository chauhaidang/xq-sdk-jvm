package com.xq;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        MessageModel model = new MessageModel(" Hello David Chau  1");
        PrintService printService = new PrintService();
        printService.print(model);
    }
}