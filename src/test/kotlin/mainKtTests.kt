import org.junit.Test

import org.junit.Assert.*

class mainKtTests {
    @Test
    fun test_newChat() {
        val chatService = ChatService
        assertTrue(chatService.newChat(10,20) != null)
    }

    @Test
    fun test_deleteChat() {
        val chatService = ChatService
        val chat = chatService.newChat(3, 4)
        assertTrue(chatService.deleteChat(chat.id))
    }

    @Test(expected = RuntimeException::class)
    fun test_newChatException() {
        val chatService = ChatService
        chatService.newMessage(1,1,"Привет!")
    }

    @Test
    fun test_newMessage() {
        val chatService = ChatService
        assertTrue(chatService.newMessage(1,2,"Привет!") != null)
    }

    @Test
    fun test_deleteMessage() {
        val chatService = ChatService
        val message = chatService.newMessage(1,2,"Привет!")
        if (message != null) {
            assertTrue(chatService.deleteMessage(message.chatId,message.id))
        } else {
            assert(false)
        }
    }
}
