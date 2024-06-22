package ru.netology.logger;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerImpl implements Logger {
    private static LoggerImpl logger;

    private LoggerImpl() {
    }

    @Override
    public void info(String message) {
        log(LogLvl.INFO, message);
    }

    @Override
    public void error(String message) {
        log(LogLvl.ERROR, message);
    }

    private void log(LogLvl lvl, String message){
        String text = "[" + lvl + "] " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")) + " >>> " + message;

        try (FileWriter writer = new FileWriter("file.log", true)) {
            writer.write(text);
            writer.append('\n');
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static LoggerImpl getInstance() {
        if (logger == null) {
            logger = new LoggerImpl();
        }
        return logger;
    }
}
