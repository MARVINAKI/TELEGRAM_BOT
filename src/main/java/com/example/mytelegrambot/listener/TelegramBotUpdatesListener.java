package com.example.mytelegrambot.listener;

import com.example.mytelegrambot.handlers.StartHandler;
import com.example.mytelegrambot.handlers.TelegramBotHandler;
import com.example.mytelegrambot.service.NotificationTaskService;
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

//	private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
//
//	private final Pattern pattern = Pattern.compile(
//			"(\\d{1,2}\\.\\d{1,2}\\.\\d{4}\\s+\\d{1,2}:\\d{2})\\s+([a-zA-Z а-яА-Я\\d\\s,.?!;:*]+)"
//	);
//
//	private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

	private final TelegramBot telegramBot;
//	private final NotificationTaskService notificationTaskService;

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
			Long chatId = update.message().chat().id();
			if ("/start".equals(text)) {
				SendMessage sendMessage = new SendMessage(update.message().chat().id(), "Привет " + update.message().from().firstName() + """
					Для теста бота пришли любое из списка:
					1. Слово "Эхо"
					2. Любой текст
					3. Картинку
					4. Создай напоминалку (пример: "18.05.2023 20:00 Привет от бота")
					""");
				telegramBot.execute(sendMessage);
			} else {
				handleUpdate(update);
			}
		});




//		updates.stream().filter(update -> update.message() != null).forEach(this::handleUpdate);
//		try {
//			updates.stream()
//					.filter(update -> update.message() != null)
//					.forEach(update -> {
//						logger.info("Handles update: {}", update);
//
//						Message message = update.message();
//						Long chatId = message.chat().id();
//						String text = message.text();
//						if (text != null) {
//							Matcher matcher = pattern.matcher(text);
//							notificationTaskService.getAll(parse(matcher.group(1)));
//						}
//						if ("/start".equals(text)) {
//							sendMessage(chatId,
//									"""
//											Привет!
//											Введите дату и время (DD.MM.YYYY HH:MM) и какое-нибудь сообщение
//											""");
//						} else if (text != null) {
//							Matcher matcher = pattern.matcher(text);
//							if (matcher.find()) {
//								LocalDateTime dateTime = parse(matcher.group(1));
//								if (Objects.isNull(dateTime)) {
//									sendMessage(chatId, "Некорректный формат даты и/или времени, чёрт возьми!");
//								} else {
//									String textMessage = matcher.group(2);
//									NotificationTask notificationTask = new NotificationTask();
//									notificationTask.setChatId(chatId);
//									notificationTask.setMessage(textMessage);
//									notificationTask.setNotificationDateTime(dateTime);
//									notificationTaskService.save(notificationTask);
//								}
//							} else {
//								sendMessage(chatId,
//										"Некорректный формат сообщения, тысяча чертей!");
//							}
//						}
//					});
//		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
//		}
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

//	private void sendMessage(Long chatId, String message) {
//		SendMessage sendMessage = new SendMessage(chatId, message);
//		SendResponse sendResponse = telegramBot.execute(sendMessage);
//		if (!sendResponse.isOk()) {
//			logger.error("Error during sending message: {}", sendResponse.description());
//		}
//	}
//
//	@Nullable
//	private LocalDateTime parse(String dateTime) {
//		try {
//			return LocalDateTime.parse(dateTime, dateTimeFormatter);
//		} catch (DateTimeParseException e) {
//			return null;
//		}
//	}
}
