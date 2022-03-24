import { useEffect, useRef, useState } from "react"
import SockJsClient from "react-stomp"
import {
  getAllChatrooms,
  getAllMessagesInChatroom,
  getChatrromById,
  getNewChatroom,
  getNewMessagesSupport,
  sendMessageSupport,
} from "../adapters/xhr"
import Chat from "../components/chat/Chat"

const SOCKET_URL = "http://localhost:8080/ws"

const Dashboard = () => {
  const [chatrooms, setChatrooms] = useState([])
  const [room, setRoom] = useState()
  const [messages, setMessages] = useState([])

  const notificationsRef = useRef(null)
  const roomRef = useRef(null)

  useEffect(() => {
    getChatrooms()
  }, [])

  const getChatrooms = async () => {
    const res = await getAllChatrooms()
    if (!res.error) setChatrooms(res.data)
  }

  const getRoomById = async (chatroomId) => {
    const res = await getChatrromById(chatroomId)
    if (!res.error) setRoom(res.data)
  }

  const handleGetNewMessages = async () => {
    if (!room) return
    const res = await getNewMessagesSupport(room.id)
    if (!res.error) {
      setMessages([...messages, ...res.data])
    }
  }

  const handleChatroomOffline = (chatroom) => {
    const chatroomsTmp = [...chatrooms]
    chatroomsTmp.find((chtrm) => chtrm.id === chatroom.id).status = "OFFLINE"
    setChatrooms([...chatroomsTmp])
  }

  const handleNewChatroom = async (chatroom) => {
    setChatrooms([...chatrooms, chatroom])
  }

  const handleSeenMessages = async (msgs) => {
    const messagesTmp = [...messages]

    msgs.forEach((msg) => {
      messagesTmp.find((message) => message.id === msg.id).seen = msg.seen
    })

    setMessages([...messagesTmp])
  }

  const handleNotificationReceived = (notification) => {
    switch (notification.type) {
      case "CHATROOM_ONLINE":
        handleNewChatroom(notification.chatroom)
        break
      case "CHATROOM_OFFLINE":
        handleChatroomOffline(notification.chatroom)
        break

      case "MESSAGE_NEW":
        handleGetNewMessages()
        break
      case "MESSAGE_SEEN":
        handleSeenMessages(notification.messages)
        break

      default:
        break
    }
  }

  const getMessages = async () => {
    if (!room) return
    const res = await getAllMessagesInChatroom(room.id)
    if (!res.error) {
      setMessages([...res.data])
    }
  }

  useEffect(() => {
    getMessages()
  }, [room])

  useEffect(() => {
    console.log(chatrooms)
  }, [chatrooms])

  const handleOnSend = async (messageContent) => {
    const res = await sendMessageSupport(room.id, messageContent)

    if (!res.error) {
      const message = res.data
      if (message) setMessages([...messages, message])
    }
  }

  return (
    <div className="dashboard">
      <div className="dashboard__chatroomsMenu">
        <SockJsClient
          url={SOCKET_URL}
          topics={["/chatroom/notifications"]}
          onMessage={(notification) => handleNotificationReceived(notification)}
          debug={true}
          ref={notificationsRef}
        />
        {chatrooms.map((chatroom) => {
          return (
            <div
              onClick={() => {
                getRoomById(chatroom.id)
              }}
            >
              {chatroom.id}
              {chatroom.status}
            </div>
          )
        })}
      </div>
      <div className="dashboard__chatroom">
        {room && (
          <>
            <Chat messages={messages} onSend={handleOnSend} />
          </>
        )}
      </div>
    </div>
  )
}

export default Dashboard
