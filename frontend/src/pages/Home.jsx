import { useEffect, useRef, useState } from "react"
import SockJsClient from "react-stomp"

const SOCKET_URL = "http://localhost:8080/ws"

const Home = () => {
    const [name, setName] = useState("")
    const [email, setEmail] = useState("")
    const [room, setRoom] = useState()
    const [notifications, setNotifications] = useState([])
    const [messageContent, setMessageContent] = useState("")
    const [messages, setMessages] = useState([])

    const clientRef = useRef(null)

    const handleConnect = async () => {
        const res = await fetch("http://localhost:8080/room", {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'

            },
            body: JSON.stringify({
                name,
                email
            })
        })
        if (res.ok) {
            setRoom(await res.json())
        }
    }

    const handleMessageReceived = (message) => {
        switch (message.type) {
            case "CHATROOM_ONLINE":
                console.log("chatroom online")
                break;
            case "MESSAGE_NEW":
                handleGetNewMessages()
                break;

            default:
                break;
        }
        console.log(message)
        // setNotifications([...notifications, ])
    }

    const handleSendMessage = async () => {
        clientRef.current.sendMessage('/app/send/' + room.id, JSON.stringify({ content: messageContent }));
        setMessageContent("")
    }

    const handleGetNewMessages = async () => {
        const res = await fetch("http://localhost:8080/messages/new/" + room.id)
        const newMessages = await res.json()

        setMessages([...messages, ...newMessages])
    }

    useEffect(() => console.log(messages),[messages])



    return (<div className="home">
        {room ? (<>
            <SockJsClient
                url={SOCKET_URL}
                topics={["/chatroom/" + room.id]}
                onMessage={(msg) => handleMessageReceived(msg)}
                debug={true}
                ref={clientRef}
                options={{
                    sessionId: () => {
                        const sessionId = Math.random().toString(36).slice(-10);
                        return "client-" + sessionId
                    }
                }}
            />
            <input type="text" value={messageContent} onChange={e => setMessageContent(e.target.value)} />
            <button onClick={handleSendMessage} >Send</button>
            <div className="home__messages">
                {messages.map(message => <p>{message.content}</p>)}
            </div>

        </>
        ) :
            <div className="home__form">
                <label className="home__form__name">
                    Name:
                    <input type="text" value={name} onChange={e => setName(e.target.value)} />
                </label>
                <label className="home__form__name">
                    Email:
                    <input type="text" value={email} onChange={e => setEmail(e.target.value)} />
                </label>
                <button onClick={handleConnect}>Connect</button>
            </div>
        }


    </div>);
}

export default Home;