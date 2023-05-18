package com.example.mytelegrambot.handlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class ImageHandler extends AbstractMessageHandler {

	public ImageHandler(TelegramBot telegramBot) {
		super(telegramBot);
	}

	@Override
	public boolean appliesTo(Update update) {
		return update.message().photo() != null && update.message().photo().length > 0;
	}

	@Override
	public void handleUpdate(Update update) {
		SendMessage sendMessage = new SendMessage(update.message().chat().id(), "Вы прислали картинку (но я её пока не увижу:)) Главное работает!)");
		telegramBot.execute(sendMessage);
	}
}
