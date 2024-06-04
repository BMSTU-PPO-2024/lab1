package com.github.romanqed.devspark.javalin;

import java.io.InputStream;
import java.util.Map;
import java.util.Scanner;

final class CliManagerImpl implements CliManager {
    private Map<String, TextCommand> commands;

    void setCommands(Map<String, TextCommand> commands) {
        this.commands = commands;
    }

    @Override
    public void handle(InputStream input) {
        var scanner = new Scanner(input);
        while (true) {
            System.out.print("> ");
            var raw = scanner.nextLine().strip();
            if ("exit".equalsIgnoreCase(raw)) {
                System.exit(0);
            }
            if ("help".equalsIgnoreCase(raw)) {
                commands.keySet().forEach(System.out::println);
                continue;
            }
            var split = raw.split(" ");
            var name = split[0];
            var command = commands.get(name);
            if (command == null) {
                System.out.println("Command not found");
                continue;
            }
            var arguments = (String[]) null;
            if (split.length > 1) {
                var length = split.length - 1;
                arguments = new String[length];
                System.arraycopy(split, 1, arguments, 0, length);
            } else {
                arguments = new String[0];
            }
            var toPass = arguments;
            try {
                command.execute(toPass);
            } catch (Throwable e) {
                System.out.println("Cannot execute command due to " + e.getMessage());
                System.out.println("Stack trace:");
                e.printStackTrace();
            }
        }
    }
}
