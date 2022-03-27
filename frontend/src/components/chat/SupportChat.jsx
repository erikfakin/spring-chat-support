import { useEffect, useRef, useState } from "react"
import SockJsClient from "react-stomp"
import {
  countNewMessagesByChatroom,
  getAllChatrooms,
  getAllMessagesInChatroom,
  getChatrromById,
  getNewMessagesSupport,
  sendMessageSupport,
} from "../../adapters/xhr"

import Chat from "./Chat"
import ChatroomList from "./ChatroomList"

import "./SupportChat.scss"

const SupportChat = () => {
  const [chatrooms, setChatrooms] = useState([])
  const [room, setRoom] = useState()
  const [messages, setMessages] = useState([])

  const notificationsRef = useRef(null)

  useEffect(() => {
    getChatrooms()
  }, [])

  useEffect(() => {
    getMessages()
  }, [room])

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

  const handleOnChatroomClick = async (chatroomId) => {
    const res = await getChatrromById(chatroomId)
    if (!res.error) setRoom(res.data)
  }

  const handleOnNotification = (notification) => {
    switch (notification.type) {
      case "CHATROOM_ONLINE":
        handleChatroomNew(notification.chatroom)
        break
      case "CHATROOM_OFFLINE":
        handleChatroomOffline(notification.chatroom)
        break

      case "MESSAGE_NEW":
        handleOnMessageNew(notification.chatroom)
        break
      case "MESSAGE_SEEN":
        handleOnMessageSeen(notification.messages, notification.chatroom)
        break

      default:
        break
    }
  }

  const handleChatroomNew = async (chatroom) => {
    setChatrooms([chatroom, ...chatrooms])
  }

  const handleChatroomOffline = (chatroom) => {
    const chatroomsTmp = [...chatrooms]
    chatroomsTmp.find((chtrm) => chtrm.id === chatroom.id).status = "OFFLINE"
    setChatrooms([...chatroomsTmp])
  }

  const handleOnMessageNew = async (chatroom) => {
    if (!room || room.id !== chatroom.id) {
      updateChatroomNewMessagesCount(chatroom)
      return
    }
    const res = await getNewMessagesSupport(room.id)
    if (!res.error) {
      setMessages([...messages, ...res.data])
    }
  }

  const handleOnMessageSeen = async (msgs, chatroom) => {
    const messagesTmp = [...messages]
    msgs.forEach((msg) => {
      messagesTmp.find((message) => message.id === msg.id).seen = msg.seen
    })
    setMessages([...messagesTmp])
  }

  const updateChatroomNewMessagesCount = async (chatroom) => {
    const chatroomsTmp = [...chatrooms]
    const countData = await countNewMessagesByChatroom(chatroom.id)
    chatroomsTmp.find((chtrm) => chtrm.id === chatroom.id).countNewMessages =
      countData.data
    setChatrooms([...chatroomsTmp])
  }

  const getMessages = async () => {
    if (!room) return
    const res = await getAllMessagesInChatroom(room.id)
    if (!res.error) {
      setMessages([...res.data])
    }
    updateChatroomNewMessagesCount(room)
  }

  const handleOnSend = async (messageContent) => {
    const res = await sendMessageSupport(room.id, messageContent)
    if (!res.error) {
      setMessages([...messages, res.data])
    }
  }

  return (
    <div className="supportChat">
      <div className={`supportChat__chatrooms ${room ? "" : "active"}`}>
        <SockJsClient
          url={process.env.REACT_APP_WS_URL}
          topics={["/chatroom/notifications"]}
          onMessage={(notification) => handleOnNotification(notification)}
          debug={true}
          ref={notificationsRef}
        />
        <div className="supportChat__chatrooms__back" onClick={() => setRoom()}>
          {`< Chatrooms`}
        </div>
        {chatrooms.map((chatroom) => (
          <ChatroomList
            activeChatroom={room}
            chatroom={chatroom}
            onClick={handleOnChatroomClick}
          />
        ))}
      </div>
      <div className={`supportChat__chatroom ${room ? "active" : ""}`}>
        {room && (
          <>
            <Chat
              messages={messages}
              onSend={handleOnSend}
              user="support"
              room={room}
            />
          </>
        )}
      </div>
    </div>
  )
}

export default SupportChat
