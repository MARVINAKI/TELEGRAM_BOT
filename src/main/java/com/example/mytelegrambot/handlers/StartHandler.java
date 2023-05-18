package com.example.mytelegrambot.handlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class StartHandler extends AbstractMessageHandler {

	public StartHandler(TelegramBot telegramBot) {
		super(telegramBot);
	}

	@Override
	public boolean appliesTo(Update update) {
		return update.message().text() != null && update.message().text().equals("/start");
	}

	@Override
	public void handleUpdate(Update update) {
		SendMessage sendMessage = new SendMessage(update.message().chat().id(), "Привет " + update.message().from().firstName());
		telegramBot.execute(sendMessage);
	}
}
