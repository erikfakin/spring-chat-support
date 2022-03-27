import { useRef, useState } from "react"
import SockJsClient from "react-stomp"
import { getNewMessagesClient, sendMessageClient } from "../../adapters/xhr"
import Chat from "./Chat"

const ClientChat = ({ room }) => {
  const [messages, setMessages] = useState([])
  const clientChatRef = useRef(null)

  const handleOnNotification = (notification) => {
    switch (notification.type) {
      case "MESSAGE_NEW":
        handleOnMessageNew()
        break
      case "MESSAGE_SEEN":
        handleOnMessageSeen(notification.messages)
        break
      default:
        break
    }
  }

  const handleOnMessageNew = async (e) => {
    const res = await getNewMessagesClient(room.id)
    if (!res.error) {
      const newMessages = res.data
      setMessages([...messages, ...newMessages])
    }
  }

  const handleOnMessageSeen = async (msgs) => {
    const messagesTmp = [...messages]
    msgs.forEach((msg) => {
      messagesTmp.find((message) => message.id === msg.id).seen = msg.seen
    })
    setMessages([...messagesTmp])
  }

  const handleOnSend = async (messageContent) => {
    const res = await sendMessageClient(room.id, messageContent)
    if (!res.error) {
      const message = res.data
      if (message) setMessages([...messages, message])
    }
  }

  return (
    <>
      <SockJsClient
        url={process.env.REACT_APP_WS_URL}
        topics={["/chatroom/" + room.id]}
        onMessage={(notification) => handleOnNotification(notification)}
        debug={true}
        ref={clientChatRef}
        options={{
          sessionId: () => {
            const sessionId = Math.random().toString(36).slice(-10)
            return "client-" + sessionId
          },
        }}
      />
      <Chat messages={messages} onSend={handleOnSend} user="client" />
    </>
  )
}

export default ClientChat
