data class Message (
    val id: Int = 0,
    val chatId: Int = 0,
    val userId: Int = 0,
    val isNew: Boolean = true,
    val date: Int = 0,
    val text: String = ""
)

class Chat(val user1Id:Int, val user2Id:Int, val id:Int) {
    var myUserId = user1Id  // от чьего имени обращаемся к чату
        set(value) {
            if ((value == user1Id) || (value == user2Id)) { field = value }
        }
    private val messages: MutableList<Message> = mutableListOf()

    val isNew = fun(message: Message) = ((message.isNew) && (myUserId != message.userId))
    val isRead = fun(message: Message) = ((!message.isNew) || (myUserId == message.userId))
    val isIncoming = fun(message: Message) = message.userId != myUserId
    val isOutgoing = fun(message: Message) = message.userId == myUserId

    //fun Iterable<Message>.filter(predicate: (Message) -> Boolean): List<Message> =
    //    filterTo(messages, predicate)

    fun getMessageCount(): Int {
        return messages.size
    }

    fun getNewMessageCount(): Int {
        return messages.filter(isNew).size
    }

    fun getReadMessageCount(): Int {
        return getMessageCount() - getNewMessageCount()
    }

    fun getMessages(messageType: Pair <Int,Int> = Pair(0 ,0)): List<Message> {
        // messageType - тип возвращаемых сообщений
        // первый параметр
            // 1 - только новые
            // 2 - только прочитанные
            // else - все сообщения
        // второй параметр
            // 1 - входящие
            // 2 - исходящие
            // else - все сообщения
        val list: List<Message> =
            when (messageType.first) {
                1 -> messages.filter(isNew)
                2 -> messages.filter(isRead)
                else -> messages
            }
        when (messageType.second) {
            1 -> return list.filter(isIncoming)
            2 -> return list.filter(isOutgoing)
            else -> return list
        }
    }

    fun addMessage(userId: Int, text: String):Message? {
        if (((userId == user1Id) || (userId == user2Id)) &&
            (messages.add(Message(
            if (messages.isEmpty()) 1 else messages.last().id + 1,
            this.id,
            userId,
            text = text)))) {
           return messages.last()
        } else {
            return null
        }
    }

    fun editMessage(message: Message, text: String):Message? {
        val index = messages.indexOf(message)
        if (index >=0) {
            messages[index] = message.copy(text = text, isNew = true)
            return messages[index]
        } else {
            return null
        }
    }

    fun editMessage(messageId: Int, text: String):Message? {
        val message = messages.find { message: Message -> message.id == messageId }
        if (message !=null) {
            return editMessage(message, text)
        } else {
            return null
        }
    }

    fun clear() {
        messages.clear();
    }

    fun delMessage(messageId:Int):Boolean {
        val message = messages.find { message: Message -> message.id == messageId }
        if (message !=null) {
            return messages.remove(message)
        }
        return false
    }

    fun markMessageRead(message: Message) {
        val index = messages.indexOf(message)
        if (index >=0) {
            messages[index] = message.copy(isNew = false)
        }
    }

    fun markMessageRead(messageId: Int) {
        val message = messages.find { message: Message -> message.id == messageId }
        if (message !=null) { markMessageRead(message) }
    }
}