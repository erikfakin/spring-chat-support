import { useEffect, useRef, useState } from "react"
import SockJsClient from "react-stomp"
import {
  getNewChatroom,
  getNewMessagesClient,
  sendMessageClient,
} from "../adapters/xhr"
import Chat from "../components/chat/Chat"
import "./Home.scss"

const SOCKET_URL = "http://localhost:8080/ws"

const Home = () => {
  const [name, setName] = useState("")
  const [email, setEmail] = useState("")
  const [room, setRoom] = useState()
  const [messages, setMessages] = useState([])

  const clientRef = useRef(null)

  const connect = async () => {
    console.log(name, email)
    const res = await getNewChatroom(name, email)

    if (!res.error) {
      setRoom(res.data)
    }
  }

  const handleOnNotification = (notification) => {
    switch (notification.type) {
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

  const handleSeenMessages = async (msgs) => {
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

  const handleGetNewMessages = async (e) => {
    const res = await getNewMessagesClient(room.id)

    if (!res.error) {
      const newMessages = res.data
      setMessages([...messages, ...newMessages])
    }
  }

  const handleUserFormSubmit = (formName, formEmail) => {
    setName(formName)
    setEmail(formEmail)
    connect()
  }

  return (
    <div className="home">
      {room ? (
        <>
          <SockJsClient
            url={SOCKET_URL}
            topics={["/chatroom/" + room.id]}
            onMessage={(notification) => handleOnNotification(notification)}
            debug={true}
            ref={clientRef}
            options={{
              sessionId: () => {
                const sessionId = Math.random().toString(36).slice(-10)
                return "client-" + sessionId
              },
            }}
          />
          <Chat messages={messages} onSend={handleOnSend} />
        </>
      ) : (
        <div className="userForm">
          <label className="userForm__name">
            Name:
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
            />
          </label>
          <label className="userForm__email">
            Email:
            <input
              type="text"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
          </label>
          <button onClick={handleUserFormSubmit}>Connect</button>
        </div>
      )}
    </div>
  )
}

export default Home
