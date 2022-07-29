object ChatService {
    val chats: MutableList<Chat> = mutableListOf()

    fun newChat(user1Id: Int, user2Id:Int): Chat {
        if (user1Id == user2Id) {
            throw RuntimeException("Нельзя сделать чат с самим собой")
        } else if (findChat(user1Id, user2Id) != null) {
            throw RuntimeException("Чат между пользователями $user1Id и $user2Id уже существует")
        } else {
            chats.add(Chat(user1Id,user2Id,if (chats.isEmpty()) 1 else chats.last().id + 1))
            return chats.last()
        }
    }

    fun deleteChat(chatId:Int): Boolean {
        val chat = chats.find { chat: Chat -> chat.id == chatId }
        if (chat != null) {
            chat.clear();
            return chats.remove(chat)
        } else {
            return false
        }
    }

    fun getChatsCount(): Int {
        return chats.filter { chat:Chat -> chat.getMessageCount() > 0 }.size
    }

    fun getUnreadChatsCount(): Int {
        return chats.filter { chat:Chat -> chat.getNewMessageCount() > 0 }.size
    }

    fun findChat(user1Id: Int, user2Id:Int):Chat? {
        return chats.find {
            chat: Chat -> (((chat.user1Id == user1Id) && (chat.user2Id == user2Id)) ||
                           ((chat.user2Id == user1Id) && (chat.user1Id == user2Id)))
        }
    }

    fun newMessage(fromUser: Int, toUser: Int, message: String):Message? {
        var chat = findChat(fromUser,toUser)
        if (chat == null) {
            chat = newChat(fromUser, toUser)
        }
        return chat.addMessage(fromUser, message)
    }

    fun getChats(): ArrayList<Pair <Int, String>> {
        val chatsInfo: ArrayList<Pair <Int, String>> = arrayListOf()
        for (chat in chats) {
            chatsInfo += Pair(chat.id, if (chat.getMessageCount() == 0) " Нет сообщений " else "Всего сообщений " + chat.getMessageCount().toString() + " новых " + chat.getNewMessageCount().toString())
        }
        return chatsInfo
    }

    fun deleteMessage(chatId: Int, messageId: Int): Boolean {
        val chat = chats.find { chat: Chat -> chat.id == chatId }
        if (chat != null) {
            val deleted = chat.delMessage(messageId)
            if ((deleted) && (chat.getMessageCount() == 0)) { chats.remove(chat) }
            return deleted
        }
        return false
    }

}
