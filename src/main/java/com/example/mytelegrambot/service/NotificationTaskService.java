package com.example.mytelegrambot.service;

import com.example.mytelegrambot.entity.NotificationTask;
import com.example.mytelegrambot.repository.NotificationTaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationTaskService {

	private final NotificationTaskRepository notificationTaskRepository;

	public NotificationTaskService(NotificationTaskRepository notificationTaskRepository) {
		this.notificationTaskRepository = notificationTaskRepository;
	}

	public void save(NotificationTask notificationTask) {
		notificationTaskRepository.save(notificationTask);
	}

	public List<NotificationTask> getAll(LocalDateTime localDateTime) {
		return notificationTaskRepository.findAllByNotificationDateTime(localDateTime);
	}
}
