import { useEffect, useRef, useState } from "react"

import SockJS from "sockjs-client"
import Stomp from "stompjs"
import SockJsClient from "react-stomp"

const SOCKET_URL = "http://localhost:8080/ws"

const Homepage = () => {
  const [room, setRoom] = useState()
  const [message, setMessage] = useState([])

  // var sock = new SockJS("http://localhost:8080/ws")
  // let stompClient = Stomp.over(sock)
  // sock.onopen = function () {
  //   console.log("open")
  // }
  // stompClient.connect({}, function (frame) {
  //   console.log("Connected: " + frame)
  //   stompClient.subscribe("/topic/public", function (greeting) {
  //     console.log(greeting)
  //     //you can execute any function here
  //   })
  // })

  const clientRef = useRef(null)

  let onConnected = () => {
    console.log("Connected!!")
  }

  let onMessageReceived = (msg) => {
    console.log(msg)
    setMessage([...message, msg])
  }

  const getChatRoom = async () => {
    const res = await fetch("http://localhost:8080/room")

    setRoom(await res.json())
  }

  useEffect(() => {
    getChatRoom()
  }, [])

  return (
    <div className="homepage">
      <div>
        {room && (
          <SockJsClient
            url={SOCKET_URL}
            topics={["/chatroom/" + room.id]}
            onConnect={onConnected}
            onDisconnect={console.log("Disconnected!")}
            onMessage={(msg) => onMessageReceived(msg)}
            debug={true}
            ref={clientRef}
          />
        )}
        <div
          onClick={() => {
            console.log("sending")
            clientRef.current.sendMessage(
              "/app/send/" + room.id,
              JSON.stringify({
                content: "hello",
              })
            )
          }}
        >
          send
        </div>
        <div>{message}</div>
      </div>
    </div>
  )
}

export default Homepage
