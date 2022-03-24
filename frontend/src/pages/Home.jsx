import { useEffect, useRef, useState } from "react"
import SockJsClient from "react-stomp"
import Chat from "../components/chat/Chat"
import UserForm from "../components/homepage/UserForm"

const SOCKET_URL = "http://localhost:8080/ws"

const Home = () => {
  const [name, setName] = useState("")
  const [email, setEmail] = useState("")
  const [room, setRoom] = useState()
  const [messages, setMessages] = useState([])

  const clientRef = useRef(null)

  const connect = async () => {
    const res = await fetch("http://localhost:8080/room", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        name,
        email,
      }),
    })
    if (res.ok) {
      setRoom(await res.json())
    }
  }

  const handleMessageReceived = (message) => {
    switch (message.type) {
      case "CHATROOM_ONLINE":
        console.log("chatroom online")
        break
      case "MESSAGE_NEW":
        handleGetNewMessages()
        break

      default:
        break
    }
    console.log(message)
    // setNotifications([...notifications, ])
  }

  const handleSendMessage = async (messageContent) => {
    const res = await fetch(
      "http://localhost:8080/messages/" + room.id + "?sender=client",
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          content: messageContent,
        }),
      }
    )
    if (res.ok) {
      const message = await res.json()
      if (message) setMessages([...messages, message])
    }
  }

  const handleGetNewMessages = async (e) => {

    const res = await fetch(
      "http://localhost:8080/messages/new/client/" + room.id
    )
    if (res.ok) {
      const newMessages = await res.json()
      console.log(newMessages)
      if (newMessages.length === 0) return
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
            onMessage={(msg) => handleMessageReceived(msg)}
            debug={true}
            ref={clientRef}
            options={{
              sessionId: () => {
                const sessionId = Math.random().toString(36).slice(-10)
                return "client-" + sessionId
              },
            }}
          />
         <Chat messages={messages} onSend={handleSendMessage}/>
        </>
      ) : (
        <UserForm onSubmit={handleUserFormSubmit} />
      )}
    </div>
  )
}

export default Home
