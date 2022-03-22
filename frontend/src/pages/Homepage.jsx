import { useEffect, useRef, useState } from "react"

import SockJS from "sockjs-client"
import Stomp from "stompjs"
import SockJsClient from "react-stomp"

const SOCKET_URL = "http://localhost:8080/ws"

const Homepage = () => {
  const [room, setRoom] = useState()
  const [message, setMessage] = useState([])
  const [roomsList, setRoomsList] = useState([])

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

  let onConnected = (e) => {
    console.log(e)
    console.log("Connected!!")
  }

  let onMessageReceived = (msg) => {
    console.log(msg)
    setMessage([...message, msg])
  }

  const getChatRoom = async () => {
    const res = await fetch("http://localhost:8080/room", {
      method: "POST",
      headers: {
        'Content-Type': 'application/json'
        // 'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: JSON.stringify({
        name: "e",
        email: "fg"
      })
    })
    console.log(res)

    setRoom(await res.json())
  }

  useEffect(() => {
    getChatRoom()
  }, [])

  useEffect(() => {console.log(roomsList)}, [roomsList])

  const getRooms= async (status) => {
    const res = await fetch("http://localhost:8080/room/OFFLINE")
    setRoomsList(await res.json())
  }

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
            options={{
              sessionId: () => {
                
                const sessionId = Math.random().toString(36).slice(-10);
                return "client-"+sessionId
             }
            }}
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
        <button onClick={e => {getRooms()}}>Get all</button>
        <button onClick={e => {}}>Get all online</button>
        <button onClick={e => {}}>Get all offline</button>

      </div>
    </div>
  )
}

export default Homepage
