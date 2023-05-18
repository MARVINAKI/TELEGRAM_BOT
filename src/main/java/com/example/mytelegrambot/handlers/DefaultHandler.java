package com.example.mytelegrambot.handlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(4)
public class DefaultHandler extends AbstractMessageHandler {

	public DefaultHandler(TelegramBot telegramBot) {
		super(telegramBot);
	}

	@Override
	public boolean appliesTo(Update update) {
		return true;
	}

	@Override
	public void handleUpdate(Update update) {
		SendMessage sendMessage = new SendMessage(update.message().chat().id(), "Текст доставлен и обработан! Спасибо!");
		telegramBot.execute(sendMessage);
	}
}
