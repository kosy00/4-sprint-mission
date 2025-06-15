package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileMessageService implements MessageService {
    public final MessageRepository messageRepository;


    public FileMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void addMessage(Message message) {
        User sender = message.getSender();
        if (sender == null || sender.getUserStatus() != UserStatus.ACTIVE) return;
        messageRepository.save(message);
    }

    @Override
    public List<Message> getAllMessages() {
        return new ArrayList<>(messageRepository.findAll());
    }

    @Override
    public void updateMessage(UUID messageId, String replacedContent) {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);

        if (optionalMessage.isPresent()) {
            Message message = optionalMessage.get();
            message.setMessageContent(replacedContent);
            message.setUpdatedAt(System.currentTimeMillis());
            messageRepository.save(message);
        } else {
            throw new RuntimeException("메세지를 찾을 수 없습니다: " + messageId);
        }


    }

    @Override
    public void replaceSubstringInContent(UUID messageId, String originalContent, String replacedContent) {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
                if (optionalMessage.isPresent()){
                    Message message = optionalMessage.get();
                    String originalMsg = message.getMessageContent();
                    if (!originalMsg.contains(originalContent)) return;
                    String replacedMsg = originalMsg.replaceFirst(originalContent, replacedContent);
                    message.setMessageContent(replacedMsg);
                    message.setUpdatedAt(System.currentTimeMillis());
                    messageRepository.save(message);
                } else {
                    throw new RuntimeException("메세지를 찾을 수 없습니다: " + messageId);
                }
    }

    @Override
    public void deleteMessage(UUID messageId) {
        messageRepository.deleteById(messageId);
    }

    @Override
    public List<Message> findMessagesByChannelName(String channelName) {
        List<Message> result = new ArrayList<>();
        for (Message message :messageRepository.findAll()) {
            if (message.getChannel() != null &&
                    (message.getChannel().getChannelName().equals(channelName))) {
                result.add(message);
            }
        }
        return result;
    }

    @Override
    public List<Message> findMessagesBySender(User sender) {
        List<Message> result = new ArrayList<>();
        for(Message message : messageRepository.findAll()) {
            if(message.getSender().equals(sender)) {
                result.add(message);
            }
        }
        return result;
    }

    @Override
    public List<Message> findMessagesByKeyword(String keyword) {
        List<Message> result = new ArrayList<>();
        for(Message message : messageRepository.findAll()) {
            if(message.getMessageContent().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(message);
            }
        }
        return result;
    }
}
