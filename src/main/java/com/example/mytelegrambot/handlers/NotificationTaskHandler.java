package com.example.mytelegrambot.handlers;

import com.example.mytelegrambot.entity.NotificationTask;
import com.example.mytelegrambot.listener.TelegramBotUpdatesListener;
import com.example.mytelegrambot.service.NotificationTaskService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Order(1)
public class NotificationTaskHandler extends AbstractMessageHandler {

	private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

	private final NotificationTaskService notificationTaskService;


	private final Pattern pattern = Pattern.compile(
			"(\\d{1,2}\\.\\d{1,2}\\.\\d{4}\\s+\\d{1,2}:\\d{2})\\s+([a-zA-Z а-яА-Я\\d\\s,.?!;:*]+)"
	);

	private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

	public NotificationTaskHandler(TelegramBot telegramBot, NotificationTaskService notificationTaskService) {
		super(telegramBot);
		this.notificationTaskService = notificationTaskService;
	}

	@Override
	public boolean appliesTo(Update update) {
		String text = update.message().text();
		return text != null && text.toLowerCase().contains("задача");
	}

	@Override
	public void handleUpdate(Update update) {
		String text = update.message().text().substring(6).trim();
		Long chatId = update.message().chat().id();
		Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			LocalDateTime dateTime = parse(matcher.group(1));
			if (Objects.isNull(dateTime)) {
				sendMessage(chatId, "Некорректный формат даты и/или времени, чёрт возьми!");
			} else {
				String textMessage = matcher.group(2);
				NotificationTask notificationTask = new NotificationTask();
				notificationTask.setChatId(chatId);
				notificationTask.setMessage(textMessage);
				notificationTask.setNotificationDateTime(dateTime);
				notificationTaskService.save(notificationTask);
				sendMessage(chatId, "Задача успешно создана и записана в БД! Круто!)");
			}
		} else {
			sendMessage(chatId,
					"Некорректный формат сообщения, тысяча чертей!");
		}
	}

	private void sendMessage(Long chatId, String message) {
		SendMessage sendMessage = new SendMessage(chatId, message);
		SendResponse sendResponse = telegramBot.execute(sendMessage);
		if (!sendResponse.isOk()) {
			logger.error("Error during sending message: {}", sendResponse.description());
		}
	}

	@Nullable
	private LocalDateTime parse(String dateTime) {
		try {
			return LocalDateTime.parse(dateTime, dateTimeFormatter);
		} catch (DateTimeParseException e) {
			return null;
		}
	}
}
