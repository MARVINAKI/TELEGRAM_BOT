package com.example.mytelegrambot.listener;

import com.example.mytelegrambot.handlers.TelegramBotHandler;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class TelegramBotUpdatesListener implements UpdatesListener {

	private final List<TelegramBotHandler> handlers;
	private final TelegramBot telegramBot;

	public TelegramBotUpdatesListener(List<TelegramBotHandler> handlers, TelegramBot telegramBot) {
		this.handlers = handlers;
		this.telegramBot = telegramBot;
	}

	@PostConstruct
	public void init() {
		telegramBot.setUpdatesListener(this);
	}

	@Override
	public int process(List<Update> updates) {
		updates.stream().filter(update -> update.message()!=null).forEach(update -> {
			String text = update.message().text();
			if ("/start".equals(text)) {
				SendMessage sendMessage = new SendMessage(update.message().chat().id(), "Привет, " + update.message().from().firstName() + """
					\nДля теста бота пришли любое из списка:
					1. Слово "Эхо"
					2. Любой текст
					3. Картинку
					4. Создай напоминалку по примеру: Задача 18.05.2023 20:00 Привет от бота
					""");
				telegramBot.execute(sendMessage);
			} else {
				handleUpdate(update);
			}
		});
		return UpdatesListener.CONFIRMED_UPDATES_ALL;
	}

	private void handleUpdate(Update update) {
		for (TelegramBotHandler handler : handlers) {
			if (handler.appliesTo(update)) {
				handler.handleUpdate(update);
				break;
			}
		}
	}
}
