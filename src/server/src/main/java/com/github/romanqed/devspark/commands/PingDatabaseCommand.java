package com.github.romanqed.devspark.commands;

import com.github.romanqed.devspark.javalin.CliCommand;
import com.github.romanqed.devspark.javalin.TextCommand;

@CliCommand("ping-db")
public final class PingDatabaseCommand implements TextCommand {

    @Override
    public void execute(String[] arguments) {

    }
}
