package com.example.mytelegrambot.handlers;

import com.pengrad.telegrambot.model.Update;

public interface TelegramBotHandler {

	boolean appliesTo(Update update);

	void handleUpdate(Update update);
}
