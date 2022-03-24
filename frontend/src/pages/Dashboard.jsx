import { useEffect, useRef, useState } from "react"
import SockJsClient from "react-stomp"

const SOCKET_URL = "http://localhost:8080/ws"

const Dashboard = () => {
  const [chatrooms, setChatrooms] = useState([])
  const [room, setRoom] = useState()
  const [messages, setMessages] = useState([])

  const notificationsRef = useRef(null)
  const roomRef = useRef(null)

  useEffect(() => {
    getOnlineChatrooms()
  }, [])

  const getOnlineChatrooms = async () => {
    const res = await fetch("http://localhost:8080/room/status/online")
    setChatrooms(await res.json())
  }

  const getRoomById = async (id) => {
    const res = await fetch("http://localhost:8080/room/" + id)
    setRoom(await res.json())
  }

  const handleGetNewMessages = async () => {
    const res = await fetch(
      "http://localhost:8080/messages/new/support/" + room.id
    )
    const newMessages = await res.json()

    console.log(newMessages)

    if (messages.length === 0) return

    setMessages([...messages, ...newMessages])
  }

  const handleMessageReceived = (msg) => {
    switch (msg.type) {
      case "CHATROOM_ONLINE":
        getOnlineChatrooms()
        break
      case "CHATROOM_OFFLINE":
        getOnlineChatrooms()
        break

      case "MESSAGE_NEW":
        handleGetNewMessages()
        break
      case "MESSAGE_SEEN":
        break

      default:
        break
    }
  }

  const getMessages = async () => {
    console.log("get all messages")
    if (!room) return
    const res = await fetch("http://localhost:8080/messages/" + room.id)
    const resMessages = await res.json()
    setMessages([...resMessages])
  }

  useEffect(() => {
    getMessages()
  }, [room])

  useEffect(() => {console.log(messages)}, [messages])

  return (
    <div className="dashboard">
      <div className="dashboard__chatroomsMenu">
        <SockJsClient
          url={SOCKET_URL}
          topics={["/chatroom/notifications"]}
          onMessage={(msg) => handleMessageReceived(msg)}
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
              {chatroom.clientUser.name}
             
            </div>
          )
        })}
      </div>
      <div className="dashboard__chatroom">
        {room && (
          <>
            {messages.map((message) => (
              <p>{message.content}</p>
            ))}
          </>
        )}
      </div>
    </div>
  )
}

export default Dashboard
