import { useEffect, useRef, useState } from "react"
import SockJsClient from "react-stomp"
import {
  countNewMessagesByChatroom,
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
    if (!res.error) {
      const chatroomsWithCount = await Promise.all(
        res.data.map(async (room) => {
          const countData = await countNewMessagesByChatroom(room.id)
          return { ...room, countNewMessages: countData.data }
        })
      )
      setChatrooms(chatroomsWithCount)
    }
  }

  const updateChatroomNewMessagesCount = async (chatroom) => {
    const chatroomsTmp = [...chatrooms]
    const countData = await countNewMessagesByChatroom(chatroom.id)
    const count = countData.data
    chatroomsTmp.find((chtrm) => chtrm.id === chatroom.id).countNewMessages =
      count
    setChatrooms([...chatroomsTmp])
  }

  const getRoomById = async (chatroomId) => {
    const res = await getChatrromById(chatroomId)
    if (!res.error) setRoom(res.data)
  }

  const handleGetNewMessages = async (chatroom) => {
    updateChatroomNewMessagesCount(chatroom)
    if (!room || room.id !== chatroom.id) return
    const res = await getNewMessagesSupport(room.id)
    if (!res.error) {
      setMessages([...messages, ...res.data])
    }
    getChatrooms()
  }

  const handleChatroomOffline = (chatroom) => {
    const chatroomsTmp = [...chatrooms]
    chatroomsTmp.find((chtrm) => chtrm.id === chatroom.id).status = "OFFLINE"
    setChatrooms([...chatroomsTmp])
  }

  const handleNewChatroom = async (chatroom) => {
    setChatrooms([...chatrooms, chatroom])
  }

  const handleSeenMessages = async (msgs, chatroom) => {
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
        handleGetNewMessages(notification.chatroom)
        break
      case "MESSAGE_SEEN":
        handleSeenMessages(notification.messages, notification.chatroom)
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
    getChatrooms()
  }

  useEffect(() => {
    getMessages()
  }, [room])

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
              key={chatroom.id}
              onClick={() => {
                getRoomById(chatroom.id)
              }}
            >
              {chatroom.id}
              {chatroom.status}
              {chatroom.countNewMessages}
            </div>
          )
        })}
      </div>
      <div className="dashboard__chatroom">
        {room && (
          <>
            <Chat messages={messages} onSend={handleOnSend} user="support" />
          </>
        )}
      </div>
    </div>
  )
}

export default Dashboard
