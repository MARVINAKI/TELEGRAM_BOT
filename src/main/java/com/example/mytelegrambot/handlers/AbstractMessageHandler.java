package com.example.mytelegrambot.handlers;

import com.pengrad.telegrambot.TelegramBot;

public abstract class AbstractMessageHandler implements TelegramBotHandler {
	protected TelegramBot telegramBot;

	public AbstractMessageHandler(TelegramBot telegramBot) {
		this.telegramBot = telegramBot;
	}
}
