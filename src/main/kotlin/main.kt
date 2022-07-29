
fun main() {
    val users: ArrayList<Pair <Int, String>> = arrayListOf()
    val chatService = ChatService

    fun printChats() {
        println("Список чатов:\n"+ chatService.getChats())
    }

    fun getUserName(userId: Int): String {
        for (user in users) {
            if (user.first == userId) return user.second
        }
        return userId.toString()
    }

    fun prinAllMessaages(chatId:Int) {
        val chat = chatService.chats.find { chat: Chat -> chat.id == chatId }
        if (chat != null) {
            val messageList = chat.getMessages();
            println("Чат " + chat.id + " между " + getUserName(chat.user1Id) + " и " + getUserName(chat.user2Id))
            if (!messageList.isEmpty()) {
                for (message in messageList) {
                    println(" "+ getUserName(message.userId) + " -> " + message.text)
                    chat.markMessageRead(message)
                }
            } else {
                println(" Нет сообщений.")
            }
        }
    }

    fun prinNewMessaages(chatId:Int) {
        val chat = chatService.chats.find { chat: Chat -> chat.id == chatId }
        if (chat != null) {
            val messageList = chat.getMessages(Pair(1,0));
            println("Чат " + chat.id + " между " + getUserName(chat.user1Id) + " и " + getUserName(chat.user2Id) + " (только новые сообщения)")
            if (!messageList.isEmpty()) {
                for (message in messageList) {
                    println(" "+ getUserName(message.userId) + " -> " + message.text)
                    chat.markMessageRead(message)
                }
            } else {
                println(" Нет сообщений.")
            }
        }
    }

    users += Pair(1, "Маша")
    users += Pair(2, "Вася")
    users += Pair(3, "Петя")

    chatService.newMessage(users[0].first,users[1].first,"Привет!")
    chatService.newMessage(users[1].first,users[0].first,"Привет!")
    val chat1Id = chatService.findChat(users[0].first,users[1].first)?.id ?: 0

    val chat2Id = chatService.newChat(users[1].first,users[2].first).id
    val chat3 = chatService.findChat(users[2].first,users[0].first) ?: chatService.newChat(users[2].first,users[0].first)
    chat3.addMessage(users[2].first , "Добрый день!")
    chat3.addMessage(users[0].first, "И вам не хворать!")
    chat3.addMessage(users[2].first, "Чего делаешь?")
    chat3.addMessage(users[0].first, "Да так")
    val messageId = chatService.newMessage(users[0].first,users[2].first,"Бездельничаю")?.id ?:0
    chat3.editMessage(messageId, "Тружусь в поте лица")

    println("Всего чатов c сообщениями : " + chatService.getChatsCount())

    println("Чатов с непрочитанными сообщениями " + chatService.getUnreadChatsCount())

    if (chatService.deleteMessage(chat1Id, 2)) {
        println("Удалено сообщение из чата " + chat1Id)
    }

    if (chatService.deleteChat(chat1Id)) {
        println("Чат $chat1Id удален")
    }

    printChats()

    prinAllMessaages(chat3.id)

    chat3.addMessage(users[0].first, "Может в кино?")

    prinNewMessaages(chat3.id)
}